package org.springframework.security.web.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * <p>
 * In the pre-authenticated authentication case (unlike CAS, for example) the
 * user will already have been identified through some external mechanism and a
 * secure context established by the time the security-enforcement filter is
 * invoked.
 * <p>
 * Therefore this class isn't actually responsible for the commencement of
 * authentication, as it is in the case of other providers. It will be called if
 * the user is rejected by the AbstractPreAuthenticatedProcessingFilter,
 * resulting in a null authentication.
 * <p>
 * The <code>commence</code> method will always return an
 * <code>HttpServletResponse.SC_FORBIDDEN</code> (403 error).
 *
 * @see org.springframework.security.web.access.ExceptionTranslationFilter
 *
 * @author Luke Taylor
 * @author Ruud Senden
 * @since 2.0
 */
@Component
public class Http403ForbiddenEntryPoint implements AuthenticationEntryPoint {
	private static final Log logger = LogFactory.getLog(Http403ForbiddenEntryPoint.class);

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	/**
	 * Always returns a 403 error code to the client.
	 */
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException, ServletException {
		if (securityContextHolderStrategy.getContext().getAuthentication() == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Pre-authenticated entry point called. Rejecting access");
		}
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");

	}

}
