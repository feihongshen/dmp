<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/billOpePage.css" type="text/css"  />
</head>
<script type="text/javascript">var _sessionId = "<%=request.getSession().getId()%>";</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<body>
<div style="margin-left: 30px;">
	<table>
		<tr>
			<td valign="top"><input type="button" class="input_button1" id="backBtn" value="返回" onclick="closeEditBox_common();" /></td>
			<td valign="top"><input type="button" class="input_button1" id="createBtn" value="保存" onclick="saveEditInfoEdit();" /></td>
			<td valign="top">
				<form method="post" name="uploadForm" id="uploadForm"  action="<%=request.getContextPath()%>/acrossPayFreightCheck/importReceOrders;jsessionid=<%=session.getId() %>" enctype="multipart/form-data">
					<div id="swfupload-control">
						<div style="float: left; overflow: auto; border: 1px solid #c0c0c0;">
							<input type="button" id="fileButton" />
						</div>
					</div>
				</form>
			</td>
			<td valign="top"><input type="button" class="input_button1" id="verifyOrderBtn" value="自动核对数据" onclick="automicVerifyData();" /></td>
		</tr>
	</table>
</div>


<div>
	<input type="hidden" id="eidtBillId" value="${expressBill.id }" />
	<input type="hidden" id="eidtBillNo" value="${expressBill.billNo }" />
	<input type="hidden" id="billState_page" value="${expressBill.billState }" />
	<table>
		<tr>
			<td class="maxtd" align="right">账单编号</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.billNo }" /></td>
			<td class="maxtd" align="right">账单状态</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.billStateStr }" /></td>
			<td class="maxtd" align="right">代收货款合计</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cod }" /></td>
		</tr>
		<tr>
			<td class="maxtd" align="right">应收省份</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.receProvinceName }" /></td>
			<td class="maxtd" align="right">应付省份</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.payProvinceName }" /></td>
			<td class="maxtd" align="right">运费金额合计</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.freight }" /></td>
		</tr>
		<tr>
			<td class="maxtd" align="right">创建日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.createTime }" /></td>
			<td class="maxtd" align="right">审核日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.auditTime }" /></td>
			<td class="maxtd" align="right">核销日期</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cavTime }" /></td>
		</tr>
		<tr>
			<td class="maxtd" align="right">创建人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.creatorName }" /></td>
			<td class="maxtd" align="right">审核人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.auditorName }" /></td>
			<td class="maxtd" align="right">核销人</td>
			<td><input disabled="disabled" id="" name="" value="${expressBill.cavName }" /></td>
		</tr>
		<tr>
			<tr>
			<td class="maxtd" align="right" valign="top">备注</td>
			<td colspan="8">
				<textarea rows="3" cols="70" id="editrRemark" name="editrRemark" value="">${expressBill.remark }</textarea>
			</td>
		</tr>
		</tr>
	</table>
</div>

<div id="table_div">
	<!-- 自动核查，若出现核对不上的运费，明细列表中的运单整行标红 -->
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
		<tr align="center">
			<th height="30px" width="120px;" align="center" valign="middle" style="font-weight: bold;" >运单号</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">件数</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">揽件员</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">派件员</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">付款方式</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">运费合计</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">运费</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">包装费用</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">保价费用</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">代收货款</th>
			<th width="60px;" align="center" valign="middle" style="font-weight: bold;">配送站点</th>
		</tr>
			<c:forEach items="${cwbListItem }" var="listItem">
		  <tr align="center">
		    <td height="30px" align="center" valign="middle">${listItem.orderNo }</td>
		    <td align="center" valign="middle">${listItem.orderCount }</td>
		    <td align="center" valign="middle">${listItem.deliveryMan }</td>
		    <td align="center" valign="middle">${listItem.sendMan }</td>
		    <td align="center" valign="middle">${listItem.payMethod }</td>
		    <td align="center" valign="middle">${listItem.transportFeeTotal }</td>
		    <td align="center" valign="middle">${listItem.transportFee }</td>
		    <td align="center" valign="middle">${listItem.packFee }</td>
		    <td align="center" valign="middle">${listItem.saveFee }</td>
		    <td align="center" valign="middle">${listItem.receivablefee }</td>
		    <td align="center" valign="middle">${listItem.branchName }</td>
		  </tr>
	  		</c:forEach>
	</table>
	<table width="100%">
	  <tr>
	    <td align="center">
	    	共${total}条${pageTotal}页　第${pageno}页
	    	<c:if test="${pageno > 1}">　　
	    	<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(1,${expressBill.id });">首页</a>　<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageno-1 },${expressBill.id });">上页</a>
	    	</c:if>
	    	<c:if test="${pageno < pageTotal}">　<!-- cwbDetailInfo4EditPage(${pageno+1 }) -->
	    	<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageno+1 },${expressBill.id });">下页</a>　<a href="javascript:void(0);" onclick="cwbDetailInfo4EditPage(${pageTotal },${expressBill.id });">尾页</a>
	    	</c:if>
	    </td>
	  </tr>
	</table>
