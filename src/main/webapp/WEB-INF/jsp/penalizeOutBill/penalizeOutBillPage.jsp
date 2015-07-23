<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.enumutil.PunishBillStateEnum"%>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>对外赔付账单</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/2.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" type="text/css"  />
<script src="${pageContext.request.contextPath}/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/workorder/csPushSmsList.js" type="text/javascript"></script>

<script type="text/javascript">

$(function(){
	/* $("#customerselect").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' }); */
	$('#add').dialog('close');
	$('#find').dialog('close');
	
	$("table#gd_table tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	if("${msg}" != ""){
		alert("${msg}");
	}
	if("${updatemsg}" != ""){
		alert("${updatemsg}");
	}
	//数据回显
	$("#find select[name='billstate']").val("${billstate}");
	$("#find select[name='customerid']").val("${customerid}");
	$("#find select[name='compensatebig']").val("${compensatebig}");
	$("#find select[name='sort']").val("${sort}");
	$("#find select[name='method']").val("${method}");
	$("#updateForm select[name='billstate']").val("${bill.billstate}");
	$("#updateForm select[name='verifier']").val('${bill.verifier}');
	$("#updateForm select[name='verificationperson']").val('${bill.verificationperson}');
	$("#deleteBillForm input[name='billbatches']").val("${billbatches}");
	$("#deleteBillForm input[name='billstate']").val("${billstate}");
	$("#deleteBillForm input[name='billCreationStartDate']").val("${billCreationStartDate}");
	$("#deleteBillForm input[name='billCreationEndDate']").val("${billCreationEndDate}");
	$("#deleteBillForm input[name='billVerificationStrartDate']").val("${billVerificationStrartDate}");
	$("#deleteBillForm input[name='billVerificationEndDate']").val("${billVerificationEndDate}");
	$("#deleteBillForm input[name='customerid']").val("${customerid}");
	$("#deleteBillForm input[name='compensatebig']").val("${compensatebig}");
	$("#deleteBillForm input[name='sort']").val("${sort}");
	$("#deleteBillForm input[name='method']").val("${method}");
	$("#updatePageForm input[name='id']").val("${bill.id}");
	$("#queryPenalizeInsideListForm select[name='compensatebig']").val("${satebig}");
	$("#queryPenalizeInsideListForm select[name='compensatesmall']").val("${satesmall}");
});
function queryPenalizeOutBill(){
	$('#find').dialog('open')
}

//赔付大类与赔付小类联动
function findbig()
{ var parent=$("#penalizesmall").find("option:selected").attr("id");
	$("#penalizebig option[value='"+parent+"']").attr("selected","selected");
} 
function find()
{ var parent=$("#small").find("option:selected").attr("id");
	$("#big option[value='"+parent+"']").attr("selected","selected");
} 
function findsma(id)
{
	var dmpurl=$("#dmpurl").val();
$("#small").empty();
$.ajax({
	type:"post",
	url:dmpurl+"/penalizeType/findsmall",
	data:{"id":id},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择</option>";

			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].id+"' id='"+data[i].parent+"'>"+data[i].text+"</option>";
			}
			
			$("#small").append(optstr);
		}
	}});
}
function findsmall(id)
{
	var dmpurl=$("#dmpurl").val();
$("#penalizesmall").empty();
$.ajax({
	type:"post",
	url:dmpurl+"/penalizeType/findsmall",
	data:{"id":id},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择</option>";

			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].id+"' id='"+data[i].parent+"'>"+data[i].text+"</option>";
			}
			
			$("#penalizesmall").append(optstr);
		}
	}});
}
function checkgeneration(){
	$("#generationloses").checked
	var chkBoxes = $("#add input[type='checkbox'][id='generationloses']");
	if(chkBoxes[0].checked == true){
		$("#add input[name='checkbox1']").val("1");
		$("#punishInside").css("display","");
		$("#punishInside1").css("display","");
	}else {
		$("#add input[name='checkbox1']").val("");
		$("#punishInside").css("display","none");
		$("#punishInside1").css("display","none");
		$("#punishInsideRemark").val("");
		$("#batchstate").val("");
		$("#dutypersonid").val("");
		$("#sumPrice").val("");
	}
}

function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}
//添加
function addbill(){
	if(!verify()){
		return false;
	}
	var compensateodd = $("#add #compensateodd").val();
	if(compensateodd == "如多个赔付单回车隔开"){
		var compensateodd = $("#add #compensateodd").val("");
	}
	var compensateexplain = $("#add #compensateexplain").val();
	if(compensateexplain == "不超过100字"){
		var compensateexplain = $("#add #compensateexplain").val("");
	}
	var punishInsideRemark = $("#add #punishInsideRemark").val();
	if(punishInsideRemark == "不超过100字"){
		var punishInsideRemark = $("#add #punishInsideRemark").val("");
	}
	var sumPrice = $("#add #sumPrice").val();
	if(sumPrice == "默认为赔付金额,可修改"){
		var sumPrice = $("#add #sumPrice").val("0");
	}
	$.ajax({
		type : "POST",
		url : $("#creationfrom").attr("action"),
		data : $("#creationfrom").serialize(),
		dataType : "json",
		success : function(data) {
			if(data != undefined){
				updateBill(data);
			}
		}
	});
}

