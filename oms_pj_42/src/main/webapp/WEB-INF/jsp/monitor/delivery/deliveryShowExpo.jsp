
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorOrderDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
 <%List<MonitorOrderDTO> showDateList =(List<MonitorOrderDTO>) request.getAttribute("showDateList");
 int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());%> 
 <%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=UTF-8"); %> 
<% response.setHeader("Content-disposition","attachment; filename=dateMonitor.xls");%>
    <html>

<head>
<style type="text/css">
body{
	margin: 6px;
	padding: 0;
	font-size: 12px;
	font-family: tahoma, arial;
	background: #fff;
}

table{
	width: 100%;
}
tr.odd{
	background: #fff;
}

tr.even{
	background: #eee;
}

div.datagrid_div{
	width: 100%;
	border: 1px solid #999;
}


table.datagrid{
	border-collapse: collapse; 
}

table.datagrid th{
	text-align: left;
	background: #9cf;
	padding: 3px;
	border: 1px #333 solid;
}

table.datagrid td{
	padding: 3px;
	border: none;
	border:1px #333 solid;
}

tr:hover,
tr.hover{
	background: #9cf;
}
</style>
</head>

<body>
 
<div style="width: 880px;">
	<table class="datagrid">
	<tr>
		<th >序号</th>
		<th >订单编号</th>
		<th >商品名称</th>
		<th >货物价钱</th>
		<th >收货人姓名</th>
		<th >收货人手机</th>
		<th >站名</th>
		<th >小件员</th>
		<th >发货时间</th>
	</tr>
	<% int i=1; %>
	 <% for(MonitorOrderDTO mo : showDateList){ %>
	<tr>
		<td><%=i++ %> </td>
		<td><%=mo.getCwb().toString() %>&nbsp; </td>
		<td><%=mo.getSendcarname() %></td>
		<td><%=mo.getCaramount() %></td>
		<td><%=mo.getConsigneename() %></td>
		<td>
		<%if(usershowphoneflag==1){ %>
		 <%=mo.getConsigneemobile() %>
		<%}else{%>
		  [不可见]
		<%} %>
		</td>
		<td><%=mo.getBranchname() %></td>
		<td><%=mo.getRealname() %></td>
		<td><%=mo.getEmaildate() %></td>
	</tr>
	<%} %> 
	</table>
</div>
</body>

</html>  