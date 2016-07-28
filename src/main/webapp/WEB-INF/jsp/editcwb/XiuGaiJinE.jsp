<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeEnum"%>
<%@page import="cn.explink.service.CwbOrderWithDeliveryState"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String [] cwbArray = (String[])request.getAttribute("cwbArray");
List<CwbOrderWithDeliveryState> allowCods = (List<CwbOrderWithDeliveryState>)request.getAttribute("allowCods");
List<CwbOrderWithDeliveryState> prohibitedCods = (List<CwbOrderWithDeliveryState>)request.getAttribute("prohibitedCods");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>修改订单金额</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<style type="text/css">
.buttonnew{
	width:180px;
	line-height:24px;
	height:24px;
	background-color:B6E0F8; 
	border:none; 
	cursor:pointer; 
	padding:0; 
	text-align:center 
}


</style>
<script type="text/javascript">
function synchronousValue(cwb){
	if($("#Receivablefee_cash_"+cwb).attr("type")=="text"){
		$("#Receivablefee_cash_"+cwb).val($("#Receivablefee_"+cwb).val());
	}
	if($("#Receivablefee_pos_"+cwb).attr("type")=="text"){
		$("#Receivablefee_pos_"+cwb).val($("#Receivablefee_"+cwb).val());
	}
	if($("#Receivablefee_checkfee_"+cwb).attr("type")=="text"){
		$("#Receivablefee_checkfee_"+cwb).val($("#Receivablefee_"+cwb).val());
	}
	if($("#Receivablefee_otherfee_"+cwb).attr("type")=="text"){
		$("#Receivablefee_otherfee_"+cwb).val($("#Receivablefee_"+cwb).val());
	}
}
function sub(){
	var isSubmit = true;
	var cwbs = "";
	$.each($("input[name='cwbs']"),function(i,cwb){
		if(i != 0){
			cwbs += ","
		}
		// 订单类型--是否上门退
		var isShangmentui = "上门退" == $("#cwbOrderType_"+cwb.value).html().trim();
		cwbs += cwb.value;
		if(!isShangmentui && $("input[name='Receivablefee_"+cwb.value+"']").val() == Number($("input[name='Receivablefee_"+cwb.value+"']").parent().prev().html())){
			alert("订单号" + cwb.value + "的代收金额与原内容没有变化，不能申请！")
			isSubmit=false;
            return false;
		} else if (isShangmentui && $("input[name='Paybackfee_"+cwb.value+"']").val() == Number($("input[name='Paybackfee_"+cwb.value+"']").parent().prev().html())){
			alert("订单号" + cwb.value + "的代退金额与原内容没有变化，不能申请！")
            isSubmit=false;
            return false;
		} else if(!isFloat($("input[name='Receivablefee_"+cwb.value+"']").val())){
			alert("订单号"+cwb.value+"的修改为代收金额内容不是数字！");
			isSubmit=false;
			return false;
		}else if(!isFloat($("input[name='Paybackfee_"+cwb.value+"']").val())){
			alert("订单号"+cwb.value+"的修改为代退金额内容不是数字！");
			isSubmit=false;
			return false;
		}else if(parseFloat($("input[name='Receivablefee_"+cwb.value+"']").val())>0
					&&parseFloat($("input[name='Paybackfee_"+cwb.value+"']").val())>0){
					alert("订单号"+cwb.value+"代收金额与代退金额不能都大于0！");
					isSubmit=false;
					return false;
		}else if($("#Receivablefee_cash_"+cwb.value).length>0){
			if(!isFloat($("#Receivablefee_cash_"+cwb.value).val())){
				alert("订单号"+cwb.value+"的反馈代收现金内容不是数字！");
				isSubmit=false;
				return false;
			}else if(!isFloat($("#Receivablefee_pos_"+cwb.value).val())){
				alert("订单号"+cwb.value+"的反馈代收POS容不是数字！");
				isSubmit=false;
				return false;
			}else if(!isFloat($("#Receivablefee_checkfee_"+cwb.value).val())){
				alert("订单号"+cwb.value+"的反馈代收支票内容不是数字！");
				isSubmit=false;
				return false;
			}else if(!isFloat($("#Receivablefee_otherfee_"+cwb.value).val())){
				alert("订单号"+cwb.value+"的反馈代收其他内容不是数字！");
				isSubmit=false;
				return false;
			}
			var rf = parseFloat($("#Receivablefee_cash_"+cwb.value).val())
			+parseFloat($("#Receivablefee_pos_"+cwb.value).val())
			+parseFloat($("#Receivablefee_checkfee_"+cwb.value).val())
			+parseFloat($("#Receivablefee_otherfee_"+cwb.value).val());
			if(parseFloat($("#Receivablefee_"+cwb.value).val())!=rf){
				alert("订单号"+cwb.value+"代收金额与实收金额总和不一致！");
				isSubmit=false;
				return false;
			}
		}		
	});
	if(isSubmit && !checkCwbs(cwbs)){
		isSubmit=false;
        return false;
	}
	if(isSubmit){
		$("#searchForm").submit();
	}
}

