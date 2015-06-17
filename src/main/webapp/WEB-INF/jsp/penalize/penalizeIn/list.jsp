<%@page import="cn.explink.domain.PenalizeType"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.PenalizeInside"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String punishsumprice=request.getAttribute("punishsumprice")==null?"0.00":request.getAttribute("punishsumprice").toString();
	List<Reason> r =request.getAttribute("lr")==null?null:(List<Reason>)request.getAttribute("lr");
	List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	Page page_obj = (Page)request.getAttribute("page_obj");
	 String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
  String sitetype = request.getAttribute("sitetype")==null?"":request.getAttribute("sitetype").toString();
  long roleid = Long.parseLong(request.getAttribute("roleid").toString());
  long userid = Long.parseLong(request.getAttribute("userid").toString());
  String url=request.getContextPath()+"/abnormalOrder/getbranchusers";
  List<PenalizeInside> penalizeInsides=(List<PenalizeInside>) request.getAttribute("penalizeInsides");
  List<PenalizeType> penalizebigList=(List<PenalizeType>)request.getAttribute("penalizebigList");
  List<PenalizeType> penalizesmallList=(List<PenalizeType>)request.getAttribute("penalizesmallList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/punishinside.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script language="javascript">
var file_id;
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#wenticjbegintime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#wenticjendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#gongdanshoulibegintime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#gongdanshouliendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	//上传附件使用
	$('#swfupload-control').swfupload({
		upload_url : $("#form2").attr("action"),
		file_size_limit : "10240",
		file_types : "*.img;*.txt",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : "<%=request.getContextPath()%>/js/swfupload/swfupload.swf",
		button_image_url :"<%=request.getContextPath()%>/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#button")[0]
	}).bind('fileQueued', function(event, file) {
		$("#txtFileName").val(file.name);
		file_id = $('#swfupload-control');
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		$("#punishbigsort3").val(0);
		$("#punishsmallsort3").val(0);
		$("#dutybranchid3").val(0);
		$("#dutypersoname3").val(0);
		$("#punishprice3").val("");
		$("#txtFileName").val("");
		$("#describe3").val("最多一100个字");
		$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
		$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
	
	//上传附件使用
	$('#swfupload-control1').swfupload({
		upload_url : $("#form3").attr("action"),
		file_size_limit : "10240",
		file_types : "*.img;*.txt",
		file_types_description : "All Files",
		file_upload_limit : "0",
		file_queue_limit : "1",
		flash_url : "<%=request.getContextPath()%>/js/swfupload/swfupload.swf",
		button_image_url :"<%=request.getContextPath()%>/images/indexbg.png",
		button_text : '选择文件',
		button_width : 50,
		button_height : 20,
		button_placeholder : $("#button1")[0]
	}).bind('fileQueued', function(event, file) {
		$("#txtFileName1").val(file.name);
		file_id = $('#swfupload-control1');
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {


	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		$(".tishi_box", parent.document).html(dataObj.error);
		$(".tishi_box", parent.document).show();
		setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
		$("#punishbigsort2").val(0);
		$("#punishsmallsort2").val(0);
		$("#dutybranchid2").val(0);
		$("#dutypersoname2").val(0);
		$("#punishprice2").val("");
		$("#txtFileName1").val("");
		$("#describe2").val("最多一100个字");
		$(form)[0].reset();
		$('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
		$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj);
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});

});


function check(){

	if($.trim($("#cwb").val()).length==0)
	{
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if(!Days($("#strtime").val(),$("#endtime").val())||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于31天！");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return;
	}

	}
	$("#searchForm").submit();
	
}
function Days(day1,day2){     
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>31){
		return false;
	}        
	return true;
}	
function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}		


