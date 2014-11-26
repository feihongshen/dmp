<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

$(function(){
	getuserdeleteright();
});

function sub(){
	var datavalue = "[";
	for(var i = 0 ; i < $("input[name^='reason']").length ; i++){
		datavalue = datavalue +"\""+$("input[name^='reason']")[i].value+"\",";
	}
	
	for(var i = 0 ; i < $("select[name^='reason']").length ; i++){
		datavalue = datavalue +"\""+$("select[name^='reason']")[i].value+"\",";
	}
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}
	datavalue= datavalue + "]";
	$.ajax({
		type: "POST",
		url:$("#SubFrom").attr("action"),
		data:{reasons:datavalue},
		dataType:"html",
		success : function(data) {
			alert("成功修改状态："+data.split("_s_")[0]+"单\n订单状态无变动："+data.split("_s_")[1]+"单");
			searchForm.submit();
		}
	});
	
}

function checkincode(){
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath() %>/deletecwb/checkuserdeleteright",
		data : {
			"checkcode" : $("#checkcode").val()
		},
		dataType : "json",
		success : function(data) {
			if(data.errorcode==0){
				alert("管理员已成功授权你删除订单功能的操作!");
				$("#cwbsTable").show();
				$("#checkcodeTable").hide();
			}else{
				alert(data.msg);
			}
		}
	});
}

function getuserdeleteright(){
	$.ajax({
		type : "POST",
		url : "<%=request.getContextPath() %>/deletecwb/getuserdeleteright",
		data : {
			"checkcode" : $("#checkcode").val()
		},
		dataType : "json",
		success : function(data) {
			if(data.msg=="no"){
				$("#cwbsTable").hide();
				$("#checkcodeTable").show();
			}else{
				$("#cwbsTable").show();
				$("#checkcodeTable").hide();				
			}
		}
	});
}
function sub(){
	if($.trim($("#cwbs").val()).length==0){
		alert("请输入订单号");
		return false;
	}else{
		var con = confirm("请确定是否删除，一旦删除，数据无法恢复！");
		if(con == true){
			$("#cwbsTableForm").submit();
		}
	}
}
</script>
</HEAD>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box"> 
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath()%>/deletecwb/deletecwbs" class="light" >订单删除</a></li>
				<li><a href="<%=request.getContextPath()%>/deletecwb/deletecwbslist/1">订单删除查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div class="kfsh_search">
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tbody>
					<tr >
						<td width="10%" height="26" align="left" valign="top">
							<table id="checkcodeTable" width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2" style="display: none;" >
								<tr>
									<td width="60" align="right">验证码：</td>
									<td align="left"><label for="textfield"></label>
										<input name="checkcode" type="text" class="inputtext_1" id="checkcode" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){checkincode();}'/>（初次使用删除订单功能，请输入验证码， 确认使用权限）
									</td>
								</tr>
								<tr>
									<td align="left">&nbsp;</td>
									<td align="left"><input name="button" type="button" class="input_button2" id="button" value="确认" onclick="checkincode();" /></td>
								</tr>
							</table>
							<form action="<%=request.getContextPath()%>/deletecwb/deletecwbs" id="cwbsTableForm">
							<table id="cwbsTable" width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2" style="display: none;" >
								<tr>
									<td width="60" align="right">订单号：</td>
									<td align="left"><label for="textfield2"></label>
										<textarea name="cwbs" rows="3" class="inputtext_1" id="cwbs"></textarea>
										<input name="button2" type="button" class="input_button2" id="button2" value="删除" onclick="sub();" /></td>
								</tr>
								<tr>
									<td align="left">&nbsp;</td>
									<td align="left">${msg }</td>
								</tr>
							</table>
							</form>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
		</div>
	</div>
</div>
</body>
</HTML>
