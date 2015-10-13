<%@page import="cn.explink.domain.orderflow.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<OrderFlow> orderFlowList = (List<OrderFlow>)request.getAttribute("orderFlowList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<style type="text/css">
div{ float:left; padding:0px 5px; list-style:none; width: 650px; }
ul{ float:left; padding:0px 5px; list-style:none; border:1px;border-color:blue; margin-left: 10px;}
li{ float:left; padding:0px 5px; list-style:none; border:1px;border-color:blue; margin-left: 10px;}
span{}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
</HEAD>
<BODY>
<div>
<% for(OrderFlow of : orderFlowList){ %>
		<%=of.toHtml() %>		
<%} %>
</div>
</BODY>
</HTML>
