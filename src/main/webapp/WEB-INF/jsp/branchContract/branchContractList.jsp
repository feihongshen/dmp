<%@page import="cn.explink.domain.ExpressSetBranchContract"%>
<%@page import="cn.explink.domain.VO.ExpressSetBranchContractVO"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.YesOrNoStateEnum"%>
<%@page import="cn.explink.enumutil.coutracManagementEnum.ContractStateEnum"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%
		List<ExpressSetBranchContract> branchContractList=request.getAttribute("branchContractList")==null?null:(List<ExpressSetBranchContract>)request.getAttribute("branchContractList");
		Page page_obj =request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
%> --%>
<%
	List<ExpressSetBranchContract> branchContractList=request.getAttribute("branchContractList")==null?null:(List<ExpressSetBranchContract>)request.getAttribute("branchContractList");
	/* ExpressSetBranchContractVO branchContract=(ExpressSetBranchContractVO)request.getAttribute("bc")==null?null:(ExpressSetBranchContractVO)request.getAttribute("bc"); */
%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript">
$(function(){
	$('#edit_btn').click(function(){
		
	});
	
	$("table#callertb tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	$('#delete_button').click(function(){
		
		var state = $("#branchContractState").val();
		var xinjian = "<%=ContractStateEnum.XinJian.getValue()%>";
		if(state != xinjian){
			alert("只有新建状态的合同才能进行删除!");
			return false;
		}
		/* var checked = $("#callertb input[type='checkbox'][name='count']");
		$(checked).each(function() {
			if ($(this).attr("checked") == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
			{ */
				$.ajax({
					type:'POST',
					data:'id='+$("#branchId").val(),
					url:'<%=request.getContextPath()%>/branchContract/deleteBranchContract',
					dataType:'json',
					success:function(data){
						if(data && data.errorCode==0){
							alert(data.error);
							window.location.href='<%=request.getContextPath()%>/branchContract/branchContractList';
						}
					}
				});
		/* 	}
		});  */
	});
});

function setBranchId(id,state){
	$("#branchId").val(id);
	$("#branchContractState").val(state);
}

function addInit(){
	
}
function afterInit(){

}

function editInit(){
	
}

function initSelect(){
	$("#contractState").append('<option value ="<%=ContractStateEnum.XinJian.getValue()%>"><%=ContractStateEnum.XinJian.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.ZhiXingZhong.getValue()%>"><%=ContractStateEnum.ZhiXingZhong.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.HeTongZhongZhi.getValue()%>"><%=ContractStateEnum.HeTongZhongZhi.getText()%></option>'); 
	$("#contractState").append('<option value ="<%=ContractStateEnum.HeTongJieShu.getValue()%>"><%=ContractStateEnum.HeTongJieShu.getText()%></option>'); 
	
	$("#isDeposit").append('<option value ="<%=YesOrNoStateEnum.No.getValue()%>"><%=YesOrNoStateEnum.No.getText()%></option>'); 
	$("#isDeposit").append('<option value ="<%=YesOrNoStateEnum.Yes.getValue()%>"><%=YesOrNoStateEnum.Yes.getText()%></option>'); 
}
function getPath(){
	var path = "<%=request.getContextPath()%>";
	return path;
}

function isFloatVal(tdInput){
	if(tdInput.value && !isFloat(tdInput.value)){
		$("#"+tdInput.id).val("");
		alert("请输入数字!!!");
		return false;
	}
}


