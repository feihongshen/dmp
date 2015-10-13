
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<EmailDate> emailDate = (List<EmailDate>)request.getAttribute("emailDate");
List<User> driverList = (List<User>)request.getAttribute("driverList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />

<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>

<script type="text/javascript">
$(function(){

	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});
</script>
</HEAD>
<script type="text/javascript">

</script>
<BODY>
<form action ="1" method="post" id="searchForm"> 
 驾驶员：<select name="userid" id="userid">
        <option value="0">请选择驾驶员</option>
        <%for(User u :driverList){ %>
           <option value="<%=u.getUserid()%>"><%=u.getUsername()%></option>
        <%} %>
        </select><br/>
邮件时间段：
 <input type ="text" name ="beginemaildate" id ="beginemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>">　到　
 <input type ="text" name= "endemaildate" id ="endemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>">     
      <input type="submit" value="查询"/>
 </form>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　</div>
	第<%=request.getAttribute("page")%>页 
	<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
	</select>
<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>

<table>
  <tr>
    <td>ID</td>
    <td>emailDate</td>
    <td>Userid</td>
  </tr>
  <%for(EmailDate e:emailDate){ %>
   <tr> 
    <td><%=e.getEmaildateid() %></td><td><%=e.getEmaildatetime()%></td><td><%=e.getUserid()%></td>
    <td><a href ="../emaildate/<%=e.getEmaildatetime()%>">入库交接单打印</a></td>
   </tr>
   <%} %>
  
</table>
</BODY>
</HTML>
