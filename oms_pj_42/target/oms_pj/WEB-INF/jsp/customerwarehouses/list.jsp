<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	List<CustomWareHouse> warehouses = (List<CustomWareHouse>)request.getAttribute("warehouses");
List<Customer> customers = (List<Customer>)request.getAttribute("customers");
Page page_obj = (Page)request.getAttribute("page_obj");
String name;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>供货商仓库管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var CustomerKeyAndValue = new Array();
<%for(Customer c : customers) {%> CustomerKeyAndValue[<%=c.getCustomerid()%>]='<%=c.getCustomername()%>';<%}%>
function getCustomerValue(key,areaid){
	$("#customerid_"+key+"_"+areaid).html(CustomerKeyAndValue[key]);
}	
</script>
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
				<span><input name="" type="button" value="创建供货商仓库" class="input_button1"  id="add_button"  />
				</span>
				<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
					供货商：<select id="customerid" name="customerid">	
								<option value=-1>全部</option>
								<%
									for(Customer c : customers){
								%>
								<option value=<%=c.getCustomerid()%> ><%=c.getCustomername()%></option>
								<%
									}
								%>	
							  </select>
							<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="搜索" class="input_button2" />
							<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
				</form>
				</div>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				  <tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">仓库名</td>
						<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
					</tr>
					  <%
					  	for(CustomWareHouse b : warehouses){
					  %>
					<tr>
						<td width="10%" align="center" valign="middle"><%=b.getWarehouseid() %></td>
						<td width="20%" align="center" valign="middle" id="customerid_<%=b.getCustomerid()%>_<%=b.getWarehouseid()%>"><script>getCustomerValue(<%=b.getCustomerid()%>,<%=b.getWarehouseid()%>)</script></td>
						<td width="20%" align="center" valign="middle"><%=b.getCustomerwarehouse()%></td>
						<td width="30%" align="center" valign="middle"><%=b.getWarehouseremark() %></td>
						<td width="20%" align="center" valign="middle" >
						[<a href="javascript:del(<%=b.getWarehouseid()%>);"><%=b.getIfeffectflag()==1?"停用":"启用" %></a>]
						[<a href="javascript:edit_button(<%=b.getWarehouseid()%>);">修改</a>]
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
$("#customerid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("customerid")) %>);
</script>
<!-- 创建供货商仓库的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/customerwarehouses/add" />
<!-- 修改供货商仓库的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/customerwarehouses/edit/" />
<!-- 删除供货商仓库的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/customerwarehouses/del/" />
</body>
</html>


