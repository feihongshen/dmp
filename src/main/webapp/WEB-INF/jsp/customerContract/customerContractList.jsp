
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.customerCoutract.CustomerContractManagement"%>
<%@page import="cn.explink.domain.customerCoutract.DepositInformation"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.customerCoutract.CustomerContractManagement"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.ContractStateEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.LoanTypeEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.ContracTypeEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.SettlementPeriodEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.InvoiceTypeEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.WhetherHaveDepositEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	List<Customer> customerList = (List<Customer>)request.getAttribute("contractList");
	List<CustomerContractManagement> customerContractList =request.getAttribute("customerContractList")==null? null : (List<CustomerContractManagement>)request.getAttribute("customerContractList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户合同管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript">
var rowCount = 0;
$(function(){
	$("table#gd_table123 tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
})
function changeContractState(state){
	 $("#contractstatus").val(state);
} 
function setId(id,state){
	$("#contractid").val(id);
	$("#customerContractState").val(state);
} 
//查询
function query(){
	if(!queryVerify()){
		return;
	}else{
		$("#queryContract").submit();
		closeBox();
	}
}

function setCondition(){
	$("#number").val("${number}");
	$("#contractstatus").val("${contractstatus}");
	$("#customerid").val("${customerid}");
	$("#partyaname").val("${partyaname}");
	$("#marketingprincipal").val("${marketingprincipal}");
	$("#othercontractors").val("${othercontractors}");
	$("#contractdescription").val("${contractdescription}");
	$("#loansandsettlementway").val("${loansandsettlementway}");
	$("#createStatrtTime").val("${createStatrtTime}");
	$("#createEndTime").val("${createEndTime}");
	$("#overStartTime").val("${overStartTime}");
	$("#overEndTime").val("${overEndTime}");
	$("#whetherhavedeposit").val("${whetherhavedeposit}");
	$("#sort").val("${sort}");
	$("#method").val("${method}");
}
function addContract(form){
	if(verify()){
		if ($("#txtFileName").val()=="") {
			$(form).attr("enctype", "");
			$(form).attr("action", "<%=request.getContextPath()%>/customerContract/addCustomerContract");
			submitCreateForm(form);
			return;
		}
		$('#swfupload-control').swfupload('addPostParam', 'number', $("#number").val());
		$('#swfupload-control').swfupload('addPostParam', 'contractstatus', $("#contractstatus").val());
		$('#swfupload-control').swfupload('addPostParam', 'contractstartdate', $("#contractstartdate").val());
		$('#swfupload-control').swfupload('addPostParam', 'contractenddate', $("#contractenddate").val());
		$('#swfupload-control').swfupload('addPostParam', 'customerid', $("#customerid").val());
		$('#swfupload-control').swfupload('addPostParam', 'contracttype', $("#contracttype").val());
		$('#swfupload-control').swfupload('addPostParam', 'whetherhavedeposit', $("#whetherhavedeposit").val());
		$('#swfupload-control').swfupload('addPostParam', 'partyaname', $("#partyaname").val());
		$('#swfupload-control').swfupload('addPostParam', 'yifangquancheng', $("#yifangquancheng").val());
		$('#swfupload-control').swfupload('addPostParam', 'othercontractors', $("#othercontractors").val());
		$('#swfupload-control').swfupload('addPostParam', 'loanssettlementcycle', $("#loanssettlementcycle").val());
		$('#swfupload-control').swfupload('addPostParam', 'loansandsettlementway', $("#loansandsettlementway").val());
		$('#swfupload-control').swfupload('addPostParam', 'paifeisettlementcycle', $("#paifeisettlementcycle").val());
		$('#swfupload-control').swfupload('addPostParam', 'paifeisettlementtype', $("#paifeisettlementtype").val());
		$('#swfupload-control').swfupload('addPostParam', 'marketingprincipal', $("#marketingprincipal").val());
		$('#swfupload-control').swfupload('addPostParam', 'invoicetype', $("#invoicetype").val());
		$('#swfupload-control').swfupload('addPostParam', 'taxrate', $("#taxrate").val());
		$('#swfupload-control').swfupload('addPostParam', 'collectionloanbank', $("#collectionloanbank").val());
		$('#swfupload-control').swfupload('addPostParam', 'collectionloanbankaccount', $("#collectionloanbankaccount").val());
		$('#swfupload-control').swfupload('addPostParam', 'expensebank', $("#expensebank").val());
		$('#swfupload-control').swfupload('addPostParam', 'expensebankaccount', $("#expensebankaccount").val());
		$('#swfupload-control').swfupload('addPostParam', 'contractdescription', $("#contractdescription").val());
		$('#swfupload-control').swfupload('addPostParam', 'depositpaymentdate', $("#depositpaymentdate").val());
		$('#swfupload-control').swfupload('addPostParam', 'depositpaymentamount', $("#depositpaymentamount").val());
		$('#swfupload-control').swfupload('addPostParam', 'depositpaymentperson', $("#depositpaymentperson").val());
		$('#swfupload-control').swfupload('addPostParam', 'depositgatherperson', $("#depositgatherperson").val());
		
		$('#swfupload-control').swfupload('startUpload');
	}
}
function submitCreateForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			if (data.errorCode == 0) {
				alert(data.error)
				document.location.reload(true);
				closeBox();
			}
		}
	});
}

 function shangchuan(){
		//上传附件使用
		$('#swfupload-control').swfupload({
			upload_url : $("#addcallerForm").attr("action"),
			file_size_limit : "10240",
			file_types_description : "All Files",
			file_upload_limit : "0",
			file_queue_limit : "1",
			flash_url : "<%=request.getContextPath()%>/js/swfupload/swfupload.swf",
			button_image_url :"<%=request.getContextPath()%>/images/indexbg.png",
			button_text : '选择文件',
			button_width : 50,
			button_height : 20,
			button_placeholder : $("#button")[0]
		}).bind('fileQueued', function(event, file) {
			$("#txtFileName").val(file.name);
		}).bind('fileQueueError', function(event, file, errorCode, message) {
		}).bind('fileDialogStart', function(event) {
			$(this).swfupload('cancelQueue');
		}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
		}).bind('uploadStart', function(event, file) {
		}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {

		}).bind('uploadSuccess', function(event, file, serverData) {
			var dataObj = eval("(" + serverData + ")");
			document.location.reload(true);
			alert(dataObj.error)
			closeBox();
			//$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
			//document.location.reload(true);
			//$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
		}).bind('uploadComplete', function(event, file) {
			$(this).swfupload('startUpload');
		}).bind('uploadError', function(event, file, errorCode, message) {
		});
	}

