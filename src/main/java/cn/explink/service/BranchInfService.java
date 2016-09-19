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
import cn.explink.dao.BranchInfDao;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchInf;
import cn.explink.domain.User;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.MD5.MD5Util;

/**
 * 站点机构接口
 * @author jian.xie
 *
 */
@Service
public class BranchInfService {
	
	private Logger logger = LoggerFactory.getLogger(BranchInfService.class);
	
	@Autowired
	private BranchInfDao branchInfDao;
	
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
		
		List<BranchInf> list = branchInfDao.getBranchInfByIsSync(false);
		updateBranchInfForTimes(list);
		Map<Long, BranchInf> map = onlyBranchInfMap(list);
		Set<Entry<Long, BranchInf>> set = map.entrySet();
		boolean result = false;
		for(Entry<Long, BranchInf> entry : set){
			try{
				result = syncRequestBranchInf(entry.getValue(), weisuda);
				// 更新已同步, 需要更新这一批数据
				if(result){
					updateBranchInfForIssync(entry.getValue(), list);
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
	private void updateBranchInfForIssync(BranchInf branchInf, List<BranchInf> list){
		for(BranchInf inf : list){
			if(inf.getBranchid() == inf.getBranchid()){
				branchInfDao.updateBranchInfForIssync(inf.getInfId());
			}
		}
	}
	
	/**
	 * 这一批数据唯一的机构主键对应的变更处理
	 */
	private Map<Long, BranchInf> onlyBranchInfMap(List<BranchInf> list){
		Map<Long, BranchInf> map = new HashMap<Long, BranchInf>();
		for(BranchInf branchInf : list){
			map.put(branchInf.getBranchid(), branchInf);
		}
		return map;
	}
	
	/**
	 * 执行新增、修改同步
	 * @param weisuda 
	 */
	private boolean syncRequestBranchInf(BranchInf branchInf, Weisuda weisuda){
		String data = getRequestXML(branchInf);
		logger.info("唯速达_05站点更新接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.siteUpdate.getValue());
		logger.info("唯速达_05站点更新返回response={}", response);
		// 根据报文处理结果 
		String result = getResultId(response, "site_id");
		if(StringUtils.isEmpty(result)){
			return false;
		}
		if(result.equals(branchInf.getBranchid() + "")){
			return true;
		}
		return false;
	}
	
	/**
	 * 获得报文
	 */
	private String getRequestXML(BranchInf branchInf){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<item>");
		sb.append("<code>");
		sb.append(branchInf.getTpsbranchcode());
		sb.append("</code>");
		sb.append("<name>");
		sb.append(branchInf.getBranchname());
		sb.append("</name>");
		sb.append("<province>");
		sb.append(branchInf.getBranchprovince());
		sb.append("</province>");
		sb.append("<city>");
		sb.append(branchInf.getBranchcity());
		sb.append("</city>");
		sb.append("<zone>");
		sb.append(branchInf.getBrancharea());
		sb.append("</zone>");
		sb.append("<password>");
		sb.append(branchInf.getPassword());
		sb.append("</password>");
		sb.append("<site_id>");
		sb.append(branchInf.getBranchid());
		sb.append("</site_id>");
		sb.append("<enable>");
		sb.append(branchInf.getStatus());
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
	
	public void saveBranchInf(Branch branch, User sessionUser){
		BranchInf branchInf = new BranchInf();
		branchInf.setBranchid(branch.getBranchid());
		branchInf.setBranchname(branch.getBranchname());
		branchInf.setTpsbranchcode(branch.getTpsbranchcode());
		branchInf.setBranchprovince(branch.getBranchprovince());
		branchInf.setBranchcity(branch.getBranchcity());
		branchInf.setBrancharea(branch.getBrancharea());
		branchInf.setPassword("");
		branchInf.setRecBranchid(0);
		branchInf.setCreateDate(new Date());
		if (sessionUser != null) {
			branchInf.setCreateUser(sessionUser.getUserid() + "");
		} else {
			branchInf.setCreateUser("");
		}
		branchInf.setIsSync(false);
		if("1".equals(branch.getBrancheffectflag())){
			// 有效
			branchInf.setStatus((byte) 0);
		}else{
			// 失效
			branchInf.setStatus((byte) 1);
		}		
		branchInfDao.saveBranchInf(branchInf);
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
			logger.error("解析站点返回报文出错", e);			
			return null;
		}
		return null;
	}
	
	/**
	 * 更新次数
	 */
	private void updateBranchInfForTimes(List<BranchInf> list){
		for(BranchInf branchInf : list){
			branchInfDao.incrTimes(branchInf.getInfId());
		}
	}
	
	/**
	 * 关闭旧接口
	 * modify by jian_xie 2016-09-19
	 * 因为oms的站点机构更新需要通过mq，所以旧接口不能关闭，已把oms旧的同步代码注释
	 */
	public boolean isCloseOldInterface(){
//		String value = systemInstallService.getParameter("closeUserAndBranchOldInterface");
//		if(StringUtils.isNotEmpty(value) && "1".equals(value)){
//			return true;
//		}	
		return false;
	}
}
