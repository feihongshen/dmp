<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0037)http://124.42.120.61:8888/Bottom.aspx -->
<HTML><HEAD><TITLE>Bottom</TITLE>
<meta http-equiv="refresh" content="200; url=bottom.jsp"><!--定时X秒刷新一次 防止session-->
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<META content="MSHTML 6.00.2900.5659" name=GENERATOR>
<META content="Visual Basic .NET 7.1" name=CODE_LANGUAGE>
<META content=JavaScript name=vs_defaultClientScript>
<META content=http://schemas.microsoft.com/intellisense/ie5 
name=vs_targetSchema><LINK href="css/styles.css" type=text/css rel=stylesheet></HEAD>
  <SCRIPT LANGUAGE='JavaScript' src='js/havetype.js'></Script>  
<BODY MS_POSITIONING="GridLayout">
<%
Map usermap=(Map)session.getAttribute("usermap");
String username=usermap.get("username").toString();
String realname=usermap.get("realname").toString();
String branchname=usermap.get("branchname").toString();
String customername=usermap.get("customername").toString();
String usercustomerid=usermap.get("usercustomerid")==null?"":usermap.get("usercustomerid").toString();
String usertypeflag=usermap.get("usertypeflag").toString();
String departname=usermap.get("departname")==null?"":usermap.get("departname").toString();

//List noticenewbottomlist=session.getAttribute("noticenewbottomlist")==null?new ArrayList():(List)session.getAttribute("noticenewbottomlist");


%>
<script>
function dosubmit(noticeid){
		var time = new Date();  //清除缓存
	window.showModalDialog("common?action=noticetoviewbottom&noticeid="+noticeid+'&time='+time,'','dialogWidth:800px;dialogHeight:600px');
 
	document.form1.submit();
}
</script>
<TABLE height=30 cellSpacing=0 cellPadding=0 width="100%" 
background=images/bottom_ima.gif border=0>
  <TBODY>
  <TR>
    <TD width=357><%if(usercustomerid.equals("0")){%>

		用户名:<font color="red"><%=username%></font>
		姓名:<font color="red"><%=realname%></font>
       <%if(usertypeflag.equals("2")){%>
	   站点:<font color="red"><%=branchname%></font>
	   <%}else if(usertypeflag.equals("1")){%>
	   部门:<font color="red"><%=username.equals("admin")?"[公司总部]":departname%></font>
	   <%}%>
<%}else{%>
               &nbsp;用户名:<font color="red"><%=username%></font>

                &nbsp;公司名称:<font color="red"><%=customername%></font>

<%} %></TD>
    <TD width=402>
   
	  
	  </TD>
    <TD align=center width=373>
      <P align=left>版权所有:<a href="http://www.explink.cn" target="_blank">易普联科</a>&nbsp;&nbsp;&nbsp;&nbsp; <A class=white>Copyright 
      &copy;&nbsp;2005-2015</A>&nbsp; <a href="http://www.linkwan.com/gb/" target="_blank">网速测试</a></P></TD>
    <TD align=right width=279>在线支持：
	<a href="tencent://message/?uin=1014790432&amp;Site=www.explink.cn&amp;Menu=yes" target="blank"><img alt="点击这里给我发消息" src="http://wpa.qq.com/pa?p=1:14771821:10" border="0" /></a>
	电话支持:010-67397638</TD>
  </TR></TBODY></TABLE><TD width="421" 
align="center"></TD></FORM></BODY></HTML>
