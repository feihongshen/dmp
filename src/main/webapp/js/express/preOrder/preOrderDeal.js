/**
 * 
 */
 
$(function(){
	
	$("#status").val($("#statusValue").val());
//	$("#siteInfo").combobox();
//	$(".combo-value").val($("#statusValue").val());
//	$("#status").text();
//	$("#status").find("option:selected").text();
//	$(".combo span input[type='text']").val();
//	$(".combo-text.validatebox-text.validatebox-f.textbox").val();//一个元素有多个class的时候，用类选择器选择钙元素的时候，每个class之间都要用.来连接，不能有空格；
	
//	$(".combo span input[type='text']").val($("#status").find("option:selected").text());
	$("#selectPg").val($("#selectPage").val());
	//查询按钮
	$("#query").bind("click",query);
	
//	$("#handMatchButtom").bind("click",getSelect('preOrderTable','handMatch'));
	
	//关闭预订单弹出框退出
	$("#cancleClose").bind("click",cancleCloseOrder);
	//手动分配站点弹出框的取消按钮
	$("#cancleMatch").bind("click",cancleMatch);
	//退回总部弹出框的取消按钮
	$("#cancleReturn").bind("click",cancleReturn);
	
	//当状态为站点超区的时候禁用自动匹配站点
	if($("#status").val()==5){
		$("#autoMatchButton").attr("disabled","disabled");
	}
	//单选模糊查询下拉框
	$("#siteInfo").combobox();
})

 function addInit(){
 	//无处理
 }
 /**
  * 设置颜色
  */
 function initColor(td_$){
 	if(td_$.checked==null||td_$.checked==undefined){
 		td_$.checked = false;
 	}
 	if(td_$.checked){
 		$(td_$).parent().parent().css("background-color","#FFD700");
 	}else{
 		$(td_$).parent().parent().css("background-color","#f5f5f5");
 	}
 }

 //全选
 function checkAll(id){ 
 	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
 	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
 	$(chkBoxes).each(function() {
 		$(this)[0].checked = chkAll;
 	});
 }

 //关闭预订单的点击触发函数
 function getSelet(id,mark){
	var ids = getIds(id);
 	if(ids.length==0){
 		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
 		return ;
 	}else{
 		//手动匹配站点弹出框
 		if(mark=="handMatch"){
 			$("#dlgSubmitExpressBox").dialog("open").dialog('setTitle', '预订单分配站点');
 		}
 		//退回总部弹出框
 		if(mark=="returnHeadQuarters"){
 			$("#returnOrderDialog").dialog("open").dialog('setTitle', '退回总部');
 			$("#returnOrderText").val("");
 		}
 		//关闭订单弹出框
 		if(mark=="closeOrder"){
			$("#closeOrderDialog").dialog("open").dialog('setTitle', '关闭预订单');
			$("#closrOrderText").val("");
 		}
 	}
 }
 
