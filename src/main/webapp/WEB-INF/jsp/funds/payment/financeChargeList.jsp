<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.FinanceAuditTypeEnum"%>
<%@page import="cn.explink.domain.FinanceAudit"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
 <%
 List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
 String starttime=request.getParameter("begindate")==null?DateTimeUtil.getCurrentDayZeroTime():request.getParameter("begindate");
 String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
 List<FinanceAudit> financeAuditList = request.getAttribute("financeAuditList")==null?null:(List<FinanceAudit>)request.getAttribute("financeAuditList");
 
 Page page_obj = (Page)request.getAttribute("page_obj");
 %>
<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>财务记账查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
});

function fukuan(id){
	window.location.href="<%=request.getContextPath()%>/funds/financeChargeconfirm/"+id;
}
</script>
</head>
<body style="background:#eef9ff" >
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm">
			&nbsp;&nbsp;
			供货商：
			<select name="customerid" id="customerid">
				<option value="0">请选择</option>
			  <%if(customerList!=null&&customerList.size()>0)for(Customer c : customerList){ %>
			  	<option value="<%=c.getCustomerid()%>"<%if(Long.parseLong(request.getParameter("customerid")==null?"0":request.getParameter("customerid").toString())==c.getCustomerid()){%>selected<%} %>><%=c.getCustomername() %></option>
			  <%} %>
			</select>
			&nbsp;
			结算类型：
			<select name="type" id="type">
			  <option value="0">请选择</option>
			  <%for(FinanceAuditTypeEnum fat : FinanceAuditTypeEnum.values()){ %>
			  	<option value="<%=fat.getValue() %>"<%if(Long.parseLong(request.getParameter("type")==null?"0":request.getParameter("type").toString())==fat.getValue()){%>selected<%} %>><%=fat.getText() %></option>
			  <%} %>
			</select>
			财务审核时间：
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
			<input type="submit" value="查询" class="input_button2">
		</form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
    <div style="width:100%; overflow-x:scroll">
    <table width="2000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tbody><tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   	<td width="120" align="center" valign="middle" bgcolor="#f3f3f3">结算类型</td>
			<td width="150" align="center" valign="middle" bgcolor="#F3F3F3">财务审核时间</td>
			<td width="200" align="center" valign="middle" bgcolor="#F3F3F3">付款时间</td>
			<td width="200" align="center" valign="middle" bgcolor="#F3F3F3">交易流水号</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">订单数</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">应付货款</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">实付货款</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">欠付款</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">应退货款</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">实退货款</td>
			<td width="100" align="center" valign="middle" bgcolor="#F3F3F3">欠退货款</td>
			<td width="150" align="center" valign="middle" bgcolor="#F3F3F3">付款方式</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">备注</td>
			<td width="150" align="center" valign="middle" bgcolor="#F3F3F3">操作</td>
			</tr>
		
		
		<%if(financeAuditList!=null&&financeAuditList.size()>0)for(FinanceAudit fa : financeAuditList){ %>
			<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle" bgcolor="#f3f3f3"><%for(FinanceAuditTypeEnum fat : FinanceAuditTypeEnum.values()){if(fa.getType()==fat.getValue()){ %><%=fat.getText() %><%}} %></td>
			<td align="center" valign="middle"><%=fa.getAuditdatetime() %></td>
			<td align="center" valign="middle"><%=fa.getPaydatetime() %></td>
			<td align="center" valign="middle"><%=fa.getPaynumber() %></td>
			<td align="center" valign="middle"><%=fa.getCwbcount() %></td>
			<%if(fa.getType()==FinanceAuditTypeEnum.TuiHuoKuanJieSuan_ChuZhang.getValue()){ %>
				<td align="center" valign="middle">0.00</td>
				<td align="center" valign="middle">0.00</td>
				<td align="center" valign="middle">0.00</td>
				<td align="center" valign="middle"><%=fa.getShouldpayamount() %></td>
				<td align="center" valign="middle"><%=fa.getPayamount() %></td>
				<td align="center" valign="middle"><%=fa.getShouldpayamount().subtract(fa.getPayamount()) %></td>
			<%}else{ %>
				<td align="center" valign="middle"><%=fa.getShouldpayamount() %></td>
				<td align="center" valign="middle"><%=fa.getPayamount() %></td>
				<td align="center" valign="middle"><%=fa.getShouldpayamount().subtract(fa.getPayamount()) %></td>
				<td align="center" valign="middle">0.00</td>
				<td align="center" valign="middle">0.00</td>
				<td align="center" valign="middle">0.00</td>
			<%} %>
			<td align="center" valign="middle"><%for(PaytypeEnum pe : PaytypeEnum.values()){if(fa.getPaytype()==pe.getValue()){ %><%=pe.getText()%><%}} %></td>
			<td align="center" valign="middle"><%=fa.getPayremark() %></td>
			<td align="center" valign="middle">
				<%if(fa.getShouldpayamount().subtract(fa.getPayamount()).compareTo(BigDecimal.ZERO)>0){ %>
				<input name="button2" type="button" class="input_button2" id="button2" value="付款" onclick='fukuan(<%=fa.getId()%>)'/>
				<%} %>
		    	<input name="button3" type="submit" class="input_button2" id="button3" value="导出" onclick="exportField(<%=fa.getId()%>);"/>
		    </td>
		  </tr>
		<%} %>
		</tbody>
		<form id="excelData" action="<%=request.getContextPath()%>/funds/financeCharge_excel">
		<input id="auditid" name="auditid" value="" type="hidden" />
		</form>
	</table>
	
    </div>
	</div>
	
	<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
			　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
					<option value="<%=i %>"><%=i %></option>
					<% } %>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	<%} %>
</div>
<script type="text/javascript">
function exportField(auditid){
	$("#btnval").attr("disabled","disabled");
	$("#btnval").val("请稍后……");
	$("#auditid").val(auditid);
	$("#excelData").submit();
}
</script>
</body>
</html>






