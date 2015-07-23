<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@page import="cn.explink.enumutil.PunishBillStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>对内扣罚账单</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$('#addPage').dialog('close');
	//$('#updatePage').dialog('close');
	$('#queryPage').dialog('close');
	//$('#penalizeInsidePage').dialog('close');
	
	$("#updateForm select[name='billState']").attr("disabled",true); 
	$("#updateForm select[name='punishbigsort']").attr("disabled",true); 
	$("#updateForm select[name='punishsmallsort']").attr("disabled",true); 
	$("#updateForm select[name='dutybranchid']").attr("disabled",true); 
});
function addInit(){
	//无处理
}
//扣罚大类触发扣罚小类
function findsmallSort(self,form)
{
var url = "<%=request.getContextPath()%>/penalizeType/findsmall";
$("#"+form+" select[name='punishsmallsort']").empty();
$.ajax({
	type:"post",
	url:url,
	data:{"id":$(self).val()},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>全部</option>";
			$("#"+form+" select[name='punishsmallsort']").append(optstr);
			for(var i=0;i<data.length;i++)
			{
				optstr = "<option value='"+data[i].id+"' title='"+data[i].parent+"'>"+data[i].text+"</option>";
				$("#"+form+" select[name='punishsmallsort']").append(optstr);
			}
		}
	}});
}
//小类触发大类
function findbigSort(self,form)
{ var parent=$(self).find("option:selected").attr("title");
	$("#"+form+" select[name='punishbigsort'] option[value='"+parent+"']").attr("selected","selected");
}

function selectbranchUsers(){
	var dutyPersonEle = $("#addForm select[name='dutypersonid']");
	var url = "<%=request.getContextPath()%>/abnormalOrder/getbranchusers";
	$.ajax({
		url:url,
		type:"POST",
		data:"dutybranchid="+$("#addForm select[name='dutybranchid']").val(),
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

function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}

function queryBillList(){
	$("#queryForm").submit();
	$('#queryPage').dialog('close');
}

function updatePage(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			strs = $(this).val().split(",");
			getEditData(strs[0]);
			return false;
		}
	}); 
}

function getEditData(val){
	$("#updatePageForm input[name='id']").val(val);
	$("#updatePageForm").submit();
}

function addPenalizeInside(){
	var id = $("#updateForm input[type='hidden'][name='id']").val(); 
	$("#addPenalizeInsideForm input[name='id']").val(id);
	$("#addPenalizeInsideForm").submit();
}

function deletePenalizeInside(){
	var sumPrice = $("#updateForm input[name='sumPrice']").val();
	var chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true){
			penalizeInsideTds = $(this).parent().parent().children();
			$(penalizeInsideTds).each(function(){
				if($(this)[0].attributes[1].value == 'punishInsideprice'){
					cwbPriceVal = $(this)[0].textContent;
					sumPrice = (parseFloat(sumPrice)-parseFloat(cwbPriceVal)).toFixed(2);
				}
			});
			$(this).parent().parent().remove();
		}
	}); 
	$("#updateForm input[name='sumPrice']").val(sumPrice);
}

function queryPenalizeInsideList(){
	var cwbs = $("#queryPenalizeInsideListForm textarea[name='cwbs']").val();
	if(cwbs){
		$("#queryPenalizeInsideListForm textarea[name='cwbs']").val(cwbs.replace(/\n/g,","));
	}
	$("#queryPenalizeInsideListForm").submit();
}

function addPenalizeInsideList(){
	var chkBoxes = $("#penalizeInsideListTable input[type='checkbox'][name='checkBox']");
	var punishNos = "";
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true){
			punishNos = $(this).val()+","+punishNos;
		}
	}); 
	if(!punishNos){
		$('#penalizeInsidePage').dialog('close');
		return false;
	}
	chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		punishNos = $(this).val()+","+punishNos;
	}); 
	
	punishNos = punishNos.substring(0,punishNos.length-1);
	var id = $("#queryPenalizeInsideListForm input[type='hidden'][name='id']").val(); 
	
	$("#penalizeInsideListForm input[name='id']").val(id);
	$("#penalizeInsideListForm input[name='punishNos']").val(punishNos);
	$("#penalizeInsideListForm").submit();
}

function updatePunishinsideBill(state){
	if(state){
		changeBillState(state);
	}
	updatePunishinsideBillFun();
}

