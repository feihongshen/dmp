<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	List<Map<String, Object>> list =request.getAttribute("list")==null?null:(List<Map<String, Object>>)request.getAttribute("list");
	String nowtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String templateName=request.getAttribute("templateName").toString();
	String balenos=request.getAttribute("balenos").toString();
%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>出库交接单打印(按包号)</title> 
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
	LODOP.PRINT_INIT("出仓交接单打印");
	LODOP.SET_PRINT_STYLE("FontSize",32);
	LODOP.SET_PRINT_STYLE("Bold",1);
	//LODOP.SET_PRINT_PAGESIZE(2, 0,0,"A4");设定纸张大小 SET_PRINT_PAGESIZE(intOrient,intPageWidth,intPageHeight,strPageName)
	
	<%if(list!=null&&!list.isEmpty()){
		for(int i=0;i<list.size();i++){
			String baleno=list.get(i).get("baleno").toString();
		%>
		//ADD_PRINT_HTM(intTop,intLeft,intWidth,intHeight,strHtml)增加超文本项
		LODOP.SET_PRINT_STYLE("FontSize",18);
		LODOP.ADD_PRINT_HTM(0,0,740,360,document.getElementById("form<%=baleno%>").innerHTML);	 
		LODOP.NEWPAGEA();
	<%}}%>
	
};	                     

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		//更新订单打印表为已打印
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/warehousegroupdetail/outbalelist_update",
			data : {"balenos":"<%=balenos%>"},
			dataType:"json",
			success : function(data) {
				prn1_print();
			}
		});
	}
}
</script>
</head>
<body style="tab-interval: 21pt;">
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>
	<a href="javascript:history.go(-1)">返回</a>
<%
	if(list!=null&&!list.isEmpty()){
		for(int i=0;i<list.size();i++){
			String baleno=list.get(i).get("baleno").toString();
%>	

<form id="form<%=baleno%>">
<div class="Section0" style="layout-grid: 15.6000pt;">
<table border="1" cellspacing="0" cellpadding="0" width="740">
	<tr >
		<td colspan=10 align="center">
				<span style="font-weight:bold" >
					 <%=templateName%>出仓交接单
				</span>
		</td>
	</tr>
	<tr>
		<td align="center" width="10%">批次号</td>
		<td align="left" width="10%"><%=baleno%></td>
		<td align="center" width="10%">发货地</td>
		<td align="left" width="10%"><%=list.get(i).get("branchname").toString()%></td>
		<td align="center" width="10%">目的地</td>
		<td align="left" width="10%"><%=list.get(i).get("nextbranchname").toString()%></td>
		<td align="center" width="10%">打单时间</td>
		<td align="left" width="12%"><%=nowtime%></td>
		<td align="center" width="9%">操作员</td>
		<td align="left" width="9%"><%=list.get(i).get("username").toString()%></td>
	</tr>
	<tr>
		<td align="center">发车时间</td>
		<td align="left">&nbsp;</td>
		<td align="center">预计到达时间</td>
		<td align="left">&nbsp;</td>
		<td align="center">实际到达时间</td>
		<td align="left">&nbsp;</td>
		<td align="center">是否准点</td>
		<td align="left" colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td align="center">供应商</td>
		<td align="center" colspan="2">承运商</td>
		<td align="center">出仓数量</td>
		<td align="center">实收数量</td>
		<td align="center" colspan="2">代收总金额</td>
		<td align="center">封签号</td>
		<td align="center" colspan="2">驾驶员</td>
	</tr>
	<%
		List<Map<String, Object>> cwbListView=(List<Map<String, Object>>)list.get(i).get("cwbListView");
		if(cwbListView!=null&&!cwbListView.isEmpty()){
			for(int j=0;j<cwbListView.size();j++){
	%>
	<tr>
		<td align="center"><%=cwbListView.get(j).get("customername")==null?"&nbsp;":cwbListView.get(j).get("customername")%></td>
		<td align="center" colspan="2">&nbsp;</td>
		<td align="center"><%=cwbListView.get(j).get("count")==null?"&nbsp;":cwbListView.get(j).get("count")%></td>
		<td align="center">&nbsp;</td>
		<td align="right"colspan="2"><%=cwbListView.get(j).get("receivablefee")==null?"&nbsp;":cwbListView.get(j).get("receivablefee")%></td>
		<td align="center">&nbsp;</td>
		<td align="center"colspan="2">&nbsp;</td>
	</tr>
	<%}}%>
	<tr>
		<td align="center">备注</td>
		<td align="left" colspan="9">&nbsp;</td>
	</tr>
	<tr>
		<td align="left" colspan="3">发货人签字确认：</td>
		<td align="left" colspan="3">承运商签字确认：</td>
		<td align="left" colspan="4">收货人签字确认：</td>
	</tr>
</table>
</div>		
	</form>
<%}}%>	

	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview()">预览</a>
	<a href="javascript:history.go(-1)">返回</a>
</body>
</html>


