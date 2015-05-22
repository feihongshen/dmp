<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.PayUp"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	BigDecimal hk_amount = request.getAttribute("hk_amount")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("hk_amount");
	Branch  ub = request.getAttribute("user_branch")==null?new Branch():(Branch)request.getAttribute("user_branch");
	List<PayUp> puList = request.getAttribute("payupList")==null?new ArrayList<PayUp>():(List<PayUp>)request.getAttribute("payupList");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>结算</title>
<link href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
$(function(){
	$("#credatetime").datepicker({
	    dateFormat: 'yy-mm-dd'
	});
	$("#credatetime1").datepicker({
	    dateFormat: 'yy-mm-dd'
	});
	$("#sub").click(function(){
		$("#subPayUp").submit();
	});
	
});
</script>
</head>

<body style="background: #f5f5f5">



<div class="menucontant">
							
			
<div class="list_topbar">				
<div>
	<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
		<tr id="customertr2" class="VwCtr" style="display:">
			<td width="200">当前站点：<strong><%=ub.getBranchname() %></strong></td>
			<td>当前欠款：<strong id="huokuan"><%=hk_amount.add(ub.getArrearagehuo()).add(ub.getArrearagepei()) %></strong>元
（货款<%=hk_amount %>元，欠款<%=ub.getArrearagehuo() %>元，赔款<%=ub.getArrearagepei() %>元）
，罚款：<strong id="fakuan"><%=ub.getArrearagefa() %></strong>元    <a href="viewCount" ><font color="#ff9900">现在交款</font></a></td>
			</tr>
		</table>
</div>
<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">

	<form name="form1" method="post" action="viewPayUp" >
	<tr id="customertr" class=VwCtr style="display:">
		<td width="200">付款状态：
			<label for="select"></label>
			<select name="upstate" id="upstate">
				<option value="-1">请选择</option>
				<option value="0" <%="0".equals(request.getParameter("upstate"))?"selected":"" %>>缴款成功</option>
				<option value="1" <%="1".equals(request.getParameter("upstate"))?"selected":"" %>>已审核</option>
		</select></td>
		<td>交款类型：
			<label for="select"></label>
			<select  name="type" id="type">
				<option value="0">请选择</option>
				<option value="1" <%="1".equals(request.getParameter("type"))?"selected":"" %>>货款</option>
				<option value="2" <%="2".equals(request.getParameter("type"))?"selected":"" %>>罚款</option>
		</select></td>
		<td>交款方式：
			<select name="way" id="way">
				<option value="0">请选择</option>
				<option value="1" <%="1".equals(request.getParameter("way"))?"selected":"" %>>银行转账</option>
				<option value="2" <%="2".equals(request.getParameter("way"))?"selected":"" %>>现金</option>
		</select></td>
		<td>付款时间：<input type="text" id="credatetime" name="credatetime" value="<%=(String)request.getAttribute("credatetime") %>" >至<input type="text" id="credatetime1" name="credatetime1" value="<%=(String)request.getAttribute("credatetime1") %>" >
		<input type="submit" value="查询" class="input_button2"  />
		<input type="button"  onclick="location.href='<%=request.getContextPath() %>/payup/viewCount'" value="返回" class="input_button2" />
		</td>
	</tr>
</form>
</table>
<%if(puList.size()>0){ %>
<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
	<tr>
		<td colspan="4"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
			<tr class="font_1">
				<td width="10%" height="28" align="center" valign="middle" bgcolor="#eef6ff">付款状态</td>
				<td width="20%"  valign="middle" bgcolor="#eef6ff">付款日期</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">付款类型</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">金额(POS和支付宝COD扫码)</td>
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">付款方式</td>
				<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">改单详情</td>
			</tr>
		</table>
	</tr>
</table>
<%} %>	
</div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>	
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>	
<div class="jg_10"></div>

<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
	<tr>
		<td colspan="4"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
			<%for(PayUp pu : puList){ %>
			<tr>
				<td width="10%" align="center" valign="middle" ><%=pu.getUpstate()==0?"缴款成功":"已审核" %></td>
				<td width="20%"  ><%=pu.getCredatetime() %></td>
				<td width="10%" valign="middle" align="center" ><%=pu.getType()==1?"货款":"罚款" %></td>
				<td width="20%" valign="middle" align="right" ><%=pu.getAmount() %>(<%=pu.getAmountPos() %>)</td>
				<td width="15%" valign="middle" align="center" ><%=pu.getWay()==1?"银行转账":"现金" %></td>
				<td width="15%" valign="middle" align="center" ><%=pu.getRemark() %></td>
				<td width="10%" valign="middle" align="center" >
				<%if(pu.getUpdateTime()!=null&&!pu.getUpdateTime().equals("")){ %>
				[<a href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?payupid=<%=pu.getId() %>');">改单详情</a>]
				<%} %>
				</td>
			</tr>
			<%} %>
		</table></td>
	</tr>
</table>
</div>

</body>
</html>
