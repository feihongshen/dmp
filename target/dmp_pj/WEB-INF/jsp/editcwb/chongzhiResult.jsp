<%@page import="cn.explink.domain.EdtiCwb_DeliveryStateDetail"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<EdtiCwb_DeliveryStateDetail> ecList = request.getAttribute("ecList")==null?new ArrayList<EdtiCwb_DeliveryStateDetail>():(List<EdtiCwb_DeliveryStateDetail>)request.getAttribute("ecList");
List<String> errorList = request.getAttribute("errorList")==null?new ArrayList<String>(): (List<String>)request.getAttribute("errorList");
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
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr>
		<td colspan="3">重置成功：<%if(ecList.size()>0){ %><%=ecList.size() %><%} %>单　重置失败<%if(errorList.size()>0) {%>：<%=errorList.size() %><%} %>单</td>
 	</tr>
 	<tr>
		<td colspan="3" align="center">
 			<input type="button" class="buttonnew" onclick="location.href='<%=request.getContextPath()%>/cwborder/toChongZhiStatus'"  value="返回" />
		</td>
 			 
 	</tr>
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>无法重置的订单号
 		</td>
 		<td>
 			当前所在环节 
 		</td>
 		<td>异常信息</td>
 	</tr>
 	<% if(errorList.size()>0){ for(String error :errorList){ %>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=error.split("_")[0] %></strong>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.getText(Integer.parseInt(error.split("_")[1])).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=error.split("_")[2] %></td>
 	</tr>
 	<%} }%>
 	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>重置成功的订单号
 		</td>
 		<td>
 			当时所在环节 
 		</td>
 		<td>重置为</td>
 	</tr>
 	<% if(ecList.size()>0){ for(EdtiCwb_DeliveryStateDetail ec :ecList){ %>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=ec.getDs().getCwb() %></strong>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.YiShenHe.getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.FenZhanLingHuo.getText() %></td>
 	</tr>
 	<%}} %>
	<tr>
		<td colspan="3" align="center">
 			<input type="button" class="buttonnew" onclick="location.href='<%=request.getContextPath()%>/cwborder/toChongZhiStatus'"  value="返回" />
		</td>
 			 
 	</tr>
</table>
</BODY>
</HTML>
