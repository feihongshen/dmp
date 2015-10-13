<%@page import="cn.explink.domain.CustomWarHouse"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
CustomWarHouse warehouses = (CustomWarHouse)request.getAttribute("customerwarehouse");
 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;border-style: dashed;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
$(function() {
	$("#save").click(function(){
		if($("#customerwarehouse").val().length>50){
			alert("仓库名称超长");
			return false;
		}if($("#customerwarehouse").val().length==0){
			alert("仓库不能为空");
			return false;
		}if($("#warehouseremark").val().length>50){
			alert("备注超长");
			return false;
		}
    });
	$("#customerwarehouse").blur(function(event){
		customerwarehouse1();
	});
});

function customerwarehouse1(){
	
		if($("#customerwarehouse").val().length>0){
			$.ajax({
				url:"<%=request.getContextPath()%>/customerwarhouses/customerwarehousecheck",
					data : {customerwarehouse : $("#customerwarehouse").val(),customerid : $("#customerid").val()},
						success : function(data) {
							if (data == false) {
								alert("仓库名称已存在");
							} else {
								alert("仓库名称可用");
							}
					}
				});
			} else {
				alert("仓库名称不能为空");
				return;
			}
	}

</script>
</HEAD>
<BODY>
<form action="../save/${customerwarehouse.warhouseid}" method="post" name="form1" id="form1">
${errorState}
${editsave }<br/>
	 <input type="hidden" name="warhouseid" value="${customerwarehouse.warhouseid}">	
供货商	：<input type="text" name="customerid" value="${name.customername}" readonly="readonly">	<br>
     <input type="hidden" name="customerid" value="${customerwarehouse.customerid}" id="customerid">
发货仓库：<input type="text" name="customerwarehouse" value="${customerwarehouse.customerwarehouse}" id="customerwarehouse"><br>
备注：<input type="text" name="warehouseremark" value="${customerwarehouse.warehouseremark}" ><br>
	<input type="submit" value="保存" id="save">
	<a href="../list/1">返回</a>
</form>

</BODY>
</HTML>
