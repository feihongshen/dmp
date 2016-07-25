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
function sub(){
	var isSubmit = true;
	var cwbs = "";
	$.each($("input[name='cwbs']"),function(i,cwb){
		if(i != 0){
            cwbs += ","
        }
        cwbs += cwb.value;
        if(!isFloat($("input[name='Shouldfare_"+cwb.value+"']").val())){
			alert("订单号"+cwb.value+"的修改为运费金额内容不是数字！");
			isSubmit=false;
			return false;
		} else if($("input[name='Shouldfare_"+cwb.value+"']").val() == Number($("input[name='Shouldfare_"+cwb.value+"']").parent().prev().html())){
			alert("订单号"+cwb.value+"的运费金额与原内容没有变化，不能申请！");
            isSubmit=false;
            return false;
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
<form id="searchForm" action ="<%=request.getContextPath()%>/editcwb/editXiuGaiKuaiDiJinE" method = "post">
<input type="hidden" name="requestUser" value="<%=request.getParameter("requestUser") %>" />
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr>
		<td colspan="6">要求修改金额总数：<%=cwbArray.length %>单　有效订单：<%=allowCods.size() %>单</td>
 	</tr>
 	
	<tr class="font_1" height="30" style="background-color: rgb(243, 243, 243); ">
		<td>没反馈最终结果的订单</td>
		<td>订单类型</td>
 		<td>运费金额</td>
 		<td>修改为运费金额</td>
 	</tr>
 	<%for(CwbOrderWithDeliveryState cods :allowCods){ 
 		if(cods.getDeliveryState()==null||cods.getDeliveryState().getDeliverystate()==DeliveryStateEnum.WeiFanKui.getValue()){
 	%>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF" ><strong  ><%=cods.getCwbOrder().getCwb() %></strong>
		<input type="hidden" name="cwbs" value="<%=cods.getCwbOrder().getCwb() %>" />
		<input type="hidden" name="isDeliveryState_<%=cods.getCwbOrder().getCwb() %>" value="no" />
		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=CwbOrderTypeIdEnum.getByValue(cods.getCwbOrder().getCwbordertypeid()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=cods.getCwbOrder().getShouldfare() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<%if(cods.getCwbOrder().getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()){
 			//快递的订单才显示代退金额的修改框
 		%>
 			<input type="text" name="Shouldfare_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getShouldfare()%>" /></td>
 		<%}else{//否则使用隐藏input 保证提交格式一致 %>
 			<input type="hidden" name="Shouldfare_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getShouldfare()%>" />
 		<%} %>
 	</tr>
 	<%}} %>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	<tr>
		<td colspan="10" align="center">
 			<input type="button" class="buttonnew" onclick="sub();"  value="修改快递运费金额申请" />　<input type="button" class="buttonnew"  onclick="location.href='<%=request.getContextPath()%>/editcwb/start'"  value="返回" />
		</td>
 			 
 	</tr>
</table>
</form>
</BODY>
</HTML>
