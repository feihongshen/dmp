<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Map usermap = (Map) session.getAttribute("usermap");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房扫描</title>
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
			if(type=="ruku"){
				getcwbsdataForCustomer("<%=request.getContextPath()%>","-1");
			}else if(type=="chuku"){
				getOutSum("<%=request.getContextPath()%>",0);
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
				$("#"+arr[i]).show();
				//$("#"+arr[i]+"_1").show();
			}
			/* if($("#P03").attr("display")==undefined||$("#P12").attr("display")==undefined||$("#P14").attr("display")==undefined){
				$("#CJ").show();
			} */
		}
	});
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
}
</script>
</head>

<body style="background:#eef9ff" onload="showpdaMenu();">

<div class="right_box">
	<div class="inputselect_box3">
		<span>
			<input style="display: none;" name="P02" id="P02" type="button" value="入库扫描" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/intowarhouse','ruku';"/>
			<input style="display: none;" name="P03" id="P03" type="button" value="出库扫描" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/exportwarhouse';"/>
			<%-- <input style="display: none;" name="P15" id="P15" type="button" value="库存盘点" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/branchscanstock','pandian');"/> --%>
			<input style="display: none;" name="P11" id="P11" type="button" value="退货入库" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/backimport';"/>
			<input style="display: none;" name="P12" id="P12" type="button" value="退货出库" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/backexport','chuku');"/>
			<input style="display: none;" name="P18" id="P18" type="button" value="退供货商出库" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/backtocustomer';"/>
			<input style="display: none;" name="P19" id="P19" type="button" value="供货商拒收返库" class="input_button1" onclick="window.location.href='<%=request.getContextPath()%>/PDA/customerrefuseback';"/>
			<input style="display: none;" name="P04" id="P04" type="button" value="理货" class="input_button1" onclick="gePDAtBox('<%=request.getContextPath()%>/PDA/scancwbbranch','lihuo');"/>
		</span>
	</div>
</div>
</body>
</html>


