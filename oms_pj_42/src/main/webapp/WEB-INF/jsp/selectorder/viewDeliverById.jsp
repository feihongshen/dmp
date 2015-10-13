<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
User u = (User)request.getAttribute("user");
int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>小件员详情</h1>
		<div id="box_form">
            <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="400"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="33%" align="center" valign="middle" bgcolor="#eef6ff" >帐户ID</td>
										<td align="center" valign="middle" ><%=u.getUserid() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >帐户名</td>
										<td align="center" valign="middle" ><%=u.getUsername() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >真实姓名</td>
										<td align="center" valign="middle" ><%=u.getIdcardno() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >身份证</td>
										<td align="center" valign="middle" ><%=u.getIdcardno() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >电话</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										    <%=u.getUserphone() %>
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >手机</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										    <%=u.getUsermobile() %>
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >地址</td>
										<td align="center" valign="middle" ><%=u.getUseraddress() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >邮箱</td>
										<td align="center" valign="middle" ><%=u.getUseremail() %></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >用户角色ID</td>
										<td align="center" valign="middle" ><%=u.getRoleid() %></td>
									</tr>
								</table></td>
							</tr>
						</table></td>
							</tr>
				</table>
		</div>
	</div>
</div>
<div id="box_yy"></div>

