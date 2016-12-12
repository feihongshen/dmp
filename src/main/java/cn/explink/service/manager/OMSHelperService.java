package cn.explink.service.manager;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.vipshop.VipShopMD5Util;
import cn.explink.domain.SystemInstall;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;

/**
 * oms项目的助手类，dmp调用oms的方法
 * @author jian.xie
 *
 */
@Service
public class OMSHelperService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SystemInstallService systemInstallService;

	/**
	 * oms地址
	 */
	private String getOmsUrl(){
		String url = null;
		url = systemInstallService.getParameter("omsUrl", "http://127.0.0.1:8080/oms/");
		return url;	
	}
	
	/**
	 * mds密文
	 * @param cwbs
	 * @param type
	 */
	private String getMD5(String cwbs, long time){
		String sign = systemInstallService.getParameter("dmpAndOmsSecretKey");
		return VipShopMD5Util.MD5(cwbs + time + sign).toLowerCase();
	}
	
	/**
	 * 封装参数
	 * @param cwbs
	 * @param type
	 * @return
	 */
	private StringBuilder getParams(String cwbs, String type) {
		long time = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append("cwbs=");
		sb.append(cwbs);
		sb.append("&type=");
		sb.append(type);
		sb.append("&md5=");
		sb.append(getMD5(cwbs, time));
		sb.append("&requesttime=");
		sb.append(time);
		return sb;
	}
	
	public void reTpoSendDoInf(String cwbs, String type){
		StringBuilder sb = getParams(cwbs, type);
		try {
			JSONReslutUtil.getResultMessage(getOmsUrl() + "DMPInterface/reTpoSendDoInf", sb.toString() , "POST");
		} catch (IOException e) {
			logger.error("reTpoSendDoInf", e);
		}
	}

	
	
	public void reTpoOtherOrderTrack(String cwbs, String type){
		StringBuilder sb = getParams(cwbs, type);
		try {
			JSONReslutUtil.getResultMessage(getOmsUrl() + "DMPInterface/reTpoOtherOrderTrack", sb.toString() , "POST");
		} catch (IOException e) {
			logger.error("reTpoOtherOrderTrack", e);
		}
	}

	public void reOpsSendB2cData(String cwbs, String type) {
		StringBuilder sb = getParams(cwbs, type);
		try {
			JSONReslutUtil.getResultMessage(getOmsUrl() + "DMPInterface/reOpsSendB2cData", sb.toString() , "POST");
		} catch (IOException e) {
			logger.error("reOpsSendB2cData", e);
		}
	}
	
}
