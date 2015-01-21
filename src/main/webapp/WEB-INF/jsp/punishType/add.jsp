<%@page import="cn.explink.domain.PunishType"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建扣罚类型</h1>
		<form id="punishType_cre_Form" name="punishType_cre_Form"  onSubmit="if(check_name()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/punishType/create" method="post"  >
		<div id="box_form">
				<ul>				
					<li><span>扣罚类型：</span><input type="text" name="name" id="name" value ="" maxlength="50">*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
