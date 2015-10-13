<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cList = (List<CwbOrder>)request.getAttribute("cwbAllDetail");
String branchname = (String)request.getAttribute("branchname");
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
BigDecimal money = new BigDecimal(0);;
for(CwbOrder c : cList){
	money=money.add(c.getCaramount());
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<object  id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>  
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed> 
</object> 
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();	
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();	
};
function prn1_printA() {		
	CreateOneFormPage();
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("入站交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TEXT(50,231,260,39,"入站交接单");
	LODOP.ADD_PRINT_HTM(88,200,350,600,document.getElementById("form1").innerHTML);
};	                     
function prn2_preview() {	
	CreateTwoFormPage();	
	LODOP.PREVIEW();	
};
function prn2_manage() {	
	CreateTwoFormPage();
	LODOP.PRINT_SETUP();	
};	
function CreateTwoFormPage(){
	LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单二");
	LODOP.ADD_PRINT_RECT(70,27,634,242,0,1);
	LODOP.ADD_PRINT_TEXT(29,236,279,38,"页面内容改变布局打印");
	LODOP.SET_PRINT_STYLEA(2,"FontSize",18);
	LODOP.SET_PRINT_STYLEA(2,"Bold",1);
	LODOP.ADD_PRINT_HTM(88,40,321,185,document.getElementById("form1").innerHTML);
	LODOP.ADD_PRINT_HTM(87,355,285,187,document.getElementById("form2").innerHTML);
	LODOP.ADD_PRINT_TEXT(319,58,500,30,"注：其中《表单一》按显示大小，《表单二》在程序控制宽度(285px)内自适应调整");
};              
function prn3_preview(){
	LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_全页");
	LODOP.ADD_PRINT_HTM(20,40,700,900,document.documentElement.innerHTML);
	LODOP.PREVIEW();	
};	

</script>
</head>
<body onload="prn1_print()">
<form id="form1">
<table>
	<thead>
		<tr>
			<td>接货站：<%=branchname %></td>
			<td>打印时间：<%=datetime%></td>
			<td>打印人：</td>
			<td></td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>单号</td>
			<td>地址</td>
			<td>收件人</td>
			<td>金额</td>
		</tr>
		 
		<%for(CwbOrder c : cList){ %>
		<tr>
			<td><%=c.getCwb()%></td>
			<td><%=c.getConsigneeaddress()%></td>
			<td><%=c.getConsigneename() %></td>
			<td><%=c.getCaramount() %></td>
		</tr>
		<%} %>	
	</tbody>
	<tfoot>     
		<tr>
			<td>合计金额：<%=money%></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>请仔细核对上述内容，签字后即要承担相关责任和法律。</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td >此联一式两份，出、收货方各留一份，自行保存。</td>
			<td ></td>
			<td></td>
			<td></td>
		</tr>
	</tfoot>
</table>
</form>
<a href="javascript:prn1_preview()">打印预览</a>
<a href="javascript:prn1_print()">直接打印</a>   
<a href="javascript:prn1_printA()">选择打印机</a>打印。<br><br>
</body>
</html>