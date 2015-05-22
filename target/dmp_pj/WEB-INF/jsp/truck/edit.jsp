<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Truck"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Truck truck = (Truck)request.getAttribute("truck");
List<User> userList = (List<User>)request.getAttribute("userList");
%>

<script>
var initEditArray = new Array();
initEditArray[0]="<%=truck.getTruckdriver()%>,truckdriver";
</script>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改车辆</h1>
		<form id="truck_save_Form" name="truck_save_Form"  onSubmit="if(check_truck()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/truck/save/${truck.truckid}" method="post"  >
		<div id="box_form">
				<ul>
				    <li><span>车牌号：</span><input type="text" name="truckno" id="truckno" value="${truck.truckno}" maxlength="20" class="input_text1">*</li>
				    <li><span>型号：</span><input type="text" name="trucktype" id="trucktype" value="${truck.trucktype}" maxlength="50" class="input_text1"></li>
				    <li><span>驾驶员：</span><select id ="truckdriver" name ="truckdriver" class="select1">
				                               <option value ="">请选择</option>
									           <%for(User user:userList){ %>
									           <option value ="<%=user.getUserid()%>"><%=user.getRealname() %></option>
									           <%} %>
									        </select></li>
				</ul>
				<input type="hidden"  name ="truckflag" id="truckflag"  value="${truck.truckflag}"/>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>