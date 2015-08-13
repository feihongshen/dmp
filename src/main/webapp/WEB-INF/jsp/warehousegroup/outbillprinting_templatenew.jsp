<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="javax.swing.text.StyledEditorKit.ForegroundAction"%>
<%@page import="cn.explink.enumutil.PrintTemplateOpertatetypeEnum"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
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
//List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userlist = (List<User>)request.getAttribute("userlist");
//long nextbranchid = (Long) request.getAttribute("nextbranchid");
long deliverid = request.getAttribute("deliverid")==null?0:Long.parseLong(request.getAttribute("deliverid").toString());
Map<String,String> map=(Map<String,String>)request.getAttribute("map");

String localbranchname = (String )request.getAttribute("localbranchname");

long iscustomer = (Long)request.getAttribute("iscustomer");
String isback = request.getAttribute("isback")==null?"":(String)request.getAttribute("isback");
long islinghuo = request.getAttribute("islinghuo")==null?0:Long.parseLong(request.getAttribute("islinghuo").toString());;


SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);

Map usermap = (Map) session.getAttribute("usermap");
Map<Long,List<CwbOrder>>  mapBybranchid =(Map<Long,List<CwbOrder>>)request.getAttribute("mapBybranchid");
String huizongcwbs = (String)request.getAttribute("cwbs");
String type=(String)request.getAttribute("flowtype");
Long flowtype=Long.parseLong(type);
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
	LODOP.PREVIEW();	
};
function prn1_print() {		
	CreateOneFormPage();
	LODOP.PRINT();	
};
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	
	<%for(Long branchid:mapBybranchid.keySet()){
		List<CwbOrder> cwbs=mapBybranchid.get(branchid);
		if(cwbs.size()>0){
		%>
		LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form<%=branchid%>").innerHTML);	
		LODOP.NEWPAGEA();
	<%}}%>
	
};	                     
function setcreowg(){
	var operatetype = <%=OutwarehousegroupOperateEnum.ChuKu.getValue()%>;
	<%if(isback.equals("1")){%>
		operatetype = <%=OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue()%>;
	<%}else if(iscustomer==1){%>
		operatetype = <%=OutwarehousegroupOperateEnum.TuiGongYingShangChuKu.getValue()%>;
	<%}else if(islinghuo==1){%>
		operatetype = <%=OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()%>;
	<%}else if(flowtype>0&&flowtype==FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()){%>
		operatetype = <%=OutwarehousegroupOperateEnum.KuDuiKuChuKu.getValue()%>;
	<%}%>
	var baleno="";
	if($("#baleno").length>0){
		baleno=$("#baleno").text();
	}
	 $.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/warehousegroup/creowgnew",
			data : {"cwbs":"<%=huizongcwbs%>","operatetype":operatetype,"driverid":<%=deliverid%>,"baleno":baleno},
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					prn1_print();
				}
			},error: function(XMLHttpRequest, textStatus, errorThrown) {
	            alert(XMLHttpRequest.status+"---"+XMLHttpRequest.responseText);
	        }
		});
 
}

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		setcreowg();
	}
}
</script>
<body style="tab-interval: 21pt;">
<a href="javascript:nowprint()">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:prn1_preview()">预览</a>
&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<%
		if(!mapBybranchid.isEmpty()){
			for(Long branchid:mapBybranchid.keySet()){
				List<CwbOrder> cwbs=mapBybranchid.get(branchid);
				if(cwbs.size()>0){
				BigDecimal money = new BigDecimal(0);
				for(CwbOrder c : cwbs){
					money=money.add(c.getCaramount());
				}
				BigDecimal allMoney = new BigDecimal(0);
				long allSendcare = 0;

				for(int i=0;i<cwbs.size();i++){ 
					allMoney = allMoney.add(cwbs.get(i).getReceivablefee());
					allSendcare += cwbs.get(i).getSendcarnum();
					
				}
				%>
	<form id="form<%=branchid %>">
		<!--StartFragment-->
		<div class="Section0" style="layout-grid: 15.6000pt;">
			<table  border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="6" align="center">
						<span style="font-family: '宋体'; font-size: 10.5000pt">
							<%if(iscustomer==1){ %>
								<%=printTemplate.getCustomname() %>—退供货商出库交接单
							<%}else if(islinghuo==1){%>
								<%=printTemplate.getCustomname() %>—小件员领货清单
							<%}else{ %>
								<%if(printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue()||printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue()){ %>
									<%=printTemplate.getCustomname() %>—中转出站至<%for(Branch b : branchlist){if(branchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%}else if(printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue()||printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()){ %>
									<%=printTemplate.getCustomname() %>—站点出站至<%for(Branch b : branchlist){if(branchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%}else{ %>
									<%=printTemplate.getCustomname() %>—出库至<%for(Branch b : branchlist){if(branchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%} %>
							<%} %>
						</span>
					</td>
				</tr>
	<tr>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">时间</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		
		<%if(islinghuo==1){%>
			<td colspan="2"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=datetime %><span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td>
			<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
									style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">配送员：<%for(User user : userlist){if(deliverid==user.getUserid()){%><%=user.getRealname() %><%}} %></span><span
									style="font-size: 9.5000pt; font-family: 'Times New Roman';">
				<o:p></o:p>
			</span></span></td>
			<td colspan="2"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;">配送站点：<%=localbranchname %><span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td>
		<%}else{ %>
			<td colspan="1"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=datetime %><span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td>
			<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
									style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">站点名称</span><span
									style="font-size: 9.5000pt; font-family: 'Times New Roman';">
				<o:p></o:p>
			</span></span></td>
			<td colspan="1"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;font-size: 9.5000pt;"><%for(Branch b : branchlist){if(Long.parseLong(usermap.get("branchid").toString())==b.getBranchid()){%><%=b.getBranchname() %><%}} %><span
									style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
				<o:p></o:p>
			</span> </span></td>
			<c:if test="${!(baleno eq null)}">
			<td align="right"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;font-size: 9.5000pt;">包号:</span</td>
			<td align="left"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;font-size: 9.5000pt;" id="baleno">${baleno}</span></td>
		</c:if>
		<%} %>
		</tr>
	<tr>
		<td colspan="6"><table  border="1" cellspacing="0" cellpadding="0">
			
			<tr>
				<%for(int i=0;i<printTemplate.getShownum();i++){ 
					for(PrintColumn printColumn:printTemplate.getColumns()){%>
					<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px"  style="font-size: 9.5000pt;" align="center" ><%=printColumn.getColumnName() %></td>
					<%} %>
				<%} %>
				</tr>
				<%for(int i=0;i<cwbs.size();i++){							
					%>
					<%if(i%printTemplate.getShownum()==0){out.print("<tr style=\"height: 15.9000pt;\">");} %>
					<% for(PrintColumn printColumn:printTemplate.getColumns()){  %>
						<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px"  style="font-size: 9.5000pt;"  align="center">
								<%if(printColumn.getField().equals("opreator")){ if(map.containsKey(cwbs.get(i).getCwb())){%>
									<%=map.get(cwbs.get(i).getCwb()) %>
								<%}}else if(printColumn.getField().equals("customername")){for(Customer c : customerlist){if(cwbs.get(i).getCustomerid()==c.getCustomerid()){ %>
									<%=c.getCustomername() %>
								<%}}}else if(printColumn.getField().equals("cwbordertypeid")){for(CwbOrderTypeIdEnum cti : CwbOrderTypeIdEnum.values()){if(cwbs.get(i).getCwbordertypeid()==cti.getValue()){%>
									<%=cti.getText() %>
								<%}}}else if(printColumn.getField().equals("cwbremark")){ %>
									<%String remark = (String)PropertyUtils.getProperty(cwbs.get(i), printColumn.getField());
									if(remark!=null&&remark.length()>0&&remark.substring(0,1).equals(",")){
										remark = remark.substring(1, remark.length());
										}
									if(remark!=null&&remark.length()>0&&remark.substring(remark.length()-1,remark.length()).equals(",")){
										remark = remark.substring(0, remark.length()-1);
									}%>	
									<%=remark %>
								<%}else if(printColumn.getField().equals("paywayid")){for(PaytypeEnum pe : PaytypeEnum.values()){if(cwbs.get(i).getPaywayid()==pe.getValue()){%>
									<%=pe.getText() %>
								<%}}}else{ %>
									<%=PropertyUtils.getProperty(cwbs.get(i), printColumn.getField()) %>
								<%} %>
					</td>
					<%}%>
				<%if(i%printTemplate.getShownum()==printTemplate.getShownum()-1||i==cwbs.size()-1){out.print("</tr>");} %>
			<%} %>
		</table></td>
		</tr>
	<tr>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">合计</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=cwbs.size() %>单</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&#23435;&#20307;';">件数：</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=allSendcare %></span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&#23435;&#20307;';">代收金额：</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=allMoney %>&nbsp;&nbsp;&nbsp;元</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		</tr>
	<tr>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">出库人</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%if(islinghuo!=1){%><%=usermap.get("realname") %><%} %></span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">司机</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
		<o:p></o:p>
		：
		</span></span></td>
		<td>&nbsp;</td>
		<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">收货人：</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="6"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
								style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">异常说明：</span><span
								style="font-size: 9.5000pt; font-family: 'Times New Roman';">
			<o:p></o:p>
		</span></span></td>
		</tr>
</table>
 
			<p class=p0 style="margin-bottom: 0pt; margin-top: 0pt;">
				<span
					style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: 'Times New Roman';"><o:p></o:p></span>
			</p>
		</div>
		<!--EndFragment-->
	</form>	
	<%}}}%>

	<a href="javascript:nowprint()">直接打印</a>
</body>
</html>
 