</div>

<script type="text/javascript">
$(function(){
	var billState = $("#billState_page").val();
	if(billState>0){
		$("#editrRemark").attr("disabled","disabled");
		$("#inportOrderBtn").attr("disabled","disabled");
		$("#verifyOrderBtn").attr("disabled","disabled");
		$("#createBtn").attr("disabled","disabled");
	}
	//初始化导入的函数
	initImportFun();
});
function saveEditInfoEdit(){
	var bill_temp_id = $("#eidtBillId").val();
	var remark_temp = $("#editrRemark").val();
	if(bill_temp_id){
		if(undefined == remark_temp){
			remark_temp = "";
		}
		saveEditInfo(bill_temp_id,remark_temp);
	}
}
/**
 * 导入的函数
 */
function initImportFun(){
	$('#swfupload-control').swfupload({
		upload_url: $("#uploadForm").attr("action"),
		file_size_limit : "10240",
		file_types : "*.xls;*.xlsx",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url :  App.ctx+"/js/swfupload/swfupload.swf",
		button_text : "<span class='importbutton'>选择文件</span>",
		button_width : 135,
		button_height : 24,
		button_text_left_padding : 0,
		button_image_url:App.ctx+"/js/swfupload/special_btn.png",
		button_text_style:'.importbutton {text-align:center;margin-top:2px;color:#ffff !important;height: 24px;line-height: 24px;min-width:135px;cursor: pointer;display: inline;}',
		button_placeholder : $('#fileButton')[0]
	}).bind('fileQueued', function(event, file) {
		file_id = $('#swfupload-control');
		//文件选择之后就立即上传
		orderNoExcelImport();
	}).bind('fileQueueError', function(event, file, message) {
		
	}).bind('fileQueuedHandler', function(event, file, message) {
		var queue = this.customSettings.queue || new Array();
        while (queue.length > 0) {
            this.cancelUpload(queue.pop(), false);
        }
        
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
		
	}).bind('uploadStart', function(event, file) {
		operateCountFlag = true;//是否是操作了的标识
		
	}).bind('uploadError',function(fileobject, message){
		
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {
		var percentage =  Math.ceil((bytesLoaded / bytesTotal) * 100);
		
	}).bind('uploadSuccess', function(event, file, serverData) {
		var data = $.parseJSON(serverData);
		if(data.status){//成功
			var attribtues = data.attributes;
			var totalCount = attribtues.totalCount;
			debugger;
			//更新表格内容
			refreshTableData();
			//提示信息
			$.messager.alert("提示", "共 "+totalCount+"条，成功导入"+totalCount+"条", "info");
		}else{//失败
			$.messager.alert("提示", data.msg, "warning");
		}
	}).bind('uploadComplete', function(event, file) {
		endImport();
	}).bind('uploadError', function(event, file, message) {
		
	});
}
/**
 * 开始导入
 */
function orderNoExcelImport(){
	$("#fileButton").attr("disabled", true);
	$("#fileButton").text("正在导入");
	//传递账单id和账单号
	$('#swfupload-control').swfupload('addPostParam', 'billId', $("#eidtBillId").val());
	$('#swfupload-control').swfupload('addPostParam', 'billNo', $("#eidtBillNo").val());
	$("#swfupload-control").swfupload('startUpload');
}
/**
 * 导入结束
 */
function endImport() {
	$("#fileButton").attr("disabled", false);
	$("#fileButton").text("选择文件");
}
/**
 * 局部刷新表格中的数据[还没生效]
 */
function refreshTableData(){
	var billId = $("#eidtBillId").val();
	
	if(billId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/acrossPayFreightCheck/switch2EditView4AcrossPayPart",
			data : {
				page:1,
				billId:billId
			},
			datatype : "json",
			success : function(result) {
				if(result){
					$('#table_div').html("");
					$('#table_div').html(result);
				}
			}
		});
	}
}

/**
 * 自动核对数据
 */
function automicVerifyData(){
	var billId = $("#eidtBillId").val();
	if(billId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/acrossPayFreightCheck/verifyImportRecords",
			data : {
				page:1,
				billId:billId
			},
			datatype : "json",
			success : function(result) {
				if(result){
					$('#table_div').html("");
					$('#table_div').html(result);
				}
			}
		});
	}
}


</script> 
</body>
</html>