<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Map usermap = (Map) session.getAttribute("usermap");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房对库房扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script>
function gePDAtBox(pageurl,type){
	$.ajax({
		type: "POST",
		url:pageurl,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
			if(type=="rukuforbale"){
				$("#baleno",parent.document).focus();
			}else if(type=="chukuforbale"){
				$("#scancwb",parent.document).focus();
			}
			
			
			//$("#baleno",parent.document).focus();
		}
	});
}

function showpdaMenu(){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/PDA/getpdaMenu",
		dataType:"html",
		success : function(data) {
			var arr = data.split(",");
			for(var i=0;i<arr.length;i++){
				$("#"+arr[i]+"_1").show();
			}
		}
	});
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
}
</script>
</head>

<body style="background:#f5f5f5" onload="showpdaMenu();">

<div class="right_box">
	<div class="inputselect_box3">
		<span>
			<input style="display: none;" name="P02_1" id="P02_1" type="button" value="入库扫描（包）" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/intowarhouseforbale','rukuforbale');"/>
			<input style="display: none;" name="P03_1" id="P03_1" type="button" value="出库扫描（包）" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/exportwarhouseforbale','chukuforbale');"/>
		</span>
	</div>
</div>
</body>
</html>


