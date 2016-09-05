<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.support.transcwb.TranscwbView"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.GoodsSizeTypeEnum"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
QuickSelectView view = (QuickSelectView)request.getAttribute("view");
Customer customer = (Customer)request.getAttribute("customer");
User deliveryname = (User)request.getAttribute("deliveryname");
Branch invarhousebranch=(Branch)request.getAttribute("invarhousebranch");
Branch deliverybranch=(Branch)request.getAttribute("deliverybranch");
Branch nextbranch=(Branch)request.getAttribute("nextbranch");
Branch startbranch=(Branch)request.getAttribute("startbranch");
Branch currentbranch=(Branch)request.getAttribute("currentbranch");
OrderFlow orderFlowRuKu = (OrderFlow)request.getAttribute("orderFlowRuKu");
OrderFlow orderFlowDaoHuo = (OrderFlow)request.getAttribute("orderFlowDaoHuo");
OrderFlow orderFlowLingHuo = (OrderFlow)request.getAttribute("orderFlowLingHuo");
GotoClassAuditing gotoClassAuditingGuiBan = (GotoClassAuditing)request.getAttribute("gotoClassAuditingGuiBan");
DeliveryState deliveryChengGong =(DeliveryState)request.getAttribute("deliveryChengGong");
DeliveryState rejectiontime =(DeliveryState)request.getAttribute("rejectiontime");
CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");

List<AbnormalWriteBackView> backViewList = (List<AbnormalWriteBackView>)request.getAttribute("abnormalWriteBackViewList");
List<ComplaintsView> comViewList = (List<ComplaintsView>)request.getAttribute("comViewList");
List<TranscwbView> transcwbList = request.getAttribute("transcwbList")==null?null:(List<TranscwbView>)request.getAttribute("transcwbList");
String isGathercomp = (String)request.getAttribute("isGathercomp");

%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#remarkButton").click(function(){
		$.ajax({
			type: "POST",
			url:$("#remarkForm").attr("action"),
			data:$("#remarkForm").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("保存成功");
					$("#remarkView").val('');
					$("#remarkView").val(data.remark);
					$("#remark").val("");
				}else{
					alert(data.remark);
				}
			}
		});		
	});
})

function goForm(cwb){
	$("#queckSelectOrder_AREA_RIGHT",parent.document)[0].contentWindow.gotoForm(cwb);
}
</script>
</HEAD>

