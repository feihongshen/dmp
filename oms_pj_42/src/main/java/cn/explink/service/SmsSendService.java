package cn.explink.service;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tempuri.WmgwLocator;

import cn.explink.dao.SmsConfigDAO;
import cn.explink.domain.SmsConfig;

@Service
public class SmsSendService {
	private static boolean warning = true;// 警告标识，如果发送过警告信息，则记录为false
	private static WmgwLocator wmgwLocator = new WmgwLocator();

	@Autowired
	SmsConfigDAO smsConfigDAO;

	/**
	 * mobileIds手机号可以以英文半角逗号分割，多少个手机号 num就是多少
	 */
	public String sendSms(String mobileIds, String strMsg, Integer num) throws UnsupportedEncodingException {

		// strMsg = new
		// String(strMsg.getBytes("ISO-8859-1"),"UTF-8");//web服务端只接受UTF—8方式的编码
		try {
			SmsConfig smsConf = smsConfigDAO.getAllSmsConfig();
			if (smsConf != null) {
				if (smsConf.getIsOpen() == 1) {
					wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
					// 获取短信剩余条数，剩余warningSwitch条时发送预警
					Integer strRet = wmgwLocator.getwmgwSoap().mongateQueryBalance(smsConf.getName(), smsConf.getPassword());
					if (smsConf.getMonitor() == 1) {// 判断是否打开监控开关 flage == 1
						if (strRet < smsConf.getWarningcount()) {// 判断是否符合预警条件
																	// count
							if (warning) {// 判断是否已经预警过
								String warningMobileIds = smsConf.getPhone(); // 预警手机号
								String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
								wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), warningMobileIds, warningStrMsg, warningMobileIds.split(",").length);
								warning = false;
							}
						} else {// 当重新充值以后，将重置预警状态
							warning = true;
						}
					}
				}
			} else {
				return "数据表没有数据，没做账户配置";
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return "接口调用发生异常";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "接口调用发生异常";
		}
		return "发送短信成功";
	}

	/**
	 * mobileIds手机号可以以英文半角逗号分割，多少个手机号 num就是多少
	 */
	public String sendSmsByTemplate(String mobileIds, String customername, String delivername, String deliverphone, Integer num) throws UnsupportedEncodingException {

		// strMsg = new
		// String(strMsg.getBytes("ISO-8859-1"),"UTF-8");//web服务端只接受UTF—8方式的编码

		try {
			SmsConfig smsConf = smsConfigDAO.getAllSmsConfig();
			if (smsConf != null) {
				if (smsConf.getIsOpen() == 1) {
					if (smsConf.getTemplatecontent() == null || "".equals(smsConf.getTemplatecontent())) {
						return "短信模板未做设置";
					}
					String strMsg = smsConf.getTemplatecontent().replaceAll("customername", customername).replaceAll("delivername", delivername).replaceAll("deliverphone", deliverphone);
					wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), mobileIds, strMsg, num.intValue());
					// 获取短信剩余条数，剩余warningSwitch条时发送预警
					Integer strRet = wmgwLocator.getwmgwSoap().mongateQueryBalance(smsConf.getName(), smsConf.getPassword());
					if (smsConf.getMonitor() == 1) {// 判断是否打开监控开关 flage == 1
						if (strRet < smsConf.getWarningcount()) {// 判断是否符合预警条件
																	// count
							if (warning) {// 判断是否已经预警过
								String warningMobileIds = smsConf.getPhone(); // 预警手机号
								String warningStrMsg = smsConf.getWarningcontent(); // 预警内容
								wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(smsConf.getName(), smsConf.getPassword(), warningMobileIds, warningStrMsg, warningMobileIds.split(",").length);
								warning = false;
							}
						} else {// 当重新充值以后，将重置预警状态
							warning = true;
						}
					}
				}
			} else {
				return "数据表没有数据，没做账户配置";
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return "接口调用发生异常";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "接口调用发生异常";
		}
		return "发送短信成功";
	}
}
