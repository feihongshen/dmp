/**
 * 
 */

function saveEditInfo(){
	var billId = $("#eidtBillId").val();
	var remarkContent = $("#editrRemark").val();
	var params = {
			billId:billId,
			remark:remarkContent
	}
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url :  App.ctx+"/customerFreightCheck/updateRecordById",
		data : params,
		datatype : "json",
		success : function(result) {
			if(result){
				if(result.status){
					showMsg("保存成功");
					//创建成功之后跳转到编辑界面【弹出框】
					$('#dlgEditBox').dialog('close');
				}else{
					console.info(result.msg);
					$.messager.alert("提示", "保存失败 ！", "warning");
				}
			}
		}
	});
}

/**
 * 
 * @param pageNo
 */
function cwbDetailInfo4EditPage(pageNo4Edit){
	var recordId = $("#eidtBillId").val();
	if(recordId){
		$.ajax({
			type : "post",
			async : false, // 设为false就是同步请求
			url :  App.ctx+"/customerFreightCheck/switch2EditView",
			data : {
				page:pageNo4Edit,
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
	}
}