<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.MqException"%>

<%
MqException mqException = (MqException)request.getAttribute("mqException");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改MQ异常</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>

function buttonSave(form){
	if(!$("#messageBody").val() && !$("#messageHeader").val()){
		alert("报文体和报文头不能同时为空");
		return false;
	} 
	if(!$("#handleCount").val()){
		alert("处理次数不能为空");
		return false;
	}
	if(isNaN(parseInt($("#handleCount").val()))){
		alert("处理次数不能只能为数字");
		return false;
	}
	if(parseInt($("#handleCount").val()) < 0 || parseInt($("#handleCount").val()) > 5){
		alert("处理次数只能是0-5间的整数");
		return false;
	}
	if(!$("#remarks").val()){
		alert("修改备注不能为空");
		return false;
	}
	
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:{
			messageBody:$("#messageBody").val(),
			messageHeader:$("#messageHeader").val(),
			handleCount:parseInt($("#handleCount").val()),
			remarks:$("#remarks").val()
		},
		dataType:"json",
		success : function(data) {
			alert(data.error);
		}
	});
}
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h2>修改MQ异常</h2>
		<form id="MqException_cre_Form" name="MqException_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/mqexception/save/<%=mqException.getId()%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>编码：</span>${mqException.exceptionCode }</li>
					<li><span>主题：</span>${mqException.topic }</li>
	           		<li><span>报文体：</span><input type="textarea" cols="100" rows="20" id="messageBody" name="messageBody" class="input_text1" value="<%=mqException.getMessageBody() %>"/>*</li>
	           		<li><span>报文头：</span><input type="textarea" cols="100" rows="20" id="messageHeader" name="messageHeader" class="input_text1" value="<%=mqException.getMessageHeader() %>"/>*</li>
	           		<li><span>处理次数：</span><input type="text" id="handleCount" name="handleCount" class="input_text1" value="<%=mqException.getHandleCount() %>"/>*</li>
	           		<li><span>修改备注：</span><input type="textarea" cols="100" rows="20" id="remarks" name="remarks" class="input_text1" value="<%=mqException.getRemarks() %>"/>*</li>

		         </ul>
			</div>
			<div align="center">
	        <input type="submit" value="确认" class="button" id="sub" />
	        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/mqexception/list/1'" /></div>
		</form>
	</div>
</div>


