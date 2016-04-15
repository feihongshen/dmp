<%@page import="cn.explink.controller.RedisController.RedisModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<RedisModel> siList = (List<RedisModel>)request.getAttribute("siList");
	String cacheName = (String)request.getAttribute("cacheName");
	String key = (String)request.getAttribute("key");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>redis缓存</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function editSuccess(data){
	$("#searchForm").submit();
}

function delSuccess(data){
	$("#searchForm").submit();
}

function del(key){
	$.ajax({
		type: "POST",
		url:$("#del").val(),
		dataType:"json",
		data:{
			cacheName:$("#cacheName").val(),
			key:key
		},
		success : function(data) {
			$("#searchForm").submit();
		}
	});
}

function testDelAll(){
	if($('#cacheName').val()){
		if(confirm('确定要删除同类所有的缓存吗?')){
			$.ajax({
				type: "POST",
				url:$("#del").val(),
				dataType:"json",
				data:{
					cacheName:$("#cacheName").val()
				},
				success : function(data) {
					$("#searchForm").submit();
				}
			});
		}
		return true;
	}else{
		alert('请先选择【缓存名】'); 
	}
}

</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<form action="" method="post" id="searchForm" method="post" >
		缓存名：<select id="cacheName" name="cacheName" class="input_text1" style="height:21px;">
		    <option value=""></option>
			<option value="YonghuiController" <%if(cacheName.equals("YonghuiController")){%>selected<%} %>>YonghuiController</option>
			<option value="YihaodianController" <%if(cacheName.equals("YihaodianController")){%>selected<%} %>>YihaodianController</option>
			<option value="JobUtilController" <%if(cacheName.equals("JobUtilController")){%>selected<%} %>>JobUtilController</option>
			<option value="JobUtil" <%if(cacheName.equals("JobUtil")){%>selected<%} %>>JobUtil</option>
			<option value="RufengdaController" <%if(cacheName.equals("RufengdaController")){%>selected<%} %>>RufengdaController</option>
			<option value="ServiceUtil" <%if(cacheName.equals("ServiceUtil")){%>selected<%} %>>ServiceUtil</option>
			<option value="ScheduledTaskEnv" <%if(cacheName.equals("ScheduledTaskEnv")){%>selected<%} %>>ScheduledTaskEnv</option>
			<option value="userCache" <%if(cacheName.equals("userCache")){%>selected<%} %>>userCache</option>
			<option value="customerCache" <%if(cacheName.equals("customerCache")){%>selected<%} %>>customerCache</option>
			<option value="branchCache" <%if(cacheName.equals("branchCache")){%>selected<%} %>>branchCache</option>
			<option value="jointCache" <%if(cacheName.equals("jointCache")){%>selected<%} %>>jointCache</option>
			<option value="reasonCache" <%if(cacheName.equals("reasonCache")){%>selected<%} %>>reasonCache</option>
			<option value="default" <%if(cacheName.equals("default")){%>selected<%} %>>default</option>
		 </select> * &nbsp;&nbsp;&nbsp;
		key：<input type="text" id="key" name="key" class="input_text1"/>
		<input type="submit" onclick="if($('#cacheName').val()){return true;}else{alert('请先选择【缓存名】'); return false;}" id="find" value="查询" class="input_button2" />
		&nbsp;&nbsp;&nbsp;
		<input type="button" onclick="testDelAll();" id="find" value="清空同类缓存"/>
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">key</td>
			<td width="75%" align="center" valign="middle" bgcolor="#eef6ff">值</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(RedisModel si : siList){ %>
		<tr>
			<td width="15%" align="center" valign="middle" ><%=si.getKey() %></td>
			<td width="75%" align="center" valign="middle" ><%=si.getValue() %></td>
			<td width="10%" align="center" valign="middle" >
			[<a href="javascript:if(confirm('确定要删除?')){del('<%=si.getKey() %>');}">删除</a>]
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$(document).ready(function(){
	$("#cacheName").val('<%=cacheName %>');
	$("#key").val('<%=key %>');
});

</script>
<!-- 删除订单流程的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/redis/del" />
</body>
</html>