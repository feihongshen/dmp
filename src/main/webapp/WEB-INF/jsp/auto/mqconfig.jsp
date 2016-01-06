<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script language="javascript">
function startMQ(cmd){
	var param = {name : ''};
	
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  cmd,
		data : param,
		datatype : "json",
		success : function(result) {
			$('#html_mqresp1').html(result);
			//alert(result.error);
		}
	});
}

function saveMQConfig(cmd){
	var param=$("#mqform").serializeArray(); 
	
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  cmd,
		data : param,
		datatype : "json",
		success : function(result) {
			$('#html_mqresp1').html(result);
			//alert(result.error);
			//location.href='list';
		}
	});
}

</script>

<button onclick="startMQ('start');">重启MQ</button>
<button onclick="startMQ('stop');">停止MQ</button>
<div id='html_mqresp1'></div>

--------
<form id="mqform" action="save" method="post">
<table>
<tr><td>参数名</td><td>参数值</td><td>说明</td></tr>
<tr><td><input type ="text" name="name" value=""/></td>
<td><input type ="text" name="text" value=""/></td>
<td><input type ="text" name="remark" value=""/> </td>
<td><input type ="reset"/> </td>
</tr>
</table>

</form>


<button onclick="saveMQConfig('save');">保存</button>

</body>
</html>

