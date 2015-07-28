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

<title>配送员派费账单</title>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
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
	$('#add').dialog('close');
	$('#find').dialog('close');
	$("#diliveryman").multiSelect({
		oneOrMoreSelected : '*',
		noneSelected : '请选择'
	});
	$("#diliveryman").multiSelect({
		oneOrMoreSelected : '*',
		noneSelected : '请选择'
	});

	$("table#gd_table tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	
	$("#updatePageForm input[name='id']").val("${bill.id}");
	$("#searchForm select[name='billstate'] ").val("${billstate}");
	$("#searchForm select[name='theirsite'] ").val("${theirsite}");
	$("#searchForm select[name='sortField'] ").val("${sortField}");
	$("#searchForm select[name='ordertype'] ").val("${ordertype}");
	$("#searchForm select[name='orderingMethod'] ").val("${orderingMethod}");
	$("#updateForm select[name='billstate']").val("${bill.billstate}");
	
	//站点与配送员下拉框联动
	var $orgId = $("#orgId");
	$orgId.change(function() {
		var orgId = $orgId.val();
		fillStationDeliver(orgId);
	});
	var $theirsite = $("#theirsite");
	$theirsite.change(function() {
		var theirsite = $theirsite.val();
		filltheirsite(theirsite);
	});
	
});
function fillStationDeliver(stationId) {
	$.ajax({
		type : "post",
		dataType : "json",
		url : "${ctx}/smtfaresettle/getstationdeliver?"
				+ Math.random(),
		data : {
			stationId : stationId
		},
		success : function(data) {
			fillDeliverInfo(data);
		}
	});
}
//查询时的站点
function filltheirsite(stationId) {
	$.ajax({
		type : "post",
		dataType : "json",
		url : "${ctx}/smtfaresettle/getstationdeliver?"
				+ Math.random(),
		data : {
			stationId : stationId
		},
		success : function(data) {
			fillDeliver(data);
		}
	});
}
function fillDeliver(data) {
	var $diliveryman = $("#dilivery_man")
	$diliveryman.empty();
	var html = '<select id="diliveryman" name="diliveryman" style="width: 190px;" multiple="multiple">'
	for ( var p in data) {
		html += '<option value="' + p +'">' + data[p] + '</option>'
	}
	html += '</select>';
	$diliveryman.append(html);
	$("#diliveryman").multiSelect({
		oneOrMoreSelected : '*',
		noneSelected : '请选择'
	});
}
//查询时的配送员
function fillDeliverInfo(data) {
	var $deliverArea = $("#deliver_area")
	$deliverArea.empty();
	var html = '<select id="diliveryman" name="diliveryman" style="width: 128px;" multiple="multiple">'
	for ( var p in data) {
		html += '<option value="' + p +'">' + data[p] + '</option>'
	}
	html += '</select>';
	$deliverArea.append(html);
	$("#diliveryman").multiSelect({
		oneOrMoreSelected : '*',
		noneSelected : '请选择'
	});
}

function openaddbill()
{
	$('#add').dialog('open');
	$("#add textarea[name='explain']").focus(function()
		{
			if($("#add textarea[name='explain']").val() && $("#add textarea[name='explain']").val() == '不超过100字')
			{
				$("#add textarea[name='explain']").val('');
			}
	}).blur(function()
		{
			if(!$("#add textarea[name='explain']").val())
			{
				$("#add textarea[name='explain']").val('不超过100字');
			}
		});
}


