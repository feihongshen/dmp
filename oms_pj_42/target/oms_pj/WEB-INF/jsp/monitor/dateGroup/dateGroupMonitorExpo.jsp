
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.EmaildateTDO"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
  <%MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); %> 
  <%MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate"); %> 
  <%MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); %> 
 <%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=GBK"); %> 
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
		<th colspan="2">导入数据</th>
		<th colspan="2">有货无单</th>
		<th colspan="2">有单无货</th>
		
	</tr>
	<tr>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
	</tr>
	<tr>
		<td><%=daoruDate.getCountsum()%></td>              
		<td><%=daoruDate.getCaramountsum()%></td>
		<td><%=youhuowudanDate.getCountsum()%></td>             
		<td><%=youhuowudanDate.getCaramountsum()%></td>         
		<td><%=youdanwuhuoDate.getCountsum()%></td>              
		<td><%=youdanwuhuoDate.getCaramountsum()%></td>          
	</tr>
	</table>
</div>
</body>

</html>  