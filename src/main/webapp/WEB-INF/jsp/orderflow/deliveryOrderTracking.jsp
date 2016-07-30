<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.support.transcwb.TranscwbView"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.VO.express.EmbracedOrderVO"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");
Branch nextbranch=(Branch)request.getAttribute("nextbranch");
Branch deliverybranch=(Branch)request.getAttribute("deliverybranch");
DeliveryState deliveryChengGong =(DeliveryState)request.getAttribute("deliveryChengGong");
DeliveryState rejectiontime =(DeliveryState)request.getAttribute("rejectiontime");
CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");

EmbracedOrderVO embracedOrderVO = (EmbracedOrderVO)request.getAttribute("embracedOrder");
Customer senderBranch = (Customer)request.getAttribute("senderCustomer");
Customer consineerBranch = (Customer)request.getAttribute("consineerCustomer");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">

</script>
</HEAD>

<body marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  style="font-size:14px">
		<tbody>
		<tr>
		<td valign="top">		
			<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px">
			<tbody>
				<tr class="font_1">
					<td height="26" align="left" valign="middle" bgcolor="#00AEF0"><h1>&nbsp;订单详情</h1></td>
				</tr>
				<tr>
					<td>
						<div style=" overflow-y:auto; overflow-x:hidden">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="right_set1"  style="font-size:14px"><!-- table_5 --> 
								<tbody>
									<tr>
										<td bgcolor="#EBFFD7"><b>运单号：</b><%=embracedOrderVO.getOrderNo()%></td>
										<td bgcolor="#EBFFD7"><b>运单类型：</b>快递</td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>寄件人姓名：</b><%=embracedOrderVO.getSender_name()%></td>
										<td bgcolor="#EBFFD7"><b>寄件人地址：</b><%=embracedOrderVO.getSender_adress()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>寄件人手机：</b><%=embracedOrderVO.getSender_cellphone() == null ? "" : embracedOrderVO.getSender_cellphone()%></td>
										<td bgcolor="#EBFFD7"><b>寄件人电话：</b><%=embracedOrderVO.getSender_telephone() == null ? "" : embracedOrderVO.getSender_telephone()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>寄件人单位：</b>[<%=senderBranch.getCustomercode() == null ? "" : senderBranch.getCustomercode()%>]<%=senderBranch.getCompanyname() == null ? "" : senderBranch.getCompanyname()%></td>
										<td bgcolor="#EBFFD7"><b></b></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>收件人姓名：</b><%=embracedOrderVO.getConsignee_name()%></td>
										<td bgcolor="#EBFFD7"><b>收件人地址：</b><%=embracedOrderVO.getConsignee_adress()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>收件人手机：</b><%=embracedOrderVO.getConsignee_cellphone() == null ? "" : embracedOrderVO.getConsignee_cellphone()%></td>
										<td bgcolor="#EBFFD7"><b>收件人电话：</b><%=embracedOrderVO.getConsignee_telephone() == null ? "" : embracedOrderVO.getConsignee_telephone()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>收件人单位：</b>[<%=consineerBranch.getCustomercode() == null ? "" : consineerBranch.getCustomercode() %>]<%=consineerBranch.getCompanyname() == null ? "" : consineerBranch.getCompanyname()%></td>
										<td bgcolor="#EBFFD7"><b></b></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>下一站：</b><%=nextbranch.getBranchname()==null?"":nextbranch.getBranchname()%></td>
										<td bgcolor="#EBFFD7"><b>配送站点：</b><%=deliverybranch.getBranchname()==null?"":deliverybranch.getBranchname()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>托物内容：</b><%=embracedOrderVO.getGoods_name()%></td>
										<td bgcolor="#EBFFD7"><b>货物尺寸：</b><%=embracedOrderVO.getCarsize() == null ? "":embracedOrderVO.getCarsize()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>数量：</b><%=embracedOrderVO.getGoods_number()==null ? "":embracedOrderVO.getGoods_number()%></td>
										<td bgcolor="#EBFFD7"><b>实际重量：</b><%=embracedOrderVO.getActual_weight() == null ? "":embracedOrderVO.getActual_weight()%>&nbsp;&nbsp;<b>计费重量：</b><%=embracedOrderVO.getCharge_weight() == null ? "":embracedOrderVO.getCharge_weight()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>付款方式：</b>
										<%if("0".equals(embracedOrderVO.getPayment_method())) {%>月结<%}else if("1".equals(embracedOrderVO.getPayment_method())){ %>现付<%}else if("2".equals(embracedOrderVO.getPayment_method())){ %>到付<%}%>
										
										
										</td>
										<td bgcolor="#EBFFD7"><b></b></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>应收合计：</b><%=embracedOrderVO.getFreight_total()==null?"":embracedOrderVO.getFreight_total()%>&nbsp;&nbsp;<b>运费：</b><%=embracedOrderVO.getFreight()==null?"":embracedOrderVO.getFreight()%>&nbsp;&nbsp;<b>包装费：</b><%=embracedOrderVO.getPacking_amount()==null?"":embracedOrderVO.getPacking_amount()%>&nbsp;&nbsp;<b>保价费：</b><%=embracedOrderVO.getInsured_cost()==null?"":embracedOrderVO.getInsured_cost()%></td>
										<td bgcolor="#EBFFD7"><b>实收运费：</b><%=cwborder.getInfactfare()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>代收款：</b><%=view.getReceivablefee()%>元</td>
										<td bgcolor="#EBFFD7"><b>原支付方式：</b><%=view.getPaytypeNameOld()%>&nbsp;&nbsp;<b>现支付方式：</b><%=view.getPaytypeName() %></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>签收人：</b><%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_man()%></td>
										<!-- <td bgcolor="#EBFFD7"><b>签收时间：年月日时分秒</b></td> -->
										<td bgcolor="#EBFFD7"><b>签收时间：</b><%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_time()%></td>
									</tr>
									<tr>
											<td bgcolor="#EBFFD7"><b>滞留原因：</b><%=view.getLeavedreason()%></td>
										<td bgcolor="#EBFFD7"><b>滞留时间：</b><%=cwborder.getFankuitime() == null ? "" : cwborder.getFankuitime()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>拒收原因：</b><%=view.getBackreason()%></td>
										<!-- <td bgcolor="#EBFFD7"><b>拒收时间：年月日时分秒</b></td> -->
										<td bgcolor="#EBFFD7"><b>拒收时间：</b><%=rejectiontime.getCreatetime()%></td >
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>备注：</b><%=embracedOrderVO.getRemarks() == null ? "":embracedOrderVO.getRemarks()%></td></td>
										<td bgcolor="#EBFFD7"><b></b></td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
		</tbody>
	</table>
	</td>
	</tr>
	</tbody>
	</table>
		
</body>
</html>