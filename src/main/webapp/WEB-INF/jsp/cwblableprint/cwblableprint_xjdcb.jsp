<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
String logo=(String)request.getAttribute("logo");
String huizongcwb = "'";
String huizongcwbs = "";

if(cwbList.size()>0){
	for(CwbOrder co : cwbList){
		huizongcwbs += co.getCwb() + ",";
	}
}

huizongcwbs = huizongcwbs.substring(0,huizongcwbs.length()-1);

huizongcwbs = huizongcwb + huizongcwbs+"'";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8"/>
<title>新疆大晨报</title> 
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs) {
	CreateOneFormPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {		
	CreateOneFormPage(cwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs) {		
	CreateOneFormPage(cwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(cwbs){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";
	
	LODOP.PRINT_INIT("新疆大晨报面单打印预览");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	
//	var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
	//LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
	
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"120mm","128mm",strFormHtml);
		var cwb = cwbs.toString().split(",")[i];
		
		LODOP.ADD_PRINT_BARCODE("0mm","70mm","50mm","22mm","128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.NEWPAGE();
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print(<%=huizongcwbs%>);
	}
}
</script>
<style type="text/css" id="style1">
*{font-size:12px; margin:0; padding:0; line-height:20px;solid #CCC;}
.table_1{border-bottom:1px solid #CCC; background:#FFF;}
.table_1 h1{font-size:16px; font-weight:bold}
.table_1 td{padding:0px}
.table_2{}
.table_2 td{padding:0px; color:#C00; background:#f8f8f8}
 .border_r{border-right:1px solid #CCC}
.border_t{border-top:1px solid #ccc} 
</style>
</head>
	<body style="padding:10px">
		<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview(<%=huizongcwbs%>)">预览</a>
			 <table ><tr><td> 
			<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder co : cwbList){ %>
			<div id="printTable<%=cwbList.indexOf(co) %>"  width="300">
				<table width="100%" align=center  class="table_1" height="24mm"> 
					
					<tr>
				<td colspan="2"><img src="<%=request.getContextPath() %>/images/<%=logo %>" width="198" height="74px"/></td>
				</tr>
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1" height="40mm">
				<tr>
				<td colspan="3" bgcolor="#f8f8f8" ><h1>客户信息：</h1></td>
				</tr>
				<tr>
				<td width="50%" >收件人：<%=co.getConsigneename().split(" ")[0] %></td>
				<td width="30%"  valign="middle">手机/电话：<%=co.getConsigneemobile().length()==0?co.getConsigneephone():co.getConsigneemobile() %></td>
				<td height="60" valign="middle">邮编：<%=co.getConsigneepostcode().length()==0?"":co.getConsigneepostcode()%></td>
				</tr>
				<tr>
				<td height="65">公司名称：</td>
				<td colspan="2" line-height="20" >详细地址：<%=co.getConsigneeaddress().length()==0?"":co.getConsigneeaddress()%></td>
				</tr>
				</table>
				 <div style="border-bottom:2px dashed #CCCCCC"></div> 
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1" height="33mm">
				<tr>
				<td width="50%" height="60" class="border_r">商品名称：<%=co.getSendcarname().length()==0?"":co.getSendcarname()%></td>
				<td rowspan="2" valign="top" class="border_l">备注信息：<p><%=co.getCwbremark().length()==0?"":co.getCwbremark()%></p></td>
				</tr>
				<tr  >
				<td height="60" class="border_r">货物金额：<%=co.getReceivablefee() %>元</td>
				</tr>
				</table>
				 <div style="border-bottom:2px dashed #CCCCCC"></div> 
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" height="33mm">
				  <tr>
				    <td width="50%" height="65px" class="border_r" >客户签名：</td>
				    <td rowspan="3" valign="top" height="61px" width="50%" ><p>温馨提示：保留此包裹单底联代表您已签收，如有疑问请联系客服咨询！ </p>
				      <p>&nbsp;</p>
				      <p><br/>咨询电话：0991-8850000</p></td>
				  </tr>
				  <tr>
				    <td height="50" align="right" class="border_r">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日&nbsp;&nbsp;</td>
				  </tr>
				</table>

			<!-- <div style="height:6px; overflow:hidden;line-height:10px">--------------------------------------------------------------------------------------------</div> -->
			<%} %></div>
			 </td></tr></table> 
		<a href="javascript:nowprint()">直接打印</a>
		
		
	</body>
</html>





