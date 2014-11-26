<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
List<Common> commonlist = (List<Common>)request.getAttribute("commonlist");
Common common = (Common)request.getAttribute("common");
CwbOrder co = (CwbOrder)request.getAttribute("cwborder");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");

SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
String datetime = df.format(date);
%>
<title>打印面单</title>
<object  id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
<param name="CompanyName" value="北京易普联科信息技术有限公司" />
<param name="License" value="653717070728688778794958093190" />
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 CompanyName="北京易普联科信息技术有限公司" 
	License="653717070728688778794958093190"></embed>
</object> 
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<script type="text/javascript">
var LODOP; //声明为全局变量 
function myPreview1(parm,data) {	
	CreateOneFormPage1(parm,data);	
	LODOP.PRINT();	
};
function myDesign1(parm,data) {		
	CreateOneFormPage1(parm,data);
	LODOP.PRINT_DESIGN();	
};
function CreateOneFormPage1(parm,data){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	/*parm = parm.substring(1,parm.length-1);
	for(var i=0;i<parm.split(";").length-1;i++){
		if(parm.split(";")[i].split(":")[1]!="0px,0px"){
			LODOP.ADD_PRINT_HTM(parm.split(";")[i].split(":")[1].substring(1,parm.split(";")[i].split(":")[1].indexOf(",")),parm.split(";")[i].split(":")[1].substring(parm.split(";")[i].split(":")[1].indexOf(",")+1,parm.split(";")[i].split(":")[1].length-1),323,140,data.split(",")[i]);
		}
	}*/
	if(parm.getdate!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.getdate.split(",")[0],parm.getdate.split(",")[1],323,140,data.split(",")[0]);
	}
	if(parm.workname!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.workname.split(",")[0],parm.workname.split(",")[1],323,140,data.split(",")[1]);
	}
	if(parm.goodsname!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.goodsname.split(",")[0],parm.goodsname.split(",")[1],323,140,data.split(",")[2]);
	}
	if(parm.cwb!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.cwb.split(",")[0],parm.cwb.split(",")[1],323,140,data.split(",")[3]);
	}
	if(parm.salecompany!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.salecompany.split(",")[0],parm.salecompany.split(",")[1],323,140,data.split(",")[4]);
	}
	if(parm.name!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.name.split(",")[0],parm.name.split(",")[1],323,140,data.split(",")[5]);
	}
	if(parm.mobile!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.mobile.split(",")[0],parm.mobile.split(",")[1],323,140,data.split(",")[6]);
	}
	if(parm.workname1!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.workname1.split(",")[0],parm.workname1.split(",")[1],323,140,data.split(",")[7]);
	}
	if(parm.address!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.address.split(",")[0],parm.address.split(",")[1],323,140,data.split(",")[8]);
	}
	if(parm.weight!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.weight.split(",")[0],parm.weight.split(",")[1],323,140,data.split(",")[9]);
	}
	if(parm.size!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.size.split(",")[0],parm.size.split(",")[1],323,140,data.split(",")[10]);
	}
	if(parm.posecode!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.posecode.split(",")[0],parm.posecode.split(",")[1],323,140,data.split(",")[11]);
	}
	if(parm.remark!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.remark.split(",")[0],parm.remark.split(",")[1],323,140,data.split(",")[12]);
	}
	if(parm.bigwords!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.bigwords.split(",")[0],parm.bigwords.split(",")[1],323,140,data.split(",")[13]);
	}
	if(parm.smallwords!="0px,0px"){
		LODOP.ADD_PRINT_HTM(parm.smallwords.split(",")[0],parm.smallwords.split(",")[1],323,140,data.split(",")[14]);
	}
	
};

function resetform(){
	$("#cwb").val("");
	$("#commoncwb").val("");
}

$(function(){
	$("#cwb").focus();
});

</script>
</head>
<body style="background:#eef9ff">
<div class="menucontant">
	<form action="">
		<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			<tr>
				<td>订单号码：<input type="text" id="cwb" name="cwb" onKeyDown='if (event.keyCode==13){submitChangeCwbForPrint($(this).val());}'><br/></td>
			</tr>
			<tr>
				<td><hr/><b>打印面单</b><br/></td>
			</tr>
			<tr>
				<td>面单类型：
					<select id="commonname" >
						<option value="0">选择标签</option>
						<%if(commonlist!=null){for(Common c : commonlist){ %>
							<option value="<%=c.getId()%>"><%=c.getCommonname() %></option>
						<%}} %>
					</select>
				</td>
			</tr>
			<tr>
				<td>运单号码：<input type="text" id="commoncwb" name="commoncwb" onKeyDown='if (event.keyCode==13&&$("#cwb").val()!=""&&$("#commoncwb").val()!=""){ChangeCwbForPrint($("#cwb").val(),$("#commoncwb").val());}'><br/></td>
			</tr>
			<tr>
				<td>
					<input type="button" value="打印" onclick="submitChangeCwbForPrint($('#cwb').val());"/>
					<input type="button" value="清空" onclick="resetform();">
				</td>
			</tr>
		</table>
	</form>
</div>
<form id ="form1">
</form>
</body>
</html>