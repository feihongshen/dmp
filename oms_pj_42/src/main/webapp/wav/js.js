// JavaScript Document

function isFloat(name){
	return /^[0-9\.]+$/.test(name);
}
function testEmail(str){
	  return  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
}
function isLetterAndNumber(name){
	return /^[A-Za-z0-9]+$/.test(name);
}
function isAll(name){
	return /^[A-Za-z0-9\u4e00-\u9fa5]+$/.test(name);
}

//窗口改变刷新
function centerBox(){
	var $scroll_hei = document.body.scrollHeight;
	var $scroll_wd = document.body.scrollWidth;
		var $see_height = document.documentElement.clientHeight;
		var $see_width = document.documentElement.clientWidth;
		//底部信息位置
			$("#box_bg").css("height",$see_height);
			if($("#box_form").height() > 500){
				$("#box_form").css("height","500px");
				$("#box_form").css("overflow","auto")
			}
		$tcbox_height = $("#box_contant").height();
		$tcbox_width = $("#box_contant").width();
		$("#box_contant").css("top",($see_height/2)-($tcbox_height/2));
		$("#box_contant").css("left",($see_width/2)-($tcbox_width/2));
}
//根据滚动条滚动时间刷新漂浮框的xy轴
function reIframeTopAndBottomXY(){
	var st = document.documentElement.scrollTop;
	if(st<document.body.scrollTop){st=document.body.scrollTop;}
	if($(".iframe_bottom")){
		$(".iframe_bottom").css("top", st+document.documentElement.clientHeight-40);
	}
	if($(".form_btnbox")){
		$(".form_btnbox").css("top", st+document.documentElement.clientHeight-40);
	}
	if($(".inputselect_box")){
		$(".inputselect_box").css("top", st);
	}
	if($(".list_topbar")){
		$(".list_topbar").css("top", st);
	}
	
}
$(window).resize(function() {
	centerBox();
	reIframeTopAndBottomXY();
	});
$(window).scroll(function(event){
	reIframeTopAndBottomXY();
});
$(window).load(function(event){
	reIframeTopAndBottomXY();
});

$(function(){

	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
});

$(function(){
	$("#add_button").click(function(){
		getAddBox();
		
	});
	
});

function edit_button(key){
	getEditBox(key);
}

function closeBox(){
	$("#alert_box").hide(300);
	$("#dress_box").css("visibility","");
}

function viewBox(){
//	$(window).keydown(function(event){
//		switch(event.keyCode) {
//			case (event.keyCode=27):window.parent.closeBox();break;
//		}
//	});
	$("#alert_box", parent.document).show();
	$("#dress_box", parent.document).css("visibility","hidden");
	window.parent.centerBox();
}

function getAddBox(){
	$.ajax({
		type: "POST",
		url:$("#add").val(),
		dataType:"html",
		success : function(data) {
			//alert(data);
			$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			addInit();//初始化某些ajax弹出页面
			viewBox();
		}
	});
}
function getEditBox(key){
	$.ajax({
		type: "POST",
		url:$("#edit").val()+key,
		dataType:"html",
		success : function(data) {
			//alert(data);
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			editInit();//初始化ajax弹出页面
			viewBox();
		}
	});
}
function getViewBox(key){
	$.ajax({
		type: "POST",
		url:$("#view").val()+key,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
			viewBox();
		}
	});
}
function submitCreateForm(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if(data.errorCode==0){
				$("#WORK_AREA")[0].contentWindow.addSuccess(data);
			}
		}
	});
}
function submitSaveForm(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if(data.errorCode==0){
				$("#WORK_AREA")[0].contentWindow.editSuccess(data);
			}
		}
	});
}

function submitSaveFormAndCloseBox(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if(data.errorCode==0){
				$("#WORK_AREA")[0].contentWindow.editSuccess(data);
				closeBox();
			}
		}
	});
}

