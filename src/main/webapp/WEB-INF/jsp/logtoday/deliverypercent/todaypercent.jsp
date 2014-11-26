<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
Map<Long,Map<Long,Long>> yingtouMap = request.getAttribute("yingtouMap")==null?new HashMap<Long,Map<Long,Long>>() : (Map<Long,Map<Long,Long>>)request.getAttribute("yingtouMap");
Map<Long,Map<Long,Long>> shitouMap = request.getAttribute("shitouMap")==null?new HashMap<Long,Map<Long,Long>>() : (Map<Long,Map<Long,Long>>)request.getAttribute("shitouMap");
Map<Long,Map<Long,Long>> tuotouMap = request.getAttribute("tuotouMap")==null?new HashMap<Long,Map<Long,Long>>() : (Map<Long,Map<Long,Long>>)request.getAttribute("tuotouMap");
List<Branch> branchList = request.getAttribute("branchList")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchList");
List<Customer> customerList = request.getAttribute("customerList")==null?new ArrayList<Customer>():(List<Customer>)request.getAttribute("customerList");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>今日投递率</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">

<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#totailPercent_title").html($("#totailPercent").html());
});
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
			&nbsp;&nbsp;公司当日妥投率：<strong class="high" id="totailPercent_title">90% </strong>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
	<div style="width:100%; overflow-x:scroll">
	<table width="<%=request.getAttribute("width")%>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="120" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">站点</td>
   		<%for(Customer c :customerList){ %>
	   		<td colspan="4" align="center" valign="middle" bgcolor="#eef6ff" ><%=c.getCustomername() %></td>
   		<%} %>
	   		<td width="50" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">合计</td>
	   		</tr>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
   		<%for(Customer c :customerList){ %>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff">应投</td>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff">实投</td>
			<td width="100" align="center" valign="middle" bgcolor="#eef6ff">妥投</td>
			<td width="50" align="center" valign="middle" bgcolor="#eef6ff">妥投率</td>
		<%} %>
			</tr>
	<%for(Branch b : branchList){ %>
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle"><%=b.getBranchname() %></td>
		<%
		Long customer_yingtou = 0L;
		Long customer_tuotou = 0L;
		for(Customer c :customerList){
			Long yingtou = 0L;
			if(yingtouMap.get(b.getBranchid())!=null&&yingtouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
			    yingtou = yingtouMap.get(b.getBranchid()).get(c.getCustomerid());
			}
			Long shitou = 0L;
			if(shitouMap.get(b.getBranchid())!=null&&shitouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
				shitou = shitouMap.get(b.getBranchid()).get(c.getCustomerid());
			} 
			Long tuotou = 0L;
			if(tuotouMap.get(b.getBranchid())!=null&&tuotouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
				tuotou = tuotouMap.get(b.getBranchid()).get(c.getCustomerid());
			}
			BigDecimal percent = yingtou==0?BigDecimal.ZERO:new BigDecimal(tuotou*100).divide(new BigDecimal(yingtou),2,BigDecimal.ROUND_HALF_UP);
			
			customer_yingtou += yingtou ;
			customer_tuotou += tuotou ;
			%>
			<td align="center" valign="middle"><%=yingtou %></td>
			<td align="center" valign="middle"><%=shitou %></td>
			<td align="center" valign="middle"><%=tuotou %></td>
			<td bgcolor="#FFE900" align="center" valign="middle"><%=percent %>%</td>
		<%}%>
			<td bgcolor="#FFE900"  align="center" valign="middle"><%=customer_yingtou==0?BigDecimal.ZERO:new BigDecimal(customer_tuotou*100).divide(new BigDecimal(customer_yingtou),2,BigDecimal.ROUND_HALF_UP) %>%</td>
			</tr>
	<%} %>
		</tbody>
</table>
	<table width="<%=request.getAttribute("width")%>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
		<tbody>
			<tr style="background-color: rgb(249, 252, 253); ">
				<td width="120" align="center" valign="middle" bgcolor="#f1f1f1">合计</td>
				<%
				Long  totail_yingtou = 0L;
				Long  totail_tuotou = 0L;
				for(Customer c :customerList){
					Long yingtou = 0L;
					Long shitou = 0L;
					Long tuotou = 0L;
					BigDecimal percent = BigDecimal.ZERO;
					for(Branch b : branchList){ 
						
						if(yingtouMap.get(b.getBranchid())!=null&&yingtouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
							yingtou += yingtouMap.get(b.getBranchid()).get(c.getCustomerid());
						}
						
						if(shitouMap.get(b.getBranchid())!=null&&shitouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
							shitou += shitouMap.get(b.getBranchid()).get(c.getCustomerid());
						} 
						
						if(tuotouMap.get(b.getBranchid())!=null&&tuotouMap.get(b.getBranchid()).get(c.getCustomerid())!=null){
							tuotou += tuotouMap.get(b.getBranchid()).get(c.getCustomerid());
						}
					}
					 percent = yingtou==0?BigDecimal.ZERO:new BigDecimal(tuotou*100).divide(new BigDecimal(yingtou),2,BigDecimal.ROUND_HALF_UP);
					 totail_yingtou += yingtou ;
					 totail_tuotou += tuotou ;
					%>
				<td width="100" align="center" valign="middle" bgcolor="#f1f1f1"><%=yingtou %></td>
				<td width="100" align="center" valign="middle" bgcolor="#f1f1f1"><%=shitou %></td>
				<td width="100" align="center" valign="middle" bgcolor="#f1f1f1"><%=tuotou %></td>
				<td bgcolor="#FFE900" width="50" align="center" valign="middle" bgcolor="#f1f1f1"><%=percent %>%</td>
				<%}%>
				<td bgcolor="#FFE900"  id="totailPercent" width="50" align="center" valign="middle" bgcolor="#f1f1f1"><%=totail_yingtou==0?BigDecimal.ZERO:new BigDecimal(totail_tuotou*100).divide(new BigDecimal(totail_yingtou),2,BigDecimal.ROUND_HALF_UP)  %>%</td>
			</tr>
			</tbody>
	</table>
	<div class="jg_35"></div>
	</div>
	</div>

</div>
</body>
</html>