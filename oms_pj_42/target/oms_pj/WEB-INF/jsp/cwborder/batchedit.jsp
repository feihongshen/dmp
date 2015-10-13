<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量修改</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
$(function(){
	onblurcwb();
	onblurcwb1();
});
function resetcwb(){
	$("#cwbarea").val("");
	$("#cwbcount").html(0);
}
function resetcwbremark(){
	$("#cwbremark").val("");
	$("#cwbremarkcount").html(0);
}
function onblurcwb(){
	if($("#cwbarea").val().length>0){
	    var ccount = $("#cwbarea").val().split("\n");
	   var cwbnum = ccount.length;
		$("#cwbcount").html(cwbnum);
	}else{
		$("#cwbcount").html(0);
	}
}
function onblurcwb1(){
	if($("#cwbremark").val().length>0){
		var ccount = $("#cwbremark").val().split("\n");
		var transcwbnum = ccount.length;
		$("#cwbremarkcount").html(transcwbnum);
    }else{
    	$("#cwbremarkcount").html(0);
    }
}

$(document).ready(function() {
	$("#btnval").click(function(){
		var ccount = $("#cwbarea").text().split("\n");
		var rcount = $("#cwbremark").text().split("\n");
		if($("#cwbcount").text() != $("#cwbremarkcount").text()){
			alert("订单号和备注的数量不一致");
			return false;
		}
		if($("#cwbcount").text()=="0" || $("#cwbremarkcount").text()=="0"){
			alert("内容不完整");
			return false;
		}
		$("#batchForm").submit();
	});
});
</script>

</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="right_title">
		<form action ="<%=request.getContextPath() %>/order/savebatchedit"   method="post" id="batchForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" >
			<tr>
				<td width="50%" align="center" valign="middle" bgcolor="#f8f8f8"><strong>订单号[<font color ="red" id ="cwbcount">0</font>]个</strong></td>
				<td align="center" valign="middle" bgcolor="#f8f8f8"><strong>对应订单号的备注[<font color ="red" id ="cwbremarkcount">0</font>]个</strong></td>
			</tr>
			<tr>
				<td align="center" valign="top" ><p>
					<label for="textfield"></label>
					<textarea cols="50" rows="20" id ="cwbarea" name ="cwb" onkeyup="onblurcwb()" ></textarea>
				</p>
					<p>
						<input type="reset" onclick="resetcwb()" value ="清空" class="input_button3" />
					</p></td>
				<td align="center" valign="top" ><p>
					 <textarea cols="50" rows="20" id ="cwbremark" name ="cwbremark" onkeyup="onblurcwb1()"></textarea>
				</p>
					<p>
						<input type="reset" onclick="resetcwbremark()" value ="清空" class="input_button3" />
					</p></td>
			</tr>
			<tr>
				<td colspan="2" align="center" valign="middle" ><input type="button" name="btnval" id="btnval" value="提交" class="button" /></td>
				</tr>
			<tr>
				<td colspan="2" align="center" valign="middle" ><script><%if(request.getAttribute("SuMessage")!=null){%>alert('<%=request.getAttribute("SuMessage")%>');<%}%></script></td>
				</tr>
		</table>
		</form>
	</div>
	
</div>
</body>



</html>