function updatePunishinsideBillFun(){
	var chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	var punishNos = "";
	$(chkBoxes).each(function() {
		punishNos = $(this).val()+","+punishNos;
	}); 
	punishNos = punishNos.substring(0,punishNos.length-1);
	$("#updateForm input[type='hidden'][name='punishNos']").val(punishNos);
	
	$("#updateForm select[name='billState']").attr("disabled",false); 
	$("#updateForm select[name='punishbigsort']").attr("disabled",false); 
	$("#updateForm select[name='punishsmallsort']").attr("disabled",false); 
	$("#updateForm select[name='dutybranchid']").attr("disabled",false); 
	
	//$("#updateForm").submit();
	 $.ajax({
			type : "POST",
			data : $('#updateForm').serialize(),
			url : $("#updateForm").attr('action'),
			dataType : "json",
			success : function(data) {
					if(data.errorCode==0){
						alert(data.error);
					  	$('#updatePage').dialog('close');
			   			window.location.href='<%=request.getContextPath()%>/punishinsideBill/punishinsideBillList/1';
					}
				}
			});
}

function deletePunishinsideBill(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	var billIds = "";
	var sign = 0;
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			strs = $(this).val().split(",");
			if(strs[1] == '${weiShenHeState}'){
				billIds = billIds + strs[0] + ",";
			} else {
				sign = 1;
			}
		}
	}); 
	if(sign == 1){
		alert("只有未审核状态才能进行删除!");
	}
	if(!billIds){
		return;
	}
	billIds = billIds.substring(0,billIds.length-1);
	if(window.confirm("是否确定删除?")){
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/punishinsideBill/deletePunishinsideBill",
			data:{"ids":billIds},
			dataType:"json",
			success:function(data){
					if(data && data.errorCode==0){
						alert(data.error);
						window.location.href='<%=request.getContextPath()%>/punishinsideBill/punishinsideBillList/1';
					}
				}
			});
	}
}

function addPunishBillPage(){
	$('#addPage').dialog('open');
	
	$("#addForm textarea[name='punishNos']").focus(function(){
		if($("#addForm textarea[name='punishNos']").val() && $("#addForm textarea[name='punishNos']").val() == '如多个扣罚单回车隔开'){
			$("#addForm textarea[name='punishNos']").val('');
		}
	}).blur(function(){
		if(!$("#addForm textarea[name='punishNos']").val()){
			$("#addForm textarea[name='punishNos']").val('如多个扣罚单回车隔开');
		}
	});
	
	$("#addForm textarea[name='punishInsideRemark']").focus(function(){
		if($("#addForm textarea[name='punishInsideRemark']").val() && $("#addForm textarea[name='punishInsideRemark']").val() == '不超过100字'){
			$("#addForm textarea[name='punishInsideRemark']").val('');
		}
	}).blur(function(){
		if(!$("#addForm textarea[name='punishInsideRemark']").val()){
			$("#addForm textarea[name='punishInsideRemark']").val('不超过100字');
		}
	});
}
function addPunishinsideBill(){
	if($("#addForm select[name='punishbigsort']").val() == 0){
		alert("扣罚大类为必填项!");
		return false;
	}
	var punishNos = $("#addForm textarea[name='punishNos']").val();
	if(punishNos){
		if(punishNos == "如多个扣罚单回车隔开"){
			$("#addForm textarea[name='punishNos']").val("");
		} else {
			$("#addForm textarea[name='punishNos']").val(punishNos.replace(/\n/g,","));
		}
	}
	var punishInsideRemark = $("#addForm textarea[name='punishInsideRemark']").val();
	if(punishInsideRemark){
		if(punishInsideRemark == "不超过100字"){
			$("#addForm textarea[name='punishInsideRemark']").val('');
		} else {
			if(punishInsideRemark.length > 100){
				alert("扣罚说明不超过100字!");
				return false;
			}
		}
	}
	$("#addForm").submit();
}
function changeBillState(state){
	if(state){
		if(state == 'ShenHe'){
			 if(confirm("是否确认审核?")){
				$("#updateForm select[name='billState']").val('${yiShenHeState}');
				$("#updateForm input[name='shenHeDate']").val('${nowDate}');
				$("#updateForm input[name='shenHePerson']").val('${userid}');
				$("#updateForm input[name='shenHePersonName']").val('${realname}');
			 }
		} else if(state == 'QuXiaoShenHe'){
			if(confirm("是否确认取消审核?")){
				$("#updateForm select[name='billState']").val('${weiShenHeState}');
				$("#updateForm input[name='shenHeDate']").val('');
				$("#updateForm input[name='shenHePerson']").val('0');
				$("#updateForm input[name='shenHePersonName']").val('');
			}
		} else if(state == 'HeXiaoWanCheng'){
			if(confirm("是否确认核销完成?")){
				$("#updateForm select[name='billState']").val('${yiHeXiaoState}');
				$("#updateForm input[name='heXiaoDate']").val('${nowDate}');
				$("#updateForm input[name='heXiaoPerson']").val('${userid}');
				$("#updateForm input[name='heXiaoPersonName']").val('${realname}');
			}
		} else if(state == 'QuXiaoHeXiao'){
			if(confirm("是否确认取消核销?")){
				$("#updateForm select[name='billState']").val('${yiShenHeState}');
				$("#updateForm input[name='heXiaoDate']").val('');
				$("#updateForm input[name='heXiaoPerson']").val('0');
				$("#updateForm input[name='heXiaoPersonName']").val('');
			}
		}
	}
}
</script>
</head>

