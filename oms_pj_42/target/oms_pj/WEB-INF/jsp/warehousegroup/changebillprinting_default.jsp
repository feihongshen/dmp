<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<CwbOrder> cwborderlist = (List<CwbOrder>)request.getAttribute("cwborderlist");
    String branchname = (String)request.getAttribute("branchname");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    String datetime = df.format(date);
    BigDecimal money = new BigDecimal(0);;
    for(CwbOrder c : cwborderlist){
    	money=money.add(c.getCaramount());
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop.exe"></embed>
</object>
</HEAD>
<script language="javascript" type="text/javascript">   
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
		LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单一");
		LODOP.SET_PRINT_STYLE("FontSize",18);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_TEXT(50,231,260,39,"打印页面部分内容");
		LODOP.ADD_PRINT_HTM(88,200,350,600,document.getElementById("form1").innerHTML);
	};	                 
</script>

<BODY onload="prn1_print()">
<form id ="form1">
<table style = "width:700px;text-align: center;">
   <tr>
     <td colspan="4" style ="text-align: right;padding-right: 10px;"><b>中转出站交接单</b></td>
   </tr>
   <tr>
     <td colspan="3" style="text-align: left;">接货站：<%=branchname %>　打印时间：<%=datetime %>　打印人：管理员</td>
   </tr>
   <tr>
     <td style ="width:120px;">订单号</td>
     <td style ="width:250px;">地址</td>
     <td style ="width:200px;">收件人</td>
     <td style ="width:120px;">金额</td>
   </tr>
   <%for(CwbOrder cwborder:cwborderlist) {%>
   <tr>
     <td><%=cwborder.getCwb() %></td>
     <td><%=cwborder.getConsigneeaddress() %></td>
     <td><%=cwborder.getConsigneename() %></td>
     <td><%=cwborder.getCaramount() %></td>
   </tr>
   <%} %>
   <tr >
    <td colspan ="4" style="text-align: left;">合计金额：<%=money %></td>
   </tr>
   <tr>
     <td colspan ="4" style="text-align: left;">请仔细核对上述内容，签字后即要承担相关责任和法律。</td>
   </tr>
   <tr>
     <td style="text-align: left;">库房签字：</td>
     <td  style="text-align: left;">驾驶员签字：</td>
     <td colspan ="2" style="text-align: left;">收货方接受人签字：</td>
   </tr>
   <tr>
     <td colspan ="4" style="text-align: left;">此联一试两份，出、收货方各留一份，自行保存。</td>
   </tr>
</table>
</form>

<a href="javascript:prn1_preview()">打印预览</a>
<a href="javascript:prn1_print()">直接打印</a>
</BODY>
</HTML>
