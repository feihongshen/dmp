<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	CustomWareHouse warehouses = (CustomWareHouse)request.getAttribute("customerwarehouse");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改供货商仓库</h1>
		<form id="customerwarehouses_save_Form" name="customerwarehouses_save_Form" onSubmit="if(check_customerwarehouses()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/customerwarehouses/save/${customerwarehouse.warehouseid }" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>供货商：</span>${name.customername}</li>
					<li><span>发货仓库：</span><input type="text" name="customerwarehouse" value="${customerwarehouse.customerwarehouse}" id="customerwarehouse"  maxlength="50">*</li>
					<li><span>备注：</span><input type="text" name="warehouseremark" id="warehouseremark" value="${customerwarehouse.warehouseremark}"  maxlength="50"></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存"  class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
