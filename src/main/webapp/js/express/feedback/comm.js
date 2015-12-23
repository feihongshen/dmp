/**
 * 反馈的时候【初始化弹出框】
 * @param feedBackState
 * @param delayed
 * @param success
 * @param fail
 * @param areaWrong
 * @param pickWrong
 * @param defaultInit
 */
function click_pickResult(feedBackState,delayed,success,fail,areaWrong,pickWrong,defaultInit) {
	var pickResultId = parseInt($("#pickResultId").val());
	init_feedBackState();
	//初始化
	if(pickResultId==delayed){//延迟
		initFeedBackDelay();
	}else if(pickResultId==success){//成功
		initFeedBackSuccess();
	}else if(pickResultId==fail){//失败
		initFeedBackFail();
	}else if(pickResultId==areaWrong){//站点超区
		initFeedBackAreaWrong();
	}else if(pickResultId==pickWrong){//揽件超区
		initFeedBackPickWrong();
	}
}
/**
 * 初始化反馈为延迟揽件
 */
function initFeedBackDelay(){
	//运单号
	$("#transNo").parent().hide();
	//失败一级二级原因
	$("#pickFailedFirstLevel").parent().hide();
	$("#pickFailedSecondLevel").parent().hide();
	//站点超区一级二级原因
	$("#areaWrongFirstLevel").parent().hide();
	$("#areaWrongSecondLevel").parent().hide();
	//揽件超区一级二级原因
	$("#pickWrongFirstLevel").parent().hide();
	$("#pickWrongSecondLevel").parent().hide();
	//揽件延迟一级二级原因
	$("#pickDelayFirstLevel").parent().show();
	$("#pickDelaySecondLevel").parent().show();
	//下次揽件时间
	$("#nextPickExpressTime").parent().show();
}
/**
 * 初始化反馈为成功
 */
function initFeedBackSuccess(){
	$("#transNo").parent().show();
	
	$("#pickFailedFirstLevel").parent().hide();
	$("#pickFailedSecondLevel").parent().hide();
	
	$("#areaWrongFirstLevel").parent().hide();
	$("#areaWrongSecondLevel").parent().hide();
	
	$("#pickWrongFirstLevel").parent().hide();
	$("#pickWrongSecondLevel").parent().hide();
	
	$("#pickDelayFirstLevel").parent().hide();
	$("#pickDelaySecondLevel").parent().hide();
	
	
	$("#nextPickExpressTime").parent().hide();
}
/**
 * 初始化反馈为失败
 */
function initFeedBackFail(){
	$("#transNo").parent().hide();
	
	$("#pickFailedFirstLevel").parent().show();
	$("#pickFailedSecondLevel").parent().show();
	
	$("#areaWrongFirstLevel").parent().hide();
	$("#areaWrongSecondLevel").parent().hide();
	
	$("#pickWrongFirstLevel").parent().hide();
	$("#pickWrongSecondLevel").parent().hide();
	
	$("#pickDelayFirstLevel").parent().hide();
	$("#pickDelaySecondLevel").parent().hide();
	$("#nextPickExpressTime").parent().hide();
}
/**
 * 初始化反馈为站点超区
 */
function initFeedBackAreaWrong(){
	$("#transNo").parent().hide();
	
	$("#pickFailedFirstLevel").parent().hide();
	$("#pickFailedSecondLevel").parent().hide();
	
	$("#areaWrongFirstLevel").parent().show();
	$("#areaWrongSecondLevel").parent().show();
	
	$("#pickWrongFirstLevel").parent().hide();
	$("#pickWrongSecondLevel").parent().hide();
	
	$("#pickDelayFirstLevel").parent().hide();
	$("#pickDelaySecondLevel").parent().hide();
	$("#nextPickExpressTime").parent().hide();
}
/**
 * 初始化反馈为揽件超区
 */
function initFeedBackPickWrong(){
	$("#transNo").parent().hide();
	
	$("#pickFailedFirstLevel").parent().hide();
	$("#pickFailedSecondLevel").parent().hide();
	
	$("#areaWrongFirstLevel").parent().hide();
	$("#areaWrongSecondLevel").parent().hide();
	
	$("#pickWrongFirstLevel").parent().show();
	$("#pickWrongSecondLevel").parent().show();
	
	$("#pickDelayFirstLevel").parent().hide();
	$("#pickDelaySecondLevel").parent().hide();
	$("#nextPickExpressTime").parent().hide();
}
/**
 * 初始化的弹出框的显示
 */
