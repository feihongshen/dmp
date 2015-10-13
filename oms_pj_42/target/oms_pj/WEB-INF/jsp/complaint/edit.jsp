<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Customer customer = (Customer)request.getAttribute("customer");
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
function isFloat(name){
	return /^[0-9\.]+$/.test(name);
}
function isLetterAndNumber(name){
	return /^[A-Za-z0-9]+$/.test(name);
}
function checkCustomername(){
	if($("#cname").val().length>0){
		$.ajax({
			url:"<%=request.getContextPath()%>/customer/customernameCheck",
			data:{cname:$("#cname").val()},
			success:function(data){
				if(data==false)alert("供货商名称已存在");
				else alert("供货商名称可用");
			}
		});
	}
}
$(function() {
	$("#cname").blur(function(event){
		checkCustomername();
	});
	$("#save").click(function(){
		if($("#cname").val().length==0){
			alert("供货商名称不能为空");
			return false;
		}if($("#cname").val().length>200){
			alert("供货商名称超长");
			return false;
		}if($("#caddress").val().length>200){
			alert("供货商地址超长");
			return  false;
		}if($("#cman").val().length>200){
			alert("供货商联系人超长");
			return false;
		}if($("#cphone").val().length>200){
			alert("供货商电话号码超长");
			return false;
		}
		$("#saveForm").submit();
		
	});
	
});

</script>
</HEAD>
<BODY>
${errorState}
<%=(String)request.getAttribute("editOk")==null?"":request.getAttribute("editOk") %>
<form id="saveForm" name="form1" action ="../save/${customer.customerid}" method="post">
供货商公司名称：<input type ="text" id="cname" name ="customername" value ="${customer.customername}" ><br/>
		地址：<input type ="text" id ="caddress" name ="customeraddress" value ="${customer.customeraddress}" ><br/>
		联系人：<input type ="text" id ="cman" name ="customercontactman" value ="${customer.customercontactman}"><br/>
		电话：<input type ="text" id ="cphone" name ="customerphone" value ="${customer.customerphone}"><br/>
		<input  id ="save" type="button"  value="修改">
		<a href="../list/1">返回</a>
</form>

</BODY>
</HTML>
