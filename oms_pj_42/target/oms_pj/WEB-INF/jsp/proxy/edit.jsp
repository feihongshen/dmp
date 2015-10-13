<%@page import="cn.explink.domain.ProxyConf"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
ProxyConf proxyConf = (ProxyConf)request.getAttribute("proxyConf");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改代理</h1>
		<form id="proxy_save_Form" name="proxy_save_Form" onSubmit="if(check_proxy()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/proxy/save/${proxyConf.id}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>代理IP：</span><input type ="text" id ="ip" name ="ip" maxlength="50" value ="${proxyConf.ip}"/>*</li>
					<li><span>端口号：</span><input type ="text" id ="port" name ="port"  maxlength="30" value ="${proxyConf.port}" />*</li>
					<li><span>速度状态：</span><input type ="radio" name ="with" id ="with" <%=proxyConf.getWith()==1?"checked":"" %> value ="1" />快 　<input type ="radio" name ="with" id ="with" value ="0" <%=proxyConf.getWith()==0?"checked":"" %>/>慢</li>
					<li><span>可用状态：</span><input type ="radio" name ="state" id ="state" value ="1" <%=proxyConf.getState()==1?"checked":"" %> />可用 　<input type ="radio" name ="state" id ="state" value ="0" <%=proxyConf.getState()==0?"checked":"" %>/>不可用</li>
					<li><span>是否默认代理：</span><input type ="radio" name ="isnoDefault" id ="isnoDefault" value ="1" <%=proxyConf.getIsnoDefault()==1?"checked":"" %> />是 　<input type ="radio" name ="isnoDefault" id ="isnoDefault" value ="0" <%=proxyConf.getIsnoDefault()==0?"checked":"" %>/>否</li>
					<li><span>密码：</span><input type ="password" id ="pass" name ="pass" maxlength="30"/>*</li>
				</ul>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
