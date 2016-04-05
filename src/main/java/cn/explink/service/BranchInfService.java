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
import cn.explink.dao.BranchInfDao;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchInf;
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
		Map<Long, BranchInf> map = onlyBranchInfMap(list);
		Set<Entry<Long, BranchInf>> set = map.entrySet();
		boolean result = false;
		for(Entry<Long, BranchInf> entry : set){
			try{
				if("delete".equalsIgnoreCase(entry.getValue().getOperType())){
					result = syncDelBranchInf(entry.getValue(), weisuda);
				} else {
					result = syncNewAndEditBranchInf(entry.getValue(), weisuda);
				}
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
	private boolean syncNewAndEditBranchInf(BranchInf branchInf, Weisuda weisuda){
		String data = getNewAndEditXML(branchInf);
		logger.info("唯速达_05站点更新接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.siteUpdate.getValue());
		logger.info("唯速达_05站点更新返回response={}", response);
		// 根据报文处理结果 TODO
		return true;
	}
	
	/**
	 * 获得报文
	 */
	private String getNewAndEditXML(BranchInf branchInf){
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
		sb.append("</item>");
		sb.append("</root>");
		return sb.toString();
	}
	
	/**
	 * 执行删除同步
	 * @param weisuda 
	 */
	private boolean syncDelBranchInf(BranchInf branchInf, Weisuda weisuda){
		String data = getDeleteXML(branchInf);
		logger.info("唯速达站点删除接口发送报文,userMessage={}", data);
		String response = check(weisuda, "data", data, WeisudsInterfaceEnum.siteDel.getValue());
		logger.info("唯速达站点删除返回response={}", response);
		// 根据返回报文处理 TODO
		return true;
	}
	
	/**
	 * 获得报文
	 */
	private String getDeleteXML(BranchInf branchInf){
		StringBuilder sb = new StringBuilder();
		sb.append("<root>");
		sb.append("<item>");
		sb.append("<site_id>");
		sb.append(branchInf.getBranchid());
		sb.append("</site_id>");
		sb.append("<rec_site_id>");
		sb.append(branchInf.getRecBranchid());
		sb.append("</rec_site_id>");
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
	
	public void saveBranchInf(Branch branch, String operType){
		BranchInf branchInf = new BranchInf();
		branchInf.setBranchid(branch.getBranchid());
		branchInf.setBranchname(branch.getBranchname());
		branchInf.setTpsbranchcode(branchInf.getTpsbranchcode());
		branchInf.setBranchprovince(branch.getBranchprovince());
		branchInf.setBranchcity(branch.getBranchcity());
		branchInf.setBrancharea(branch.getBrancharea());
		branchInf.setPassword("");
		branchInf.setRecBranchid(0);
		branchInf.setCreateDate(new Date());
		branchInf.setCreateUser("");
		branchInf.setIsSync(false);
		branchInf.setOperType(operType);
		branchInfDao.saveBranchInf(branchInf);
	}
	
}
