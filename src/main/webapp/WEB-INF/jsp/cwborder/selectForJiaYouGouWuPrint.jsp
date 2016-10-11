<%@page import="cn.explink.domain.ShangMenTuiCwbDetail"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
List<ShangMenTuiCwbDetail> smtlist = (List<ShangMenTuiCwbDetail>) request.getAttribute("smtlist");
String cwbs = "";

if (smtlist.size() > 0) {
	for (ShangMenTuiCwbDetail smtcd : smtlist) {
		cwbs += smtcd.getCwb() + ",";
	}
}
if(cwbs.length()>0){
	cwbs = cwbs.substring(0,cwbs.length()-1);
}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>家有购物模板</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs) {	
	CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {		
	CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	LODOP.PRINT_INIT("家有购物取退货单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var cwb = cwbs.toString().split(",")[i];
		LODOP.ADD_PRINT_HTM("25%",0,"100%","50%","<style>*{margin:0;border:0;} </style><body>"+document.getElementById("print"+cwb).innerHTML+"</body>");
		LODOP.ADD_PRINT_BARCODE("25%",550,200,55,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
		LODOP.NEWPAGE();
	}
};	
	
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=cwbs%>');
	}
}
</script>

</head>
<body >
<%if(cwbs==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=cwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
<%} %>
<%if(smtlist!=null&&smtlist.size()>0){ for(ShangMenTuiCwbDetail smtcd:smtlist ){%>
<div id="print<%=smtcd.getCwb() %>" >
<table width="100%" border="0" cellspacing="0" cellpadding="5"  margin="0" padding="0" >
	<tbody>
		<tr height="30" style="background-color: rgb(255, 255, 255); ">
			<td align="left" valign="middle" bgcolor="#00b4ff">&nbsp;</td>
			<td valign="top" bgcolor="#00B4FF"><table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="34%" bgcolor="#00B4FF"><strong style="color:#FFF; font-family:'微软雅黑', '黑体'; font-style:italic; font-size:36px">家有购物取退货单</strong></td>
					</tr>
				</table></td>
			<td align="left" valign="middle" bgcolor="#00B4FF">&nbsp;</td>
		</tr>
		<tr height="30" style="background-color: rgb(255, 255, 255); ">
			<td width="5" align="center" valign="middle" bgcolor="#00B4FF">&nbsp;</td>
			<td valign="top" bgcolor="#FFFFFF">
				<table width="100%" border="0" cellspacing="0" cellpadding="10">
					<tr>
						<td width="50%" valign="top">
						<table width="100%" border="0" cellspacing="10" cellpadding="5">
								<tr>
									<td>订单类型：<%=CwbOrderTypeIdEnum.Shangmentui.getText()%></td>
								</tr>
								<tr>
									<td>客户姓名：<%=smtcd.getConsigneename() %></td>
								</tr>
								<tr>
									<td style="border-bottom:2px dashed #CCCCCC">收件地址：<%if(smtcd.getConsigneeaddress().length()>14){ %><%=smtcd.getConsigneeaddress().substring(0,14) %><%}else{ %><%=smtcd.getConsigneeaddress() %><%} %></td>
								</tr>
								
								<tr>
									<td style="border-bottom:2px dashed #CCCCCC"><%if(smtcd.getConsigneeaddress().length()>14&&smtcd.getConsigneeaddress().length()<=34){ %><%=smtcd.getConsigneeaddress().substring(14) %><%}else if(smtcd.getConsigneeaddress().length()>34){ %><%=smtcd.getConsigneeaddress().substring(14, 34) %><%}else{ %>&nbsp;<%} %></td>
								</tr>
								
								<tr>
									<%if(smtcd.getConsigneeaddress().length()>34){ %>
									<td style="border-bottom:2px dashed #CCCCCC"><%=smtcd.getConsigneeaddress().substring(34) %></td>
									<%}else{ %>
										<td>&nbsp;</td>
									<%} %>
								</tr>
								
								<tr>
									<td>商品代码：<%=smtcd.getRemark5() %></td>
								</tr>
								<tr>
									<td style="border-bottom:2px dashed #CCCCCC"><p>配送日期：</p></td>
								</tr>
							</table>
						</td>
						<td width="50%"  valign="top">
						<table width="100%" border="0" cellspacing="10" cellpadding="5">
							<tr>
								<td>订单编号：<%=smtcd.getCwb() %></td>
							</tr>
							<tr>
								<td>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机：<%=smtcd.getConsigneemobile() %></td>
							</tr>
							<tr>
								<td style="border-bottom:2px dashed #CCCCCC" >商品配套：<%if(smtcd.getRemark4().length()>=14){ %><%=smtcd.getRemark4().substring(0,14) %><%}else{%><%=smtcd.getRemark4() %><%}%></td>
							</tr>
							<tr>
								<td style="border-bottom:2px dashed #CCCCCC"><%if(smtcd.getRemark4().length()>14){ %><%=smtcd.getRemark4().substring(14) %><% }else {%>&nbsp;<% }%></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td style="border-bottom:2px dashed #CCCCCC"><p>客户签名：</p>
								</td>
							</tr>
						</table></td>
					</tr>
				</table></td>
			<td width="5" align="center" valign="middle" bgcolor="#00B4FF">&nbsp;</td>
		</tr>
		<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
			<td align="center" valign="middle" bgcolor="#00B4FF">&nbsp;</td>
			<td align="right" valign="middle" bgcolor="#00B4FF"><strong style="color:#FFF; font-family:'微软雅黑', '黑体'; font-style:italic; font-size:24px">上海宽容物流配送</strong></td>
			<td align="center" valign="middle" bgcolor="#00B4FF">&nbsp;</td>
		</tr>
	</tbody>
</table>
</div>
<%} }%>



</body>
</html>