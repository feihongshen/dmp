<%@page import="cn.explink.domain.SystemInstall,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
SystemInstall systemInstall = (SystemInstall)request.getAttribute("systemInstall");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改系统设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>

function buttonSave(form){
	if($("#chinesename").val().length==0){
		alert("名称不能为空");
		return false;
	}else if($("#name").val().length==0){
		alert("变量不能为空");
		return false;
	}else if($("#value").val().length==0){
		alert("值不能为空");
		return false;
	}else{
		$.ajax({
			type: "POST",
			url:$(form).attr("action"),
			data:{
				chinesename:$("#chinesename").val(),
				name:$("#name").val(),
				value:$("#value").val()
			},
			dataType:"json",
			success : function(data) {
				alert(data.error);
			}
		});
	}
}
</script>
<div style="background:#eef9ff">
	<div id="box_in_bg">
		<h1>修改系统设置</h1>
		<form id="SystemInstall_cre_Form" name="SystemInstall_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/systeminstall/save/<%=systemInstall.getId()%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>名称：</span><input type="text" id="chinesename" name="chinesename" value="<%=systemInstall.getChinesename()%>"/>*</li>
					<li><span>变量：</span><input type="text" id="name" name="name" value="<%=systemInstall.getName() %>"/>*</li>
	           		<li><span>值：</span><input type="text" id="value" name="value" value="<%=systemInstall.getValue() %>"/>*</li>
	           		
		         </ul>
			</div>
			<div align="center">
	        <input type="submit" value="确认" class="button" id="sub" />
	        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/systeminstall/list/1'" /></div>
		</form>
	</div>
</div>


