<%@page import="cn.explink.domain.Branch,cn.explink.domain.Function"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branches = (List<Branch>)request.getAttribute("branches");
	List<Function> functiones = (List<Function>)request.getAttribute("functiones");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;border-style: dashed;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
var cwbtobranchids = "";
function clickNextStopBranch(id){
	var clickCwb = "";
	var isChoice = 0;//0为选用 1为取消
	//遍历全局变量 并判断此操作是取消还是选取
	for(var i = 0 ; i < cwbtobranchids.length ; i++){
		if(cwbtobranchids[i]!=id ){
			if(cwbtobranchids[i].length>0){clickCwb = clickCwb+","+cwbtobranchids[i];}
		}else{
			isChoice = 1;//如果触发的ID与cwbtobranchids中的数组有相同，则认为是取消操作
			$("#li_"+id).css('color','#000');
			$("#li_"+id).css('background','#fff');
		}
	}
	if(isChoice==0){
		clickCwb = clickCwb +","+ id; //如果不是取消，则将这个ID累加在数组中
		$("#li_"+id).css('color','#fff');
		$("#li_"+id).css('background','red');
	}
	clickCwb = clickCwb.substring(1, clickCwb.length);
	$("#cwbtobranchid").val(clickCwb);
	cwbtobranchids = clickCwb.split(",");
}

var functionids = "";
function clickFunctionToBranch(id){
	var clickFun = "";
	var isChoice = 0;//0为选用 1为取消
	//遍历全局变量 并判断此操作是取消还是选取
	for(var i = 0 ; i < functionids.length ; i++){
		if(functionids[i]!=id ){
			if(functionids[i].length>0){clickFun = clickFun+","+functionids[i];}
		}else{
			isChoice = 1;//如果触发的ID与cwbtobranchids中的数组有相同，则认为是取消操作
			$("#li_f_"+id).css('color','#000');
			$("#li_f_"+id).css('background','#fff');
		}
	}
	if(isChoice==0){
		clickFun = clickFun +","+ id; //如果不是取消，则将这个ID累加在数组中
		$("#li_f_"+id).css('color','#fff');
		$("#li_f_"+id).css('background','red');
	}
	clickFun = clickFun.substring(1, clickFun.length);
	$("#functionids").val(clickFun);
	functionids = clickFun.split(",");
}


$(function() {
	$("#branchid").change(function(){
		var branchid = $("#branchid").val();
		$("#BranchList li").show();
		$("#BranchList li").css('color','#000');
		$("#BranchList li").css('background','#fff');
		
		$("#FunctionList li").css('color','#000');
		$("#FunctionList li").css('background','#fff');
		
		$("#li_"+branchid).hide();
		$("#cwbtobranchid").val($("#"+branchid).val());
		
		if(branchid>-1){
			//将数组放入全局变量
			cwbtobranchids = $("#"+branchid).val().split(",");
			for(var i = 0 ; i < cwbtobranchids.length ; i++){
				$("#li_"+cwbtobranchids[i]).css('color','#fff');
				$("#li_"+cwbtobranchids[i]).css('background','red');
			}
			$("#functionids").val($("#F_"+branchid).val());
			functionids = $("#F_"+branchid).val().split(",");
			for(var i = 0 ; i < functionids.length ; i++){
				$("#li_f_"+functionids[i]).css('color','#fff');
				$("#li_f_"+functionids[i]).css('background','red');
			}
		}
	});
});

function clickSubmit(){
	if($("#branchid").val()==-1){
		alert("请选择您要操作的机构!");
		return false;
	}else{
		return true;
	}
}
</script>
</HEAD>
<BODY>
<div>
<form id="fromBranch"  action="save" onsubmit="return clickSubmit()">

	<div><%=(String)(request.getAttribute("errorState")==null?"":request.getAttribute("errorState")) %></div>
	<select id="branchid" name="branchid">
	<option value="-1" >----请选择----</option>
	<% for(Branch b : branches){ %>
	<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
	<%} %>
	</select>
	<input type="submit" value="保存修改">
	<input type="hidden" id="cwbtobranchid" name="cwbtobranchid" value="">
	<input type="hidden" id="functionids" name="functionids" value="">
	</form>
<p> </p>
	<div id="FunctionList">
	<% for(Function f : functiones){ %>
	<li id="li_f_<%=f.getFunctionid() %>" onclick="clickFunctionToBranch(<%=f.getFunctionid() %>)"><%=f.getFunctionid() %>-<%=f.getFunctionname() %></li>
	<%} %>
	</div>

<p> </p>
	
	<div id="BranchList">
	<% for(Branch b : branches){ %>
	<li id="li_<%=b.getBranchid() %>" onclick="clickNextStopBranch(<%=b.getBranchid() %>)"><%=b.getBranchname() %></li>
	<%} %>
	</div>
	
	<% for(Branch b : branches){ %>
	<input type="hidden" id="<%=b.getBranchid()%>" value="<%=b.getCwbtobranchid() %>" >
	<input type="hidden" id="F_<%=b.getBranchid()%>" value="<%=b.getFunctionids() %>" >
	<%} %>
</div>	
</BODY>
</HTML>
