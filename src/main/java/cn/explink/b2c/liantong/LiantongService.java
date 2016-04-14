package cn.explink.b2c.liantong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.liantong.json.Goods;
import cn.explink.b2c.liantong.json.UnicomRequest;
import cn.explink.b2c.liantong.json.UnicomResponse;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class LiantongService {
	private static Logger logger = LoggerFactory.getLogger(LiantongService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CustomerService customerService;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Liantong getLianTong(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Liantong dangdang = (Liantong) JSONObject.toBean(jsonObj, Liantong.class);
		return dangdang;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Liantong lt = new Liantong();
		String customerid = request.getParameter("customerid");
		lt.setCustomerid(customerid);
		lt.setPrivate_key(request.getParameter("private_key"));
		lt.setSearch_url(request.getParameter("search_url"));
		String warehouseid = request.getParameter("warehouseid").isEmpty() ? "0" : request.getParameter("warehouseid");
		lt.setWarehouseid(Long.valueOf(warehouseid));
		lt.setRequest_url(request.getParameter("request_url"));
		lt.setFeedback_url(request.getParameter("feedback_url"));

		lt.setAppcode(request.getParameter("appcode"));
		lt.setAppsecret(request.getParameter("appsecret"));
		lt.setSignSecurity(request.getParameter("signSecurity"));
		lt.setAppkey(request.getParameter("appkey"));
		lt.setMethod(request.getParameter("method"));
		lt.setMaxCount(Long.valueOf(request.getParameter("maxCount").isEmpty() ? "20" : request.getParameter("maxCount")));

		JSONObject jsonObj = JSONObject.fromObject(lt);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getLianTong(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 调用订单导入方法
	 * 
	 * @param requestXml
	 * @return
	 */
	public String invokeDealWithCwbOrderImport(String JsonContent, String RequestTime, String sign) {

		try {
			Liantong lt = this.getLianTong(B2cEnum.Liantong.getKey());
			String localMd5 = MD5Util.md5(JsonContent + RequestTime + lt.getPrivate_key());
			if (!localMd5.equalsIgnoreCase(sign)) {
				return buildUnicomResponse("", "", "FAIL", "签名验证异常");
			}

			UnicomRequest unicomRequest = JacksonMapper.getInstance().readValue(JsonContent, UnicomRequest.class);

			if (unicomRequest == null) {
				return buildUnicomResponse("", "", "FAIL", "JSON数据转化为Bean为空");
			}

			List<Map<String, String>> datalist = buildOrders(unicomRequest);

			if (datalist == null || datalist.size() == 0) {
				return buildUnicomResponse(unicomRequest.getOrderNo(), unicomRequest.getMailNo(), "FAIL", "数据重复");
			}

			dataImportInterface.Analizy_DataDealByB2c(Long.valueOf(lt.getCustomerid()), B2cEnum.Liantong.getMethod(), datalist, lt.getWarehouseid(), true);
			logger.info("处理联通导入后的订单信息成功,cwb={}", unicomRequest.getOrderNo());

			return buildUnicomResponse(unicomRequest.getOrderNo(), unicomRequest.getMailNo(), "SUCCESS", "成功");

		} catch (Exception e) {
			return buildUnicomResponse("", "", "FAIL", "未知异常" + e.getMessage());
		}

	}

	private List<Map<String, String>> buildOrders(UnicomRequest order) {

		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();

		Map<String, String> orderMap = new HashMap<String, String>();

		String cwb = order.getOrderNo();

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder != null) {
			logger.warn("获取0联通商城0订单中含有重复数据cwb={}", cwb);
			return null;
		}

		String applyType = StringUtil.nullConvertToEmptyString(order.getApplyType()); // 代收货款订单：CMONEY；普通业务：COMMON；
																						// 如果是代收货款业务，OrderAmount必须有值
		String senderInfo = getSenderInfo(order); // 寄件人信息
		String RevName = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevName()); // 收件人
		String RevPostcode = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevPostcode()); // 邮编
		String RevMobile = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevMobile()); // 手机
		String RevTel = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevTel());
		String RevProvince = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevProvince());
		String RevCity = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevCity());
		String RevCounty = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevCounty());
		String RevAddress = StringUtil.nullConvertToEmptyString(order.getReceiver().getRevAddress());
		String Remark = StringUtil.nullConvertToEmptyString(order.getReceiver().getRemark());
		String custid = StringUtil.nullConvertToEmptyString(order.getCustid());

		// 详细地址=省+市+区+地址
		String RevAddress_detail = RevProvince + RevCity + RevCounty + RevAddress;

		String sendcarname = "";// 发货商品名称
		int sendcarnum = 0;
		double weight = 0;
		double GoodsVolume = 0;
		double cargoamount = 0;
		List<Goods> goods = order.getGoods();
		for (Goods good : goods) {
			sendcarname += good.getGoodsName() + ",";
			sendcarnum += good.getGoodsNum();
			weight += good.getGoodsWeight();
			GoodsVolume += good.getGoodsVolume();
			cargoamount += good.getGoodsPrice();
		}

		orderMap.put("cwb", cwb);
		orderMap.put("transcwb", order.getMailNo());
		orderMap.put("consigneename", RevName);
		orderMap.put("consigneepostcode", RevPostcode);
		orderMap.put("consigneemobile", RevMobile);
		orderMap.put("consigneephone", RevTel);
		orderMap.put("consigneeaddress", RevAddress_detail);
		orderMap.put("cwbprovince", RevProvince);
		orderMap.put("cwbcity", RevCity);
		orderMap.put("cwbcounty", RevCounty);

		orderMap.put("receivablefee", ("CMONEY".equals(applyType) ? order.getOrderAmount() : "0"));
		orderMap.put("sendcarnum", sendcarnum == 0 ? "1" : String.valueOf(sendcarnum));
		orderMap.put("sendcargoname", sendcarname);
		orderMap.put("cargoamount", String.valueOf(cargoamount));
		orderMap.put("cargovolume", String.valueOf(GoodsVolume));
		orderMap.put("cargorealweight", String.valueOf(weight));

		orderMap.put("cwbremark", Remark);
		orderMap.put("remark1", custid);
		orderMap.put("remark5", senderInfo);

		datalist.add(orderMap);
		return datalist;
	}

	private String getSenderInfo(UnicomRequest order) {
		String SedCompany = order.getSender().getSedCompany();
		String SedName = order.getSender().getSedName();
		String SedPostcode = order.getSender().getSedPostcode();
		String SedMobile = order.getSender().getSedMobile();
		String SedTel = order.getSender().getSedTel();
		String SedProvince = order.getSender().getSedProvince();
		String SedCity = order.getSender().getSedCity();
		String SedCounty = order.getSender().getSedCounty();
		String SedAddress = order.getSender().getSedAddress();

		String senderInfo = "寄件人姓名：" + SedName + ",邮编：" + SedPostcode + ",电话：" + SedMobile + ",地址：" + SedAddress;
		return senderInfo;
	}

	/**
	 * 构建返回的JSON格式
	 * 
	 * @param cwb
	 * @param transcwb
	 * @param result
	 * @param remark
	 * @return
	 */
	private static String buildUnicomResponse(String cwb, String transcwb, String result, String remark) {
		UnicomResponse unicomResponse = new UnicomResponse();
		unicomResponse.setOrderNo(cwb);
		unicomResponse.setMailNo(transcwb);
		unicomResponse.setRespCode(result);
		unicomResponse.setRespDesc(remark);
		String returnStrs = "";
		try {
			returnStrs = JacksonMapper.getInstance().writeValueAsString(unicomResponse);
		} catch (Exception e) {
			logger.error("", e);
		}
		return returnStrs;
	}

}