<body style="background:#eef9ff">

<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 60%">
			    <tr>
				    <td>
					    <input class="input_button2" type="button" onclick="addPunishBillPage()" value="新增"/>
					    <input class="input_button2" type="button" onclick="updatePage()" value="查看/修改"/>
					    <input class="input_button2" type="button" onclick="deletePunishinsideBill()" value="删除"/>
					    <input class="input_button2" type="button" onclick="$('#queryPage').dialog('open')" value="查询"/>
			    	</td>
			    </tr>
		 </table>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
		<div class="right_title">
			<div style="overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="listTable">
					<tr>
						<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('listTable')"/> </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 账单批次</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 账单状态</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 责任机构</td>
						<td align="center" valign="middle" style="font-weight: bold;"> 责任人 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 对内扣罚金额 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 创建人 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 创建日期 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 撤销人 </td>
						<td align="center" valign="middle" style="font-weight: bold;"> 撤销日期 </td>
					</tr>
					<c:forEach items="${punishinsideBillList}" var="bill">
					<tr>
						<td height="30px" align="center"  valign="middle"><input type="checkbox" name="checkBox" value="${bill.id}${','}${bill.billState}" /> </td>
						<td align="center" valign="middle" >${bill.billBatch}</td>
						<td align="center" valign="middle" >
							<c:forEach var="item" items="${billStateMap}">
								<c:if test="${bill.billState==item.key}">${item.value}</c:if>
							</c:forEach> 
						</td>
						<td align="center" valign="middle" >
							<c:forEach items="${branchList}" var="branch">
							<c:if test="${bill.dutybranchid==branch.branchid}">${branch.branchname}</c:if>
							</c:forEach>
						</td>
						<td>
							<c:forEach items="${dutyPersonList}" var="dutyPerson">
							<c:if test="${bill.dutypersonid==dutyPerson.userid}">${dutyPerson.realname}</c:if>
							</c:forEach>
						</td>
						<td align="center" valign="middle" >${bill.sumPrice}</td>
						<td align="center" valign="middle" >
							<c:forEach items="${userList}" var="user">
							<c:if test="${bill.creator==user.userid}">${user.realname}</c:if>
							</c:forEach>
						</td>
						<td align="center" valign="middle" >${bill.createDate}</td>
						<td align="center" valign="middle" >
							<c:forEach items="${userList}" var="user">
							<c:if test="${bill.cheXiaoPerson==user.userid}">${user.realname}</c:if>
							</c:forEach>
						</td>
						<td align="center" valign="middle" >${bill.cheXiaoDate}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
</div>

