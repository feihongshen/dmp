package cn.explink.pos.unionpay;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.Base64Utils;
import cn.explink.util.MD5.MD5Util;

@Service
public class UnionPayService {
	private Logger logger = LoggerFactory.getLogger(UnionPayService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	JointService jointService;
	@Autowired
	UnionPayServiceMaster unionPayServiceMaster;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;

	public UnionPay getUnionPaySettingMethod(int key) {
		UnionPay UnionPay = null;
		if (jointService.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(jointService.getObjectMethod(key).getJoint_property());
			UnionPay = (UnionPay) JSONObject.toBean(jsonObj, UnionPay.class);
		} else {
			UnionPay = new UnionPay();
		}

		return UnionPay == null ? new UnionPay() : UnionPay;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		UnionPay unionpay = new UnionPay();
		String private_key = StringUtil.nullConvertToEmptyString(request.getParameter("private_key"));
		String request_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));
		int isotherdeliveroper = Integer.parseInt(request.getParameter("isotherdeliveroper"));
		String requestPosUrl = StringUtil.nullConvertToEmptyString(request.getParameter("requestPosUrl"));

		unionpay.setPrivate_key(private_key);
		unionpay.setRequest_url(request_url);
		unionpay.setIsotherdeliveroper(isotherdeliveroper);
		unionpay.setRequestPosUrl(requestPosUrl);
		JSONObject jsonObj = JSONObject.fromObject(unionpay);
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

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 验证是否通过 公共的方法
	 * 
	 * @param jsondata
	 * @param Md5str
	 * @param private_key
	 */
	public boolean ValidateMAC_publicMethod(String Data, String Md5str, String private_key) {
		try {
			String CheckMd5 = MD5Util.md5(Data + private_key).substring(8, 24); // 16位的md5加密
			CheckMd5 = CheckMd5.toLowerCase().trim();
			Md5str = Md5str.toLowerCase().trim();
			if (Md5str.equals(CheckMd5)) {
				return true;
			}
		} catch (Exception e) {
			logger.error("请求UnionPay解密异常", e);
			e.printStackTrace();
		}
		return false;
	}

	public String RespPublicMsg(String code, String exptmsg) {
		logger.info("code={},errDescribe={}", code, exptmsg);
		return "{\"Response\":\"" + code + "\",\"ErrorDescription\":\"" + exptmsg + "\"}";
	}

	/**
	 * 处理unionpay的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */

	public String AnalyzXMLByUnionPay(String Command, String Data, String Md5str, UnionPay unionpay) {
		String returnCode = null;
		if (!ValidateMAC_publicMethod(Data, Md5str, unionpay.getPrivate_key())) {
			logger.error("UnionPay签名验证失败!" + "请求格式:Command=" + Command + ",Data=" + Data + ",Md5str=" + Md5str);
			return RespPublicMsg("01", "请求签名验证失败");
		}

		JSONObject jsondata = ConvertJsonObject(Data);
		if (jsondata == null) {
			return RespPublicMsg("01", "请求报文信息转化JSON格式异常");
		}

		logger.info("获取UnionPay的业务编码={};请求格式={}", Command, jsondata.toString());
		if (Command.equals(UnionPayEnum.Login.getCommand())) { // 派送员登陆
			returnCode = unionPayServiceMaster.getUnionPayService_Login().toLogin(jsondata);
		} else if (Command.equals(UnionPayEnum.LoginOut.getCommand())) { // 登出
			returnCode = unionPayServiceMaster.getUnionPayService_LoginOut().toLoginOut(jsondata);
		} else if (Command.equals(UnionPayEnum.Search.getCommand())) { // 运单查询
			returnCode = unionPayServiceMaster.getUnionPayService_Search().cwbSearch(jsondata);
		} else if (Command.equals(UnionPayEnum.Delivery.getCommand())) { // 运单处理结果报告
			returnCode = unionPayServiceMaster.getUnionPayService_Delivery().deliveryStateFeedback(jsondata);
		}
		return returnCode;

	}

	/**
	 * 字符串转化为json格式
	 * 
	 * @param jsondata
	 * @return
	 */
	protected JSONObject ConvertJsonObject(String jsondata) {
		try {
			String jsonDataInfo = new String(Base64Utils.decryptBASE64(jsondata.replaceAll("@", "+")), "utf-8");
			JSONObject json = JSONObject.fromObject(jsonDataInfo);
			return json;
		} catch (Exception e) {
			logger.error("字符串转化为Json格式发生异常,jsondata=" + jsondata, e);
			e.printStackTrace();
		}
		return null;

	}

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = userDAO.getUsersByUsername(deliver_man);
			if (userlist != null && userlist.size() > 0) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {
			logger.info("POS查询-没有查询到小件员[" + deliver_man + "]");
			e.printStackTrace();
		}
		return deliverid;
	}

	protected long getPodResultIdByCwb(int cwbordertypeid) {
		if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

	public String getDeliveryStateById(long deliverystate) {
		for (DeliveryStateEnum d : DeliveryStateEnum.values()) {
			if (deliverystate == d.getValue()) {
				return d.getText();
			}
		}
		return "未知";
	}

}
