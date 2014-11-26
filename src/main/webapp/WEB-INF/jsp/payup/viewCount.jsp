<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.GotoClassAuditing"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	BigDecimal hk_amount = request.getAttribute("hk_amount")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("hk_amount");
	BigDecimal hk_amount_pos =request.getAttribute("hk_amount_pos")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("hk_amount_pos");
	String gcaids = request.getAttribute("gcaids")==null?"":(String)request.getAttribute("gcaids");
	String gcaidsMAC = request.getAttribute("gcaidsMAC")==null?"":(String)request.getAttribute("gcaidsMAC");
	List<Branch> caiwu_branch = request.getAttribute("caiwu_branch")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("caiwu_branch");	
	Branch  ub = request.getAttribute("user_branch")==null?new Branch():(Branch)request.getAttribute("user_branch");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结算</title>
<link href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
$(function(){
	$("#type").change(function(){
		if($(this).val()==0){
			$("#amount").val(0.00);
			$("#amount_pos").val(0.00);
			
		}else if($(this).val()==1){
			$("#amount").val($("#huokuan").html());
			$("#amount_pos").val($("#hk_amount_pos").val());
		}else{
			$("#amount").val($("#fakuan").html());
			$("#amount_pos").val(0.00);
		}
		$("#amount_view").html($("#amount").val()+"[POS:"+$("#amount_pos").val()+"]");
	});
	$("#sub").click(function(){
		if($("#upbranchid").val()==0){
			alert("请选择上交对象！");
			return;
		}else if($("#type").val()==0){
			alert("选择交款类型！");
			return;
		}else if($("#way").val()==0){
			alert("选择交款方式！");
			return;
		/* }else if(!isFloat($("#amount").val())){
			alert("请输入正确的金额！");
			return; */
		}/* else if(parseFloat($("#amount").val())<=0&&parseFloat($("#amount_pos").val())<=0){
			//alert("交款金额不能为0元或者负数！");
			alert("您目前没有需要上交的款！")
			return;
		} */else if($("#type").val()==1&&parseFloat($("#hk_amount").val())<parseFloat($("#amount").val())){
			alert("货款金额不能大于未缴款金额")
			return;
		}else if($("#type").val()==2&&parseFloat($("#fa_amount").val())<parseFloat($("#amount").val())){
			alert("罚款金额不能大于未缴罚款金额")
			return;
		}
		if(confirm("您确定上交结算吗？")){
			$("#subPayUp").submit();
		}
	});
	
});
if(<%=gcaids.length() %><=0){
	alert("您当前没有可交款项");
	location.href="viewPayUp";
}
</script>
</head>

<body style="background: #eef9ff">
<div class="menucontant">
							
<form id="subPayUp" name="subPayUp" method="post" action="subPayUp" >
<input type="hidden" name="gcaids" value="<%=gcaids %>" />
<input type="hidden" name="gcaidsMAC" value="<%=gcaidsMAC %>" />
<input type="hidden" id="hk_amount" name="hk_amount" value="<%=hk_amount.add(ub.getArrearagehuo()).add(ub.getArrearagepei()) %>" />
<input type="hidden" id="hk_amount_pos" name="hk_amount_pos" value="<%=hk_amount_pos %>" />
<input type="hidden" id="fa_amount" name="fa_amount" value="<%=ub.getArrearagefa() %>" />
<div class="form_topbg">
累计未缴款：<strong id="huokuan"><%=hk_amount.add(ub.getArrearagehuo()).add(ub.getArrearagepei()) %></strong>元
（货款<%=hk_amount %>元[pos(含支付宝COD扫码):<%=hk_amount_pos %>元]，欠款<%=ub.getArrearagehuo() %>元，赔款<%=ub.getArrearagepei() %>元）
，罚款：<strong id="fakuan"><%=ub.getArrearagefa() %></strong>元    <a href="viewPayUp" ><font color="#ff9900">查看交款历史</font></a>　　<a href="javascript:getNoPayUpDetailView();" ><font color="#ff9900">查看未交款明细</font></a></div>
<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
	<tr id="customertr" class=VwCtr style="display:">
		<td >上交对象：
			<select name="upbranchid" id="upbranchid">
				<%for(Branch b :caiwu_branch){ %>
				<option value="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
				<%} %>
		</select> *</td>
		<td>交款类型：
			<label for="select"></label>
			<select  name="type" id="type">
				<option value="0">请选择</option>
				<option value="1">货款</option>
				<!-- <option value="2">罚款</option> -->
		</select></td>
		<td>交款方式：
			<select name="way" id="way">
				<option value="0">请选择</option>
				<option value="1">银行转账</option>
				<option value="2">现金</option>
		</select> *</td>
		<td>缴款金额[现金、支票、其它]：<label id="amount_view">0.00[POS:0]</label>
		<input type="hidden" name="amount" id="amount" value="0.00" /><input type="hidden" name="amount_pos" id="amount_pos" value="0.00" />元</td>
	</tr>
	<tr>
		<td colspan="3" >　　备注：
			<label for="textfield"></label>
		<input type="text" name="remark" id="remark" size="80" maxlength="100" />100字</td>
		<td>　　　　　<input name="button35" type="button" id="sub" class="button" value="交款" /></td>
	</tr>
	
</table>
</form>
	
</div>
<!-- 查看未缴款明细的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/payup/noPayUpDetail/" />
</body>
</html>
