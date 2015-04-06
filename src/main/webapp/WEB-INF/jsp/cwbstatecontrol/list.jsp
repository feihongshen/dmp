<%@page import="cn.explink.domain.CwbStateControl,cn.explink.enumutil.*,cn.explink.util.Page"%>
<%@page import="java.util.Map,java.util.HashMap"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Map<String,List<CwbStateControl>> mapList = (Map<String,List<CwbStateControl>>)request.getAttribute("mapList");
	
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单流程设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//window.parent.uploadFormInit("user_cre_Form");
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
	<%-- <span><input name="" type="button" value="创建订单流程" class="input_button1"  onclick="window.location.href='<%=request.getContextPath()%>/cwbStateControl/add'"/>
	</span> --%>
	<form action="<%=request.getContextPath()%>/cwbStateControl/list" method="post" id="searchForm" method="post" >
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">当前环节</td>
			<td width="65%" align="center" valign="middle" bgcolor="#eef6ff">下一环节</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(FlowOrderTypeEnum et : FlowOrderTypeEnum.values()){ %>
		 <%if(et.getValue()!=FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()&&et.getValue()!=FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()&&
		 et.getValue()!=FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()&&et.getValue()!=FlowOrderTypeEnum.BeiZhu.getValue()&&
		 et.getValue()!=FlowOrderTypeEnum.ShouGongXiuGai.getValue()&&et.getValue()!=FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()){ %>
		<tr>
			<td width="20%" align="center" valign="middle" ><%=et.getText() %></td>
			<td width="65%" align="left" valign="middle" >
			<%for(CwbStateControl csc : mapList.get(et.getValue()+"List")){ %>
			<% for(FlowOrderTypeEnum ete : FlowOrderTypeEnum.values()){if(csc.getTostate()==ete.getValue()){ %>
				<%=ete.getText() %><%if(mapList.get(et.getValue()+"List").size()>1){ %> | <%} %>
			<%}}} %>
			</td>
			<td width="15%" align="center" valign="middle" >
			[<a href="<%=request.getContextPath()%>/cwbStateControl/edit/<%=et.getValue() %>;">修改</a>]
			</td>
		</tr>
		<%}} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>