//******************************************************************
function update(){
	$("#contractstatus").removeAttr("disabled");
	
	
		if(getBranchContractDetailVOList()){
			var depositInformationStr = JSON.stringify(getBranchContractDetailVOList());
			$('#depositInformationStr').val(depositInformationStr);
		}
		 $.ajax({
				type : "POST",
				data : $('#updateContract').serialize(),
				url : $("#updateContract").attr('action'),
				dataType : "json",
				success : function(data) {
					if(data.errorCode==0){
						alert(data.error);
						   if (data.errorCode == 0) { 
							   document.location.reload(true);
						   }  
							closeBox();
						}
					}
				});
}
function getBranchContractDetailVOList(){
	var list = new Array();
	var contractTrs = $("#depositTable .contractTr");
	for(var i=0;i<contractTrs.length;i++){
		trObj = {};
		tds = contractTrs[i].children;
		for(var j=1;j<tds.length;j++){
			tdVal = tds[j].children[0].value;
			name = tds[j].attributes[1].value;
			if(tdVal){
				trObj[name]=tdVal;
			}
		}
		if(!jQuery.isEmptyObject(trObj)){
			//字表外键值
			trObj['contractid'] = $("#contractid").val();
			list[i]=trObj;
		}
	}
	return list;
}

//添加行
/* function addTr(){
	var table = $('#depositTable'); 
	var tr = $("<tr class='contractTr' name='tr"+rowCount+"'></tr>"); 
	var checkTd = $("<td></td>");
	checkTd.append($("<input type='checkbox' name='count' value='checkbox"+rowCount+"'>"));
	tr.append(checkTd); 
	var depositReturnTimeTd = $("<td class='contractTd' name='depositreturndate'></td>"); 
	tr.append(depositReturnTimeTd); 
	var depositReturnAmountTd = $("<td class='contractTd' name='depositreturnsum'></td>"); 
	tr.append(depositReturnAmountTd); 
	var depositReturnPersonTd = $("<td class='contractTd' name='refundpeople'></td>"); 
	tr.append(depositReturnPersonTd); 
	var depositCollectorTd = $("<td class='contractTd' name='payee'></td>"); 
	tr.append(depositCollectorTd); 
	var remarkTd = $("<td class='contractTd' name='remarks'></td>"); 
	tr.append(remarkTd); 
	table.append(tr);
	rowCount++;
	initBranchContract();
} */

