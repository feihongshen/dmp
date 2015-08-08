<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>派费结算规则</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css" />
<script src="${pageContext.request.contextPath}/js/paifeirule.js"></script>

<script type="text/javascript">
	
</script>
</head>

<body style="background: #eef9ff">
<fieldset><legend>按类型查询</legend>
<form action="${pageContext.request.contextPath}/paifeirule/testTAB" method="post">
补助类型：
<select name="tab">
<c:forEach items="${tabs}" var="op">
<option value="${op.value}">${op.text}</option>
</c:forEach>
</select><br></br>
名称：<input type="text" name="name"/><br></br>
订单号：<textarea name="cwbs"></textarea>
<input type="submit" value="查询"/>
</form>
</fieldset>
<hr></hr>
<fieldset><legend>按具体类型查询</legend>
<form action="${pageContext.request.contextPath}/paifeirule/testTYPE" method="post">
规则类型：
<select name="type">
<c:forEach items="${types}" var="op">
<option value="${op.value}">${op.text}</option>
</c:forEach>
</select><br></br>
补助类型：
<select name="tab">
<c:forEach items="${tabs}" var="op">
<option value="${op.value}">${op.text}</option>
</c:forEach>
</select><br></br>
补助具体类型：
<select name="kind">
<c:forEach items="${kinds}" var="op">
<option value="${op.value}">${op.text}</option>
</c:forEach>
</select><br></br>
名称：<input type="text" name="name"/><br></br>
订单号:<textarea name="cwbs"></textarea>
<input type="submit" value="查询"/>
</form>
</fieldset>
</body>
</html>


