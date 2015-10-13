package org.tempuri;

import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 * 
 */
public interface WebService {

	public String sendSms(String mobileId, String strMsg, Integer num) throws UnsupportedEncodingException;
}
