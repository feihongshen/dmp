
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.EmaildateTDO"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
 <%MonitorDTO daoruDate =(MonitorDTO) request.getAttribute("daoruDate"); %> 
 <%MonitorDTO kufangDate =(MonitorDTO) request.getAttribute("kufangDate"); %> 
 <%MonitorDTO zaituDate =(MonitorDTO) request.getAttribute("zaituDate"); %> 
 <%MonitorDTO zhanDate =(MonitorDTO) request.getAttribute("zhanDate"); %> 
 <%MonitorDTO renDate =(MonitorDTO) request.getAttribute("renDate"); %> 
 <%MonitorDTO tuihuoDate =(MonitorDTO) request.getAttribute("tuihuoDate"); %> 
 <%MonitorDTO zhongzhuanDate =(MonitorDTO) request.getAttribute("zhongzhuanDate"); %> 
 <%MonitorDTO chenggongDate =(MonitorDTO) request.getAttribute("chenggongDate"); %> 
 <%MonitorDTO diushiDate =(MonitorDTO) request.getAttribute("diushiDate"); %> 
 <%MonitorDTO yichangDate =(MonitorDTO) request.getAttribute("yichangDate"); %> 
 <%MonitorDTO chaDate =(MonitorDTO) request.getAttribute("chaDate"); %> 
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
		<th colspan="2">导入数据</th>
		<th colspan="2">库房</th>
		<th colspan="2">在途</th>
		<th colspan="2">站点</th>
		<th colspan="2">小件员</th>
		<th colspan="2">退货</th>
		<th colspan="2">中转</th>
		<th colspan="2">成功</th>
		<th colspan="2">丢失</th>
		<th colspan="2">异常</th>
		<th colspan="2">差</th>
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
	</tr>
	<tr>
		<td><%=daoruDate.getCountsum()%></td>              
		<td><%=daoruDate.getCaramountsum()%></td>
		<td><%=kufangDate.getCountsum()%></td>             
		<td><%=kufangDate.getCaramountsum()%></td>         
		<td><%=zaituDate.getCountsum()%></td>              
		<td><%=zaituDate.getCaramountsum()%></td>          
		<td><%=zhanDate.getCountsum()%></td>               
		<td><%=zhanDate.getCaramountsum()%></td>           
		<td><%=renDate.getCountsum()%></td>                
		<td><%=renDate.getCaramountsum()%></td>            
		<td><%=tuihuoDate.getCountsum()%></td>             
		<td><%=tuihuoDate.getCaramountsum()%></td>         
		<td><%=zhongzhuanDate.getCountsum()%></td>         
		<td><%=zhongzhuanDate.getCaramountsum()%></td>     
		<td><%=chenggongDate.getCountsum()%></td>          
		<td><%=chenggongDate.getCaramountsum()%></td>      
		<td><%=diushiDate.getCountsum()%></td>             
		<td><%=diushiDate.getCaramountsum()%></td>         
		<td><%=yichangDate.getCountsum()%></td>            
		<td><%=yichangDate.getCaramountsum()%></td>
		<td><%=chaDate.getCountsum()%></td>
		<td><%=chaDate.getCaramountsum()%></td>
	</tr>
	</table>
</div>
</body>

</html>  