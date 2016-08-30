<%@page import="cn.explink.util.StringUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Customer"%>
<%

	List<Branch> branchList = (List<Branch>) request
			.getAttribute("branchList");
	List<CwbOrder> clist = (List<CwbOrder>) request
			.getAttribute("clist");
	List<Customer> customerlist = (List<Customer>) request
			.getAttribute("customerlist");
	String cwbs = "";
	String oldcwbs = "";

	if (clist.size() > 0) {
		for (CwbOrder co : clist) {
			cwbs += co.getCwb() + ",";
			JSONObject json1 = new JSONObject();
			 if(co.getRemark4() != null && co.getRemark4().indexOf("originalOrderNum")>-1 ){
			     json1 = JSONObject.fromObject(co.getRemark4());
			     oldcwbs += json1.get("originalOrderNum") + "," ;
			 }
		}
	}
	if(cwbs.length()>0){
		cwbs = cwbs.substring(0,cwbs.length()-1);
	}
	if(oldcwbs.length()>0){
	    oldcwbs = oldcwbs.substring(0,oldcwbs.length()-1);
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
function prn1_preview(cwbs,oldCwbs) {	
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	CreatePrintPageOld(oldCwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs,oldCwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	CreatePrintPageOld(oldCwbs);
	LODOP.PRINT();	
};
function prn1_printA(cwbs,oldCwbs) {		
	CreateOneFormPage();
	CreatePrintPage(cwbs);
	CreatePrintPageOld(oldCwbs);
	LODOP.PRINTA(); 	
};	
function CreateOneFormPage(){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("上门退交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	LODOP.ADD_PRINT_HTM(0,0,740,1100,document.getElementById("form1").innerHTML);
};
function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var h=0;//第二页的高度
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var cwb = cwbs.toString().split(",")[i];
		if(i>0){h=h+546;}
		LODOP.ADD_PRINT_BARCODE(h+133,450,200,60,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	}
};	
function CreatePrintPageOld(oldcwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var h=0;//第二页的高度
	for(var i=0;i<oldcwbs.toString().split(",").length;i++){
		var cwb = oldcwbs.toString().split(",")[i];
		if(i>0){h=h+546;}
		LODOP.ADD_PRINT_BARCODE(h+250,70,200,60,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	}
};	
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=cwbs%>','<%=oldcwbs%>');
	}
}
</script>
</head>
<body marginwidth="0" marginheight="0">
<%if(cwbs==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=cwbs%>','<%=oldcwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_printA('<%=cwbs%>','<%=oldcwbs%>')">选择打印机</a>
<%} %>
		<form id="form1">
			<%for(CwbOrder co : clist){ %>
					<table id="printTable<%=clist.indexOf(co) %>"  height="527.5px" width="100%" border="1" cellspacing="0" cellpadding="0" class="table_dd" >
					<tr>
					<td id="TD<%=co.getCwb() %>"  height="300" width="50%" valign="top" bgcolor="#FFFFFF"><table width="100%" border="1" style="font-size:12px" bordercolor="#CCCCCC" bordercolordark="#CCCCCC" cellspacing="0" cellpadding="3" >
					<tr>
						<td colspan="3" align="center"><%=request.getAttribute("companyName") %></td>
					</tr>
					<tr>
					  <td colspan="3" align="center"><%for(Customer c : customerlist){if(c.getCustomerid()==co.getCustomerid()){%> <%=c.getCustomername()%> <%}} %>
									<%if(co.getPrinttime()!=null&&co.getPrinttime().length()>0){ %>
									（补打）
									<%} %></td>
				  </tr>
					<tr>
					  <td colspan="3" align="center">
					  上门退订单打印时间：<%if(co.getPrinttime() != null && co.getPrinttime().length() > 18){%><%=co.getPrinttime().substring(0, 4) %>年<%=co.getPrinttime().substring(5, 7) %>月<%=co.getPrinttime().substring(8,10) %>日 <%=co.getPrinttime().substring(11,co.getPrinttime().length())%><%} %></td>
				  </tr>
					<tr>
					  <td rowspan="4" align="center">客户信息</td>
					 					  <td>姓名：<%=co.getConsigneename() %></td>
					  <td width="50%">退货单号 :<%=co.getCwb() %></td>
				  </tr>
					<tr>
					  <td>电话：<%=co.getConsigneephone()%></td>
					  <td rowspan="3" id="printcwb<%=co.getCwb() %>" name="printcwb<%=co.getCwb() %>" ></td>
				  </tr>
					<tr>
					  <td>手机：<%=co.getConsigneemobileOfkf()%></td> <!-- 上门退打印不受用户权限限制全部显示明文 modify by vic.liang@pjbest.com 2016-08-29 -->
				  </tr>
					<tr>
					  <td height="40px">地址：<%=co.getConsigneeaddress() %></td>
				  </tr>
					<tr>
					 <% JSONObject json = new JSONObject();
					 if(co.getRemark4() != null && co.getRemark4().indexOf("originalOrderNum")>-1 ){
					     json = JSONObject.fromObject(co.getRemark4());
					 }
					  %>
					  <td colspan="2" valign="top" id="printcwbold<%=json.get("originalOrderNum") %>" name="printcwbold<%=json.get("originalOrderNum") %>"><p>原订单号：<%=StringUtil.nullConvertToEmptyString(json.get("originalOrderNum")) %></p>
				      <p></p></td>
					  <td align="center" height="135px">订单日期：<%=StringUtil.nullConvertToEmptyString(json.get("orderDate")) %><br/>发货仓库：北京电商自有大库
					  <br/>出库日期：<%=StringUtil.nullConvertToEmptyString(json.get("exDate")) %><br/>配送站点：
					  <%for(Branch b:branchList){ %>
					<%if(b.getBranchid() == co.getDeliverybranchid()){%>
					  <%=b.getBranchname()%>
					  <%  break;}} %></td>
				  </tr>
					<tr>
					 <% 
					 String partDescname="";
					 String packingListname="";
				   JSONObject jsonDetail = new JSONObject();
				   if(co.getRemark5() != null && co.getRemark5().indexOf("partDesc")>-1){
				       JSONArray jsonArr = JSONArray.fromObject(co.getRemark5());
					   if(jsonArr!= null && jsonArr.size() >0){
						   for(int i=0;i<jsonArr.size();i++){
							   jsonDetail = JSONObject.fromObject(jsonArr.get(0));
							   partDescname += JSONObject.fromObject(jsonArr.get(i)).getString("partDesc")+" ";
							   packingListname += JSONObject.fromObject(jsonArr.get(i)).getString("packingList")+" ";
						   }
					   }
				   }
				   %>
					  <td rowspan="6" align="left" valign="middle">货物信息</td>
					  <td colspan="2" height="40px" align="left" valign="top">货物名称：<%=StringUtil.nullConvertToEmptyString(jsonDetail.get("partDesc")) %>，
					  包装清单：<%=StringUtil.nullConvertToEmptyString(jsonDetail.get("packingList")) %></td>
				  </tr>
					<tr>
					  <td align="left" valign="top"><p>货物取回件数：<%=co.getBackcarnum() %></p></td>
					  <td align="left" valign="top"><p>应退金额：<%=co.getPaybackfee() %>元</p></td>
				  </tr>
					<tr>
					  <td colspan="2" align="left" valign="top"><p>货品描述：<%=StringUtil.nullConvertToEmptyString(jsonDetail.get("partDesc"))%></p></td>
				  </tr>
					<tr>
					  <td colspan="2" align="center" valign="top"><p>是否需要质检报告：
					    <input type="checkbox" name="checkbox" id="checkbox" <%if("Y".equals(jsonDetail.get("qcReportFlag"))){ %>checked="checked"<%} %>/>
					    <label for="checkbox"></label>
					  是 &nbsp;
					  <input type="checkbox" name="checkbox2" id="checkbox2" <%if("N".equals(jsonDetail.get("qcReportFlag"))){ %>checked="checked"<%} %>/>
					  否</p></td>
				  </tr>
					<tr>
					  <td colspan="2" align="center" valign="top"><p>是否需要原始发票：
					    <input type="checkbox" name="checkbox3" id="checkbox3" <%if("Y".equals(jsonDetail.get("invoiceCollectFlag"))){ %>checked="checked"<%} %>/>
				        <label for="checkbox3"></label>
				是 &nbsp;
				<input type="checkbox" name="checkbox3" id="checkbox4" <%if("N".equals(jsonDetail.get("invoiceCollectFlag"))){ %>checked="checked"<%} %>/>
				否</p></td>
				  </tr>
					<tr>
					  <td align="center" valign="top"><p>时间：</p></td>
					  <td align="center" valign="top"><p>客户签字：</p></td>
				  </tr>
				  </table>
				  </td>
				  </tr>
				</table>
				<%if(clist.indexOf(co)<clist.size()-1){ %>
				<br/>
				<%} %>
		<% }%>
			</form>
<div class="clear"></div>
			
<%if(cwbs==""){ %>
	<div>无未打印过的上门退订单</div>
<%}else{ %>
	<a href="javascript:prn1_preview('<%=cwbs%>','<%=oldcwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_printA('<%=cwbs%>','<%=oldcwbs%>')">选择打印机</a>
<%} %>
</body>
</html>