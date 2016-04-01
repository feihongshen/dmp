/**
 * 
 */
package org.tempuri;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.util.FinalVar;

/**
 * @author Administrator
 * 
 */
public class WebServiceImpl implements WebService {
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceImpl.class);
	
	private static ResourceBundle rbint = ResourceBundle.getBundle("jrtcms");
	private String userName;
	private String password;
	private static boolean warning = true;// 警告标识，如果发送过警告信息，则记录为false

	private static WmgwLocator wmgwLocator = new WmgwLocator();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * mobileIds手机号可以以英文半角逗号分割，多少个手机号 num就是多少
	 */
	public String sendSms(String mobileIds, String strMsg, Integer num) throws UnsupportedEncodingException {

		// strMsg = new
		// String(strMsg.getBytes("ISO-8859-1"),"UTF-8");//web服务端只接受UTF—8方式的编码
		try {
			wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(userName, password, mobileIds, strMsg, num.intValue());

			// 获取短信剩余条数，剩余warningSwitch条时发送预警
			Integer strRet = wmgwLocator.getwmgwSoap().mongateQueryBalance(userName, password);
			if ("Y".equalsIgnoreCase(rbint.getString("warningSwitch"))) {// 判断是否打开监控开关
				if (strRet < Integer.parseInt(rbint.getString("warningMinNum"))) {// 判断是否符合预警条件
					if (warning) {// 判断是否已经预警过
						String warningMobileIds = rbint.getString("warningMobileIds");
						String warningStrMsg = rbint.getString("warningStrMsg").replace("{strRet}", strRet.toString());
						wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(userName, password, warningMobileIds, warningStrMsg, warningMobileIds.split(",").length);
						warning = false;
					}
				} else {// 当重新充值以后，将重置预警状态
					warning = true;
				}
			}
		} catch (RemoteException e) {
			logger.error("", e);
			return FinalVar.INTERFACE_ERROR;
		} catch (ServiceException e) {
			logger.error("", e);
			return FinalVar.INTERFACE_ERROR;
		}
		return FinalVar.INTERFACE_OK;
	}

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		WmgwLocator wmgwLocator = new WmgwLocator();
		String strArgs[] = new String[10];
		strArgs[0] = "J00348"; // 帐号
		strArgs[1] = "142753"; // 密码
		strArgs[2] = "15201231082";// 手机号
		strArgs[3] = "下面A+B C"; // 测试信息
		strArgs[4] = "1"; // 手机个数
		strArgs[5] = "*"; // 子端口
		String strMsg = new String(strArgs[3].getBytes("UTF-8"));// web服务端只接受UTF—8方式的编码
		// mongateCsSendSmsEx
		try {
			logger.info("Test mongateCsSendSmsEx ...");
			logger.info("back value is :" + wmgwLocator.getwmgwSoap().mongateCsSendSmsEx(strArgs[0], strArgs[1], strArgs[2], strMsg, Integer.valueOf(strArgs[4]).intValue()));
			logger.info("send mongateCsSendSmsEx end !");
		} catch (RemoteException e) {
			logger.error("", e);
		} catch (ServiceException e) {
			logger.error("", e);
		}

		// mongateCsSendSmsExNew
		try {
			logger.info("Test mongateCsSendSmsExNew ...");
			logger.info("back value is :" + wmgwLocator.getwmgwSoap().mongateCsSpSendSmsNew(strArgs[0], strArgs[1], strArgs[2], strMsg, Integer.valueOf(strArgs[4]).intValue(), strArgs[5]));
			logger.info("send mongateCsSendSmsExNew end !");
		} catch (RemoteException e) {
			logger.error("", e);
		} catch (ServiceException e) {
			logger.error("", e);
		}

		// mongateCsGetStatusReportExEx
		try {
			logger.info("Test mongateCsGetStatusReportExEx ...");
			String[] strRet = wmgwLocator.getwmgwSoap().mongateCsGetStatusReportExEx(strArgs[0], strArgs[1]);
			logger.info("back value is :");
			if (strRet != null) {
				for (int i = 0; i < strRet.length; ++i) {
					logger.info(strRet[i]);
				}
			} else {
				logger.info("null");
			}
			logger.info("send mongateCsGetStatusReportExEx end !");
		} catch (RemoteException e) {
			logger.error("", e);
		} catch (ServiceException e) {
			logger.error("", e);
		}

		// mongateQueryBalance
		try {
			logger.info("Test mongateQueryBalance ...");
			logger.info("back value is :" + wmgwLocator.getwmgwSoap().mongateQueryBalance(strArgs[0], strArgs[1]));
			logger.info("send mongateQueryBalance end !");
		} catch (RemoteException e) {
			logger.error("", e);
		} catch (ServiceException e) {
			logger.error("", e);
		}

		// mongateCsGetSmsExEx
		try {
			logger.info("Test mongateCsGetSmsExEx ...");
			String[] strRet = wmgwLocator.getwmgwSoap().mongateCsGetSmsExEx(strArgs[0], strArgs[1]);
			logger.info("back value is :");
			if (strRet != null) {
				for (int i = 0; i < strRet.length; ++i) {
					logger.info(strRet[i]);
				}
			} else {
				logger.info("null");
			}
			logger.info("send mongateCsGetSmsExEx end !");
		} catch (RemoteException e) {
			logger.error("", e);
		} catch (ServiceException e) {
			logger.error("", e);
		}
	}
}
