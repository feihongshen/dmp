<%@page import="net.sf.json.JSONArray"%>
<%@page import="cn.explink.b2c.saohuobang.CargoInfo"%>
<%@page import="cn.explink.pos.tools.JacksonMapper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.codehaus.jackson.type.TypeReference"%>
<%@page import="net.sf.json.JSON"%>
<%@page import="cn.explink.b2c.saohuobang.Saohuobanginfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Saohuobanginfo> saoList=(List)request.getAttribute("saolist")==null?new ArrayList<Saohuobanginfo>():(List)request.getAttribute("saolist");
String logo=(String)request.getAttribute("logo");
String huizongcwb = "'";
String huizongcwbs = "";

if(saoList.size()>0){
	for(Saohuobanginfo so : saoList){
		huizongcwbs += so.getCwb() + ",";
	}
}
huizongcwbs = huizongcwb + huizongcwbs+"'";
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>扫货帮标签打印</title> 
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
	for (i = 0; i < <%=saoList.size()%>; i++) {
		
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		var cwb = cwbs.toString().split(",")[i];
		//ADD_PRINT_BARCODE(Top,Left,Width,Height,BarCodeType,BarCodeValue);
		LODOP.ADD_PRINT_BARCODE(0,500,250,55,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.ADD_PRINT_BARCODE(535,500,250,55,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
		LODOP.NewPage();
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
*{margin:0px}
.table1 td{padding:0; line-height:18px; font-size:12px}
.table2{padding:0;font-size:16px}
</style>
</head>
	<body>
		<a href="javascript:nowprint()">直接打印</a>
		<a href="javascript:prn1_preview(<%=huizongcwbs%>)">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
		<form id="form1">
			<!--StartFragment-->
			<%if(saoList!=null&&saoList.size()>0)for(Saohuobanginfo so : saoList){ %>
			<div id="printTable<%=saoList.indexOf(so) %>" >
			<table width="100%" border="0" cellspacing="0" cellpadding="5" height="535px" >
				<tbody>
					<tr height="30" style="background-color: rgb(255, 255, 255); ">
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td valign="top"  >
						</td>
					</tr>
					<tr height="20"><td>&nbsp;&nbsp;&nbsp;</td><td></td> </tr>
					<tr height="30" style="background-color: rgb(255, 255, 255); ">
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr height="20"><td></td> </tr>
								<tr>
									<td width="50%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="1" class="table2">
											<tr>
												<td>商&nbsp;&nbsp;铺&nbsp;&nbsp;名：</td>
												<td><%=so.getStoreName() %></td>
											</tr>
											<tr>
												<td>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
												<td><%=so.getStoreAddress()%></td>
											</tr>
											<tr>
												<td>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
												<td><%=so.getStorePhone() %></td>
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>收&nbsp;&nbsp;货&nbsp;&nbsp;人：</td>
												<td><%=so.getConsigneename() %></td>
											</tr>
											<tr>
												<td  valign="top">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
												<td><%=so.getConsigneeaddress() %></td>
											</tr>
											<tr>
												<td>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
												<td><%=so.getConsigneemobile() %></td>
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>打印时间:</td>
												<td><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %></td>
											</tr>
											<tr>
												<td>下单时间:</td>
												<td><%=so.getDealTime() %></td>
											</tr>
											<tr>
												<td>送货时间:</td>
												<td><%=so.getCommand() %></td>
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>代收金额:</td>
												<td><%=so.getMoney() %>&nbsp;元</td>
											</tr>
											
										</table></td>
									<td align="left" valign="top">
									<%List<CargoInfo> jList=new ArrayList<CargoInfo>();
										try{
										if(so.getCargoInfo()!=null&&so.getCargoInfo().length()>0){
										  jList = JacksonMapper.getInstance().readValue(so.getCargoInfo(),new TypeReference<List<CargoInfo>>(){});
										}  
										}catch(Exception e){
										}
									%>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td align="right" height="230px" valign="top"><table width="360" border="1" cellspacing="0" cellpadding="3" class="table1">
														<tr>
															<td width="50" align="center">编号</td>
															<td align="center">品牌</td>
															<td align="center">货号</td>
														</tr>
														<%if(jList.size()>0){for(int i=0;i<jList.size();i++){ 
														 %>
															<tr>
																<td width="50" align="center"><%=i+1l %></td>
																<td align="center"><%=jList.get(i).getItemsBrandname() %></td>
																<td align="center"><%=jList.get(i).getNum()%></td>
															</tr>
														<%}}else{ %>
															<%for(int j=0;j<10;j++){ %>
															 <tr>
																<td width="50" align="center"><%=j+1l %></td>
																<td align="center">&nbsp;</td>
																<td align="center">&nbsp;</td>
															</tr>  
															<%} %>
														<%} %>
													</table></td>
											</tr>
											
										</table></td>
								</tr>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
			<!--绿色 -->
			<table width="100%" border="0" cellspacing="0" cellpadding="5" height="535px">
				<tbody>
					<tr height="30" style="background-color: rgb(255, 255, 255); ">
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td valign="top"  ></td>
					</tr>
					<tr height="20"><td>&nbsp;&nbsp;&nbsp;</td><td></td> </tr>
					<tr height="30" style="background-color: rgb(255, 255, 255); ">
						<td >&nbsp;&nbsp;&nbsp;</td>
						<td valign="top" bgcolor="#FFFFFF"><table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr>
									
									<td width="50%" valign="top" ><table width="100%" border="0" cellspacing="0" cellpadding="1" class="table2">
											<tr height="20"><td></td> </tr>
											<tr>
												<td>商&nbsp;&nbsp;铺&nbsp;&nbsp;名：</td>
												<td><%=so.getStoreName() %></td>
												
											</tr>
											<tr>
												<td>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
												<td><%=so.getStoreAddress() %></td>
											</tr>
											<tr>
												<td>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
												<td><%=so.getStorePhone() %></td>
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>收&nbsp;&nbsp;货&nbsp;&nbsp;人：</td>
												<td><%=so.getConsigneename() %></td>
											</tr>
											<tr>
												<td valign="top">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
												<td><%=so.getConsigneeaddress() %></td>
											</tr>
											<tr>
												<td>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
												<td><%=so.getConsigneemobile() %></td>
												
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>打印时间:</td>
												<td><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %></td>
											</tr>
											<tr>
												<td>下单时间:</td>
												<td><%=so.getDealTime() %></td>
											</tr>
											<tr>
												<td>送货时间:</td>
												<td><%=so.getCommand() %></td>
												
											</tr>
											<tr height="12">
												<td></td>
											</tr>
											<tr>
												<td>代收金额:</td>
												<td><%=so.getMoney() %>&nbsp;元</td>
											</tr>
											
										</table></td>
									<td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr height="20"><td></td> </tr>
											<tr>
												<td align="right" height="230px" valign="top"><table width="360" border="1" cellspacing="0" cellpadding="3"  class="table1">
														<tr>
															<td style="padding:0; line-height:21px; font-size:12px" width="50" align="center">编号</td>
															<td style="padding:0; line-height:21px; font-size:12px" align="center">品牌</td>
															<td style="padding:0; line-height:21px; font-size:12px" align="center">货号</td>
														</tr>
														<%if(jList.size()>0){for(int i=0;i<jList.size();i++){ %>
															<tr>
																<td width="50" align="center"><%=i+1l %></td>
																<td align="center"><%=jList.get(i).getItemsBrandname() %></td>
																<td align="center"><%=jList.get(i).getNum() %></td>
															</tr>
														<%}}else{ %>
															<%for(int j=0;j<10;j++){ %>
															<tr>
																<td width="50" align="center"><%=j+1l %></td>
																<td align="center">&nbsp;</td>
																<td align="center">&nbsp;</td>
															</tr> 
															<%} %>
														<%} %>
													</table></td>
											</tr>
										</table></td>
								</tr>
							</table></td>
					</tr>
					
				</tbody>
			</table>
			</div>
			<%}%>
			<!--EndFragment-->
		</form>
		<a href="javascript:nowprint()">直接打印</a>
	</body>
</html>


