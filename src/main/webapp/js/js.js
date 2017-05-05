// JavaScript Document
function isFloat(name) {
	return /^[-+]?[0-9]+(\.[0-9]+)?$/.test(name);
}
function testEmail(str) {
	return /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
}
function isLetterAndNumber(name) {
	return /^[A-Za-z0-9]+$/.test(name);
}
function isNumber(name) {
	return /^[0-9]+$/.test(name);
}

function isAll(name) {
	return /^[A-Za-z0-9\u4e00-\u9fa5]+$/.test(name);
}
function isChinese(name) {
	return /^[^[\u4e00-\u9fa5]*]*$/.test(name);
}
function focusCwb() {
	//$("#WORK_AREA")[0].contentWindow.focusCwb();
	$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

}

function isPhoneNumber(number) {
	return /^1[3|4|5|8][0-9]\d{4,8}$/.test(number);
}
function isMobileNumber(number) {
	return /^1[34578]\d{9}$/.test(number);
}
/* 按钮滑过样式 */
$(function() {
	$(".inputselect_box3 :input").hover(function() {
		$(this).css("background-position", "left -740px");
	}, function() {
		$(this).css("background-position", "left -680px");
	});
});

// 窗口改变刷新
function centerBox() {
	var $scroll_hei = document.body.scrollHeight;
	var $scroll_wd = document.body.scrollWidth;
	var $see_height = document.documentElement.clientHeight;
	var $see_width = document.documentElement.clientWidth;
	// 底部信息位置
	$("#box_bg,.boxbg").css("height", $see_height);
	if ($("#box_form").height() > 500) {
		$("#box_form").css("height", "500px");
		$("#box_form").css("overflow", "auto");
	}
	$tcbox_height = $("#box_contant,.box_contant").height();
	$tcbox_width = $("#box_contant,.box_contant").width();
	$("#box_contant,.box_contant").css("top", ($see_height / 2) - ($tcbox_height / 2));
	$("#box_contant,.box_contant").css("left", ($see_width / 2) - ($tcbox_width / 2));

	$tcbox_height = $(".box_contant2").height();
	$tcbox_width = $(".box_contant2").width();
	$(".box_contant2").css("top", ($see_height / 2) - ($tcbox_height / 2));
	$(".box_contant2").css("left", ($see_width / 2) - ($tcbox_width / 2));
}
// 窗口改变刷新
function centerBox1() {
	var $see_height = document.documentElement.clientHeight;
	var $see_width = document.documentElement.clientWidth;
	// 底部信息位置
	$("#box_bg1,.boxbg").css("height", $see_height);
	if ($("#box_form").height() > 500) {
		$("#box_form").css("height", "500px");
		$("#box_form").css("overflow", "auto");
	}
	$tcbox_height = $("#box_contant1,.box_contant").height();
	$tcbox_width = $("#box_contant1,.box_contant").width();
	$("#box_contant1,.box_contant").css("top", ($see_height / 2) - ($tcbox_height / 2));
	$("#box_contant1,.box_contant").css("left", ($see_width / 2) - ($tcbox_width / 2));
	
/*	$tcbox_height = $(".box_contant2").height();
	$tcbox_width = $(".box_contant2").width();
	$(".box_contant2").css("top", ($see_height / 2) - ($tcbox_height / 2));
	$(".box_contant2").css("left", ($see_width / 2) - ($tcbox_width / 2));*/
}
// 根据滚动条滚动时间刷新漂浮框的xy轴
function reIframeTopAndBottomXY() {
	var st = document.documentElement.scrollTop;
	if (st == 0) {
		st = document.body.scrollTop;
	}
	var ch = document.documentElement.clientHeight;
	if (ch == 0) {
		ch = document.body.clientHeight;
	}

	if ($(".kf_content2")) {
		$(".kf_content").scroll(function() {
			var divwidth = document.getElementById("kf_divwidth").scrollLeft;
			// 滚轴高度
			var divwidthmax = document.getElementById("kf_divwidth").scrollWidth;
			// 滚轴最大值
			var ct2 = $(".kf_content2").width();
			// 中间容器宽度
			var ct = $(".kf_content").width();

			if (ct2 < ct2 + divwidth) {
				$(".kf_content2").css("width", ct + divwidth);
			} else if (divwidth == 0) {
				$(".kf_content2").css("width", ct);
			}

		});
		$(".kf_content2").css("height", st + ch - 70);
	}
	if ($(".kf_content")) {
		$(".kf_content").css("height", st + ch - 70);
	}

	if ($(".iframe_bottom")) {
		$(".iframe_bottom").css("top", st + ch - 40);
	}
	if ($(".iframe_bottom2")) {
		$(".iframe_bottom2").css("top", st + ch - 65);
	}
	if ($("#scroll")) {
		$("#scroll").css("height", st + ch - 130);
	}
	if ($(".form_btnbox")) {
		$(".form_btnbox").css("top", st + ch - 75);
	}
	if ($(".form_btnbox2")) {
		$(".form_btnbox2").css("top", st + ch - 40);
	}
	if ($(".uc_midbg,.kfsh_tabbtn,.kf_listtop,.list_topbar,.inputselect_box")) {
		$(".uc_midbg,.kfsh_tabbtn,.kf_listtop,.list_topbar,.inputselect_box").css("top", st);
	}

}
$(window).resize(function() {
	centerBox();
	reIframeTopAndBottomXY();
});
$(window).scroll(function(event) {
	reIframeTopAndBottomXY();
});
$(window).load(function(event) {
	reIframeTopAndBottomXY();
});

$(function() {

	$("table#gd_table tr:odd").css("backgroundColor", "#f9fcfd");
	$("table#gd_table tr:odd").hover(function() {
		$(this).css("backgroundColor", "#fff9ed");
	}, function() {
		$(this).css("backgroundColor", "#f9fcfd");
	});
	$("table#gd_table tr:even").hover(function() {
		$(this).css("backgroundColor", "#fff9ed");
	}, function() {
		$(this).css("backgroundColor", "#fff");
	});// 表单颜色交替和鼠标滑过高亮

	$(".index_dh li").mouseover(function() {
		// $(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function() {

		$(".menu_box").hide();
	});

});

function edit_button(key) {
	getEditBox(key);
}

function closeBox() {
	$("#alert_box").hide(300);
	$("#dress_box").css("visibility", "");
	$(".alert_box").hide(300);
	$(".dress_box").css("visibility", "");
}

function viewBox() {
	// $(window).keydown(function(event){
	// switch(event.keyCode) {
	// case (event.keyCode=27):window.parent.closeBox();break;
	// }
	// });
	$("#alert_box", parent.document).show();
	$("#dress_box", parent.document).css("visibility", "hidden");
	window.parent.centerBox();
}

function getEditBox(key) {
	$.ajax({
		type : "POST",
		url : $("#edit").val() + key,
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);
			
		},
		complete : function() {
			editInit();// 初始化ajax弹出页面
			viewBox();
			//////////////      liumurong
			if($('#isypdjusetranscwb',parent.document).val() == "0"){
				//一票多件用运单号如果为否，那么集单为否并且不可修改。
				$("#ifjidan",parent.document).attr("disabled","disabled");
				$('#ifjidan',parent.document).val("0");
			}		
			// 有其他需求可以参阅 transcwbswitch() 这个方法
			////////////////////////
		}
	});
}
function getViewBox(key) {
	$.ajax({
		type : "POST",
		url : $("#view").val() + key,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
			viewBox();
		}
	});
}
function submitCreateForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$(form)[0].reset();
				//$("#WORK_AREA")[0].contentWindow.addSuccess(data);  
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
			}
		}
	});
}


function submitCreateFormAndCloseBox(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				//$("#WORK_AREA")[0].contentWindow.addSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

				closeBox();
			}
		}
	});
}

function submitSaveForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				//$("#WORK_AREA")[0].contentWindow.editSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				closeBox();
			}
		}
	});
}

/**
 * 
 * @description 通联接口参数设置时的保存方法
 * @author 刘武强
 * @date  2015年12月14日上午9:49:26
 * @param  @param form
 * @return  void
 */
function tongLianSaveForm(formName) {
	var formData = new FormData($("#"+formName)[0]);
	$.ajax({
		type : "POST",
		url : $("#savePath").val(),
		data: formData, 
		dataType : "json",
        async: false,  
        cache: false,  
        contentType: false,  
        processData: false,  
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				closeBox();
			}
		}
	});
}


function submitChangeCwbSaveForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			if (data.errorCode != 0) {
				alert(data.error);
			}
			resetform();
		}
	});
}

function submitSaveFormAndCloseBox(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			console.info(data.errorCode);
			if (data.errorCode == 0) {
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				closeBox();
				$("#WORK_AREA")[0].contentWindow.refreshState();
			}
		}
	});
}
function submitSaveFormAndCloseBox4Shangmentui(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			console.info(data.errorCode);
			if (data.errorCode == 0) {
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				closeBox();
				$("#WORK_AREA")[0].contentWindow.refreshState();
			}else{
				$('#sub').val('保存');
				$('#sub').removeAttr("disabled");
			}
		}
	});
}
function yichuli1() {
	$("#describe2").val($("#describe").val());
	// alert($("#describe2").val());
	// $("#form2").submit();
	$.ajax({
		type : "POST",
		url : $("#form2").attr("action"),
		data : $("#form2").serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$("#WORK_AREA")[0].contentWindow.editSuccess(data);
				closeBox();
			}
		}
	});
}

// 归班审核功能，有使用小件员交款功能时使用
function submitAuditAndCloseBox(form) {
	var radioArray = $("input[name='deliverpayuptype']");
	var deliverpayuptypeVal = 0;

	for (var i = 0; i < radioArray.length; i++) {
		if (radioArray[i].checked) {
			deliverpayuptypeVal = radioArray[i].value;
		}
	}
	if (deliverpayuptypeVal == 0) {
		alert("请选择交款方式！");
		return false;
	}
	if (!isFloat($("#deliverpayupamount").val())) {
		alert("应交金额输入的数字格式不正确！");
		return false;
	}
	if (!isFloat($("#deliverpayupamount_pos").val())) {
		alert("用户POS刷卡金额输入的数字格式不正确！");
		return false;
	}
	$('#send_addcityuser').attr('disabled', 'disabled');
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

				//$("#WORK_AREA")[0].contentWindow.editSuccess(data);
				closeBox();
			}
		}
	});
}
// 归班审核功能，有使用小件员交罚款功能时使用
function submitAuditArrearageAndCloseBox() {
	$('#send_addcityuser').attr('disabled', 'disabled');
	closeBox();
}
// 归班审核功能////////END/////

function del(key) {
	$.ajax({
		type : "POST",
		url : $("#del").val() + key,
		dataType : "json",
		success : function(data) {
			// alert(data.error);
			if (data.errorCode == 0) {
				delSuccess(data);
			}
			if (data.errorCode == 1) {
				alert(data.error);
			}
		}
	});
}
// 上传文件初始化上传组件
function uploadFormInit(form, contextPath) {
	$('#swfupload-control').swfupload({
		upload_url : $("#" + form, parent.document).attr("action"),
		file_size_limit : "10240",
		file_types : "*.wav;*.WAV",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : contextPath + "/js/swfupload/swfupload.swf",
		button_image_url : contextPath + "/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#upbutton")[0]
	}).bind('fileQueued', function(event, file) {
		$("#wavText").val(file.name);
		$("#wavText").attr("selectedUploadFile", true);
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {
		/*
		 * 进度条 var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		 * $("#progressbar").progressbar({ value : percent });
		 * $("#progressstatus").text(percent);
		 */

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		// $("#wavText").val("");
		if (dataObj.errorCode == 0) {
			if (dataObj.type == "add") {
//				$("#WORK_AREA", parent.document)[0].contentWindow.addSuccess(dataObj);
				$("#box_contant", parent.document).hide(300);
				$("#alert_box", parent.document).css("display", "");
				$("#sub", parent.document).removeAttr("disabled");
				$("#sub", parent.document).val("确认");
			} else if (dataObj.type == "edit") {
//				$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
				$("#box_contant", parent.document).hide(300);
				$("#alert_box", parent.document).css("display", "");
				$("#sub", parent.document).removeAttr("disabled");
				$("#sub", parent.document).val("保存");
			}
		}
		// setTimeout(queryProgress, 10);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}

//上传文件初始化上传组件-通联
function uploadFormTonglianInit(name,form,btname,contextPath) {
	$('#swfupload-control').swfupload({
		upload_url : $("#" + form, parent.document).attr("action")+"?type="+name,
		file_size_limit : "10240",
		file_types : "*.p12;*.cer",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : contextPath + "/js/swfupload/swfupload.swf",
		button_image_url : contextPath + "/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#"+btname)[0]
	}).bind('fileQueued', function(event, file) {
		$("#"+name).val(file.name);
		$("#"+name).attr("selectedUploadFile", true);
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {
		/*
		 * 进度条 var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		 * $("#progressbar").progressbar({ value : percent });
		 * $("#progressstatus").text(percent);
		 */

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		// $("#wavText").val("");
		if (dataObj.errorCode == 0) {
			if (dataObj.type == "add") {
				$("#WORK_AREA", parent.document)[0].contentWindow.addSuccess(dataObj);
				$("#sub", parent.document).removeAttr("disabled");
				$("#sub", parent.document).val("确认");
			} else if (dataObj.type == "edit") {
				$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
				$("#sub", parent.document).removeAttr("disabled");
				$("#sub", parent.document).val("保存");
			}
		}
		// setTimeout(queryProgress, 10);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}

// ///////////订单类型设置////////////////
function check_cwbOrderType() {
	if ($("#importtypeid").val() < 0) {
		alert("请选择类型");
		return false;
	}
	return true;
}
// ///////////订单类型设置 END////////////////
// ///////////导入设置////////////////
function check_excelcolumn() {
	if ($("#customerid").val() == 0) {
		alert("请选择供货商！");
		return false;
	} else if ($("#customerid").val().split("-")[1] == 0 && $("#cwbindex").val() == 0) {
		alert("请选择表格中对应条码号/订单号的列！");
		return false;
	} else if ($("#cwbordertypeindex").val() == 0) {
		alert("请选择表格中对应订单类型的列！");
		return false;
	}/*
		 * else if($("#consigneenameindex").val()==0){
		 * alert("请选择表格中对应收件人姓名的列！"); return false; }
		 */else if ($("#consigneeaddressindex").val() == 0) {
		alert("请选择表格中对应收件人地址的列！");
		return false;
	} else if ($("#cargoamountindex").val() == 0) {
		alert("请选择表格中对应货物金额的列！");
		return false;
	}

	return true;
}
// ///////////导入设置 END////////////////
// ///////////车辆设置////////////////
function isCheckNumber(name) {
	return /^[0-9\.]+$/.test(name);
}
function check_truck() {
	if ($("#truckno").val().length == 0) {
		alert("车牌号不能为空");
		return false;
	}
	return true;
}
// ///////////车辆设置END////////////////
// ///////////机构管理////////////////
// 弹出框弹出时进行初始化
function init_branch() {
	$("#branchname").parent().hide();
	$("#branchcode").parent().hide();
	$("#branchphone").parent().hide();
	// $("#accountarea").parent().hide();
	$("#branchprovince").parent().hide();
	$("#branchcity").parent().hide();
	$("#brancharea").parent().hide();
	$("#branchstreet").parent().hide();
	$("#branchaddress").parent().hide();
	$("#branchmobile").parent().hide();
	$("#branchemail").parent().hide();
	$("#remandtype").parent().hide();
	$("#branchmatter").parent().hide();
	$("#contractflag").parent().hide();
	$("#pfruleid").parent().hide();
	$("#bankcard").parent().hide();
	$("#zhongzhuanid").parent().hide();
	$("#tuihuoid").parent().hide();
	$("#caiwuid").parent().hide();
	$(".zhandian").parent().hide();
	$(".kefu").parent().hide();
	$(".yunying").parent().hide();
	$("#pda_title").hide();
	$("#pda").hide();
	$("#insert").parent().hide();
	$("#wav").hide();
	$("#bindmsksid").parent().hide();
	$("#branchBail").parent().hide();
	
	//自动核销的组件隐藏
	$("#tl").parent().hide();
	$("#cft").parent().hide();
	
	//通联
	$("#bankCardNo").parent().hide();
	$("#bankCode").parent().hide();
	$("#ownerName").parent().hide();
	$("#bankAccountType").parent().hide();
	$("#bankCode").combobox();
	//财付通
	$("#cftAccountNo").parent().hide();
	$("#cftBankCode").parent().hide();
	$("#cftAccountName").parent().hide();
	$("#cftAccountProp").parent().hide();
	$("#cftBankCode").combobox();
	$("#cftCertId").parent().hide();
	$("#cftCertType").parent().hide();
	//站点缴款方式
	$('#payinType').parent().hide();
}
function gonggongObj_branch() {
	$("#branchname").parent().show();
	$("#branchphone").parent().show();
	$("#branchcontactman").parent().show();
	$("#insert").parent().show();

}
function payMthodchange(objId){
	if(objId == "tl"){//通联显示，	财付通隐藏
		$("#bankCardNo").parent().show();
		$("#bankCode").parent().show();
		$("#ownerName").parent().show();
		$("#bankAccountType").parent().show();
	
		$("#cftAccountNo").parent().hide();
		$("#cftBankCode").parent().hide();
		$("#cftAccountName").parent().hide();
		$("#cftAccountProp").parent().hide();
		$("#cftCertId").parent().hide();
		$("#cftCertType").parent().hide();
	}else if(objId == "cft"){//财付通显示，通联隐藏
		$("#cftAccountNo").parent().show();
		$("#cftBankCode").parent().show();
		$("#cftAccountName").parent().show();
		$("#cftAccountProp").parent().show();
		$("#cftCertId").parent().show();
		$("#cftCertType").parent().show();
		
		$("#bankCardNo").parent().hide();
		$("#bankCode").parent().hide();
		$("#ownerName").parent().hide();
		$("#bankAccountType").parent().hide();		
	}
}
function zhandianObj() {
	gonggongObj_branch();
	// $("#accountarea").parent().show();
	$("#branchcode").parent().show();
	$("#branchprovince").parent().show();
	$("#branchcity").parent().show();
	$("#brancharea").parent().show();
	$("#branchstreet").parent().show();
	$("#branchaddress").parent().show();
	$("#branchmobile").parent().show();
	$("#branchemail").parent().show();
	$("#contractflag").parent().show();
	$("#pfruleid").parent().show();
	$("#bankcard").parent().show();
	$("#zhongzhuanid").parent().show();
	$("#tuihuoid").parent().show();
	$("#caiwuid").parent().show();
	$("#branchBail").parent().show();
	// $("#remandtype").parent().show();
	$("#pda_title").show();
	$("#pda").show();
	$("#wav").show();
	$(".zhandian").parent().show();
	$("#bindmsksid").parent().show();
	//自动核销站点信息组件
	$("#tl").parent().show();
	$("#cft").parent().show();
	//通联
	$("#bankCardNo").parent().show();
	$("#bankCode").parent().show();
	$("#ownerName").parent().show();
	$("#bankAccountType").parent().show();
	//站点缴款方式
	$('#payinType').parent().show();
}
function yunyingObj() {
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	$(".yunying").parent().show();
	$("#branchmobile").parent().show();
	$("#branchcode").val('');
}
function kefuObj() {
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	$("#branchmobile").parent().show();
	$(".kefu").parent().show();
	$("#branchcode").val('');
}
function caiwuObj() {
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	$("#branchmobile").parent().show();
	// $("#swfupload-control").parent().show();
	$("#branchcode").val('');
}
function qitaObj() {
	gonggongObj_branch();
	$("#branchprovince").parent().show();
	$("#branchmobile").parent().show();
	$("#branchaddress").parent().show();
	$("#remandtype").parent().show();
	$("#pda_title").show();
	$("#pda").show();
	$("#branchcode").val('');
	// $("#wav").show();
}
function checkGongGong_branch() {
	if ($("#branchname").val().length == 0) {
		alert("机构名称不能为空");
		return false;
	}
	if (!isAll($("#branchname").val())) {
		alert("机构名称不能含有非法字符");
		return false;
	}
	if ($("#branchcontactman").val().length == 0) {
		alert("负责人不能为空");
		return false;
	}
	if (!isAll($("#branchcontactman").val())) {
		alert("负责人不能含有非法字符");
		return false;
	}
	return true;
}
function checkQiTa() {
	if (!checkGongGong_branch()) {
		return false;
	} else if ($("#remandtype").val() == 0) {
		alert("请选择拣线提示方式");
		return false;
	}
	// 18对应了枚举中的语音提醒的值
	if (!checkWav()) {
		return false;
	}
	return true;
}
function checkWav() {
	if (!$("#wav").is(":hidden")) {
		if ($("#update").contents().find("#wavText").val().length > 4
				&& $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length - 4) != ".wav"
				&& $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length - 4) != ".WAV") {
			alert("声音文件只能选择wav格式，如xxx.wav");
			return false;
		}
		// if($("#wavText").val().length==0 &&(
		// $("#wavh").length==0||$("#wavh").val().length==0)){
		// alert("请选择上传文件");
		// return false;
		// }
	}
	return true;
}
function checkZhanDian() {
	if (!checkGongGong_branch()) {
		return false;
	}
	if ($("#branchcode").val().length == 0) {
		alert("站点编号不能为空");
		return false;
	}
	if ($("#tpsbranchcode").val().length == 0) {
		alert("机构编码不能为空");
		return false;
	}
	/*
	 * if($("#accounttype").val()==0){ alert("请选择结算类型"); return false; }
	 * if($("#accountbranch").val()==0){ alert("请选择结算对象"); return false; }
	 * if($("#accountexcesstype").val()==0){ alert("请选择超额设置"); return false; }
	 * if($("#accountexcessfee").val().length==0){ alert("超额金额不能为空"); return
	 * false; } if($("#credit").val().length==0){ alert("信誉额度不能为空"); return
	 * false; }
	 */

	if ($("#zhongzhuanid").val() == 0) {
		alert("请选择此站点对应的中转站");
		return false;
	} else if ($("#tuihuoid").val() == 0) {
		alert("请选择此站点对应的退货站");
		return false;
	} else if ($("#caiwuid").val() == 0) {
		alert("请选择此站点对应的上交款财务");
		return false;
	} else if (!testEmail($("#branchemail").val())) {
		alert("Email格式不正确");
		return false;
	}
	if (!checkWav()) {
		return false;
	}
	return true;
}
function checkKeTuiCai() {
	if (!checkGongGong_branch()) {
		return false;
	}
	if (!testEmail($("#branchemail").val())) {
		alert("Email格式不正确");
		return false;
	}
	return true;
}
// 验证机构字段是否正确输入
function check_branch(ZhanDian, YunYing, KeFu, CaiWu) {
	var sitetype = $("#sitetype").val();
	if (sitetype == -1) {
		return false;
	}
	if (sitetype == ZhanDian) {// 站点
		return checkZhanDian();
	} else if (sitetype == YunYing) {// 运营
		return checkKeTuiCai();
	} else if (sitetype == KeFu) {// 客服
		return checkKeTuiCai();
	} else if (sitetype == CaiWu) {// 财务
		return checkKeTuiCai();
	} else {// 库房 中转站 退货站 其他
		return checkQiTa();
	}
	return true;
}
// 监控机构类型变化 对显示字段做相应处理
function click_sitetype(ZhanDian, YunYing, KeFu, CaiWu) {
	var sitetype = $("#sitetype").val();
	init_branch();
	// 库房 中转站 退货站 其他
	if (sitetype == ZhanDian) {// 站点
		zhandianObj();
	} else if (sitetype == YunYing) {// 运营
		yunyingObj();
	} else if (sitetype == KeFu) {// 客服
		kefuObj();
	} else if (sitetype == CaiWu) {// 财务
		caiwuObj();
	} else {
		qitaObj();
	}
	// $("#remandtype").val(0);
	centerBox();
}

// 创建机构提交
function submitAddBranch(form) {

	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "branch/create");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitBranchLoad(form);
}

function submitAddCustomer(form) {
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "customer/create");
		submitCreateForm(form);
		return;
	}
	$("#update")[0].contentWindow.submitCustomerLoad();
}

var selectedUploadFile = false;

function submitEditCustomer(form, id) {
	var $wavText = $("#update").contents().find("#wavText");
	var selectedUploadFile = $wavText.attr("selectedUploadFile");
	if (selectedUploadFile) {
		$("#update")[0].contentWindow.submitEditCustomerLoad();
	} else {
		$(form).attr("enctype", "");
		$(form).attr("action", "customer/save/" + id);
		submitSaveForm(form);
		return;
	}
}

// 修改机构提交
function submitEditBranch(form, key) {
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "branch/save/" + key);
		submitSaveForm(form);
		return;
	}
	$("#update")[0].contentWindow.submitBranchLoad(form);
}

function initBranch(functionids) {
	$("#cb_" + functionids).attr("checked", "checked");
}