/**
 * 添加验证，如果存在未审核的修改申请，则不允许申请。
 * @author jian.xie
 * @date 2016-07-14
 */
function checkCwbs(cwbs){
	var result = true;
	$.ajax({ 
        'url':'<%=request.getContextPath() %>/editcwb/checkIsExist',
        'data':{'cwbs':cwbs}, 
        'type':'POST', 
        'async': false,
        'success':function(data){ 
        	if(data){
        		alert("提交失败，订单【" + data + "】存在未确认的支付信息修改申请");
        		result = false;
        	}
        }        
	});
	return result;
}
</script>
</HEAD>
<BODY style="background:#f5f5f5">
<form id="searchForm" action ="<%=request.getContextPath()%>/editcwb/editXiuGaiJinE" method = "post">
<input type="hidden" name="requestUser" value="<%=request.getParameter("requestUser") %>" />

<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr>
		<td colspan="6">要求修改订单类型订单总数：<%=cwbArray.length %>单　有效订单：<%=allowCods.size() %>单　不符合条件订单：<%=prohibitedCods.size() %>单</td>
 	</tr>
 	
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>不能修改订单类型的订单</td>
		<td>订单类型</td>
		<td>当前环节</td>
 		<td>原因</td>
 	</tr>
 	<%for(CwbOrderWithDeliveryState cods :prohibitedCods){ 
 	%>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=cods.getCwbOrder().getCwb() %></strong></td>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=CwbOrderTypeIdEnum.getByValue(cods.getCwbOrder().getCwbordertypeid()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=FlowOrderTypeEnum.getText(cods.getCwbOrder().getFlowordertype()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getError() %></td>
 	</tr>
 	<%} %>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>没反馈最终结果的订单</td>
		<td>订单类型</td>
 		<td>代收金额</td>
 		<td>修改为代收金额</td>
 		<td>代退金额</td>
 		<td>修改为代退金额</td>
 	</tr>
 	<%for(CwbOrderWithDeliveryState cods :allowCods){ 
 		if(cods.getDeliveryState()==null||cods.getDeliveryState().getDeliverystate()==DeliveryStateEnum.WeiFanKui.getValue()){
 	%>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF" ><strong  ><%=cods.getCwbOrder().getCwb() %></strong>
		<input type="hidden" name="cwbs" value="<%=cods.getCwbOrder().getCwb() %>" />
		<input type="hidden" name="isDeliveryState_<%=cods.getCwbOrder().getCwb() %>" value="no" />
		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		     <span id="cwbOrderType_<%=cods.getCwbOrder().getCwb()%>">
                 <%=CwbOrderTypeIdEnum.getByValue(cods.getCwbOrder().getCwbordertypeid()).getText() %>
             </span>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getCwbOrder().getReceivablefee() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if(cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
 			//上门换与配送的订单才显示代收金额的修改框
 		%>
 			<input type="text" name="Receivablefee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getReceivablefee() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			<input type="hidden" name="Receivablefee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getCwbOrder().getPaybackfee() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if(cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()
			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
 			//上门退的订单才显示代退金额的修改框
 		%>
 			<input type="text" name="Paybackfee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getPaybackfee() %>" /></td>
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			<input type="hidden" name="Paybackfee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 	</tr>
 	<%}} %>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>有反馈最终结果的订单</td>
		<td>订单类型</td>
 		<td>代收金额</td>
 		<td>修改为代收金额</td>
 		<td>反馈代收现金</td>
 		<td>反馈代收POS</td>
 		<td>反馈代收支票</td>
 		<td>反馈代收其他</td>
 		<td>代退金额</td>
 		<td>修改为代退金额</td>
 	</tr>
 	<%for(CwbOrderWithDeliveryState cods :allowCods){ 
 		if(cods.getDeliveryState()!=null&&cods.getDeliveryState().getDeliverystate()!=DeliveryStateEnum.WeiFanKui.getValue()){
 	%>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong  ><%=cods.getCwbOrder().getCwb() %></strong>
		<input type="hidden" name="cwbs" value="<%=cods.getCwbOrder().getCwb() %>" />
		<input type="hidden" name="isDeliveryState_<%=cods.getCwbOrder().getCwb() %>" value="yes" />
		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		     <span id="cwbOrderType_<%=cods.getCwbOrder().getCwb()%>">
 		             <%=CwbOrderTypeIdEnum.getByValue(cods.getCwbOrder().getCwbordertypeid()).getText() %>
 		     </span>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getCwbOrder().getReceivablefee() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if(cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
 			//上门换与配送的订单才显示代收金额的修改框
 		%>
 			<input type="text" id="Receivablefee_<%=cods.getCwbOrder().getCwb() %>"  onkeyup="synchronousValue('<%=cods.getCwbOrder().getCwb() %>')" name="Receivablefee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getReceivablefee() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			<input type="hidden" id="Receivablefee_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if((cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue())
 			&&(cods.getDeliveryState().getCash().compareTo(BigDecimal.ZERO)>0
 			||cods.getCwbOrder().getReceivablefee().compareTo(BigDecimal.ZERO)==0)){
 			//上门换与配送的订单才显示代收金额的修改框  现金中有钱显示现金  或者订单本身是零款的货也只显示现金的输入框
 		%>
 			<input type="text" id="Receivablefee_cash_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_cash_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getDeliveryState().getCash() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			0.00<input type="hidden" id="Receivablefee_cash_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_cash_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if((cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 	 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue())
 	 			&&cods.getDeliveryState().getPos().compareTo(BigDecimal.ZERO)>0){
 			//上门换与配送的订单才显示代收金额的修改框  Pos中有钱显示POS
 		%>
 			<input type="text"  id="Receivablefee_pos_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_pos_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getDeliveryState().getPos() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			0.00<input type="hidden" id="Receivablefee_pos_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_pos_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if((cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 	 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue())
 	 			&&cods.getDeliveryState().getCheckfee().compareTo(BigDecimal.ZERO)>0){
 			//上门换与配送的订单才显示代收金额的修改框  支票中有钱显示支票
 		%>
 			<input type="text" id="Receivablefee_checkfee_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_checkfee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getDeliveryState().getCheckfee() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			0.00<input type="hidden" id="Receivablefee_checkfee_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_checkfee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if((cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()
 	 			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue())
 	 			&&cods.getDeliveryState().getOtherfee().compareTo(BigDecimal.ZERO)>0){
 			//上门换与配送的订单才显示代收金额的修改框  其他中有钱显示其他
 		%>
 			<input type="text" id="Receivablefee_otherfee_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_otherfee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getDeliveryState().getOtherfee() %>" />
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			0.00<input type="hidden" id="Receivablefee_otherfee_<%=cods.getCwbOrder().getCwb() %>" name="Receivablefee_otherfee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getCwbOrder().getPaybackfee() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if(cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()
			||cods.getCwbOrder().getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()){
 			//上门退的订单才显示代退金额的修改框
 		%>
 			<input type="text" name="Paybackfee_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getPaybackfee() %>" /></td>
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			<input type="hidden" name="Paybackfee_<%=cods.getCwbOrder().getCwb() %>" value="0" />
 		<%} %>
 	</tr>
 	<%}} %>
	<tr>
		<td colspan="10" align="center">
 			<input type="button" class="buttonnew" onclick="sub()"  value="修改订单金额申请" />　<input type="button" class="buttonnew"  onclick="location.href='<%=request.getContextPath()%>/editcwb/start'"  value="返回" />
		</td>
 			 
 	</tr>
</table>
</form>
</BODY>
</HTML>
