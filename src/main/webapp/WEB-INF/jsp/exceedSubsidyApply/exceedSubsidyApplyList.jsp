<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>超围/大件补助申请</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/commonUtil.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/workorder/csPushSmsList.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$('#addPage').dialog('close');
	$('#queryPage').dialog('close');
	$("table#listTable tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
});
function addInit(){ }
function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}
function queryApplyList(){
	$("#queryForm").submit();
	$('#queryPage').dialog('close');
}
function updatePage(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array(); //定义一数组 
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) {	
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
function updateExceedSubsidyApply(state,form,type,page){
	if(state) changeApplyState(state,form);
	updateExceedSubsidyApplyFun(form,type,page);
}
function updateExceedSubsidyApplyFun(form,type,page){
	/* var cwbExist = 1;
	validateCwbOrder(form,cwbExist);
	if(cwbExist == 0){
		alert("订单号不存在!");
		return false;
	} */
	var cwb = $("#"+form+" input[name='cwbOrder']").val();
	if(!cwb){
		alert("订单号为必填项!");
		return false;
	}
	var applyId = $("#"+form+" input[name='id']").val();
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/exceedSubsidyApply/validateCwbOrder",
		data:{"cwb":cwb,"id":applyId},
		dataType:"json",
		success:function(data){
			if(data){
				if(data.isExist==0){
					alert("订单号不存在!");
					return false;
				} else if(data.isExist==1){
					$("#"+form+" select[name='cwbOrderState']").val(data.cwbOrderState);
					$("#"+form+" input[name='receiveAddress']").val(data.receiveAddress);
					if(!$("#"+form+" select[name='deliveryPerson']").val()){
						alert("配送员为必填项!");
						return false;
					}
					if($("#"+form+" input[type='checkbox'][name='isExceedAreaSubsidy']").is(':checked') == true){
						$("#"+form+" input[type='checkbox'][name='isExceedAreaSubsidy']").attr("value",1);
						if(!$("#"+form+" input[name='exceedAreaSubsidyRemark']").val()){
							alert("超区补助为必填项!");
							return false;
						}
					}
					if($("#"+form+" input[type='checkbox'][name='isBigGoodsSubsidy']").is(':checked') == true){
						$("#"+form+" input[type='checkbox'][name='isBigGoodsSubsidy']").attr("value",1);
						if(!$("#"+form+" input[name='bigGoodsSubsidyRemark']").val()){
							alert("大件补助为必填项!");
							return false;
						}
					}
					var remark = $("#"+form+" textarea[name='remark']").val();
					if (remark) {
						if (remark == "不超过100字") {
							$("#"+form+" textarea[name='remark']").val('');
						} else {
							if (remark.length > 100) {
								alert("备注不超过100字!");
								return false;
							}
						}
					}
					if(type == "check"){
						var exceedAreaSubsidyAmount = $("#"+form+" input[name='exceedAreaSubsidyAmount']").val();
						if(exceedAreaSubsidyAmount && !isFloat(exceedAreaSubsidyAmount)){
							alert("超区补助金额请输入数字!");
							$("#"+form+" input[name='exceedAreaSubsidyAmount']").val('');
							return false;
						}
						var bigGoodsSubsidyAmount = $("#"+form+" input[name='bigGoodsSubsidyAmount']").val();
						if(bigGoodsSubsidyAmount && !isFloat(bigGoodsSubsidyAmount)){
							alert("大件补助金额请输入数字!");
							$("#"+form+" input[name='bigGoodsSubsidyAmount']").val('');
							return false;
						}
						var shenHeIdea = $("#"+form+" textarea[name='shenHeIdea']").val();
						if (shenHeIdea) {
							if (shenHeIdea == "不超过100字") {
								$("#"+form+" textarea[name='shenHeIdea']").val('');
							} else {
								if (shenHeIdea.length > 100) {
									alert("审核意见不超过100字!");
									return false;
								}
							}
						}
					}
					$("#"+form+" select[name='deliveryPerson']").attr("disabled",false);
					$("#"+form+" select[name='applyState']").attr("disabled",false);
					$("#"+form+" select[name='cwbOrderState']").attr("disabled",false);
					$("#"+form+" input[type='checkbox'][name='isExceedAreaSubsidy']").attr("disabled",false);
					$("#"+form+" input[type='checkbox'][name='isBigGoodsSubsidy']").attr("disabled",false);
					//$("#updateForm").submit();
					$.ajax({
						type : "POST",
						data : $("#"+form).serialize(),
						url : $("#"+form).attr('action'),
						dataType : "json",
						success : function(data) {
								if(data && data.errorCode==0){
									if(type == "save"){
										alert("保存成功!");
									} else if(type == "submit"){
										alert("提交成功!");
									} else if(type == "check"){
										alert("审批成功!");
									}
								  	$("#"+page).dialog('close');
								  	//document.location.reload(true);
						   			window.location.href='<%=request.getContextPath()%>/exceedSubsidyApply/exceedSubsidyApplyList/1';
								}
							}
						});
				} else if(data.isExist==2){
					alert("订单号重复!");
					return false;
				}
			}
		}
	});
}
function validateCwbOrder(form,cwbExist){
	var cwb = $("#"+form+" input[name='cwbOrder']").val();
	if(!cwb){
		alert("订单号为必填项!");
		return false;
	}
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/exceedSubsidyApply/validateCwbOrder",
		data:{"cwb":cwb},
		dataType:"json",
		success:function(data){
			if(data){
				if(data.isExist==1){
					$("#"+form+" select[name='cwbOrderState']").val(data.cwbOrderState);
					$("#"+form+" input[name='receiveAddress']").val(data.receiveAddress);
				} else {
					cwbExist = 0;
					/* alert("订单号不存在!");
					return false; */
				}
			}
		}
	});
}
function deleteExceedSubsidyApply(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array(); //定义一数组 
	var ids = "";
	var sign = 0;
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true){
			strs = $(this).val().split(",");
			if(strs[1] == '${xinJianState}'){
				ids = ids + strs[0] + ",";
			} else { sign = 1; }
		}
	}); 
	if(sign == 1)alert("只有新建状态才可删除!");
	if(!ids)return;
	ids = ids.substring(0,ids.length-1);
	if(window.confirm("是否确定删除?")){
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/exceedSubsidyApply/deleteExceedSubsidyApply",
			data:{"ids":ids},
			dataType:"json",
			success:function(data){
					if(data && data.errorCode==0){
						alert(data.error);
						window.location.href='<%=request.getContextPath()%>/exceedSubsidyApply/exceedSubsidyApplyList/1';
							}
						}
					});
		}
	}
	function addexceedSubsidyApplyPage() {
		$('#addPage').dialog('open');
		$("#addForm textarea[name='remark']")
				.focus(function() {
							if ($("#addForm textarea[name='remark']").val()
									&& $("#addForm textarea[name='remark']")
											.val() == '不超过100字') {
								$("#addForm textarea[name='remark']").val('');
							}
						}).blur(function() {
							if (!$("#addForm textarea[name='remark']").val()) {
								$("#addForm textarea[name='remark']").val('不超过100字');
							}
						});
	}
	function addExceedSubsidyApply(form) {
		var cwb = $("#"+form+" input[name='cwbOrder']").val();
		if(!cwb){
			alert("订单号为必填项!");
			return false;
		}
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/exceedSubsidyApply/validateCwbOrder",
			data:{"cwb":cwb},
			dataType:"json",
			success:function(data){
				if(data){
					if(data.isExist==0){
						alert("订单号不存在!");
						return false;
					} else if(data.isExist==1){
						$("#"+form+" select[name='cwbOrderState']").val(data.cwbOrderState);
						$("#"+form+" input[name='receiveAddress']").val(data.receiveAddress);
						if(!$("#"+form+" select[name='deliveryPerson']").val()){
							alert("配送员为必填项!");
							return false;
						}
						if($("#"+form+" input[type='checkbox'][name='isExceedAreaSubsidy']").is(':checked') == true){
							$("#"+form+" input[type='checkbox'][name='isExceedAreaSubsidy']").attr("value",1);
							if(!$("#"+form+" input[name='exceedAreaSubsidyRemark']").val()){
								alert("超区补助为必填项!");
								return false;
							}
						}
						if($("#"+form+" input[type='checkbox'][name='isBigGoodsSubsidy']").is(':checked') == true){
							$("#"+form+" input[type='checkbox'][name='isBigGoodsSubsidy']").attr("value",1);
							if(!$("#"+form+" input[name='bigGoodsSubsidyRemark']").val()){
								alert("大件补助为必填项!");
								return false;
							}
						}
						var remark = $("#"+form+" textarea[name='remark']").val();
						if (remark) {
							if (remark == "不超过100字") {
								$("#"+form+" textarea[name='remark']").val('');
							} else {
								if (remark.length > 100) {
									alert("备注不超过100字!");
									return false;
								}
							}
						}
						$("#"+form+" select[name='deliveryPerson']").attr("disabled",false);
						$("#"+form+" select[name='applyState']").attr("disabled",false);
						$("#"+form+" select[name='cwbOrderState']").attr("disabled",false);
						$("#"+form).submit();
					} else if(data.isExist==2){
						alert("订单号重复!");
						return false;
					}
				}
			}
		});
	}
	function submitExceedSubsidyApply(state,form){
		changeApplyState(state,form);
		addExceedSubsidyApply(form);
	}
	function queryPage() {
		if('${deliveryAuthority}' == 1){
			$("#queryForm input[name='deliveryPersonName']").val('${currentUser.realname}');
			$("#queryForm input[name='deliveryPersonName']").attr("disabled","disabled");
		} else if('${advanceAuthority}' == 1){
			$("#queryForm select[name='applyState'] option[value='${xinJianState}']").remove();
		}
		$('#queryPage').dialog('open');
	}
	function changeApplyState(state,form) {
		if (state) {
			if(state == 'WeiShenHe'){
				if (confirm("是否确认提交申请?")) {
					$("#"+form+" select[name='applyState']").val('${weiShenHeState}');
				}
			} else if (state == 'YiShenHe') {
				if (confirm("是否确认审核通过?")) {
					$("#"+form+" select[name='applyState']").val('${yiShenHeState}');
				}
			} else if (state == 'YiJuJue') {
				if (confirm("是否确认拒绝通过?")) {
					$("#"+form+" select[name='applyState']").val('${yiJuJueState}');
				}
			}
		}
	}
	function getCurrentTime() { 
        var now = new Date();
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        var ss = now.getSeconds();          //秒
        var clock = year + "-";
        if(month < 10) clock += "0";
        clock += month + "-";
        if(day < 10) clock += "0";
        clock += day + " ";
        if(hh < 10) clock += "0";
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm + ":"; 
        if (ss < 10) clock += '0'; 
        clock += ss; 
        return clock; 
    }
