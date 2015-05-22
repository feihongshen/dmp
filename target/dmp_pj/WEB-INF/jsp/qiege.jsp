<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background:#f5f5f5">
<form action="qiege" method="post">
回车分割标识<input size=50 name="Hqie" value="<%=request.getParameter("Hqie")==null?"":request.getParameter("Hqie").replaceAll("\"", "”") %>" />多个3个英文逗号分割<br/>
制表分割标识<input size=50 name="qie" value="<%=request.getParameter("qie")==null?"":request.getParameter("qie").replaceAll("\"", "”") %>" />多个3个英文逗号分割<br/>
内容：
<textarea rows="4" cols="50" name="t"><%=request.getParameter("t")==null?"":request.getParameter("t") %></textarea>
结果：<textarea rows="4" cols="50" name="jieguo"><%
String t = request.getParameter("t")==null?"":request.getParameter("t");
for(String ts :(request.getParameter("qie")==null?"":request.getParameter("qie")).split(",,,")){
t = t.replaceAll(ts.replaceAll("”", "\"").replaceAll("｝", "\\\\}").replaceAll("｛", "\\\\{").replaceAll("【", "\\\\[").replaceAll("】", "\\\\]"), "\t"+ts);
}
for(String ts :(request.getParameter("Hqie")==null?"":request.getParameter("Hqie")).split(",,,")){
t = t.replaceAll(ts.replaceAll("”", "\"").replaceAll("｝", "\\\\}").replaceAll("｛", "\\\\{").replaceAll("【", "\\\\[").replaceAll("】", "\\\\]"), "\n"+ts);
}
out.print(t);
%></textarea><br/>
<input type="submit" value="提交切割数据" /><br/>
</form>
<form action="qiege" method="post">
SQL：<input size=50 name="sql" value="<%=request.getParameter("sql")==null?"":request.getParameter("sql") %>" />多个3个英文逗号分割   UPDATE express_ops_cwb_detail SET carrealweight='【value】' WHERE cwb='【cwb】';<br />
单号[cwb]：<textarea rows="10" cols="20" name="cwbs"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %></textarea>
值[value]：<textarea rows="10" cols="20" name="zhi"><%=request.getParameter("zhi")==null?"":request.getParameter("zhi") %></textarea><br/>

<input type="submit" value="生成sql" /><br/>
结果：<textarea rows="10" cols="100" name="jieguo"><%
String cwbs= request.getParameter("cwbs")==null?"":request.getParameter("cwbs");
String values=request.getParameter("zhi")==null?"":request.getParameter("zhi");
String sql = request.getParameter("sql")==null?"":request.getParameter("sql");

String [] cwb = cwbs.split("\n");
String [] value = values.split("\n");
for(int i = 0 ; i < cwb.length ; i ++){
	out.print( sql.replaceAll("【cwb】", cwb[i].trim()).replaceAll("【value】", value[i].trim())+"\n");
}


%></textarea><br/>
</form>
</body>

</html>
