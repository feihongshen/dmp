<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>易普配送信息管理平台DMP</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#sendButton").click(function(){
		var cwbs=$("#cwbs").val();
		$.ajax({
			url:"<%=request.getContextPath()%>/commencwborder/resendCwborder",
			type:"POST",
			data:{cwbs:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});

})
</script>
</head>
<body bgcolor="#3c7fb5">
	<div>
	运单号: <textarea id="cwbs" rows="25" cols="100"></textarea>
	<button id="sendButton">外发单消息补录(将当前状态的流程信息进行补充)</button>
	</div>
</body>
</html>