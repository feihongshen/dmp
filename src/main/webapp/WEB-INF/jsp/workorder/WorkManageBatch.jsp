<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>工单批量导入/处理</title>
	
	<script src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js" type="text/javascript"></script>
	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" type="text/css" media="all" />
	<link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
	
	<style type="text/css">
	.multiSelect{
		display: inline-block;
	}
	.multiSelect input{
	 	margin: 0px;
	  	padding: 0px;
	  	line-height: 15px;
	  	border: 0px;
		height:25px;
	}
</style>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
	<div data-options="region:'center'" style="height:100%;overflow-x: auto; overflow-y: auto;">
		<table id="importList" width="100%" class="easyui-datagrid" toolbar="#workorder_toolbar" rownumbers="true" pagination="true" fit="true"
		  		url="" pageSize="10" pageList="[10,20,50,100]" showFooter="true" fitColumns="false" singleSelect="false" 
		  		data-options="
		  		rowStyler: function(index,row){
					if (row.errorMsg){
						return 'background-color:#ffff00;font-weight:bold;';
					}
				}"
			>
			<thead width="100%">  
	           <tr>  
	           </tr>  
	       </thead>  
		</table>
			<div id="workorder_toolbar">
				<div class="form-inline" style="padding:10px">
				 <form name="uploadForm" id="uploadForm" method="POST" action="<%=request.getContextPath()%>/workorder/WorkManageBatchUploadFile"  enctype="multipart/form-data" >
				   <table width="70%";>
				      <tr>
				          <td width="30%">
				          <span style="color:red">*</span><label for="type">导入处理方式：</label>
				          <select name="type" id="type" style="width: 100px;" onchange="initDataGrid()">
						      <option value="0" >请选择</option>
						      <option value="1">新建</option>
						      <option value="2">处理</option>
					      </select>
				          </td>
				          <td width="50%">
				          <span style="color:red">*</span>
				          <label for="excel">上传文件：</label>
                          <span id="swfupload-control" style="vertical-align: middle;height: 38px;">
							<input type="text" id="txtFileName" disabled="true" style="border: solid 1px;background-color: #FFFFFF;width:140px;" />
							<div class="btn btn-default" id="button"></div>
						  </span>
						  <input name="button35" type="button" id="importButton" value="开始导入" class="input_button2" style="vertical-align: top; margin-top:2px;width:75px;width:70px;"/>				      
				          </td>
				          <td width="10%">
				          <a href="javascript:downloadTemplateNew();">
					      <span>下载新增模板</span>
					      </td>
				          <td width="10%">
				          <a href="javascript:downloadTemplateHandle();">
					      <span>下载处理模板</span>
				          </td>
				      </tr>
				   </table>
				   </form>
				</div>
		</div>
