<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>quartz定时任务管理</title>

<!-- 想使用相对路径引入,但是框架中对js和css等静态文件进行了控制,放在了专门存放静态文件的地方。 -->
<!-- 所以想把这个jsp文件和相关的js和css文件用相对路径关联的想法失败。因为在当前的框架中,对静态资源文件都是专门进行
url控制的。不太可能单独这个jsp页面的js和css文件单独进行配置,这样也就失去了我想对这个部分进行模块化的意义。
也就是说,还是需要人来手工调整加载js和 css文件                 warehousegroupdetail  -->
<!-- <link rel="stylesheet" type="text/css"	href="icon.css"> -->
<!-- <link rel="stylesheet" type="text/css"	href="easyui.css"> -->
<!-- <script type="text/javascript"	src="jquery.min.js"></script> -->
<!-- <script type="text/javascript"	src="jquery.easyui.min.js"></script> -->

<!-- 这里href相对的是/,webapp的根目录。 而不是相对于当前jsp文件。-->
<link rel="stylesheet" type="text/css"	href="timetask/icon.css"></link>
<link rel="stylesheet" type="text/css"	href="timetask/easyui.css"></link>
<script type="text/javascript"	src="timetask/jquery.min.js"></script>
<script type="text/javascript"	src="timetask/jquery.easyui.min.js"></script>

<%-- <link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/WEB-INF/jsp/timetask/icon.css"> --%>
<!-- <link rel="stylesheet" type="text/css"	href="WEB-INF/jsp/timetask/easyui.css"> -->
<!-- <script type="text/javascript"	src="WEB-INF/jsp/timetask/jquery.min.js"></script> -->
<!-- <script type="text/javascript"	src="WEB-INF/jsp/timetask/jquery.easyui.min.js"></script> -->

<%-- <link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/webapp/WEB-INF/jsp/timetask/icon.css"> --%>
<%-- <link rel="stylesheet" type="text/css"	href="<%=request.getContextPath()%>/webapp/WEB-INF/jsp/timetask/easyui.css"> --%>
<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/webapp/WEB-INF/jsp/timetask/jquery.min.js"></script> --%>
<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/webapp/WEB-INF/jsp/timetask/jquery.easyui.min.js"></script> --%>

<script type="text/javascript">
function closeWindow() {
	document.forms['triggerCronForm'].reset();
	$('#dlgCommon').dialog('close');
}

function showMsg(message) {
	$.messager.show({
		title : '提示',
		msg : message,
		timeout : 1000,
		style : {
			right : '',
			top : 200 + document.body.scrollTop
					+ document.documentElement.scrollTop,
			bottom : ''
		}
	});
}


function dateFormatter(value){
	 var unixTimestamp = new Date(value);  
     return unixTimestamp.toLocaleString();  
}

function refreshGridData(){
	$('#scheduler_dg').datagrid('reload');
}

function checkAndDealWithResult(result){
	var rs;
	if(typeof(result)=='string'){
			rs = JSON.parse(result);
	}
	else if(typeof(result)=='object'){
			rs=result;
	}

	return rs;
}

function getTriggerNames(){
	var rows = $('#scheduler_dg').datagrid('getChecked');
	if(rows.length <= 0){
		$.messager.alert("提示", "请选中记录！", "warning");
		return null;
	}
	var triggerNames = new Array();
	for (var i=0;i<rows.length;i++)
	{
		triggerNames.push(rows[i].triggerName);
	}
	return triggerNames;
}

function doStart(){
	var triggerNames = getTriggerNames();
	if(triggerNames){
		$.messager.confirm("操作提醒", "您确定要启动选中的定时任务吗？", function (data) {
	        if (data) {
	        	var param = {
	        			triggerNames:triggerNames
	        	}
	        	$.ajax({
	        	    type: "post",
	        	    async: false, //设为false就是同步请求
// 	        	    /quartz/startTrigger.action?这个表示绝对路径了
// 					quartz/startTrigger.action? 这个是相对于这个app的路径
	        	    url:  "quartz/startTrigger.do?"+Math.random(),
	        	    data:param,
	                datatype: "json",
	        	    success: function (result) {
	        	    	var rs=checkAndDealWithResult(result);
// 	        	    	var rs = JSON.parse(result);
	        	    	if (!rs.status) {
	        	    		$.messager.alert("提示", rs.msg, "warning");
	        	    	} else {
	        	    		refreshGridData();
	        	    		showMsg("操作成功！");
	        	    	}
	        	    }
	        	});
	        } else {
	            return;
	        }
	    });
	}
}

function doPause(){
	var triggerNames = getTriggerNames();
	if(triggerNames){
		$.messager.confirm("操作提醒", "您确定要暂停选中的定时任务吗？", function (data) {
	        if (data) {
	        	var param = {
	        			triggerNames:triggerNames
	        	}
	        	$.ajax({
	        	    type: "post",
	        	    async: false, //设为false就是同步请求
	        	    url:  "quartz/pauseTrigger.do?"+Math.random(),
	        	    data:param,
// 	                datatype: "json",
	                datatype: "text",
	        	    success: function (result) {
// 	        	    	mappingJacksonHttpMessageConverter
// 		        	    alert(typeof(result));
// 		        	    alert("result:"+result);
// 		        	    alert("result instanceof String:"+(result instanceof String));
		        	    var rs=checkAndDealWithResult(result);
// 	        	    	var rs = JSON.parse(result);
	        	    	if (!rs.status) {
	        	    		$.messager.alert("提示", rs.msg, "warning");
	        	    	} else {
	        	    		refreshGridData();
	        	    		showMsg("操作成功！");
	        	    	}
	        	    }
	        	});
	        } else {
	            return;
	        }
	    });
	}
}

