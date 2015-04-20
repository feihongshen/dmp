<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%
	Branch b = (Branch)request.getAttribute("branch");
	User u = (User)request.getAttribute("user");
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
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();	
	//CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs) {		
	CreateOneFormPage();
	//CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("归班审核存单");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_TEXT(50,231,260,39,"归班审核存单");
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
</head>
<body>
<form id="form1">
<!--StartFragment-->
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			历史归班详情</h1>
		<div id="box_form">
			
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
						<tr id="customertr" class=VwCtr style="display:">
							<td width="200">站点：<%=b.getBranchname() %></td>
							<td width="200">姓名：<%=u.getRealname() %></td>
							<td>审核日期：<%=request.getParameter("auditingtime") %></td>
						</tr>
						<tr><td colspan="3"><hr/></td></tr>
						<tr>
							<td><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table3">
								<tr >
									<td width="50%" align="right" valign="middle" >当日领货</td>
									<td align="center" valign="middle" ><strong><%=request.getParameter("nownumber") %></strong></td>
									
								</tr>
								<tr>
									<td width="10%" align="right" valign="middle" >遗留货物</td>
									<td align="center" valign="middle" ><strong><%=request.getParameter("yiliu") %></strong></td>
									
								</tr>
									<tr>
									<td width="10%" align="right" valign="middle" >历史未归班</td>
									<td align="center" valign="middle" ><strong><%=request.getParameter("lishiweishenhe") %></strong></td>
									
								</tr>
								<tr>
									<td width="10%" align="right" valign="middle" >暂不处理</td>
									<td align="center" valign="middle" ><strong><%=request.getParameter("zanbuchuli") %></strong></td>
									
								</tr>
								
								<tr>
									<td align="right" valign="middle" >总货数</td>
									<td align="center" valign="middle" ><strong><%=request.getParameter("SumCount") %></strong></td>
								</tr>
							</table>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3">归班结果：共计<%=request.getParameter("SumCount") %>单，审核<%=request.getParameter("SumReturnCount") %>单</td>
						</tr>
						<tr>
							<td colspan="3"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
								<tr class="font_1">
									<td width="20%" height="38" align="center" valign="middle" bgcolor="#eef6ff">归班状态</td>
									<td align="center" valign="middle" bgcolor="#eef6ff">单数</td>
									<td width="60%" align="center" valign="middle" bgcolor="#eef6ff">收款金额</td>
								</tr>
								<tr>
									<td width="10%" align="center" valign="middle" bgcolor="#eef6ff" >配送成功</td>
									<td align="center" valign="middle" ><%=request.getParameter("pscg") %></td>
									<td width="10%" align="center" >POS收款:<%=request.getParameter("pscgposamount") %>元　COD扫码收款:<%=request.getParameter("pscgcodposamount")==null?"0.00":request.getParameter("pscgcodposamount") %>元　其他收款:<%=request.getParameter("pscgamount") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >拒收</td>
									<td align="center" valign="middle" ><%=request.getParameter("tuihuo") %></td>
									<td align="center" ><%=request.getParameter("tuihuoamount") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >部分拒收</td>
									<td align="center" valign="middle" ><%=request.getParameter("bufentuihuo") %></td>
									<td align="center" >POS收款:<%=request.getParameter("bufentuihuoposamount") %>元　其他收款:<%=request.getParameter("bufentuihuoamount") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >滞留</td>
									<td align="center" valign="middle" ><%=request.getParameter("zhiliu")  %></td>
									<td align="center" ><%=request.getParameter("zhiliuamount")%>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退成功</td>
									<td align="center" valign="middle" ><%=request.getParameter("smtcg") %></td>
									<td align="center" ><%=request.getParameter("smtcgamount") %>元　实收运费：<%=request.getParameter("smtcgfare") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门退拒退</td>
									<td align="center" valign="middle" ><%=request.getParameter("smtjutui") %></td>
									<td align="center" ><%=request.getParameter("smtjutuiamount") %>元　实收运费：<%=request.getParameter("smtjutuifare") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >上门换成功</td>
									<td align="center" valign="middle" ><%=request.getParameter("smhcg") %></td>
									<td align="center" >POS收款:<%=request.getParameter("smhcgposamount") %>元　COD扫码收款:<%=request.getParameter("smhcgcodposamount")==null?"0.00":request.getParameter("smhcgcodposamount") %>元　其他收款:<%=request.getParameter("smhcgamount") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#eef6ff" >丢失破损</td>
									<td align="center" valign="middle" ><%=request.getParameter("diushi") %></td>
									<td align="center" ><%=request.getParameter("diushiamount") %>元</td>
								</tr>
								<tr>
									<td align="center" valign="middle" bgcolor="#f4f4f4" >合计</td>
									<td align="center" valign="middle" bgcolor="#f4f4f4" ><strong><%=request.getParameter("SumReturnCount")  %></strong></td>
									<td align="center" bgcolor="#f4f4f4" ><strong>pos收款:<%=request.getParameter("SumReturnCountPosAmount") %>元　COD扫码收款:<%=request.getParameter("SumReturnCountCodPosAmount") %>元　其他收款:<%=request.getParameter("SumReturnCountAmount") %>元　实收运费:<%=request.getParameter("SumSmtFare") %>元</strong></td>
								</tr>
							</table></td>
						</tr>
						<tr>
							<td>审核人签字</td>
							<td></td>
							<td>归班人签字</td>
						</tr>
					</table>
		</div>
	</div>
</div>
<!--EndFragment-->
</form>
<a href="javascript:prn1_preview()">打印预览</a>
<a href="javascript:nowprint()">直接打印</a>
<a href="javascript:prn1_printA()">选择打印机</a>
<!-- <script>$(function(){prn1_print()})</script> -->
</body>
</html>
