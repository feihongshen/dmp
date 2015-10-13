

<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>explink对接查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>


<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");

%>
 

</head>
<body style="background:#eef9ff">
<div class="menucontant">

	<form action="<%=request.getContextPath()%>/explinkInterface/test"  method="post" id="searchForm">
		请配置参数:companyname=<input type="text" name="companyname" value="test_explink"/>
		请配置参数:sign=<input type="text" name="sign" value="ed7da012c1849f976c3e7fcf1a2ce8e1"/>
		请输入查询订单号：<input type="text" name="cwb" /><br>
		<input type="submit" /><br>
		返回的结果：<textarea rows="20" cols="50"><%=values%></textarea>
		
	</form>
	
</div>

</body>
</html>
