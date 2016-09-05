package cn.explink.b2c.jiuye.addressmatch;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.yihaodian.RestTemplateClient;
import cn.explink.b2c.yihaodian.addressmatch.YihaodianAddMatchService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;

@Service
public class JiuYeAddMatchService {
	private Logger logger =LoggerFactory.getLogger(YihaodianAddMatchService.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;
	
	public String getObjectMethod(int key){
		JointEntity obj=jiontDAO.getJointEntity(key);
		return obj==null?null:obj.getJoint_property();
	}
	public JiuYeAddressMatch getJiuYe(int key){
		if(getObjectMethod(key)==null){
			return null;
		}
	    JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key)); 
	    JiuYeAddressMatch jiuye = (JiuYeAddressMatch)JSONObject.toBean(jsonObj,JiuYeAddressMatch.class);
		return jiuye;
	}
	public void edit(HttpServletRequest request,int joint_num){
		String maxCount=request.getParameter("maxCount").isEmpty()?"0":request.getParameter("maxCount");
		JiuYeAddressMatch jiuye=new JiuYeAddressMatch();
		
		jiuye.setReceiver_url(request.getParameter("receiver_url"));
		jiuye.setCustomerid(request.getParameter("customerid"));
		jiuye.setPrivate_key(request.getParameter("private_key"));
		jiuye.setDmsCode(request.getParameter("dmsCode"));
		jiuye.setB2cenum(joint_num);
		jiuye.setMaxCount(Integer.parseInt(maxCount));
		
		JSONObject jsonObj = JSONObject.fromObject(jiuye);
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
		
	}
	public void update(int joint_num,int state){
			jiontDAO.UpdateState(joint_num, state);
	}
	
	public int getStateForJiuYe(int key){
		JointEntity obj=null;
		int state=0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state=obj.getState();
		} catch (Exception e) {
			logger.error("", e);
		}
		return state;
	}
	
	/**
	 * 接收处理九曳站点匹配的方法
	 * 
	 * @return
	 * @throws Exception 
	 * 
	 */
	public String invokeJiuYeAddressmatch(JiuYeRequest jyreq,JiuYeAddressMatch jiuye)  {
		
		try {
			String requestName=jyreq.getRequestName();
			String sign=jyreq.getSign();
			String requestTime=jyreq.getTimeStamp();
			String delveryCode = jyreq.getDelveryCode();
			Content content = jyreq.getContent();
			String province = content.getGetProvice();
			String city = content.getGetCity();
			String county = content.getGetCounty();
			String address = content.getGetAddress();
			String localSign = MD5Util.md5(requestTime+jiuye.getPrivate_key());
			if(!requestName.equals("RequestOrdersStation")){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"非法的请求格式\"}";
			}
			if(!delveryCode.equals(jiuye.getDmsCode())){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"传入物流公司编码为空或无效\"}";
			}
			if(!localSign.equals(sign)){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"MD5签名错误\"}";
			}
			//详细地址=省+市+区/县
			String totalAddress = province+city+county+address;
			if(totalAddress==null||totalAddress.isEmpty()){
				return null;
			}
			//matchAddressByInterface
			JSONObject addressJson =  addressMatchService.matchAddressByInterfaces("jiuye", totalAddress);
			if(addressJson==null){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"不在派送区域内\"}";
			}
			String station=addressJson.getString("station"); //对应返回的站点ID
			if(station==null||station.equals("")){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"不在派送区域内\"}";
			}
		
			Branch branch=branchDAO.getBranchByBranchid(Long.valueOf(station));
			if(branch.getBranchid()==0){
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"找不到关联站点\"}"; 
			}
			
			/**mod by yurong.liang 2016-08-31**/
			if(branch.getBranchname()!=null&&"EMS".equals(branch.getBranchname().trim())){//九曳方要求匹配站点名称是EMS的直接返回"不在派送区域内"
				logger.info("九曳订单地址库匹配为EMS站点,按九曳方要求返回【不在派送区域内】,订单详细地址为："+totalAddress);
				return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"不在派送区域内\"}";
			}
			/***************mod end*****************/
			
			return getJiuYeResponse(true,true,"成功",branch); //构建每个订单号的返回结果
			
		} catch (Exception e) {
			logger.error("九曳站点匹配-系统未知异常",e);
			return "{\"RequestName\": \"RequestOrdersStation\",\"Partner\": \"\",\"Success\": true,\"IsArrive\": false,\"SiteNum\":\"\",\"SiteName\": \"\",\"Msg\": \"未知异常\"}";
		}
	
		
	}
	
	private String getJiuYeResponse(boolean isSuccess,boolean isArrive,String msg,Branch branch) {
		//String id=addressJson.getString("id"); //请求唯一编码
		JiuYeResponse jiuyeResponse=new JiuYeResponse();
		jiuyeResponse.setRequestName("RequestOrdersStation");
		jiuyeResponse.setPartner("");
		jiuyeResponse.setSuccess(isSuccess);
		jiuyeResponse.setIsArrive(isArrive);
		if(isArrive==false){
			jiuyeResponse.setSiteNum("");
			jiuyeResponse.setSiteName("");
		}
		jiuyeResponse.setSiteNum(branch.getBranchcode()==null?"":branch.getBranchcode());//站点编号
		jiuyeResponse.setSiteName(branch.getBranchname()==null?"":branch.getBranchname()); //站点名称
		jiuyeResponse.setMsg(msg);
		String response = "";
		try {
			response = JacksonMapper.getInstance().writeValueAsString(jiuyeResponse);
		} catch (Exception e) {
			logger.error("", e);
		} 
		return response;
		
	}
	
}
