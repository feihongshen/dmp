<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");
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

</script>
</HEAD>
<BODY>
<form id="saveForm" name="form1" method="POST" action="../save/<%=cwborder.getOpscwbid()%>" >
	收件人编号：<input type="text" name="consigneeno" value="<%=cwborder.getConsigneeno() %>"><br>
	收件人名称：<input type="text" name="consigneename" value="<%=cwborder.getConsigneename() %>"><br>
	收件人地址：<input type="text" name="consigneeaddress" value="<%=cwborder.getConsigneeaddress() %>"><br>
	收件人邮编：<input type="text" name="consigneepostcode" value="<%=cwborder.getConsigneepostcode() %>"><br>
	收件人电话：<input type="text" name="consigneephone" value="<%=cwborder.getConsigneephone() %>"><br>
	<input type="submit" value="保存修改">
</form>

</BODY>
</HTML>
