<%@page import="cn.explink.domain.orderflow.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<%
   String results = request.getAttribute("result")==null?"":request.getAttribute("result").toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>修复数据</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="right_title">
		
		<form action ="<%=request.getContextPath() %>/orderflow/userinsert" method ="post" id ="batchForm">
		<br>姓名：<br>
		&nbsp;&nbsp;&nbsp;<textarea name="realname" cols="20" rows="20" ></textarea>
		账号：<textarea name="username" cols="20" rows="20" ></textarea>
		密码：<textarea name="password" cols="20" rows="20" ></textarea>
		机构：<textarea name="branchid" cols="20" rows="20" ></textarea>
		角色：<textarea name="roleid" cols="20" rows="20" ></textarea>
		手机号：<textarea name="usermobile" cols="20" rows="20" ></textarea>
		<input type="submit" name="button3" id="btnval2" value="确定" class="button" />
		
		<br/>结果：<br>&nbsp;&nbsp;&nbsp;<textarea name="cwbss" cols="200" rows="50" ><%=results %></textarea>
		
		</form>
	</div>
	
</div>
</body>

</HTML>
