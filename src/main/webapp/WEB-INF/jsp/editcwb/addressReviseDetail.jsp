<%@page import="cn.explink.domain.OrderAddressRevise"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%
    List<OrderAddressRevise> orderAddressRevises=request.getAttribute("orderAddressRevises")==null?new ArrayList<OrderAddressRevise>():(List<OrderAddressRevise>)request.getAttribute("orderAddressRevises");
    %>
    
<html>
<head>
</head>
<body>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>订单相关信息修改详情</h1>
		<div class="tabbox">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
										<tr class="font_1">
											<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">订单号</td>
											<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">收件人姓名修改信息</td>
											<td valign="middle"  align="center" width="10%" align="center" bgcolor="#e7f4e3">收件人电话修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">收件人地址修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">配送站点修改信息</td>
											<td valign="middle" align="center"  width="10%"   align="center" bgcolor="#e7f4e3">电商要求修改信息</td>
											<td valign="middle"  align="center" width="10%" bgcolor="#e7f4e3">修改人</td>
											<td valign="middle"  align="center" width="10%" bgcolor="#e7f4e3">修改时间</td>
											
									</tr>
									<%for(OrderAddressRevise data:orderAddressRevises){
									%>
									<tr>
									<td   valign="middle" align="center"  ><%=data.getCwb() %></td>
									<td   valign="middle" align="center"  ><%=data.getReceivemen()==null?"":data.getReceivemen() %></td>
									<td   valign="middle" align="center"  ><%=data.getPhone()==null?"":data.getPhone() %></td>
									<td   valign="middle"  align="center" ><textarea readonly="readonly"><%=data.getAddress()%></textarea></td>
									<td   valign="middle"  align="center" ><%=data.getDestination()==null?"":data.getDestination() %></td>
									<td   valign="middle"  align="center" ><%=data.getCustomerrequest()==null?"":data.getCustomerrequest() %></td>
									<td   valign="middle"  align="center" ><%=data.getModifiername() %></td>
									<td   valign="middle"  align="center" ><%=data.getRevisetime() %></td>
									</tr>
									<%} %>
										
								</table>
							</td>
						</tr>
					</tbody>
				</table></div>
	</div>
</div>
</body>
</html>