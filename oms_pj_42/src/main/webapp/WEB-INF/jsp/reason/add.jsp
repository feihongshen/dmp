<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

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
			alert("内容过长");
			return false;
		}
		if($("#reasontype").val().length==0){
			alert("请选择类型");
			return false;
		}
		$("#saveForm").submit();
		
	});
	
});

</script>
</HEAD>
<BODY>
<%=(String)request.getAttribute("addOk")==null?"":request.getAttribute("addOk") %>
<form id="saveForm"  method="post" action="create" >
		内容： <input type ="text" id="rcontent" name ="reasoncontent" ><br/>
		类型：<select name ="reasontype" id="reasontype">
		       <option value ="">请选择</option>
               <option value ="<%=ReasonTypeEnum.ChangeTrains.getValue()%>"><%=ReasonTypeEnum.ChangeTrains.getText() %></option>
               <option value ="<%=ReasonTypeEnum.BeHelpUp.getValue()%>"><%=ReasonTypeEnum.BeHelpUp.getText()%></option>
               <option value ="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"><%=ReasonTypeEnum.ReturnGoods.getText()%></option>
               <option value ="<%=ReasonTypeEnum.GiveResult.getValue()%>"><%=ReasonTypeEnum.GiveResult.getText()%></option>
           </select>
		<input type ="button" id ="save"  value="新建">
		<a href="list/1">返回</a>
</form>

</BODY>
</HTML>
