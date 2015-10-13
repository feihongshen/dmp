<%@page import="cn.explink.domain.CreateSelectTerm"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CreateSelectTerm> createSelectTermList = (List<CreateSelectTerm>)request.getAttribute("createselecttermList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</HEAD>
<body style="background:#eef9ff">
<%for(CreateSelectTerm t:createSelectTermList) {%>
 <input type ="checkbox" id ="" name =""/><%=t.getTermname() %><br/>
<%} %>
</body>
</HTML>
