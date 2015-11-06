<%@page import="cn.explink.domain.PrintView"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	CwbOrder cwbOrder = (CwbOrder) request.getAttribute("cwbOrder");
	String query = (String) request.getAttribute("query");
	if (query == null) {
		query = "";
	}
    String cwb = "";
    int cwbordertypeid = 0;
    if (cwbOrder != null) {
    	cwbordertypeid = cwbOrder.getCwbordertypeid();
    	if (cwbordertypeid == 4 || cwbordertypeid == 5) {
    		cwb = cwbOrder.getCwb();
    	}
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OXO订单运单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function cwbfind(){
	var cwb = $("#cwb").val();
	if (cwb == null || '' == cwb) {
		alert("请输入订单号！");
		return;
	}
	$("#searchForm").submit();
}

function bdprint(){
	var printcwb = $("#printcwb").val();
	if (printcwb == null || '' == printcwb) {
		alert("没有要打印的订单！");
		return;
	} 
	$("#cwbbillprintForm").submit();
}

$(function() {
	var cwb = $("#printcwb").val();
	var query = $("#query").val();
	var cwbordertypeid = $("#cwbordertypeid").val();
	if (query != null && query != '') { //查询
		if (cwbordertypeid == 0) {
			alert("不存在该订单！");
		} else if (cwb == null || cwb == '') {
			alert("非OXO、OXO_JIT类型的订单不能打印！");
		}
	}
})
</script>
</head>
<body style="background: #f5f5f5">
	<div class="right_box">
		<div class="inputselect_box">
			<span> </span>
			<form action="1" method="post" id="searchForm">
				订单号： <input type="text" name="cwb" id="cwb" value=""
					class="input_text1"><input type="button" id="find"
					onclick="cwbfind();" value="查询" class="input_button2" /> <input
					type="button" onclick="bdprint();" value="打印" class="input_button2" />
				&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;采用A4纸打印，激光or针式打印机
			</form>
		</div>
		<div style="height: 26px;"></div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0"
			class="table_1">
			<tr class="font_1">
				<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">收货人</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">电话</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">收货地址</td>
			</tr>
		</table>

		<table width="100%" border="0" cellspacing="1" cellpadding="0"
			class="table_2" id="gd_table">
			<%if (cwbOrder != null && (cwbordertypeid == 4 || cwbordertypeid == 5)) {%>
			<tr class="font_1">
				<td width="25%" align="center" valign="middle"><%=cwbOrder.getCwb()%></td>
				<td width="25%" align="center" valign="middle"><%=cwbOrder.getConsigneename()%></td>
				<td width="20%" align="center" valign="middle"><%=cwbOrder.getConsigneemobile()%></td>
				<td width="20%" align="center" valign="middle"><%=cwbOrder.getConsigneeaddress()%></td>
			</tr>
			<%}%>
		</table>
		<form action="<%=request.getContextPath() %>/cwboxodetail/oxobillprint" method="post" id="cwbbillprintForm" >
	<input type="hidden" id="query" name="query" value="<%=query %>"/>
	<input type="hidden" name="printcwb" id="printcwb" value="<%=cwb %>" />
	<input type="hidden" name="cwbordertypeid" id="cwbordertypeid" value="<%=cwbordertypeid %>" />
	</form>
</body>
</html>
