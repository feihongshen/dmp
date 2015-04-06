<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String [] cwbArray = (String[])request.getAttribute("cwbArray");
List<CwbOrder> allowCwb = (List<CwbOrder>)request.getAttribute("allowCwb");
List<CwbOrder> prohibitedCwb = (List<CwbOrder>)request.getAttribute("prohibitedCwb");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>修改订单</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<style type="text/css">
.buttonnew{
	width:180px;
	line-height:24px;
	height:24px;
	background-color:B6E0F8; 
	border:none; 
	cursor:pointer; 
	padding:0; 
	text-align:center 
}


</style>
</HEAD>
<BODY style="background:#f5f5f5">
<form id="searchForm" action ="<%=request.getContextPath()%>/editcwb/ChongZhiShenHeZhuangTai" method = "post">
<input type="hidden" name="requestUser" value="<%=request.getParameter("requestUser") %>" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr>
		<td colspan="3">要求重置订单总数：<%=cwbArray.length %>单　有效订单：<%=allowCwb.size()+prohibitedCwb.size() %>单　可以重置：<%=allowCwb.size() %>单　无法重置：<%=prohibitedCwb.size() %>单</td>
 	</tr>
 	<tr>
		<td colspan="3" align="center">
 			<input type="submit" class="buttonnew"  value="确认提交重置审核状态" />　<input type="button" class="buttonnew"  onclick="location.href='<%=request.getContextPath()%>/cwborder/toChongZhiStatus'"  value="返回" />
		</td>
 			 
 	</tr>
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>无法重置的订单号
 		</td>
 		<td>
 			当前所在环节 
 		</td>
 		<td>是否可以重置</td>
 	</tr>
 	<%for(CwbOrder co :prohibitedCwb){ %>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=co.getCwb() %></strong>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">[　禁止　] <%=co.getRemark1() %></td>
 	</tr>
 	<%} %>
 	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>可以重置的订单号
 		</td>
 		<td>
 			当前所在环节 
 		</td>
 		<td>是否可以重置</td>
 	</tr>
 	<%for(CwbOrder co :allowCwb){ %>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=co.getCwb() %></strong><input type="hidden" name="cwbs" value="<%=co.getCwb() %>" />
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">[　允许　]</td>
 	</tr>
 	<%} %>
	<tr>
		<td colspan="3" align="center">
 			<input type="submit" class="buttonnew"   value="确认提交重置审核状态" />　<input type="button" class="buttonnew"  onclick="location.href='<%=request.getContextPath()%>/cwborder/toChongZhiStatus'"  value="返回" />
		</td>
 			 
 	</tr>
</table>
</form>
</BODY>
</HTML>
