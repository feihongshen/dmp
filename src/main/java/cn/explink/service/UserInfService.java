package cn.explink.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
		Map<Long, UserInf> map = onlyUserInfMap(list);
		Set<Entry<Long, UserInf>> set = map.entrySet();
		boolean result = false;
		for(Entry<Long, UserInf> entry : set){
			try{
				if("delete".equalsIgnoreCase(entry.getValue().getOperType())){
					result = syncDelUserInf(entry.getValue(), weisuda);
				} else {
					result = syncNewAndEditUserInf(entry.getValue(), weisuda);
				}
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
	private boolean syncNewAndEditUserInf(UserInf userInf, Weisuda weisuda){
		String data = getNewAndEditXML(userInf);
		logger.info("唯速达_05站点更新接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.siteUpdate.getValue());
		logger.info("唯速达_05站点更新返回response={}", response);
		// 根据报文处理结果 TODO
		return true;
	}
	
	/**
	 * 获得报文
	 */
	private String getNewAndEditXML(UserInf userInf){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<item>");
		sb.append("<code>");
		sb.append(userInf.getUsername());
		sb.append("</code>");
		sb.append("<old_code>");
		sb.append(userInf.getOldusername());
		sb.append("</old_code>");
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
		sb.append("</item>");
		sb.append("</root>");
		return sb.toString();
	}
	
	/**
	 * 执行删除同步
	 * @param weisuda 
	 */
	private boolean syncDelUserInf(UserInf userInf, Weisuda weisuda){
		String data = getDeleteXML(userInf);
		logger.info("唯速达站点删除接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.siteDel.getValue());
		logger.info("唯速达站点删除返回response={}", response);
		// 根据返回报文处理 TODO
		return true;
	}
	
	/**
	 * 获得报文
	 */
	private String getDeleteXML(UserInf userInf){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<item>");
		sb.append("<user_id>");
		sb.append(userInf.getUserid());
		sb.append("</user_id>");
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
	
	
	public void saveUserInf(User user, String operType){
		UserInf userInf = new UserInf();
		userInf.setUserid(user.getUserid());
		userInf.setUsername(user.getUsername());
		userInf.setRealname(user.getRealname());
		userInf.setUsermobile(user.getUsermobile());
		userInf.setPassword(user.getPassword());
		userInf.setIsSync(false);
		userInf.setOperType(operType);
		userInf.setCreateDate(new Date());
		userInf.setCreateUser("");
		userInf.setBranchid(user.getBranchid());
		userInf.setOldusername(user.getOldusername());
		userInfDao.saveUserInf(userInf);
	}
}
