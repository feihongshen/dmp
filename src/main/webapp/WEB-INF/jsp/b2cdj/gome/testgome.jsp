<%@page import="cn.explink.domain.orderflow.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>国美xml数据测试</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="right_title">
		
		<form action ="<%=request.getContextPath() %>/gome/testxml" method ="post" id ="batchForm">
		<br>订单号：<br>
		&nbsp;&nbsp;&nbsp;<textarea name="xml" cols="100" rows="30" ></textarea>
		<input type="submit" name="button3" id="btnval2" value="确定" class="button" />
		</form>
	</div>
	
</div>
</body>

</HTML>
