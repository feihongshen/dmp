<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import="cn.explink.util.HealthCheck"%>
<%@ page import="cn.explink.util.impl.HealthCheckImpl"%>
<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%
	String status = "FAILED";
	HealthCheck healthCheck = HealthCheckImpl.get_instance();
	if (healthCheck != null && healthCheck.doCheck(1) > 0) {
		status = "OK";
	}
	out.print(status);
	session.invalidate();
%>
