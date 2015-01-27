<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<String> list = (List<String>) request.getAttribute("branchids");
	String branchids = "";

	if (list.size() > 0) {
		for (String smtcd : list) {
			branchids += smtcd + ",";
		}
	}
	if(branchids.length()>0){
		branchids = branchids.substring(0,branchids.length()-1);
	}
	
	Map usermap = (Map) session.getAttribute("usermap");
%>

<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>-----------------------包裹单------------------------</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(branchids) {	
	CreateOneFormPage();
	CreatePrintPage(branchids);
	LODOP.PREVIEW();	
};
function prn1_print(branchids) {		
	CreateOneFormPage();
	CreatePrintPage(branchids);
	LODOP.PRINT();	
};
function prn1_printA(branchids) {		
	CreateOneFormPage();
	CreatePrintPage(branchids);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("条码打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	/* LODOP.ADD_PRINT_HTM(60,0,740,1100,document.getElementById("form1").innerHTML); */
};
function CreatePrintPage(branchids) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	for(var i=0;i<branchids.toString().split(",").length;i++){
		var branchid = branchids.toString().split(",")[i];
		var code=branchid.toString().split("#");
		LODOP.ADD_PRINT_BARCODE(2,2,"5cm","3cm","128Auto",code[0]);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 60);
		LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
		LODOP.ADD_PRINT_TEXT("3cm","0cm","5cm",100,code[1].toString().split("&")[0]);
		LODOP.NewPage(); 
	}
};	
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=branchids%>');
	}
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="right_title">
		<div class="menucontant">
		<form id="form1">
			<table>
			<tr>
			<td>站点名称</td>
			</tr>
				<%for(String smtcd : list){
					String branch=smtcd.split("#")[1];
					%>
				<tr >
				<td><%=branch.split("&")[0] %></td>
				</tr>
				<% }%>
			</table>
			</form>
		</div>
	</div>
</div>
			
<%if(branchids==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=branchids%>');">打印预览</a>
	<a href="javascript:nowprint();">直接打印</a>
	<a href="javascript:prn1_printA(<%=branchids%>);">选择打印机</a>
	<a href="javascript:history.go(-1);">返回</a>
<%} %>
</body>
</html>