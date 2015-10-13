<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	List<CustomWareHouse> warehouses = (List<CustomWareHouse>)request.getAttribute("warehouses");
  List<Customer> customers = (List<Customer>)request.getAttribute("customers");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商仓库</h1>
		<form id="customerwarehouses_cre_Form" name="customerwarehouses_cre_Form" onSubmit="if(check_customerwarehouses()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/customerwarehouses/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>供货商：</span> 
					<select id="customerid" name="customerid">
						<option value=-1>----请选择----</option>
						<% for(Customer w1 : customers){ %>
						<option value=<%=w1.getCustomerid()%>><%=w1.getCustomername() %></option>
						<%} %>
					</select>*</li>
					<li><span>发货仓库：</span><input type="text" name="customerwarehouse" id="customerwarehouse" value="${customerwarehouse.customerwarehouse }" maxlength="50">*</li>
				    <li><span>备注：</span><input type="text" name="warehouseremark" id="warehouseremark" value="${customerwarehouse.warehouseremark }"  maxlength="50"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
