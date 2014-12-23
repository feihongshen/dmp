<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%  int num=30;

	List<Backintowarehouse_print> backIntoprintList = (List<Backintowarehouse_print>) request.getAttribute("bPrints");
	 List<User> driverList = (List<User>)request.getAttribute("driverList");
	  List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
	  List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
	  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
	  List<User> userList = (List<User>)request.getAttribute("userList");
	  int count=backIntoprintList.size()/num;
	  int isno=backIntoprintList.size()%num;
	  if(count==0){count=1;}
	  if(backIntoprintList.size()>num&&isno>0){count+=1;}
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>-----------------------退货出库交接单------------------------</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
	<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0
		CompanyName="北京易普联科信息技术有限公司" License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
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
/*  function prn1_printA(cwbs) {
	CreateOneFormPage(cwbs);
	//CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
}; 	 */
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("上门退交接单打印");
 	//LODOP.SET_PRINT_PAGESIZE(0,"840","800","");
 		var count=<%=count%>;
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
	for(var i=0;i<count;i++)
		{
		var table="#table"+i;
	var height=$(table).height();
	var width=$(table).width();
	LODOP.ADD_PRINT_HTM(30,10,width,height+15,document.getElementById("form"+i).innerHTML);
	LODOP.NEWPAGE();
		}
};


</script>
</head>
<body marginwidth="0" marginheight="0" style="font-size: 14px" onload="load()">
	<div>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="javascript:prn1_print()">直接打印</a>
	</div>
<div id="form0" align="center">
		<table id="table0" width="80%" border="1" cellspacing="0" cellpadding="0"
				>
				<tr class="font_1">
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">运单号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">上一站</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">扫描人</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">退货站入库时间</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">司机</td>
				</tr>
				<%
					int i = 0;
				int k=0;
					for (Backintowarehouse_print pv : backIntoprintList) {
						
						if(i%num==0&&i>0){
							k++;
							%>
							</table>
							</div>
							<div id=<%="form"+k %> align="center">
				<table id=<%="table"+k %> width="80%" border="1" cellspacing="0" cellpadding="0"
				>
				<tr class="font_1">
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">运单号</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">上一站</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">扫描人</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">退货站入库时间</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">司机</td>
				</tr>
							<%
							
						}
						i++;
				%>
				<tr>
					<td width="10%" align="center" valign="middle"><%=i%></td>
					<td align="center" valign="middle"><%=pv.getCwb()%></td>
					<td align="center" valign="middle"><%=pv.getTranscwb()%></td>
					<td align="center" valign="middle">
						<%
							for (Customer c : customerList) {
									if (c.getCustomerid() == pv.getCustomerid())
										out.print(c.getCustomername());
								}
						%>
					</td>
					<td align="center" valign="middle">
						<%
							for (Branch b : branchList) {
									if (b.getBranchid() == pv.getStartbranchid())
										out.print(b.getBranchname());
								}
						%>
					</td>
					<td align="center" valign="middle">
						<%
							for (User u : userList) {
									if (u.getUserid() == pv.getUserid())
										out.print(u.getRealname());
								}
						%>
					</td>
					<td align="center" valign="middle"><%=pv.getCreatetime()%></td>
					<td align="center" valign="middle">
						<%
							for (Reason r : reasonList) {
									if (r.getReasonid() == pv.getBackreasonid())
										out.print(r.getReasoncontent());
								}
						%>
					</td>
					<td align="center" valign="middle">
						<%
							for (User u : userList) {
									if (u.getUserid() == pv.getDriverid())
										out.print(u.getRealname());
								}
						%>
					</td>
				</tr>
				<%
					}
				%>
			</table>
	</div>
	<div>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="javascript:prn1_print()">直接打印</a>
	</div>

	
</body>
</html>