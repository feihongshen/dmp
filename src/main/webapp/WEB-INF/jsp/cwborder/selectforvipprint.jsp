<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.OrderGoods"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Customer"%>
<%
	List<Branch> branchList = (List<Branch>) request.getAttribute("branchList");
	List<CwbOrder> clist = (List<CwbOrder>) request.getAttribute("clist");
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customerlist");
	Map<String, List<OrderGoods>> mapOrderGoods= (Map<String, List<OrderGoods>>)request.getAttribute("mapOrderGoods");
	String cwbs = "";
	String cwbs2 = "";
	String oldcwbs = "";

	if (clist.size() > 0) {
		for (CwbOrder co : clist) {
			cwbs2 += co.getCwb() + "," + co.getCwb() + ",";
		}
	}

	if (clist.size() > 0) {
		for (CwbOrder co : clist) {
			cwbs += co.getCwb() + ",";
			JSONObject json1 = new JSONObject();
			if (co.getRemark4() != null && co.getRemark4().indexOf("originalOrderNum") > -1) {
				json1 = JSONObject.fromObject(co.getRemark4());
				oldcwbs += json1.get("originalOrderNum") + ",";
			}
		}
	}
	if (cwbs.length() > 0) {
		cwbs = cwbs.substring(0, cwbs.length() - 1);
	}
	if (cwbs2.length() > 0) {
		cwbs2 = cwbs2.substring(0, cwbs2.length() - 1);
	}
	if (oldcwbs.length() > 0) {
		oldcwbs = oldcwbs.substring(0, oldcwbs.length() - 1);
	}

	Map usermap = (Map) session.getAttribute("usermap");
%>

<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>-----------------------包裹单------------------------</title>
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
function prn1_preview(cwbs) {
	CreateOneFormPage(cwbs);
	//CreatePrintPage(cwbs);
	LODOP.PREVIEW();	
};
function prn1_print(cwbs) {	
	CreateOneFormPage(cwbs);
	//CreatePrintPage(cwbs);
	LODOP.PRINT();	
};
/*  function prn1_printA(cwbs) {
	CreateOneFormPage(cwbs);
	//CreatePrintPage(cwbs);
	LODOP.PRINTA(); 	
}; 	 */
function CreateOneFormPage(cwbs){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("上门退交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	var flag=document.getElementById("flag").checked;
	
	for( var i=0;i<<%=clist.size()%>;i++){
		var cwb = cwbs.toString().split(",")[i];
		var table="";
		var log="";
	if(flag)
		{ 	
			table="image"+i;
			LODOP.NEWPAGE();
			LODOP.ADD_PRINT_HTM(0,30,840,1100,document.getElementById(table).innerHTML);
			LODOP.ADD_PRINT_BARCODE(40,515,200,50,"128Auto", cwb);
			LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
		}
	else
		{	
			 table="noimage"+i;
			LODOP.NEWPAGE();
			LODOP.ADD_PRINT_HTM(0,30,840,1100,document.getElementById(table).innerHTML);
			LODOP.ADD_PRINT_BARCODE(40,515,200,50,"128Auto", cwb);
			LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
			LODOP.ADD_PRINT_BARCODE(40+535,515,200,50,"128Auto", cwb);
			LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	 }

	}
};
<%-- function CreatePrintPage(cwbs) {
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	var h=0;//第二页的高度
	var n=546;
	for(var i=0;i<cwbs.toString().split(",").length;i++){
		var cwb = cwbs.toString().split(",")[i];
		var flag=document.getElementById("flag").checked;
		if(flag)
			{n=1092;}
		if(i>0){h=h+n;}
		LODOP.ADD_PRINT_BARCODE(h+115,470,180,50,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 12);
	}
};	 --%>
function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		var flag=document.getElementById("flag").checked;
		if(flag)
		prn1_print('<%=cwbs%>');
		else
		prn1_print('<%=cwbs%>');
		}
	}
	function load(){
		document.getElementById("showImage").style.display='none';
		document.getElementById("noImage").style.display='block';
	}
	function show()
	{   
		var flag=document.getElementById("flag").checked;
		if(flag==false){
		document.getElementById("showImage").style.display='none';
		document.getElementById("noImage").style.display='block';
		}
		else {
			document.getElementById("showImage").style.display='block';
			document.getElementById("noImage").style.display='none';
		}
	}
