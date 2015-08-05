function doAssign() {
	if ($("#deliverid").val() == -1) {
		return;
	}
	$.ajax({
		type : "POST",
		url : $("#doAssign").val(),
		data : {
			"selectedPreOrders" : $("#selectedPreOrders").val(),
			"deliverid" : $("#deliverid").val()
		},
		success : function(data) {
			showHintInfo(data);
		},
		complete : function() {
			// addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
};

function doAssignAndExport() {
	if ($("#deliverid").val() == -1) {
		return;
	}
	doAssign();
	exportExcel();
};
function exportExcel(){
	$.ajax({
		type : "POST",
		url : $("#exportExcel").val(),
		data : {
			"selectedPreOrders" : $("#selectedPreOrders").val()
		},
		success : function(data) {
			
		},
		complete : function() {
			// addInit();// 初始化某些ajax弹出页面
//			viewBox();
		}
	});
}

function doSuperzone() {
	if ($("#note").val() == null||$("#note").val() =="") {
		return;
	}
	$.ajax({
		type : "POST",
		url : $("#doSuperzone").val(),
		data : {
			"selectedPreOrders" : $("#selectedPreOrders").val(),
			"note" : $("#note").val()
		},
		success : function(data) {
			showHintInfo(data);
		},
		complete : function() {
			// addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
};
function showHintInfo(data){
	if(data){
		$(".tishi_box").html("操作成功！");
	}else{
		$(".tishi_box").html("操作失败！");
	}
	$(".tishi_box").show();
	setTimeout("$(\".tishi_box\").hide(1000)", 2000);
	closeBox();
}