<!-- 新增层显示 -->
<div  id="addPage" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save'" style="width:780px;height:250px;">
	<form action="<%=request.getContextPath()%>/punishinsideBill/addPunishinsideBill" method="post" id="addForm">
		<table width="100%" border="0" cellspacing="5" cellpadding="0" style="margin-top: 10px;font-size: 10px; border-collapse:separate">
         		<tr>
		         	<td colspan="2" align="center" valign="bottom">
			         	<input type="button" class="input_button2" value="返回" onclick="$('#addPage').dialog('close');"/>
			         	<input type="button" class="input_button2" value="创建" onclick="addPunishinsideBill()"/>
		         	</td>
		         	<td></td>
	         		<td></td>
		         	<td></td>
	         		<td></td>
	         	</tr>
	         	<tr>
	         		<td align="left"><font color="red">*</font>扣罚大类</td>
	         		<td>
		         		<select name="punishbigsort" class="select1" onchange="findsmallSort(this,'addForm');">
		         			<option value="0"></option>
		         			<c:forEach items="${punishbigsortList}" var="bigsort">
		         				<option value="${bigsort.id}">${bigsort.text}</option>
							</c:forEach>
		         		</select>
		         	</td>
	         		<td align="left">扣罚小类</td>
	         		<td>
		         		<select name="punishsmallsort" class="select1" onchange="findbigSort(this,'addForm');">
		         			<option value="0">全部</option>
		         			<c:forEach items="${punishsmallsortList}" var="smallsort">
		         				<option value="${smallsort.id}" title="${smallsort.parent}">${smallsort.text}</option>
							</c:forEach>
		         		</select>
		         	</td>
	         		<td  align="left" rowspan="3" valign="top">扣罚单号</td>
	         		<td   rowspan="3" valign="top" >
	        		 	<textarea style="width:100%;height:60px;resize: none;" name="punishNos">如多个扣罚单回车隔开</textarea>
	        		</td>
	         	</tr>
	         	<tr>
	         		<td align="left">责任机构</td>
	         		<td>
		         		<select name="dutybranchid" class="select1" onchange="selectbranchUsers();">
		         			<option value="0">请选择责任机构</option>
		         			<c:forEach items="${branchList}" var="branch">
		         				<option value="${branch.branchid}">${branch.branchname}</option>
							</c:forEach>
		         		</select>
		         	</td>
	         		<td align="left">扣罚单创建日期</td>
	         		<td >
		         		<input type="text" name="punishNoCreateBeginDate"  class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '开始日期'"/>
		         		至 
	   	       		 	<input type="text" name="punishNoCreateEndDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '结束日期'"/>
		         	</td>
	         	</tr>
	         	<tr>
	         		<td  align="left">责任人</td>
	         		<td>
	         			<select  name="dutypersonid" class="select1">
							<option value='0' selected="selected">请选择责任人</option>
						</select>
	         		</td>
	         		<td></td>
	         		<td></td>
	         	</tr>
	         	<tr>
	         		<td  align="left">扣罚说明</td>
	         		<td  colspan="5">
				   	 <textarea name="punishInsideRemark" style="width:100%;height:60px;resize: none;">不超过100字</textarea>
			        </td>
	         	</tr>
         	</table>
         </form>
