<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> objList = request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
List<JSONObject> falList=(List<JSONObject>)request.getAttribute("falList");
List<User> uList = (List<User>)request.getAttribute("userList");
long deliverid=request.getParameter("deliverid")==null?0:Long.parseLong(request.getParameter("deliverid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽收到货扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function() {
	$("#cwbs").focus();
});
function sub(){
	if($("#deliverid").val()==-1){
		$("#msg").html("确定批量处理前请选择揽收人");
		return false;
	}else if($.trim($("#cwbs").val()).length==0){
		$("#msg").html("确定批量处理前请输入订单号，多个订单用回车分割");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
	
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<form id="subForm" action="<%=request.getContextPath() %>/PDA/cwblanshoudaohuoBatch" method="post">
<div class="inputselect_box" style="top: 0px; height:26px ">
	选择揽收人：
	<select id="deliverid" name="deliverid">
		<option value="-1" selected>请选择</option>
		<%for(User u : uList){ %>
			<option value="<%=u.getUserid() %>"  <%if(deliverid==u.getUserid()) {%>selected=selected<%} %> ><%=u.getRealname() %></option>
		<%} %>
	</select>*
</div>
<div style="height:36px"></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="10" class="table_5" >
		<tbody>
			<tr>
				<td align="left" valign="top">快递单号：
					<textarea  name ="cwbs" id="cwbs"  cols="30" rows="5" style="vertical-align:middle"></textarea>
					<input type="button" name="subButton" id="subButton" value="确定批量扫描" class="button" onclick="sub()"/>
					<br/>
					<div class="saomiao_right2">
						<p id="msg" name="msg" >${msg }</p>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	
	<div >
	<div class="saomiao_tab2" style="height:30px;">
		<ul id="smallTag">
			<li><a id="yichang" href="#">异常订单</a></li>
		</ul>
	</div>
	<div class="tabbox">
		<table width="100%" border="0" cellspacing="0" cellpadding="2"
			class="table_5" style=" height:150;overflow-y: scroll">
			<tr>
				<td width="50%" align="center" bgcolor="#f1f1f1">订单号</td>
				<td width="50%" align="center" bgcolor="#f1f1f1">异常原因</td>
			</tr>
			<%if(falList!=null&&falList.size()>0){for(JSONObject co : falList){ %>
			<tr >
				<td width="50%" align="center" bgcolor="#f1f1f1"><%=co.getString("cwb") %></td>
				<td width="50%" align="center" bgcolor="#f1f1f1"><%=co.getString("errorinfo") %></td>
			</tr>
			<%} }%>
					
			</table>
	</div>
	</div> 
</form>
</body>
</html>
