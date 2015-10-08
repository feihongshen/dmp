/**************************初始化默认值***************************/
$("#customers").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择客户' });
$(".multiSelect .multiSelect_txt").removeAttr("readonly");


/**************************初始化默认值***************************/

Array.prototype.contains = function(item){
	return RegExp("\\b"+item+"\\b").test(this);
};

/**************************查询区域按钮***************************/
var _pageSize=10;
$("#selectBtn").click(function(){
	var branchids = new Array();
	$("#signFee_toolbar input[name='branchids']:checked").each(function(){
		branchids.push($(this).val());
	})
	if(branchids.length==0){
		EapTip.alertError("请选择站点");
		return false;
	}
	query(1,_pageSize);
});

function selectQuery(){
	var branchids = new Array();
	$("#signFee_toolbar input[name='branchids']:checked").each(function(){
		branchids.push($(this).val());
	})
	if(branchids.length==0){
		EapTip.alertError("请选择站点");
		return false;
	}
	if($("#beginday").val()==""){
		EapTip.alertError("请选择开始日期");
		return false;
	}
	if($("#begindate").val()==""){
		EapTip.alertError("请选择时间");
		return false;
	}
	
	query(1,_pageSize);
//	layer.closeAll();
	
}

function query(page, number){
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
//	loading11(true);
//	tt_loading11(true);
	var branchids = new Array();
	$("#signFee_toolbar input[name='branchids']:checked").each(function(){
		branchids.push($(this).val());
	})
	var param = {
		beginday:$("#beginday").val(),
		endday:$("#endday").val(),
		begindate:$("#begindate").val(),
		branchids: branchids.join(","),
		currentPage : page,
		pageSize : number
	}
	
	$.ajax({
	    type: "post",
	    async: false, //设为false就是同步请求
	    url: "reportForm.do?stationsSignFeeFormList&"+Math.random(),
	    data:param,
        datatype: "json",
	    success: function (result) {
	    	layer.close(layEle);
	    	var data = JSON.parse(result);
	    	for (var i = 0; i < data.rows.length; i++) {
	    		var thisObj = data.rows[i];
	    		for (var j in thisObj) {
	    			var detailField = ["initialBalance","cash","pos","scan","other","smtFreightCash"];
	    			if ( detailField.contains(j))
	    				if(getDmpid().length>0 && thisObj[j] != "0"){//onclick='http:localhost:8080/dmp/eapCwbDetail.do?index&path="+path+"'
	    					thisObj[j] = "<a href='javascript:void(0)' onclick='cwbDetailInfo(1," + getRowType(j) + ", "+ thisObj['branchid'] + ");'>" + thisObj[j] + "</a>";
	    				}else{
	    					thisObj[j] = "<a href='javascript:void(0)' onclick='showDataList(" + getRowType(j) + ", "+ thisObj['branchid'] + ");'>" + thisObj[j] + "</a>";
	    				}
	    				
	    		}
	    	}
	    	$("#dg").datagrid('loadData', data);
	    	var pg1 = $("#dg").datagrid("getPager");
	    	$(pg1).pagination({
	    		onSelectPage : function(pageNumber, pageSize) {
	    			_pageSize=pageSize;
	    			query(pageNumber, pageSize);
	    		}
	    	});
	    }
	});
	
}