var rowCount = 0;
function addContract() {
	$("#box_contant").html('');
	$("#box_contant").html('<div id="box_top_bg"></div>'+
			'	<div id="box_in_bg">'+
			'			<h1><div id="close_box" onclick="closeBox()"></div>创建加盟商合同</h1>'+
			'		<div id="box_form">'+
			'			<form method="post" onSubmit="addContractForm(this);return false;" action="<%=request.getContextPath()%>/branchContract/addBranchContractfile;jsessionid=<%=session.getId()%>" id="addcallerForm" enctype="multipart/form-data">'+
			'		 	<table>'+
			'					<tr>'+
			'						<th align="left">编号</th>'+
			'						<td><input type="text" name="contractNo" value="[自动生成]" id="contractNo" maxlength="20"/></td>'+
			'						<th align="left">合同状态</th>'+
			'						<td>'+
			'							<select id="contractState" name="contractState">'+
			'				         	</select>'+
			'		           		</td>'+
			'						<th align="left">合同日期范围</th>'+
			'						<td>'+
			'							<input type="text" name="contractBeginDate" id="contractBeginDate" class="input_text1">'+
			'							至'+
			'							<input type="text" name="contractEndDate" id="contractEndDate" class="input_text1">'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">加盟商名称</th>'+
			'						<td><input type="text" name="branchName" value="" id="branchName" maxlength="50"/></td>'+
			'						<th align="left">站点负责人</th>'+
			'						<td><input type="text" name="siteChief" value="" id="siteChief" maxlength="20"/></td>'+
			'						<th align="left">负责人身份证</th>'+
			'						<td><input type="text" name="chiefIdentity" value="" id="chiefIdentity" maxlength="20" onblur="IdCardValidate(this.value)"/></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">区域经理</th>'+
			'						<td><input type="text" name="areaManager" value="" id="areaManager" maxlength="20"/></td>'+
			'						<th align="left">是否有押金</th>'+
			'						<td>'+
			'							<select id ="isDeposit" name ="isDeposit" onchange="changeDeposit();">'+
			'					        </select>'+
			'						</td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">质控条款</th>'+
			'						<td colspan="5">'+
			'						<textarea style="width:100%;height:60px;resize: none;" maxlength="500" name="qualityControlClause" id="qualityControlClause"></textarea>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">合同详细描述</th>'+
			'						<td colspan="5">'+
			'						<textarea style="width:100%;height:60px;resize: none;" maxlength="500" name="contractDescription" id="contractDescription"></textarea>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			' 						<th align="left" for="fileField" >上传附件：</th>'+
			'						<td> '+
			'							<span id="swfupload-control"><input type="text" id="txtFileName" class="input_text1" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" />'+
			'						</td>'+
			'						<td> '+
			'							<input type="button" id="button" /></span>*'+
			'						</td>'+
			'					</tr>'+
			'					<tr id="firstDepositTr" style="display: none">'+
			'						<th align="left"><font color="red">*</font>押金收取日期</th>'+
			'						<td><input type="text" name="depositCollectDate" id="depositCollectDate" value="" class="input_text1"/></td>'+
			'						<th align="left"><font color="red">*</font>押金收取金额</th>'+
			'						<td><input type="text" name="depositCollectAmount" value="" id="depositCollectAmount" maxlength="20"/></td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>					'+
			'					<tr id="secondDepositTr" style="display: none">'+
			'						<th align="left"><font color="red">*</font>押金收取人</th>'+
			'						<td><input type="text" name="depositCollector" value="" id="depositCollector" maxlength="20"/></td>'+
			'						<th align="left"><font color="red">*</font>押金付款人</th>'+
			'						<td><input type="text" name="depositPayor" value="" id="depositPayor" maxlength="20"/></td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>'+
			'			</table>'+
			'			<div align="center">'+
			'				<input type="submit" value="提交" class="button">'+
			'				<input type="button" value="取消" class="button" align="center" onclick="closeBox()"/>'+
			'			</div>'+
			'		</form>	'+
			'		</div>'+
			'		</div>');
	initSelect();
	$("#contractBeginDate").datepicker();
	$("#contractEndDate").datepicker();
	$("#depositCollectDate").datepicker();
	
	$('#contractNo').focus(function(){
		$('#contractNo').val('');
	}).blur(function(){
		$('#contractNo').val('[自动生成]');
	});
	
	$("#alert_box").show();
	upLoad();
	centerBox();
}

