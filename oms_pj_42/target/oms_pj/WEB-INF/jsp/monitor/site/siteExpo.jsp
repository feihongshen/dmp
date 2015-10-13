
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
 <%
 
 MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); 
 MonitorDTO youdanwuhuoDate =(MonitorDTO) request.getAttribute("youdanwuhuoDate"); 
 MonitorDTO youhuowudanDate =(MonitorDTO) request.getAttribute("youhuowudanDate");
 MonitorDTO weidaohuoDate =(MonitorDTO) request.getAttribute("weidaohuoDate"); 
 MonitorDTO rukuweilingDate =(MonitorDTO) request.getAttribute("rukuweilingDate"); 
	MonitorDTO yichangdanDate =(MonitorDTO) request.getAttribute("yichangdanDate"); 
	MonitorDTO yilinghouDate =(MonitorDTO) request.getAttribute("yilinghuoDate"); 
	MonitorDTO yiliudanDate =(MonitorDTO) request.getAttribute("yiliudanDate"); 
	MonitorDTO kucuntuihuoDate =(MonitorDTO) request.getAttribute("kucuntuihuoDate"); 
	MonitorDTO kucunzhiliuDate =(MonitorDTO) request.getAttribute("kucunzhiliuDate"); 
	MonitorDTO zaituzhongzhuanDate =(MonitorDTO) request.getAttribute("zaituzhongzhuanDate"); 
	MonitorDTO zaitutuihuoDate =(MonitorDTO) request.getAttribute("zaitutuihuoDate"); 
	MonitorDTO weijiaokuanDate =(MonitorDTO) request.getAttribute("weijiaokuanDate"); 
	MonitorDTO qiankuanDate =(MonitorDTO) request.getAttribute("qiankuanDate"); 
 
 %>
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
 
<div >
	<table class="datagrid">
	<tr>
		<th colspan="2">未到货</th>
		<th colspan="8">已到货</th>
		<th colspan="2">已领货</th>
		<th colspan="4">库存</th>
		<th colspan="4">在途</th>
		<th colspan="2">未交款</th>
		<th colspan="2">欠款</th>
	</tr>
	<tr>
		<td colspan="2">未到货</td>
		<td colspan="2">入库未领</td>
		<td colspan="2">有货无单</td>
		<td colspan="2">异常单</td>
		<td colspan="2">有单无货</td>
		<td colspan="2">已领货</td>
		<td colspan="2">退货</td>
		<td colspan="2">滞留</td>
		<td colspan="2">中转</td>
		<td colspan="2">退货</td>
		<td colspan="2">未交款</td>
		<td colspan="2">欠款</td>
	</tr>
	<tr>
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
		<td><%=weidaohuoDate.getCountsum()%></td>             
		<td><%=weidaohuoDate.getCaramountsum()%></td>         
		<td><%=rukuweilingDate.getCountsum()%></td>              
		<td><%=rukuweilingDate.getCaramountsum()%></td>          
		<td><%=youhuowudanDate.getCountsum()%></td>               
		<td><%=youhuowudanDate.getCaramountsum()%></td>           
		<td><%=yichangdanDate.getCountsum()%></td>               
		<td><%=yichangdanDate.getCaramountsum()%></td>           
		<td><%=youhuowudanDate.getCountsum()%></td>               
		<td><%=youhuowudanDate.getCaramountsum()%></td>           
		<td><%=yilinghouDate.getCountsum()%></td>             
		<td><%=yilinghouDate.getCaramountsum()%></td>         
		<td><%=kucuntuihuoDate.getCountsum()%></td>             
		<td><%=kucuntuihuoDate.getCaramountsum()%></td>         
		<td><%=kucunzhiliuDate.getCountsum()%></td>             
		<td><%=kucunzhiliuDate.getCaramountsum()%></td>         
		<td><%=zaituzhongzhuanDate.getCountsum()%></td>             
		<td><%=zaituzhongzhuanDate.getCaramountsum()%></td>         
		<td><%=zaitutuihuoDate.getCountsum()%></td>             
		<td><%=zaitutuihuoDate.getCaramountsum()%></td>         
		<td><%=weijiaokuanDate.getCountsum()%></td>             
		<td><%=weijiaokuanDate.getCaramountsum()%></td>         
		<td><%=qiankuanDate.getCountsum()%></td>             
		<td><%=qiankuanDate.getCaramountsum()%></td>         
	</tr>
	</table>
</div>
</body>

</html>  