package cn.explink.b2c.weisuda;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.core_up.CommonCoreService;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryCashDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;

@Service
public class WeisudaService {
	// private Logger logger = LoggerFactory.getLogger(WeisudaService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	DeliveryCashDAO deliveryCashDAO;

	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbOrderService cwborderService;

	private Logger logger = LoggerFactory.getLogger(CommonCoreService.class);

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = this.jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
		}
		return posValue;
	}

	public Weisuda getWeisudaSettingMethod(int key) {
		Weisuda weisuda = new Weisuda();
		if (!"".equals(this.getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			weisuda = (Weisuda) JSONObject.toBean(jsonObj, Weisuda.class);
		} else {
			weisuda = new Weisuda();
		}

		return weisuda == null ? new Weisuda() : weisuda;
	}

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = this.userDAO.getUsersByUsername(deliver_man);
			if ((userlist != null) && (userlist.size() > 0)) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {

		}
		return deliverid;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Weisuda weisuda = new Weisuda();
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("code"));
		String v = StringUtil.nullConvertToEmptyString(request.getParameter("v"));
		String secret = StringUtil.nullConvertToEmptyString(request.getParameter("secret"));
		String pushOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("pushOrders_URL"));
		String unVerifyOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("UnVerifyOrders_URL"));
		String updateUnVerifyOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("updateUnVerifyOrders_URL"));
		String nums = StringUtil.nullConvertToEmptyString(request.getParameter("nums"));
		String count = StringUtil.nullConvertToEmptyString(request.getParameter("count"));
		String updateOrders_URL = StringUtil.nullConvertToEmptyString(request.getParameter("updateOrders_URL"));
		String siteUpdate_URL = StringUtil.nullConvertToEmptyString(request.getParameter("siteUpdate_URL"));
		String siteDel_URL = StringUtil.nullConvertToEmptyString(request.getParameter("siteDel_URL"));
		String courierUpdate_URL = StringUtil.nullConvertToEmptyString(request.getParameter("courierUpdate_URL"));
		String carrierDel_URL = StringUtil.nullConvertToEmptyString(request.getParameter("carrierDel_URL"));

		weisuda.setCode(code);
		weisuda.setV(v);
		weisuda.setSecret(secret);
		weisuda.setPushOrders_URL(pushOrders_URL);
		weisuda.setUnVerifyOrders_URL(unVerifyOrders_URL);
		weisuda.setUpdateUnVerifyOrders_URL(updateUnVerifyOrders_URL);
		weisuda.setUpdateOrders_URL(updateOrders_URL);
		weisuda.setSiteUpdate_URL(siteUpdate_URL);
		weisuda.setSiteDel_URL(siteDel_URL);
		weisuda.setCourierUpdate_URL(courierUpdate_URL);
		weisuda.setCarrierDel_URL(carrierDel_URL);
		weisuda.setNums(nums);
		weisuda.setCount(count);

		JSONObject jsonObj = JSONObject.fromObject(weisuda);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}

	}

	protected User getUser(long userid) {
		return this.userDAO.getUserByUserid(userid);
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 对接反馈接口
	 * 
	 * @param commd
	 * @param cwb
	 * @param podresultid
	 * @param user
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public void dealwith_fankui(String datajson) throws IOException, JsonParseException, JsonMappingException {

		OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(datajson, OrderFlowDto.class);

		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(orderFlowDto.getCwb());

		BigDecimal pos = BigDecimal.ZERO;
		BigDecimal check = BigDecimal.ZERO;
		BigDecimal cash = BigDecimal.ZERO;
		BigDecimal paybackedfee = BigDecimal.ZERO;

		long podresultid = Long.valueOf(orderFlowDto.getDeliverystate());
		podresultid = this.getPodresultid(podresultid, cwbOrder);

		DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(orderFlowDto.getCwb());

		if (deliverystate == null) {
			this.logger.info("订单{}不满足支付条件，deliverystate表为空", deliverystate);
			return;
		}

		// 撤销
		if (orderFlowDto.getIsCancel() == 1) {
			// //////////////////////////执行撤销方法
			this.cwbOrderService.deliverStatePodCancel(orderFlowDto.getCwb(), deliverystate.getDeliverybranchid(), deliverystate.getDeliveryid(), "运单撤销", 0);
			return;
		}

		if (deliverystate.getIsout() == 1) { // 应退款
			pos = BigDecimal.ZERO;
			cash = BigDecimal.ZERO;
			check = BigDecimal.ZERO;
			if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
					|| (podresultid == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				paybackedfee = deliverystate.getBusinessfee();
			}

		} else if ((podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) || (podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) { // 应收款
			SystemInstall isToCash = this.systemInstallDAO.getSystemInstallByName("isToCash");
			if ((isToCash != null) && isToCash.getValue().equals("yes")) {
				cash = deliverystate.getBusinessfee();
			} else {

				if (orderFlowDto.getPaytype() == PaytypeEnum.Pos.getValue()) {
					pos = deliverystate.getBusinessfee();
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.Xianjin.getValue()) {
					cash = deliverystate.getBusinessfee();
				} else if (orderFlowDto.getPaytype() == PaytypeEnum.Zhipiao.getValue()) {
					check = deliverystate.getBusinessfee();
				} else {

					if (cwbOrder.getPaywayid() == PaytypeEnum.Xianjin.getValue()) {
						cash = deliverystate.getBusinessfee();
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.Pos.getValue()) {
						pos = deliverystate.getBusinessfee();
					} else if (cwbOrder.getPaywayid() == PaytypeEnum.Zhipiao.getValue()) {
						check = deliverystate.getBusinessfee();
					}
				}
			}
		} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

		}

		long backedreasonid = 0;
		long leavedreasonid = 0;

		String deliverstateremark = "系统对接";

		if ((podresultid == DeliveryStateEnum.JuShou.getValue()) || (podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) || (podresultid == DeliveryStateEnum.BuFenTuiHuo.getValue())) {

			backedreasonid = Long.valueOf((orderFlowDto.getExptcode() == null) || orderFlowDto.getExptcode().isEmpty() ? "0" : orderFlowDto.getExptcode());
		}

		long deliverid = deliverystate.getDeliveryid();
		long infactDeliverid = 0;
		try {
			User user = this.userDAO.getUserByUsernameToUpper(orderFlowDto.getDeliveryname());
			infactDeliverid = user.getUserid();
			if ((infactDeliverid != deliverid) && (infactDeliverid != 0)) {
				deliverid = infactDeliverid;
				this.deliveryStateDAO.updateDeliveryidByCwb(deliverid, user.getBranchid(), deliverystate.getCwb());
				this.deliveryCashDAO.saveDeliveryCashByDeliverystateid(deliverid, user.getBranchid(), orderFlowDto.getRequestTime(), deliverystate.getId());
			}
		} catch (Exception e1) {
			this.logger.error("唯速达_02回传username=" + orderFlowDto.getDeliveryname() + "不存在，cwb=" + cwbOrder.getCwb());
			infactDeliverid = 0;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", deliverid);
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", backedreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		parameters.put("receivedfeecash", cash);
		parameters.put("receivedfeepos", pos);
		parameters.put("receivedfeecheque", check);
		parameters.put("receivedfeeother", BigDecimal.ZERO);
		parameters.put("paybackedfee", paybackedfee);
		parameters.put("podremarkid", (long) 0);
		parameters.put("posremark", pos.compareTo(BigDecimal.ZERO) > 0 ? "POS刷卡" : "");
		parameters.put("checkremark", check.compareTo(BigDecimal.ZERO) > 0 ? "支票支付" : "");
		parameters.put("deliverstateremark", deliverstateremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", deliverystate.getDeliverybranchid());
		parameters.put("sessionuserid", deliverystate.getDeliveryid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", orderFlowDto.getConsignee());
		parameters.put("sign_time", orderFlowDto.getRequestTime());

		// String oldcwbremark = cwbOrder.getCwbremark().length() > 0 ?
		// cwbOrder.getCwbremark() + "\n" : "";
		String newcwbremark = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + orderFlowDto.getCwbremark();
		try {
			this.cwbDAO.updateCwbRemark(orderFlowDto.getCwb(), newcwbremark);
			cwbOrder.setCwbremark(newcwbremark);

		} catch (Exception e) {
			this.logger.error("error while saveing cwbremark,cwb:" + cwbOrder.getCwb() + "cwbremark:" + newcwbremark, e);
			throw new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.Bei_Zhu_Tai_Chang);
		}
		parameters.put("nosysyemflag", "1");//

		if ((orderFlowDto.getExptmsg() != null) && !orderFlowDto.getExptmsg().isEmpty()) {
			this.cwbDAO.saveCwbForBackreason(orderFlowDto.getCwb(), orderFlowDto.getExptmsg(), 0);
		}
		if ((orderFlowDto.getStrandedrReason() != null) && !orderFlowDto.getStrandedrReason().isEmpty()) {
			this.cwbDAO.saveCwbForLeavereason(orderFlowDto.getCwb(), orderFlowDto.getStrandedrReason(), 0);
		}

		User user = this.userDAO.getAllUserByid(deliverid);

		this.cwborderService.deliverStatePod(user, orderFlowDto.getCwb(), orderFlowDto.getCwb(), parameters);

	}

	private long getPodresultid(long podresultid, CwbOrder cwbOrder) {
		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			} else if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			}

		} else if (podresultid == DeliveryStateEnum.JuShou.getValue()) {
			if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenJuTui.getValue();
			}
		} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			podresultid = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		return podresultid;
	}

}