function updateContract(){
	$("#box_contant").html('');
	$("#box_contant").html('<div id="box_top_bg"></div>'+
			'	<div id="box_in_bg">'+
			'		<h1><div id="close_box" onclick="closeBox()"></div>修改加盟商合同</h1>'+
			'		<div id="box_form">'+
			'			<form action="<%=request.getContextPath()%>/branchContract/updateBranchContract" id="editcallerForm">'+
			'			<div align="center">'+
			'				<input type="button" value="中止" id="breakOffContract" style="display:none;" class="input_button2" onclick="changeContractState('+'<%=ContractStateEnum.HeTongZhongZhi.getValue()%>'+')"/>'+
			'				<input type="button" value="结束" id="finishContract" style="display:none;" class="input_button2" align="center" onclick="changeContractState('+'<%=ContractStateEnum.HeTongJieShu.getValue()%>'+')"/>'+
			'				<input type="button" value="开始执行" id="startContract" style="display:none;" class="input_button2" onclick="changeContractState('+'<%=ContractStateEnum.ZhiXingZhong.getValue()%>'+')"/>'+
			'			</div>'+
			'				<table>'+
			'					<tr>'+
			'						<th align="left">编号</th>'+
			'						<td><input type="text" name="contractNo" value="" id="contractNo" maxlength="20" readonly="true"/></td>'+
			'						<th align="left">合同状态</th>'+
			'						<td>'+
			'							<select id="contractState" name="contractState">'+
			'				         	</select>'+
			'						</td>'+
			'						<th align="left">合同日期范围</th>'+
			'						<td>'+
			'							<input type="text" name="contractBeginDate" id="contractBeginDate" value="" class="input_text1"  readonly="true"/>'+
			'							至'+
			'							<input type="text" name="contractEndDate" id="contractEndDate" value="" class="input_text1"  readonly="true"/>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">加盟商名称</th>'+
			'						<td><input type="text" name="branchName" value="" id="branchName" maxlength="50"  readonly="true"/></td>'+
			'						<th align="left">站点负责人</th>'+
			'						<td><input type="text" name="siteChief" value="" id="siteChief" maxlength="20"  readonly="true"/></td>'+
			'						<th align="left">负责人身份证</th>'+
			'						<td><input type="text" name="chiefIdentity" value="" id="chiefIdentity" maxlength="20" readonly="true"/></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">区域经理</th>'+
			'						<td><input type="text" name="areaManager" value="" id="areaManager" maxlength="20"/></td>'+
			'						<th align="left">是否有押金</th>'+
			'						<td>'+
			'							<select id ="isDeposit" name ="isDeposit"  onchange="changeUpdateDeposit();">'+
			'					        </select>'+
			'						</td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">质控条款</th>'+
			'						<td colspan="5">'+
			'						<textarea style="width:100%;height:60px;resize: none;" maxlength="500" name="qualityControlClause" id="qualityControlClause"></textarea>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">合同详细描述</th>'+
			'						<td colspan="5">'+
			'						<textarea style="width:100%;height:60px;resize: none;" maxlength="500" name="contractDescription" id="contractDescription"></textarea>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">合同附件</th>'+
			'						<td colspan="5">'+
			'							<span style="display:none;" id="fileSpan"><a href="#" id="file" >附件下载</a></span>'+
			'						</td>'+
			'					</tr>'+
			'					<tr id="firstDepositTr" style="display: none">'+
			'						<th align="left">押金收取日期</th>'+
			'						<td><input type="text" name="depositCollectDate" value="" id="depositCollectDate" maxlength="20"  readonly="true"/></td>'+
			'						<th align="left">押金收取金额</th>'+
			'						<td><input type="text" name="depositCollectAmount" value="" id="depositCollectAmount" maxlength="20"  readonly="true"/></td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>					'+
			'					<tr id="secondDepositTr" style="display: none">'+
			'						<th align="left">押金收取人</th>'+
			'						<td><input type="text" name="depositCollector" value="" id="depositCollector" maxlength="20"  readonly="true"/></td>'+
			'						<th align="left">押金付款人</th>'+
			'						<td><input type="text" name="depositPayor" value="" id="depositPayor" maxlength="20"/></td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>'+
			'					<tr id="thirdDepositTr" style="display: none">'+
			'						<td colspan="6">'+
			'							<table id="depositTable" border="1" style="border-collapse:collapse;">'+
			'								<tbody>'+
			'									<tr>'+
			'										<td align="center"><input type="checkbox" name="checkAllTrs" onclick="javascript:checkAllTrCheckboxes();"/></td>'+
			'										<td align="center"  width="120px">押金退换日期</td>'+
			'										<td align="center"  width="120px">押金退还金额</td>'+
			'										<td align="center"  width="120px">退款人</td>'+
			'										<td align="center"  width="120px">收款人</td>'+
			'										<td align="center"  width="180px">备注</td>'+
			'									</tr>'+
			'								</tbody>'+
			'							</table>'+
			'						</td>'+
			'					</tr>'+
			'					<tr id="forthDepositTr" style="display: none">'+
			'						<td colspan="6">'+
			'							<input type="button" value="添加" class="input_button2" align="center" onclick="addTr()"/>'+
			'							<input type="button" value="移除" class="input_button2" align="center" onclick="deleteTr()"/>'+
			'						</td>'+
			'					</tr>'+
			'					</table>'+
			'					<input type="hidden" id="id" name="id" value="">'+
			'					<input type="hidden" id="branchContractDetailVOStr" name="branchContractDetailVOStr" value="">'+
			'			<div align="center">'+			
			'				<input type="button" value="返回" class="input_button2" onclick="closeBox()"/>'+
			'				<input type="button" value="保存" class="input_button2" align="center" onclick="updateContractData()"/>'+
			'			</div>'+			
			'			</form>	'+
			'		</div>'+
			'		</div>');
	initSelect();
	$("#contractBeginDate").datepicker();
	$("#contractEndDate").datepicker();
	$("#depositCollectDate").datepicker();
	getEditData($("#branchId").val());
	
	var xinJianState = '<%=ContractStateEnum.XinJian.getValue()%>';
	var zhiXingZhongState = '<%=ContractStateEnum.ZhiXingZhong.getValue()%>';
	var heTongZhongZhiState = '<%=ContractStateEnum.HeTongZhongZhi.getValue()%>';
	if($("#contractState").val() == xinJianState){
		 $("#breakOffContract").css('display' ,'');  
		 $("#finishContract").css('display' ,'');  
		 $("#startContract").css('display' ,'');  
	} else if($("#contractState").val() == zhiXingZhongState){
		 $("#breakOffContract").css('display' ,'');  
		 $("#finishContract").css('display' ,'');  
	} else if($("#contractState").val() == heTongZhongZhiState){
		 $("#finishContract").css('display' ,'');  
		 $("#startContract").css('display' ,'');  
	}  
	
	$("#contractState").attr("disabled","disabled");
	$("#isDeposit").attr("disabled","disabled");
	
	$("#alert_box").show();
	centerBox();
}

