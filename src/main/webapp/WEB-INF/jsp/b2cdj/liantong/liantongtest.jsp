

<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.explink.pos.unionpay.*" %>
<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");


%>
<html>
<head>

<title>联通数据导入测试平台</title>
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
 <script type="text/javascript">
 
 $(document).ready(function() {
	 $("#btn").click(function (){
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/liantong/received');
		 $('#searchForm').submit();
		 
	 });
	});
	 
 </script>




</head>
<body style="background:#eef9ff">
<div class="menucontant">
	<font  color="red">请在对接管理设置配置信息</font>
	<br><br><br>
	<form  method="post" id="searchForm">
		请选择请求接口:action=<select name="action" id="action">
									<option value="">请选择</option>
									<option value="put" >put</option>
									<option value="del" >del</option>
							  </select>
		<br><br>
		<br><input type="button" value="提交请求" id="btn" name="btn"/><br>
		<li>请求时间：<input type="text" name="RequestTime" value="2014-03-10 17:00:00"/> </li>
		<li>请求sign：<input type="text" name="sign" value=""/> </li>
		<li>请求的JSON:<textarea rows="10" cols="50" name="JsonContent"></textarea></li>
		<li>返回的JSON：<textarea rows="10" cols="50" ><%=values%></textarea><font color="red"></font></li>
		
		
		<br>
	</form>
	
</div>

</body>
</html>
