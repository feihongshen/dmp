<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");

String isSearchFlag=request.getAttribute("isSearchFlag")==null?"":request.getAttribute("isSearchFlag").toString();

String remand = request.getAttribute("remand")==null?"":request.getAttribute("remand").toString();
%>
<head>
<title>订单查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script>

$(document).ready(function() {
	$("#cwb").keydown(function(event){
		if(event.keyCode==13) {
			if($("#cwb").val()==""){
				alert('请输入订单号');
				return false;
			}
			$("#seachForm").submit();
		}
	});
	$("#btnsearch").click(function(){
		if($("#cwb").val()==""){
			alert('请输入订单号');
			return;
		}
		$("#seachForm").submit();
		
	});
});

</script>
</head>

<body onLoad="$('#cwb').focus();">
<table width="628" border="0" cellspacing="0" cellpadding="0"  align="center" class="table_2" style="height:567px">
	<tr align="center">
		<td width="100%" valign="top">
		<div style="height:567px; font-size:12px; overflow-y:auto; overflow-x:hidden">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="font-size:12px">
			<tr class="font_1">
				<td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff" style ="font-size: 20px;">&nbsp;
				订单查询(客服热线电话:<%=request.getAttribute("telephone") %>)</td>
			</tr>
			<tr>
				<td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff">
				<form action="<%=request.getContextPath()%>/order/orderFlowQueryByCwb" id="seachForm" method="post">
				     &nbsp;请输入订单号：
					<label for="textfield2"></label>
					<input type="text" name="cwb" id="cwb"  class="input_text2"  />　
					<input type="button" name="btnsearch" value="查询" id="btnsearch"/>
					</form>
				</td>
			</tr>
			<tr>
			  <td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff">
			      <font color = "red"><%=remand %></font>
			  </td>
			</tr>
			<tr>
				<td width="100%" align="center" valign="top" valign="middle" >
					<%if(view != null){ %>
					<table width="100%" border="0" cellspacing="1" cellpadding="0"   class="right_set2">
						<tr>
							<td width="20%"  align="left" valign="middle">
								<p>订单号：<font color="red"><%=view.getCwb() %></font></p>
							</td>
							<td colspan="2" >
							</td>
						</tr>
						<tr>
							<td width="20%" align="left"  >操作时间</td>
							<td width="10%" align="center"  >操作人</td>
						    <td width="70%" align="left" >操作过程
						    </td>
                        </tr>
						 <% for(OrderFlowView flow:view.getOrderFlowList()){%>
						<tr>
							<td align="left" ><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
							<td align="center" ><%=flow.getOperator().getRealname() %></td>
						    <td><%=flow.getDetail()%>
						    </td>
                        </tr>	
						<%}
						}%> 
					</table>
				</td>
			</tr>
		</table>
		</div></td>
	</tr>
</table>
</body>
</html>