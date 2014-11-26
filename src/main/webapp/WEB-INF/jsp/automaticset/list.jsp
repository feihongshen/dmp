<%@page import="cn.explink.domain.AutomaticSet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AutomaticSet> automaticSet = (List<AutomaticSet>)request.getAttribute("aslist");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
</head>
<body>
	<div>
		<ul>
			<li><span>自动环节：</span></li>
		</ul>
		<ul class="checkedbox1"><%
					for (AutomaticSet as : automaticSet) {
						if(as.getIsauto()==1){
				%><li><p><%=as.getNowlinkname()%></p>
				</li><%
					}}
		%></ul>
	</div>
	<div align="center"><a href="../JmsCenter/getlink">修改设置</a></div>
</body>
</html>