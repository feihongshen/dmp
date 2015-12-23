/**
 * 揽件入站 --jiangyu 2015-08-05
 */
$(function() {
	// 1.监听按键事件
	$("#scanNo").keydown(function checkIsEnter2OutStation(event) {
		// 获取输入框对象
		$("#scanNo").val($("#scanNo").val().trim());
		var _this = $("#scanNo");
		if (validateIsEmpty(_this.val())) {
			_this.val("");
		}
		// 使用正则表达式去除前后空格
		// 监听回车事件
		if (event.keyCode == 13 && _this.val().length > 0) {
			// 1.清空消息
			clearMsg();
			// 2.校验
			var valiRes = validateNextStation();
			if (!valiRes) {
				return;
			}
			// 3.执行操作+提示信息的展示
			executeOutStation(_this);
		}
	});
});

/**
 * 监听输入框的按键事件[入站操作的入口]
 */
function checkIsEnter2OutStation() {
	// 获取输入框对象
	$("#scanNo").val($("#scanNo").val().trim());
	var _this = $("#scanNo");
	if (validateIsEmpty(_this.val())) {
		_this.val("");
	}
	// 使用正则表达式去除前后空格
	// 监听回车事件
	if (event.keyCode == 13 && _this.val().length > 0) {
		// 1.清空消息
		clearMsg();
		// 2.校验
		var valiRes = validateNextStation();
		if (!valiRes) {
			return;
		}
		// 3.执行操作+提示信息的展示
		executeOutStation(_this);
	}
}

/**
 * 清空提示信息
 */
function clearMsg() {
	$("#msg").html("");
	$("#showScanNo_msg").html("");
	$("#destBranch_msg").html("");
	$("#nextBranch_msg").html("");
}

/**
 * 校验必输项：下一站
 * 
 * @returns {Boolean}
 */
function validateNextStation() {
	var flag = true;
	var _nextBranch = $("#nextBranch");
	// 是否为空的校验
	var validateRes = validateIsEmpty(_nextBranch.val());
	if (validateRes) {// 为空
		$("#msg").html("扫描前请选择下一站");
		flag = false;
	}
	return flag;
}

/**
 * 为空的校验
 * 
 * @param _thisEle
 * @returns {Boolean}
 */
function validateIsEmpty(_thisEle) {
	var isEmpty = false;
	if (undefined == _thisEle || null == _thisEle || "" == _thisEle) {
		isEmpty = true;
	}
	return isEmpty;
}

/**
 * 执行出站操作
 */
function executeOutStation(_thisScanNo) {
	// 获取参数值 
	var nextBranch = $("#nextBranch").val();
	var driverId = $("#driverId").val();
	var vehicleId = $("#vehicleId").val();
	var vehicleType = $("#vehicleType").val();
	var scanNo = _thisScanNo.val();
	// 组装参数
	var params = {
		nextBranch : nextBranch,
		driverId : driverId,
		vehicleId : vehicleId,
		vehicleType : vehicleType,
		scanNo : scanNo
	};
	// 执行操作
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/expressOutStation/executeOutStation?"+Math.random(),
		data : params,
		datatype : "json",
		success : function(result) {
			//清空输入框中的订单号
			$("#scanNo").val("");
			// 提示信息的处理
			executeResultMsgDisplay(result,scanNo);
		}
	});
}

/**
 * 操作结束之后的提示信息展示
 * 
 * @param _res
 */
function executeResultMsgDisplay(data,page_scanNo) {
	//判断是订单号还是包号
	var opeFlag = data.attributes.opeFlag;//[1:订单号，2：包号]
	if(data.status){
		if(opeFlag==1){//订单号
			$("#msg").html(page_scanNo+data.msg+"  （已扫1单，共"+data.obj.sendcarnum+"件）");
			$("#showScanNo_msg").html("运单号："+page_scanNo);
		}else{//包号
			$("#msg").html(page_scanNo+data.msg+"  （已扫1包，共"+data.obj.cwbcount+"单）");
			$("#showScanNo_msg").html("包    号："+page_scanNo);
		}
		$("#destBranch_msg").html("目的站："+data.attributes.deliveryBranchName);
		$("#nextBranch_msg").html("下一站："+data.attributes.nextBranchName);
	}else{
		clearMsg();
		$("#msg").html(page_scanNo + "  （异常扫描）" + data.msg);
	}
	
	batchPlayWav(data.wavList);
}
/**
 * 去除前后的空格
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");

}


$(function(){
	//单选模糊查询下拉框
	 $("#nextBranch").combobox();
	 $("#driverId").combobox();
 })