function init_feedBackState(){
	$("#transNo").parent().hide();
	
	$("#pickFailedFirstLevel").parent().hide();
	$("#pickFailedSecondLevel").parent().hide();
	
	$("#areaWrongFirstLevel").parent().hide();
	$("#areaWrongSecondLevel").parent().hide();
	
	$("#pickWrongFirstLevel").parent().hide();
	$("#pickWrongSecondLevel").parent().hide();
	
	$("#pickDelayFirstLevel").parent().hide();
	$("#pickDelaySecondLevel").parent().hide();
	$("#nextPickExpressTime").parent().hide();
}
/**
 * 反馈之前的校验
 */
function checkFeedBackStateContent(delayed,success,fail,areaWrong,pickWrong){
	//反馈结果
	var pickResultId = parseInt($("#pickResultId").val());
	var selText = $("#pickResultId").find("option:selected").text()
	if(pickResultId==-1){
		return checkFeedBackStateCommon();
	}else if(pickResultId==delayed){//延迟揽件
		var reasonFlagCheck = checkFirstAndSecondLevel('pickDelayFirstLevel','pickDelaySecondLevel',selText);
		var nextPickTime = checkNextPickTime();
		return reasonFlagCheck&&nextPickTime;
	}else if(pickResultId==success){//揽件成功
		return checkTransNo();
	}else if(pickResultId==fail){//揽件失败
		return checkFirstAndSecondLevel('pickFailedFirstLevel','pickFailedSecondLevel',selText);
	}else if(pickResultId==areaWrong){//站点超区
		return checkFirstAndSecondLevel('areaWrongFirstLevel','areaWrongSecondLevel',selText);
	}else if(pickResultId==pickWrong){//揽件超区
		return checkFirstAndSecondLevel('pickWrongFirstLevel','pickWrongSecondLevel',selText);
	}
}
/**
 * 揽件成功的时候检查运单号是否填写
 */
function checkTransNo(){
	//运单号
	var transNo = $("#transNo").val();
	transNo = transNo.trim();
	if(transNo==null||transNo==""||transNo.length==0){
		alert("请填写运单号");
		return false;
	}else if(!checkTransNoIsExist(transNo)){
		alert("系统中该运单号已经存在");
		return false;
	}else{
		return checkFeedBackStateCommon();
	}
}
/**
 * 校验运单号是否存在
 * @param transNo_temp
 */
function checkTransNoIsExist(transNo_temp){
	var checkFlag = true;
	var params = {
		transNo:transNo_temp
	}
	$.ajax({
	    type: "post",
	    async: false, //设为false就是同步请求
	    url: "expressFeedback/checkTransNoIsRepeat?"+Math.random(),
	    data: params,
	    datatype: "json",
	    success: function (result) {
	    	if(result){
	    		if (result.status) {
	    			checkFlag = false;
	    		} else {
	    			checkFlag = true;
	    		}
	    	}
	    }
	});
	return checkFlag;
}


/**
 * 检查一级原因和二级原因
 * @param first_name
 * @param second_name
 * @param sel_Text
 * @returns {Boolean}
 */
function checkFirstAndSecondLevel(first_name,second_name,sel_Text){
	var firstLevel = parseInt($("#"+first_name).val());
	var secondLevel = parseInt($("#"+second_name).val());
	if (firstLevel == 0) {
		alert("请选择"+sel_Text+"一级原因");
		return false;
	}else if (secondLevel == 0) {
		alert("请选择"+sel_Text+"二级原因");
		return false;
	} else {
		return checkFeedBackStateCommon();
	}
}
/**
 * 校验下次揽件时间
 */
function checkNextPickTime(){
	var time = $("#nextPickExpressTime").val();
	if(time==null||time==""){
		alert("请填写预计下次揽件时间");
		return false;
	}else if(time.length!=19){
		alert("时间格式为：yyyy-MM-dd HH:mm:ss");
	}else{
		return checkFeedBackStateCommon();
	}
}

/**
 * 
 * @returns {Boolean}
 */
function checkFeedBackStateCommon() {
	if (parseInt($("#pickResultId").val()) == -1) {
		alert("请选择反馈结果");
		return false;
	}
	return true;
}
