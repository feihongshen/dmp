package cn.explink.pos.etong;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class EtongService {
	private static Logger logger = LoggerFactory.getLogger(EtongService.class);
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

	public Etong getEtong(int key) {
		Etong alipay = new Etong();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (Etong) JSONObject.toBean(jsonObj, Etong.class);
		} else {
			alipay = new Etong();
		}

		return alipay == null ? new Etong() : alipay;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Etong mobile = new Etong();
		String sender_url = StringUtil.nullConvertToEmptyString(request.getParameter("sender_url"));
		String receiver_url = StringUtil.nullConvertToEmptyString(request.getParameter("receiver_url"));
		String express_id = StringUtil.nullConvertToEmptyString(request.getParameter("express_id"));

		mobile.setSender_url(sender_url);
		mobile.setReceiver_url(receiver_url);
		mobile.setExpress_id(express_id);

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

	public DeliveryEtong loadingDeliveryEtongParms(String cwb, String username, String delivermobile, String podresultid, String backreasonid, String leavedreasonid, String receivedfeecash,
			String receivedfeepos, String receivedfeecheque, String remark, String photofile, String operatedate) {
		DeliveryEtong etong = new DeliveryEtong();
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

	public String dealWithDeliveryInfoByEtong(DeliveryEtong det) {
		responseJson resp = new responseJson();
		resp.setCwb(det.getCwb());
		try {
			CwbOrder cwborder = cwbDAO.getCwbByCwb(det.getCwb());
			if (cwborder == null) {
				return "订单号不存在";
			}
			List<User> deliveryList = userDAO.getUserByUsernameAndMobile(det.getDelivermobile());
			if (deliveryList == null || deliveryList.size() == 0 || deliveryList.size() > 1) {
				return "用户名不存在或者电话号码存在重复";
			}
			User deliveryUser = deliveryList.get(0);

			long podresultid = getPodresultid(det, cwborder);

			BigDecimal pos = det.getReceivedfeepos();
			BigDecimal check = det.getReceivedfeecheque();
			BigDecimal cash = det.getReceivedfeecash();
			BigDecimal paybackedfee = BigDecimal.ZERO;
			long backedreasonid = 0;
			long leavedreasonid = 0;

			if (podresultid == DeliveryStateEnum.JuShou.getValue() || podresultid == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				backedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(det.getBackreasonid(), PosEnum.MobileEtong.getKey())).getReasonid();
			} else if (podresultid == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(det.getLeavedreasonid(), PosEnum.MobileEtong.getKey())).getReasonid();
			}

			String deliverremark = "";
			if (pos.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "刷卡支付";
			} else if (check.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "支票支付";
			} else if (cash.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "现金支付";
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
			parameters.put("deliverstateremark", "手机终端反馈-" + deliverremark);
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", deliveryUser.getBranchid());
			parameters.put("sessionuserid", deliveryUser.getUserid());
			parameters.put("sign_typeid", 1);
			parameters.put("sign_man", cwborder.getConsigneename());
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//

			cwbOrderService.deliverStatePod(deliveryUser, cwborder.getCwb(), cwborder.getCwb(), parameters);

			resp.setStatuscode("000000");

		} catch (Exception e) {
			logger.error("物流E通手机端反馈信息发生未知异常", e);
			resp.setStatuscode("100001");
			resp.setErrorinfo(e.getMessage());
		}

		String reponse = "";
		try {
			reponse = jacksonmapper.writeValueAsString(resp);
		} catch (JsonGenerationException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

		return reponse;
	}

	private long getPodresultid(DeliveryEtong det, CwbOrder cwborder) {
		long podresultid = 0;
		String podresultidstr = det.getPodresultid();
		if (podresultidstr.equals("9")) {
			if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				podresultid = DeliveryStateEnum.PeiSongChengGong.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			}

		} else if (podresultidstr.equals("2")) {
			if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				podresultid = DeliveryStateEnum.JuShou.getValue();
			} else if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				podresultid = DeliveryStateEnum.ShangMenJuTui.getValue();
			}
		} else if (podresultidstr.equals("1")) {
			podresultid = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		return podresultid;
	}

}
