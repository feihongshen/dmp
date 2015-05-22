<%@page import="cn.explink.domain.CwbALLStateControl"%>
<%@page import="cn.explink.domain.CwbStateControl,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Map<String,List<CwbALLStateControl>> mapList = (Map<String,List<CwbALLStateControl>>)request.getAttribute("mapList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单状态设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function addInit(){
}
function addSuccess(data){
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
	window.parent.crossCapablePDA();
	//window.parent.uploadFormInit("user_save_Form");
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getContextPath()%>/cwbAllStateControl/list" method="post" id="searchForm" method="post" >
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
			<td width="70%" align="center" valign="middle" bgcolor="#eef6ff">可操作的环节</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(CwbStateEnum ce : CwbStateEnum.values()){ %>
			<tr>
				<td width="15%" align="center" valign="middle" ><%=ce.getText() %></td>
				<td width="70%" align="center" valign="middle" >
				<%for(CwbALLStateControl cs : mapList.get(ce.getValue()+"List")){ %>
				<% for(FlowOrderTypeEnum fte : FlowOrderTypeEnum.values()){if(cs.getToflowtype()==fte.getValue()){ %>
					<%=fte.getText() %><%if(mapList.get(ce.getValue()+"List").size()>1){ %> | <%} %>
				<%}}} %>
				</td>
			<td width="10%" align="center" valign="middle" >
			[<a href="<%=request.getContextPath()%>/cwbAllStateControl/edit/<%=ce.getValue() %>">修改</a>]
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>
			
	<div class="jg_10"></div>
	
	<div class="clear"></div>
</body>
</html>