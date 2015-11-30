<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="cn.explink.domain.Customer,cn.explink.domain.Branch" %>
<%
List<Branch> listBranchs = (List<Branch>)request.getAttribute("listBranchs");
List<Customer> listCustomers = (List<Customer>)request.getAttribute("listCustomers");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>创建系统设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script>
/* 
 * 多选下拉框
$(function(){
	$("#stationName").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择站点' });
})
 */

function buttonSave(form){
	
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

function changeStation(){
	$.ajax({
		type: "POST",
		url:$("#getAreaURL").val(),
		data:{
			branchid : $("#stationName").val()
		},
		dataType:"json",
		success : function(data) {
			$("#areaInput").val(data.error);
		}
	});
}

$(function(){
	$("#areaInput").attr("disabled","disabled");
})
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h2>创建系统设置</h2>
		<form id="addressCustomerStation_cre_Form" name="addressCustomerStation_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/addressCustomerStationMap/create" method="post"  >
			<div id="box_form">
				<div>
				<span>客户名称：</span><select id="customerName" name="customerName" class="select1">
					<%for(Customer customer : listCustomers){ %>
						<option value="<%= customer.getCustomerid()%>"><%=customer.getCustomername() %></option>
					<%} %>
					</select>*
				<!-- </div>
				<div> -->
				<span>站点名称：</span><select id="stationName" name="stationName" class="select1" onchange="changeStation();">
					<%for(Branch branch : listBranchs){ %>
						<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
					<%} %>
					</select>*
				<span>区域：</span><input type="text" id="areaInput" class="input_text1" style="width:300px"/>
				<input type="hidden" id="checkFlag" name="checkFlag" value="0">	
				</div>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/addressCustomerStationMap/list/1'" /></div>
	</form>
	</div>
	
	<input type="hidden" id="getAreaURL" value="<%=request.getContextPath()%>/addressCustomerStationMap/getAreaByBranchid">
</div>