function openModifyJobPlanDialog(){
	var rows = $('#scheduler_dg').datagrid('getChecked');
	if(rows.length != 1){
		$.messager.alert("提示", "请选中一条记录！", "warning");
		return null;
	}
	if(rows[0].triggerType=='SIMPLE'){
		$.messager.alert("提示", "SIMPLETrigger不支持CRON表达式！", "warning");
		return null;
	}
	$("#triggerName").val(rows[0].triggerName);
	$("#triggerPlan").val(rows[0].triggerPlan);
	$('#dlgCommon').dialog('open').dialog('setTitle', '修改执行计划');
}

function doModifyJobPlan(){
	var triggerName = $("#triggerName").val();
	var triggerPlan = $("#triggerPlan").val();
	if(!triggerName || triggerName == ""){
		$.messager.alert("提示", "任务名称不能为空！", "warning");
		return;
	}
	if(!triggerPlan || triggerPlan == ""){
		$.messager.alert("提示", "任何计划不能为空！", "warning");
		return;
	}
	
	var param = {
			triggerName:triggerName,
			cronExpression:triggerPlan
	}
	$.ajax({
	    type: "post",
	    async: false, //设为false就是同步请求
	    url:  "quartz/modifyCronExpression.do?"+Math.random(),
	    data:param,
        datatype: "json",
	    success: function (result) {
// 	    	var rs = JSON.parse(result);
	    	var rs=checkAndDealWithResult(result);
	    	if (!rs.status) {
	    		$.messager.alert("提示", rs.msg, "warning");
	    	} else {
	    		closeWindow();
	    		refreshGridData();
	    		showMsg("操作成功！");
	    	}
	    }
	});
}

function doExcuteNow(){
	var rows = $('#scheduler_dg').datagrid('getChecked');
	if(rows.length != 1){
		$.messager.alert("提示", "请选中一条记录！", "warning");
		return null;
	}
	
	$.messager.confirm("操作提醒", "您确定要立即执行选中的定时任务吗？", function (data) {
		if(data){
			var param = {
					triggerName:rows[0].triggerName,
			}
			$.ajax({
			    type: "post",
			    async: false, //设为false就是同步请求
			    url:  "quartz/executeTriggerNow.do?"+Math.random(),
			    data:param,
		        datatype: "json",
			    success: function (result) {
// 			    	var rs = JSON.parse(result);
			    	var rs=checkAndDealWithResult(result);
			    	if (!rs.status) {
			    		$.messager.alert("提示", rs.msg, "warning");
			    	} else {
			    		showMsg("操作成功！");
			    	}
			    }
			});
		}else{
			return;
		}
	
	});
}

</script>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0">
<!-- 	<div id="alertMessage"></div> -->
	<div data-options="region:'center'" style="overflow-x: hidden; overflow-y: auto;">
		<table id="scheduler_dg" class="easyui-datagrid" style="height:480px"
			url="quartz/findTriggerList.do" pagination="false" toolbar="#scheduler_toolbar"
			title="quartz定时任务触发器(Trigger)控制台">
			<thead>
				<tr>
						<th field="cb" checkbox="true"></th>
						<th field="triggerId" width="50">ID</th>
						<th field="triggerName" width="200">Trigger名称</th>
						<th field="triggerType" width="75">Trigger类型</th>
						<th field="triggerDesc" width="200">Trigger描述</th>
						<th field="statusName" width="75">Trigger状态</th>
						<th field="triggerPlan" width="200">执行计划</th>
						<th field="triggerGroup" width="75">Trigger组别</th>
						<th field="previousFireTime" formatter="dateFormatter" width="150">上次执行时间</th>
						<th field="nextFireTime" formatter="dateFormatter" width="150">下次时间</th>
				</tr>
			</thead>
		</table>
	
		<div id="scheduler_toolbar">
<!-- 				<div style="border: 1px;border-bottom-color: grey;margin-top: 4px;margin-left: 5px;" > -->
				<div style="padding:5px;background:#fafafa;width:500px;border:1px solid #ccc">
							<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:refreshGridData();">刷新</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="javascript:doStart();">启动</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-filter" plain="true" onclick="javascript:doPause()">暂停</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-back" plain="true" onclick="javascript:doExcuteNow()">执行</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="javascript:openModifyJobPlanDialog()">修改Cron表达式</a>
				</div>
		</div>
	</div>

	<div id="dlgCommon" class="easyui-dialog" style="width: 500px; height: 268px; padding: 10px 10px"
		closed="true" buttons="#dlg-buttons">
		<div style="margin-left: 15px;margin-top:10px" id="htmlContent">
		  	<form id="triggerCronForm" action="">
				<table>
					<tr>
						<td>触发器名称：</td>					
						<td><input id="triggerName" name="triggerName" readonly="readonly" style="width:350px;font-size: large;font-weight: bold;"/></td>
					</tr>
					<tr>
						<td style="">执行计划：<font color="red">*</font></td>					
						<td><input id="triggerPlan" name="triggerPlan" style="width:350px;font-size: large;font-weight: bold;" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<div id="dlg-buttons">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="text-align:right">
					<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:doModifyJobPlan()">保存</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:closeWindow()">取消</a>
				</td>
			</tr>
		</table>
	</div>

</body>
</html>