//责任机构下拉框联动
function selectbranchUsers(){
	var dutyPersonEle = $("#creationfrom select[name='dutypersonid']");
	var url = "<%=request.getContextPath()%>/abnormalOrder/getbranchusers";
	$.ajax({
		url:url,
		type:"POST",
		data:"dutybranchid="+$("#creationfrom select[name='batchstate']").val(),
	dataType:'json',
	success:function (json){
		dutyPersonEle.empty();
		var optstr = "<option value ='0'>请选择责任人</option>";
		dutyPersonEle.append(optstr);// 添加下拉框的option
		for (var j = 0; j < json.length; j++) {
			optstr = "<option value='"+ json[j].userid +"'>"+json[j].realname+"</option>";
			dutyPersonEle.append(optstr);
		}
	}
	});
}
//删除
function deleteBill(){
	var id = $("#updatePageForm input[name='id']").val();
	if(id==""){
		alert("请选择需要删除的记录！")
		return false;
	}
	var state = $("#updatePageForm input[name='billstate']").val();
	var weishenhe = "<%=PunishBillStateEnum.WeiShenHe.getValue()%>";
	if(state != weishenhe){
		alert("只有未审核状态的合同才能进行删除!");
		return false;
	}
	if(confirm("确定要删除吗？")){
		$.ajax({
			type : "POST",
			url : '${ctx}/penalizeOutBill/deletePenalizeOutBill',
			data : {id : id},
			dataType : "json",
			success : function(data) {
				if (data.errorCode == 0) {
					alert(data.error);
					$("#deleteBillForm").submit();
				}
			}
		});
	}
}
//-----------------------------查看/修改----------------------------------------
/* function updatePage(){
	var id = $("#updatePageForm input[name='id']").val();
	$("#updatePageForm").submit();
} */
function updateBill(data){
	if(data == undefined){
		var id = $("#updatePageForm input[name='id']").val();
		if(id ==""){
			alert("请选择需要查看/修改的记录！")
			return false;
		}
	}else{
		$("#updatePageForm input[name='id']").val(data)
	}
	$("#updatePageForm").submit();
}

function addPenalizeInside(){
	var id = $("#updateForm input[type='hidden'][name='id']").val(); 
	$("#addPenalizeInsideForm input[name='id']").val(id);
	$("#addPenalizeInsideForm").submit();
}
function queryPenalizeInsideList(){
	$("#queryPenalizeInsideListForm").submit();
}
function addPenalizeInsideList(){
	var chkBoxes = $("#penalizeInsideListTable input[type='checkbox'][name='checkBox']");
	var compensateodd = "";
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			//$(this).parent().parent().remove();
			compensateodd = $(this).val()+","+compensateodd;
		}
	}); 
	compensateodd = compensateodd.substring(0,compensateodd.length-1);
	var id = $("#queryPenalizeInsideListForm input[type='hidden'][name='id']").val(); 
	
	$("#penalizeInsideListForm input[name='id']").val(id);
	$("#penalizeInsideListForm input[name='compensateodd']").val(compensateodd);
	$("#penalizeInsideListForm").submit();
}

function deletePenalizeInside(){
	var chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			$(this).parent().parent().remove();
		}
	}); 
}

function updatePunishinsideBill(){
	if($("#compensateexplain").val().length > 100){
		alert("赔付说明不允许超过100位字符！")
		return false;
	}
	var chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	var compensateodd = "";
	$(chkBoxes).each(function() {
		compensateodd = $(this).val()+","+compensateodd;
	}); 
	compensateodd = compensateodd.substring(0,compensateodd.length-1);
	$("#updateForm input[type='hidden'][name='compensateodd']").val(compensateodd);
	$("#updateForm select[name='billstate']").attr("disabled",false);
	$("#updateForm select[name='verifier']").attr("disabled",false);
	$("#updateForm select[name='verificationperson']").attr("disabled",false);
	$("#updateForm").submit();
}
function findbigSort(self,chufaobject){ 
	var parent=$(self).find("option:selected").attr("title");
	$("#addForm select[name='punishbigsort'] option[value='"+parent+"']").attr("selected","selected");
}

function setId(data,billstate){
	$("#updatePageForm input[name='id']").val(data);
	$("#updatePageForm input[name='billstate']").val(billstate);
}