// 创建和修改时 如果上传声音文件 泽通过 swfupload插件上传
function submitBranchLoad(form) {
	$("#sub", parent.document).attr("disabled", "disabled");
	$("#sub", parent.document).val("保存中...");
	var attrMap = getAttrMapByForm(form);
	
	$('#swfupload-control').swfupload('addPostParam', 'sitetype', $("#sitetype", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchname', $("#branchname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchcode', $("#branchcode", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'tpsbranchcode', $("#tpsbranchcode", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchBail', $("#branchBail", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchcontactman', $("#branchcontactman", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchphone', $("#branchphone", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchmobile', $("#branchmobile", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchprovince', $("#branchprovince", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchcity', $("#branchcity", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'brancharea', $("#brancharea", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchstreet', $("#branchstreet", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchaddress', $("#branchaddress", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchemail', $("#branchemail", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'accounttype', $("#accounttype", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'accountbranch', $("#accountbranch", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'accountexcessfee', $("#accountexcessfee", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'accountexcesstype', $("#accountexcesstype", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'credit', $("#credit", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'prescription24', $("#prescription24", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'prescription48', $("#prescription48", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'backtime', $("#backtime", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'contractflag', $("#contractflag", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'pfruleid', $("#pfruleid", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'bindmsksid', $("#bindmsksid", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'bankcard', $("#bankcard", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'zhongzhuanid', $("#zhongzhuanid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'tuihuoid', $("#tuihuoid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'caiwuid', $("#caiwuid", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'remandtype', $("#remandtype", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'wavh', $("#wavh", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'payMethodType', $("#input[name='payMethodType']", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'branchmatter', $("#branchmatter", parent.document).val());
	
	//自动核销字段的获取--通联
	$('#swfupload-control').swfupload('addPostParam', 'bankCardNo', $("#bankCardNo", parent.document).val());
//	$('#swfupload-control').swfupload('addPostParam', 'bankCode', $("#bankCode", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'bankCode', getValueByNameFromAttrMap("bankCode", attrMap));
	$('#swfupload-control').swfupload('addPostParam', 'ownerName', $("#ownerName", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'bankAccountType', $("#bankAccountType", parent.document).val());
	
	//自动核销字段的获取--财付通
	$('#swfupload-control').swfupload('addPostParam', 'cftAccountNo', $("#cftAccountNo", parent.document).val());
//	$('#swfupload-control').swfupload('addPostParam', 'cftBankCode', $("#cftBankCode", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cftBankCode', getValueByNameFromAttrMap("cftBankCode", attrMap));
	$('#swfupload-control').swfupload('addPostParam', 'cftAccountName', $("#cftAccountName", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cftAccountProp', $("#cftAccountProp", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cftCertId', $("#cftCertId", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cftCertType', $("#cftCertType", parent.document).val());
	
	$('#swfupload-control').swfupload('addPostParam', 'branchid', $("#branchid", parent.document).val());
	//自动化分拣需要的出货口编号
	$('#swfupload-control').swfupload('addPostParam', 'outputno', $("#outputno", parent.document).val());
	
	var checkedValues = new Array();
	$('input[name="functionids"]:checked', parent.document).each(function() {
		checkedValues.push($(this).val());
	});
	$('#swfupload-control').swfupload('addPostParam', 'functionids', checkedValues);
	
	$('#swfupload-control').swfupload('startUpload');
}

function getAttrMapByForm(form){
	var attrMap = new Array();
	if(!!form) {
		var serializedParams = $(form).serializeArray();
		for(var i=0; i<serializedParams.length; i++){
			var param = serializedParams[i];
			attrMap[param["name"]] = param["value"];
		}
	}
	return attrMap;
}

function getValueByNameFromAttrMap(name, attrMap){
	var value = "";
	if(attrMap != null && attrMap instanceof Array) {
		value = attrMap[name]; 
	}  
	if (value == null || value == ""){
		value = $("#"+name, parent.document).val();
	}
	return value;
}

function submitCustomerLoad() {
	$("#sub", parent.document).attr("disabled", "disabled");
	$("#sub", parent.document).val("保存中...");
	var formVO = getFormValueObject($("#customer_cre_Form", parent.document));
	var $control = $('#swfupload-control');
	for ( var name in formVO) {
		$control.swfupload('addPostParam', name, formVO[name]);
	}
	$('#swfupload-control').swfupload('startUpload');
}

function submitEditCustomerLoad() {
	$("#sub", parent.document).attr("disabled", "disabled");
	$("#sub", parent.document).val("保存中...");
	var formVO = getFormValueObject($("#customer_save_Form", parent.document));
	var $control = $('#swfupload-control');
	for ( var name in formVO) {
		$control.swfupload('addPostParam', name, formVO[name]);
	}
	$('#swfupload-control').swfupload('startUpload');
}

function getFormValueObject($form) {
	var obj = {};
	$form.find("input,select").each(function() {
		var $this = $(this);
		var name = $this.attr("name");
		if (name == undefined) {
			return;
		}
		var value = $this.val();
		obj[name] = value;
	});
	return obj;
}

// ///////////机构管理 END////////////////

// ///////////常用语设置////////////////
function check_reason() {
	if ($("#reasontype").val() == 0) {
		alert("请选择类型");
		return false;
	}
	if ($("#reasontype").val() == 1&&$("#radio2").attr("checked") == "checked"&&$("#parentid").val()==0) {
		alert("请选择一级原因");
		return false;
	}
	
	if ($("#reasontype").val() == 15 && $("#intercept_1").attr("checked") != "checked" && $("#intercept_2").attr("checked") != "checked" && $("#intercept_3").attr("checked") != "checked") {
		alert("请根据填写的具体内容描述标识其含义");
		return false;
	}
	if ($("#reasoncontent").val().length == 0) {
		alert("常用语不能为空");
		return false;
	}
	return true;
}

// /////////常用语设置END////////////////

// /////////供货商设置////////////////
function check_customer() {
	if ($("#customername").val().length == 0) {
		alert("供货商名称不能为空");
		return false;
	} else if ($("#companyname").val().length == 0) {
		alert("公司名称不能为空");
		return false;
	} else if ($("#paytype").val() == 0) {
		alert("结算类型不能为空");
		return false;
	} else if ($("#customercode").val() == 0) {
		alert("供货商编码不能为空");
		return false;
	} else if ($("#isAutoProductcwb").val() == 1) {
		var temp = $("#autoProductcwbpre").val().replace(/(^\s*)|(\s*$)/g, "");
		if (temp.length == 0) {
			alert("订单号前缀不能为空或空格");
			return false;
		}
		if (temp.indexOf(",") > -1 || temp.indexOf(":") > -1 || temp.indexOf("，") > -1 || temp.indexOf("：") > -1) {
			alert("订单号前缀不能包含分号和逗号");
			return false;
		}
		/*
		 * if(! new
		 * RegExp(/^[a-zA-Z0-9-*_]+$/).test($("#autoProductcwbpre").val())){
		 * alert("格式不对!不能含有非法字符！"); return false; }
		 * if($("#autoProductcwbpre").val().length>6){ alert("订单号前缀不超过6位");
		 * return false; }
		 */
	}else if($('#ifjidan').val() == "1"){
		if($('input:radio[name="mpsswitch"]:checked').val() == null && $('input:radio[name="mpsswitch"]:checked').val() == null){
			alert("当前您已启用集单模式，请根据业务情况选择「库房集单」还是「站点集单」");
			return false;
		}
		if($('#isypdjusetranscwb').val() != "1" || $('#isUsetranscwb').val() != "0"){
			alert("当前您已启用集单模式，「一票多件用运单号」与「扫描运单号」均需选择「是」");
			return false;
		}
	}

	return true;
}

// ///////供货商设置////////////////

// ///////供货商仓库设置////////////////
function check_customerwarehouses() {
	if ($("#customerwarehouse").val().length == 0) {
		alert("仓库不能为空");
		return false;
	}
	if ($("#customerid").val() == -1) {
		alert("请选择您的供货商");
		return false;
	}
	if ($("#warehouse_no").val().length == 0) {
		alert("仓库编码不能为空");
		return false;
	}
	return true;
}
// ///////供货商仓库设置END////////////////

// ///////供货商结算区域设置////////////////
function check_accountareas() {
	if ($("#customerid").val() < 0) {
		alert("请选择供货商");
		return false;
	}
	if ($("#areaname").val().length == 0) {
		alert("结算区域不能为空");
		return false;
	}
	return true;
}

// /////供货商结算区域设置END////////////////

// ///////角色权限设置//////////////
function check_role() {
	if ($("#rolename").val().length == 0) {
		alert("角色名称不能为空");
		return false;
	}
	return true;
}

// /////角色权限设置END//////////////
// ///////员工管理//////////////
function checkRealname() {
	if ($("#realname").val().length > 0) {
//		$.ajax({
//			type : "POST",
//			url : "user/userrealnamecheck",
//			data : {
//				realname : $("#realname").val()
//			},
//			success : function(data) {
//				if (data == false)
//					alert("员工姓名已存在");
//				// else alert("员工姓名可用");
//			}
//		});
		return true;
	} else {
		alert("员工姓名不能为空");
	}
}
function crossCapablePDA() {
	if ($("#branchid").val() > -1 && $("#roleid").val() > -1) {
		$.ajax({
			type : "POST",
			url : "user/crossCapablePDA",
			data : {
				branchid : $("#branchid").val(),
				roleid : $("#roleid").val()
			},
			success : function(data) {
				$("#pda").html(data);
			}
		});
	}
}
function checkUsername() {
	if ($("#username").val().length == 0) {
		alert("员工登录名不能为空");
		return;
	}
	if (!isLetterAndNumber($("#username").val())) {
		alert("员工登录名格式不正确");
		return;
	}
	$.ajax({
		type : "POST",
		url : "user/usercheck",
		data : {
			username : $("#username").val()
		},
		success : function(data) {
			if (data == false)
				alert("员工登录名已存在");
			// else alert("员工登录名可用");
		}
	});
}

/**
 * 用户管理机构变化
 * @param userJobnumIsNeed
 * @returns {Boolean}
 */
function branchChange(branchid){	
	var isCheckJobnum = false;
	branchid = branchid || $("#branchid").val();	
	if("-1" != branchid){
		$.ajax({
			url:"user/isCheckJobnum",
			async:false,
			data:{"branchid": branchid},
			dataType: "json",
			success:function(data){
				isCheckJobnum = data;
			}
		});
	}
	
	
	$('#userJobnumIsNeed').val(isCheckJobnum);
	if(isCheckJobnum){
		$("#showJobNumNeed").show();
	} else {
		$("#showJobNumNeed").hide();
	}
}

function check_user(userJobnumIsNeed) {
	//var checkuserformid = "user_cre_Form";
	roleChange();
	if ($("#realname").val().length == 0) {
		alert("员工姓名不能为空!");
		return false;
	}
	if ($("#branchid").val() == -1) {
		alert("请选择员工所在机构");
		return false;
	}
	if ($("#roleid").val() == -1) {
		alert("请选择员工对应的角色");
		return false;
	}
	/*
	 * if ($("#showphoneflag").val() == -1) { alert("请选择员工 订单电话/手机是否可见"); return
	 * false; }
	 */
	if ($("#update").contents().find("#wavText").val().length > 4
			&& $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length - 4) != ".wav"
			&& $("#update").contents().find("#wavText").val().substring($("#wavText").val().length - 4) != ".WAV") {
		alert("员工声音文件只能选择wav格式，如xxx.wav");
		return false;
	}
	if ($("#username").val().length == 0) {
		alert("员工登录名不能为空");
		return false;
	}
	
	if (!isLetterAndNumber($("#username").val())) {
		alert("员工登录名格式不正确");
		return false;
	}
	
	// 网页登录密码, start
	if ($("#webPassword").val().length == 0) {
		alert("网页登录密码不能为空");
		return false;
	}
	if ($("#webPassword1").val() != $("#webPassword").val()) {
		alert("网页登录密码两次输入不一致");
		return false;
	}
	if(!isPasswordValidForWeb($('#webPassword').val())){
		alert("网页登录密码至少要含小写字母、大写字母、数字、特殊字符中的任意三种，且长度至少需要八位");
		return false;
	}
	if(!isUsernamePasswordNotContainEachOther($('#username').val(), $('#webPassword').val())){
		alert("用户名与网页登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
		return false;
	}
	// 网页登录密码, end
	// POS登录密码, start
	if ($("#roleid").val() == 2 || $("#roleid").val() == 4){	// 小件员、站长可的POS登录密码为必填
		if ($("#password").val().length == 0) {
			alert("POS登录密码不能为空");
			return false;
		}
		if ($("#password1").val() != $("#password").val()) {
			alert("POS登录密码两次输入不一致");
			return false;
		}
		if(!isPasswordValidForPDA($('#password').val())){
			alert("POS登录密码必须为数字，不能过于简单，至少要含三个不同数字，数字不能为日期，且长度至少需要六位");
			return false;
		}
		if(!isUsernamePasswordNotContainEachOther($('#username').val(), $('#password').val())){
			alert("用户名与POS登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
			return false;
		}
	}
	// POS登录密码, end
		
	if ($("#roleid").val() == 2 || $("#roleid").val() == 4){
		if ($("#username").val().length > 9) {
			alert("小件员/站长登录名长度不能超过9位！");
			return false;
		}
	}
	if ($("#tip").html().length > 0) {
		if ($("#usermobile").val().length == 0) {
			alert("手机号码不能为空!");
			return false;
		//}else if ($("#usermobile").val().length != 11 || isMobileNumber($("#usermobile").val()) == false) {
		}else if ( isNumber($("#usermobile").val()) == false) {
		  alert("手机号码格式有误!"); return false; 
		}
	}
	
	if ($("#idcardno").val().length == 0) {
		alert("身份证号不能为空!");
		return false;
	}
	
	if(userJobnumIsNeed == "true" && !$("#jobnum").val()){
		alert("工号不能为空");
		return false;
	}
	
	var regu = "/[^/d]/g";
	var re = new RegExp(regu);
	if(re.test(s1)){
		
	}
	var creandsave = $("#creandsave").val();
	var checkuserformid;
	if(creandsave==0){
		checkuserformid = "user_cre_Form";
	}else if(creandsave==1){
		checkuserformid = "user_save_Form";
	}
	var s1 = $("#"+checkuserformid+" input[id=maxcutpayment]").val();
	var s2 = $("#"+checkuserformid+" input[id=basicadvance]").val();
	var s3 = $("#"+checkuserformid+" input[id=lateradvance]").val();
	var s4 = $("#"+checkuserformid+" input[id=fixedadvance]").val();
	var s5 = $("#"+checkuserformid+" input[id=basicfee]").val();
	var s6 = $("#"+checkuserformid+" input[id=areafee]").val();
	//re.test(s2)||re.test(s3)||re.test(s4)
	//^[0-9]+[\.][0-9]{0,2}$
	//var regu = "^[0-9].*$";
	var regu = "^[+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
	var re = new RegExp(regu);
	if (s1!=''&&!re.test(s1)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s2!=''&&!re.test(s2)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s3!=''&&!re.test(s3)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s4!=''&&!re.test(s4)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s5!=''&&!re.test(s5)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s6!=''&&!re.test(s6)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	
	return true;
}

function roleChange() {
	$("#tip").html("");
	$("#pdaPwdDiv").css('display','none');
	if ($("#roleid").val() == '2' || $("#roleid").val() == '4') {
		$("#tip").html("*");
		$("#pdaPwdDiv").css('display','');
	}
}
function isPasswordValidForPDA(loginPwd){
	if(loginPwd.length<6){
		return false;
	}
	//必须为纯数字
	if(isNaN(loginPwd)){
		return false;
	}
	if(loginPwd=='123456' || loginPwd=='654321' || loginPwd=='123123' || loginPwd=='321321'){
		return false;
	}
	//至少含三个不同数字
	var numberArray = loginPwd.split("");
	var numberCountMap = new Array();
	for(var i=0; i<numberArray.length; i++){
		var num = numberArray[i];
		if(isNaN(numberCountMap[num])){
			numberCountMap[num] = 0;
		}
		numberCountMap[num] = numberCountMap[num] + 1;
	}
	var diffNumberCount = 0;
	for(index in numberCountMap){
		if(numberCountMap[index] > 0){
			diffNumberCount += 1;
		}
	}
	if(diffNumberCount < 3){
		return false;
	}
	
	//不能为日期（生日）
	if(loginPwd.length==8) {
		var loginPwdInDateStr = loginPwd.substring(0,4) + "-" + loginPwd.substring(4,6) + "-" + loginPwd.substring(6,8);
		var loginPwdInDate = new Date(loginPwdInDateStr);
		if(loginPwdInDate instanceof Date && loginPwdInDate.toDateString() != "Invalid Date") {
			return false;
		}
	}
	return true;
}
function isPasswordValidForWeb(loginPwd){
	if(loginPwd.length<8){
		return false;
	}
	var flag1=false,flag2=false,flag3=false,flag4=false;
	for(var i=0;i<loginPwd.length;i++){
		var asc=loginPwd.charCodeAt(i);
			//判断是否数字
			if(48<=asc&&asc<=57)
				flag1=true;
			//判断是否小写字母
			if(65<=asc&&asc<=90)
				flag2=true;
			//判断是否是大写字母
			if(97<=asc&&asc<=122)
				flag4=true;
			//判断是否字符
			if((33<=asc&&asc<=47)||(58<=asc&&asc<=64)||(91<=asc&&asc<=96)||(123<=asc&&asc<=126))
				flag3=true;
	}
	var count = 0;
	if(flag1 == true){
		count++;
	}
	if(flag2 == true){
		count++;
	}
	if(flag3 == true){
		count++;
	}
	if(flag4 == true){
		count++;
	}
	if(count == 3 || count == 4){
		return true;
	}else{
		return false;
	}
}
function isUsernamePasswordNotContainEachOther(loginUsername, loginPwd){
	var loginUsernameInLowerCase = loginUsername.toLowerCase();
	var loginPwdInLowerCase = loginPwd.toLowerCase();
	var loginPwdInLowerCaseReversed = loginPwdInLowerCase.split("").reverse().join("");
	
	//正序忽略大小写，不能相互包含
	if(loginUsernameInLowerCase.indexOf(loginPwdInLowerCase) >= 0 || loginPwdInLowerCase.indexOf(loginUsernameInLowerCase) >= 0){
		return false;
	}
	//倒序忽略大小 写，不能相互包含
	if(loginUsernameInLowerCase.indexOf(loginPwdInLowerCaseReversed) >= 0 || loginPwdInLowerCaseReversed.indexOf(loginUsernameInLowerCase) >= 0){
		return false;
	}
	
	return true;
}
function submitAddUser(form) {

	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "user/create");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitUserLoad();
}
function submitSaveUser(form, key) {

	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "user/save/" + key);
		submitSaveForm(form, key);
		return;
		
	}
	$("#update")[0].contentWindow.submitUserLoad();

}

// 创建和修改用户时 如果选择上传声音文件 泽通过 swfupload插件上传
function submitUserLoad() {
	$("#sub", parent.document).attr("disabled", "disabled");
	$("#sub", parent.document).val("保存中...");
	$('#swfupload-control').swfupload('addPostParam', 'realname', $("#realname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'branchid', $("#branchid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'showphoneflag', $("#showphoneflag", parent.document).val());

	$('#swfupload-control').swfupload('addPostParam', 'employeestatus', $("#employeestatus", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'idcardno', $("#idcardno", parent.document).val());
	// $$("#update").contents().find('#swfupload-control').swfupload('addPostParam','usersalary',$("#usersalary").val());
	// $('#swfupload-control').swfupload('addPostParam','userphone',$("#userphone").val());
	$('#swfupload-control').swfupload('addPostParam', 'usermobile', $("#usermobile", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'useremail', $("#useremail", parent.document).val());
	// $('#swfupload-control').swfupload('addPostParam','useraddress',$("#useraddress").val());
	// $('#swfupload-control').swfupload('addPostParam','userremark',$("#userremark").val());
	$('#swfupload-control').swfupload('addPostParam', 'username', $("#username", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'password', $("#password", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'password1', $("#password1", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'roleid', $("#roleid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'usercustomerid', $("#usercustomerid", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
// ///////员工管理END//////////////

// ///////超额提示设置//////////////
function check_exceedfee() {
	if (!isFloat($("#exceedfee").val())) {
		alert("输入的数字格式不正确！");
		return false;
	}
	return true;
}
// ///////超额提示设置END//////////////

// ///////设置权限//////////////
function getEditRoleAndMenu(key) {
	$.ajax({
		type : "POST",
		url : $("#editRoleAndMenu").val() + key,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			editRoleAndMenuInit();// 初始化ajax弹出页面
			viewBox();
		}
	});
}

function submitEditRoleAndMenuForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				//$("#WORK_AREA")[0].contentWindow.editRoleAndMenuSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

			}
		}
	});
}

function initMenu(role_menu) {
	$("#cb_" + role_menu).attr("checked", "checked");
	initShowMenu(role_menu);
	$("#cb_PDA").attr("checked", "checked");
	checkPDAMenu();
}

function initShowMenu(menu){
	$("#menu_" + menu).parent().find("div").show();
}

function checkMenu(menu) {
	if ($("#menu_" + menu)) {
		if ($("#menu_" + menu).is(":hidden")) {
			$("#cb_" + menu).attr("checked", false);
			$("#menu_" + menu).show();
			$("#pic_" + menu).addClass("click");
		} else {
			checkAll(menu);
			$("#menu_" + menu).hide();
			$("#pic_" + menu).find("input[type='checkbox']").attr("checked", false);
			$("#pic_" + menu).removeClass("click");
		}
	}
}

function checkAll(menu) {
	if ($("#menu_" + menu)) {
		if ($("#menu_" + menu).is(":hidden")) {
			$("#menu_" + menu).parent().find("div").show();
			$("#menu_" + menu).parent().find("input[type='checkbox']").attr("checked", true);
			$("#pic_" + menu).parent().find("h2[id^='pic']").addClass("click");
			$("#pic_" + menu).parent().find("h3[id^='pic']").addClass("click");
		} else {
			$("#menu_" + menu).hide();
			$("#menu_" + menu).parent().find("input[type='checkbox']").attr("checked", false);
			$("#pic_" + menu).parent().find("h2[id^='pic']").removeClass("click");
			$("#pic_" + menu).parent().find("h3[id^='pic']").removeClass("click");
		}
	}
}

//判断上级下层所有选项未选中，取消当前(岗位权限管理)
function validataJuniorCheck(menu,upperMenu){
	var clearFlag = true;
	if ($("#menu_" + menu)) {
		var arrayTemp = $("#menu_" + menu).find("input[type='checkbox']");
		$(arrayTemp).each(function(){
			var $thisCB = $(this);
			if($thisCB.attr("checked")){
				clearFlag = false;
				return;
			}
		});
		if(clearFlag){
			$("#cb_" + menu).attr("checked", false);
		}else{
			$("#cb_" + menu).attr("checked", true);
			$("#cb_" + upperMenu).attr("checked", true);
		}
	}
	
}
//选中所有下级菜单
function juniorCheck(menu){
	$("#menu_" + menu).parent().find("input[type='checkbox']").attr("checked", true);
	$("#pic_" + menu).parent().find("h2[id^='pic']").addClass("click");
	$("#pic_" + menu).parent().find("h3[id^='pic']").addClass("click");
}
//选中所有下级菜单
function juniorUnCheck(menu){
	$("#cb_" + menu).attr("checked", false);
	$("#menu_" + menu).parent().find("input[type='checkbox']").attr("checked", false);
}

function checkPDAMenu() {
	if ($("#menuPDA1").is(":hidden") && $("#menuPDA2").is(":hidden") && $("#cb_PDA").attr("checked") == "checked") {
		$("#menuPDA1").show();
		$("#menuPDA2").show();
	} else {
		$("#menuPDA1").hide();
		$("#menuPDA2").hide();
		$("#menuPDA1").find("input[type='checkbox']").attr("checked", false);
	}
}
function checkPDAAll() {
	if ($("#menuPDA1") && $("#menuPDA2")) {
		if ($("#menuPDA1").is(":hidden") && $("#menuPDA2").is(":hidden")) {
			$("#menuPDA1").show();
			$("#menuPDA2").show();
			$("#PDAMenu").find("input[type='checkbox']").attr("checked", true);
		} else {
			$("#menuPDA1").hide();
			$("#menuPDA2").hide();
			$("#PDAMenu").find("input[type='checkbox']").attr("checked", false);
		}
	}
}

// /////设置权限END//////////////

// /////归班汇总修改BEGIN////////////
function init_deliverystate() {
	$("#backreasonid").parent().hide();
	$("#leavedreasonid").parent().hide();
	$("#firstlevelreasonid").parent().hide();
	$("#podremarkid").parent().hide();
	// $("#shouldfee").parent().hide();
	$("#infactfee").parent().hide();
	$("#returnedfee").parent().hide();
	$("#receivedfeecash").parent().hide();
	$("#receivedfeepos").parent().hide();
	$("#posremark").parent().hide();
	$("#receivedfeecheque").parent().hide();
	$("#receivedfeeother").parent().hide();
	$("#checkremark").parent().hide();
	$("#deliverstateremark").parent().hide();
	$("#weishuakareasonid").parent().hide();
	$("#losereasonid").parent().hide();
	$("#signmanid").parent().hide();
	$("#signman").parent().hide();
	$("#shouldfare").parent().hide();
	$("#infactfare").parent().hide();
	$("#changereasonid").parent().hide();
	$("#firstchangereasonid").parent().hide();
	$("#signmanphone").parent().hide();
	$("#transcwb").parent().hide();
	$("#shouldfareVipSmh").parent().hide();
	$("#infactfareVipSmh").parent().hide();
	
	//added by gordon.zhou 2016/12/15 如果是到付类型的快递单，需要在归班反馈页面的“到付费用合计”元素
	if($("#cwbordertypeid").val() == 6 && $("#paymethod").val() == 2){
		$("#infacttotalfee").parent().show(); 
	}else{
		$("#infacttotalfee").parent().hide();
	}
	//end aded by gordon.zhou
	
}

function gonggong() {
	$("#podremarkid").parent().show();
	$("#deliverstateremark").parent().show();
}
function peisongObj(podremarkid) {
	gonggong();
	// $("#shouldfee").parent().show();
	$("#receivedfeecash").parent().show();
	$("#receivedfeepos").parent().show();
	$("#posremark").parent().show();
	$("#receivedfeecheque").parent().show();
	$("#receivedfeeother").parent().show();
	$("#checkremark").parent().show();
	if (podremarkid == 1) {
		$("#signmanid").parent().show();
		$("#signmanid").val(1);
		if($("#exchangeflag").val()=='1'){
			$("#transcwb").parent().show();
			$("#shouldfareVipSmh").parent().show();
			$("#infactfareVipSmh").parent().show();
		}
	}
}
function shagnmentuiObj() {
	gonggong();
	$("#backreasonid").parent().show();
	$("#returnedfee").parent().show();
	$("#shouldfare").parent().show();
	$("#infactfare").parent().show();
	$("#transcwb").parent().show();
}
function shangmenhuanObj(podremarkid) {
	gonggong();
	// $("#leavedreasonid").parent().show();
	if ($("#isReceivedfee").val() == "yes") {
		peisongObj(podremarkid);
	} else {
		$("#returnedfee").parent().show();
	}
	$("#signmanid").parent().show();
	$("#signmanid").val(1);
}

function quantuiObj() {
	gonggong();
	$("#backreasonid").parent().show();
}

function bufentuihuoObj(podremarkid) {
	// gonggong();
	peisongObj(podremarkid);
	$("#backreasonid").parent().show();

	// $("#shouldfee").parent().show();
	// $("#infactfee").parent().show();
}
function zhiliuObj() {
	gonggong();
	$("#leavedreasonid").parent().show();
	$("#firstlevelreasonid").parent().show();
}

function zhongzhuanObj() {
	gonggong();
	$("#changereasonid").parent().show();
	$("#firstchangereasonid").parent().show();
}

function shangmenjutuiObj() {
	gonggong();
	$("#backreasonid").parent().show();
	$("#shouldfare").parent().show();
	$("#infactfare").parent().show();
}
function huowudiushiObj() {
	gonggong();
	if (parseInt($("#isOpenFlag").val()) != 0) {
		$("#losereasonid").parent().show();
	}
}
// pos 与 现金 切换
function forchange() {
	var cash = $("#receivedfeecash").val();
	var pos = $("#receivedfeepos").val();
	$("#receivedfeecash").val(pos);
	$("#receivedfeepos").val(cash);
	weishuakachange();
}

function weishuakachange() {
	var cash = $("#receivedfeecash").val();
	var cheque = $("#receivedfeecheque").val();
	var other = $("#receivedfeeother").val();
	if (parseInt($("#isOpenFlag").val()) != 0 && (cash > 0 || cheque > 0 || other > 0) && $("#paywayid").val() == 2) {
		// 原支付方式为pos,输入现金金额的时候,必须选未刷卡原因
		$("#weishuakareasonid").parent().show();
	} else {
		$("#weishuakareasonid").parent().hide();
	}
}

function signmanchange() {
	if ($("#signmanid").val() == 2) {
		$("#signman").parent().show();
		$("#signmanphone").parent().show();
	} else {
		$("#signman").val("");
		$("#signmanphone").val("");
		$("#signman").parent().hide();
		$("#signmanphone").parent().hide();
	}
}


function click_podresultid(deliverystate, PeiSongChengGong, ShangMenTuiChengGong, ShangMenHuanChengGong, JuShou, BuFenTuiHuo, FenZhanZhiLiu,
		ZhiLiuZiDongLingHuo, ShangMenJuTui, HuoWuDiuShi,DaiZhongZhuan, backreasonid, leavedreasonid, podremarkid, newpaywayid, weishuakareasonid, losereasonid,
		showposandqita, needdefault) {
	var podresultid = parseInt($("#podresultid").val());
	$("#infactfare").removeAttr('disabled');
	init_deliverystate();
	if (podresultid == PeiSongChengGong) {// 配送成功
		// added by gordon.zhou 2016/12/15 到付快递订单类型处理
		if($("#cwbordertypeid").val() == 6 && $("#paymethod").val() == 2){
			for (var i = 0; i < newpaywayid.split(",").length; i++) {
				var newpayway = newpaywayid.split(",")[i];
				if (newpayway == 1) {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100  - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 2 && showposandqita == "yes") {
					$("#receivedfeepos").val(
							(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 3) {
					$("#receivedfeecheque").val(
							(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 4 && showposandqita == "yes") {
					$("#receivedfeeother").val(
							(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
				} else {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				}
			}
		}
		//end added by gordon.zhou
		else{
			for (var i = 0; i < newpaywayid.split(",").length; i++) {
				var newpayway = newpaywayid.split(",")[i];
				if (newpayway == 1) {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 2 && showposandqita == "yes") {
					$("#receivedfeepos").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 3) {
					$("#receivedfeecheque").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 4 && showposandqita == "yes") {
					$("#receivedfeeother").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
				} else {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				}
			}
		}

		peisongObj(podresultid);
	} else if (podresultid == ShangMenTuiChengGong) {// 上门退成功
		$("#returnedfee").val($("#shouldfee").val());
		if (needdefault) {
			$("#infactfare").val(parseFloat($("#shouldfare").val()));
		}
		shagnmentuiObj();
	} else if (podresultid == ShangMenHuanChengGong) {// 上门换成功
		if ($("#isReceivedfee").val() == "yes") {
			for (var i = 0; i < newpaywayid.split(",").length; i++) {
				var newpayway = newpaywayid.split(",")[i];
				if (newpayway == 1) {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 2 && showposandqita == "yes") {
					$("#receivedfeepos").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 3) {
					$("#receivedfeecheque").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 4 && showposandqita == "yes") {
					$("#receivedfeeother").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
				} else {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				}
			}
		}
		shangmenhuanObj(podresultid);
	} else {
		$("input[id*='receivedfee']").val(0.00);
		$("#returnedfee").val(0.00);
		if (podresultid == JuShou) {// 拒收
			quantuiObj();
		} else if (podresultid == BuFenTuiHuo) {// 部分退货
			// added by gordon.zhou 2016/12/15 到付快递订单类型处理
			if($("#cwbordertypeid").val() == 6 && $("#paymethod").val() == 2){
				for (var i = 0; i < newpaywayid.split(",").length; i++) {
					var newpayway = newpaywayid.split(",")[i];
					if (newpayway == 1) {
						$("#receivedfeecash").val(
								(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100  - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 2 && showposandqita == "yes") {
						$("#receivedfeepos").val(
								(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 3) {
						$("#receivedfeecheque").val(
								(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 4 && showposandqita == "yes") {
						$("#receivedfeeother").val(
								(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
					} else {
						$("#receivedfeecash").val(
								(parseFloat($("#shouldfee").val()) * 100 + parseFloat($("#totalfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					}
				}
			}
			// end added by gordon.zhou
			else{
				for (var i = 0; i < newpaywayid.split(",").length; i++) {
					var newpayway = newpaywayid.split(",")[i];
					if (newpayway == 1) {
						$("#receivedfeecash").val(
								(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 2 && showposandqita == "yes") {
						$("#receivedfeepos").val(
								(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 3) {
						$("#receivedfeecheque").val(
								(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					} else if (newpayway == 4 && showposandqita == "yes") {
						$("#receivedfeeother").val(
								(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
					} else {
						$("#receivedfeecash").val(
								(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
										- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
					}
				}
			}
			bufentuihuoObj(podresultid);
		} else if (podresultid == FenZhanZhiLiu || podresultid == ZhiLiuZiDongLingHuo) {// 分站滞留、滞留自动领货		
			if (needdefault) {
				$("#infactfare").val(0);//DMP4.2.3 修复页面下拉先上门退成功=》再选分站滞留，实收运费被设置成应收运费的缺陷 by jinghui.pan 20151021
			}
			zhiliuObj();
		}else if (podresultid == DaiZhongZhuan) {// 待中转
			zhongzhuanObj();
		}
		
		else if (podresultid == ShangMenJuTui) {// 上门拒退
			//DMP4.2.3 修复页面下拉先上门退成功=》再选分站滞留，实收运费被设置成应收运费的缺陷 by jinghui.pan 20151021
			if (needdefault) {
				//$("#infactfare").val(parseFloat($("#shishou").val()));
				$("#infactfare").val(0);
			}
			shangmenjutuiObj();
		} else if (podresultid == HuoWuDiuShi) {// 货物丢失
			// 如果是货物丢失，就将实收现金和实收款赋值成当前应收款对应的值，如果是退货丢失的话，应该还钱，所以不需要赋值到退货款中
			$("#infactfee").val($("#shouldfee").val());
			$("#receivedfeecash").val($("#shouldfee").val());
			huowudiushiObj();
		}
	}

	$("#backreasonid").val(backreasonid);
	$("#leavedreasonid").val(leavedreasonid);
	$("#podremarkid").val(podremarkid);

	if (parseInt($("#isOpenFlag").val()) != 0) {
		$("#weishuakareasonid").val(weishuakareasonid);
		$("#losereasonid").val(losereasonid);
	}

	// $("#remandtype").val(0);
	centerBox();
	if(podresultid==7){
		$("#infactfare").attr('disabled','true');
		$("#infactfare").val(0);

	}
}

// 验证字段是否正确输入
function check_deliveystate(PeiSongChengGong, ShangMenTuiChengGong, ShangMenHuanChengGong, JuShou, BuFenTuiHuo, FenZhanZhiLiu, ZhiLiuZiDongLingHuo,
		ShangMenJuTui, HuoWuDiuShi,DaiZhongZhuan, isReasonRequired,oldTranscwb) {
	var podresultid = parseInt($("#podresultid").val());
	var backreasonid = parseInt($("#backreasonid").val());
	var leavereasonid = parseInt($("#leavedreasonid").val());
	var firstchangereasonid = parseInt($("#firstchangereasonid").val());
	var changereasonid = parseInt($("#changereasonid").val());
	var firstlevelreasonid=parseInt($("#firstlevelreasonid").val());
	var exchangeflag=$("#exchangeflag").val();
	if (!checkTime($("#deliverytime").val())) {
		return false;
	}
	;
	if (podresultid == -1) {
		return checkGongGong_delivery();
	}
	if (podresultid == PeiSongChengGong) {// 配送成功
		return checkPeiSong();
	} else if (podresultid == ShangMenTuiChengGong) {// 上门退成功
		if(oldTranscwb!=''&&oldTranscwb!=$("#transcwb").val()){
			if(!confirm('订单原来已经有快递单号，是否要修改快递单号？')){
				return false;
			}
		}
		
		return checkShangMenTui();
	} else if (podresultid == ShangMenHuanChengGong) {// 上门换成功
		return checkShangMenHuan();
	} else if (podresultid == BuFenTuiHuo) {// 部分退货
		if(exchangeflag=='1'){
			alert("唯品会上门换业务不允许反馈为部分拒收");
			return false;
		}
		if (isReasonRequired == 'yes' && !backreasonid > 0) {
			alert("请选择退货原因");
			return false;
		} else {
			return checkBuFenJuShou();
		}
	} else if (podresultid == JuShou) {// 拒收
		if (isReasonRequired == 'yes' && !backreasonid > 0) {
			alert("请选择退货原因");
			return false;
		} else {
			return checkGongGong_delivery();
		}
	} else if (podresultid == FenZhanZhiLiu || podresultid == ZhiLiuZiDongLingHuo) {// 分站滞留、滞留自动领货
		if (isReasonRequired == 'yes' && (firstlevelreasonid==0||leavereasonid==0)) {//!leavereasonid > 0||!firstlevelreasonid>0
			alert("请选择滞留原因");
			return false;
			//&& (firstlevelreasonid==0||leavereasonid==0)
		} else if(isReasonRequired != 'yes'){
			if(firstlevelreasonid!=0&&leavereasonid==0){
				alert("请选择滞留原因");
				return false;
			}
		}
		else {
			return checkGongGong_delivery();
		}
	} else if (podresultid == DaiZhongZhuan) {// 分站滞留、滞留自动领货
		if (firstchangereasonid == 0) {
			alert("请选择一级中转原因");
			return false;
		}else if (changereasonid == 0) {
			alert("请选择二级中转原因");
			return false;
		} else {
			return checkGongGong_delivery();
		}
	} 
	
	
	else if (podresultid == ShangMenJuTui) {// 上门拒退
		if (isReasonRequired == 'yes' && !backreasonid > 0) {
			alert("请选择退货原因");
			return false;
		}/*
			 * else if (parseFloat($("#shouldfare").val()) !=
			 * parseFloat($("#infactfare").val())) { alert("实收运费应该为" +
			 * parseFloat($("#shouldfare").val()));
			 * $("#infactfare").val(parseFloat($("#shouldfare").val())); return
			 * false; }
			 */
		else if (parseFloat($("#shouldfare").val()) == 0 && parseFloat($("#infactfare").val()) > 0) {
			alert("应收运费为0，实收运费不允许大于应收运费。");
			return false;
		}
		
		
		else if (!isFloat($("#infactfare").val())) {
			alert("实收运费只能为数值");
			return false;
		} 
		
		
		else {
			return checkGongGong_delivery();
		}
	} else if (podresultid == HuoWuDiuShi) {// 货物丢失
		return checkGongGong_diushi();
	}

	return true;
}

function checkGongGong_delivery() {
	if (parseInt($("#podresultid").val()) == -1) {
		alert("请选择配送结果");
		return false;
	}
	return true;
}

/**
 * 验证时间格式是否为 “yyyy-mm-dd hh:mm:ss ”
 * 
 * @param str
 * @returns {Boolean}
 */
function checkTime(str) {
	var r = str.match(/^(\d{1,4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
	if (r == null) {
		alert("请输入格式正确的时间\n\r 时间格式：yyyy-mm-dd hh:mm:ss\n\r例    如：2013-08-08 15:55:33\n\r");
		return false;
	}
	var d = new Date(r[1], r[2] - 1, r[3], r[4], r[5], r[6]);
	var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[2] && d.getDate() == r[3] && d.getHours() == r[4] && d.getMinutes() == r[5] && d
			.getSeconds() == r[6]);
	if (num == 0) {
		alert("请输入格式正确的时间\n\r 时间格式：yyyy-mm-dd hh:mm:ss\n\r例    如：2013-08-08 15:55:33\n\r");
		return false;
	} else {
		if (2000 < d.getFullYear() && d.getFullYear() < 3000) {
			return true;
		} else {
			alert("年份应在2000--3000之间");
			return false;
		}
		;
	}

}

function checkGongGong_diushi() {
	if (parseInt($("#podresultid").val()) == -1) {
		alert("请选择配送结果");
		return false;
	}
	if (parseInt($("#isOpenFlag").val()) != 0 && parseInt($("#losereasonid").val()) == 0) {
		alert("请选择丢失原因");
		return false;
	}
	return true;
}

function checkWeishuaka() {
	if (parseInt($("#isOpenFlag").val()) != 0
			&& $("#paywayid").val() == 2
			&& $("#weishuakareasonid").val() == 0
			&& (parseFloat($("#receivedfeecash").val()) > 0 || parseFloat($("#receivedfeecheque").val()) > 0 || parseFloat($("#receivedfeeother")
					.val()) > 0)) {
		alert("请选择未刷卡原因");
		return false;
	}
	return true;
}

function checkBuFenJuShou() {
	if (!checkGongGong_delivery()) {
		return false;
	}
	if (!checkWeishuaka()) {
		return false;
	}
	if (!checksignman()) {
		return false;
	}
	return true;
}

function checkPeiSong() {
	if (!checkGongGong_delivery()) {
		return false;
	}

	if (!checksignman()) {
		return false;
	}
	// added by gordon.zhou 2016/12/19 快递到付订单反馈金额校验
	if($("#cwbordertypeid").val() == 6 && $("#paymethod").val() == 2){
		if ((parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
				+ parseFloat($("#receivedfeeother").val()) != parseFloat($("#shouldfee").val()) + parseFloat($("#totalfee").val()))) {
			alert("配送成功时，总金额不等于应收款，不可以反馈");
			return false;
		}
	}else{
		if ((parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
				+ parseFloat($("#receivedfeeother").val()) != parseFloat($("#shouldfee").val()))) {
			alert("配送成功时，总金额不等于应收款，不可以反馈");
			return false;
		}
	}

	if (!checkWeishuaka()) {
		return false;
	}
	
	if($("#exchangeflag").val()=='1'){
		var transcwbSmh=$("#transcwb").val();
		if(transcwbSmh==null||$.trim(transcwbSmh)==''){
			alert("快递单号不能为空");
			return false;
		}
		if (!isFloat($("#infactfareVipSmh").val())) {
			alert("实收运费只能为数值");
			return false;
		}
		if (parseFloat($("#shouldfareVipSmh").val()) != parseFloat($("#infactfareVipSmh").val())) {
			alert("实收运费应该等于应收运费");
			return false;
		}
	}
	
	return true;
}



function checksignman() {
	if (parseFloat($("#signmanid").val()) == 2 && $("#signman").val().length == 0) {
		alert("请输入签收人姓名");
		return false;
	}
	if (parseFloat($("#signmanid").val()) == 2 && $("#signmanphone").val().length == 0) {
		alert("请输入代签收人电话");
		return false;
	}
	return true;
}


function checkShangMenTui() {
	if (!checkGongGong_delivery()) {
		return false;
	}
	if (parseFloat($("#returnedfee").val()) != parseFloat($("#shouldfee").val())) {
		alert("退还金额应该为" + parseFloat($("#shouldfee").val()));
		$("#returnedfee").val(parseFloat($("#shouldfee").val()));
		return false;
	}
	if (parseFloat($("#shouldfare").val()) == 0 && parseFloat($("#infactfare").val()) > 0) {
		alert("应收运费为0，实收运费不允许大于应收运费。");
		return false;
	}
	if (!isFloat($("#infactfare").val())) {
		alert("实收运费只能为数值");
		return false;
	}
	if (parseFloat($("#shouldfare").val()) > 0 && parseFloat($("#infactfare").val()) == 0) {
		if(!confirm('实收运费为0，默认会等于应收运费，确定提交么？')){
			return false;
		}
	} 
	/*
	 * if (parseFloat($("#shouldfare").val()) !=
	 * parseFloat($("#infactfare").val())) { alert("实收运费应该为" +
	 * parseFloat($("#shouldfare").val()));
	 * $("#infactfare").val(parseFloat($("#shouldfare").val())); return false; }
	 */
	return true;
}

function checkShangMenHuan() {
	if (!checkGongGong_delivery()) {
		return false;
	}
	if (!checksignman()) {
		return false;
	}
	if (($("#isReceivedfee").val() == "yes" && parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val())
			+ parseFloat($("#receivedfeecheque").val()) + parseFloat($("#receivedfeeother").val()) + parseFloat($("#returnedfee").val()) > parseFloat($(
			"#shouldfee").val()))) {
		alert("货款金额不能大于应处理金额");
		return false;
	}
	if ($("#isReceivedfee").val() == "yes"
			&& (parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
					+ parseFloat($("#receivedfeeother").val()) != parseFloat($("#shouldfee").val()))) {
		alert("上门换成功时，总金额不等于应收款，不可以反馈");
		return false;
	}

	if ($("#isReceivedfee").val() == "no" && parseFloat($("#returnedfee").val()) != parseFloat($("#shouldfee").val())) {
		alert("上门换成功时，退还现金不等于应退款，不可以反馈");
		return false;
	}

	if (!checkWeishuaka()) {
		return false;
	}
	return true;
}
/*
 * function checkQuanTui(){ if(!checkGongGong_delivery()){return false;}
 * if(parseFloat($("#returnedfee").val())<1){ alert("退还金额不能为空"); return false; } }
 * function checkBuFenTui(){ if(!checkGongGong_delivery()){return false;}
 * if(parseFloat($("#returnedfee").val())<1){ alert("退还金额不能为空"); return false; } }
 */
/*----------------------承运商管理-------------------------*/
function check_common() {
	if ($("#commonname").val().length == 0) {
		alert("承运商名称不能为空");
		return false;
	}
	if ($("#commonnumber").val().length == 0) {
		alert("承运商编号不能为空");
		return false;
	}
	if ($("#commenbranchid").val() > 0 && $("#commenUserid").val() == 0) {
		alert("选择了站点，必须选择站长");
		return false;
	}
	if (!isChinese($("#commonnumber").val())) {
		alert("承运商编号不能带有中文");
		return false;
	}
	if (!isChinese($("#orderprefix").val())) {
		alert("承运商订单号前缀不能带有中文");
		return false;
	}
	if ($("#pageSize").val() == '') {
		$("#pageSize").val(0);

	}

	if ($("#loopcount").val() == '') {
		$("#loopcount").val(0);
	}
	if (!isNumber($("#pageSize").val())) {
		alert("最大查询数量只能为数字");
		$("#pageSize").focus();
		return false;
	}
	if (!isNumber($("#loopcount").val())) {
		alert("重发次数只能为数字");
		return false;
	}

	return true;
}
function check_commonmodel() {
	if ($("#modelname").val().length == 0) {
		alert("模版名称不能为空");
		return false;
	}
	return true;
}
function updateCommenDeliver(pname) {
	$.ajax({
		url : pname + "/common/updateDeliver",// 后台处理程序
		type : "POST",// 数据发送方式
		data : "branchid=" + $("#commenbranchid").val(),// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#commenUserid").empty();// 清空下拉框//$("#select").html('');
			$("<option value='0'>==请选择==</option>").appendTo("#commenUserid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].userid + "'>" + json[j].realname + "</option>").appendTo("#commenUserid");
			}
		}
	});
}
/*----------------------承运商管理END----------------------*/

/**
 * 打印面单
 */
function submitChangeCwbForPrint(cwb) {
	if (cwb != "") {
		$.ajax({
			type : "POST",
			url : "../changecwb/getCommonByCwb/" + cwb,
			dataType : "json",
			success : function(data) {
				if (data.error != "0") {
					alert(data.error);
					$("#cwb").val("");
					$("#commonname").val("0");
					$("#commoncwb").val("");
				} else {
					if (data.common != undefined || data.common != null) {
						$("#commonname").val(data.common.id);

						var myDate = new Date();
						var nowtime = myDate.getFullYear() + "-" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
						for (var i = 0; i < data.customerlist.length; i++) {
							if (data.cwborder.customerid == data.customerlist[i].customerid) {
								var dstr = nowtime + ',' + data.cwborder.cwb + ',' + data.cwborder.sendcarname + ',' + data.cwborder.cwb + ','
										+ data.customerlist[i].customername + ',' + data.cwborder.consigneename + ',' + data.cwborder.consigneemobile
										+ ',' + data.cwborder.cwb + ',' + data.cwborder.consigneeaddress + ',' + data.cwborder.carrealweight + ','
										+ data.cwborder.carsize + ',' + data.cwborder.consigneepostcode + ',' + data.cwborder.cwbremark;
								myPreview1(data.commonmodel.coordinate, dstr);
							}
						}
						$("#commoncwb").focus();

					} else {
						alert("该订单没有设置面单类型");
					}

				}
			}
		});
	}
}
/**
 * 打印面单（带称重）
 * 
 * @param cwb
 */
function submitChangeCwbForweightPrint(cwb) {
	if (cwb != "") {
		$.ajax({
			type : "POST",
			url : "../changecwb/getCommonByCwb/" + cwb,
			dataType : "json",
			success : function(data) {
				if (data.error != "0") {
					alert(data.error);
					$("#cwb").val("");
					$("#commonname").val("0");
					$("#commoncwb").val("");
				} else {

					if ($("#carrealweight").val() == "") {
						alert("请称重");
						return false;
					} else if ($("#paohuo").attr("checked") == "checked"
							&& ($("#length").val() == "" || $("#width").val() == "" || $("#height").val() == "")) {
						alert("包裹体积填写不完整，长宽高都要填");
						return false;
					} else if (data.common == undefined || data.common == null) {
						alert("该订单没有设置面单类型");
						return false;
					} else {
						$("#commonname").val(data.common.id);

						data.cwborder.carrealweight = $("#carrealweight").val();
						data.cwborder.carsize = $("#length").val() + "," + $("#width").val() + "," + $("#height").val();

						var myDate = new Date();
						var nowtime = myDate.getFullYear() + "-" + (myDate.getMonth() + 1) + "-" + myDate.getDate();
						for (var i = 0; i < data.customerlist.length; i++) {
							if (data.cwborder.customerid == data.customerlist[i].customerid) {
								var dstr = nowtime + ',' + data.cwborder.cwb + ',' + data.cwborder.sendcarname + ',' + data.cwborder.cwb + ','
										+ data.customerlist[i].customername + ',' + data.cwborder.consigneename + ',' + data.cwborder.consigneemobile
										+ ',' + data.cwborder.cwb + ',' + data.cwborder.consigneeaddress + ',' + data.cwborder.carrealweight + ','
										+ data.cwborder.carsize + ',' + data.cwborder.consigneepostcode + ',' + data.cwborder.cwbremark;
								myPreview1(data.common.coordinate, dstr);
							}
						}
						$("#commoncwb").focus();
					}
				}
			}
		});
	}
}

function ChangeCwbForPrint(cwb, commoncwb) {
	$.ajax({
		type : "POST",
		url : "../changecwb/printCwbForScan/" + cwb + "?commoncwb=" + commoncwb,
		dataType : "json",
		success : function(data) {
			if (data.errorCode != 0) {
				alert(data.error);
			}
			$("#cwb").val("");
			$("#commonname").val("0");
			$("#commoncwb").val("");
		}
	});
}

function ChangeCwbWeightForPrint(cwb) {
	$.ajax({
		type : "POST",
		url : "../changecwb/printCwbAndWeightForScan/" + cwb,
		dataType : "json",
		success : function(data) {
			if (data.errorCode != 0) {
				alert(data.error);
			}
			$("#cwb").val("");
			$("#commonname").val("0");
			$("#carrealweight").val("");
			$("#length").val("");
			$("#width").val("");
			$("#height").val("");
			$("#commoncwb").val("");
		}
	});
}

/*----------订单查询中的（操作日志）--------------*/
function getViewBoxd(key, durl) {
	$.ajax({
		type : "POST",
		url : durl + key,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
			viewBoxd();
		}
	});
}
function viewBoxd() {
	// $(window).keydown(function(event){
	// switch(event.keyCode) {
	// case (event.keyCode=27):window.parent.closeBox();break;
	// }
	// });
	$("#alert_box", parent.document).show();
	$("#dress_box", parent.document).css("visibility", "hidden");
	window.parent.centerBox();
}
/*----------订单查询中的（操作日志END）--------------*/
function check_liebo() {

	return true;
}

/** ***********************************************************PDA功能js**************************************************** */
/**
 * 退货站入库扫描
 */
/*
 * function
 * submitBackIntoWarehouse(pname,scancwb,driverid,requestbatchno,comment){
 * if(scancwb.length>0){ for(var i=0;i<scancwb.split(",").length;i++){ var cwb =
 * scancwb.split(",")[i]; $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbbackintowarhouse/"+cwb+"?driverid="+driverid+"&requestbatchno="+requestbatchno,
 * data:{ "comment":comment }, dataType:"json", success : function(data) {
 * $("#scancwb").val(""); if(data.statuscode=="000000"){
 * $("#cwbgaojia").parent().hide();
 * 
 * $("#excelbranch").parent().show(); $("#customername").parent().show();
 * 
 * 
 * if(data.body.cwbOrder.deliverybranchid!=0){
 * $("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
 * }else{ $("#excelbranch").html("尚未匹配站点"); }
 * 
 * $("#customername").html(data.body.cwbcustomername);
 * $("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
 * $("#msg").html(cwb+data.errorinfo+"
 * （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
 * getcwbsdataForCustomer(pname,customerid);
 * 
 * 
 * if(data.body.cwbbranchnamewav!=pname+"/wav/"){
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random()); }
 * 
 * if(data.body.cwbgaojia!=undefined&&data.body.cwbgaojia!=''){
 * $("#cwbgaojia").parent().show(); try{ document.gaojia.Play(); }catch (e) {} }
 * if(data.body.cwbOrder.sendcarnum>1){ try{ document.ypdj.Play(); }catch (e) {} }
 * 
 * $("#scansuccesscwb").val(cwb);
 * 
 * }else{ $("#excelbranch").parent().hide(); $("#customername").parent().hide();
 * $("#cwbgaojia").parent().hide(); $("#damage").parent().hide();
 * $("#multicwbnum").parent().hide(); $("#multicwbnum").val("1");
 * $("#msg").html(" （异常扫描）"+data.errorinfo); } errorvedioplay(pname,data);
 * $("#responsebatchno").val(data.responsebatchno); } }); } } }
 */

/*
 * //得到当前入库的供货商的库存量 function getcwbsdataForCustomer(pname,customerid){ $.ajax({
 * type: "POST", url:pname+"/PDA/getInSum", data:{ "customerid":customerid },
 * dataType:"json", success : function(data) { //alert(data.statuscode); var
 * showhtml=""; if(data.length!=0){ showhtml=data.branch.branchname+"："+"
 * 单数："+data.size[0].count+" 件数："+data.size[0].sum; }
 * if($("#rukukucundanshu",parent.document).length>0){
 * $("#rukukucundanshu",parent.document).html(showhtml); }else{
 * $("#rukukucundanshu").html(showhtml); } } }); }
 */

/**
 * 入库扫描
 */
/*
 * function
 * submitIntoWarehouse(pname,scancwb,customerid,driverid,requestbatchno,rk_switch,comment){
 * if(scancwb.length>0){ for(var i=0;i<scancwb.split(",").length;i++){ var cwb =
 * scancwb.split(",")[i]; $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbintowarhouse/"+cwb+"?customerid="+customerid+"&driverid="+driverid+"&requestbatchno="+requestbatchno,
 * data:{ "comment":comment }, dataType:"json", success : function(data) {
 * $("#scancwb").val(""); if(data.statuscode=="000000"){
 * $("#cwbgaojia").parent().hide();
 * 
 * $("#excelbranch").parent().show(); $("#customername").parent().show();
 * $("#damage").parent().show(); $("#multicwbnum").parent().show();
 * 
 * $("#customerid").val(data.body.cwbOrder.customerid);
 * 
 * if(data.body.cwbOrder.deliverybranchid!=0){
 * $("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
 * }else{ $("#excelbranch").html("尚未匹配站点"); }
 * 
 * $("#customername").html(data.body.cwbcustomername);
 * $("#multicwbnum").val(data.body.cwbOrder.sendcarnum);
 * $("#msg").html(cwb+data.errorinfo+"
 * （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
 * getcwbsdataForCustomer(pname,customerid);
 * 
 * if(rk_switch=="rkbq_01"){
 * $("#printcwb",parent.document).attr("src",pname+"/printcwb?scancwb="+cwb+"&a="+Math.random()); }
 * 
 * if(data.body.cwbbranchnamewav!=pname+"/wav/"){
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random()); }
 * 
 * if(data.body.cwbgaojia!=undefined&&data.body.cwbgaojia!=''){
 * $("#cwbgaojia").parent().show(); try{ document.gaojia.Play(); }catch (e) {} }
 * if(data.body.cwbOrder.sendcarnum>1){ try{ document.ypdj.Play(); }catch (e) {} }
 * 
 * $("#scansuccesscwb").val(cwb);
 * 
 * }else{ $("#excelbranch").parent().hide(); $("#customername").parent().hide();
 * $("#cwbgaojia").parent().hide(); $("#damage").parent().hide();
 * $("#multicwbnum").parent().hide(); $("#multicwbnum").val("1");
 * $("#msg").html(" （异常扫描）"+data.errorinfo); } errorvedioplay(pname,data);
 * $("#responsebatchno").val(data.responsebatchno); } }); } } }
 */

/**
 * 入库扫描（包）
 */
function submitIntoWarehouseforbale(pname, driverid, baleno) {
	if (scancwb.length == 0 && baleno.length == 0) {
		$("#msg").html("请先扫描");
		return;
	}
	if (baleno.length > 0) {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbintowarhouseByPackageCode/" + baleno + "?driverid=" + driverid,
			dataType : "json",
			success : function(data) {
				$("#bale").val("");
				$("#msg").html(data.body.packageCode + "　（" + data.errorinfo + "）");
			}
		});
	}
}

/**
 * 出库扫描
 */
/*
 * var successnum = 0,errorcwbnum = 0; function
 * exportWarehouse(pname,scancwb,branchid,driverid,truckid,requestbatchno,baleno,ck_switch,confirmflag,size,zhongzhuan){
 * if(zhongzhuan&&size==0){ alert("抱歉，系统未分配中转站点，请联络您公司管理员！"); return; }
 * if(scancwb.length>0){ $("#close_box").hide(); $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag="+confirmflag+"&requestbatchno="+requestbatchno+"&baleno="+baleno,
 * dataType:"json", success : function(data) { $("#scancwb").val("");
 * $("#msg").parent().show(); if(data.statuscode=="000000"){
 * if($("#scansuccesscwb").val()!=scancwb){ successnum += 1; }
 * $("#msg").parent().show(); $("#excelbranch").parent().show();
 * 
 * $("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
 * $("#msg").html(scancwb+data.errorinfo+"
 * （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
 * 
 * $("#branchid").val(data.body.cwbOrder.nextbranchid);
 * $("#scansuccesscwb").val(scancwb); getOutSum(pname,0);
 * if(data.body.cwbOrder.sendcarnum>1){ document.ypdj.Play(); }
 * if(data.body.cwbbranchnamewav!=pname+"/wav/"){
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random()); }
 * errorvedioplay(pname,data); }else if(data.statuscode=="49"){
 * if(ck_switch=="ck_01"){//允许出库 var con = confirm("出库站点与分拣站点不一致，是否强制出库？");
 * if(con == false){ errorcwbnum += 1; $("#excelbranch").parent().hide();
 * $("#msg").html(scancwb+" （异常扫描）出库站点与分拣站点不一致");
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random());
 * }else{ successnum += 1; $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbexportwarhouse/"+scancwb+"?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&confirmflag=1&requestbatchno="+requestbatchno+"&baleno="+baleno,
 * dataType:"json", success : function(data) { var successnum = 0,errorcwbnum =
 * 0; if(data.statuscode=="000000"){ if($("#scansuccesscwb").val()!=scancwb){
 * successnum += 1; } $("#msg").parent().show();
 * $("#excelbranch").parent().show();
 * 
 * $("#excelbranch").html("目的站："+data.body.cwbdeliverybranchname+"<br/>下一站："+data.body.cwbbranchname);
 * $("#msg").html(scancwb+data.errorinfo+"
 * （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
 * 
 * $("#branchid").val(data.body.cwbOrder.nextbranchid);
 * $("#scansuccesscwb").val(scancwb); getOutSum(pname,0);
 * if(data.body.cwbOrder.sendcarnum>1){ document.ypdj.Play(); }
 * if(data.body.cwbbranchnamewav!=pname+"/wav/"){
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.body.cwbbranchnamewav+"&a="+Math.random()); }
 * }else{ errorcwbnum += 1; $("#excelbranch").parent().hide();
 * $("#msg").html(scancwb+" （异常扫描）"+data.errorinfo); }
 * errorvedioplay(pname,data); } }); } }else{ errorcwbnum += 1;
 * $("#excelbranch").parent().hide(); $("#msg").html(scancwb+"
 * （异常扫描）出库站点与分拣站点不一致");
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/fail.wav&a="+Math.random()); }
 * }else{ errorcwbnum += 1; $("#excelbranch").parent().hide();
 * $("#msg").html(scancwb+" （异常扫描）"+data.errorinfo); errorvedioplay(pname,data); }
 * $("#responsebatchno").val(data.responsebatchno);
 * $("#successcwbnum").html("成功扫描："+successnum);
 * $("#errorcwbnum").html("放错货："+errorcwbnum);
 * getOutSum(pname,$("#branchid").val()); } }); } }
 */

/**
 * 出库扫描（包）
 */
function exportWarehouseforbale(pname, scancwb, targetcarwarehouseid, driverid, requestbatchno, baleno, ck_switch) {
	if (targetcarwarehouseid == "-1") {
		$("#msg").html("请先选择目标库房");
		return;
	}
	if (scancwb.length > 0) {
		$("#close_box").hide();
		$
				.ajax({
					type : "POST",
					url : pname + "/PDA/cwbexportwarhouseforbale/" + scancwb + "?targetcarwarehouseid=" + targetcarwarehouseid + "&driverid="
							+ driverid + "&requestbatchno=" + requestbatchno + "&baleno=" + baleno + "&confirmflag=0",
					dataType : "json",
					success : function(data) {
						$("#scancwb").val("");

						if (data.statuscode == "000000" || data.statuscode == "200029" || data.statuscode == "200051") {
							if (data.cwboldtargetcarwarhouse != "" && data.cwboldtargetcarwarhouse != data.cwbtargetcarwarhouse
									&& ck_switch == "ck_01") {
								var con = confirm("目标库房与指定目标库房不一致，是否强制出库？");
								if (con == false) {
									errorcwbnum += 1;
									$("#excelbranch").parent().hide();
									$("#cwbbranchnamesorted").parent().hide();

									$("#msg").html(scancwb + "         （异常扫描）目标库房与指定目标库房不一致");
									$("#wavPlay", parent.document).attr("src",
											pname + "/wavPlay?wavPath=" + pname + "/images/waverror/fail.wav&a=" + Math.random());
									doWavAudio(pname + "/images/waverror/fail.wav");
								} else {
									$.ajax({
										type : "POST",
										url : pname + "/PDA/cwbexportwarhouseforbale/" + scancwb + "?targetcarwarehouseid=" + targetcarwarehouseid
												+ "&driverid=" + driverid + "&requestbatchno=" + requestbatchno + "&baleno=" + baleno
												+ "&confirmflag=1",
										dataType : "json",
										success : function(data) {
											$("#scancwb").val("");

											if (data.statuscode == "000000" || data.statuscode == "200051") {
												if ($("#scansuccesscwb").val() != scancwb) {
													successnum += 1;
												}

												$("#excelbranch").parent().show();
												$("#cwbbranchnamesorted").parent().show();

												$("#msg").html(scancwb + "         （成功扫描）");
												$("#excelbranch").html(data.cwbbranchnamesorted);
												$("#cwbbranchnamesorted").html(data.cwbbranchname.substring(0, data.cwbbranchname.indexOf("(")));

												$("#scansuccesscwb").val(scancwb);
												if (data.cwbbranchnamewav != pname + "/wav/") {
													$("#wavPlay", parent.document).attr("src",
															pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
													doWavAudio(data.cwbbranchnamewav);
												}
												errorvedioplay(pname, data);
											} else if (data.statuscode == "200029") {// 一票多件
												$("#excelbranch").parent().show();
												$("#cwbbranchnamesorted").parent().show();

												$("#excelbranch").html(data.cwbbranchnamesorted);
												$("#cwbbranchnamesorted").html(data.cwbbranchname.substring(0, data.cwbbranchname.indexOf("(")));

												$("#msg").html(scancwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
												$("#scansuccesscwb").val(scancwb);
												if (data.cwbbranchnamewav != pname + "/wav/") {
													$("#wavPlay", parent.document).attr("src",
															pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
													doWavAudio(data.cwbbranchnamewav);
												}
												document.ypdj.Play();
												errorvedioplay(pname, data);
											} else {
												errorcwbnum += 1;
												$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
												errorvedioplay(pname, data);
											}
											$("#responsebatchno").val(data.responsebatchno);
											$("#successcwbnum").html("成功扫描：" + successnum);
											$("#errorcwbnum").html("放错货：" + errorcwbnum);
										}
									});
								}
							} else if ((data.cwboldtargetcarwarhouse == "" || (data.cwboldtargetcarwarhouse != "" && data.cwboldtargetcarwarhouse == data.cwbtargetcarwarhouse))
									&& data.statuscode == "000000") {
								if ($("#scansuccesscwb").val() != scancwb) {
									successnum += 1;
								}

								$("#excelbranch").parent().show();
								$("#cwbbranchnamesorted").parent().show();

								$("#msg").html(scancwb + "         （成功扫描）");
								$("#excelbranch").html(data.cwbbranchnamesorted);
								$("#cwbbranchnamesorted").html(data.cwbtargetcarwarhouse.substring(0, data.cwbtargetcarwarhouse.indexOf("(")));

								$("#scansuccesscwb").val(scancwb);
								if (data.cwbtargetcarwarhousewav != pname + "/wav/") {
									$("#wavPlay", parent.document).attr("src",
											pname + "/wavPlay?wavPath=" + data.cwbtargetcarwarhousewav + "&a=" + Math.random());
									doWavAudio(data.cwbtargetcarwarhousewav);
								}
								errorvedioplay(pname, data);
							} else if ((data.cwboldtargetcarwarhouse == "" || (data.cwboldtargetcarwarhouse != "" && data.cwboldtargetcarwarhouse == data.cwbtargetcarwarhouse))
									&& data.statuscode == "200029") {
								if ($("#scansuccesscwb").val() != scancwb) {
									successnum += 1;
								}

								$("#excelbranch").parent().show();
								$("#cwbbranchnamesorted").parent().show();

								$("#excelbranch").html(data.cwbbranchnamesorted);
								$("#cwbbranchnamesorted").html(data.cwbtargetcarwarhouse.substring(0, data.cwbtargetcarwarhouse.indexOf("(")));

								$("#msg").html(scancwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
								$("#scansuccesscwb").val(scancwb);
								if (data.cwbtargetcarwarhousewav != pname + "/wav/") {
									$("#wavPlay", parent.document).attr("src",
											pname + "/wavPlay?wavPath=" + data.cwbtargetcarwarhousewav + "&a=" + Math.random());
									doWavAudio(data.cwbtargetcarwarhousewav);
								}
								document.ypdj.Play();
								errorvedioplay(pname, data);
							} else {
								errorcwbnum += 1;
								$("#excelbranch").parent().hide();
								$("#cwbbranchnamesorted").parent().hide();

								$("#msg").html(scancwb + "         （异常扫描）目标库房与指定目标库房不一致");
								$("#wavPlay", parent.document).attr("src",
										pname + "/wavPlay?wavPath=" + pname + "/images/waverror/fail.wav&a=" + Math.random());
								doWavAudio(pname + "/images/waverror/fail.wav");
							}

							/*
							 * $("#excelbranch").parent().show();
							 * $("#cwbbranchnamesorted").parent().show();
							 * 
							 * $("#msg").html(scancwb+" （成功扫描）");
							 * $("#excelbranch").html(data.cwbbranchnamesorted);
							 * $("#cwbbranchnamesorted").html(data.cwbbranchname.substring(0,data.cwbbranchname.indexOf("(")));
							 * 
							 * $("#scansuccesscwb").val(scancwb);
							 * if(data.cwbbranchnamewav!=pname+"/wav/"){
							 * $("#wavPlay").attr("src",pname+"/wavPlay?wavPath="+data.cwbbranchnamewav+"&a="+Math.random()); }
							 */
						}/*
							 * else if(data.statuscode=="200029"){//一票多件
							 * $("#excelbranch").parent().show();
							 * $("#cwbbranchnamesorted").parent().show();
							 * 
							 * $("#excelbranch").html(data.cwbbranchnamesorted);
							 * $("#cwbbranchnamesorted").html(data.cwbbranchname.substring(0,data.cwbbranchname.indexOf("(")));
							 * 
							 * $("#msg").html(scancwb+"
							 * （一票多件，共"+data.cwbsendcarnum+"件，已扫"+data.cwbscannum+"件）");
							 * $("#scansuccesscwb").val(scancwb);
							 * if(data.cwbbranchnamewav!=pname+"/wav/"){
							 * $("#wavPlay").attr("src",pname+"/wavPlay?wavPath="+data.cwbbranchnamewav+"&a="+Math.random()); }
							 * document.ypdj.Play(); }
							 */else {
							errorcwbnum += 1;
							$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
							errorvedioplay(pname, data);
						}
						$("#responsebatchno").val(data.responsebatchno);
						$("#successcwbnum").html("成功扫描：" + successnum);
						$("#errorcwbnum").html("放错货：" + errorcwbnum);
					}
				});
	}
}

/**
 * 加急件出库扫描
 */
var jjsuccessnum = 0, jjerrorcwbnum = 0;
function urgentexportWarehouse(pname, scancwb, branchid, driverid, truckid, requestbatchno, baleno, ck_switch) {
	if (scancwb.length > 0) {
		$("#close_box").hide();
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbexportwarhouse/" + scancwb + "?branchid=" + branchid + "&driverid=" + driverid + "&truckid=" + truckid
					+ "&confirmflag=0&requestbatchno=" + requestbatchno + "&baleno=" + baleno,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				$("#msg").parent().show();
				if (data.statuscode == "000000") {
					if ($("#scansuccesscwb").val() != scancwb) {
						jjsuccessnum += 1;
					}
					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);
					$("#msg").html(scancwb + "         （成功扫描）");
					$("#branchid").val(data.cwbbranchid);
					$("#scansuccesscwb").val(scancwb);
					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
				} else if (data.statuscode == "200029") {// 一票多件
					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);
					$("#msg").html(scancwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
					$("#branchid").val(data.cwbbranchid);
					$("#scansuccesscwb").val(scancwb);
					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
					document.ypdj.Play();
				} else {
					jjerrorcwbnum += 1;
					$("#excelbranch").parent().hide();
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
				}
				errorvedioplay(pname, data);
				$("#responsebatchno").val(data.responsebatchno);
				$("#successcwbnum").html("成功扫描：" + jjsuccessnum);
				$("#errorcwbnum").html("放错货：" + jjerrorcwbnum);
			}
		});
	}
}

function exportwarhousetocheck(pname, branchid, driverid, truckid, requestbatchno) {
	closeBox();
	/*
	 * $.ajax({ type: "POST",
	 * url:pname+"/PDA/finishexportwarhouse?branchid="+branchid+"&driverid="+driverid+"&truckid="+truckid+"&requestbatchno="+requestbatchno,
	 * dataType:"json", success : function(data) { } });
	 */
}

function clicksys() {
	if ($("#issyc").attr("checked") == "checked") {
		$("#sysintowarhouse").val("1");
	} else {
		$("#sysintowarhouse").val("0");
	}
}

/**
 * 分站到货扫描
 */
function branchImport(pname, scancwb, driverid, requestbatchno, baleno, sysintowarhouse) {
	$("#msg").parent().show();
	if (scancwb.length > 0 || baleno.length > 0) {
		$("#close_box").hide();
		if (baleno.length > 0) {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/getcwbbybaleno/" + baleno + "?driverid=" + driverid + "&sysintowarhouse=" + sysintowarhouse,
				dataType : "json",
				success : function(data) {
					$("#baleno").val("");
					if (data.errorinfo == "1") {
						$("#msg").html("包不存在！");
					} else {
						scancwb += data.cwbs;
						$("#msg").html(baleno + "         （包成功扫描）");
						$("#scansuccessbale").val(baleno);
					}
				}
			});
		}

		if (scancwb.length > 0) {
			for (var i = 0; i < scancwb.split(",").length; i++) {
				var cwb = scancwb.split(",")[i];
				$.ajax({
					type : "POST",
					url : pname + "/PDA/cwbbranchimport/" + cwb + "?driverid=" + driverid + "&requestbatchno=" + requestbatchno,
					dataType : "json",
					success : function(data) {
						$("#scancwb").val("");
						if (data.statuscode == "000000" || data.statuscode == "200045") {
							$("#cwbgaojia").parent().hide();
							if (data.statuscode == "000000") {
								$("#exceldeliverid").parent().show();

								$("#exceldeliverid").html(data.cwbdelivername);
								$("#msg").html(cwb + "         （成功扫描）");

								if (data.cwbdelivernamewav != pname + "/wav/") {
									$("#wavPlay", parent.document).attr("src",
											pname + "/wavPlay?wavPath=" + data.cwbdelivernamewav + "&a=" + Math.random());
									doWavAudio(data.cwbdelivernamewav);
								}
								if (data.cwbgaojia != undefined) {
									$("#cwbgaojia").parent().show();
									document.gaojia.Play();
								}
								$("#wavPlay", parent.document).attr("src",
										pname + "/wavPlay?wavPath=" + pname + "/images/waverror/success.wav&a=" + Math.random());
								doWavAudio(pname + "/images/waverror/success.wav");
							} else {
								$("#exceldeliverid").parent().show();

								$("#exceldeliverid").html(data.cwbdelivername);
								$("#msg").html(cwb + "         （" + data.errorinfo + "）");

								if (data.cwbdelivernamewav != pname + "/wav/") {
									$("#wavPlay", parent.document).attr("src",
											pname + "/wavPlay?wavPath=" + data.cwbdelivernamewav + "&a=" + Math.random());
									doWavAudio(data.cwbdelivernamewav);
								}
								if (data.cwbgaojia != undefined) {
									$("#cwbgaojia").parent().show();
									document.gaojia.Play();
								}
							}
							$("#scansuccesscwb").val(cwb);

						} else if (data.statuscode == "200029") {// 一票多件
							$("#cwbgaojia").parent().hide();
							$("#exceldeliverid").parent().show();

							$("#exceldeliverid").html(data.cwbdelivername);
							$("#msg").html(cwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
							$("#scansuccesscwb").val(cwb);
							if (data.cwbdelivernamewav != pname + "/wav/") {
								$("#wavPlay", parent.document).attr("src",
										pname + "/wavPlay?wavPath=" + data.cwbdelivernamewav + "&a=" + Math.random());
								doWavAudio(data.cwbdelivernamewav);
							}
							document.ypdj.Play();
							if (data.cwbgaojia != undefined) {
								$("#cwbgaojia").parent().show();
								document.gaojia.Play();
							}
						} else {
							$("#exceldeliverid").parent().hide();
							$("#cwbgaojia").parent().hide();
							$("#msg").html(cwb + "         （异常扫描）" + data.errorinfo);
						}
						errorvedioplay(pname, data);
						$("#responsebatchno").val(data.responsebatchno);
					}

				});
			}
		}
	}
}

/**
 * 包完成确认
 */
function cwbbalecheck(pname, driverid) {
	closeBox();
	/*
	 * if($("#scansuccessbale").val()==""){ $("#msg").html("请先扫描包"); return ;
	 * }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/cwbbalecheck?driverid=" + driverid,
		dataType : "json",
		success : function(data) {
			/*
			 * $("#successcwbnum").parent().show();
			 * $("#lesscwbnum").parent().show();
			 * 
			 * $("#successcwbnum").html(data.successbalenum);
			 * $("#lesscwbnum").html(data.losebalenum);
			 * 
			 * $("#driverid").parent().hide(); $("#baleno").parent().hide();
			 * $("#scancwb").parent().hide(); $("#msg").parent().hide();
			 * $("#exceldeliverid").parent().hide();
			 * $("#finish").parent().hide();
			 * 
			 * $("#driverid").val("-1"); $("#scansuccessbale").val("");
			 */
		}
	});
	/* } */
}
/**
 * 小件员领货扫描
 */
/*
 * var linghuoSuccessCount = 0; function
 * branchDeliver(pname,scancwb,deliverid,requestbatchno){
 * $("#msg").parent().show(); if(deliverid==-1){ $("#msg").html("请选择小件员");
 * return ; }else if(scancwb.length>0){ $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbbranchdeliver/"+scancwb+"?deliverid="+deliverid+"&requestbatchno="+requestbatchno,
 * dataType:"json", success : function(data) { $("#scancwb").val("");
 * $("#msg").parent().show(); if(data.statuscode=="000000"){
 * 
 * $("#exceldeliverid").parent().show();
 * 
 * $("#exceldeliverid").html(data.cwbdelivername);
 * 
 * $("#msg").html(scancwb+data.errorinfo+"
 * （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
 * 
 * $("#scansuccesscwb").val(scancwb); linghuoSuccessCount+=1;
 * $("#successcwbnum").html(linghuoSuccessCount);
 * 
 * if(data.body.cwbOrder.sendcarnum>1){ document.ypdj.Play(); }
 * 
 * if(data.cwbdelivernamewav!=pname+"/wav/"){
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+data.cwbdelivernamewav+"&a="+Math.random()); }
 * 
 * }else{ $("#exceldeliverid").parent().hide(); $("#msg").html(scancwb+"
 * （异常扫描）"+data.errorinfo); } errorvedioplay(pname,data);
 * $("#responsebatchno").val(data.responsebatchno); } }); } }
 */
// 小件员批量反馈根据不同的配送结果显示相应的原因以及支付方式
function checkpodresultid(id, podresultid) {
	if (id == "paywayid") {
		$("#paywayid_p").parent().show();
		$("#backreasonid_p").parent().hide();
		$("#leavedreasonid_p").parent().hide();
	} else if (id == "backreasonid") {
		$("#paywayid_p").parent().hide();
		$("#backreasonid_p").parent().show();
		$("#leavedreasonid_p").parent().hide();
	} else if (id == "leavedreasonid") {
		$("#paywayid_p").parent().hide();
		$("#backreasonid_p").parent().hide();
		$("#leavedreasonid_p").parent().show();
	}
	$("#backreasonid_p").val("0");
	$("#leavedreasonid_p").val("0");
	$("#podresultid_p").val(podresultid);
}

/**
 * 小件员批量反馈ddd
 */
function deliverpod(pname, deliverid, scancwb, podresultid, paywayid, backreasonid, leavedreasonid,firstlevelreasonid, deliverstateremark, type, isReasonRequired,
		jushou, zhiliu){  
	var cwbTr=$("tr[id='weifankui'] tr[id='"+scancwb.trim()+"']");
	if(cwbTr.length>0){
		 if($(cwbTr).attr("nofankui")=='1'){
			alert("订单号“"+scancwb+"”唯品会上门换业务的揽退单不允许直接反馈，请操作其相关联的配送单！");
			return false;
		 }
	 }
	
	if ($("#podresultid_p").val() == 0) {
		alert("请选择反馈结果");
		return false;
	}
	if ($("#podresultid_p").val() == jushou) {// 退货
		if (isReasonRequired == 'yes' && !(backreasonid > 0)) {
			alert("请选择退货原因");
			return false;
		}
	}
	if ($("#podresultid_p").val() == zhiliu) {// 滞留
		if (isReasonRequired == 'yes' && !(leavedreasonid > 0)) {
			alert("请选择滞留原因");
			return false;
		}
	}

	if ($("#podresultid_p").val() == 1 && $("#paywayid_p").val() == 0) {
		alert("请选择支付方式");
		return false;
	}
	if (deliverid == -1) {
		alert("请选择小件员");
		return false;
	} else {
		var arr = scancwb.split("\n");
		var newcwb = "";
		for (var i = 0; i < arr.length; i++) {
			if (arr[i].replace(/\s/g, "").length != 0) {
				newcwb += arr[i] + ",";
			}
		}
		newcwb = newcwb.substring(0, newcwb.length - 1);
		if (newcwb.length > 0 && podresultid != 0) {
			if (podresultid != 1) {
				paywayid = 0;
			}
			$.ajax({
				type : "POST",
				url : pname + "/PDA/cwbdeliverpod/" + newcwb,
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				dataType : "json",
				data : {
					deliverid : deliverid,
					podresultid : podresultid,
					paywayid : paywayid,
					backreasonid : backreasonid,
					leavedreasonid : leavedreasonid,
					firstlevelreasonid : firstlevelreasonid,
					deliverstateremark : encodeURI(deliverstateremark)
				},
				success : function(data) {
					if (data.statuscode != "000000") {
						var str = "反馈失败信息：\r\n";
						var failarr = data.errorinfo.split(";");
						var failcwbStr = "";
						for (var i = 0; i < failarr.length - 1; i++) {
							str += failarr[i].split("@")[0] + "(" + failarr[i].split("@")[1] + ")\r\n";
							failcwbStr = newcwb.replace(failarr[i].split("@")[0] + ",", "");
						}
						if (type == "N") {
							for (var k = 0; k < failcwbStr.split(",").length; k++) {
								$("tr[id='" + failcwbStr.split(",")[k] + "'] td").attr("style", "color:#FFF; background:#09C; font-weight:bold");
								$("#" + failcwbStr.split(",")[k] + "_cz").html("已反馈");
							}
						}

						alert(str);
					} else {
						$(".tishi_box", parent.document).html("反馈成功");
						$(".tishi_box", parent.document).show();
						setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
						if (type == "N") {
							for (var k = 0; k < newcwb.split(",").length; k++) {
								$("tr[id='" + newcwb.split(",")[k] + "'] td").attr("style", "color:#FFF; background:#09C; font-weight:bold");
								$("#" + newcwb.split(",")[k] + "_cz").html("已反馈");
							}
						}
					}
					$("#scancwb_p").val("");
					if (type == "N") {
						$("#flashpage").show();
						$("#subButton").attr("disabled", "disabled");
						;
					}
				}
			});
		}
	}
}

// 开始库存盘点
function beginscanstock(pname, branchid) {
	$("#msg").parent().show();
	if (branchid == -1) {
		$("#msg").html("请选择站点");
		return;
	} else {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/stockscanquery/" + branchid,
			dataType : "json",
			success : function(data) {
				$("#stockcwbnum").html("库存票数：" + data.stockcwbnum);

				$("#msg").parent().hide();
				$("#scancwb").parent().show();
				$("#cancelscancwb").parent().show();
				$("#scanfinish").parent().show();
				$("#successcwbnum").parent().show();
				$("#morecwbnum").parent().show();
				$("#lesscwbnum").parent().show();
			}
		});
	}
}

// 盘点扫描提交
function scanstockcwbsubmit(pname, scancwb, branchid) {
	if (scancwb.length > 0) {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/stockscansubmit/" + scancwb + "?branchid=" + branchid,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				if (data.statuscode == "000000" || data.statuscode == "200017") {
					if (data.errorinfo.length > 0) {
						$("#msg").parent().hide();
						$("#errorinfo").parent().show();
						$("#errorinfo").html(data.errorinfo);
					} else {
						$("#msg").parent().show();
						$("#errorinfo").parent().hide();
						$("#msg").html(scancwb + "(成功扫描)");
					}

					$("#scansuccesscwb").val(scancwb);

				} else {
					$("#msg").parent().show();
					$("#msg").html(data.errorinfo);
					errorvedioplay(pname, data);
				}
			}
		});
	}
}

// 盘点扫描取消
function scanstockcancel(pname, branchid) {
	$.ajax({
		type : "POST",
		url : pname + "/PDA/stockscancancel/" + branchid,
		dataType : "json",
		success : function(data) {
			$("#msg").html("盘点取消");
			$("#scancwb").parent().hide();
			$("#cancelscancwb").parent().hide();
			$("#scanfinish").parent().hide();
			$("#successcwbnum").parent().hide();
			$("#morecwbnum").parent().hide();
			$("#lesscwbnum").parent().hide();
		}
	});
}

// 盘点完成确认
function scanstockfinish(pname, branchid) {
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/stockscanfinish/" + branchid,
		dataType : "json",
		success : function(data) {
			$("#successcwbnum").html(data.successcwbnum);
			$("#morecwbnum").html(data.morecwbnum);
			$("#lesscwbnum").html(data.lesscwbnum);

			$("#beginscanstock").parent().hide();
			$("#msg").parent().hide();
			$("#stockcwbnum").parent().hide();
			$("#scancwb").parent().hide();
			$("#cancelscancwb").parent().hide();
			$("#scanfinish").parent().hide();
			$("#scansuccesscwb").val("");
			$("#requestbatchno").val("");
		}
	});
	/* } */
}

// 退货出站扫描
/*
 * var backsuccessnum = 0; function
 * branchbackexport(pname,scancwb,driverid,truckid,requestbatchno){
 * if(scancwb.length>0){ $("#close_box").hide(); $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbbranchbackexport/"+scancwb+"?driverid="+driverid+"&truckid="+truckid+"&requestbatchno="+requestbatchno,
 * dataType:"json", success : function(data) { $("#scancwb").val("");
 * 
 * if(data.statuscode=="000000"){ backsuccessnum += 1;
 * 
 * $("#msg").html(scancwb+" （成功扫描）");
 * $("#successscancwbnum").html(backsuccessnum);
 * 
 * $("#scansuccesscwb").val(scancwb);
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/success.wav&a="+Math.random());
 * 
 * }else{ $("#msg").html(scancwb+" （异常扫描）"+data.errorinfo); }
 * errorvedioplay(pname,data); $("#responsebatchno").val(data.responsebatchno); }
 * }); } } //退货出站扫描完成确认 function
 * branchbackexportfinish(pname,driverid,truckid,requestbatchno){ closeBox();
 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{ $.ajax({
 * type: "POST",
 * url:pname+"/PDA/cwbbranchfinishbackexport/"+driverid+"?truckid="+truckid+"&requestbatchno="+requestbatchno,
 * dataType:"json", success : function(data) { if(data.statuscode=="000000"){
 * $("#successcwbnum").parent().show();
 * $("#successcwbnum").html(data.successcwbnum);
 * 
 * $("#successscancwbnum").html("0"); $("#msg").parent().hide();
 * $("#msg").html("");
 * 
 * $("#driverid").parent().hide(); $("#truckid").parent().hide();
 * $("#baleid").parent().hide(); $("#scancwb").parent().hide();
 * $("#msg").parent().hide(); $("#successscancwbnum").parent().hide();
 * $("#finish").parent().hide();
 * 
 * $("#scansuccesscwb").val(""); $("#requestbatchno").val(""); }else{
 * $("#msg").html(data.errorinfo); errorvedioplay(pname,data); }
 * 
 * $("#truckid").val("-1"); $("#driverid").val("-1"); } }); } }
 */

// 中转出站扫描
/*
 * var changesuccessnum = 0; function
 * branchchangeexport(pname,scancwb,reasonid,driverid,truckid,requestbatchno){
 * if(scancwb.length>0){ $("#close_box").hide(); $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbbranchchangeexport/"+scancwb+"?reasonid="+reasonid+"&driverid="+driverid+"&truckid="+truckid+"&requestbatchno="+requestbatchno,
 * dataType:"json", success : function(data) { $("#scancwb").val("");
 * $("#msg").parent().show(); if(data.statuscode=="000000"){ changesuccessnum +=
 * 1;
 * 
 * $("#msg").html(scancwb+" （成功扫描）");
 * $("#successscancwbnum").html(changesuccessnum);
 * 
 * $("#scansuccesscwb").val(scancwb);
 * 
 * $("#wavPlay",parent.document).attr("src",pname+"/wavPlay?wavPath="+pname+"/images/waverror/success.wav&a="+Math.random());
 * }else{ $("#msg").html(scancwb+" （异常扫描）"+data.errorinfo); }
 * errorvedioplay(pname,data); $("#responsebatchno").val(data.responsebatchno); }
 * }); } } //中转出站扫描完成确认 function
 * branchchangeexportfinish(pname,driverid,truckid,requestbatchno){ closeBox();
 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{ $.ajax({
 * type: "POST",
 * url:pname+"/PDA/cwbbranchfinishchangeexport/"+driverid+"?truckid="+truckid+"&requestbatchno="+requestbatchno,
 * dataType:"json", success : function(data) { if(data.statuscode=="000000"){
 * $("#successcwbnum").parent().show();
 * $("#successcwbnum").html(data.successcwbnum);
 * 
 * $("#reasonid").parent().hide(); $("#driverid").parent().hide();
 * $("#truckid").parent().hide(); $("#scancwb").parent().hide();
 * $("#msg").parent().hide(); $("#successscancwbnum").parent().hide();
 * $("#finish").parent().hide();
 * 
 * $("#successscancwbnum").html("0"); $("#scansuccesscwb").val("");
 * $("#requestbatchno").val(""); }else{ $("#msg").html(data.errorinfo);
 * errorvedioplay(pname,data); }
 * 
 * $("#reasonid").val("-1"); $("#truckid").val("-1"); $("#driverid").val("-1"); }
 * }); } }
 */

// 库存盘点理货
/*
 * function scanstockCwb(pname,branchid){ $.ajax({ type: "POST",
 * url:pname+"/PDA/stockscanquery/"+branchid, dataType:"json", success :
 * function(data) { $("#nowkucundanshu",
 * parent.document).html(data.stockcwbnum); } }); }
 */

// 理货
function cwbscancwbbranch(pname, scancwb, type) {
	if (scancwb.length > 0) {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbscancwbbranch/" + scancwb,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				// var kc = parseInt($("#nowkucundanshu",
				// parent.document).html());
				if (data.statuscode == "000000") {
					$("#msg").html(scancwb + "         （成功扫描）");
					// kc = kc-1;
					// $("#nowkucundanshu", parent.document).html(kc);

					$("#cwbreceivablefee").html(data.cwbreceivablefee);
					if (data.cwbbranchnamewav != pname + "/wav/") {
						if (type == "zhandian") {
							$("#cwbbranchname").html(data.cwbbranchname + "-" + data.cwbdelivername);
							$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbdelivernamewav + "&a=" + Math.random());
						} else if (type == "kufang") {
							$("#cwbbranchname").html(data.cwbbranchname);
							$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						}
					}
				} else {
					$("#cwbreceivablefee").html("");
					$("#cwbbranchname").html("");
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
				}
				errorvedioplay(pname, data);
			}
		});
	}
}
/*
 * //退供货商出库 function cwbbacktocustomer(pname,scancwb){
 * $("#msg").parent().show(); if(scancwb.length>0){ $.ajax({ type: "POST",
 * url:pname+"/PDA/cwbbacktocustomer/"+scancwb, dataType:"json", success :
 * function(data) { $("#scancwb").val(""); $("#msg").parent().show();
 * if(data.statuscode=="000000"){ $("#msg").html(scancwb+" （成功扫描）");
 * 
 * $("#customername").parent().show();
 * $("#customername").html(data.customername); }else{ $("#msg").html(scancwb+"
 * （异常扫描）"+data.errorinfo); $("#customername").parent().hide(); }
 * errorvedioplay(pname,data); } }); } }
 */

/**
 * 中转站入库
 */
function changeimport(pname, scancwb, driverid, requestbatchno, baleno, sysintowarhouse) {
	if (scancwb.length > 0 || baleno.length > 0) {
		$("#close_box").hide();
		if (baleno.length > 0) {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/getcwbbybaleno/" + baleno + "?driverid=" + driverid + "&sysintowarhouse=" + sysintowarhouse,
				dataType : "json",
				success : function(data) {
					$("#baleno").val("");

					$("#msg").parent().show();
					if (data.errorinfo == "1") {
						$("#msg").html("包不存在！");
					} else {
						scancwb += data.cwbs;

						$("#msg").html(baleno + "         （包成功扫描）");
						$("#scansuccessbale").val(baleno);
					}
				}
			});
		}

		if (scancwb.length > 0) {
			for (var i = 0; i < scancwb.split(",").length; i++) {
				var cwb = scancwb.split(",")[i];
				$.ajax({
					type : "POST",
					url : pname + "/PDA/cwbchangeimport/" + cwb + "?driverid=" + driverid + "&confirmflag=0&requestbatchno=" + requestbatchno,
					dataType : "json",
					success : function(data) {
						$("#scancwb").val("");
						$("#msg").parent().show();
						if (data.statuscode == "000000") {
							$("#msg").html(cwb + "         （成功扫描）");

							$("#scansuccesscwb").val(cwb);
							$("#excelbranch").parent().show();

							$("#excelbranch").html(data.cwbbranchname);
							if (data.cwbbranchnamewav != pname + "/wav/") {
								$("#wavPlay", parent.document).attr("src",
										pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
								doWavAudio(data.cwbbranchnamewav);
							}

						} else if (data.statuscode == "200001") {// 有货无单
							$("#excelbranch").parent().hide();

							$("#msg").html(cwb + "         （非中转货未入库）");
						} else if (data.statuscode == "200029") {// 一票多件
							$("#msg").html(cwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
							$("#scansuccesscwb").val(cwb);
							$("#excelbranch").parent().show();

							$("#excelbranch").html(data.cwbbranchname);
							if (data.cwbbranchnamewav != pname + "/wav/") {
								$("#wavPlay", parent.document).attr("src",
										pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
								doWavAudio(data.cwbbranchnamewav);
							}
							document.ypdj.Play();
						} else {
							$("#excelbranch").parent().hide();
							$("#msg").html(cwb + "         （异常扫描）" + data.errorinfo);
						}
						errorvedioplay(pname, data);
						$("#responsebatchno").val(data.responsebatchno);
					}
				});
			}
		}
	}
}

/**
 * 中转站入库完成确认
 */
function changefinishimport(pname, driverid, requestbatchno) {
	closeBox();
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/cwbchangefinishimport/" + driverid + "?requestbatchno=" + requestbatchno,
		dataType : "json",
		success : function(data) {
			/*
			 * if(data.statuscode=="000000"){
			 * $("#successcwbnum").parent().show();
			 * $("#morecwbnum").parent().show();
			 * $("#lesscwbnum").parent().show();
			 * 
			 * $("#successcwbnum").html(data.successcwbnum);
			 * $("#morecwbnum").html(data.morecwbnum);
			 * $("#lesscwbnum").html(data.lesscwbnum);
			 * 
			 * $("#driverid").parent().hide(); $("#scancwb").parent().hide();
			 * $("#msg").parent().hide(); $("#baleno").parent().hide();
			 * $("#finish").parent().hide();
			 * 
			 * $("#scansuccesscwb").val(""); $("#requestbatchno").val("");
			 * }else{ $("#msg").html(data.errorinfo);
			 * errorvedioplay(pname,data); } $("#driverid").val("-1");
			 */
		}
	});
	/* } */
}

/**
 * 中转站出库扫描
 */
var successnum = 0, errorcwbnum = 0;
function changeexport(pname, scancwb, branchid, driverid, truckid, requestbatchno, baleno) {
	$("#msg").parent().show();
	if (branchid == -1) {
		$("#msg").html("请选择目的站");
		return;
	} else if (scancwb.length > 0) {
		$("#close_box").hide();
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbchangeexport/" + scancwb + "?branchid=" + branchid + "&driverid=" + driverid + "&truckid=" + truckid
					+ "&requestbatchno=" + requestbatchno + "&baleno=" + baleno,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				$("#msg").parent().show();
				if (data.statuscode == "000000") {
					successnum += 1;
					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);
					$("#msg").html(scancwb + "         （成功扫描）");

					$("#scansuccesscwb").val(scancwb);

					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
				} else {
					errorcwbnum += 1;
					$("#excelbranch").parent().hide();
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
				}
				errorvedioplay(pname, data);
				$("#responsebatchno").val(data.responsebatchno);
				$("#successcwbnum").html("成功扫描：" + successnum);
				$("#errorcwbnum").html("放错货：" + errorcwbnum);
			}
		});
	}
}

/**
 * 中转站出库扫描完成确认
 */
function changefinishexport(pname, branchid, driverid, truckid, requestbatchno) {
	closeBox();
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/cwbchangefinishexport?branchid=" + branchid + "&driverid=" + driverid + "&truckid=" + truckid + "&requestbatchno="
				+ requestbatchno,
		dataType : "json",
		success : function(data) {
			/*
			 * if(data.statuscode=="000000"){ $("#allcwbnum").parent().show();
			 * 
			 * $("#allcwbnum").html(data.successcwbnum);
			 * 
			 * $("#branchid").parent().hide(); $("#driverid").parent().hide();
			 * $("#truckid").parent().hide(); $("#baleid").parent().hide();
			 * $("#scancwb").parent().hide(); $("#msg").parent().hide();
			 * $("#excelbranch").parent().hide();
			 * $("#successcwbnum").parent().hide();
			 * $("#errorcwbnum").parent().hide(); $("#finish").parent().hide();
			 * 
			 * $("#scansuccesscwb").val(""); $("#requestbatchno").val("");
			 * }else{ $("#msg").html(data.errorinfo);
			 * errorvedioplay(pname,data); } $("#branchid").val("-1");
			 * $("#driverid").val("-1"); $("#truckid").val("-1");
			 */
		}
	});
	/* } */
}

/**
 * 退货站入库
 */
function backimport(pname, scancwb, driverid, requestbatchno, baleno, sysintowarhouse) {
	if (scancwb.length > 0 || baleno.length > 0) {
		$("#close_box").hide();
		if (baleno.length > 0) {
			$.ajax({
				type : "POST",
				url : pname + "/PDA/getcwbbybaleno/" + baleno + "?driverid=" + driverid + "&sysintowarhouse=" + sysintowarhouse,
				dataType : "json",
				success : function(data) {
					$("#baleno").val("");
					$("#msg").parent().show();
					if (data.errorinfo == "1") {
						$("#msg").html("包不存在！");
					} else {
						scancwb += data.cwbs;
						$("#msg").html(baleno + "         （包成功扫描）");
						$("#scansuccessbale").val(baleno);
					}
				}
			});
		}

		if (scancwb.length > 0) {
			for (var i = 0; i < scancwb.split(",").length; i++) {
				var cwb = scancwb.split(",")[i];
				$.ajax({
					type : "POST",
					url : pname + "/PDA/cwbbackimport/" + cwb + "?driverid=" + driverid + "&confirmflag=0&requestbatchno=" + requestbatchno,
					dataType : "json",
					success : function(data) {
						$("#scancwb").val("");
						$("#msg").parent().show();
						if (data.statuscode == "000000") {
							$("#customername").parent().show();
							$("#customername").html(data.cwbcustomername);

							$("#msg").html(cwb + "         （成功扫描）");

							$("#scansuccesscwb").val(cwb);

						} else if (data.statuscode == "200001") {// 有货无单
							$("#customername").parent().hide();

							$("#msg").html(cwb + "         （非退货未入库）");
						} else if (data.statuscode == "200029") {// 一票多件
							$("#msg").html(cwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
							$("#customername").parent().show();
							$("#customername").html(data.cwbcustomername);

							$("#scansuccesscwb").val(cwb);
							document.ypdj.Play();
						} else {
							$("#customername").parent().hide();

							$("#msg").html(cwb + "         （异常扫描）" + data.errorinfo);
						}
						errorvedioplay(pname, data);
						$("#responsebatchno").val(data.responsebatchno);
					}
				});
			}
		}
	}
}

/**
 * 退货站入库完成确认
 */
function backfinishimport(pname, driverid, requestbatchno) {
	closeBox();
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/cwbbackfinishimport/" + driverid + "?requestbatchno=" + requestbatchno,
		dataType : "json",
		success : function(data) {
			/*
			 * if(data.statuscode=="000000"){
			 * $("#successcwbnum").parent().show();
			 * $("#morecwbnum").parent().show();
			 * $("#lesscwbnum").parent().show();
			 * 
			 * $("#successcwbnum").html(data.successcwbnum);
			 * $("#morecwbnum").html(data.morecwbnum);
			 * $("#lesscwbnum").html(data.lesscwbnum);
			 * 
			 * $("#driverid").parent().hide(); $("#baleno").parent().hide();
			 * $("#scancwb").parent().hide(); $("#msg").parent().hide();
			 * $("#finish").parent().hide();
			 * 
			 * $("#scansuccesscwb").val(""); $("#requestbatchno").val("");
			 * }else{ $("#msg").html(data.errorinfo);
			 * errorvedioplay(pname,data); } $("#driverid").val("-1");
			 */
		}
	});
	/* } */
}

/**
 * 退货站再投扫描
 */
var successnum = 0, errorcwbnum = 0;
function backexport(pname, scancwb, branchid, driverid, truckid, requestbatchno, baleno) {
	$("#msg").parent().show();
	if (branchid == -1) {
		$("#msg").html("请选择目的站");
		return;
	} else if (scancwb.length > 0) {
		$("#close_box").hide();
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbbackexport/" + scancwb + "?branchid=" + branchid + "&driverid=" + driverid + "&truckid=" + truckid
					+ "&requestbatchno=" + requestbatchno + "&baleno=" + baleno,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				$("#msg").parent().show();
				if (data.statuscode == "000000") {
					successnum += 1;
					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);

					$("#msg").html(scancwb + "         （成功扫描）");

					$("#scansuccesscwb").val(scancwb);

					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
				} else {
					errorcwbnum += 1;
					$("#excelbranch").parent().hide();
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
				}
				errorvedioplay(pname, data);

				$("#responsebatchno").val(data.responsebatchno);
				$("#successcwbnum").html("成功扫描：" + successnum);
				$("#errorcwbnum").html("放错货：" + errorcwbnum);
			}
		});
	}
}

/**
 * 退货站再投扫描完成确认
 */
function backfinishexport(pname, branchid, driverid, truckid, requestbatchno) {
	closeBox();
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/PDA/cwbbackfinishexport?branchid=" + branchid + "&driverid=" + driverid + "&truckid=" + truckid + "&requestbatchno="
				+ requestbatchno,
		dataType : "json",
		success : function(data) {
			/*
			 * if(data.statuscode=="000000"){ $("#allcwbnum").parent().show();
			 * 
			 * $("#allcwbnum").html(data.successcwbnum);
			 * 
			 * $("#branchid").parent().hide(); $("#driverid").parent().hide();
			 * $("#truckid").parent().hide(); $("#baleid").parent().hide();
			 * $("#scancwb").parent().hide(); $("#msg").parent().hide();
			 * $("#excelbranch").parent().hide();
			 * $("#successcwbnum").parent().hide();
			 * $("#errorcwbnum").parent().hide(); $("#finish").parent().hide();
			 * 
			 * $("#scansuccesscwb").val(""); $("#requestbatchno").val("");
			 * }else{ $("#msg").html(data.errorinfo);
			 * errorvedioplay(pname,data); } $("#branchid").val("-1");
			 * $("#driverid").val("-1"); $("#truckid").val("-1");
			 */
		}
	});
	/* } */
}

/**
 * 供货商拒收返库
 */
var refusenum = 0;
function cwbcustomerrefuseback(pname, scancwb, remarkcontent) {
	if (scancwb.length > 0) {
		$.ajax({
			type : "POST",
			url : pname + "/PDA/cwbcustomerrefuseback/" + scancwb,
			data : {
				remarkcontent : remarkcontent
			},
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				if (data.statuscode == "000000") {
					refusenum += 1;
					$("#msg").html(scancwb + "         （成功扫描）");
				} else {
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
				}
				errorvedioplay(pname, data);
				$("#successcwbnum").html(refusenum);
			}
		});
	}
}

/**
 * 二级分拨扫描
 */
function cwbresetbranch(pname, scancwb, branchid, requestbatchno) {
	if (branchid == -1) {
		$("#msg").html("请选择子站点");
	} else if (scancwb.length > 0) {
		$("#close_box").hide();
		$.ajax({
			type : "POST",
			url : pname + "/cwborderPDA/cwbresetbranch/" + scancwb + "?branchid=" + branchid + "&requestbatchno=" + requestbatchno,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");
				if (data.statuscode == "000000") {
					if ($("#scansuccesscwb").val() != scancwb) {
						successnum += 1;
					}
					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);
					$("#msg").html(scancwb + "         （成功扫描）");
					$("#branchid").val(data.cwbbranchid);
					$("#scansuccesscwb").val(scancwb);
					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
				} else if (data.statuscode == "200029") {// 一票多件

					$("#excelbranch").parent().show();

					$("#excelbranch").html(data.cwbbranchname);
					$("#msg").html(scancwb + "         （一票多件，共" + data.cwbsendcarnum + "件，已扫" + data.cwbscannum + "件）");
					$("#branchid").val(data.cwbbranchid);
					$("#scansuccesscwb").val(scancwb);
					if (data.cwbbranchnamewav != pname + "/wav/") {
						$("#wavPlay", parent.document).attr("src", pname + "/wavPlay?wavPath=" + data.cwbbranchnamewav + "&a=" + Math.random());
						doWavAudio(data.cwbbranchnamewav);
					}
					document.ypdj.Play();
				} else {
					errorcwbnum += 1;
					$("#excelbranch").parent().hide();
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
					errorvedioplay(pname, data);
				}
				$("#responsebatchno").val(data.responsebatchno);
				$("#successcwbnum").html("成功扫描：" + successnum);
				$("#errorcwbnum").html("放错货：" + errorcwbnum);
			}
		});
	}
}

/**
 * 二级分拨扫描完成确认
 */
function cwbresetbranchtocheck(pname, branchid, requestbatchno) {
	closeBox();
	/*
	 * if($("#scansuccesscwb").val()==""){ $("#msg").html("请先扫描订单"); }else{
	 */
	$.ajax({
		type : "POST",
		url : pname + "/cwborderPDA/cwbresetbranchcheck?branchid=" + branchid + "&requestbatchno=" + requestbatchno,
		dataType : "json",
		success : function(data) {
			/*
			 * if(data.statuscode=="000000"){ $("#allcwbnum").parent().show();
			 * 
			 * $("#branchid").parent().hide(); $("#driverid").parent().hide();
			 * $("#truckid").parent().hide(); $("#baleid").parent().hide();
			 * $("#scancwb").parent().hide(); $("#msg").parent().hide();
			 * $("#excelbranch").parent().hide();
			 * $("#successcwbnum").parent().hide();
			 * $("#errorcwbnum").parent().hide(); $("#finish").parent().hide();
			 * 
			 * $("#allcwbnum").html(data.successcwbnum);
			 * $("#scansuccesscwb").val(""); $("#requestbatchno").val("");
			 * }else{ $("#msg").html(data.errorinfo);
			 * errorvedioplay(pname,data); } $("#branchid").val("-1");
			 * $("#driverid").val("-1"); $("#truckid").val("-1");
			 */
		}
	});
	/* } */
}

// 库对库退货出库扫描
function warehousebackexport(pname, scancwb, requestbatchno) {
	if (scancwb.length > 0) {
		$("#close_box").hide();
		$.ajax({
			type : "POST",
			url : pname + "/cwborderPDA/warehouseback/" + scancwb + "?requestbatchno=" + requestbatchno,
			dataType : "json",
			success : function(data) {
				$("#scancwb").val("");

				if (data.statuscode == "000000") {

					$("#msg").html(scancwb + "         （成功扫描）");

					$("#scansuccesscwb").val(scancwb);
					$("#wavPlay", parent.document).attr("src",
							pname + "/wavPlay?wavPath=" + pname + "/images/waverror/success.wav&a=" + Math.random());
					doWavAudio(pname + "/images/waverror/success.wav");

				} else {
					$("#msg").html(scancwb + "         （异常扫描）" + data.errorinfo);
					errorvedioplay(pname, data);
				}
				$("#responsebatchno").val(data.responsebatchno);
			}
		});
	}
}

function check_bale() {
	if ($("#baleno").val().length == 0) {
		$("#msg").html("包号不能为空");
		return false;
	}
	return true;
}

function allCheck(pname) {
	var arr = getCheckValue();
	if (arr == "") {
		alert("请选择需要处理的订单");
	} else {
		for (var i = 0; i < arr.split(",").length; i++) {
			$.ajax({
				type : "POST",
				url : pname + "/cwborderPDA/controlCwbForHanlder/" + arr.split(",")[i],
				success : function(data) {
					alert("批量处理成功！");
					$("#searchFormFlash").submit();
				}
			});
		}
	}
}

function errorvedioplay(pname, data) {
	// $("#wavPlay", parent.document).attr("src",
	// pname + "/wavPlay?wavPath=" + data.wavPath + "&a=" + Math.random());
	var url = pname + "/images/waverror/fail.wav";
	newPlayWav(url);
}
function numbervedioplay(pname, numStr) {
	// $("#wavPlay", parent.document).attr("src",
	// pname + "/wavPlay?wavPath=" + data.wavPath + "&a=" + Math.random());
	var url = pname + "/images/wavnums/" + numStr + ".wav";
	newPlayWav(url);
}

function successvedioplay(pname, data) {
	// $("#wavPlay", parent.document).attr("src",
	// pname + "/wavPlay?wavPath=" + data.wavPath + "&a=" + Math.random());
	var url = pname + "/images/waverror/success.wav";
	newPlayWav(url);
}

function playGoodsTypeWav(contextPath, data) {
	var goodsTypeWav = data.body.goodsTypeWav;
	if (goodsTypeWav != "") {
		newPlayWav(contextPath + goodsTypeWav);
	}
}

function newPlayWav(url) {
//	if ($.browser.mozilla || $.browser.msie) {
//		$("#wav").remove();
//		$("body").append("<bgsound id='wav' src='" + url + "' autostart='true' width='100' height='100' volume='0'/>");
//	} else {
//		$("#wav").remove();
//		$("body").append("<embed id='wav' src='" + url + "'width='0' height='0' loop='1' autostart='false' hidden='true'/>");
//	}
	doWavAudio(url);
}
function testTemp(){
	console.info("ssssssssssssssssss");
}
// obj=[{"time":1000,url:"/images/waverror/fail.wav"}];
function batchPlayWav(obj) {
	
	var waitTime = 100;
	for (var i = 0; i < obj.length; i++) {
		var wavObj = obj[i];
	
		setTimeout(_play(wavObj.url,i), waitTime);
		

		waitTime += wavObj.time;
		
	}
}

function _play(url,i) {
	return function() {

/*		if ($.browser.msie && $.browser.version == "8.0") {
			$("#wav").remove();
			$("body").append("<bgsound id='wav' src='" + url + "' width='100' height='100'  volume='0'/>");
			
		} else {
			$("#wav").remove();
			$("body").append("<embed id='wav' src='" + url + "' width='0' height='0'/>");
		
		}*/

//		if ($.browser.msie && $.browser.version == "8.0") {
//			$("#wav").remove();
//			$("body").append("<bgsound id='wav' src='" + url + "' width='0' height='0'  volume='0'/>");
//		} else {
//			$("#wav").remove();
//			$("body").append("<embed id='wav' src='" + url + "' width='0' height='0'/>");
//		}
		doWavAudio(url,i);

	};
}

/** ***********************************************************PDA功能js**************************************************** */

/** ***************************************pos对接相关验证*****begin********************************************** */

function check_alipay() {
	if ($("#requester").val() == '') {
		alert("请输入请求方!");
		return false;
	}
	if ($("#targeter").val().length == '') {
		alert("请输入应答方!");
		return false;
	}
	if ($("#privateKey").val().length == '') {
		alert("请输入加密私钥!");
		return false;
	}
	if ($("#publicKey").val().length == '') {
		alert("请输入解密公钥!");
		return false;
	}

	return true;
}

function check_yeepay() {
	if ($("#requester").val() == '') {
		alert("请输入请求方!");
		return false;
	}
	if ($("#targeter").val().length == '') {
		alert("请输入应答方!");
		return false;
	}
	if ($("#privatekey").val().length == '') {
		alert("请输入加密密钥!");
		return false;
	}

	return true;
}

function check_bill99() {
	if ($("#requester").val() == '') {
		alert("请输入请求方!");
		return false;
	}
	if ($("#targeter").val().length == '') {
		alert("请输入应答方!");
		return false;
	}

	return true;
}

function check_unionpay() {
	if ($("#private_key").val() == '') {
		alert("请输入加密信息!");
		return false;
	}
	if ($("#request_url").val().length == '') {
		alert("请输入请求URL!");
		return false;
	}

	return true;
}

/** *****************************************pos对接相关验证******end************************************************************ */

/*----------代理--------------*/
function check_proxy() {
	if ($("#ip").val().length == 0) {
		alert("请填写代理ip");
		return false;
	}
	if ($("#port").val().length == 0) {
		alert("请填写端口号");
		return false;
	}
	if ($("#pass").val().length == 0) {
		alert("请填写密码");
		return false;
	}
	return true;
}
// 删除
function delproxy(key) {
	getDelBox(key);
}
function getDelBox(key) {
	$.ajax({
		type : "POST",
		url : $("#delproxy").val() + key,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			viewBox();
		}
	});
}
function check_proxy1() {
	if ($("#pass").val().length == 0) {
		alert("请填写密码");
		return false;
	}
	return true;
}
function submitDelForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				//$("#WORK_AREA")[0].contentWindow.delSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

				closeBox();
			}
		}
	});
}
/*----------代理END--------------*/

/*--------模板-----------*/
function initSetExport(fieldid) {
	$("#se_" + fieldid).attr("checked", "checked");
	$("#fc_" + fieldid).css("color", "red");
}

function getSetExportViewBox(key) {
	$.ajax({
		type : "POST",
		url : $("#view").val() + key,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			editInit();
			viewBox();
		}
	});
}
function checkMould() {
	if ($("#mouldname").val().length == 0) {
		alert("请填写模版名称！");
		return false;
	}

	if (!($(".multiSelectOptions  input[name='roleid']:checked ").size() > 0)) {
		alert("请选择角色");
		return false;
	}
	return true;
}

/**
 * 修改生产导出模板 验证是否已选择 角色 模板名
 * 
 * @returns {Boolean}
 */
function checkMouldEdit() {
	if ($("#mouldname").val().length == 0) {
		alert("请填写模版名称！");
		return false;
	}
	if (!($("#roleid").val() >= 0)) {
		alert("请选择角色");
		return false;
	}
	return true;
}
/*----------投诉处理--------------*/
function checkNum(id) {
	// 如用户倍数框留空，光标离开倍数输入框，则倍数输入框默认为1.

	if ($('#' + id).val() == '' || $('#' + id).val() == undefined || $('#' + id).val() == null || Number($('#' + id).val()) < 0) {
		$('#' + id).val('');
		$('#' + id).focus();
		$('#' + id).select();
	}
	// 自动转换为半角，不支持标点、小数点以及英文字母等其他输入。
	var pattern = /^-?\d+$/;
	if (isNaN($('#' + id).val()) || $('#' + id).val().search(pattern) != 0) {
		$('#' + id).val('');
		$('#' + id).focus();
		$('#' + id).select();
		return false;
	}
	return true;
}
function check_Complaint() {
	if ($("#ccwb").val().length == 0) {
		alert("请填写投诉单号！");
		return false;
	}
	if ($("#cuserdesc").val().length == 0) {
		alert("请填写被投诉人备注！");
		return false;
	}
	if ($("#ccontactman").val().length == 0) {
		alert("请填写投诉人！");
		return false;
	}
	if ($("#cphone").val().length == 0) {
		alert("请填写投诉人联系电话！");
		return false;
	}
	if ($("#ccontent").val().length == 0) {
		alert("请填写投诉内容！");
		return false;
	}
	return true;
}

/*----------投诉处理END--------------*/

/*----------短信账户管理--------------*/
function check_sysconfig() {
	if ($("#pass").val().length == 0) {
		alert("请填写密码");
		return false;
	}
	return true;
}
// 弹出填写密码的框
function getSysconfigBox() {
	$.ajax({
		type : "POST",
		url : $("#alertsms").val(),
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			viewBox();
		}
	});
}

function submitSysconfigForm(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				//$("#WORK_AREA")[0].contentWindow.saveSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

				closeBox();
			}
		}
	});
}
/*----------短信账户管理END--------------*/

