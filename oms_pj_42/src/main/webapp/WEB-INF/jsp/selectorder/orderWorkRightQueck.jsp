<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
List<CwbOrder> oList = (List<CwbOrder>)request.getAttribute("cwborder");
List<OrderFlow> orderFlowlist = (List<OrderFlow>)request.getAttribute("flowOrder");
String orderFlowStr = request.getAttribute("flowOrderStr").toString();
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<User> userList = (List<User>)request.getAttribute("userList");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(function() {
	$("#flagtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
});



$(document).ready(function() {
   
$("#bqianshou").click(function(){

	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/sign/"+$("#cwb").val(),
		data:{flagtime:$("#flagtime").val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				$("#flagtime").val(data.date);
			}else{
				alert(data.error);
			}
		}
	});
});
$("#bqianshouren").click(function(){

	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/signname/"+$("#cwb").val(),
		data:{flagname:$("#flagname").val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				$("#flagname").val(data.date);
				$("#signinmanID").html($("#flagname").val());
			}else{
				alert(data.error);
			}
		}
	});
});

$("#payChange").click(function(){
	if($("#cwbremackID").val() ==""){
		alert("请填写备注！");
		return false;
	}
	$("#payChangeForm").submit();
});

$("#editreceivablefee").click(function(){
	$("#editreceivablefee").hide();
	$("#receivablefee").show();
	$("#savereceivablefee").show();
});
$("#savereceivablefee").click(function(){
	
	if(!isFloat($("#receivablefee").val())){
		alert("代收款金额格式不正确！");
		return false;
	}if (confirm("确定要修改订单金额为："+$("#receivablefee").val()+"元?")	){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/saveReceivablefee/"+$("#cwb").val(),
		data:{receivablefee:$("#receivablefee").val()},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				$("#receivablefee_view").html("("+$("#receivablefee").val()+")");
				$("#editreceivablefee").show();
				$("#receivablefee").hide();
				$("#savereceivablefee").hide();
			}else{
				alert(data.error);
			}
			
		}
	});
	}
	
});


$("#orderSearch").keydown(function(event){
	if(event.keyCode==13) {
		if($(this).val()==null){
			return false;
		}
		window.location.href="<%=request.getContextPath()%>/order/selectOrder/1?cwb="+this.value;
	}
});

});
function dgetViewBox(key){
	var durl = $("#view").val();
	window.parent.dgetViewBox(key,durl);
}
function gotoForm(cwb){
	window.location.href="<%=request.getContextPath()%>/order/selectOrder/1?cwb="+cwb;
}
function updateMack(cwb){
	var type = "0";
	if($("#quxiaoMackButton").val()=="标记"){
		type ="1";
	}
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/order/updateMack",
		data:{mackType:type,cwb:cwb},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
				if($("#quxiaoMackButton").val()=="标记"){
					$("#quxiaoMackButton").val("取消标记");
				}else{
					$("#quxiaoMackButton").val("标记");
				}
			}else{
				alert(data.error);
			}
		}
	});
	$("#WORK_AREA_LEFT",parent.document)[0].contentWindow.goMyForm();
}
</script>
</HEAD>

