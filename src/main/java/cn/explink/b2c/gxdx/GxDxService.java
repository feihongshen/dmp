/**
 * 
 */
package cn.explink.b2c.gxdx;

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

import cn.explink.b2c.gxdx.xmldto.RequestDto;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.StringUtil;

/**
 * @ClassName: GxDxService
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月10日下午2:30:43
 */
@Service
public class GxDxService {

	private Logger logger = LoggerFactory.getLogger(GxDxService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	CustomerDAO customerDAO;

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public GxDx getGxDx(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		GxDx gxdx = (GxDx) JSONObject.toBean(jsonObj, GxDx.class);
		return gxdx;
	}

	private Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public void edit(HttpServletRequest request, int joint_num) {
		GxDx dms = new GxDx();
		String private_key = request.getParameter("private_key");
		String maxCountstr = request.getParameter("maxCount");
		String logisticProviderID = request.getParameter("logisticProviderID");
		String customerid = request.getParameter("customerid");
		String requestUrl = request.getParameter("requestUrl");
		String exportbranchid = request.getParameter("exportbranchid");
		dms.setPrivate_key(StringUtil.nullConvertToEmptyString(private_key));
		dms.setRequestUrl(StringUtil.nullConvertToEmptyString(requestUrl));
		dms.setCustomerid(StringUtil.nullConvertToEmptyString(customerid));
		dms.setMaxCount(Integer.parseInt(StringUtil.nullConvertToEmptyString(maxCountstr)));
		dms.setExportbranchid(Long.parseLong(StringUtil.nullConvertToEmptyString(exportbranchid)));
		dms.setLogisticProviderID(StringUtil.nullConvertToEmptyString(logisticProviderID));
		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, "", joint_num);
	}


	/**
	 * @param logicdata
	 * @param checkdata
	 * @param gxdx
	 * @return
	 */
	public String orderDetailExportInterface(RequestDto request, GxDx gxdx) {
		String cwb = "";
		try {
			List<Map<String, String>> xmllist = getOrderDetailParms(request);

			cwb = xmllist.get(0).get("cwb").toString();

			List<CwbOrderDTO> cwbdto = dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(gxdx.getCustomerid()), B2cEnum.GuangXinDianXin.getMethod(), xmllist, gxdx.getExportbranchid(), true);
			if(cwbdto == null){
				return responseXml(request.getWaybillNo(), request.getLogisticProviderID(), "False", "失败");
			}
			logger.info("处理广信电信导入后的订单信息成功,cwb={}", cwb);

			return responseXml(request.getWaybillNo(), request.getLogisticProviderID(), "True", "成功");

		} catch (Exception e) {
			logger.error("广信电信请求接口遇到未知异常" + e.getMessage(), e);
			return responseXml(request.getWaybillNo(), request.getLogisticProviderID(), "False", e.getMessage());
		}
	}

	public String responseXml(String cwb, String logisticProviderID, String flag, String remark) {
		String xmlString = " <ResponseWorkOrder> " + "<WaybillNo>" + cwb + "</WaybillNo> " + " <LogisticProviderID>" + logisticProviderID + "</LogisticProviderID> " + "<Success>" + flag
				+ "</Success>" + "<Remark>" + remark + "</Remark>" + "</ResponseWorkOrder>";
		logger.info("返回广信电信-xml={}", xmlString);

		return xmlString;
	}

	private List<Map<String, String>> getOrderDetailParms(RequestDto request) {
		List<Map<String, String>> xmllist = new ArrayList<Map<String, String>>();
		Map<String, String> xmlMap = new HashMap<String, String>();

		xmlMap.put("cwb", request.getWaybillNo());// cwb
		xmlMap.put("transcwb", request.getClientCode());// transcwb
		
		xmlMap.put("remark1", request.getLogisticProviderID());// 物流公司编号
		xmlMap.put("receivablefee", request.getReplCost().toString());// 代收款,没有写0，负数表示应退金额，正数表示应收金额
		xmlMap.put("consigneename", request.getGetPerson());// 收货人
		xmlMap.put("cwbcity", request.getGetCity());// 收货城市
		xmlMap.put("consigneeaddress", request.getGetAddress());// 收货地址
		xmlMap.put("consigneephone", request.getGetMobile());// 收货人手机
		//xmlMap.put("consigneephone", request.getGetTel());// 收货人座机
		xmlMap.put("caramount", request.getGoodsValue().toString());// 货物金额
		String cwbordertypeid = request.getWorkType();
		if("T1".equals(cwbordertypeid)){
			cwbordertypeid = String.valueOf(CwbOrderTypeIdEnum.Shangmenhuan.getValue());
		}else if("T2".equals(cwbordertypeid)){
			cwbordertypeid = String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue());
		}else{
			cwbordertypeid = String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue());
		}
		xmlMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));// T0:普通T1：换货,T2:取货
		xmlMap.put("sendcarnum", String.valueOf(request.getGoodsNum()));// 总件数
		xmlMap.put("cargorealweight", request.getGoodsHav().toString());// 总重量(KG)
		xmlMap.put("paywayid", "1");
		xmlMap.put("customercommand",(request.getHoliday()==0?"节假日可派送":"工作日派送"));// 物流公司编号
		xmlMap.put("consigneepostcode", request.getGetZipCode());// 邮编
		
		
		
		xmllist.add(xmlMap);
		return xmllist;
	}

	public String getParamsString(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "";
	}
	
	
}
