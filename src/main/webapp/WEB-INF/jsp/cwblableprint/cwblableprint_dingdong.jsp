<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
String logo=(String)request.getAttribute("logo");

String huizongcwbs = "";
if(cwbList.size()>0){
	for(CwbOrder co : cwbList){
		huizongcwbs += co.getCwb() + ",";
	}
}
if(huizongcwbs.length()>0){
	huizongcwbs = huizongcwbs.substring(0,huizongcwbs.length()-1);
}
String customername="";String pinming="";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8"/>
<title>出库交接单</title> 
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
	LODOP.PRINT_INIT("标签打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	for (var i=0; i<cwbs.toString().split(",").length; i++) {
		var cwb = cwbs.toString().split(",")[i];
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("p"+cwb).innerHTML+"</body>";
		
		LODOP.ADD_PRINT_HTM(0,"5mm","RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		LODOP.ADD_PRINT_BARCODE("1mm","65mm",120,"15mm","128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.ADD_PRINT_BARCODE("95mm","65mm",120,"15mm","128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.ADD_PRINT_BARCODE("130mm","65mm",120,"15mm","128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.NEWPAGE();
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=huizongcwbs%>');
	}
}
</script>
<style type="text/css"id="style1">
*{font-size:12px; margin:0; padding:0;  font-family:"微软雅黑", "黑体"}
.table_dd{ border-left:1px solid #CCC}
.table_dd td{padding:5px; border-right:1px solid #CCC; border-bottom:1px solid #CCC; border-top:1px solid #CCC}
.table_dd h1{font-size:24px}
.table_normal{border:none}
.table_normal td{padding:2px; border:none }
.table_normal h1{font-size:24px}
.table_dd h2{font-size:16px}
.table_dd2{ border:2px solid #000}
.table_dd2 td{padding:5px; border:none;font-weight:bold}
</style>
</head>
	<body style="tab-interval: 21pt;">
		<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview('<%=huizongcwbs%>');">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
		<form id="form1">
			<%if(cwbList!=null&&cwbList.size()>0){for(CwbOrder co : cwbList){ 
				/*  if(co.getCwbordertypeid()==CwbOrderTypeIdEnum.Peisong.getValue()){
					pinming=co.getSendcarname();					
				}else{
					pinming=co.getBackcarname();
				} */
				%> 
		   <div style="width:330px;" id="p<%=co.getCwb()%>">
				<div style="height:65px;width:360px;border-bottom:5px solid transparent ">
					<table width="100%" border="0" cellspacing="5" cellpadding="0">
						<tr>
							<td width="80"  align="left" valign="middle"></td>
							<td width="150" align="left" valign="middle"><p>快递查询电话：</p>
								<p>010-5759 4816</p>
							</td>
						</tr>
					</table>
				</div>
				<div style="height:285px;width:360px;border-right:1px solid transparent;border-bottom:4px dashed transparent ">
				    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_dd">
						<tr>
							<td width="50%">
								<p>发货公司：<%if(customerlist!=null&&customerlist.size()>0)for(Customer cus : customerlist){if(co.getCustomerid()==cus.getCustomerid()){ customername=cus.getCustomername(); %><%=cus.getCustomername() %><%}} %></p>
								<p>售后电话：<%=co.getRemark1() %></p>
							</td>
							<td align="center"><h1><%=co.getCwbcity() %></h1></td>
						</tr>
						<tr>
							<td valign="top"><p>收&nbsp;件&nbsp;人&nbsp;：<%=co.getConsigneename() %></p></td>
							<td valign="top">电&nbsp;&nbsp;&nbsp;&nbsp;话：<%=co.getConsigneephone() %></td>
						</tr>
						<tr>
							<td colspan="2">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：<%=co.getConsigneeaddress() %></td>
						</tr>
						<tr>
							<td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_normal">
								<tr>
									<td width="70" valign="top">品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
									<td valign="top"><%=co.getSendcarname()%></td>
								</tr>
								<tr>
									<td valign="top">订&nbsp;单&nbsp;号&nbsp;：</td>
									<td valign="top"><%=co.getCwb() %></td>
								</tr>
								<tr>
									<td valign="top">制单时间：</td>
									<td valign="top"><%=DateTimeUtil.getNowDate() %></td>
								</tr>
							</table>
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_dd2">
								<tr>
									<td width="70" valign="top">代收金额：</td>
									<td valign="top"><%=co.getReceivablefee() %></td>
								</tr>
							</table>
							
							</td>
							<td valign="top">备注：<%=co.getCwbremark() %></td>
						</tr>
					</table>
				</div>
				<div style="height:133px;width:360px; border-right:1 px solid transparent;border-bottom:4px dashed transparent;border-top:4px dashed transparent ">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_dd">
								<tr>
									<td width="50%" align="center"><h3>北京叮咚配送网络</h3></td>
									<td rowspan="2"  align="center" valign="top">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_normal">
											<tr>
												<td>&nbsp;</td>
												<td >&nbsp;</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td >&nbsp;</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td >&nbsp;</td>
											</tr>
											<tr>
												<td >签&nbsp;收&nbsp;人&nbsp;：</td>
												<td >&nbsp;</td>
											</tr>
											<tr>
												<td>时&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
												<td></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_normal">
										<tr>
											<td width="70" valign="top">品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
											<td valign="top"><%=co.getSendcarname() %></td>
										</tr>
										<tr>
											<td valign="top">收&nbsp;件&nbsp;人&nbsp;：</td>
											<td valign="top"><%=co.getConsigneename() %></td>
										</tr>
										<tr>
											<td valign="top">代收金额：</td>
											<td valign="top"><%=co.getReceivablefee() %></td>
										</tr>
										<tr>
											<td valign="top">订&nbsp;单&nbsp;号&nbsp;：</td>
											<td valign="top"><%=co.getCwb() %></td>
										</tr>
									</table></td>
								</tr>
						</table>
				</div>
				<div style="height:120px;width:360px;border-right:1 px solid transparent;border-top:4px dashed transparent ">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_dd">
							<tr>
								<td width="50%" align="center"><h2><%=customername %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=co.getCwbcity() %></h2></td>
								<td rowspan="2" align="center" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_normal">
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td width="70">制单时间：</td>
											<td><%=DateTimeUtil.getNowDate() %></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_normal">
									<tr>
										<td width="70" valign="top">收&nbsp;件&nbsp;人&nbsp;：</td>
										<td valign="top"><%=co.getConsigneename() %></td>
									</tr>
									<tr>
										<td valign="top">品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
										<td valign="top"><%=co.getSendcarname() %></td>
									</tr>
									<tr>
										<td valign="top">订&nbsp;单&nbsp;号&nbsp;：</td>
										<td valign="top"><%=co.getCwb() %></td>
									</tr>
								</table></td>
							</tr>
					</table>		
				</div>
			</div>
			<div class="clear" height="2px" style="width:500px;border:2px dashed #0099CC"></div>
			<%}} %>
		</form>
		<a href="javascript:nowprint()">直接打印</a>
	</body>
</html>


