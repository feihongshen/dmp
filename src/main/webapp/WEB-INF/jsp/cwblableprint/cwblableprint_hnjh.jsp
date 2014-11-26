<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
String logo=(String)request.getAttribute("logo");
String huizongcwb = "'";
String huizongcwbs = "";

if(cwbList.size()>0){
	for(CwbOrder co : cwbList){
		huizongcwbs += co.getCwb() + ",";
	}
}
huizongcwbs = huizongcwb + huizongcwbs+"'";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8"/>
<title>出库交接单</title> 
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
	for (i = 0; i < <%=cwbList.size()%>; i++) {
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM("10%",0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		var cwb = cwbs.toString().split(",")[i];
		LODOP.ADD_PRINT_BARCODE("10%",285,150,70,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print(<%=huizongcwbs%>);
	}
}
</script>
<style type="text/css"id="style1">
*{font-size:12px; margin:0; padding:0; line-height:24px}
.table_1{border-bottom:1px solid #CCC; background:#FFF}
.table_1 h1{font-size:16px; font-weight:bold}
.table_1 td{padding:5px}
.table_2{}
.table_2 td{padding:5px; color:#C00; background:#f8f8f8}
.border_r{border-right:1px solid #CCC}
.border_t{border-top:1px solid #ccc}
</style>
</head>
	<body style="padding:10px">
		<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview(<%=huizongcwbs%>)">预览</a>
		&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
		<form id="form1">
			<!--StartFragment-->
			<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder co : cwbList){ %>
			<%-- <table id="printTable<%=cwbList.indexOf(co) %>" width="370" border="0" cellspacing="0" cellpadding="0"> --%>
			<div style="width:500px; height:500px; border:1px solid #000; background:#f8f8f8" id="printTable<%=cwbList.indexOf(co) %>">
<table width="100%"  style="border:1px solid #000;border-bottom:0" cellspacing="5" cellpadding="0" class="table_1">
	<tr>
		<td colspan="2"><p>供货商：<%if(customerlist!=null&&customerlist.size()>0)for(Customer cus : customerlist){if(co.getCustomerid()==cus.getCustomerid()){%><%=cus.getCustomername() %><%}} %></p></td>
		<td rowspan="2" align="center" valign="middle">&nbsp;</td>
		<td rowspan="2" align="center" valign="middle">&nbsp;</td>
			
	</tr>
	<tr>
		<td>始发地：<%if(branchlist!=null&&branchlist.size()>0){for(Branch b :branchlist){if(b.getBranchid()==Long.parseLong(co.getCarwarehouse())){%><%=b.getBranchname() %><%}}} %></td>
		<td>目的地：<%if(branchlist!=null&&branchlist.size()>0){for(Branch b :branchlist){if(b.getBranchid()==co.getNextbranchid()){%><%=b.getBranchname() %><%}}} %></td>
		<td>&nbsp;</td>
	</tr>
</table>
<table width="100%" style="border:1px solid #000;border-bottom:0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td colspan="3" bgcolor="#f8f8f8"><h1>客户信息：</h1></td>
		</tr>
	<tr>
		<td>收件人：<%=co.getConsigneename() %></td>
		<td align="center" valign="middle">电话：<%=co.getConsigneemobile() %></td>
		<td align="center" valign="middle">邮编：<%=co.getConsigneepostcode() %></td>
	</tr>
	<tr>
		<td colspan="3">地址：<%=co.getConsigneeaddress() %></td>
		</tr>
</table>
<table width="100%" style="border:1px solid #000;border-bottom:0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td width="30%" class="border_r">金额：<%=co.getReceivablefee() %></td>
		<td width="20%" class="border_r">包号：<%=co.getPackagecode() %></td>
		<td width="30%">客户签收：</td>
	</tr>
	<tr>
		<td class="border_r">付款方式：<%=PaytypeEnum.getByValue((int)co.getPaywayid()).getText() %></td>
		<td class="border_r">重量：<%=co.getCarrealweight() %></td>
		<td>签收时间：</td>
		
	</tr>
	</table>
<table width="100%" style="border:1px solid #000;border-bottom:0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td width="25%" class="border_r">品名：<%=co.getSendcarname() %></td>
		<td width="25%" rowspan="3" valign="top" class="border_r"><p>运单号：</p>
			<p><%=co.getTranscwb() %></p>
			</td>
		<td width="30%" rowspan="3" valign="top"><p>备注信息：</p>
			<p><%=co.getCwbremark() %></p></td>
	</tr>

	<tr>
		<td class="border_r">件数：<%=co.getSendcarnum() %></>件</td>
		</tr>
</table>
<table width="100%" style="border:1px solid #000" cellspacing="0" cellpadding="0" class="table_2">
	<tr>
		<td width="30%" height="40">温馨提示:您对此单的签收,代表您已收到货物,确认发货信息无误后签字!</td>
		</tr>
	</table>
</div>
			
			
			
			<div style="height:6px; overflow:hidden;line-height:10px">--------------------------------------------------------------------------------------------</div>
			<%} %>
			<!--EndFragment-->
		</form>
		<a href="javascript:nowprint()">直接打印</a>
		
		
	</body>
</html>


