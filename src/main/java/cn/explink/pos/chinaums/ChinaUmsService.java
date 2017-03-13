package cn.explink.pos.chinaums;

import java.io.UnsupportedEncodingException;
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

import cn.explink.b2c.dangdang.DangDang;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.chinaums.xml.ChinaumsUnmarchal;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService.class);
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
	ChinaUmsServiceMaster chinaUmsServiceMaster;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ChinaUmsService_public chinaUmsService_public;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();
	/**
	 * 通过key获取POS对接中的参数
	 * @param key
	 * @return
	 */
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
	/**
	 * 将获取的JSON数据装换成ChinaUms
	 * @param key
	 * @return
	 */
	public ChinaUms getChinaUmsSettingMethod(int key) {
		ChinaUms chinaUms = new ChinaUms();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			chinaUms = (ChinaUms) JSONObject.toBean(jsonObj, ChinaUms.class);
		} else {
			chinaUms = new ChinaUms();
		}

		return chinaUms == null ? new ChinaUms() : chinaUms;
	}

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = userDAO.getUsersByUsername(deliver_man);
			if (userlist != null && userlist.size() > 0) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {

		}
		return deliverid;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		ChinaUms chinaums = new ChinaUms();
		String private_key = StringUtil.nullConvertToEmptyString(request.getParameter("private_key"));
		String request_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));
		int isotherdeliveroper = Integer.parseInt(request.getParameter("isotherdeliveroper"));

		String mer_id = StringUtil.nullConvertToEmptyString(request.getParameter("mer_id")); // 商户号
		
		String forward_url = StringUtil.nullConvertToEmptyString(request.getParameter("forward_url"));
		
		String isforward = StringUtil.nullConvertToEmptyString(request.getParameter("isforward"));
		int isfW = Integer.parseInt(("".equals(isforward))?"0":isforward);//新加（是否允许转发）---LX 1允许，0禁止

		String version= request.getParameter("version");
		
		
		
		
		chinaums.setPrivate_key(private_key);
		chinaums.setRequest_url(request_url);
		chinaums.setIsotherdeliveroper(isotherdeliveroper);
		chinaums.setMer_id(mer_id);
		chinaums.setForward_url(forward_url);
		chinaums.setIsForward(isfW);//是否允许转发----LX
		chinaums.setVersion(Integer.valueOf(version));
		chinaums.setIsAutoSupplementaryProcess(Integer.valueOf(request.getParameter("isAutoSupplementaryProcess")));
		
		JSONObject jsonObj = JSONObject.fromObject(chinaums);
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

	protected User getUser(long userid) {
		return userDAO.getUserByUserid(userid);
	}

	/**
	 * 处理chinaums的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */
	public String AnalyzXMLByChinaUms(ChinaUms chinaUms, String xmlstr) {
		if (xmlstr != null && !"".equals(xmlstr)) {
			try {
				Transaction rootnote = ChinaumsUnmarchal.Unmarchal(xmlstr);
				if (!ValidateMAC_publicMethod(rootnote, chinaUms, xmlstr)) {
					logger.error("chinaums请求签名验证失败!");
					rootnote.getTransaction_Header().setResponse_code(ChinaUmsExptMessageEnum.SignValidateFailed.getResp_code());
					rootnote.getTransaction_Header().setResponse_msg(ChinaUmsExptMessageEnum.SignValidateFailed.getResp_msg());
					return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
				}
				return DealWithchinaumsInterface(chinaUms, xmlstr, rootnote);
			} catch (Exception e) {
				this.logger.error("", e);
				return "Convert XML to Transaction Bean Exception! reason=" + e;
			}

		} else {
			return "chinaums request XML is null.";
		}

	}

	/**
	 * chinaums请求的接口 判断
	 * 
	 * @param chinaUms
	 * @param xmlstr
	 * @param rootnote
	 * @return
	 */
	private String DealWithchinaumsInterface(ChinaUms chinaUms, String xmlstr, Transaction rootnote) {
		String transaction_id = rootnote.getTransaction_Header().getTranstype();
		String cwb = "";
		try {
			if (ChinaUmsEnum.Delivery.getCommand().equals(transaction_id)
					||ChinaUmsEnum.DeliveryCancel.getCommand().equals(transaction_id)
					||ChinaUmsEnum.OrderRegistration.getCommand().equals(transaction_id)
					||ChinaUmsEnum.Search.getCommand().equals(transaction_id)) {// 查询支付反馈异常件需要判断是否要转发
				
				if(chinaUms.getIsForward()==1){
					cwb = cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrderno());
					CwbOrder co = cwbDAO.getCwbByCwb(cwb);
					if(co == null){ //转发
						Map<String,String> paraMap=new HashMap<String,String>();
						paraMap.put("context", xmlstr);
						String forwardStr = RestHttpServiceHanlder.sendHttptoServer(paraMap, chinaUms.getForward_url());
						logger.info("chinaums转发URL返回={}",forwardStr);
						return forwardStr;
					}
				}
				
			}
		} catch (Exception e) {
			logger.error("chinaums转发ULR异常,cwb="+cwb,e);
		}
		
		try {
			
			logger.info("获取chinaums的业务编码[" + transaction_id + "];请求XML:" + xmlstr);
			if (ChinaUmsEnum.Login.getCommand().equals(transaction_id)) {// 派送员登陆
				return chinaUmsServiceMaster.getChinaUmsService_toLogin().tologin(rootnote, chinaUms);
			}
			if (ChinaUmsEnum.LoginOut.getCommand().equals(transaction_id)) {// 登出
				return chinaUmsServiceMaster.getChinaUmsService_toLogout().tologout(rootnote, chinaUms);
			}
			if (ChinaUmsEnum.Delivery.getCommand().equals(transaction_id)) { // 派送运单支付反馈
				return chinaUmsServiceMaster.getChinaUmsService_toPayAmount().toPayAmountForPos(rootnote, chinaUms);
			}
			if (ChinaUmsEnum.DeliveryCancel.getCommand().equals(transaction_id)) {// 撤销交易结果反馈
				return chinaUmsServiceMaster.getChinaUmsService_toBackOut().toBackOut(rootnote, chinaUms);
			}
			if (ChinaUmsEnum.OrderRegistration.getCommand().equals(transaction_id)) { // 派件异常反馈
				return chinaUmsServiceMaster.getChinaUmsService_toExptFeedBack().toExceptionFeedBack(rootnote, chinaUms);
			}
			if (ChinaUmsEnum.Search.getCommand().equals(transaction_id)) { // 运单查询
				return chinaUmsServiceMaster.getChinaUmsService_toCwbSearch().toCwbSearch(rootnote, chinaUms);
			}

		} catch (Exception e) {
			logger.error("处理chinaums请求发生异常，异常原因：", e);
			return "处理chinaums请求发生异常" + e.getMessage();
		}
		return "暂无此接口" + transaction_id;
	}

	private boolean ValidateMAC_publicMethod(Transaction rootnote, ChinaUms chinaUms, String xmlDOC) {

		String transtype = rootnote.getTransaction_Header().getTranstype();
		String mac = rootnote.getTransaction_Header().getMac();
		String xmltrimStr = xmlDOC;
		StringBuffer strb = new StringBuffer();
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<transaction>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<mac>"));
		strb.append(xmlsB);
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</mac>") + 6, xmltrim.length());
		strb.append(xmlsE);
		String key = chinaUms.getPrivate_key();
		String str1 = MD5Util.md5(key);// 约定的私钥,加密后得到32位私钥
		logger.info(transtype + "需要验证的内容:" + strb.toString() + key);
		String str2 = MD5Util.md5(strb.toString() + str1,"UTF-8").toUpperCase();// 去掉mac节点剩下的所有的报文加上私钥进行ＭＤ５加密
		logger.info(transtype + "签名验证的内容:" + str2);
		logger.info(transtype + "签名验证mac:" + mac);
		// 验证签名
		boolean checkMACflag = false;
		try {
			checkMACflag = str2.equals(mac);
		} catch (Exception e) {
			logger.error("chinaums签名验证异常!");
			logger.error("", e);
		}
		return checkMACflag;
	}

	/**
	 * 返回一个签名的结果,用于返回给chinaums信息时候加密
	 * 
	 * @param chinaUms
	 * @param str
	 * @return
	 */
	protected String CreateRespSign(ChinaUms chinaUms, String str) {
		String MAC = "";
		try {
			String key = chinaUms.getPrivate_key();
			MAC = MD5Util.md5(str+MD5Util.md5(key),"UTF-8").toUpperCase();
		} catch (Exception e) {
			logger.error("移动POS(chinaums):返回签名加密异常!", e);
		}
		return MAC;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public DangDang getDangDangSettingMethod(int key) {
		DangDang dangdang = new DangDang();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			dangdang = (DangDang) JSONObject.toBean(jsonObj, DangDang.class);
		} else {
			dangdang = null;
		}
		return dangdang == null ? new DangDang() : dangdang;
	}

	public long getPodResultIdByCwb(String cwbordertypeid) {
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

	/**
	 * 为ChinaUmsRespNote封装 公用对象
	 * 
	 * @param rootnote
	 * @param chinaUmsRespNote
	 * @return
	 */
	protected ChinaUmsRespNote BuildChinaumsRespClass(Transaction rootnote) {
		ChinaUmsRespNote chinaUmsRespNote = new ChinaUmsRespNote();

		chinaUmsRespNote.setDeliverid(getUserIdByUserName(rootnote.getTransaction_Header().getEmployno()));

		long deliverid = chinaUmsRespNote.getDeliverid();
		ChinaUms chinaUms = getChinaUmsSettingMethod(PosEnum.ChinaUms.getKey());
		
		String cwb = cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrderno());

		CwbOrder co = cwbDAO.getCwbDetailByCwbAndDeliverId(0, cwb);
		
		chinaUmsRespNote.setCwbOrder(co);
		
		DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		
		if(ds==null){
			if(chinaUms.getIsAutoSupplementaryProcess()==1){  //开启补充流程环节
				ds = this.getDeliveryStateAutoSupplementaryProcess(cwb, deliverid);
				co = cwbDAO.getCwbDetailByCwbAndDeliverId(0, cwb);
			}else{
				chinaUmsRespNote.setCwbOrder(null);
			}
		}
		
		if (chinaUms.getIsotherdeliveroper() == 1) { // 限制他人刷卡，只能自己刷自己名下订单
			if(co.getDeliverid()!=0&&co.getDeliverid()!=deliverid){
				chinaUmsRespNote.setCwbOrder(null);
			}
		}
		
		chinaUmsRespNote.setDeliverstate(ds);
		chinaUmsRespNote.setOrder_no(cwb);
		chinaUmsRespNote.setTransaction_id(rootnote.getTransaction_Header().getTranstype());
		return chinaUmsRespNote;
	}
	
	public DeliveryState getDeliveryStateAutoSupplementaryProcess(String cwb, long deliverid) {
	
			this.logger.info("POS支付跳流程，需自动创建数据到deliver_state表，订单号={},deliverid={}", cwb, deliverid);
			this.cwbOrderService.receiveGoods(this.userDAO.getUserByUserid(deliverid), this.userDAO.getUserByUserid(deliverid), cwb, cwb);
			DeliveryState deliverstate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
			return deliverstate;
	}

	protected ChinaUmsRespNote BuildChinaumsRespClassAndSign(Transaction rootnote) {
		String cwb = cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrderno());
		ChinaUmsRespNote chinaUmsRespNote = new ChinaUmsRespNote();
		chinaUmsRespNote.setDeliverid(getUserIdByUserName(rootnote.getTransaction_Header().getEmployno()));
		chinaUmsRespNote.setCwbOrder(cwbDAO.getCwbDetailByCwbAndDeliverId(0, cwb));
		DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb_posAndSign(cwb, chinaUmsRespNote.getDeliverid()); // 如果根据订单号可以查到对象，则返回，如果查询不到，则调用receiveGoods创建。
		chinaUmsRespNote.setDeliverstate(ds);
		chinaUmsRespNote.setOrder_no(cwb);
		chinaUmsRespNote.setTransaction_id(rootnote.getTransaction_Header().getTranstype());
		return chinaUmsRespNote;
	}

}