//打开修改页面
function openUpdatePage()
{
	var chkBoxes = $("#gd_table input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	var billIds = "";
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			strs = $(this).val().split(",");
			billIds =billIds + strs[0] + ",";
		}
	}); 
	billIds = billIds.substring(0,billIds.length-1);
	if(!billIds){
		alert("请选择需要查看/修改的账单！");
		return false;
	}
	if(billIds.indexOf(",") > 0){
		alert("请只选择一条需要查看/修改的账单")
		return false;
	}
	$("#updatePageForm input[name='id']").val(billIds);
	$("#updatePageForm").submit();
}
//修改账单信息
function updateDiliverymanBill()
{
	if(!verify())
	{
		return false;
	}
	$("#updateForm select[name='billstate']").attr("disabled",false);
	$.ajax({
		type : "POST",
		url : $("#updateForm").attr("action"),
		data : $("#updateForm").serialize(),
		dataType : "json",
		success : function(data) {
			if (data.errorCode == 0) {
				alert(data.error)
				$('#update').dialog('close');
				//$("#updatePageForm").submit();
				$("#searchForm").submit();
			}
		}
	});
}
//日期校验
function Days(day1,day2){     
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>60){
		return false;
	}        
	return true;
}	


function addbill()
{
	if($("#creationfrom select[name='site']").val() == "0"){
		alert("请选择所属站点！");
		return false;
	}
	if($("#creationfrom input[name='startDate']").val() == ""){
		alert("请选择开始时间！");
		return false;
	}
	if($("#creationfrom input[name='endDate']").val() == ""){
		alert("请选择结束时间！");
		return false;
	}
	if($("#creationfrom input[name='startDate']").val()>$("#creationfrom input[name='endDate']").val()){
		alert("开始日期不能大于结束日期");
		return false;
	}
	if(!Days($("#creationfrom input[name='startDate']").val(),$("#creationfrom input[name='endDate']").val())||($("#creationfrom input[name='startDate']").val()=='' &&$("#creationfrom input[name='endDate']").val()!='')||($("#creationfrom input[name='startDate']").val()!='' &&$("#creationfrom input[name='endDate']").val()=='')){
		alert("时间跨度不能大于两个月！");
		return false;
	}

	
	if(!verify())
	{
		return false;
	}
	var explain = $("#add #explain").val();
	if(explain == "不超过100字"){
		var explain = $("#add #explain").val("");
	}
	$.messager.show({
		title:'提示消息',
		msg:'正在新增账单，请稍后...',
		timeout:2000,
		showType:'show'
	});

	$.ajax({
		type : "POST",
		url : $("#creationfrom").attr("action"),
		data : $("#creationfrom").serialize(),
		dataType : "json",
		success : function(data) {
				$('#add').dialog('close');
				alert("成功创建 "+data+" 个配送员派费账单")
				$("#searchForm").submit();
		}
	});
}
//移除订单 
function deleteorder(){
	var id = $("#updateForm input[type='hidden'][name='id']").val();
	var chkBoxes = $("#deiliverymanTable input[type='checkbox'][name='checkBox']");
	var ordernumber = "";
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			ordernumber = $(this).val()+","+ordernumber;
		}
	}); 
	ordernumber = ordernumber.substring(0,ordernumber.length-1);
	if(ordernumber == ""){
		alert("请选择要移除的订单！");
		return false;
	}
	if(confirm("确定要移除吗？")){
		$.ajax({
			type : "post",
			dataType : "json",
			url : "${ctx}/diliverymanpaifeibill/deleteorder",
			data : {
				ordernumber : ordernumber,
				id : id
			},
			success : function(data) {
				if(data.errorCode == 0){
					$("#updatePageForm").submit();
					/* $(chkBoxes).each(function() {
						if ($(this)[0].checked == true)
						{
							$(this).parent().parent().remove();
						}
					});  */
				}
			}
		});
	}
}





