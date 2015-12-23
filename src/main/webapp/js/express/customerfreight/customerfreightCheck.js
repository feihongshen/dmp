/**
 * 客户运费对账
 */
var baseUrl_cust = "customerFreightCheck";
var opeFlag_cust = "Customer";
var customerPageCache = [];
$(function(){
	customerPageCache = getSelectInfo(baseUrl_cust,opeFlag_cust);
	//1.新增按钮
	$("#add_btn").bind("click",initAddBox);
	//2.修改按钮
	$("#edit_btn").bind("click",initEditBox);

	//3.删除按钮
	$("#delete_btn").bind("click",executeDeleteOpe);
	//4.查询按钮
	$("#query_btn").bind("click",initQueryBox);
	
	//5.新增中的创建按钮
	$("#createBtn").bind("click",executeCreateBill);
	//6.新增中的返回按钮
	$("#backBtn").bind("click",backCreatePage_common);

	//9.查询界面的查询按钮
	$("#queryBtn").bind('click',executeQuery);
	//10.查询界面的关闭按钮
	$("#closeBtn").bind('click',closeQuerDlg_common);
});

/**
 * 初始化新增的弹出框
 */
function initAddBox(){
	//清空上一次输入的内容
	initAddDlgBox_common('add','addForm');
	initSelect("customerId4Create",customerPageCache);
	//获取账单编号
	$('#dlgAddBox').dialog('open').dialog('setTitle', '新增账单');
}



/**
 * 查询界面初始化
 */
function initQueryBox(){
	$('#dlgQueryBox').dialog('open').dialog('setTitle', '查询条件');
	//初始化查询的条件  TODO
	initSelect("customerId",customerPageCache,true);
}

//================================Common===================




/**
 * 初始化编辑窗口
 */
function initEditBox(){
	initEditBox_common(baseUrl_cust,opeFlag_cust);
}

/**
 * 执行删除操作
 */
function executeDeleteOpe(){
	executeDeleteOpe_common(baseUrl_cust,opeFlag_cust);
}
/**
 * 执行查询
 */
function executeQuery(){
	executeQuery_common(baseUrl_cust,opeFlag_cust);
}


/**
 * 执行创建账单的操作
 */
function executeCreateBill(){
	executeCreateBill_common(baseUrl_cust,opeFlag_cust);
}


/**
 * 预览界面的生成按钮
 */
function generateBillOpe(params_tmp){
	generateBillOpe_common(params_tmp,baseUrl_cust,opeFlag_cust);
}

/**
 * 编辑页面的保存
 * @param billId
 * @param remarkContent
 */
function saveEditInfo(billId,remarkContent){
	saveEditInfo_common(billId,remarkContent,baseUrl_cust,opeFlag_cust);
}

/**
 * 编辑的时候出现的提示框中的分页
 * @param pageNo
 */
function cwbDetailInfo4EditPage(pageNo4Edit,billId){
	cwbDetailInfo4EditPage_common(pageNo4Edit,billId,baseUrl_cust,opeFlag_cust);
}
/**
 * 预览界面的分页
 * @param pageNo
 */
function cwbDetailInfoPreScanPage(temp_params){
	cwbDetailInfoPreScanPage_common(temp_params,baseUrl_cust,opeFlag_cust);
}