<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建模版</h1>
		<form id="common_cre_Form" name="common_cre_Form" onSubmit="if(check_commonmodel()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/commonmodel/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>模版名称：</span><input type ="text" id ="modelname" name ="modelname" maxlength="30" class="input_text1"/>*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