/*---------异常码对接设置--------------------------*/
function changethisB2cFlag(obj, key) {
	if (obj == key) {
		$("#b2cflag").show();
	} else {
		$("#b2cflag").hide();
	}

}
function check_exptreason(obj) {
	if ($("#support_key").val() == -1) {
		alert("请选择异常码提供方!");
		return false;
	}

	if ($("#expt_code").val() == '') {
		alert("请填入异常码!");
		return false;
	}
	if ($("#expt_msg").val() == '') {
		alert("请填入异常码描述!");
		return false;
	}
	if($("#customerid").val()==""){
		layer.tips('供货商不能为空!', '#customerid',{
			time :2,
			more:true
		});
		return;
	}
	if($("#customercode").val()==""){
		layer.tips('供货商编码不能为空!', '#customercode',{
			time :2,
			more:true
		});
		return;
	}

	return true;
}

function check_exptreasonn(obj) {
	
	if($("#customerid").val()=="-1"){
		alert('供货商不能为空!');
		return false;
	}
	if($("#customercode").val()==""){
		alert('供货商编码不能为空!');
		return false;
	}

	return true;
}



// 异常码配对显示 非空校验
function check_exptCodeMatch() {
	if ($("#expt_type").val() == -1) {
		alert("请选择异常原因类型!");
		$("#expt_type").focus();
		return false;
	}
	if ($("#reasonid").val() == -1) {
		alert("请选择公司异常原因!");
		$("#reasonid").focus();
		return false;
	}
	if ($("#support_key").val() == -1) {
		alert("请选择异常码提供方!");
		$("#support_key").focus();
		return false;
	}
	if ($("#exptsupportreason").val() == -1) {
		alert("请选择异常码/描述信息!");
		$("#exptsupportreason").focus();
		return false;
	}

	return true;
}
function updateExptReasonByType(URL, expt_type) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "expt_type=" + expt_type,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#reasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择异常原因</option>").appendTo("#reasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].reasonid + "'>" + json[j].reasoncontent + "</option>").appendTo("#reasonid");
			}
		}
	});
}
// 提供方异常编码联动
function updateExptReasonSupportByType(URL, support_key, expt_type) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "expt_type=" + expt_type + "&support_key=" + support_key,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#exptsupportreason").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择异常原因</option>").appendTo("#exptsupportreason");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].exptid + "'>" + (json[j].expt_code + '===' + json[j].expt_msg) + "</option>").appendTo(
						"#exptsupportreason");
			}
		}
	});
}