</div>
<script type="text/javascript" > 
var _ctx = "<%=request.getContextPath()%>";
var _sessionId = "<%=request.getSession().getId()%>";
$(function() {
	//上传数据
	var url = _ctx +"/workorder/WorkManageBatchUploadFile;jsessionid="+ _sessionId;
	$('#swfupload-control').swfupload({
		upload_url : url,
		file_size_limit : "10240",
		file_types : "*.xls;*.xlsx",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : _ctx+"/js/swfupload/swfupload.swf",
		button_image_url :_ctx+"/js/swfupload/XPButtonNoText_160x22.png",
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
		displayRowData(serverData);
		alert(file.name+"上传成功！");
	}).bind('uploadComplete', function(event, file) {
		endImport();
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
	
	//点击导入按钮
	$("#importButton").click(function(){
		if($("#txtFileName").val() == ''){
			alert("请选择上传文件！");
			return;
		}
		if($('#type').val() == '0') {
			alert("请选择导入处理方式！");
			return;
		}
		importExcel();
	});
	//初始化表格
	initDataGrid();
}); 
function initImport(){
	$("#importButton").removeAttr("disabled"); 
	$("#importButton").val("开始导入");
	$("#txtFileName").val("");
}
function endImport() {
	$("#importButton").removeAttr("disabled"); 
	$("#importButton").val("开始导入");
	$("#txtFileName").val("");
}
function importExcel(){
	$("#importButton").attr("disabled","disabled");
	$("#importButton").val("正在导入");
	$('#swfupload-control').swfupload('addPostParam', 'type', $('#type').val());
	$('#swfupload-control').swfupload('startUpload');
	$("#importList").datagrid('loadData',[]); // 清空数据
}

function initDataGrid() {
	var type = $("#type").val();
	var dg = $("#importList");
	if (type == 1 || type == 0) {
		dg.datagrid({columns: [[
		                          { field: 'acceptNo', title: '工单号', width: 120, align: 'left' },
		                          { field: 'contact', title: '联系人', width: 50, align: 'left' },
		                          { field: 'phoneOne', title: '联系电话', width: 80, align: 'left' },
		                          { field: 'email', title: '邮箱', width: 120, align: 'left' },
		                          { field: 'customerName', title: '客户', width: 120, align: 'left' },
		                          { field: 'orderNo', title: '订单号', width: 120, align: 'left' },
		                          { field: 'complaintOneLevelName', title: '一级分类', width: 100, align: 'left' },
		                          { field: 'complaintTwoLevelName', title: '二级分类', width: 100, align: 'left' },
		                          { field: 'content', title: '工单内容', width: 120, align: 'left' },
		                          { field: 'codOrgIdName', title: '责任机构', width: 80, align: 'left' },
		                          { field: 'complaintUser', title: '责任人', width: 80, align: 'left' },
		                          { field: 'errorMsg', title: '失败原因', width: 200, align: 'left' }
		                      ]]});
	} else if (type == 2) {
		dg.datagrid( {columns: [[
		                          { field: 'acceptNo', title: '工单号', width: 120, align: 'left' },
		                          { field: 'contact', title: '联系人', width: 50, align: 'left' },
		                          { field: 'phoneOne', title: '联系电话', width: 80, align: 'left' },
		                          { field: 'email', title: '邮箱', width: 120, align: 'left' },
		                          { field: 'customerName', title: '客户', width: 120, align: 'left' },
		                          { field: 'orderNo', title: '订单号', width: 120, align: 'left' },
		                          { field: 'complaintOneLevelName', title: '一级分类', width: 100, align: 'left' },
		                          { field: 'complaintTwoLevelName', title: '二级分类', width: 100, align: 'left' },
		                          { field: 'content', title: '工单内容', width: 120, align: 'left' },
		                          { field: 'codOrgIdName', title: '责任机构', width: 80, align: 'left' },
		                          { field: 'complaintUser', title: '责任人', width: 80, align: 'left' },
		                          { field: 'remark', title: '处理结果', width: 120, align: 'left' },
		                          { field: 'errorMsg', title: '失败原因', width: 200, align: 'left' }
		                      ]]});
	}
	$("#importList").datagrid('loadData',[]); // 清空数据
}

function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){ // 判断数据是否是数组
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $("#importList");
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

function displayRowData(result) {
	//$("#importList").datagrid('loadData',[]); // 清空数据
	var data = JSON.parse(result);
	var gridData = data.map.result;
    //加载数据
    $('#importList').datagrid({loadFilter:pagerFilter}).datagrid('loadData', gridData);
}

//模板下载
function downloadTemplateNew() {
	var form = $("<form>");   //定义一个form表单
	form.attr('style', 'display:none');   //在form表单中添加查询参数
	form.attr('target', 'exportFrame');
	form.attr('method', 'post');
	form.attr('action', _ctx + "/workorder/DownloadTemplateNew;jsessionid="+ _sessionId);
	$('body').append(form); 
	form.submit();
};

//模板下载
function downloadTemplateHandle() {
	var form = $("<form>");   //定义一个form表单
	form.attr('style', 'display:none');   //在form表单中添加查询参数
	form.attr('target', 'exportFrame');
	form.attr('method', 'post');
	form.attr('action', _ctx + "/workorder/DownloadTemplateHandle;jsessionid="+ _sessionId);
	$('body').append(form); 
	form.submit();
};
</script>
</body>
</html>