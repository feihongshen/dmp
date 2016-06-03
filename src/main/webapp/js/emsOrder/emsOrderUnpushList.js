(function($){
	$("#customerIds").multipleSelect({
		placeholder: "请选择客户",
		required:true ,
		multiple:true ,
	    filter: true
	});
	$.fn.serializeJson=function(){
		var serializeObj={};
		$(this.serializeArray()).each(function(){
			serializeObj[this.name]=this.value;
		});
		return serializeObj;
	};
})(jQuery);

function getSelectedCustomerIds(){
	var customerIds = [] ;
	$("input[name='selectItemcustomerIds']:checked").each(function(i, o){
		customerIds.push(o.value);
	})
	return customerIds;
}

function query(pageNumber , pageSize){
	var customerIds = getSelectedCustomerIds().join(",") ;
	var startTime = jQuery("#startTime").val().trim() ;
	var endTime = jQuery("#endTime").val().trim() ;
	var orderType = jQuery("#orderType").val().trim() ;
	if(customerIds.length == 0){
		layer.tips('请选择客户', $("#customerIds").next().find("button") , {guide: 0, time: 3});
		return false ;
	}
	if(startTime != "" && endTime != "" && startTime > endTime){
		layer.tips('结束日期不能小于开始日期', $("#endTime") , {guide: 0, time: 3});
		return false ;
	}
	if(pageNumber == undefined){
		pageNumber = 1 ;
	}
	if(pageSize == undefined){
		pageSize = 10 ;
	}
	var param = {
			 customerIds:customerIds ,
			 orderType:orderType,
	         startTime:startTime ,
	         endTime:endTime ,
	         pageNumber:pageNumber,
	         pageSize:pageSize
	} ;
	getAjaxObject(_ctx + "emsOrderUnpushManager.do?query", param, "get").done(function(rs){
		$("#unpush_order_dg").datagrid("loadData" , {
			total:rs.total ,
			rows:rs.rows
			}) ;
		var reportDgPager = $("#unpush_order_dg").datagrid("getPager").pagination({
			onSelectPage : function(pageNumber, pageSize) {
				query(pageNumber, pageSize);
			}
		});
	})
}

function getAjaxObject(url, param, type ,contentType){
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
	

	var ajaxConf = {
	    type: type,
	    async: true, 
	    url: url,
	    data:param,
        datatype: "json"
	}

	if(contentType){
		ajaxConf.contentType = contentType;
	}


	var dtd = $.Deferred()

	$.ajax(ajaxConf).then(function(result){
		layer.close(layEle);
		var rs = JSON.parse(result);
    	if(!rs.success || rs.success == "true"){
    		dtd.resolve(rs);
    	}else{
    		dtd.reject(rs);
    	}
	})

	return dtd.promise();
}

function resetQueryCondition(){
	$("input[name='selectItemcustomerIds']:checked").each(function(i, o){
		$(this).attr("checked" , false) ;
	})
	$("#customerIds").next().find("button :first-child").html("请选择客户");
	$("#orderType").find("option:first-child").attr("selected" , true);
	$("#startTime").val("") ;
	$("#endTime").val("") ;
}

/**
 * 导出记录
 */
function exportExcel(){
	var customerIds = getSelectedCustomerIds().join(",") ;
	var startTime = jQuery("#startTime").val().trim() ;
	var endTime = jQuery("#endTime").val().trim() ;
	var orderType = jQuery("#orderType").val().trim() ;
	if(customerIds.length == 0){
		layer.tips('请选择客户', $("#customerIds").next().find("button") , {guide: 0, time: 3});
		return false ;
	}
	if(startTime != "" && endTime != "" && startTime > endTime){
		layer.tips('结束日期不能小于开始日期', $("#endTime") , {guide: 0, time: 3});
		return false ;
	}
	$.messager.confirm("确认","最多只能导出10000条未推送EMS订单记录",function(flag){
		if(flag){
			var fieldDef = new Array();
			var fields = jQuery('#unpush_order_dg').datagrid('getColumnFields');
			for (var i = 0; i < fields.length; i++) {
				var col = jQuery('#unpush_order_dg').datagrid('getColumnOption', fields[i]);
				if(col.field == 'id' || col.field == 'customerId' ){
					continue;
				}
				fieldDef.push({
					field:col.field,
					width:col.width,
					title:col.title
				})
			}
			var queryConditions = [{type:'hidden' , name:"startTime" , value:startTime}
			                        ,{type:'hidden' , name:"endTime" , value:endTime}
			                        ,{type:'hidden' , name:"orderType" , value:orderType}
			                        ,{type:'hidden' , name:"customerIds" , value:customerIds}
			                        ,{type:'hidden' , name:"columnDefs" , value:JSON.stringify(fieldDef)}] ;
			getExportExcel(queryConditions,_ctx + "emsOrderUnpushManager.do?exportReportExcel");
		}
	}) ;
}

function getExportExcel(queryConditions,actionUrl){
	if(queryConditions == undefined || queryConditions.length == undefined || queryConditions.length <= 0){
		return ;
	}
	var form = $("#export_excel_form");   //定义一个form表单
	if(form.html() != ""){
		form.html("") ;
	}
	for(var i = 0 ; i < queryConditions.length ; i++){
		var condition = queryConditions[i] ;
		var input = $('<input>');
	    input.attr('type', condition.type);
	    input.attr('name', condition.name);
	    input.attr('value', condition.value);
	    form.append(input);
	}
	form.attr('action', actionUrl);
	form.submit() ;
}