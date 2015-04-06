<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.controller.PayUpDTO"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<PayUpDTO>  payupList = request.getAttribute("payupList")==null?null:(List<PayUpDTO> )request.getAttribute("payupList");
  String starttime=request.getParameter("strateBranchpaydatetime")==null?DateTimeUtil.getPreviousDate(new Date()).toString()+" 00:00:00":request.getParameter("strateBranchpaydatetime");
  String endtime=request.getParameter("endBranchpaydatetime")==null?DateTimeUtil.getPreviousDate(new Date()).toString()+" 23:59:59":request.getParameter("endBranchpaydatetime");
  //现金欠款
  //BigDecimal totalcashdebtfee = request.getAttribute("totalcashdebtfee")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("totalcashdebtfee");
//pos欠款
  //BigDecimal totalposdebtfee = request.getAttribute("totalposdebtfee")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("totalposdebtfee");
//累计欠款
  //BigDecimal totaldebtfee = request.getAttribute("totalposdebtfee")==null?BigDecimal.ZERO:(BigDecimal)request.getAttribute("totaldebtfee");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>


<script type="text/javascript">
function check(){
	if($("#sBranchpaydatetime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#eBranchpaydatetime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#sBranchpaydatetime").val()>$("#eBranchpaydatetime").val()){
		alert("站点交款开始时间不能大于结束时间");
		return false;
	}
	
	return true;
}
</script>
<script type="text/javascript">  

function checkfeereg(money){
	var istrue = /^\-?(0|[1-9]\d{0,})\.\d{1,2}$/.test(money);
	if(!istrue){
		return 1;
	}
}
$("document").ready(function(){  
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkboxup']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkboxup']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#btnval").click(function(){
		if(check()){
	    $("#searchForm").submit();
		}
	});

	$("#updateF").click(function(){
		$("#controlStr").val("");
		$("#mackStr").val("");
		$("#cashStr").val("");
		$("#posStr").val("");
		var k=0;
		var str="["; 
		$("input[name='checkboxup']:checked").each(function(){  
			var id = $(this).val();
			if(checkfeereg($("#cash"+id).val())==1 ||checkfeereg($("#pos"+id).val()) ==1 ){
				k=1;
			}
			str += "{branchid:\""+$("#branchid"+id).val()+"\",payupids:\""+id+"\",mackStr:\""+$("#aremark"+id).val()+"\",cash:\""+$("#cash"+id).val()+"\",pos:\""+$("#pos"+id).val()+"\",oldcash:\""+$("#oldcash"+id).val()+"\",oldpos:\""+$("#oldpos"+id).val()+"\"},";
		}); 
		if(k==1){
			alert("金额格式不正确！");
			return false;
		}
		if(str!="["){
			str = str.substring(0, str.length-1)+"]";
			
		}
		$("#controlStr").val(str);
		if($("#controlStr").val()=="["){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		$("#updateForm").submit();
	});
}); 

