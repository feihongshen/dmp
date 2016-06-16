<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.MqException"%>
<%@page import="cn.explink.domain.MqExceptionBuilder.MessageSourceEnum"%>

<%
MqException mqException = (MqException)request.getAttribute("mqException");
String messageSourceName = MessageSourceEnum.getMessageSourceEnum(mqException.getMessageSource()).getName();
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
	if(!$.trim($("#messageBody").val()) && !$.trim($("#messageHeader").val())){
		alert("报文体和报文头不能同时为空");
		return false;
	} //alert($.trim($("#messageHeader").val()));
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
	if(!$.trim($("#remarks").val())){
		alert("修改备注不能为空");
		return false;
	}
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:{
			messageBody:$.trim($("#messageBody").val()),
			messageHeader:$.trim($("#messageHeader").val()),
			handleCount:parseInt($("#handleCount").val()),
			remarks:$.trim($("#remarks").val()),
			isAutoResend:$("#isAutoResend").val()
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
					<li><span>消息来源：</span><%=messageSourceName %></li>
					<li style="height: auto !important;"><span>异常原因：</span>${mqException.exceptionInfo }</li>
	           		<li><span>自动重发：</span>
		           		 <select id="isAutoResend" name="isAutoResend" class="input_text1">
	 							<option value="1" <%if(mqException.isAutoResend()){%>selected<%} %>>是</option>
	 							<option value="0" <%if(!mqException.isAutoResend()){%>selected<%} %>>否</option>
	 					 </select> *
					</li>
					<li><span>处理次数：</span>&nbsp;<input type="text" id="handleCount" name="handleCount" class="input_text1" value="<%=mqException.getHandleCount() %>"/>*</li>
	           		<li style="height: auto !important;"><span>报文体：</span>
	           			<textarea cols="1500" rows="50" id="messageBody" name="messageBody" class="input_text1" style="width:700px; height:65px; float: none !important;"><%=mqException.getMessageBody().trim() %></textarea>&nbsp;*
	           		</li>
	           		<li style="height: auto !important;"><span>报文头：</span>
	           			<textarea cols="1500" rows="50" id="messageHeader" name="messageHeader" class="input_text1" style="width:700px; height:65px; float: none !important;"><%=mqException.getMessageHeader().trim() %></textarea>&nbsp;*
		            </li>
	           		<li style="height: auto !important;"><span>修改备注：</span>
	           			<textarea cols="1500" rows="50" id="remarks" name="remarks" class="input_text1" style="width:700px; height:65px; float: none !important;"><%=mqException.getRemarks().trim() %></textarea>&nbsp;*
	           		</li>
		         </ul>
			</div>
			<div align="center">
	        <input type="submit" value="确认" class="button" id="sub" />
	        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/mqexception/list/1'" /></div>
		</form>
	</div>
</div>


