<%@page import="cn.explink.domain.AccountArea,cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("customeres");
	AccountArea accountArea = (AccountArea)request.getAttribute("accountArea");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;border-style: dashed;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">


$(function() {
	$("#save").click(function(){
		if($("#customerid").val()<0){
			alert("请选择供货商");
			return;
		}
		if($("#areaname").val().length==0){
			alert("结算区域不能为空");
			return;
		}if($("#areaname").val().length>25){
			alert("结算区域超长");
			return;
		}
		
		$("#saveForm").submit();
	});
});


</script>
</HEAD>
<BODY>
<form id="saveForm" name="form1" method="POST" action="../save/<%=accountArea.getAreaid() %>" target="_self">
		${errorState}<br/>
		供货商：
		<select id="customerid" name="customerid">
		<option value="-1">----请选择----</option>
		<% for(Customer c : customerList){ %>
			<option value="<%=c.getCustomerid() %>"><%=c.getCustomername() %></option>
		<%} %>
		</select>
		结算区域：<input type="text" id="areaname" name="areaname" value="<%=accountArea.getAreaname() %>" /><br/>
		备注：<input type="text" id="arearemark" name="arearemark" value="<%=accountArea.getArearemark() %>" />
		<input type="button" id="save" value="保存"><a href="../list/1">取消</a>
</form>
<script type="text/javascript">
$("#customerid").val(<%=accountArea.getCustomerid() %>);
</script>
</BODY>
</HTML>