function queryContract(){
	$("#box_contant").html('');
	$("#box_contant").html('<div id="box_top_bg"></div>'+
			'	<div id="box_in_bg">'+
			'		<h1><div id="close_box" onclick="closeBox()"></div>查询加盟商合同</h1>'+
			'		<div id="box_form">'+
			'			<form action="'+getPath()+'/branchContract/branchContractList" id="querycallerForm">'+
			'		 	<table>'+
			'					<tr>'+
			'						<th align="left">编号</th>'+
			'						<td><input type="text" name="contractNo" value="" id="contractNo" maxlength="20"/></td>'+
			'						<th align="left">合同状态</th>'+
			'						<td>'+
			'							<select id="contractState" name="contractState">'+
			'								<option value="0">--全部--</option>'+
			'				         	</select>'+
			'		           		</td>'+
			'					</tr>'+	
			'					<tr>'+
			'						<th align="left">加盟商名称</th>'+
			'						<td><input type="text" name="branchName" value="" id="branchName" maxlength="50"/></td>'+
			'						<th align="left">区域经理</th>'+
			'						<td><input type="text" name="areaManager" value="" id="areaManager" maxlength="20"/></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">站点负责人</th>'+
			'						<td><input type="text" name="siteChief" value="" id="siteChief" maxlength="20"/></td>'+
			'						<th align="left">负责人身份证</th>'+
			'						<td><input type="text" name="chiefIdentity" value="" id="chiefIdentity" maxlength="20"/></td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">合同详细描述</th>'+
			'						<td><input type="text" name="contractDescription" id="contractDescription" maxlength="20"/></td>'+
			'						<th align="left">是否有押金</th>'+
			'						<td>'+
			'							<select id ="isDeposit" name ="isDeposit">'+
			'								<option value="2">--全部--</option>'+
			'					        </select>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">合同开始日期</th>'+
			'						<td>'+
			'							<input type="text" name="contractBeginDateFrom" id="contractBeginDateFrom" value="" class="input_text1"/>'+
			'							至'+
			'							<input type="text" name="contractBeginDateTo" id="contractBeginDateTo" value="" class="input_text1"/>'+
			'						</td>'+
			'						<th align="left">合同结束日期</th>'+
			'						<td>'+
			'							<input type="text" name="contractEndDateFrom" id="contractEndDateFrom" value="" class="input_text1"/>'+
			'							至'+
			'							<input type="text" name="contractEndDateTo" id="contractEndDateTo" value="" class="input_text1"/>'+
			'						</td>'+
			'					</tr>'+
			'					<tr>'+
			'						<th align="left">排序</th>'+
			'						<td>'+
			'							<select id="contractColumn" name="contractColumn">'+
			'								<option value="contractNo">编号</option>'+
			'								<option value="contractState">合同状态</option>'+
			'								<option value="branchName">加盟商名称</option>'+
			'								<option value="areaManager">区域经理</option>'+
			'								<option value="siteChief">站点负责人</option>'+
			'								<option value="chiefIdentity">负责人身份证</option>'+
			'								<option value="isDeposit">是否有押金</option>'+
			'								<option value="contractBeginDate">合同开始日期</option>'+
			'								<option value="contractEndDate">合同结束日期</option>'+
			'				         	</select>'+
			'							<select id="contractColumnOrder" name="contractColumnOrder">'+
			'								<option value="asc">升序</option>'+
			'								<option value="desc">降序</option>'+
			'				         	</select>'+
			'		           		</td>'+
			'						<th></th>'+
			'						<td></td>'+
			'					</tr>'+
			'			</table>'+
			'			<div align="center">'+
			'				<input type="button" value="查询" class="button" align="center" onclick="querycaller()">'+
			'				<input type="button" value="关闭" class="button" align="center" onclick="closeBox()"/>'+
			'			</div>'+
			'		</form>	'+
			'		</div>'+
			'		</div>');
	initSelect();
	$("#contractBeginDateFrom").datepicker();
	$("#contractBeginDateTo").datepicker();
	$("#contractEndDateFrom").datepicker();
	$("#contractEndDateTo").datepicker();
	$("#alert_box").show();
	centerBox();
}

