<%@page import="cn.explink.dao.SystemInstallDAO"%>
<%@page import="cn.explink.domain.SystemInstall"%>
<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<%
String  scancwb = request.getAttribute("scancwb").toString();

%>
<!DOCTYPE html PUBLIC "-//W3C//Dsapn XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dsapn/xhtml1-transitional.dsapn">
<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
var h=55;
function prn1_preview(scancwb) {	
	CreateOneFormPage(scancwb);
	LODOP.PREVIEW();	
};
function prn1_print(scancwb) {		
	CreateOneFormPage(scancwb);
	LODOP.PRINT();
	$("#WORK_AREA",parent.document)[0].contentWindow.focusCwb();
};
function CreateOneFormPage(scancwb){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.NEWPAGE();
	LODOP.PRINT_INIT("订单打印");
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTML(h-70,0,"35.5mm","27.5mm",document.getElementById("form1").innerHTML);
	LODOP.ADD_PRINT_BARCODE(h,10,"30mm","5mm","128B", scancwb);
	LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
	
};
function cwbscan(scancwb){
	if(scancwb.length>0){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/cwbscancwbbranchnew/"+scancwb,
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					$("#companyname").html(data.body.print.companyname);
					$("#phone").html(data.body.print.phone);
					$("#website").html(data.body.print.website);
					$("#cwbno").html(data.body.cwbOrder.opscwbid);
					$("#branchname").html(data.body.cwbOrder.excelbranch);
					$("#username").html(data.body.username);
					$("#cwb").html(data.body.cwbOrder.cwb);
					$("#branchcode").html(data.body.branchcode);
					prn1_print(scancwb);
				}
			}
		});
	}
}
</script>
<style>
/*  Table{width:35.5mm;height:25.5;font-size:9px;border: 1px;border: solid;} */
.cwb{width:5mm;font-size:6px} 
</style>
</head>
<body  <% if(request.getParameter("scancwb")!=null&&request.getParameter("scancwb").length()!=0){ %>onload="cwbscan('<%=request.getParameter("scancwb") %>');" <%} %>>
<% if(request.getParameter("scancwb")!=null){ %>
			<div id="form1">     
		<table border='0' style="font-size: 10px;width:35.5mm;height:25.5; "  cellspacing="0" cellpadding="0">
			<tr><td><sapn><span id="companyname"></span></span>&nbsp;
				<sapn><span id="phone"/></span>
			<br>
				<sapn><span id="website"/></span>&nbsp;&nbsp;
				<sapn><span id="cwbno"/></span> 
			
			<table border="0" height="15">
			<tr>
				<td  style="font-size:9px" height="7" >
					<sapn><span id="branchname"/></span>&nbsp;
					<sapn><span id="username" /></span>&nbsp;<br>
					<sapn><span id="cwb"  class="cwb"/></span>
				</td>
				<td rowspan="2" valign="middle" align="left">
					<sapn  valign="bottom"><strong><div style="font: bolder;font-size: 25px;font-weight: 800;z-index: 3;letter-spacing: -2.5px" align="center" id="branchcode"/></strong></span>
				</td>
			<tr>
			</table>
				<sapn id="code"/>&nbsp;<br>
				</td></tr>
		</table>
		</div>
<%} %>
<a href="javascript:prn1_preview('<%=scancwb %>')">打印预览</a>
</body>

</html>
