<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结算</title>
<link href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css" />
</head>

<body style="background: #eef9ff">
<div class="menucontant">
							
<div class="form_topbg">
<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
	<tr id="customertr" class=VwCtr style="display:">
		<td width="200"></td>
		<td align="center"><%=request.getAttribute("error") %></td>
		<td width="200"></td>
	</tr>
	<tr>
		<td colspan="3" align="center">
		<input name="button35" type="button" id="importButton" class="button" onclick="location.href='viewCount'" value="确 认" />
		<input name="button35" type="button" id="importButton" class="button" onclick="location.href='viewPayUp'" value="查看明细" />
		</td>
	</tr>
</table>
				
</div>
</div>
</body>
</html>
