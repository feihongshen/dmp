package cn.explink.b2c.mss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.explink.b2c.jiuye.JiuYe;
import cn.explink.b2c.mss.json.Consignee;
import cn.explink.b2c.mss.json.Good;
import cn.explink.b2c.pjwl.ExpressCwbOrderDTO;
import cn.explink.b2c.pjwl.ExpressCwbOrderDataImportDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.VO.express.ExtralInfo4Address;
import cn.explink.service.addressmatch.AddressMatchExpressService;
import cn.explink.util.B2cUtil;
import cn.explink.util.StringUtil;
import net.sf.json.JSONObject;

@Service
public class MssService {
	private static final String String = null;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2cUtil b2cUtil;
	private Logger logger = LoggerFactory.getLogger(MssService.class);
	@Autowired
	ExpressCwbOrderDataImportDAO expressCwbOrderDataImportDAO;
	@Autowired
	AddressMatchExpressService addressMatchExpressService;
	
	@Autowired
	private BranchDAO branchDao;

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Mss dms = new Mss();
		String customerid = request.getParameter("customerid");
		dms.setCustomerid(customerid);
		dms.setFeedbackUrl(request.getParameter("feedbackUrl"));
		dms.setImportUrl(request.getParameter("importUrl"));
		String maxCount = StringUtil.nullConvertToEmptyString(request.getParameter("maxCount"));
		dms.setMaxCount(("".equals(maxCount)) ? 0 : (Integer.valueOf(request.getParameter("maxCount"))));
		dms.setImgUrl(request.getParameter("imgUrl"));
		dms.setAccessKey(request.getParameter("access_key"));
		dms.setCmd(request.getParameter("cmd"));
		dms.setTicket(request.getParameter("ticket"));
		dms.setVersion(request.getParameter("version"));
		dms.setSecretKey(request.getParameter("secretKey"));
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.b2cUtil.getViewBean(joint_num, new JiuYe().getClass()).getCustomerid();
			} catch (Exception e) {
				this.logger.error("解析【otms】基础设置异常,原因:{}", e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}

	@SuppressWarnings({  "unchecked" })
	public String RequestOrdersToTMS(String params, Mss mss) throws Exception {
		Map<String, Object> productMap = new HashMap<String, Object>();
		try {
			productMap = new ObjectMapper().readValue(params, Map.class);
		} catch (Exception e) {
			this.logger.warn("解析otmsjson异常，" + e);
			return this.responseJson("5001", "系统异常	", mss,"");
		}
		if (productMap.isEmpty()) {
			this.logger.info("otms参数为空,params={}", params);
			return this.responseJson("4002", "请求参数错误", mss,"");
		}
		// 获取签名
		String sign = (String) productMap.get("sign");
		// 删除签名
		productMap.remove("sign");
		String signKey=SignUtil.getSign(productMap, mss.getSecretKey());
			
		if (!signKey.equals(sign)) {
			this.logger.warn("签名错误");
			return this.responseJson("4001", "验签错误", mss,"");
			}
		Map<String, Object> body = (Map<String, Object>) productMap.get("body");
		ExpressCwbOrderDTO orderlist=null;
		try {
			orderlist = this.parseCwbArrByOrderDto(body, mss);
		} catch (Exception e) {
			logger.error("otms请求参数错误,订单号{}",body.get("partner_order_id"));
			return this.responseJson("4002", "请求参数错误", mss,"");
		}
		if ((orderlist == null) ) {
			this.logger.warn("otms-请求没有封装参数，订单号可能为空");
			return this.responseJson("4004", "请求参数缺失", mss,"");
		}
		expressCwbOrderDataImportDAO.insertTransOrder_toTempTable(orderlist);
		this.logger.info("otms-订单导入成功");
		return this.responseJson("200", "成功", mss,(String)body.get("partner_order_id"));
	}

	@SuppressWarnings("unchecked")
	private ExpressCwbOrderDTO parseCwbArrByOrderDto(Map<String, Object> body, Mss mss) {
			ExpressCwbOrderDTO expressCwbOrderDTO=new ExpressCwbOrderDTO();
			String cwb = (String) body.get("partner_order_id");
		if (!body.isEmpty()) {
			CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);// 商户订单编号（在o3系统中必须唯一，不能重复）
			if (cwbOrder != null) {
				this.logger.warn("获取otms订单中含有重复数据cwb={}", body.get("partner_order_id"));
				return null;
			}
		}
		//订单号
		expressCwbOrderDTO.setTransportNo(cwb);
		expressCwbOrderDTO.setCustOrderNo(cwb);
		List<Map<String, Object>> goodsMap = (List<Map<String, Object>>) body.get("goods");
		String sendcarname = "";
		String carsize = "";
		Integer count = 0;
		Long price = 0L;
		for (Map<String, Object> good : goodsMap) {
			sendcarname += (String)good.get("name") + ",";
			carsize += (String)good.get("specs") + ",";
			count += (Integer)good.get("quantity");
			price += (Integer)good.get("price");
		}
		Branch branch = null;
		 Map<String,String> consigneeMap = (Map<java.lang.String, java.lang.String>) body.get("consignee");
		Map<String, Object> extraMap = (Map<String, Object>) body.get("extra_services");
		Map<String, Object> deliveryMap = (Map<String, Object>) extraMap.get("delivery");
		Map<String, String> pickMap = (Map<String, String>) deliveryMap.get("pick_up");
		Map<String,String> extraMetas = (Map<String, String>) body.get("extra_metas");
		String totalVolume = extraMetas.get("total_volume");
		String totalWeight = extraMetas.get("total_weight");
		String address=pickMap.get("address");
		expressCwbOrderDTO.setCnorProv("北京市");//发货省
		expressCwbOrderDTO.setCnorCity("北京市");//发货市
		expressCwbOrderDTO.setCnorRegion("朝阳区");//发货区
		expressCwbOrderDTO.setCnorAddr(pickMap.get("address"));//发货地址
		expressCwbOrderDTO.setCnorName(pickMap.get("name"));//发货人
		expressCwbOrderDTO.setCnorTel(pickMap.get("tel"));//发货人电话
		expressCwbOrderDTO.setCnorMobile(pickMap.get("mobile"));//发件人手机
		expressCwbOrderDTO.setCnorRemark((String)body.get("note"));
		expressCwbOrderDTO.setCneeProv("北京市");//收件人省
		expressCwbOrderDTO.setCneeCity("北京市");//收件人市
		expressCwbOrderDTO.setCneeRegion("朝阳区");//收件区 
		expressCwbOrderDTO.setCneeName(consigneeMap.get("name"));//收件人姓名
		expressCwbOrderDTO.setCneeAddr(consigneeMap.get("address"));//收件人地址
		expressCwbOrderDTO.setCneeTel(consigneeMap.get("tel"));//收件人电话
		expressCwbOrderDTO.setCneeMobile(consigneeMap.get("mobile"));//收件人手机
		expressCwbOrderDTO.setTotalWeight(new BigDecimal(totalWeight.substring(0, totalWeight.indexOf("K")-1)) );//合计重量
		expressCwbOrderDTO.setTotalVolume(new BigDecimal(totalVolume.substring(0, totalVolume.indexOf("C")-1)));//合计体积
		expressCwbOrderDTO.setTotalBox(1);//箱号
		expressCwbOrderDTO.setCargoName(sendcarname);
		expressCwbOrderDTO.setTotalNum(count);//发货数量
		expressCwbOrderDTO.setIsCod(0);//是否货到付款
		expressCwbOrderDTO.setCodAmount(new BigDecimal("0"));//代收货款
		expressCwbOrderDTO.setPayType(1);//付款方式/结算方式
		expressCwbOrderDTO.setPayment(-1);//支付方式
		expressCwbOrderDTO.setCargoLength(new BigDecimal("1"));//长
		expressCwbOrderDTO.setCargoWidth(new BigDecimal("1"));//宽
		expressCwbOrderDTO.setCargoHeight(new BigDecimal("1"));//高
		expressCwbOrderDTO.setAssuranceFee(new BigDecimal("0.0"));//保费
		expressCwbOrderDTO.setIsAcceptProv(1);
		if(StringUtils.isNotEmpty(address)){
			branch = this.matchDeliveryBranch(cwb,address);
		}
		try {
			expressCwbOrderDTO.setAcceptDept(branch.getBranchname());
			String branchcontactman =this.branchDao.getBranchByBranchid(branch.getBranchid()).getBranchcontactman();
			expressCwbOrderDTO.setAcceptOperator(branchcontactman);
			this.logger.info("订单{}匹配地址为{}",cwb,branch.getBranchname());
		} catch (Exception e) {
			this.logger.error("订单{}匹配地址失败",cwb);
			return null;
		}
		expressCwbOrderDTO.setCneePeriod(0);
		expressCwbOrderDTO.setCount(count);
		expressCwbOrderDTO.setAssuranceValue(new BigDecimal(0));
		return expressCwbOrderDTO;
	}

	/**
	 * 排序之后返回otms
	 *
	 * @param code
	 * @param success
	 * @param msg
	 * @param mss
	 * @return
	 */
	public String responseJson(String code, String msg, Mss mss,String cwb) {
		long time = System.currentTimeMillis();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		responseMap.put("access_key", mss.getAccessKey());
		responseMap.put("cmd", mss.getCmd());
		responseMap.put("ticket", mss.getTicket());
		bodyMap.put("code", code);
		bodyMap.put("message", msg);
		if ("200".equals(code)) {
			//dataMap.put("partner_id", mss.get);
			dataMap.put("partner_order_id", cwb);
		}
		bodyMap.put("data", dataMap);
		responseMap.put("body", bodyMap);
		responseMap.put("time", time / 1000);
		responseMap.put("sign", SignUtil.getSign(responseMap, mss.getSecretKey()));
		Map<String, Object> response = SignUtil.sortMapByKey(responseMap);
		return SignUtil.toJson(response);
	}
	
	
	private Branch matchDeliveryBranch(String cwb,String cneeAddr) {
		Branch branch = null;
		ExtralInfo4Address info = new ExtralInfo4Address(cwb,1L,cneeAddr);
		//匹配站点
		branch = addressMatchExpressService.matchAddress4SinfferTransData(info);
		return branch;
	}

}
