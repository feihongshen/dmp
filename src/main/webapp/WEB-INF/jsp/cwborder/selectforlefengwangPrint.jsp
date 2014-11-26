<%@page import="cn.explink.domain.lefengVo"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.ShangMenTuiCwbDetail"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%
		List<CwbOrder> cList = (List<CwbOrder>)request.getAttribute("clist");
 		List<Customer> customerlist =request.getAttribute("customer")==null?null:(List<Customer>)request.getAttribute("customer");
 		List<lefengVo> volist =request.getAttribute("Vo")==null?null:(List<lefengVo>)request.getAttribute("Vo");
		List<Branch> branchList = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
		long cwbordertypeid = request.getAttribute("cwbordertypeid")==null?0:(Long)request.getAttribute("cwbordertypeid");
		String huizongcwb = "'";
		String huizongcwbs = "";
		String printtime=request.getAttribute("printtime")==null?"":(String)request.getAttribute("printtime");
		if(cList.size()>0){
		for(CwbOrder co : cList){
		huizongcwbs += co.getCwb() + ",";
		}
		}
		huizongcwbs = huizongcwb + huizongcwbs+"'";
%>
<html>
<head>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs) {	
	CreateOneFormPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {		
	CreateOneFormPage(cwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs) {	
	
	CreateOneFormPage(cwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(cwbs){
	
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";
	
	LODOP.PRINT_INIT("标签打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	//LODOP.ADD_PRINT_HTM(1,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
	for (i = 0; i < <%=cList.size()%>; i++) {
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
		LODOP.SET_PRINT_PAGESIZE(2, 0,0,"A4");
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print(<%=huizongcwbs%>);
	}
}
</script>
<title>取货单打印面单</title>
<style type="text/css" id="style1">
*{font-size:12px; margin:0; padding:0; line-height:24px}
.table_1{border-bottom:0px solid #CCC; background:#FFF}
.table_1 h1{font-size:16px; font-weight:bold}
.table_1 td{padding:3px; border-right:1px solid #CCC; border-bottom:1px solid #CCC}
</style>
</head>

<body style="padding:10px">
<!--框架 -->
<div style="width:1200px; border:1px solid #CCC; background:#f8f8f8">

<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview(<%=huizongcwbs%>)">预览</a>
<%if(cList!=null&&cList.size()>0){for(CwbOrder c:cList){ %>
		<form id="printTable<%=cList.indexOf(c) %>">
<table   width="100%" border="0" cellspacing="0" cellpadding="5" class="table_1">
	<tr>
		<td width="100%" align="center" valign="middle"><p>乐峰网上门取货面单</p></td>
	</tr>
	<tr>
		<td colspan="2" align="center" bgcolor="#F4F4F4">上门换订单打印时间：<%=printtime%></td>
		</tr>
	</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td width="50%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1">
			<tr>
				<td width="100" rowspan="4" align="center" bgcolor="#F4F4F4" class="border_r">客户信息</td>
				<td width="250" valign="top"><p>订购人姓名：<%=c.getConsigneename() %></p></td>
				<td valign="top">邮编：<%=c.getConsigneepostcode() %></td>
			</tr>
			<tr>
				<td colspan="2">订购人电话：<%=c.getConsigneephone()%>&nbsp;&nbsp;<%=c.getConsigneemobile() %></td>
				</tr>
			<tr>
				<td colspan="2">取货地址：<%=c.getConsigneeaddress()%></td>
				</tr>
			<tr>
				<td colspan="2">取货日期：</td>
				</tr>
			<tr>
				<td rowspan="4" align="center" bgcolor="#F4F4F4" class="border_r">货物信息</td>
				<td colspan="2">换货取货单号：<%=c.getCwb() %></td>
				</tr>
			<tr>
				<td colspan="2">订单号：<%=c.getTranscwb() %></td>
				</tr>
			<tr>
				<td>应收运费金额：元</td>
				<td>应退款金额：<%=c.getPaybackfee() %> 元</td>
			</tr>
			<tr>
				<td colspan="2">是否上门退款： 否</td>
			</tr>
			</table></td>
		<td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1">
			<tr>
				<td width="100" rowspan="8" align="center" bgcolor="#F4F4F4" class="border_r">物流信息</td>
				<td colspan="2" valign="top"><p>物流商名称：<%for(Customer cm :customerlist){if(cm.getCustomerid()==c.getCustomerid()){ %><%=cm.getCustomername()%><%}}%></p></td>
				<td colspan="2" valign="top">物流商取货人：<%for(lefengVo vo:volist){if(vo.getCwb()==c.getCwb()){%><%=vo.getDeliveryname()%><%}} %></td>
			</tr>
			<tr>
				<td colspan="5" align="center" bgcolor="#F4F4F4">取货商品清单</td>
			</tr>
			<tr>
				<td width="100" align="center" rowspan="2">商品信息</td>
				<td width="180" align="center">SKU</td>
				<td width="200" align="center">规格</td>
				<td width="150" align="center">件数</td>
			</tr>
			 <tr>
				<td align="center">&nbsp;<%=c.getRemark1() %></td>
				<td align="center">&nbsp;<%=c.getCarsize() %></td>
				<td align="center">&nbsp;<%=c.getSendcarnum() %></td>
			</tr>
			<tr>
				<td align="center">退货原因</td>
				<td colspan="4"><%=c.getCustomercommand() %></td>
			</tr>
			<tr>
				<td align="center">商品名稱</td>
				<td colspan="4"><%=c.getSendcarname() %></td>
			</tr>
			<tr>
				<td colspan="5" align="left">换货人签字：</td>
			</tr> 
		</table></td>
	</tr>
</table></form><%} }%>
</div><a href="javascript:nowprint()">直接打印</a>
</body>
</html>