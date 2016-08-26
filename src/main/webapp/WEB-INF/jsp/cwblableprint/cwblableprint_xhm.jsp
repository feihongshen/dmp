<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
huizongcwbs = huizongcwb + huizongcwbs+"'";

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
	//LODOP.ADD_PRINT_HTM(1,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
	for (i = 0; i < <%=cwbList.size()%>; i++) {
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		var cwb = cwbs.toString().split(",")[i];
		LODOP.ADD_PRINT_BARCODE(145,206,180,70,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print(<%=huizongcwbs%>);
	}
}
</script>
<style type="text/css"id="style1">
*{font-size:12px; line-height:20px; margin:0; padding:0; font-family:方正姚体}
.table1 td{padding:9px 0; line-height:20px}
.table2 td{padding:4px 0}
</style>
</head>
	<body style="tab-interval: 21pt;">
		<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview(<%=huizongcwbs%>)">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
		<form id="form1">
			<!--StartFragment-->
			<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder co : cwbList){ %>
			<table id="printTable<%=cwbList.indexOf(co) %>" width="370" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<table width="100%" border="1" cellspacing="0" cellpadding="5" class="table1">
							<tr>
								<td colspan="3"><img src="<%=request.getContextPath() %>/images/<%=logo %>" width="131" height="37" style="float:left"/>
								<div style="float:right; width:200px; line-height:20px">&nbsp;
								<%if(co.getEmaildate()==null || co.getEmaildate().trim().length()<10 ){ %>
									北京小红帽
								<%}else{ %>
									<%=co.getEmaildate().substring(0, 10) %>&nbsp;北京小红帽
								<%} %>
								&nbsp;<%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){if(co.getDeliverybranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %><br/>邮编：<%=co.getConsigneepostcode() %></div>
								</td>
							</tr>
							<tr>
								<td width="49" align="center" height="80px" >信息</td>
								<td colspan="2" align="center" style="padding: 1px 0;height:80;font-size:14px;font-weight:bold"><%-- <p><%=co.getCwbprovince() %>……<%=co.getCwbcity() %>……<%=co.getCwbcounty() %>&nbsp;&nbsp;邮编：<%=co.getConsigneepostcode() %></p> --%>
									<p style="line-height: 12px;"><%=co.getConsigneeaddress() %><br/>客户：<%=co.getConsigneename() %>&nbsp;&nbsp;电话：<%=co.getConsigneemobile().length()==0?co.getConsigneephone():co.getConsigneemobile() %></p></td>
							</tr>
							<tr>
								<td rowspan="2" align="center">金额</td>
								<td width="205" align="center" style="font-size:14px;font-weight:bold">货品价值：<%=co.getCaramount() %>元</td>
								<td width="238" rowspan="2" style="font-size:14px;font-weight:bold;" align="center" id="printcwb<%=co.getCwb() %>" name="printcwb<%=co.getCwb() %>">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" height="36px" style="font-size:14px;font-weight:bold;padding:0px 0;">代收款：<%=co.getReceivablefee() %>元</td>
							</tr>
						</table>
						<table width="100%" border="1" cellspacing="0" cellpadding="5" style="margin-top:-2px">
							<tr>
								<td align="center">订单类型：<%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){if(co.getCwbordertypeid()==ct.getValue()){ %><%=ct.getText() %><%}} %></td>
								<td rowspan="2" align="left"><p>客户签名：　　　　</p>
									<p>时间：　　　　年　　月　　日</p></td>
							</tr>
							<tr>
								<td align="center" style="font-size:14px;font-weight:bold">件数：<%=co.getSendcarnum() %>（件）</td>
							</tr>
						</table>
						<div style="height:6px; overflow:hidden;line-height:10px">--------------------------------------------------------------------------------------------</div>
						<table width="100%" border="1" cellspacing="0" cellpadding="5" class="table2">
							<tr>
								<td><img src="<%=request.getContextPath() %>/images/<%=logo %>" width="160" height="37" /></td>
								<td style="font-size:14px;font-weight:bold">配送单号：<%=co.getCwb() %></td>
							</tr>
							<tr>
								<td style="line-height: 12px;font-size:14px;font-weight:bold" >客户：<%=co.getConsigneename() %><br/>电话：<%=co.getConsigneemobile().length()==0?co.getConsigneephone():co.getConsigneemobile() %></td>
								<td style="font-size:14px;font-weight:bold">应收金额：<%=co.getReceivablefee() %>元</td>
							</tr>
							<tr>
								<td><p>网址：www.bjxhm.com.cn</p>
									<p style="line-height: 12px;">北京订报热线：010-67756666-2</p></td>
								<td valign="top" style="line-height: 12px;">保留此单包裹单底代表您已经签收并认可我们的服务，如有任何疑问可联系客服咨询。</td>
							</tr>
							<tr>
								<td colspan="2">备注：<%=co.getRemark1() %></td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
			<div style="height:6px; overflow:hidden;line-height:10px">--------------------------------------------------------------------------------------------</div>
			<%} %>
			<!--EndFragment-->
		</form>
		<a href="javascript:nowprint()">直接打印</a>
		
		
	</body>
</html>