// 提供方异常编码联动
function updateExptReasonSupportByCode(URL, support_key, expt_type) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "expt_type=" + expt_type + "&support_key=" + support_key,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#exptsupportreason").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择异常原因</option>").appendTo("#exptsupportreason");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].id + "'>" + (json[j].expt_code + '===' + json[j].expt_msg) + "</option>")
						.appendTo("#exptsupportreason");
			}
		}
	});
}
/*-----------------------------------*/

// ///////////支付方式设置////////////////
function check_paywayid() {
	if ($("#paywayid").val() < 0) {
		alert("请选择支付方式");
		return false;
	}
	return true;
}

// ///////////查询未缴款明细////////////////
function getNoPayUpDetailView() {
	$.ajax({
		type : "POST",
		url : $("#view").val(),
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
			viewBox();
		}
	});
}

// ///////////库房对应省市管理////////////////
function check_warehousekey() {
	if ($("#targetcarwarehouseid").val() < 0) {
		alert("请选择目标仓库");
		return false;
	}
	if ($.trim($("#keyname").val()).length == 0) {
		alert("请填写库房对应省市关键字");
		return false;
	}
	return true;
}

// ///////////////////交接单封包功能///////////////////////////
function setowgfengbao(pname, owgid) {
	$.ajax({
		type : "POST",
		url : "owgtofengbao/",
		url : pname + "/warehousegroup/owgtofengbao/" + owgid,
		dataType : "html",
		success : function(data) {
		}
	});
}