</div>
<!-- 查看/修改层显示 -->
<c:if test="${updatePage==1}">
<div  id="updatePage" class="easyui-dialog" title="编辑" data-options="iconCls:'icon-save'" style="width:800px;height:500px;">
	<form action="<%=request.getContextPath()%>/punishinsideBill/updatePunishinsideBill" method="post" id="updateForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
        	<tr>
	         	<td colspan="3"  align="left">
		         	<input type="button" class="input_button2" value="返回" onclick="$('#updatePage').dialog('close');"/>
		         	<c:if test="${weiShenHeState==punishinsideBillVO.billState}">
		         		<input type="button" class="input_button2" value="保存" onclick="updatePunishinsideBill('')"/>
		         	</c:if>
	         	</td>
	         	<c:choose>
	         	<c:when test="${jiesuanAuthority==1}">
	         		<c:choose>
			         	<c:when test="${weiShenHeState==punishinsideBillVO.billState}">
			         		<td align="right" colspan="3"> 
			         			<input type="button" class="input_button2" onclick="updatePunishinsideBill('ShenHe')" value="审核"/>
			         		</td>
			         	</c:when>
			         	<c:when test="${yiShenHeState==punishinsideBillVO.billState}">
			         		<td align="right" colspan="3">
			         			<input type="button" class="input_button2" onclick="updatePunishinsideBill('QuXiaoShenHe')" value="取消审核"/>
			         			<input type="button" class="input_button2" onclick="updatePunishinsideBill('HeXiaoWanCheng')" value="核销完成"/>
			         		</td>
			         	</c:when>
		         	</c:choose>
	         	</c:when>
	         	<c:when test="${jiesuanAdvanceAuthority==1 && yiHeXiaoState==punishinsideBillVO.billState}">
		         	<td align="right" colspan="3"> 
		         		<input type="button" class="input_button2" onclick="updatePunishinsideBill('QuXiaoHeXiao')" value="取消核销"/>
		         	</td>
	         	</c:when>
	         	<c:otherwise>
					<td colspan="3">
			         	&nbsp;&nbsp;&nbsp;&nbsp;
			        </td>
				</c:otherwise>
	         	</c:choose>
	         	<td colspan="2">
			         &nbsp;&nbsp;&nbsp;&nbsp;
			   	</td>
         	</tr>
        	<tr>
         		<td align="left">账单批次</td>
         		<td>
         			<input type="text"  name="billBatch" readonly="readonly"  value="${punishinsideBillVO.billBatch}" style="background-color:#DCDCDC"/> 
        			</td>
         		<td  align="left" >账单状态</td>
         		<td>
         			<select name="billState" style="background-color:#DCDCDC">
			         	<c:forEach var="item" items="${billStateMap}">
							<c:if test="${punishinsideBillVO.billState==item.key}">
								<option value="${item.key}" selected="selected">${item.value}</option>
							</c:if>
							<c:if test="${punishinsideBillVO.billState!=item.key}">
								<option value="${item.key}">${item.value}</option>
							</c:if>
						</c:forEach> 
					</select>
         		</td>
         		<td  align="left">扣罚大类</td>
         		<td>
        			<select name="punishbigsort" style="background-color:#DCDCDC">
		         			<c:forEach items="${punishbigsortList}" var="bigsort">
		         				<c:if test="${punishinsideBillVO.punishbigsort==bigsort.id}">
									<option value="${bigsort.id}" selected="selected">${bigsort.text}</option>
								</c:if>
							</c:forEach>
		         	</select>
        		 </td>
         		<td  align="left">扣罚小类</td>
         		<td>
       				<select name="punishsmallsort" style="background-color:#DCDCDC">
       						<option value="0">全部</option>
							<c:forEach items="${punishsmallsortList}" var="smallsort">
								<c:if test="${punishinsideBillVO.punishsmallsort==smallsort.id}">
			         				<option value="${smallsort.id}" title="${smallsort.parent}" selected="selected">${smallsort.text}</option>
			         			</c:if>
							</c:forEach>
		         	</select>
       		 	</td>
       		</tr>
       		<tr>
       			<td align="left">扣罚金额(元)</td>
         		<td>
         			<input type="text"  name="sumPrice" readonly="readonly"  value="${punishinsideBillVO.sumPrice}" style="background-color:#DCDCDC"/> 
        			</td>
         		<td  align="left" >创建日期</td>
         		<td>
		         	<input type="text"  name="createDate" readonly="readonly"  value="${punishinsideBillVO.createDate}" style="background-color:#DCDCDC"/>
         		</td>
         		<td  align="left"  >审核日期</td>
         		<td>
        				<input type="text"  name="shenHeDate" readonly="readonly"  value="${punishinsideBillVO.shenHeDate}" style="background-color:#DCDCDC"/>
        		 	</td>
         		<td  align="left"  >核销日期</td>
         		<td>
       				<input type="text"  name="heXiaoDate" readonly="readonly"  value="${punishinsideBillVO.heXiaoDate}" style="background-color:#DCDCDC"/>
       		 	</td>
         	</tr>
       		<tr>
       			<td align="left">责任机构</td>
         		<td>
         			<select name="dutybranchid" style="background-color:#DCDCDC">
		         			<c:forEach items="${branchList}" var="branch">
		         				<c:if test="${punishinsideBillVO.dutybranchid==branch.branchid}">
		         					<option value="${branch.branchid}">${branch.branchname}</option>
		         				</c:if>
							</c:forEach>
		         	</select>
        		</td>
         		<td  align="left">创建人</td>
         		<td>
		         	<input type="text"  name="creatorName" readonly="readonly"  value="${punishinsideBillVO.creatorName}" style="background-color:#DCDCDC"/>
		         	<input type="hidden"  name="creator"  value="${punishinsideBillVO.creator}"/>
         		</td>
         		<td  align="left">审核人</td>
         		<td>
       				<input type="text"  name="shenHePersonName" readonly="readonly"  value="${punishinsideBillVO.shenHePersonName}" style="background-color:#DCDCDC"/>
       				<input type="hidden"  name="shenHePerson"  value="${punishinsideBillVO.shenHePerson}"/>
       		 	</td>
         		<td  align="left">核销人</td>
         		<td>
       				<input type="text"  name="heXiaoPersonName" readonly="readonly"  value="${punishinsideBillVO.heXiaoPersonName}" style="background-color:#DCDCDC"/>
       				<input type="hidden"  name="heXiaoPerson"  value="${punishinsideBillVO.heXiaoPerson}"/>
       		 	</td>
         	</tr>
         	<tr>
         		<td  align="left">责任人</td>
         		<td>
       				<input type="text"  name="dutypersonname" readonly="readonly"  value="${punishinsideBillVO.dutypersonname}" style="background-color:#DCDCDC"/>
       				<input type="hidden"  name="dutypersonid"  value="${punishinsideBillVO.dutypersonid}"/>
       		 	</td>
       		 	<td colspan="6"></td>
         	</tr>
         	<tr>
         		<td  align="left">扣罚说明</td>
         		<td  colspan="7" >
			   	 <textarea rows="3"  name="punishInsideRemark" style="width: 100%;resize: none;">${punishinsideBillVO.punishInsideRemark}</textarea>
		        </td>
         	</tr>
         	<tr>
	         	<td colspan="8">
	         		<input type="hidden" name="id" value="${punishinsideBillVO.id}">
	         	</td>
         	</tr>
         	<tr>
	         	<td colspan="8">
	         		<input type="hidden" name="punishNos" value="${punishinsideBillVO.punishNos}">
	         	</td>
         	</tr>
         	<tr>
         		<td colspan="8">
		         	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="penalizeInsideTable" >
						<tr>
							<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('penalizeInsideTable')"/> </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 扣罚单号</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单号</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单状态</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 责任人 </td>
							<!-- <td align="center" valign="middle" style="font-weight: bold;"> 订单类型 </td> -->
							<td align="center" valign="middle" style="font-weight: bold;"> 订单金额 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 对内扣罚金额 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 扣罚小类 </td>
						</tr>
						<c:forEach var="pIn" items="${punishinsideBillVO.penalizeInsideList}">
							<tr>
								<td height="30px"  valign="middle"><input type="checkbox" name="checkBox" value="${pIn.punishNo}"/> </td>
								<td align="center" valign="middle">${pIn.punishNo}</td>
								<td align="center" valign="middle">${pIn.cwb}</td>
								<td align="center" valign="middle">
									<c:forEach var="item" items="${cwbStateMap}">
										<c:if test="${pIn.cwbstate==item.key}">${item.value}</c:if>
									</c:forEach> 
								</td>
								<td align="center" valign="middle">${pIn.dutypersonname}</td>
								<!-- <td align="center" valign="middle"></td> -->
								<td align="center" name="cwbPrice" valign="middle">${pIn.cwbPrice}</td>
								<td align="center" name="punishInsideprice" valign="middle">${pIn.punishInsideprice}</td>
								<td align="center" valign="middle">
									<c:forEach items="${punishsmallsortList}" var="smallsort">
										<c:if test="${pIn.punishsmallsort==smallsort.id}">${smallsort.text}</c:if>
									</c:forEach>
								</td>
							</tr>
         				</c:forEach>
					</table>
					<input type="button" class="input_button2"  onclick="addPenalizeInside()" value="添加"/>
	         		<input type="button" class="input_button2"  onclick="deletePenalizeInside()" value="移除"/>
				</td>
			</tr>
         </table>
      </form>
