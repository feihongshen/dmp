<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.TransferResMatch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<TransferResMatch> reasonList = (List<TransferResMatch>)request.getAttribute("reasonList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中转原因匹配设置</title>
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
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
	var value = initReasontype.split(",")[0];
	var name = initReasontype.split(",")[1];
	$("#"+name, parent.document).val(value);
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
	<span><input name="" type="button" value="新建匹配" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		 系统中转原因：<input type="text"  name="systemreason" value=""/>
		 <input type="submit"  onclick="$('#searchForm').attr('action',1);return true;"  id="find" value="查询" class="input_button2" />
		 
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">编号</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">中转原因(常用语)</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">系统规定原因</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
	<%for(TransferResMatch r:reasonList){ %>
		<tr>
			<td width="15%" align="center" valign="middle"><%=r.getId()%></td>
			<td width="20%" align="center" valign="middle"><%=r.getReasonname() %></td>
			<td width="40%" align="center" valign="middle"><%=r.getTransferreasonname()%></td>
			<td width="25%" align="center" valign="middle">
			[<a href="javascript:edit_button(<%=r.getId() %>);">修改</a>]
			[<a href="javascript:if(confirm('删除后不可恢复,是否确定删除?'))del('<%=r.getId()%>');">删除</a>]
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
$("#reasontype").val(<%=request.getParameter("reasontype")==null?"0":request.getParameter("reasontype") %>);
</script>
<!-- 创建常用于设置的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/transferReason/add" />
<!-- 修改常用于设置的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/transferReason/edit/" />
<!-- 删除的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/transferReason/del/" />
</body>
</html>

