<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.AccountCwbSummary"%>
<%@page import="cn.explink.enumutil.AccountTongjiEnum"%>
<%
	Map usermap = (Map) session.getAttribute("usermap");
	long userbranchid=Long.parseLong(usermap.get("branchid").toString());

	AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
	accountCwbSummary=(AccountCwbSummary)request.getAttribute("accountCwbSummary");
	
	long yj=AccountTongjiEnum.YingJiaoNums.getValue();
	long wj=AccountTongjiEnum.QianKuan.getValue();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配送结果结算汇总</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
var zfhjFee=0;
var yjzfFee=<%=accountCwbSummary.getHjfee()==null?0:accountCwbSummary.getHjfee()%>;
var sxhjFee=0;
var state="<%=accountCwbSummary.getCheckoutstate()%>";


/* //光标离开金额框时统计合计
function hjFee(){
	var feetransfer=$("#feetransfer").val()==""?0:$("#feetransfer").val();
	var feecash=$("#feecash").val()==""?0:$("#feecash").val();
	var feepos=$("#feepos").val()==""?0:$("#feepos").val();
	var feecheck=$("#feecheck").val()==""?0:$("#feecheck").val();
	
	
	zfhjFee=((parseFloat(feetransfer).toFixed(2)*100+parseFloat(feecash).toFixed(2)*100
			+parseFloat(feepos).toFixed(2)*100+parseFloat(feecheck).toFixed(2)*100)/100).toFixed(2);
	
	sxhjFee=((parseFloat(yjzfFee).toFixed(2)*100-parseFloat(zfhjFee).toFixed(2)*100)/100).toFixed(2);
	$("#sxhjShow").html(sxhjFee);
} */


function checkFClick(){
	if(!checkForm()){
		return false;
	}
	
/* 	if((zfhjFee*100)<(yjzfFee*100)){
		alert("您有未交款！");
		return false;
	} */
	
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
   		url:'<%=request.getContextPath()%>/accountcwbdetail/savedelivery/<%=accountCwbSummary.getSummaryid()%>?checkoutstate='+chk,
   		data:$('#submitForm').serialize(),
   		dataType : "json",
   		success : function(data) {
   			if(data.errorCode==0){
   				alert(data.error);
   				location.href="<%=request.getContextPath()%>/accountcwbdetail/listDelivery/1?checkoutstate="+chk;
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

function backFClick(){
	window.location.href="<%=request.getContextPath()%>/accountcwbdetail/listDelivery/1?checkoutstate=<%=accountCwbSummary.getCheckoutstate()%>";
}


function detailCwbs(flowOrderType,nums){
	if(nums!=0){
		window.open("<%=request.getContextPath()%>/accountcwbdetail/deliverycwbs/1?summaryid=<%=accountCwbSummary.getSummaryid()%>&flowOrderType="+flowOrderType);
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
	    	<td bgcolor="#F4F4F4" rowspan="2" valign="middle">冲减款</td>
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
			<td bgcolor="#F4F4F4">欠&nbsp;&nbsp;款</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.QianKuan.getValue()%>','<%=accountCwbSummary.getQknums()%>')"><%=accountCwbSummary.getQknums()%></a></td>
			<td><strong><%=accountCwbSummary.getQkcash()%></strong></td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F4">货款</td>
			<td bgcolor="#F4F4F4">本次实交</td>
			<td><a href="#" onclick="detailCwbs('<%=AccountTongjiEnum.YingJiao.getValue()%>','<%=accountCwbSummary.getYjnums()%>')"><%=accountCwbSummary.getYjnums()%></a></td>
			<td><strong><%=accountCwbSummary.getYjfee()%></strong></td>
		</tr>
	</table>
	<div class="right_title">
	<h1>实际支付金额：<font style="font-family:'微软雅黑', '黑体'; font-size:25px"><%=accountCwbSummary.getHjfee()%></font></h1>
	<table id="submitTable"  width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr>
			<td width="20%" bgcolor="#F4F4F4">付款方式</td>
			<td bgcolor="#F4F4F4">金额[元]</td>
			<td bgcolor="#F4F4F4">付款人</td>
			<td bgcolor="#F4F4F4">卡号</td>
		</tr>
		<tr>
			<td>转账</td>
			<td><input name="feetransfer" type="text" id="feetransfer" size="10" maxlength="10" value="<%=accountCwbSummary.getFeetransfer()%>" readonly="readonly"/></td>
			<td><input name="usertransfer" type="text" id="usertransfer" size="10" maxlength="20" value="<%=accountCwbSummary.getUsertransfer()%>" readonly="readonly"/></td>
			<td><input name="cardtransfer" type="text" id="cardtransfer" size="25" maxlength="20" value="<%=accountCwbSummary.getCardtransfer()%>" readonly="readonly"/></td>
		</tr>
		<tr>
			<td>现金</td>
			<td><input name="feecash" type="text" id="feecash" size="10" maxlength="10" value="<%=accountCwbSummary.getFeecash()%>" readonly="readonly"/></td>
			<td><input name="usercash" type="text" id="usercash" size="10" maxlength="20" value="<%=accountCwbSummary.getUsercash()%>" readonly="readonly"/></td>
			<td></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><input name="feepos" type="text" id="feepos" size="10" maxlength="10" value="<%=accountCwbSummary.getFeepos()%>" readonly="readonly"/></td>
			<td><input name="userpos" type="text" id="userpos" size="10" maxlength="20" value="<%=accountCwbSummary.getUserpos()%>" readonly="readonly"/></td>
			<td></td>
		</tr>
		<tr>
			<td>支票</td>
			<td><input name="feecheck" type="text" id="feecheck" size="10" maxlength="10" value="<%=accountCwbSummary.getFeecheck()%>" readonly="readonly"/></td>
			<td><input name="usercheck" type="text" id="usercheck" size="10" maxlength="20" value="<%=accountCwbSummary.getUsercheck()%>" readonly="readonly"/></td>
			<td></td>
		</tr>
		<tr>
			<td valign="middle" bgcolor="#F4F4F4">备&nbsp;&nbsp;注：</td>
			<td colspan="3" align="left" bgcolor="#F4F4F4">
			<textarea name="memo" cols="100" rows="4" id="memo"><%=accountCwbSummary.getMemo()==null?"":accountCwbSummary.getMemo()%></textarea></td>
		</tr>
		<tr>
			<td colspan="4" bgcolor="#F4F4F4"><p>&nbsp;
				</p>
				<p>
					<input type="hidden" id="summaryid" name="summaryid" value="${accountCwbSummary.summaryid}" />
					<input type="hidden" id="branchid" name="branchid" value="${accountCwbSummary.branchid}" />
					<input type="button" class="input_button1" id="backF" onclick="backFClick()" value="返回" />
					<%
						if(accountCwbSummary.getCheckoutstate()==0){
							if(userbranchid!=accountCwbSummary.getBranchid()){
					%>
					&nbsp;&nbsp;<input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="交款审核" />
					<%}}%>
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
