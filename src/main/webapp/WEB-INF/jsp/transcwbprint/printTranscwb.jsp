<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<String> list = (List<String>) request.getAttribute("cwbs");
	String cwbs = "";

	if (list.size() > 0) {
		for (String smtcd : list) {
			cwbs += smtcd + ",";
		}
	}
	if(cwbs.length()>0){
		cwbs = cwbs.substring(0,cwbs.length()-1);
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
function prn1_preview(cwbs) {	
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	var printStatus=LODOP.PREVIEW();
	if(printStatus>0){//打印成功
		updatePrintStatus(cwbs);
	}
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
	LODOP.PRINT_INIT("条码打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	/* LODOP.ADD_PRINT_HTM(60,0,740,1100,document.getElementById("form1").innerHTML); */
};
function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var cwb = cwbs.toString().split(",")[i];
		// 条码打印修改为双联 modify by vic.liang@pjbest.com 2016-08-31
		var top_first = i*80 + 8;//双联单第一个条码的高度
		var top_second = i*80 + 48;//双联单第二个条码的高度
		LODOP.ADD_PRINT_BARCODE(top_first+"mm","16mm","45mm","12mm","128Auto",cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.ADD_PRINT_BARCODE(top_second+"mm","16mm","45mm","12mm","128Auto",cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		//LODOP.NewPage(); 
	}
};	
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		//发一个ajax请求更新打印状态
    	updatePrintStatus('<%=cwbs%>');
	    //打印
	    prn1_print('<%=cwbs%>');
	    	
	}
}
//更新打印状态
function updatePrintStatus(cwbs){
	$.ajax({
	    type: "post",
	    async: false, 
	    url: "printTranscwb",
	    data:{transcwbs:cwbs},
        datatype: "json",
	    success: function (result) {//更新成功
	   		}
		});	
}

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="right_title">
		<div class="menucontant">
		<form id="form1">
			<table>
				<%for(String smtcd : list){ %>
				<tr >
				<td  id="printcwb<%=smtcd%>" name="printcwb<%=smtcd %>" colspan="2" ><%=smtcd %></td>
				</tr>
				<% }%>
			</table>
			</form>
		</div>
	</div>
</div>
			
<%if(cwbs==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=cwbs%>');">打印预览</a>
	<a href="javascript:nowprint();">直接打印</a>
	<a href="javascript:prn1_printA(<%=cwbs%>);">选择打印机</a>
	<a href="javascript:location.href='printList/1';">返回</a>
<%} %>
</body>
</html>