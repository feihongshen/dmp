/**
 * 清空输入内容
 */
var msgArray=[];
function initAddDlgBox_common(opeFlag,boxFormId,billFlag){
	var _this = $("#"+boxFormId);
	if(_this){
		_this.find("input,select,textarea:visible").each(function(){
			var item = $(this);
			if(item.attr("type")!="hidden"){
				if(item.attr("name")!="billNo4Create"&&item.attr("name")!="receProvince4CreateName"&&item.attr("name")!="payProvince4CreateName"){
					item.val("");
					if(item.attr("disabled")){
						item.removeAttr("disabled");
					}
				}
				if(item.attr("id")=="remark4Create"){
					item.text("");
				}
			}
		});
	}
}

/**
 * 初始化编辑窗口
 */
function initEditBox_common(baseUrl,opeFlag){
	//1.选择的记录
	var recordId = getSelectedItem('listTable',false,false);
	if(recordId){
		//创建操作开始
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/"+baseUrl+"/switch2EditView4"+opeFlag,
			data : {
				billId:recordId
			},
			datatype : "json",
			success : function(result) {
				if(result){
					$('#editScanContainer').html("");
					$('#editScanContainer').html(result);
				}
			}
		});
		//修改账单的弹窗口
		$('#dlgEditBox').dialog('open').dialog('setTitle', '修改账单');
	}
}
/**
 * 执行删除操作
 */
function executeDeleteOpe_common(baseUrl,opeFlag){
	//获取选择的账单id
	var recordId = getSelectedItem('listTable',false,true);
	if(recordId){
		var params={
				billId:recordId
		}
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/"+baseUrl+"/deleteBillById4"+opeFlag,
			data : params,
			datatype : "json",
			success : function(result) {
				//刷新页面
				if(result){
					if(result.status){
						showMsg("删除成功");
						setTimeout(function(){
							queryData_common();
						},1000);
					}else{
						showMsg(result.msg);
					}
				}
			}
		});
	}
}
/**
 * 执行查询
 */
function executeQuery_common(baseUrl,opeFlag){
	var url = App.ctx+"/"+baseUrl+"/billList4"+opeFlag+"/1";
	//form提交
	$("#queryBoxItemForm")
		.attr('action',url)
		.submit();
}




/**
 * 重新请求页面
 */
