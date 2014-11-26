package cn.explink.pos.mobileapp_dcb;

import java.math.BigDecimal;
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

import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class MobiledcbService {
	private Logger logger = LoggerFactory.getLogger(MobiledcbService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return posValue;
	}

	public Mobiledcb getMobiledcb(int key) {
		Mobiledcb alipay = new Mobiledcb();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (Mobiledcb) JSONObject.toBean(jsonObj, Mobiledcb.class);
		} else {
			alipay = new Mobiledcb();
		}

		return alipay == null ? new Mobiledcb() : alipay;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Mobiledcb mobile = new Mobiledcb();
		String sender_url = StringUtil.nullConvertToEmptyString(request.getParameter("sender_url"));
		String receiver_url = StringUtil.nullConvertToEmptyString(request.getParameter("receiver_url"));
		String sender_usersyn = StringUtil.nullConvertToEmptyString(request.getParameter("sender_usersyn"));

		mobile.setSender_url(sender_url);
		mobile.setSender_usersyn(sender_usersyn);
		mobile.setReceiver_url(receiver_url);

		JSONObject jsonObj = JSONObject.fromObject(mobile);
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
	}

	public DeliveryDcb loadingDeliveryEtongParms(HttpServletRequest request) {

		DeliveryDcb etong = new DeliveryDcb();

		String cwb = request.getParameter("cwb");
		String username = request.getParameter("username");// 此字段待确认
		String delivermobile = request.getParameter("delivermobile");
		String podresultid = request.getParameter("podresultid");
		String backreasonid = request.getParameter("backreasonid") == null ? "0" : request.getParameter("backreasonid");
		String leavedreasonid = request.getParameter("leavedreasonid") == null ? "0" : request.getParameter("leavedreasonid");
		String receivedfeecash = request.getParameter("receivedfeecash") == null || request.getParameter("receivedfeecash").isEmpty() ? "0" : request.getParameter("receivedfeecash");
		String receivedfeepos = request.getParameter("receivedfeepos") == null || request.getParameter("receivedfeepos").isEmpty() ? "0" : request.getParameter("receivedfeepos");
		String receivedfeecheque = request.getParameter("receivedfeecheque") == null || request.getParameter("receivedfeecheque").isEmpty() ? "0" : request.getParameter("receivedfeecheque");
		String remark = request.getParameter("remark") == null ? "" : request.getParameter("remark");
		String photofile = request.getParameter("photofile");// 签收图片文件
		String operatedate = request.getParameter("operatedate");// 反馈时间

		etong.setDelivermobile(delivermobile);
		etong.setPodresultid(podresultid);
		etong.setBackreasonid(backreasonid);
		etong.setLeavedreasonid(leavedreasonid);
		etong.setReceivedfeecash(BigDecimal.valueOf(Double.valueOf(receivedfeecash)));
		etong.setReceivedfeepos(BigDecimal.valueOf(Double.valueOf(receivedfeepos)));
		etong.setReceivedfeecheque(BigDecimal.valueOf(Double.valueOf(receivedfeecheque)));
		etong.setRemark(remark);
		etong.setPhotofile(photofile);
		etong.setOperatedate(operatedate);
		etong.setUsername(username);
		etong.setCwb(cwb);

		return etong;
	}

	//
	// public String dealwithmobileApp_feedback(DeliveryDcb det) throws
	// JsonGenerationException, JsonMappingException, IOException {
	//
	// return jacksonmapper.writeValueAsString(dealwithmobileApp_feedback(det));
	// //返回一个处理结果
	// }

	/**
	 * 处理和验证参数是否正确，并返回一个结果标识
	 * 
	 * @param det
	 * @param json
	 * @return
	 */
	public ResponseJson dealwithmobileApp_feedback(DeliveryDcb det) {
		ResponseJson json = new ResponseJson();
		// 验证参数
		try {
			ValidateRequestParams(det.getCwb(), det.getUsername(), det.getDelivermobile(), det.getPodresultid());
		} catch (RuntimeException e1) {
			json.setStatuscode(DcbcodeEnum.SYSTEMERROR.getCode());
			json.setErrorinfo(e1.getMessage());
			logger.error("参数验证错误" + det.getCwb(), e1);
			return json;
		}
		json.setCwb(det.getCwb());

		List<User> deliveryList = userDAO.getUserByUsernameAndMobile(det.getDelivermobile());
		if (deliveryList == null || deliveryList.size() == 0) {
			json.setStatuscode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
			json.setErrorinfo("手机号不存在" + det.getDelivermobile());
			return json;
		}

		// if(deliveryList.size()>1){
		// json.setStatuscode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
		// json.setErrorinfo("手机号存在重复"+det.getDelivermobile());
		// return json;
		// }

		User user = userDAO.getUserByUsername(det.getUsername());
		if (user == null) {
			json.setStatuscode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
			json.setErrorinfo("用户不存在" + det.getDelivermobile());
			return json;
		}

		CwbOrder cwborder = cwbDAO.getCwbByCwb(det.getCwb());

		if (cwborder == null) {
			json.setStatuscode(DcbcodeEnum.CWBNOTFOUNT.getCode());
			json.setErrorinfo(DcbcodeEnum.CWBNOTFOUNT.getText());
			return json;
		}

		User deliveryUser = deliveryList.get(0);
		if (deliveryUser.getUserid() != cwborder.getDeliverid()) {
			json.setStatuscode(DcbcodeEnum.SYSTEMERROR.getCode());
			json.setErrorinfo("不是同一个操作人");
			return json;
		}

		// 处理订单
		try {

			excuteFeedback(det, cwborder, deliveryUser);

			json.setStatuscode(DcbcodeEnum.SUCCESS.getCode());
			json.setErrorinfo(DcbcodeEnum.SUCCESS.getText());
			return json;
		} catch (Exception e) {
			json.setStatuscode(DcbcodeEnum.SYSTEMERROR.getCode());
			json.setErrorinfo("处理异常:" + e.getMessage());
			logger.error("处理异常" + det.getCwb(), e);
			return json;
		}

	}

	private void excuteFeedback(DeliveryDcb det, CwbOrder cwborder, User deliveryUser) {

		long podresultid = getPodresultid(det, cwborder);

		BigDecimal pos = det.getReceivedfeepos();
		BigDecimal check = det.getReceivedfeecheque();
		BigDecimal cash = det.getReceivedfeecash();

		BigDecimal totalAmount = pos.add(check).add(cash);

		if (podresultid == DeliveryStateEnum.PeiSongChengGong.getValue() || podresultid == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			if (totalAmount.compareTo(cwborder.getReceivablefee()) != 0) {
				throw new RuntimeException("实收金额有误");
			}
		}

		if (det.getPodresultid().equals(DcbPodresultEnum.JuShou.getCode()) || det.getPodresultid().equals(DcbPodresultEnum.ZhiLiu.getCode())) {
			pos = BigDecimal.ZERO;
			check = BigDecimal.ZERO;
			cash = BigDecimal.ZERO;
		}

		BigDecimal paybackedfee = BigDecimal.ZERO;
		long backedreasonid = 0;
		long leavedreasonid = 0;

		if (podresultid == DeliveryStateEnum.JuShou.getValue() || podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			backedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(det.getBackreasonid(), PosEnum.MobileApp_dcb.getKey())).getReasonid();
		} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(det.getLeavedreasonid(), PosEnum.MobileApp_dcb.getKey())).getReasonid();
		}

		Reason reason = reasonDAO.getReasonByReasonid(backedreasonid == 0 ? leavedreasonid : backedreasonid);
		if (reason == null) {
			backedreasonid = 0;
			leavedreasonid = 0;
		}

		String deliverremark = "";
		if (pos.compareTo(BigDecimal.ZERO) > 0) {
			deliverremark = "-刷卡支付";
		} else if (check.compareTo(BigDecimal.ZERO) > 0) {
			deliverremark = "-支票支付";
		} else if (cash.compareTo(BigDecimal.ZERO) > 0) {
			deliverremark = "-现金支付";
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", deliveryUser.getUserid());
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
		parameters.put("deliverstateremark", "手机终端反馈" + deliverremark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", deliveryUser.getBranchid());
		parameters.put("sessionuserid", deliveryUser.getUserid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", cwborder.getConsigneename());
		parameters.put("sign_time", DateTimeUtil.getNowTime());
		parameters.put("nosysyemflag", "1");//

		cwbOrderService.deliverStatePod(deliveryUser, cwborder.getCwb(), cwborder.getCwb(), parameters);
	}

	/**
	 * 校验必备的参数
	 * 
	 * @param cwb
	 * @param username
	 * @param delivermobile
	 * @param podresultid
	 */
	private void ValidateRequestParams(String cwb, String username, String delivermobile, String podresultid) {
		if (cwb == null || cwb.isEmpty()) {
			throw new RuntimeException("订单号不能为空");
		}
		if (username == null || username.isEmpty()) {
			throw new RuntimeException("username不能为空");
		}
		if (delivermobile == null || delivermobile.isEmpty()) {
			throw new RuntimeException("delivermobile不能为空");
		}
		if (podresultid == null || podresultid.isEmpty()) {
			throw new RuntimeException("podresultid不能为空");
		}
	}

	private long getPodresultid(DeliveryDcb det, CwbOrder cwborder) {
		long podresultid = 0;
		String podresultidstr = det.getPodresultid();
		if (podresultidstr.equals(DcbPodresultEnum.PeisongChengGong.getCode())) {
			if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				podresultid = DeliveryStateEnum.PeiSongChengGong.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			}

		} else if (podresultidstr.equals(DcbPodresultEnum.JuShou.getCode())) {
			if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenJuTui.getValue();
			}
		} else if (podresultidstr.equals(DcbPodresultEnum.ZhiLiu.getCode())) {
			podresultid = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		return podresultid;
	}

	/**
	 * 处理和验证参数是否正确，并返回一个结果标识
	 * 
	 * @param det
	 * @param json
	 * @return
	 */
	public ResponseJsonSearch dealwithmobileApp_cwbSearch(String cwb, String username, String delivermobile) {
		ResponseJsonSearch json = new ResponseJsonSearch();
		// 验证参数
		try {
			ValidateRequestParams(cwb, username, delivermobile, "留空");
		} catch (RuntimeException e1) {
			json.setErrcode(DcbcodeEnum.SYSTEMERROR.getCode());
			json.setErrmsg(e1.getMessage());
			logger.error("参数验证错误" + cwb, e1);
			return json;
		}
		json.setCwb(cwb);

		List<User> deliveryList = userDAO.getUserByUsernameAndMobile(delivermobile);
		if (deliveryList == null || deliveryList.size() == 0) {
			json.setErrcode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
			json.setErrmsg("手机号不存在" + delivermobile);
			return json;
		}

		// if(deliveryList.size()>1){
		// json.setErrcode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
		// json.setErrmsg("手机号存在重复"+delivermobile);
		// return json;
		// }
		//
		User user = userDAO.getUserByUsername(username);
		if (user == null) {
			json.setErrcode(DcbcodeEnum.YONGHUBUCUNZAI.getCode());
			json.setErrmsg("用户不存在" + username);
			return json;
		}

		CwbOrder cwborder = cwbDAO.getCwbByCwb(cwb);

		if (cwborder == null) {
			json.setErrcode(DcbcodeEnum.CWBNOTFOUNT.getCode());
			json.setErrmsg(DcbcodeEnum.CWBNOTFOUNT.getText());
			return json;
		}

		json.setErrcode(DcbcodeEnum.SUCCESS.getCode());
		json.setErrmsg(DcbcodeEnum.SUCCESS.getText());

		DeliveryState deliverState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		long deliverystate = deliverState.getDeliverystate();
		String cwbstatus = "";
		if (deliverystate == 0) {
			cwbstatus = "DELIVING";
		} else if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			cwbstatus = "SIGN";
		} else if (deliverystate == DeliveryStateEnum.JuShou.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			cwbstatus = "REFUSE";
		} else if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			cwbstatus = "RETENTION";
		}
		json.setCwbstatus(cwbstatus);
		json.setCwbremark(cwborder.getCustomercommand());

		return json;

	}

}
