<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供货商管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
function editInit(){
	//无处理
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建供货商" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		供货商公司名称：<input type ="text" name ="customername"value="<%=request.getParameter("customername")==null?"":request.getParameter("customername") %>" />
		<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="搜索" class="input_button2" />
		<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">供货商名称</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">地址</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">联系人</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">电话</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<% for(Customer c : customerList){ %>
		<tr>
			<td width="10%" align="center" valign="middle"><%=c.getCustomerid()%></td>
			<td width="20%" align="center" valign="middle"><%=c.getCustomername()%></td>
			<td width="25%" align="center" valign="middle"><%=c.getCustomeraddress()%></td>
			<td width="15%" align="center" valign="middle"><%=c.getCustomercontactman()%></td>
			<td width="15%" align="center" valign="middle"><%=c.getCustomerphone()%></td>
			<td width="15%" align="center" valign="middle" >
			[<a href="javascript:del(<%=c.getCustomerid()%>);"><%=(c.getIfeffectflag()==1?"停用":"启用") %></a>]
			[<a href="javascript:edit_button(<%=c.getCustomerid()%>);">修改</a>]
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
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
</script>
<!-- 创建供货商的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/customer/add" />
<!-- 修改供货商的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/customer/edit/" />
<!-- 删除供货商的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/customer/del/" />
</body>
</html>

