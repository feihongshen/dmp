<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.print.template.PrintColumn"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%  int num=30;
	PrintTemplate printTemplate = (PrintTemplate) request.getAttribute("pt");
	List<Map<String, String>> listMap  = (List<Map<String, String>>) request.getAttribute("listMap");
	List<Backintowarehouse_print> backIntoprintList = (List<Backintowarehouse_print>) request.getAttribute("bPrints");
	 List<User> driverList = (List<User>)request.getAttribute("driverList");
	  List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
	  List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
	  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	  List<User> userList = (List<User>)request.getAttribute("userList");
	  Branch branch = request.getAttribute("branch")==null?new Branch():(Branch)request.getAttribute("branch");
	  int count=backIntoprintList.size()/num;
	  int isno=backIntoprintList.size()%num;
	  if(count==0){count=1;}
	  if(backIntoprintList.size()>num&&isno>0){count+=1;}
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>-----------------------退货出库交接单------------------------</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0
		CompanyName="北京易普联科信息技术有限公司" License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print() {	
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
/*  function prn1_printA(cwbs) {
	CreateOneFormPage(cwbs);
	//CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
}; 	 */
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("上门退交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
	var table="#table0";
	var height=$(table).height();
	var width=$(table).width();
	var top=30;height+=15;
	
	if(<%=backIntoprintList.size()%><5){height+=50;}
	LODOP.ADD_PRINT_TABLE(top,10,width/1.9,height,document.getElementById("form0").innerHTML);
	LODOP.NEWPAGE();
};


</script>
</head>
<body marginwidth="0" marginheight="0" style="font-size: 14px" onload="load()">
	<div>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="javascript:prn1_print()">直接打印</a>
	</div>
<div id="form0" align="center">
		<table  id="table0" width="100%" border="1" cellspacing="0" cellpadding="0"
				><tr>
				<td border="0" colspan="<%=printTemplate.getColumns().size()+1%>"><p align="center">
			<%
				String branchname = branch.getBranchname();
				out.print(branchname+"退货入库交接单");
			%></p></td></tr>
				<tr class="font_1">
					<td width="60px"  align="center" valign="middle" bgcolor="#eef6ff">序号</td>
					<% 
					for (PrintColumn printColumn:printTemplate.getColumns()) {
							%>
					<td align="center" valign="middle" bgcolor="#eef6ff" width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px" style="font-size: 9.5000pt;" ><%=printColumn.getColumnName()%></td>
					<%}%>
				</tr>
				<% int i=1;
				for (Map<String, String> map : listMap) { %>
				<tr>
				<td  width="60px" align="center" valign="middle"><%=i%></td>
				<% 
					for (PrintColumn printColumn:printTemplate.getColumns()) {
						%> 
			<td  align="center" valign="middle"  width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px" style="font-size: 9.5000pt;" ><%=map.get(printColumn.getField())%></td>
					<%}%>
				</tr>
				<%
				i++;} 
				%>
			</table>
	</div>
	<div>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="javascript:prn1_print()">直接打印</a>
	</div>

	
</body>
</html>