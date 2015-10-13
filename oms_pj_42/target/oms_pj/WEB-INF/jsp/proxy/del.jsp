<%@page import="cn.explink.domain.ProxyConf"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
ProxyConf proxyConf = (ProxyConf)request.getAttribute("proxyConf");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>删除代理</h1>
		<form id="proxy_del_Form" name="proxy_del_Form" onSubmit="if(check_proxy1()){submitDelForm(this);}return false;" action="<%=request.getContextPath()%>/proxy/delproxy/${proxyConf.id}" method="post"  >
		<div id="box_form">
				<ul>
				    <li><center><font color="red"><b>您确定要删除此代理吗？</b></font></center></li>
					<li><span>密码：</span><input type ="password" id ="pass" name ="pass" maxlength="30"/>*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" />　<input type="button" onclick="closeBox()" value="取消" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
