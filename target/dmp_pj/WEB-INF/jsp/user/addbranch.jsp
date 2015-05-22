<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	Branch branch = (Branch)request.getAttribute("branch") ;
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建用户</h1>
		<form id="user_cre_Form" name="user_cre_Form" 
			 onSubmit="if(check_userbranch()){submitCreateFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/user/createBranch;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
				<ul>
	           		<li><span>所属机构：</span><%=branch.getBranchname() %></li>
	           		<li><span>用户角色：</span>小件员</li>
	           		<li><span>姓名：</span><input type="text" id="realname" name="realname" value="" maxlength="50"/>*</li>
					<li><span>登录用户名：</span><input type="text" id="username" name="username" value="" maxlength="50"/>*</li>
					<li><span>登录密码：</span><input type="password" id="password" name="password" value="" maxlength="50"/>*</li>
			        <li><span>确认密码：</span><input type="password" id="password1" name="password1" value="" maxlength="50"/>*</li>
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="" maxlength="50"/></li>
					<li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="" maxlength="50"/>*</li>
					<li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="" /></li>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="usercustomerid" name="usercustomerid" value="0" />
        <input type="hidden" id="showphoneflag" name="showphoneflag" value="0" />
        <input type="hidden" id="employeestatus" name="employeestatus" value="<%=UserEmployeestatusEnum.GongZuo.getValue() %>" />
        <input type="submit" value="确认" class="button" id="sub" /></div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