function allchecked()
{ var ids="";
	$("[id=id]").each(
			function()
			{
				if($(this).attr('checked')=='true'||$(this).attr('checked')=='checked')
					{
					ids+=","+$(this).val();
					}
			});
	if(ids.indexOf(',')!=-1)
		{
		ids=ids.substr(1);
		}
	
	var dmpurl=$("#dmpurl").val();
	if(window.confirm("确定要移除吗！")&&ids.length>0){
	$.ajax({
		type:"post",
		url:dmpurl+"/salaryFixed/delete",
		data:{"ids":ids},
		dataType:"json",
		success:function(data){
			if(data.counts>0){
				alert("成功移除"+data.counts+"记录");
				}
			$("#searchForm").submit();
			}
		});
	}
}
/* function checkall()
{ var checked=$("#all")[0].checked;
	$("[id=id]").each(function(){
		var e = $(this)[0];
		if(checked=='true'||checked=='checked')
		{
			e['checked'] = checked;
			//$(e).attr('checked',checked);
			}
		else {
			//$(e).removeAttr('checked');
			e['checked'] = checked;
		}
	});
} */


function addPunishBillPage(){
	$('#add').dialog('open');
	
	$("#add textarea[name='compensateodd']").focus(function(){
		if($("#add textarea[name='compensateodd']").val() && $("#add textarea[name='compensateodd']").val() == '如多个赔付单回车隔开'){
			$("#add textarea[name='compensateodd']").val('');
		}
	}).blur(function(){
		if(!$("#add textarea[name='compensateodd']").val()){
			$("#add textarea[name='compensateodd']").val('如多个赔付单回车隔开');
		}
	});
	
	$("#add textarea[name='compensateexplain']").focus(function(){
		if($("#add textarea[name='compensateexplain']").val() && $("#add textarea[name='compensateexplain']").val() == '不超过100字'){
			$("#add textarea[name='compensateexplain']").val('');
		}
	}).blur(function(){
		if(!$("#add textarea[name='compensateexplain']").val()){
			$("#add textarea[name='compensateexplain']").val('不超过100字');
		}
	});
	$("#add textarea[name='punishInsideRemark']").focus(function(){
		if($("#add textarea[name='punishInsideRemark']").val() && $("#add textarea[name='punishInsideRemark']").val() == '不超过100字'){
			$("#add textarea[name='punishInsideRemark']").val('');
		}
	}).blur(function(){
		if(!$("#add textarea[name='punishInsideRemark']").val()){
			$("#add textarea[name='punishInsideRemark']").val('不超过100字');
		}
	});
	$('#sumPrice').focus(function(){
		$('#sumPrice').val('');
	}).blur(function(){
		$('#sumPrice').val('默认为赔付金额,可修改');
	});
}
function changeBillState(state){
	var myDate = new Date();
	var date = myDate.format("yyyy-MM-dd HH:mm:ss");
	if(state){
		if(state == 'ShenHe'){
			 if(confirm("是否确认审核?")){
				$("#updateForm select[name='billstate']").val('${yiShenHeState}');
				$("#updateForm input[name='checktime']").val(date);
				$("#updateForm select[name='verifier']").val('${userid}');
			 }
		} else if(state == 'QuXiaoShenHe'){
			if(confirm("是否确认取消审核?")){
				$("#updateForm select[name='billstate']").val('${weiShenHeState}');
				$("#updateForm input[name='checktime']").val('');
				$("#updateForm select[name='verifier']").val('');
			}
		} else if(state == 'HeXiaoWanCheng'){
			if(confirm("是否确认核销完成?")){
				$("#updateForm select[name='billstate']").val('${yiHeXiaoState}');
				$("#updateForm input[name='verificationdate']").val(date);
				$("#updateForm select[name='verificationperson']").val('${userid}');
			}
		} else if(state == 'QuXiaoHeXiao'){
			if(confirm("是否确认取消核销?")){
				$("#updateForm select[name='billstate']").val('${yiShenHeState}');
				$("#updateForm input[name='verificationdate']").val('');
				$("#updateForm select[name='verificationperson']").val('');
			}
		}
	}
}
function verify(){
	var flag = true;
	if($("#penalizebig").val() == ""){
		alert("赔付大类为必选项！");
		return false;
	}
	var pattern = new RegExp("[`'~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;|{}【】‘；：”“'。，、？\"]");
	var compensateodd = $("#compensateodd").val();
	if(pattern.test(compensateodd)){
		alert("单号中不允许含有特殊字符，多个单号请以回车分隔！")
		return false;
	}
	if($("#compensateexplain").val().length >100){
		alert("赔付说明长度不允许超过100位字符！");
		return false;
	}
	if($("#punishInsideRemark").val().length >100){
		alert("扣罚说明长度不允许超过100位字符！");
		return false;
	}
	return flag;
}
</script>
</head>

