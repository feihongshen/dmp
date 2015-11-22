package cn.explink.b2c.bjUnion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.bjUnion.login.LoginReq;
import cn.explink.b2c.bjUnion.query.BodydataQuery;
import cn.explink.b2c.bjUnion.query.QueryReq;
import cn.explink.b2c.bjUnion.revoke.RevokeReq;
import cn.explink.b2c.bjUnion.sign.BodydataSign;
import cn.explink.b2c.bjUnion.sign.SignReq;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomerDAO;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class BJUnionService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;
	@Autowired
	BJUnionInterface bjUnionInterface;
	
	private Logger logger = LoggerFactory.getLogger(BJUnionService.class);
	
	public void update(int joint_num,int state){
		jiontDAO.UpdateState(joint_num, state);
    }
	public void edit(HttpServletRequest request,int joint_num){
		BJUnion dms=new BJUnion();
		dms.setRequestUrl(request.getParameter("requestUrl"));//北京银联(浙江)请求路径
		dms.setPrivate_key(request.getParameter("private_key"));
		String ischecksign = request.getParameter("ischecksign")==null?"":(request.getParameter("ischecksign"));
		dms.setIschecksign(ischecksign);
		String customerid = request.getParameter("customerid");
		dms.setCustomerid(customerid);
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(dms);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if(jointEntity == null){
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		}else{
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		//保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}
	
	//获取北京银联基础设置信息
	public BJUnion getBJUnion(int yhd_key) {
		if (getObjectMethod(yhd_key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(yhd_key));
		BJUnion bjunion = (BJUnion) JSONObject.toBean(jsonObj, BJUnion.class);
		return bjunion;
	}
	public Object getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}
	
	//处理北京银联(浙江)请求入
	public String dealWithRequest(String context) {
		//获取北京银联(浙江)配置信息
		BJUnion bu = getBJUnion(PosEnum.BJUnion.getKey());
		int isOpenFlag = this.jointService.getStateForJoint(PosEnum.BJUnion.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启【北京银联(浙江)】对接！");
			return "请沟通开启【北京银联(浙江)】对接！";
		}
		String checksignStr = "";
		try{
			checksignStr = checkSign(context,bu);
		}catch(Exception e){
			this.logger.error("北京银联接口异常:{}",e);
		}
		//校验签名
		return checksignStr;
	}
	//校验签名是否成功
	public String checkSign(String context,BJUnion bu){
		String str = "";
		if(!"".equals(context)){
			try{
				String beforeMAC = context.substring(context.indexOf("<transaction>"), context.indexOf("<mac>"));
				String afterMAC = context.substring(context.indexOf("</mac>")+6, context.length());
				String subtractStr = beforeMAC + afterMAC;
				String mac= context.substring(context.indexOf("<mac>")+5,context.indexOf("</mac>"));//校验签名
				
				if("yes".equals(bu.getIschecksign())){
					boolean isSign = isSign(mac,subtractStr,bu,context);
					if(!isSign){
						//签名失败，返回公用的失败方法。
						String beforemac = context.substring(0,context.indexOf("<request_time>"));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						String responsetime = "<response_time>"+sdf.format(new Date())+"</response_time>";
						String middleStr = "<response_code>"+ResponseEnum.SignValidateFailed.getResponse_code()+"</response_code>"
								+"<response_msg>"+ResponseEnum.SignValidateFailed.getResponse_msg()+"</response_msg>";
						String aftermac = "</transaction_header>"
								+ "<transaction_body>"
								+ "</transaction_body>"
								+ "</transaction>";
						String macStr = MD5Util.md5(beforemac+responsetime+middleStr+aftermac+bu.getPrivate_key());
						return (beforemac+responsetime+middleStr+macStr+aftermac).replace("null", "");
					}
				}
				
				try{
				 	return getTRUEObject(context,subtractStr,bu);
				}catch(Exception e){
					this.logger.error("解析xml报文异常:{}",e);
					str = e.getMessage();
				}
			}catch(Exception e){
				this.logger.error("校验加密信息异常,原因:{}",e);
				str = e.getMessage();
			}
		}
		return ("".equals(str))?"未知异常,及时处理!":(str);
	}
	public static void main(String[] args) {
		String str="afafgag1124242<mac>666666</mac>4g1121g12";
		System.out.println(str.substring(str.indexOf("<mac>")+5,str.indexOf("</mac>")));
		
	}
	//获取请求过来的加密字符串并进行处理（32位）
	public String getTRUEObject(String context,String subtractStr,BJUnion bu) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		if(context.contains(DealRemarkEnum.login.getRemark())){//登录处理
			LoginReq req = new LoginReq();
			req = (LoginReq)XmlToBean.toBean(context,req);
			HeaderdataReq reqs = req.getHeader();
			map = getheaderMap(reqs);
			map.put("passwd", req.getBody().getPasswd());
			return this.bjUnionInterface.doLoginReq(map, bu);
		}else if(context.contains(DealRemarkEnum.sign.getRemark())){//签收处理
			SignReq req = new SignReq();
			req = (SignReq)XmlToBean.toBean(context,req);
			HeaderdataReq reqs = req.getHeader();
			BodydataSign reqsbody = req.getBody();
			map = getheaderMap(reqs);
			map.put("order_no", reqsbody.getOrder_no());//订单号
			map.put("order_payable_amt", reqsbody.getOrder_payable_amt());//应收款
			map.put("pay_type", reqsbody.getPay_type());//付款方式
			map.put("order_ref_no", reqsbody.getOrder_ref_no());//银行参考号
			map.put("postrace", reqsbody.getPostrace());//pos流水号
			map.put("tracetime", reqsbody.getTracetime());//银行交易时间
			map.put("cardid", reqsbody.getCardid());//银行卡号
			map.put("signee", reqsbody.getSignee());//签收人
			map.put("consignee_sign_flag", reqsbody.getConsignee_sign_flag());//是否本人签收
			return this.bjUnionInterface.doSignReq(map, bu);
		}else if(context.contains(DealRemarkEnum.query.getRemark())){//查询处理
			QueryReq req = new QueryReq();
			req = (QueryReq)XmlToBean.toBean(context,req);
			HeaderdataReq reqs = req.getHeader();
			BodydataQuery queryreq = req.getBody();
			map = getheaderMap(reqs);
			map.put("order_no", queryreq.getOrder_no());//订单号
			return this.bjUnionInterface.doQueryReq(map, bu);
		}else if(context.contains(DealRemarkEnum.revoke.getRemark())){//撤销处理
			RevokeReq req = new RevokeReq();
			req = (RevokeReq)XmlToBean.toBean(context,req);
			HeaderdataReq reqs = req.getHeader();
			map = getheaderMap(reqs);
			map.put("order_no", req.getBody().getOrder_no());
			map.put("amt", req.getBody().getAmt());
			return this.bjUnionInterface.doRevokeReq(map, bu);
		}
		String beforemac = context.substring(context.indexOf("<transaction>"), context.indexOf("<request_time>"));
		String timestr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String middleStr = "<response_time>"+timestr+"</response_time>"
				+ "<response_code>"+ResponseEnum.jiaoyileixingma.getResponse_code()+"</response_code>"
				+ "<response_msg>"+ResponseEnum.jiaoyileixingma.getResponse_msg()+"</response_msg>";
		String afterStr = "</transaction_header>"
				+"<transaction_body>"
				+ "</transaction_body>"
				+ "</transaction>";
		String md5Str = MD5Util.md5(beforemac+middleStr+afterStr+bu.getPrivate_key()).toUpperCase();
		String resposeStr = beforemac+middleStr+"<mac>"+md5Str+"</mac>"+afterStr;
		return resposeStr;
	}
	
	private boolean isSign(String mac,String subtractStr,BJUnion bu,String context){
		boolean isSign = true;
		if("".equals(mac)){
			this.logger.info("加密验证失败,请求参数为:{}",context);
			isSign = false;
		}
		//本地加密字符串
		String sign = MD5Util.md5(subtractStr+bu.getPrivate_key()).toUpperCase();
		//对比加密信息是否通过
		if(!mac.equals(sign)){
			this.logger.info("加密验证失败,参数为:{},本地待加密信息:{},本地加密结果:{},实际加密结果:{}",new Object[]{context,subtractStr+bu.getPrivate_key(),sign,mac});
			isSign = false;
		}
		return isSign;
	}
	
	private Map<String, String> getheaderMap(HeaderdataReq reqs){
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("version", reqs.getVersion());
		headerMap.put("transaction_id", reqs.getTransaction_id());
		headerMap.put("employno", reqs.getEmployno());
		headerMap.put("termid", reqs.getTermid());
		return headerMap;
	}
	
}
