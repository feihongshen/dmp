<%@page import="cn.explink.domain.Reason"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Reason reason = (Reason)request.getAttribute("reason");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;border-style: dashed;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">

$(function() {
	
	$("#save").click(function(){
		if($("#rcontent").val().length>500){
			alert("修改的内容过长");
			return false;
		}
		$("#saveForm").submit();
		
	});
	
});

</script>
</HEAD>
<BODY>
<%=(String)request.getAttribute("editOk")==null?"":request.getAttribute("editOk") %>
<form id="saveForm" name="form1" action ="../save/${reason.reasonid}" method="post">
修改内容：<input type ="text" id="rcontent" name ="reasoncontent" value ="${reason.reasoncontent}" ><br/>
		 <input  id ="save" type="button"  value="修改">
		 <a href="../list/1">返回</a>
</form>

</BODY>
</HTML>