// ///////////////////默认导出模板/////////////////////////////
function setDefaultExportViewBox() {
	$.ajax({
		type : "POST",
		url : $("#view").val(),
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			editInit();
			viewBox();
		}
	});
}
function editDefailtExpButton() {
	getDefaultExpEditBox();
}
function getDefaultExpEditBox() {
	$.ajax({
		type : "POST",
		url : $("#edit").val(),
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			editInit();// 初始化ajax弹出页面
			viewBox();
		}
	});
}

function clickswitch(name) {
	if ($("#" + name).attr("checked") == "checked") {
		$("#ck_switch").val("ck_01");
	} else {
		$("#ck_switch").val("ck_02");
	}
}

function check_printtemplate() {
	if ($("#name").val().length == 0) {
		alert("模版名称不能为空");
		return false;
	}
	if ($("#detail").val().length == 0) {
		alert("模版内容不能为空");
		return false;
	}
	return true;
}

function getcontents() {
	var contents = "";
	$('input[name="content"]:checked').each(function() { // 由于复选框一般选中的是多个,所以可以循环输出
		contents += $(this).val() + ",";
	});
	$("#detail").val(contents);
}

/**
 * 暂时增加，之后会删掉
 */
$(function() {
	$("#add_button").click(function() {
		getAddBox();
		
	});

});
function getAddBox() {
	$.ajax({
		type : "POST",
		url : $("#add").val(),
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);
			
		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}

/**
 * 
 * 
 * 
 */
$(function() {
	$("#add_buttonn").click(function() {
		getAddBox1();
	});

});
function getAddBox1() {
	$.ajax({
		type : "POST",
		url : $("#add").val(),
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}



function checkbeizhu() {
	if ($("#comment", parent.document).val().length == 0) {
		alert("备注不能为空");
		return false;
	}
	return true;
}

function check_userAndbranch() {
	if ($("#userid", parent.document).val() == 0) {
		alert("用户不能为空");
		return false;
	}
	if ($("#branchid", parent.document).val() == 0) {
		alert("可查询站点不能为空");
		return false;
	}
	return true;
}

function check_name() {
	if ($("#name").val().length == 0) {
		alert("问题件类型不能为空");
		return false;
	}
	return true;
}

function check_describe() {
	console.info($("#describe").val());
	if ($("#describe").val().length == 0) {
		alert("请填写处理内容");
		return false;
	}
	return true;
}

function check_describeAdd() {
	console.info($("#describe").val());
	if ($("#describe").val().length == 0||$("#describe").val()=='最多100个字') {
		alert("请填写处理内容");
		return false;
	}
	return true;
}
//处理问题件弹出框时的判断
function check_describeAndDutyInfo() {
	if($("#dutybranchid").val()==0||$("#dutyname").val()==0){
		alert("请选择责任机构与责任人！！");
		return false;
	}
	console.info($("#describe").val());
	if($("#describe").val().length>100){
		alert("处理内容过长，请合理输入！！");
	}
	if ($("#describe").val().length == 0||$("#describe").val()=='最多100个字') {
		alert("请填写处理内容");
		return false;
	}
	return true;
}

function check_userbranch(userJobnumIsNeed) {
	if ($("#realname").val().length == 0) {
		alert("员工姓名不能为空");
		return false;
	}
	if ($("#username").val().length == 0) {
		alert("员工登录名不能为空");
		return false;
	}
	if (!isLetterAndNumber($("#username").val())) {
		alert("员工登录名格式不正确");
		return false;
	}
	
	if ($("#username").val().length > 9) {
		alert("登录名长度不能超过9位！");
		return false;
	}
	
	// 网页登录密码, start
	if ($("#webPassword").val().length == 0) {
		alert("网页登录密码不能为空");
		return false;
	}
	if ($("#webPassword1").val() != $("#webPassword").val()) {
		alert("网页登录密码两次输入不一致");
		return false;
	}
	if(!isPasswordValidForWeb($('#webPassword').val())){
		alert("网页登录密码至少要含小写字母、大写字母、数字、特殊字符中的任意三种，且长度至少需要八位");
		return false;
	}
	if(!isUsernamePasswordNotContainEachOther($('#username').val(), $('#webPassword').val())){
		alert("用户名与网页登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
		return false;
	}
	// 网页登录密码, end
	// POS登录密码, start
	if ($("#password").val().length == 0) {
		alert("POS登录密码不能为空");
		return false;
	}
	if ($("#password1").val() != $("#password").val()) {
		alert("POS登录密码两次输入不一致");
		return false;
	}
	if(!isPasswordValidForPDA($('#password').val())){
		alert("POS登录密码必须为数字，不能过于简单，至少要含三个不同数字，数字不能为日期，且长度至少需要六位");
		return false;
	}
	if(!isUsernamePasswordNotContainEachOther($('#username').val(), $('#password').val())){
		alert("用户名与POS登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
		return false;
	}
	// POS登录密码, end
	
	if ($("#usermobile").val().length == 0) {
		alert("员工手机不能为空");
		return false;
	//}else if ($("#usermobile").val().length != 11 || isMobileNumber($("#usermobile").val()) == false) {
	}else if ( isNumber($("#usermobile").val()) == false) {
		 alert("手机号码格式有误!"); 
		 return false; 
	}
	if ($("#idcardno").val().length == 0) {
		alert("身份证号不能为空!");
		return false;
	}
	if(userJobnumIsNeed && !$("#jobnum").val()){
		alert("工号不能为空");
		return false;
	}
	var creandsave = $("#creandsave").val();
	var checkuserformid;
	if(creandsave==0){
		checkuserformid = "user_cre_Form";
	}else if(creandsave==1){
		checkuserformid = "user_save_Form";
	}
	var s1 = $("#"+checkuserformid+" input[id=maxcutpayment]").val();
	var s2 = $("#"+checkuserformid+" input[id=basicadvance]").val();
	var s3 = $("#"+checkuserformid+" input[id=lateradvance]").val();
	var s4 = $("#"+checkuserformid+" input[id=fixedadvance]").val();
	var s5 = $("#"+checkuserformid+" input[id=basicfee]").val();
	var s6 = $("#"+checkuserformid+" input[id=areafee]").val();
	//re.test(s2)||re.test(s3)||re.test(s4)
	//^[0-9]+[\.][0-9]{0,2}$
	//var regu = "^[0-9].*$";
	var regu = "^[+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
	var re = new RegExp(regu);
	if (s1!=''&&!re.test(s1)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s2!=''&&!re.test(s2)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s3!=''&&!re.test(s3)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s4!=''&&!re.test(s4)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s5!=''&&!re.test(s5)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	if (s6!=''&&!re.test(s6)) {
		alert("请输入正确格式的金额(正数)!");
		return false;
	}
	
	/*
	 * if ($("#usermobile").val().length != 11 ||
	 * isMobileNumber($("#usermobile").val()) == false) { alert("手机号码格式有误!");
	 * return false; }
	 */
	return true;
}

// /////////投诉审核验证////////////////
function check_complaint_update() {
	if ($("#auditType").val() == -1) {
		alert("请选择审核结果");
		return false;
	} else if ($("#auditRemarkid").val().length == 0) {
		alert("请输入备注");
		return false;
	} else if ($("#auditRemarkid").val().length > 100) {
		alert("备注内容不能超过100个字符");
		return false;
	}
	return true;
}

function check_complaint_create() {
	if ($("#cbranchid").val() == -1) {
		alert("请选择站点");
		return false;
	} else if ($("#cdeliveryid").val() == -1) {
		alert("请选择投诉对象");
		return false;
	} else if ($("#servertreasonid").val() == -1) {
		alert("请选择服务投诉类型");
		return false;
	} else if ($("#contentid").val().length == 0) {
		alert("请输入客户要求");
		return false;
	} else if ($("#contentid").val().length > 500) {
		alert("客户要求内容不能超过500个字符");
		return false;
	}
	return true;
}
// ///////投诉审核验证////////////////

function updateBranchDeliver(path) {
	$.ajax({
		url : path + "/advancedquery/updateDeliver",// 后台处理程序
		type : "POST",// 数据发送方式
		data : "branchid=" + $("#cbranchid").val(),// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#cdeliveryid").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择小件员</option>").appendTo("#cdeliveryid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].userid + "'>" + json[j].realname + "</option>").appendTo("#cdeliveryid");
			}
		}
	});
}

function updateComplaintFrom(type) {
	if (type == 1) {
		$("#smsid").val(1);
		$("#complaint_cuijian_Form").submit();
	} else {
		$("#smsid").val(0);
		$("#complaint_cuijian_Form").submit();
	}
}

function upOrDown(id, pname, level) {
	$.ajax({
		type : "POST",
		url : pname + "/setexportcwb/editlevel/" + id + "/" + level,
		dataType : "json",
		success : function(data) {
		}
	});
}

/** ****************************************修复订单的配送结果BEGIN***************************************** */
function init_deliverystate_edit() {
	$("#backreasonid").parent().parent().hide();
	$("#leavedreasonid").parent().parent().hide();
	$("#podremarkid").parent().parent().hide();
	$("#infactfee").parent().parent().hide();
	$("#returnedfee").parent().parent().hide();
	$("#receivedfeecash").parent().parent().hide();
	$("#receivedfeepos").parent().parent().hide();
	$("#posremark").parent().parent().hide();
	$("#receivedfeecheque").parent().parent().hide();
	$("#receivedfeeother").parent().parent().hide();
	$("#checkremark").parent().parent().hide();
	$("#deliverstateremark").parent().parent().hide();
}

function gonggong_edit() {
	$("#podremarkid").parent().parent().show();
	$("#deliverstateremark").parent().parent().show();
}
function peisongObj_edit() {
	gonggong_edit();
	$("#receivedfeecash").parent().parent().show();
	$("#receivedfeepos").parent().parent().show();
	$("#posremark").parent().parent().show();
	$("#receivedfeecheque").parent().parent().show();
	$("#receivedfeeother").parent().parent().show();
	$("#checkremark").parent().parent().show();
}
function shagnmentuiObj_edit() {
	gonggong_edit();
	$("#backreasonid").parent().parent().show();
	$("#returnedfee").parent().parent().show();
}
function shangmenhuanObj_edit() {
	gonggong_edit();
	// $("#leavedreasonid").parent().show();
	if ($("#isReceivedfee").val() == "yes") {
		peisongObj_edit();
	} else {
		$("#returnedfee").parent().parent().show();
	}
}

function quantuiObj_edit() {
	gonggong_edit();
	$("#backreasonid").parent().parent().show();
}

function bufentuihuoObj_edit() {
	// gonggong();
	peisongObj_edit();
	$("#backreasonid").parent().parent().show();

	// $("#shouldfee").parent().show();
	// $("#infactfee").parent().show();
}
function zhiliuObj_edit() {
	gonggong_edit();
	$("#leavedreasonid").parent().parent().show();
	
}

function zhongzhuanObj_edit() {
	gonggong_edit();
	$("#changereasonid").parent().parent().show();
	$("#firstchangereasonid").parent().show();
}

function shangmenjutuiObj_edit() {
	gonggong_edit();
	$("#backreasonid").parent().parent().show();
}
function huowudiushiObj_edit() {
	gonggong_edit();
}
// 监控配送状态变化 对显示字段做相应处理
function click_podresultid_edit(PeiSongChengGong, ShangMenTuiChengGong, ShangMenHuanChengGong, JuShou, BuFenTuiHuo, FenZhanZhiLiu, ShangMenJuTui,
		HuoWuDiuShi, backreasonid, leavedreasonid, podremarkid, newpaywayid) {
	var podresultid = parseInt($("#podresultid").val());
	init_deliverystate_edit();
	if (podresultid == PeiSongChengGong) {// 配送成功
		for (var i = 0; i < newpaywayid.split(",").length; i++) {
			var newpayway = newpaywayid.split(",")[i];
			if (newpayway == 1) {
				$("#receivedfeecash").val(
						(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
								- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
			} else if (newpayway == 2) {
				$("#receivedfeepos").val(
						(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
								- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
			} else if (newpayway == 3) {
				$("#receivedfeecheque").val(
						(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
								- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
			} else if (newpayway == 4) {
				$("#receivedfeeother").val(
						(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
								- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
			} else {
				$("#receivedfeecash").val(
						(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
								- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
			}
		}

		peisongObj_edit();
	} else if (podresultid == ShangMenTuiChengGong) {// 上门退成功
		$("#returnedfee").val($("#shouldfee").val());
		shagnmentuiObj_edit();
	} else if (podresultid == ShangMenHuanChengGong) {// 上门换成功
		shangmenhuanObj_edit();
	} else {
		$("input[id*='receivedfee']").val(0.00);
		$("#returnedfee").val(0.00);
		if (podresultid == JuShou) {// 拒收
			quantuiObj_edit();
		} else if (podresultid == BuFenTuiHuo) {// 部分退货
			for (var i = 0; i < newpaywayid.split(",").length; i++) {
				var newpayway = newpaywayid.split(",")[i];
				if (newpayway == 1) {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 2) {
					$("#receivedfeepos").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 3) {
					$("#receivedfeecheque").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecash").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				} else if (newpayway == 4) {
					$("#receivedfeeother").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeecash").val()) * 100) / 100);
				} else {
					$("#receivedfeecash").val(
							(parseFloat($("#shouldfee").val()) * 100 - parseFloat($("#receivedfeepos").val()) * 100
									- parseFloat($("#receivedfeecheque").val()) * 100 - parseFloat($("#receivedfeeother").val()) * 100) / 100);
				}
			}
			bufentuihuoObj_edit();
		} else if (podresultid == FenZhanZhiLiu) {// 分站滞留
			zhiliuObj_edit();
		} else if (podresultid == ShangMenJuTui) {// 上门拒退
			shangmenjutuiObj_edit();
		} else if (podresultid == HuoWuDiuShi) {// 货物丢失
			// 如果是货物丢失，就将实收现金和实收款赋值成当前应收款对应的值，如果是退货丢失的话，应该还钱，所以不需要赋值到退货款中
			$("#infactfee").val($("#shouldfee").val());
			$("#receivedfeecash").val($("#shouldfee").val());
			huowudiushiObj_edit();
		}
	}

	$("#backreasonid").val(backreasonid);
	$("#leavedreasonid").val(leavedreasonid);
	$("#podremarkid").val(podremarkid);
	// $("#remandtype").val(0);
	centerBox();
}

// 验证字段是否正确输入
function check_deliveystate_edit(PeiSongChengGong, ShangMenTuiChengGong, ShangMenHuanChengGong, JuShou, BuFenTuiHuo, FenZhanZhiLiu, ShangMenJuTui,
		HuoWuDiuShi) {
	var podresultid = parseInt($("#podresultid").val());
	if (podresultid == -1) {
		return checkGongGong_delivery_edit();
	}

	if (podresultid == PeiSongChengGong) {// 配送成功
		return checkPeiSong_edit();
	} else if (podresultid == ShangMenTuiChengGong) {// 上门退成功
		return checkShangMenTui_edit();
	} else if (podresultid == ShangMenHuanChengGong) {// 上门换成功
		return checkShangMenHuan_edit();
	} else if (podresultid == BuFenTuiHuo) {// 部分退货
		return checkGongGong_delivery_edit();
	} else if (podresultid == JuShou) {// 拒收
		return checkGongGong_delivery_edit();
	} else if (podresultid == FenZhanZhiLiu) {// 分站滞留
		return checkGongGong_delivery_edit();
	} else if (podresultid == ShangMenJuTui) {// 上门拒退
		return checkGongGong_delivery_edit();
	} else if (podresultid == HuoWuDiuShi) {// 货物丢失
		return checkGongGong_delivery_edit();
	}

	return true;
}

function checkGongGong_delivery_edit() {
	if (parseInt($("#podresultid").val()) == -1) {
		alert("请选择配送结果");
		return false;
	}
	return true;
}

function checkPeiSong_edit() {
	if (!checkGongGong_delivery_edit()) {
		return false;
	}

	if ((parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
			+ parseFloat($("#receivedfeeother").val()) != parseFloat($("#shouldfee").val()))) {
		alert("配送成功时，总金额不等于应收款，不可以反馈");
		return false;
	}
	return true;
}

function checkShangMenTui_edit() {
	if (!checkGongGong_delivery_edit()) {
		return false;
	}
	if (parseFloat($("#returnedfee").val()) != parseFloat($("#shouldfee").val())) {
		alert("退还金额应该为" + parseFloat($("#shouldfee").val()));
		$("#returnedfee").val(parseFloat($("#shouldfee").val()));
		return false;
	}
	return true;
}

function checkShangMenHuan_edit() {
	if (!checkGongGong_delivery_edit()) {
		return false;
	}
	if ((parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
			+ parseFloat($("#receivedfeeother").val()) + parseFloat($("#returnedfee").val()) > parseFloat($("#shouldfee").val()))) {
		alert("货款金额不能大于应处理金额");
		return false;
	}
	if ($("#isReceivedfee").val() == "yes"
			&& (parseFloat($("#receivedfeecash").val()) + parseFloat($("#receivedfeepos").val()) + parseFloat($("#receivedfeecheque").val())
					+ parseFloat($("#receivedfeeother").val()) != parseFloat($("#shouldfee").val()))) {
		alert("上门换成功时，总金额不等于应收款，不可以反馈");
		return false;
	}

	if ($("#isReceivedfee").val() == "no" && parseFloat($("#returnedfee").val()) != parseFloat($("#shouldfee").val())) {
		alert("上门换成功时，退还现金不等于应退款，不可以反馈");
		return false;
	}
	return true;
}

/** ****************************************修复订单的配送结果END***************************************** */

/** ****************************************财务功能弹框验证与提交***************************************** */
function check_deliveraccountEdit() {
	if ($("#deliverpayupamount").val() == "") {
		$("#deliverpayupamount").val("0");
	}
	if (!isFloat($("#deliverpayupamount").val())) {
		alert("调整现金金额必须为数字！");
		return false;
	}
	if ($("#deliverpayupamount_pos").val() == "") {
		$("#deliverpayupamount_pos").val("0");
	}
	if (!isFloat($("#deliverpayupamount_pos").val())) {
		alert("调整POS金额必须为数字！");
		return false;
	}
	$('#sub').attr('disabled', 'disabled');
	return true;

}
function submitDeliveraccountEdit(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == "0") {
				//$("#WORK_AREA")[0].contentWindow.saveSuccess(data);
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

				closeBox();
			}
		}
	});
}
/** ****************************************财务功能弹框验证与提交END***************************************** */

/** ****************************************修改订单相关js***************************************** */
function getEditOrderList(listUrl) {
	$.ajax({
		type : "POST",
		url : listUrl,
		dataType : "html",
		success : function(data) {
			$("#alert_box", parent.document).html(data);
			viewBox();
		}
	});
}
/**
 * ****************************************修改订单相关js
 * END*****************************************
 */

// //////////下载数字/////////////
$(function() {
	$(".down_li").hover(function() {
		$(".down_xiangqing").show();
	}, function() {
		$(".down_xiangqing").hide();
	});
});

/** ******************************************入库、出库扫描页面****************************************************** */
function ypdjruku(path, pagenum, ypdjrukupage, customerid) {
	ypdjrukupage += 1;
	$.ajax({
		type : "post",
		url : path + "/PDA/getInQueListPage",
		data : {
			"page" : ypdjrukupage,
			"customerid" : customerid
		},
		success : function(data) {
			if (data.length > 0) {
				var optionstring = "";
				for (var i = 0; i < data.length; i++) {
					optionstring += "<tr id='" + data[i].cwb + "'  cwb='" + data[i].cwb + "' customerid='" + data[i].customerid + "' nextbranchid="
							+ data[i].nextbranchid + ">" + "<td width='120' align='center'>" + data[i].cwb + "</td>"
							+ "<td width='120' align='center'>" + data[i].transcwb + "</td>" + "<td width='100' align='center'> "
							+ data[i].customername + "</td>" + "<td width='140' align='center'> " + data[i].emaildate + "</td>"
							+ "<td width='100' align='center'> " + data[i].consigneename + "</td>" + "<td width='100' align='center'> "
							+ data[i].receivablefee + "</td>" + "<td  align='left'> " + data[i].consigneeaddress + "</td>" + "</tr>";
				}
				$("#ypdjrk").remove();
				$("#lesscwbTable").append(optionstring);
				if (data.length == pagenum) {
					var more = '<tr align="center"  ><td  colspan="7" style="cursor:pointer" onclick=ypdjruku("' + path + '",' + pagenum + ','
							+ ypdjrukupage + ',' + customerid + '); id="ypdjrk">查看更多</td></tr>';
					$("#lesscwbTable").append(more);
				}
			}
		}
	});
}

function ypdjchuku(path, pagenum, ypdjchukupage, nextbranchid) {
	ypdjchukupage += 1;
	$.ajax({
		type : "post",
		url : path + "/PDA/getOutQueListPage",
		data : {
			"page" : ypdjchukupage,
			"nextbranchid" : nextbranchid
		},
		success : function(data) {
			if (data.length > 0) {
				var optionstring = "";
				for (var i = 0; i < data.length; i++) {
					optionstring += "<tr id='" + data[i].cwb + "'  cwb='" + data[i].cwb + "' customerid='" + data[i].customerid + "' nextbranchid="
							+ data[i].nextbranchid + ">" + "<td width='120' align='center'>" + data[i].cwb + "</td>"
							+ "<td width='120' align='center'>" + data[i].transcwb + "</td>" + "<td width='100' align='center'> "
							+ data[i].customername + "</td>" + "<td width='140' align='center'> " + data[i].emaildate + "</td>"
							+ "<td width='100' align='center'> " + data[i].consigneename + "</td>" + "<td width='100' align='center'> "
							+ data[i].receivablefee + "</td>" + "<td  align='left'> " + data[i].consigneeaddress + "</td>" + "</tr>";
				}
				$("#ypdjck").remove();
				$("#lesscwbTable").append(optionstring);
				if (data.length == pagenum) {
					var more = '<tr align="center"  ><td  colspan="7" style="cursor:pointer" onclick=ypdjchuku("' + path + '",' + pagenum + ','
							+ ypdjchukupage + ',' + nextbranchid + '); id="ypdjck">查看更多</td></tr>';
					$("#lesscwbTable").append(more);
				}
			}
		}
	});
}

/** ************************************************************************************************ */

function check_epaiApi(obj) {
	if ($("#userCode").val() == '') {
		alert("请输入用户编码!");
		$("#userCode").focus();
		return false;
	}

	if ($("#customerid").val() == -1) {
		alert("请选择上游电商!");
		$("#customerid").focus();
		return false;
	}
	if ($("#getOrder_url").val() == '') {
		alert("请输入获取订单URL!");
		$("#getOrder_url").focus();
		return false;
	}
	if ($("#pageSize").val() == '') {
		alert("请输入每次获取数量!");
		$("#pageSize").focus();
		return false;
	}
	if ($("#feedback_url").val() == '') {
		alert("请输入状态回传URL!");
		$("#feedback_url").focus();
		return false;
	}

	if ($("#private_key").val() == '') {
		alert("请输入密钥信息!");
		$("#private_key").focus();
		return false;
	}
	if ($("#warehouseid").val() == -1) {
		alert("请选择订单导入库房!");
		$("#warehouseid").focus();
		return false;
	}
	return true;
}

function changemskshow(branchtype) {

	if (branchtype == 2) {
		$("#bindmsksid").parent().show();
	} else {
		$("#bindmsksid").parent().hide();
	}

}

function checkPayamount() {
	if ($("#payamount").val().length == 0) {
		alert("请填写赔偿金额");
		return false;
	}
	if (parseFloat($("#payamount").val()) != parseFloat($("#caramount").val())) {
		alert("赔偿金额应该为" + parseFloat($("#caramount", parent.document).val()));
		return false;
	}
	return true;
}

function pageComimit(path, begintime, endtime, customerid, page) {
	$.ajax({
		type : "POST",
		url : path + "/percent/show/" + begintime + "/" + endtime + "/" + customerid + "/" + page,
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#table11", parent.document).html('');
			var htmltr = "<table width=\"1000\"  border=\"0\" cellspacing=\"1\" cellpadding=\"0\" class=\"table_2\" id=\"gd_table\">"
					+ "<tr class=\"font_1\">" + "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\" >订单号</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\" >供货商</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\" >发货时间</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >订单类型</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >当前状态</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >配送结果</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >入库仓库</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >上一站</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\"  >当前站</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\" >下一站</td>"
					+ "<td  align=\"center\" valign=\"middle\" bgcolor=\"#eef6ff\">配送站点</td>" + "</tr>" +

					"</table>";
			$("#table11", parent.document).html(htmltr);
		}
	});
}

// ///////自定义款项设置////////////////
function check_accountfeetype() {
	if ($("#feetype").val() == 0) {
		alert("款项类型不能为空");
		return false;
	}
	if ($("#feetypename").val() == "") {
		alert("名称不能为空");
		return false;
	}
	return true;
}
// ///////自定义款项明细设置////////////////
function check_accountfeedetail() {
	if ($("#feetypeid").val() == 0) {
		alert("款项不能为空");
		return false;
	}
	if ($("#branchid").val() == 0) {
		alert("站点不能为空");
		return false;
	}
	if ($("#customfee").val() == "") {
		alert("金额不能为空");
		return false;
	}
	if (!isFloat($("#customfee").val())) {
		alert("金额输入的数字格式不正确");
		return false;
	}
	return true;
}
// ///////自定义款项明细设置END////////////////
// js读取url参数 根据获取html的参数值控制html页面输出
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return 0;
}
function checkIsDelivery() {
	if ($("#cb_isDelivery").attr("checked") == "checked") {
		$("#cb_isDelivery").val(1);
	} else {
		$("#cb_isDelivery").val(0);
	}
}

// 品信中转原因匹配 非空校验
function check_transferResasonMatch() {
	if ($("#transferReasonid").val() == -1) {
		alert("请选择固定中转原因!");
		$("#transferReasonid").focus();
		return false;
	}
	if ($("#reasonid").val() == -1) {
		alert("请选择常用语中转原因!");
		$("#reasonid").focus();
		return false;
	}

	return true;
}





/////////////////////运费交款////////////////////////////////////

function checkFClick(){
	if($("#feetransfer").val()!=""){
		if(!isFloat($("#feetransfer").val())){
			alert("转账金额应为数字");
			return false;
		}
	}
	if($("#feecash").val()!=""){
		if(!isFloat($("#feecash").val())){
			alert("现金金额应为数字");
			return false;
		}
	}
	
	if((zfhjFee*100)>(infactyingjiao*100)){
		alert("超额支付！");
		return false;
	}
	
	if((zfhjFee*100)<(infactyingjiao*100)){
		alert("您有未交款,请检查交款金额！");
		return false;
	}
	
	if(confirm("确定交款吗？")){
 		$("#checkF").attr("disabled","disabled");
    	$("#checkF").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/accountcwbfare/payfare',
    		data:$('#createForm').serialize(),
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				sumitForm();
    			}else{
    				alert(data.error);
    			}
    		}
    	});
	}
}

/////////////////////////////////////////////////////////
function initMultipSelect(){
	
	
 //	$("#toBranchId").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
}

function ghhg(){
	var sitetype = $("#sitetype").val();
	if (sitetype == -1) {
		$('#la').css('color','red');
		$('#la').show();
		setTimeout('hidelalast()',3000);
	}
	
}

function hidelalast(){
	$("#la").hide();
}


//报声
function doWavAudio(wavPath,name){
	//console.info(wavPath);
	//<audio id="wavPlay1"></audio>
	$("#wav"+name).remove();
	$("body").append("<audio id='wav"+name+"' src='" + wavPath + "'  />");
	var audioElement = document.getElementById('wav'+name);
	audioElement.load();
	audioElement.play();
}

//验证
function validate(id){
	  var reg = new RegExp("^[0-9]*$");
	  var obj = document.getElementById(id);
	if(!reg.test(obj.value)){
	   alert("请输入数字!");
	   obj.value="";
	   //return false;
	}

	}







function updatechangelevel(URL, firstchangereasonid) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "firstchangereasonid=" + firstchangereasonid,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#changereasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value ='0'>==请选择==</option>").appendTo("#changereasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].reasonid +" '>" + json[j].reasoncontent + "</option>").appendTo("#changereasonid");
			}
		}
	});
}


