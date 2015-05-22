<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
String branchname = (String)request.getAttribute("branchname");
String exceldeliver = (String)request.getAttribute("exceldeliver");
String localbranchname = (String)request.getAttribute("localbranchname");
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
BigDecimal money = new BigDecimal(0);
int allnum = 0;
BigDecimal weight = new BigDecimal(0);
for(int i=0;i<cwbList.size();i++){
	money = money.add(cwbList.get(i).getReceivablefee());
	weight = weight.add(cwbList.get(i).getCarrealweight());
	allnum += cwbList.get(i).getSendcarnum()+cwbList.get(i).getBackcarnum();
}
Map usermap = (Map) session.getAttribute("usermap");
String cwbs = "";

 if(cwbList.size()>0){
	for(CwbOrder co : cwbList){
		cwbs += co.getCwb() + ",";
	}
} 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>小件员领货交接单</title>

<object  id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>  
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object> 
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();	
	//CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	setcreowg("<%=request.getContextPath()%>",<%=cwbList.get(0).getNextbranchid()%>,"<%=cwbs%>",<%=OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()%>,<%=cwbList.get(0).getDeliverid()%>,0);
	//CreatePrintPage(cwbs);
	<%-- if('<%=owg.getPrinttime()%>'==""){
		setowgfengbao("<%=request.getContextPath()%>",owgid);
	} --%>
	LODOP.PRINT();	
};
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("小件员领货交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TEXT(50,231,260,39,"小件员领货交接单");
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
	LODOP.SET_PRINT_STYLE("FontSize",10);
	//LODOP.SET_PRINT_STYLEA(0,"FontSize",10);

};
/* function CreatePrintPage(cwbs) {
	LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var top = 65,left=50;
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		LODOP.ADD_PRINT_BARCODE(top,left,150,42,"128Auto", cwbs.toString().split(",")[i]);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		left += 170;
		if(left>600){
			left = 50;
			top += 50;
		}
	}
};  */

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
</head>
<body style="border:1px solid #ccc; padding:10px; background:#fff">
<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:prn1_preview()">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<form id="form1">
		<!--StartFragment-->
		<div>
			<h3 align="center">
			<%-- <%if(owg.getPrinttime().equals("")){ %> --%>
				小件员领货清单
			<%-- <%}else{ %>
				小件员领货清单（补打）
			<%} %> --%>
			</h3>		
			<h3 align="left"><span style="float:right">配送站点：<%=localbranchname %> &nbsp;&nbsp;&nbsp;&nbsp;</span><span style="float:right">配送员：<%=exceldeliver %> &nbsp;&nbsp;&nbsp;&nbsp;</span>打印时间：<%=datetime %>&nbsp;&nbsp;&nbsp;&nbsp;</h3>
			
			<table width="100%" border="1" cellspacing="0" cellpadding="2">
			<tr>
				<td style="font-size: 9.5000pt;">供货商</td>
				<td style="font-size: 9.5000pt;">订单号</td>
				<td style="font-size: 9.5000pt;">小件员</td>
				<td style="width:200pt;font-size: 9.5000pt;">收货人信息</td>
				<td style="font-size: 9.5000pt;">代收货款</td>
			</tr>
			
			<%for(CwbOrder co : cwbList){ %>
			<tr>
				<!-- <td style="font-size: small;"> --><td style="font-size: 9.5000pt;">
				<%for(Customer c : customerlist){if(co.getCustomerid()==c.getCustomerid()){ %>
					<%=c.getCustomername() %>
				<%}} %>
				</td>
				<!-- <td style="font-size: small;"> --><td style="font-size: 9.5000pt;"><%=co.getCwb() %>
				</td>
				<%for(User u:deliverList){if(co.getDeliverid()==u.getUserid()){ %>
				<td style="font-size: 9.5000pt;"><%=u.getRealname() %></td>
				<%}} %>
				<!-- <td style="font-size: small;"> --><td style="font-size: 9.5000pt;">
					<!-- <o:p> -->
						<%=co.getConsigneeaddress() %><br/>
						<%=co.getConsigneename() %>	<%if(co.getConsigneephone()!=null){%><%=co.getConsigneephone() %><%} %>
					<!-- </o:p> -->
				</td>
				<!-- <td style="font-size: small;"> --><td style="font-size: 9.5000pt;"><%=co.getReceivablefee() %></td>
			</tr>
			<%} %>
			</table>
			
			<div align="center" style="font-size: 9.5000pt;">共<%=cwbList.size() %> 单，总重<%=weight %>千克，代收货款共现金<%=money %>。</div>
			<hr />
			<table>
				<tr>
					<td width="350" style="font-size: 9.5000pt;">出库人：</td>
					<td width="350" style="font-size: 9.5000pt;">入库人：</td>
				</tr>
				<tr>
					<td width="350" style="font-size: 9.5000pt;">时间：</td>
					<td width="350" style="font-size: 9.5000pt;">时间：</td>
				</tr>
			</table>
			<hr />
		</div>
		<!--EndFragment-->
	</form>
	<a href="javascript:nowprint()">直接打印</a>
</body>
</html>