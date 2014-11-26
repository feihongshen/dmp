package cn.explink.b2c.jumei;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkFlowEnum;
import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class JuMeiYouPinService {
	private Logger logger = LoggerFactory.getLogger(JuMeiYouPinService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	DeliveryStateDAO deliverystateDao;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;

	private final String XMLNoteStart = "<orderresponse>";
	private final String XMLNoteEnd = "</orderresponse>";

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public JuMeiYouPin getJuMeiYouPin(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		JuMeiYouPin jumeiyoupin = (JuMeiYouPin) JSONObject.toBean(jsonObj, JuMeiYouPin.class);
		return jumeiyoupin;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		JuMeiYouPin jumeiyoupin = new JuMeiYouPin();
		jumeiyoupin.setExpress_id(request.getParameter("express_id"));
		jumeiyoupin.setCount(Integer.parseInt(request.getParameter("count")));
		jumeiyoupin.setCustomerids(request.getParameter("customerids"));
		jumeiyoupin.setOwn_url(request.getParameter("own_url"));
		jumeiyoupin.setPrivate_key(request.getParameter("private_key"));
		jumeiyoupin.setTuisong_url(request.getParameter("tuisong_url"));
		jumeiyoupin.setDescirbe(request.getParameter("descirbe"));

		JSONObject jsonObj = JSONObject.fromObject(jumeiyoupin);
		String customerids = request.getParameter("customerids");
		String oldCustomerids = "";
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getJuMeiYouPin(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 聚美优品请求接口
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String requestMethod(JuMeiYouPin jumei, String XMLStr, String cwbstr, String unixstamp, String sign) {
		String jumeiParmStr = "cwb=" + cwbstr + ",unixstamp=" + unixstamp + ",sign=" + sign;
		logger.info("聚美-客户备注处理-请求参数:{},XML={}", jumeiParmStr, XMLStr);
		String MD5Data = MD5Util.md5(MD5Util.md5(cwbstr + unixstamp) + jumei.getPrivate_key());
		Map<String, Object> xmlMap = null;
		StringBuffer sub = new StringBuffer(XMLNoteStart);
		try {
			xmlMap = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(XMLStr);
		} catch (Exception e) {
			logger.error("聚美-客户备注处理:解析XML异常!" + e);
			return responseExptXML(sub, "", jumei.getExpress_id(), JuMeiExptMessageEnum.IllegalXMLFormat).append(XMLNoteEnd).toString();
		}
		try {
			ValidateSystemExpt(jumei, sign, MD5Data, xmlMap, sub);
		} catch (JuMeiException e) {
			logger.error("聚美-客户备注处理:系统错误信息!" + e.getError());
			return responseExptXML(sub, "", jumei.getExpress_id(), e.getError()).append(XMLNoteEnd).toString();
		}

		List<Map<String, Object>> xmlList = (List<Map<String, Object>>) xmlMap.get("orderlist");
		for (Map<String, Object> dataMap : xmlList) {
			int expt_flag = 0; // 标识是否出现异常，0,无异常，1有异常
			String cwb = dataMap.get("cwb").toString();
			String trackdatetime = dataMap.get("trackdatetime").toString();
			String trackstatus = dataMap.get("trackstatus").toString();
			String cs_note = "聚美优品客服备注：[" + dataMap.get("cs_note").toString() + "]";
			String update_time = dataMap.get("update_time").toString();
			String trackstatusArr[] = trackstatus.split(",");

			try {
				ValidateBusinessLogic(cwb, trackstatus, trackstatusArr);
			} catch (JuMeiException e) {
				logger.error("聚美-客户备注处理:业务逻辑验证错误!处理单号cwb=" + cwb + "Exception:" + e.getError());
				sub.append(responseExptXML(sub, cwb, jumei.getExpress_id(), e.getError()));
				expt_flag = 1;
			}
			if (expt_flag == 0) {
				sub.append(responseSuccessXML(cwb, jumei.getExpress_id()));
				cwbOrderService.updateCwbRemark(cwb, cs_note);
				logger.info("聚美-客户备注处理:数据处理成功!cwb={},操作时间={},客服修改时间=" + update_time, cwb, trackdatetime);
			}
		}

		sub.append(XMLNoteEnd);
		return sub.toString();
	}

	/**
	 * 验证业务逻辑
	 * 
	 * @param cwb
	 * @param trackstatus
	 * @param trackstatusArr
	 */
	private void ValidateBusinessLogic(String cwb, String trackstatus, String[] trackstatusArr) {
		if (getOccurStr(trackstatus, ",") != 2) {
			throw new JuMeiException(JuMeiExptMessageEnum.R1);
		}
		if (!statusCodeFlag(trackstatusArr[0])) {
			throw new JuMeiException(JuMeiExptMessageEnum.R7);
		}
		if (cwbDAO.getCwbByCwb(cwb) == null) {
			throw new JuMeiException(JuMeiExptMessageEnum.R5);
		}
	}

	/**
	 * 验证是否系统出现错误
	 * 
	 * @param jumei
	 * @param sign
	 * @param MD5Data
	 * @param xmlMap
	 * @param sub
	 */
	private void ValidateSystemExpt(JuMeiYouPin jumei, String sign, String MD5Data, Map<String, Object> xmlMap, StringBuffer sub) {
		String company = xmlMap.get("company").toString(); // 公司名称
		if (!MD5Data.equals(sign)) {
			throw new JuMeiException(JuMeiExptMessageEnum.IllegalSign);
		}
		if (xmlMap == null || xmlMap.size() == 0) {
			throw new JuMeiException(JuMeiExptMessageEnum.IllegalXMLFormat);
		}
		if (!company.equals(jumei.getExpress_id())) {
			throw new JuMeiException(JuMeiExptMessageEnum.IllegalCompany);
		}
	}

	/**
	 * 获取逗号的个数
	 * 
	 * @param src
	 * @param find
	 * @return
	 */
	public int getOccurStr(String src, String find) {
		int o = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++o;
		}
		return o;
	}

	/**
	 * 聚美优品反馈类型
	 */
	public boolean statusCodeFlag(String statuscode) {
		String str = statuscode.trim();
		if ("deferred".equals(str)) {
			return true;
		}
		if ("rejected".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 返回错误验证 Xml的信息
	 * 
	 * @param cwb
	 * @param company
	 * @param result
	 * @param reason
	 * @return
	 */
	private StringBuffer responseExptXML(StringBuffer sub, String cwb, String company, JuMeiExptMessageEnum jumeiExpt) {
		sub.append("<cwb>" + cwb + "</cwb>");
		sub.append("<company>" + company + "</company>");
		sub.append("<success>False</success>");
		sub.append("<reason>" + jumeiExpt.getResp_code() + "</reason>");
		return sub;
	}

	/**
	 * 返回成功的XMl信息
	 * 
	 * @param cwb
	 * @param company
	 * @return
	 */
	private String responseSuccessXML(String cwb, String company) {
		StringBuffer sub = new StringBuffer();
		sub.append("<cwb>" + cwb + "</cwb>");
		sub.append("<company>" + company + "</company>");
		sub.append("<success>true</success>");
		return sub.toString();
	}

	/**
	 * 聚美订单查询接口
	 */
	public String requestMehodCwbSearch(JuMeiYouPin jumei, String cwbs) {

		try {
			ValidatorBusinessMethod(cwbs, jumei); // 验证基本参数
			StringBuffer responesXML = new StringBuffer();
			responesXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			responesXML.append("<Result>");
			int j = 0;
			for (String cwb : cwbs.split(",")) {
				List<OrderFlow> list = orderFlowDAO.getOrderFlowByCwb(cwb);
				j++;
				int m = 0;
				for (int i = 0, k = list.size(); i < list.size(); i++) {
					OrderFlow orderflow = list.get(i);
					m++;
					if (getExplinkFlowEnum(orderflow.getFlowordertype()) == null) {
						if (i > 0) {
							m = m - 1;
						}
						continue;
					}
					String trackevent = explinkService.getDetail(orderflow);
					if (orderflow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
						trackevent = trackevent + jumei.getDescirbe();
					}

					responesXML.append("<row resultcount=\"" + (j == 1 ? m : (j - 1) * k + m) + "\">");
					responesXML.append("<cwb>" + orderflow.getCwb() + "</cwb>");// <!--订单号
																				// -->
					responesXML.append("<trackdatetime>" + DateTimeUtil.formatDate(orderflow.getCredate()) + "</trackdatetime>");// <!--操作时间
																																	// -->
					responesXML.append("<branchname>" + branchDAO.getBranchByBranchid(orderflow.getBranchid()).getBranchname() + "</branchname>");// <!--站点
																																					// -->
					responesXML.append("<trackevent>" + (trackevent) + "</trackevent>");// <!--跟踪信息
																						// -->
					responesXML.append("<podresultname>" + getFlowOrderTextById(orderflow.getFlowordertype(), orderflow.getCwb()) + "</podresultname>");// <!--订单状态
																																						// -->
					responesXML.append("</row>");
				}
			}

			responesXML.append("</Result>");
			return responesXML.toString();

		} catch (Exception e) {
			logger.error("处理[聚美]请求发生未知异常" + e.getMessage(), e);
			return respExptMsg(e.getMessage());
		}

	}

	public String getExplinkFlowEnum(long flowordertype) {

		for (ExplinkFlowEnum dd : ExplinkFlowEnum.values()) {
			if (dd.getStatuscode().equals(flowordertype + "") && flowordertype != FlowOrderTypeEnum.YiFanKui.getValue() && flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				return dd.getStatuscode();
			}
			if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) { // 反馈不让推送
				return null;
			}
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				return "有结果";
			}
		}

		return null;

	}

	private void ValidatorBusinessMethod(String cwbs, JuMeiYouPin jumei) {
		if (cwbs == null) {
			throw new RuntimeException("缺少参数cwb");
		}
		if (cwbs.split(",").length > jumei.getCount()) {
			throw new RuntimeException("订单数不能超过" + jumei.getCount() + "个");
		}

	}

	private String getFlowOrderTextById(int flowordertype, String cwb) {
		for (FlowOrderTypeEnum e : FlowOrderTypeEnum.values()) {
			if (e.getValue() == flowordertype) {
				if (e.getValue() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					return getYiFanKuiStrings(cwb);
				}
				return e.getText();
			}

		}
		return "";

	}

	private String getYiFanKuiStrings(String cwb) {
		long deliverystate = deliverystateDao.getActiveDeliveryStateByCwb(cwb).getDeliverystate();
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return "妥投";
		}
		if (deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return "货物丢失";
		}

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		ExptReason exptreason = getExptReasonByJuMei(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "");

		if (deliverystate == DeliveryStateEnum.JuShou.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return "拒收" + "," + exptreason.getExpt_code() + "," + exptreason.getExpt_msg();
		}
		if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return "滞留" + "," + exptreason.getExpt_code() + "," + exptreason.getExpt_msg();
		}

		return "未知";
	}

	/**
	 * 根据B2CId查询对应的 异常信息
	 * 
	 * @param reasontype
	 * @param leavedreasonid
	 * @param backreasonid
	 * @param customerids
	 * @return
	 */
	public ExptReason getExptReasonByJuMei(long leavedreasonid, long backreasonid, String customerids) {
		ExptReason exptreason = null;
		if (leavedreasonid != 0) { // 滞留
			exptreason = getExptCodeJointByJuMeiKey(leavedreasonid, ReasonTypeEnum.BeHelpUp.getValue(), customerids);
		} else if (backreasonid != 0) { // 拒收
			exptreason = getExptCodeJointByJuMeiKey(backreasonid, ReasonTypeEnum.ReturnGoods.getValue(), customerids);
		}

		return exptreason == null ? new ExptReason() : exptreason;
	}

	public ExptReason getExptCodeJointByJuMeiKey(long reasonid, int expt_type, String customerids) {
		ExptReason exptReason = new ExptReason();
		String expt_code = "";
		String expt_msg = "";
		if (expt_type == 2) { // 默认
			expt_code = "17";
			expt_msg = "其他原因";
		} else if (expt_type == 3) {// 默认
			expt_code = "27";
			expt_msg = "其他原因";
		}
		try {
			JSONObject jparm = JSONObject.fromObject(exptCodeJointDAO.getExpMatchByKey(reasonid, customerids, expt_type));
			exptReason.setExpt_code(jparm.get("expt_code") != null && !"".equals(jparm.getString("expt_code")) ? jparm.getString("expt_code") : expt_code);
			exptReason.setExpt_msg(jparm.get("expt_msg") != null && !"".equals(jparm.getString("expt_msg")) ? jparm.getString("expt_msg") : expt_msg);
		} catch (Exception e) {
			logger.error("获取指定getExptCodeJointByKey异常");
		}
		return exptReason == null ? new ExptReason() : exptReason;
	}

	public String respExptMsg(String msg) {
		return "<Result><msg>" + msg + "</msg></Result>";
	}

	public static void main(String[] args) {

		String aa = "1,2";
		int j = 0;
		for (String a : aa.split(",")) {
			j++;
			for (int i = 1; i <= 10; i++) {
				System.out.println(j == 1 ? i : (j - 1) * 10 + i);
			}
		}

	}

}
