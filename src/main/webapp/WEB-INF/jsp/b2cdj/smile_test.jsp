

<%@page import="cn.explink.b2c.tools.*,cn.explink.util.DateTimeUtil" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>广州思迈速递测试平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");
String MD5 = request.getAttribute("MD5")==null?"":(String)request.getAttribute("MD5");

%>
 

</head>
<body style="background:#eef9ff">
<div class="menucontant">

	<form action="<%=request.getContextPath()%>/smile/order"  method="post" id="searchForm">
	<table >
		<tr>
		<td>Request=</td>
		<td align="left"><textarea rows="10" cols="50" name="Request"></textarea>*XML串</td>
		</tr>
		<tr>
		<td>MD5=</td>
		<td  align="left"><input type="text" name="MD5" value="" size="45"/>*MD5加密结果</td>
		</tr>
		<tr>
		<td>Action=</td>
		<td  align="left"><input type="text" name="Action" value="RequestOrder"/>*指令名称</td>
		</tr>
		
		<tr>
		<td colspan="2"><input type="submit" value="提交请求" /><br>
		返回的结果：<textarea rows="10" cols="50"><%=values%></textarea>
		</td>
		</tr>
		
		
		</table>
	</form>
	
</div>

</body>
</html>
