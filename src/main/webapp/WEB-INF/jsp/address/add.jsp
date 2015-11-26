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

$(function(){
	$("#stationName").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择操作下一环节' });
})


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
				alert(data.error);
			}
		});
	}
}
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h2>创建系统设置</h2>
		<form id="addressCustomerStation_cre_Form" name="addressCustomerStation_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/addressCustomerStationMap/create" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>客户名称：</span><select id="customerName" name="customerName" class="select1">
					<%for(Customer customer : listCustomers){ %>
						<option value="<%= customer.getCustomerid()%>"><%=customer.getCustomername() %></option>
					<%} %>
					</select>*</li>
					<li><span>站点名称：</span><select id="stationName" name="stationName" class="select1" multiple="multiple" style="height: 20px;width: 500px">
					<%for(Branch branch : listBranchs){ %>
						<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
					<%} %>
					</select>*</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/addressCustomerStationMap/list/1'" /></div>
	</form>
	</div>
</div>


