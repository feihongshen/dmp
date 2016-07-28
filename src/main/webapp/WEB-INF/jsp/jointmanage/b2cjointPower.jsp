
<%@page import="cn.explink.b2c.tools.power.*" %>
<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>对接权限设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>


<%
List<JointPower> powerlist = (List<JointPower>)request.getAttribute("b2cenumlist");
B2cEnum enums[]=B2cEnum.valuesSortedByText();
%>
 
<script type="text/javascript">
function addInit(){
	//无处理
}

function submitSaveForm(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if(data.errorCode==0){
				$("#WORK_AREA")[0].contentWindow.editSuccess(data);
			}
		}
	});
}

</script>
</head>
<body style="background:#f5f5f5">
<div class="menucontant">
<div class="uc_midbg">
			<ul>
				<li><a href="#" class="light">对接权限设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointb2c">电商对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointpos">POS对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1">异常码关联</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/epaiApi/1">系统环形对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/address/1">地址库同步</a></li>
			</ul>
	</div>
	<form onSubmit="submitSaveForm(this); return false;" action="<%=request.getContextPath()%>/jointpower/save"  method="post" id="searchForm">
	
<div class="right_box">
				<div class="right_title">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"  id="gd_table">
					<tr class="font_1">
						<td  align="center" valign="middle" colspan="5" bgcolor="#eef6ff">B2C对接开启设置</td>
					</tr>
					 <%
						for(int i=0;i<enums.length;i++){
							String text=enums[i].getText();
							int key=enums[i].getKey();
							if(i%5==0){ %><tr class="font_2"><%}%>
							  <td width="20%"  align="left">
							  <input name="b2cpower" type="checkbox" value="<%=key %>" 
							  <%if(powerlist!=null&&powerlist.size()>0) 
							  	for(JointPower jum:powerlist){
							  		if(jum.getJoint_num()==key){
							  			%>checked<%
							  		}
							  	}
							  %> 
							   /><%=text %> </td>
							 <%if((i+1)%5==0){ %></tr><%}%>
						<%}%>
					
					<tr>
					<td colspan="5" align="center">
						<!-- <input name="" type="button" value="保存设置" class="input_button1"  id="add_button" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/jointpower/');return true;"   /> -->
						<input type="submit" value="保存设置" class="button"  />
					</td>
					</tr>
				</table>
				</div>
				
			</div>
			
			
			
<div class="tishi_box"></div>


</div>
</form>


</body>
</html>
