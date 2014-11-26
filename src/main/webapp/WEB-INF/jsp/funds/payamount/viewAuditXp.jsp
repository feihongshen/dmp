<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.controller.PayUpDTO"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@page contentType="application/msexcel" %>  
<% response.setContentType("application/vnd.ms-excel;charset=UTF-8"); %> 
<% response.setHeader("Content-disposition","attachment; filename=viewAudit.xls");%>
<%
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<PayUpDTO>  payupList = request.getAttribute("payupList")==null?null:(List<PayUpDTO> )request.getAttribute("payupList");
%>
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
		<th >站点</th>
		<th colspan="2">站点上交时间</th>
		<th colspan="2">员工交款日期</th>
		<th >票数</th>
		<th >当日应上缴</th>
		<th >当日收款（不含pos）</th>
		<th>当日收款（含pos）</th>
		<th >现金[元]</th>
		<th >现金实收[元]</th>
		<th>现金欠款[元]</th>
		<th >pos</th>
		
		<th >pos实收</th>
		<th >pos欠款</th>
		<th >累计欠款</th>
		<th >其他款项</th>
		<th >支票实收</th>
		<th >上交款审核备注</th>
		<th >上交款备注</th>
		<th >上交款方式</th>
		<th >交款类型</th>
		<th >上交款人</th>
		<%if("1".equals(request.getParameter("upstate"))){%>
		<th >审核人</th>
		<th >审核时间</th><%} %>
	</tr>
	<%if(payupList != null && payupList.size()>0){ 
	 for(PayUpDTO payUp : payupList){ %>
	<tr>
		<td><% if(branchList!=null&&branchList.size()>0){for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getBranchname());}}} %></td>              
		<td colspan="2"><%=payUp.getCredatetime().substring(0,10) %></td>
		<td colspan="2"><%=payUp.getCredatetime().substring(0,10) %></td>             
		<td><%=payUp.getAduitJson().getInt("countCwb") %></td>         
		<td><%=payUp.getAmount() %></td>              
		<td><%=payUp.getAmount() %></td>          
		<td><%=payUp.getAmount().add(payUp.getAmountPos()) %></td>               
		<td><%=payUp.getAduitJson().getDouble("sumCash") %> </td>           
		<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
								<td><%=payUp.getAduitJson().getDouble("sumCash")%></td>
								<%}else{ %>
								<td><%=payUp.getRamount()%></td>
								<%} %>            
		<td><%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit());}} %></td>             
		
		
		
		<td><%=payUp.getAmountPos() %></td>         
		
		
		<td><%=payUp.getAmountPos() %>
								</td>         
		
		
		<td><%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getPosarrearagepayupaudit());}} %></td>     
		<td><%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit().add(b.getPosarrearagepayupaudit()));}} %></td>
		
		<td><%=payUp.getAduitJson().getDouble("sumOrderfee") %></td>          
		
		<td> <%=payUp.getAduitJson().getDouble("sumCheckfee") %></td>      
		
		<td><%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%></td>             
		
		<td><%=payUp.getRemark() %></td>         
		
		<td><%=payUp.getWays().replaceAll("1", "银行转账").replaceAll("2", "现金")%></td>            
		<td><%=payUp.getTypes().replaceAll("1", "货款").replaceAll("2", "罚款")%> </td>
		<td><%=StringUtil.nullConvertToEmptyString(payUp.getUpuserrealname()) %></td>
		<% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditinguser()%></td>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditingtime()%></td>
								<%} %>
	</tr>
	<%}} %> 
</table>
</div>



</body>
</html>  
   
