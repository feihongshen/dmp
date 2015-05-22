<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
String cwb = request.getAttribute("cwb")==null?"":(String)request.getAttribute("cwb");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
</HEAD>

<body onLoad="$(&#39;#orderSearch&#39;).focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" style="font-size:14">
		<tbody>
		<tr>
			<td valign="top" ><iframe src="<%=request.getContextPath()%>/order/queckSelectOrderleft/<%=cwb %>" width="100%" height="920"  id="queckSelectOrder_AREA_LEFT"></iframe>
			</td>
			<td valign="top" width="50%" >
			<iframe src="<%=request.getContextPath()%>/order/queckSelectOrderright/<%=cwb %>" width="100%" height="920"  id="queckSelectOrder_AREA_RIGHT"></iframe>
			</td>
		</tr>
		</tbody>
	</table>
</body>
</html>