<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  style="font-size:14px">
		<tbody><tr>
		<td valign="top">
		<%if(transcwbList!=null&&transcwbList.size()>0){ %>
		<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px">
			<tbody>
				<tr class="font_1">
					<td height="26" align="left" valign="middle" bgcolor="#00AEF0"><h1>&nbsp;运单状态信息</h1></td>
				</tr>
				<tr>
					<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px" >
						<tbody>
							<tr>
								<td align="center" bgcolor="#F1F1F1">订单号</td>
								<td align="center" bgcolor="#F1F1F1">运单号</td>
								<c:choose>
								<c:when test="${jdtype == 1}">
								<td align="center" bgcolor="#F1F1F1"><font>一票多件当前状态</font></td>
								<td align="center" bgcolor="#F1F1F1"><font>运单状态<font></td>
								</c:when>
								<c:otherwise>
								<td align="center" bgcolor="#F1F1F1">运单号当前状态</td>
								</c:otherwise>
								</c:choose>
							</tr>
							<%for(TranscwbView ts : transcwbList){ %>
							<tr onclick="goForm('<%=ts.getTranscwb() %>');">
								<td align="center" bgcolor="#EBFFD7"><%=ts.getCwb() %></td>
								<td align="center" bgcolor="#EBFFD7"><%=ts.getTranscwb() %></td>
								<td align="center" bgcolor="#EBFFD7"><%=ts.getFlowordername() %></td>
								<c:if test="${jdtype == 1}">
								<td align="center" bgcolor="#EBFFD7"><font><%=ts.getTranscwbstate() %></font></td>
								</c:if>
							</tr>
							<%} %>
						</tbody>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		<%} %>
		<%if(view !=null ){ %>
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
										<td width="50%" bgcolor="#EBFFD7"><b>订&nbsp;单&nbsp;号：</b><%=view.getCwb()%><input type="hidden" id="cwb"  value="<%=view.getCwb()%>"></td>
										
										<td bgcolor="#EBFFD7"><b>订单类型：</b><%=view.getOrderType()%></td>
									</tr>
									<tr>
										<td width="50%"  bgcolor="#EBFFD7"><b>运&nbsp;单&nbsp;号：</b><%=view.getTranscwb()%><input type="hidden" id="cwb"  value="<%=view.getCwb()%>"></td>
										
										<td bgcolor="#EBFFD7"><b>客户要求：</b><%=view.getCustomercommand()%></td>
									</tr>
									<tr>
										<td width="50%" bgcolor="#EBFFD7"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b><%=view.getConsigneename()%></td>
										<td bgcolor="#EBFFD7"><b>收件人地址：</b><%=view.getConsigneeaddress()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>手机：</b><%=view.getConsigneemobile()%></td>
										<td bgcolor="#EBFFD7"><b>原收件人地址：</b><span style="color:red"><%=view.getOldconsigneeaddress()%></span></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>派送城市：</b><%=view.getCity()==null?"":view.getCity()%></td>
										<td bgcolor="#EBFFD7"><b>电话：</b><%=view.getConsigneephone()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>订单金额：</b><%=view.getCaramount()%>元</td>
										<td bgcolor="#EBFFD7"><b>派送区域：</b><%=view.getArea()==null?"":view.getArea()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>重量：</b><%=view.getCarrealweight()%></td>
										<td bgcolor="#EBFFD7"><b>代收款：</b><%=view.getReceivablefee()%>
										元
										</td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>货物尺寸：</b><%=view.getCarsize()%></td>
										<td bgcolor="#EBFFD7"><b>原支付方式：</b><%=view.getPaytypeNameOld()%>&nbsp;&nbsp;<b>现支付方式：</b><%=view.getPaytypeName() %></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>应退金额：</b><%=view.getPaybackfee()%></td>
										<td bgcolor="#EBFFD7"><b>供货商发货仓库：</b><%=view.getCustomerwarehouseid()%></td>
									</tr>
										<tr>
										<% if(cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()){ %>
											<td bgcolor="#EBFFD7"><b>应收运费：</b><%=cwborder.getTotalfee()%></td>
										<%}else{ %>
											<td bgcolor="#EBFFD7"><b>应收运费：</b><%=cwborder.getShouldfare()%></td>
										<%} %>
										<td bgcolor="#EBFFD7"><b>小件员：</b><%=StringUtil.nullConvertToEmptyString(deliveryname.getRealname()==null?view.getExceldeliver():deliveryname.getRealname())%></td>
									</tr>
									<%-- <tr>
										<td bgcolor="#EBFFD7"><b>应收运费：</b><%=cwborder.getShouldfare()%></td>
										<td bgcolor="#EBFFD7"><b>小件员：</b><%=StringUtil.nullConvertToEmptyString(deliveryname.getRealname()==null?view.getExceldeliver():deliveryname.getRealname())%></td>
									</tr> --%>
									 <tr>
										<td bgcolor="#EBFFD7"><b>供&nbsp;货&nbsp;商：</b><%=StringUtil.nullConvertToEmptyString(customer.getCustomername()) %><b>&nbsp;&nbsp;供货商订单号：</b><%=cwborder.getCommoncwb()%></td>
										<td bgcolor="#EBFFD7"><b>实收运费：</b><%=cwborder.getInfactfare()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>发货时间：</b><%=view.getEmaildate()%></td>
										<td bgcolor="#EBFFD7"><b>入库库房：</b><%=invarhousebranch.getBranchname()==null?"":invarhousebranch.getBranchname()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>上一站点：</b><%=startbranch.getBranchname()==null?"":startbranch.getBranchname()%></td>
										<td bgcolor="#EBFFD7"><b>入库时间：</b><%=orderFlowRuKu.getCredate()==null?"":orderFlowRuKu.getCredate() %></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>下一站点：</b><%=nextbranch.getBranchname()==null?"":nextbranch.getBranchname()%></td>
										<td bgcolor="#EBFFD7"><b>当前站点：</b><%=currentbranch.getBranchname()==null?"":currentbranch.getBranchname()%><b style="color: red;"><%if(view.getFlowordertype()==8){%>(到错货站点)<% }%></b></td>
									</tr>

									<tr>
										<td bgcolor="#EBFFD7"><b>发货件数：</b><%=view.getSendcarnum()%><%if("1".equals(isGathercomp)) {%>  （发货完成）<%}else if("0".equals(isGathercomp)){ %><span style="color: red">（发货未完成）</span><%}%></td>
										<td bgcolor="#EBFFD7"><b>配送站点：</b><%=deliverybranch.getBranchname()==null?"":deliverybranch.getBranchname()%></td>
									</tr>

									<tr>
										<td bgcolor="#EBFFD7"><b>小件员领货时间：</b><%=orderFlowLingHuo.getCredate()==null?"":orderFlowLingHuo.getCredate() %></td>
										<td bgcolor="#EBFFD7"><b>货物类型：</b><%=view.getCartype()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7">&nbsp;</td>
										<td bgcolor="#EBFFD7"><b>货物尺寸类型：</b><%=GoodsSizeTypeEnum.getTextByValue(view.getGoodsSizeType()) %></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>签收时间：</b><input name="flagtime" type="text" id="flagtime" value="<%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_time()%>" /></td>
										<td bgcolor="#EBFFD7"><b>签收人：</b><input name="flagname" type="text" id="flagname" value="<%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_man()%>" /></td>
									</tr>	
									<tr>
										<td bgcolor="#EBFFD7"><b>派送类型：</b><%=view.getCwbdelivertypeStr()%></td>
										<td bgcolor="#EBFFD7"><b>拒收时间：</b><%=rejectiontime.getCreatetime()%></td >
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>拒收原因：</b><%=view.getBackreason()%></td>
										<td bgcolor="#EBFFD7"><b>滞留原因：</b><%=view.getLeavedreason()%></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>备注1：</b><%=cwborder.getRemark1()==null?"":cwborder.getRemark1()%></td>
										<td bgcolor="#EBFFD7"><b>备注2：</b><%=cwborder.getRemark2()==null?"":cwborder.getRemark2() %></td>
									</tr>
									<tr>
										<td bgcolor="#EBFFD7"><b>备注3：</b><%=cwborder.getRemark3()==null?"":cwborder.getRemark3() %></td>
										<td bgcolor="#EBFFD7"><b>备注4：</b><%=cwborder.getRemark4()==null?"":cwborder.getRemark4() %></td>
									</tr> 
									
									<tr>
									<td bgcolor="#EBFFD7"><b>备注5：</b><%=cwborder.getRemark5()==null?"":cwborder.getRemark5()%></td>
									<td width="50%"  valign="top" style="height:20px" bgcolor="#EBFFD7"><b>历史备注：</b>
										<textarea name="remarkView" rows="2" id="remarkView" readonly style="width:85%;height:60px"><%=view.getCwbremark()%></textarea></td>
									</tr>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
					<%if(BranchEnum.KeFu.getValue()==(Integer)request.getAttribute("userInBranchType")) {%>
						<form method="post" id="remarkForm" action="../saveCwbRemark/<%=view.getCwb()%>">
						<input type="hidden" name="cwb" value="40000056">
						<table width="100%" cellspacing="0" cellpadding="10" border="0"  style="font-size:14px">
							<tbody><tr>
								<td>
									<table width="100%" cellspacing="1" cellpadding="2" border="0" class="table_2" style="font-size:14px">
										<tbody>
											<tr class="font_1">
												<td width="10%" valign="middle" height="26" bgcolor="#00AEF0" align="left">
												 <span style="float:right"><input type="button" id="remarkButton" class='input_button2' value="保存备注" /></span>
												 <h1>&nbsp;订单备注</h1>
												</td>
											</tr>
											<tr>
												<td width="10%" valign="middle" align="left" style="height:40px" class="right_set1"><span style="height:20px">
												<textarea style="width:85%;height:60px" id="remark" rows="2" name="remark"></textarea>
												</span>
												</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
						</tbody></table>
					</form>
					<%} %>
				</tbody>
				</table>
				<%} else{
					if(request.getParameter("cwb")!=null&&request.getParameter("cwb").length()>0){
				%>
					<script>
					alert("订单<%=request.getParameter("cwb") %>不存在！");
					$("#orderSearch").val(<%=request.getParameter("cwb") %>);
					</script>
				<%} %>
					<!-- <div><span style="color: red;font-size: medium;">订单不存在！</span></div> -->
					<table width="100%" border="0" cellspacing="1" cellpadding="2" style="font-size:14px">
						<tbody>
							<tr class="font_1">
								<td>
									<table cellpadding="0px" cellspacing="0px">
										<tr>
											<td width="10%" height="26" align="left" valign="middle"
												bgcolor="#00AEF0">
												<h1>&nbsp;订单详情</h1>
											</td>
											<td width="10%" height="26" align="left" valign="middle"
												bgcolor="#00AEF0">
												<h1
													style="color: red; font-size: medium; margin-right: 10px;">订单不存在
												</h1>
											</td>
										</tr>
									</table>
							</tr>
							<tr>
								<td>
								<div style="overflow-y:auto; overflow-x:hidden">
									<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_set1" style="font-size:14px">
										<tbody>
											<tr>
												<td width="50%" bgcolor="#EBFFD7"><b>订&nbsp;单&nbsp;号：</b><input type="hidden" id="cwb"  value=""></td>
												
												<td bgcolor="#EBFFD7"><b>订单类型：</b></td>
											</tr>
											<tr>
												<td width="50%" bgcolor="#EBFFD7"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b></td>
												<td bgcolor="#EBFFD7"><b>收件人地址：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>手机：</b></td>
												<td bgcolor="#EBFFD7"><b>电话：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>订单金额：</b>元</td>
												<td bgcolor="#EBFFD7"><b>代收款：</b>
												元
												</td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>重量：</b></td>
												<td bgcolor="#EBFFD7"><b>原支付方式：</b><b>现支付方式：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>应退金额：</b></td>
												<td bgcolor="#EBFFD7"><b>小件员：</b></td>
											</tr>
											 <tr>
												<td bgcolor="#EBFFD7"><b>供&nbsp;货&nbsp;商：</b></td>
												<td bgcolor="#EBFFD7"><b>入库库房：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>发货时间：</b></td>
												<td bgcolor="#EBFFD7"><b>入库时间：</b></td>
												
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>配送站点：</b></td>
												<td bgcolor="#EBFFD7"><b>下一站点：</b></td>
											</tr>
											<tr>
											
												<td bgcolor="#EBFFD7"><b>到站时间：</b></td>
												<td bgcolor="#EBFFD7"><b>小件员领货时间：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>归班审核时间：</b></td>
												<td bgcolor="#EBFFD7"><b>配送成功时间：</b></td>
											</tr>
											<!-- <tr>
												<td><b> 修改时间：</b></td>
												<td><b>标记时间：</b></td>
											</tr>  -->
											<tr>
												<td bgcolor="#EBFFD7"><b>签收时间：</b></td>
												<td bgcolor="#EBFFD7"><b>签收人：</b></td>
											</tr>	
											<tr>
												<td bgcolor="#EBFFD7"><b>发货件数：</b></td>
												<td bgcolor="#EBFFD7"><b>货物类型：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>派送类型：</b></td>
												<td bgcolor="#EBFFD7"><b>滞留原因：</b></td>
												
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>拒收原因：</b></td>
											    <td bgcolor="#EBFFD7"><b>备注1：</b></td>
											</tr>
											<tr>
												<td bgcolor="#EBFFD7"><b>备注2：</b></td>
											    <td bgcolor="#EBFFD7"><b>备注3：</b></td>
											</tr> 
											<tr>
												<td bgcolor="#EBFFD7"><b>备注4：</b></td>
											    <td bgcolor="#EBFFD7"><b>备注5：</b></td>
											</tr>
											<tr>
												<td colspan="2" valign="top" style="height:20px" bgcolor="#EBFFD7"><b>历史备注：</b>
												<textarea name="textfield3" rows="2" id="textfield3" readonly style="width:85%;height:60px"></textarea></td>
											</tr>
										</tbody>
									</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
					<%} %>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>