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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");
List<TranscwbView> transcwbList = request.getAttribute("transcwbList")==null?null:(List<TranscwbView>)request.getAttribute("transcwbList");
String scancwb = (String)request.getAttribute("scancwb");
List<AorderFlowView> aorderFlowViews=(List<AorderFlowView>)request.getAttribute("aorderFlowViews");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
function gotoForm(cwb){
	window.location.href="<%=request.getContextPath()%>/order/queckSelectOrderright/"+cwb;
}
</script>
</HEAD>

<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2"  style="font-size:14px">
		<tbody><tr>
		<%if(view !=null ){ %>
				<td valign="top">
						<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px;">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
									<h1>订单过程</h1></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0" style="font-size:14px;">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="top">
									<div style=" overflow-y:auto; overflow-x:hidden">
									订单号：<strong><%=view.getCwb() %></strong>&nbsp;&nbsp;
									<font <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if> >订单号当前状态：</font><strong <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if>><%if(view.getFlowordertypeMethod()=="已审核"){%>审核为：<%=view.getDeliveryStateText() %><%}else if(view.getFlowordertypeMethod()=="已反馈") {%>反馈为：<%=view.getDeliveryStateText() %><%}else{ %><%=view.getFlowordertypeMethod() %><%} %></strong>
									<c:if test="${jdtype == 1}">
										&nbsp;&nbsp;<font>一票多件当前状态：<strong>${slowtranscwbtype}</strong></font>
									</c:if>
									<font <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if> >&nbsp;&nbsp;配送状态:</font><strong <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if>><%=view.getCwbdelivertypeStr() %></strong>
									<font <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if> >订单状态：</font><strong <c:if test="${jdtranstype == 1}"> style="display: none;"</c:if>>
										<% for (CwbStateEnum  cwb   : CwbStateEnum.values()) {if (cwb.getValue()==view.getCwbstate()) {%>
										<%=cwb.getText()%>
										<% }}%>
									</strong><br/>
									运单号：<strong>
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
									<c:if test="${jdtranstype == 1}">
										&nbsp;&nbsp;<font color="red">一票多件当前状态：</font><strong>${ypdjcurrentstate}</strong>
										&nbsp;&nbsp;<font color="red">运单状态：</font><strong>${transcwbstate}</strong>
									</c:if>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5"  style="font-size:14px;">
											<tr>
												<td width="150" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
											<%if(aorderFlowViews != null && aorderFlowViews.size()>0){ %> 
											  
											   <% for(AorderFlowView af : aorderFlowViews){if(af.getContent()!=null){%>
												<tr>
													<td align="center"><%=af.getTime().substring(0,19) %></td>
													<td align="center"><%=af.getUsername()%></td>
												    <td align="left"><%=af.getContent().replaceAll("null", "") %>
												    </td>
                                                 </tr>	
											<% }} }%> 
									</table>
									</div>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				<%} else{
					if(request.getParameter("cwb")!=null&&request.getParameter("cwb").length()>0){
				%>
					<script>
					alert("订单<%=request.getParameter("cwb") %>不存在！");
					$("#orderSearch").val(<%=request.getParameter("cwb") %>);
					</script>
				<%} %>
				<td valign="top">
						<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px;">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="top" bgcolor="#00AEF0">&nbsp;
									<h1>订单过程</h1></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0" style="font-size:14px;">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="top">
									订单号：<strong></strong>&nbsp;&nbsp;运单号：<strong></strong>&nbsp;&nbsp;订单号当前状态：<strong></strong>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px">
											<tr>
												<td width="136" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
				</td>
				<%} %>
			</tr>
		</tbody>
	</table>
</body>
</html>