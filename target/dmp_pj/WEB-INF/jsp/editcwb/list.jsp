<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.EditCwbTypeEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.EdtiCwb_DeliveryStateDetail"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<EdtiCwb_DeliveryStateDetail> ecList = (List<EdtiCwb_DeliveryStateDetail>)request.getAttribute("ecList");
List<User> userList = (List<User>)request.getAttribute("userList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>未缴款明细</h1>
		<div id="box_form">
		<table width="900px" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		   <tr class="font_1">
				<td width="100px" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">订单号</td> 
				<td width="260px" colspan="4" align="center" valign="middle" bgcolor="#0ef6ff">修改前</td>
				<td width="320px" colspan="5" align="center" valign="middle" bgcolor="#eef60f">修改后</td> 
				<td width="60px" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
				<td width="80px" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">修改类型</td>
				<td width="40px" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">申请人</td>
				<td width="40px" rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">修改人</td>
				
			</tr>
			<tr class="font_1">
				
				<td align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">非POS实收</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">POS实收</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">实退金额</td>
				
				<td align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">非POS实收</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">POS实收</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">实退金额</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">所在环节</td>
			</tr>
			<%for(EdtiCwb_DeliveryStateDetail ec :ecList){ %>
			<tr>
				<td align="center" valign="middle"><%=ec.getDs().getCwb() %></td>
				<td align="center" valign="middle" bgcolor="#0ef6ff"><%=CwbOrderTypeIdEnum.getByValue(ec.getCwbordertypeid()).getText() %></td>
				<td align="right"  valign="middle" bgcolor="#0ef6ff"><%=ec.getDs().getCash().add(ec.getDs().getCheckfee()).add(ec.getDs().getOtherfee()).doubleValue() %></td>
				<td align="right"  valign="middle" bgcolor="#0ef6ff"><%=ec.getDs().getPos() %></td>
				<td align="right"  valign="middle" bgcolor="#0ef6ff"><%=ec.getDs().getReturnedfee() %></td>
				
				<td align="center" valign="middle" bgcolor="#eef60f"><%=CwbOrderTypeIdEnum.getByValue(ec.getNew_cwbordertypeid()).getText() %></td>
				<td align="right" valign="middle" bgcolor="#eef60f"><%=ec.getNew_receivedfee().subtract(ec.getNew_pos()).doubleValue()%></td>
				<td align="right" valign="middle" bgcolor="#eef60f"><%=ec.getNew_pos() %></td>
				<td align="right" valign="middle" bgcolor="#eef60f"><%=ec.getNew_returnedfee() %></td>
				<td align="center" valign="middle" bgcolor="#eef60f"><%=FlowOrderTypeEnum.getText(ec.getNew_flowordertype()).getText() %></td>
				<td align="center" valign="middle"><%=ec.getDs().getCreatetime() %></td> 
				<td align="center" valign="middle"><%=EditCwbTypeEnum.getByValue(ec.getEditcwbtypeid()).getText() %></td>              
				
				<td align="center" valign="middle"><%for(User u : userList){if(u.getUserid()==ec.getRequestUser()){out.print(u.getRealname());}} %></td>
				<td align="center" valign="middle"><%for(User u : userList){if(u.getUserid()==ec.getEditUser()){out.print(u.getRealname());}} %></td>
			</tr>
			<%} %>
		</table> 
				
		</div>
	</div>
</div>
<div id="box_yy"></div>
