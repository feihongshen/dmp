<%@page import="cn.explink.enumutil.PrintTemplateOpertatetypeEnum"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="cn.explink.print.template.PrintColumn"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
PrintTemplate printTemplate = (PrintTemplate) request.getAttribute("template");


SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);

%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>测试交接单</title>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA"
	width=0 height=0>
	<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" 
	license="653717070728688778794958093190"></embed>
</object>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js"
	type="text/javascript"></script>
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
	LODOP.PRINT();	
};
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>测试交接单");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};	                     

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print();
	}
}
</script>
<body style="tab-interval: 21pt;">
	
	<form id="form1">
		<!--StartFragment-->
		<div class="Section0" style="layout-grid: 15.6000pt;">
			<table  border="0" cellspacing="0" cellpadding="0" >
				<tr style="height: 60px">
					<td colspan="6" align="right" style="border-left: none;border-right: none;border-top: none;border-bottom: none">
						<span style="font-family: '宋体'; font-size:25px;font-weight: 1000">
							<%=printTemplate.getCustomname() %>发货交接单确认表
							
						</span>
					</td>
				</tr>
				
				
	<tr style="height: 30px">
		
			<td style="border-right: none"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
									style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">下一站:</span><span
									style="font-size: 9.5000pt; font-family: 'Times New Roman';">
				<o:p></o:p>
			</span>&nbsp;</span></td>
			<!--显示下一站站名  -->
			<!-- <td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;">北京西站<span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td> -->
			
			<td style="border-right: none;border-left: none"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;">车牌号:<span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span>&nbsp;</span></td>
			<!--显示车牌号  -->
			<!-- <td ><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;">CF123456<span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td> -->
			<!-- 显示时间 -->
			<td style="border-left: none"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">时间:</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
			</span>&nbsp;
			</span></td>
		
			<%-- <td colspan="2"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=datetime %><span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td> --%>
	</tr>
	
	<tr>
		<td colspan="6" style="border-top-style: solid;border-top-color: black;border-top-width: 1px"><table  border="0" cellspacing="0" cellpadding="0">

			
			
			<tr>
				<td width="55px" style="font-size: 9.5000pt;">序号</td>
				<%for(int i=0;i<printTemplate.getShownum();i++){ 
					for(PrintColumn printColumn:printTemplate.getColumns()){ %>
					<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px" style="font-size: 9.5000pt;" ><%=printColumn.getColumnName() %></td>
					<%} %>
				<%} %>
			</tr>
			
				<tr>
				<td width="55px" style="font-size: 9.5000pt;">&nbsp;</td>
				<%for(int i=0;i<printTemplate.getShownum();i++){ 
					for(PrintColumn printColumn:printTemplate.getColumns()){%>
					<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px" style="font-size: 9.5000pt;" >&nbsp;</td>
					<%} %>
				<%} %>
				</tr>
				
				<tr>
					<td width="55px" style="font-size: 9.5000pt;">合计:</td>
					<%for(int i=0;i<printTemplate.getShownum();i++){ 
					for(PrintColumn printColumn:printTemplate.getColumns()){%>
					<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px" style="font-size: 9.5000pt;" >&nbsp;</td>
					<%} %>
				<%} %>
				</tr>
				
		</table></td>
		</tr>
	
	
	
	<tr style="height: 30px">
		<td colspan="2" style="border-top-style: solid;border-top-color: black;border-top-width: 1px"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">分拣交接负责人:</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
			
		<td style="font-size: 9.5000pt;border-top-style: solid;border-top-color: black;border-top-width: 1px" >时间：
		</span>&nbsp;</span>
		</td>
		
	</tr>
	<tr style="height: 30px">
		<td  colspan="2" style="border-top-style: solid;border-top-color: black;border-top-width: 1px" ><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">司机:</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span>&nbsp;</span></td>
		
		<td style="font-size: 9.5000pt;border-top-style: solid;border-top-color: black;border-top-width: 1px">时间：
		</span></span>
		</td>
		
	</tr>
	<tr style="height: 30px;">
		<td colspan="2" style="border-top-style: solid;border-top-color: black;border-top-width: 1px" ><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">站点提货人</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>	
		<td style="font-size: 9.5000pt;border-top-style: solid;border-top-color: black;border-top-width: 1px">时间：
		</span>&nbsp;</span>
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
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">打印预览</a>
	<a href="<%=request.getContextPath()%>/printtemplate/list/1" >返回</a>
</body>
</html>
 

