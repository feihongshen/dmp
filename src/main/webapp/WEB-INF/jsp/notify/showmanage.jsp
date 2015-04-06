<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@page import="cn.explink.domain.Notify"%>
<%
	Notify nf=(Notify)request.getAttribute("nf");
	String  username=(String)request.getAttribute("username");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<title>公告通知</title>
<script type="text/javascript">
$(function(){
	var str='公告';
	<%if(nf.getType()==2){%>
		str='通知'
	<%}%>
	$("#now_path",parent.document).html("公告区  > "+str+"详情");
});
</script>
</head>
<body style="position:relative;background:#f5f5f5;margin-left:20px;margin-right:20px;">
	<div  style="position:absolute;top:0px;right:20px;left:20px;">
		<table style="border:0px;width:100%">
			<tr>
				<td align="center" >
					<h1 title="<%=nf.getTitle()%>"><%=nf.getTitle().length()>40?nf.getTitle().substring(0,40)+"...":nf.getTitle() %></h1>
				</td>
			</tr>
			<tr>
				<td align="center">
					<hr>
				</td>
			</tr>
			<tr>
				<td align="center">
				<span>发布时间：<%=nf.getCretime() %></span>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;<span>发布人:<%=username %></span>&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;<span>类型：<%=nf.getType()==1?"公告":"通知" %></span>
				</td>
			</tr>
	</table>
	</div>
	<div style="height:60px"></div>
	<div style="height:520px;overflow-y:scroll;">
			<table style="border:0px;width:100%">
			
			<tr>
				<td >
					<div style="margin:50px;margin-top:20px">
					<%=nf.getContent() %>
					</div>
				</td>
			</tr>
	</table>
	
	</div>
	<div  style="position:absolute;top:0px;right:20px;">
		<input type="button" value="返回" class="button" onclick="location.href='<%=request.getContextPath()%>/notify/managelist/1'"/> 
	</div>
</body>
</html>