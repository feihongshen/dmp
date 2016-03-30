/**
 * 
 */
package org.tempuri;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * 
 */
public class WebServiceTest {

	private static Logger logger = LoggerFactory.getLogger(WebServiceTest.class);
	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		WmgwLocator wmgwLocator = new WmgwLocator();
		String strArgs[] = new String[10];
		strArgs[0] = "JR008"; // 帐号 J00348 JR008
		strArgs[1] = "123456"; // 密码 142753 123456
		strArgs[2] = "15201231082";// 手机号
		strArgs[3] = "测试信息1"; // 测试信息
		strArgs[4] = "1"; // 手机个数
		strArgs[5] = "*"; // 子端口
		String strMsg = new String(strArgs[3].getBytes("UTF-8"));// web�����ֻ����UTF��8��ʽ�ı���
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
