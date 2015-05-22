<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.AccountCwbSummary"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum,cn.explink.enumutil.AccountFlowOrderTypeEnum"%>
<%
	AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
	accountCwbSummary=(AccountCwbSummary)request.getAttribute("accountCwbSummary");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>买单结算汇总</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
var zfhjFee=0;
var yjzfFee=<%=accountCwbSummary.getHjfee()==null?0:accountCwbSummary.getHjfee()%>;
var sxhjFee=0;
var state="<%=accountCwbSummary.getCheckoutstate()%>";

$(function(){
	hjFee();
	if(state==1){//审核通过禁用输入框
		$("#submitTable input[type=text]").attr("readonly","readonly");
	}
	
	if(state==1){
		$("#zfShow").hide();
		$("#checkShow").show();	
	}else{
		$("#zfShow").show();
		$("#checkShow").hide();
	}
});


//光标离开金额框时统计合计
function hjFee(){
	var feetransfer=$("#feetransfer").val()==""?0:$("#feetransfer").val();
	var feecash=$("#feecash").val()==""?0:$("#feecash").val();
	var feepos=$("#feepos").val()==""?0:$("#feepos").val();
	var feecheck=$("#feecheck").val()==""?0:$("#feecheck").val();
	
	
	zfhjFee=((parseFloat(feetransfer).toFixed(2)*100+parseFloat(feecash).toFixed(2)*100
			+parseFloat(feepos).toFixed(2)*100+parseFloat(feecheck).toFixed(2)*100)/100).toFixed(2);
	
	sxhjFee=((parseFloat(yjzfFee).toFixed(2)*100-parseFloat(zfhjFee).toFixed(2)*100)/100).toFixed(2);
	checkButton(sxhjFee);
	$("#sxhjShow").html(sxhjFee);
}

//交款按钮验证
function checkButton(sxhjFee){
	$("#checkF").removeAttr("disabled");
	$("#updateF").removeAttr("disabled");
	if(sxhjFee==0){
		$("#updateF").attr("disabled","disabled");
	}
	else{
		$("#checkF").attr("disabled","disabled");
	}
}


//交款
function updateFClick(){
	if(!checkForm()){
		return false;
	}
	if((parseFloat(sxhjFee).toFixed(2)*100)<(parseFloat("<%=accountCwbSummary.getHjfee()%>").toFixed(2)*100)){
		$("#checkF").attr("disabled","disabled");
		$("#updateF").attr("disabled","disabled");
		$("#updateF").val("请稍候");
		submit(0);
	}else{
		alert("您未交款,请检查交款金额！");
	}
}
//审核
function checkFClick(){
	if(!checkForm()){
		return false;
	}
	if((zfhjFee*100)<(yjzfFee*100)){
		alert("您有未交款,请检查交款金额！");
		return false;
	}
	
	if(confirm("确定交款并审核吗？")){
		$("#updateF").attr("disabled","disabled");
		$("#checkF").attr("disabled","disabled");
		$("#checkF").val("请稍候");
		submit(1);
	}
	
}

function submit(chk){
   	$.ajax({
   		type: "POST",
   		url:'<%=request.getContextPath()%>/accountcwbdetail/saveOutwarehouse/<%=accountCwbSummary.getSummaryid()%>?checkoutstate='+chk,
   		data:$('#submitForm').serialize(),
   		dataType : "json",
   		success : function(data) {
   			if(data.errorCode==0){
   				location.href="<%=request.getContextPath()%>/accountcwbdetail/listOutwarehouse/1?checkoutstate="+chk;
   			}else{
   				alert(data.error);
   			}
   		}
   	});
}


function checkForm(){
	if($("#feetransfer").val()!=""){
		if(!isFloat($("#feetransfer").val())){
			alert("转账金额应为数字");
			return false;
		}
	}
	if($("#feecash").val()!=""){
		if(!isFloat($("#feecash").val())){
			alert("现金金额应为数字");
			return false;
		}
	}
	if($("#feepos").val()!=""){
		if(!isFloat($("#feepos").val())){
			alert("POS金额应为数字");
			return false;
		}
	}
	if($("#feecheck").val()!=""){
		if(!isFloat($("#feecheck").val())){
			alert("支票金额应为数字");
			return false;
		}
	}
	
	if((zfhjFee*100)>(yjzfFee*100)){
		alert("超额支付！");
		return false;
	}
	
	if($("#memo").val().length>200){
		alert("备注不能超过200个字！");
		return false;
	}
	return true;
}

