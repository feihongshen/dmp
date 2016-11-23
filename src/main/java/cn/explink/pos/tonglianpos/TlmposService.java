package cn.explink.pos.tonglianpos;

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
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tonglianpos.xmldto.TlmposUnmarchal;
import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.StringUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService.class);
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
	TlmposServiceMaster tlmposServiceMaster;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	
	@Autowired
	SystemInstallService systemInstallService;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = this.jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return posValue;
	}

	public Tlmpos gettlmposSettingMethod(int key) {
		Tlmpos tlmpos = new Tlmpos();
		if (!"".equals(this.getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			tlmpos = (Tlmpos) JSONObject.toBean(jsonObj, Tlmpos.class);
		} else {
			tlmpos = new Tlmpos();
		}

		return tlmpos == null ? new Tlmpos() : tlmpos;
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
		Tlmpos tlmpos = new Tlmpos();
		String requester = StringUtil.nullConvertToEmptyString(request.getParameter("requester"));
		String targeter = StringUtil.nullConvertToEmptyString(request.getParameter("targeter"));
		String privateKey = StringUtil.nullConvertToEmptyString(request.getParameter("privateKey"));
		String publicKey = StringUtil.nullConvertToEmptyString(request.getParameter("publicKey"));

		String isotherdeliverupdate = StringUtil.nullConvertToEmptyString(request.getParameter("isotherdeliverupdate"));// 刷卡是否更新派送员
		String isotheroperator = StringUtil.nullConvertToEmptyString(request.getParameter("isotheroperator"));// 刷卡是否更新派送员

		String request_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));

		String isValidateSign = request.getParameter("isValidateSign"); // 是否开启签名验证
		String isbackout = request.getParameter("isbackout"); // 是否允许撤销

		String isshowPhone = request.getParameter("isshowPhone"); // 是否显示电话 0不显示
																	// 1显示
		String isshowPaytype = request.getParameter("isshowPaytype"); // 是否显示支付方式放在remark列
		String private_key = request.getParameter("private_key"); // 0不显示
		// 1显示

		tlmpos.setRequester(requester);
		tlmpos.setTargeter(targeter);
		tlmpos.setPublicKey(publicKey);
		tlmpos.setPrivateKey(privateKey);
		tlmpos.setIsotherdeliverupdate(Integer.parseInt(isotherdeliverupdate));
		tlmpos.setIsotheroperator(Integer.valueOf(isotheroperator));
		tlmpos.setRequest_url(request_url);
		tlmpos.setIsValidateSign(Integer.valueOf(isValidateSign));
		tlmpos.setIsbackout(Integer.valueOf(isbackout));
		String forwardUrl = request.getParameter("forwardUrl");
		tlmpos.setForwardUrl(forwardUrl);
		tlmpos.setIsshowPhone(Integer.valueOf(isshowPhone));
		tlmpos.setIsshowPaytype(Integer.valueOf(isshowPaytype));
		tlmpos.setPrivate_key(private_key);
		JSONObject jsonObj = JSONObject.fromObject(tlmpos);
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

	/**
	 * 处理tlmpos的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */
	public String AnalyzXMLBytlmpos(Tlmpos tlmpos, String xmlstr) {
		if ((xmlstr != null) && !"".equals(xmlstr)) {
			try {

				Transaction rootnote = TlmposUnmarchal.Unmarchal(xmlstr);
				String requester = rootnote.getTransaction_Header().getRequester();
				String target = rootnote.getTransaction_Header().getTarget();
				String requester_set = tlmpos.getRequester();
				String target_set = tlmpos.getTargeter();

				if (!requester.equals(requester_set) || !target.equals(target_set)) {
					this.logger.error("tlmpos请求方或应答方配置信息有误!");
					return "请求方或应答方配置信息有误";
				}
				if (!this.ValidateMAC_publicMethod(rootnote, tlmpos, xmlstr)) {
					this.logger.error("tlmpos请求签名验证失败!xml=" + xmlstr);
					if (tlmpos.getIsValidateSign() == 1) {
						return "签名验证失败";
					}
				}
				return this.DealWithtlmposInterface(tlmpos, xmlstr, rootnote);
			} catch (Exception e) {
				e.printStackTrace();
				return "Convert XML to Transaction Bean Exception! reason=" + e;
			}

		} else {
			return "TongLian Pos request XML is null.";
		}

	}

	/**
	 * tlmpos请求的接口 判断
	 * 
	 * @param tlmpos
	 * @param xmlstr
	 * @param rootnote
	 * @return
	 */
	private String DealWithtlmposInterface(Tlmpos tlmpos, String xmlstr, Transaction rootnote) {
		String transaction_id = "";
		
		String cwb="";
		try {
				if(!"MI0001".equals(transaction_id)){
					cwb = cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrder_no());
					CwbOrder co = cwbDAO.getCwbByCwb(cwb);
					if(co == null){ //转发
						String forwardStr = RestHttpServiceHanlder.sendHttptoServer(xmlstr, tlmpos.getForwardUrl());
						logger.info("tlmpos转发URL返回={}",forwardStr);
						return forwardStr;
					}
				}
				
		} catch (Exception e) {
			logger.error("tlmpos转发ULR异常,cwb="+cwb,e);
		}
		
		try {
			transaction_id = rootnote.getTransaction_Header().getTransaction_id();
			// logger.info("获取tlmpos的业务编码["+transaction_id+"];请求XML:"+xmlstr);
			if ("MI0001".equals(transaction_id)) {// 派送员登陆
				return this.tlmposServiceMaster.getTlmposService_toLogin().tologin(rootnote, tlmpos);
			}
			if ("MI0010".equals(transaction_id)) { // 运单查询
				return this.tlmposServiceMaster.getTlmposService_toCwbSearch().toCwbSearch(rootnote, tlmpos);
			}
			if ("MI0005".equals(transaction_id)) { // 派送运单支付反馈
				return this.tlmposServiceMaster.getTlmposService_toPayAmount().toPayAmountForPos(rootnote, tlmpos);
			}
			if ("MI0006".equals(transaction_id)) {// 派件签收结果反馈
				return this.tlmposServiceMaster.getTlmposService_toCwbSign().toCwbSign(rootnote, tlmpos);
			}
			if ("MI0007".equals(transaction_id)) {// 撤销交易结果反馈
				return this.tlmposServiceMaster.getTlmposService_toBackOut().toBackOut(rootnote, tlmpos);
			}
			if ("MI0008".equals(transaction_id)) { // 派件异常反馈
				return this.tlmposServiceMaster.getTlmposService_toExptFeedBack().toExceptionFeedBack(rootnote, tlmpos);
			}

			if ("MI0009".equals(transaction_id)) { // 上门揽退业务
				return this.tlmposServiceMaster.getTlmposService_toSmt().toDealWithSmt(rootnote, tlmpos);
			}
		} catch (Exception e) {
			this.logger.error("处理tlmpos请求发生异常，异常原因：", e);
			e.printStackTrace();
			return "处理tlmpos请求发生异常" + e.getMessage();
		}
		return "无此接口" + transaction_id;
	}

	private boolean ValidateMAC_publicMethod(Transaction rootnote, Tlmpos tlmpos, String xmlDOC) {
		String transaction_id = rootnote.getTransaction_Header().getTransaction_id();
		String xmltrimStr = xmlDOC;
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<Transaction_Header>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<MAC>"));
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</MAC>") + 6, xmltrim.indexOf("</Transaction>"));
		String checkMACdata = xmlsB + xmlsE;
		this.logger.info(transaction_id + "签名验证的内容:" + checkMACdata);
		// 验证签名
		boolean checkMACflag = false;
		try {
			checkMACflag = RSACoder.verify(checkMACdata.getBytes(), tlmpos.getPublicKey(), rootnote.getTransaction_Header().getMAC());
			// logger.info("checkMACdata="+checkMACdata+",public_key="+tlmpos.getPublicKey()+",MAC="+rootnote.getTransaction_Header().getMAC()+",checkMACflag="+checkMACflag);
		} catch (Exception e) {
			this.logger.error("tlmpos签名验证异常!业务编码");
			e.printStackTrace();
		}

		return checkMACflag;
	}

	public static void main(String[] args) throws Exception {

		System.out.println(isAcronym("ABC1DE"));

	}

	/**
	 * 返回一个签名的结果,用于返回给tlmpos信息时候加密
	 * 
	 * @param tlmpos
	 * @param str
	 * @return
	 */
	protected String CreateRespSign(Tlmpos tlmpos, String str) {
		String MAC = "";
		try {
			MAC = RSACoder.sign(str.getBytes(), tlmpos.getPrivateKey());
		} catch (Exception e) {
			this.logger.error("移动POS(tlmpos):返回签名加密异常!", e);

		}
		return MAC;
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public DangDang getDangDangSettingMethod(int key) {
		DangDang dangdang = new DangDang();
		if (!"".equals(this.getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
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
	 * 判断字符串是否含有大写字母 true 全大写，false有小写
	 * @param word
	 * @return
	 */
	public static boolean isAcronym(String word)
	 {
		 int flag=0;
		  for(int i = 0; i < word.length(); i++)
		  {
			   char c = word.charAt(i);
			   if (!Character.isLowerCase(c))
			   {
				   flag++;
			   }
		  }
		  if(flag==word.length()){
			  return true;
		  }
		  return false;
	 }
	
	
	/**
	 * 为tlmposRespNote封装 公用对象
	 * 
	 * @param rootnote
	 * @param tlmposRespNote
	 * @return
	 */
	protected TlmposRespNote buildtlmposRespClass(Transaction rootnote, TlmposRespNote tlmposRespNote) {
		
		String cwbTransCwb = this.cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrder_no()); // 可能是订单号也可能是运单号
		
		String userName=rootnote.getTransaction_Header().getExt_attributes().getDelivery_man();
		// modify by jian_xie 关闭大写转小写功能，2016-11-23，运行一段时间后，代码可删除
		boolean closePJDUppercaseToLowercase = systemInstallService.isBoolenInstall("ClosePJDUppercaseToLowercase");
		if(!closePJDUppercaseToLowercase && isAcronym(userName)){ //如果全部为大写，很可能是品骏达一体机,系统中不允许有大小写混合的
			userName=userName.toLowerCase();
		}
		
		tlmposRespNote.setDeliverid(this.getUserIdByUserName(userName));
		

		long deliverid = 0;
		Tlmpos tlmpos = this.gettlmposSettingMethod(PosEnum.TongLianPos.getKey());
		if (tlmpos.getIsotheroperator() == 1) { // 限制他人刷卡，只能自己刷自己名下订单
			deliverid = tlmposRespNote.getDeliverid() == 0 ? -1 : tlmposRespNote.getDeliverid();
		}
		
		
		tlmposRespNote.setCwbOrder(this.cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwbTransCwb));
		if (tlmposRespNote.getCwbOrder() == null) {
			return tlmposRespNote;
		}

		tlmposRespNote.setBranchid(this.userDAO.getUserByUsername(userName).getBranchid());

		DeliveryState ds = this.deliveryStateDAO.getDeliveryStateByCwb_posHelper(cwbTransCwb, tlmposRespNote.getDeliverid()); // 如果根据订单号可以查到对象，则返回，如果查询不到，则调用receiveGoods创建。

		tlmposRespNote.setDeliverstate(ds);
		tlmposRespNote.setOrder_no(cwbTransCwb);
		tlmposRespNote.setTransaction_id(rootnote.getTransaction_Header().getTransaction_id());
		tlmposRespNote.setDelivery_man(userName);

		return tlmposRespNote;
	}

	private User getPjdUserName(String cwbTransCwb) {
		User u=null;
		CwbOrder cwbOrder = this.cwbDAO.getCwbDetailByCwbAndDeliverId(0, cwbTransCwb);
		if(cwbOrder!=null){
			long deliveryId = cwbOrder.getDeliverid();
			if(deliveryId!=0){
				u= this.userDAO.getAllUserByid(deliveryId);
			}
		}
		return u;
	}

}
