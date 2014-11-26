<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer,cn.explink.domain.Branch,cn.explink.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
String huizongcwb = "'";
String huizongcwbs = "";

if(cwbList.size()>0){
	for(CwbOrder c : cwbList){
		if(c.getTranscwb().length()>0){
			if(c.getSendcarnum()>1&&c.getTranscwb().indexOf(",")>-1){
				for(int i=1;i<=c.getSendcarnum();i++){
		   			String sigletranscwb = c.getTranscwb().split(",")[i-1]==null?"":c.getTranscwb().split(",")[i-1];
		   			huizongcwbs += sigletranscwb + ",";
	   			}
			}else{
				huizongcwbs += c.getTranscwb() + ",";
			}
		}else{
			huizongcwbs += c.getCwb() + ",";
		}
	}
}
//huizongcwbs = huizongcwb + huizongcwbs+"'";

%>
<html xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:w="urn:schemas-microsoft-com:office:word"
	xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<title>出库交接单</title> 
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
	//var strFormHtml=strBodyStyle+"<body>"+document.getElementById("form1").innerHTML+"</body>";
	
	LODOP.PRINT_INIT("标签打印");
	LODOP.SET_PRINT_STYLE("FontSize",18);
	LODOP.SET_PRINT_STYLE("Bold",1);
	//LODOP.ADD_PRINT_HTM(1,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
	for (i = 0; i < cwbs.split(",").length-1; i++) {
		var cwb = cwbs.split(",")[i];
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+$("#printTable"+cwb).html()+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		LODOP.ADD_PRINT_BARCODE(8,5,210,50,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print('<%=huizongcwbs%>');
	}
}
</script>
<style type="text/css" id="style1">
*{margin:0; padding:0; font-size:12px}
.tiaoxingma{border:1px solid #999; width:50px; height:20px}
.tiaoxingma p{border-top:1px solid #999; padding:1px; height:60px"}
.tiaoxingma strong{padding:0 50px 0 50px}
</style>
</head>
<body style="tab-interval: 21pt;">
	<a href="javascript:nowprint()">直接打印</a>
	<a href="javascript:prn1_preview('<%=huizongcwbs%>')">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:history.go(-1)">返回</a>
	<form id="form1">
		<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder c : cwbList){ %>
			
				<%if(c.getTranscwb().length()>0){ %>
					<%if(c.getSendcarnum()>1&&c.getTranscwb().indexOf(",")>-1){ %>
				   		<%for(int i=1;i<=c.getSendcarnum();i++){ %>
				   		<%String sigletranscwb = c.getTranscwb().split(",")[i-1]==null?"":c.getTranscwb().split(",")[i-1]; %>
				   		<div style="border:1px solid #999; width:80px; height:50px" id="printTable<%=sigletranscwb%>" >
							<p style="border-top:1px solid #999; padding:1px; height:60px" id="printcwb<%=sigletranscwb %>" name="printcwb<%=sigletranscwb %>">&nbsp;</p>
							<%-- <p style="border-top:1px solid #999; padding:1px"><strong>收件人：<%=c.getConsigneename() %></strong>&nbsp;&nbsp;&nbsp;&nbsp;<%=c.getSendcarnum() %>-<%=i %></p> --%>
						</div>
					<%}}else{ %>
						<div style="border:1px solid #999; width:80px; height:50px" id="printTable<%=c.getTranscwb()%>" >
							<p style="border-top:1px solid #999; padding:1px; height:60px" id="printcwb<%=c.getTranscwb() %>" name="printcwb<%=c.getTranscwb() %>">&nbsp;</p>
							<%-- <p style="border-top:1px solid #999; padding:1px"><strong>收件人：<%=c.getConsigneename() %></strong>&nbsp;&nbsp;&nbsp;&nbsp;<%=c.getSendcarnum() %>-1</p> --%>
						</div>
				<%}}else{ %>
					<div style="border:1px solid #999; width:80px; height:50px" id="printTable<%=c.getCwb()%>" >
						<p style="border-top:1px solid #999; padding:1px; height:60px" id="printcwb<%=c.getCwb() %>" name="printcwb<%=c.getCwb() %>">&nbsp;</p>
						<%-- <p style="border-top:1px solid #999; padding:1px"><strong>收件人：<%=c.getConsigneename() %></strong>&nbsp;&nbsp;</p> --%>
					</div>
				<%} %>
			
		<%} %>
	</form>
	<a href="javascript:nowprint()">直接打印</a>
</body>
</html>


