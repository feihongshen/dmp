<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");
String remand = request.getAttribute("remand").toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<title>订单查询</title>
<script type="text/javascript">
$(document).ready(function() {
	$("#cwb").keydown(function(event){
		if(event.keyCode==13) {
			if($("#cwb").val()==""){
				alert('请输入订单号');
				return false;
			}
			if($("#validateCode").val()==""){
				alert('请输入验证码');
				return false;
			}else{
				window.location.href="<%=request.getContextPath()%>/order/orderFlowQueryByCwb/?cwb="+$("#cwb").val()+"&validateCode="+$("#validateCode").val();
			}
		}
	});
	$("#validateCode").keydown(function(event){
		if(event.keyCode==13) {
			if($("#cwb").val()==""){
				alert('请输入订单号');
				return false;
			}
			if($("#validateCode").val()==""){
				alert('请输入验证码');
				return false;
			}
			else{
				window.location.href="<%=request.getContextPath()%>/order/orderFlowQueryByCwb/?cwb="+$("#cwb").val()+"&validateCode="+$("#validateCode").val();
			}
		}
	});
	$("#btnsearch").click(function(){
		if($("#cwb").val()==""){
			alert('请输入订单号');
			return;
		}
		if($("#validateCode").val()==""){
			alert('请输入验证码');
			return;
		}
		window.location.href="<%=request.getContextPath()%>/order/orderFlowQueryByCwb/?cwb="+$("#cwb").val()+"&validateCode="+$("#validateCode").val();
		
	});
});
</script>
</head>
<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:560px">
		<tbody>
			<tr>
			<%if(view !=null ){ %>
				<td valign="top">
					<div style="height:467px;  overflow-y:auto; overflow-x:hidden">
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tbody>
								<tr class="font_1">
									<td width="30%" height="26" align="center" valign="middle" bgcolor="#eef6ff" style="font-size:20;">&nbsp;订单查询</td>
								</tr>
								<tr >
									<td width="30%" height="26" align="center" valign="middle" bgcolor="#eef6ff">&nbsp;请输入订单号：
										<input type="text" name="cwb" id="cwb"  class="input_text2"  />　
										<label for="textfield">验证码：</label>
										<input name="validateCode" type="text"  id="validateCode" maxlength="30"/>
										<img src="<%=request.getContextPath()%>/image?a=<%=System.currentTimeMillis() %>">
										<input type="button" name="btnsearch" value="查询" id="btnsearch"/>
										<font color = "red"><%=remand %></font>
									</td>
								</tr>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle">
									 订单号：<strong><%=view.getCwb() %></strong>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px">
											<tr>
											    <td width="20%" bgcolor="#f1f1f1">　</td>
												<td width="200" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
											
										   	<% for(OrderFlowView flow:view.getOrderFlowList()){%>
											 <tr>
											 	<td width="20%">　</td>
												<td align="center"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
											    <td><%=flow.getDetail() %></td>
                                             </tr>	
										    <%}%> 
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			<%} %>
				
			</tr>
		</tbody>
	</table>

</body>
</html>