</div>
</c:if>
<!-- 查询层显示 -->
	<div  id="queryPage" class="easyui-dialog" title="查询条件" data-options="iconCls:'icon-save'" style="width:700px;height:220px;">
	<form action="<%=request.getContextPath()%>/punishinsideBill/punishinsideBillList/1" method="post" id="queryForm">
         <table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="left"  style="width: 15%;">账单批次</td>
         		<td  style="width: 30%;">
         			<input type="text" name="billBatch" value="${queryConditionVO.billBatch}"/> 
         		</td>
         		<td  align="left" style="width: 15%;">账单状态</td>
         		<td  style="width: 30%;">
	         		<select name="billState">
						<option value="0">---全部---</option>
						<c:forEach var="item" items="${billStateMap}">
							<c:if test="${queryConditionVO.billState==item.key}">
								<option value="${item.key}" selected="selected">${item.value}</option>
							</c:if>
							<c:if test="${queryConditionVO.billState!=item.key}">
								<option value="${item.key}">${item.value}</option>
							</c:if>
						</c:forEach> 
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td align="left">账单创建日期</td>
         		<td >
		         	 <input type="text" name="createDateFrom" value="${queryConditionVO.createDateFrom}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/> 至 
	   	       		 <input type="text" name="createDateTo" value="${queryConditionVO.createDateTo}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/>
         		</td>
         		<td align="left">账单核销日期</td>
         		<td >
		         	 <input type="text" name="heXiaoDateFrom" value="${queryConditionVO.heXiaoDateFrom}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/> 至 
	   	       		 <input type="text" name="heXiaoDateTo"  value="${queryConditionVO.heXiaoDateTo}" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left" >责任机构</td>
         		<td >
         			<input type="text" name="dutybranchname" value="${queryConditionVO.dutybranchname}" /> 
         		</td>
         		<td  align="left" >扣罚大类</td>
         		<td >
         			<select name="punishbigsort" class="select1" onchange="findsmallSort(this,'queryForm');">
	         			<option value="0">---全部---</option>
	         			<c:forEach items="${punishbigsortList}" var="bigsort">
	         				<c:if test="${queryConditionVO.punishbigsort==bigsort.id}">
		         				<option value="${bigsort.id}" selected="selected">${bigsort.text}</option>
		         			</c:if>
	         				<c:if test="${queryConditionVO.punishbigsort!=bigsort.id}">
		         				<option value="${bigsort.id}">${bigsort.text}</option>
		         			</c:if>
						</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left" >责任人</td>
         		<td >
         			<input type="text" name="dutypersonname" value="${queryConditionVO.dutypersonname}" /> 
         		</td>
         		<td  align="left" >扣罚小类</td>
         		<td >
         			<select name="punishsmallsort" class="select1" onchange="findbigSort(this,'queryForm');">
	         			<option value="0">---全部---</option>
	         			<c:forEach items="${punishsmallsortList}" var="smallsort">
	         				<c:if test="${queryConditionVO.punishsmallsort==smallsort.id}">
		         				<option value="${smallsort.id}" title="${smallsort.parent}" selected="selected">${smallsort.text}</option>
		         			</c:if>
	         				<c:if test="${queryConditionVO.punishsmallsort!=smallsort.id}">
		         				<option value="${smallsort.id}" title="${smallsort.parent}">${smallsort.text}</option>
		         			</c:if>
						</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left">排序</td>
         		<td >
			    	<select name="contractColumn">
			    		<option <c:if test='${queryConditionVO.contractColumn == billBatch}'>selected="selected"</c:if> value="billBatch">账单批次</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == billState}'>selected="selected"</c:if> value="billState">账单状态</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == createDate}'>selected="selected"</c:if> value="createDate">账单创建日期</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == heXiaoDate}'>selected="selected"</c:if> value="heXiaoDate">账单核销日期</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == dutybranchname}'>selected="selected"</c:if> value="dutybranchname">责任机构</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == punishbigsort}'>selected="selected"</c:if> value="punishbigsort">扣罚大类</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == dutypersonname}'>selected="selected"</c:if> value="dutypersonname">责任人</option>
			    		<option <c:if test='${queryConditionVO.contractColumn == punishsmallsort}'>selected="selected"</c:if> value="punishsmallsort">扣罚小类</option>
			    	</select>
			    	<select name="contractColumnOrder">
			    		<option <c:if test='${queryConditionVO.contractColumnOrder == asc}'>selected="selected"</c:if> value="asc">升序</option>
			    		<option <c:if test='${queryConditionVO.contractColumnOrder == desc}'>selected="selected"</c:if> value="desc">降序</option>
			    	</select>
		        </td>
		        <td></td>
		        <td></td>
         	</tr>
         	<tr>
	         	<td colspan="4" >&nbsp;</td>
         	</tr>
         	<tr>
	         	<td colspan="4" rowspan="2" align="center" valign="bottom">
	         	<input type="button" class="input_button2" value="查询" onclick="queryBillList()"/>
	         	<input type="button" class="input_button2" value="关闭" onclick="$('#queryPage').dialog('close');"/>
	         	</td>
         	</tr>
         </table>
       </form>
	</div>
	
