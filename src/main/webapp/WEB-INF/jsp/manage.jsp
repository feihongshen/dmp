<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>易普配送信息管理平台DMP</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>
	$(function() {
		//$("#submitButton").button();
		$("#sendButton").click(function(){
			var cwbs=$("#cwbs").val();
			$.ajax({
				url:"<%=request.getContextPath()%>/manage/resendFlowJms",
				type:"POST",
				data:{cwbs:cwbs},
				success:function(data){
					alert(data)
				}
			});
		});
		
		$("#sendGCAButton").click(function(){
			var cwbs=$("#cwbs").val();
			$.ajax({
				url:"<%=request.getContextPath()%>/manage/resendGotoClassJms",
				type:"POST",
				data:{gcaids:cwbs},
				success:function(data){
					alert(data)
				}
			});
		});
		
		$("#sendPayupButton").click(function(){
			var cwbs=$("#cwbs").val();
			$.ajax({
				url:"<%=request.getContextPath()%>/manage/resendPayup",
				type:"POST",
				data:{ids:cwbs},
				success:function(data){
					alert(data)
				}
			});
		});
		
		$("#sendPdaButton").click(function(){
			var cwbs=$("#cwbs").val();
			$.ajax({
				url:"<%=request.getContextPath()%>/manage/resendPdaDeliverJms",
				type:"POST",
				data:{flowids:cwbs},
				success:function(data){
					alert(data)
				}
			});
		});
	});
</script>
</head>
<body bgcolor="#3c7fb5">
<div>
	运单号: <textarea id="cwbs" rows="25" cols="100"></textarea>
	<button id="sendButton">重发流程消息</button>
	
	<button id="sendGCAButton">重发归班消息</button>
	
	<button id="sendPayupButton">重发交款消息</button>
	
	<button id="sendPdaButton">重发反馈消息</button>
</div>
</body>
</html>

