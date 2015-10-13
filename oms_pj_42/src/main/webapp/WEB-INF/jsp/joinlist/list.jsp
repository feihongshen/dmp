<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</HEAD>
<BODY>
<%=(String)request.getAttribute("showMessage")==null?"":request.getAttribute("showMessage") %>
<form action ="<%=request.getContextPath()%>/joinlist/upload" method ="post"  enctype="multipart/form-data" target="_self">
上传文件：<input type ="file" name = "txt" >   
         <input type ="hidden" name ="showMessage" value ="上传成功">
         <input type ="submit" value ="上传">
</form>
</BODY>
</HTML>
