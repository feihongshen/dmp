<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="cn.explink.domain.Customer,cn.explink.domain.Branch"%>
<%
	List<Branch> listBranchs = (List<Branch>) request.getAttribute("listBranchs");
	List<Customer> listCustomers = (List<Customer>) request.getAttribute("listCustomers");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>创建系统设置</title>
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
<script>
	/* 
	 * 多选下拉框
	 $(function(){
	 $("#stationName").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择站点' });
	 })
	 */

	 function buttonSave(form) {
		var select = document.getElementById("stationName");
		var str = [];
		for(i=0;i<select.length;i++){
			if(select.options[i].selected){
				str.push(select[i].value);
			}
			
		}
		var select1 = document.getElementById("excute_branchid");
		var strs = [];
		for(i=0;i<select1.length;i++){
			if(select1.options[i].selected){
				strs.push(select[i].value);
			}
			
		}
		$("#executes").val(strs);
		$("#stations").val(str);
		if($("#customerName").val().length==0){
			alert("名称不能为空");
			return false;
		}else if($("#stationName").val()==0){
			alert("变量不能为空");
			return false;
		}else{
			$.ajax({
				type: "POST",
				url:$(form).attr("action"),
				data:$(form).serialize(),
				dataType:"json",
				success : function(data) {
					//判断是否添加相同数据
					if(data.errorCode==1){
						if(confirm(data.error)){
							$("#checkFlag").val("1");
							$("#sub").click();
							$("#checkFlag").val("0");
						   }
					}else{
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
				branchid : $("#stationName").val()
			},
			dataType : "json",
			success : function(data) {
				$("#areaInput").val(data.error);
			}
		});
	}  

	$(function() {
		
		$("#excute_branchid").multipleSelect({
			filter : true
		});

		$("#stationName").multipleSelect({
			filter : true
		});

	})
</script>
<div style="background: #f5f5f5">
	<div id="box_in_bg">
		<h2>创建系统设置</h2>
		<form id="addressCustomerStation_cre_Form"
			name="addressCustomerStation_cre_Form"
			onSubmit="buttonSave(this);return false;"
			action="<%=request.getContextPath()%>/addressCustomerStationMap/create"
			method="post">
			<input type="hidden" name="executes" id="executes"/>
			<input type="hidden" name="stations" id="stations"/>
			<div id="box_form" style="height:300px">
				地址库站点：
				<select id="stationName"  class="select1"  multiple="multiple">
					<%for (Branch branch : listBranchs) {%>
					  <option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
					<%}%>
					</select>
				
				 客户：<select id="customerName" name="customerName" class="select1" onChange="changeCustomer();">
					
					<%
						for (Customer customer : listCustomers) {
					%>
					<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
					<%
						}
					%>
				</select>执行站点：<select id="excute_branchid" 
					class="select1"
					 multiple="multiple">
					
					<%for (Branch branch : listBranchs) {%>
					<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
					<%}%>
				</select> </select>* <input type="hidden" id="checkFlag" name="checkFlag" value="0">
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