</script>
</head>
<body marginwidth="0" marginheight="0" style="font-size: 14px" onload="load()">
	<%
		if (cwbs == "") {
	%>
	<div>无未打印过的上门退订单</div>
	<%
		} else {
	%>
	是否打印图片：<input type="checkbox" id="flag" name="flag" onchange="show()"/>
	
	<%
		}
	%>
	<div id="noImage">
	<a href="javascript:prn1_preview('<%=cwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
<%-- 	<a href="javascript:prn1_printA('<%=cwbs%>')">选择打印机</a>
 --%>	<div id="form1" align="center">
		<%	
			for (CwbOrder co : clist) {
				%>
				<div id="noimage<%=clist.indexOf(co)%>"><%
				for(int i=0;i<2;i++){
		%>
		
		<table id="noprintTable<%=clist.indexOf(co)%>" height="535px"
			width="700px" border="0"  cellspacing="0" cellpadding="0"
			 style="font-size: 14px">
			<tr>
				<td colspan="7" align="center" height="50"><font size="5px"><strong>唯品会退货单</strong></font></td>
			</tr>
			<tr>
				<td width="86" valign="bottom" height="30">会员名称:</td>
				<td width="86" valign="bottom"><%=co.getConsigneename()%></td>
				<td width="77" valign="bottom">联系方式:</td>
				<td width="81"  valign="bottom"><%=co.getConsigneemobileOfkf()%></td> <!-- 上门退打印不受用户权限限制全部显示明文 modify by vic.liang@pjbest.com 2016-08-29 -->
				<td colspan="3" valign="bottom">&nbsp;&nbsp;&nbsp;应收运费:&nbsp;&nbsp;&nbsp;&nbsp;<%=co.getShouldfare()%></td>
			</tr>
			<tr>
				<td height="16"  valign="bottom">取件地址：</td>
				<td colspan="5"  valign="bottom"><%=co.getConsigneeaddress()%></td>
			</tr>
		
			<tr>
				<td height="40" colspan="7" valign="top">
					<table height="100%"  style="font-size: 12px; text-align:center;"  width="700px"  border="1" cellspacing="0" cellpadding="0">
						 <tr  align="center" >
						    <td width="24" height="20">序号</td>
						    <td width="58">商品编码</td>
						    <td width="65">品牌名称</td>
						    <td width="75">商品名称</td>
						    <td width="49">规格</td>
						    <td width="52">申请数量</td>
						    <td width="52">实收数量</td>
						    <td width="130">退货原因</td>
						    <td width="52">特批数量</td>
						    <td width="79">特批备注</td>
						  </tr>
						<% 
						List<OrderGoods> orderGoods=mapOrderGoods.get(co.getCwb());
						if(orderGoods.size()>50)
						{%>
						<tr align="center" style="font-size: 14px">
							<td  colspan="10">商品数量较多，无法正常显示</td>
							</tr>
						<%}
						else{
						for(int j=0;j<orderGoods.size();j++){ %>
						<tr align="center" style="font-size: 10px">
							<td  width="24" height="8px"><%=j+1 %></td>
							<td width="58"><%=orderGoods.get(j).getGoods_code()%></td>
							<td width="65"><%=orderGoods.get(j).getGoods_brand() %></td>
							<td width="75"><%=orderGoods.get(j).getGoods_name() %></td>
							<td width="49"><%=orderGoods.get(j).getGoods_spec() %></td>
							<td width="52"><%=orderGoods.get(j).getGoods_num() %></td>
							<td width="52"></td>
							<td width="130"><%=orderGoods.get(j).getReturn_reason() %></td>
							<td width="52"></td>
							<td width="79"></td>
						</tr>
						<%} 
						}%>
					</table>
				</td>
			</tr>
			<tr>
				<td height="12px">退货仓地址:</td>
				<td colspan="4"><%=co.getRemark5() %>/<font size=5><%for (Customer c : customerlist) {if (c.getCustomerid() == co.getCustomerid()) {	%>
					<%=c.getCustomername()%> <%}}%></font></td>
				<td height="12px">建议最晚揽件时间:</td>
				<td colspan="2"><%=co.getCustomercommand()!=null&&co.getCustomercommand().contains("预约揽收时间")?(co.getCustomercommand().substring(co.getCustomercommand().indexOf("预约揽收时间:")+9)):"" %></td>
			</tr>
			<tr>
				<td height="16">取件承运商：</td>
				<td ><%=request.getAttribute("companyName") %>
				</td>
				 <td>打印日期：</td>
				<td colspan="1"><% SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); %><%=dateFormat.format(new Date()) %></td>
				 <td>取货人签字：</td>
				 <td width="86"></td>
				 <td width="148">取货时间：________</td>
			</tr>
		</table>
<%-- 		<%
			if (clist.indexOf(co) < clist.size() - 1) {
		%>
				<br>
		<%
			}
		%> --%>
		<%
			}%>
				</div>
				<%
			}
		%>
	</div>
	
	</div>
	<div id="showImage">
	<a href="javascript:prn1_preview('<%=cwbs%>')">打印预览</a>
	<a href="javascript:nowprint()">直接打印</a>
