<%@page import="java.util.Date"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>

<%
String pager=(String)request.getAttribute("pager");
String state=(String)request.getAttribute("state");
String err=(String)request.getAttribute("err");

Long lastFlowIdVar=(Long)request.getAttribute("lastFlowIdVar");
Long firstFlowIdVar=(Long)request.getAttribute("firstFlowIdVar");
Long endIdVar=(Long)request.getAttribute("endIdVar");
Long startIdVar=(Long)request.getAttribute("startIdVar");

Date startDate=(Date)request.getAttribute("startDate");
Date endDate=(Date)request.getAttribute("endDate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>


<body style="background: #fff" marginwidth="0" marginheight="0">
<%if(pager!=null&&pager.equals("1")){
if(err!=null){
%>
<%=err%>
<%
}else{
%>
	到站数据迁移任务已触发.&nbsp;&nbsp; <a href='2'>查询运行状态</a>
<%
}
%>

<%} else if(pager!=null&&(pager.equals("2")||pager.equals("3"))){%>
到站数据迁移任务状态：<%=state%> &nbsp;&nbsp;<br/>
开始运行时间:<%=startDate==null?"":startDate%> &nbsp;&nbsp;<br/>
结束运行时间:<%=endDate==null?"":endDate%> &nbsp;&nbsp;<br/>
目标区间ID:<%=firstFlowIdVar%>--<%=lastFlowIdVar%> &nbsp;&nbsp;<br/>
当前区间ID:<%=startIdVar%>--<%=endIdVar%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href='2'>刷新状态</a>
<br/><br/><br/>
<a href='1'>点击启动</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href='3'>中断运行</a> 
<%}%>
</body>
</html>

