
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.dto.*"%>

<%
 
List<CarAmountStatiticsDTO> datalist =(List<CarAmountStatiticsDTO>) request.getAttribute("datalist");
 
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 Customer customer =(Customer) request.getAttribute("customer");
 long customerid = Long.parseLong(request.getAttribute("customerid")==null?"0":request.getAttribute("customerid").toString());
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
 
<div >
	<table class="datagrid">
	<tr>
		<th >序号</th>
		<th >供货商</th>
		<th >发货总数</th>
		<th >发货总金额</th>
		<th >配送成功数量</th>
		<th >配送成功金额</th>
		<th >退货数量</th>
		<th >退货金额</th>
		<th >库存遗留数量</th>
		<th >库存遗留金额</th>
		<th> 查询导出时间段</th>
	</tr>
<%
		long totalcount=0;
		double totalamount=0;
		long successcount=0;
		double successamount=0;
		long kucuncount=0;
		double kucunamount=0;
		long backcount=0;
		double backamount=0;
		long i=0;
		for(CarAmountStatiticsDTO dt:datalist){
			i++;
			totalcount+=dt.getTotalcount();
			totalamount+=dt.getTotalamount();
			successcount+=dt.getSuccesscount();
			successamount+=dt.getSuccessamount();
			kucuncount+=dt.getKucuncount();
			kucunamount+=dt.getKucunamount();
			backcount+=dt.getBackcount();
			backamount+=dt.getBackamount();
		%>
		<tr>
			<td width="5%" align="center" valign="middle" ><%=i %></td>
			<td width="10%" align="center" valign="middle"><%=dt.getCustomername()%></td>              
			<td width="8%"  align="center" valign="middle"><%=dt.getTotalcount()%></td>              
			<td width="8%"  align="center" valign="middle"><%=dt.getTotalamount()%></td>
			<td width="10%"align="center" valign="middle" ><%=dt.getSuccesscount()%></td>             
			<td width="8%"  align="center" valign="middle"><%=dt.getSuccessamount()%></td>         
			<td width="10%"align="center" valign="middle" ><%=dt.getBackcount()%></td>              
			<td width="8%"  align="center" valign="middle"><%=dt.getBackamount()%></td>          
			<td width="10%"align="center" valign="middle" ><%=dt.getKucuncount()%></td>               
			<td width="8%"  align="center" valign="middle"><%=dt.getKucunamount()%></td>    
			<td><%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>
		-<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %></td>       
		</tr>
		<%
		} %>
	
	<tr>
		    <td>合计：</td>
		    <td></td>
			<td><%=totalcount%>[票]</td>              
			<td><%=JMath.numstring(totalamount)%>[元]</td>
			<td><%=successcount%>[票]</td>             
			<td><%=JMath.numstring(successamount)%>[元]</td>         
			<td><%=backcount%>[票]</td>              
			<td><%=JMath.numstring(backamount)%>[元]</td>          
			<td><%=kucuncount%>[票]</td>               
			<td><%=JMath.numstring(kucunamount)%>[元]</td>                 
			<td></td>  
	   </tr>
	</table>
</div>
</body>

</html>  