function checkAll(id)
{ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() 
	{
		$(this)[0].checked = chkAll;
	});
}
//删除
function deleteBill()
{
	
	var chkBoxes = $("#gd_table input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	var billIds = "";
	var sign = 0;
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			strs = $(this).val().split(",");
			if(strs[1] == <%=PunishBillStateEnum.WeiShenHe.getValue()%>){
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
		alert("请选择要删除的账单");
		return false;
	}
	
	billIds = billIds.substring(0,billIds.length-1);
	if(confirm("确定要删除吗？"))
	{
		$.ajax
		({
			type : "POST",
			url : '${ctx}/diliverymanpaifeibill/deletePaiFeiBill',
			data : {id : billIds},
			dataType : "json",
			success : function(data) 
			{
					alert("成功删除"+data+"条账单");
					$("#searchForm").submit();
			}
		});
	}
}
//改变账单状态
function updateBillState(state){
	var id = $("#updateForm input[name='id']").val();
	$.ajax({
		type : "post",
		url : '${ctx}/diliverymanpaifeibill/updateBillState',
		data : {id : id,state :state},
		dataType : "json",
		success : function(data){
			if(data.errorCode==0){
				alert(data.error);
			}
			
		}
	})
}

function changeBillState(state){
	if(state){
		if(state == 'ShenHe'){
			 if(confirm("是否确认审核?")){
				$("#updateForm select[name='billstate']").val('${yiShenHeState}');
				updateDiliverymanBill()
				/* updateBillState('${yiShenHeState}'); */
			 }
		} else if(state == 'QuXiaoShenHe'){
			if(confirm("是否确认取消审核?")){
				$("#updateForm select[name='billstate']").val('${weiShenHeState}');
				updateDiliverymanBill()
				/* updateBillState('${weiShenHeState}'); */
			}
		} else if(state == 'HeXiaoWanCheng'){
			if(confirm("是否确认核销完成?")){
				$("#updateForm select[name='billstate']").val('${yiHeXiaoState}');
				/* updateBillState('${yiHeXiaoState}'); */
				updateDiliverymanBill()
			}
		} else if(state == 'QuXiaoHeXiao'){
			if(confirm("是否确认取消核销?")){
				$("#updateForm select[name='billstate']").val('${yiShenHeState}');
				/* updateBillState('${yiShenHeState}'); */
				updateDiliverymanBill()
			}
		}
	}
}
function verify(){
	var flag = true;
	var pattern = new RegExp("[`'~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;|{}【】‘；：”“'。，、？\"]");
	if($("#explain").val().length >100){
		alert("备注长度不允许超过100位字符！");
		return false;
	}
	if($("#explain").val().length >100){
		alert("备注说明长度不允许超过100位字符！");
		return false;
	}
	return flag;
}
</script>
</head>

<body style="background:#eef9ff">
<div>
<div class="right_box">
	<div class="inputselect_box">
		<table style="width: 60%">
		    <tr>
			    <td>
				    <input class="input_button2" type="button" onclick="openaddbill();" value="新增"/>
				    <input class="input_button2" type="button" onclick="openUpdatePage();" value="查看/修改"/>
				    <input class="input_button2" type="button" onclick="deleteBill();" value="删除"/>
				    <input class="input_button2" type="button" onclick="$('#find').dialog('open');" value="查询"/>
			    </td> 
		    </tr>
		 </table>
	</div>


	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div>
	<div class="right_title">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr>
		<td align="center" valign="middle"style="font-weight: bold;"><input type="checkbox" name="checkAll" onclick="checkAll('gd_table')"/></td>
		<td align="center" valign="middle"style="font-weight: bold;"> 账单批次</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 账单状态</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 配送员</td>
		<td align="center" valign="middle"style="font-weight: bold;"> 所属站点 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 日期范围 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 对应订单数 </td>
		<td align="center" valign="middle"style="font-weight: bold;"> 派费金额(元)</td>
		<td align="center" valign="middle"style="font-weight: bold;width:300px;"> 备注 </td>
	</tr>
	<c:forEach items="${DiliverymanPaifeiBillList}" var="bill">
	<tr onclick="setId(${bill.id},${bill.billstate})">
		<td align="center" valign="middle" ><input type="checkbox" name="checkBox" value="${bill.id},${bill.billstate}" /></td>
		<td align="center" valign="middle" >${bill.billbatch}</td>
		<td align="center" valign="middle" >
			<c:forEach items="${PunishBillStateEnum}" var="state">
				<c:if test="${bill.billstate==state.value}">${state.text }</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${diliverymanName}" var="dilivery">
				<c:if test="${bill.diliveryman==dilivery.userid}">${dilivery.realname }</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			<c:forEach items="${orgMap}" var="om">
				<c:if test="${bill.theirsite==om.key}">${om.value}</c:if>
			</c:forEach>
		</td>
		<td align="center" valign="middle" >
			${bill.daterange}
		</td>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
		<td align="center" valign="middle" >
			${bill.ordersum}
		</td>
		<td align="center" valign="middle" >
			${bill.paifeimoney}
		</td>
		<td align="center" valign="middle" style="width:300px;" >
			${bill.remarks}
		</td>
	</tr>
	</c:forEach>
	</table>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
	</div>
</div>
		<div class="iframe_bottom"> 
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
						<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/diliverymanpaifeibillPage/1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/diliverymanpaifeibillPage/${page_obj.previous<1?1:page_obj.previous}');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/diliverymanpaifeibillPage/${page_obj.next<1?1:page_obj.next }');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/diliverymanpaifeibillPage/${page_obj.maxpage<1?1:page_obj.maxpage}');$('#searchForm').submit();" >最后一页</a>
						　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
						<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
								<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
							</c:forEach>
						</select>页
					</td>
				</tr>
			</table>
		</div>
		</div>
