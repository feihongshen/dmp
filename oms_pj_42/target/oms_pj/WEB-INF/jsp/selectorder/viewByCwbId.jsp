<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> oList = (List<CwbOrder>)request.getAttribute("cwborder");
int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>订单详情</h1>
		<div id="box_form">
			<%if(oList!=null && oList.size()>0){ %>
			<% for(CwbOrder o:oList){%>
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="400"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="33%" align="center" valign="middle" bgcolor="#eef6ff" >订单号:</td>
										<td align="center" valign="middle" ><%=o.getCwb()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >退供货商封包批次号:</td>
										<td align="center" valign="middle" ><%=o.getBacktocustomer_awb()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >订单流程类型 :</td>
										<td align="center" valign="middle" ><%=o.getCwbflowflag()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物重量kg:</td>
										<td align="center" valign="middle" ><%=o.getCarrealweight()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物类别:</td>
										<td align="center" valign="middle" ><%=o.getCartype()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货仓库:</td>
										<td align="center" valign="middle" ><%=o.getCarwarehouse()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >商品尺寸:</td>
										<td align="center" valign="middle" ><%=o.getCarsize()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取回货物金额:</td>
										<td align="center" valign="middle" ><%=o.getBackcaramount()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货数量:</td>
										<td align="center" valign="middle" ><%=o.getSendcarnum()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取货数量:</td>
										<td align="center" valign="middle" ><%=o.getBackcarnum()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物金额:</td>
										<td align="center" valign="middle" ><%=o.getCaramount()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取回商品名称:</td>
										<td align="center" valign="middle" ><%=o.getBackcarname()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发出商品名称:</td>
										<td align="center" valign="middle" ><%=o.getSendcarname()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >派送员id:</td>
										<td align="center" valign="middle" ><%=o.getDeliverid()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点收款是否已上交总部 :</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadflag()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点上交款时间:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadtime()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点交款总部审核时:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadchecktime()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >总部交款审核状态:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadcheckflag()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货时间:</td>
										<td align="center" valign="middle" ><%=o.getEmaildate()%></td>
									</tr>
									
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >供货商运单号:</td>
										<td align="center" valign="middle" ><%=o.getShipcwb()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人编号:</td>
										<td align="center" valign="middle" ><%=o.getConsigneeno()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人名称:</td>
										<td align="center" valign="middle" ><%=o.getConsigneename()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人地址:</td>
										<td align="center" valign="middle" ><%=o.getConsigneeaddress()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人邮编:</td>
										<td align="center" valign="middle" ><%=o.getConsigneepostcode()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人电话:</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										   <%=o.getConsigneephone()%>
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >订单备注:</td>
										<td align="center" valign="middle" ><%=o.getCwbremark()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >客户要求:</td>
										<td align="center" valign="middle" ><%=o.getCustomercommand()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >运输方式:</td>
										<td align="center" valign="middle" ><%=o.getTransway()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >代收货款应收金额:</td>
										<td align="center" valign="middle" ><%=o.getReceivablefee()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >上门退货应退金额:</td>
										<td align="center" valign="middle" ><%=o.getPaybackfee()%></td>
									</tr>
									
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人手机:</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										   <%=o.getConsigneemobile()%>
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >运单号:</td>
										<td align="center" valign="middle" ><%=o.getTranscwb()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >目的地: </td>
										<td align="center" valign="middle" ><%=o.getDestination()%></td>
									</tr>
								</table></td>
							</tr>
						</table></td>
							</tr>
				</table>                                                                                                       
			<%} %>
			<%}else{ %>
			<div style= "width:160px;height:30px;padding-top: 20px;">
			  <center>查无此单!</center>
			</div>
			<% }%>
			
		</div>
	</div>
</div>
<div id="box_yy"></div>