<!-- 扣罚单finder层显示 -->
<c:if test="${penalizeInsidePage==1}">
<div  id="penalizeInsidePage" class="easyui-dialog" title="扣罚单" data-options="iconCls:'icon-save'" style="width:700px;height:600px;">
	<div style="width:100%;">
		<form action="<%=request.getContextPath()%>/punishinsideBill/penalizeInsideList/1" method="post" id="queryPenalizeInsideListForm">
			<table width="80%" style="margin-top: 10px;font-size: 10px;">
				  <tr>
						<td align="left">扣罚大类</td>
		         		<td>
			         		<select name="punishbigsort" class="select1" onchange="findsmallSort(this,'queryPenalizeInsideListForm');">
			         			<option value="0">全部</option>
			         			<c:forEach items="${punishbigsortList}" var="bigsort">
			         				<option value="${bigsort.id}">${bigsort.text}</option>
								</c:forEach>
			         		</select>
			         	</td>
			         	<td align="left">创建日期</td>
	         			<td>
			         		<input type="text" name="punishNoCreateBeginDate"  class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '开始日期'" value="${punishinsideBillVO.punishNoCreateBeginDate}"/>
			         		至 
		   	       		 	<input type="text" name="punishNoCreateEndDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '结束日期'" value="${punishinsideBillVO.punishNoCreateEndDate}"/>
		         		</td>
		         	</tr>
		         	<tr>
		         		<td align="left">扣罚小类</td>
		         		<td>
			         		<select name="punishsmallsort" class="select1" onchange="findbigSort(this,'queryPenalizeInsideListForm');">
			         			<option value="0">全部</option>
			         			<c:forEach items="${punishsmallsortList}" var="smallsort">
			         				<option value="${smallsort.id}">${smallsort.text}</option>
								</c:forEach>
			         		</select>
			         	</td>
		         		<td align="left">订单号</td>
		         		<td>
		        		 	<textarea style="width:100%;height:40px;resize:none;" name="cwbs"></textarea>
		        		</td>
				  </tr>
				  <tr>
				  	<td colspan="4">
				  		<input type="hidden" name="id" value="${punishinsideBillVO.id}">
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
					<td align="center" valign="middle" style="font-weight: bold;"> 责任机构</td>
					<td align="center" valign="middle" style="font-weight: bold;"> 对内扣罚金额 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 扣罚大类 </td>
					<td align="center" valign="middle" style="font-weight: bold;"> 扣罚小类 </td>
				</tr>
				<c:forEach items="${penalizeInsideList}" var="list">
				<tr>
					<td height="30px" align="center"  valign="middle"><input type="checkbox" name="checkBox" value="${list.punishNo}" /> </td>
					<td align="center" valign="middle" >${list.cwb}</td>
					<td align="center" valign="middle" >
						<c:forEach items="${branchList}" var="branch">
							<c:if test="${list.dutybranchid==branch.branchid}">${branch.branchname}</c:if>
						</c:forEach>
					</td>
					<td align="center" valign="middle" >${list.punishInsideprice}</td>
					<td>
						<c:forEach items="${punishbigsortList}" var="bigsort">
						<c:if test="${list.punishbigsort==bigsort.id}">${bigsort.text}</c:if>
						</c:forEach>
					</td>
					<td>
						<c:forEach items="${punishsmallsortList}" var="smallsort">
						<c:if test="${list.punishsmallsort==smallsort.id}">${smallsort.text}</c:if>
						</c:forEach>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<div>
			<input class="input_button2" type="button" onclick="addPenalizeInsideList()" value="确认"/>
			<input class="input_button2" type="button" onclick="$('#penalizeInsidePage').dialog('close')" value="取消"/>
		</div>
	</div>
		<%-- <c:if test="${page_obj.maxpage>1}"> --%>
			<div class="iframe_bottom"> 
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr>
						<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','1');$('#queryPenalizeInsideListForm').submit();" >第一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#queryPenalizeInsideListForm').submit();">上一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#queryPenalizeInsideListForm').submit();" >下一页</a>　
						<a href="javascript:$('#queryPenalizeInsideListForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#queryPenalizeInsideListForm').submit();" >最后一页</a>
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
		<%-- </c:if> --%>
