
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.DeliveryState"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
  List<DeliveryState> deliverySteteList = (List<DeliveryState>)request.getAttribute("deliveryStatelist"); 
  int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
  Page page_obj =(Page)request.getAttribute("page_obj");
%>
<%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=UTF-8"); %> 
<% response.setHeader("Content-disposition","attachment; filename=payup.xls");%>
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
								<th>订单编号</th>
								<th >派送员</th>
								<th >收到总金额</th>
								<th >退还金额</th>
								<th >应处理金额</th>
								<th >现金实收</th>
								<th >pos实收</th>
								<th >pos备注</th>
								<th >pos反馈时间</th>
								<th >支票实收</th>
								<th >支票号备注</th>
								<th >商品名称</th>
								<th >货物金额</th>
								<th >收件人姓名</th>
								<th >收件人电话/手机</th>
								<th >收件人地址</th>
								<th >发货时间</th>
							</tr>
							<% for(DeliveryState d : deliverySteteList){ %>
							<tr>
										<td><%=d.getCwb()%></td>
										<td><%=d.getDeliveryName()==null?"":d.getDeliveryName()%></td>
										<td><%=d.getReceivedfee()%></td>
										<td><%=d.getReturnedfee()%> </td>
										<td><%=d.getBusinessfee()%></td>
										<td><%=d.getCash()%></td>
										<td><%=d.getPos()%></td>
										<td><%=d.getPosremark()==null?"":d.getPosremark()%></td>
										<td><%=d.getMobilepodtime()%></td>
										<td><%=d.getCheckfee()%></td>
										<td><%=d.getCheckremark()==null?"":d.getCheckremark()%></td>
										<td><%=d.getSendcarname()%></td>
										<td><%=d.getCaramount()%></td>
										<td><%=d.getConsigneename()%> </td>
										<td>
										<%if(usershowphoneflag==1){ %>
										<%=d.getConsigneephone()%> 
										<%}else{ %>[不可见]
										<%} %>
										</td>
										<td><%=d.getConsigneeaddress()%></td>
										<td><%=d.getEmaildate()%></td>
									</tr>
							 <%} %> 
					   </table>
	</div>
</body>
</HTML>
