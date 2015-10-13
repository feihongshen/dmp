/*
 * Copyright (c) 2013, S.F. Express Inc. All rights reserved.
 */
package cn.explink.b2c.gztl.webservice;

import javax.jws.WebService;

/**
 *
 *
 *
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID   DATE           PERSON          REASON
 *  1    2013-3-21      204062         Create
 * ****************************************************************************
 * </pre>
 *
 * @author 204062
 * @since 1.0
 */
@WebService
public interface GztlWebService {
	public String orderAndFeedbackApi(String code, String invokeMethod, String sign, String xml);
}