<!-- 新增层显示 -->
<div  id="add" class="easyui-dialog" data-options="modal:true" title="新增"  style="width:700px;height:220px;">
	<form action="${ctx}/diliverymanpaifeibill/addDilivermanBill" method="post" id="creationfrom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         		<tr >
		         	<td colspan="6" align="left" valign="bottom">
		         	<input type="button" class="input_button2" value="返回" onclick="$('#add').dialog('close');"/>
		         	<input type="button" class="input_button2" onclick="addbill();" value="保存"/>
		         	</td>
	         	</tr>
         		<tr colspan="4">
	         		<td align="right" nowrap="nowrap" style="width: 10%;"><font color="red">*</font>所属站点：</td>
	         		<td nowrap="nowrap" style="width: 20%;">
	         			<select id="orgId"
							name="site" class="select1">
							<option value="0">请选择</option>
							<c:forEach items="${orgMap}" var="entry">
								<option value="${entry.key}" <c:if test="${cond.orgId == entry.key}">selected</c:if>>${entry.value}</option>
							</c:forEach>
						</select> 
         			</td>
	         		<td nowrap="nowrap" align="right" style="width: 12%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单类型：</td>
	         		<td nowrap="nowrap" style="width: 20%;">
		         		<select  name="orderType" style="width: 100%;"  >
		         			<option value="0">全部</option>
							<c:forEach items="${OrderTypeEnum}" var="state">
		         			<option value="${state.value}">${state.text}</option>
				        </c:forEach>
		         		</select>
	         		</td>
	         		<td colspan="4"></td>
         	</tr>
         <!--  -->
         	<tr >
         		<td nowrap="nowrap" align="right" >配送员：</td>
         		
         		<td nowrap="nowrap" align="left" id="deliver_area" >
         			<select id="diliveryman" name="diliveryman" style="width: 128px;" multiple="multiple">
						<%--<c:forEach items="${constant.deliverMap}" var="entry">
							<option value="${entry.key}"
								 <c:if test="${fn:contains(cond.delivers,entry.key)}">selected</c:if>>${entry.value}</option> 
						</c:forEach>--%>
					</select>
         		</td>
         		<td nowrap="nowrap"  >
         			<font color="red">*</font><select  name="dateType" style="width: 95%;"  >
	         			<c:forEach items="${DateTypeEnum}" var="date">
							<option value="${date.value}" >${date.text}</option>
						</c:forEach>
		         	</select>
         		</td>
         		
         		<td nowrap="nowrap">
		         	 <input type="text" name="startDate"  class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		 <input type="text" name="endDate" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         		<td colspan="4"></td>
         	<tr>
         		<td></td>
         		<!-- <td>
         			[<a href="javascript:multiSelectAll('delivers',1,'请选择');">全选</a>]
         			[<a href="javascript:multiSelectAll('delivers',0,'请选择');">取消全选</a>]
         		</td> -->
         	</tr>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">备注：</td>
         		<td nowrap="nowrap" colspan="6" >
			   	 	<textarea rows="3"  id="explain" name="explain" style="width: 100%;resize: none;" value="">不超过100字</textarea>
		        </td>
         	</tr>
       </table>
    </form>
