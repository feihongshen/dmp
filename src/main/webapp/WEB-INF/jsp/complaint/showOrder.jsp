<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

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
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#remarkButton").click(function(){
		var remark=$("#remark").val().replace(/(^\s*)|(\s*$)/g,"");
		if(remark.length>0){
			$.ajax({
				type: "POST",
				url:$("#remarkForm").attr("action"),
				data:$("#remarkForm").serialize(),
				dataType:"json",
				success : function(data) {
					if(data.errorCode==0){
						alert("保存成功");
						$("#remarkView").val("[客服]"+$("#remarkView").val()+"\n"+remark);
						$("#remark").val("");
					}else{
						alert("保存失败");
					}
				}
			});
			
		}else{
			alert("备注不为空");
		}
		
		
	});
});

function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
}
function editInit(){
	//无处理
}
function editSuccess(data){
	//无处理
}
function delSuccess(data){
	//无处理
}

</script>
</HEAD>

<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:400px">
		<tbody>
			<tr>
			<%if(view !=null ){ %>
				<td valign="top"  width="560">
					<div style="height:400px;  overflow-y:auto; overflow-x:hidden">		
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">
									  <h1>&nbsp;订单详情</h1>
									</td>
								
								</tr>
								<tr>
									<td width="10%" align="left" colspan="2" valign="middle">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_set1" >
											<tbody>
												<tr>
													<td width="50%"><b>订&nbsp;单&nbsp;号：</b><%=view.getCwb()%><input type="hidden" id="cwb"  value="<%=view.getCwb()%>"></td>
													
													<td><b>订单类型：</b><%=view.getOrderType()%></td>
												</tr>
												<tr>
													<td width="50%" ><b>运&nbsp;单&nbsp;号：</b><%=view.getTranscwb()%><input type="hidden" id="cwb"  value="<%=view.getCwb()%>"></td>
													
													<td><b>客户要求：</b><%=view.getCustomercommand()%></td>
												</tr>
												<tr>
													<td width="50%"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b><%=view.getConsigneename()%></td>
													<td><b>收件人地址：</b><%=view.getConsigneeaddress()%></td>
												</tr>
												<tr>
													<td><b>手机：</b><%=view.getConsigneemobile()%></td>
													<td><b>电话：</b><%=view.getConsigneephone()%></td>
												</tr>
												<tr>
													<td><b>订单金额：</b><%=view.getCaramount()%>元</td>
													<td><b>代收款：</b><%=view.getReceivablefee()%>
													元
													</td>
												</tr>
												<tr>
													<td><b>重量：</b><%=view.getCarrealweight()%></td>
													<td><b>原支付方式：</b><%=view.getPaytypeNameOld()%>&nbsp;&nbsp;<b>现支付方式：</b><%=view.getPaytypeName() %></td>
												</tr>
												<tr>
													<td><b>应退金额：</b><%=view.getPaybackfee()%></td>
													<td><b>小件员：</b><%=StringUtil.nullConvertToEmptyString(deliveryname.getRealname())%></td>
												</tr>
												 <tr>
													<td><b>供&nbsp;货&nbsp;商：</b><%=customer.getCustomername()%></td>
													<td><b>入库库房：</b><%=invarhousebranch.getBranchname()==null?"":invarhousebranch.getBranchname()%></td>
												</tr>
												<tr>
													<td><b>发货时间：</b><%=view.getEmaildate()%></td>
													<td><b>入库时间：</b><%=orderFlowRuKu.getCredate()==null?"":orderFlowRuKu.getCredate() %></td>
													
												</tr>
																								<tr>												<tr>
													<td><b>上一站点：</b><%=startbranch==null?"":StringUtil.nullConvertToEmptyString(startbranch.getBranchname())%></td>
													<td><b>当前站点：</b><%=currentbranch==null?"":StringUtil.nullConvertToEmptyString(currentbranch.getBranchname())%></td>
												</tr>
												<tr>
													<td><b>目的站点：</b><%=nextbranch.getBranchname()==null?"":StringUtil.nullConvertToEmptyString(nextbranch.getBranchname())%></td>
													<td><b>配送站点：</b><%=deliverybranch.getBranchname()==null?"":StringUtil.nullConvertToEmptyString(deliverybranch.getBranchname())%></td>
													
												</tr>

												<tr>
												
													<td><b>发货件数：</b><%=view.getSendcarnum()%></td>
													<td><b>货物类型：</b><%=view.getCartype()%></td>
													
												</tr>

												<tr>
													<td><b>小件员领货时间：</b><%=orderFlowLingHuo.getCredate()==null?"":orderFlowLingHuo.getCredate() %></td>
													<td><b>拒收时间：</b><%=rejectiontime.getCreatetime()%></td >
												</tr>
													<td><b>签收时间：</b><input name="flagtime" type="text" id="flagtime" value="<%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_time()%>" /></td>
													<td><b>签收人：</b><input name="flagname" type="text" id="flagname" value="<%=rejectiontime.getCreatetime().length()>0 && deliveryChengGong.getCreatetime().length()==0?"":view.getSign_man()%>" /></td>
												</tr>	
												<tr>
													<td><b>派送类型：</b><%=view.getCwbdelivertypeStr()%></td>
													<td><b>滞留原因：</b><%=view.getLeavedreason()%></td>
												</tr>
												<tr>
													<td><b>拒收原因：</b><%=view.getBackreason()%></td>
													<td><b>备注1：</b><%=cwborder.getRemark1()==null?"":cwborder.getRemark1()%></td>
												</tr>
												<tr>
													<td><b>备注2：</b><%=cwborder.getRemark2()==null?"":cwborder.getRemark2() %></td>
													<td><b>备注3：</b><%=cwborder.getRemark3()==null?"":cwborder.getRemark3() %></td>
												</tr>
												<tr>
													<td><b>备注4：</b><%=cwborder.getRemark4()==null?"":cwborder.getRemark4() %></td>
													<td><b>备注5：</b><%=cwborder.getRemark5()==null?"":cwborder.getRemark5()%></td>
												</tr> 
												
												
												<tr>
													<td colspan="2" valign="top" style="height:20px"><b>历史备注：</b>
													<textarea name="remarkView" rows="2" id="remarkView" readonly style="width:85%;height:60px"><%=view.getCwbremark()%></textarea></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					<%if(BranchEnum.KeFu.getValue()==(Integer)request.getAttribute("userInBranchType")) {%>
						<form method="post" id="remarkForm" action="../saveCwbRemark/<%=view.getCwb()%>">
						<input type="hidden" name="cwb" value="40000056">
						<table width="100%" cellspacing="0" cellpadding="10" border="0">
							<tbody><tr>
								<td>
									<table width="100%" cellspacing="1" cellpadding="2" border="0" class="table_2">
										<tbody>
											<tr class="font_1">
												<td width="10%" valign="middle" height="26" bgcolor="#00AEF0" align="left">
												 <span style="float:right"><input type="button" id="remarkButton" class='input_button2' value="保存日志" /></span>
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
					</div>
					
				</td>
				<td valign="top">
					<div style="height:400px;  overflow-y:auto; overflow-x:hidden">
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
									<h1>订单过程</h1></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle">
									订单号：<strong><%=view.getCwb() %></strong>&nbsp;&nbsp;当前状态：<strong><%if(view.getFlowordertypeMethod()=="已审核"){%>审核为：<%=view.getDeliveryStateText() %><%}else if(view.getFlowordertypeMethod()=="已反馈") {%>反馈为：<%=view.getDeliveryStateText() %><%}else{ %><%=view.getFlowordertypeMethod() %><%} %></strong>&nbsp;&nbsp;配送状态:<strong><%=view.getCwbdelivertypeStr() %></strong>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px">
											<tr>
												<td width="150" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
											<!-- <tr>
												<td align="center">2012-11-05 15:25:44</td>
												<td align="center">王飞</td>
												<td>货物由[P1商州站]的小件员[鱼芳妮]开始配送,联系电话:[15829570614]</td>
											</tr> -->
											   <% for(OrderFlowView flow:view.getOrderFlowList()){%>
												<tr>
													<td align="center"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
													<td align="center"><%=flow.getOperator().getRealname() %></td>
												    <td><%=flow.getDetail()%>
												    </td>
                                                 </tr>	
											<%}%> 
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
				<%} else{
					if(request.getParameter("cwb")!=null&&request.getParameter("cwb").length()>0){
				%>
					<script>
					alert("订单<%=request.getParameter("cwb") %>不存在！");
					$("#orderSearch").val(<%=request.getParameter("cwb") %>);
					</script>
				<%} %>
				<td valign="top"  width="560">
					<div style="height:495px;  overflow-y:auto; overflow-x:hidden">		
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">
									  <h1>&nbsp;订单详情</h1>
									</td>
								
								</tr>
								<tr>
									<td width="10%" align="left" colspan="2" valign="middle">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_set1" >
											<tbody>
												<tr>
													<td width="50%"><b>订&nbsp;单&nbsp;号：</b><input type="hidden" id="cwb"  value=""></td>
													
													<td><b>订单类型：</b></td>
												</tr>
												<tr>
													<td width="50%"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b></td>
													<td><b>收件人地址：</b></td>
												</tr>
												<tr>
													<td><b>手机：</b></td>
													<td><b>电话：</b></td>
												</tr>
												<tr>
													<td><b>订单金额：</b>元</td>
													<td><b>代收款：</b>
													元
													</td>
												</tr>
												<tr>
													<td><b>重量：</b></td>
													<td><b>原支付方式：</b><b>现支付方式：</b></td>
												</tr>
												<tr>
													<td><b>应退金额：</b></td>
													<td><b>小件员：</b></td>
												</tr>
												 <tr>
													<td><b>供&nbsp;货&nbsp;商：</b></td>
													<td><b>入库库房：</b></td>
												</tr>
												<tr>
													<td><b>发货时间：</b></td>
													<td><b>入库时间：</b></td>
													
												</tr>
												<tr>
													<td><b>配送站点：</b></td>
													<td><b>目的站点：</b></td>
												</tr>
												<tr>
												
													<td><b>到站时间：</b></td>
													<td><b>小件员领货时间：</b></td>
												</tr>
												<tr>
													<td><b>归班审核时间：</b></td>
													<td><b>配送成功时间：</b></td>
												</tr>
												<!-- <tr>
													<td><b> 修改时间：</b></td>
													<td><b>标记时间：</b></td>
												</tr>  -->
												<tr>
													<td><b>签收时间：</b></td>
													<td><b>签收人：</b></td>
												</tr>	
												<tr>
													<td><b>发货件数：</b></td>
													<td><b>货物类型：</b></td>
												</tr>
												<tr>
													<td><b>派送类型：</b></td>
													<td><b>滞留原因：</b></td>
													
												</tr>
												<tr>
													<td><b>拒收原因：</b></td>
												    <td><b>备注1：</b></td>
												</tr>
												<tr>
													<td><b>备注2：</b></td>
												    <td><b>备注3：</b></td>
												</tr> 
												<tr>
													<td><b>备注4：</b></td>
												    <td><b>备注5：</b></td>
												</tr>
												<tr>
													<td colspan="2" valign="top" style="height:20px"><b>历史备注：</b>
													<textarea name="textfield3" rows="2" id="textfield3" readonly style="width:85%;height:60px"></textarea></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
				<td valign="top">
					<div style="height:467px;  overflow-y:auto; overflow-x:hidden">
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
									<h1>订单过程</h1></td>
								</tr>
							</tbody>
						</table>
						<table width="100%" border="0" cellspacing="10" cellpadding="0">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle">
									订单号：<strong></strong>&nbsp;&nbsp;当前状态：<strong></strong>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px">
											<tr>
												<td width="136" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
				<%} %>
				
			</tr>
		</tbody>
	</table>

<!--投诉处理-->
<div class="jg_10"></div>
<form method="post">
	<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
			<tbody>
				<tr class="font_1">
				<td height="26" align="left" valign="middle" bgcolor="#00AEF0">&nbsp;
					<h1>投诉处理</h1></td>
			</tr>
			<tr>
				<td align="center" valign="middle" style="font-size:12px"><p>
					<input type="button"  value="催件投诉" class="button" onclick="getSetExportViewBox(<%=ComplaintTypeEnum.CuijianTousu.getValue()%>)">
					<input type="button"  value="服务投诉" class="button" onclick="getSetExportViewBox(<%=ComplaintTypeEnum.FuwuTousu.getValue()%>)">
					<input type="button"  value="客服备注" class="button" onclick="getSetExportViewBox(<%=ComplaintTypeEnum.KefuBeizhu.getValue()%>)">
					<input type="button" value="返回受理页面" class="button" onclick="back();">
				</p>
				<p>&nbsp; </p></td>
			</tr>
		</tbody></table>
		</form>
	<form action="<%=request.getContextPath()%>/order/complaintlist/1" method="post" id="back">
	</form>
	
	<script type="text/javascript">
	function back(){
		$("#back").submit();
	}
	</script>
	
	<input type="hidden" id="view" value="<%=request.getContextPath()%>/complaint/showbyType/<%=cwborder==null?"--":cwborder.getCwb()%>/" />
</body>
</html>