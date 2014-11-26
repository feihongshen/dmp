<%@page import="cn.explink.domain.Bale"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建包号</h1>
		<form id="bale_cre_Form" name="bale_cre_Form" onSubmit="if(check_bale()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/bale/create" method="post"  >
		<div id="box_form">
				<ul>
           		<li><span>包　 名：</span><input type="text" name="baleno" id="baleno" maxlength="50"/></li>
	         </ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" id="sub" /></div>
		</form>	
	</div>
</div>

<div id="box_yy"></div>

