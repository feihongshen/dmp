package cn.explink.service.addressmatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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

import cn.explink.b2c.zjfeiyuan.responsedto.Item;
import cn.explink.b2c.zjfeiyuan.responsedto.ResponseData;
import cn.explink.b2c.zjfeiyuan.service.RequestFYService;
import cn.explink.dao.AddressCustomerStationDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressCustomerStationVO;
import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.AddressMappingResultEnum;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.OrderAddressMappingResult;
import cn.explink.domain.addressvo.OrderVo;
import cn.explink.domain.addressvo.ResultCodeEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.BranchAutoWarhouseService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.service.SystemConfigChangeListner;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.baiduAPI.GeoCoder;
import cn.explink.util.baiduAPI.GeoPoint;
import cn.explink.util.baiduAPI.ReGeoCoderResult;

@Component
@DependsOn({ "systemInstallService" })
public class AddressMatchService implements SystemConfigChangeListner, ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String IS_CUSTOMER_STATION_MAPPING_ENABLED = "is_customer_station_mapping_enabled";

	private static final String ENABLED = "1";
	private static final String DISABLED = "0";

	private String address_url;
	private String addZhanDian_url;
	private String address_userid;
	private String addressMatchConsumerCount;
	private String value;

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
	@Autowired
	RequestFYService requestFYService;

	@Autowired
	private AddressCustomerStationDao addressCustomerStationDao;

	public void init() {
		this.logger.info("init addressmatch camel routes");
		try {
			this.address_url = this.systemInstallService.getParameter("addressmatch.url");
			this.addZhanDian_url = this.systemInstallService.getParameter("addressmatch.addZhanDianurl");
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");
			// 浙江飞远匹配地址库
			this.value = this.systemInstallService.getParameter("requestFEIYUANaddress");

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
						/*
						 * this.from(
						 * "jms:queue:VirtualTopicConsumers.cwbbaiduMap.addressmatch?concurrentConsumers="
						 * + AddressMatchService.this.addressMatchConsumerCount)
						 * .
						 * to("bean:addressMatchService?method=matchAddressMap")
						 * .routeId("地址百度匹配");
						 */
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
						/*
						 * this.from(
						 * "jms:queue:VirtualTopicConsumers.cwbbaiduMap.addressmatch?concurrentConsumers="
						 * + AddressMatchService.this.addressMatchConsumerCount)
						 * .
						 * to("bean:addressMatchService?method=matchAddressMap")
						 * .routeId("地址百度匹配");
						 */
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

	/**
	 * 生成applicationVo
	 */
	private OrderVo getOrderVoByDuiJie(String orderId, String address) {
		OrderVo ordervo = new OrderVo();
		ordervo.setOrderId(orderId);
		ordervo.setAddressLine(address);
		ordervo.setVendorId(null);
		ordervo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		return ordervo;
	}

	public void matchAddress(@Header("userid") long userid, @Header("cwb") String cwb) {
		this.logger.info("start address match for {}", cwb);
		try {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			User user = this.userDAO.getUserByUserid(userid);
			if ("yes".equals(this.value)) {
				try {
					this.addressMatchZJFY(cwbOrder, user);
				} catch (Exception e) {
					this.logger.error("请求浙江飞远异常,原因:{}", e);
				}
			} else {
				String addressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((addressenabled != null) && addressenabled.equals("1")) {
					// TODO 启用新地址库 调用webservice
					List<OrderVo> orderVoList = new ArrayList<OrderVo>();
					try {
						orderVoList.add(this.getOrderVo(cwbOrder));
						AddressMappingResult addressreturn = this.addressMappingService.mappingAddress(this.getApplicationVo(), orderVoList);
						int successFlag = addressreturn.getResultCode().getCode();
						if (successFlag == ResultCodeEnum.success.getCode()) {
							OrderAddressMappingResult mappingResult = addressreturn.getResultMap().get(cwb);

							this.logger.info("阡陌地址库匹配返回json={}", JacksonMapper.getInstance().writeValueAsString(mappingResult));
							if (mappingResult != null) {
								List<DeliveryStationVo> deliveryStationList = mappingResult.getDeliveryStationList();
								List<DelivererVo> delivererList = mappingResult.getDelivererList();
								List<Integer> timeLimitList = mappingResult.getTimeLimitList();
								if ((deliveryStationList == null) || deliveryStationList.isEmpty()) {
									return;
								}
								AddressMappingResultEnum mappingResultEnum = mappingResult.getResult();
								if (mappingResultEnum.equals(AddressMappingResultEnum.singleResult)) {
									this.setStationNameToOrder(cwbOrder, user, deliveryStationList.get(0).getExternalId(), delivererList, timeLimitList);
								} else if (mappingResultEnum.equals(AddressMappingResultEnum.multipleResult)) {
									// 对于匹配多站按照客户-站点映射关系选择的功能默认不可用
									String mappingEnabled = this.systemInstallService.getParameter(AddressMatchService.IS_CUSTOMER_STATION_MAPPING_ENABLED, AddressMatchService.DISABLED);
									if ((mappingEnabled == null) || mappingEnabled.equals(AddressMatchService.DISABLED)) {
										return;
									}
									if (mappingEnabled.equals(AddressMatchService.ENABLED)) {
										this.processMultiMatch(cwbOrder, user, deliveryStationList, delivererList, timeLimitList);
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

			}
		} catch (Exception e) {
			// e.printStackTrace();
			this.logger.error("error while doing address match for {}", cwb);
		}
	}

	/**
	 * 对匹配多站的处理
	 *
	 * @param cwbOrder
	 * @param user
	 * @param deliveryStationList
	 * @param delivererList
	 * @param timeLimitList
	 * @throws Exception
	 */
	private void processMultiMatch(CwbOrder cwbOrder, User user, List<DeliveryStationVo> deliveryStationList, List<DelivererVo> delivererList, List<Integer> timeLimitList) throws Exception {
		Long selectedBranchId = null;
		List<Long> externalIdList = this.mapCustomerToStation(cwbOrder.getCustomerid(), deliveryStationList);
		if (externalIdList.isEmpty()) {
			return;
		}
		int stationCount = externalIdList.size();
		if (stationCount == 1) {
			selectedBranchId = externalIdList.get(0);
		} else {
			String addressLine = cwbOrder.getConsigneeaddress();
			if ((addressLine == null) || addressLine.trim().equals("")) {
				return;
			}
			int addressLength = addressLine.trim().length();
			// 使用取模的方式达到将不同的订单分散到不同的站点中的目的，同时多次请求结果相同（前提是订单地址长度不能改变）
			selectedBranchId = externalIdList.get(addressLength % stationCount);
		}
		if (selectedBranchId == null) {
			return;
		}
		this.setStationNameToOrder(cwbOrder, user, selectedBranchId, delivererList, timeLimitList);
	}

	/**
	 * 获取订单上客户可以配送的站点
	 *
	 * @param customerid
	 * @param deliveryStationList
	 * @return
	 */
	private List<Long> mapCustomerToStation(long customerid, List<DeliveryStationVo> deliveryStationList) {
		List<Long> externalIdList = new ArrayList<Long>();
		Set<Long> externalIdSet = new TreeSet<Long>();
		for (DeliveryStationVo deliveryStation : deliveryStationList) {
			externalIdSet.add(deliveryStation.getExternalId());
		}
		List<AddressCustomerStationVO> customerStationMappingList = this.addressCustomerStationDao.getCustomerStationByCustomerid(customerid);
		if ((customerStationMappingList == null) || customerStationMappingList.isEmpty()) {
			return externalIdList;
		}

		Set<Long> mappingStationIdSet = new TreeSet<Long>();
		for (AddressCustomerStationVO customerStationMapping : customerStationMappingList) {
			mappingStationIdSet.add(Long.valueOf(customerStationMapping.getBranchid()));
		}
		// 只有既属于地址库匹配的站点，又在映射关系中的站点
		externalIdSet.retainAll(mappingStationIdSet);
		externalIdList.addAll(externalIdSet);
		return externalIdList;
	}

	private void setStationNameToOrder(CwbOrder cwbOrder, User user, long branchid, List<DelivererVo> delivererList, List<Integer> timeLimitList) throws Exception {
		Branch b = this.branchDAO.getEffectBranchById(branchid);
		if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
			this.cwbOrderService.updateAddressMatch(user, cwbOrder, b, CwbOrderAddressCodeEditTypeEnum.DiZhiKu, delivererList, timeLimitList);
			// 触发上门退自动分站领货
			try {
				this.branchAutoWarhouseService.branchAutointoWarhouse(cwbOrder, b);
			} catch (Exception e) {
				this.logger.error("上门退单匹配地址库自动分站到货报错", e);
			}

		}
	}

	private void addressMatchZJFY(CwbOrder cwbOrder, User user) throws Exception {
		// 此时请求浙江飞远地址库信息----LX
		ResponseData respdata = this.requestFYService.dealAddressMatch(cwbOrder, user);
		// 单个订单请求，成功时返回空字符""
		if (!"".equals(respdata.getHead().getMsg())) {
			this.logger.info("返回的错误信息为:{}", respdata.getHead().getMsg());
			return;
		} else {// 处理当前订单所要在本系统中对应的站点
			List<Item> items = respdata.getItems().getItems();
			String siteno = "";
			if ((items != null) && !items.isEmpty()) {
				for (Item ie : items) {
					String netid = ie.getNetid();// 分拣编码
					String netpoint = ie.getNetpoint();// 分拣站点名
					siteno = ie.getSiteno();// 配送站点编码(唯一需要处理字段信息)
					String sitename = ie.getSitename();// 配送站点名称
				}
			}
			if (!"".equals(siteno)) {
				/*
				 * List<DeliveryStationVo> deliveryStationList = new
				 * ArrayList<DeliveryStationVo>(); List<DelivererVo>
				 * delivererList = new ArrayList<DelivererVo>(); List<Integer>
				 * timeLimitList = new ArrayList<Integer>();
				 */
				Branch branch = this.branchDAO.getEffectBranchByCodeStr(siteno);
				/*
				 * DeliveryStationVo dsv = new DeliveryStationVo();
				 * dsv.setExternalId(cwbOrder.getDeliverid());//小件员id（指定）
				 * dsv.setName("");//用于地址库匹配时指定的小件员姓名 DelivererVo dv = new
				 * DelivererVo(); dv.setExternalId(branch.getBranchid());
				 * dv.setName("");//默认小件员为""
				 */
				// 需要确认的地方 TODO 时效/时限------暂时不处理
				/*
				 * Integer inte = 0; timeLimitList.add(inte);
				 */

				/*
				 * deliveryStationList.add(dsv); delivererList.add(dv);
				 */
				if ((branch.getSitetype() == BranchEnum.ZhanDian.getValue()) || (branch.getSitetype() == BranchEnum.KuFang.getValue())) {
					try {
						// 触发上门退自动分站领货
						if (branch == null) {
							this.logger.info("请求浙江飞远地址库返回编码在本系统站点编码中未设置不存在,订单号:{},返回站点编码:{}", new Object[] { cwbOrder.getCwb(), siteno });
						} else {
							this.cwbOrderService.updateDeliveryBranch(user, cwbOrder, branch, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
							this.branchAutoWarhouseService.branchAutointoWarhouse(cwbOrder, branch);
						}
					} catch (Exception e) {
						this.logger.error("上门退单匹配地址库自动分站到货报错", e);
					}
				}
			}
		}
	}

	public void matchAddressMap(@Header("userid") long userid, @Header("cwb") String cwb) {
		this.logger.info("start addressMAP match for {}", cwb);
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder == null) {
			return;
		}
		// TODO 启用新地址库 调用webservice
		try {
			GeoPoint position = GeoCoder.getInstance().getGeoCoder().GetLocationDetails(cwbOrder.getConsigneeaddress());
			if (position != null) {
				ReGeoCoderResult reG = GeoCoder.getInstance().getGeoCoder().GetAddress(position.getLng(), position.getLat());
				if (reG != null) {
					this.cwbDAO.updateMapByCwb(reG.getAddressComponent().getCity(), reG.getAddressComponent().getDistrict(), cwb);
				} else {
					this.logger.info("订单地址在百度API没有匹配到省市区,cwb:{}", cwb);
				}
			} else {
				this.logger.info("订单地址在百度API没有匹配到省市区,cwb:{}", cwb);
			}
		} catch (Exception e) {
			this.logger.error("error while doing addressMAPAPI match for {}", cwb);
			e.printStackTrace();
		}
	}

	/**
	 * 唯品会请求地址库接口
	 *
	 * @param itemno
	 * @param Address
	 * @return
	 */
	public JSONObject matchAddressByInterface(String itemno, String Address, int printflag) {
		this.logger.info("唯品会匹配站点: 地址： {} 开始", Address);
		JSONObject json = new JSONObject();
		try {

			JSONArray addressList = new JSONArray();

			String addressenabled = this.systemInstallService.getParameter("newaddressenabled"); // 新旧地址库

			if ((addressenabled != null) && addressenabled.equals("1")) {

				addressList = this.invokeNewAddressMatchService(itemno, Address);

			} else {
				addressList = JSONArray
						.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + itemno + "@" + Address.replaceAll(",", ""), "POST"));
			}

			Branch b = null;
			if ((addressList != null) && (addressList.size() > 0)) {
				try {
					b = this.branchDAO.getBranchById(addressList.getJSONObject(0).getLong("station"));
					if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						json.put("itemno", itemno);
						json.put("netid", b.getBranchid());
						json.put("netpoint", (printflag == 0 ? b.getBranchname() : b.getBranchcode()));
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
			} else {
				json.put("itemno", itemno);
				json.put("netid", "");
				json.put("netpoint", "");
				json.put("remark", "未匹配到站点");
				this.logger.info("唯品会匹配站点: 地址：{},未匹配到站点", Address);
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

	private JSONArray invokeNewAddressMatchService(String itemno, String Address) {
		// TODO 启用新地址库 调用webservice
		List<OrderVo> orderVoList = new ArrayList<OrderVo>();
		try {
			JSONArray addressList = new JSONArray();

			String orderId = UUID.randomUUID().toString() + itemno;
			String address = Address;
			orderVoList.add(this.getOrderVoByDuiJie(orderId, address));
			AddressMappingResult addressreturn = this.addressMappingService.mappingAddress(this.getApplicationVo(), orderVoList);
			int successFlag = addressreturn.getResultCode().getCode();
			if (successFlag != 0) {
				return null;
			}

			OrderAddressMappingResult mappingresult = addressreturn.getResultMap().get(orderId);
			if (mappingresult != null) {
				List<DeliveryStationVo> deliveryStationList = mappingresult.getDeliveryStationList();

				if ((deliveryStationList == null) || (deliveryStationList.size() == 0)) {
					return null;
				}
				Set<Long> set = new HashSet<Long>();
				for (DeliveryStationVo desvo : deliveryStationList) {
					set.add(desvo.getExternalId());
				}
				if (set.size() == 1) {
					JSONObject jsonobj = new JSONObject();
					jsonobj.put("station", deliveryStationList.get(0).getExternalId());
					addressList.add(jsonobj);
					return addressList;
				}
			}
		} catch (Exception e) {
			this.logger.error("error while doing address match for vipshop", e);
		}
		return null;
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

	/**
	 * 对接站点匹配地址库，支持新老地址库
	 *
	 * @param itemno
	 * @param Address
	 * @return
	 * @throws IOException
	 */
	public JSONObject matchAddressByInterfaces(String itemno, String Address) throws IOException {
		this.logger.info("{}匹配站点: 地址： {} 开始", itemno, Address);
		JSONArray addressList = new JSONArray();
		String addressenabled = this.systemInstallService.getParameter("newaddressenabled"); // 新旧地址库
		if ((addressenabled != null) && addressenabled.equals("1")) {
			addressList = this.invokeNewAddressMatchService(itemno, Address);
		} else {
			addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url, "userid=" + this.address_userid + "&address=" + itemno + "@" + Address.replaceAll(",", ""), "POST"));
		}

		return (addressList == null) || (addressList.size() == 0) ? null : addressList.getJSONObject(0);

	}

}
