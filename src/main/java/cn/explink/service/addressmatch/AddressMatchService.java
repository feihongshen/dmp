package cn.explink.service.addressmatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.OrderAddressMappingResult;
import cn.explink.domain.addressvo.OrderVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.BranchAutoWarhouseService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.service.SystemConfigChangeListner;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.ResourceBundleUtil;

@Component
@DependsOn({ "systemInstallService" })
public class AddressMatchService implements SystemConfigChangeListner, ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String address_url;
	private String addZhanDian_url;
	private String address_userid;
	private String addressMatchConsumerCount;

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
	BranchAutoWarhouseService branchAutoWarhouseService;

	public void init() {
		this.logger.info("init addressmatch camel routes");
		try {
			this.address_url = this.systemInstallService.getParameter("addressmatch.url");
			this.addZhanDian_url = this.systemInstallService.getParameter("addressmatch.addZhanDianurl");
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");
			this.addressMatchConsumerCount = this.systemInstallService.getParameter("addressMatch.consumerCount");
			if (!StringUtils.hasLength(this.addressMatchConsumerCount)) {
				this.addressMatchConsumerCount = "1";
			}
			String addressmatchenabled = this.systemInstallService.getParameter("addressmatch.enabled");
			if (addressmatchenabled.equals("1")) {
				this.logger.info("enable addressmatch camel routes");
				this.camelContext.addRoutes(new RouteBuilder() {
					@Override
					public void configure() throws Exception {
						this.from("jms:queue:VirtualTopicConsumers.cwborderinsert.addressmatch?concurrentConsumers=" + AddressMatchService.this.addressMatchConsumerCount)
								.to("bean:addressMatchService?method=matchAddress").routeId("地址匹配");
						this.from("jms:queue:VirtualTopicConsumers.omsToAddressmatch.addzhandian?concurrentConsumers=" + AddressMatchService.this.addressMatchConsumerCount)
								.to("bean:addressMatchService?method=addZhanDian").routeId("创建地址库中的站点");
					}
				});
			} else {
				this.logger.info("disable addressmatch camel routes");
				this.camelContext.addRoutes(new RouteBuilder() {
					@Override
					public void configure() throws Exception {
						this.from("jms:queue:VirtualTopicConsumers.cwborderinsert.addressmatch?concurrentConsumers=" + AddressMatchService.this.addressMatchConsumerCount)
								.to("bean:addressMatchService?method=empty").routeId("地址匹配");
						this.from("jms:queue:VirtualTopicConsumers.omsToAddressmatch.addzhandian?concurrentConsumers=" + AddressMatchService.this.addressMatchConsumerCount)
								.to("bean:addressMatchService?method=empty").routeId("创建地址库中的站点");
					}
				});
			}

		} catch (Exception e) {
			this.logger.error("camel context start fail", e);
		}

	}

	public void addZhanDian(@Header("branchid") String branchid) {
		this.logger.info("add zhandian branche id:{}", branchid);
		try {
			Branch b = this.branchDAO.getBranchByBranchid(Long.parseLong(branchid));
			JSONReslutUtil.getResultMessage(this.addZhanDian_url, "userid=" + this.address_userid + "&stationId=" + b.getBranchid() + "&name=" + b.getBranchname(), "POST");
		} catch (Exception e) {
			this.logger.error("error add zhandian branche id:{}", branchid);
		}
	}

	public void empty(@Header("cwb") String cwb) {
		this.logger.debug("empty process cwb:{}", cwb);
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
	private OrderVo getOrderVo(CwbOrder cwb) {
		OrderVo ordervo = new OrderVo();
		ordervo.setOrderId(cwb.getCwb());
		ordervo.setAddressLine(cwb.getConsigneeaddress());
		ordervo.setVendorId(cwb.getCustomerid());
		ordervo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		return ordervo;
	}

	public void matchAddress(@Header("userid") long userid, @Header("cwb") String cwb) {
		this.logger.info("start address match for {}", cwb);
		try {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			User user = this.userDAO.getUserByUserid(userid);
			String addressenabled = this.systemInstallService.getParameter("newaddressenabled");
			if ((addressenabled != null) && addressenabled.equals("1")) {
				// TODO 启用新地址库 调用webservice
				List<OrderVo> orderVoList = new ArrayList<OrderVo>();
				try {
					orderVoList.add(this.getOrderVo(cwbOrder));
					AddressMappingResult addressreturn = this.addressMappingService.mappingAddress(this.getApplicationVo(), orderVoList);
					int successFlag = addressreturn.getResultCode().getCode();
					if (successFlag == 0) {
						OrderAddressMappingResult mappingresult = addressreturn.getResultMap().get(cwb);
						if (mappingresult != null) {
							List<DeliveryStationVo> deliveryStationList = mappingresult.getDeliveryStationList();
							List<DelivererVo> delivererList = mappingresult.getDelivererList();
							List<Integer> timeLimitList = mappingresult.getTimeLimitList();
							if (deliveryStationList.size() == 0) {
								return;
							}
							Set<Long> set = new HashSet<Long>();
							for (DeliveryStationVo desvo : deliveryStationList) {
								set.add(desvo.getExternalId());
							}
							if (set.size() == 1) {
								Branch b = this.branchDAO.getEffectBranchById(deliveryStationList.get(0).getExternalId());
								if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
									this.cwbOrderService.updateAddressMatch(user, cwbOrder, b, CwbOrderAddressCodeEditTypeEnum.DiZhiKu, deliveryStationList, delivererList, timeLimitList);
								}
							}
						}
					}
				} catch (Exception e) {
					this.logger.error("error while doing address match for {}", cwb);
				}

			} else {
				// 老地址库
				JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + cwbOrder.getCwb() + "@"
						+ cwbOrder.getConsigneeaddress().replaceAll(",", ""), "POST"));
				Branch b = null;
				for (int i = 0; i < addressList.size(); i++) {
					try {
						b = this.branchDAO.getEffectBranchById(addressList.getJSONObject(i).getLong("station"));
						if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
							this.cwbOrderService.updateDeliveryBranch(user, cwbOrder, b, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
							// 触发上门退自动分站领货
							this.branchAutoWarhouseService.branchAutointoWarhouse(cwbOrder, b);
						}
					} catch (CwbException ce) {
						this.logger.error("update branche for cwb send jms {},error:{}", cwb, ce.getMessage());
					}
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			this.logger.error("error while doing address match for {}", cwb);
		}
	}

	/**
	 * 唯品会请求地址库接口
	 * 
	 * @param itemno
	 * @param Address
	 * @return
	 */
	public JSONObject matchAddressByInterface(String itemno, String Address) {
		this.logger.info("唯品会匹配站点: 地址： {} 开始", Address);
		JSONObject json = new JSONObject();
		try {
			JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + itemno + "@" + Address.replaceAll(",", ""),
					"POST"));
			Branch b = null;
			if ((addressList != null) && (addressList.size() > 0)) {
				try {
					b = this.branchDAO.getBranchById(addressList.getJSONObject(0).getLong("station"));
					if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						json.put("itemno", itemno);
						json.put("netid", b.getBranchid());
						json.put("netpoint", b.getBranchname());
						json.put("remark", "已匹配到站点");
						this.logger.info("唯品会匹配站点: 地址：{},匹配结果:{}", Address, b.getBranchname());
					} else {
						json.put("itemno", itemno);
						json.put("netid", "");
						json.put("netpoint", "");
						json.put("remark", "未匹配到站点");
						this.logger.info("唯品会匹配站点: 地址：{},匹配结果:{}", Address, "返回的站点不属于系统中站点类型");
					}
				} catch (Exception e) {
					json.put("itemno", itemno);
					json.put("netid", "");
					json.put("netpoint", "");
					json.put("remark", "未匹配到站点");
					this.logger.info("唯品会匹配站点: 地址：{},未匹配到站点", Address);
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			json.put("itemno", itemno);
			json.put("netid", "");
			json.put("netpoint", "");
			json.put("remark", "未匹配到站点");
			this.logger.error("唯品会未匹配到站点,接口异常", e);
		}
		return json;
	}

	/**
	 * ADD BY WANGYCH 20140725 唯品会请求地址库接口,面单前置
	 * 
	 * @param itemno
	 * @param Address
	 * @return
	 */
	public JSONObject matchAddressByPre(String itemno, String Address) {
		this.logger.info("唯品会匹配站点: 地址： {} 开始", Address);
		JSONObject json = new JSONObject();
		try {
			JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + itemno + "@" + Address.replaceAll(",", "")
					+ "&showinfo=true", "POST"));
			Branch b = null;
			if ((addressList != null) && (addressList.size() > 0)) {
				try {
					b = this.branchDAO.getBranchById(addressList.getJSONObject(0).getLong("station"));
					if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						json.put("itemno", addressList.getJSONObject(0).getString("id"));
						json.put("netid", b.getBranchid());
						json.put("netpoint", b.getBranchname());
						json.put("error", addressList.getJSONObject(0).getString("error"));
						json.put("province", addressList.getJSONObject(0).getString("province"));
						json.put("city", addressList.getJSONObject(0).getString("city"));
						json.put("district", addressList.getJSONObject(0).getString("district"));
						json.put("finaladdress", addressList.getJSONObject(0).getString("finaladdress"));
						json.put("remark", "已匹配到站点");
						this.logger.info("唯品会匹配站点: 地址：{},匹配结果:{}", Address, b.getBranchname());
					} else {
						json.put("itemno", itemno);
						json.put("netid", "");
						json.put("netpoint", "");
						json.put("error", addressList.getJSONObject(0).getString("error"));
						json.put("province", "");
						json.put("city", "");
						json.put("district", "");
						json.put("finaladdress", "");
						json.put("remark", "未匹配到站点");
						this.logger.info("唯品会匹配站点: 地址：{},匹配结果:{}", Address, "返回的站点不属于系统中站点类型");
					}
				} catch (Exception e) {
					json.put("itemno", itemno);
					json.put("netid", "");
					json.put("netpoint", "");
					json.put("error", "");
					json.put("province", "");
					json.put("city", "");
					json.put("district", "");
					json.put("finaladdress", "");
					json.put("remark", "未匹配到站点");
					this.logger.info("唯品会匹配站点: 地址：{},未匹配到站点", Address);
				}
			}

		} catch (Exception e) {
			json.put("itemno", itemno);
			json.put("netid", "");
			json.put("netpoint", "");
			json.put("error", "");
			json.put("province", "");
			json.put("city", "");
			json.put("district", "");
			json.put("finaladdress", "");
			json.put("remark", "未匹配到站点");
			this.logger.error("唯品会未匹配到站点,接口异常", e);
		}
		return json;
	}

	/**
	 * 请求匹配地址公用接口
	 * 
	 * @param itemno
	 * @param Address
	 * @return
	 */
	public JSONObject matchAddressByPublicInterface(String itemno, String Address) {
		this.logger.info("start address match for {}", itemno);
		try {
			JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + itemno + "@" + Address.replaceAll(",", ""),
					"POST"));

			return addressList.getJSONObject(0);

		} catch (Exception e) {
			this.logger.error("请求站点地址信息发生异常", e);
			return null;
		}

	}

	public void matchAddress1(@Header("cwb") String cwb) {
		this.logger.info("start address match for {}", cwb);
		try {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			List<MatchResult> matchedBranchids = this.addressMatchManager.getMatch(cwbOrder.getConsigneeaddress());
			if (matchedBranchids.size() == 1) {
				this.jdbcTemplate.update("update express_ops_cwb_detail set excelbranch=? where opscwbid=? and state=1 ", matchedBranchids.get(0).getBranchid(), cwbOrder.getOpscwbid());
			}
			if (matchedBranchids.size() > 1) {
				String keyword = "";
				String branch = "";
				for (MatchResult matchResult : matchedBranchids) {
					branch += matchResult.getBranchid();
					keyword += matchResult.getKey();
				}
				this.logger.warn("find multiple match site for cwb{}", cwb);
				this.jdbcTemplate.update("update express_ops_cwb_detail set excelbranch=?,multipbranchflag=1,nextbranchid=? where opscwbid=? and state=1 ", branch, keyword, cwbOrder.getOpscwbid());
			}
			if (matchedBranchids.size() == 0) {
				this.jdbcTemplate.update("update express_ops_cwb_detail set multipbranchflag=-1 where opscwbid=? and state=1", cwbOrder.getOpscwbid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("error while doing address match for {}", cwb);
		}
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
			this.addZhanDian_url = this.systemInstallService.getParameter("addressmatch.addZhanDianurl");
		}
		if (parameters.keySet().contains("addressmatch.addZhanDianurl")) {
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");
		}
		if (parameters.keySet().contains("addressMatch.consumerCount")) {
			this.addressMatchConsumerCount = this.systemInstallService.getParameter("addressMatch.consumerCount");
		}

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.init();
	}
}
