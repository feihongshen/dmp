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
});
function addInit(){
	//无处理
}
//扣罚大类触发扣罚小类
function findsmallSort(self,chufaobject)
{
var url = "<%=request.getContextPath()%>/penalizeType/findsmall";
$("#addForm select[name='punishsmallsort']").empty();
$.ajax({
	type:"post",
	url:url,
	data:{"id":$(self).val()},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择扣罚小类</option>";
			$("#addForm select[name='punishsmallsort']").append(optstr);
			for(var i=0;i<data.length;i++)
			{
				optstr = "<option value='"+data[i].id+"' title='"+data[i].parent+"'>"+data[i].text+"</option>";
				$("#addForm select[name='punishsmallsort']").append(optstr);
			}
		}
	}});
}
//小类触发大类
function findbigSort(self,chufaobject)
{ var parent=$(self).find("option:selected").attr("title");
	$("#addForm select[name='punishbigsort'] option[value='"+parent+"']").attr("selected","selected");
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
function checkAll()
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
}
function showUp()
{
	$("#fileup").removeAttr('style');
	$("#top").removeAttr('style');
	$("#br").attr('style','display: none;');
	$("#imp").attr('disabled','disabled');
//	$("#box_form").removeAttr('style');
	}
function showButton()
{ 	if($("#filename").val().indexOf(".xlsx")==-1&&$("#filename").val().indexOf(".xls")==-1)
	{
	alert("文件类型必须为xls或者xlsx");
	$("#filename").val('');
	$("#subfile").attr('disabled','disabled');
	return false;
	}
	if($("#filename").val().length>0)
	{
	$("#subfile").removeAttr('disabled');
	}
}

function queryBillList(){
	$("#queryForm").submit();
	$('#queryPage').dialog('close');
}

function updatePage(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			//$(this).parent().parent().remove();
			getEditData($(this).val());
		}
	}); 
	//$('#updatePage').dialog('open');
}

function getEditData(val){
	$("#updatePageForm input[name='id']").val(val);
	$("#updatePageForm").submit();
}

function addPenalizeInside(){
	var id = $("#updateForm input[type='hidden'][name='id']").val(); 
	
	$("#addPenalizeInsideForm input[name='id']").val(id);
	$("#addPenalizeInsideForm").submit();
	//$('#penalizeInsidePage').dialog('open');
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

function queryPenalizeInsideList(){
	$("#queryPenalizeInsideListForm").submit();
}

function addPenalizeInsideList(){
	var chkBoxes = $("#penalizeInsideListTable input[type='checkbox'][name='checkBox']");
	var punishNos = "";
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			//$(this).parent().parent().remove();
			punishNos = $(this).val()+","+punishNos;
		}
	}); 
	punishNos = punishNos.substring(0,punishNos.length-1);
	var id = $("#queryPenalizeInsideListForm input[type='hidden'][name='id']").val(); 
	
	$("#penalizeInsideListForm input[name='id']").val(id);
	$("#penalizeInsideListForm input[name='punishNos']").val(punishNos);
	$("#penalizeInsideListForm").submit();
}

