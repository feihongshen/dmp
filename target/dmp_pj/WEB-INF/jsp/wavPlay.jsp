<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java"
	import="java.util.List,java.util.ArrayList,java.util.Map"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
		type="text/javascript"></script>
</head>
<body>
    <input id="wavPlay" type="hidden"></input>
	<audio id="wavPlay1"></audio>
</body>
</html>