function showDataList(rowType,branchid){
	var beginday=$("#beginday").val(),begindate=$("#begindate").val();
//	if(getDmpid().length>0){
//		var dmpid = getDmpid().replace(/(^\s*)|(\s*$)/g, "");
//		var beginday=$("#beginday").val(),begindate=$("#begindate").val();
//		var path = "/CwbOrderDetailListController.do?isIframe&index&dmpid="+dmpid+"&branchid="+branchid+"&beginday="+beginday+"&begindate="+begindate+"&rowtype="+rowType+"&searchtype=1";
//		window.location.href = "http:127.0.0.1:8080/dmp/eapCwbDetail.do?index&path='"+path+"'";
////		window.parent.addTab("订单明细",  "https://127.0.0.1:8080/dmp/CwbOrderDetailListController.do?isIframe&index&dmpid="+dmpid+"&branchid="+branchid+"&beginday="+beginday+"&begindate="+begindate+"&rowtype="+rowType+"&searchtype=1","pictures");
//	}else{
//		parent.closetab("订单明细");
//		parent.addTab("订单明细", _ctx + "/CwbOrderDetailListController.do?isIframe&index&branchid="+branchid+"&beginday="+beginday+"&begindate="+begindate+"&rowtype="+rowType+"&searchtype=1","pictures");
//	}
	parent.closetab("订单明细");
	parent.addTab("订单明细", _ctx + "/CwbOrderDetailListController.do?isIframe&index&branchid="+branchid+"&beginday="+beginday+"&begindate="+begindate+"&rowtype="+rowType+"&searchtype=1","pictures");
}

function link2dmp2(rowType,branchid){
	
	var beginday=$("#beginday").val(),begindate=$("#begindate").val();
	if(temp_path){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url : "reportForm.do?link2dmp&" + Math.random(),
			data : {
				path_temp:temp_path
			},
			datatype : "json",
			success: function(result){
				
			}
		});
	}
}

//获取订单详情
function getCwbDetail(rowType,branchid){
	var beginday=$("#beginday").val();
	var endday = $("#endday").val();
	var begindate=$("#begindate").val();
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : "reportForm.do?link2dmp&" + Math.random(),
		data : {
			rowType : rowType,
			branchid : branchid,
			beginday : beginday,
			endday:endday,
			begindate : begindate
			
		},
		datatype : "json",
		success: function(result){
			
		}
	});
}


function getRowType(type){
	var val=0;
	if(type=="initialBalance"){
		val=1;
	}else if(type=="cash"){
		val=2;
	}else if(type=="pos"){
		val=3;
	}else if(type=="scan"){
		val=4;
	}else if(type=="other"){
		val=5;
	}else if(type=="differenceAmount"){
		val=6;
	}else if(type=="remittance"){
		val=7;
	}else if(type=="smtFreightCash"){//订单详情-上门退运费现金
		val=21;
	}
	return val;
}

function billExportExecute(){
		var fieldDef = new Array();
		var fields = $('#dg').datagrid('getColumnFields');
		for (var i = 0; i < fields.length; i++) {
			var col = $('#dg').datagrid('getColumnOption', fields[i]);
			fieldDef.push({
				field:col.field,
				width:col.width,
				title:col.title,
				rowspan:col.rowspan,
				colspan:col.colspan
			})
		}
		
		var branchids = new Array();
		$("#signFee_toolbar input[name='branchids']:checked").each(function(){
			branchids.push($(this).val());
		})
		var form = $("<form>");   //定义一个form表单
		form.attr('style', 'display:none');   //在form表单中添加查询参数
		form.attr('target', 'exportFrame');
		form.attr('method', 'post');
		form.attr('action', "reportForm.action?exportExcel");

		var input1 = $('<input>');
		input1.attr('type', 'hidden');
		input1.attr('name', 'beginday');
		input1.attr('value', $("#beginday").val());
		form.append(input1);
		var input2 = $('<input>');
		input2.attr('type', 'hidden');
		input2.attr('name', 'begindate');
		input2.attr('value', $("#begindate").val());
		form.append(input2);
		var input22 = $('<input>');
		input22.attr('type', 'hidden');
		input22.attr('name', 'endday');
		input22.attr('value', $("#endday").val());
		form.append(input22);
		var input3 = $('<input>');
		input3.attr('type', 'hidden');
		input3.attr('name', 'branchids');
		input3.attr('value', branchids.join(","));
		form.append(input3);
		var input4 = $('<input>');
		input4.attr('type', 'hidden');
		input4.attr('name', 'currentPage');
		input4.attr('value', "1");
		form.append(input4);
		var input5 = $('<input>');
		input5.attr('type', 'hidden');
		input5.attr('name', 'pageSize');
		input5.attr('value', branchids.length);
		form.append(input5);
		var input9 = $('<input>');
		input9.attr('type', 'hidden');
		input9.attr('name', 'columnDefs');
		input9.attr('value', JSON.stringify(fieldDef));
		form.append(input9);
		$('body').append(form); 
		form.submit();
}


