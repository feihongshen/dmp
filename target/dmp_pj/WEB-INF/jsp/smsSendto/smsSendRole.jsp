<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%List<Branch> branchList = (List<Branch>)request.getAttribute("branchList"); 
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>群发短信</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(document).ready(function() {
	   // Options displayed in comma-separated list
	   $("#roleids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'角色' });
	});
	
function sendSms(){
	loading();
	var params=$('#sendForm').serialize();
$.ajax({
	url:"<%=request.getContextPath()%>/sms/sendRole",//后台处理程序
	type:"POST",//数据发送方式 
	data:params,//参数
	dataType:'json',//接受数据格式
	success:function(json){
		loadend();
		str="";
		str +="<p>发送成功数：<font color='red'>"+json.sussesCount+"</font></p>";
		str +="<p>发送失败数：<font color='red'>"+json.errorCount+"</font></p>";
		str +="<p>失败原因：</p>";
		str +=json.errorMsg;
		$("#msg").html(str);
	}
	   
});
}

function loading(){
	document.getElementById('container').style.display='block';
}
function loadend(){
	document.getElementById('container').style.display='none';
}

function onblursms(){
	$("#smscount").html($("#smsRemackID").val().length);
}

$(document).ready(function() {
	$("#btnval").click(function(){
		if($("#smsRemackID").val().length==0){
			alert("请输入短信内容！");
			return false;
			}
		sendSms();
	});
	
});

</script>

</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
	<div class="uc_midbg">
		<ul>
			<li><a href="#" class="light">公司内部</a></li>
			<li><a href="<%=request.getContextPath()%>/sms/viewcwb">订单号</a></li>
			 <li><a href="<%=request.getContextPath()%>/sms/viewbranch">站点区域</a></li> 
			 <li><a href="<%=request.getContextPath()%>/sms/viewmobile">手机号</a></li> 
		</ul>
	</div>
	<div class="tabbox">
	  <li>
	  <div class="right_title">
	  	<form name="form1" method="post" action="" id="sendForm"><table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_2">
	  		<tr>
	  			<td width="60" align="left">岗位角色：</td>
	  			<td align="left">
	  			<select name ="roleids" id ="roleids" multiple="multiple" style="width: 180px;">
							<%for(Role r : roleList){ %>
							<option value="<%=r.getRoleid() %>"  ><%=r.getRolename() %></option>
							<%} %>
				        </select>短信将发送给所选角色的公司职员
				        </td>
  			</tr>
	  		<tr>
	  			<td align="left" valign="top">短信内容：</td>
	  			<td align="left"><textarea name="smsRemack" cols="50" rows="5" id="smsRemackID"  onkeyup="onblursms();"  ><%if(request.getParameter("smsRemack")!=null){%><%=request.getParameter("smsRemack") %><%} %></textarea>
	  			建议不超过70个字符，已输入（<font color="red" id="smscount">0</font>）字 </td>
  			</tr>
	  		<tr>
	  			<td align="left">&nbsp;</td>
	  			<td align="left"><input name="button" type="button" class="input_button1" id="btnval" value="发送"  >
	  			<div id="container" style="display:none">
  					正在发送，请耐心等候。。<img src="<%=request.getContextPath()%>/images/aloading.gif" alt="" />
               </div>
	  			</td>
  			</tr>
  		</table>
	  	</form>
	  	<div id="msg">
	  	
	  	</div>
	  </div>
	  </li>
	</div>
</div>
</div>
</body>
</html>
