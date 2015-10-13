
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
  <%MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("rukuzongliangDate"); %> 
 <%MonitorDTO weirukuDate =(MonitorDTO) request.getAttribute("weirukuDate"); %> 
 <%MonitorDTO yirukuDate =(MonitorDTO) request.getAttribute("yirukuDate"); %> 
 <%MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); %> 
 <%MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate"); %> 
 <%MonitorDTO chukuzaituDate =(MonitorDTO) request.getAttribute("chukuzaituDate"); %> 
 <%MonitorDTO kucunDate =(MonitorDTO) request.getAttribute("kucunDate"); %> 
 <%String branchName = request.getAttribute("nowbranchName")==null?"全部":request.getAttribute("nowbranchName").toString(); %> 
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
		<th colspan="1">库房</th>
		<th colspan="2">应入库总量</th>
		<th colspan="2">未入库</th>
		<th colspan="2">已入库</th>
		<th colspan="2">有单无货</th>
		<th colspan="2">有货无单</th>
		<th colspan="2">出库在途</th>
		<th colspan="2">库存</th>
	</tr>
	<tr>
		<td>-</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
		<td>单数</td>
		<td>金额</td>
	</tr>
	<tr>
		<td><%=(branchName==null||"".equals(branchName))?"全部":branchName%></td>              
		<td><%=daoruDate.getCountsum()%></td>             
		<td><%=daoruDate.getCaramountsum()%></td>         
		<td><%=weirukuDate.getCountsum()%></td>             
		<td><%=weirukuDate.getCaramountsum()%></td>         
		<td><%=yirukuDate.getCountsum()%></td>              
		<td><%=yirukuDate.getCaramountsum()%></td>          
		<td><%=youdanwuhuoDate.getCountsum()%></td>               
		<td><%=youdanwuhuoDate.getCaramountsum()%></td>           
		<td><%=youhuowudanDate.getCountsum()%></td>               
		<td><%=youhuowudanDate.getCaramountsum()%></td>           
		<td><%=chukuzaituDate.getCountsum()%></td>                
		<td><%=chukuzaituDate.getCaramountsum()%></td>            
		<td><%=kucunDate.getCountsum()%></td>             
		<td><%=kucunDate.getCaramountsum()%></td>         
	</tr>
	</table>
</div>
</body>

</html>  