</div>
<!-- 查看/修改层显示 -->
<c:if test="${updatePage==1}"> 
<div  id="update" class="easyui-dialog" data-options="modal:true" title="查看/修改"  style="width:1200px;height:600px;">
	<form action="${ctx}/diliverymanpaifeibill/updateDilivermanBill" method="post" id="updateForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
      		<tr>
	        	<td colspan="3"  align="left" valign="bottom" nowrap="nowrap">
	         		<input type="button" class="input_button2" value="返回" onclick="$('#update').dialog('close');"/>
	         		<input type="button" class="input_button2" value="保存" onclick="updateDiliverymanBill()"/>
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
         			<input type="text" style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.billbatch}"/> 
        		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">账单状态：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select id="billstate" name="billstate" style="width: 100%;background-color:#DCDCDC" disabled="disabled"  >
						<c:forEach items="${PunishBillStateEnum}" var="state">
							<option value="${state.value}" >${state.text}</option>
				        </c:forEach>
         			</select>
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;" >日期范围：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input type="text"   style="width: 100%;background-color:#DCDCDC" readonly="readonly" value="${bill.daterange }"></input>
        		 </td>
         		<td colspan="2"> </td>
       		</tr>
       		<tr>
       			<td align="right" nowrap="nowrap" style="width: 10%;">派费合计(元)：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<input type="text"  style="width: 100%;background-color:#DCDCDC" readonly="readonly"  value="${bill.paifeimoney}"/> 
        		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">对应订单数：</td>
         		<td nowrap="nowrap" style="width: 20%;">
		         	<input type="text"  value="${bill.ordersum}" style="width: 100%;background-color:#DCDCDC" readonly="readonly"></input>
         		</td>
         		<td colspan="4"> </td>
         	</tr>
       		<tr>
       			<td align="right" nowrap="nowrap" style="width: 10%;">配送员姓名：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select style="width: 100%;background-color:#DCDCDC" disabled="disabled">
         				<c:forEach items="${diliverymanName}" var="dilivery">
							<c:if test="${dilivery.userid==bill.diliveryman}">
								<option value="${dilivery.userid}" selected="selected">${dilivery.username}</option>
							</c:if>
				        </c:forEach>
         			</select>
        		</td>
         		<td nowrap="nowrap" align="right" style="width: 10%;">所属站点：</td>
         		<td nowrap="nowrap" style="width: 20%;">
         			<select style="width: 100%;background-color:#DCDCDC" disabled="disabled">
         				<c:forEach items="${orgMap}" var="om">
							<c:if test="${om.key==bill.theirsite}">
								<option value="${om.key}" selected="selected">${om.value}</option>
							</c:if>
				        </c:forEach>
         			</select>
         		</td>
         		<td colspan="4"> </td>
         	</tr>
         	<tr>
	         	<td colspan="8" >
	         		<input type="hidden" name="id" value="${bill.id}">
	         		<input type="hidden" name="ordernumber" ></input>
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" rowspan="2">备注：</td>
         		<td nowrap="nowrap" colspan="5" >
			   	 <textarea rows="3"  id="remarks" name="remarks" style="width: 100%;resize: none;" value="">${bill.remarks}</textarea>
		        </td>
         	</tr>
         	<tr>
	         	<td colspan="8" >
	         	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	         	</td>
         	</tr>
         </table>
      </form>
      			<div>
		         	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="deiliverymanTable" >
						<tr>
							<td height="30px"  valign="middle"><input type="checkbox" name="checkAll" onclick="checkAll('deiliverymanTable')"/> </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单编号</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单状态</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 订单类型</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 到货时间 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 发货时间 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 付款方式 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 签收日期 </td>
							<td align="center" valign="middle" style="font-weight: bold;"> 派费合计(元)</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 基本派费</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 代收补助费</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 区域属性补助费</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 超区补助</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 业务补助</td>
							<td align="center" valign="middle" style="font-weight: bold;"> 拖单补助</td>
						</tr>
						<c:forEach var="order" items="${bill.diliverymanPaifeiOrderList}">
						<tr>
							<td height="30px"  valign="middle" align="center"><input type="checkbox" name="checkBox" value="${order.ordernumber}"/> </td>
							<td align="center" valign="middle">${order.ordernumber}</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${FlowOrderTypeEnum}">
									<c:if test="${order.orderstatus==item.value}">${item.text}</c:if>
								</c:forEach> 
							</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${OrderTypeEnum}">
									<c:if test="${order.ordertype==item.value}">${item.text}</c:if>
								</c:forEach> 
							</td>
							<td align="center" valign="middle">${order.timeofdelivery}</td>
							<td align="center" valign="middle">${order.deliverytime}</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${PaytypeEnum}">
									<c:if test="${order.paymentmode==item.value}">${item.text}</c:if>
								</c:forEach> 
							</td>
							<td align="center" valign="middle">${order.dateoflodgment}</td>
							<td align="center" valign="middle">${order.paifeicombined}</td>
							<td align="center" valign="middle">${order.basicpaifei}</td>
							<td align="center" valign="middle">${order.subsidiesfee}</td>
							<td align="center" valign="middle">${order.areasubsidies}</td>
							<td align="center" valign="middle">${order.beyondareasubsidies}</td>
							<td align="center" valign="middle">${order.businesssubsidies}</td>
							<td align="center" valign="middle">${order.delaysubsidies}</td>
							
							</tr>
         				</c:forEach> 
					</table>
					</div>
         
      <div class="iframe_bottom"> 
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff" style="font-size: 10px;">
					<c:if test="${weiShenHeState==bill.billstate}">
						<input type="button" class="input_button2"  onclick="deleteorder()" value="移除"/>
	         		</c:if>
	         		
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/queryById/1');$('#updatePageForm').submit();" >第一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/queryById/${page_ob.previous<1?1:page_ob.previous}');$('#updatePageForm').submit();">上一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/queryById/${page_ob.next<1?1:page_ob.next }');$('#updatePageForm').submit();" >下一页</a>　
					<a href="javascript:$('#updatePageForm').attr('action','<%=request.getContextPath()%>/diliverymanpaifeibill/queryById/${page_ob.maxpage<1?1:page_ob.maxpage}');$('#updatePageForm').submit();" >最后一页</a>
					　共${page_ob.maxpage}页　共${page_ob.total}条记录 　当前第<select
							id="selectPg"
							onchange="$('#updatePageForm').attr('action',$(this).val());$('#updatePageForm').submit()">
							<c:forEach var="i" begin="1" end="${page_ob.maxpage}">
							<option value='${i}' ${pag==i?'selected=seleted':''}>${i}</option>
							</c:forEach>
							</select>页
					</td>
				</tr>
			</table>
		</div>