function upLoad(){
	//上传附件使用
	$('#swfupload-control').swfupload({
		upload_url : $("#addcallerForm").attr("action"),
		file_size_limit : "10240",
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
	}).bind('fileQueueError', function(event, file, errorCode, message) {
	}).bind('fileDialogStart', function(event) {
		$(this).swfupload('cancelQueue');
	}).bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued) {
	}).bind('uploadStart', function(event, file) {
	}).bind('uploadProgress', function(event, file, bytesLoaded, bytesTotal) {

	}).bind('uploadSuccess', function(event, file, serverData) {
		var dataObj = eval("(" + serverData + ")");
		alert(dataObj.error)
		document.location.reload(true);
		/* $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true);
		$("#WORK_AREA", parent.document)[0].contentWindow.editSuccess(dataObj); */
	}).bind('uploadComplete', function(event, file) {
		$(this).swfupload('startUpload');
	}).bind('uploadError', function(event, file, errorCode, message) {
	});
}

/*function addcaller(){
	 
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
	 
}*/

function addContractForm(form){
	if($('#isDeposit option:selected').text()=="是"){  
		if(!$("#depositCollectDate").val()){
			alert("押金收取日期为必填项!");
			return false;
		}
		if(!$("#depositCollectAmount").val()){
			alert("押金收取金额为必填项!");
			return false;
		}
		if(!$("#depositCollector").val()){
			alert("押金收取人为必填项!");
			return false;
		}
		if(!$("#depositPayor").val()){
			alert("押金付款人为必填项!");
			return false;
		}
	}
	
	
	if ($("#txtFileName").val()=="") {
		$(form).attr("enctype", "");
		$(form).attr("action", getPath()+"/branchContract/addBranchContract");
		submitCreateFormAdd(form);
		document.location.reload(true);
		return;
	}
	
	$('#swfupload-control').swfupload('addPostParam', 'contractNo', $("#contractNo").val());
	$('#swfupload-control').swfupload('addPostParam', 'contractState', $("#contractState").val());
	$('#swfupload-control').swfupload('addPostParam', 'contractBeginDate', $("#contractBeginDate").val());
	$('#swfupload-control').swfupload('addPostParam', 'contractEndDate', $("#contractEndDate").val());
	$('#swfupload-control').swfupload('addPostParam', 'branchName', $("#branchName").val());
	$('#swfupload-control').swfupload('addPostParam', 'siteChief', $("#siteChief").val());
	$('#swfupload-control').swfupload('addPostParam', 'chiefIdentity', $("#chiefIdentity").val());
	$('#swfupload-control').swfupload('addPostParam', 'areaManager', $("#areaManager").val());
	$('#swfupload-control').swfupload('addPostParam', 'isDeposit', $("#isDeposit").val());
	$('#swfupload-control').swfupload('addPostParam', 'contractDescription', $("#contractDescription").val());
	$('#swfupload-control').swfupload('addPostParam', 'qualityControlClause', $("#qualityControlClause").val());
	$('#swfupload-control').swfupload('addPostParam', 'depositCollectDate', $("#depositCollectDate").val());
	$('#swfupload-control').swfupload('addPostParam', 'depositCollectAmount', $("#depositCollectAmount").val());
	$('#swfupload-control').swfupload('addPostParam', 'depositCollector', $("#depositCollector").val());
	$('#swfupload-control').swfupload('addPostParam', 'depositPayor', $("#depositPayor").val());
	$('#swfupload-control').swfupload('startUpload');
}

function changeDeposit(){
	if($('#isDeposit option:selected').text()=="是"){  
        $("#firstDepositTr").css('display' ,'');  
        $("#secondDepositTr").css('display' ,'');  
    }else{  
        $("#depositCollectDate").val("");  
        $("#depositCollectAmount").val("");  
        $("#depositCollector").val("");  
        $("#depositPayor").val("");  
        $("#firstDepositTr").css('display', 'none');  
        $("#secondDepositTr").css('display' ,'none');
   }
}

function changeUpdateDeposit(){
	if($('#isDeposit option:selected').text()=="是"){  
        $("#firstDepositTr").css('display' ,'');  
        $("#secondDepositTr").css('display' ,'');  
        $("#thirdDepositTr").css('display' ,'');  
        $("#forthDepositTr").css('display' ,'');  
    }else{  
        $("#depositCollectDate").val("");  
        $("#depositCollectAmount").val("");  
        $("#depositCollector").val("");  
        $("#depositPayor").val("");  
        
        $("#depositTable input[type='checkbox'][name='checkAllTrs']")[0].checked = true;
        checkAllTrCheckboxes();
        deleteTr();
        
        $("#firstDepositTr").css('display', 'none');  
        $("#secondDepositTr").css('display' ,'none');
        $("#thirdDepositTr").css('display' ,'none');  
        $("#forthDepositTr").css('display' ,'none');  
   }
}

function changeContractState(state){
	 $("#contractState").val(state);
} 

