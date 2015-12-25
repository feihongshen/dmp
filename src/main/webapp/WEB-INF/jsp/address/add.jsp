<%@page
	import="cn.explink.domain.addressvo.AddressCustomerStationVO,cn.explink.domain.Customer,cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%
	List<AddressCustomerStationVO> listRalations = (List<AddressCustomerStationVO>) request
	.getAttribute("listRalations");
	Page page_obj = (Page) request.getAttribute("page_obj");
	List<Customer> listCustomers = (List<Customer>) request.getAttribute("listCustomers");
	List<Branch> listBranchs = (List<Branch>) request.getAttribute("listBranchs");

	// 	HashMap<String, List<AddressCustomerStationVO>> map = (HashMap<String, List<AddressCustomerStationVO>>)request.getAttribute("mapRalation");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统设置</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/multiple-select.css"
	type="text/css" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/multiple-select.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
	function buttonSave(form) {
		var select = document.getElementById("station");
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
		} else if (strs.length==0) {
			alert("执行站点不能为空");
			return false;

		} else if (str.length == 0) {
			alert("地址库站点不能为空");
			return false;

		}

		else {
				$.ajax({
					type : "POST",
					url : $(form).attr("action"),
					data : $(form).serialize(),
					dataType : "json",
					success : function(data) {
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
				branchid : $("#station").val()
			},
			dataType : "json",
			success : function(data) {
				$("#areaInput").val(data.error);
			}
		});
	}
	function stationquery() {
		if ($("#station").val() == 0) {
			alert("变量不能为空");
			return false;
		} else {
			return true;
		}
	}
	function branckquery() {
		if ($("#excute_branchid").val() == 0) {
			alert("变量不能为空");
			return false;
		} else {
			return true;
		}
	}
	$(function() {

		$("#station").multipleSelect({
			filter : true
		});

		$("#excute_branchid").multipleSelect({
			filter : true
		});

	})
</script>
</head>

<body style="background: #f5f5f5">

	<div class="box_in_bg">

		<form id="addressCustomerStation_cre_Form"
			name="addressCustomerStation_cre_Form"
			onSubmit="buttonSave(this);return false;"
			action="<%=request.getContextPath()%>/addressCustomerStationMap/create"
			method="post">

			<input type="hidden" name="executes" id="executes" /> <input
				type="hidden" name="stations" id="stations" /> <input type="hidden"
				name="executes" id="executes" /> <input type="hidden"
				name="stations" id="stations" /> 
				数据库站点：<select id="station"
				onchange="stationquery();" class="select1" multiple="multiple">

				<%
					for (Branch branch : listBranchs) {
				%>
				<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
				<%
					}
				%>

			</select> 客户：<select id="customerName" name="customerName" class="select1"
				onChange="changeStation();">
					<option></option>
				<%
					for (Customer customer : listCustomers) {
				%>
				<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
				<%
					}
				%>
			</select>执行站点：<select id="excute_branchid" class="select1" multiple="multiple">

				<%
					for (Branch branch : listBranchs) {
				%>
				<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
				<%
					}
				%>
			</select>


			<div align="center">
				<input type="hidden" id="checkFlag" name="checkFlag" value="0" /><input
					type="submit" value="确认" class="button" id="sub" /> <input
					type="button" value="返回" class="button" id="cancel"
					onclick="location='<%=request.getContextPath()%>/addressCustomerStationMap/list/1'" />
			</div>
		</form>
	</div>

	<%-- $("#selectPg").val(<%=request.getAttribute("page") %>); --%>
	<%-- $("#name").val(<%=request.getParameter("name") %>); --%>
	<%-- $("#value").val(<%=request.getParameter("value") %>); --%>
	</script>
	<!-- 删除订单流程的ajax地址 -->
	<input type="hidden" id="getAreaURL"
		value="<%=request.getContextPath()%>/addressCustomerStationMap/getAreaByBranchid">
</body>
</html>