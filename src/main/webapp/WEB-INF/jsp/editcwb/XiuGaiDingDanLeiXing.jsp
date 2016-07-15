<%@page import="cn.explink.enumutil.PaytypeEnum"%>
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
<TITLE>修改订单类型</TITLE>
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
    });
    if(!checkCwbs(cwbs)){
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
<form id="searchForm" action ="<%=request.getContextPath()%>/editcwb/editXiuGaiDingDanLeiXing" method = "post">
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
		<td>可以修改订单类型的订单</td>
		<td>订单类型</td>
 		<td>更改为订单类型</td>
 	</tr>
 	<%for(CwbOrderWithDeliveryState cods :allowCods){ %>
 	<tr>
		<td align="center" valign="middle" bgcolor="#EEF6FF"><strong><%=cods.getCwbOrder().getCwb() %></strong>
		<input type="hidden" name="cwbs" value="<%=cods.getCwbOrder().getCwb() %>" />
		<input type="hidden" name="cwbordertypeid_<%=cods.getCwbOrder().getCwb() %>" value="<%=cods.getCwbOrder().getCwbordertypeid() %>"/>
		</td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF"><%=CwbOrderTypeIdEnum.getByValue(cods.getCwbOrder().getCwbordertypeid()).getText() %></td>
 		<td align="center" valign="middle" bgcolor="#EEF6FF">
 		<select name="Newcwbordertypeid_<%=cods.getCwbOrder().getCwb() %>">
 		<%if(cods.getCwbOrder().getCwbordertypeid()!=CwbOrderTypeIdEnum.Peisong.getValue()&&cods.getCwbOrder().getPaybackfee().compareTo(BigDecimal.ZERO)==0){ %>
 			<option value="<%=CwbOrderTypeIdEnum.Peisong.getValue() %>" ><%=CwbOrderTypeIdEnum.Peisong.getText() %></option>
		<%} if(cods.getCwbOrder().getCwbordertypeid()!=CwbOrderTypeIdEnum.Shangmentui.getValue()&&cods.getCwbOrder().getReceivablefee().compareTo(BigDecimal.ZERO)==0){ %>
 			<option value="<%=CwbOrderTypeIdEnum.Shangmentui.getValue() %>" ><%=CwbOrderTypeIdEnum.Shangmentui.getText() %></option>
		<%} if(cods.getCwbOrder().getCwbordertypeid()!=CwbOrderTypeIdEnum.Shangmenhuan.getValue()){ %>
 			<option value="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue() %>" ><%=CwbOrderTypeIdEnum.Shangmenhuan.getText() %></option>
		<%} %>
 		</select>
 		</td>
 	</tr>
 	<%} %>
	<tr>
		<td colspan="10" align="center">
 			<input type="button"  class="buttonnew" onclick="sub();" value="修改订单类型申请" />　<input type="button" class="buttonnew"  onclick="location.href='<%=request.getContextPath()%>/editcwb/start'"  value="返回" />
		</td>
 			 
 	</tr>
</table>
</form>
</BODY>
</HTML>
