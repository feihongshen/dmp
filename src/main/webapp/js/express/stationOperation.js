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
			refresh();
		}
	});
};

function assignAndExport() {
	if ($("#deliverid").val() == -1) {
		return false;
	}
	// 导出（先执行导出，再执行分配？）
	$('#exportExcelForm').attr('action', $("#export").val());
	$("#exportExcelForm").submit();
	// 分配
	doAssign();
}

function refresh() {
	$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location
			.reload(true);
}

function doSuperzone() {
	if ($("#note").val() == null || $("#note").val() == "") {
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
			refresh();
		},
		complete : function() {
			// addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
};
function showHintInfo(data) {
	if (data) {
		$(".tishi_box").html("操作成功！");
	} else {
		$(".tishi_box").html("操作失败！");
	}
	$(".tishi_box").show();
	setTimeout("$(\".tishi_box\").hide(1000)", 2000);
	closeBox();
};

function removeWaybillNo(event) {
	var waybillNo = $("#waybillNo").val();
	if (event.keyCode == 13 && waybillNo.length > 0) {
		var waybillNos = $("#waybillNos").val();
		var waybillNoArray = waybillNos.split(',');
		var leftWaybillNos = "";
		var findWaybillNo = false;
		for (var i = 0; i < waybillNoArray.length; i++) {
			if (waybillNo !== waybillNoArray[i]) {
				leftWaybillNos = leftWaybillNos + "," + waybillNoArray[i];
			} else {
				findWaybillNo = true;
			}
		}
		if (findWaybillNo) {
			$("#waybillNos").attr("value", leftWaybillNos);
			$("#waybillTotalCount").attr("value",
					parseInt($("#waybillTotalCount").val()) - 1);
			setItemTotalCountByWaybillNo(waybillNo);
			refreshRemoveDlg(leftWaybillNos);
		} else {
			$(".tishi_box").html("您要移除的运单号不在合包集合中！");
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
		}
		// 清空运单号
		$("#waybillNo").val("");
	}
};

function refreshRemoveDlg(waybillNos) {
	$.ajax({
		type : "POST",
		url : $("#remove").val(),
		dataType : "html",
		data : {
			"packageNo" : $("#packageNo").val(),
			"nextBranch" : $("#nextBranch").val(),
			"provinceId" : $("#provinceId").val(),
			"waybillNos" : waybillNos,
			"waybillTotalCount" : $("#waybillTotalCount").val(),
			"itemTotalCount" : $("#itemTotalCount").val()
		},
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			viewBox();
		}
	});
};

function setItemTotalCountByWaybillNo(waybillNo) {
	$.ajax({
		type : "POST",
		url : $("#getExpressOrderByWaybillNo").val(),
		async: false,
		data : {
			"waybillNo" : waybillNo
		},
		success : function(data) {
			var itemCount = data.cwbOrder.sendcarnum;
			$("#itemTotalCount").attr("value",
					parseInt($("#itemTotalCount").val()) - itemCount);
		}
	});
};

function closeDlgAndRefresh() {
	closeBox();
	var url = $("#doRemove").val() + "?packageNo=" + $("#packageNo").val()
			+ "&nextBranch=" + $("#nextBranch").val() + "&provinceId="
			+ $("#provinceId").val() + "&selectedCities="
			+ $("#selectedCities").val() + "&leftWaybillNos="
			+ $("#waybillNos").val() + "&waybillTotalCount="
			+ $("#waybillTotalCount").val() + "&itemTotalCount="
			+ $("#itemTotalCount").val();
	$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.href = url;
};

