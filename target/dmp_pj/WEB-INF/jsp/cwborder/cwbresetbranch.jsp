<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>二级分拨扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script>
function gePDAtBox(pageurl){
	$.ajax({
		type: "POST",
		url:pageurl,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
			$("#scancwb",parent.document).focus();
			
			//$("#baleno",parent.document).focus();
		}
	});
}


function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
}
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box3">
		<span>
			<input name="" id="" type="button" value="扫描" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/cwborderPDA/cwbresetbranchpage');"/>
		</span>
	</div>
</div>
</body>
</html>


