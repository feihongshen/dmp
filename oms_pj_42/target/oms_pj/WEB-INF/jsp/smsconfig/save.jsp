<%@page import="cn.explink.domain.ProxyConf"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>保存短信账户</h1>
		<form id="smsconfig_save_Form" name="smsconfig_save_Form" onSubmit="if(check_sysconfig()){submitSysconfigForm(this);}return false;" action="<%=request.getContextPath()%>/smsconfig/savesmsconfig" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>输入密码：</span><input type ="password" id ="pass" name ="pass" maxlength="30"/>*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" />　<input type="button" onclick="closeBox()" value="取消" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
