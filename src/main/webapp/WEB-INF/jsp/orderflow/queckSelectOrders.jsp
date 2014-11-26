<%@page import="net.sf.cglib.core.Local"%>
<%@page import="cn.explink.support.transcwb.TranscwbView"%>
<%@page import="cn.explink.controller.TranscwbOrderFlowView"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Map<String, Object>> selectOrdersList = request.getAttribute("selectOrdersList")==null?null:(List<Map<String, Object>>)request.getAttribute("selectOrdersList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>多订单追踪</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(function() {
	$(".table_zd").toggle(function(){
		$(this).siblings().css("display","none");
	},function(){
		$(this).siblings().css("display","block");
	})
});
</script>
</head>

<body marginwidth="0" marginheight="0">

<%
	if(selectOrdersList!=null&&!selectOrdersList.isEmpty()){
		for(int i=0;i<selectOrdersList.size();i++){
			QuickSelectView view = (QuickSelectView)selectOrdersList.get(i).get("view");
			String scancwb=selectOrdersList.get(i).get("scancwb").toString();
			List<TranscwbView> transcwbList=selectOrdersList.get(i).get("transcwbList")==null?null:(List<TranscwbView>)selectOrdersList.get(i).get("transcwbList");
			List<AorderFlowView> aorderFlowViews=selectOrdersList.get(i).get("aorderFlowViews")==null?null:(List<AorderFlowView>)selectOrdersList.get(i).get("aorderFlowViews");
%>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
	<tbody><tr>
		<td valign="top">
		<div style=" overflow-y:auto; overflow-x:hidden">
		<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_zd">
			<tbody>
				<tr class="font_1">
				<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
					<h1>&gt;&gt;&nbsp;&nbsp;&nbsp;&nbsp;订单号：<%=view.getCwb()%></h1></td>
			</tr>
			</tbody></table>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tbody>
				<tr class="font_1">
					<td width="10%" align="left" valign="middle">
					订单号：<strong><%=view.getCwb()%></strong>
					&nbsp;&nbsp;当前状态：<strong><%if(view.getFlowordertypeMethod()=="已审核"){%>审核为：<%=view.getDeliveryStateText() %><%}else if(view.getFlowordertypeMethod()=="已反馈") {%>反馈为：<%=view.getDeliveryStateText() %><%}else{ %><%=view.getFlowordertypeMethod() %><%} %></strong>
					&nbsp;&nbsp;配送状态：<strong><%=view.getCwbdelivertypeStr() %></strong>
					&nbsp;&nbsp;订单状态：<strong><% for (CwbStateEnum cwb:CwbStateEnum.values()){if(cwb.getValue()==view.getCwbstate()){out.print(cwb.getText());}}%></strong>
					&nbsp;&nbsp;运单号：<strong>
									<%if(scancwb.equals(view.getCwb())){ %>
										<%=view.getTranscwb() %>
									<%}else if(transcwbList!=null&&transcwbList.size()>0){ %>
										<%if(view.getTranscwborderFlowList() != null && view.getTranscwborderFlowList().size()>0){ %>
											<%=view.getTranscwborderFlowList().get(0).getScancwb() %>
										<%}else{ %>
										<%=scancwb %>
										<%} %>
									<%}else{%>
									<%=view.getTranscwb() %>
									<%} %>
									</strong>
					<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
					  <tr>
							<td width="120" align="center" bgcolor="#f1f1f1">操作时间</td>
							<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
							<td align="center" bgcolor="#f1f1f1">操作详情</td>
					  </tr>
					  <%if(aorderFlowViews!=null&&!aorderFlowViews.isEmpty()){ %> 
						   <% for(AorderFlowView af : aorderFlowViews){
							   if(af.getContent()!=null){%>
					  <tr>
							<td align="center"><%=af.getTime().substring(0,19) %></td>
							<td align="center"><%=af.getUsername()%></td>
						    <td><%=af.getContent().replaceAll("null", "") %></td>
	                  </tr>	
					  <%}}}%> 
					  </table>
					</td>
				</tr>
			</tbody>
		</table>
		</div></td>
	</tr>
</tbody>
</table>
<%}}else{%>
<script>
	alert("订单不存在！");
</script>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
	<tbody><tr>
		<td valign="top">
		<div style=" overflow-y:auto; overflow-x:hidden">
		<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_zd">
			<tbody>
				<tr class="font_1">
				<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
					<h1>订单号：</h1></td>
			</tr>
			</tbody></table>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tbody>
				<tr class="font_1">
					<td width="10%" align="left" valign="middle">
					订单号：<strong></strong>
					&nbsp;&nbsp;当前状态：<strong></strong>
					&nbsp;&nbsp;配送状态：<strong></strong>
					&nbsp;&nbsp;订单状态：<strong></strong>
					&nbsp;&nbsp;运单号：<strong></strong>
					<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
					  <tr>
							<td width="120" align="center" bgcolor="#f1f1f1">操作时间</td>
							<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
							<td align="center" bgcolor="#f1f1f1">操作详情</td>
					  </tr>
					  </table>
					</td>
				</tr>
			</tbody>
		</table>
		</div></td>
	</tr>
</tbody>
</table>

<%}%>
</body></html>