function getEditData(val){
	$.ajax({
		type : "POST",
		url : $("#edit").val(),
		data : {"id":val},
		dataType : "json",
		success : function(data) {
			if(data){
				$("#editcallerForm input[name='id']").val(data.id);
				$("#editcallerForm input[name='contractNo']").val(data.contractNo);
				$("#editcallerForm select[name='contractState']").find("option[value='"+data.contractState+"']").attr("selected",true);
				$("#editcallerForm input[name='contractBeginDate']").val(data.contractBeginDate);
				$("#editcallerForm input[name='contractEndDate']").val(data.contractEndDate);
				$("#editcallerForm input[name='branchName']").val(data.branchName);
				$("#editcallerForm input[name='siteChief']").val(data.siteChief);
				$("#editcallerForm input[name='chiefIdentity']").val(data.chiefIdentity);
				$("#editcallerForm input[name='areaManager']").val(data.areaManager);
				$("#editcallerForm select[name='isDeposit']").find("option[value='"+data.isDeposit+"']").attr("selected",true);
				$("#editcallerForm textarea[name='contractDescription']").html(data.contractDescription);
				$("#editcallerForm textarea[name='qualityControlClause']").html(data.qualityControlClause);
				$("#editcallerForm input[name='depositCollectDate']").val(data.depositCollectDate);
				$("#editcallerForm input[name='depositCollectAmount']").val(data.depositCollectAmount);
				$("#editcallerForm input[name='depositCollector']").val(data.depositCollector);
				$("#editcallerForm input[name='depositPayor']").val(data.depositPayor);
				
				if(data.contractAttachment){
					$("#fileSpan").css("display","block");
					$("#file").attr("href","<%=request.getContextPath()%>/branchContract/download?filepathurl=" +data.contractAttachment);
				}
				
				initDepositTable(data.branchContractDetailVOList);
			}
		}
	});
}

function updateContractData(){
	//提交前移除disabled属性
	$("#contractState").removeAttr("disabled"); 
	$("#isDeposit").removeAttr("disabled"); 
	
	if(getBranchContractDetailVOList()){
		var branchContractDetailVOStr = JSON.stringify(getBranchContractDetailVOList());
		$('#branchContractDetailVOStr').val(branchContractDetailVOStr);
	}
	 if(Callerifnull())
	 $.ajax({
			type : "POST",
			data : $('#editcallerForm').serialize(),
			url : $("#editcallerForm").attr('action'),
			dataType : "json",
			success : function(data) {
				if(data.errorCode==0){
					$(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0) { 
					   /*  $('.tabs-panels > .panel:visible > .panel-body > iframe').get(0).contentDocument.location.reload(true); */
						   document.location.reload(true);
					   }  
						closeBox();
					}
				}
			});
}

function querycaller(){
	 $("#querycallerForm").submit();
	closeBox();
}

function getBranchContractDetailVOList(){
	var list = new Array();
	var contractTrs = $("#depositTable .contractTr");
	for(var i=0;i<contractTrs.length;i++){
		trObj = {};
		tds = contractTrs[i].children;
		for(var j=1;j<tds.length;j++){
			tdVal = tds[j].children[0].value;
			name = tds[j].attributes[1].value;
			if(tdVal){
				trObj[name]=tdVal;
			}
		}
		if(!jQuery.isEmptyObject(trObj)){
			trObj['branchId'] = $("#branchId").val();
			list[i]=trObj;
		}
	}
	return list;
}

