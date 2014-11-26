
<%@page import="cn.explink.controller.gotoClassAuditingView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.GotoClassAuditing"%>
<%@page import="cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> us = (List<User>)request.getAttribute("us");
List<gotoClassAuditingView> gcas = (List<gotoClassAuditingView>)request.getAttribute("gcas");
Page page_obj = (Page)request.getAttribute("page_obj");

String usedeliverpayup = request.getAttribute("usedeliverpayup")==null?"no":(String)request.getAttribute("usedeliverpayup");
%>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>归班审核历史</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script type="text/javascript">

function addInit(){
	window.parent.init_branch();
}
function addSuccess(data){
	$("#searchForm").submit();
}
function editInit(){
    
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
$(function() {
	$("#begindate").datepicker();
	$("#enddate").datepicker();
});
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
		<span></span>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			归班人：<select name="deliverealuser" id="deliverealuser">
			<option value="0">请选择</option>
			<%for(User u : us){ %>
			<option value="<%=u.getUserid() %>"><%=u.getRealname() %></option>
			<%} %>
			</select>
			归班时间：
			<input  type="text" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" id="begindate" name="begindate"/>
			  到
			<input type="text" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" id="enddate" name="enddate"/>
			(默认为当天归班记录)
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询"  class="input_button2" />
			<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
			<a href="../auditView"><< 审核归班</a>
		</form>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">归班序号</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">归班金额</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">归班人</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">审核人</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">上交状态</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
						
					</tr>
					  <% for(gotoClassAuditingView gca : gcas){ %>
					<tr>
					 	<td width="10%" align="center" valign="middle"><%=gca.getId() %></td>
						<td width="20%" align="center" valign="middle"><%=gca.getAuditingtime() %></td>
						<td width="10%" align="center" valign="middle"><%=gca.getPayupamount() %></td>
						<td width="15%" align="center" valign="middle"><%=gca.getDeliverealuser_name() %></td>
						<td width="15%" align="center" valign="middle"><%=gca.getReceivedfeeuser_name() %></td>
						<td width="10%" align="center" valign="middle"><%=gca.getPayupid()!=0?"已上交":"未上交" %></td>
						<td width="20%" align="center" valign="middle" >[<a href="javascript:getViewBox(<%=gca.getId() %>);">详情</a>]　
						<%if(gca.getUpdatetime()!=null&&!gca.getUpdatetime().equals("")){ %>
						[<a href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?gcaid=<%=gca.getId() %>');">改单详情</a>]
						<%} %>
						</td>
					</tr>
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				<%} %>
			</div>
			
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#deliverealuser").val(<%=request.getParameter("deliverealuser")==null?"0":request.getParameter("deliverealuser") %>);
</script>
<!-- 查看历史详情 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/delivery/viewOldSub/" />
</body>
</html>
