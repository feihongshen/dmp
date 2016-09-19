package cn.explink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.weisuda.Weisuda;
import cn.explink.b2c.weisuda.WeisudaService;
import cn.explink.b2c.weisuda.WeisudsInterfaceEnum;
import cn.explink.dao.UserInfDao;
import cn.explink.domain.User;
import cn.explink.domain.UserInf;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.MD5.MD5Util;

/**
 * 小件员接口
 * @author jian.xie
 *
 */
@Service
public class UserInfService {
	
	private Logger logger = LoggerFactory.getLogger(UserInfService.class);
	
	@Autowired
	private UserInfDao userInfDao;
	
	@Autowired
	private WeisudaService weisudaService;
	
	@Autowired
	JointService jointService;
	
	@Autowired
	SystemInstallService systemInstallService;
	
	/**
	 * 执行同步机构站点
	 */
	public void processSync(){
		int isOpenFlag = jointService.getStateForJoint(PosEnum.Weisuda.getKey());
		if (isOpenFlag == 0) {
			logger.info("未开启唯速达[" + PosEnum.Weisuda.getKey() + "]接口！");
			return;
		}
		
		Weisuda weisuda = weisudaService.getWeisudaSettingMethod(PosEnum.Weisuda.getKey());
		
		List<UserInf> list = userInfDao.getUserInfByIsSync(false);
		updateUserInfForTimes(list);
		Map<Long, UserInf> map = onlyUserInfMap(list);
		Set<Entry<Long, UserInf>> set = map.entrySet();
		boolean result = false;
		for(Entry<Long, UserInf> entry : set){
			try{
				result = syncRequestUserInf(entry.getValue(), weisuda);
				// 更新已同步, 需要更新这一批数据
				if(result){
					updateUserInfForIssync(entry.getValue(), list);
				}
			}catch(Exception e){
				logger.error("同步站点机构异常,inf_id" + entry.getValue().getInfId(), e);
			}
		}		
	}
	
	/**
	 * 把这一批数据标识成已同步,因为存在一个机构多次修改
	 */
	@Transactional
	private void updateUserInfForIssync(UserInf userInf, List<UserInf> list){
		for(UserInf inf : list){
			if(inf.getUserid() == inf.getUserid()){
				userInfDao.updateUserInfForIssync(inf.getInfId());
			}
		}
	}
	
	/**
	 * 这一批数据唯一的机构主键对应的变更处理
	 */
	private Map<Long, UserInf> onlyUserInfMap(List<UserInf> list){
		Map<Long, UserInf> map = new HashMap<Long, UserInf>();
		for(UserInf userInf : list){
			map.put(userInf.getUserid(), userInf);
		}
		return map;
	}
	
