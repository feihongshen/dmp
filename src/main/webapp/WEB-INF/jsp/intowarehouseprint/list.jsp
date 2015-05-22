
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入库标签打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function print(url)
{
	var typeid=$("#typeid").val();
		$("#form1").attr('action',url+typeid);
		$("#form1").submit();
	}
</script>
</head>

<body style="background: #eef9ff">

	<div class="right_box">
	<div class="inputselect_box">
	<form action="" id="form1" method="post">
	<input type="text" name="scancwb" id="scancwb"/>
	<select id="typeid">
	<option value="1">请选择</option>
	<option value="1">标签1</option>
	<option value="2">标签2</option>
	<option value="3">标签3</option>
	</select>
	<input type="button" value="生成" onclick="print('<%=request.getContextPath()%>/printcwb/style')" /> 
	</form>
		</div>
	</div>
</body>
</html>


