
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");

Branch branchNow =(Branch) request.getAttribute("branchNow");

List deliveryList =(List) request.getAttribute("deliveryList");

Map typeNameMap =(Map) request.getAttribute("typeNameMapPram");

String typeList =(String) (request.getAttribute("typeList")==null?"":request.getAttribute("typeList"));
String[] typeListS = typeList.split(",");

String typenameList =(String) request.getAttribute("typenameList");

%>
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
 
 <div style="text-align:center;">
	<table class="datagrid">
	<tr>
		<th > 站点</th>
		<th >状态</th>
		<th >订单数量</th>
		
	</tr>
	<% if(typeListS.length>0){%> 
	<%for(int j=0;j<typeListS.length;j++){%>
	<tr>
		<td><%=branchNow.getBranchname()%></td>              
		<td><%=typeNameMap.get(typeListS[j])%></td>
		<td><%=((Map)deliveryList.get(j)).get(typeListS[j])%></td>             
	</tr>
	<%} %>
	<%} %>
	</table>
</div>
</body>

</html>  