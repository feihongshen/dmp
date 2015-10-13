
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.StringUtil"%>
 <%List<CwbOrder> showDateList =(List<CwbOrder>) request.getAttribute("showDateList"); 
 int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());%> 
<%Page page_obj = (Page)request.getAttribute("page_obj"); %>
<%int page1 = Integer.parseInt(request.getAttribute("page").toString()) ; %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<script type="text/javascript">
function check(){
	if($("#date").val() == 1){
		window.location.href="<%=request.getContextPath()%>/monitor/dateShowExport/3";
		return true;
	}else{
		alert("没有数据，不能导出！");
		return false;
	}
}

</script>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		<input type ="button" id="back" value="返回" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/monitorhouse/back'"/>
		<input type ="button" id="btnval" value="导出excle" class="input_button2" onclick="exportField();"/>
		<font color="red">&nbsp;&nbsp;<%=request.getAttribute("flowTypeStr")==null?"":request.getAttribute("flowTypeStr")%></font>
		</td>
	</tr>
	
</table>
	</form>
	<form action="<%=request.getContextPath()%>/monitorhouse/dateShowExport" method="post" id="searchForm2">
	</form>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="5000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customername');" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >批次</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbordertypeid');" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbprovince');" >省份</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbcity');" >城市</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbcounty');" >地区</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneeaddress');" >地址</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneename');" >收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneemobile');" >收件人手机号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneephone');" >收件人电话</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneepostcode');" >收货邮编</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('signinman');" >签收人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('signintime');" >签收时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerwarehouseid');" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('inhouse');" >入库仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('startbranchname');" >当前站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('nextbranchname');" >下一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('excelbranch');" >配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('exceldeliver');" >小件员</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('carrealweight');" >发货重量（kg）</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('realweight');" >称重重量（kg）</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('goodsremark');" >货品备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >应收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('caramount');" >保价金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('paybackfee');" >应退金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('paytype');" >支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark1');" >备注1</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark2');" >备注2</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark3');" >备注3</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark4');" >备注4</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark5');" >备注5</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('newfollownotes');" >订单最新跟踪记录</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('returngoodsremark');" >退货备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('auditstate');" >退货是否审核</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('auditor');" >审核人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('audittime');" >审核时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflag');" >是否标记</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflagmen');" >标记人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflagtime');" >标记时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('instoreroomtime');" >入库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('outstoreroomtime');" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('inSitetime');" >到站时间</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('pickGoodstime');" >小件员领货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('sendSuccesstime');" >配送成功时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('gobacktime');" >反馈时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('goclasstime');" >归班时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('nowtime');" >最后更新时间</td>
		</tr>
		 <% for(int i=((page1-1)*10);i<page1*10;i++){ %>
		<% CwbOrder c = showDateList.get(i); %> 
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate()%></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=c.getCwbprovince()%></td>
					<td  align="center" valign="middle"><%=c.getCwbcity() %></td>
					<td  align="center" valign="middle"><%=c.getCwbcounty() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneemobile() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneephone() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneepostcode() %></td>
					<td  align="center" valign="middle"><%=c.getSigninman() %></td>
					<td  align="center" valign="middle"><%=c.getSignintime() %></td>
					<td  align="center" valign="middle"><%=c.getCustomerwarehousename()%></td>
					<td  align="center" valign="middle"><%=c.getCarwarehousename() %></td>
					<td  align="center" valign="middle"><%=c.getStartbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getExcelbranch() %></td>
					<td  align="center" valign="middle"><%=c.getExceldeliver() %></td>
					<td  align="center" valign="middle"><%=c.getCarrealweight() %></td>
					<td  align="center" valign="middle"><%=c.getRealweight() %></td>
					<td  align="center" valign="middle"><%=c.getGoodsremark()%></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getCaramount()%></td>
					<td  align="center" valign="middle"><%=c.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=c.getPaytype() %></td>
					<td  align="center" valign="middle"><%=c.getRemark1() %></td>
					<td  align="center" valign="middle"><%=c.getRemark2() %></td>
					<td  align="center" valign="middle"><%=c.getRemark3() %></td>
					<td  align="center" valign="middle"><%=c.getRemark4() %></td>
					<td  align="center" valign="middle"><%=c.getRemark5() %></td>
					<td  align="center" valign="middle"><%=c.getFlowordertypeMethod() %></td>
					<td  align="center" valign="middle"><%=c.getReturngoodsremark() %></td>
					<td  align="center" valign="middle"><%=c.getAuditstate()==1?"是":"否" %></td>
					<td  align="center" valign="middle"><%=c.getAuditor() %></td>
					<td  align="center" valign="middle"><%=c.getAudittime() %></td>
					<td  align="center" valign="middle"><%=c.getMarksflag()==1?"是":"否" %></td>
					<td  align="center" valign="middle"><%=c.getMarksflagmen()  %></td>
					<td  align="center" valign="middle"><%=c.getMarksflagtime()  %></td>
					<td  align="center" valign="middle"><%=c.getInstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
					<td  align="center" valign="middle"><%=c.getPickGoodstime()  %></td>
					<td  align="center" valign="middle"><%=c.getSendSuccesstime() %></td>
					<td  align="center" valign="middle"><%=c.getGobacktime() %></td>
					<td  align="center" valign="middle"><%=c.getGoclasstime()%></td>
					<td  align="center" valign="middle"><%=c.getNowtime() %></td>
				 </tr>
		 <%} %>
	</table>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#datetype").val(<%=request.getParameter("datetype")==null?"1":request.getParameter("datetype")%>);
</script>

<script type="text/javascript">

function exportField(){
		$("#searchForm2").submit();	
}

</script>
<!-- 标记ajax地址 -->
<input type="hidden" id="updateMack_url" value="<%=request.getContextPath()%>/order/updateMack" />
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />
</body>
</html>




