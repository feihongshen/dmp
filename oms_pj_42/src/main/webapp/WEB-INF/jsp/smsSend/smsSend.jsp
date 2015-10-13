<%@page import="cn.explink.domain.orderflow.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>群发短信</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>

function onblurcwb(){
	if($("#mobilesID").val().length>0){
		if($("#mobilesID").val().split("\n").length >1){
			
		$("#cwbcount").html($("#mobilesID").val().split("\n").length);
		}else{
			$("#cwbcount").html(1);
		}
	}else{
		$("#cwbcount").html(0);
	}
}
function onblursms(){
	$("#smscount").html($("#smsRemackID").val().length);
}

$(document).ready(function() {
	$("#btnval").click(function(){
		if($("#mobilesID").val().length==0){
			alert("请输入单号！");
			return false;
			}
		if($("#smsRemackID").val().length==0){
			alert("请输入短信内容！");
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
		<form action ="<%=request.getContextPath() %>/sms/send" method ="post" id ="batchForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" >
			<tr>
				<td width="50%" align="center" valign="middle" bgcolor="#f8f8f8"><strong>订单号/运单号(回车隔开多个单号)：</strong></td>
				<td align="center" valign="middle" bgcolor="#f8f8f8"><strong>短信内容：</strong></td>
			</tr>
			<tr>
				<td align="center" valign="top" ><p>
					<label for="textfield"></label>
					<textarea name="mobiles" cols="50" rows="20" id="mobilesID" onkeyup="onblurcwb();"></textarea>
				</p>
					<p>
						订单数
					（<font color ="red" id ="cwbcount">0</font>）</p></td>
				<td align="center" valign="top" ><p>
					<textarea name="smsRemack" cols="50" rows="20" id="smsRemackID" onkeyup="onblursms();" ><%if(request.getParameter("smsRemack")!=null){%><%=request.getParameter("smsRemack") %><%} %></textarea>
				</p>
					<p>
						字数（<font color ="red" id ="smscount">0</font>）
					</p></td>
			</tr>
			<tr>
				<td colspan="2" align="center" valign="middle" ><input type="button" name="button3" id="btnval" value="发送短信" class="button" /></td>
				</tr>
				<tr>
         		 <td colspan ="2" align="center"><script><%if(request.getAttribute("msg")!=""){%>alert('<%=request.getAttribute("msg")%>');<%}%></script></td>
        		</tr>
		</table>
		</form>
	</div>
	
</div>
</body>

</HTML>
