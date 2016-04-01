package cn.explink.b2c.zhemeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.zhemeng.xmldto.RequestAddressShell;
import cn.explink.b2c.zhemeng.xmldto.RequestItems;
import cn.explink.b2c.zhemeng.xmldto.RequestOrder;
import cn.explink.b2c.zhemeng.xmldto.ResponseAddress;
import cn.explink.b2c.zhemeng.xmldto.ResponseItem;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;

@Service
public class ZhemengService {
	private Logger logger = LoggerFactory.getLogger(ZhemengService.class);
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	
	
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerService customerService;
	
	
	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Zhemeng getZhenMeng(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Zhemeng jx = (Zhemeng) JSONObject.toBean(jsonObj, Zhemeng.class);
		return jx;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Zhemeng zm = new Zhemeng();
		zm.setPrivate_key(request.getParameter("private_key"));
		zm.setTms_service_code(request.getParameter("tms_service_code"));
		zm.setCustomerid(request.getParameter("customerid"));
		zm.setReceiver_url(request.getParameter("receiver_url"));
		zm.setSend_url(request.getParameter("send_url"));
		zm.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));

		String customerid = request.getParameter("customerid");
		String oldCustomerid = "";

		JSONObject jsonObj = JSONObject.fromObject(zm);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerid = getZhenMeng(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerid, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getState(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	
	/**
	 * 订单创建
	 * @param client_id
	 * @param data
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public String createOrder(String service,String content,String sign) throws Exception{
		Zhemeng zm = this.getZhenMeng(B2cEnum.ZheMeng.getKey());
		
		
		try {
			if(!"tms_order_notify".equals(service)){
				return this.response_XML("fail", "service不正确："+service);
			}
			String localMd5=MD5Util.md5(content+zm.getPrivate_key());
			if(localMd5!=null&&!localMd5.equalsIgnoreCase(sign)){
				logger.info("签名异常，本地签名:"+localMd5);
				return this.response_XML("fail", "签名异常");
			}
			
			RequestOrder order = (RequestOrder)ObjectUnMarchal.XmltoPOJO(content, new RequestOrder());
			
			List<Map<String, String>> xmlList = getDetailParamList(order,zm);
			if(xmlList==null||xmlList.size()==0){
				return this.response_XML("fail", "处理失败或订单重复");
			}
			
			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(zm.getCustomerid()), B2cEnum.ZheMeng.getMethod(), xmlList, zm.getWarehouseid(), true);
			
			return response_XML("success", "成功");
			
		} catch (Exception e) {
			logger.error("处理哲盟安达信异常",e);
			return response_XML("fail", "异常"+e.getMessage());
		}
		
		
	}
	
	
	public List<Map<String, String>> getDetailParamList(RequestOrder order,Zhemeng zm) {
		List<Map<String, String>> paralist = new ArrayList<Map<String, String>>();
	
		long cwbordertypeid=1;
		String ordertype=order.getOrdertype();
		if("订货订单".equals(ordertype)){
			cwbordertypeid=1;
		}else if("换货订单".equals(ordertype)){
			cwbordertypeid=3;
		}else if("代取订单".equals(ordertype)){ //？代取订单是上门退么？
			cwbordertypeid=2;
		}
		
		
		
		double receivablefee = order.getTotal_amount()/100;
		
		CwbOrderDTO cwbOrderDTO = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getOrder_code());
		if(cwbOrderDTO!=null){
			logger.info("订单重复,cwb:{}",order.getOrder_code());
			return null;
		}
		
		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(order.getOrder_code());
		if(cwbOrder!=null){
			logger.info("订单重复,cwb:{}",cwbOrder.getCwb());
			return null;
		}
		
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", order.getOrder_code());
		dataMap.put("transcwb",  order.getOrderstr()==null||order.getOrderstr().isEmpty()?order.getOrder_code():order.getOrderstr());
		dataMap.put("consigneename", order.getReceiver_name());
		dataMap.put("consigneeaddress", order.getReceiver_address());
		dataMap.put("cwbprovince", order.getReceiver_province());
		dataMap.put("cwbcity", order.getReceiver_city());
		dataMap.put("consigneemobile", order.getReceiver_mobile());
		dataMap.put("consigneephone", order.getReceiver_phone());
		dataMap.put("cargoamount",  receivablefee+""); // 货物金额
		dataMap.put("receivablefee", receivablefee + ""); // 代收款
		dataMap.put("sendcarnum", (order.getPcs()==0?1: order.getPcs())+""); // 发货商品
		dataMap.put("cwbordertypeid", String.valueOf(cwbordertypeid)); // 订单类型
		dataMap.put("cargorealweight", order.getPackage_weight()); //重量
		dataMap.put("cargovolume", (order.getPackage_volume()==null||order.getPackage_volume().isEmpty()?"0":order.getPackage_volume())); //体积
		dataMap.put("customercommand", order.getRemark()); //备注
		
		paralist.add(dataMap);

		return paralist;
	}
	
	
	
	
	
	/**
	 * 反馈给zhemeng信息
	 * 
	 * @param log_result
	 * @param log_event
	 * @return
	 */
	public String response_XML(String log_result, String log_event) {
		String strs = "";
		if ("success".equals(log_result)) {
			strs = "<wlb><is_success>T</is_success></wlb>";
		} else {
			strs = "<wlb><is_success>F</is_success><error>" + log_event + "</error></wlb>";
		}
		logger.info(strs);
		return strs;

	}
	

	
	/**
	 * 配送地址检查
	 * @param client_id
	 * @param data
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public String checkAddress(String service,String content,String sign) throws Exception{
		Zhemeng zm = this.getZhenMeng(B2cEnum.ZheMeng.getKey());
		
		ResponseAddress  respAdd = new ResponseAddress();
		List<ResponseItem> respItems= new ArrayList<ResponseItem>();
		
		try {
			if(!"tms_point".equals(service)){
				return "service不正确："+service;
			}
			String localMd5=MD5Util.md5(content+zm.getPrivate_key());
			if(localMd5!=null&&!localMd5.equalsIgnoreCase(sign)){
				return "签名异常"+service;
			}
		
			
			RequestAddressShell addressShell = (RequestAddressShell)ObjectUnMarchal.XmltoPOJO(content, new RequestAddressShell());
			
			for(RequestItems address:addressShell.getRequestAddressItems().getItems()){
				String addressDetail = address.getProvince()+address.getCity()+address.getArea()+address.getAddress();
				JSONObject addressJson =  addressMatchService.matchAddressByInterfaces("zhemeng-andaxin", addressDetail);
				
				ResponseItem respItem = new ResponseItem();
				
				if(addressJson == null){
					respItem.setItemno(address.getItemno());
					respItem.setNetid("");
					respItem.setNetpoint("");
					respItems.add(respItem);
					continue;
				}
				
				String branchid=addressJson.getString("station"); //对应返回的站点ID
				if(branchid==null||branchid.isEmpty()){
					respItem.setItemno(address.getItemno());
					respItem.setNetid("");
					respItem.setNetpoint("");
					respItems.add(respItem);
					continue;
				}
				
				Branch branch = branchDAO.getBranchById(Long.valueOf(branchid));
				respItem.setItemno(address.getItemno());
				respItem.setNetid(branch.getBranchcode());
				respItem.setNetpoint(branch.getBranchname());
				respItems.add(respItem);
			
			}
			respAdd.setItems(respItems);
			String responseXML = ObjectUnMarchal.POJOtoXml(respAdd);
			return responseXML;
			
			
		} catch (Exception e) {
			logger.error("未知异常",e);
			return "未知异常"+e.getMessage();
		}
		
		
		
		
	}
	
	

}