//添加行
 function addTr(){
	var table = $('#depositTable'); 
	var tr = $("<tr class='contractTr' name='tr"+rowCount+"'></tr>"); 
	var checkTd = $("<td></td>");
	checkTd.append($("<input type='checkbox' name='count' value='checkbox"+rowCount+"'>"));
	tr.append(checkTd); 
	var depositReturnTimeTd = $("<td class='contractTd' name='depositreturndate' width='120px'></td>"); 
	depositReturnTimeTd.append($("<input type='text' id='depositreturndate"+rowCount+"' name='depositreturndate"+rowCount+"' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositReturnTimeTd); 
	var depositReturnAmountTd = $("<td class='contractTd' name='depositreturnsum' width='120px'></td>"); 
	depositReturnAmountTd.append($("<input type='text' id='depositreturnsum"+rowCount+"' name='depositreturnsum"+rowCount+"' style='border:0;outline:none;width:120px' onblur='isFloatVal(this)'/>"));
	tr.append(depositReturnAmountTd); 
	var depositReturnPersonTd = $("<td class='contractTd' name='refundpeople' width='120px'></td>"); 
	depositReturnPersonTd.append($("<input type='text' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositReturnPersonTd); 
	var depositCollectorTd = $("<td class='contractTd' name='payee' width='120px'></td>"); 
	depositCollectorTd.append($("<input type='text' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositCollectorTd); 
	var remarkTd = $("<td class='contractTd' name='remarks' width='180px'></td>"); 
	remarkTd.append($("<input type='text' style='border:0;outline:none;width:180px'/>"));
	tr.append(remarkTd); 
	table.append(tr);
	
	$("#depositreturndate"+rowCount).datepicker();
	rowCount++;
	//initBranchContract();
} 




//移除行
function deleteTr(){
	var checked = $("#depositTable input[type='checkbox'][name='count']");
	$(checked).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			$(this).parent().parent().remove();
		}
	}); 
	//判断全选框置为"不勾选"
	$("#depositTable input[type='checkbox'][name='checkAllTrs']")[0].checked = false;
}


//********************************************

function deleteContract(){
	
	var contractId = $("#contractid").val()
	 if(contractId == ""){
			alert("请先选择需操作的合同!");
			return false;
	} 
	var state = $("#customerContractState").val();
	var xinjian = "<%=ContractStateEnum.XinJian.getValue()%>";
	if(state != xinjian){
		alert("只有新建状态的合同才能进行删除!");
		return false;
	}
	if(confirm("确定要删除吗？")){
		$.ajax({
			type:'POST',
			data:{id:contractId},
			url:'<%=request.getContextPath()%>/customerContract/deleteContract',
			dataType:'json',
			success:function(data){
				if(data.errorCode==0){
					document.location.reload(true);
					alert(data.error);
				}
			}
		});
	}
}
//根据是否有押金显示押金信息栏
function show(){
	var num = $("#whetherhavedeposit option:selected").val();
	if(num==1){
		$("#firstDepositTr").css('display' ,'');  
        $("#secondDepositTr").css('display' ,'');
        $("#depositMessage").css('display' ,'');
        $("#depositButton").css('display' ,'');
	}else{
		 $("#depositCollectDate").val("");  
         $("#depositpaymentperson").val(""); 
         $("#depositpaymentamount").val("");  
         $("#depositgatherperson").val("");
         $("#firstDepositTr").css('display', 'none');  
         $("#secondDepositTr").css('display' ,'none');
	}
	
}
//根据合同类型显示合同商
function showOthercontractors(){
	var num = $("#contracttype option:selected").val();
	if(num==2){
		$("#othercontractors").css('display','');
		$("#contractors").css('display','');
	}else if(num == 1){
		$("#othercontractors").val("");
		$("#othercontractors").css('display','none');
		$("#contractors").css('display','none');
	}
}

//修改时先查询指定的合同信息
function  demandContract(){
	var contractId = $("#contractid").val()
	$.ajax({
		type:'POST',
		data:{id:contractId},
		 async: false,
		url:'<%=request.getContextPath()%>/customerContract/queryById',
		dataType:'json',
		success:function(data){
			$("#id").val(data.id);
			$("#number").val(data.number);
			$("#contractstatus").find("option[value="+data.contractstatus+"]").attr("selected",true);
			$("#contractdate").val(data.contractstartdate+" 至 "+data.contractenddate);
			$("#contractstartdate").val(data.contractstartdate);
			$("#contractenddate").val(data.contractenddate);
			$("#customerid").find("option[value="+data.customerid+"]").attr("selected",true);
			$("#contracttype").find("option[value="+data.contracttype+"]").attr("selected",true);
			$("#whetherhavedeposit").find("option[value="+data.whetherhavedeposit+"]").attr("selected",true);
			$("#partyaname").val(data.partyaname);
			$("#yifangquancheng").val(data.yifangquancheng);
			$("#contractors").val(data.contractors);
			$("#loanssettlementcycle").find("option[value="+data.loanssettlementcycle+"]").attr("selected",true);
			$("#loansandsettlementway").find("option[value="+data.loansandsettlementway+"]").attr("selected",true);
			$("#paifeisettlementcycle").find("option[value="+data.paifeisettlementcycle+"]").attr("selected",true);
			$("#paifeisettlementtype").find("option[value="+data.paifeisettlementtype+"]").attr("selected",true);
			$("#marketingprincipal").val(data.marketingprincipal);
			$("#invoicetype").find("option[value="+data.invoicetype+"]").attr("selected",true);
			$("#taxrate").val(data.taxrate);
			$("#collectionloanbank").val(data.collectionloanbank);
			$("#collectionloanbankaccount").val(data.collectionloanbankaccount);
			$("#expensebank").val(data.expensebank);
			$("#expensebankaccount").val(data.expensebankaccount);
			$("#contractdescription").val(data.contractdescription);
			$("#depositpaymentdate").val(data.depositpaymentdate);
			$("#paymentdate").val(data.depositpaymentdate);
			$("#depositpaymentamount").val(data.depositpaymentamount);
			$("#depositpaymentperson").val(data.depositpaymentperson);
			$("#paymentperson").val(data.depositpaymentperson);
			$("#depositgatherperson").val(data.depositgatherperson);
			$("#gatherperson").val(data.depositgatherperson);
			$("#othercontractors").val(data.othercontractors);
			if(data.contractaccessory != ""&&data.contractaccessory !=null){
				$("#contractFile").css('display' ,'');  
				$("#file").attr("href","<%=request.getContextPath()%>/customerContract/download?filepathurl=" +data.contractaccessory);
			}
			if(data.whetherhavedeposit == 1){
				$("#firstDepositTr").css('display' ,'');  
		        $("#secondDepositTr").css('display' ,'');
		        $("#depositMessage").css('display' ,'');
		        $("#depositButton").css('display' ,'');
			}
			if(data.contracttype == 2){
				 $("#othercontractors").css('display' ,'');
				 $("#contractors").css('display' ,'');
			}
			initDepositTable(data.depositInformationList);
			 //根据合同的状态显示不同的按钮
		 	var xinJianState = '<%=ContractStateEnum.XinJian.getValue()%>';
			var zhiXingZhongState = '<%=ContractStateEnum.ZhiXingZhong.getValue()%>';
			var heTongZhongZhiState = '<%=ContractStateEnum.HeTongZhongZhi.getValue()%>';
			var heTongJieShuState = '<%=ContractStateEnum.HeTongJieShu.getValue()%>';
			if($("#contractstatus").val() == xinJianState){
				 $("#breakOffContract").css('display' ,'');  
				 $("#finishContract").css('display' ,'');  
				 $("#startContract").css('display' ,'');  
			} else if($("#contractstatus").val() == zhiXingZhongState){
				 $("#breakOffContract").css('display' ,'');  
				 $("#finishContract").css('display' ,'');  
			} else if($("#contractstatus").val() == heTongZhongZhiState){
				 $("#finishContract").css('display' ,'');  
				 $("#startContract").css('display' ,'');
			}
		}
	});
}

//初始化押金table
function initDepositTable(list){
	if(list){
		for(var i=0;i<=list.length;i++){
			trObj = list[i];
			rowCount = i;
			addTr();
			tds = $("#depositTable tr[class='contractTr'][name='tr"+i+"']")[0].children;
			for(var j=1;j<tds.length;j++){
				tdName = tds[j].attributes[1].value;
				if(trObj){
					tds[j].children[0].value = trObj[tdName];
				}
			}
		}
	}
}

//校验
function verify(){
	var flag = true;
	if($("#contractstartdate").val()==""){
		alert("合同开始时间不能为空！")
		return false;
	}
	if($("#contractenddate").val()==""){
		alert("合同结束时间不能为空！")
		return false;
	}
	if($("#contractstartdate").val()> $("#contractenddate").val()){
		alert("合同开始时间不能大于结束时间！");
		return  false;
	}
	
	 var regTime = /^(\d{1,2}(\.\d{1,2})?|99.99)$/;
	var parameterValue = document.getElementById("taxrate").value;
	var fla = regTime.test(parameterValue);
	if(!fla){
		alert("税率只能是大于0小于100的数字，允许保留两位小数")
		return false;
	} 
	
	if($("#number").val()==""){
		alert("合同编号不能为空！");
		return false;
	}
	if($("#number").val().lenght > 100){
		alert("合同编号长度不能超过100位字符！")
	}
	if($("#contracttype").val()==2){
		if($("#othercontractors").val()==""){
			alert("当合同类型为三方时,其他合同商名称不能为空！");
			return false;
		}
	}
	if($("#whetherhavedeposit").val()==1){
		if($("#depositpaymentdate").val()==""||$("#depositpaymentamount").val()==""||$("#depositpaymentperson").val()==""||$("#depositgatherperson").val()==""){
			alert("当有押金时，押金信息不能为空！");
			return false;
		}
	}
	if($("#contractdescription").val().lenght > 800){
		alert("合同详细描述不能大于800位字符！");
		return false;
	}
	return flag;
}

function checkAllTrCheckboxes(){
	var checked = $("#depositTable input[type='checkbox'][name='checkAllTrs']")[0].checked;
	var trChecks = $("#depositTable input[type='checkbox'][name='count']");
	$(trChecks).each(function() {
		$(this)[0].checked = checked;
	});
}

function account(data){
	regAccount = /^(\d{16}|\d{19})$/;
	if(data == "bank"){
		var accountValue = $("#collectionloanbankaccount").val();
		var flag = regAccount.test(accountValue);
		if(!flag){
			alert("请输入正确的银行卡号！")
			return false;
		}
	}else if(data == "expensebank"){
		var accountValue = $("#expensebankaccount").val();
		var flag = regAccount.test(accountValue);
		if(!flag){
			alert("请输入正确的银行卡号！")
			return false;
		}
	}
}

function queryVerify(){
	var flag = true;
	var pattern = new RegExp("[`'~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;|{}【】‘；：”“'。，、？\"]");
    var numberValue = $("#number").val();
 	var partyanameValue = $("#partyaname").val();
 	var marketingprincipal = $("#marketingprincipal").val();
 	var othercontractors = $("#othercontractors").val();
 	var contractdescription = $("#contractdescription").val();
	if(pattern.test(numberValue)||pattern.test(partyanameValue)||pattern.test(marketingprincipal)||pattern.test(othercontractors)||pattern.test(contractdescription)){
        alert("不允许输入特殊字符！")
        return false;
    }
	return flag;
}

 //打开对应的页面
function showBox(data){
	 $("#box_contant").html("");
	if(data == "add"){
		var str = '<div id="box_top_bg"></div>'+
		'	<div id="box_in_bg">'+
		'	<div id="box_form">'+
		'		<h1><div id="close_box" onclick="closeBox()"></div>创建合同</h1>'+
		'			<form method="post" onSubmit="addContract(this);return false;" action="<%=request.getContextPath()%>/customerContract/addCustomerContractfile;jsessionid=<%=session.getId()%>" id="addcallerForm" enctype="multipart/form-data">'+
		'		 	<table>'+
		'					<tr>'+
		'						<th align="left"><font color="red">*</font>编号:</th>'+
		'						<td><input type="text" id="number" name="number" maxlength="20" value="[自动生成]" style="width:150px"></input></td>'+
		'						<th align="left">合同状态:</th>'+
		'						<td>'+
		'							<input type="text" id="contractstatus" name="contractstatus" value="1" style="display: none"></input>'+		
		'							<input type="text"  value="新建" disabled="disabled"  style="background-color:#DCDCDC;width:150px"></input>'+		
		'		           		</td>'+
		'						<th align="left"><font color="red">*</font>合同日期范围:</th>'+
		'						<td>'+
		'							<input type="text" name="contractstartdate" id="contractstartdate"style="width:68px"  maxlength="10">'+
		'							至'+
		'							<input type="text" name="contractenddate" id="contractenddate"style="width:67px"  maxlength="10">'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">客户名称:</th>'+
		'						<td>'+
		'							<select id="customerid" name="customerid" style="width:155px">'+
		'								<%for(Customer br : customerList){ %>'+
		'										<option value="<%=br.getCustomerid() %>" ><%=br.getCustomername()%></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">合同类型:</th>'+
		'						<td><select id="contracttype" onclick="showOthercontractors();" name="contracttype" style="width:155px">'+
		'								<%for(ContracTypeEnum br : ContracTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">是否有押金:</th>'+
		'						<td>'+
		'							<select id="whetherhavedeposit" name ="whetherhavedeposit" onchange="show();" style="width:155px" >'+
		'								<%for(WhetherHaveDepositEnum br : WhetherHaveDepositEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">甲方全称:</th>'+
		'						<td><input type="text" name="partyaname" value="" id="partyaname" maxlength="20"style="width:150px" /></td>'+
		'						<th align="left">乙方全称</th>'+
		'						<td><input type="text" name="yifangquancheng" value="" id="yifangquancheng" maxlength="20" style="width:150px"/></td>'+
		'						<th align="left" id="contractors" style="display: none"><font color="red">*</font>其他合同商:</th>'+
		'						<td><input type="text" style="display: none" name="othercontractors" value="" id="othercontractors" maxlength="20" style="width:150px"/></td>'+
		'					</tr>'+			
		'					<tr>'+
		'						<th align="left">代收货款结算周期:</th>'+
		'						<td>'+
		'							<select id="loanssettlementcycle" name ="loanssettlementcycle" style="width:155px">'+
		'								<%for(SettlementPeriodEnum br : SettlementPeriodEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">货款结算方式:</th>'+
		'						<td>'+
		'							<select id="loansandsettlementway" name ="loansandsettlementway" style="width:155px">'+
		'								<%for(LoanTypeEnum br : LoanTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">派费结算周期:</th>'+
		'						<td>'+
		'							<select id="paifeisettlementcycle" name ="paifeisettlementcycle" style="width:155px">'+
		'								<%for(SettlementPeriodEnum br : SettlementPeriodEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">派费结算方式:</th>'+
		'						<td>'+
		'							<select id="paifeisettlementtype" name ="paifeisettlementtype" style="width:155px">'+
		'								<%for(LoanTypeEnum br : LoanTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">营销负责人:</th>'+
		'						<td>'+
		'							<input type="text" id="marketingprincipal" name="marketingprincipal" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">发票类型:</th>'+
		'						<td>'+
		'							<select id="invoicetype" name ="invoicetype" style="width:155px" >'+
		'								<%for(InvoiceTypeEnum br : InvoiceTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">税率(%):</th>'+
		'						<td>'+
		'							<input type="text" id="taxrate" name="taxrate" maxlength="20" value="0" style="width:150px"/>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">代收货款银行:</th>'+
		'						<td>'+
		'							<input type="text" id="collectionloanbank" name="collectionloanbank" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">代收货款银行账户:</th>'+
		'						<td>'+
		'							<input type="text" id="collectionloanbankaccount" onblur="account(\'bank\');" name="collectionloanbankaccount" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">费用银行:</th>'+
		'						<td>'+
		'							<input type="text" id="expensebank" name="expensebank" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">费用银行账户:</th>'+
		'						<td>'+
		'							<input type="text" id="expensebankaccount" onblur="account(\'expensebank\');" name="expensebankaccount" maxlength="20"style="width:150px" />'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">合同详细描述:</th>'+
		'						<td colspan="5">'+
		'							<textarea style="width:100%;height:60px;resize: none;" name="contractdescription" id="contractdescription" style="width:150px"></textarea>'+
		'						</td>'+
		'					</tr>'+
		'					  <tr>'+
		' 						<th align="left" for="fileField" >上传附件：</th>'+
		'						<td> '+
		'							<span id="swfupload-control">&nbsp;&nbsp;&nbsp;<input type="text" id="txtFileName"  disabled="true" style="border: solid 1px; background-color: #FFFFFF;width:150px" />'+
		'						</td>'+
		'						<td> '+
		'					<input type="button" id="button" /></span>*'+
		'						</td>'+
		'					</tr> '+
		'					 <tr id="firstDepositTr" >'+
		'						<th align="left"><font color="red">*</font>押金支付日期:</th>'+
		'						<td><input type="text" name="depositpaymentdate" id="depositpaymentdate" value="" style="width:150px"/></td>'+
		'						<th align="left"><font color="red">*</font>押金支付金额:</th>'+
		'						<td><input type="text" name="depositpaymentamount" value="" id="depositpaymentamount" maxlength="20"  style="width:150px"/></td>'+
		'						<th></th>'+
		'						<td></td>'+
		'					</tr>					'+
		'					<tr id="secondDepositTr">'+
		'						<th align="left"><font color="red">*</font>押金支付人:</th>'+
		'						<td><input type="text" name="depositpaymentperson" value="" id="depositpaymentperson" maxlength="20" style="width:150px"/></td>'+
		'						<th align="left"><font color="red">*</font>押金收取人:</th>'+
		'						<td><input type="text" name="depositgatherperson" value="" id="depositgatherperson" maxlength="20" style="width:150px" /></td>'+
		'						<th></th>'+
		'						<td></td>'+
		'					</tr>'+
		'			</table>'+
		'			<div align="center">'+
		'	 			<input type="submit" value="保存" align="center" class="button">'+
		'				<input type="button" value="返回" class="button" align="center" onclick="closeBox()"/>'+
		'			</div>'+
		'		</form>	'+
		'	</div>'+
		'	</div>';
		$("#box_contant").append(str);
		$("#contractstartdate").datepicker();
		$("#contractenddate").datepicker();
		$("#depositpaymentdate").datepicker();
		shangchuan();
		$('#number').focus(function(){
			$('#number').val('');
		}).blur(function(){
			$('#number').val('[自动生成]');
		});
		$("#alert_box").show();
		centerBox();
	}else if(data == "update"){
		var contractId = $("#contractid").val()
		 if(contractId == ""){
				alert("请先选择需操作的合同!");
				return false;
		} 
		var str='<div id="box_top_bg"></div>'+
		'	<div id="box_in_bg">'+
		'	<div id="box_form">'+
		'		<h1><div id="close_box" onclick="closeBox()"></div>查看/修改合同</h1>'+
		'			<form action="<%=request.getContextPath()%>/customerContract/update" id="updateContract">'+
		'			<input type="hidden" name="depositInformationStr" id="depositInformationStr"/>'+
	  	'		<table>'+
		'				<input type="button" value="中止" id="breakOffContract" style="display:none;" class="input_button2" onclick="changeContractState('+'<%=ContractStateEnum.HeTongZhongZhi.getValue()%>'+')"/>'+
		'				<input type="button" value="结束" id="finishContract" style="display:none;" class="input_button2" align="center" onclick="changeContractState('+'<%=ContractStateEnum.HeTongJieShu.getValue()%>'+')"/>'+
		'				<input type="button" value="开始执行" id="startContract" style="display:none;" class="input_button2" onclick="changeContractState('+'<%=ContractStateEnum.ZhiXingZhong.getValue()%>'+')"/>'+
		'		</table>'+  
		'		 	<table>'+
		'					<tr>'+
		'						<input type="hidden" name="id" id="id" >'+
		'						<th align="left">编号:</th>'+
		'						<td><input type="text" id="number" name="number" maxlength="20"  readonly="readonly" style="background-color:#DCDCDC;width:150px"></input></td>'+
		'						<th align="left">合同状态:</th>'+
		'						<td>'+
		'							<select id="contractstatus" name ="contractstatus" disabled="disabled" style="background-color:#DCDCDC;width:155px" readonly="readonly">'+
		'								<%for(ContractStateEnum br : ContractStateEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'		           		</td>'+
		'						<th align="left">合同日期范围:</th>'+
		'						<td>'+
		'							<input type="text" name="contractstartdate"  id="contractstartdate" style="display: none"  maxlength="10">'+
		'							<input type="text" name="contractenddate" id="contractenddate" style="display: none"  maxlength="10">'+
		'							<input type="text" name="contractdate" id="contractdate"  maxlength="10" disabled="disabled" style="background-color:#DCDCDC;width:150px">'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">客户名称:</th>'+
		'						<td>'+
		'							<select id="customerid" name="customerid"  disabled="disabled" style="background-color:#DCDCDC;width:155px">'+
		'								<%for(Customer br : customerList){ %>'+
		'									<option value="<%=br.getCustomerid() %>" ><%=br.getCustomername()%></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">合同类型:</th>'+
		'						<td><select id="contracttype" onclick="showOthercontractors();" name="contracttype"style="background-color:#DCDCDC;width:155px"  disabled="disabled">'+
		'								<%for(ContracTypeEnum br : ContracTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">是否有押金:</th>'+
		'						<td>'+
		'							<select id="whetherhavedeposit" name ="whetherhavedeposit" onchange="show();" style="background-color:#DCDCDC;width:155px" disabled="disabled" >'+
		'								<%for(WhetherHaveDepositEnum br : WhetherHaveDepositEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">甲方全称:</th>'+
		'						<td><input type="text" name="partyaname"  id="partyaname" maxlength="20" style="width:150px"/></td>'+
		'						<th align="left">乙方全称</th>'+
		'						<td><input type="text" name="yifangquancheng"  id="yifangquancheng" maxlength="20" style="width:150px"/></td>'+
		'						<th align="left" id="contractors" style="display: none">其他合同商:</th>'+
		'						<td><input type="text" style="display: none" name="othercontractors"  id="othercontractors" maxlength="20" style="width:150px"/></td>'+
		'					</tr>'+		
		'					<tr>'+
		'						<th align="left">代收货款结算周期:</th>'+
		'						<td>'+
		'							<select id="loanssettlementcycle" name ="loanssettlementcycle" style="width:155px">'+
		'								<%for(SettlementPeriodEnum br : SettlementPeriodEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">货款结算方式:</th>'+
		'						<td>'+
		'							<select id="loansandsettlementway" name ="loansandsettlementway" style="width:155px">'+
		'								<%for(LoanTypeEnum br : LoanTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">派费结算周期:</th>'+
		'						<td>'+
		'							<select id="paifeisettlementcycle" name ="paifeisettlementcycle" style="width:155px">'+
		'								<%for(SettlementPeriodEnum br : SettlementPeriodEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">派费结算方式:</th>'+
		'						<td>'+
		'							<select id="paifeisettlementtype" name ="paifeisettlementtype" style="width:155px">'+
		'								<%for(LoanTypeEnum br : LoanTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">营销负责人:</th>'+
		'						<td>'+
		'							<input type="text" id="marketingprincipal" name="marketingprincipal" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">发票类型:</th>'+
		'						<td>'+
		'							<select id="invoicetype" name ="invoicetype" style="width:155px">'+
		'								<%for(InvoiceTypeEnum br : InvoiceTypeEnum.values()){ %>'+
		'									<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'								<%} %>'+
		'							</select>'+
		'						</td>'+
		'						<th align="left">税率(%):</th>'+
		'						<td>'+
		'							<input type="text" id="taxrate" name="taxrate" maxlength="20" style="width:155px"/>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">代收货款银行:</th>'+
		'						<td>'+
		'							<input type="text" id="collectionloanbank" name="collectionloanbank" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">代收货款银行账户:</th>'+
		'						<td>'+
		'							<input type="text" id="collectionloanbankaccount" name="collectionloanbankaccount" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">费用银行:</th>'+
		'						<td>'+
		'							<input type="text" id="expensebank" name="expensebank" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'						<th align="left">费用银行账户:</th>'+
		'						<td>'+
		'							<input type="text" id="expensebankaccount" name="expensebankaccount" maxlength="20" style="width:150px"/>'+
		'						</td>'+
		'					</tr>'+
		'					<tr>'+
		'						<th align="left">合同详细描述:</th>'+
		'						<td colspan="5">'+
		'							<textarea style="width:100%;height:60px;resize: none;" name="contractdescription" id="contractdescription"></textarea>'+
		'						</td>'+
		'					</tr>'+
		'					 <tr id="contractFile" style="display: none">'+
		'						<th align="left">合同附件:</th>'+
		'						<td colspan="5">'+
		'							<span><a href="" id="file" >附件下载</a></span>'+
		'						</td>'+
		'					</tr>'+
		'					<tr id="firstDepositTr" style="display: none">'+
		'						<th align="left">押金支付日期:</th>'+
		'						<td><input type="text" name="depositpaymentdate" id="depositpaymentdate" style="background-color:#DCDCDC;width:150px" readonly="readonly"/></td>'+
		'						<th align="left">押金支付金额:</th>'+
		'						<td><input type="text" name="depositpaymentamount"  id="depositpaymentamount" maxlength="20" style="background-color:#DCDCDC;width:150px" readonly="readonly" /></td>'+
		'						<th></th>'+
		'						<td></td>'+
		'					</tr>					'+
		'					<tr id="secondDepositTr" style="display: none">'+
		'						<th align="left">押金支付人:</th>'+
		'						<td><input type="text" name="depositpaymentperson" style="background-color:#DCDCDC;width:150px" id="depositpaymentperson" maxlength="20"  readonly="readonly"/></td>'+
		'						<th align="left">押金收取人:</th>'+
		'						<td><input type="text"  name="depositgatherperson" id="depositgatherperson" maxlength="20" style="width:150px"/></td>'+
		'						<th></th>'+
		'						<td></td>'+
		'					</tr>'+
		'					<tr id="depositMessage" style="display: none">'+
		'						<td colspan="6">'+
		'							<table id="depositTable" border="1" style="border-collapse:collapse;">'+
		'								<tbody>'+
		'									<tr>'+
		'										<td align="center"><input type="checkbox" name="checkAllTrs" onclick="javascript:checkAllTrCheckboxes();"/></td>'+
		'										<td align="center"  width="120px">押金退换日期</td>'+
		'										<td align="center"  width="120px">押金退还金额</td>'+
		'										<td align="center"  width="120px">退款人</td>'+
		'										<td align="center"  width="120px">收款人</td>'+
		'										<td align="center"  width="180px">备注</td>'+
		'									</tr>'+
		'								</tbody>'+
		'							</table>'+
		'						</td>'+
		'					</tr> '+
		'					<tr id="depositButton" style="display: none">'+
	'						<td colspan="6">'+
	'							<input type="button" value="添加" class="input_button2"align="center" onclick="addTr()"/>'+
	'							<input type="button" value="移除" class="input_button2"align="center" onclick="deleteTr()"/>'+
	'						</td>'+
	'					</tr>'+
		'			</table>'+
		'		</form>	'+
		'		</div>'+
		'		<div align="center">'+
		'			<input type="button" value="保存" class="button" align="center" onclick="update()">'+
		'			<input type="button" value="返回" class="button" align="center" onclick="closeBox()"/>'+
		'		</div>'+
		'		</div>';
			
		$("#box_contant").append(str);
		$("#contractstartdate").datepicker();
		$("#contractenddate").datepicker();
		demandContract();
		$("#alert_box").show();
		centerBox();
	}else if(data == "query"){
		var str = '<div id="box_top_bg"></div>'+
		'	<div id="box_in_bg">'+
		'	<div id="box_form">'+
		'		<h1><div id="close_box" onclick="closeBox()"></div>查询</h1>'+
		'			<form action="<%=request.getContextPath()%>/customerContract/customerContractList" id="queryContract">'+
		'		 	<table>'+
		'					<tr>'+
		'				<th align="left">编号:</th>'+
		'				<td>'+
		'					<input type="text"  id="number" name="number"  style="width:150px"/>'+
		'				</td>'+
		'				<th align="left">合同状态:</th>'+
		'				<td>'+
		'					<select id="contractstatus" name ="contractstatus"  style="width:155px">'+
		'				<option value="0" selected>----全部----</option>'+
		'						<%for(ContractStateEnum br : ContractStateEnum.values()){ %>'+
		'							<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'						<%} %>'+
		'					</select>'+
		'				</td>'+
		'		</tr>'+
		'		<tr>'+
		'		<th align="left">客户名称:</th>'+
		'		<td>'+
		'			<select id="customerid" name="customerid" style="width:155px">'+
		'				<option value="0" selected>----全部----</option>'+
		'				<%for(Customer br : customerList){ %>'+
		'					<option value="<%=br.getCustomerid() %>" ><%=br.getCustomername()%></option>'+
		'				<%} %>'+
		'			</select>'+
		'		</td>'+
		'		<th align="left"> 甲方全称：</th>'+
		'			<td>'+
		'				<input type="text" id="partyaname" name="partyaname" style="width:150px"/>'+
		'			</td>'+
		'		</tr>'+
		'		<tr>'+
		'			<th align="left">营销负责人:</th>'+
		'			<td>'+
		'				<input type="text" id="marketingprincipal" name="marketingprincipal" style="width:150px"/>'+
		'			</td>'+
		'			<th align="left">其他合同商:</th>'+
		'			<td>'+
		'				<input type="text" id="othercontractors" name="othercontractors" style="width:150px"/>'+
		'			</td>'+
		'		</tr>'+
		'		<tr>'+
		'			<th align="left">合同详细描述:</th>'+
		'			<td>'+
		'				<input type="text" id="contractdescription" name="contractdescription"  style="width:150px"/>'+
		'			</td>'+
		'			<th align="left">货款类型:</th>'+
		'			<td>'+
		'				<select id="loansandsettlementway" name ="loansandsettlementway" style="width:155px">'+
		'				<option value="0" selected>----全部----</option>'+
		'					<%for(LoanTypeEnum br : LoanTypeEnum.values()){ %>'+
		'						<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'					<%} %>'+
		'				</select>'+
		'			</td>'+
		'		</tr>'+
		'		<tr>'+
		'			<th align="left">合同开始时间:</th>'+
		'			<td>'+
		'				<input type="text" style="width:68px" id="createStatrtTime" name="createStatrtTime"  />至<input type="text"style="width:67px" id="createEndTime" name="createEndTime" value="${createEndTime}"/>'+
		'			</td>'+
		'			<th align="left">合同结束时间:</th>'+
		'			<td>'+
		'				<input type="text" style="width:68px" id="overStartTime" name="overStartTime" value="${overStartTime}"/>至<input type="text"style="width:67px" id="overEndTime" name="overEndTime" value="${overEndTime}"/>'+
		'			</td>'+
		'		</tr>'+
		'		<tr>'+
		'			<th align="left">是否有押金:</th>'+
		'			<td>'+
		'				<select id="whetherhavedeposit" name ="whetherhavedeposit" style="width:155px">'+
		'				 <option value="0" selected>----全部----</option>'+
		'					<%for(WhetherHaveDepositEnum br : WhetherHaveDepositEnum.values()){ %>'+
		'						<option value="<%=br.getValue() %>" ><%=br.getText() %></option>'+
		'					<%} %>'+
		'				</select>'+
		'			</td>'+
		'			<th align="left">排序:</th>'+
		'			<td>'+
		'				<select style="width:80px" id="sort" name ="sort" >'+
		'					<option value="number" >编号</option>'+
		'					<option value="contractstatus" >合同状态</option>'+
		'					<option value="contracttype" >合同类型</option>'+
		'					<option value="partyaname" >甲方名称</option>'+
		'				</select>'+
		'				<select style="width:70px" id="method" name ="method" >'+
		'					<option value="ASC" >升序</option>'+
		'					<option value="DESC" >降序</option>'+
		'				</select>'+
		'			</td>'+
		'		</tr>'+
		'			</table>'+
		'		</form>	'+
		'		</div>'+
		'		<div align="center">'+
		'			<input type="button" value="查询" class="button" align="center" onclick="query()">'+
		'			<input type="button" value="关闭" class="button" align="center" onclick="closeBox()"/>'+
		'		</div>'+
		'		</div>';
		$("#box_contant").append(str);
		$("#createStatrtTime").datepicker();
		$("#createEndTime").datepicker();
		$("#overStartTime").datepicker();
		$("#overEndTime").datepicker();
		setCondition();
		$("#alert_box").show();
		centerBox();
	}
}

