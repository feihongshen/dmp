<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWarHouse"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
List<CustomWarHouse> warehouses = (List<CustomWarHouse>)request.getAttribute("warehouses");
List<Customer> customers = (List<Customer>)request.getAttribute("customers");
Page page_obj = (Page)request.getAttribute("page_obj");
String name;
%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;border-style: dashed;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">

var CustomerKeyAndValue = new Array();
<%for(Customer c : customers) {%> CustomerKeyAndValue[<%=c.getCustomerid() %>]='<%=c.getCustomername() %>';<%} %>
function getCustomerValue(key,areaid){
	$("#customerid_"+key+"_"+areaid).html(CustomerKeyAndValue[key]);
}
	
</script>
</HEAD>
<BODY>

<form action="1" method="post" name="form1" id="form1">
	查询<select id="customerid" name="customerid">	
		<option value=-1>全部</option>
		<% for(Customer c : customers){ %>
		<option value=<%=c.getCustomerid()%> ><%=c.getCustomername() %></option>
		<%} %>	
	</select><br>
	<input type="submit" value="查询">
	<a href="../add">新建</a>
</form>
<%if(page_obj.getTotal()!=0){ %>
<div>
 <a href="javascript:;" onclick="$('#form1').attr('action','1');$('#form1').submit()">第一页</a>
<a href="javascript:;" onclick="$('#form1').attr('action','<%=page_obj.getPrevious() %>');$('#form1').submit()">上一页</a>
<a href="javascript:;" onclick="$('#form1').attr('action','<%=page_obj.getNext() %>');$('#form1').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#form1').attr('action','<%=page_obj.getMaxpage() %>');$('#form1').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　
		<select id="selectPg" onchange="$('#form1').attr('action',$(this).val());$('#form1').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>" ><%=i %></option>
		<% } %>
		</select>
	</div>
</div>
<script type="text/javascript">$("#selectPg").val(<%=request.getAttribute("page") %>)</script>
	     <% for(CustomWarHouse b : warehouses){ %>
	     <ul>
	     	<li><%=b.getWarhouseid() %></li>
	     	<li id="customerid_<%=b.getCustomerid() %>_<%=b.getWarhouseid()%>"><script>getCustomerValue(<%=b.getCustomerid()%>,<%=b.getWarhouseid()%>)</script>　</li>
	     	<li><%=b.getCustomerwarehouse()%></li>
	     	<li><%=b.getWarehouseremark() %></li>
	     	<li><a onclick="return confirm('确定要停用?')"  href="../del/<%=b.getWarhouseid()%>">停用</a></li>
	     	<li><a href ="<%=request.getContextPath()%>/customerwarhouses/edit/<%=b.getWarhouseid()%>">修改</a></li>
	     	 </ul>
	     <%}} %>
	
	<script type="text/javascript">
	$("#customerid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("customerid")) %>);
	</script>
	
</BODY>
</HTML>
