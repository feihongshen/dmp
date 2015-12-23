<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.VO.express.ExpressQuickQueryView"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
ExpressQuickQueryView view = (ExpressQuickQueryView)request.getAttribute("view");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

</HEAD>

<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  style="font-size:14px">
		<tbody><tr>
		<td valign="top">
		
		<%if(view !=null ){ %>
			<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px">
			<tbody>
				<tr class="font_1">
					<td height="26" align="left" valign="middle" bgcolor="#00AEF0"><h1>&nbsp;预订单详情</h1></td>
				</tr>
				<tr>
					<td>
						<div style=" overflow-y:auto; overflow-x:hidden">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="right_set1"  style="font-size:14px"><!-- table_5 --> 
								<tbody>
									<tr>
										<td width="50%" bgcolor="#EBFFD7"><b>预订单号：</b><%=view.getPreOrderNo()%><input type="hidden" id="preOrder"  value="<%=view.getPreOrderNo()%>"></td>
										
										<td bgcolor="#EBFFD7"><b>执行状态：</b><%=view.getExcuteState()%></td>
									</tr>
									<tr>
										<td width="50%"  bgcolor="#EBFFD7"><b>发件人：</b><%=view.getSendPerson()%></td>
										
										<td bgcolor="#EBFFD7"><b>手机号码：</b><%=view.getCellphone()%></td>
									</tr>
									<tr>
										<td width="50%" bgcolor="#EBFFD7"><b>固定电话：</b><%=view.getTelephone()%></td>
										<td bgcolor="#EBFFD7"><b>取件地址：</b><%=view.getCollectAddress()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>原因：</b><%=view.getReason()%></td>
										<td bgcolor="#EBFFD7"><b>分配站点名称：</b><%=view.getBranchName()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>处理预订单时间：</b><%=view.getHandleTime()%></td>
										<td bgcolor="#EBFFD7"><b>处理人姓名：</b><%=view.getHandleUserName()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>分配小件员的时间：</b><%=view.getDistributeDelivermanTime()%></td>
										<td bgcolor="#EBFFD7"><b>生成时间：</b><%=view.getCreateTime()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>预约时间：</b><%=view.getArrangeTime()%></td>
										<td bgcolor="#EBFFD7"><b>小件员姓名：</b><%=view.getDelivermanName()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>分配小件员的操作人姓名：</b><%=view.getDistributeUserName()%></td>
										<td bgcolor="#EBFFD7"><b>快递单号：</b><%=view.getOrderNo()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>一级原因：</b><%=view.getFeedbackFirstReason()%></td>
										<td bgcolor="#EBFFD7"><b>二级原因：</b><%=view.getFeedbackSecondReason()%></td>
									</tr>
										<tr>
										<td bgcolor="#EBFFD7"><b>反馈备注：</b><%=view.getFeedbackRemark()%></td>
										<td bgcolor="#EBFFD7"><b>反馈人姓名：</b><%=view.getFeedbackUserName()%></td>
									</tr>
									 <tr>
										<td bgcolor="#EBFFD7"><b>反馈时间：</b><%=view.getFeedbackTime() %><b></td>
										<td bgcolor="#EBFFD7"><b>预计下次揽件时间：</b><%=view.getNextPickTime()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>托物资料-其他：</b><%=view.getOther()%></td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>

				</tbody>
				</table>
				<%} %>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>