//关闭预订单弹出框的确认操作
function confirmClose(id){
		var ids = getIds(id);
		ids = ids.substring(0,ids.length-1);
		var obj = getParams(ids);
		var param = {};
		param.ids=ids;
		param.preOrderNo = obj.preOrderNos;
		param.reason = obj.reasons;
		$.ajax({
			type : "POST",
			async : false,
			url : App.ctx + "/preOrderOperationController/closeOrder",
			data : param,
			dataType : "json",
//			scriptCharset : "ISO-8859-1",
			success : function(data) {
				cancleCloseOrder();
				$.messager.alert('提示框','关闭预订单成功！');
				$('#searchForm_t').attr('action',$("#selectPg").val());
				query();
			}
		});
}

 //退回总部弹出框的确认操作
 function confirmReturn(id){
	var ids = getIds(id);
	ids = ids.substring(0,ids.length-1);
	var obj = getParams(ids);
	var param = {};
	param.ids=ids;
	param.preOrderNo = obj.preOrderNos;
	param.reason = obj.reasons;
	$.ajax({
		type : "POST",
		async : false,
		url : App.ctx + "/preOrderOperationController/getReturnHeadQuarters",
		data : param,
		dataType : "json",
		success : function(data) {
			cancleReturn();
			$.messager.alert('提示框','退回总部成功！');
			$('#searchForm_t').attr('action',$("#selectPg").val());
			query();
		}
	});
 }
 
 //手动分配站点弹出框的确认按钮
 function confirmMatch(id){
	var ids = getIds(id);
 	ids = ids.substring(0,ids.length-1);
 	var obj = getParams(ids);
	var param = {};
	param.ids = ids;
	param.siteName = $(".combo-text.validatebox-text.validatebox-f.textbox").val();
//	$("#siteInfo").show();
//以下两种设置下拉框选中的方式失效，原因未知；
//	$("#siteInfo option[text='11']").attr("selected", true);
//	 $("#siteInfo").find("option[text='11']").attr("selected",true);
//	$.each($("#siteInfo")[0].options,function(ind,opt){
//		if(param.siteName==opt.text){
//			console.info(opt.value);
//			param.siteId = opt.value;
//		}
//	});
//	param.siteId = $("#siteInfo").val();
//	$("#siteInfo").hide();
	param.siteId = $("#siteInfo").val();
	param.siteName = $("#siteInfo").find("option:selected").text();
	param.preOrderNo = obj.preOrderNos;
	param.reason = obj.reasons;
	if(param.siteId==0){
		$.messager.alert('提示框','请选择站点！');
		return;
	}
	$.ajax({
			type : "POST",
			async : false,
			url : App.ctx + "/preOrderOperationController/handMatch",
 			data : param,
 			dataType : "json",
 			success : function(data) {
 				$.messager.alert('提示框','分配站点成功！');  
				cancleMatch();
 			}
 		});
	$('#searchForm_t').attr('action',$("#selectPg").val());
	query();
 }
 
 //获取id
 function getIds(id){
	 	var ids ="";
	 	//拿到操作的id
	 	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	 	$(chkBoxes).each(function() {
	 		if($(this)[0].checked){
	 			ids +=$(this).val()+ ",";
	 		}
	 	});
		return ids;
 }
 
 //获取参数
 function getParams(ids){
	var preOrderNo = "";
	var preOrderNos = "";
	var reason = "";
	var reasons = "";
	var address = "";
	var addresses = "";
	var idArray = ids.split(",");
	for(var i = 0;i < idArray.length;i++){
		preOrderNo = idArray[i]+"preOrderNo";
		preOrderNos += $("#"+preOrderNo).html()+",";
		reason = idArray[i]+"reason";
		reasons += $("#"+reason).html()+",";
		address = idArray[i]+"collectAddress";
		addresses += $("#"+address).html()+",";
	}
	preOrderNos = preOrderNos.substring(0,preOrderNos.length-1);
	reasons = reasons.substring(0,reasons.length-1);
	addresses = addresses.substring(0,addresses.length-1);
	var obj ={};
	obj.preOrderNos = preOrderNos;
	obj.reasons = reasons;
	obj.addresses = addresses;
	 return obj;
 }
 
 
 //手动分配站点弹出框的取消按钮
 function cancleMatch(){
	 $("#dlgSubmitExpressBox").dialog("close");
 }
 //关闭预订单弹出框的取消按钮
 function cancleCloseOrder(){
	 $("#closeOrderDialog").dialog("close");
 }
 //退回总部弹出框的取消按钮
 function cancleReturn(){
	 $("#returnOrderDialog").dialog("close");
 }
 
 
 //查询提交方法
 function query(){
 	$("#searchForm_t").submit();
 }

 //自动匹配站点方法
 function autoMatch(id){
	var ids = getIds(id);
 	if(ids.length==0){
 		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
 		return ;
 	}else{
 		
 		ids = ids.substring(0,ids.length-1);
 		var obj = getParams(ids);
 		var param = {};
 		param.ids=ids;
 		param.preOrderNo = obj.preOrderNos;
 		param.reason = obj.reasons;
 		param.address = obj.addresses;
 		$.ajax({
 			type : "POST",
 			async : false,
 			url : App.ctx + "/preOrderOperationController/autoMatch",
 			data : param,
 			dataType : "json",
 			success : function(data) {
 				setTimeout('query()',1000);
 			}
 		});
 	}
 }
 
 //匹配状态下拉框改变事件
 function autoChange(){
	 //当状态为站点超区的时候禁用自动匹配站点
	if($("#status").val()==5){
		$("#autoMatchButton").attr("disabled","disabled");
	}else{
		$("#autoMatchButton").removeAttr("disabled");
	}
 }