//初始化押金table
function initDepositTable(list){
	if(list){
		for(var i=0;i<=list.length;i++){
			trObj = list[i];
			rowCount = i;
			addTr();
			tds = $("#depositTable tr[class='contractTr'][name='tr"+i+"']")[0].children;
			for(var j=1;j<tds.length;j++){
				tdName = tds[j].attributes[1].value;
				if(trObj){
					tds[j].children[0].value = trObj[tdName];
				}
			}
		}
	}
}
function checkAllTrCheckboxes(){
	var checked = $("#depositTable input[type='checkbox'][name='checkAllTrs']")[0].checked;
	var trChecks = $("#depositTable input[type='checkbox'][name='count']");
	$(trChecks).each(function() {
		$(this)[0].checked = checked;
	});
}
//添加行
function addTr(){
	var table = $('#depositTable'); 
	var tr = $("<tr class='contractTr' name='tr"+rowCount+"'></tr>"); 
	var checkTd = $("<td></td>");
	checkTd.append($("<input type='checkbox' name='count' value='checkbox"+rowCount+"'>"));
	tr.append(checkTd); 
	var depositReturnTimeTd = $("<td class='contractTd' name='depositReturnTime' width='120px'></td>"); 
	depositReturnTimeTd.append($("<input type='text' id='depositReturnTime"+rowCount+"' name='depositReturnTime"+rowCount+"' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositReturnTimeTd); 
	var depositReturnAmountTd = $("<td class='contractTd' name='depositReturnAmount' width='120px'></td>"); 
	depositReturnAmountTd.append($("<input type='text' id='depositReturnAmount"+rowCount+"' name='depositReturnAmount"+rowCount+"' style='border:0;outline:none;width:120px' onblur='isFloatVal(this)'/>"));
	tr.append(depositReturnAmountTd); 
	var depositReturnPersonTd = $("<td class='contractTd' name='depositReturnPerson' width='120px'></td>"); 
	depositReturnPersonTd.append($("<input type='text' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositReturnPersonTd); 
	var depositCollectorTd = $("<td class='contractTd' name='depositCollector' width='120px'></td>"); 
	depositCollectorTd.append($("<input type='text' style='border:0;outline:none;width:120px'/>"));
	tr.append(depositCollectorTd); 
	var remarkTd = $("<td class='contractTd' name='remark' width='180px'></td>"); 
	remarkTd.append($("<input type='text' style='border:0;outline:none;width:180px'/>"));
	tr.append(remarkTd); 
	table.append(tr);
	
	$("#depositReturnTime"+rowCount).datepicker();
	rowCount++;
	//initBranchContract();
}
//移除行
function deleteTr(){
	var checked = $("#depositTable input[type='checkbox'][name='count']");
	$(checked).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			$(this).parent().parent().remove();
		}
	}); 
	//判断全选框置为"不勾选"
	$("#depositTable input[type='checkbox'][name='checkAllTrs']")[0].checked = false;
}

function initBranchContract() {
	//使td节点具有click点击能力
	var tdNods = $("#depositTable .contractTd");
	tdNods.click(tdClick);
}
//td的点击事件
function tdClick() {
	// 将td的文本内容保存
	var td = $(this);
	var tdText = td.text();
	// 将td的内容清空
	td.empty();
	// 新建一个输入框
	var input = $("<input>");
	// 将保存的文本内容赋值给输入框
	input.attr("value", tdText);
	input.attr("class", "tdInputClass");
	var tdVal = td[0].attributes[1].value;
	var trVal = td[0].parentElement.attributes[1].value;
	if(tdVal=="depositReturnTime"){
		input.attr("id", "depositReturnTime"+trVal.substr(2));
		input.attr("name", "depositReturnTime"+trVal.substr(2));
	}
	// 将输入框添加到td中
	td.append(input);
	// 给输入框注册事件，当失去焦点时就可以将文本保存起来
	if(tdVal!="depositReturnTime"){
		input.blur(function() {
			// 将输入框的文本保存
			var input = $(this);
			var inputText = input.val();
			// 将td的内容，即输入框去掉,然后给td赋值
			var td = input.parent("td");
			td.html(inputText);
			// 让td重新拥有点击事件
			td.click(tdClick);
		});
	} 
	// 将输入框中的文本高亮选中
	// 将jquery对象转化为DOM对象
	var inputDom = input.get(0);
	inputDom.select();
	// 将td的点击事件移除
	td.unbind("click");
	
	$(".tdInputClass").css("width","120px");
	//时间控件
	if(tdVal=="depositReturnTime"){
		var tdId = "depositReturnTime"+trVal.substr(2);
		$("#"+tdId).datepicker();
	}
}

/**************身份证校验Begin**************/
var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X   
function IdCardValidate(idCard) { 
	var rtnBoolean;
    idCard = trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
    if (idCard.length == 15) {   
    	rtnBoolean = isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证    
    } else if (idCard.length == 18) {   
        var a_idCard = idCard.split("");                // 得到身份证数组   
        if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
        	rtnBoolean = true;   
        }else {   
        	rtnBoolean = false;   
        }   
    } else {   
    	rtnBoolean = false;   
    }
    
    if(rtnBoolean == false){
    	alert('请输入正确的身份证号');
    	$('#chiefIdentity').val("");
    }
    return rtnBoolean;
}   
/**  
 * 判断身份证号码为18位时最后的验证位是否正确  
 * @param a_idCard 身份证号码数组  
 * @return  
 */  
function isTrueValidateCodeBy18IdCard(a_idCard) {   
    var sum = 0;                             // 声明加权求和变量   
    if (a_idCard[17].toLowerCase() == 'x') {   
        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
    }   
    for ( var i = 0; i < 17; i++) {   
        sum += Wi[i] * a_idCard[i];            // 加权求和   
    }   
    valCodePosition = sum % 11;                // 得到验证码所位置   
    if (a_idCard[17] == ValideCode[valCodePosition]) {   
        return true;   
    } else {   
        return false;   
    }   
}   
/**  
  * 验证18位数身份证号码中的生日是否是有效生日  
  * @param idCard 18位书身份证字符串  
  * @return  
  */  
function isValidityBrithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
    // 这里用getFullYear()获取年份，避免千年虫问题   
    if(temp_date.getFullYear()!=parseFloat(year)   
          ||temp_date.getMonth()!=parseFloat(month)-1   
          ||temp_date.getDate()!=parseFloat(day)){   
            return false;   
    }else{   
        return true;   
    }   
}   
  /**  
   * 验证15位数身份证号码中的生日是否是有效生日  
   * @param idCard15 15位书身份证字符串  
   * @return  
   */  
  function isValidityBrithBy15IdCard(idCard15){   
      var year =  idCard15.substring(6,8);   
      var month = idCard15.substring(8,10);   
      var day = idCard15.substring(10,12);   
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
      if(temp_date.getYear()!=parseFloat(year)   
              ||temp_date.getMonth()!=parseFloat(month)-1   
              ||temp_date.getDate()!=parseFloat(day)){   
                return false;   
        }else{   
            return true;   
        }   
  }   
//去掉字符串头尾空格   
function trim(str) {   
    return str.replace(/(^\s*)|(\s*$)/g, "");   
}
/**************身份证校验End**************/
</script>
</head>
<body>
	<!-- 弹出框开始 -->
	<div id="alert_box" style="display:none;" align="center">
		<div id="box_bg"></div>
		<div id="box_contant"></div>
	</div>
	<!-- 弹出框结束 -->
	
	<div class="right_box">
		<div class="inputselect_box">	
			<table>
				<tr>
					<td><button id="add_btn" onclick="addContract()" class="input_button2">新增</button></td>
					<td><button id="edit_btn" onclick="updateContract()" class="input_button2">查看/修改</button></td>
					<td><button id="delete_button" class="input_button2">删除</button></td>
					<td><button id="query_button" onclick="queryContract()" class="input_button2">查询</button></td>
				</tr>
			</table>
		</div>
			<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
				<tr class="font_1">
					<!-- <th bgcolor="#eef6ff"><input type="checkbox" id="checkAll" value=""/></th> -->
					<th bgcolor="#eef6ff">编号</th>
					<th bgcolor="#eef6ff">合同状态</th>
					<th bgcolor="#eef6ff">加盟商名称</th>
					<th bgcolor="#eef6ff">区域经理</th>
					<th bgcolor="#eef6ff">合同日期范围</th>
					<th bgcolor="#eef6ff">站点负责人</th>
					<th bgcolor="#eef6ff">负责人身份证</th>
				</tr>
				<%if(branchContractList!=null){ %>
				<%for(ExpressSetBranchContract bc:branchContractList){ %>
				<tr onclick="setBranchId('<%=bc.getId()%>','<%=bc.getContractState()%>')">
					<td><%=bc.getContractNo()==null?"": bc.getContractNo()%></td>
					<td>
					<%if(bc.getContractState() == ContractStateEnum.XinJian.getValue()){%>
					<%=ContractStateEnum.XinJian.getText()%>
					<%} else if(bc.getContractState() == ContractStateEnum.ZhiXingZhong.getValue()){%>
					<%=ContractStateEnum.ZhiXingZhong.getText()%>
					<%} else if(bc.getContractState() == ContractStateEnum.HeTongZhongZhi.getValue()){%>
					<%=ContractStateEnum.HeTongZhongZhi.getText()%>
					<%} else if(bc.getContractState() == ContractStateEnum.HeTongJieShu.getValue()){%>
					<%=ContractStateEnum.HeTongJieShu.getText()%>
					<%}%>
					</td>
					<td><%=bc.getBranchName()==null?"":bc.getBranchName() %></td>				
					<td><%=bc.getAreaManager()==null?"":bc.getAreaManager() %></td>
					<td><%=bc.getContractBeginDate()==null?"": bc.getContractBeginDate()%>至<%=bc.getContractEndDate()==null?"": bc.getContractEndDate()%></td>
					<td><%=bc.getSiteChief()==null?"":bc.getSiteChief() %></td>
					<td><%=bc.getChiefIdentity()==null?"":bc.getChiefIdentity() %></td>
				</tr>
				<%}%>
				<%}%>
				
			<%-- <input type="hidden" id="add" value="<%=request.getContextPath()%>/branchContract/addBranchContractPage"/> --%>
			<input type="hidden" id="edit" value="<%=request.getContextPath()%>/branchContract/updateBranchContractPage/"/>
			<input type="hidden" id="branchId" value=""/>
			<input type="hidden" id="branchContractState" value=""/>
			
			</table>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
		</div>
	</div>
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<%-- <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();" >第一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#PageFromW').submit();">上一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#PageFromW').submit();" >下一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#PageFromW').submit();" >最后一页</a>
				　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
						<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
						<option value="<%=i %>"><%=i %></option>
						<% } %>
					</select>页
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page") %>);
	</script> --%>
	
	
</body>
</html>