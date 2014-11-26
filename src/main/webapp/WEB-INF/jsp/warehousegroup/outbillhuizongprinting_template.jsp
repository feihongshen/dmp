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
List<User> userlist = (List<User>)request.getAttribute("userlist");

List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
long nextbranchid = (Long) request.getAttribute("nextbranchid");
long deliverid = request.getAttribute("deliverid")==null?0:Long.parseLong(request.getAttribute("deliverid").toString());


String localbranchname = (String )request.getAttribute("localbranchname");

long iscustomer = (Long)request.getAttribute("iscustomer");
String isback = request.getAttribute("isback")==null?"":(String)request.getAttribute("isback");
long islinghuo = request.getAttribute("islinghuo")==null?0:Long.parseLong(request.getAttribute("islinghuo").toString());;

SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
Map usermap = (Map) session.getAttribute("usermap");
String huizongcwbs = (String)request.getAttribute("cwbs");
long cwbcount = 0,jianshucount = 0;

for(int i=0;i<cwbList.size();i++){ 
	cwbcount += Long.parseLong(cwbList.get(i).getString("cwbcount"));
	 for(PrintColumn printColumn:printTemplate.getColumns()){if(printColumn.getField().equals("sendcarnum")){ 
		jianshucount += cwbList.get(i).getLong("sendcarnum"); 
	}else if(printColumn.getField().equals("backcarnum")){ 
		jianshucount += cwbList.get(i).getLong("backcarnum"); 
	}} 
}

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
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>退供货商出库交接单");
	<%}else if(islinghuo==1){%>
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>小件员领货清单");
	<%}else{ %>
		LODOP.PRINT_INIT("<%=printTemplate.getCustomname() %>出库至<%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单");
	<%} %>
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	<%-- <%if(iscustomer==1){ %>
		LODOP.ADD_PRINT_TEXT(50,231,260,39,"<%=printTemplate.getCustomname() %>退供货商出库交接单");
	<%}else if(islinghuo==1){%>
		LODOP.ADD_PRINT_TEXT(50,231,260,39,"<%=printTemplate.getCustomname() %>小件员领货清单");
	<%}else{ %>
		LODOP.ADD_PRINT_TEXT(50,231,260,39,"<%=printTemplate.getCustomname() %>出库至<%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单");
	<%} %> --%>
	LODOP.ADD_PRINT_HTM(15,21,740,1100,document.getElementById("form1").innerHTML);
};	                     
function setcreowg(){
	var operatetype = <%=OutwarehousegroupOperateEnum.ChuKu.getValue()%>;
	<%if(isback.equals("1")){%>
		operatetype = <%=OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue()%>;
	<%}else if(iscustomer==1){%>
		operatetype = <%=OutwarehousegroupOperateEnum.TuiGongYingShangChuKu.getValue()%>;
	<%}else if(islinghuo==1){%>
		operatetype = <%=OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()%>;
	<%}%>
	
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/warehousegroup/creowg/<%=nextbranchid%>?driverid=<%=deliverid%>&customerid=0&operatetype="+operatetype,
		data : {cwbs:"<%=huizongcwbs%>"},
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				prn1_print();
			};
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status+"---"+XMLHttpRequest.responseText);
        }
	});
}
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		setcreowg();
	};
}
</script>
<body style="tab-interval: 21pt;">
<a href="javascript:nowprint();">直接打印</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:prn1_preview();">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1);">返回</a>
	<form id="form1">
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
									<%=printTemplate.getCustomname() %>—中转出站至<%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%}else if(printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue()||printTemplate.getOpertatetype()==PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()){ %>
									<%=printTemplate.getCustomname() %>—站点出站至<%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%}else{ %>
									<%=printTemplate.getCustomname() %>—出库至<%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %>清单
								<%} %>
							<%} %>
						</span>
					</td>
				</tr>
				<%if(iscustomer!=1&&islinghuo!=1){ %>
				<tr>
					<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
											style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">线路编码</span><span
											style="font-size: 9.5000pt; font-family: 'Times New Roman';">
						<o:p></o:p>
					</span></span></td>
					<td colspan="5"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=localbranchname %> - <%for(Branch b : branchlist){if(nextbranchid==b.getBranchid()){%><%=b.getBranchname() %><%}} %><span
											style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
						<o:p></o:p>
					</span> </span></td>
				</tr>
				<%} %>
				<tr>
					<%if(islinghuo==1){ %>
						<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
												style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">时间</span><span
												style="font-size: 9.5000pt; font-family: 'Times New Roman';">
							<o:p></o:p>
						</span></span></td>
						<td colspan="2"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=datetime %><span
												style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
							<o:p></o:p>
						</span> </span>
						</td>
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
						<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
												style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">时间</span><span
												style="font-size: 9.5000pt; font-family: 'Times New Roman';">
							<o:p></o:p>
						</span></span></td>
						<td colspan="5"><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt; font-size: 9.5000pt;"><%=datetime %><span
												style="font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">
							<o:p></o:p>
						</span> </span>
						</td>
					<%} %>
				</tr>
				
				<tr>
					<td colspan="6"><table  border="1" cellspacing="0" cellpadding="0">
					<tr>
						<%for(int i=0;i<printTemplate.getShownum();i++){
						for(PrintColumn printColumn:printTemplate.getColumns()){%>
							<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px"  style="font-size: 9.5000pt;" align="center"><%=printColumn.getColumnName() %></td>
						<%}}%>
					</tr>
						<%for(int i=0;i<cwbList.size();i++){ %>
						<%if(i%printTemplate.getShownum()==0){out.print("<tr style=\"height: 15.9000pt;\">");} %>
						<% for(PrintColumn printColumn:printTemplate.getColumns()){ %>
							<td width="<%=printColumn.getWidth()==""?30:Float.parseFloat(printColumn.getWidth()) %>*28 px"  style="font-size: 9.5000pt;"  align="center">
								<%=PropertyUtils.getProperty(cwbList.get(i), printColumn.getField()) %>
							</td>
						<%}if(i%printTemplate.getShownum()==printTemplate.getShownum()-1||i==cwbList.size()-1){out.print("</tr>");} %>
						<%} %>
						</table>
					</td>
				</tr>
				
				<tr>
					<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
											style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';">合计</span><span
											style="font-size: 9.5000pt; font-family: 'Times New Roman';">
						<o:p></o:p>
					</span></span></td>
					<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
											style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=cwbcount %>单</span><span
											style="font-size: 9.5000pt; font-family: 'Times New Roman';">
						<o:p></o:p>
					</span></span></td>
					<td><span class="p0" style="margin-bottom: 0pt; margin-top: 0pt;"><span
											style="mso-spacerun: 'yes'; font-size: 9.5000pt; font-family: '&amp;#23435;&amp;#20307;';"><%=jianshucount %>件</span><span
											style="font-size: 9.5000pt; font-family: 'Times New Roman';">
						<o:p></o:p>
					</span></span></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
	<a href="javascript:nowprint();">直接打印</a>
</body>
</html>
