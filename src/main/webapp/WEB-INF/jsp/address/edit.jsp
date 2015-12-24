<%@page
	import="cn.explink.domain.addressvo.AddressCustomerStationVO,cn.explink.domain.Customer,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	AddressCustomerStationVO addressCustomerStationVO = (AddressCustomerStationVO) request
			.getAttribute("addressCustomerStationVO");
	// List<AddressCustomerStationVO> listCustomer = (List<AddressCustomerStationVO>)request.getAttribute("listCustomer");
	List<Customer> listCustomers = (List<Customer>) request.getAttribute("listCustomers");
	List<Branch> listBranchs = (List<Branch>) request.getAttribute("listBranchs");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改系统设置</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/multiple-select.css"
	type="text/css" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
<script
	src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/multiple-select.js"></script>
<script>
	$(function() {

		$("#excute_branchid").multipleSelect({
			filter : true
		});

		$("#branchName").multipleSelect({
			filter : true
		});
	})

	function buttonSave(form) {
		var select = document.getElementById("branchName");
		var str = [];
		for (i = 0; i < select.length; i++) {
			if (select.options[i].selected) {
				str.push(select[i].value);
			}

		}
		var select1 = document.getElementById("excute_branchid");
		var strs = [];
		for (i = 0; i < select1.length; i++) {
			if (select1.options[i].selected) {
				strs.push(select[i].value);
			}

		}
		$("#executes").val(strs);
		$("#stations").val(str);
		if ($("#customerName").val().length == 0) {
			alert("名称不能为空");
			return false;
		} else if ($("#branchName").val() == 0) {
			alert("变量不能为空");
			return false;
		} else {
			$.ajax({
				type : "POST",
				url : $(form).attr("action"),
				data : $(form).serialize(),
				dataType : "json",
				success : function(data) {
					// 				alert(data.error);
					//判断是否添加相同数据
					if (data.errorCode == 1) {
						if (confirm(data.error)) {
							$("#checkFlag").val("1");
							$("#sub").click();
							$("#checkFlag").val("0");
						}
					} else {
						alert(data.error);
					}

				}
			});
		}
	}

	function changeStation() {
		$.ajax({
			type : "POST",
			url : $("#getAreaURL").val(),
			data : {
				branchid : $("#branchName").val()
			},
			dataType : "json",
			success : function(data) {
				$("#areaInput").val(data.error);
			}
		});
	}
</script>
<div style="background: #f5f5f5">
	<div id="box_in_bg">
		<h2>修改系统设置</h2>
		<form id="addressCustomerStation_cre_Form"
			name="addressCustomerStation_cre_Form"
			onSubmit="buttonSave(this);return false;"
			action="<%=request.getContextPath()%>/addressCustomerStationMap/save"
			method="post">
			<div id="box_form" style="height: 120px">
				<input type="hidden" name="executes" id="executes" /> <input
					type="hidden" name="stations" id="stations" /> <input
					type="hidden" name="id"
					value="<%=addressCustomerStationVO.getId()%>"> <span>地址库站点：</span><select
					id="excute_branchid" class="select1" multiple="multiple">
						<%
							String[] executes = addressCustomerStationVO.getBranchid().split(",");
							for (Branch branch : listBranchs) {
						%>
						<option value="<%=branch.getBranchid()%>"
							<%for (int i = 0; i < executes.length; i++) {
							if (Long.parseLong(executes[i]) == branch.getBranchid()) {%>
							selected="selected" <%}
							}%>>
							<%=branch.getBranchname()%></option>
						<%
							}
						%>
				</select>* <span>客户名称：</span><select id="customerName" name="customerName"
					class="select1">
					<%
						for (Customer customer : listCustomers) {
					%>
					<option value="<%=customer.getCustomerid()%>"
						<%if (addressCustomerStationVO.getCustomerid() == customer.getCustomerid()) {%>
						selected="selected" <%}%>><%=customer.getCustomername()%></option>
					<%
						}
					%>
				</select>* <span>执行站点：</span><select id="branchName" class="select1"
					multiple="multiple">
					<%
						String[] arcvos = addressCustomerStationVO.getExecute_branchid().split(",");
						for (Branch branch : listBranchs) {
					%>
					<option value="<%=branch.getBranchid()%>"
						<%for (int i = 0; i < arcvos.length; i++) {
					if (Long.parseLong(arcvos[i]) == branch.getBranchid()) {%>
						selected="selected" <%}}%>><%=branch.getBranchname()%></option>
					<%
						

						}
					%>
				</select>* <input type="hidden" id="checkFlag" name="checkFlag" value="0">
					<input type="hidden" id="customerId" name="customerId"
					value="<%=addressCustomerStationVO.getCustomerid()%>">
			</div>
			<div align="center">
				<input type="submit" value="确认" class="button" id="sub" /> <input
					type="button" value="返回" class="button" id="cancel"
					onclick="location='<%=request.getContextPath()%>/addressCustomerStationMap/list/1'" />
			</div>
		</form>
	</div>

	<input type="hidden" id="getAreaURL"
		value="<%=request.getContextPath()%>/addressCustomerStationMap/getAreaByBranchid">
</div>