function updatePunishinsideBill(){
	var chkBoxes = $("#penalizeInsideTable input[type='checkbox'][name='checkBox']");
	var punishNos = "";
	$(chkBoxes).each(function() {
		punishNos = $(this).val()+","+punishNos;
	}); 
	punishNos = punishNos.substring(0,punishNos.length-1);
	
	$("#updateForm input[type='hidden'][name='punishNos']").val(punishNos);
	$("#updateForm").submit();
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
			    <input class="input_button2" type="button"  value="删除"/>
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
						<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll()"/> </td>
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
						<td height="30px" align="center"  valign="middle"><input type="checkbox" name="checkBox" value="${bill.id}" /> </td>
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
	<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
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
	         		<td align="left">扣罚大类</td>
	         		<td>
		         		<select name="punishbigsort" class="select1" onchange="findsmallSort(this,'punishsmallsort');">
		         			<option value="0">请选择扣罚大类</option>
		         			<c:forEach items="${punishbigsortList}" var="bigsort">
		         				<option value="${bigsort.id}">${bigsort.text}</option>
							</c:forEach>
		         		</select>
		         	</td>
	         		<td align="left">扣罚小类</td>
	         		<td>
		         		<select name="punishsmallsort" class="select1" onchange="findbigSort(this,'punishbigsort');">
		         			<option value="0">请选择扣罚小类</option>
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
	         	<td colspan="2"  align="center" valign="bottom" >
		         	<input type="button" class="input_button2" value="返回" onclick="$('#save').dialog('close');"/>
		         	<input type="button" class="input_button2" value="保存" onclick="updatePunishinsideBill()"/>
	         	</td>
	         	<td  align="left" colspan="3"  > 
	         		<input type="button" class="input_button2" id=""  onclick="showUp()" value="审核"/>
	         		<input type="button" class="input_button2" id=""  onclick="showUp()" value="取消审核"/>
	         	</td>
	         	<td align="left" >
	         		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         		<input type="button" class="input_button2" id=""  onclick="showUp()" value="核销完成"/>
	         		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         	</td>
	         	<td   colspan="2"> &nbsp;&nbsp;&nbsp;</td>
         	</tr>
        	<tr>
         		<td align="left">账单批次</td>
         		<td>
         			<input type="text"  name="billBatch" readonly="readonly"  value="${punishinsideBillVO.billBatch}"/> 
        			</td>
         		<td  align="left" >账单状态</td>
         		<td>
         			<select name="billState" class="select1">
			         	<c:forEach var="item" items="${billStateMap}">
							<c:if test="${punishinsideBillVO.billState==item.key}">
								<option value="${item.key}" selected="selected">${item.value}</option>
							</c:if>
						</c:forEach> 
					</select>
         		</td>
         		<td  align="left">扣罚大类</td>
         		<td>
        			<select name="punishbigsort" class="select1">
		         			<c:forEach items="${punishbigsortList}" var="bigsort">
		         				<c:if test="${punishinsideBillVO.punishbigsort==bigsort.id}">
									<option value="${bigsort.id}" selected="selected">${bigsort.text}</option>
								</c:if>
							</c:forEach>
		         	</select>
        		 </td>
         		<td  align="left">扣罚小类</td>
         		<td>
       				<select name="punishsmallsort" class="select1">
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
         			<input type="text"  name="sumPrice" readonly="readonly"  value="${punishinsideBillVO.sumPrice}"/> 
        			</td>
         		<td  align="left" >创建日期</td>
         		<td>
		         	<input type="text"  name="createDate" readonly="readonly"  value="${punishinsideBillVO.createDate}"/>
         		</td>
         		<td  align="left"  >审核日期</td>
         		<td>
        				<input type="text"  name="shenHeDate" readonly="readonly"  value="${punishinsideBillVO.shenHeDate}"/>
        		 	</td>
         		<td  align="left"  >核销日期</td>
         		<td>
       				<input type="text"  name="heXiaoDate" readonly="readonly"  value="${punishinsideBillVO.heXiaoDate}"/>
       		 	</td>
         	</tr>
       		<tr>
       			<td align="left">责任机构</td>
         		<td>
         			<select name="dutybranchid" class="select1">
		         			<c:forEach items="${branchList}" var="branch">
		         				<c:if test="${punishinsideBillVO.dutybranchid==branch.branchid}">
		         					<option value="${branch.branchid}">${branch.branchname}</option>
		         				</c:if>
							</c:forEach>
		         	</select>
        		</td>
         		<td  align="left">创建人</td>
         		<td>
		         	<input type="text"  name="creatorName" readonly="readonly"  value="${punishinsideBillVO.creatorName}"/>
		         	<input type="hidden"  name="creator"  value="${punishinsideBillVO.creator}"/>
         		</td>
         		<td  align="left">审核人</td>
         		<td>
       				<input type="text"  name="shenHePersonName" readonly="readonly"  value="${punishinsideBillVO.shenHePersonName}"/>
       				<input type="hidden"  name="shenHePerson"  value="${punishinsideBillVO.shenHePerson}"/>
       		 	</td>
         		<td  align="left">核销人</td>
         		<td>
       				<input type="text"  name="heXiaoPersonName" readonly="readonly"  value="${punishinsideBillVO.heXiaoPersonName}"/>
       				<input type="hidden"  name="heXiaoPerson"  value="${punishinsideBillVO.heXiaoPerson}"/>
       		 	</td>
         	</tr>
         	<tr>
         		<td  align="left">责任人</td>
         		<td>
       				<input type="text"  name="dutypersonname" readonly="readonly"  value="${punishinsideBillVO.dutypersonname}"/>
       				<input type="hidden"  name="dutypersonid"  value="${punishinsideBillVO.dutypersonid}"/>
       		 	</td>
       		 	<td colspan="6"></td>
         	</tr>
         	<tr>
         		<td  align="left">扣罚说明</td>
         		<td  colspan="7" >
			   	 <textarea rows="3"  name="punishInsideRemark" style="width: 100%;resize: none;" value="${punishinsideBillVO.punishInsideRemark}"></textarea>
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
							<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick=""/> </td>
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
							<td align="center" valign="middle">${pIn.cwbPrice}</td>
							<td align="center" valign="middle">${pIn.punishInsideprice}</td>
							<td align="center" valign="middle">
								<c:forEach items="${punishsmallsortList}" var="smallsort">
									<c:if test="${pIn.punishsmallsort==smallsort.id}">${smallsort.text}</c:if>
								</c:forEach>
							</td>
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
	<form action="<%=request.getContextPath()%>/punishinsideBill/punishinsideBillList" method="post" id="queryForm">
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="left"  style="width: 15%;">账单批次</td>
         		<td  style="width: 30%;">
         			<input type="text" name="billBatch" /> 
         		</td>
         		<td  align="left" style="width: 15%;">账单状态</td>
         		<td  style="width: 30%;">
	         		<select name="billState">
	         			<option value="0">---全部---</option>
						<c:forEach var="item" items="${billStateMap}">
							<option value="${item.key}">${item.value}</option>
						</c:forEach> 
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td align="left">账单创建日期</td>
         		<td >
		         	 <input type="text" name="createDateFrom" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/> 至 
	   	       		 <input type="text" name="createDateTo" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/>
         		</td>
         		<td align="left">账单核销日期</td>
         		<td >
		         	 <input type="text" name="heXiaoDateFrom" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/> 至 
	   	       		 <input type="text" name="heXiaoDateTo"   class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''"/>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left" >责任机构</td>
         		<td >
         			<input type="text" name="dutybranchname" /> 
         		</td>
         		<td  align="left" >扣罚大类</td>
         		<td >
         			<select name="punishbigsort">
	         			<option value="0">---全部---</option>
	         			<c:forEach items="${punishbigsortList}" var="bigsort">
		         				<option value="${bigsort.id}">${bigsort.text}</option>
						</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left" >责任人</td>
         		<td >
         			<input type="text" name="dutypersonname" /> 
         		</td>
         		<td  align="left" >扣罚小类</td>
         		<td >
         			<select name="punishsmallsort">
	         			<option value="0">---全部---</option>
	         			<c:forEach items="${punishsmallsortList}" var="smallsort">
		         				<option value="${smallsort.id}">${smallsort.text}</option>
						</c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
         		<td  align="left">排序</td>
         		<td >
			    	<select name="contractColumn">
			    		<option value="billBatch">账单批次</option>
			    		<option value="billState">账单状态</option>
			    		<option value="createDate">账单创建日期</option>
			    		<option value="heXiaoDate">账单核销日期</option>
			    		<option value="dutybranchname">责任机构</option>
			    		<option value="punishbigsort">扣罚大类</option>
			    		<option value="dutypersonname">责任人</option>
			    		<option value="punishsmallsort">扣罚小类</option>
			    	</select>
			    	<select name="contractColumnOrder">
			    		<option value="asc">升序</option>
			    		<option value="desc">降序</option>
			    	</select>
		        </td>
		        <td></td>
		        <td></td>
         	</tr>
         	<tr>
         	<td colspan="4" >
         	&nbsp;
         	</td>
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
		<form action="<%=request.getContextPath()%>/punishinsideBill/penalizeInsideList" method="post" id="queryPenalizeInsideListForm">
			<table width="60%" style="margin-top: 10px;font-size: 10px;">
				  <tr>
						<td align="left">扣罚大类</td>
		         		<td>
			         		<select name="punishbigsort" >
			         			<option value="0">全部</option>
			         			<c:forEach items="${punishbigsortList}" var="bigsort">
			         				<option value="${bigsort.id}">${bigsort.text}</option>
								</c:forEach>
			         		</select>
			         	</td>
			         	<td align="left">创建日期</td>
	         			<td>
			         		<input type="text" name="punishNoCreateBeginDate"  class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '开始日期'" value="${punishinsideBill.punishNoCreateBeginDate}"/>
			         		至 
		   	       		 	<input type="text" name="punishNoCreateEndDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: '结束日期'" value="${punishinsideBill.punishNoCreateEndDate}"/>
		         		</td>
		         	</tr>
		         	<tr>
		         		<td align="left">扣罚小类</td>
		         		<td>
			         		<select name="punishsmallsort" >
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
				  		<input type="hidden" name="id" value="${punishinsideBill.id}">
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
					<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll()"/> </td>
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
					<td align="center" valign="middle" >${list.dutybranchname}</td>
					<td align="center" valign="middle" >${list.punishInsideprice}</td>
					<td>
						<c:forEach items="${punishbigsortList}" var="bigsort">
						<c:if test="${list.punishbigsort==bigsort.id}">${bigsort.text}</c:if>
						</c:forEach>
					</td>
					<td>
						<c:forEach items="${punishsmallsortList}" var="smallsort">
						<c:if test="${list.punishbigsort==smallsort.id}">${smallsort.text}</c:if>
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
		<%-- <c:if test="${page_obj.maxpage>1}">
			<div class="iframe_bottom"> 
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr>
						<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
						　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
								<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
								</c:forEach>
								</select>页
						</td>
					</tr>
				</table>
			</div>
		</c:if> --%>
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
</body>
</html>