<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%String dd=(String)request.getAttribute("dd"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>测试快乐购</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/happyGo/happyyou" method="post">
<table>
<tr><td align="center">获得快乐购的xml是：</td>
<td><textarea rows="10" cols="15" name="testarea"></textarea></td>
<td>
<select name="leixing" id="leixing">
<option value="HOPE000002">出库订单导入</option>
<option value="HOPE000007">回收订单导入</option>
</select>
</td>
<td>批次号为：<input type="text" name="pici" id="pici" value=""></td>
<td>批次时间为：<input type="text" name="time" id="time" value=""></td>
</tr>
<tr><td><input type="submit"  id="submit" value="提交"></td></tr>
</table>
<table><tr><td colspan="4"><input type="text" value="<%=dd%>"><input type="hidden" value="1" name="test"></td></tr></table></form></body></html>