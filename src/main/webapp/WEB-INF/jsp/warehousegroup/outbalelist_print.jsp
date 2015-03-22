<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%
	Map<Long, List<Map<String,Object>>> list =request.getAttribute("branchmap")==null?null:(Map<Long, List<Map<String,Object>>>)request.getAttribute("branchmap");
	List<Long> nextbranchids = request.getAttribute("nextbranchids")==null?null:(List<Long>)request.getAttribute("nextbranchids");
	Map<Long,String> branchmap = (Map<Long,String>)request.getAttribute("branchMap");
	Map<String,String> tmap = (Map<String,String>)request.getAttribute("tmap");
	String nowtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String templateName=request.getAttribute("templateName").toString();
	String branchname=request.getAttribute("branchname").toString();
	String username=request.getAttribute("username").toString();
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
	
	<%if(nextbranchids!=null && nextbranchids.size() >0){
		for(int i=0;i<nextbranchids.size();i++){
			long baleno=nextbranchids.get(i);
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
	if(nextbranchids!=null && nextbranchids.size() >0){
		for(int i=0;i<nextbranchids.size();i++){
			String id=( System.currentTimeMillis() + i) +"";
  %>	

<form id="form<%=nextbranchids.get(i)%>">
<div class="Section0" style="layout-grid: 15.6000pt;">
<table border="1" cellspacing="0" cellpadding="0" width="740">
	<tr >
		<td colspan=10 align="center">
				<span style="font-weight:bold" >
					 <%=templateName%><input type="text"id='headName' value="出库交接单" style="border:none;font-weight:bold;font-size: 15px"/>
				</span>
		</td>
	</tr>
	<tr>
		<td align="center" width="10%">交接单号</td>
		<td align="left" width="10%"><%=id%></td>
		<td align="center" width="10%">发货地</td>
		<td align="left" width="10%"><%=branchname%></td>
		<td align="center" width="10%">目的地</td>
		<td align="left" width="10%"><%=branchmap.get(nextbranchids.get(i))%></td>
		<td align="center" width="10%">打单时间</td>
		<td align="left" width="12%"><%=nowtime%></td>
		<td align="center" width="9%">操作员</td>
		<td align="left" width="9%"><%=username%></td>
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
	<%if(tmap != null && tmap.size() > 0){ %>
		<td align="center" colspan="2">包号</td>
		<%if(tmap != null && tmap.size() == 3){ %>
		<td align="center">单数</td>
		<td align="center">件数</td>
		<td align="center">重量</td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("jianshu") != null){%>
		<td align="center" colspan="2">单数</td>
		<td align="center">件数</td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("carrealweight") != null){%>
		<td align="center" colspan="2">单数</td>
		<td align="center">重量</td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("jianshu") != null && tmap.get("carrealweight") != null){%>
		<td align="center" >件数</td>
		<td align="center" colspan="2">重量</td>
		<%}  else if(tmap != null && tmap.size() == 1 && tmap.get("danshu") != null ){%>
		<td align="center" colspan="3">单数</td>
		<%} else if(tmap != null && tmap.size() == 1 && tmap.get("jianshu") != null ){%>
		<td align="center"  colspan="3">件数</td>
		<%} else if(tmap != null && tmap.size() == 1 &&  tmap.get("carrealweight") != null){%>
		<td align="center" colspan="3">重量</td>
		<%} %>
	<%}else{ %>	
		<td align="center" colspan="5">包号</td>
	<%} %>
		
		<td align="center" colspan="2">代收总金额</td>
		<td align="center" colspan="3">驾驶员</td>
	</tr>
	<%
		List<Map<String,Object>> bList = list.get(nextbranchids.get(i));
		if(bList!=null && bList.size()>0){
			long count =0;
			long sendcarnum =0;
		    BigDecimal carrealweight =BigDecimal.ZERO;
		    BigDecimal receivablefee =BigDecimal.ZERO;
			
			for(int j=0;j<bList.size();j++){
				Map<String, Object> cwbListView = (Map<String, Object>)bList.get(j).get("cwbListView");
				count += (cwbListView.get("count")==null?0:Long.parseLong(cwbListView.get("count").toString()) );
				sendcarnum += (cwbListView.get("sendcarnum")==null?0:Long.parseLong(cwbListView.get("sendcarnum").toString()) );
				carrealweight = carrealweight.add( new BigDecimal(cwbListView.get("carrealweight")==null?"0":cwbListView.get("carrealweight").toString()) );
				receivablefee = receivablefee.add( new BigDecimal(cwbListView.get("receivablefee")==null?"0":cwbListView.get("receivablefee").toString()) );
				
	%>
	<tr>
		<%if(tmap != null && tmap.size() > 0){ %>
			<td align="center" colspan="2"><%=bList.get(j).get("baleno")==null?"&nbsp;":bList.get(j).get("baleno")%></td>
			<%if(tmap != null && tmap.size() == 3){ %>
			<td align="center"><%=cwbListView.get("count")==null?"0":cwbListView.get("count")%></td>
			<td align="center"><%=cwbListView.get("sendcarnum")==null?"0":cwbListView.get("sendcarnum")%></td>
			<td align="center"><%=cwbListView.get("carrealweight")==null?"0":cwbListView.get("carrealweight")%></td>
			<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("jianshu") != null){%>
			<td align="center" colspan="2"><%=cwbListView.get("count")==null?"0":cwbListView.get("count")%></td>
			<td align="center"><%=cwbListView.get("sendcarnum")==null?"0":cwbListView.get("sendcarnum")%></td>
			<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("carrealweight") != null){%>
			<td align="center" colspan="2"><%=cwbListView.get("count")==null?"0":cwbListView.get("count")%></td>
			<td align="center"><%=cwbListView.get("carrealweight")==null?"0":cwbListView.get("carrealweight")%></td>
			<%} else if(tmap != null && tmap.size() == 2 && tmap.get("jianshu") != null && tmap.get("carrealweight") != null){%>
			<td align="center" ><%=cwbListView.get("sendcarnum")==null?"0":cwbListView.get("sendcarnum")%></td>
			<td align="center" colspan="2"><%=cwbListView.get("carrealweight")==null?"0":cwbListView.get("carrealweight")%></td>
			<%}  else if(tmap != null && tmap.size() == 1 && tmap.get("danshu") != null ){%>
			<td align="center" colspan="3"><%=cwbListView.get("count")==null?"0":cwbListView.get("count")%></td>
			<%} else if(tmap != null && tmap.size() == 1 && tmap.get("jianshu") != null ){%>
			<td align="center"  colspan="3"><%=cwbListView.get("sendcarnum")==null?"0":cwbListView.get("sendcarnum")%></td>
			<%} else if(tmap != null && tmap.size() == 1 &&  tmap.get("carrealweight") != null){%>
			<td align="center" colspan="3"><%=cwbListView.get("carrealweight")==null?"0":cwbListView.get("carrealweight")%></td>
			<%} %>
		<%}else{ %>	
			<td align="center" colspan="5"><%=bList.get(j).get("baleno")==null?"&nbsp;":bList.get(j).get("baleno")%></td>
		<%} %>
	<td align="right" colspan="2" ><%=cwbListView.get("receivablefee")==null?"0":cwbListView.get("receivablefee")%></td>
	<td align="center" colspan="3"><%=bList.get(j).get("drivername")==null?"&nbsp;":bList.get(j).get("drivername")%></td>
		
	</tr>
	<%}%>
		<tr>
		
		<%if(tmap != null && tmap.size() > 0){ %>
		<td align="center" colspan="2">合计</td>
		<%if(tmap != null && tmap.size() == 3){ %>
		<td align="center"><%=count%></td>
		<td align="center"><%=sendcarnum%></td>
		<td align="center"><%=carrealweight%></td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("jianshu") != null){%>
		<td align="center" colspan="2"><%=count%></td>
		<td align="center"><%=sendcarnum%></td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("danshu") != null && tmap.get("carrealweight") != null){%>
		<td align="center" colspan="2"><%=count%></td>
		<td align="center"><%=carrealweight%></td>
		<%} else if(tmap != null && tmap.size() == 2 && tmap.get("jianshu") != null && tmap.get("carrealweight") != null){%>
		<td align="center" ><%=sendcarnum%></td>
		<td align="center" colspan="2"><%=carrealweight%></td>
		<%}  else if(tmap != null && tmap.size() == 1 && tmap.get("danshu") != null ){%>
		<td align="center" colspan="3"><%=count%></td>
		<%} else if(tmap != null && tmap.size() == 1 && tmap.get("jianshu") != null ){%>
		<td align="center"  colspan="3"><%=sendcarnum%></td>
		<%} else if(tmap != null && tmap.size() == 1 &&  tmap.get("carrealweight") != null){%>
		<td align="center" colspan="3"><%=carrealweight%></td>
		<%} %>
	<%}else{ %>	
		<td align="center" colspan="5">合计</td>
	<%} %>
			<td align="right" colspan="2"><%=receivablefee%></td>
			<td align="center" colspan="3">&nbsp;</td>
		</tr>		
		<% }%>
	
	
	<tr>
		<td align="center">备注</td>
		<td align="left" colspan="9">&nbsp;</td>
	</tr>
	<tr>
		<td align="left" colspan="3">发货人签字确认：</td>
		<td align="left" colspan="3">驾驶员签字确认：</td>
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


