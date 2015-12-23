<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    List<String> List = request.getAttribute("slist")==null?null:(List<String>)request.getAttribute("slist");
    Object num=request.getAttribute("textfield")==null?"0": request.getAttribute("textfield");
    String typeid=request.getAttribute("typeid")==null?"baleno": request.getAttribute("typeid").toString();
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>快递包号生成打印</title>
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
				alert("请选择要打印的包号！");
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
</script>
</head>
<div class="saomiao_box2">
	<div>
		<!-- <div class="kfsh_tabbtn">
			<ul>
				<li><a href="./barcodeprint" >手工输入生成</a></li>
				<li><a href="./randomcodeprint">随机生成</a></li>
				<li><a href="./branchcodeprint/1">机构条形码打印</a></li>
				<li><a href="#" class="light">快递包号</a></li>
			</ul>
		</div> -->
		<div class="tabbox">
			<table width="100%" border="0" cellspacing="10" cellpadding="0">
			<tr>
				<td><form action="" method="post">
						<%-- <input type="radio" name="typeid"  <%if(typeid.equals("cwb")) {%> checked="checked"<%} %> value="cwb" />订单号&nbsp;
						<input type="radio" name="typeid"  <%if(typeid.equals("baleno")) {%> checked="checked"<%} %> value="baleno" />包	号<br> --%>
						生成数量<input name="textfield" type="text" id="textfield" style="vertical-align:middle" value="<%=num %>" size="10" />
						<input type="hidden" value="1" name="isshow" id="isshow">
						<input name="button" type="submit" class="input_button2" id="button" value="生成" />
						<input type="button" value="打印" onclick='submitCwbPrint();' class="input_button2">
						<span>数量不能超过1000</span>
						
					</form></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" valign="top">
				<%if(List!=null&&List.size()>0){ %>
				<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
						<tr class="font_1">
								<td width="60" align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
								<td align="center" bgcolor="#e7f4e3">序号</td>
							<td align="center" bgcolor="#e7f4e3"><%if(typeid.equals("baleno")) {%>订单号<%}else {%>包号<%} %></td>
							</tr>
							<%int i=1; %>
						<%for(String l:List){ %>
						<tr>
							<td align="center">
							<input id="isprint" type="checkbox" value="<%=l %>" name="isprint"/>
							</td>
							<td align="center" ><%=i++ %></td>
							<td align="center"><%=l %></td>
							</tr>
						<%} %>
					</table>
					<%} %>
					</td>
			</tr>
		</table>
		</div>
		<form action="<%=request.getContextPath()%>/cwbLablePrint/printExcle" method="post" id="searchForm2">
			<input type="hidden" name="cwbs" id="cwbs" value=""/>
		</form>
		
	
</div>
</html>