function check_branchroute() {
	if ($("#fromBranchId").val() == 0) {
		alert("当前站点不能为空");
		return false;
	}
	if ($("#toBranchId").val==0) {
		alert("目的站点不能为空");
		return false;
	}
	if ($("#type").val() == 0) {
		alert("流向方向不能为空");
		return false;
	}
	return true;
}

function editoffenword(){
	if($('#reasontype').val()==1){
		$('#selectapply').removeAttr('hidden');
	}else{
		$('#selectapply').attr('hidden','true');
	}
}

function SubmitHandleabnormalBatch(flag) {
	$("#ishandle").val(flag);
}
function detail(key) {
	$.ajax({
		type : "POST",
		url : $("#detail").val() + key,
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);
		},
		complete : function() {
			editInit();// 初始化ajax弹出页面
			viewBox();
		}
	});
}
function setFlag(url) {
var replyDetail=$("#replyDetail").val();
	$.ajax({
		url : url, // 后台处理程序
		type : "POST",// 数据发送方式
		dataType : 'json',// 接受数据格式
		data:{"replyDetail":replyDetail},
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				closeBox();
				$("#WORK_AREA")[0].contentWindow.addSuccess(data);
			}
		}
	});
}


function getfirstvalue(URL, expt_type) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "expt_type=" + expt_type,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#firstreasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>==一级滞留==</option>").appendTo("#firstreasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].reasonid + "'>" + json[j].reasoncontent + "</option>").appendTo("#firstreasonid");
			}
		}
	});
}