</script>
</head>
<body style="background: #eef9ff">
	<div class="right_box">
		<div class="inputselect_box">
			<table style="width: 60%">
				<tr>
					<td>
						<c:if test="${advanceAuthority!=1}">
							<input class="input_button2" type="button" onclick="addexceedSubsidyApplyPage()" value="新增" />
						</c:if>
						<input class="input_button2" type="button" onclick="updatePage()" value="查看/修改" />
						<c:if test="${advanceAuthority!=1}">
							<input class="input_button2" type="button" onclick="deleteExceedSubsidyApply()" value="删除" />
						</c:if>
						<input class="input_button2" type="button" onclick="queryPage()" value="查询" />
					</td>
				</tr>
			</table>
		</div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="right_title">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="listTable">
					<tr>
						<td height="30px" valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('listTable')" /></td>
						<td align="center" valign="middle" style="font-weight: bold;">申请编号</td>
						<td align="center" valign="middle" style="font-weight: bold;">订单号</td>
						<td align="center" valign="middle" style="font-weight: bold;">配送员</td>
						<td align="center" valign="middle" style="font-weight: bold;">状态</td>
						<td align="center" valign="middle" style="font-weight: bold;">备注</td>
					</tr>
					<c:forEach items="${exceedSubsidyApplyList}" var="apply">
						<tr>
							<td height="30px" align="center" valign="middle"><input type="checkbox" name="checkBox" value="${apply.id}${','}${apply.applyState}"/></td>
							<td align="center" valign="middle">${apply.applyNo}</td>
							<td align="center" valign="middle">${apply.cwbOrder}</td>
							<td align="center" valign="middle">
								<c:forEach items="${deliveryUserList}" var="user">
									<c:if test="${apply.deliveryPerson==user.userid}">${user.realname}</c:if>
								</c:forEach>
							</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${applyStateMap}">
									<c:if test="${apply.applyState==item.key}">${item.value}</c:if>
								</c:forEach></td>
							<td align="center" valign="middle">${apply.remark}</td>
						</tr>
					</c:forEach>
				</table>
		</div>
	</div>
	<!-- 新增层显示 -->
	<div id="addPage" class="easyui-dialog" title="新增" data-options="iconCls:'icon-save'" style="width: 780px; height: 300px;">
		<form action="<%=request.getContextPath()%>/exceedSubsidyApply/addExceedSubsidyApply" method="post" id="addForm">
			<table width="100%" border="0" cellspacing="5" cellpadding="0" style="margin-top: 10px; font-size: 10px; border-collapse: separate">
				<tr>
					<td colspan="4" align="left">
						<input type="button" class="input_button2" value="返回" onclick="$('#addPage').dialog('close');" />
						<input type="button" class="input_button2" value="保存" onclick="addExceedSubsidyApply('addForm')" />
						<input type="button" class="input_button2" value="提交申请" onclick="submitExceedSubsidyApply('WeiShenHe','addForm')" />
					</td>
				</tr>
				<tr>
					<td align="left"><font color="red">*</font>申请编号</td>
					<td> <input type="text" name="applyNo" readonly="readonly" style="background-color: #DCDCDC" value="[自动生成]"></td>
					<td align="left"><font color="red">*</font>状态</td>
					<td>
						<select name="applyState" disabled="disabled">
							<c:forEach items="${applyStateMap}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</td>
					<td align="left"><font color="red">*</font>配送员</td>
					<td>
						<select name="deliveryPerson" <c:if test="${masterAuthority!=1}">disabled="disabled"</c:if>>
							<option value=""></option>
							<c:forEach items="${deliveryUserList}" var="user">
								<c:if test="${currentUser.userid==user.userid}">
									<option value="${user.userid}" selected="selected">${user.realname}</option>
								</c:if>
								<c:if test="${currentUser.userid!=user.userid}">
									<option value="${user.userid}">${user.realname}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>订单号
					</td>
					<td>
						<input type="text" name="cwbOrder" value="">
					</td>
					<td align="left">
						<font color="red">*</font>订单状态
					</td>
					<td>
						<select name="cwbOrderState" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${cwbStateMap}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>收货地址
					</td>
					<td colspan="5">
						<input type="text" name="receiveAddress" readonly="readonly" style="background-color: #DCDCDC">
					</td>
				</tr>
				<tr>
					<td align="left">
						<input type="checkbox" name="isExceedAreaSubsidy"/>超区补助
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyRemark" style="width: 100%;"/>
					</td>
				</tr>
				<tr>
					<td align="left">
						<input type="checkbox" name="isBigGoodsSubsidy"/>大件补助
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyRemark" style="width: 100%;"/>
					</td>
				</tr>
				<tr>
					<td align="left">备注</td>
					<td colspan="5">
						<textarea name="remark" style="width: 100%; height: 60px; resize: none;">不超过100字</textarea>
	         		</td>
				</tr>
			</table>
		</form>
	</div>
	
	<!-- 查看/修改层显示-->
	<!-- 状态为新建-->	
	<!-- 角色为配送员-->
	<c:if test="${xinJianStatePage==1 && deliveryAuthority == 1}">
	<div id="updatePageForXinJian" class="easyui-dialog" title="编辑"
		data-options="iconCls:'icon-save'"
		style="width: 780px; height: 300px;">
		<form
			action="<%=request.getContextPath()%>/exceedSubsidyApply/updateExceedSubsidyApply"
			method="post" id="updateFormForXinJian">
			<table width="100%" border="0" cellspacing="5" cellpadding="0"
				style="margin-top: 10px; font-size: 10px; border-collapse: separate">
				<tr>
					<td colspan="4" align="left">
						<input type="button" class="input_button2" value="返回" onclick="$('#updatePageForXinJian').dialog('close');" />
						<input type="button" class="input_button2" value="保存" onclick="updateExceedSubsidyApply('','updateFormForXinJian','save','updatePageForXinJian')" />
						<input type="button" class="input_button2" value="提交申请" onclick="updateExceedSubsidyApply('WeiShenHe','updateFormForXinJian','submit','updatePageForXinJian')" />
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>申请编号
					</td>
					<td>
						<input type="text" name="applyNo" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.applyNo}">
						<input type="hidden" name="id" value="${exceedSubsidyApplyVO.id}">
					</td>
					<td align="left">
						<font color="red">*</font>状态
					</td>
					<td>
						<select name="applyState" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${applyStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.applyState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.applyState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td align="left">
						<font color="red">*</font>配送员
					</td>
					<td>
						<select name="deliveryPerson" <c:if test="${masterAuthority!=1}">disabled="disabled"</c:if>>
							<option value=""></option>
							<c:forEach items="${deliveryUserList}" var="user">
								<c:if test="${exceedSubsidyApplyVO.deliveryPerson==user.userid}">
									<option value="${user.userid}" selected="selected">${user.realname}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.deliveryPerson!=user.userid}">
									<option value="${user.userid}">${user.realname}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>订单号
					</td>
					<td>
						<input type="text" name="cwbOrder" value="${exceedSubsidyApplyVO.cwbOrder}">
					</td>
					<td align="left">
						订单状态
					</td>
					<td>
						<select name="cwbOrderState" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${cwbStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">
						收货地址
					</td>
					<td colspan="5">
						<input type="text" style="width: 100%;" name="receiveAddress" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.receiveAddress}">
					</td>
				</tr>
				<tr>
					<td align="left">
						<input type="checkbox" name="isExceedAreaSubsidy" <c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>checked</c:if>/>超区补助
					</td>
					<td colspan="5">
						<input type="text" style="width: 100%;" name="exceedAreaSubsidyRemark" value="${exceedSubsidyApplyVO.exceedAreaSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">
						<input type="checkbox" name="isBigGoodsSubsidy" <c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>checked</c:if>/>大件补助
					</td>
					<td colspan="5">
						<input type="text" style="width: 100%;" name="bigGoodsSubsidyRemark" value="${exceedSubsidyApplyVO.bigGoodsSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">备注</td>
					<td colspan="5">
						<textarea name="remark" style="width: 100%; height: 60px; resize: none;">${exceedSubsidyApplyVO.remark}</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
	</c:if>
	
	<!-- 查看/修改层显示 -->
	<!-- 状态为未审核 -->
	<!-- 角色为配送员 -->
	<c:if test="${weiShenHeStatePage==1 && deliveryAuthority == 1}">
		<div id="updatePageForWeiShenHe" class="easyui-dialog" title="编辑"
			data-options="iconCls:'icon-save'"
			style="width: 780px; height: 300px;">
			<form
				action="<%=request.getContextPath()%>/exceedSubsidyApply/updateExceedSubsidyApply"
				method="post" id="updateFormForWeiShenHe">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; font-size: 10px;">
					<tr>
						<td colspan="6" align="left">
							<input type="button" class="input_button2" value="返回" onclick="$('#updatePageForWeiShenHe').dialog('close');" />
						</td>
					</tr>
					<tr>
					<td align="left">
						<font color="red">*</font>申请编号
					</td>
					<td>
						<input type="text" name="applyNo" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.applyNo}">
					</td>
					<td align="left">
						<font color="red">*</font>状态
					</td>
					<td>
						<select name="applyState" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${applyStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.applyState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.applyState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td align="left">
						<font color="red">*</font>配送员
					</td>
					<td>
						<input type="text" name="deliveryPersonName" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.deliveryPersonName}">
						<input type="hidden" name="deliveryPerson" value="${exceedSubsidyApplyVO.deliveryPerson}">
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>订单号
					</td>
					<td>
						<input type="text" name="cwbOrder" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.cwbOrder}">
					</td>
					<td align="left">
						订单状态
					</td>
					<td>
						<select name="cwbType" disabled="disabled">
							<option value=""></option>
							<c:forEach items="${cwbStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">
						收货地址
					</td>
					<td colspan="5">
						<input type="text" name="receiveAddress" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.receiveAddress}">
					</td>
				</tr>
				<c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isExceedAreaSubsidy" disabled="disabled" <c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>checked</c:if> disabled="disabled"/>超区补助
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.exceedAreaSubsidyRemark}">
					</td>
				</tr>
				</c:if>
				<c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isBigGoodsSubsidy" disabled="disabled" <c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>checked</c:if> disabled="disabled"/>大件补助
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.bigGoodsSubsidyRemark}">
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="left">备注</td>
					<td colspan="5">
						<textarea name="remark" style="width: 100%; height: 60px; resize: none;background-color: #DCDCDC" readonly="readonly">${exceedSubsidyApplyVO.remark}</textarea>
					</td>
				</tr>
				</table>
			</form>
		</div>
	</c:if>
	
	<!-- 查看/修改层显示 -->
	<!-- 状态为已审核或已拒绝 -->
	<!-- 角色为全部 -->
	<c:if test="${yiShenHeStatePage==1 || yiJuJueStatePage==1}">
		<div id="updatePageForYiShenHeOrYiJuJue" class="easyui-dialog" title="编辑"
			data-options="iconCls:'icon-save'"
			style="width: 780px; height: 400px;">
			<form action="<%=request.getContextPath()%>/exceedSubsidyApply/updateExceedSubsidyApply"
				method="post" id="updateFormForYiShenHeOrYiJuJue">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; font-size: 10px;">
					<tr>
						<td colspan="6" align="left">
							<input type="button" class="input_button2" value="返回" onclick="$('#updatePageForYiShenHeOrYiJuJue').dialog('close');" />
						</td>
					</tr>
					<tr>
					<td align="left">
						<font color="red">*</font>申请编号
					</td>
					<td>
						<input type="text" name="applyNo" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.applyNo}">
					</td>
					<td align="left">
						<font color="red">*</font>状态
					</td>
					<td>
						<select name="applyState" disabled="disabled">
							<c:forEach items="${applyStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.applyState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.applyState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td align="left">
						<font color="red">*</font>配送员
					</td>
					<td>
						<input type="text" name="deliveryPersonName" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.deliveryPersonName}">
						<input type="hidden" name="deliveryPerson" value="${exceedSubsidyApplyVO.deliveryPerson}">
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>订单号
					</td>
					<td>
						<input type="text" name="cwbOrder" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.cwbOrder}">
					</td>
					<td align="left">
						订单状态
					</td>
					<td>
						<select name="cwbOrderState" disabled="disabled">
							<c:forEach items="${cwbStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">
						收货地址
					</td>
					<td colspan="5">
						<input type="text" name="receiveAddress" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.receiveAddress}">
					</td>
				</tr>
				<c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isExceedAreaSubsidy" <c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>checked</c:if> disabled="disabled"/>超区补助
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.exceedAreaSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">
						超区补助金额
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyAmount" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.exceedAreaSubsidyAmount}">
					</td>
				</tr>
				</c:if>
				<c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isBigGoodsSubsidy" <c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>checked</c:if> disabled="disabled"/>大件补助
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.bigGoodsSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">
						大件补助金额
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyAmount" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.bigGoodsSubsidyAmount}">
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="left">备注</td>
					<td colspan="5">
						<textarea name="remark" readonly="readonly" style="width: 100%; height: 60px; resize: none;background-color: #DCDCDC">${exceedSubsidyApplyVO.remark}</textarea>
					</td>
				</tr>
				<tr>
					<td align="left">审核意见</td>
					<td colspan="5">
						<textarea name="shenHeIdea" readonly="readonly" style="width: 100%; height: 60px; resize: none;background-color: #DCDCDC">${exceedSubsidyApplyVO.shenHeIdea}</textarea>
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>审核人
					</td>
					<td>
						<input type="text" name="shenHePersonName" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.shenHePersonName}">
						<input type="hidden" name="shenHePerson" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.shenHePerson}">
					</td>
					<td align="left">
						<font color="red">*</font>审核时间
					</td>
					<td>
						<input type="text" name="shenHeTime" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.shenHeTime}">
					</td>
					<td></td>
					<td></td>
				</tr>
				</table>
			</form>
		</div>
	</c:if>
	
	<!-- 查看/修改层显示 -->
	<!-- 状态为未审核 -->
	<!-- 角色为审批人 -->
	<c:if test="${weiShenHeStatePage==1 && advanceAuthority==1}">
		<div id="updatePageForWeiShenHeByAdvanceAuthority" class="easyui-dialog" title="编辑"
			data-options="iconCls:'icon-save'"
			style="width: 780px; height: 400px;">
			<form
				action="<%=request.getContextPath()%>/exceedSubsidyApply/updateExceedSubsidyApply"
				method="post" id="updateFormForWeiShenHeByAdvanceAuthority">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; font-size: 10px;">
					<tr>
						<td colspan="6" align="left">
							<input type="button" class="input_button2" value="返回" onclick="$('#updatePageForWeiShenHeByAdvanceAuthority').dialog('close');" />
							<input type="button" class="input_button2" onclick="updateExceedSubsidyApply('YiShenHe','updateFormForWeiShenHeByAdvanceAuthority','check','updatePageForWeiShenHeByAdvanceAuthority')" value="审核通过" />
							<input type="button" class="input_button2" onclick="updateExceedSubsidyApply('YiJuJue','updateFormForWeiShenHeByAdvanceAuthority','check','updatePageForWeiShenHeByAdvanceAuthority')" value="拒绝通过" />
						</td>
					</tr>
					<tr>
					<td align="left">
						<font color="red">*</font>申请编号
					</td>
					<td>
						<input type="text" name="applyNo" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.applyNo}">
						<input type="hidden" name="id" value="${exceedSubsidyApplyVO.id}">
					</td>
					<td align="left">
						<font color="red">*</font>状态
					</td>
					<td>
						<select name="applyState" disabled="disabled">
							<c:forEach items="${applyStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.applyState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.applyState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td align="left">
						<font color="red">*</font>配送员
					</td>
					<td>
						<input type="text" name="deliveryPersonName" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.deliveryPersonName}">
						<input type="hidden" name="deliveryPerson" value="${exceedSubsidyApplyVO.deliveryPerson}">
					</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>订单号
					</td>
					<td>
						<input type="text" name="cwbOrder" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.cwbOrder}">
					</td>
					<td align="left">
						<font color="red">*</font>订单状态
					</td>
					<td>
						<select name="cwbOrderState" disabled="disabled">
							<c:forEach items="${cwbStateMap}" var="item">
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${exceedSubsidyApplyVO.cwbOrderState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>收货地址
					</td>
					<td colspan="5">
						<input type="text" name="receiveAddress" readonly="readonly" style="background-color: #DCDCDC" value="${exceedSubsidyApplyVO.receiveAddress}">
					</td>
				</tr>
				<c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isExceedAreaSubsidy" <c:if test='${exceedSubsidyApplyVO.isExceedAreaSubsidy == 1}'>checked</c:if> disabled="disabled"/>超区补助
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.exceedAreaSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">
						超区补助金额
					</td>
					<td colspan="5">
						<input type="text" name="exceedAreaSubsidyAmount" value="${exceedSubsidyApplyVO.exceedAreaSubsidyAmount}" maxlength="20">
					</td>
				</tr>
				</c:if>
				<c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>
				<tr>
					<td align="left">
						<input type="checkbox" name="isBigGoodsSubsidy" <c:if test='${exceedSubsidyApplyVO.isBigGoodsSubsidy == 1}'>checked</c:if> disabled="disabled"/>大件补助
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyRemark" readonly="readonly" style="width: 100%;background-color: #DCDCDC" value="${exceedSubsidyApplyVO.bigGoodsSubsidyRemark}">
					</td>
				</tr>
				<tr>
					<td align="left">
						大件补助金额
					</td>
					<td colspan="5">
						<input type="text" name="bigGoodsSubsidyAmount" value="${exceedSubsidyApplyVO.bigGoodsSubsidyAmount}" maxlength="20">
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="left">备注</td>
					<td colspan="5">
						<textarea name="remark" readonly="readonly" style="width: 100%; height: 60px; resize: none; background-color: #DCDCDC;">${exceedSubsidyApplyVO.remark}</textarea>
	         		</td>
				</tr>
				<tr>
					<td align="left">审核意见</td>
					<td colspan="5">
						<textarea name="shenHeIdea" style="width: 100%; height: 60px; resize: none;" 
						onfocus="if(this.value=='不超过100字') {this.value='';}" 
						onblur="if(this.value=='') {this.value='不超过100字';}">不超过100字</textarea>
	         		</td>
				</tr>
				<tr>
					<td align="left">
						<font color="red">*</font>审核人
					</td>
					<td>
						<input type="text" name="shenHePersonName" readonly="readonly" style="background-color: #DCDCDC" value="${currentUser.realname}">
						<input type="hidden" name="shenHePerson" value="${currentUser.userid}">
					</td>
					<td align="left">
						<font color="red">*</font>审核时间
					</td>
					<td>
						<input type="text" name="shenHeTime" readonly="readonly" style="background-color: #DCDCDC" value="${currentTime}">
					</td>
					<td></td>
					<td></td>
				</tr>
				</table>
			</form>
		</div>
	</c:if>
	
	<!-- 查询层显示 -->
	<div id="queryPage" class="easyui-dialog" title="查询条件"
		data-options="iconCls:'icon-save'"
		style="width: 780px; height: 220px;">
		<form
			action="<%=request.getContextPath()%>/exceedSubsidyApply/exceedSubsidyApplyList/1"
			method="post" id="queryForm">
			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				style="margin-top: 10px; font-size: 10px;">
				<tr>
					<td align="left" style="width: 15%;">订单号</td>
					<td style="width: 30%;">
						<input type="text" name="cwbOrder" value="${queryConditionVO.cwbOrder}" />
					</td>
					<td align="left" style="width: 15%;">状态</td>
					<td style="width: 30%;">
						<select name="applyState">
							<option value="0">---全部---</option>
							<c:forEach var="item" items="${applyStateMap}">
								<c:if test="${queryConditionVO.applyState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${queryConditionVO.applyState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td align="left">配送员</td>
					<td><input type="text" name="deliveryPersonName"
						value="${queryConditionVO.deliveryPersonName}" /></td>
					<td align="left">排序</td>
					<td>
						<select name="column">
							<option
								<c:if test='${queryConditionVO.column == applyNo}'>selected="selected"</c:if>
								value="applyNo">申请编号</option>
							<option
								<c:if test='${queryConditionVO.column == cwbOrder}'>selected="selected"</c:if>
								value="cwbOrder">订单号</option>
							<option
								<c:if test='${queryConditionVO.column == deliveryPersonName}'>selected="selected"</c:if>
								value="deliveryPersonName">配送员</option>
							<option
								<c:if test='${queryConditionVO.column == applyState}'>selected="selected"</c:if>
								value="applyState">状态</option>
						</select> 
						<select name="columnOrder">
							<option
								<c:if test='${queryConditionVO.columnOrder == asc}'>selected="selected"</c:if>
								value="asc">升序</option>
							<option
								<c:if test='${queryConditionVO.columnOrder == desc}'>selected="selected"</c:if>
								value="desc">降序</option>
						</select>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4" rowspan="2" align="center" valign="bottom">
						<input type="button" class="input_button2" value="查询" onclick="queryApplyList()" />
						<input type="button" class="input_button2" value="关闭" onclick="$('#queryPage').dialog('close');" />
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="updatePageDiv">
		<form
			action="<%=request.getContextPath()%>/exceedSubsidyApply/updateExceedSubsidyApplyPage"
			method="post" id="updatePageForm">
			<input type="hidden" name="id" value="">
		</form>
	</div>

<div class="jg_10"></div>
<div class="jg_10"></div>
<c:if test="${page_obj.maxpage>1}">
<div class="iframe_bottom"> 
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#exceedSubsidyApplyListForm').attr('action','${pageContext.request.contextPath}/exceedSubsidyApply/exceedSubsidyApplyList/1');$('#exceedSubsidyApplyListForm').submit();" >第一页</a>　
			<a href="javascript:$('#exceedSubsidyApplyListForm').attr('action','${pageContext.request.contextPath}/exceedSubsidyApply/exceedSubsidyApplyList/${page_obj.previous<1?1:page_obj.previous}');$('#exceedSubsidyApplyListForm').submit();">上一页</a>　
			<a href="javascript:$('#exceedSubsidyApplyListForm').attr('action','${pageContext.request.contextPath}/exceedSubsidyApply/exceedSubsidyApplyList/${page_obj.next<1?1:page_obj.next }');$('#exceedSubsidyApplyListForm').submit();" >下一页</a>　
			<a href="javascript:$('#exceedSubsidyApplyListForm').attr('action','${pageContext.request.contextPath}/exceedSubsidyApply/exceedSubsidyApplyList/${page_obj.maxpage<1?1:page_obj.maxpage}');$('#exceedSubsidyApplyListForm').submit();" >最后一页</a>
			　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
			<select id="selectPg" onchange="$('#exceedSubsidyApplyListForm').attr('action',$(this).val());$('#exceedSubsidyApplyListForm').submit()">
				<c:forEach var="i" begin='1' end='${page_obj.maxpage}'>
					<option value='${pageContext.request.contextPath}/exceedSubsidyApply/exceedSubsidyApplyList/${i}' ${page==i?'selected=seleted':''}>${i}</option>
				</c:forEach>
			</select>页
			</td>
		</tr>
	</table>
</div>
</c:if>
<div>
	<form action="<%=request.getContextPath()%>/exceedSubsidyApply/exceedSubsidyApplyList/1" method="post" id="exceedSubsidyApplyListForm">
		<input type="hidden" name="cwbOrder" value="${queryConditionVO.cwbOrder}" />
		<input type="hidden" name="applyState" value="${empty queryConditionVO.applyState ? '0' : queryConditionVO.applyState}" />
		<input type="hidden" name="deliveryPersonName" value="${queryConditionVO.deliveryPersonName}" />
		<input type="hidden" name="column" value="${queryConditionVO.column}" />
		<input type="hidden" name="columnOrder" value="${queryConditionVO.columnOrder}" />
	</form>
</div>
</body>
</html>