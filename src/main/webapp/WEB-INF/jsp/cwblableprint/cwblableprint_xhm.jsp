<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
String logo=(String)request.getAttribute("logo");
String huizongcwb = "'";
String huizongcwbs ="";
String huizongTrans="'";
String huizongTransCwb="";
if(cwbList.size()>0){
	for(CwbOrder co : cwbList){
		huizongcwbs += co.getCwb() + ",";
		huizongTransCwb+=co.getTranscwb()+",";
	}
}
huizongTransCwb=huizongTrans+huizongTransCwb+"'";
huizongcwbs = huizongcwb + huizongcwbs+"'";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8" />
<title>京华热敏纸</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司"
		license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs,transcwb) {	
	CreateOneFormPage(cwbs,transcwb);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs,transcwb) {		
	CreateOneFormPage(cwbs,transcwb);
	LODOP.PRINT();	
};
function prn1_printA(cwbs,transcwb) {		
	CreateOneFormPage(cwbs,transcwb);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(cwbs,transcwb){
	LODOP=getLodop("<%=request.getContextPath()%>",
			document.getElementById('LODOP'), document.getElementById('LODOP_EM'));
		var strBodyStyle = "<style>"+ document.getElementById("style1").innerHTML + "</style>";
		LODOP.PRINT_INIT("标签打印");
		LODOP.SET_PRINT_STYLE("FontSize", 18);
		LODOP.SET_PRINT_STYLE("Bold", 1);
		//LODOP.ADD_PRINT_HTM(1,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		for (i = 0; i <<%=cwbList.size()%>; i++) {
			LODOP.NewPage();
			var strFormHtml = strBodyStyle + "<body>"+ document.getElementById("printTable" + i).innerHTML+ "</body>";
			LODOP.ADD_PRINT_HTM(0, 0, "RightMargin:0mm", "BottomMargin:0mm",strFormHtml);

			//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
			var cwb = cwbs.toString().split(",")[i];
			var trans=transcwb.toString().split(",")[i];
			LODOP.ADD_PRINT_BARCODE(67, 60,270, 58, "128Auto", cwb);
			LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
			if(trans.replace(/(^s*)|(s*$)/g, "").length ==0){
				trans=cwb;
			}
			LODOP.ADD_PRINT_BARCODE(448, 60, 270, 58, "128Auto", cwb);
			LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
		}
	};

	function nowprint() {
		var con = confirm("您确认要打印该页吗？");
		if (con == true) {
			prn1_print(<%=huizongcwbs%>,<%=huizongTransCwb%>
	);
		}
	}
</script>
<style type="text/css" id="style1">
.td {
	font-size: 11px
}

