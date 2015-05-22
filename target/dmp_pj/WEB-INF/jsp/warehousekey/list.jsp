<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> kflist = (List<Branch>)request.getAttribute("kflist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<WarehouseKey> warehouseKeys = (List<WarehouseKey>)request.getAttribute("warehousekeys");
WarehouseKey warehouseKey = (WarehouseKey)request.getAttribute("importCwbOrderType");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房对应省市管理</title>
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

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	<span><input name="" type="button" value="创建对应记录" class="input_button1"  id="add_button"  />
	</span>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		目标仓库： <select name="targetcarwarehouseid" id="targetcarwarehouseid">
			<option value="-1">请选择目标仓库</option>
			<%for(Branch b : kflist){ %>
				<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
			<%} %>
		</select><input type="submit"  onclick="$('#searchForm').attr('action',1);return true;"  id="find" value="查询" class="input_button2" />
		<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">目标仓库</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">目标仓库关键字，对应省市字段</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(WarehouseKey w : warehouseKeys){ %>
		<tr>
			 <% for(Branch b : branchlist){if(w.getTargetcarwarehouseid()==b.getBranchid()){ %>
	         <td width="30%" align="center" valign="middle"><%=b.getBranchname() %></td>
	         <%}} %>
			<td width="30%" align="center" valign="middle"><%=w.getKeyname()%></td>
			<td width="40%" align="center" valign="middle" >
			[<a href="javascript:del(<%=w.getId()%>);"><%=(w.getIfeffectflag()==1?"停用":"启用")%></a>]
			[<a href="javascript:edit_button(<%=w.getId()%>);">修改</a>]
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
</div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#targetcarwarehouseid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("targetcarwarehouseid")) %>);
</script>
<!-- 创建订单类型的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/warehousekey/add" />
<!-- 修改订单类型的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/warehousekey/edit/" />
<!-- 删除订单类型的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/warehousekey/del/" />
</body>
</html>
