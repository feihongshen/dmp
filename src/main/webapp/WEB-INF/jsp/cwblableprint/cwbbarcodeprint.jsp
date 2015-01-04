<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
List<String> List = request.getAttribute("list")==null?null:(List<String>)request.getAttribute("list");
    String textfield = request.getParameter("textfield")==null?"":request.getParameter("textfield");
    String huizongcwb = "'";
    String huizongcwbs = "";

    if(List!=null&&List.size()>0){
    	for(String co : List){
    		huizongcwbs += co+ ",";
    	}
    }
    huizongcwbs = huizongcwb + huizongcwbs+"'";
    String typeid=request.getAttribute("typeid")==null?"cwb": request.getAttribute("typeid").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>条形码打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
function sub(){
	$("#formsubmit").submit();
		
}
function printAll(){
	
}
function submitCwbPrint(){
	if(<%=List != null && List.size()>0 %>){
	var cwbs="";
		var isprint = "";
		$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
			isprint = $(this).val();
			if($.trim(isprint).length!=0){
			cwbs+=""+isprint+",";
			}
			});
		if(isprint==""){
			alert("请选择要打印的订单！");
		}else{
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#cwbs").val(cwbs.substring(0, cwbs.length-1));
	 	$("#searchForm2").submit();
		}
	}else{
		alert("没有做查询操作，不能打印！");
	};
}
</script>

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
	if(List!=null&&List.size()>0){
	for (var i = 0; i < List.size(); i++) {
		LODOP.NewPage();
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("printTable"+i).innerHTML+"</body>";
		LODOP.ADD_PRINT_HTM(0,0,"RightMargin:0mm","BottomMargin:0mm",strFormHtml);
		
		//LODOP.ADD_PRINT_RECT(0,0,360,515,0,1);
		var cwb = cwbs.toString().split(",")[i];
		LODOP.ADD_PRINT_BARCODE(25,250,150,70,"128Auto", cwb);
		LODOP.SET_PRINT_STYLEA(0, "FontSize", 6);
	}
	}
};

function nowprint(){
	var con = confirm("您确认要打印该页吗？");
	if(con==true){
		prn1_print(<%=huizongcwbs%>);
	}
}
function tip(val){
	if(val.value=='cwb')
		{
		$("#tip").html("多个订单号用回车隔开,数量小于或等于1000（订单号不可大于9位）");		
		}
	if(val.value=='baleno')
		{
		$("#tip").html("多个包号用回车隔开,数量小于或等于1000（包号不可大于21位）");		
		}
}
function load(val){
	if(val=='cwb')
		{
		$("#tip").html("多个订单号用回车隔开,数量小于或等于1000（订单号不可大于9位）");		
		}
	if(val=='baleno')
		{
		$("#tip").html("多个包号用回车隔开,数量小于或等于1000（包号不可大于21位）");		
		}
}
</script>
<style type="text/css"id="style1">
*{font-size:12px; margin:0; padding:0; line-height:24px}
.table_1{border-bottom:1px solid #CCC; background:#FFF}
.table_1 h1{font-size:16px; font-weight:bold}
.table_1 td{padding:5px}
.table_2 td{padding:5px; color:#C00; background:#f8f8f8}
.border_r{border-right:1px solid #CCC}
.border_t{border-top:1px solid #ccc}
</style>

<body style="background:#eef9ff" marginwidth="0" marginheight="0" onload="load('<%=typeid%>')">
<div class="saomiao_box2">
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="#" class="light">手工输入生成</a></li>
				<li><a href="./randomcodeprint">随机生成</a></li>
			</ul>
		</div>
		<div class="tabbox">
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td><form action="<%=request.getContextPath() %>/cwbLablePrint/barcodeprint" method="post" id="formsubmit">
						
						<input type="radio" name="typeid" <%if(typeid.equals("cwb")) {%> checked="checked"<%} %> value="cwb" onclick="tip(this);"/>订单号&nbsp;
						<input type="radio" name="typeid" <%if(typeid.equals("baleno")) {%> checked="checked"<%} %> value="baleno" onclick="tip(this);"/>包	号<br>
						<textarea name="textfield" cols="20"  id="textfield" class="kfsh_text"  style="vertical-align:middle"  rows="3" name="cwb"><%=textfield %></textarea>
						<input type="hidden" value="1" name="isshow" id="isshow">
						<input name="button" type="button" class="input_button2" id="button1" value="生成"  onclick="sub();"/>
						<input type="button" value="打印" onclick='submitCwbPrint();' class="input_button2" >
						<span id="tip"></span>
						<!-- <input name="btnval" type="button" class="input_button2" id="button3" value="导出"  onclick="exportExcel();"/> -->
					</form></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" valign="top">
				<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
							<td width="60" align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
							<td align="center" bgcolor="#e7f4e3">序号</td>
							<td align="center" bgcolor="#e7f4e3"><%if(typeid.equals("baleno")) {%>包号<%}else {%>订单号<%} %></td>
							</tr>
							<%if(List!=null&&List.size()>0){int i=1;for(String s:List){ %>
						<tr>
							<td align="center">
							<input id="isprint" type="checkbox" value="<%=s %>" name="isprint"/>
							</td>
							<td align="center" ><%=i++ %></td>
							<td align="center"><%=s %></td>
							</tr>
						<%} }%>
					</table></td>
			</tr>
		</table>
		</div>
		<form action="<%=request.getContextPath()%>/cwbLablePrint/printExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value=""/>
		</form>
		
</div>
</body>

</html>