</div>
</c:if>

<div id="updatePageDiv">
	<form action="<%=request.getContextPath()%>/punishinsideBill/updatePunishinsideBillPage" method="post" id="updatePageForm">
		<input type="hidden" name="id" value="">
	</form>
</div>

<div id="addPenalizeInsideDiv">
	<form action="<%=request.getContextPath()%>/punishinsideBill/penalizeInsidePage" method="post" id="addPenalizeInsideForm">
		<input type="hidden" name="id" value="">
	</form>
</div>

<div id="penalizeInsideListDiv">
	<form action="<%=request.getContextPath()%>/punishinsideBill/addPenalizeInsideList" method="post" id="penalizeInsideListForm">
		<input type="hidden" name="id" value="">
		<input type="hidden" name="punishNos" value="">
	</form>
</div>

<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="iframe_bottom"> 
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#punishinsideBillListForm').attr('action','1');$('#punishinsideBillListForm').submit();" >第一页</a>　
			<a href="javascript:$('#punishinsideBillListForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#punishinsideBillListForm').submit();">上一页</a>　
			<a href="javascript:$('#punishinsideBillListForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#punishinsideBillListForm').submit();" >下一页</a>　
			<a href="javascript:$('#punishinsideBillListForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#punishinsideBillListForm').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
			<select id="selectPg" onchange="$('#punishinsideBillListForm').attr('action',$(this).val());$('#punishinsideBillListForm').submit()">
				<c:forEach var="i" begin='1' end='${page_obj.maxpage}'>
					<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
				</c:forEach>
			</select>页
			</td>
		</tr>
	</table>
</div>
<div>
	<form action="<%=request.getContextPath()%>/punishinsideBill/punishinsideBillList/1" method="post" id="punishinsideBillListForm">
	</form>
</div>
</body>
</html>