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
%>
<title>打印面单（带称重）</title>
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
	LODOP.PREVIEW();	
};
function myDesign1(parm,data) {		
	CreateOneFormPage1(parm,data);
	LODOP.PRINT_DESIGN();	
};
function CreateOneFormPage1(parm,data){
	LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	parm = parm.substring(1,parm.length-1);
	for(var i=0;i<parm.split(";").length-1;i++){
		if(parm.split(";")[i].split(":")[1]!="0px,0px"){
			LODOP.ADD_PRINT_HTM(parm.split(";")[i].split(":")[1].substring(1,parm.split(";")[i].split(":")[1].indexOf(",")),parm.split(";")[i].split(":")[1].substring(parm.split(";")[i].split(":")[1].indexOf(",")+1,parm.split(";")[i].split(":")[1].length-1),323,140,data.split(",")[i]);
		}
	}
	
};

function ispaohuo(){
	if($("#paohuo").attr("checked")=="checked"){
		$("#sizenum").show();
	}else{
		$("#sizenum").hide();
	}
}
function resetform(){
	$("#cwb").val("");
	$("#carrealweight").val("");
	$("#length").val("");
	$("#width").val("");
	$("#height").val("");
	$("#commoncwb").val("");
}


$(function(){
	setInterval(carrealweightPrint, "100"); 
	$("#cwb").focus();
});

function carrealweightPrint(){
	try{
		 var fso = new ActiveXObject("Scripting.FileSystemObject");
		 if(fso.FileExists("C:\\test.txt")){
			 var ts = fso.OpenTextFile("C:\\test.txt",1); //   权限只读(只读=1，只写=2 ，追加=8 等权限)
			   try{
					var s=ts.ReadAll(); 
					if(s!=""){
						$('#carrealweight').val(s);
					}else{
						$('#carrealweight').val('0');
					}
					$('#chengzhong_tishi').hide();
				}catch(e){
					$('#chengzhong_tishi').show();
					$('#carrealweight').val('0');
				}
		       ts.Close();
		 }
	}catch(e){
		$('#chengzhong_tishi').show();
		$('#carrealweight').val('0');
	}

}
</script>
</head>
<body style="background:#f5f5f5">
<div class="menucontant">
	<form action="">
		<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			<tr>
				<td>订单号码：<input type="text" id="cwb" name="cwb" onKeyDown='if (event.keyCode==13){submitChangeCwbForweightPrint($(this).val());}'><br/></td>
			</tr>
			<tr>
				<td><hr/><b>打印面单</b><br/></td>
			</tr>
			<tr>
				<td>面单类型：
					<select id="commonname" id="commonname">
						<option value="0">选择标签</option>
						<%if(commonlist!=null){for(Common c : commonlist){ %>
							<option value="<%=c.getId()%>"><%=c.getCommonname() %></option>
						<%}} %>
					</select>
				</td>
			</tr>
			<tr id="chengzhong_tishi" style="display: none">
				<td>
					<b>如果您的称重无法读取到电子称的数值，请确定您使用的是IE7或者IE8浏览器，并对IE浏览器做如下操作</b><br/>
					<br/>IE----工具---Internet选项----安全----Internet-----自定义级别,Intranet-----自定义级别
	  				<br/>ActiveX控件和插件的第二项和第三项：
	  				<br/>　　对标记为可安全执行脚本的  ActiveX控件执行脚本设置为“启用”
	 				<br/>　　对没有标记为安全的  ActiveX控件进行初始化和脚本运行设置为“启用”
	 				<br/><a href="<%=request.getContextPath()%>/images/dianzicheng.rar"><font color="red">下载电子称重程序</font></a>
 				</td>
			</tr>
			<tr>
				<td>实际重量：
					<input type="text" id="carrealweight" name="carrealweight"><input type="checkbox" id="paohuo" checked="checked" onclick="ispaohuo();" onKeyDown='if (event.keyCode==13){submitChangeCwbForweightPrint($("#cwb").val());}'>泡货<br/></td>
			</tr>
			<tr id="sizenum">
				<td>长*宽*高：
					<input type="text" id="length" name="length" onKeyDown='if (event.keyCode==13){submitChangeCwbForweightPrint($("#cwb").val());}'>  
					<input type="text" id="width" name="width" onKeyDown='if (event.keyCode==13){submitChangeCwbForweightPrint($("#cwb").val());}' >  
					<input type="text" id="height" name="height" onKeyDown='if (event.keyCode==13){submitChangeCwbForweightPrint($("#cwb").val());}' > cm<br/></td>
			</tr>
			<tr>
				<td>运单号码：<input type="text" id="commoncwb" name="commoncwb" onKeyDown='if (event.keyCode==13&&$("#cwb").val().trim()!=""&&$("#carrealweight").val().trim()!=""&&$("#commoncwb").val().trim()!=""){ChangeCwbWeightForPrint($("#cwb").val());}'><br/></td>
			</tr>
			<tr>
				<td>
					<input type="button" value="打印" onclick='submitChangeCwbForweightPrint($("#cwb").val());'>
					<input type="reset" value="清空" onclick="resetform();">
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>