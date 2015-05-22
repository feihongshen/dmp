<%@page import="cn.explink.domain.AbnormalType"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
AbnormalType abnormalType = (AbnormalType)request.getAttribute("abnormalType");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改问题件类型</h1>
		<form id="truck_save_Form" name="truck_save_Form"  onSubmit="if(check_name()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/abnormalType/save/${abnormalType.id}" method="post"  >
		<div id="box_form">
				<ul>
				    <li><span>问题件类型：</span><input type="text" name="name" id="name" value ="${abnormalType.name}" maxlength="50">*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>