<%-- 	<a href="javascript:prn1_printA('<%=cwbs%>')">选择打印机</a>
 --%>	<div id="form2" align="center">
	
		<%	
			for (CwbOrder co : clist) {
		%><div id="image<%=clist.indexOf(co)%>"><%
		%>
			<table id="printTable<%=clist.indexOf(co)%>" height="535px"
			width="700px" border="0"  cellspacing="0" cellpadding="0"
			class="table_dd" style="font-size: 14px;text-align: justify;">
			<tr>
				<td colspan="7" align="center" height="50"><font size="5px" ><strong>唯品会退货单</strong></font></td>
			</tr>
			<tr>
				<td width="86" valign="bottom" height="30">会员名称:</td>
				<td width="86" valign="bottom"><%=co.getConsigneename()%></td>
				<td width="77" valign="bottom">联系方式:</td>
				<td  width="81"  valign="bottom"><%=co.getConsigneemobileOfkf()%></td>
				<td colspan="3" valign="bottom">&nbsp;&nbsp;&nbsp;应收运费:&nbsp;&nbsp;&nbsp;&nbsp;<%=co.getShouldfare()%></td>
			</tr>
			<tr>
				<td height="16"  valign="bottom">取件地址：</td>
				<td colspan="5"  valign="bottom"><%=co.getConsigneeaddress()%></td>
			</tr>
		
			<tr>
				<td height="40" colspan="7" valign="top">
					<table height="100%"  style="font-size: 12px; text-align:center;"  width="700px"  border="1" cellspacing="0" cellpadding="0">
						 <tr  align="center" >
						    <td width="24" height="20">序号</td>
						    <td width="58">商品编码</td>
						    <td width="65">品牌名称</td>
						    <td width="75">商品名称</td>
						    <td width="49">规格</td>
						    <td width="52">申请数量</td>
						    <td width="52">实收数量</td>
						    <td width="130">退货原因</td>
						    <td width="52">特批数量</td>
						    <td width="79">特批备注</td>
						  </tr>
						<% 
						List<OrderGoods> orderGoods=mapOrderGoods.get(co.getCwb());
						if(orderGoods.size()>50)
						{%>
						<tr align="center" style="font-size: 14px">
							<td colspan="10">商品数量较多，无法正常显示</td>
							</tr>
						<%}
						else{
						for(int j=0;j<orderGoods.size();j++){ %>
						<tr   align="center" style="font-size: 10px">
							<td  width="24" height="8px"><%=j+1 %></td>
							<td width="58"><%=orderGoods.get(j).getGoods_code()%></td>
							<td width="65"><%=orderGoods.get(j).getGoods_brand() %></td>
							<td width="75"><%=orderGoods.get(j).getGoods_name() %></td>
							<td width="49"><%=orderGoods.get(j).getGoods_spec() %></td>
							<td width="52"><%=orderGoods.get(j).getGoods_num() %></td>
							<td width="52"></td>
							<td width="130"><%=orderGoods.get(j).getReturn_reason() %></td>
							<td width="52"></td>
							<td width="79"></td>
						</tr>
						<%} }%>
					</table>
				</td>
			</tr>
			<tr>
				<td height="12px">退货仓地址:</td>
				<td colspan="6"><%=co.getRemark5() %></td>
				
			</tr>
			<tr>
				<td height="16">取件承运商：</td>
				<td >
					<%
						for (Customer c : customerlist) {
								if (c.getCustomerid() == co.getCustomerid()) {
					%>
					<%=c.getCustomername()%> <%
 	}
 		}
 %>
						</td>
				 <td>打印日期：</td>
				<td colspan="1"><% SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); %><%=dateFormat.format(new Date()) %></td>
				 <td>取货人签字：</td>
				 <td width="86"></td>
				 <td width="148">取货时间：________</td>
			</tr>
		</table>
			<table id="cwb" height="535px"
			width="700" border="0" cellspacing="0" cellpadding="0"
			class="table_dd" style="font-size: 14px">
						<tr align="center" style="font-size: 14px">
			<% orderGoods=mapOrderGoods.get(co.getCwb());
			if(orderGoods.size()>50){%><td>商品数量太多！图片无法显示</td></tr><%}else{
				if(orderGoods.size()==1){
				String url_pic=orderGoods.get(0).getGoods_pic_url();
				url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
				%>
					<td valign="top"><img height="350px" width="350px" src="<%=url_pic%>"><br>
					商品编码：<%=orderGoods.get(0).getGoods_code() %></td>
					<% }else if(orderGoods.size()==2){
						for(int j=0;j<orderGoods.size();j++)
						{
							String url_pic=orderGoods.get(j).getGoods_pic_url();
							url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
							%>
							<td valign="top"><img height="300px" width="300px" src="<%=url_pic%>"><br>
							商品编码：<%=orderGoods.get(j).getGoods_code() %></td>
							<% 
						}
						
					}
					else if(orderGoods.size()==3){
						for(int j=0;j<orderGoods.size();j++)
						{
							String url_pic=orderGoods.get(j).getGoods_pic_url();
							url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
							%>
							<td ><img height="220px" width="220px" src="<%=url_pic%>"><br>
							商品编码：<%=orderGoods.get(j).getGoods_code() %></td>
							<% 
						
						}
					}
					else if(orderGoods.size()==4){
						for(int j=0;j<orderGoods.size();j++)
						{
							String url_pic=orderGoods.get(j).getGoods_pic_url();
							url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
							%>
							<td ><img height="200px" width="200px" src="<%=url_pic%>"><br>
							商品编码：<%=orderGoods.get(j).getGoods_code() %></td>
							<% 
							if((j+1)%2==0){%>
							</tr><tr align="center" style="font-size: 14px"><%
						}
						}
						
					}
					else if(orderGoods.size()==9||(orderGoods.size()>4&&orderGoods.size()<=6)){
						for(int j=0;j<orderGoods.size();j++)
						{
							String url_pic=orderGoods.get(j).getGoods_pic_url();
							url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
							%>
							<td ><img height="160px" width="160px" src="<%=url_pic%>"><br>
							商品编码：<%=orderGoods.get(j).getGoods_code() %></td>
							<% 
							if((j+1)%3==0){%>
							</tr><tr align="center" style="font-size: 14px"><%
						}
						}
						
					}
					else if(orderGoods.size()>6&&orderGoods.size()<=12){
						for(int j=0;j<orderGoods.size();j++)
						{
							String url_pic=orderGoods.get(j).getGoods_pic_url();
							url_pic=url_pic.contains("http:")?url_pic:("http://"+url_pic);
							%>
							<td ><img height="150px" width="150px" src="<%=url_pic%>"><br>
							商品编码：<%=orderGoods.get(j).getGoods_code() %></td>
							<% 
							if((j+1)%4==0){%>
							</tr><tr align="center" style="font-size: 14px"><%
						}
						}
						
					}
					 }%>
						</tr>
			</table>
		<%-- <%
			if (clist.indexOf(co) < clist.size() - 1) {
		%>
		<br>
		<%
			}
		%> --%>
		<%
			%></div><%}
		%>
	</div>
	</div>
	<%
		if (cwbs == "") {
	%>
	<div>无未打印过的上门退订单</div>
	<%
		} else {
	%>

	<%
		}
	%>
</body>
</html>