function del(key){
	$.ajax({
		type: "POST",
		url:$("#del").val()+key,
		dataType:"json",
		success : function(data) {
			//alert(data.error);
			if(data.errorCode==0){
				delSuccess(data);
			}
		}
	});
}
//上传文件初始化上传组件
function uploadFormInit(form,contextPath){
	$('#swfupload-control').swfupload({
		upload_url: $("#"+form,parent.document).attr("action"),
		file_size_limit : "10240",
		file_types : "*.wav;*.WAV",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : contextPath+"/js/swfupload/swfupload.swf",
		button_image_url: contextPath+"/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#upbutton")[0]
	}).bind('fileQueued', function(event, file) {
		$("#wavText").val(file.name);
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete',function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress',function(event, file, bytesLoaded, bytesTotal) {
				/*进度条
				var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
				$("#progressbar").progressbar({
					value : percent
				});
				$("#progressstatus").text(percent);*/
			
	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj=eval("("+serverData+")");
		$(".tishi_box",parent.document).html(dataObj.error);
		$(".tishi_box",parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		if(dataObj.errorCode==0){
			if(dataObj.type=="add"){
				$("#WORK_AREA",parent.document)[0].contentWindow.addSuccess(dataObj);
				$("#sub",parent.document).removeAttr("disabled");
				$("#sub",parent.document).val("确认");
			}else if(dataObj.type=="edit"){
				$("#WORK_AREA",parent.document)[0].contentWindow.editSuccess(dataObj);
				$("#sub",parent.document).removeAttr("disabled");
				$("#sub",parent.document).val("保存");
			}
		}
		//setTimeout(queryProgress, 10);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}


/////////////订单类型设置////////////////
function check_cwbOrderType(){
	if($("#importtypeid").val()<0){
		alert("请选择类型");
		return false;
	}
	return true;
}
/////////////订单类型设置 END////////////////
/////////////导入设置////////////////
function check_excelcolumn(){
	if($("#customerid").val()==-1){
		alert("请选择供货商！");
		return false;
	}else if($("#cwbindex").val()==0){
		alert("请选择您的条码号/订单号！");
		return false;
	} 
	return true;
}
/////////////导入设置 END////////////////
/////////////车辆设置////////////////
function isCheckNumber(name){
	return /^[0-9\.]+$/.test(name);
}
function check_truck(){
	if($("#truckno").val().length==0){
		alert("车牌号不能为空");
		return false;
	}
	if($("#truckoil").val().length==0){
		$("#truckoil").val(0);
	}
	if($("#truckkm").val().length==0){
		$("#truckkm").val(0);
	}
	if($("#truckstartkm").val().length==0){
		$("#truckstartkm").val(0);
	}
	if(!isCheckNumber($("#truckoil").val())){
		alert("百公里耗油量数字的格式不正确");
		return false;
	}
	if(!isCheckNumber($("#truckkm").val())){
		alert("每天公里数的格式不正确");
		return false;
	}
	if(!isCheckNumber($("#truckstartkm").val())){
		alert("起始里程数的格式不正确");
		return false;
	}
	return true;
}
/////////////车辆设置END////////////////
/////////////机构管理////////////////
//弹出框弹出时进行初始化
function init_branch(){
	$("#branchname").parent().hide();
	$("#branchphone").parent().hide();
	//$("#accountarea").parent().hide();
	$("#branchprovince").parent().hide();
	$("#branchaddress").parent().hide();
	$("#branchmobile").parent().hide();
	$("#branchemail").parent().hide();
	$("#remandtype").parent().hide();
	$("#branchmatter").parent().hide();
	$(".zhandian").parent().hide();
	$(".kefu").parent().hide();
	$(".yunying").parent().hide();
	$("#insert").parent().hide();
	$("#wav").hide();
}
function gonggongObj_branch(){
	$("#branchname").parent().show();
	$("#branchphone").parent().show();
	$("#branchcontactman").parent().show();
	$("#insert").parent().show();
}
function zhandianObj(){
	gonggongObj_branch();
	//$("#accountarea").parent().show();
	$("#branchaddress").parent().show();
	$("#branchmobile").parent().show();
	$("#branchemail").parent().show();
	//$("#remandtype").parent().show();
	$("#wav").show();
	$(".zhandian").parent().show();
}
function yunyingObj(){
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	$(".yunying").parent().show();
}
function kefuObj(){
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	$(".kefu").parent().show();
}
function caiwuObj(){
	gonggongObj_branch();
	$("#branchmatter").parent().show();
	//$("#swfupload-control").parent().show();
}
function qitaObj(){
	gonggongObj_branch();
	$("#branchprovince").parent().show();
	$("#branchaddress").parent().show();
	$("#remandtype").parent().show();
	$("#wav").show();
}
function checkGongGong_branch(){
	if($("#branchname").val().length==0){
		alert("机构名称不能为空");
		return false;
	}if(!isAll($("#branchname").val())){
		alert("机构名称不能含有非法字符");
		return false;
	}if($("#branchcontactman").val().length==0){
		alert("负责人不能为空");
		return false;
	}if(!isAll($("#branchcontactman").val())){
		alert("负责人不能含有非法字符");
		return false;
	}
	return true;
}
function checkQiTa(){
	if(!checkGongGong_branch()){return false;}
	else if($("#remandtype").val()==0){
		alert("请选择拣线提示方式");
		return false;
	}
	//18对应了枚举中的语音提醒的值
	if(!checkWav()){return false;}
	return true;
}
function checkWav(){
	if(!$("#wav").is(":hidden")){
		if($("#update").contents().find("#wavText").val().length>4 && $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length-4)!=".wav" && $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length-4)!=".WAV"){
			alert("声音文件只能选择wav格式，如xxx.wav");
			return false;
		}
//		if($("#wavText").val().length==0 &&( $("#wavh").length==0||$("#wavh").val().length==0)){
//			alert("请选择上传文件");
//			return false;
//		}
	}
	return true;
}
function checkZhanDian(){
	if(!checkGongGong_branch()){return false;}
	if(!testEmail($("#branchemail").val())){
		alert("Email格式不正确");
		return false;
	}
	if(!checkWav()){return false;}
	return true;
}
function checkKeTuiCai(){
	if(!checkGongGong_branch()){return false;}
	if(!testEmail($("#branchemail").val())){
		alert("Email格式不正确");
		return false;
	}
	return true;
}
//验证机构字段是否正确输入
function check_branch(ZhanDian,YunYing,KeFu,CaiWu){
	var sitetype =  $("#sitetype").val();
	if(sitetype==-1){
		return false;
	}		
	if(sitetype==ZhanDian){//站点
		return checkZhanDian();
	}else if(sitetype==YunYing){//运营
		return checkKeTuiCai();
	}else if(sitetype==KeFu){//客服
		return checkKeTuiCai();
	}else if(sitetype==CaiWu){//财务
		return checkKeTuiCai();
	}else{//库房 中转站 退货站 其他
		return checkQiTa();
	}
	return true;		
}
//监控机构类型变化 对显示字段做相应处理
function click_sitetype(ZhanDian,YunYing,KeFu,CaiWu){
	var sitetype =  $("#sitetype").val();
	init_branch();
	//库房 中转站 退货站 其他
	if(sitetype==ZhanDian){//站点
		zhandianObj();
	}else if(sitetype==YunYing){//运营
		yunyingObj();
	}else if(sitetype==KeFu){//客服
		kefuObj();
	}else if(sitetype==CaiWu){//财务
		caiwuObj();
	}else{
		qitaObj();
	}
	//$("#remandtype").val(0);
	centerBox();
}
//创建机构提交
function submitAddBranch(form){
	
	if($("#update").contents().find("#wavText").val()==""){
		$(form).attr("enctype","");
		$(form).attr("action","branch/create");
		submitCreateForm(form);
		return;
	}
	
	$("#update")[0].contentWindow.submitBranchLoad();
}
//修改机构提交
function submitEditBranch(form,key){
	
	if($("#update").contents().find("#wavText").val()==""){
		$(form).attr("enctype","");
		$(form).attr("action","branch/save/"+key);
		submitSaveForm(form);
		return;
	}
	
	$("#update")[0].contentWindow.submitBranchLoad();
}

function initBranch(functionids){
	$("#cb_"+functionids).attr("checked","checked");
}

//创建和修改时 如果上传声音文件 泽通过 swfupload插件上传
function submitBranchLoad(){
	$("#sub",parent.document).attr("disabled","disabled");
	$("#sub",parent.document).val("保存中...");
	$('#swfupload-control').swfupload('addPostParam','sitetype',$("#sitetype",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchname',$("#branchname",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchcontactman',$("#branchcontactman",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchphone',$("#branchphone",parent.document).val());			
	$('#swfupload-control').swfupload('addPostParam','branchaddress',$("#branchaddress",parent.document).val());	
	//$('#swfupload-control').swfupload('addPostParam','accountarea',$("#accountarea").val());
	$('#swfupload-control').swfupload('addPostParam','branchmobile',$("#branchmobile",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchemail',$("#branchemail",parent.document).val());
	//$('#swfupload-control').swfupload('addPostParam','branchemail',$("#branchemail").val());
	/*
			 <li><span>预付款后缴款设置：</span><input type="hidden" name="" class ="zhandian" /></li>
			 <li><span>账户设置：</span><input type="hidden" name="" class ="zhandian" /></li>
	         <li><span>额度设置：</span><input type="hidden" name="" class ="zhandian" /></li>
	         <li><span>货物流向设置：</span><input type="hidden" name="" class ="zhandian" /></li>*/
	$('#swfupload-control').swfupload('addPostParam','branchprovince',$("#branchprovince",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','remandtype',$("#remandtype",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchmatter',$("#branchmatter",parent.document).val());
	var checkedValues = new Array();   
    $('input[name="functionids"]:checked',parent.document).each(function(){   
            checkedValues.push($(this).val());   
    }) ;
	$('#swfupload-control').swfupload('addPostParam','functionids',checkedValues);
	//$('#swfupload-control').swfupload('addPostParam','branchmatter',$("#branchmatter").val());
	/*	 <li><span>导出信息设置：</span><input type="hidden" name="" class ="kefu" /></li>
         <li><span>查询统计内容设置：</span><input type="hidden" name="" class ="yunying" /></li>*/
	$('#swfupload-control').swfupload('startUpload');
}
/////////////机构管理 END////////////////

/////////////常用语设置////////////////
function check_reason(){
	if($("#reasontype").val()==0){
		alert("请选择类型");
		return false;
	}
	return true;
}

///////////常用语设置END////////////////

///////////供货商设置////////////////
function check_customer(){
	if($("#customername").val().length==0){
		alert("供货商名称不能为空");
		return false;
	}
	return true;
}
/////////供货商设置////////////////

/////////供货商仓库设置////////////////
function check_customerwarehouses(){
if($("#customerwarehouse").val().length==0){
	alert("仓库不能为空");
	return false;
}if($("#customerid").val()==-1){
	alert("请选择您的供货商");
	return false;
}
	return true;	
}
/////////供货商仓库设置END////////////////

/////////供货商结算区域设置////////////////
function check_accountareas(){
	if($("#customerid").val()<0){
		alert("请选择供货商");
		return false;
	}
	if($("#areaname").val().length==0){
		alert("结算区域不能为空");
		return false;
	}
	return true;
}

///////供货商结算区域设置END////////////////

/////////角色权限设置//////////////
function check_role(){
	if($("#rolename").val().length==0){
		alert("角色名称不能为空");
		return false;
	}
	return true;
}

///////角色权限设置END//////////////
/////////员工管理//////////////
function checkRealname(){
	if($("#realname").val().length>0){
		$.ajax({
			url:"user/userrealnamecheck",
			data:{realname:$("#realname").val()},
			success:function(data){
				if(data==false)alert("员工姓名已存在");
				//else alert("员工姓名可用");
			}
		});
	}else{
		alert("员工姓名不能为空");
	}
}
function checkUsername(){
	if($("#username").val().length==0){
		alert("员工登录名不能为空");
		return;
	}
	if(!isLetterAndNumber($("#username").val())){
		alert("员工登录名格式不正确");
		return;
	}
	$.ajax({
		url:"user/usercheck",
		data:{username:$("#username").val()},
		success:function(data){
			if(data==false)alert("员工登录名已存在");
			//else alert("员工登录名可用");
		}
	});
	
}

function check_user(){
	
	
	if($("#realname").val().length==0){
		alert("员工姓名不能为空");
		return false;
	}if($("#branchid").val()==-1){
		alert("请选择员工所在机构");
		return false;
	}if($("#showphoneflag").val()==-1){
		alert("请选择员工 订单电话/手机是否可见");
		return false;
	}if($("#employeestatus").val()==-1){
		alert("请选择员工的工作状态");
		return false;
	}
//	if($("#usersalary").val().length==0){
//		$("#usersalary").val(0);
//	}if(!isFloat($("#usersalary").val())){
//		alert("员工基本工资需要为整数");
//		return false;
//	}if($("#userphone").val().length>50){
//		alert("员工联系电话超长");
//		return false;
//	}
//	if($("#useraddress").val().length>25){
//		alert("员工联系地址超长");
//		return false;
//	}if($("#userremark").val().length>250){
//		alert("员工备注信息超长");
//		return false;
//	}
	if($("#update").contents().find("#wavText").val().length>4 && $("#update").contents().find("#wavText").val().substring($("#update").contents().find("#wavText").val().length-4)!=".wav" && $("#update").contents().find("#wavText").val().substring($("#wavText").val().length-4)!=".WAV"){
		alert("员工声音文件只能选择wav格式，如xxx.wav");
		return false;
	}if($("#username").val().length==0){
		alert("员工登录名不能为空");
		return false;
	}if(!isLetterAndNumber($("#username").val())){
		alert("员工登录名格式不正确");
		return false;
	}if($("#password").val().length==0){
		alert("员工登录密码不能为空");
		return false;
	}if($("#password1").val()!=$("#password").val()){
		alert("员工登录密码两次输入不一致");
		return false;
	}if($("#roleid").val()==-1){
		alert("请选择员工对应的角色");
		return false;
	}
	return true;
}

function submitAddUser(form){
		
		if($("#update").contents().find("#wavText").val()==""){
			$(form).attr("enctype","");
			$(form).attr("action","user/create");
			submitCreateForm(form);
			return;
		}
		
		$("#update")[0].contentWindow.submitUserLoad();
}
function submitSaveUser(form,key){
	
	if($("#update").contents().find("#wavText").val()==""){
		$(form).attr("enctype","");
		$(form).attr("action","user/save/"+key);
		submitSaveForm(form,key);
		return;
	}
	$("#update")[0].contentWindow.submitUserLoad();
}


//创建和修改用户时 如果选择上传声音文件 泽通过 swfupload插件上传
function submitUserLoad(){
	$("#sub",parent.document).attr("disabled","disabled");
	$("#sub",parent.document).val("保存中...");
	$('#swfupload-control').swfupload('addPostParam','realname',$("#realname",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','branchid',$("#branchid",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','showphoneflag',$("#showphoneflag",parent.document).val());
	 
	$('#swfupload-control').swfupload('addPostParam','employeestatus',$("#employeestatus",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','idcardno',$("#idcardno",parent.document).val());
//	$$("#update").contents().find('#swfupload-control').swfupload('addPostParam','usersalary',$("#usersalary").val());
//	$('#swfupload-control').swfupload('addPostParam','userphone',$("#userphone").val());
	$('#swfupload-control').swfupload('addPostParam','usermobile',$("#usermobile",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','useremail',$("#useremail",parent.document).val());
//	$('#swfupload-control').swfupload('addPostParam','useraddress',$("#useraddress").val());
//	$('#swfupload-control').swfupload('addPostParam','userremark',$("#userremark").val());
	$('#swfupload-control').swfupload('addPostParam','username',$("#username",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','password',$("#password",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','password1',$("#password1",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','roleid',$("#roleid",parent.document).val());
	$('#swfupload-control').swfupload('addPostParam','usercustomerid',$("#usercustomerid",parent.document).val());
	$('#swfupload-control').swfupload('startUpload');
}
/////////员工管理END//////////////

/////////超额提示设置//////////////
function check_exceedfee(){
	if(!isFloat($("#exceedfee").val())){
		alert("输入的数字格式不正确！");
		return false;
	}
	return true;
}
/////////超额提示设置END//////////////

/////////设置权限//////////////
function getEditRoleAndMenu(key){
	$.ajax({
		type: "POST",
		url:$("#editRoleAndMenu").val()+key,
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			editRoleAndMenuInit();//初始化ajax弹出页面
			viewBox();
		}
	});
}

function submitEditRoleAndMenuForm(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if(data.errorCode==0){
				$("#WORK_AREA")[0].contentWindow.editRoleAndMenuSuccess(data);
			}
		}
	});
}

function initMenu(role_menu){
	$("#cb_"+role_menu).attr("checked","checked");
	$("#cb_PDA").attr("checked","checked");
	checkMenu(role_menu);
	checkPDAMenu();
}

function checkMenu(menu){
	if($("#menu_"+menu)){
		if($("#menu_"+menu).is(":hidden") && $("#cb_"+menu).attr("checked")=="checked"){
			$("#menu_"+menu).show();
		}else{
			$("#menu_"+menu).hide();
			$("#menu_"+menu).find("input[type='checkbox']").attr("checked",false);
		}
	}
}

function checkAll(menu){
	if($("#menu_"+menu)){
		if($("#menu_"+menu).is(":hidden")){
			$("#menu_"+menu).parent().find("div").show();
			$("#menu_"+menu).parent().find("input[type='checkbox']").attr("checked",true);
		}else{
			$("#menu_"+menu).hide();
			$("#menu_"+menu).parent().find("input[type='checkbox']").attr("checked",false);
		}
	}
}
function checkPDAMenu(){
	if($("#menuPDA1").is(":hidden") && $("#menuPDA2").is(":hidden") && $("#cb_PDA").attr("checked")=="checked"){
		$("#menuPDA1").show();
		$("#menuPDA2").show();
	}else{
		$("#menuPDA1").hide();
		$("#menuPDA2").hide();
		$("#menuPDA1").find("input[type='checkbox']").attr("checked",false);
	}
}
function checkPDAAll(){
	if($("#menuPDA1") && $("#menuPDA2")){
		if($("#menuPDA1").is(":hidden") && $("#menuPDA2").is(":hidden")){
			$("#menuPDA1").show();
			$("#menuPDA2").show();
			$("#PDAMenu").find("input[type='checkbox']").attr("checked",true);
		}else{
			$("#menuPDA1").hide();
			$("#menuPDA2").hide();
			$("#PDAMenu").find("input[type='checkbox']").attr("checked",false);
		}
	}
}

///////设置权限END//////////////

///////归班汇总修改BEGIN////////////
function init_deliverystate(){
	$("#backreasonid").parent().hide();
	$("#leavedreasonid").parent().hide();
	$("#podremarkid").parent().hide();
	$("#businessfee").hide();
	$("#returnedfee").parent().hide();
	$("#receivedfeecash").parent().hide();
	$("#receivedfeepos").parent().hide();
	$("#posremark").parent().hide();
	$("#receivedfeecheque").parent().hide();
	$("#receivedfeeother").parent().hide();
	$("#checkremark").parent().hide();
	$("#deliverstateremark").parent().hide();
}

function gonggong(){
	$("#podremarkid").parent().show();
	$("#deliverstateremark").parent().show();
}
function peisongObj(){
	gonggong();
	$("#businessfee").show();
	$("#receivedfeecash").parent().show();
	$("#receivedfeepos").parent().show();
	$("#posremark").parent().show();
	$("#receivedfeecheque").parent().show();
	$("#receivedfeeother").parent().show();
	$("#checkremark").parent().show();
}
function shagnmentuiObj(){
	gonggong();
	$("#backreasonid").parent().show();
	$("#returnedfee").parent().show();
}
function shangmenhuanObj(){
	gonggong();
	$("#leavedreasonid").parent().show();
}

function quantuiObj(){
	gonggong();
	$("#backreasonid").parent().show();
	$("#returnedfee").parent().show();
}

function bufentuihuoObj(){
	gonggong();
	$("#backreasonid").parent().show();
	$("#returnedfee").parent().show();
}
function zhiliuObj(){
	gonggong();
	$("#leavedreasonid").parent().show();
}
function shangmenjutuiObj(){
	gonggong();
	$("#backreasonid").parent().show();
}
function huowudiushiObj(){
	gonggong();
}
//监控配送状态变化 对显示字段做相应处理
function click_podresultid(PeiSongChengGong,ShangMenTuiChengGong,ShangMenHuanChengGong,QuanBuTuiHuo,BuFenTuiHuo,FenZhanZhiLiu,ShangMenJuTui,HuoWuDiuShi){
	var podresultid =  parseInt($("#podresultid").val());
	init_deliverystate();
	if(podresultid==PeiSongChengGong){//配送成功
		peisongObj();
	}else if(podresultid==ShangMenTuiChengGong){//上门退成功
		shagnmentuiObj();
	}else if(podresultid==ShangMenHuanChengGong){//上门换成功
		shangmenhuanObj();
	}else if(podresultid==QuanBuTuiHuo){//全部退货
		quantuiObj();
	}else if(podresultid==BuFenTuiHuo){//部分退货
		bufentuihuoObj();
	}else if(podresultid==FenZhanZhiLiu){//分站滞留
		zhiliuObj();
	}else if(podresultid==ShangMenJuTui){//上门拒退
		shangmenjutuiObj();
	}else if(podresultid==HuoWuDiuShi){//货物丢失
		huowudiushiObj();
	}
	//$("#remandtype").val(0);
	centerBox();
}

//验证字段是否正确输入
function check_deliveystate(PeiSongChengGong,ShangMenTuiChengGong,ShangMenHuanChengGong,QuanBuTuiHuo,BuFenTuiHuo,FenZhanZhiLiu,ShangMenJuTui,HuoWuDiuShi){
	var podresultid =  parseInt($("#podresultid").val());
	if(podresultid==-1){
		return checkGongGong_delivery();
	}		
	
	if(podresultid==PeiSongChengGong){//配送成功
		return checkPeiSong();
	}else if(podresultid==ShangMenTuiChengGong){//上门退成功
		return checkShangMenTui();
	}else if(podresultid==ShangMenHuanChengGong){//上门换成功
		return checkGongGong_delivery();
	}else if(podresultid==QuanBuTuiHuo){//全部退货
		return checkGongGong_delivery();
	}else if(podresultid==BuFenTuiHuo){//部分退货
		return checkGongGong_delivery();
	}else if(podresultid==FenZhanZhiLiu){//分站滞留
		return checkGongGong_delivery();
	}else if(podresultid==ShangMenJuTui){//上门拒退
		return checkGongGong_delivery();
	}else if(podresultid==HuoWuDiuShi){//货物丢失
		return checkGongGong_delivery();
	}
	
	return true;		
}

function checkGongGong_delivery(){
	if(parseInt($("#podresultid").val())==-1){
		alert("请选择配送结果");
		return false;
	}
	return true;
}

function checkPeiSong(){
	if(!checkGongGong_delivery()){return false;}

	if(parseFloat($("#receivedfeecash").val())<1&&parseFloat($("#receivedfeepos").val())<1&&parseFloat($("#receivedfeecheque").val())<1&&parseFloat($("#receivedfeeother").val())<1){
		alert("金额不能全为空");
		return false;
	}if((parseFloat($("#receivedfeecash").val())+parseFloat($("#receivedfeepos").val())+parseFloat($("#receivedfeecheque").val())+parseFloat($("#receivedfeeother").val())!=parseFloat($("#infactfee").val()))){
		alert("配送成功时，总金额不等于应收款，不可以反馈");
		return false;
	}
	return true;
}

function checkShangMenTui(){
	if(!checkGongGong_delivery()){return false;}
	if(parseFloat($("#returnedfee").val())<1){
		alert("退还金额不能为空");
		return false;
	}
}
/*function checkQuanTui(){
	if(!checkGongGong_delivery()){return false;}
	if(parseFloat($("#returnedfee").val())<1){
		alert("退还金额不能为空");
		return false;
	}
}
function checkBuFenTui(){
	if(!checkGongGong_delivery()){return false;}
	if(parseFloat($("#returnedfee").val())<1){
		alert("退还金额不能为空");
		return false;
	}
}*/

