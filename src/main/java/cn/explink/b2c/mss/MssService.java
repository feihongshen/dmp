package cn.explink.b2c.mss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.jiuye.JiuYe;
import cn.explink.b2c.mss.json.Consignee;
import cn.explink.b2c.mss.json.Good;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.util.B2cUtil;
import cn.explink.util.StringUtil;
import net.sf.json.JSONObject;

@Service
public class MssService {
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
		dms.setWarehouseid(Integer.valueOf(request.getParameter("warehouseid")));
		dms.setAccess_key(request.getParameter("access_key"));
		dms.setSecret_key(request.getParameter("secret_key"));
		dms.setDmsCode(request.getParameter("dmsCode"));
		dms.setCmd(request.getParameter("cmd"));
		dms.setVersion(request.getParameter("version"));
		dms.setPartner_id(request.getParameter("partner_id"));
		dms.setPartner_order_id(request.getParameter("partner_order_id"));
		dms.setTicket(request.getParameter("ticket"));
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
				this.logger.error("解析【美食送】基础设置异常,原因:{}", e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}

	public String RequestOrdersToTMS(String params, Mss mss) throws Exception {
		Map<String, Object> productMap = new HashMap<String, Object>();
		try {
			productMap = new ObjectMapper().readValue(params, Map.class);
		} catch (Exception e) {
			this.logger.warn("解析美食送json异常，" + e);
			return this.responseJson("5001", "系统异常	", mss,"");
		}
		if (productMap.isEmpty()) {
			this.logger.info("美食送参数为空,params={}", params);
			return this.responseJson("4002", "请求参数错误", mss,"");
		}
		// 获取签名
		String sign = (String) productMap.get("sign");
		// 删除签名
		productMap.remove("sign");
		String signMSS = SignUtil.getSign(productMap, mss.getSecret_key());
		if (!sign.equals(signMSS)) {
			this.logger.warn("签名错误");
			return this.responseJson("4001", "验签错误", mss,"");
		}
		Map<String, Object> BodyMap = (Map<String, Object>) productMap.get("body");
		List<Map<String, String>> orderlist = this.parseCwbArrByOrderDto(BodyMap, mss);
		if ((orderlist == null) || (orderlist.size() == 0)) {
			this.logger.warn("美食送-请求没有封装参数，订单号可能为空");
			return this.responseJson("4004", "请求参数缺失", mss,"");
		}
		long warehouseid = mss.getWarehouseid(); // 订单导入的库房Id
		this.dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(mss.getCustomerid()), B2cEnum.MSS.getMethod(), orderlist, warehouseid, true);
		this.logger.info("美食送-订单导入成功");
		return this.responseJson("200", "成功", mss,(String)BodyMap.get("partner_order_id"));
	}

	private List<Map<String, String>> parseCwbArrByOrderDto(Map<String, Object> bodyMap, Mss mss) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();
		if (!bodyMap.isEmpty()) {
			CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp((String) bodyMap.get("partner_order_id"));// 商户订单编号（在o3系统中必须唯一，不能重复）
			if (cwbOrder != null) {
				this.logger.warn("获取美食送订单中含有重复数据cwb={}", bodyMap.get("partner_order_id"));
				return null;
			}
		}
		List<Good> goods = (List<Good>) bodyMap.get("goods");
		Map<String, String> cwbMap = new HashMap<String, String>();
		cwbMap.put("cwb", (String) bodyMap.get("partner_order_id"));
		String sendcarname = "";
		String carsize = "";
		int count = 0;
		long price = 0L;
		for (Good good : goods) {
			sendcarname += good.getName() + ",";
			carsize += good.getSpecs() + ",";
			count += good.getQuantity();
			price += good.getPrice().longValue();
		}
		Consignee consignee = (Consignee) bodyMap.get("extra_metas");
		Map<String, Object> extraMap = (Map<String, Object>) bodyMap.get("extra_services");
		Map<String, Object> raMap = (Map<String, Object>) extraMap.get("ra");
		Map<String, Object> paMap = (Map<String, Object>) extraMap.get("pa");
		Map<String, Object> rcMap = (Map<String, Object>) extraMap.get("rc");
		String receipt_amount = (String) raMap.get("receipt_amount");// 代收款金额(单位：分)
		String pay_amount = (String) paMap.get("pay_amount");// 代付款金额(单位：分)
		String recv_code = (String) paMap.get("recv_code");// 提货码（骑士提货时需要提供）
		cwbMap.put("cwbcity", (String) bodyMap.get("city_code"));// 城市代码（详见附录）
		cwbMap.put("sendcarname", sendcarname);
		cwbMap.put("carsize", carsize);
		cwbMap.put("consigneename", consignee.getName());
		cwbMap.put("consigneeaddress", consignee.getAddress());
		cwbMap.put("consigneephone", consignee.getTel());
		cwbMap.put("consigneemobile", consignee.getMobile());
		cwbMap.put("receivablefee", receipt_amount);
		cwbMap.put("cwbremark", recv_code);
		cwbMap.put("remark1", sendcarname + "," + "数量" + count + ",金额" + price);
		cwbList.add(cwbMap);
		return cwbList;
	}

	/**
	 * 排序之后返回美食送
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
		responseMap.put("access_key", mss.getAccess_key());
		responseMap.put("cmd", mss.getCmd());
		responseMap.put("ticket", mss.getDmsCode());
		bodyMap.put("code", code);
		bodyMap.put("message", msg);
		if ("200".equals(code)) {
			dataMap.put("partner_id", mss.getPartner_id());
			dataMap.put("partner_order_id", cwb);
		}
		bodyMap.put("data", dataMap);
		responseMap.put("body", bodyMap);
		responseMap.put("time", time / 1000);
		responseMap.put("sign", SignUtil.getSign(responseMap, mss.getSecret_key()));
		Map<String, Object> response = SignUtil.sortMapByKey(responseMap);
		return SignUtil.toJson(response);
	}

}
