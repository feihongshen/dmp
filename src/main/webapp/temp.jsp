<%@page import="cn.explink.domain.DeliveryState"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.dao.OrderFlowDAO"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.dao.CwbDAO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<% 
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
		CwbDAO cwbDAO=ctx.getBean(CwbDAO.class);
		OrderFlowDAO orderFlowDAO = (OrderFlowDAO) ctx.getBean("orderFlowDAO"); 
		String orderflowids=request.getParameter("ids");
		for(String orderflowid:orderflowids.split(",")){
			OrderFlow orderFlow= orderFlowDAO.getOrderFlowById(Long.parseLong(orderflowid));
			CwbOrder cwbOrder=cwbDAO.getCwbByCwb(orderFlow.getCwb());
		}

%> 

</body>
</html>