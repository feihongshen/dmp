/**************************初始化默认值***************************/
$("#customers").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择客户' });
//$(".multiSelect .multiSelect_txt").removeAttr("readonly");

 
/**************************查询区域按钮***************************/
var _pageSize=10;


function getMutiSelectedArray(){
	var customers = new Array();
	$("#signFee_toolbar input[name='customers']:checked").each(function(){
		customers.push($(this).val());
	})
	return customers;
}

function getQueryDate(){
	var querydate = $("#querydate").val();
	return querydate;
}

function selectQuery(){
	var customers = getMutiSelectedArray();
	
	var queryDate = getQueryDate();


	if(customers.length==0){
		EapTip.alertError("请选择客户");
		return false;
	}
	
	query(1,_pageSize, queryDate);
}

function query(page, number,queryDate){
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
//	loading11(true);
//	tt_loading11(true);
	var customers = getMutiSelectedArray()
/*	var param = {
		customers: customers.join(","),
		currentPage : page,
		pageSize : number
	}*/

	var param = {
		customers: customers.join(","),
		queryDate, queryDate,
		page : page,
		pageSize : number
	}
	
	$.ajax({
	    type: "post",
	    async: false, //设为false就是同步请求
	    url: _ctx + "/orderlifecycle/list?r="+Math.random(),
	    data:param,
        datatype: "json",
	    success: function (result) {
	    	layer.close(layEle);
	    	var data = JSON.parse(result);
	    	for (var i = 0; i < data.rows.length; i++) {
	    		var thisObj = data.rows[i];
	    		for (var prop in thisObj) {
	    			if ( prop.startsWith("amount") || prop.startsWith("count")){

	    				var typeid = prop.replace(/[a-z]*/gi,"")
	    				var val = thisObj[prop] 

	    				if(val != "0" ){
	    					thisObj[prop] = "<a href='javascript:void(0)' onclick='cwbDetailInfo(1," + thisObj['customerid'] +","+ typeid +","+ thisObj['reportdate'] + ");'>" + val + "</a>";	
	    				}
	    				
	    			}
	    		}
	    	}
	    	$("#dg").datagrid('loadData', data);
	    	// var pg1 = $("#dg").datagrid("getPager");
	    	// $(pg1).pagination({
	    	// 	onSelectPage : function(pageNumber, pageSize) {
	    	// 		_pageSize=pageSize;
	    	// 		query(pageNumber, pageSize);
	    	// 	}
	    	// });
	    }
	});
	
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
		
		var customerids = getMutiSelectedArray();
		var queryDate = getQueryDate();

		var form = $("<form>");   //定义一个form表单
		form.attr('style', 'display:none');   //在form表单中添加查询参数
		form.attr('target', 'exportFrame');
		form.attr('method', 'post');
		form.attr('action', _ctx + "/orderlifecycle/export2Excel");

		var input1 = $('<input>');
		input1.attr('type', 'hidden');
		input1.attr('name', 'queryDate');
		input1.attr('value', queryDate);
		form.append(input1);	
		var input3 = $('<input>');
		input3.attr('type', 'hidden');
		input3.attr('name', 'customerids');
		input3.attr('value', customerids.join(","));
		form.append(input3);
		var input4 = $('<input>');
		input4.attr('type', 'hidden');
		input4.attr('name', 'currentPage');
		input4.attr('value', "1");
		form.append(input4);
		var input5 = $('<input>');
		input5.attr('type', 'hidden');
		input5.attr('name', 'pageSize');
		input5.attr('value', customerids.length);
		form.append(input5);
		var input9 = $('<input>');
		input9.attr('type', 'hidden');
		input9.attr('name', 'columnDefs');
		input9.attr('value', JSON.stringify(fieldDef));
		form.append(input9);
		$('body').append(form); 
		form.submit();
}

function cwbDetailInfo(page, customerid, typeid,reportdate) {
	
	initDategridAndPaginationStatus("ord_dg");

	queryOrderDetail(customerid, typeid, reportdate, page, _pageSize)
	
	$('#dlg_Common').dialog('open').dialog('setTitle', '订单详情');
}

function initDategridAndPaginationStatus(dgId){
	$("#" + dgId).datagrid("loadData",[])
	var pg1 = $("#" + dgId).datagrid("getPager");
	$(pg1).pagination('select',1);
}

function queryOrderDetail(customerid, typeid, reportdate, page, number){
	//数据加载动画
	var layEle = layer.load({
		type:3
	});

	
	var param = {
		customerid:  customerid,
		typeid : typeid,
		reportdate, reportdate,
		page : page,
		pageSize : number
	}
	
	$.ajax({
	    type: "post",
	    async: false, //设为false就是同步请求
	    url: _ctx + "/orderlifecycle/getCwbOrderDetailList",
	    data:param,
        datatype: "json",
	    success: function (result) {
	    	layer.close(layEle);
	    	var data = JSON.parse(result);
	    	
	    	$("#ord_dg").datagrid('loadData', data);
	    	var pg1 = $("#ord_dg").datagrid("getPager");
	    	$(pg1).pagination({
	    		onSelectPage : function(pageNumber, pageSize) {
	    			_pageSize=pageSize;
	    			queryOrderDetail(customerid, typeid, reportdate, pageNumber, pageSize);
	    		}
	    	});
	    }
	});
	
}


function closeWindow() {
	$('#html_Content1').html('');
	$('#dlg_Common').dialog('close');
}


