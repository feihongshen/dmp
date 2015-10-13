<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> oList = (List<CwbOrder>)request.getAttribute("cwborder");
List<OrderFlow> orderFlowlist = (List<OrderFlow>)request.getAttribute("flowOrder");
int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
%>
<style>
ul{width:100%;list-style-type:none;}
li{width:115px;float:left;border:1px solid blue;text-align: center;}
</style>
		<div align="center" id="box_form">
		<%if(orderFlowlist!=null && orderFlowlist.size()>0) {%>
		    <h2>订单状态</h2>
		    <table width="40%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr>
				  <td>
					<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				       <tr>
							<td width="450">
							    <table width="100%" border="1" cellspacing="1" cellpadding="0"  class="table_2" id="gd_table2">
								    <ul>
									  <li>订单号</li>
									  <li>状态</li>
									  <li>是否当前状态</li>
									  <li>操作与否</li>
									</ul>
									<%for(OrderFlow orderflow:orderFlowlist){ %>
									<ul>
									  <li><%=orderflow.getCwb() %></li>
									  <li><%=orderflow.getFlowordertypeText()%></li>
									  <li><%=orderflow.getIsnow()==0?"否":"是" %></li>
									  <li><%=orderflow.getIsGo()==0?"否":"是" %></li>
									</ul>
								<%} %>
		                           </table>
		                       </td>
                        </tr>
                     </table>
	                </td>
				</tr>
		      </table>
		      <%} %>
			<%if(oList!=null && oList.size()>0){ %>
			<% for(CwbOrder o:oList){%>
			<h2>订单详情</h2>
			<table width="40%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="450"><table width="100%" border="1" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="40%" align="center" valign="middle" bgcolor="#eef6ff" >订单号:</td>
										<td align="center" valign="middle" ><%=o.getCwb()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >退供货商封包批次号:</td>
										<td align="center" valign="middle" ><%=o.getBacktocustomer_awb()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >订单流程类型 :</td>
										<td align="center" valign="middle" ><%=o.getCwbflowflag()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物重量kg:</td>
										<td align="center" valign="middle" ><%=o.getCarrealweight()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物类别:</td>
										<td align="center" valign="middle" ><%=o.getCartype()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货仓库:</td>
										<td align="center" valign="middle" ><%=o.getCarwarehouse()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >商品尺寸:</td>
										<td align="center" valign="middle" ><%=o.getCarsize()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取回货物金额:</td>
										<td align="center" valign="middle" ><%=o.getBackcaramount()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货数量:</td>
										<td align="center" valign="middle" ><%=o.getSendcarnum()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取货数量:</td>
										<td align="center" valign="middle" ><%=o.getBackcarnum()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >货物金额:</td>
										<td align="center" valign="middle" ><%=o.getCaramount()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >取回商品名称:</td>
										<td align="center" valign="middle" ><%=o.getBackcarname()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发出商品名称:</td>
										<td align="center" valign="middle" ><%=o.getSendcarname()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >派送员id:</td>
										<td align="center" valign="middle" ><%=o.getDeliverid()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点收款是否已上交总部 :</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadflag()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点上交款时间:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadtime()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >站点交款总部审核时:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadchecktime()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >总部交款审核状态:</td>
										<td align="center" valign="middle" ><%=o.getPodfeetoheadcheckflag()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >发货时间:</td>
										<td align="center" valign="middle" ><%=o.getEmaildate()%>&nbsp;</td>
									</tr>
									
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >供货商运单号:</td>
										<td align="center" valign="middle" ><%=o.getShipcwb()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人编号:</td>
										<td align="center" valign="middle" ><%=o.getConsigneeno()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人名称:</td>
										<td align="center" valign="middle" ><%=o.getConsigneename()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人地址:</td>
										<td align="center" valign="middle" ><%=o.getConsigneeaddress()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人邮编:</td>
										<td align="center" valign="middle" ><%=o.getConsigneepostcode()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人电话:</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										   <%=o.getConsigneephone()%>&nbsp;
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >订单备注:</td>
										<td align="center" valign="middle" ><%=o.getCwbremark()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >客户要求:</td>
										<td align="center" valign="middle" ><%=o.getCustomercommand()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >运输方式:</td>
										<td align="center" valign="middle" ><%=o.getTransway()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >代收货款应收金额:</td>
										<td align="center" valign="middle" ><%=o.getReceivablefee()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >上门退货应退金额:</td>
										<td align="center" valign="middle" ><%=o.getPaybackfee()%>&nbsp;</td>
									</tr>
									
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >收件人手机:</td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										  <%=o.getConsigneemobile()%>&nbsp;
										<%}else{%>
											[不可见]
										<%} %>
										</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >运单号:</td>
										<td align="center" valign="middle" ><%=o.getTranscwb()%>&nbsp;</td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >目的地: </td>
										<td align="center" valign="middle" ><%=o.getDestination()%>&nbsp;</td>
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

