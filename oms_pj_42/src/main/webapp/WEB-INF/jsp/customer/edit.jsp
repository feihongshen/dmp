<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Customer customer = (Customer)request.getAttribute("customer");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改供货商</h1>
		<form id="customer_save_Form" name="customer_save_Form" onSubmit="if(check_customer()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/customer/save/${customer.customerid }" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>供货商公司名称：</span><input type ="text" id ="customername" name ="customername" value ="${customer.customername}"  maxlength="30"/>*</li>
					<li><span>地址：</span><input type ="text" id ="customeraddress" name ="customeraddress" value ="${customer.customeraddress}"  maxlength="100"/></li>
					<li><span>联系人：</span><input type ="text" id ="customercontactman" name ="customercontactman"  value ="${customer.customercontactman}"  maxlength="30"/></li>
					<li><span>电话：</span><input type ="text" id ="customerphone" name ="customerphone" value ="${customer.customerphone}" maxlength="30"/> </li>
				</ul>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