<body style="background:#eef9ff">
<input type="hidden" id="dmpurl" value="${pageContext.request.contextPath}" />
<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 60%">
			    <tr>
			    <td>
			    <input class="input_button2" type="button" onclick="addPunishBillPage();" value="新增"/>
			    <input class="input_button2" type="button" onclick="updateBill();" value="查看/修改"/>
			    <input class="input_button2" type="button" onclick="deleteBill();" value="删除"/>
			    <input class="input_button2" type="button" onclick="queryPenalizeOutBill();" value="查询"/>
			    </td>
			    </tr>
		 </table>
	</div>


	<div class="right_title">
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div style="overflow: auto;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
		<!-- <td align="center" valign="middle"style="font-weight: bold;"><input type="checkbox" name="checkAll" onclick="checkAll('gd_table')"/></td> -->
		<td align="center" valign="middle"style="font-weight: bold;"> 账单批次</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 账单状态</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 客户名称</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 赔付大类 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 赔付小类 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 赔付金额 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 创建人 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 创建日期 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 审核人 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 审核日期 </td>
	</tr>
	<c:forEach items="${billList}" var="bill">
	<tr onclick="setId(${bill.id},${bill.billstate})">
		<%-- <td align="center" valign="middle" ><input type="checkbox" name="checkBox" value="${bill.id}" /></td> --%>
		<td align="center" valign="middle" >${bill.billbatches}</td>
		<td align="center" valign="middle" >
			<c:forEach items="${PunishBillStateEnum}" var="state">
				<c:if test="${bill.billstate==state.value}">${state.text }</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${customerList}" var="customer">
   				<c:if test="${bill.customerid==customer.customerid}">
					${customer.customername}
				</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${penalizebigList}" var="pl">
				<c:if test="${bill.compensatebig==pl.id}">${pl.text}</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${penalizesmallList}" var="small">
				<c:if test="${bill.compensatesmall==small.id}">${small.text}</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			${bill.compensatefee}
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${userList}" var="user">
				<c:if test="${bill.founder==user.userid}">${user.realname }</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			${bill.createddate}
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${userList}" var="user">
				<c:if test="${bill.verifier==user.userid}">${user.realname }</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			${bill.checktime}
		</td>
	</tr>
	</c:forEach>
	</table>
	</div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
	</div>
</div>
		<div class="iframe_bottom"> 
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillPage/1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillPage/${page_ob.previous<1?1:page_ob.previous}');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillPage/${page_ob.next<1?1:page_ob.next }');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillPage/${page_ob.maxpage<1?1:page_ob.maxpage}');$('#searchForm').submit();" >最后一页</a>
					　共${page_ob.maxpage}页　共${page_ob.total}条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<c:forEach var="i" begin="1" end="${page_ob.maxpage}">
							<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
							</c:forEach>
							</select>页
					</td>
				</tr>
			</table>
		</div>
	
<!-- 新增层显示 -->
<div  id="add" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save,modal:true'" style="width:700px;height:350px;">
	<form action="${ctx}/penalizeOutBill/addPenalizeOutBill" method="post" id="creationfrom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         		<tr >
		         	<td colspan="4" align="left" valign="bottom">
		         	<input type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');"/>
		         	<input type="button" class="input_button2" onclick="addbill();" value="保存"/>
		         	</td>
		         		<td nowrap="nowrap" align="right" rowspan="3" valign="bottom">赔付单号：</td>
	         		<td nowrap="nowrap"  rowspan="3" valign="bottom" >
         		 		<textarea id="compensateodd" name ="compensateodd" style="width: 100%" rows="3">如多个赔付单回车隔开</textarea>
         		 	</td>
	         	</tr>
         		<tr >
	         		<td align="right" nowrap="nowrap" style="width: 10%;">赔付大类：</td>
	         		<td nowrap="nowrap" style="width: 20%;">
	         			<select style="width: 100%" id="penalizebig" name="compensatebig" onchange="findsmall($(this).val())">
							<option value ="">请选择</option>
							<c:forEach items="${penalizebigList}" var="big" >
								<option value="${big.id}">${big.text}</option>
							</c:forEach>
						</select>
         			</td>
	         		<td nowrap="nowrap" align="right" style="width: 10%;">赔付小类：</td>
	         		<td nowrap="nowrap" style="width: 20%;">
	         			<select style="width: 100%" id="penalizesmall" name="compensatesmall" onchange="findbig()">
							<option value ="0">请选择</option>
							<c:forEach items="${penalizesmallList}" var="small">
								<option value="${small.id}"  id="${small.parent }">${small.text}</option>
							</c:forEach>
						</select>
	         		</td>
         	</tr>
         <!--  -->
         	<tr >
         		<td nowrap="nowrap" align="right" >客户名称：</td>
         		<td nowrap="nowrap"  >
         			 <select  id="customerselect" name="customerid" style="width: 100%;" >
						<c:forEach items="${customerList}" var="cus">
				          <option value="${cus.customerid}"  ${cus.customerid==customerid?'selected=selected':'' }>${cus.customername}</option>
				        </c:forEach>
         			</select>
         		</td>
         		<td nowrap="nowrap" align="right">赔付单创建日期：</td>
         		<td nowrap="nowrap">
	         	 <input type="text" name="creationStartDate"  class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
   	       		 <input type="text" name="creationEndDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         		<tr><td>&nbsp;</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">赔付说明：</td>
         		<td nowrap="nowrap" colspan="6" >
			   	 	<textarea rows="3"  id="compensateexplain" name="compensateexplain" style="width: 100%;resize: none;" value="">不超过100字</textarea>
		        </td>
         	</tr>
         	</tr>
         		<tr><td>&nbsp;</td>
         	</tr>
         	<tr>
         		<td align="left" nowrap="nowrap" colspan="6">
         			<input type="checkbox" id="generationloses" name="generationloses"  align="left" onclick="checkgeneration();">同时生成对内扣罚账单</input>
         			<input type="hidden" name="checkbox1" ></input>
         		</td>
         	</tr>
         	</tr>
         		<tr><td>&nbsp;</td>
         	</tr>
         	<tr style="display: none" id="punishInside">
         		<td align="right" nowrap="nowrap">责任机构:</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select name="batchstate" style="width: 100%;" onchange="selectbranchUsers();">
	         			<c:forEach items="${branchList}" var="branch">
	         				<option value="${branch.branchid}" ${branch.branchid==batchstate?'selected=seleted':''}>${branch.branchname}</option>
						</c:forEach>
		         	</select>
         		</td>
         		<td nowrap="nowrap" align="right">责任人:</td>
       			<td>
         			<select  name="dutypersonid" name = "dutypersonid" class="select1">
						<option value='0' selected="selected">请选择责任人</option>
					</select>
         		</td>
         		<td nowrap="nowrap" align="right">扣罚金额:</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input  type="text" id="sumPrice" name="sumPrice" style="width: 100%;" value="默认为赔付金额,可修改"></input>
         		</td>
         	</tr>
         	</tr>
         		<tr><td>&nbsp;</td>
         	</tr>
         	<tr style="display: none" id="punishInside1">
         		<td nowrap="nowrap" align="right" rowspan="2">扣罚说明：</td>
         		<td nowrap="nowrap" colspan="6" >
			   	 <textarea rows="3" id="punishInsideRemark" name="punishInsideRemark" style="width: 100%;resize: none;">不超过100字</textarea>
		        </td>
         	</tr>
         	</table>
         	</form>
