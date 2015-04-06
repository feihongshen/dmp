<%@page import="cn.explink.domain.FinanceAudit"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.FinanceAuditTypeEnum"%>
 <%
 Customer customer = request.getAttribute("customer")==null?new Customer():(Customer)request.getAttribute("customer");
 CustomWareHouse customWareHouse = request.getAttribute("customerwarehouse")==null?new CustomWareHouse():(CustomWareHouse)request.getAttribute("customerwarehouse");
 CwbOrderTypeIdEnum cwbOrderType = request.getAttribute("cwbOrderType")==null?CwbOrderTypeIdEnum.Peisong:(CwbOrderTypeIdEnum)request.getAttribute("cwbOrderType");
 FinanceAudit fa = request.getAttribute("fa")==null?null:(FinanceAudit)request.getAttribute("fa");
 int type=request.getAttribute("type")==null?1:Integer.parseInt(request.getAttribute("type").toString());
 %>
<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>确认审核结算</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#paydatetime").datepicker();
});
</script>
</head>
<body style="background:#f5f5f5" >
<div class="right_box">
  <div class="right_title">
	<form action="" method="post">
    <table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
	   <tbody>
	   <%if(fa==null){ %>
	    <tr height="30" >
	   		<td width="50%" align="left" valign="middle" colspan="2">${error}</td>
		</tr>
	   <%}else{ %>
	   <tr height="30" >
		   	<td colspan="2" width="50%" align="center" valign="middle"><img src="<%=request.getContextPath() %>/images/u42_normal.png" width="135" height="45"></td>
		</tr>
		<tr>
			<td align="right" valign="middle">当前交款金额：</td>
			<td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getPayamount() %></strong>元</label></td>
		  </tr>
		
	   <tr>
	   	<td width="50%" align="right" valign="middle">供货商：<strong><%=customer.getCustomername() %></strong></td>
			<td align="left" valign="middle">供货商发货仓库：<strong><%=customWareHouse.getCustomerwarehouse()==null?"全部":customWareHouse.getCustomerwarehouse() %></strong>
			</td>
		</tr>
		<tr>
		  <td align="right" valign="middle">付款流水号：</td>
		  <td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getPaynumber() %></strong></label></td>
		</tr>
		<tr>
		  <td align="right" valign="middle">应付款：</td>
		  <td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getShouldpayamount() %></strong>元</label></td>
		</tr>
		<tr>
		  <td align="right" valign="middle">实付款：</td>
		  <td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getPayamount() %></strong>元</label></td>
		</tr>
		<tr>
		  <td align="right" valign="middle">当前欠款：</td>
		  <td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getQiankuanamount() %></strong>元</label></td>
		</tr>
		
		<tr>
		  <td align="right" valign="middle">付款备注：</td>
		  <td align="left" valign="middle"><label for="textfield2"><strong><%=fa.getPayremark() %></strong></label></td>
		</tr>
		<%} %>
	</table>
    <table width="100%" border="0" cellspacing="10" cellpadding="0" class="table_5">
  <tr>
    <th scope="col">
    <%if(FinanceAuditTypeEnum.AnFaHuoShiJian.getValue() == type){ %>
    <input name="button2" onclick="location.href='paymentMonitorbyemailtime/1'" type="button" class="input_button1" id="button2" value="返回">
    <%}else if(FinanceAuditTypeEnum.AnPeiSongJieGuo.getValue() == type){ %>
    <input name="button2" onclick="location.href='paymentfordeliverystate/1'" type="button" class="input_button1" id="button2" value="返回">
    <%} else if(FinanceAuditTypeEnum.TuiHuoKuanJieSuan_RuZhang.getValue() == type || FinanceAuditTypeEnum.TuiHuoKuanJieSuan_ChuZhang.getValue() == type ){ %>
    <input name="button2" onclick="location.href='paymentBack/1'" type="button" class="input_button1" id="button2" value="返回">
    <%} %>
    
    </th>
  </tr>
</table>

    </form>
	
	
	</div>

</div>
</body>
</html>






