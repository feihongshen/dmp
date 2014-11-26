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
 CwbOrderTypeIdEnum cwbOrderType = request.getAttribute("cwbOrderType")==null?CwbOrderTypeIdEnum.Peisong:(CwbOrderTypeIdEnum)request.getAttribute("cwbOrderType");
 List<CwbOrder> coList = request.getAttribute("coList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("coList");
 BigDecimal countfee = request.getAttribute("countfee")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("countfee");
 String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 
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

function checkfeereg(){
	var istrue = /^[0-9\.]+$/.test($("#payamount").val());
	if($("#payamount").val().length==0){
		alert("请认真填写实付金额！");
		return false;
	}
	if(!istrue){
		alert("实付金额格式不正确！");
		return false;
	}
	if(parseFloat($("#payamount").val()) > parseFloat($("#shouldpayamount").val())){
		alert("实付金额不能大于应付金额！");
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
<body style="background:#eef9ff" >
<div class="right_box">
  <div class="right_title">
	<form id="financeauditForm" action="financeaudit" method="POST" onsubmit="if(checkfeereg()){$('#financeauditForm').submit();}return false;">
    <table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
	   <tbody><tr height="30" >
	   	<td width="50%" align="left" valign="middle">供货商：<strong><%=customer.getCustomername() %></strong></td>
			<td align="left" valign="middle">供货商仓库：<strong><%=customWareHouse==null?"全部":customWareHouse.getCustomerwarehouse() %></strong>
			<input type="hidden" name="auditdatetime" value="<%=nowTime %>" />
			<input type="hidden" name="auditCustomerid" value="<%=customer.getCustomerid() %>" />
			<input type="hidden" name="auditCustomerwarehouseid" value="<%=customWareHouse==null?-1:customWareHouse.getWarehouseid() %>" />
			<input type="hidden" name="auditCwbOrderType" value="<%=cwbOrderType.getValue() %>" />
			<input type="hidden" name="cwbs" value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %>" />
			<input type="hidden" name="type" value="<%=request.getParameter("type")==null?"0":request.getParameter("type") %>" />
			</td>
		</tr>
		
		<tr>
			<td align="left" valign="middle">订单类型：<%=cwbOrderType.getText() %></td>
			<td align="left" valign="middle">订单数：<%=coList.size() %>票</td>
		  </tr>
		  <tr>
			<td align="left" valign="middle">付款日期：<input type="text" id="paydatetime" name="paydatetime" value="" size="10" /></td>
			<td align="left" valign="middle">付款方式：<select name="paytype" id="paytype">
			    <option value="1">现金</option>
			    <option value="2">POS</option>
	        </select></td>
		  </tr>
		<tr>
		  <td align="left" valign="middle">应付金额：<strong><%=countfee %></strong>元</td>
		  <td align="left" valign="middle">实际付款金额：<input type="text" name="payamount" id="payamount" value="<%=countfee %>"  />
			<input type="hidden" name="shouldpayamount" id="shouldpayamount" value="<%=countfee %>" />  
		  </td>
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
    <%if("2".equals(request.getParameter("type"))){ %>
      <input name="button2" type="button" class="input_button1" id="button2" value="返回" onclick='window.location.href="<%=request.getContextPath()%>/funds/paymentfordeliverystate/1"'>
 <%}else if("1".equals(request.getParameter("type"))){ %>
     <input name="button2" type="button" class="input_button1" id="button2" value="返回" onclick='window.location.href="<%=request.getContextPath()%>/funds/paymentMonitorbyemailtime/1"'>
 <%} %>
 </th>
  </tr>
</table>

    </form>
	
	
	</div>

</div>
</body>
</html>






