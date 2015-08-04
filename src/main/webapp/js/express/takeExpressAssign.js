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
		},
		complete : function() {
			// addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
};