$("#billExportBtn").click(function exportExcel(){
	var fieldDef = new Array();
	var fields = $('#dg').datagrid('getColumnFields');
	for (var i = 0; i < fields.length; i++) {
		var col = $('#dg').datagrid('getColumnOption', fields[i]);
		fieldDef.push({
			field:col.field,
			width:col.width,
			title:col.title,
			rowspan:col.rowspan,
			colspan:col.colspan
		})
	}
	
	var branchids = new Array();
	$("#signFee_toolbar input[name='branchids']:checked").each(function(){
		branchids.push($(this).val());
	})
	var form = $("<form>");   //定义一个form表单
	form.attr('style', 'display:none');   //在form表单中添加查询参数
	form.attr('target', 'exportFrame');
	form.attr('method', 'post');
	form.attr('action', "reportForm.action?exportExcel");

	var input1 = $('<input>');
	input1.attr('type', 'hidden');
	input1.attr('name', 'beginday');
	input1.attr('value', $("#beginday").val());
	form.append(input1);
	var input2 = $('<input>');
	input2.attr('type', 'hidden');
	input2.attr('name', 'begindate');
	input2.attr('value', $("#begindate").val());
	form.append(input2);
	var input3 = $('<input>');
	input3.attr('type', 'hidden');
	input3.attr('name', 'branchids');
	input3.attr('value', branchids.join(","));
	form.append(input3);
	var input4 = $('<input>');
	input4.attr('type', 'hidden');
	input4.attr('name', 'currentPage');
	input4.attr('value', "1");
	form.append(input4);
	var input5 = $('<input>');
	input5.attr('type', 'hidden');
	input5.attr('name', 'pageSize');
	input5.attr('value', branchids.length);
	form.append(input5);
	var input9 = $('<input>');
	input9.attr('type', 'hidden');
	input9.attr('name', 'columnDefs');
	input9.attr('value', JSON.stringify(fieldDef));
	form.append(input9);
	$('body').append(form); 
	form.submit();
});

//获取dmpid
function getDmpid(){
	var dmpid = $("#dmpValue").val().replace(/(^\s*)|(\s*$)/g, "");
	if(dmpid){
		return dmpid;
	}
	return "";
}

function cwbDetailInfo(page,rowType,branchid) {
	
	var beginday = $("#beginday").val();
	var endday = $("#endday").val();
	var begindate = $("#begindate").val();
	var param = {};
	if(rowType==undefined || branchid ==undefined){
		$.extend(param,tempCwbSearchData);
		param.page = page;
	}else{
		param = {
				branchid : branchid,
				beginday : beginday,
				endday : endday,
				begindate : begindate,
				rowtype : rowType,
				searchtype : 1,
				page:1,
				currentPage : page,
				pageSize : 10
		};
	}
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  "CwbOrderDetailListController.do?showCwbOrderDetail1",
		data : param,
		datatype : "json",
		success : function(result) {
			$('#html_Content1').html(result);
		}
	});
	
	$('#dlg_Common').dialog('open').dialog('setTitle', '订单详情');
}

function closeWindow() {
	$('#html_Content1').html('');
	$('#dlg_Common').dialog('close');
}

/**
 * 初始化结束时间等同开始时间（需求变更）
 */
function initEndTime(){
	$('#endday').val($('#beginday').val());
}