<body onload="$('#orderSearch').focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:495px">
		<tbody>
			<tr>
			<%if(oList!=null && oList.size()>0){ %>
				<td valign="top"  width="500">
		            <% for(CwbOrder o:oList){%>
					<div style="height:480px;  overflow-y:auto; overflow-x:hidden">		
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">
									  <h1>&nbsp;订单详情</h1>
									</td>
								
								</tr>
								<tr>
									<td width="10%" align="left" colspan="2" valign="middle">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_set1" style="height:460px">
											<tbody>
												<tr>
													<td width="50%"><b>订&nbsp;单&nbsp;号：</b><%=o.getCwb()%><input type="hidden" id="cwb"  value="<%=o.getCwb()%>"></td>
													
													<td><b>订单类型：</b><%=o.getOrderType()%></td>
												</tr>
												<tr>
													<td width="50%"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b><%=o.getConsigneename()%></td>
													<td><b>收件人地址：</b><%=o.getConsigneeaddress()%></td>
												</tr>
												<tr>
													<td><b>手机：</b><%=o.getConsigneemobile()%></td>
													<td><b>电话：</b><%=o.getConsigneephone()%></td>
												</tr>
												<tr>
													<td><b>订单金额：</b><%=o.getCaramount()%>元</td>
													<td><b>代收款：</b><%=o.getReceivablefee()%>
													元
													</td>
												</tr>
												<tr>
													<td><b>重量：</b><%=o.getCarrealweight()%></td>
													<td><b>原支付方式：</b><%=o.getPaytypeNameOld()%>&nbsp;&nbsp;<b>现支付方式：</b><%=o.getPaytypeName() %></td>
												</tr>
												<tr>
													<td><b>应退金额：</b><%=o.getPaybackfee()%></td>
													<td><b>小件员：</b><%=o.getDelivername()%></td>
												</tr>
												<tr>
													<td><b>供&nbsp;货&nbsp;商：</b><%=o.getCustomername()%></td>
													<td><b>入库库房：</b><%=o.getCarwarehousename()%></td>
												</tr>
												<tr>
													<td><b>发货时间：</b><%=o.getEmaildate()%></td>
													<td><b>入库时间：</b><%=o.getInstoreroomtime()%></td>
													
												</tr>
												<tr>
													<td><b>配送站点：</b><%=o.getExcelbranch()%></td>
													<td><b>滞留拒收原因：</b><%=o.getLeavedreasonStr()+o.getBackreason()%></td>
												</tr>
												<tr>
													<td><b>到站时间：</b><%=o.getInSitetime()%></td>
													<td><b>小件员领货时间：</b><%=o.getPickGoodstime()%></td>
												</tr>
												<tr>
													<td><b>归班审核时间：</b><%=o.getGoclasstime()%></td>
													<td><b>配送成功时间：</b><%=o.getSendSuccesstime()%></td>
												</tr>
												<tr>
													<td><b> 修改时间：</b><%=o.getEdittime()==null || o.getEdittime().length()==0?"":o.getEdittime()%><%if(!o.getEditman().equals("")){ %>(<%=o.getEditman()==null?"":o.getEditman()%>)<%} %></td>
													<td><b>标记时间：</b><%=o.getMarksflagtime()%><%if(o.getMarksflagmen()!=null && !o.getMarksflagmen().equals("")){ %>(<%=o.getMarksflagmen()%>)<%} %></td>
												</tr>
												<tr>
													<td><b>签收时间：</b><input name="flagtime" type="text" id="flagtime" value="<%=o.getEditsignintime()==null||o.getEditsignintime().length()==0?"当前时间":o.getEditsignintime()%>" /></td>
													<td><b>签收人：</b><input name="flagname" type="text" id="flagname" value="<%=o.getSigninman()==null?"":o.getSigninman()%>" /></td>
												</tr>	
												<tr>
													<td><b>发货件数：</b><%=o.getSendcarnum()%></td>
													<td><b>货物类型：</b><%=o.getCartype()%></td>
												</tr>
												<tr>
													<td><b>派送类型：</b><%=o.getCwbdelivertypeStr()%></td>
													<td><b>备注1：</b><%=o.getRemark1()%></td>
												</tr>
												<tr>
													<td><b>备注2：</b><%=o.getRemark2()%></td>
													<td><b>备注3：</b><%=o.getRemark3()%></td>
												</tr>
												<tr>
													<td><b>备注4：</b><%=o.getRemark4()%></td>
													<td><b>备注5：</b><%=o.getRemark5()%></td>
												</tr>
												<tr>
													<td colspan="2" valign="top" style="height:20px"><b>历史备注：</b>
													<textarea name="textfield3" rows="2" id="textfield3" readonly style="width:85%;height:60px"><%=o.getCwbremark()%></textarea></td>
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
									订单号：<strong><%=o.getCwb() %></strong>&nbsp;&nbsp;当前状态：<strong><%=o.getFlowordertypeMethod() %></strong>
										<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="font-size:14px">
											<tr>
												<td width="136" align="center" bgcolor="#f1f1f1">操作时间</td>
												<td width="60" align="center" bgcolor="#f1f1f1">操作员</td>
												<td align="center" bgcolor="#f1f1f1">操作详情</td>
											</tr>
											<%if(oList!=null && oList.size()>0){ %>
												<% for(CwbOrder co:oList){%>
													<%=orderFlowStr %>
											<%break;}} %>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
				<%break;} %>
				<%} else{
					if(request.getParameter("cwb")!=null&&request.getParameter("cwb").length()>0){
				%>
					<script>
					alert("订单<%=request.getParameter("cwb") %>不存在！");
					$("#orderSearch").val(<%=request.getParameter("cwb") %>);
					</script>
				<%} %>
				<td valign="top"  width="500">
					<div style="height:480px;  overflow-y:auto; overflow-x:hidden">		
						<table width="100%" border="0" cellspacing="1" cellpadding="2">
							<tbody>
								<tr class="font_1">
									<td width="10%" height="26" align="left" valign="middle" bgcolor="#00AEF0">
									  <h1>&nbsp;订单详情</h1>
									</td>
								
								</tr>
								<tr>
									<td width="10%" align="left" colspan="2" valign="middle">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_set1" style="height:460px">
											<tbody>
												<tr>
													<td width="50%"><b>订&nbsp;单&nbsp;号：</b><input type="hidden" id="cwb"></td>
													
													<td><b>订单类型：</b></td>
												</tr>
												<tr>
													<td width="50%"><b>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</b></td>
													<td><b>发货时间：</b></td>
												</tr>
												<tr>
													<td><b>手&nbsp;机&nbsp;号：</b></td>
													<td><b>收件人地址：</b></td>
												</tr>
												<tr>
													<td><b>订单金额：</b>元</td>
													<td><b>代收款：</b>
													元
													</td>
												</tr>
												<tr>
													<td><b>重量：</b></td>
													<td><b>支付方式：</b></td>
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
													<td><b>滞留拒收原因：</b></td>
												</tr>
												<tr>
													<td><b>到站时间：</b></td>
													<td><b>小件员领货时间：</b></td>
												</tr>
												<tr>
													<td><b>归班审核时间：</b></td>
													<td><b>配送成功时间：</b></td>
												</tr>
												<tr>
													<td><b> 修改时间：</b></td>
													<td><b>标记时间：</b></td>
												</tr>
												<tr>
													<td><b>签收时间：</b></td>
													<td><b>签收人：</b></td>
												</tr>
												<tr>
													<td><b>电话号：</b></td>
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
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="/oms/order/makelog/">	

</body>
</html>