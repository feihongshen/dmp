<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.OutWarehouseGroup"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwborderlist = (List<CwbOrder>)request.getAttribute("cwborderlist");
String customername = (String)request.getAttribute("customername");
OutWarehouseGroup owg = (OutWarehouseGroup)request.getAttribute("owg");
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
BigDecimal money = new BigDecimal(0);;
for(CwbOrder c : cwborderlist){
	money=money.add(c.getCaramount());
}
Map usermap = (Map) session.getAttribute("usermap");

String cwbs = "";

if(cwborderlist.size()>0){
	for(CwbOrder co : cwborderlist){
		cwbs += co.getCwb() + ",";
	}
}
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退供货商出库交接单</title>
<!--[if gte mso 9]><xml><w:WordDocument><w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel><w:DisplayHorizontalDrawingGridEvery>0</w:DisplayHorizontalDrawingGridEvery><w:DisplayVerticalDrawingGridEvery>2</w:DisplayVerticalDrawingGridEvery><w:DocumentKind>DocumentNotSpecified</w:DocumentKind><w:DrawingGridVerticalSpacing>7.8</w:DrawingGridVerticalSpacing><w:View>Print</w:View><w:Compatibility></w:Compatibility><w:Zoom>0</w:Zoom></w:WordDocument></xml><![endif]-->
<style>
@font-face {
	font-family: "Times New Roman";
}

@font-face {
	font-family: "&#23435;&#20307;";
}

@font-face {
	font-family: "Symbol";
}

@font-face {
	font-family: "Arial";
}

@font-face {
	font-family: "&#40657;&#20307;";
}

@font-face {
	font-family: "Courier New";
}

@font-face {
	font-family: "Wingdings";
}

p.p0 {
	margin: 0pt;
	margin-bottom: 0.0001pt;
	margin-bottom: 0pt;
	margin-top: 0pt;
	text-align: justify;
	font-size: 10.5000pt;
	font-family: 'Times New Roman';
}

span.10 {
	font-size: 10.0000pt;
	font-family: 'Times New Roman';
}

p.p15 {
	margin-bottom: 0pt;
	margin-top: 0pt;
	border-top: none;;
	mso-border-top-alt: none;;
	border-right: none;;
	mso-border-right-alt: none;;
	border-bottom: none;;
	mso-border-bottom-alt: none;;
	border-left: none;;
	mso-border-left-alt: none;;
	padding: 1pt 4pt 1pt 4pt;
	text-align: justify;
	font-size: 9.0000pt;
	font-family: 'Times New Roman';
}

p.p16 {
	margin-bottom: 0pt;
	margin-top: 0pt;
	text-align: left;
	font-size: 9.0000pt;
	font-family: 'Times New Roman';
}

@page {
	mso-page-border-surround-header: no;
	mso-page-border-surround-footer: no;
}

@page Section0 {
	margin-top: 27.7500pt;
	margin-bottom: 34.9000pt;
	margin-left: 27.0000pt;
	margin-right: 32.3000pt;
	size: 595.3000pt 841.9000pt;
	layout-grid: 15.6000pt;
}

div.Section0 {
	page: Section0;
}
</style>

<object  id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>  
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object> 
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(owgid) {		
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	if('<%=owg.getPrinttime()%>'==""){
		setowgfengbao("<%=request.getContextPath()%>",owgid);
	}
	LODOP.PRINT();	
};
function prn1_printA(owgid) {		
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	if('<%=owg.getPrinttime()%>'==""){
		setowgfengbao("<%=request.getContextPath()%>",owgid);
	}
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("退供货商出库交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TEXT(50,231,260,39,"退供货商出库交接单");
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};	                     
/* function CreatePrintPage(cwbs) {
	LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var top = 65,left=50;
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		LODOP.ADD_PRINT_BARCODE(top,left,150,42,"128Auto", cwbs.toString().split(",")[i]);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		left += 170;
		if(left>600){
			left = 50;
			top += 50;
		}
	}
};	 */         


function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=owg.getId()%>');
	}
}
</script>
</head>
<body >
<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<form id="form1">
		<!--StartFragment-->
		<div>
			<h3 align="center">
			<%if(owg.getPrinttime().equals("")){ %>
				退供货商出库交接单
			<%}else{ %>
				退供货商出库交接单（补打）
			<%} %>
			</h3>				
			<h2 align="right">供货商：<%=customername %></h2>
			<hr />
			<table width="750px;">
			<tr>
				<td>订单类型</td>
				<td>订单号</td>
				<td style="width:200pt;">退货原因</td>
				<td>货物金额</td>
				<td>代收货款</td>
				<td>件数</td>
			</tr>
			
			<%for(CwbOrder co : cwborderlist){ %>
			<tr>
				<td>
					<%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){ if(co.getCwbordertypeid()==ct.getValue()){%>
					<%=ct.getText() %>
					<%}} %>
				</td>
				<td><%=co.getCwb() %></td>
				<td style="width:200pt;">
					<o:p><%=co.getBackreason() %>	</o:p>
				</td>
				<td><%=co.getCaramount() %></td>
				<td><%=co.getReceivablefee() %></td>
				<td><%=co.getSendcarnum() %></td>
			</tr>
			<%} %>
			</table>
			
			<hr />
			<table>
				<tr align="right">
					<td width="350">出库人：</td>
					<td width="350">时间：<%=owg.getPrinttime() %></td>
				</tr>
			</table>
			<hr />
		</div>
		<!--EndFragment-->
	</form>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_printA('<%=owg.getId()%>')">选择打印机</a>
</body>
</html>
