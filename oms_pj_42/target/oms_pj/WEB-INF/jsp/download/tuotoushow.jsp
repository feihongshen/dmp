<%@page import="cn.explink.domain.DownloadManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
DownloadManager down = (DownloadManager)request.getAttribute("down");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>文件查询条件</h1>
		<form id="common_save_Form" name="common_save_Form" onSubmit="if(check_common()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/common/save/${common.id}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>文件名：<%=down.getCnfilename() %></span></li>
					<li><span>条件：<%=down.getDatajson() %></span></li>
				</ul>
		</div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