</script>
</head>

<body style="background:#f5f5f5">
<!-- 弹出框开始 -->
<div id="alert_box" style="display:none;" align="center" tip="1">
<div id="box_bg"></div>
<div id="box_contant">
	</div>
</div>
	
<div class="right_box">
	<div class="inputselect_box">
		<table>
			<td><button id="add_buttonhhhhh" onclick="showBox('add');"  class="input_button2">新增</button></td>
			<td><button id="edit_but" onclick="showBox('update');"class="input_button2">查看/修改</button></td>
			<td><button id="remove_button" onclick="deleteContract();" class="input_button2">删除</button></td>
			<td><button id="query_button" onclick="showBox('query');" class="input_button2">查询</button></td>
		</table>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table123">
				<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">编号</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">合同状态</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">客户名称</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">合同日期范围</td>
						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">贷款类型</td>
						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">营销负责人</td>
						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">合同类型</td>
						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">甲方全称</td>
						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">其他合同商</td>
						
					</tr>
					<%if(customerContractList!=null){ %>
					   <% for(CustomerContractManagement cc : customerContractList){ %>
					<tr onclick="setId('<%=cc.getId()%>','<%=cc.getContractstatus()%>')">
					 	<td width="10%" align="center" valign="middle"><%=cc.getNumber() %></td>
					 	<td width="10%" align="center" valign="middle"><%=ContractStateEnum.getByValue(cc.getContractstatus()) %></td>
					 	 <%for(Customer br : customerList){ %>
						  <%if(cc.getCustomerid()==br.getCustomerid()){ %>
								<td width="10%" align="left" valign="middle"><%=br.getCustomername() %></td>
						 <%}} %>
						<td width="15%" align="center" valign="middle"><%=(cc.getContractstartdate()+"至"+cc.getContractenddate()) %></td>
						<td width="9%" align="center" valign="middle"><%=LoanTypeEnum.getByValue(cc.getLoansandsettlementway()) %></td>
						<td width="9%" align="center" valign="middle"><%=cc.getMarketingprincipal() %></td>
						<td width="9%" align="center" valign="middle"><%=ContracTypeEnum.getByValue(cc.getContracttype()) %></td>
						<td width="9%" align="center" valign="middle" ><%=cc.getPartyaname() %></td>
						<td width="9%" align="center" valign="middle" ><%=cc.getOthercontractors() %></td>
					</tr>
					<%} }%> 
					
				</table>
				<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	</div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<input type="hidden" id="contractid" value=""/>
	<input type="hidden" id="customerContractState" value=""/>
</body>
</html>
