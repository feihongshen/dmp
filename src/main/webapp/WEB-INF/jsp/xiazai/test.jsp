<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>测试下载</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	
	<form action="<%=request.getContextPath()%>/xiazai/xiazai1" method="post"  >
		<input type="submit"  value="下载" class="input_button2" />
	</form>
	<form action="<%=request.getContextPath()%>/xiazai/tingzhi" method="post"  >
		<input type="submit"  value="停止" class="input_button2" />
	</form>
	</div>
	</div>
</body>
</html>