function reasonByLevel(URL, expt_type){
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "firstreasonid=" + expt_type,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#reasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择异常原因</option>").appendTo("#reasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value='" + json[j].reasonid + "'>" + json[j].reasoncontent + "</option>").appendTo("#reasonid");
			}
		}
	});
}

function whenhidden(){
	
	$("#div_2").attr('hidden','true');
	$("#divs").attr('hidden','true');
	$("#div_changealowflag").attr('hidden','true');
	if($("#reasontype").val()==2||$("#reasontype").val()==13||$("#reasontype").val()==15||$("#reasontype").val()==16||$("#reasontype").val()==17||$("#reasontype").val()==18)
	{
		$("#divs").removeAttr('hidden');
		if($("#radio2").attr('checked')||$("#radio2").attr('checked')=='checked'){
			$("#div_2").removeAttr('hidden');
		}
	}
	
	$("#div_changealowflag").attr('hidden');
	if($("#reasontype").val()==1)
	{
		$("#divs").removeAttr('hidden');
		$("#div_changealowflag").removeAttr('hidden');
		
		if($("#radio2").attr('checked')||$("#radio2").attr('checked')=='checked'){
			$("#div_2").removeAttr('hidden');
			$("#div_changealowflag").attr('hidden');
		}
	}
	if($("#reasontype").val()==15)
	{
		$("#div_intercept").removeAttr('hidden');
	}
	
} 




function to_change(flag){
	/*if($("#reasontype").val()==1||$("#reasontype").val()==13)
	{
		$("#divs").removeAttr('hidden');
			if(flag==1){
				$("#div_2").attr('hidden','true');
			}
			else 
			{
				$("#div_2").removeAttr('hidden');
			}
	}*/
	
	if($("#reasontype").val()==1||$("#reasontype").val()==2||$("#reasontype").val()==13||$("#reasontype").val()==15||$("#reasontype").val()==16||$("#reasontype").val()==17||$("#reasontype").val()==18)
	{
	
		$("#divs").removeAttr('hidden');
		if($("#reasontype").val()==2||$("#reasontype").val()==13){
			if(flag==1){
				$("#div_2").attr('hidden','true');
			}else{
				$("#div_2").removeAttr('hidden');
			}
		}else{
			if(flag==1){
				$("#div_2").attr('hidden','true');
				$("#div_changealowflag").removeAttr('hidden');
			}else{
				$("#div_2").removeAttr('hidden');
			}
		}
		/*if(flag==1){
			$("#div_2").attr('hidden','true');
			$("#div_changealowflag").removeAttr('hidden');
		}
		else 
		{
			$("#div_2").removeAttr('hidden');
			$("#div_changealowflag").attr('hidden','true');
		}*/
	}
	
}




function updaterelatelevel(URL, firstlevelreasonid) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "firstlevelreasonid=" + firstlevelreasonid,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#leavedreasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value ='0'>==请选择==</option>").appendTo("#leavedreasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].reasonid +" '>" + json[j].reasoncontent + "</option>").appendTo("#leavedreasonid");
			}
		}
	});
	
	
}

function updaterelatelevel2(URL, firstlevelreasonid) {
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : "firstzhiliureason=" + firstlevelreasonid,// 参数
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#leavedreasonid").empty();// 清空下拉框//$("#select").html('');
			$("<option value ='0'>==请选择==</option>").appendTo("#leavedreasonid");// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].reasonid +" '>" + json[j].reasoncontent + "</option>").appendTo("#leavedreasonid");
			}
		}
	});
}

function getfirstlevel(flag){
	$("#first").attr('hidden',true); 
	if(flag==2){
		$("#first").attr('hidden',false);
	}
	
}

function getReasonByFirstReason(URL,targetid,requestValue) {
	var reasontypes=$("#reasontype").val();
	
	
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : {
			reasontype : reasontypes
		},
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#"+targetid).empty();// 清空下拉框//$("#select").html('');
			$("<option value ='0'>==请选择==</option>").appendTo("#"+targetid);// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].reasonid +" '>" + json[j].reasoncontent + "</option>").appendTo("#"+targetid);
			}
		}
	});
}


function getSecondReasonByFirstreasonid(URL,targetid,firstreasonid) {
	
	$.ajax({
		url : URL, // 后台处理程序
		type : "POST",// 数据发送方式
		data : {
			firstreasonid : firstreasonid
		},
		dataType : 'json',// 接受数据格式
		success : function(json) {
			$("#"+targetid).empty();// 清空下拉框//$("#select").html('');
			$("<option value ='0'>==请选择==</option>").appendTo("#"+targetid);// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].reasonid +" '>" + json[j].reasoncontent + "</option>").appendTo("#"+targetid);
			}
		}
	});
}





function selectbranchUsers(targetid,oldid){
	$.ajax({
		url:$("#getbranchusers").val(),
		type:"POST",
		data:"dutybranchid="+$("#"+oldid).val(),
	dataType:'json',
	success:function (json){
		$("#"+targetid).empty();
		$("<option value ='0'>==请选择机构责任人==</option>").appendTo("#"+targetid);// 添加下拉框的option
		for (var j = 0; j < json.length; j++) {
			$("<option value=' "+ json[j].userid +" '>"+json[j].realname+"</option>").appendTo("#"+targetid);
		}
	}
	});
}

var addupoadfileid=1;

function addupoadfile(){
	document.getElementById("uploadfile").innerHTML+='<div id="div_'+addupoadfileid+'"><input  name="file" type="file"  /><input type="button" value="删除"  onclick="uploadfiledel('+addupoadfileid+')"/></div>';  
	addupoadfileid= addupoadfileid + 1;  
}
function uploadfiledel(o){
    document.getElementById("uploadfile").removeChild(document.getElementById("div_"+o));  
}

//上传文件初始化上传组件
function uploadFormInitAdd(form, contextPath) {
	$('#swfupload-control').swfupload({
		upload_url : $("#" + form, parent.document).attr("action"),
		file_size_limit : "10240",
		file_types : "*.img;*.txt;*.jpg;*.gif;",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : contextPath + "/js/swfupload/swfupload.swf",
		button_image_url : contextPath + "/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#upbutton")[0]
	}).bind('fileQueued', function(event, file) {
		$("#wavText").val(file.name);
		$("#wavText").attr("selectedUploadFile", true);
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {
		/*
		 * 进度条 var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		 * $("#progressbar").progressbar({ value : percent });
		 * $("#progressstatus").text(percent);
		 */

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
		$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
		// $("#wavText").val("");
		/*if (dataObj.errorCode == 0) {
			alert("处理成功");
		}*/
		// setTimeout(queryProgress, 10);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}
//问题件处理提交
function submitAbnormalHH(form) {

	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "abnormalOrder/SubmitHandleabnormalwithNofile");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitAbnormalLoad();
}
//问题件处理与修改的时候
function submitAbnormalLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid', $("#dutybranchid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutyname', $("#dutyname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwb', $("#cwb", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'isfind', $("#isfind", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
//问题件结案处理
function submitAbnormalLast(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "abnormalOrder/submitHandleabnormalResult");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitAbnormalLastLoad();
}
//问题件结案处理
function submitAbnormalLastLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dealresult', $("#dealresult", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid', $("#dutybranchid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutyname', $("#dutyname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwb', $("#cwb", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
//修改创建的问题件
function reviseAbnormalCreateData(form){
	
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "abnormalOrder/SubmitReviseQuestion");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitReviseAbnormalCreate();
}
function submitReviseAbnormalCreate(){
	$('#swfupload-control').swfupload('addPostParam', 'abnormaltypeid', $("#abnormaltypeid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'ids', $("#ids", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
//判断申诉页面的弹出框的相关的条件
function check_punishshensucondition(){
	if($("#shensutype").val()==0){
		alert("请输入申诉类型！");
		return false;
	}
	if($("#describe").val()==""||$("#describe").val()=="最多100个字"){
		alert("请输入申诉说明！");
		return false;
	}
	if($("#describe").val().length>100){
		alert("输入的申诉说明不能 超过100个字噢！！");
		return false;
	}
	return true;
}
//对内扣罚申诉
function reviseShensuToSubmit(form){
	
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "inpunish/submitPunishShensu");
		submitCreateForm(form);
		return;
	}
	
	$("#update")[0].contentWindow.submitPunishShensu();
	$("#shensutype").val(0);
	$("#describe").val("最多100个字");
	$("#update")[0].contentWindow.chuShihuatiaojian();

}
function submitPunishShensu(){
	$('#swfupload-control').swfupload('addPostParam', 'shensutype', $("#shensutype", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'ids', $("#ids", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
//处理结案操作弹出框的判断
function check_dealJieanResulet() {
	if($("#dealresult").val()==0){
		alert("请选择问题件处理结果！！");
		return false;
	}
	if($("#describe").val().length>100){
		alert("结案处理说明过长！！");
		return false;
	}
	console.info($("#describe").val());
	if ($("#dutybranchid").val() == 0||$("#dutyname").val()==0) {
		alert("请选择最终审判的机构与机构当事人！！");
		return false;
	}
	return true;
}
//问题件下载
function filedownloadwithquestionfile(){
	if($("#filepathsumsize").val()<=1){
		alert("没有更多的附件能下载！！");
	}
	$("#morefiles").show();
	$("#morefilebutton").hide();
}

/**modify by bruce.shangguan 20160903
 * 根据订单创建对内扣罚单的时候的校验
 * @returns {Boolean}
 */
function check_createbycwb(){
	var cwbgoodprice = jQuery("#cwbgoodprice").val().trim() ;
	var cwbqitaprice = jQuery("#cwbqitaprice").val().trim() ;
	if($("#cwb").val()==""){
		alert("请输入订单号！！");
		return false;
	}else if($("#punishbigsort").val()==0){
		alert("请选择扣罚大类！！");
		return false;
	}else if($("#dutybranchid").val()==0){
		alert("请输入责任机构！！");
		return false;
	}
	// add by bruce shangguan 20160903 根据订单号创建对内扣罚单时，验证扣罚金额是否为数值
	if(!isFloat(cwbgoodprice)){
		alert("货物扣罚金额必须是数值！");
		jQuery("#cwbgoodprice").select() ;
		return false ;
	}
	if(!isFloat(cwbqitaprice)){
		alert("其它扣罚金额必须是数值！");
		jQuery("#cwbqitaprice").select() ;
		return false ;
	}
	// end by bruce shangguan 20160903
	if($("#punishdescribe").val().length>100){
		alert("扣罚说明不能超过100个字噢！！");
		return false;
	}
	if($("#punishprice").val()==""||$("#punishprice").val()=="0"||$("#punishprice").val()=="0.00"){
		if(confirm("当前扣罚金额为0元，您确定要继续创建吗")){
			return true;
		}
		return false;
	}
	return true;
}
//根据订单号创建对内扣罚单
function submitPunishCreateBycwb(form){
	console.info("hhh");
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "inpunish/createbycwbwithoutfile");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitPunishInsideByCwb();
}
function submitPunishInsideByCwb(){
	$('#swfupload-control').swfupload('addPostParam', 'cwb', $("#cwb", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwbstate', $("#cwbstate", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwbprice', $("#cwbprice", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishbigsort', $("#punishbigsort", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishsmallsort', $("#punishsmallsort", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid', $("#dutybranchid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutyname', $("#dutyname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishprice', $("#punishprice", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishdescribe', $("#punishdescribe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishinsidetype', $("#punishinsidetype", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'flowordertype', $("#flowordertype", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwbgoodprice', $("#cwbgoodprice", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'cwbqitaprice', $("#cwbqitaprice", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
/*function btnswd(ev){
	$('#cls').val(ev);
	alert(ev);
	$.ajax({
				type:'POST',
				data:$('#WorkOrderQueryForm').serialize(),
				url:$('#WorkOrderQueryForm').attr('action'),
				dataType:'json',				
				success:function(data){
					alert(data.error);
				}
	});
	closeBox();
}*/
/*
function getSV(v){
	$('#stateNum').val(v);
}*/
function btnccwo(Val){
	$('#cwo').val(Val);		
	$.ajax({
				type:'POST',
				data:$('#ComplainWorkOrderF').serialize(),
				url:$('#ComplainWorkOrderF').attr('action'),
				dataType:'json',				
				success:function(data){
					
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0) {
					    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

					   }  		
					
				}
	});
	closeBox();
	$('#cwo').val('');	
	$('#WaitAcceptNo').val("");
	$('#GV').val('');
	
	}

function smsSend(){
	$('#cwo').val("1");	
	$.ajax({
		type:'POST',
		data:$('#ComplainWorkOrderF').serialize(),
		url:"workorder/smsSend",
		dataType:'json',				
		success:function(data){
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
			} 	
		}
	});
	closeBox();
}

function btnupdate(csv){
	$('#cwo').val(csv);
	$.ajax({
				type:'POST',
				data:$('#UpdateComplainWorkOrderF').serialize(),
				url:$('#UpdateComplainWorkOrderF').attr('action'),
				dataType:'json',				
				success:function(data){
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0) {  
					    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

					   }  
					}
				});
	closeBox();
	}
	
 function getAddBoxx2(valWo) {
$.ajax({
	type : "POST",
	data:"workorder="+valWo,
	url : $("#GoOnacceptWo").val(),
	dataType : "html",
	success : function(data) {
		// alert(data);
		$("#alert_box", parent.document).html(data);
		
	},
	complete : function() {
		addInit();// 初始化某些ajax弹出页面			
		viewBox();
	}
});
}
 
 function addcaller(){
	 
	 if(Callerifnull())		
	 $.ajax({
			type : "POST",
			data:$('#addcallerForm').serialize(),
			url : $("#addcallerForm").attr('action'),
			dataType : "json",
			success : function(data) {
				if(data.errorCode==0){
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0) { 
					    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

					   }  
						closeBox();
					}
				}
				
			});
	 
 }
 
 function editcaller(){
	 if(Callerifnull())
	 $.ajax({
			type : "POST",
			data:$('#editcallerForm').serialize(),
			url : $("#editcallerForm").attr('action'),
			dataType : "json",
			success : function(data) {
				if(data.errorCode==0){
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0) { 
					    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

					   }  
						closeBox();
					}
				}
				
			});
	 
 }
 
function AlreadyVerify(AVV){
	$('#AlreadyVerifycomplaintState').val(AVV);
}
	


function btnswd(ev){
			$('#cls').val(ev);
			$.ajax({
						type:'POST',
						data:$('#WorkOrderQueryForm').serialize(),
						url:$('#WorkOrderQueryForm').attr('action'),
						dataType:'json',				
						success:function(data){
							$(".tishi_box").html(data.error);
							   $(".tishi_box").show();
							   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
							   if (data.errorCode == 0){
							    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

							   }  
						}
			});
			closeBox();

}

function getSV(v){
	$('#stateNum').val(v);
}
function btnccwo(Val){
	if($('#stateNum').val()==$('#bcl').val()){
		$('#cwo').val($('#yjs').val());	
			
	}else{
		$('#cwo').val(Val);	
		
	}
	
	$.ajax({
				type:'POST',
				data:$('#ComplainWorkOrderF').serialize(),
				url:$('#ComplainWorkOrderF').attr('action'),
				dataType:'json',				
				success:function(data){
					
					alert(data.error);
					
				}
	});
	closeBox();
	$('#cwo').val('');	
	$('#WaitAcceptNo').val("");
	$('#GV').val('');
	
	}


function btnupdate(csv){
	$('#cwo').val(csv);
	$.ajax({
				type:'POST',
				data:$('#UpdateComplainWorkOrderF').serialize(),
				url:$('#UpdateComplainWorkOrderF').attr('action'),
				dataType:'json',				
				success:function(data){
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0){
					    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

					   }  
					}
				});
	closeBox();
	$('#WaitAcceptNo').val("");
	$('#GV').val('');
	}
	
 function getAddBoxx2(valWo) {
	 
$.ajax({
	type : "POST",
	data:"workorder="+valWo,
	url : $("#GoOnacceptWo").val(),
	dataType : "html",
	success : function(data) {
		// alert(data);
		$("#alert_box", parent.document).html(data);
		
	},
	complete : function() {
		addInit();// 初始化某些ajax弹出页面			
		viewBox();
	}
});
}

function AlreadyVerify(AVV){
	$('#AlreadyVerifycomplaintState').val(AVV);
	$.ajax({
		type:'POST',
		data:$('#ChangeComplaintStateF').serialize(),
		url:$('#ChangeComplaintStateF').attr('action'),
		dataType:'json',				
		success:function(data){
			$(".tishi_box").html(data.error);
			   $(".tishi_box").show();
			   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			   if (data.errorCode == 0){
			    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

			   }  
			}
		});
	closeBox();
	$('#AlreadyVerifycomplaintState').val("");
}

function AlreadyVerifyJieAn(AVV){
	if($('#acceptresult').val()!=""&&$('#acceptresult').val()==$('#acceptresultVl').val()){
		$('#AlreadyVerifycomplaintState').val($('#acceptresultV').val());
	}else{
	$('#AlreadyVerifycomplaintState').val(AVV);
	}
	$.ajax({
		type:'POST',
		data:$('#ChangeComplaintStateF').serialize(),
		url:$('#ChangeComplaintStateF').attr('action'),
		dataType:'json',				
		success:function(data){
			$(".tishi_box").html(data.error);
			   $(".tishi_box").show();
			   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			   if (data.errorCode == 0){
			    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);

			   }  
			}
		});
	closeBox();
	$('#AlreadyVerifycomplaintState').val("");
}
	

function revisefindWithByCreateGongdan(form){
	$.ajax({
		type : "POST",
		url:$(form).attr("action"),
		data : $(form).serialize(),
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
var selectedTr=null;
function clickonenum(obj){
	 obj.style.backgroundColor = 'gray'; //把点到的那一行变希望的颜色;   
     if (selectedTr != null)  
         $(selectedTr).attr("style", "BACKGROUND-COLOR: white");  
     if (selectedTr == obj)  
         selectedTr = null;//加上此句，以控制点击变白，再点击反灰   
     else  
         selectedTr = obj;  
}

function heshiremarkV(){
	if($('#remark').val()==""){
		alert('请输入内容');
		return false;
	}	
	return true;
}


/*得到选中行相关的值*/  
function checkCreateGongdan() {  
    if (selectedTr != null) {  
       // var str = selectedTr.cells[0].childNodes[0].value;  
    	var str=$(selectedTr).find("td:first").text();
    	$("#availablecwb").val(str);
        alert(str);
    } else {  
        alert("请选择一条要创建的问题件的记录！！");  
    }  
}  
//验证通过工单创建对内扣罚单的参数
function check_createbygongdan(){
	 if (selectedTr != null) {  
	    	var str=$(selectedTr).find("td:first").text();
	    	var str1=$(selectedTr).children('td').eq(1).text();
	    	$("#availablecwb2").val(str);
			$("#cwbhhh2").val(str1);
	       /* alert(str);*/
	    } else {  
	        alert("请选择一条要创建的工单的记录！！"); 
	        return false;
	    }  
	if($("#availablecwb2").val()==""){
		alert("请选择需要创建的工单！！");
		return false;
	}
	if($("#punishbigsort2").val()==0){
		alert("请选择扣罚大类！！");
		return false;
	}
	
	if($("#dutybranchid2").val()==0){
		alert("请选择责任机构！！");
		return false;
	}
	
	if($("#describe2").val().length>100){
		alert("扣罚说明不能超过100个字！！");
		return false;
	}
	if($("#punishprice2").val()==""||$("#punishprice2").val()=="0"||$("#punishprice2").val()=="0.00"){
		if(confirm("当前扣罚金额为0元，您确定要继续创建吗")){
			return true;
		}
		return false;
	}
	return true;
}
//验证通过问题件创建对内扣罚单的参数
function check_createbywentijian(){
	if (selectedTr != null) {  
		// var str = selectedTr.cells[0].childNodes[0].value;  
		var str=$(selectedTr).find("td:first").text();
		var str1=$(selectedTr).children('td').eq(1).text();
		$("#availablecwb3").val(str);
		$("#cwbhhh3").val(str1);
		/*alert(str);*/
	} else {  
		alert("请选择一条要创建的问题件的记录！！"); 
		return false;
	}  
	if($("#availablecwb3").val()==""){
		alert("请选择需要创建的问题件！！");
		return false;
	}
	if($("#punishbigsort3").val()==0){
		alert("请选择扣罚大类！！");
		return false;
	}

	if($("#dutybranchid3").val()==0){
		alert("请选择责任机构！！");
		return false;
	}
	
	if($("#describe3").val().length>100){
		alert("扣罚说明不能超过100个字！！");
		return false;
	}
	if($("#punishprice3").val()==""||$("#punishprice3").val()=="0"||$("#punishprice3").val()=="0.00"){
		if(confirm("当前扣罚金额为0元，您确定要继续创建吗")){
			return true;
		}
		return false;
	}
	return true;
}
/*//根据工单创建对内扣罚单
function submitPunishCreateBygongdan(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "inpunish/submitPunishCreateBygongdanLoad");
		submitCreateForm(form);
		return;
	}
	$("#update")[0].contentWindow.submitPunishCreateBygongdanLoad();
}
//根据工单创建对内扣罚单（带文件的）
function submitPunishCreateBygongdanLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'punishbigsort', $("#punishbigsort", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishsmallsort', $("#punishsmallsort", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid', $("#dutybranchid", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'dutypersoname', $("#dutypersoname", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'punishprice', $("#punishprice", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'type', $("#type", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'availablecwb', $("#availablecwb", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}*/




function submitHeShi(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "workorder/HeshiChangeComplaintState");
		submitCreateForm(form);
		closeBox();
		return;
	}

	closeBox();
	$("#update")[0].contentWindow.submitsubmitHeShiLoad();
}
//问题件结案处理
function submitsubmitHeShiLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'remark', $("#remark", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintState', $("#complaintState", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}

function jieanremarkV(){
	if($('#jieanremark').val()==""){
		alert('请输入内容');
		return false;
	}	
	return true;
}

function submitJieAn(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "workorder/JieAnChangeComplaintState");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitJieAnLoad();
}
//结案处理
function submitJieAnLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'jieanremark', $("#jieanremark", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintState', $("#complaintState", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintResult', $("#acceptresult", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}


/*function decideV(){
	if($('#acceptresult').val()==$('#acceptresultVl').val()){
		$('#complaintState').val($('#acceptresultV').val())
	}
}*/

function IfShenSu(){
	if($('#shensuremark').val()==""){
		alert('请输入内容');
		return false;
	}	
	return true;
}

function submitShenSu(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "workorder/ShenSuChangeComplaintState");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitsubmitShenSuLoad();
}
//结案处理
function submitsubmitShenSuLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'shensuremark', $("#shensuremark", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintState', $("#complaintState", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}

function JieAnChongShen(){
	if($('#jieanchongshenremark').val()==""){
		alert('请输入内容');
		return false;
	}	
	return true;
	
}


function submitJieAnChongShen(form){
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "workorder/JieAnChongShenChangeComplaintState");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitJieAnChongShenLoad();
}
//结案处理
function submitJieAnChongShenLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'jieanchongshenremark', $("#jieanchongshenremark", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintState', $("#complaintState", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'complaintResult', $("#complaintResult", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}

function penalizeType()
{  	if($("#type").val()=='1')
		{
			$("#isshow").attr('style','display:none');
			$("#parent").val(0);
		}
		else
		{
			$("#isshow").attr('style','');
		}
}

function findbig()
{ var parent=$("#penalizesmall").find("option:selected").attr("id");
	$("#penalizebig option[value='"+parent+"']").attr("selected","selected");
} 
function findsmall(id)
{
	var dmpurl=$("#dmpurl").val();
$("#penalizesmall").empty();
$.ajax({
	type:"post",
	url:dmpurl+"/penalizeType/findsmall",
	data:{"id":id},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择</option>";
			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].id+"' id='"+data[i].parent+"'>"+data[i].text+"</option>";
			}
			
			$("#penalizesmall").append(optstr);
		}
	}});
}
function init(cwb)
{
	var dmpurl=$("#dmpurl").val();
	$.ajax({
		type:"post",
		url:dmpurl+"/penalizeOut/initaddpenalizeOut",
		dataType:"json",
		data:{"cwb":cwb},
		success:function(data){
		$("#flowordertype").val(data.flowordertyleValue);
		$("#flowordertypeText").val(data.flowordertyleText);
		$("#caramount").val(data.caramount);
		$("#penalizeOutGoodsfee").val(data.caramount);
		$("#fee1").val(data.receivablefee);
		$("#customername").val(data.customername);
		$("#customerid").val(data.customerid);
		}});
}

function acceptcloseDiv(){
	if($('#jieanremark').val()!=""){
		closeBox();
	}	
}

function acceptqueryWoJieAn(){
	$.ajax({
		type:'POST',
		data:$('#GoonAcceptWorkOrderQueryForm').serialize(),
		url:$("#GoonAcceptWorkOrderQueryForm").attr('action'),
		dataType:'json',
		success:function(data){
			$(".tishi_box").html(data.error);
			   $(".tishi_box").show();
			   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			   if (data.errorCode == 0){
			    $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				closeBox();
			   }  
			
			
			
		}
		
		
	});
}
function createbycwbinit(cwb)
{
	var dmpurl=$("#absolutepath").val();
	$.ajax({
		type:"post",
		url:dmpurl+"/penalizeOut/initaddpenalizeOut",
		dataType:"json",
		data:{"cwb":cwb},
		success:function(data){
		$("#flowordertype").val(data.flowordertyleText);
		$("#cwbprice").val(data.caramount);
		$("#cwbstate").val(data.flowordertyleValue);


		}});
}
function submitCreateFormAdd(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
			if (data.errorCode == 0) {
				/*$(form)[0].reset();*/
				//$("#WORK_AREA")[0].contentWindow.addSuccess(data);  
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
				$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
			}
		}
	});
}