</div>
<!-- 查看/修改层显示 -->
<c:if test="${updatePage==1}">
<div  id="update" class="easyui-dialog" title="查看/修改" data-options="iconCls:'icon-save,modal:true'" style="width:900px;height:500px;">
	<form action="${ctx}/penalizeOutBill/penalizeOutBillUpdate" method="post" id="updateForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
        		<tr>
	         	<td colspan="4"  align="left" valign="bottom" nowrap="nowrap">
	         		
		         	<input type="button" class="input_button2" value="返回" onclick="$('#update').dialog('close');"/>
		         	<%-- <c:if test="${weiShenHeState==bill.billstate}"> --%>
		         		<input type="button" class="input_button2" value="保存" onclick="updatePunishinsideBill()"/>
		         	<%-- </c:if> --%>
		         	
	         	</td>
	         	<td nowrap="nowrap" align="left" colspan="3"  > 
	         		<c:if test="${jiesuanAuthority==1}">
	         			<c:if test="${weiShenHeState==bill.billstate}">
	         				<input type="button" class="input_button2" onclick="changeBillState('ShenHe')" value="审核"/>
	         			</c:if>
	         			<c:if test="${yiShenHeState==bill.billstate}">
	         				<input type="button" class="input_button2" onclick="changeBillState('QuXiaoShenHe')" value="取消审核"/>
			         		<input type="button" class="input_button2" onclick="changeBillState('HeXiaoWanCheng')" value="核销完成"/>
	         			</c:if>
	         		</c:if>
	         	</td>
	         	<td align="left" nowrap="nowrap">
	         		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         		<c:if test="${jiesuanAdvanceAuthority==1}">
		         		<c:if test="${yiHeXiaoState==bill.billstate}">
		         			<input type="button" class="input_button2" onclick="changeBillState('QuXiaoHeXiao')" value="取消核销"/>
		         		</c:if>
	         		</c:if>
	         		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         	</td>
	         	<td  nowrap="nowrap"> &nbsp;&nbsp;&nbsp;</td>
         	</tr>
        	<tr>
         		<td align="right" nowrap="nowrap" style="width: 10%;">账单批次：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input type="text" id="billbatches" name="billbatches" style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.billbatches}"/> 
        			</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">账单状态：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="billstate" name="billstate" style="width: 100%;background-color:#DCDCDC" disabled="disabled"  >
						<c:forEach items="${PunishBillStateEnum}" var="state">
								<option value="${state.value}" >${state.text}</option>
				        </c:forEach>
         			</select>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >赔付大类：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="compensatebig"   name="compensatebig" style="width: 100%;background-color:#DCDCDC" disabled="disabled">
							<c:forEach items="${penalizebigList}" var="big" >
								<c:if test="${bill.compensatebig==big.id}">
									<option value="${big.id}" selected="selected">${big.text}</option>
								</c:if>
							</c:forEach>
		         		</select>
        		 	</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >赔付小类：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select  id="compensatesmall" name="compensatesmall" style="width: 100%;background-color:#DCDCDC" disabled="disabled">
						<c:forEach items="${penalizesmallList}" var="small">
							<c:if test="${bill.compensatesmall==small.id}">
								<option value="${small.id}" selected="selected">${small.text}</option>
							</c:if>
						</c:forEach>
	         		</select>
       		 	</td>
       		</tr>
       		<tr>
       			<td align="right" nowrap="nowrap" style="width: 10%;">赔付金额：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input type="text" id="compensatefee" name="compensatefee" style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.compensatefee}"/> 
        			</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">创建日期：</td>
         		<td nowrap="nowrap" style="width: 20%;">
		         	<input type="text" id="createddate" name="createddate"  style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.createddate}"/>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >审核日期：</td>
         		<td nowrap="nowrap" style="width: 20%;">
        				<input type="text" id="checktime" name="checktime"  style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.checktime}"/>
        		 	</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >核销日期：</td>
         		<td nowrap="nowrap" style="width: 20%;">
       				<input type="text" id="verificationdate" name="verificationdate"  style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.verificationdate}"/>
       		 	</td>
         	</tr>
       		<tr>
       			<td align="right" nowrap="nowrap" style="width: 10%;">客户名称：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="customerid" name="customerid"  style="width: 100%;background-color:#DCDCDC" disabled="disabled">
	         			<c:forEach items="${customerList}" var="customer">
	         				<c:if test="${bill.customerid==customer.customerid}">
								<option value="${user.userid}" selected="selected">${customer.customername}</option>
							</c:if>
						</c:forEach>
					</select>
        		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">创建人：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="founder" name="founder"  style="width: 100%;background-color:#DCDCDC" disabled="disabled">
	         			<c:forEach items="${userList}" var="user">
							<c:if test="${bill.founder==user.userid}">
								<option value="${user.userid}" selected="selected">${user.realname}</option>
							</c:if>
						</c:forEach>
					</select>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >审核人：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="verifier" name="verifier"  style="width: 100%;background-color:#DCDCDC" disabled="disabled">
	         			<c:forEach items="${userList}" var="user">
	         				<c:if test="${bill.verifier==user.userid}">
								<option value="${user.userid}" selected="selected">${user.realname}</option>
							</c:if>
						</c:forEach>
					</select>
        		 </td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >核销人：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="verificationperson" name="verificationperson"  style="width: 100%;background-color:#DCDCDC" disabled="disabled">
	         			<c:forEach items="${userList}" var="user">
	         				<c:if test="${bill.verificationperson==user.userid}">
								<option value="${user.userid}" selected="selected">${user.realname}</option>
							</c:if>
						</c:forEach>
					</select>
       		 	</td>
         	</tr>
         	<tr>
	         	<td colspan="8" >
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">赔付说明：</td>
         		<td nowrap="nowrap" colspan="8" >
			   	 <textarea rows="3"  id="compensateexplain" name="compensateexplain" style="width: 100%;resize: none;" value="">${bill.compensateexplain}</textarea>
		        </td>
         	</tr>
         	<tr>
	         	<td colspan="8" >
	         	&nbsp;<input type="hidden" name="id" value="${bill.id}">
	         		<input type="hidden" name="compensateodd" value="${bill.compensateodd}">
	         	</td>
         	</tr>
         	 </table>
      </form>
      <div>
		         	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="penalizeInsideTable" >
						<tr>
							<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('penalizeInsideTable')"/> </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单编号</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 赔付单号</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单状态</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单金额 </td>
							<td align="center" valign="middle" style="font-weight: bold;">对外赔付金额 </td>
							<td align="center" valign="middle" style="font-weight: bold;">赔付小类 </td>
						</tr>
						<c:forEach var="pol" items="${bill.penalizeOutList}">
						<tr>
							<td height="30px"  valign="middle" align="center"><input type="checkbox" name="checkBox" value="${pol.cwb}"/> </td>
							<td align="center" valign="middle">${pol.cwb}</td>
							<td align="center" valign="middle">${pol.penalizeOutNO}</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${cwbStateMap}">
									<c:if test="${pol.flowordertype==item.key}">${item.value}</c:if>
								</c:forEach> 
							</td>
							<td align="center" valign="middle">${pol.caramount}</td>
							<td align="center" valign="middle">${pol.penalizeOutfee}</td>
							<td align="center" valign="middle">
								<c:forEach items="${penalizesmallList}" var="smallsort">
									<c:if test="${pol.penalizeOutsmall==smallsort.id}">${smallsort.text}</c:if>
								</c:forEach>
							</td>
							</tr>
         				</c:forEach>
					</table>
        </div>
        
      <div class="iframe_bottom"> 
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
					<input type="button" class="input_button2"  onclick="addPenalizeInside()" value="添加"/>
	         		<input type="button" class="input_button2"  onclick="deletePenalizeInside()" value="移除"/>
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/queryById/1');$('#updatePageForm').submit();" >第一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/queryById/${page_o.previous<1?1:page_o.previous}');$('#updatePageForm').submit();">上一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/queryById/${page_o.next<1?1:page_o.next }');$('#updatePageForm').submit();" >下一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/queryById/${page_o.maxpage<1?1:page_o.maxpage}');$('#updatePageForm').submit();" >最后一页</a>
					　共${page_o.maxpage}页　共${page_o.total}条记录 　当前第<select
							id="selectPg"
							onchange="$('#updatePageForm').attr('action',$(this).val());$('#updatePageForm').submit()">
							<c:forEach var="i" begin="1" end="${page_o.maxpage}">
							<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
							</c:forEach>
							</select>页
					</td>
				</tr>
			</table>
		</div>
