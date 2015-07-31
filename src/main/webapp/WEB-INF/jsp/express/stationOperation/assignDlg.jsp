<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
  List<User> deliverList = (List<User>)request.getAttribute("deliverList");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>分配小件员</h1>
		<form id="customerwarehouses_cre_Form" name="customerwarehouses_cre_Form" onSubmit="if(check_customerwarehouses()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/customerwarehouses/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>小件员：</span> 
					<select id="deliverid" name="deliverid" class="select1">
						<option value="-1" selected>请选择</option>
					<%
						for (User deliver : deliverList) {
					%>
						<option value=<%=deliver.getUserid()%>><%=deliver.getRealname()%></option>
					<%
						}
					%>
					</select>
					</li>
				</ul>
		</div>
		<input type="submit" value="分配" class="button" />
		<input type="submit" value="分配并导出" class="button" />
		<input type="submit" value="关闭" class="button" />
	</form>
	</div>
</div>
<div id="box_yy"></div>
