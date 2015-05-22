<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbOrder> cwborderlist = (List<CwbOrder>)request.getAttribute("cwborderlist");
String branchname = (String)request.getAttribute("branchname");
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
<title>出库交接单</title>
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
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs) {	
	CreateOneFormPage();	
	CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("出库交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TEXT(50,231,260,39,"出库交接单");
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};	                     
function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
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
};                 
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=cwbs.substring(0, cwbs.length()-1) %>');
	}
}

</script>
<body style="tab-interval: 21pt;">
<a href="javascript:prn1_preview('<%=cwbs.substring(0, cwbs.length()-1) %>')">打印预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="jsvascript:history.go(-1);">返回</a>
	<!--StartFragment-->
	<form id ="form1">
	<div class="Section0" style="layout-grid: 15.6000pt;">
	
		<table
			style="border-collapse: collapse; margin-left: 6.0500pt; mso-table-layout-alt: fixed; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt;">
			<tr>
				<td width=713 valign=top colspan=3
					style="width: 534.7500pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: 0.5000pt solid rgb(0, 0, 0); mso-border-left-alt: 0.5000pt solid rgb(0, 0, 0); border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: 0.5000pt solid rgb(0, 0, 0); mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
						接货站：<%=branchname %>&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;打印时间：<%=datetime %>&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;打印人：<%=usermap.get("realname") %> &#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;</span>
						<span style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
			</tr>
			<tr style="height: 690.8500pt;">
				<td width=713 valign=top colspan=3
					style="width: 534.7500pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: 0.5000pt solid rgb(0, 0, 0); mso-border-left-alt: 0.5000pt solid rgb(0, 0, 0); border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: none;; mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span
							style="font-size: 10.5000pt; font-family: 'Times New Roman';"><o:p></o:p></span>
					</p></td>
			</tr>
			<tr>
				<td width=713 valign=top colspan=3
					style="width: 534.7500pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: 0.5000pt solid rgb(0, 0, 0); mso-border-left-alt: 0.5000pt solid rgb(0, 0, 0); border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: none;; mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span
							style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">&#21512;&#35745;&#37329;&#39069;&#65306;</span><span
							style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=money %><font
							face="&#23435;&#20307;">&#20803;</font></span><span
							style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
			</tr>
			<tr>
				<td width=225 valign=top
					style="width: 168.7500pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: 0.5000pt solid rgb(0, 0, 0); mso-border-left-alt: 0.5000pt solid rgb(0, 0, 0); border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: none;; mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span
							style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">&#24211;&#25151;&#31614;&#23383;</span><span
							style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
				<td width=224 valign=top
					style="width: 168.0000pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: none;; mso-border-left-alt: none;; border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: 0.5000pt solid rgb(0, 0, 0); mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span
							style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">&#39550;&#39542;&#21592;&#31614;&#23383;</span><span
							style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
				<td width=264 valign=top
					style="width: 198.0000pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: none;; mso-border-left-alt: none;; border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: 0.5000pt solid rgb(0, 0, 0); mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span
							style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">&#25910;&#36135;&#26041;&#25509;&#25910;&#20154;&#31614;&#23383;</span><span
							style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
			</tr>
			<tr>
				<td width=713 valign=top colspan=3
					style="width: 534.7500pt; padding: 0.0000pt 5.4000pt 0.0000pt 5.4000pt; border-left: 0.5000pt solid rgb(0, 0, 0); mso-border-left-alt: 0.5000pt solid rgb(0, 0, 0); border-right: 0.5000pt solid rgb(0, 0, 0); mso-border-right-alt: 0.5000pt solid rgb(0, 0, 0); border-top: none;; mso-border-top-alt: 0.5000pt solid rgb(0, 0, 0); border-bottom: 0.5000pt solid rgb(0, 0, 0); mso-border-bottom-alt: 0.5000pt solid rgb(0, 0, 0);"><p
						class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
						<span style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
							请仔细核对上述内容，签字后即要承担相关责任和法律,此联一试两份，出、收货放各留一份，自行保存。
							</span><span
							style="font-size: 10.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><o:p></o:p></span>
					</p></td>
			</tr>
		</table>
		<p class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
			<span
				style="mso-spacerun: 'yes'; font-size: 10.5000pt; font-family: 'Times New Roman';"><o:p></o:p></span>
		</p>
		
	</div>
	</form>
	<a href="javascript:prn1_preview('<%=cwbs.substring(0, cwbs.length()-1) %>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_printA('<%=cwbs.substring(0, cwbs.length()-1) %>')">选择打印机</a>
	<!--EndFragment-->
</body>
</html>
