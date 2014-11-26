<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建角色</h1>
		<form id="role_cre_Form" name="role_cre_Form" onSubmit="if(check_role()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/role/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>角色名称：</span><input type="text" id="rolename" name="rolename" maxlength="30"/>*</li>
				</ul>
				
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
