
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.PayUp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<String> Foridlist=(List<String>)request.getAttribute("serch");
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<PayUp>  payupList = request.getAttribute("payupList")==null?new ArrayList<PayUp>():(List<PayUp> )request.getAttribute("payupList");
  Map<Long,JSONObject> groupByPayupidCount = (Map<Long,JSONObject>)request.getAttribute("groupByPayupidCount");
  String starttime=request.getParameter("strateBranchpaydatetime")==null?DateTimeUtil.getPreviousDate(new Date()).toString()+" 00:00:00":request.getParameter("strateBranchpaydatetime");
  String endtime=request.getParameter("endBranchpaydatetime")==null?DateTimeUtil.getPreviousDate(new Date()).toString()+" 23:59:59":request.getParameter("endBranchpaydatetime");
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
$("document").ready(function(){  
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkbox']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkbox']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#btnval").click(function(){
		if(check()){
	    $("#searchForm").submit();
		}
	});

	$("#updateF").click(function(){//
		$("#controlStr").val("");
		$("#mackStr").val("");
		var str=""; 
		var mackStr = "";
		$("input[name='checkbox']:checkbox:checked").each(function(){  
			var id = $(this).val();
			if(id!=-1){
				str+=$(this).val()+";";
				mackStr+=$("#aremark"+id).val()+"P:P"; 
			}
			
		}); 
		$("#controlStr").val(str);
		$("#mackStr").val(mackStr);
		if($("#mackStr").val()==""){
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
<body style="background:#f5f5f5">
   <div class="menucontant">
		<form id="searchForm" action ="<%=request.getContextPath()%>/funds/payamount" method = "post">
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
			  <tr>
					<td width="100%">
					审核状态：<select name="upstate" id="upstate">
								<option value="0" <%=request.getParameter("upstate")==null||request.getParameter("upstate")=="0"?"selected":"" %> >未审核</option>
								<option value="1" <%=request.getParameter("upstate")=="1"?"selected":"" %> >已审核</option>
							</select>
					    站点：<select name ="branchid" id="branid">
				               <option value="-1">全部</option>
				               <%if(branchList != null && branchList.size()>0){ %>
				                <%for( Branch b:branchList){ %>
				               <option value ="<%=b.getBranchid()%>" <%if(b.getBranchid() == new Long(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))) {%>selected="selected"<%} %>><%=b.getBranchname() %></option>
				               <%} }%>
			              </select> 
                                                       站点上交款时间：<input type ="text" name ="strateBranchpaydatetime" id="sBranchpaydatetime" value ="<%=starttime %>">
                                                                                                到<input type ="text" name ="endBranchpaydatetime" id="eBranchpaydatetime" value ="<%=endtime%>">
					<input type ="button"  value ="查看" id="btnval" class="input_button2"/>
					<input type="button" value="导出excel" class="input_button2"  onclick="exportfile()">
					</td>
				</tr>
				</table>
		 </form>
		 <form action="<%=request.getContextPath()%>/funds/exprotTestYB" id="form3" method="post">
			<%
				  String id="";
			if(Foridlist.size()>0){
				 
				for(int i=0;i<Foridlist.size();i++){
					id+=Foridlist.get(i) +",";
				}
				 	id= id.substring(0,id.length()-1);
				 	
			}
			
			%>
			<table><tr><td>
			<input type="hidden" name="id" value="<%=id%>" />
			</td></tr></table>
			</form>
 
 
 
 
	<div style="padding:10px; overflow:hidden" id="scroll2">	
		<h1 style="line-height:30px; font-size:18px; font-family:'微软雅黑', '黑体'; font-weight:bold; color:#369"><%="1".equals(request.getParameter("upstate"))?"已审核":"未审核" %>记录：</h1>	
		<div style=" position:relative; z-index:0; width:100% " >	
				<!--左侧开始 -->
					<div style="width:232px; position: absolute; overflow:hidden; left: 0px; top: 0px; overflow: hidden; z-index: 8; background: #FFF; border-right:1px solid #9EC5DA">
					<table width="3000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							
							<tr class="font_1">
								<td width="30" align="center" valign="middle" ><input type="checkbox" name='checkbox' id="btn1" value="-1"/></td>
								<td width="200" align="center" valign="middle" >站点名称</td>
								<td align="center" valign="middle" >站点上缴时间</td>
								<td valign="middle" >员工交款日期</td>
								<td align="center" valign="middle" >票数</td>
								<td align="center" valign="middle" >当日应上缴</td>
								<td align="center" valign="middle" >当日实收（不含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" >现金实收[元]</td>
								<td align="center" valign="middle" >其他款项 </td>
								<td align="center" valign="middle" >累计欠款</td>
								<td align="center" valign="middle" >POS实收(含支付宝COD扫码)</td>
								<td align="center" valign="middle" >支票实收</td>
								<td align="center" valign="middle" >当日实收（含POS和支付宝COD扫码）</td>
								<td width="200" align="center" valign="middle" >上交款审核备注</td>
								<td align="center" valign="middle" >上交款备注 </td>
								<td align="center" valign="middle" >上交款方式</td>
								<td align="center" valign="middle" >交款类型 </td>
								<td align="center" valign="middle" >上交款人</td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td   align="center" valign="middle" bgcolor="#eef6ff" width="200px">审核人</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff" width="200px">审核时间</td>
								<%} %>
								<td align="center" valign="middle" >改单详情</td>
							</tr>
							 <% 
							  BigDecimal receivablefee=BigDecimal.ZERO; //应上缴金额
							  BigDecimal receivedfee=BigDecimal.ZERO; //实际上缴金额
							  BigDecimal receivedfeecash=BigDecimal.ZERO; //现金金额
							  BigDecimal totaldebtfee=BigDecimal.ZERO; //累计欠款
							  BigDecimal receivedfeepos=BigDecimal.ZERO; //pos实收
							  BigDecimal receivedfeecheque=BigDecimal.ZERO; //支票实收
							  BigDecimal otherbranchfee=BigDecimal.ZERO; //其他款项
							  BigDecimal receivedfeeAndPos=BigDecimal.ZERO; //当日实收总计
							  /* Map<Long,BigDecimal> branchTotalFee = new HashMap<Long,BigDecimal>(); */
							  
							  long cwbnum = 0;
							%>
						    <% for(PayUp payUp : payupList){ %>
						    <%
						    
						    BigDecimal sumCash =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumCash")));
						    BigDecimal sumPos =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumPos")));
						    BigDecimal sumCheckfee =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumCheckfee")));
						    BigDecimal sumOrderfee =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumOrderfee")));
						    BigDecimal sumReturnfee = BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumReturnfee")));
						    
						    %>
								<tr valign="middle">
								<td align="center" valign="middle" > <input type="checkbox" name="checkbox" value="<%=payUp.getId() %>"> </td>
								<td align="center" valign="middle" > <%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getBranchname());}} %> </td>
								<td align="center" valign="middle" ><%=payUp.getCredatetime().substring(0,19) %> </td>
								<td align="center" valign="middle"  ><%=payUp.getCredatetime().substring(0,10) %></td>
								<td align="center" valign="middle" ><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=groupByPayupidCount == null? 0:(groupByPayupidCount.get(payUp.getId())==null?0:groupByPayupidCount.get(payUp.getId()).getLong("countCwb") ) %></a></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumOrderfee %></a></strong></td>
								<td align="right" valign="middle" ><strong>0</strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumPos %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCheckfee%></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.subtract(sumReturnfee).add(sumPos).add(sumCheckfee).add(sumOrderfee).doubleValue() %></a></strong></td>
								<td align="center" valign="middle" >
								<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
								<input  type="text"  <%-- id="aremark<%=payUp.getId()%>" --%> value="<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>"/>
								<%}else{ %>
								<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>
								<%} %>
								</td>
								<td align="center" valign="middle" ><%=payUp.getRemark() %></td>
								<td align="center" valign="middle" ><%=payUp.getWay()==1?"银行转账":"现金"%></td>
								<td align="center" valign="middle" ><%=payUp.getType()==1?"货款":"罚款"%></td>
								 <td align="center" valign="middle" ><%=StringUtil.nullConvertToEmptyString(payUp.getUpuserrealname()) %></td>
								 <% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditinguser()%></td>
								<td align="center" valign="middle" bgcolor="#eef6ff" ><%=payUp.getAuditingtime()%></td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff" >
								<%if(payUp.getUpdateTime()!=null&&!payUp.getUpdateTime().equals("")){ %>
								[<a href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?payupid=<%=payUp.getId() %>');">改单详情</a>]
								<%} %>
								</td>
							 </tr>
							 <%
								    
									receivablefee = receivablefee.add(sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee));
									receivedfee = receivedfee.add(sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee));
									receivedfeecash = receivedfeecash.add(sumCash).subtract(sumReturnfee);
									otherbranchfee = otherbranchfee.add(sumOrderfee);
									
									/* for(Branch b :branchList){
										if(payUp.getBranchid()==b.getBranchid()){
											branchTotalFee.put(payUp.getBranchid(), (b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
										}
									}
									List<Long> keys = new ArrayList<Long>(branchTotalFee.keySet());
									for (int i = 0; i < keys.size(); i++) {
										totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i)));
									} */
									for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){
										totaldebtfee = totaldebtfee.add((b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
									}}
									receivedfeepos = receivedfeepos.add(sumPos);
									receivedfeecheque = receivedfeecheque.add(sumCheckfee);
									receivedfeeAndPos = receivedfeeAndPos.add(sumCash.subtract(sumReturnfee).add(sumPos).add(sumCheckfee).add(sumOrderfee));
									cwbnum = cwbnum + (groupByPayupidCount == null? 0:(groupByPayupidCount.get(payUp.getId())==null?0:groupByPayupidCount.get(payUp.getId()).getLong("countCwb") )); 
								  %>
							<%} %> 
								<tr valign="middle" >
									<td >合计</td>
									<td ></td>
									<td ></td>
									<td ></td>
									<td ><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=cwbnum%></a>[票]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivablefee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeecash%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= otherbranchfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/0/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= totaldebtfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeepos%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeecheque%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeeAndPos%></a></strong>[元]</td>
									<td ></td>
									<td ></td> 
									<td ></td>
									<td ></td>
									<td ></td>
									<% if("1".equals(request.getParameter("upstate"))){ %>
									<td ></td>
									<td ></td>
									<%} %>
									<td ></td>
								</tr>
								<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
							    <tr  valign="middle">
							       <td colspan ="20" align="center" valign="middle" width="200px">
				                  </td>
					            </tr>
					            <%} %>	
								
						</tbody>
					</table>
			</div>
				<!--左侧结束 -->
				
				<!--滑动内容开始 -->
				<div style="overflow-x:scroll; width:100%;">
					<table width="3000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
							<tr class="font_1">
								<td width="30" align="center" valign="middle" ></td>
								<td width="200" align="center" valign="middle" ><!-- 站点  -->站点名称</td>
								<td align="center" valign="middle" >站点上缴时间</td>
								<td valign="middle" >员工交款日期</td>
								<td align="center" valign="middle" >票数</td>
								<td align="center" valign="middle" >当日应上缴</td>
								<td align="center" valign="middle" >当日实收（不含POS和支付宝COD扫码）</td>
								<td align="center" valign="middle" >现金实收[元]</td>
								<td align="center" valign="middle" >其他款项 </td>
								<td align="center" valign="middle" >累计欠款</td>
								<td align="center" valign="middle" >POS实收(含支付宝COD扫码)</td>
								<td align="center" valign="middle" >支票实收</td>
								<td align="center" valign="middle" >当日实收（含POS和支付宝COD扫码）</td>
								<td width="200" align="center" valign="middle" >上交款审核备注</td>
								<td align="center" valign="middle" >上交款备注 </td>
								<td align="center" valign="middle" >上交款方式</td>
								<td align="center" valign="middle" >交款类型 </td>
								<td align="center" valign="middle" >上交款人</td>
								<% if("1".equals(request.getParameter("upstate"))){ %>
								<td   align="center" valign="middle" bgcolor="#eef6ff" width="200px">审核人</td>
								<td   align="center" valign="middle" bgcolor="#eef6ff" width="200px">审核时间</td>
								<%} %>
								<td align="center" valign="middle" >改单详情</td>
							</tr>
							 <% 
							   receivablefee=BigDecimal.ZERO; //应上缴金额
							   receivedfee=BigDecimal.ZERO; //实际上缴金额
							   receivedfeecash=BigDecimal.ZERO; //现金金额
							   //totaldebtfee=BigDecimal.ZERO; //累计欠款
							   receivedfeepos=BigDecimal.ZERO; //pos实收
							   receivedfeecheque=BigDecimal.ZERO; //支票实收
							   otherbranchfee=BigDecimal.ZERO; //其他款项
							   receivedfeeAndPos=BigDecimal.ZERO; //当日实收总计
							   cwbnum = 0;
							%>
						   
						    <% for(PayUp payUp : payupList){ %>
						    <%
						    
						    BigDecimal sumCash =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumCash")));
						    BigDecimal sumPos =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumPos")));
						    BigDecimal sumCheckfee =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumCheckfee")));
						    BigDecimal sumOrderfee =  BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumOrderfee")));
						    BigDecimal sumReturnfee = BigDecimal.valueOf(groupByPayupidCount == null? 0.00:(groupByPayupidCount.get(payUp.getId())==null?0.00:groupByPayupidCount.get(payUp.getId()).getDouble("sumReturnfee")));
						    
						    %>
								<tr valign="middle">
								<td align="center" valign="middle" ><%-- <input type="checkbox" name="checkbox" value="<%=payUp.getId() %>"> --%></td>
								<td align="center" valign="middle" ><%-- <%for(Branch b :branchList){if(payUp.getBranchid()==b.getBranchid()){out.print(b.getBranchname());}} %> --%></td>
								<td align="center" valign="middle" ><%=payUp.getCredatetime().substring(0,19) %> </td>
								<td align="center" valign="middle"  ><%=payUp.getCredatetime().substring(0,10) %></td>
								<td align="center" valign="middle" ><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=groupByPayupidCount == null? 0:(groupByPayupidCount.get(payUp.getId())==null?0:groupByPayupidCount.get(payUp.getId()).getLong("countCwb") ) %></a></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.subtract(sumReturnfee).doubleValue() %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumOrderfee %></a></strong></td>
								<td align="right" valign="middle" ><strong>0</strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumPos %></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCheckfee%></a></strong></td>
								<td align="right" valign="middle" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeShow/<%=payUp.getId() %>/-2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=sumCash.subtract(sumReturnfee).add(sumPos).add(sumCheckfee).add(sumOrderfee).doubleValue() %></a></strong></td>
								<td align="center" valign="middle" >
								<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
								<input  type="text"  id="aremark<%=payUp.getId()%>" value="<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>"/>
								<%}else{ %>
								<%=StringUtil.nullConvertToEmptyString(payUp.getAuditingremark())%>
								<%} %>
								</td>
								<td align="center" valign="middle" ><%=payUp.getRemark() %></td>
								<td align="center" valign="middle" ><%=payUp.getWay()==1?"银行转账":"现金"%></td>
								<td align="center" valign="middle" ><%=payUp.getType()==1?"货款":"罚款"%></td>
								 <td align="center" valign="middle" ><%=StringUtil.nullConvertToEmptyString(payUp.getUpuserrealname()) %></td>
								 <% if("1".equals(request.getParameter("upstate"))){ %>
								<td align="center" valign="middle" bgcolor="#eef6ff"><%=payUp.getAuditinguser()%></td>
								<td align="center" valign="middle" bgcolor="#eef6ff" ><%=payUp.getAuditingtime()%></td>
								<%} %>
								<td align="center" valign="middle" bgcolor="#eef6ff" >
								<%if(payUp.getUpdateTime()!=null&&!payUp.getUpdateTime().equals("")){ %>
								[<a href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?payupid=<%=payUp.getId() %>');">改单详情</a>]
								<%} %>
								</td>
							 </tr>
							 <%
								    
									receivablefee = receivablefee.add(sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee));
									receivedfee = receivedfee.add(sumCash.add(sumCheckfee).add(sumOrderfee).subtract(sumReturnfee));
									receivedfeecash = receivedfeecash.add(sumCash).subtract(sumReturnfee);
									otherbranchfee = otherbranchfee.add(sumOrderfee);
									/* Map<Long,BigDecimal> branchTotalFee = new HashMap<Long,BigDecimal>();
									for(Branch b :branchList){
										if(payUp.getBranchid()==b.getBranchid()){
											branchTotalFee.put(payUp.getBranchid(), (b.getArrearagepayupaudit()==null?BigDecimal.ZERO:b.getArrearagepayupaudit()).add(b.getPosarrearagepayupaudit()==null?BigDecimal.ZERO:b.getPosarrearagepayupaudit()));
										}
									}
									List<Long> keys = new ArrayList<Long>(branchTotalFee.keySet());
									for (int i = 0; i < keys.size(); i++) {
										totaldebtfee=totaldebtfee.add(branchTotalFee.get(keys.get(i)));
									} */
									receivedfeepos = receivedfeepos.add(sumPos);
									receivedfeecheque = receivedfeecheque.add(sumCheckfee);
									receivedfeeAndPos = receivedfeeAndPos.add(sumCash.subtract(sumReturnfee).add(sumPos).add(sumCheckfee).add(sumOrderfee));
									cwbnum = cwbnum + (groupByPayupidCount == null? 0:(groupByPayupidCount.get(payUp.getId())==null?0:groupByPayupidCount.get(payUp.getId()).getLong("countCwb") )); 
								  %>
							<%} %> 
								<tr valign="middle" >
									<td ></td>
									<td >合计</td>
									<td ></td>
									<td ></td>
									<td ><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%=cwbnum%></a>[票]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivablefee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/1/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeecash%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/3/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= otherbranchfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/0/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= totaldebtfee%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeepos%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/4/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeecheque%></a></strong>[元]</td>
									<td align="right" ><strong><a href="<%=request.getContextPath()%>/funds/deliveryStateTypeAllShow/-2/1?branchid=<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>&strateBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime"))%>&endBranchpaydatetime=<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime"))%>&upstate=<%=request.getParameter("upstate")==null?"0":request.getParameter("upstate") %>"><%= receivedfeeAndPos%></a></strong>[元]</td>
									<td ></td>
									<td ></td> 
									<td ></td>
									<td ></td>
									<td ></td>
									<% if("1".equals(request.getParameter("upstate"))){ %>
									<td ></td>
									<td ></td>
									<%} %>
									<td ></td>
								</tr>
								<%if(request.getParameter("upstate")==null||Long.parseLong(request.getParameter("upstate"))==0){%>
							    <tr  valign="middle">
							       <td colspan ="20" align="center" valign="middle" >
								      <form id="updateForm" action ="<%=request.getContextPath()%>/funds/update"  method = "post">
					                      <input type="hidden" id="controlStr" name="controlStr" value=""/>
					                      <input type="hidden" id="mackStr" name="mackStr" value=""/>
					                      <input type="hidden" id="branchid" name="branchid" value="<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>"/>
					                      <input type="hidden" id="strateBranchpaydatetime" name="strateBranchpaydatetime" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("strateBranchpaydatetime")) %>"/>
					                      <input type="hidden" id="endBranchpaydatetime" name="endBranchpaydatetime" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endBranchpaydatetime")) %>"/>
					                      <input type="button" id="updateF"  value="审核交款"/>
					                  </form>
				                  </td>
					            </tr>
					            <%} %>	
								 
						</tbody>
					</table>
				</div>
				<!--滑动内容结束 -->
	   </div>	
				
				</div>
         
			<div class="jg_10"></div>
			<div class="jg_10"></div>
	</div>
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

function exportfile(){
	if(<%=payupList.size()>0%> ){
		$("#form3").submit();
	}else{
		alert("没有数据无法导出！");
	}
	}

</script> 
   </body>
</HTML>
   
