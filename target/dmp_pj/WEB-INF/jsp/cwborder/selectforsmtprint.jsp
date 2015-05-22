<%@page import="cn.explink.domain.ShangMenTuiCwbDetail"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Customer"%>
<%
	List<ShangMenTuiCwbDetail> smtlist = (List<ShangMenTuiCwbDetail>) request.getAttribute("smtlist");
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customerlist");
	String cwbs = "";

	if (smtlist.size() > 0) {
		for (ShangMenTuiCwbDetail smtcd : smtlist) {
			cwbs += smtcd.getCwb() + ",";
		}
	}
	if(cwbs.length()>0){
		cwbs = cwbs.substring(0,cwbs.length()-1);
	}
	
	Map usermap = (Map) session.getAttribute("usermap");
%>

<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>-----------------------包裹单------------------------</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview(cwbs) {	
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("上门退交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTM(60,0,740,1100,document.getElementById("form1").innerHTML);
};
function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var cwb = cwbs.toString().split(",")[i];
		var num;//行数 每两单1行
		var h;//第二页的高度
		if(i%2==0){	num = i;}
		if(i%8==0){ h=(parseInt(i)/8)*60;}
		LODOP.ADD_PRINT_BARCODE(document.getElementById("printcwb"+cwb).offsetTop+document.getElementById("printTable"+num).offsetTop+h+98,
				document.getElementById("printcwb"+cwb).offsetLeft+document.getElementById("TD"+cwb).offsetLeft+53,150,30,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
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
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="right_title">
		<div class="menucontant">
		<form id="form1">
			<%for(ShangMenTuiCwbDetail smtcd : smtlist){ %>
			
				<%if(smtlist.indexOf(smtcd)%2==0){ %>
				<table id="printTable<%=smtlist.indexOf(smtcd) %>" width="750"  border="0" cellspacing="1" cellpadding="10" >
					<tr>
				<%} %>
						<td id="TD<%=smtcd.getCwb() %>"  height="242" width="50%" valign="top" bgcolor="#FFFFFF"><table width="100%" border="1" style="font-size:12px" bordercolor="#CCCCCC" bordercolordark="#CCCCCC" cellspacing="0" cellpadding="3" >
							<tr>
								<td colspan="4" align="center" bgcolor="#ebf1b5">
									<strong><%for(Customer c : customerlist){if(c.getCustomerid()==smtcd.getCustomerid()){%> <%=c.getCustomername()%> <%}} %>
									<%if(smtcd.getPrinttime()!=null&&smtcd.getPrinttime().length()>0){ %>
									（补打）
									<%} %>
									</strong></td>
							</tr>
							<tr>
								<td colspan="4" align="center" bgcolor="#fafbfd"><%=smtcd.getEmaildate().substring(0, 4) %>年<%=smtcd.getEmaildate().substring(5, 7) %>月<%=smtcd.getEmaildate().substring(8,10) %>日上门退订单</td>
							</tr>
							<tr>
								<td colspan="4" align="center" bgcolor="#fafbfd"><%=request.getAttribute("companyName") %></td>
							</tr>
							<tr>
								<td rowspan="2" bgcolor="#fafbfd">客户<br/>信息</td>
								<td colspan="3" width="300px"><%=smtcd.getConsigneeaddress() %>邮编：<%=smtcd.getConsigneepostcode() %></td>
							</tr>
							<tr>
								<td><%=smtcd.getConsigneename() %></td>
								<td colspan="2">电话：<%=smtcd.getConsigneemobile()+" "+smtcd.getConsigneephone()%></td>
							</tr>
							<tr>
								<td bgcolor="#fafbfd">其他<br/>信息</td>
								<td ><%=usermap.get("branchname")==null?"-":usermap.get("branchname") %>发货</td>
								<td id="printcwb<%=smtcd.getCwb() %>" name="printcwb<%=smtcd.getCwb() %>" colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td rowspan="2" bgcolor="#fafbfd">货物<br/>信息</td>
								<td><%=smtcd.getBackcarname() %></td>
								<td bgcolor="#fafbfd">客户签字</td>
								<td>&nbsp;&nbsp;年&nbsp;&nbsp;月&nbsp;&nbsp;日</td>
							</tr>
							<tr>
								<td>应退：<%=smtcd.getPaybackfee() %>元</td>
								<td bgcolor="#fafbfd">小件员</td>
								<td>&nbsp;&nbsp;</td>
							</tr>
					</table></td>
				<%if(smtlist.size()%2==1&&smtlist.indexOf(smtcd)==(smtlist.size()-1)){ %>
				<td></td>
				<%} %>
				<%if(smtlist.indexOf(smtcd)%2==1||smtlist.indexOf(smtcd)==(smtlist.size()-1)){ %>
				</tr>
			</table>
				<%} }%>
				
			</form>
		</div>
	</div>
</div>
<div class="clear"></div>
			
<%if(cwbs==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=cwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_printA('<%=cwbs%>')">选择打印机</a>
<%} %>
</body>
</html>