div {
	padding-top: 0px;
	padding-right: 0px;
	padding-bottom: 0px;
	padding-left: 0px;
}
</style>
</head>
<body>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview(<%=huizongcwbs%>,<%=huizongTransCwb%>)">预览</a>&nbsp;&nbsp;
	<a href="javascript:history.go(-1)">返回</a>
	<form id="form1">
		<c:forEach var="cwb" items="${cwbList }" varStatus="index">
			<div id="printTable${index.index}" style="width: 100mm; height: 175mm;">
				<div
					style="width: 94mm; height: 105mm; margin-left: auto; margin-right: auto; position: relative; border-collapse: collapse;">
					<div style="width: 94mm; height: 15.5mm">
						<table align="center" style="width: 94mm; height: 15mm; border-collapse: collapse" border="1">
							<tr>
								<td align="center" style="width: 54mm; height: 15mm"><img
									src="<%=request.getContextPath()%>/images/jietongkuaidi.png"
									style="width: 50mm; height: 15mm" /></td>
								<td align="center"
									style="width: 40mm; height: 15mm; line-height: 12px; margin: 0; padding: 0; font-size: 12px">${cwb.emaildate }</td>
							</tr>
						</table>
					</div>
					<div style="width: 94mm; height: 17mm">
						<table align="center" style="width: 94mm; height: 17mm; border-collapse: collapse" border="1">
							<tr>
								<td align="center"
									style="width: 10mm; height: 17mm; test-align: right; font-size: 15px; line-height: 20px; margin: 0; padding: 0;">订
									<br />单 <br />号
								</td>
								<td
									style="width: 80mm; height: 17mm; test-align: right; line-height: 12px; margin: 0; padding: 0;"></td>
							</tr>
						</table>
					</div>
					<div style="width: 94mm; height: 72.5mm">
						<table align="center" border="1"
							style="width: 94mm; height: 70mm; border-collapse: collapse; border-color: black;">
							<tr>
								<td colspan="3" width="100%" height="100%" style="padding: 0px;">
									<table align="center" border="1"
										style="width: 94mm; height: 22.5mm; border-collapse: collapse; border-color: black;">
										<tr>
											<td align="left" style="width: 45mm; height: 7.5mm">收件人：${cwb.consigneename }</td>
											<td align="left" style="width: 45mm; height: 7.5mm">电话：<c:if test="${not empty cwb.consigneemobile}">  ${cwb.consigneemobile }</c:if>  <c:if test="${empty cwb.consigneemobile}">  ${cwb.consigneephone }</c:if></td>
										</tr>
										<tr>
											<td align="left"
												style="width: 80mm; height: 15mm; font-size: 12px; WORD-WRAP: break-word" colspan="2">收件地址：${cwb.consigneeaddress }</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="5" style="padding: 0px;">
									<table align="center" border="1"
										style="width: 94mm; height: 15mm; border-collapse: collapse; border-color: black;">
										<tr>
											<td style="width: 47mm; height: 15mm;">寄件方：<c:forEach
												items="${customerlist }" var="customer">
												<c:if test="${customer.customerid==cwb.customerid }">${customer.customername }</c:if>
												</c:forEach>
											</td>
											<td style="width: 47mm; height: 15mm;font-size: 25px;font-weight:bold">配送站点:<c:forEach
											var="branch" items="${branchlist }">
											<c:if test="${cwb.deliverybranchid ==branch.branchid }">${branch.branchname }</c:if>
										</c:forEach></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="left"
									style="width: 20mm; height: 7.5mm; line-height: 12px; margin: 0; padding: 0;">件数：${cwb.sendcarnum }
								</td>
								<td align="left" style="width: 37mm; height: 7.5mm">代收款(元):${cwb.receivablefee}</td>
								<td align="left" style="width: 37mm; height: 7.5mm">到付款(元)：${cwb.shouldfare }</td>
							</tr>

							<tr>
								<td align="left"
									style="width: 54.5mm; height: 7.5mm; line-height: 12px; margin: 0; padding: 0;"
									colspan="2">商品：${cwb.cartype}</td>
								<td align="left" style="width: 39.5mm; height: 7.5mm">重量(kg):${cwb.carrealweight }</td>
							</tr>
							<tr>
								<td colspan="5" style="padding: 0px;">
									<table align="center" border="1"
										style="width: 94mm; height: 15mm; border-collapse: collapse; border-color: black;">
										<tr>
											<td style="width: 47mm; height: 20mm;">备注：</td>
											<td style="width: 47mm; height: 20mm;">客户签收栏：<br />时间：
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div
					style="width: 94mm; height: 6mm; margin-left: auto; margin-right: auto; position: relative;"></div>
				<div
					style="width: 94mm; height: 60mm; margin-left: auto; margin-right: auto; position: relative">
					<div style="width: 94mm; height: 17mm">
						<table align="center" style="width: 94mm; height: 17mm; border-collapse: collapse" border="1">
							<tr>
								<td align="center"
									style="width: 10mm; height: 17mm; test-align: right; font-size: 15px; line-height: 20px; margin: 0; padding: 0;">订
									<br />单 <br />号
								</td>
								<td
									style="width: 80mm; height: 17mm; test-align: right; line-height: 12px; margin: 0; padding: 0;"></td>
							</tr>
						</table>
					</div>
					<div style="width: 94mm; height: 43mm">
						<table align="center" border="1"
							style="width: 94mm; height: 43mm; border-collapse: collapse; border-color: black;">
							<tr>
								<td align="left"
									style="width: 20mm; height: 7.5mm; line-height: 12px; margin: 0; padding: 0;">件数：${cwb.sendcarnum }
								</td>
								<td align="left" style="width: 37mm; height: 7.5mm">代收款(元):${cwb.receivablefee}</td>
								<td align="left" style="width: 37mm; height: 7.5mm">到付款(元)：${cwb.shouldfare }</td>
							</tr>
							<tr>
								<td colspan="3" style="padding: 0px;">
									<table align="center" border="1" width="100%" height="100%"
										style="border-collapse: collapse; border-color: black;">
										<tr>
											<td align="left" style="width: 45mm; height: 7.5mm">收件人：${cwb.consigneename }</td>
											<td align="left" style="width: 45mm; height: 7.5mm">电话：<c:if test="${not empty cwb.consigneemobile}">  ${cwb.consigneemobile }</c:if>  <c:if test="${empty cwb.consigneemobile}">  ${cwb.consigneephone }</c:if></td>
										</tr>
										<tr>
											<td align="left" style="width: 80mm; height: 15mm; font-size: 12px; WORD-WRAP: break-word"
												colspan="2">收件地址：${cwb.consigneeaddress }</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td style="width: 94mm; height: 13mm;" colspan="3">商品：${cwb.cartype}</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</c:forEach>
	</form>
	<br />
	<a href="javascript:nowprint()">直接打印</a>
</body>
</html>