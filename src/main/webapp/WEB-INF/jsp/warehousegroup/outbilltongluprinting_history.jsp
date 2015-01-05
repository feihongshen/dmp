<%@page import="cn.explink.controller.WarehouseGroupPrintDto"%>
<%@page import="cn.explink.enumutil.PrintTemplateOpertatetypeEnum"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="cn.explink.print.template.PrintColumn"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
PrintTemplate printTemplate = (PrintTemplate) request.getAttribute("template");
List<JSONObject> cwbList = (List<JSONObject>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");

String localbranchname = (String )request.getAttribute("localbranchname");
String branchname = (String )request.getAttribute("branchname");
long operatetype = (Long)request.getAttribute("operatetype");



long iscustomer = (Long)request.getAttribute("iscustomer");
String isback = request.getAttribute("isback")==null?"":(String)request.getAttribute("isback");
long islinghuo = request.getAttribute("islinghuo")==null?0:Long.parseLong(request.getAttribute("islinghuo").toString());;
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
Map usermap = (Map) session.getAttribute("usermap");
String cwbs = (String)request.getAttribute("cwbs");

long cwbcount = 0,jianshucount = 0;
List<WarehouseGroupPrintDto> brDtos=(List<WarehouseGroupPrintDto>)request.getAttribute("printDtos");
List<CwbOrder> cwbOrderList=(List<CwbOrder>)request.getAttribute("cwbOrderList");
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>出库交接单</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function prn1_preview() {	
	CreateOneFormPage();	
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();	
};
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	<%if(iscustomer==1){ %>
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>退供货商出库交接单打印");
	<%}else if(operatetype==OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()){%>
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>领货清单打印");
	<%}else{ %>
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>出库至<%=branchname%>清单打印");
	<%} %>
	LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	//LODOP.SET_PRINT_MODE("FULL_HEIGHT_FOR_OVERFLOW",true); 
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	/* var width=$("#table1").width();
	if(width>600&&width<1800)
		{width=740;}
	if(width>1800)
	{width=$("#table1").width()/2;} */
	//LODOP.ADD_PRINT_HTM(15,21,1050,50,document.getElementById("form1").innerHTML);
	LODOP.ADD_PRINT_TABLE(10,10,1050,1000,document.getElementById("table1").innerHTML);
};	                     

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
<body style="tab-interval: 21pt;">
<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:prn1_preview();">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1);">返回</a>
	<form id="form1">
		<!--StartFragment-->
		<div class="Section0" style="layout-grid: 15.6000pt;">
			<table id="table1" border="1" cellspacing="0" cellpadding="0" style="width: 100%">
				<%-- <tr>
					<td colspan="<%=printTemplate.getColumns().size() %>" align="center" >
						<span style="font-family: '宋体'; font-size: 10.5000pt">
							//<%if(iscustomer==2){ %>
								站点: <%=localbranchname%>
							<%}%>
						</span>
					</td>
				</tr> --%>
				<tr>
					<td colspan="<%=printTemplate.getColumns().size() %>" >
						<table  border="1" cellspacing="0" cellpadding="0" style="width: 100%;">
							
							<tr>
								<td  colspan="<%=printTemplate.getColumns().size()+1%>" align="center">
									<span style="font-family: '宋体'; font-size: 10.5000pt">
						
											站点: <%=localbranchname%>
								
									</span>
								</td>
							</tr>
							
							<tr>
								<td width="4%"  style="font-size: 9.5000pt;" align="center">序号</td>
								<%for(int i=0;i<printTemplate.getShownum();i++){
									for(PrintColumn printColumn:printTemplate.getColumns()){%>
										<td width="auto"  style="font-size: 9.5000pt;" align="center"><%=printColumn.getColumnName() %></td>
									<%}}%>
							</tr>
							<%for(int i=0;i<cwbOrderList.size();i++){ %>
								<tr>
									<%for(int j=0;j<printTemplate.getColumns().size();j++){
											if(j==0){%>
											<td width="auto"  style="font-size: 9.5000pt;"  align="center"><%=i+1 %></td>
										<%
										}
										%>
										<td width="auto"  style="font-size: 9.5000pt;"  align="center">
											<%-- <%=PropertyUtils.getProperty(cwbList.get(i), printColumn.getField())%> --%>
											<%=PropertyUtils.getProperty(brDtos.get(i),printTemplate.getColumns().get(j).getField())%>
											<%-- <%=printTemplate.getColumns().get(j).getField()%> --%>
										</td>
									<%} %>
								</tr>
							<%} %>
								
						</table>
					</td>
				</tr>
			</table> 
			 <p class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
				<span
					style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: 'Times New Roman';"><o:p></o:p></span>
			</p>
		</div>
		<!--EndFragment-->
	</form>
	<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;<a href="javascript:prn1_preview()">打印预览</a>
</body>
</html>