function getComplaintUserValue(){
	var jsonData = {};
	jsonData['codOrgId'] = $('#codOrgIdValue').val();
	$.ajax({
		type:'POST',
		data:jsonData,
		url:'workorder/getBeiTouSuRenValue',
		dataType:'json',
		success:function(data){
			var Str = "<option value='-1'>请选择</option>";
			$.each(data,function(ind,ele){
				var dataTrStr = "<option value='" + ele.username + "'>" + ele.realname + "</option>";
				Str+=dataTrStr;
			});
			
			$('#ComplaintUseridValue').html(Str);
		}
		
		
		
		
	});
	
	
	
}



//对内扣罚审核的判断
function check_punishinsideshenhe(){
	if($("#resultgoodprice").val()==""||$("#resultqitaprice").val()==""){
		alert("最终货物扣罚金额与最终其它扣罚金额不能为空，请重输入！！");
		return false;
	}
	if(isNaN($("#resultgoodprice").val())&&isNaN($("#resultqitaprice").val())){
		$("#resultgoodprice").val("");
		$("#resultqitaprice").val("");
		alert("最终货物扣罚金额与最终其它扣罚金额不能为非数字类型，请重新输入！！");
		return false;
	}else if(isNaN($("#resultgoodprice").val())){
		$("#resultgoodprice").val("");
		alert("最终货物扣罚金额不能为非数字类型，请重新输入！！");
		return false;
	}else if(isNaN($("#resultqitaprice").val())){
		$("#resultqitaprice").val("");
		alert("最终其它扣罚金额不能为非数字类型，请重新输入！！");
		return false;
	}

	if($("#describe").val()==""||$("#describe").val()=="最多100个字"){
		alert("请输入对内扣罚审核说明！！");
		return false;
	}
	if($("#describe").val().length>100){
		alert("输入的对内扣罚审核说明不能 超过100个字！！");
		return false;
	}
	if($("#koufajine").val()==""||$("#koufajine").val()=="0"||$("#koufajine").val()=="0.00"){
		if(confirm("当前扣罚金额为0元，您确定要继续创建吗")){
			return true;
		}
		return false;
	}
	return true;
}
//对内扣罚之扣罚成立
function koufachengli(){
	if(check_punishinsideshenhe()){
		$("#shenheresult").val("1");
		if ($("#update").contents().find("#wavText").val() == "") {
			$("#form1").attr("enctype", "");
			$("#form1").attr("action", "inpunish/submitHandleShenheResult");
			submitCreateFormShenheKoufa($("#form1"));
			return;
		}
		$("#update")[0].contentWindow.submitShenheLoad();
		$("#koufajine").val("");
		$("#resultgoodprice").val("");
		$("#resultqitaprice").val("0.00");
		$("#describe").val("最多100个字");
		$("#update")[0].contentWindow.chuShihuatiaojian();

	}
	
}
//对内扣罚之撤销扣罚
function chexiaokoufa(){
	if(check_punishinsideshenhe()){
		$("#shenheresult").val("2");
		if ($("#update").contents().find("#wavText").val() == "") {
			$("#form1").attr("enctype", "");
			$("#form1").attr("action", "inpunish/submitHandleShenheResult");
			submitCreateFormShenheKoufa($("#form1"));
			return;
		}
		$("#update")[0].contentWindow.submitShenheLoad();
/*		$("#update")[0].contentWindow.submitShenheLoad();
*/		$("#koufajine").val("");
		$("#resultgoodprice").val("");
		$("#resultqitaprice").val("0.00");
		$("#describe").val("最多100个字");
		$("#update")[0].contentWindow.chuShihuatiaojian();
	}
	
}
function submitShenheLoad(){
	$('#swfupload-control').swfupload('addPostParam', 'koufajine', $("#koufajine", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'describe', $("#describe", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'id', $("#id", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'shenheresult', $("#shenheresult", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'resultgoodprice', $("#resultgoodprice", parent.document).val());
	$('#swfupload-control').swfupload('addPostParam', 'resultqitaprice', $("#resultqitaprice", parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}

function getReasonValue(){
	var jsonData = {};
	jsonData['complaintOneLevel'] = $('#olreason').val();
	$.ajax({
		type:'POST',
		data:jsonData,
		url:'workorder/getTwoValueByOneReason',
		dataType:'json',
		success:function(data){
			if(data!=null){
			var Str = "<option value='-1'>请选择</option>";
			$.each(data,function(ind,ele){
				var dataTrStr = "<option value='" + ele.reasonid + "'>" + ele.reasoncontent+ "</option>";
				Str+=dataTrStr;
			});
			
			$('#tlreason').html(Str);
			}
		}
	});	
}
function aabzc(){
	if($('#compaltecontent').val()=="订单号+工单内容")
	$('#compaltecontent').val('');
}
function aabzc1(){
	if($('#compaltecontent').val()==""){
		$('#compaltecontent').val('订单号+工单内容');
	}
}
function checkLen(obj) {  

	var maxChars = 150;//最多字符数  
	if (obj.value.length > maxChars){
		obj.value = obj.value.substring(0,maxChars);  
	} 
	var curr = maxChars - obj.value.length;  
	document.getElementById("count").innerHTML = curr.toString(); 
} 
function maxCheck(fee){
	if(fee-0>1000000000000000)
		{
		return false;
		}
	if(fee.indexOf('.')!=-1)
	{
		if(fee.split('.')[1].length>2)
		{
		
			return false;
	
		}
	}

		return true;
	
}
function checke_fee(){
	if(!isFloat($("#fee1").val())||!maxCheck($("#fee1").val()))
	{
		alert("金额输入有误！");
		$("#fee1").val('');
		return false;
	}
	if(!isFloat($("#fee2").val())||!maxCheck($("#fee2").val()))
	{
		alert("金额输入有误！");
		$("#fee2").val('');
		return false;
	}
	if(!isFloat($("#fee3").val())||!maxCheck($("#fee3").val()))
	{
		alert("金额输入有误！");
		$("#fee3").val('');
		return false;
	}
	return true;

}

function  decidecomplain(){
/*	if($('#codOrgIdValue').val()=='-1'){
		alert('请选择机构');
		return false;
	}
	if($('#ComplaintUseridValue').val()==-1){
		alert('请选择被投诉人');
		return false;
	}*/
	if($('#olreason').val()==-1){
		alert('请输入一级分类');
		return false;
	}
	
	if($('#tlreason').val()==-1){
		alert('请输入二级分类');
		return false;
	}
	if($('#acceptresultC').val()==$('#cwclv').val()){
		alert('请将处理结果设置为成立/不成立的状态');
		return false;
	}
	 if($('#compaltecontent').val()==""){
		alert('请填写投诉内容');
		return false;
	}
	return true;
}	
function  decidecomplain1(){
		if($('#codOrgIdValue').val()=='-1'){
			alert('请选择责任机构');
			return false;
		}
		/*if($('#ComplaintUseridValue').val()==-1){
			alert('请选择责任人');
			return false;
		}*/
		if($('#olreason').val()==-1){
			alert('请输入一级分类');
			return false;
		}
		
		if($('#tlreason').val()==-1){
			alert('请输入二级分类');
			return false;
		}
		if($('#acceptresultC').val()!=$('#cwclv').val()){
		
			alert('请将处理结果设置为待定');
			return false;
		}
		 if($('#compaltecontent').val()==''){
			alert('请填写投诉内容');
			return false;
		}
		return true;
	}	
function  decidequery(){
	 if($('#queryContentd').val()==""){
		alert('请填写查询内容');
		return false;
	}
	 return true;
}	

//扣罚大类触发扣罚小类
function findsmallAdd(contextPath,self,chufaobject)
{
	
$("#"+chufaobject).empty();
$.ajax({
	type:"post",
	url:contextPath+"/penalizeType/findsmall",
	data:{"id":$(self).val()},
	dataType:"json",
	success:function(data){
		if(data.length>0){
			var optstr="<option value='0'>请选择扣罚小类</option>";
			for(var i=0;i<data.length;i++)
			{
				optstr+="<option value='"+data[i].id+"' id='"+data[i].parent+"'>"+data[i].text+"</option>";
			}
			
			$("#"+chufaobject).append(optstr);
		}
	}});
}
//小类触发大类
function findbigAdd(self,chufaobject)
{ var parent=$(self).find("option:selected").attr("id");
	$("#"+chufaobject+" option[value='"+parent+"']").attr("selected","selected");
} 

function Callerifnull(){
if($('#caname').val()==''){
		alert('请输入姓名');
		return false;
	}
if($('#cp').val()==''){
	alert('请输入电话');
	return false;
}
/****begin****/
 //vic.liang@pjbest.com 2016-07-13 工单导入取消必填项
/*if($('#ccity').val()==''){
	alert('请输入城市');
	return false;
}
if($('#cprovince').val()==''){
	alert('请输入省份');
	return false;
}
if($('#skhfl').val()==-1){
	alert('请选择客户分类');
	return false;
}if($('#cprovince').val()==''){
	alert('请输入省份');
	return false;
}
if($('#callerremark').val()==''){
	alert('请输入备注');
	return false;
}*/
/****end****/	
	
	return true;
	 
}


/*function ifphoneNum(phonevalue){
	if(isMobileNumber(phonevalue)){		
		return true;
	}else{
		alert('请输入正确的手机号');
		$('#cp').val('');
		return false;
	}
}*/
function ifphoneNum(str) {
	var reg = /^1\d{10}$/ //电话号码验证是否为空和是否为数字
	if (!reg.test(str)) {
		alert("请输入正确格式的手机号码!");
		
		return false;
	}
}



function isEmailValue(str){
   var re = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
   if(!re.test(str)){
	   alert('请输入正确的邮箱');
		$('#mailBoxid').val("");
		return false;
   }
}

function isChineseValue(str){
	   var re = /^[\u4e00-\u9fa5]{2,10}$/ 
	   if(!re.test(str)){
		   alert('请输入正确的省份');
			$('#province').val("");
			return false;
	   }
	}
function isChineseValue1(str){
	var re1 = /^[\u4e00-\u9fa5]{2,10}$/
	   if(!re1.test(str)){
		   alert('请输入正确的城市');
			$('#city').val("");	
			return false;
	   }
	}

function isnum(str){
	 var reg = new RegExp("^[0-9]*$");
	    if(!/^[0-9]*$/.test(str)){
	        alert("请输入数字!");
	        $('this').val('');
	        $('#contactNum').val("");	
	        return false;
	    }
}
function isbranchnum(str){
	 var reg = new RegExp("^[0-9]*$");
	    if(!/^[0-9]*$/.test(str.value)){
	       alert("请输入数字!");
	        $('#'+str.id).val('');	
	        return false;
	    }
}
function chuShihuatiaojian(){
	$("#wavText").val("");
}
//验证通过工单创建对内扣罚单的参数
function check_createbyshixiaokaohe(){
	 if (selectedTr != null) {  
	    	var str=$(selectedTr).find("td:first").text();
	    	var str1=$(selectedTr).children('td').eq(1).text();
	    	$("#availablecwb4").val(str);
			$("#cwbhhh4").val(str1);
	       /* alert(str);*/
	    } else {  
	        alert("请选择一条要创建的时效的记录！！"); 
	        return false;
	    }  
	if($("#availablecwb4").val()==""){
		alert("请选择需要创建的工单！！");
		return false;
	}
	if($("#punishbigsort4").val()==0){
		alert("请选择扣罚大类！！");
		return false;
	}
	
	if($("#dutybranchid4").val()==0){
		alert("请选择责任机构！！");
		return false;
	}
	if($("#punishprice4").val()==""){
		alert("请输入扣罚金额！！");
		return false;
	}
	if($("#describe4").val().length>100){
		alert("扣罚说明不能超过100个字！！");
		return false;
	}
	return true;
}
//根据时效弹出框中的查询的判断条件
function check_CreatepunishByShixiao(){
	if($("#celuename").val()==0&&$("#kaoheproject").val()==0&&$("#cwb").val()==""){
		alert("请至少选择一个条件进行查询噢！！");
		return false;
	}
	return true;
}

function createinpunishbycwb(form){
	$.ajax({
		type : "POST",
		url:$(form).attr("action"),
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
//根据时效创建对内扣罚单
function submitPunishCreateByshixiaokoahe(form){
	console.info("hhh");
	if ($("#update").contents().find("#wavText").val() == "") {
		$(form).attr("enctype", "");
		$(form).attr("action", "inpunish/createbyshixiao");
		submitCreateForm(form);
		return;
	}

	$("#update")[0].contentWindow.submitPunishInsideByShixiao();
}
function submitPunishInsideByShixiao(){
	$('#swfupload-control1').swfupload('addPostParam', 'punishbigsort4', $("#punishbigsort4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'punishsmallsort4', $("#punishsmallsort4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'dutybranchid4', $("#dutybranchid4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'dutypersoname4', $("#dutypersoname4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'punishprice4', $("#punishprice4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'describe4', $("#describe4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'type4', $("#type4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'availablecwb4', $("#availablecwb4", parent.document).val());
	$('#swfupload-control1').swfupload('addPostParam', 'cwbhhh4', $("#cwbhhh4", parent.document).val());
	$('#swfupload-control1').swfupload('startUpload');
}
function alculateSumprice(selfObject,addObject,sumPrice){
	var selfdata=$(selfObject);
	var addData=$("#"+addObject);
	var sumSP=$("#"+sumPrice);
	 
         var num1 = selfdata.val();// 取得first对象的值  
         var num2 = addData.val();// 取得second对象的值  
         var sum = sumPriceAdd(num1,num2,selfdata,addData); 

         console.info(isNaN($(sumSP).val()));
         if(isNaN(sum)){
        	 $(selfObject).val("");
        	 var num3 = selfdata.val();
             var num4 = addData.val();
             var sum1 = sumPriceAdd(num3,num4,selfdata,addData,sumSP);
             	if(isNaN(num4)){
             		$(addData).val("");
             		sumSP.val("");
             	}else {
             		sumSP.val(sum1);
				}
             
           
        	alert("亲，只能输入数字类型的噢,请重新输入！！");
         }else{
        	 sumSP.val(sum);

         }
         
}
function changeSumhh(selfObject,addData,sumSP){
	 $(selfObject).val("");
	 var num3 = selfdata.val();
     var num4 = addData.val();
     var sum1 = sumPriceAdd(num3,num4,selfdata,addData);
     sumSP.val(sum1);
}
function sumPriceAdd(arg1,arg2,selfdata,addData,sumSP){
	 var a1,a2,m;  
     try{  
    	 if(arg1.toString().length>14){
    		 alert("输入的金额位数不能超过14位噢！！");
        	 $(selfdata).val("");
        	 arg1="";
    	 }
         a1 = arg1.toString().split(".")[1].length;
         if(a1>2){
        	 alert("输入的金额小数点后只能输入两位,请重新输入！！");
        	 $(selfdata).val("");
        	 arg1="";
        	 
         }
     }catch(e){  
         a1 = 0;  
     }  
     try{  
    	 if(arg2.toString().length>14){
    		 alert("输入的金额位数不能超过14位噢！！");
        	 $(addData).val("");
        	 arg2="";
    	 }
         a2 = arg2.toString().split(".")[1].length; 
         if(a2>2){
        	 alert("输入的金额小数点后只能输入两位,请重新输入！！");
        	 $(addData).val("");
        	 arg2="";
         }
     }catch(e){  
         a2 = 0;  
     }  
     m = Math.pow(10, Math.max(a1,a2));  
     return (arg1*m+arg2*m)/m;  
}
//上传文件初始化上传组件
function uploadFormInitAddhh(form, contextPath) {
	$('#swfupload-control').swfupload({
		upload_url : $("#" + form, parent.document).attr("action"),
		file_size_limit : "10240",
		file_types : "*.xls;*.xlsx",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : contextPath + "/js/swfupload/swfupload.swf",
		button_image_url : contextPath + "/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#upbutton")[0]
	}).bind('fileQueued', function(event, file) {
		$("#wavText").val(file.name);
		$("#wavText").attr("selectedUploadFile", true);
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {
		
		/* * 进度条 var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		 * $("#progressbar").progressbar({ value : percent });
		 * $("#progressstatus").text(percent);*/
		 

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		if(dataObj.errorCode==1){
			$(".tishi_box", parent.document).html(dataObj.error);
			$(".tishi_box", parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		}
		/*$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);*/
	/*	$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);*/
		// $("#wavText").val("");
		if (dataObj.errorCode == 0) {
		/*	console.info(dataObj.success);
			alert(dataObj.success);*/
			$("#successCount111",parent.document).html(dataObj.success);
			$("#emaildate",parent.document).val(dataObj.emaildate);
			/*console.info(dataObj.error);*/
			$("#failCount111",parent.document).html(dataObj.error);
		/*	alert(dataObj.error);*/
/*			$("#errorName",parent.document).val(dataObj.emailid);
*/			
			alert("导入完成");
/*			$("#appendhtmlid3",parent.document).html("dsadsaf");
*/		}
//		$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
//		$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
		// setTimeout(queryProgress, 10);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}

//excel导入
function submitPunishCreateByExcel(form){
	if($("#update")[0].contentWindow.checkIsExistexcelData()){
		alert("您还未选择导入的excel！！");
		return;
	}
	$("#importdaorubutton").attr("disabled","disabled");
	$("#importdaorubutton").val("正在导入...");
	$("#update")[0].contentWindow.submitPunishInsideByExcel();
}
function checkIsExistexcelData(){
	if($("#wavText").val()==""){
		return true;
	}else{
		return false;
	}
}
function submitPunishInsideByExcel(){
	$('#swfupload-control').swfupload('startUpload');
}

function punishExcelImport(systemflag,contextPath,appendObject,pathurl){
	if($("#emaildate").val()==""){
		alert("当前没有导入失败的订单！！");
		return;
	}
	var showhtml="";
	$.ajax({
		url:contextPath+$("#"+pathurl).val()+$("#"+systemflag).val(),
		dataType:'json',
		success:function (data){
			$("#importdaorubutton").attr("disabled","disabled");
			$("#importdaorubutton").val("正在导入...");
			$.each(data,function (i,a){
				showhtml+="<tr height=\"30\" onclick=\"clickonenum(this);\" bgcolor=\"#cccccc\">"+
				"<td align=\"center\" valign=\"middle\"><strong>"+a.cwb+"</strong></td>"+
				"<td align=\"center\" valign=\"middle\"><strong>"+a.error+"</strong></td>"+
				+"</tr>";
			});
			$("#gd_table4").show();
			$("#"+appendObject).html("");
			$("#"+appendObject).html(showhtml);
		}
	});
}
function punishExcelImportClear(){
	$("#gd_table4").hide();
}
function punishExcelImportSuccess(systemflag,contextPath,appendObject,pathurl){
	if($("#emaildate").val()==""){
		alert("当前没有导入成功的订单！！");
		return;
	}
	var showhtml="";
	$.ajax({
		url:contextPath+$("#"+pathurl).val()+$("#"+systemflag).val(),
		dataType:'json',
		success:function (data){
			$("#importdaorubutton").attr("disabled","disabled");
			$("#importdaorubutton").val("正在导入...");
			$.each(data,function (i,a){
				showhtml+="<tr height=\"30\" onclick=\"clickonenum(this);\" bgcolor=\"#cccccc\">"+
				"<td align=\"center\" valign=\"middle\"><strong>"+a+"</strong></td>"+
				"<td align=\"center\" valign=\"middle\"><strong>成功</strong></td>"+
				+"</tr>";
			});
			$("#gd_table4").show();
			$("#"+appendObject).html("");
			$("#"+appendObject).html(showhtml);
		}
	});
}

function BuprintTag(scpath){

	var scancwb=$("#scancwbprint").val();
	var rk_switch=$("#updateswitch").val();
	if (rk_switch == "rkbq_01") {
		$("#printcwb",parent.document).attr("src",pname + "/printcwb?scancwb="+ scancwb + "&a="+ new Date());
	}else if (rk_switch == "rkbq_03") {
		//广州通路
		printIntowarehouse(scancwb);
	}
	else if (rk_switch == "rkbq_04") {						
		$("#printcwb",parent.document).attr("src",pname + "/printcwb/printCwbruku?scancwb="+ scancwb + "&a="+ new Date());
	}
	else if (rk_switch == "rkbq_05") {
		//重庆华宇
		printIntowarehouseNew(scancwb);
	}
	else if (rk_switch == "rkbq_06") {	
		//福建飞远30*25
		printIntowarehouse3025(scancwb);
	}
	else if (rk_switch == "rkbq_07") {	
		//武汉飞远35*35
		printIntowarehousewuhan(scancwb);
	}
	
	$('#scancwbprint').val('') //补打后删除输入框内容 add by vic.liang@pjbest.com 2016-08-13
	/*	
	if(scancwb==null){
		alert("请输入单号！");
		return false;
	}else{
		$("#printcwb",parent.document).attr("src",scpath+"/printcwb/printCwbruku?scancwb="+ scancwb + "&a=" + new Date());
		
		return true;
	}		*/
}

function changeJSstate(contextPath){
	$("#jiesuanstate").empty();
	var employeestatus = $("#employeestatus").val();
	$.ajax({
		type:"POST",
		url:contextPath,
		data:{employeestatus:employeestatus},
		dataType:"json",
		success:function(data){
			var optionStr = "";
			if(data.errorCode==1){
				optionStr = "<option value='1'>正常结算</option>";				
			}else if(data.errorCode==2){
				optionStr = "<option value='2'>暂停结算</option>";
			}else{
				optionStr = "<option value='3'>停止结算</option>";
			}
				$("#jiesuanstate").append(optionStr);
		}
	});
}

function checkRevisePunishinsidePriceandDuty(){
	if($("#describe").val()==""||$("#describe").val()=="最多100个字"){
		alert("请输入修改备注");
		return false;
	}
	if($("#describe").val().length>100){
		alert("输入的对内扣罚审核说明不能 超过100个字！！");
		return false;
	}
	if($("#koufajine").val()==""||$("#koufajine").val()=="0"||$("#koufajine").val()=="0.00"){
		if(confirm("当前扣罚金额为0元，您确定要继续修改吗")){
			return true;
		}
		return false;
	}
	return true;
}
function revisePriceOrdutyinfo(form){
	if(checkRevisePunishinsidePriceandDuty()){
		$("#dutybranchid").removeAttr("disabled");
		submitCreateFormPunishInsideRevise(form);
		$("#dutybranchid").attr("disabled","disabled");
		return false;
	}
}

function submitCreateFormPunishInsideRevise(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				/*$(form)[0].reset();*/
			/*	$("#dutybranchid").val(0);
				$("#dutynameAdd").val(0);
				$("#revisegoodprice").val("");
				$("#reviseqitaprice").val("0.00");
				$("#koufajine").val("");*/
				$("#describe").val("最多100个字");
				//$("#WORK_AREA")[0].contentWindow.addSuccess(data);  
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
			}
		}
	});
}
function showAndoffChatWin(){
		$("#chatcontent").toggle();
}

function showAndoffHuiFuChatWin(){
	$("#ifShowTiJaoContent").toggle();
}

function submitchatcontent(){
	$.ajax({
		type:"POST",
		url:$('#chatcontentFormUrl').val(),
		data:{textareavalue:$('#chattextareaid').val(),acceptNo:$('#chatacceptnoid').val()},
		dataType:"json",
		success:function(data){
			if(data.success==0){
				alert(data.successdata);
			}
	
		}
	});
	
	
	document.getElementById('chattextareaid').innerHtml='';
}

function selectbranchUsersInputer(targetid,oldid){
	$.ajax({
		url:$("#getbranchusers").val(),
		type:"POST",
		data:"dutybranchid="+$("#"+oldid+"_value").val(),
		dataType:'json',
		success:function (json){
			$("#"+targetid).empty();
			$("<option value ='0'>==请选择机构责任人==</option>").appendTo("#"+targetid);// 添加下拉框的option
			for (var j = 0; j < json.length; j++) {
				$("<option value=' "+ json[j].userid +" '>"+json[j].realname+"</option>").appendTo("#"+targetid);
			}
		}
	});
}



function alculateSumpriceCreate(selfObject,addObject,sumPrice){
	var selfdata=$(selfObject);
	var addData=$("#"+addObject);
	var sumSP=$("#"+sumPrice);
	 
         var num1 = selfdata.val();// 取得first对象的值  
         var num2 = addData.val();// 取得second对象的值  
        // var sum = sumPriceAdd(num1,num2,selfdata,addData); 
         if($(selfdata).val()==""&&$(addData).val()==""){
        	 sumSP.val("");
        	 return;
         }
         if(isNaN(num1)&&isNaN(num2)){
        	 $(sumSP).val("");
        	 return;
         }else if(!isNaN(num1)&&!isNaN(num2)){
        	 var sum1 = sumPriceAdd(num1,num2,selfdata,addData,sumSP);
             sumSP.val(sum1);
             return;
         }else if(isNaN(num1)){
        	  var num4 = addData.val();
             var sum1 = sumPriceAdd("0",num2,selfdata,addData,sumSP);
             sumSP.val(sum1);
             return;
         }else{
        	 var num3 = selfdata.val();
             var sum1 = sumPriceAdd(num1,"0",selfdata,addData,sumSP);
             sumSP.val(sum1);
         }
        
         
}



function submitCreateFormShenheKoufa(form) {
	$.ajax({
		type : "POST",
		url : $(form).attr("action"),
		data : $(form).serialize(),
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				/*$(form)[0].reset();*/
				//$("#WORK_AREA")[0].contentWindow.addSuccess(data);  
				$("#koufajine").val("");
				$("#resultgoodprice").val("");
				$("#resultqitaprice").val("0.00");
				$("#describe").val("最多100个字");
				$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
			}
		}
	});
	
}

function changejd(){
	if($('#ifjidan').val() == "0"){
		
		$('#jdType1').parent().hide();
		$('#jdType1').val("0");
		$('#jdType2').val("0");		
		
	}
	if($('#ifjidan').val() == "1"){
	
		$('#jdType1').parent().show();
	}
}

function transcwbswitch(){
	if($('#isypdjusetranscwb').val() == "0"){
		//集单开关为否
		$("#ifjidan").attr("disabled","disabled");
		$('#ifjidan').val("0");
		
		$('#jdType1').parent().hide();
		$('#jdType1').val("0");
		$('#jdType2').val("0");		
		
	}
	if($('#isypdjusetranscwb').val() == "1"){
		$("#ifjidan").removeAttr("disabled");
		if($('#ifjidan').val() == "1"){	
			$('#jdType1').parent().show();
		}
	}
}

function onCityChangeForBranch(contextPath, id) {
    var param = {
        city:$(id).val()
    }

    var url = "branch/queryCounty";
    if(contextPath){
        url=  contextPath + "/branch/queryCounty"
    }

    $.ajax({
        type: "post",
        async: false, //设为false就是同步请求
        url: url,
        data:param,
        datatype: "json",
        success: function (result) {
            var county = $("#brancharea");
            county.empty();
            county.append("<option value=''>请选择</option>");
            $.each(result,function(index,item) {
                item.code;
                item.name;
                county.append("<option value='"+item.id+"'>"+item.name+"</option>");
            });
        }
    });
}
	function liftLoginForbiddance(userid, contextPath) {
	$.ajax({
		type : "POST",
		url : contextPath + "/user/liftLoginForbiddance",
		dataType : "json",
		data : {
			userid : userid
		},
		success : function(data) {
			if (data.errorCode == 0) {
				alert(data.error);
			} else {
				alert("解禁失败");
			}
		}
	});
}
	
//唐富忠 判断重量数值合法性并转为float. 不合法返回-1
function parseWeight_(number_) {

	try {
		if (isNaN(number_)) {
			return -1;
		}		
	} catch (err) {
		return -1;
	}

	try {
		number_ = parseFloat(number_);
	} catch (err) {
		return -1;
	} finally {
		if (isNaN(number_)) {
			return -1;
		}
	}
	return number_;
}

	
	/**
	 * add by bruce shangguan 20170122 判断获取的重量是否合法
	 */
	function isRightWeight(carrealweight){
		if(isNaN(carrealweight)){
			return false ;
		}
		try{
			carrealweight = parseFloat(carrealweight);
	    }catch(err){
	    	carrealweight = 0 ;
	    }
		if(isNaN(carrealweight) || carrealweight <= 0){
			return false ;
		}
		return true ;
	}

	