</div>
</c:if>
<!-- 赔付单 -->
<c:if test="${updateDedailPage==1}">
<div  id="penalizeInsidePage" class="easyui-dialog" title="赔付单" data-options="iconCls:'icon-save'" style="width:700px;height:600px;">
	<div style="width:100%;">
		<form action="<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillList/1" method="post" id="queryPenalizeInsideListForm">
			<table width="60%" style="margin-top: 10px;font-size: 10px;">
				  <tr>
				  
						<td nowrap="nowrap" align="left">赔付大类</td>
		         		<td>
		         			<select  id="big" name="compensatebig" onchange="findsma($(this).val())">
								<option value ="0">请选择</option>
								<c:forEach items="${penalizebigList}" var="big" >
									<option value="${big.id}">${big.text}</option>
								</c:forEach>
							</select>
			         	</td>
			         	<td nowrap="nowrap" align="left">创建日期</td>
	         			<td nowrap="nowrap">
			         		<input type="text" name="creationStartDate" value="${creationStartDate }" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '开始日期'" value="${creationStartDate}"/>
			         		至 
		   	       		 	<input type="text" name="creationEndDate" value="${creationEndDate}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '结束日期'" value="${creationEndDate}"/>
		         		</td>
		         	</tr>
		         	<tr>
		         		<td nowrap="nowrap" align="left">赔付小类</td>
		         		<td>
		         			<select style="width: 100%" id="small" name="compensatesmall" onchange="find()">
								<option value ="0">请选择</option>
								<c:forEach items="${penalizesmallList}" var="small">
									<option value="${small.id}"  id="${small.parent }">${small.text}</option>
								</c:forEach>
							</select>
			         	</td>
		         		<td nowrap="nowrap" align="left">订单号</td>
		         		<td>
		        		 	<textarea name="compensateodd" style="width:100%;height:40px;resize:none;">${compen}</textarea>
		        		</td>
				  </tr>
				  <tr>
				  	<input type="hidden" name="customerid" value="${bill.customerid }"></input>
				  	<td colspan="4">
				  		<input type="hidden" name="id" value="${bill.id}">
				  		<input class="input_button2" type="button" onclick="queryPenalizeInsideList()" value="查询"/>
				  	</td>
				  </tr>
			 </table>
		</form>
	</div>

	<!-- <div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div> -->
	<div style="width:100%;">
		<div style="overflow: auto;">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="penalizeInsideListTable">
				<tr>
					<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('penalizeInsideListTable')"/> </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 订单号</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 客户</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 对外赔付金额 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 赔付大类 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 赔付小类 </td>
				</tr>
				 <c:forEach items="${penalizeOut}" var="list"> 
				<tr>
					<td height="30px" align="center"  valign="middle"><input type="checkbox" name="checkBox" value="${list.cwb}" /> </td>
					<td align="center" valign="middle" >${list.cwb}</td>
					<td align="center" valign="middle" >
						<c:forEach items="${customerList}" var="cus">
							<c:if test="${list.customerid==cus.customerid}">${cus.customername}</c:if>
				        </c:forEach>
					</td>
					<td align="center" valign="middle" >${list.penalizeOutfee}</td>
					<td>
						<c:forEach items="${penalizebigList}" var="bigsort">
						<c:if test="${list.penalizeOutbig==bigsort.id}">${bigsort.text}</c:if>
						</c:forEach>
					</td>
					<td>
						<c:forEach items="${penalizesmallList}" var="bigsort">
						<c:if test="${list.penalizeOutsmall==bigsort.id}">${bigsort.text}</c:if>
						</c:forEach>
					</td>
				</tr>
				 </c:forEach> 
			</table>
		</div>
		
	</div>
		 <c:if test="${page_obj.maxpage>0}">
			<div class="iframe_bottom"> 
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr>
						<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
							<input class="input_button2" type="button" onclick="addPenalizeInsideList()" value="确认"/>
							<input class="input_button2" type="button" onclick="$('#penalizeInsidePage').dialog('close')" value="取消"/>
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillList/1');$('#queryPenalizeInsideListForm').submit();" >第一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillList/${page_obj.previous<1?1:page_obj.previous}');$('#queryPenalizeInsideListForm').submit();">上一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillList/${page_obj.next<1?1:page_obj.next }');$('#queryPenalizeInsideListForm').submit();" >下一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillList/${page_obj.maxpage<1?1:page_obj.maxpage}');$('#queryPenalizeInsideListForm').submit();" >最后一页</a>
						　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第<select
								id="selectPg"
								onchange="$('#queryPenalizeInsideListForm').attr('action',$(this).val());$('#queryPenalizeInsideListForm').submit()">
								<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
								<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
								</c:forEach>
								</select>页
						</td>
					</tr>
				</table>
			</div>
		</c:if>
