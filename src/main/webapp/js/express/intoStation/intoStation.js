
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
		$(td_$).parent().parent().css("background-color","#FFFFFF");
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
/**
 * 查询数据
 */
function queryData(){
	$("#queryCondition").submit();
}


/**
 * 确认交接前校验
 */
function confirmBeforeValidate(){
	var result_flag = true;
	var deliverman = $("#deliveryId").val();
	if(deliverman<=0){
		result_flag = false;
	}
	return result_flag;
}

/**
 * 揽件确认
 */
function confirmIntoStation(id){
	/*var flag = confirmBeforeValidate();
	if(!flag){
		$.messager.alert("提示", "请先选择小件员 ！", "warning");
		return;
	}*/
	var ids ="";
	//拿到操作的id
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if($(this)[0].checked){
			ids +=$(this).val()+ ",";
		}
	});
	if(ids.length==0){
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return ;
	}else{
		ids = ids.substring(0,ids.length-1);
	}
	//缓存选中的id
	$("#exp_selectIds").val(ids);
	//查询数据
	var data = querySelectRecord(ids);
	
	if(data!=null&&data!=undefined){
		$("#totalOrderCount").html(data.attributes.totalOrderCount);
		$("#totalTransFee").html(data.attributes.totalTransFee);
		/*
		$("#nowPayOrderCount").html(data.attributes.nowPayOrderCount);
		$("#nowPayTransFee").html(data.attributes.nowPayTransFee);
		$("#arrivePayOrderCount").html(data.attributes.arrivePayOrderCount);
		$("#arrivePayTransFee").html(data.attributes.arrivePayTransFee);
		$("#monthPayOrderCount").html(data.attributes.monthPayOrderCount);
		$("#monthPayTransFee").html(data.attributes.monthPayTransFee);
		*/
		$("#deliveryman").html(data.attributes.deliverNameStr);
	}
	
	$("#deliveryManId").val(0);
	$("#dlgSubmitExpressBox").dialog("open").dialog('setTitle', '交件确认');
}

/**
 * 查询总的记录数
 */
function querySelectRecord(temp_ids){
	var backData = {};
	//请求后台操作
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/expressIntoStation/querySelectIdsTotalRecord",
		data : {"ids":temp_ids},
		datatype : "json",
		success : function(result) {
			if(result){
				backData = result;
			}
		}
	});
	return backData;
}



//控制显示还是不显示
function controlDisplayBtn(selEle){
	var _this = $(selEle);
	if(_this.val()==2){
		$("#confirm_btn").attr("disabled","disabled");
	}else{
		$("#confirm_btn").removeAttr("disabled");
	}
}

/**
 * 小件员的触发
 */
function controlBtnEnable(selEle){
	if(_this.val()<=0){
		$("#confirm_btn").attr("disabled","disabled");
	}else{
		$("#confirm_btn").removeAttr("disabled");
	}
}

//交件确认
function submitExpressOperate(){
	var deliveryManId = $("#deliveryManId").val();
	var submitTime = $("#submitTime").html();
	var ids = $("#exp_selectIds").val();
	var params = {
			ids:ids,
			deliveryId:deliveryManId,
			pickExpressTime:submitTime
	};
	//请求后台操作
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/expressIntoStation/executeIntoStation",
		data : params,
		datatype : "json",
		success : function(result) {
			if(result){
				if(result.status){//成功
					showMsg("确认交件成功操作"+result.recordCount+"单！");
					//关闭弹出框
					$("#dlgSubmitExpressBox").dialog("close");
					//重新查询记录
					setTimeout(function(){
						$('#queryCondition').attr('action',$("#selectPg").val());
						queryData();
					},1000);
				}else{//失败
					$.messager.alert("提示", "操作失败，请联系管理员 ！", "error");
					$("#dlgSubmitExpressBox").dialog("close");
				}
			}
		}
	});
}
/**
 * 提示框出现
 */
function showMsg(msg){
	var options = {
	        title: "操作提示",
	        msg: msg+"！",
	        showType: 'slide',
	        timeout: 1000,
	        style : {
				right : '',
				top :$(document).height()/2-150,
				left:$(document).width()/2-120,
				bottom : ''
			}
	};
	$.messager.show(options);
}
/**
 * 打印功能
 */
function printFunc(){
	$.messager.alert("提示", "暂时没能实现 ！", "warning");
}