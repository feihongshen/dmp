<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> cwbOrderList = (List<CwbOrder>)request.getAttribute("cwborderList");
  int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
%>
<%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=UTF-8"); %> 
<% response.setHeader("Content-disposition","attachment; filename=order.xls");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
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

</HEAD>
<body style="background:#eef9ff">
   <div class="menucontant">
   
   <table class="datagrid">
							<tr class="font_1">
								<th width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单编号</th>
								<th width="10%"align="center" valign="middle" bgcolor="#eef6ff">商品名称</th>
								<th width="8%" align="center" valign="middle" bgcolor="#eef6ff">货物金额</th>
								<th width="6%" align="center" valign="middle" bgcolor="#eef6ff">收件人姓名</th>
								<th width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人电话/手机</th>
								<th width="25%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</th>
								<th width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</th>
							    
							</tr>
							<% for(CwbOrder c : cwbOrderList){ %>
							<tr valign="middle">
										<td align="center" valign="middle" >&nbsp;<%=c.getCwb().toString()%></td>
										<td align="center" valign="middle" ><%=c.getSendcarname()%></td>
										<td align="center" valign="middle" ><%=c.getCaramount()%></td>
										<td align="center" valign="middle" ><%=c.getConsigneename()%> </td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										   <%if(c.getConsigneephone() != null && !"".equals(c.getConsigneephone())){%><%=c.getConsigneephone()%> / <%}%><%=c.getConsigneemobile()%> 
										<%}else{%>
											[不可见]
										<%} %>
										</td>
										<td align="center" valign="middle" ><%=c.getConsigneeaddress()%></td>
										<td align="center" valign="middle" ><%=c.getEmaildate()%></td>
										
									</tr>
							 <%} %> 
							 <tr valign="middle">
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										
							 <tr valign="middle">
										
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
							 <tr valign="middle">
										
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
					   </table>
   </div> 
   
</body>

</HTML>
