<%@ page language="java"  pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改角色</h1>
		<form id="role_save_Form" name="role_save_Form" onSubmit="if(check_role()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/role/save/${role.roleid}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>角色名称：</span><input type="text" id="rolename" name="rolename" value="${role.rolename}" />*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>

