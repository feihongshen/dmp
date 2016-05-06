package cn.explink.service.addressmatch;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.GeneralDAO;
import cn.explink.dao.express.PreOrderDao;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.domain.MqExceptionBuilder.MessageSourceEnum;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.OrderAddressMappingResult;
import cn.explink.domain.addressvo.OrderVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.express.AddressMatchEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.service.SystemConfigChangeListner;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.ResourceBundleUtil;

@Component
@DependsOn({ "systemInstallService" })
public class AddressMatchExpressService implements SystemConfigChangeListner, ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	DataImportService dataImportService;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private AddressMatchManager addressMatchManager;

	@Autowired
	private CamelContext camelContext;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CwbOrderService cwbOrderService;

	@Autowired
	SystemInstallService systemInstallService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	AddressMappingService addressMappingService;

	@Autowired
	PreOrderDao preOrderDao;

	@Autowired
	private GeneralDAO generalDAO;

	private String address_url;
	private String address_userid;

	ObjectMapper objectMapper = JacksonMapper.getInstance();
	ObjectReader expressAddressInfoReader = this.objectMapper.reader(ExtralInfo4Address.class);
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private static final String MQ_FROM_URI_AUTO_ADDRESS_INFO2 = "jms:queue:VirtualTopicConsumers.express2.autoAddressInfo2";

	public void init() {
		this.logger.info("init addressmatch camel routes");
		try {
			this.address_url = this.systemInstallService.getParameter("addressmatch.url");
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");

			String addressmatchenabled = this.systemInstallService.getParameter("addressmatch.enabled");
			if (addressmatchenabled.equals("1")) { // 地址库启用
				this.logger.info("enable addressmatch camel routes");
				this.camelContext.addRoutes(new RouteBuilder() {
					@Override
					public void configure() throws Exception {
						this.from(MQ_FROM_URI_AUTO_ADDRESS_INFO2 + "?concurrentConsumers=10").to("bean:addressMatchExpressService?method=matchAddress")
								.routeId("express-addressMatch2");
					}
				});
			}
		} catch (Exception e) {
			this.logger.error("camel context start fail", e);
		}

	}

	/**
	 * 生成applicationVo
	 */
	private ApplicationVo getApplicationVo() {
		ApplicationVo applicationVo = new ApplicationVo();
		applicationVo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		applicationVo.setId(Long.parseLong(ResourceBundleUtil.addressid));
		applicationVo.setPassword(ResourceBundleUtil.addresspassword);
		return applicationVo;
	}

	/**
	 * 生成applicationVo
	 */
	private OrderVo getOrderVo(CwbOrder order, String address) {
		OrderVo ordervo = new OrderVo();
		ordervo.setOrderId(order.getCwb());
		ordervo.setAddressLine(address);
		ordervo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		return ordervo;
	}

	/**
	 *
	 * @param userid
	 * @param cwb
	 * @param address
	 *            地址
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void matchAddress(@Header("autoMatchAddressInfo") String addressExtralInfo, @Header("MessageHeaderUUID") String messageHeaderUUID) throws JsonProcessingException, IOException {
		logger.info("走到了匹配方法");
		ExtralInfo4Address info = this.expressAddressInfoReader.readValue(addressExtralInfo);
		this.logger.info("start address match for {}", /* map.get("cwb") */info.getCwb());
		try {

			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setCwb(info.getCwb());
			User user = this.userDAO.getUserByUserid(info.getUserId());
			String addressenabled = this.systemInstallService.getParameter("newaddressenabled");

			Branch branch = this.getAddressMathcBranch(info.getCwb(), info.getAddress(), 0, cwbOrder, user, addressenabled);

			if (info.getAddressMatcher() == AddressMatchEnum.PreOrderMatch.getValue()) {
				if (branch == null) {
					this.logger.info("预订单自动匹配站点失败！");
					return;
				}
				// TODO 志宇的自动匹配站点处理
				// 拿到站点id和站点名，更新数据库
				Date date = new Date(System.currentTimeMillis());
				this.preOrderDao.updatePreOrderAuto(info.getCwb(), (int) branch.getBranchid(), branch.getBranchname(), user.getUserid(), user.getUsername(), date);

				this.logger.info("预订单自动匹配站点成功！");
			} else if (info.getAddressMatcher() == AddressMatchEnum.OrderEmbraceMatch.getValue()) {
				if (branch == null) {
					this.logger.info("订单补录匹配站点失败！");
					return;
				}
				// 地址匹配成功后，将得到的站点id存到订单表的deliverybranchid中
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("deliverybranchid", branch.getBranchid());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", info.getCwb());
				this.generalDAO.update(params, "express_ops_cwb_detail", map);
				this.logger.info("订单补录匹配站点成功！");
			} else if (info.getAddressMatcher() == AddressMatchEnum.ExpressOutStation.getValue()) {
				if (branch == null) {
					this.logger.info("揽件出站匹配站点失败！");
					return;
				}
				// 地址匹配成功后，将得到的站点id存到订单表的deliverybranchid中
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("deliverybranchid", branch.getBranchid());
				params.put("excelbranch", branch.getBranchname());
				params.put("addresscodeedittype", CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", info.getCwb());
				this.generalDAO.update(params, "express_ops_cwb_detail", map);
				this.logger.info("揽件出站匹配站点成功！");
			}
		} catch (Exception e) {
			this.logger.error("error while doing address match for " + info.getCwb(), e);
			
			// 把未完成MQ插入到数据库中, start
			//消费MQ异常表
			this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".matchAddress")
					.buildExceptionInfo(e.toString()).buildTopic(MQ_FROM_URI_AUTO_ADDRESS_INFO2)
					.buildMessageHeader("autoMatchAddressInfo", addressExtralInfo)
					.buildMessageHeaderUUID(messageHeaderUUID).buildMessageSource(MessageSourceEnum.receiver.getIndex()).getMqException());
			// 把未完成MQ插入到数据库中, end
		}
	}

	/**
	 *
	 * @param userid
	 * @param cwb
	 * @param address
	 *            地址
	 */
	private Branch getAddressMathcBranch(String cwb, String address, long notifytype, CwbOrder cwbOrder, User user, String addressenabled) throws IOException, Exception {
		if ((addressenabled != null) && addressenabled.equals("1")) {
			// TODO 启用新地址库 调用webservice
			List<OrderVo> orderVoList = new ArrayList<OrderVo>();
			try {
				List<DeliveryStationVo> deliveryStationList = new ArrayList<DeliveryStationVo>();
				Map<String, Object> map = this.addressMatchStation(cwb, address, cwbOrder, user, orderVoList, deliveryStationList);
				deliveryStationList = (List<DeliveryStationVo>) map.get("stationList");
				Set<Long> set = (Set<Long>) map.get("set");
				if (set.size() == 1) {

					Branch b = this.branchDAO.getEffectBranchById(deliveryStationList.get(0).getExternalId());
					if (null != b && (b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						return b;
					}
				}

			} catch (Exception e) {
				this.logger.error("error while doing address match for " + cwb, e);
			}
			return null;

		} else {
			// 老地址库
			String params = "userid=" + this.address_userid + "&address=" + cwbOrder.getCwb() + "@" + address.replaceAll(",", "");
			JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, params, "POST"));
			Branch b = null;
			for (int i = 0; i < addressList.size(); i++) {
				try {
					if (org.apache.commons.lang3.StringUtils.isBlank(addressList.getJSONObject(i).getString("station"))) {
						return null;
					}

					b = this.branchDAO.getEffectBranchById(addressList.getJSONObject(i).getLong("station"));
					if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						return b;
					}
				} catch (CwbException ce) {
					this.logger.error("update branche for cwb send jms {},error:{}", cwb, ce.getMessage());
				}
			}
			return null;
		}
	}

	private Map<String, Object> addressMatchStation(String cwb, String address, CwbOrder cwbOrder, User user, List<OrderVo> orderVoList, List<DeliveryStationVo> deliveryStationList) throws Exception {
		Set<Long> set = new HashSet<Long>();
		orderVoList.add(this.getOrderVo(cwbOrder, address));
		AddressMappingResult addressreturn = this.addressMappingService.mappingAddress(this.getApplicationVo(), orderVoList);
		int successFlag = addressreturn.getResultCode().getCode();
		if (successFlag == 0) {
			OrderAddressMappingResult mappingresult = addressreturn.getResultMap().get(cwb);
			if (mappingresult != null) {
				deliveryStationList = mappingresult.getDeliveryStationList();
				if (null == deliveryStationList || deliveryStationList.size() == 0) {
					this.logger.error("预订单[" + cwb + "]自动匹配站点功能，未匹配到站点！");
					return null;
				}
				for (DeliveryStationVo desvo : deliveryStationList) {
					set.add(desvo.getExternalId());
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stationList", deliveryStationList);
		map.put("set", set);

		this.logger.info("预订单自动匹配站点成功，并更新到数据库！");

		return map;
	}

	@Override
	public void onChange(Map<String, String> parameters) {
		if (parameters.keySet().contains("addressmatch.enabled")) {
			this.init();
			return;
		}
		if (parameters.keySet().contains("addressmatch.url")) {
			this.address_url = this.systemInstallService.getParameter("addressmatch.url");
		}

		if (parameters.keySet().contains("addressmatch.addZhanDianurl")) {
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");
		}

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.init();
	}

	/**
	 * 定时器抓取运单数据的时候匹配目的站点
	 *
	 * @param info
	 * @return
	 */
	public Branch matchAddress4SinfferTransData(ExtralInfo4Address info) {
		this.logger.info(" start address match for {} express order", info.getCwb());
		try {

			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setCwb(info.getCwb());
			User user = this.userDAO.getUserByUserid(info.getUserId());
			// 地址库是否开启
			String addressenabled = this.systemInstallService.getParameter("newaddressenabled");
			// 匹配站点
			Branch branch = this.getAddressMathcBranch(info.getCwb(), info.getAddress(), 0, cwbOrder, user, addressenabled);
			return branch;
		} catch (Exception e) {
			this.logger.info("定时器抓取运单数据的时候匹配目的站点异常，异常原因：{}", e.getMessage());
		}
		return null;
	}
}