function queryData_common(){
	$("#queryCondition").submit();
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
 * 执行创建账单的操作
 */
function executeCreateBill_common(baseUrl,opeFlag){
	if(validateBeforeCreate()){
		$('#dlgAddBox').dialog('close');//可以试一下效果  如果不行就直接先隐藏然后再次弹出
		//查询数据并且组织好数据显示
		var preRes = initPreScanData_common(1,baseUrl,opeFlag);
		if(preRes){
			//弹出到预览的弹出框
			$('#dlgPreScanBox4Cust').dialog('open').dialog('setTitle', '预览');
		}else{
			$.messager.alert("提示", "没有可以生成账单的订单数据！", "warning");
			return ;
		}
	}else{
		if(msgArray.length>0){
			var _this = $('#'+msgArray[0])[0];
			if(msgArray[0]=="deadLineTime4Create"){
				$($('#'+msgArray[0])[0].form).find("input").each(function(){
					var item = $(this);
					if(item.attr("class")=="combo-text validatebox-text validatebox-f textbox"){
						_this = item[0];
					}
				});
			}
			layer.tips('提示：不能为空', _this,1);
			return false;
		}
	}
}

/**
 * 创建之前的校验
 */
function validateIsNotNull(itemId){
	if($("#"+itemId)){
		var _this_cre = $("#"+itemId).val();
		if(itemId=="deadLineTime4Create"){
			_this_cre = $("input[name='deadLineTime4Create']").val();
			$("#"+itemId).val(_this_cre);
		}
		if(undefined==_this_cre){
			return true;
		}
		if(null==_this_cre||_this_cre==""){//undefined==_this_cre||
			msgArray.push(itemId);
			return false;
		}
		return true;
	}else{
		return true;
	}
}

/**
 * 创建操作之前的校验
 */
function validateBeforeCreate(){
	var valiParams = [];
	valiParams.push("billNo4Create");
	valiParams.push("customerId4Create");
	valiParams.push("payMethod4Create");
	valiParams.push("deadLineTime4Create");
	valiParams.push("branchId4Create");
	valiParams.push("payProvince4Create");
	valiParams.push("receProvince4Create");
	var result = validateArrayValue(valiParams);
	return result;
}
/**
 * 循环校验
 */
function validateArrayValue(datas){
	var valiRes = true;
	if(datas){
		msgArray = [];
		$.each(datas,function(index,data){
			if(!validateIsNotNull(data)){
				valiRes = false;
				return false;
			}
		});
	}
	return valiRes;
}


/**
 * 初始化预览界面
 */
function initPreScanData_common(pageNo,baseUrl,opeFlag){
	var flag_temp = true;
	if(pageNo){
		pageNo = 1;
	}
	var params4Create={
			page:pageNo,
			billNo4Create:$("#billNo4Create").val(),	
			customerId4Create:$("#customerId4Create").val(),	
			payMethod4Create:$("#payMethod4Create").val(),	
			deadLineTime4Create:$("#deadLineTime4Create").val(),	
			remark4Create:$("#remark4Create").val(),	
			branchId4Create:$("#branchId4Create").val(),
			receProvince4Create:$("#receProvince4Create").val(),
			payProvince4Create:$("#payProvince4Create").val()
	}
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/"+baseUrl+"/preScanView4"+opeFlag,
		data : params4Create,
		datatype : "json",
		success : function(result) {
			if(result.indexOf('<span><font color="red">没有可以生成账单的订单数据</font></span>')>0){
				flag_temp = false;
			}else{
				$('#preScanContainer4Cust').html("");
				$('#preScanContainer4Cust').html(result);
			}
		}
	});
	return flag_temp;
}


/**
 * 预览界面的返回按钮
 */
function cancleCreateBill_common(baseUrl,opeFlag){
	//关闭预览界面回到创建页面
	$('#dlgPreScanBox4Cust').dialog('close');
	//弹出到创建的弹出框
	$('#dlgAddBox').dialog('open').dialog('setTitle', '新增账单');//可以试一下效果  如果不行就直接先隐藏然后再次弹出
}


/**
 * 预览界面的生成按钮
 */
function generateBillOpe_common(params_tmp_1,baseUrl,opeFlag){
	var params = params_tmp_1;
	//创建操作开始
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/"+baseUrl+"/createBillRecord4"+opeFlag,
		data : params,
		datatype : "json",
		success : function(result) {
			if(result){
				$('#editScanContainer').html("");
				$('#editScanContainer').html(result);
			}
		}
	});
	//创建成功之后跳转到编辑界面【弹出框】
	$('#dlgPreScanBox4Cust').dialog('close');
	$('#dlgEditBox').dialog('open').dialog('setTitle', '修改账单');
}



/**
 * 编辑页面的返回按钮
 */
function closeEditBox_common(){
	$('#dlgEditBox').dialog('close');
}

/**
 * 关闭查询框
 * @returns
 */
function closeQuerDlg_common(){
	$("#dlgQueryBox").dialog('close');
}

/**
 * 新增界面的返回按钮 done
 */
function backCreatePage_common(){
	//关闭当前弹出框
	$('#dlgAddBox').dialog('close');
}

/**
 * 获取选择的checkbox
 * @param tableId
 * @param mutilSupport
 * @returns {String}
 */
