

/**
 * 预览界面的生成按钮
 */
function generateBillOpe4Station(){
	var params = {
			billNo4Create:$("#billNo4PreScan").val(),
			customerId4Create:$("#customerId4PreScan").val(),
			payMethod4Create:$("#payMethod4PreScan").val(),
			deadLineTime4Create:$("#deadLineTime4PreScan").val(),
			remark4Create:$("#remark4PreScan").val(),
			branchId4Create:$("#branchId4PreScan").val()
	};
	generateBillOpe(params);
}