</script>
<script>
$(function() {
	$("#sBranchpaydatetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#eBranchpaydatetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});
</script>
</HEAD>
<body style="background: #f5f5f5">
	<div class="menucontant">
		<form id="searchForm"
			action="<%=request.getContextPath()%>/funds/payamount" method="post">
			<table width="100%" height="23" border="0" cellpadding="0"
				cellspacing="5" class="right_set1" border="1">
				<tr>
					<td width="100%">审核状态：<select name="upstate" id="upstate">
							<option value="0"
								<%=request.getParameter("upstate")==null||request.getParameter("upstate")=="0"?"selected":"" %>>未审核</option>
							<option value="1"
								<%=request.getParameter("upstate")=="1"?"selected":"" %>>已审核</option>
					</select> 站点：<select name="branchid" id="branid">
							<option value="-1">全部</option>
							<%if(branchList != null && branchList.size()>0){ %>
							<%for( Branch b:branchList){ %>
							<option value="<%=b.getBranchid()%>"
								<%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>
								selected="selected" <%} %>><%=b.getBranchname() %></option>
							<%} }%>
					</select> 站点上交款时间：<input type="text" name="strateBranchpaydatetime"
						id="sBranchpaydatetime"
						value="<%=starttime %>">
						到<input type="text" name="endBranchpaydatetime"
						id="eBranchpaydatetime"
						value="<%=endtime%>">
						<input type="button" value="查看" id="btnval" class="input_button2" />
						<input type="button" value="导出excel" class="input_button2"
						id="checkexcel">

					</td>
				</tr>
			</table>
		</form>
		<!--yes  -->
		<form action="<%=request.getContextPath()%>/funds/dateExport" method="post" id="form3">
			<input type="hidden"
				value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime")) %>"
				name="strateBranchpaydatetime"> 
				<input type="hidden"
				value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime")) %>"
				name="endBranchpaydatetime"> 
				<input type="hidden"
				value="<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid") %>"
				name="branchid"> 
				<input type="hidden"
				value="<%=request.getParameter("upstate")==null?"-1":request.getParameter("upstate") %>"
				name="upstate">
		</form>

		<div style="position:relative; z-index:0;width: 100%" id="scroll2">
			<h1 style="line-height:30px; font-size:18px; font-family:'微软雅黑', '黑体'; font-weight:bold; color:#369"><%="1".equals(request.getParameter("upstate"))?"已审核":"未审核" %>记录：</h1>
			<div style=" position:relative; z-index:0; width:100% " >	
				<!--左侧开始 -->
					<div style="width:232px; position: absolute; overflow:hidden; left: 0px; top: 0px; overflow: hidden; z-index: 8; background: #FFF; border-right:1px solid #9EC5DA">
						<table width="3000" border="0" cellspacing="1" cellpadding="0" class="table_2" >
								<tr class="font_1">
								<td height="38" width="30" align="center" valign="middle" bgcolor="#eef6ff" ><input type="checkbox" id="btn1" value="-1"/></td>
								<td align="center" valign="middle" bgcolor="#eef6ff" width="200px" >站点</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">站点上缴时间</td>
								<td valign="middle" bgcolor="#eef6ff">员工交款日期</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">票数</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日应上缴</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日收款（不含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日收款（含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金实收[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金欠款[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS实收(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS欠款(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">累计欠款</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">其他款项</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">支票实收</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款审核备注</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款备注
								</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款方式</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">交款类型</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款人</td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff">审核人</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff">改单详情</td>
							</tr>
							<% 
							  BigDecimal receivablefee=BigDecimal.ZERO; //应上缴金额
							  BigDecimal receivedfee=BigDecimal.ZERO; //实际上缴金额
							  BigDecimal receivedfeecash=BigDecimal.ZERO; //现金金额
							  BigDecimal receivedfeepos=BigDecimal.ZERO; //pos实收
							  BigDecimal receivedfeecheque=BigDecimal.ZERO; //支票实收
							  BigDecimal otherbranchfee=BigDecimal.ZERO; //其他款项
							  BigDecimal totalposdebtfee=BigDecimal.ZERO; //pos欠款
							  BigDecimal totalcashdebtfee=BigDecimal.ZERO; //现金欠款
							  BigDecimal totaldebtfee=BigDecimal.ZERO; //累计欠款 
							  BigDecimal receivedfeeAndPos=BigDecimal.ZERO; //当日实收总计
							  /* Map<Long,BigDecimal> branchCashTotalFee = new HashMap<Long,BigDecimal>();
							  Map<Long,BigDecimal> branchPosTotalFee = new HashMap<Long,BigDecimal>();
							  Map<Long,BigDecimal> branchTotalFee = new HashMap<Long,BigDecimal>(); */
							  
							  long cwbnum = 0;
							%>
							<!-- {"countCwb":13,"sumCash":109999.99,"sumPos":12222.22,"sumCheckfee":0,"sumOrderfee":0,"sumReturnfee":22222.22}
							1.现金 2.pos 3.其他 4.支票-->
							<% for(PayUpDTO payUp : payupList){ %>

							<tr valign="middle">
								<td align="center" valign="middle">
									<input type="checkbox" name="checkboxup" value="<%=payUp.getIds().replaceAll(",", "_") %>">
								</td>
								<td align="center" valign="middle"  bgcolor="#eef6ff">
									<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getBranchname());}} %>
									<input type="hidden" id="branchid<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getBranchid() %>" />
								</td>
								<td align="center" valign="middle"><%=payUp.getCredatetime().substring(0,10) %>
								</td>
								<td align="center" valign="middle"><%=payUp.getCredatetime().substring(0,10) %></td>
								<td align="center" valign="middle"><a
									href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
										<%=payUp.getAduitJson().getInt("countCwb") %></a></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount() %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount() %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount().add(payUp.getAmountPos()) %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumCash") %></a></strong> <input
									type="hidden"
									value="<%=payUp.getAduitJson().getDouble("sumCash") %>" /></td>
								<td align="right" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text"
								
									value="<%=payUp.getAduitJson().getDouble("sumCash")%>" /> <%}else{ %>
									<%=payUp.getRamount()%> <%} %>
								</td>
								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit());}} %>
								</strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmountPos() %></a></strong> <input type="hidden"
									value="<%=payUp.getAmountPos() %>" /></td>
								<td align="right" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text"
									
									value="<%=payUp.getAmountPos() %>" /> <%}else{ %> <%=payUp.getRamountPos() %>
									<%} %>
								</td>
								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getPosarrearagepayupaudit());}} %>
								</strong></td>

								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit().add(b.getPosarrearagepayupaudit()));}} %>
								</strong></td>

								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumOrderfee") %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumCheckfee") %></a></strong></td>
								<td align="center" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text" value="<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>" />
									<%}else{ %> <%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>
									<%} %>
								</td>
								<td align="center" valign="middle"><%=payUp.getRemark() %></td>
								<td align="center" valign="middle"><%=payUp.getWays().replaceAll("1", "银行转账").replaceAll("2", "现金")%></td>
								<td align="center" valign="middle"><%=payUp.getTypes().replaceAll("1", "货款").replaceAll("2", "罚款")%></td>
								<td align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(payUp.getUpuserrealname()) %></td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditinguser()%></td>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditingtime()%></td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff">
									<%if(payUp.getUpdateTime()!=null&&!payUp.getUpdateTime().equals("")){ %>
									[<a
									href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?payupid=<%=payUp.getIds() %>');">改单详情</a>]
									<%} %>
								</td>
							</tr>
							<%
								cwbnum += payUp.getAduitJson().getInt("countCwb");
								//应上缴金额
								receivablefee = receivablefee.add(payUp.getAmount());
								//实际上缴金额
								receivedfee = receivedfee.add(payUp.getAmount());
								//现金金额
								receivedfeecash = receivedfeecash.add(new BigDecimal(payUp.getAduitJson().getString("sumCash")));
								
								//pos
								receivedfeepos = receivedfeepos.add(payUp.getAmountPos());
								//其他
								otherbranchfee = otherbranchfee.add(new BigDecimal(payUp.getAduitJson().getString("sumOrderfee")));
								//支票
								receivedfeecheque = receivedfeecheque.add(new BigDecimal(payUp.getAduitJson().getString("sumCheckfee")));
								//所有
								receivedfeeAndPos = receivedfeeAndPos.add(payUp.getAmount().add(payUp.getAmountPos()));
								
								for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){
									//现金欠款
									totalcashdebtfee = totalcashdebtfee.add(b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit());
									//pos欠款
									totalposdebtfee = totalposdebtfee.add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit());
									//总欠款
									totaldebtfee = totaldebtfee.add((b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
								}}
								/* for(Branch b :branchList){
									if(payUp.getBranchid()==b.getBranchid()){
										branchCashTotalFee.put(payUp.getBranchid(), b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit());
										branchPosTotalFee.put(payUp.getBranchid(), b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit());
										branchTotalFee.put(payUp.getBranchid(), (b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
									}
								}
								List<Long> cashkeys = new ArrayList<Long>(branchCashTotalFee.keySet());
								for (int i = 0; i < cashkeys.size(); i++) {
									//现金欠款
									totalcashdebtfee=totalcashdebtfee.add(branchCashTotalFee.get(cashkeys.get(i)));
								}
								List<Long> poskeys = new ArrayList<Long>(branchPosTotalFee.keySet());
								for (int i = 0; i < poskeys.size(); i++) {
									//pos欠款
									totalposdebtfee=totalposdebtfee.add(branchPosTotalFee.get(poskeys.get(i)));
								}
								List<Long> keys = new ArrayList<Long>(branchTotalFee.keySet());
								for (int i = 0; i < keys.size(); i++) {
									//总欠款
									totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i)));
								} */
							%>

							<%} %>
							<tr valign="middle">
								<td width="30px"></td>
								<td width="200px">合计</td>
								<td></td>
								<td></td>
								<td><a
									href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=cwbnum%></a>[票]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivablefee%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfee%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeeAndPos%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeecash%></a></strong>[元]</td>
								<td align="right"></td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totalcashdebtfee %></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeepos%></a></strong>[元]</td>
								<td align="right"></td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totalposdebtfee %></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/0/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totaldebtfee %></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= otherbranchfee%></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeecheque%></a></strong>[元]</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td></td>
								<td></td>
								<%} %>
								<td></td>
							</tr>
							<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
							<tr valign="middle">
								<td colspan="24" align="center" valign="middle">
									
								</td>
							</tr>
							<%} %>
							
						</table>
					</div>
					<!--左侧结束   滑动开始  -->
					<div style="overflow-x:scroll; width:100%;">
						<table width="3000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
								<tr class="font_1">
								<td height="38" align="center" valign="middle" bgcolor="#eef6ff" width="30px"></td>
								<td align="center" valign="middle" bgcolor="#eef6ff"  width="200px"></td>
								<td align="center" valign="middle" bgcolor="#eef6ff">站点上缴时间</td>
								<td valign="middle" bgcolor="#eef6ff">员工交款日期</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">票数</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日应上缴</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日收款（不含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">当日收款（含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金实收[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">现金欠款[元]</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS实收(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">POS欠款(含支付宝COD扫码)</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">累计欠款</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">其他款项</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">支票实收</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款审核备注</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款备注
								</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款方式</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">交款类型</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">上交款人</td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff">审核人</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff">改单详情</td>
							</tr>
							
							<!-- {"countCwb":13,"sumCash":109999.99,"sumPos":12222.22,"sumCheckfee":0,"sumOrderfee":0,"sumReturnfee":22222.22}
							1.现金 2.pos 3.其他 4.支票-->
							
							<% 
							   cwbnum=0;
							   receivablefee=BigDecimal.ZERO; //应上缴金额
							   receivedfee=BigDecimal.ZERO; //实际上缴金额
							   receivedfeecash=BigDecimal.ZERO; //现金金额
							   receivedfeepos=BigDecimal.ZERO; //pos实收
							   receivedfeecheque=BigDecimal.ZERO; //支票实收
							   otherbranchfee=BigDecimal.ZERO; //其他款项
							   /* totaldebtfee=BigDecimal.ZERO; //累计欠款
							   totalposdebtfee=BigDecimal.ZERO; //pos欠款
							   totalcashdebtfee=BigDecimal.ZERO; //现金欠款 */
							   receivedfeeAndPos=BigDecimal.ZERO; //当日实收总计
							
							%>
							
							
							<% for(PayUpDTO payUp : payupList){ %>
								<tr valign="middle">
								<td align="center" valign="middle">
									<input type="checkbox" name="checkbox" value="<%=payUp.getIds().replaceAll(",", "_") %>">
								</td>
								<td align="center" valign="middle"  bgcolor="#eef6ff">
									<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getBranchname());}} %>
									<input type="hidden" id="branchid<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getBranchid() %>" />
								</td>
								<td align="center" valign="middle"><%=payUp.getCredatetime().substring(0,10) %></td>
								<td align="center" valign="middle"><%=payUp.getCredatetime().substring(0,10) %></td>
								<td align="center" valign="middle"><a
									href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
										<%=payUp.getAduitJson().getInt("countCwb") %></a></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount() %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount() %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmount().add(payUp.getAmountPos()) %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumCash") %></a></strong> <input
									type="hidden"
									id="oldcash<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getAduitJson().getDouble("sumCash") %>" /></td>
								<td align="right" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text"
									id="cash<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getAduitJson().getDouble("sumCash")%>" /> <%}else{ %>
									<%=payUp.getRamount()%> <%} %>
								</td>
								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit());}} %>
								</strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAmountPos() %></a></strong> <input type="hidden"
									id="oldpos<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getAmountPos() %>" /></td>
								<td align="right" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text"
									id="pos<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=payUp.getAmountPos() %>" /> <%}else{ %> <%=payUp.getRamountPos() %>
									<%} %>
								</td>
								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getPosarrearagepayupaudit());}} %>
								</strong></td>

								<td align="right" valign="middle"><strong>
										<%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getArrearagepayupaudit().add(b.getPosarrearagepayupaudit()));}} %>
								</strong></td>

								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumOrderfee") %></a></strong></td>
								<td align="right" valign="middle"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAuditShow/<%=payUp.getIds() %>/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=payUp.getAduitJson().getDouble("sumCheckfee") %></a></strong></td>
								<td align="center" valign="middle">
									<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
									<input type="text"
									id="aremark<%=payUp.getIds().replaceAll(",", "_")%>"
									value="<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>" />
									<%}else{ %> <%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>
									<%} %>
								</td>
								<td align="center" valign="middle"><%=payUp.getRemark() %></td>
								<td align="center" valign="middle"><%=payUp.getWays().replaceAll("1", "银行转账").replaceAll("2", "现金")%></td>
								<td align="center" valign="middle"><%=payUp.getTypes().replaceAll("1", "货款").replaceAll("2", "罚款")%></td>
								<td align="center" valign="middle"><%=StringUtil.nullConvertToEmptyString(payUp.getUpuserrealname()) %></td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditinguser()%></td>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditingtime()%></td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff">
									<%if(payUp.getUpdateTime()!=null&&!payUp.getUpdateTime().equals("")){ %>
									[<a
									href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?payupid=<%=payUp.getIds() %>');">改单详情</a>]
									<%} %>
								</td>
							</tr>
							<% 
							   
								cwbnum += payUp.getAduitJson().getInt("countCwb");
								//应上缴金额
								receivablefee = receivablefee.add(payUp.getAmount());
								//实际上缴金额
								receivedfee = receivedfee.add(payUp.getAmount());
								//现金金额
								receivedfeecash = receivedfeecash.add(new BigDecimal(payUp.getAduitJson().getString("sumCash")));
								//totaldebtfee = totaldebtfee.add(b.getTotaldebtfee()==null?BigDecimal.ZERO:b.getTotaldebtfee());
								//pos
								receivedfeepos = receivedfeepos.add(payUp.getAmountPos());
								//其他
								otherbranchfee = otherbranchfee.add(new BigDecimal(payUp.getAduitJson().getString("sumOrderfee")));
								//支票
								receivedfeecheque = receivedfeecheque.add(new BigDecimal(payUp.getAduitJson().getString("sumCheckfee")));
								//所有
								receivedfeeAndPos = receivedfeeAndPos.add(payUp.getAmount().add(payUp.getAmountPos()));
								/* Map<Long,BigDecimal> branchCashTotalFee = new HashMap<Long,BigDecimal>();
								Map<Long,BigDecimal> branchPosTotalFee = new HashMap<Long,BigDecimal>();
								Map<Long,BigDecimal> branchTotalFee = new HashMap<Long,BigDecimal>();
								for(Branch b :branchList){
									if(payUp.getBranchid()==b.getBranchid()){
										branchCashTotalFee.put(payUp.getBranchid(), b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit());
										branchPosTotalFee.put(payUp.getBranchid(), b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit());
										branchTotalFee.put(payUp.getBranchid(), (b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
									}
								}
								List<Long> cashkeys = new ArrayList<Long>(branchCashTotalFee.keySet());
								for (int i = 0; i < cashkeys.size(); i++) {
									//现金欠款
									totalcashdebtfee=totalcashdebtfee.add(branchCashTotalFee.get(cashkeys.get(i)));
								}
								List<Long> poskeys = new ArrayList<Long>(branchPosTotalFee.keySet());
								for (int i = 0; i < poskeys.size(); i++) {
									//pos欠款
									totalposdebtfee=totalposdebtfee.add(branchPosTotalFee.get(poskeys.get(i)));
								}
								List<Long> keys = new ArrayList<Long>(branchTotalFee.keySet());
								for (int i = 0; i < keys.size(); i++) {
									//总欠款
									totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i)));
								} */
								
								%>

							<%} %>
							<tr valign="middle">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><a
									href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=cwbnum%></a>[票]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivablefee%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfee%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeeAndPos%></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeecash%></a></strong>[元]</td>
								<td align="right"></td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totalcashdebtfee %></a></strong>[元]</td>
								<td align="right"><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeepos%></a></strong>[元]</td>
								<td align="right"></td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totalposdebtfee %></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/0/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%=totaldebtfee %></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= otherbranchfee%></a></strong>[元]</td>
								<td><strong><a
										href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>">
											<%= receivedfeecheque%></a></strong>[元]</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td></td>
								<td></td>
								<%} %>
								<td></td>
							</tr>
							<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
							<tr valign="middle">
								<td colspan="24" align="center" valign="middle">
									<form id="updateForm"
										action="<%=request.getContextPath()%>/funds/updateAudit"
										method="post">
										<input type="hidden" id="controlStr" name="controlStr" value="" />
										<input type="hidden" id="branchid" name="branchid" value="<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>"/>
					                      <input type="hidden" id="strateBranchpaydatetime" name="strateBranchpaydatetime" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime")) %>"/>
					                      <input type="hidden" id="endBranchpaydatetime" name="endBranchpaydatetime" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime")) %>"/> 
					                      <input type="button" id="updateF" value="审核交款" />
									</form>
								</td>
							</tr>
							<%} %>
						</table>
					</div>
			</div>
			
			</div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
		</div>

	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="clear"></div>

	<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#upstate").val(<%=request.getParameter("upstate")==null?0:request.getParameter("upstate")%>);
</script>
	<script type="text/javascript">

$("#checkexcel").click(
		function(){
		if(<%=payupList.size()%>>0){
		$("#form3").submit();
		}else{
			alert("没有数据无法导出");
		}
		}
		);
</script>

</body>
</HTML>