</div>
</c:if>
<!-- 查询层显示 -->
	<div  id="find" class="easyui-dialog" data-options="modal:true" title="查询条件"  style="width:700px;height:280px;">
	<form action="${ctx}/diliverymanpaifeibill/diliverymanpaifeibillPage/1" method="post" id="searchForm">
         	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="margin-top: 10px;font-size: 10px;">
         	<tr>
         		<td align="right" nowrap="nowrap" style="width: 15%;">账单批次：</td>
         		<td nowrap="nowrap" style="width: 30%;">
         			<input type="text" name="billbatch" style="width: 90%;" value="${billbatch}"/> 
         		</td>
         		<td nowrap="nowrap" align="right" style="width: 15%;">账单状态：</td>
         		<td nowrap="nowrap" style="width: 30%;">
         			<select  name="billstate" style="width: 90%;">
         				<option value="" >---全部---</option>
						<c:forEach items="${PunishBillStateEnum}" var="state">
		         			<option value="${state.value}">${state.text}</option>
				        </c:forEach>
         			</select>
         		</td>
         	</tr>
         	<tr>
	         	<td colspan="4" >
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right">账单创建日期：</td>
         		<td nowrap="nowrap">
		         	 <input type="text" name="billCreationStartDate" value="${billCreationStartDate}"  class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		 <input type="text" name="billCreationEndDate" value="${billCreationEndDate}"   class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         		<td nowrap="nowrap" align="right">账单核销日期：</td>
         		<td nowrap="nowrap">
		         	 <input type="text" name="billVerificationStrartDate" value="${billVerificationStrartDate}" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '起始时间'"/> 到 
	   	       		 <input type="text" name="billVerificationEndDate"   value="${billVerificationEndDate}" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:95,prompt: '终止时间'"/>
         		</td>
         	</tr>
         	<tr>
	         	<td colspan="4" >
	         	<input type="hidden" name="CreationStartDate" value="${CreationStartDate}"/>
	         	<input type="hidden" name="CreationEndDate" value="${CreationEndDate}"/>
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >配送员所属站点：</td>
         		<td nowrap="nowrap">
         			<select id="theirsite" name="theirsite" style="width: 214px;" >
						<option value="0" >---全部---</option>
						<c:forEach items="${orgMap}" var="entry">
							<option value="${entry.key}" <c:if test="${cond.orgId == entry.key}">selected</c:if>>${entry.value}</option>
						</c:forEach>
					</select>
         		</td>
         		<td nowrap="nowrap" align="right" >订单类型：</td>
         		<td nowrap="nowrap">
         			<select  name="ordertype" style="width: 90%;">
         				<option value="" >---全部---</option>
						<c:forEach items="${OrderTypeEnum}" var="state">
		         			<option value="${state.value}">${state.text}</option>
				        </c:forEach>
	         		</select>
         		</td>
         	</tr>
         	<tr>
	         	<td colspan="4" >
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
         		<td nowrap="nowrap" align="right" >配送员姓名：</td>
         		<td nowrap="nowrap" id="dilivery_man">
         			<select id="diliveryman" name="diliveryman" style="width: 190px;" multiple="multiple">
						<%--<c:forEach items="${constant.deliverMap}" var="entry">
							<option value="${entry.key}"
								 <c:if test="${fn:contains(cond.delivers,entry.key)}">selected</c:if>>${entry.value}</option> 
						</c:forEach>--%>
					</select>
         		</td>
         		<td nowrap="nowrap" align="right">排序：</td>
         		<td nowrap="nowrap">
			    	<select style="width:60%;" name="sortField">
			    		<option value="billstate">账单状态</option>
			    		<option value="billbatch">账单批次</option>
			    		<option value="billestablishdate">账单创建日期</option>
			    		<option value="ordersum">对应订单数</option>
			    	</select>
			    	<select style="width:30%;" name="orderingMethod">
			    		<option  value="desc">降序</option>
			    		<option  value="asc">升序</option>
			    	</select>
		        </td>
         	</tr>
         	<tr>
	         	<td colspan="4" >
	         	&nbsp;
	         	</td>
         	</tr>
         	<tr>
	         	<td colspan="4" >
	         	&nbsp;
	         	</td>
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
	<form action="<%=request.getContextPath()%>/diliverymanpaifeibill/queryById/1" method="post" id="updatePageForm">
		<input type="hidden" name="id" value=""/>
		<input type="hidden" name="billstate" value="">
	</form>
</div>
<div id="deiliverymanDiv">
	<form action="<%=request.getContextPath()%>/" method="post" id="deiliverymanForm">
		<input type="hidden" name="id" value=""/>
		<input type="hidden" name="theirsite" />
	</form>
</div>
<div id="deiliverymanListDiv">
	<form action="<%=request.getContextPath()%>/penalizeOutBill/addpenalizeOutBillList" method="post" id="penalizeInsideListForm">
		<input type="hidden" name="id" value="">
		<input type="hidden" name="orderids" value=""/>
	</form>
</div>
</body>
</html>


