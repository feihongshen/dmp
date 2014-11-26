<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	User u = (User)request.getAttribute("user"); 
	Branch branch = (Branch)request.getAttribute("branch") ;
%>
<%if(request.getAttribute("user")!=null){%>
<script type="text/javascript">
var initUser = new Array();
initUser[0]="<%=u.getShowphoneflag() %>,showphoneflag";
initUser[1]="<%=u.getEmployeestatus() %>,employeestatus";
</script>
<%} %>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改用户</h1>
		<form id="user_save_Form" name="user_save_Form" 
			 onSubmit="if(check_userbranch()){submitSaveFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/user/saveBranch/<%=u.getUserid() %>;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
					<li><span>所属机构：</span><%=branch.getBranchname() %></li>
	           		<li><span>用户角色：</span>小件员</li>
					<li><span>姓名：</span><input type="text" id="realname" name="realname" value="<%=u.getRealname() %>" maxlength="50"/>*</li>
					 <li><span>登录用户名：</span><input type="text" id="username" name="username" value="<%=u.getUsername() %>" maxlength="50"/>*</li>
					 <li><span>登录密码：</span><input type="text" id="password" name="password" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
			         <li><span>确认密码：</span><input type="text" id="password1" name="password1" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
			        <li><span>工作状态：</span>
						<select id="employeestatus" name="employeestatus">
							<option value="<%=UserEmployeestatusEnum.GongZuo.getValue() %>" ><%=UserEmployeestatusEnum.GongZuo.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.XiuJia.getValue() %>" ><%=UserEmployeestatusEnum.XiuJia.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.LiZhi.getValue() %>" ><%=UserEmployeestatusEnum.LiZhi.getText() %></option>
				        </select>*
					</li>
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="<%=u.getIdcardno() %>" maxlength="50"/></li>
					<li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="<%=u.getUsermobile() %>" maxlength="50"/><span style="text-align: left;" name="tip" id="tip">*</span></li>
					<li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="<%=u.getUseremail() %>" maxlength="50"/></li>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="usercustomerid" name="usercustomerid" value="0" /><!-- 0表示为内部员工 -->
        <input type="submit"  value="保存" class="button" id="sub" /><br/><br/>
        </div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