</div>
 </c:if> 



<!-- 查询层显示 -->
	<div  id="find" class="easyui-dialog" title="查询条件" data-options="iconCls:'icon-save,modal:true'" style="width:700px;height:220px;">
	<form action="${ctx}/penalizeOutBill/penalizeOutBillPage/1" method="post" id="searchForm">
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 15%;">账单批次：</td>
         		<td nowrap="nowrap" style="width: 30%;">
         			<input type="text" name="billbatches" style="width: 100%;" value="${billbatches}"/> 
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 15%;">账单状态：</td>
         		<td nowrap="nowrap" style="width: 30%;">
         			<select  name="billstate" style="width: 100%;">
         				<option value="" >---全部---</option>
						<c:forEach items="${PunishBillStateEnum}" var="state">
		         			<option value="${state.value}">${state.text}</option>
				        </c:forEach>
         			</select>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">账单创建日期：</td>
         		<td nowrap="nowrap">
		         	 <input type="text" name="billCreationStartDate" value="${billCreationStartDate}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		 <input type="text" name="billCreationEndDate"   value="${billCreationEndDate}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         		<td nowrap="nowrap" align="right">账单核销日期：</td>
         		<td nowrap="nowrap">
		         	 <input type="text" name="billVerificationStrartDate" value="${billVerificationStrartDate}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		 <input type="text" name="billVerificationEndDate"   value="${billVerificationEndDate}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >客户名称：</td>
         		<td nowrap="nowrap">
         			<select  name="customerid" style="width: 100%;">
         				<option value="" >---全部---</option>
						<c:forEach items="${customerList}" var="cus">
		         			<option value="${cus.customerid}" >${cus.customername}</option>
				        </c:forEach>
         			</select>
         		</td>
         		<td nowrap="nowrap" align="right" >赔付大类：</td>
         		<td nowrap="nowrap">
         			<select  name="compensatebig" style="width: 100%;">
         				<option value="" >---全部---</option>
						<c:forEach items="${penalizebigList}" var="big" >
		         			<option value="${big.id}"<c:if test='${compensatebig == big.id}'>selected="selected"</c:if>>${big.text}</option>
						</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">排序：</td>
         		<td nowrap="nowrap">
			    	<select style="width:70%;" name="sort">
			    		<option value="billstate">账单状态</option>
			    		<option value="billbatches">账单批次</option>
			    		<option value="compensatefee">赔付金额</option>
			    		<option value="createddate">创建日期</option>
			    	</select>
			    	<select style="width:30%;" name="method">
			    		<option  value="desc">降序</option>
			    		<option  value="asc">升序</option>
			    	</select>
		        </td>
		        <input type="hidden" name="query" value="1"></input>
		        <input type="hidden" id="id"/>
         	</tr>
         	<tr>
         	<td colspan="4" >
         	&nbsp;
         	</td>
         	</tr>
         	<tr>
         	<td colspan="4" rowspan="2" align="center" valign="bottom">
         	<input type="submit" class="input_button2" value="查询" />
         	<input type="button" class="input_button2" value="关闭" onclick="$('#find').dialog('close');"/>
         	</td>
         	</tr>
         	</table>
         	</form>
	</div>
	
