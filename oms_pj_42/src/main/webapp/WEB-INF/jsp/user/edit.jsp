<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch,cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	User u = (User)request.getAttribute("user"); 
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches") ;
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
%>

<%

if(request.getAttribute("user")!=null){ 
	User user = (User)request.getAttribute("user");
%>
<script type="text/javascript">
var initUser = new Array();
initUser[0]="<%=user.getBranchid() %>,branchid";
initUser[1]="<%=user.getShowphoneflag() %>,showphoneflag";
initUser[2]="<%=user.getEmployeestatus() %>,employeestatus";
initUser[3]="<%=user.getRoleid() %>,roleid";
initUser[4]="<%=user.getShowphoneflag() %>,showphoneflag";
</script>
<%} %>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改用户</h1>
		<form id="user_save_Form" name="user_save_Form" enctype="multipart/form-data"
			 onSubmit="if(check_user()){submitSaveUser(this,<%=u.getUserid() %>);}return false;" 
			 action="<%=request.getContextPath()%>/user/saveFile/<%=u.getUserid() %>;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
					<li><span>姓名：</span><input type="text" id="realname" name="realname" value="<%=u.getRealname() %>" maxlength="50"/>*</li>
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
					 <li><span>登录用户名：</span><input type="text" id="username" name="username" value="<%=u.getUsername() %>" maxlength="50"/>*</li>
					 <li><span>登录密码：</span><input type="text" id="password" name="password" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
			         <li><span>确认密码：</span><input type="text" id="password1" name="password1" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
	           		<li><span>上传声音文件：</span><iframe id="update" name="update" src="user/update?fromAction=user_save_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
	           		<%if(u.getUserwavfile()!=null&&u.getUserwavfile().length()>4){ %>
			         <a href="javascript:document.music1.Play();">点击测试</a>
			         <%} %>
	           		<input type="hidden" id ="wavh" name="wavh" value ="<%=u.getUserwavfile() %>"  />
	           		<EMBED id='music1' NAME='music1' SRC='<%=request.getContextPath()+ServiceUtil.wavPath+u.getUserwavfile() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
	           		</li>
	           		
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
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="<%=u.getIdcardno() %>" maxlength="50"/></li>
				    <%-- <li><span>基本工资：</span><input type="text"  id="usersalary" name="usersalary" value="<%=u.getUsersalary() %>" /></li>
					
					 <li><span>电话：</span><input type="text" id="userphone"  name="userphone" value="<%=u.getUserphone() %>" /></li> --%>
					 <li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="<%=u.getUsermobile() %>" maxlength="50"/></li>
					 <li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="<%=u.getUseremail() %>" maxlength="50"/></li>
			         <%-- <li><span>联系地址：</span><input type="text" id="useraddress" name="useraddress" value="<%=u.getUseraddress() %>" /></li>
			         <li><span>备注信息：</span><input type="text" id="userremark" name="userremark" value="<%=u.getUserremark() %>" /></li> --%>
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
