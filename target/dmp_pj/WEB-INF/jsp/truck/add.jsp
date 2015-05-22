<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

List<User> userList = (List<User>)request.getAttribute("userList");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建车辆</h1>
		<form id="truck_cre_Form" name="truck_cre_Form"  onSubmit="if(check_truck()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/truck/create" method="post"  >
		<div id="box_form">
				<ul>				
					<li><span>车牌号：</span><input type="text" name="truckno" id="truckno" value ="" maxlength="20" class="input_text1">*</li>
					<li><span>型号：</span><input type="text" name="trucktype" id="trucktype" value ="" maxlength="50" class="input_text1"></li>
					<li><span>驾驶员：</span><select id ="truckdriver" name ="truckdriver" class="select1">
									           <option value ="">请选择</option>
									           <%for(User user:userList){ %>
									           <option value ="<%=user.getUserid()%>"><%=user.getRealname() %></option>
									           <%} %>
									        </select></li>
					
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