//撤销
function saveBackClick(){
	if(confirm("您确定要撤销本次交款吗？")){
		$("#saveBack").attr("disabled","disabled");
    	$("#saveBack").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/accountcwbdetail/outwarehouseDelete',
    		data:{
    			summaryid:"<%=accountCwbSummary.getSummaryid()%>",
    			branchid:"<%=accountCwbSummary.getBranchid()%>"
    		},
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/accountcwbdetail/outwarehouseInfo/<%=accountCwbSummary.getBranchid()%>";
    			}else{
    				alert(data.error);
    				return;
    			}
    		}
    	});
    	
    	
    	
    	
    	
	}
}

function backFClick(){
	window.location.href="<%=request.getContextPath()%>/accountcwbdetail/listOutwarehouse/1?checkoutstate=1";
}


function detailCwbs(flowOrderType,nums){
	if(nums!=0){
		window.open("<%=request.getContextPath()%>/accountcwbdetail/outwarehousecwbs/1?summaryid=<%=accountCwbSummary.getSummaryid()%>&flowOrderType="+flowOrderType);
	}
}

//加减款详情
function detailJiaJian(feetype,nums){
	if(nums!=0){
		$("#feetype").val(feetype);
		$("#detailJiaJianForm").submit();
	}
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<form id="submitForm" action="" method="post">
	<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr>
			<td width="15%" bgcolor="#F4F4F4">&nbsp;</td>
			<td width="15%" bgcolor="#F4F4F4">款项</td>
			<td bgcolor="#F4F4F4">订单数</td>
			<td bgcolor="#F4F4F4">金额[元]</td>
		</tr>	
	    <tr>
	    	<td bgcolor="#F4F4F4" rowspan="5" valign="middle">冲减款</td>
			<td bgcolor="#F4F4F4">中转退款</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()%>','<%=accountCwbSummary.getZznums()%>')"><%=accountCwbSummary.getZznums()%></a></td>
			<td><strong><%=accountCwbSummary.getZzcash()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">退货退款</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue()%>','<%=accountCwbSummary.getThnums()%>')"><%=accountCwbSummary.getThnums()%></a></td>
			<td><strong><%=accountCwbSummary.getThcash()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">POS退款</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountFlowOrderTypeEnum.Pos.getValue()%>','<%=accountCwbSummary.getPosnums()%>')"><%=accountCwbSummary.getPosnums()%></a></td>
			<td><strong><%=accountCwbSummary.getPoscash()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">加款</td>
			<td><a href="#" onclick="detailJiaJian(1,'<%=accountCwbSummary.getOtheraddnums()%>')"><%=accountCwbSummary.getOtheraddnums()%></a></td>
			<td><strong><%=accountCwbSummary.getOtheraddfee()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">减款</td>
			<td><a href="#" onclick="detailJiaJian(2,'<%=accountCwbSummary.getOthersubnums()%>')"><%=accountCwbSummary.getOthersubnums()%></a></td>
			<td><strong><%=accountCwbSummary.getOthersubtractfee()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">欠款</td>
			<td bgcolor="#F4F4F4">欠&nbsp;&nbsp;款：</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.QianKuan.getValue()%>','<%=accountCwbSummary.getQknums()%>')"><%=accountCwbSummary.getQknums()%></a></td>
			<td><strong><%=accountCwbSummary.getQkcash()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">货款</td>
			<td bgcolor="#F4F4F4">本次实交：</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.YingJiao.getValue()%>','<%=accountCwbSummary.getYjnums()%>')"><%=accountCwbSummary.getYjnums()%></a></td>
			<td><strong><%=accountCwbSummary.getYjcash()%></strong></td>
		</tr>
	</table>
	<div class="right_title">
	<div id="zfShow"><h1>您需支付金额：<font style="font-family:'微软雅黑', '黑体'; font-size:25px"><%=accountCwbSummary.getHjfee()%></font>
	元，&nbsp;&nbsp;还有<font id="sxhjShow" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>元未支付。</lable></h1></div>
	<div id="checkShow"><h1>实际支付金额：<font style="font-family:'微软雅黑', '黑体'; font-size:25px"><%=accountCwbSummary.getHjfee()%></font>元</h1></div>
	<table id="submitTable"  width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr>
			<td width="20%" bgcolor="#F4F4F4">付款方式</td>
			<td bgcolor="#F4F4F4">金额</td>
			<td bgcolor="#F4F4F4">付款人</td>
			<td bgcolor="#F4F4F4">卡号</td>
		</tr>
		<tr>
			<td>转账</td>
			<td><input onblur="hjFee()" name="feetransfer" type="text" id="feetransfer" size="10" maxlength="10" value="${accountCwbSummary.feetransfer==null?"":accountCwbSummary.feetransfer}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
			<td><input name="usertransfer" type="text" id="usertransfer" size="10" maxlength="20" value="${accountCwbSummary.usertransfer==null?"":accountCwbSummary.usertransfer}"/></td>
			<td><input name="cardtransfer" type="text" id="cardtransfer" size="25" maxlength="20" value="${accountCwbSummary.cardtransfer==null?"":accountCwbSummary.cardtransfer}"/></td>
		</tr>
		<tr>
			<td>现金</td>
			<td><input onblur="hjFee()" name="feecash" type="text" id="feecash" size="10" maxlength="10" value="${accountCwbSummary.feecash==null?"":accountCwbSummary.feecash}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
			<td><input name="usercash" type="text" id="usercash" size="10" maxlength="20" value="${accountCwbSummary.usercash==null?"":accountCwbSummary.usercash}"/></td>
			<td></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><input onblur="hjFee()" name="feepos" type="text" id="feepos" size="10" maxlength="10" value="${accountCwbSummary.feepos==null?"":accountCwbSummary.feepos}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
			<td><input name="userpos" type="text" id="userpos" size="10" maxlength="20" value="${accountCwbSummary.userpos==null?"":accountCwbSummary.userpos}"/></td>
			<td></td>
		</tr>
		<tr>
			<td>支票</td>
			<td><input onblur="hjFee()" name="feecheck" type="text" id="feecheck" size="10" maxlength="10" value="${accountCwbSummary.feecheck==null?"":accountCwbSummary.feecheck}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
			<td><input name="usercheck" type="text" id="usercheck" size="10" maxlength="20" value="${accountCwbSummary.usercheck==null?"":accountCwbSummary.usercheck}"/></td>
			<td></td>
		</tr>
		<tr>
			<td valign="middle" bgcolor="#F4F4F4">备&nbsp;&nbsp;注：</td>
			<td colspan="4" align="left" bgcolor="#F4F4F4">
			<textarea name="memo" cols="100" rows="2" id="memo">${accountCwbSummary.memo==null?"":accountCwbSummary.memo}</textarea></td>
		</tr>
		<tr>
			<td colspan="4" bgcolor="#F4F4F4"><p>&nbsp;
				</p>
				<p>
					<input type="hidden" name="summaryid" id="summaryid" value="${accountCwbSummary.summaryid}"/>
					<input type="hidden" id="branchid" name="branchid" value="${accountCwbSummary.branchid}" />
					<%
						if(accountCwbSummary.getCheckoutstate()==1){
					%>
						<input type="button" class="input_button1" id="backF" onclick="backFClick()" value="返回" />
					<%}else{ %>
						<input type="button" class="input_button1" id="updateF" onclick="updateFClick()" value="交 款" />
						&nbsp;&nbsp;
						<input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="交款审核" />
						
						<input type="button" class="input_button1" id="saveBack" onclick="saveBackClick()" value="撤销" />
					<%} %>
				</p>
				<p>&nbsp;</p></td>
			</tr>
	</table>
	</div>
</form>
<form action="<%=request.getContextPath() %>/accountfeedetail/detailSummaryidList" target="_blank" method="post" id="detailJiaJianForm">
	<input type="hidden" id="summaryid" name="summaryid" value="${accountCwbSummary.summaryid}"/>
	<input type="hidden" id="feetype" name="feetype" value=""/>
</form>
</body>
</html>
