package cn.explink.service.addressmatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.StringUtils;
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
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.AddressMappingResult;
import cn.explink.domain.addressvo.ApplicationVo;
import cn.explink.domain.addressvo.DeliveryStationVo;
import cn.explink.domain.addressvo.OrderAddressMappingResult;
import cn.explink.domain.addressvo.OrderVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.service.SystemConfigChangeListner;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.ResourceBundleUtil;

@Component
@DependsOn({ "systemInstallService" })
public class AddressMatchOXOService implements SystemConfigChangeListner, ApplicationListener<ContextRefreshedEvent> {

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

	private String address_url;
	private String address_userid;
	
	
	public void init() {
		this.logger.info("init addressmatch camel routes");
		try {
			this.address_url = this.systemInstallService.getParameter("addressmatch.url");
			this.address_userid = this.systemInstallService.getParameter("addressmatch.userid");
			
			String addressmatchenabled = this.systemInstallService.getParameter("addressmatch.enabled");
			if (addressmatchenabled.equals("1")) { //地址库启用
				this.logger.info("enable addressmatch camel routes");
				this.camelContext.addRoutes(new RouteBuilder() {
					@Override
					public void configure() throws Exception {
						this.from("jms:queue:VirtualTopicConsumers.oxo.addressmatchOXO?concurrentConsumers=10").to("bean:addressMatchOXOService?method=matchAddress").routeId("OXO-addressMatch");
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
	private OrderVo getOrderVo(CwbOrder order,String address) {
		OrderVo ordervo = new OrderVo();
		ordervo.setOrderId(order.getCwb());
		ordervo.setAddressLine(address);
		ordervo.setVendorId(order.getCustomerid());
		ordervo.setCustomerId(Long.parseLong(ResourceBundleUtil.addresscustomerid));
		return ordervo;
	}



	/**
	 * 
	 * @param userid
	 * @param cwb
	 * @param address 地址
	 * @param notifytype 匹配地址类型  0 揽件地址   1派件地址
	 */
	public void matchAddress(@Header("userid") long userid, @Header("cwb") String cwb,@Header("address") String address,@Header("notifytype") long notifytype) {
		this.logger.info("start address match for {}", cwb);
		try {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			if(!(cwbOrder.getCwbordertypeid()==4||cwbOrder.getCwbordertypeid()==5)){
				return ;
			}
			User user = this.userDAO.getUserByUserid(userid);
			String addressenabled = this.systemInstallService.getParameter("newaddressenabled");
			
			Branch branch = getAddressMathcBranch(cwb, address, notifytype, cwbOrder, user,addressenabled);
			if(branch == null){
				return ;
			}
			if(notifytype == 0){
				cwbOrderService.updatePickBranch(user, cwbOrder, branch, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
			}else{
				cwbOrderService.updateDeliveryBranch(user, cwbOrder, branch, CwbOrderAddressCodeEditTypeEnum.DiZhiKu);
			}

		} catch (Exception e) {
			// e.printStackTrace();
			this.logger.error("error while doing address match for "+cwb,e);
		}
	}



	private Branch getAddressMathcBranch(String cwb, String address, long notifytype,
			CwbOrder cwbOrder, User user, String addressenabled)
			throws IOException, Exception {
		if ((addressenabled != null) && addressenabled.equals("1")) {
			// TODO 启用新地址库 调用webservice
			List<OrderVo> orderVoList = new ArrayList<OrderVo>();
			try {
				List<DeliveryStationVo> deliveryStationList=new ArrayList<DeliveryStationVo>();
				Set<Long> set = addressMatchStation(cwb, address, cwbOrder, user, orderVoList,deliveryStationList);
				
				if(set == null||set.size()==0){
					return null;
				}
				if (set.size() == 1) {
					Branch b = this.branchDAO.getEffectBranchById(deliveryStationList.get(0).getExternalId());
					if ((b.getSitetype() == BranchEnum.ZhanDian.getValue()) || (b.getSitetype() == BranchEnum.KuFang.getValue())) {
						return b;
					}
				}
				
			} catch (Exception e) {
				this.logger.error("error while doing address match for "+cwb,e);
			}
			return null;

		} else {
			// 老地址库
			String params="userid="+this.address_userid+ "&address=" + cwbOrder.getCwb() + "@"+ address.replaceAll(",", "");
			JSONArray addressList = JSONArray.fromObject(JSONReslutUtil.getResultMessage(this.address_url,params , "POST"));
			Branch b = null;
			for (int i = 0; i < addressList.size(); i++) {
				try {
					if(StringUtils.isBlank(addressList.getJSONObject(i).getString("station")))
					{
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



	private Set<Long> addressMatchStation(String cwb, String address, CwbOrder cwbOrder,
			User user, List<OrderVo> orderVoList,List<DeliveryStationVo> deliveryStationList) throws Exception {
		Set<Long> set = new HashSet<Long>();
		orderVoList.add(this.getOrderVo(cwbOrder,address));
		AddressMappingResult addressreturn = this.addressMappingService.mappingAddress(this.getApplicationVo(), orderVoList);
		int successFlag = addressreturn.getResultCode().getCode();
		if (successFlag == 0) {
			OrderAddressMappingResult mappingresult = addressreturn.getResultMap().get(cwb);
			if (mappingresult != null) {
				deliveryStationList = mappingresult.getDeliveryStationList();
				if (deliveryStationList.size() == 0) {
					return null;
				}
				for (DeliveryStationVo desvo : deliveryStationList) {
					set.add(desvo.getExternalId());
				}
			}
		}
		
		return set;
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
}
