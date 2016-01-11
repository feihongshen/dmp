<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<script language="javascript">
function controlMQ(cmd){
	try{
		var param = {name : ''};
		
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  cmd,
			data : param,
			datatype : "json",
			success : function(data) {
				//$('#html_mqresp1').html(data);
				var data = eval('('+data+')');
				alert(data.error);
				location.href='list';
			},
			error : function(xhr, err) {            
			      alert("readyState: " + xhr.readyState + "\nstatus: " + xhr.status);
			      alert("responseText: " + xhr.responseText); 
			      location.href='list';
			}
		});
	}catch(e){
		alert(e.message);
	}
}

function saveMQConfig(cmd,formid){
	try{
		var param=$(formid).serializeArray(); 

		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  cmd,
			data : param,
			datatype : "json",
			success : function(data) {
				//$('#html_mqresp1').html(data);
				var data = eval('('+data+')');
				alert(data.error);
				location.href='list';
			}
		});
	}catch(e){
		alert(e.message);
	}
}

</script>
</head>

<body style="background:#f5f5f5">
<div class="right_box">
<div class="right_title">
MQ维护：
<button onclick="if(!confirm('确定要重启MQ吗？'))return false;controlMQ('start');">重启MQ</button>
<button onclick="if(!confirm('确定要停止MQ吗？'))return false;controlMQ('stop');">停止MQ</button>
<div id='html_mqresp1'></div>

<br/>
新建连接参数：
<form id="mqform" action="save" method="post">
<table width="100%"  border="0" cellspacing="1" cellpadding="0" class="table_2" >
<tr class="font_1"><td bgcolor="#eef6ff">参数名</td><td bgcolor="#eef6ff">参数值</td><td bgcolor="#eef6ff">说明</td><td colspan="2"  bgcolor="#eef6ff">操作</td></tr>
<tr><td><input type ="text" name="name" value="" size='40'/></td>
<td><input type ="text" name="text" value="" size='40'/></td>
<td><input type ="text" name="remark" value="" size='40'/> </td>
<td><input type ="reset"/> </td>
<td>
<button onclick="if(!confirm('确定要保存吗？'))return false;saveMQConfig('save','#mqform');return false;">保存</button>
</td>
</tr>
</table>
</form>


<br/>
连接参数列表：
<div class="right_box">
<div class="right_title">
<table width="100%"  border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
<tr class="font_1">
<td align="center" valign="middle" bgcolor="#eef6ff">参数名</td>
<td align="center" valign="middle" bgcolor="#eef6ff">参数值</td>
<td align="center" valign="middle" bgcolor="#eef6ff">说明</td>
<td align="center" valign="middle" bgcolor="#eef6ff">连接状态</td>
<td align="center" valign="middle" bgcolor="#eef6ff" colspan="2">操作</td>
</tr>
<c:forEach items="${cfgList}" var="vo" varStatus="status">
<form id="mqfm${status.index}" action="save" method="post">
<tr><td><input type ="text" name="name" value="${vo.name}" size='40'/></td>
<td><input type ="text" name="text" value="${vo.text}" size='40'/></td>
<td><input type ="text" name="remark" value="${vo.remark}" size='40'/> </td>
<td>${vo.connected>0?"已连接":""}</td>
<td><button onclick="if(!confirm('确定要修改吗？'))return false;saveMQConfig('save','#mqfm${status.index}');return false;">修改</button></td>
<td><button onclick="if(!confirm('确定要删除吗？'))return false;saveMQConfig('del','#mqfm${status.index}');return false;">删除</button></td>
</tr>
</form>
</c:forEach>
</table>
</div>
</div>
</body>
</html>

