

<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.explink.pos.unionpay.*" %>

<html>
<head>

<title>银联商务测试平台</title>
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
			
		 if($("#command").val()==''){
			 alert('请选择请求接口！');
			 $("#command").focus();
			 return;
		 }
		 if(($("#command").val()=='0102'||$("#command").val()=='0103')&&$("#cwb").val()==''){
			 alert('请输入订单号！');
			 $("#command").focus();
			 return;
		 }
		 if($("#command").val()=='0103'&&$("#pay_type").val()==''){
			 alert('请选择交易状态！');
			 $("#pay_type").focus();
			 return;
		 }
		
		 
		 
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/unionpay_test/request');
		 $('#searchForm').submit();
	});
	 
	 $("#command").change(function (){
		 if($("#command").val()=='0100'||$("#command").val()=='0101'){
			 $("#search").hide();
			 $("#login").show();
			 $("#pay_type1").hide();
		 }
		 if($("#command").val()=='0102'){
			 $("#login").show();
			 $("#search").show();
			 $("#pay_type1").hide();
		 }
		 
		 if($("#command").val()=='0103'){   //运单处理结果报告
			 $("#login").show();
			 $("#search").show();
			 $("#pay_type1").show();
			 
		 }
		 
		 
		
		 
		 
		 
	 });
	 
	 $("#pay_type").change(function (){
		 if($("#pay_type").val()=='20'||$("#pay_type").val()=='40'||$("#pay_type").val()=='70'){
			 $("#sign").hide();
			 if($("#pay_type").val()=='20'||$("#pay_type").val()=='40'){
				 $("#expt_code1").show();
			 }
			 
		 }else{
			 $("#sign").show();
		 }
		 
	 });
		
	 $("#sign_type").change(function (){
		 if($("#sign_type").val()=='2'){
			 $("#instead").show();
		 }else{
			 $("#instead").hide();
		 }
		 
	 });
	 
	 
	 
	 
	 
 });
	 

 
 </script>




</head>
<body style="background:#eef9ff">
<div class="menucontant">
	<font  color="red">请在对接管理设置配置信息</font>
	<br><br><br>
	<form  method="post" id="searchForm" action="<%=request.getContextPath()%>/chinaums/">
		
		请求的JSON:<textarea rows="10" cols="200" name="context"></textarea>
		<br>
		<input type="submit"  value="提交"/>
	</form>
	
</div>

</body>
</html>
