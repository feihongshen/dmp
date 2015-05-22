<%@page import="cn.explink.domain.FinanceAudit"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
 <%
 Customer customer = request.getAttribute("customer")==null?new Customer():(Customer)request.getAttribute("customer");
 CustomWareHouse customWareHouse = request.getAttribute("customerwarehouse")==null?null:(CustomWareHouse)request.getAttribute("customerwarehouse");
 String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 FinanceAudit financeChargeDetail = request.getAttribute("financeChargeDetail")==null?null:(FinanceAudit)request.getAttribute("financeChargeDetail");
 %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>付款</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#payqiankuandatetime").datetimepicker({
		changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
});

function checkfeereg(){
	var istrue = /^[0-9\.]+$/.test($("#payqiankuanamount").val());
	
	if($("#payqiankuanamount").val().length==0){
		alert("请认真填写上交欠款金额！");
		return false;
	}
	if(!istrue){
		alert("上交欠款金额格式不正确！");
		return false;
	}
	if($("#paynumber").val().length>100){
		alert("付款流水号最多100个！");
		return false;
	}
	if($("#payremark").val().length>500){
		alert("备注最多500字！");
		return false;
	}
	return true;
}

</script>
</head>
<body style="background:#f5f5f5" >
<div class="right_box">
  <div class="right_title">
	<form id="financechargeForm" action="<%=request.getContextPath() %>/funds/financecharge/<%=financeChargeDetail.getId() %>" method="POST" onsubmit="if(checkfeereg()){$('#financechargeForm').submit();}return false;">
    <table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
	   <tbody><tr height="30" >
	   	<td width="50%" align="left" valign="middle">供货商：<strong><%=customer.getCustomername() %></strong></td>
			<td align="left" valign="middle">供货商发货仓库：<strong><%=customWareHouse==null?"全部":customWareHouse.getCustomerwarehouse() %></strong>
			<input type="hidden" name="auditdatetime" value="<%=nowTime %>" />
			<input type="hidden" name="auditCustomerid" value="<%=customer.getCustomerid() %>" />
			<input type="hidden" name="auditCustomerwarehouseid" value="<%=customWareHouse==null?-1:customWareHouse.getWarehouseid() %>" />
			</td>
		</tr>
		
		<tr>
			<td align="left" valign="middle">订单数：<%=financeChargeDetail.getCwbcount() %>票</td>
			<td align="left" valign="middle">应付金额：<strong><%=financeChargeDetail.getShouldpayamount() %></strong>元</td>
			
		  </tr>
		  <tr>
			  <td align="left" valign="middle">实际付款金额：<strong><%=financeChargeDetail.getPayamount() %></strong>元</td>
			<td align="left" valign="middle">上交欠款时间：<input type="text" id="payqiankuandatetime" name="payqiankuandatetime" value=""/></td>
			
		  </tr>
		<tr>
			<td align="left" valign="middle">上交欠款金额：<input id="payqiankuanamount" name="payqiankuanamount" type="text" value="<%=financeChargeDetail.getShouldpayamount().subtract(financeChargeDetail.getPayamount())%>"/>元</td>
			<td align="left" valign="middle">付款方式：<select name="paytype" id="paytype">
			    <option value="1">现金</option>
			    <option value="2">POS</option>
	        </select></td>
		  
		  </tr>
	  <tr>
		  <td align="left" valign="middle">付款流水号：<input type="text" name="paynumber" id="paynumber" size="40"></td>
		  <td rowspan="2" align="left" valign="top">付款备注：<textarea name="payremark" cols="50" rows="5" id="payremark"></textarea></td>
	  </tr>
		<tr>
			<td align="left" valign="middle">审核时间：<%=nowTime %></td>
			
		</tr>
		</tbody>
	</table>
    <table width="100%" border="0" cellspacing="10" cellpadding="0" class="table_5">
	  <tr>
	    <th scope="col"><input name="button" type="submit" class="input_button1" id="button" value="确认付款">&nbsp;&nbsp;
	      <input name="button2" type="button" class="input_button1" id="button2" onclick="location.href='<%=request.getContextPath()%>/funds/financeChargeList/1'" value="返回"></th>
	  </tr>
	</table>

    </form>
	</div>
</div>
</body>
</html>