<div id="updatePageDiv">
	<form action="<%=request.getContextPath()%>/penalizeOutBill/queryById/1" method="post" id="updatePageForm">
		<input type="hidden" name="id" value="">
		<input type="hidden" name="billstate" value="">
	</form>
</div>
<div id="addpenalizeOutDiv">
	<form action="<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillDedail" method="post" id="addPenalizeInsideForm">
		<input type="hidden" name="id" value="">
	</form>
</div>
<div id="penalizeOutListDiv">
	<form action="<%=request.getContextPath()%>/penalizeOutBill/addpenalizeOutBillList" method="post" id="penalizeInsideListForm">
		<input type="hidden" name="id" value="">
		<input type="hidden" name="compensateodd" value="">
	</form>
</div>
<div id="deleteBill">
	<form action="<%=request.getContextPath()%>/penalizeOutBill/penalizeOutBillPage/1" method="post" id="deleteBillForm">
		<input type="hidden" name="billbatches" value=""/> 
		<input type="hidden" name="billstate"  value=""/> 
		<input type="hidden" name="billCreationStartDate"  value=""/> 
		<input type="hidden" name="billCreationEndDate"  value=""/> 
		<input type="hidden" name="billVerificationStrartDate"  value=""/> 
		<input type="hidden" name="billVerificationEndDate"  value=""/> 
		<input type="hidden" name="customerid"  value=""/> 
		<input type="hidden" name="compensatebig"  value=""/> 
		<input type="hidden" name="sort"  value=""/> 
		<input type="hidden" name="method"  value=""/> 
		<input type="hidden" name="query" value="1"></input>
	</form>
</div>
</body>
</html>