function isgetallcheck(){
	if($('input[name="id"]:checked').size()>0){
		$('input[name="id"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="id"]').attr("checked",true);
	}
}
function createinpunishbycwb(){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/inpunish/createinpunishbycwb",
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function shensuopteration(){
	var ids="";
	$('input[name="wentid"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	if(ids.length==0){
		alert("请选择要操作的记录！");
		return;
	}
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/inpunish/shensupage",
		data:{ids:ids},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function shenheopteration(id){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/inpunish/shenhepage",
		data:{id:id},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function findthisValue(id){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/inpunish/findthisValue",
		data:{id:id},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
		},
		complete:function(){
			viewBox();
		}
	});
}
function createinpunishbygogdan(){
	$("#appendhtmlid2").html("");
	$("#alert_box").show();
	centerBox();
}
function createinpunishbyquestionno(){
	$("#appendhtmlid3").html("");
	$("#alert_box1").show();
	centerBox1();
}
//根据工单创建对内扣罚单
function submitPunishCreateBygongdan(form){
	if ($("#txtFileName1").val()=="") {
		$(form).attr("enctype", "");
		$(form).attr("action", "<%=request.getContextPath()%>/inpunish/submitPunishCreateBygongdanLoad");
		submitCreateFormAdd(form);
		return;
	}
	$('#swfupload-control1').swfupload('addPostParam', 'punishbigsort2', $("#punishbigsort2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'punishsmallsort2', $("#punishsmallsort2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'dutybranchid2', $("#dutybranchid2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'dutypersoname2', $("#dutypersoname2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'punishprice2', $("#punishprice2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'describe2', $("#describe2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'type2', $("#type2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'availablecwb2', $("#availablecwb2").val());
	$('#swfupload-control1').swfupload('addPostParam', 'cwbhhh2', $("#cwbhhh2").val());
	$('#swfupload-control1').swfupload('startUpload');
	//submitPunishCreateBygongdanLoad();
}
//根据问题件创建对内扣罚单
function submitPunishCreateBywentijian(form){
	if ($("#txtFileName").val()=="") {
		$(form).attr("enctype", "");
		$(form).attr("action", "<%=request.getContextPath()%>/inpunish/submitPunishCreateBywentijian");
		submitCreateFormAdd(form);
		return;
	}
	$('#swfupload-control').swfupload('addPostParam', 'punishbigsort3', $("#punishbigsort3").val());
	$('#swfupload-control').swfupload('addPostParam', 'punishsmallsort3', $("#punishsmallsort3").val());
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid3', $("#dutybranchid3").val());
	$('#swfupload-control').swfupload('addPostParam', 'dutypersoname3', $("#dutypersoname3").val());
	$('#swfupload-control').swfupload('addPostParam', 'punishprice3', $("#punishprice3").val());
	$('#swfupload-control').swfupload('addPostParam', 'describe3', $("#describe3").val());
	$('#swfupload-control').swfupload('addPostParam', 'type1', $("#type1").val());
	$('#swfupload-control').swfupload('addPostParam', 'availablecwb3', $("#availablecwb3").val());
	$('#swfupload-control').swfupload('addPostParam', 'cwbhhh3', $("#cwbhhh3").val());
	$('#swfupload-control').swfupload('startUpload');
	//submitPunishCreateBygongdanLoad();
}
/* //根据工单创建对内扣罚单（带文件的）
function submitPunishCreateBygongdanLoad() {
	$('#swfupload-control').swfupload('addPostParam', 'punishbigsort1', $("#punishbigsort1").val());
	$('#swfupload-control').swfupload('addPostParam', 'punishsmallsort1', $("#punishsmallsort1").val());
	$('#swfupload-control').swfupload('addPostParam', 'dutybranchid1', $("#dutybranchid1").val());
	$('#swfupload-control').swfupload('addPostParam', 'dutypersoname1', $("#dutypersoname1").val());
	$('#swfupload-control').swfupload('addPostParam', 'punishprice1', $("#punishprice1").val());
	$('#swfupload-control').swfupload('addPostParam', 'describe1', $("#describe1").val());
	$('#swfupload-control').swfupload('addPostParam', 'type1', $("#type1").val());
	$('#swfupload-control').swfupload('addPostParam', 'availablecwb', $("#availablecwb").val());
	$('#swfupload-control').swfupload('startUpload');
} */
function check_revisefindWithByCreateQuestionNo(){
	if($("#cwb").val()==""&&$("#wenticwb").val()==""&&$("wentitype").val()==0&&$("#wentistate").val()==0&&$("#wenticjbegintime").val()==""&&$("#wenticjendtime").val()==""){
		alert("至少选择一个条件进行查询！！");
		return false;
	}
	if($("#wenticjbegintime").val("")==""){
		if($("#wenticjendtime").val()!=""){
			alert("请输入开始时间！！");
		}else{
			alert("请输入时间！！");
		}
		return false;
	}
	if($("#wenticjendtime").val("")==""){
		if($("#wenticjbegintime").val()!=""){
			alert("请输入结束时间！！");
		}else{
			alert("请输入时间！！");
		}
		return false;
	}
	if($("#wenticjendtime").val("")<$("#wenticjbegintime").val()){
		alert("结束时间不能小于开始时间");
		return false;
	}
	return true;
}
function resetData(){
	$("#cwb").val("");
	$("#dutybranchid").val(0);
	$("#cwbstate").val(0);
	$("#cwbpunishtype").val(0);
	$("#dutyname").val(0);
	$("#punishbigsort").val(0);
	$("#strtime").val("");
	$("#endtime").val("");
}
function getReasonValueVh(){
	/* var jsonData = {};
	jsonData['complaintOneLevel'] = $('#olreasonV').val(); */
	$.ajax({
		type:'POST',
		data:'complaintOneLevel='+$('#tousuonesort').val(),
		url:"<%=request.getContextPath()%>/workorder/getTwoValueByOneReason",
		dataType:'json',
		success:function(data){
			var Str = "<option value='-1'>全部</option>";
			$.each(data,function(ind,ele){
				var dataTrStr = "<option value='" + ele.reasonid + "'>" + ele.reasoncontent+ "</option>";
				Str+=dataTrStr;
			});
			
			$('#tousutwosort').html(Str);
		}
	
	});	
}
function closeBox1() {
	$("#alert_box1").hide(300);
	$("#dress_box").css("visibility", "");
	$(".alert_box").hide(300);
	$(".dress_box").css("visibility", "");
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">

	<!-- 2弹出框开始 -->
	<div id="alert_box1" style="display:none; " align="center" tip="1">
	<div id="box_contant1" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="overflow: scroll;width: 800px;height: 600px;">
		<h1><div id="close_box" onclick="closeBox1()"></div>根据问题件创建对内扣罚单</h1>
		<form method="post" id="wentiform"  action="<%=request.getContextPath()%>/inpunish/createinpunishbyQuestNo" onsubmit="if(check_revisefindWithByCreateQuestionNo())createinpunishbyQuestNo(this,'appendhtmlid3');return false;">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="1" align="left" valign="top">订单号：
							<input type="text" id="cwb" name="cwb" class="input_text1" style="height:15px;width: 120px;"/>
							&nbsp;&nbsp;问题件单号：
							<input type="text" id="wenticwb" name="wenticwb" class="input_text1" style="height:15px;width: 120px;"/>
							&nbsp;&nbsp;问题件类型：
							<select id="wentitype" name="wentitype" class="select1">
							<option value="0" selected="selected">请选择问题件类型</option>
										<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
										<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>25){%><%=at.getName().substring(0,25)%><%}else{%><%=at.getName() %><%} %></option>
										<%} %>
							</select>
							&nbsp;&nbsp;处理状态：
							<select id="wentistate" name="wentistate" class="select1">
									<option value="0">请选择处理状态</option>
									<option value="<%=AbnormalOrderHandleEnum.weichuli.getValue()%>"><%=AbnormalOrderHandleEnum.weichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.daichuli.getValue()%>"><%=AbnormalOrderHandleEnum.daichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.yichuli.getValue()%>"><%=AbnormalOrderHandleEnum.yichuli.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.jieanchuli.getValue()%>"><%=AbnormalOrderHandleEnum.jieanchuli.getText() %></option>
							</select>						
							</tr>
						<tr class="font_1">
							<td  align="left" valign="top">
							&nbsp;&nbsp;创建时间：
									<input type ="text" name ="wentibegindateh" id="wenticjbegintime"  value="<%=request.getParameter("begindateh")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:15px;width: 120px;"/>
									到
									<input type ="text" name ="wentienddateh" id="wenticjendtime"  value="<%=request.getParameter("enddateh")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:15px;width: 120px;"/>
						</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
							<input type="submit"   value="查询" class="input_button2"/>
							<input type="reset"    value="重置" class="input_button2" >
<!-- 							<input type="button" onclick="checkCreateGongdan();"   value="选中的值" class="input_button2" >
 -->						</td>
						</tr>
			</table>
			</td>
			</tr>
			</table>
			</form>
			<tr class="font_1">
				<td  align="left" valign="top">
				 <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
        		<tbody >
        			<tr class="font_1" height="30">
        				<td align="center" valign="middle" bgcolor="#E7F4E3">问题件单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单类型</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">创建人</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">创建时间</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">问题件类型</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">处理状态</td>
        				
        				
        				<td align="center" valign="middle" bgcolor="#E7F4E3">处理结果</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">丢失找回</td>
        				</tr>
                        </tbody>
                        <tbody  id="appendhtmlid3" style="width: 100px;height:100px; ">
                        </tbody>
                        
                    </table>
            </div>
				</td>
						</tr>
		<form method="post" id="form2" onSubmit="if(check_createbywentijian()){submitPunishCreateBywentijian(this);}return false;" action="<%=request.getContextPath()%>/inpunish/submitPunishCreateBywentijianfile;jsessionid=<%=session.getId()%>" enctype="multipart/form-data">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td align="left" valign="top">
							扣罚大类<font color="red">*</font>：
							<select id="punishbigsort3" name="punishbigsort3" class="select1" onchange="findbigAdd(this,'punishbigsort2');">
								<option value="0">==请选择扣罚大类==</option>
								<%if(penalizebigList!=null&&penalizebigList.size()>0){for(PenalizeType   pType :penalizebigList) {%>
								<option value="<%=pType.getId()%>"><%=pType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;扣罚小类：
							<select id="punishsmallsort3" name="punishsmallsort3" class="select1" onchange="findbigAdd(this,'punishbigsort2');">
								<option value="0">==请选择扣罚小类==</option>
								<%if(penalizesmallList!=null&&penalizesmallList.size()>0){for(PenalizeType     penType:penalizesmallList) {%>
								<option value="<%=penType.getId()%>" id="<%=penType.getParent() %>"><%=penType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;责任机构<font color="red">*</font>:<select  id="dutybranchid3" name="dutybranchid3" class="select1" onchange="selectbranchUsers('dutypersoname3','dutybranchid3');">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
						&nbsp;&nbsp;责任人:
							<select id="dutypersoname3" name="dutypersoname3" class="select1" >
							<option value ='0' selected="selected">请选择机构责任人</option>
							</select>
						
							</td>
						
						</tr >
						<tr class="font_1">
						<td align="left" valign="top">
						&nbsp;&nbsp;扣罚金额<font color="red">*</font>:<input type="text" id="punishprice3" name="punishprice3" class="input_text1" style="height:15px;width: 120px;"/>
						&nbsp;&nbsp;上传附件：
						<label for="fileField"></label>
						<span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button" /></span>*
						</td>
						</tr>
						 <tr class="font_1">
							<td  align="left" valign="top"> 
							扣罚说明：<textarea name="describe3" id="describe3" cols="40" rows="4"  onblur="jascript:if(this.val=='最多100个字')this.val=''" onfocus="javascript:if(this.val=='')this.val='最多一100个字'"></textarea>
							</td>
						</tr>  
					</table>
					
					</td>
				</tr>
			</table>
			<input type="hidden" id="type1" name="type1" value="3"/>
			<input type="hidden" id="availablecwb3" name="availablecwb3" value=""/>
			<input type="hidden" id="cwbhhh3" name="cwbhhh3" value=""/>
			<div align="center">
				<input type="submit" value="提交" class="button">
				<input type="button" onclick="closeBox1()"  value="取消" class="button">
			</div>
		</form>
	</div>
</div>
</div>
	<!-- 2弹出框结束 --> 
<!-- 弹出框开始 -->
	<div id="alert_box" style="display:none; " align="center" tip="2">
	<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 800px;height: 600px;overflow: auto;">
		<h1><div id="close_box" onclick="closeBox()"></div>根据工单创建对内扣罚单</h1>
		<form method="post" id="form1"  action="<%=request.getContextPath()%>/inpunish/querygogdan" onsubmit="revisefindWithByCreateGongdan(this,'appendhtmlid2');return false;">
			<table  width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">订单号：
							<input type="text" id="cwb" name="cwb" class="input_text1" style="height:15px;width: 120px;"/>
							&nbsp;&nbsp;工单号：
							<input type="text" id="gongdancwb" name="gongdancwb" class="input_text1" style="height:15px;width: 120px;"/>
							&nbsp;&nbsp;工单类型：
							<select id="gongdantype" name="gongdantype" class="select1">
							<option value="0" selected="selected">请选择工单类型</option>
							<option value="<%=ComplaintTypeEnum.DingDanChaXun.getValue()%>"><%=ComplaintTypeEnum.DingDanChaXun.getText()%></option>
							<option value="<%=ComplaintTypeEnum.CuijianTousu.getValue()%>"><%=ComplaintTypeEnum.CuijianTousu.getText()%></option>
							</select>
							&nbsp;&nbsp;工单状态：
							<select id="gongdanstate" name="gongdanstate" class="select1">
							<option value="-1">全部</option>
							<%for(ComplaintStateEnum c:ComplaintStateEnum.values()){ %>	
							<%if(c.getValue()!=0){ %>
							<option value="<%=c.getValue()%>"><%=c.getText()%></option>
						<%} }%>
							</select>						</tr>
						<tr class="font_1">
							<td  align="left" valign="top">被投诉机构： 
							<select  id="complainedmechanism" name="complainedmechanism" class="select1">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
							&nbsp;&nbsp;投诉处理结果：
							<select id="complainresultcontent" name="complainresultcontent" class="select1">
								<option value="-1">全部</option>
								<option value="<%=ComplaintResultEnum.WeiChuLi.getValue()%>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.ChengLi.getValue()%>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.BuChengLi.getValue()%>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>								
							</select>
						
							&nbsp;&nbsp;受理时间：
									<input type ="text" name ="begindateh" id="gongdanshoulibegintime"  value="<%=request.getParameter("begindateh")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:15px;width: 120px;"/>
									到
									<input type ="text" name ="enddateh" id="gongdanshouliendtime"  value="<%=request.getParameter("enddateh")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:15px;width: 120px;"/>
						</td>
						</tr>
						<tr  class="font_1">
						<td align="left" valign="top">
						投诉一级分类:
						<select id="tousuonesort" name="tousuonesort" class="select1" onchange="getReasonValueVh()">
							<option value="-1">全部</option>
								<%if(r!=null){%>
								<%for(Reason reason:r){ %>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} }%>
						</select>
						&nbsp;&nbsp;投诉二级分类:
						<select id="tousutwosort" name="tousutwosort" class="select1">
							<option value="0">==请选择投诉二级分类==</option>
						</select>
						</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
							<input type="submit"   value="查询" class="input_button2"/>
							<input type="reset"    value="重置" class="input_button2" >
						</td>
						</tr>
			</table>
			</td>
			</tr>
			</table>
			</form>
			<tr class="font_1">
				<td  align="left" valign="top">
				 <div class="right_title">
        	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table4">
        		<tbody>
        			<tr class="font_1" height="30">
        				<td align="center" valign="middle" bgcolor="#E7F4E3">工单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">工单类型</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">工单状态</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">来电号码</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">被投诉机构</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">工单受理人</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">受理事时间</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">投诉一级分类</td>
        				
        				
        				<td align="center" valign="middle" bgcolor="#E7F4E3">投诉二级分类</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">投诉处理结果</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">客户名称</td>
        				<td align="center" valign="middle" bgcolor="#E7F4E3">催件次数</td>
        				</tr>
                        </tbody>
                         <tbody  id="appendhtmlid2" style="width: 100px;height:100px; ">
                        </tbody>
                    </table>
            </div>
				</td>
						</tr>
		<form method="post" id="form3" onSubmit="if(check_createbygongdan()){submitPunishCreateBygongdan(this);}return false;" action="<%=request.getContextPath()%>/inpunish/submitPunishCreateBygongdanLoadfile;jsessionid=<%=session.getId()%>" enctype="multipart/form-data">
			<table width="900" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td align="left" valign="top">
							扣罚大类：<font color="red">*</font>
							<select id="punishbigsort2" name="punishbigsort2" class="select1" onchange="findsmallAdd('<%=request.getContextPath() %>',this,'punishsmallsort1');">
								<option value="0">请选择扣罚大类</option>
								<%if(penalizebigList!=null&&penalizebigList.size()>0){for(PenalizeType   pType :penalizebigList) {%>
								<option value="<%=pType.getId()%>"><%=pType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;扣罚小类：
							<select id="punishsmallsort2" name="punishsmallsort2" class="select1" onchange="findbigAdd(this,'punishbigsort1');">
								<option value="0">请选择扣罚小类</option>
								<%if(penalizesmallList!=null&&penalizesmallList.size()>0){for(PenalizeType     penType:penalizesmallList) {%>
								<option value="<%=penType.getId()%>" id="<%=penType.getParent() %>"><%=penType.getText() %></option>
								<%}} %>
							</select>
							&nbsp;&nbsp;责任机构<font color="red">*</font>:<select  id="dutybranchid2" name="dutybranchid2" class="select1" onchange="selectbranchUsers('dutypersoname2','dutybranchid2');">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
						&nbsp;&nbsp;责任人:<!-- <input type="text" id="dutypersoname2" name="dutypersoname2" class="input_text1" style="height:15px;width: 120px;"/> -->
							<select id="dutypersoname2" name="dutypersoname2" class="select1" >
							<option value ='0' selected="selected">请选择机构责任人</option>
							</select>
							</td>
						
						</tr >
						<tr class="font_1">
						<td align="left" valign="top">
						&nbsp;&nbsp;扣罚金额<font color="red">*</font>:<input type="text" id="punishprice2" name="punishprice2" class="input_text1" style="height:15px;width: 120px;"/>
						&nbsp;&nbsp;上传附件：
						<label for="fileField"></label>
						<span id="swfupload-control1"><input type="text" id="txtFileName1" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button1" /></span>*
						</td>
						</tr>
						 <tr class="font_1">
							<td  align="left" valign="top"> 
							扣罚说明：<textarea name="describe2" id="describe2" cols="40" rows="4"  onblur="jascript:if(this.val=='最多100个字')this.val=''" onfocus="javascript:if(this.val=='')this.val='最多一100个字'"></textarea>
							</td>
						</tr>  
					</table>
					
					</td>
				</tr>
			</table>
			<input type="hidden" id="type2" name="type2" value="2"/>
			<input type="hidden" id="availablecwb2" name="availablecwb2" value=""/>
			<input type="hidden" id="cwbhhh2" name="cwbhhh2" value=""/>
			<div align="center">
				<input type="submit" value="提交" class="button">
				<input type="button" onclick="closeBox()"  value="取消" class="button">
			</div>
		</form>
	</div>
</div>
</div>
	<!-- 弹出框结束 -->
	
	
<div class="right_box">
	<div style="background:#FFF">
		<div style="position:relative; z-index:0;" >
			<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
				<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<table width="100%"  style="font-size: 12px;">
								<tr>
								<td align="left" >
								订单号：
									<textarea id="cwb" class="kfsh_text" rows="2" name="cwb" ></textarea>
								</td>
								
								<td align="left">
								&nbsp;责 任 机构：
									<label for="select2"></label>
									<select name="dutybranchid" id="dutybranchid" class="select1" onchange="selectbranchUsers('dutyname','dutybranchid');">
										<option value="0">请选择责任机构</option>
										<%if(branchList!=null||branchList.size()!=0){for(Branch b : branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
										<%}} %>
									</select>
								<br>
								<br>
									扣罚单状态： <select name ="cwbpunishtype" id ="cwbpunishtype"  style="width: 120px;"  class="select1">
									<option value="0" >请选择</option>
									<%for(PunishInsideStateEnum    punishInsideStateEnum:PunishInsideStateEnum.values()){ %>
										<option value="<%=punishInsideStateEnum.getValue() %>"><%=punishInsideStateEnum.getText() %></option>
									<%} %>
		      						  </select>
		      						</td>
		      						<td>
									<label for="select3"></label>
									&nbsp;&nbsp;责      任      人：
									<select  id="dutyname" name="dutyname" class="select1">
									<option value ='0' selected="selected">请选择机构责任人</option>
									</select>
									<br><br>
									&nbsp;订单状态：
									<label for="select2"></label>
										<select name="cwbstate" id="cwbstate"  class="select1">
										<option value="0">请选择订单状态</option>
										<%for(FlowOrderTypeEnum flowOrderTypeEnum:FlowOrderTypeEnum.values()) {%>
											<option value="<%=flowOrderTypeEnum.getValue()%>"><%=flowOrderTypeEnum.getText() %></option>
										<%} %>
									</select>
									</td>
										<td>
									<label for="select3"></label>
									扣罚大类：
									<select name="punishbigsort" id="punishbigsort"  class="select1" onchange="findsmallAdd('<%=request.getContextPath() %>',this,'punishsmallsort');">
										<option value="0">请选择扣罚大类</option>
										<%if(penalizebigList!=null&&penalizebigList.size()>0){for(PenalizeType   pType :penalizebigList) {%>
								<option value="<%=pType.getId()%>"><%=pType.getText() %></option>
								<%}} %>
										
									</select>
										&nbsp;&nbsp;扣罚小类：
									<label for="select4"></label>
									<select name="punishsmallsort" id="punishsmallsort"  class="select1" onchange="findbigAdd(this,'punishbigsort');">
										<option value="0">请选择扣罚小类</option>
										<%if(penalizesmallList!=null&&penalizesmallList.size()>0){for(PenalizeType     penType:penalizesmallList) {%>
								<option value="<%=penType.getId()%>" id="<%=penType.getParent() %>"><%=penType.getText() %></option>
								<%}} %>
									</select>
									<br><br>
										<strong id="chuli">创建时间：</strong>
									<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:20px;"/>
									<strong id="chulidown">到</strong>
									<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:20px;"/>
									<input type="hidden" name="isshow" value="1"/>
									</td>
									</tr>
									<tr>
									<td align="left">
									<input type="button"   onclick="createinpunishbycwb()" value="根据订单创建" class="input_button1"/>
									<input type="button"  onclick="createinpunishbygogdan()" value="根据工单创建" class="input_button1"/>
									</td>
									<td>
									<input type="button"  onclick="createinpunishbyquestionno()" value="根据问题件创建" class="input_button1"/>
									<input type="button"  onclick="createbyshixiaokaohe()" value="根据时效考核" class="input_button1"/>
									</td>
									<td>
<!-- 									<input type="button"  onclick="shenheopteration()" value="审核" class="input_button2"/>
 -->									<input type="button"  onclick="shensuopteration()" value="申诉" class="input_button2"/>
									</td>
									<td align="left">
									<input type="button"  onclick="check()" value="查询" class="input_button2"/>
									<input type="button"    value="重置" class="input_button2" onclick="resetData();">
									<input type ="button" id="btnval" value="导出" class="input_button2" <%if(penalizeInsides.size()==0){ %> disabled="disabled" <%} %> onclick="exportField();"/>
									</td>
								</tr>
								
								</table>
								<input type="hidden" name="getbranchusers" id="getbranchusers" value="<%=url %>">
							</form>
				</div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tbody>
						<tr class="font_1" height="30" >
							<td width="30"  align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3">扣罚单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#f3f3f3">来源</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">来源单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">责任机构</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">责任人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单状态</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">订单金额</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">对内扣罚金额</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">扣罚大类</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">扣罚小类</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">创建日期</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">扣罚单状态</td>
							<td width="160" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
						</tr>
					</tbody>
				</table>
				</div>
			
			<div style="height: 500px;overflow:auto; ">
		    <div style="height: 140px;"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
				<tbody>
					<%if(penalizeInsides!=null||penalizeInsides.size()>0){for(PenalizeInside view : penalizeInsides){ %>
					<tr height="30" >
						<td width="30" align="center" valign="middle" bgcolor="#eef6ff">
						<input id="wentid" type="checkbox" value="<%=view.getId()%>"  name="wentid"/>
						</td>
						<td width="100" align="center" valign="middle"><%=view.getPunishNo() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCreateBysourcename() %></td>
						<td width="100" align="center" valign="middle"><%=view.getSourceNo() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCwb() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDutybranchname() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDutypersonname() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCwbstatename() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCwbPrice() %></td>
						<td width="100" align="center" valign="middle"><%=view.getPunishInsideprice()==null?"":view.getPunishInsideprice() %></td>
						<td width="100" align="center" valign="middle"><%=view.getPunishbigsortname() %></td>
						<td width="100" align="center" valign="middle"><%=view.getPunishsmallsortname() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCreUserName() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCreDate() %></td>
						<td width="100" align="center" valign="middle"><%=view.getPunishcwbstatename() %></td>
						<td width="160" align="center" valign="middle">
						<a href="javascript:shenheopteration('<%=view.getId() %>');" ><font color="blue">审核</font></a>
						<a href="javascript:findthisValue('<%=view.getId() %>');" ><font color="blue">查看</font></a>
						</td>
					</tr>
					<%} %>
						<tr>
						<td width="30" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"><font color="red">扣罚总计</font></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"><font color="red"><%=punishsumprice==""?0.00:punishsumprice %></font></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="100" align="center" valign="middle"></td>
						<td width="160" align="center" valign="middle"></td>
					</tr>
					<%} %>
				</tbody>
			</table>
			<div style="height: 120px;"></div>
			</div>
			</div>
			<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
			<%} %>
	</div>
</div>
<form action="<%=request.getContextPath()%>/inpunish/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name="cwbStrs" id="cwbStrs" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb") %>"/>

	<input type="hidden" name="dutybranchid1" id="dutybranchid1" value="<%=request.getParameter("dutybranchid")==null?"0":request.getParameter("dutybranchid")%>"/>
	<input type="hidden" name="dutyname1" id="dutyname1" value="<%=request.getParameter("dutyname")==null?"0":request.getParameter("dutyname")%>"/>
	<input type="hidden" name="cwbpunishtype1" id="cwbpunishtype1" value="<%=request.getParameter("cwbpunishtype")==null?"0":request.getParameter("cwbpunishtype")%>"/>
	<input type="hidden" name="cwbstate1" id="cwbstate1" value="<%=request.getParameter("cwbstate")==null?"0":request.getParameter("cwbstate")%>"/>
	<input type="hidden" name="punishbigsort1" id="punishbigsort1" value="<%=request.getParameter("punishbigsort")==null?"0":request.getParameter("punishbigsort")%>"/>
	<input type="hidden" name="punishsmallsort1" id="punishsmallsort1" value="<%=request.getParameter("punishsmallsort")==null?"0":request.getParameter("punishsmallsort")%>"/>
	<input type="hidden" name="begindate1" id="bedindate1" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
	<input type="hidden" name="enddate1" id="enddate1" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#cwbpunishtype").val(<%=request.getParameter("cwbpunishtype")==null?0:Long.parseLong(request.getParameter("cwbpunishtype"))%>);
$("#dutybranchid").val(<%=request.getParameter("dutybranchid")==null?0:Long.parseLong(request.getParameter("dutybranchid"))%>);
$("#cwbstate").val(<%=request.getParameter("cwbstate")==null?0:Long.parseLong(request.getParameter("cwbstate"))%>);
$("#cwbpunishtype").val(<%=request.getParameter("cwbpunishtype")==null?0:Long.parseLong(request.getParameter("cwbpunishtype"))%>);
$("#punishbigsort").val(<%=request.getParameter("punishbigsort")==null?0:Long.parseLong(request.getParameter("punishbigsort"))%>);
$("#punishsmallsort").val(<%=request.getParameter("punishsmallsort")==null?0:Long.parseLong(request.getParameter("punishsmallsort"))%>);
$("#strtime").val('<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>');
$("#endtime").val('<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>');
function exportField(){
	if(<%=penalizeInsides != null && penalizeInsides.size()>0 %>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

</script>
</body>
</html>

