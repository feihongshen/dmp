<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches") ;
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建用户</h1>
		<form id="user_cre_Form" name="user_cre_Form" enctype="multipart/form-data"
			 onSubmit="if(check_user()){submitAddUser(this);}return false;" 
			 action="<%=request.getContextPath()%>/user/createFile;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>姓名：</span><input type="text" id="realname" name="realname" value="" maxlength="50"/>*</li>
	           		<li><span>所属机构：</span><select id="branchid" name="branchid">
						<option value="-1" selected>----请选择----</option>
						<%for(Branch b : branchList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
						<%} %></select>*
					</li>
	           		<li><span>用户角色：</span>
						<select id="roleid" name="roleid">
							<option value="-1" selected>----请选择----</option>
							<%for(Role r : roleList){ %>
							<option value="<%=r.getRoleid() %>" ><%=r.getRolename() %></option>
							<%} %>
				        </select>*
					</li>
					<li><span>登录用户名：</span><input type="text" id="username" name="username" value="" maxlength="50"/>*</li>
					<li><span>登录密码：</span><input type="text" id="password" name="password" value="" maxlength="50"/>*</li>
			        <li><span>确认密码：</span><input type="text" id="password1" name="password1" value="" maxlength="50"/>*</li>
	           		<li><span>上传声音文件：</span><iframe id="update" name="update" src="user/update?fromAction=user_cre_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe></li>
	           		<li><span>导出时联系方式：</span>
	           			<select id="showphoneflag" name="showphoneflag">
							<option value="-1" selected>----请选择----</option>
							<option value="0">不可见</option>
							<option value="1">可见</option>
						</select>*
					</li>
					<li><span>　</span>选择不可见 订单电话/手机在页面显示/导出excel全部隐藏</li>
			        <li><span>工作状态：</span>
						<select id="employeestatus" name="employeestatus">
							<option value="-1" selected>----请选择----</option>
							<option value="<%=UserEmployeestatusEnum.GongZuo.getValue() %>" ><%=UserEmployeestatusEnum.GongZuo.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.XiuJia.getValue() %>" ><%=UserEmployeestatusEnum.XiuJia.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.LiZhi.getValue() %>" ><%=UserEmployeestatusEnum.LiZhi.getText() %></option>
				        </select>*
					</li>
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="" maxlength="50"/></li>
				    <!-- <li><span>基本工资：</span><input type="text"  id="usersalary" name="usersalary" value="" /></li> -->
					
					 <!-- <li><span>电话：</span><input type="text" id="userphone"  name="userphone" value="" /></li> -->
					 <li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="" maxlength="50"/></li>
					 <li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="" /></li>
			        <!--  <li><span>联系地址：</span><input type="text" id="useraddress" name="useraddress" value="" /></li> -->
			        <!-- <li><span>备注信息：</span><input type="text" id="userremark" name="userremark" value="" /></li> -->
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="usercustomerid" name="usercustomerid" value="0" /><!-- 0表示为内部员工 -->
        <input type="submit" value="确认" class="button" id="sub" /></div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
