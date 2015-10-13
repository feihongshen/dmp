<%@page import="cn.explink.domain.Menu"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Menu> menus = (List<Menu>)request.getAttribute("menus");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
	.ui-accordion-icons .ui-accordion-header a { padding-left: 1.2em; }
	.ui-accordion .ui-accordion-header .ui-icon { position: absolute; left: .1em; top: 50%; margin-top: -8px; }
	.ui-accordion .ui-accordion-content { padding: 1em 2.2em 1em 1.2em; border-top: 0; margin-top: -2px; position: relative; top: 1px; margin-bottom: 2px; overflow: auto; display: none; zoom: 1; }
	.ui-accordion .ui-accordion-content li{ white-space: nowrap; }
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script>
	$(function() {
		$( "#accordion" ).accordion({ collapsible: true });
	});
</script>
</HEAD>
<BODY>
	<div id="accordion">
		<% for(Menu menu:menus){ %>
		<h3>
			<a class="menubar" href="#"><%=menu.getName() %></a>
		</h3>
		<ul>
		<% 
			List<Menu> submenus=(List<Menu>) request.getAttribute("submenu"+menu.getId());
			for(Menu subMenu:submenus){
		%>
			<li><a href="<%=request.getContextPath() +subMenu.getUrl() %>" target="MainFrame"><%=subMenu.getName() %></a> </li>
		<%
			}
		%>
		</ul>
		<%} %>
	</div>
</BODY>
</HTML>
