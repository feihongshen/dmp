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
	/* LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTML(h-70,0,"50mm","30mm",document.getElementById("form1").innerHTML);
	LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0); */
	LODOP.ADD_PRINT_TABLE(10,10,500,300,document.getElementById("form1").innerHTML)
	
};
function cwbscan(scancwb){
	
	if(scancwb.length>0){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/PDA/cwbscancwbbranchruku/"+scancwb,
			dataType:"json",
			success : function(data) {
				if(data.statuscode=="000000"){
					/* $("#cwbno").html(data.body.cwbOrder.opscwbid); */
					$("#branchname").html(data.body.cwbOrder.excelbranch);//站点中文名
					$("#transcwb").html(data.body.cwbOrder.transcwb);//运单号
					$("#branchcode").html(data.body.branchcode);//站点分拣编码
				var len=data.body.branchcode.length;
				var length=100-Math.floor(len/2)+3+"mm";
				var length2=45-len;
				if(len==4){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"21mm",
						"font-weight":"bolder",
						"display":"block",
						"line-height":"18mm",
						
					});
				}else if(len==5){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"19mm",
						"font-weight":"bolder",
						"display":"block",
						
					});
				}
				else if(len==6){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"16mm",
						"font-weight":"bolder",
						"display":"block",
						
					});
				}else if(len==7||len==8||len==9){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"15mm",
						"line-height":"9mm",
						"font-weight":"bolder",
						"display":"block",
						"word-wrap":"break-word",
					});
				}else if(len==10||len==11||len==12){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"12mm",
						"line-height":"9mm",
						"font-weight":"bolder",
						"display":"block",
						"word-wrap":"break-word",
					});
				}else if(len==13||len==14||len==15){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"9mm",
						"line-height":"9mm",
						"font-weight":"bolder",
						"display":"block",
						"word-wrap":"break-word",
					});
				}else if(len==16||len==17||len==18){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"8mm",
						"line-height":"6mm",
						"font-weight":"bolder",
						"line-height":"9mm",
						"display":"block",
						"word-wrap":"break-word",
					});
				}else if(len==19||len==20||len==21){
					$("#branchcode").css({
						"width":"40mm",
						"height":"18mm",
						"font-family":"黑体",
						"font":"bolder",
						"font-size":"7mm",
						"line-height":"6mm",
						"font-weight":"bolder",
						"line-height":"9mm",
						"display":"block",
						"word-wrap":"break-word",
					});
				}else{
					$("#branchcode").css({
						"line-height":"20mm",
						
					});
				}
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
		<table border='0' style="width:50mm;height:30mm; display: table; "  cellspacing="0" cellpadding="0" >
			<tr>
				<td style="margin: -20;padding: -20;display: table-cell;">
					<div style="font-size:3mm ;height: 4mm;line-height: 4mm;" align="center" id="branchname"/>
				</td>
			</tr>
			<tr style="width:50mm;height: 20mm;">
				<td style="padding:-20;margin:-20 ;width:50mm;height: 20mm;">
					<div style="width:50mm;height: 20mm;font-family:黑体; font: bolder;font-size:26mm;font-weight:bolder;" align="center" id="branchcode"/>
				</td>
			</tr>
			<tr>
				<td style="padding: -20;margin: -20;display: table-cell;">
					<div style="font: bolder;font-size: 3mm;height: 4mm;line-height: 4mm;" align="center" id="transcwb"/>
				</td>
			</tr>
			
			
		</table>
		</div>
<%} %>
<a href="javascript:prn1_preview('<%=scancwb %>')">打印预览</a>
</body>

</html>
