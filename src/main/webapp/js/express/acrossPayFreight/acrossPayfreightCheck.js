/**
 * 跨省运费应付对账
 */
var baseUrl_pay="acrossPayFreightCheck";
var opeFlag_rece = "AcrossPay";
var provincePageCache = [];
$(function(){
	provincePageCache = getSelectInfo(baseUrl_pay,opeFlag_rece);
	//1.新增按钮
	$("#add_btn").bind("click",initAddBox);
	//2.修改按钮
	$("#edit_btn").bind("click",initEditBox);
	
	//3.删除按钮
	$("#delete_btn").bind("click",executeDeleteOpe);
	//4.查询按钮
	$("#query_btn").bind("click",initQueryBox);
	
	//5.新增中的创建按钮
	$("#createBtn").bind("click",generateBillOpe);

	//6.查询界面的查询按钮
	$("#queryBtn").bind('click',executeQuery);
	//7.查询界面的关闭按钮
	$("#closeBtn").bind('click',closeQuerDlg_common);
});

/**
 * 初始化新增的弹出框
 */
function initAddBox(){
	//清空上一次输入的内容
	initAddDlgBox_common('add','addForm');
	
	initSelect("receProvince4Create",provincePageCache);
	//获取账单编号
	$('#dlgAddBox').dialog('open').dialog('setTitle', '新增账单');
}

/**
 * 查询界面初始化
 */
function initQueryBox(){
	$('#dlgQueryBox').dialog('open').dialog('setTitle', '查询条件');
	//初始化查询的条件  TODO
	initSelect("payableProvinceId",provincePageCache,true);
}

//================================Common===================

/**
 * 初始化编辑窗口
 */
function initEditBox(){
	initEditBox_common(baseUrl_pay,opeFlag_rece);
}

/**
 * 执行删除操作
 */
function executeDeleteOpe(){
	executeDeleteOpe_common(baseUrl_pay,opeFlag_rece);
}
/**
 * 执行查询
 */
function executeQuery(){
	executeQuery_common(baseUrl_pay,opeFlag_rece);
}



/**
 * 预览界面的生成按钮
 */
function generateBillOpe(){
	var params = {
			billNo4Create:$("#billNo4Create").val(),
			customerId4Create:$("#customerId4Create").val(),
			payMethod4Create:$("#payMethod4Create").val(),
			deadLineTime4Create:$("#deadLineTime4Create").val(),
			remark4Create:$("#remark4Create").val(),
			branchId4Create:$("#branchId4Create").val(),
			receProvince4Create:$("#receProvince4Create").val(),
			payProvince4Create:$("#payProvince4Create").val()
		}
	if(validateBeforeCreate()){
		generateBillOpe_common(params,baseUrl_pay,opeFlag_rece);
		//关闭创建的弹出框
		$('#dlgAddBox').dialog('close');
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
 * 编辑页面的保存
 * @param billId
 * @param remarkContent
 */
function saveEditInfo(billId,remarkContent){
	saveEditInfo_common(billId,remarkContent,baseUrl_pay,opeFlag_rece);
}

/**
 * 编辑的时候出现的提示框中的分页
 * @param pageNo
 */
function cwbDetailInfo4EditPage(pageNo4Edit,billId){
	if(billId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/"+baseUrl_pay+"/switch2EditView4"+opeFlag_rece,
			data : {
				page:pageNo4Edit,
				billId:billId,
				effectiveState:1
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
	
	
	cwbDetailInfo4EditPage_common(pageNo4Edit,billId,baseUrl_pay,opeFlag_rece);
}