function getSelectedItem(tableId,mutilSupport,isDelete){
	var selectItemCount = 0;
	var seleIds ="";
	var billStateVal = "";
	//拿到操作的id
	var chkBoxes = $("#"+ tableId +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if($(this)[0].checked){
			selectItemCount++;
			seleIds +=$(this).val()+ ",";
			//获取账单的状态
			billStateVal = $(this).attr("extralval");
		}
	});
	if(selectItemCount>1&&!mutilSupport){
		$.messager.alert("提示", "当前操作只支持单条记录 ！", "warning");
		return ;
	}
	//是否是删除操作
	if(isDelete){
		if(billStateVal==null||billStateVal.length==0){
			$.messager.alert("提示", "账单的状态未知，不允许删除 ！", "warning");
			return ;
		}else if(parseInt(billStateVal)>0){//未审核的账单
			$.messager.alert("提示", "删除只支持未审核的账单！", "warning");
			return ;
		}
	}
	if(seleIds.length==0){
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return ;
	}else{
		seleIds = seleIds.substring(0,seleIds.length-1);
	}
	return seleIds;
}

/**
 * 设置颜色[只支持单选]
 */
function initColor(td_$,id){
	if(td_$.checked==null||td_$.checked==undefined){
		td_$.checked = false;
	}else{
		if(td_$.checked){
			//选中的全部不再选中
			var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
			$(chkBoxes).each(function() {
				$(this)[0].checked = false;
				$(this).parent().parent().css("background-color","#FFFFFF");
			});
			td_$.checked = true;
		}
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
 * 编辑页面的保存
 * @param billId
 * @param remarkContent
 */
function saveEditInfo_common(billId,remarkContent,baseUrl,opeFlag){
	var params = {
		billId:billId,
		remark:remarkContent
	}
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/"+baseUrl+"/updateRecordByBillId4"+opeFlag,
		data : params,
		datatype : "json",
		success : function(result) {
			if(result){
				if(result.status){
					showMsg("保存成功");
					//创建成功之后跳转到编辑界面【弹出框】
					$('#dlgEditBox').dialog('close');
					//页面数据刷新
					setTimeout(function(){
						queryData_common();
					},1000);
				}else{
					console.info(result.msg);
					$.messager.alert("提示", "保存失败 ！", "warning");
				}
			}
		}
	});
}
/**
 * 编辑的时候出现的提示框中的分页
 * @param pageNo
 */
function cwbDetailInfo4EditPage_common(pageNo4Edit,billId,baseUrl,opeFlag){
	if(billId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/"+baseUrl+"/switch2EditView4"+opeFlag,
			data : {
				page:pageNo4Edit,
				billId:billId
			},
			datatype : "json",
			success : function(result) {
				if(result){
					$('#editScanContainer').html("");
					$('#editScanContainer').html(result);
				}
			}
		});
	}
}
/**
 * 预览界面的分页
 * @param pageNo
 */
function cwbDetailInfoPreScanPage_common(temp_params,baseUrl,opeFlag){
	var params = {};
	$.extend(params,temp_params);
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/"+baseUrl+"/preScanView4"+opeFlag,
		data : params,
		datatype : "json",
		success : function(result) {
			$('#preScanContainer4Cust').html("");
			$('#preScanContainer4Cust').html(result);
		}
	});
}


/**
 * 初始化select[特殊待遇]
 */
function initSelect(eleName,items,special){
	var _this = null;
	if(special){//特殊
		_this = $("#dlgQueryBox select[name=\'"+eleName+"\']");
	}else{
		_this = $($("#"+eleName)[0]);
	}
	_this.empty();
	_this.append("<option value=''></option>");
	$.each(items, function(i, item) {
		_this.append("<option value=\"" + item.hiddenValue + "\"" + (_this.value == item.hiddenValue ? "  selected" : "") + ">" + item.displayValue + "</option>");
	});
}
/**
 * 获取集合
 */
function getSelectInfo(baseUrl,suffix){
	var result = [];
	var params = {};
	$.ajax({
		type:"post",
		async:false,
		url:App.ctx+"/"+baseUrl+"/getSelectInfo4"+suffix,
		data:params,
		datatype:"json",
		success:function(res){
			if(res){
				result = res;
			}
		}
	});
	return result;
}