	/**
	 * 执行新增、修改同步
	 * @param weisuda 
	 */
	private boolean syncRequestUserInf(UserInf userInf, Weisuda weisuda){
		String data = getRequestXML(userInf);
		logger.info("唯速达_小件员更新接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.courierUpdate.getValue());
		logger.info("唯速达_小件员更新返回response={}", response);
		// 根据返回报文处理 
		String result = getResultId(response, "courier_id");
		if(StringUtils.isEmpty(result)){
			return false;
		}
		if(result.equals(userInf.getUserid() + "")){
			return true;
		}
		return false;
	}
	
	/**
	 * 获得报文
	 */
	private String getRequestXML(UserInf userInf){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<item>");
		sb.append("<code>");
		sb.append(userInf.getUsername());
		sb.append("</code>");
		sb.append("<courier_id>");
		sb.append(userInf.getUserid());
		sb.append("</courier_id>");
		sb.append("<name>");
		sb.append(userInf.getRealname());
		sb.append("</name>");
		sb.append("<password>");
		sb.append(userInf.getPassword());
		sb.append("</password>");
		sb.append("<site_id>");
		sb.append(userInf.getBranchid());
		sb.append("</site_id>");
		sb.append("<mobile>");
		sb.append(userInf.getUsermobile());
		sb.append("</mobile>");
		sb.append("<enable>");
		sb.append(userInf.getStatus());
		sb.append("</enable>");
		sb.append("</item>");
		sb.append("</root>");
		return sb.toString();
	}
	
	private String check(Weisuda weisuda, String params, String value, int type) {
		String timestamp = (System.currentTimeMillis() / 1000) + "";
		String code = weisuda.getCode();
		String secret = weisuda.getSecret();
		String v = weisuda.getV();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapMd5 = new HashMap<String, String>();
		mapMd5.put(params, value);
		String md5 = createLinkString(mapMd5);
		String sign = MD5Util.md5(secret + md5 + secret);

		String access_token = MD5Util.md5(timestamp + "_" + secret + "_" + sign);
		map.put("timestamp", timestamp);
		map.put("code", code);
		map.put("v", v);
		map.put(params, value);
		map.put("sign_method", "fullmd5");
		map.put("sign", sign);
		map.put("access_token", access_token);
		String url = "";
		switch (type) {
		case 1:
			url = weisuda.getPushOrders_URL();
			break;
		case 2:
			url = weisuda.getUnVerifyOrders_URL();
			break;
		case 3:
			url = weisuda.getUpdateUnVerifyOrders_URL();
			break;
		case 4:
			url = weisuda.getUpdateOrders_URL();
			break;
		case 5:
			url = weisuda.getSiteUpdate_URL();
			break;
		case 6:
			url = weisuda.getSiteDel_URL();
			break;
		case 7:
			url = weisuda.getCourierUpdate_URL();
			break;
		case 8:
			url = weisuda.getCarrierDel_URL();
			break;
		case 9:
			url = weisuda.getUnboundOrders_URL();
			break;
		case 10:
			url = weisuda.getGetback_boundOrders_URL();
			break;
		case 11:
			url = weisuda.getGetback_getAppOrders_URL();
			break;
		case 12:
			url = weisuda.getGetback_confirmAppOrders_URL();
			break;
		case 13:
			url = weisuda.getGetback_updateOrders_URL();
			break;
		default:
			break;
		}
		String str = RestHttpServiceHanlder.sendHttptoServer(map, url);
		return str;
	}
	
	private static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			prestr = prestr + key + value;
		}
		return prestr;
	}
	
	public void saveUserInf(User user, User sessionUser){
		if(user.getRoleid() != 2 && user.getRoleid() != 4){
			return;
		}
		UserInf userInf = new UserInf();
		userInf.setUserid(user.getUserid());
		userInf.setUsername(user.getUsername());
		userInf.setRealname(user.getRealname());
		userInf.setUsermobile(user.getUsermobile());
		userInf.setPassword(user.getPassword());
		userInf.setIsSync(false);
		if(user.getEmployeestatus() == 3){
			// 失效
			userInf.setStatus((byte) 1);
		}else{
			// 有效
			userInf.setStatus((byte) 0);
		}		
		userInf.setCreateDate(new Date());
		if (sessionUser != null) {
			userInf.setCreateUser(sessionUser.getUserid() + "");
		} else {
			userInf.setCreateUser("");
		}
		userInf.setBranchid(user.getBranchid());
		userInf.setOldusername(user.getOldusername());
		userInfDao.saveUserInf(userInf);
	}
	
	/**
	 * 把报文解析返回处理的id
	 */
	private String getResultId(String response, String nodeName){
		if(StringUtils.isEmpty(response) || StringUtils.isEmpty(nodeName)){
			return null;
		}		 
		try {
			Document document = DocumentHelper.parseText(response);
			//获取根节点  
	        Element root = document.getRootElement();
	        if(root != null){
	        	List childran = root.elements();
	        	if(CollectionUtils.isNotEmpty(childran)){
	        		Element child = (Element) childran.get(0);
	        		if(nodeName.equals(child.getName())){
	        			return child.getTextTrim();
	        		}
	        	}
	        }			
		} catch (DocumentException e) {
			logger.error("解析小件员返回报文出错", e);			
			return null;
		}
		return null;
	}
	
	/**
	 * 更新同步次数
	 * @param list
	 */
	private void updateUserInfForTimes(List<UserInf> list){
		for(UserInf userInf : list){
			userInfDao.incrTimes(userInf.getInfId());
		}
	}
	
	/**
	 * 关闭旧接口
	 * modify by jian_xie 2016-09-19
	 * 因为oms的小件员更新需要通过mq，所以旧接口不能关闭，已把oms旧的同步代码注释
	 */
	public boolean isCloseOldInterface(){
//		String value = systemInstallService.getParameter("closeUserAndBranchOldInterface");
//		if(StringUtils.isNotEmpty(value) && "1".equals(value)){
//			return true;
//		}	
		return false;
	}
}
