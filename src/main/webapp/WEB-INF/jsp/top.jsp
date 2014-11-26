<%@page import="cn.explink.domain.Menu"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Map usermap = (Map) session.getAttribute("usermap");
	String username = usermap.get("username").toString();
	List<Menu> menutopList = (List<Menu>) request.getAttribute("MENUPARENTLIST");
%>

<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
	$(function(){
		$("cwb").mouseover(function(){});
	});

	function querycwb(form) {
		var cwb = trim(form.cwb.value);
		form.action = "common?action=opscwbtoview&checkinbranchflag=1&cwb="
				+ cwb;
		form.submit();

	}

	function queryshipcwb(form) {
		var shipcwb = trim(form.shipcwb.value);
		form.action = "common?action=opsshipcwbtoview&checkinbranchflag=1&shipcwb="
				+ shipcwb;
		form.submit();

	}

</script>
</head>
<body>
	<table width="100%" height="87" border="0" cellpadding="0" cellspacing="0" background="images/top_bg.gif">
		<tr>
			<td width="1%" align="left" valign="top">&nbsp;</td>
			<td width="80%" valign="top">
				<table width="10%" border="0" cellspacing="0" cellpadding="0">
					<tr align="left">
						<%
							for (Menu menu : menutopList) {
						%>
						<td align="left"><a href="<%=request.getContextPath()%>/menu/left/<%=menu.getId()%>" target="LeftMenu"> <img src="<%=menu.getImage()%>" border="0"></a></td>
						<%
							}
						%>
					</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="5" valign="top"></td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
					<tr>
						<td width="52%" align="left" valign="top"><span class="STYLE1"> 快捷查询：</span> <input type="text" id="cwb" name="cwb" size="25" value="请输入订单号 回车查询"><input type="text" id="shipcwb" name="shipcwb" size="25" value="请输入配送单号 回车查询"></td>
						<td width="48%" align="left" valign="top">
							<a href="<%=request.getContextPath()%>/passwordupdate" target="MainFrame"><img src="images/put_but_changepsw_o.gif" name="but-changepsw" width="84" height="21" border="0"></a> 
							<img src="images/ima_line_topbut.gif" width="2" height="15"	style="cursor: pointer">
							<a href="common?action=logintotemp" target="MainFrame"><img src="images/top_toolbut_desk_over.gif" name="Image15" width="57" height="21" border="0" style="cursor: pointer"></a> 
							<a href="<%=request.getContextPath() %>/resources/j_spring_security_logout" target="_top"	onClick="return confirm('确定要重新登录吗？');"><img src="images/top_toolbut_afresh_over.gif" name="toolbut-afresh" height="21" border="0" style="cursor: pointer"></a> 
							<a href="common?action=userinfologintolist" target="MainFrame"><img src="images/put_but_userlist.gif" name="but-changepsw" width="84" height="21" border="0"></a> <img border="0" src="images/139email.gif" name="Image19" onClick="window.open('http://mail.10086.cn/','_new')" style="cursor: pointer;"> 
							<img border="0" src="images/baidu.gif" name="Image19" onClick="window.open('http://www.baidu.com/','_new')"	style="cursor: pointer;">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>