<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.haoxgou.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");


%>
<html>
<head>

<title>好享购数据导入测试平台</title>
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
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/hxg/test/1');
		 $('#searchForm').submit();
		 
	 });
	});
	 
 </script>




</head>
<body style="background:#f5f5f5">
<div class="menucontant">
	<font  color="red">请在对接管理设置配置信息</font>
	<br><br><br>
	<form  method="post" id="searchForm">
		请选择请求接口:action=<select name="action" id="action">
									<option value="">请选择</option>
									<option value="1" >配送单</option>
									<option value="2" >退货单</option>
							  </select>
		<br><br>
		<br><input type="button" value="提交请求" id="btn" name="btn"/><br>
		请求的JSON:<textarea rows="10" cols="50" name="order_list"></textarea>
		返回：<textarea rows="10" cols="50" ><%=values%></textarea>
		<br>
	</form>
	
</div>

</body>
</html>
