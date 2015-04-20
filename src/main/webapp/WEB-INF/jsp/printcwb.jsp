<%@page import="cn.explink.dao.SystemInstallDAO"%>
<%@page import="cn.explink.domain.SystemInstall"%>
<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<%
String [] printcwbTLF = (String[])request.getAttribute("printcwbTLF");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
function prn1_preview() {	
	CreateOneFormPage();
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();
	//$("#WORK_AREA",parent.document)[0].contentWindow.focusCwb();
	$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
};
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("订单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTM(<%=printcwbTLF[0] %>,<%=printcwbTLF[1] %>,740,1100,document.getElementById("form1").innerHTML);
};
function cwbscan(scancwb){
	if(scancwb.length>0){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/cwbscancwbbranch/"+scancwb,
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					$("#cwbcustomername").html(data.body.cwbcustomername);
					$("#cwb").html(data.body.cwbOrder.cwb);
					$("#branchcode").html(data.body.cwbbranchcode);
					$("#branchname").html(data.body.cwbbranchname);
					prn1_print();
				}
			}
		});
	}
}
</script>
</head>
<body <% if(request.getParameter("scancwb")!=null&&request.getParameter("scancwb").length()!=0){ %>onload="cwbscan('<%=request.getParameter("scancwb") %>');" <%} %>>
<% if(request.getParameter("scancwb")!=null){ %>

		
			<form id="form1">
			<%if(Integer.parseInt(printcwbTLF[2])>0){ %>
				<span id="cwbcustomername" style='font-family:"黑体"; font-size:<%=printcwbTLF[2] %>px; font-weight:bold;padding-right:15px; '></span>
			<%} if(Integer.parseInt(printcwbTLF[3])>0){%>
				<span id="cwb" style='font-family:"黑体"; font-size:<%=printcwbTLF[3] %>px; font-weight:bold;padding-right:15px; '></span>
			<%} %>
			<%if(Integer.parseInt(printcwbTLF[2])>0||Integer.parseInt(printcwbTLF[3])>0){ %>
			<br/>
			<%}if(Integer.parseInt(printcwbTLF[4])>0){ %>
				<span id="branchcode" style='font-family:Arial; font-size:<%=printcwbTLF[4] %>px; padding-right:15px'></span>
			<%} if(Integer.parseInt(printcwbTLF[5])>0){ %>
				<span id="branchname" style='font-family:"黑体"; font-size:<%=printcwbTLF[5] %>px'></span>
			<%} %>
			</form>
		
<%} %>
<a href="javascript:prn1_preview()">打印预览</a>
</body>

</html>
