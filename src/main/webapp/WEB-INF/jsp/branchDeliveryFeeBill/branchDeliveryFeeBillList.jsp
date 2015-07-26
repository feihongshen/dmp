<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<jsp:useBean id="now" class="java.util.Date" scope="page" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>加盟商派费账单</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel=File-List href="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/filelist.xml">
<link rel=Edit-Time-Data href="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/editdata.mso">
<link rel=dataStoreItem href="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/item0001.xml"
	target="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/props0002.xml">
<link rel=themeData href="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/themedata.thmx">
<link rel=colorSchemeMapping
	href="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/colorschememapping.xml">

<script
	src="<%=request.getContextPath()%>/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/commonUtil.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/workorder/csPushSmsList.js"
	type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$('#addPage').dialog('close');
	//$('#updatePage').dialog('close');
	$('#queryPage').dialog('close');
	$('#branchListPage').dialog('close');
	$('#exportByCustomerPage').dialog('close');
	
	$("#updateForm select[name='billState']").attr("disabled",true); 
	$("table#branchListTable tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	
	$("#addForm input[name='branchName']").focus(function(){
		$('#branchListPage').dialog('open');
	});
	
	var addPageFlag = '${addPage}';
	if(addPageFlag){
		$('#addPage').dialog('open');
	}
	var branchListPageFlag = '${branchListPage}';
	if(branchListPageFlag){
		$('#branchListPage').dialog('open');
	}
});
function addInit(){
	//无处理
}

function checkAll(id){ 
	var chkAll = $("#"+ id +" input[type='checkbox'][name='checkAll']")[0].checked;
	var chkBoxes = $("#"+ id +" input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		$(this)[0].checked = chkAll;
	});
}

function queryBillList(){
	$("#queryForm").submit();
	$('#queryPage').dialog('close');
}

function updatePage(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断
		{
			strs = $(this).val().split(",");
			getEditData(strs[0]);
			return false;
		}
	}); 
}

function getEditData(val){
	$("#updatePageForm input[name='id']").val(val);
	$("#updatePageForm").submit();
}

function deleteCwbOrder(){
	var deliveryFee = $("#updateForm input[name='deliveryFee']").val();
	var cwbCount = $("#updateForm input[name='cwbCount']").val();
	var chkBoxes = $("#cwbOrderTable input[type='checkbox'][name='checkBox']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true){
			tds = $(this).parent().parent().children();
			$(tds).each(function(){
				if($(this)[0].attributes[1].value == 'sumFee'){
					sumFee = $(this)[0].textContent;
					deliveryFee = (parseFloat(deliveryFee)-parseFloat(sumFee)).toFixed(2);
				}
			});
			cwbCount = parseInt(cwbCount)-1;
			$(this).parent().parent().remove();
		}
	}); 
	$("#updateForm input[name='deliveryFee']").val(deliveryFee);
	$("#updateForm input[name='cwbCount']").val(cwbCount);
}

function queryBranchList(){
	var branchName = $("#addForm input[name='branchName']").val();
	var branchId = $("#addForm input[name='branchId']").val();
	var cwbType = $("#addForm select[name='cwbType']").val();
	var dateType = $("#addForm select[name='dateType']").val();
	var beginDate = $("#addForm input[name='beginDate']").val();
	var endDate = $("#addForm input[name='endDate']").val();
	var remark = $("#addForm input[name='remark']").val();
	
	$("#queryBranchListForm input[type='hidden'][name='branchName']").val(branchName);
	$("#queryBranchListForm input[type='hidden'][name='branchId']").val(branchId);
	$("#queryBranchListForm input[type='hidden'][name='cwbType']").val(cwbType);
	$("#queryBranchListForm input[type='hidden'][name='dateType']").val(dateType);
	$("#queryBranchListForm input[type='hidden'][name='beginDate']").val(beginDate);
	$("#queryBranchListForm input[type='hidden'][name='endDate']").val(endDate);
	$("#queryBranchListForm input[type='hidden'][name='remark']").val(remark);
	$("#queryBranchListForm").submit();
}

function selectBranch(){
	var branchname = $("#branchListPage input[type='hidden'][name='branchname']").val();
	var branchid = $("#branchListPage input[type='hidden'][name='branchid']").val();
	$("#addForm input[name='branchName']").val(branchname);
	$("#addForm input[name='branchId']").val(branchid);
	$('#branchListPage').dialog('close');
}

function updateBranchDeliveryFeeBill(){
	var chkBoxes = $("#cwbOrderTable input[type='checkbox'][name='checkBox']");
	var cwbs = "";
	$(chkBoxes).each(function() {
		cwbs = $(this).val()+","+cwbs;
	}); 
	cwbs = cwbs.substring(0,cwbs.length-1);
	$("#updateForm input[type='hidden'][name='cwbs']").val(cwbs);
	$("#updateForm select[name='billState']").attr("disabled",false); 
	
	//$("#updateForm").submit();
	 $.ajax({
			type : "POST",
			data : $('#updateForm').serialize(),
			url : $("#updateForm").attr('action'),
			dataType : "json",
			success : function(data) {
					if(data.errorCode==0){
						alert(data.error);
					  	$('#updatePage').dialog('close');
			   			window.location.href='<%=request.getContextPath()%>/branchDeliveryFeeBill/branchDeliveryFeeBillList/1';
					}
				}
			});
}

function deleteBranchDeliveryFeeBill(){
	var chkBoxes = $("#listTable input[type='checkbox'][name='checkBox']");
	var strs= new Array();
	var billIds = "";
	var sign = 0;
	$(chkBoxes).each(function() {
		if ($(this)[0].checked == true)
		{
			strs = $(this).val().split(",");
			if(strs[1] == '${weiShenHeState}'){
				billIds = billIds + strs[0] + ",";
			} else {
				sign = 1;
			}
		}
	});
	if(sign == 1){
		alert("只有未审核状态才能进行删除!");
	}
	if(!billIds){
		return;
	}
	billIds = billIds.substring(0,billIds.length-1);
	if(window.confirm("是否确定删除?")){
		$.ajax({
			type:"post",
			url:"<%=request.getContextPath()%>/branchDeliveryFeeBill/deleteBranchDeliveryFeeBill",
			data:{"ids":billIds},
			dataType:"json",
			success:function(data){
					if(data && data.errorCode==0){
						alert(data.error);
						window.location.href='<%=request.getContextPath()%>/branchDeliveryFeeBill/branchDeliveryFeeBillList/1';
							}
						}
					});
		}
	}

	function addDeliveryFeeBillPage() {
		$('#addPage').dialog('open');

		$("#addForm textarea[name='remark']")
				.focus(
						function() {
							if ($("#addForm textarea[name='remark']").val()
									&& $("#addForm textarea[name='remark']")
											.val() == '不超过100字') {
								$("#addForm textarea[name='remark']").val('');
							}
						}).blur(function() {
					if (!$("#addForm textarea[name='remark']").val()) {
						$("#addForm textarea[name='remark']").val('不超过100字');
					}
				});
	}
	function addBranchDeliveryFeeBill() {
		if(!$("#addForm input[name='branchName']").val()){
			alert("加盟商站点为必填项!");
			return false;
		}
		var beginDate = $("#addForm input[name='beginDate']").val();
		var endDate = $("#addForm input[name='endDate']").val();
		if (beginDate && endDate) {
			if (!compareDate(beginDate, endDate)) {
				alert("开始日期必须小于等于结束日期!");
				return false;
			}
			var afterDate = addDate(beginDate, 2);
			if (!compareDate(endDate, afterDate)) {
				alert("时间跨度不能超过两个月!");
				return false;
			}
		} else {
			alert("日期范围为必填项且时间跨度不能超过两个月!");
			return false;
		}

		var remark = $("#addForm textarea[name='remark']").val();
		if (remark) {
			if (remark == "不超过100字") {
				$("#addForm textarea[name='remark']").val('');
			} else {
				if (remark.length > 100) {
					alert("备注不超过100字!");
					return false;
				}
			}
		}
		$("#addForm").submit();
	}

	function addDate(date, months) {
		var d = new Date(date);
		d.setMonth(d.getMonth() + months);
		var month = d.getMonth() + 1;
		var day = d.getDate();
		if (month < 10) {
			month = "0" + month;
		}
		if (day < 10) {
			day = "0" + day;
		}
		var val = d.getFullYear() + "-" + month + "-" + day;
		return val;
	}

	function compareDate(a, b) {
		var aArr = a.split("-");
		if(aArr.length == 1){
			aArr = a.split("/");
		}
		var aDate = new Date(aArr[0], aArr[1], aArr[2]);
		var aTimes = aDate.getTime();
		var bArr = b.split("-");
		if(bArr.length == 1){
			bArr = b.split("/");
		}
		var bDate = new Date(bArr[0], bArr[1], bArr[2]);
		var bTimes = bDate.getTime();
		if (aTimes <= bTimes) {
		    return true;
		} else{
		    return false;
		}
	}
	function queryPage() {
		$("#queryForm input[type='createDateFrom']").val(getFirstDate());
		$("#queryForm input[type='createDateTo']").val(getNowDate());
		$('#queryPage').dialog('open');
	}
	function getNowDate() {
		var now = new Date();
		var nowDate = now.getYear() + "-"
				+ ((now.getMonth() + 1) < 10 ? "0" : "") + (now.getMonth() + 1)
				+ "-" + (now.getDate() < 10 ? "0" : "") + now.getDate();
		return nowDate;
	}

	function getFirstDate() {
		var now = new Date();
		var firstDate = now.getYear() + "-"
				+ ((now.getMonth() + 1) < 10 ? "0" : "") + (now.getMonth() + 1)
				+ "-01";
		return firstDate;
	}

	function changeBillState(state) {
		if (state) {
			if (state == 'ShenHe') {
				if (confirm("是否确认审核?")) {
					$("#updateForm select[name='billState']").val(
							'${yiShenHeState}');
					/* $("#updateForm input[name='shenHeDate']").val('${nowDate}');
					$("#updateForm input[name='shenHePerson']")
							.val('${userid}');
					$("#updateForm input[name='shenHePersonName']").val(
							'${realname}'); */
				}
			} else if (state == 'QuXiaoShenHe') {
				if (confirm("是否确认取消审核?")) {
					$("#updateForm select[name='billState']").val(
							'${weiShenHeState}');
					/* $("#updateForm input[name='shenHeDate']").val('');
					$("#updateForm input[name='shenHePerson']").val('');
					$("#updateForm input[name='shenHePersonName']").val(''); */
				}
			} else if (state == 'HeXiaoWanCheng') {
				if (confirm("是否确认核销完成?")) {
					$("#updateForm select[name='billState']").val(
							'${yiHeXiaoState}');
					/* $("#updateForm input[name='heXiaoDate']").val('${nowDate}');
					$("#updateForm input[name='heXiaoPerson']")
							.val('${userid}');
					$("#updateForm input[name='heXiaoPersonName']").val(
							'${realname}'); */
				}
			} else if (state == 'QuXiaoHeXiao') {
				if (confirm("是否确认取消核销?")) {
					$("#updateForm select[name='billState']").val(
							'${yiShenHeState}');
					/* $("#updateForm input[name='heXiaoDate']").val('');
					$("#updateForm input[name='heXiaoPerson']").val('');
					$("#updateForm input[name='heXiaoPersonName']").val(''); */
				}
			}
		}
	}

	function exportByDetail() {
		var chkBoxes = $("#cwbOrderTable input[type='checkbox'][name='checkBox']");
		var cwbs = "";
		var sign = 1;
		$(chkBoxes).each(function() {
			if ($(this)[0].checked == true){
				cwbs = $(this).val() + "," + cwbs;
				sign = 0;
			}
		}); 
		if(sign == 1){
			alert("没有数据无法导出!");
			return;
		}
		cwbs = cwbs.substring(0,cwbs.length-1);
		$("#exportByDetailForm input[name='cwbs']").val(cwbs);
		$("#exportByDetailForm").submit();
	}
	function setBranchId(id, name) {
		$("#branchListPage input[type='hidden'][name='branchid']").val(id);
		$("#branchListPage input[type='hidden'][name='branchname']").val(name);
	}

	function exportByCustomerPage() {
		var chkBoxes = $("#cwbOrderTable input[type='checkbox'][name='checkBox']");
		var cwbs = "";
		var sign = 1;
		$(chkBoxes).each(function() {
			if ($(this)[0].checked == true){
				cwbs = $(this).val() + "," + cwbs;
				sign = 0;
			}
		}); 
		if(sign == 1){
			alert("没有数据无法导出!");
			return;
		}
		cwbs = cwbs.substring(0,cwbs.length-1);
		$.ajax({
			type:'POST',
			data:{"cwbs":cwbs},
			url:'<%=request.getContextPath()%>/branchDeliveryFeeBill/getExportData',
			dataType:'json',
			success:function(data){
				if(data){
					//${"#branchFeeTable"}
					if(data.customerDeliveryFee){
						var customerDeliveryFeeObj = data.customerDeliveryFee;
						var count = 0;
						var tdCount = 0;
						rowSpanSize = parseInt(customerDeliveryFeeObj.length)*3+1;
						for(var i in customerDeliveryFeeObj){
								++count;
								customerName = i;
								
								trObj = "<tr style='mso-yfti-irow: 1; height: 19.95pt'></tr>";
								if(count == 1){
									receiveTdObj = "<td width=23 rowspan='"+rowSpanSize+"' "+
									"					style=\'width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal align=center style=\'text-align: center\'>"+
									"						<span lang=EN-US"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>1<o:p></o:p></span>"+
									"					</p>"+
									"				</td>"+
									"				<td width=50 rowspan='"+rowSpanSize+"' "+
									"					style=\'width: 37.85pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal>"+
									"						<span"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>派送费<span"+
									"							lang=EN-US><o:p></o:p></span></span>"+
									"					</p>"+
									"				</td>"+
									"				<td width=34 rowspan=3 "+
									"					style=\'width: 25.7pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal>"+
									"						<span"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>"+customerName+"<span"+
									"							lang=EN-US><o:p></o:p></span></span>"+
									"					</p>"+
									"				</td>"+
									"				<td width=60"+
									"					style=\'width: 44.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal>"+
									"						<span"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>代收订单<span"+
									"							lang=EN-US><o:p></o:p></span></span>"+
									"					</p>"+
									"				</td>";
								} else {
									receiveTdObj = "<td width=34 rowspan=3 "+
									"					style=\'width: 25.7pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal>"+
									"						<span"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>"+customerName+"<span"+
									"							lang=EN-US>B<o:p></o:p></span></span>"+
									"					</p>"+
									"				</td>"+
									"				<td width=60"+
									"					style=\'width: 44.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt\'>"+
									"					<p class=MsoNormal>"+
									"						<span"+
									"							style=\'font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt\'>代收订单<span"+
									"							lang=EN-US><o:p></o:p></span></span>"+
									"					</p>"+
									"				</td>";
								}
							   feeObj = customerDeliveryFeeObj[i];
							   for(var j in feeObj){
								   ++tdCount;
								   if(tdCount == 1){
									   
								   } else if(tdCount == 2){
									   
								   } else if(tdCount == 3){
									   
								   }
								   feeObj[j].cwbOrderCount;
								   feeObj[j].deliveryBasicFee;
								   feeObj[j].deliveryCollectionSubsidyFee;
								   feeObj[j].deliveryAreaSubsidyFee;
								   feeObj[j].deliveryExceedSubsidyFee;
								   feeObj[j].deliveryBusinessSubsidyFee;
								   feeObj[j].deliveryAttachSubsidyFee;
								   
							   }
						}
						
						
					}
				}
			}
		});
		initExportBillForm();
		$('#exportByCustomerPage').dialog('open');
	}
	function exportByCustomer() {
		var divObj = $("#exportBranchDeliveryFeeBillListForm").next()[0].outerHTML;
		var divJson = {content:divObj};
		$.ajax({
			type:'POST',
			data:divJson,
			url:'<%=request.getContextPath()%>/branchDeliveryFeeBill/exportByCustomer',
			dataType:'json',
			success:function(data){
				if(data && data.errorCode==0){
					alert(data.error);
				}
			}
		});
	}
	
	function initExportBillForm() {
		//使td节点具有click点击能力
		var tdNods = $(".MsoTableGrid td[class='editTd']");
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
		/* input.attr("class", "tdInputClass"); */
		/* input.attr("width", "100%"); */
		input.attr("style", "border:0;outline:none;width:100%");
		/* input.attr("style", "outline:none;width:100%;height:100%;"); */
		// 将输入框添加到td中
		td.append(input);
		// 给输入框注册事件，当失去焦点时就可以将文本保存起来
		input.blur(function() {
			// 将输入框的文本保存
			var input = $(this);
			var inputText = input.val();
			// 将td的内容，即输入框去掉,然后给td赋值
			var td = input.parent("td");
			
			//td.empty();
			input.remove();
			var oEle = $("<o:p>&nbsp;</o:p>");
			var spanEle = $("<span lang=EN-US style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'></span>");
			var pEle = $("<p class=MsoNormal align=center style='text-align: center'></p>"); 
			spanEle.append(oEle); 
			pEle.append(spanEle);
			td.append(pEle);
			oEle.html(inputText);
			
			/* td.html(inputText); */
			// 让td重新拥有点击事件
			td.click(tdClick);
		});
		// 将输入框中的文本高亮选中
		// 将jquery对象转化为DOM对象
		/* var inputDom = input.get(0);
		inputDom.select(); */
		// 将td的点击事件移除
		td.unbind("click");
		/* $(".tdInputClass").css("width","120px"); */
	}

</script>
</head>

<body style="background: #eef9ff">

	<div class="right_box">
		<div class="inputselect_box">
			<table style="width: 60%">
				<tr>
					<td>
						<input class="input_button2" type="button" onclick="addDeliveryFeeBillPage()" value="新增" />
						<input class="input_button2" type="button" onclick="updatePage()" value="查看/修改" />
						<input class="input_button2" type="button" onclick="deleteBranchDeliveryFeeBill()" value="删除" />
						<input class="input_button2" type="button" onclick="queryPage()" value="查询" />
					</td>
				</tr>
			</table>
		</div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="jg_10"></div>
		<div class="right_title">
			<div style="overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					class="table_2" id="listTable">
					<tr>
						<td height="30px" valign="middle">
							<input type="checkbox" name="checkAll" onclick="checkAll('listTable')" />
						</td>
						<td align="center" valign="middle" style="font-weight: bold;">账单批次</td>
						<td align="center" valign="middle" style="font-weight: bold;">账单状态</td>
						<td align="center" valign="middle" style="font-weight: bold;">加盟商名称</td>
						<td align="center" valign="middle" style="font-weight: bold;">日期范围</td>
						<td align="center" valign="middle" style="font-weight: bold;">对应订单数</td>
						<td align="center" valign="middle" style="font-weight: bold;">派费金额</td>
						<td align="center" valign="middle" style="font-weight: bold;">创建日期</td>
						<td align="center" valign="middle" style="font-weight: bold;">核销日期</td>
					</tr>
					<c:forEach items="${branchDeliveryFeeBillList}" var="bill">
						<tr>
							<td height="30px" align="center" valign="middle">
								<input type="checkbox" name="checkBox" value="${bill.id}${','}${bill.billState}" />
							</td>
							<td align="center" valign="middle">${bill.billBatch}</td>
							<td align="center" valign="middle">
								<c:forEach var="item" items="${billStateMap}">
									<c:if test="${bill.billState==item.key}">${item.value}</c:if>
								</c:forEach>
							</td>
							<td align="center" valign="middle">
								<c:forEach items="${branchList}" var="branch">
									<c:if test="${bill.branchId==branch.branchid}">${branch.branchname}</c:if>
								</c:forEach>
							</td>
							<td align="center" valign="middle">${bill.beginDate}至${bill.endDate}</td>
							<td align="center" valign="middle">${bill.cwbCount}</td>
							<td align="center" valign="middle">${bill.deliveryFee}</td>
							<td align="center" valign="middle">${bill.createDate}</td>
							<td align="center" valign="middle">${bill.heXiaoDate}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

	<!-- 新增层显示 -->
	<div id="addPage" class="easyui-dialog" title="新增"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 250px;">
		<form
			action="<%=request.getContextPath()%>/branchDeliveryFeeBill/addBranchDeliveryFeeBill"
			method="post" id="addForm">
			<table width="100%" border="0" cellspacing="5" cellpadding="0"
				style="margin-top: 10px; font-size: 10px; border-collapse: separate">
				<tr>
					<td colspan="4" align="left">
						<input type="button" class="input_button2" value="返回" onclick="$('#addPage').dialog('close');" />
						<input type="button" class="input_button2" value="创建" onclick="addBranchDeliveryFeeBill()" />
					</td>
				</tr>
				<tr>
					<td align="left" width="15%"><font color="red">*</font>加盟商站点</td>
					<td align="left" width="35%">
						<input type="text" name="branchName" readonly="readonly">
						<input type="hidden" name="branchId">
					</td>
					<td align="left" width="10%"><font color="red">*</font>订单类型</td>
					<td align="left" width="40%">
						<select name="cwbType">
							<c:forEach items="${cwbTypeMap}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td align="left"><font color="red">*</font>
						<select name="dateType">
							<c:forEach items="${dateTypeMap}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="text" name="beginDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" />
						至
						<input type="text" name="endDate" class="easyui-my97" datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" />
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="left">备注</td>
					<td colspan="3" >
						<textarea name="remark" style="width: 100%; height: 60px; resize: none;" onfocus="if(this.value=='不超过100字') {this.value='';}" onblur="if(this.value=='') {this.value='不超过100字';}">不超过100字</textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 查看/修改层显示 -->
	<c:if test="${updatePage==1}">
		<div id="updatePage" class="easyui-dialog" title="编辑"
			data-options="iconCls:'icon-save'"
			style="width: 800px; height: 500px;">
			<form
				action="<%=request.getContextPath()%>/branchDeliveryFeeBill/updateBranchDeliveryFeeBill"
				method="post" id="updateForm">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					style="margin-top: 10px; font-size: 10px;">
					<tr>
						<td colspan="2" align="left">
							<input type="button" class="input_button2" value="返回" onclick="$('#updatePage').dialog('close');" />
							<c:if test="${weiShenHeState==branchDeliveryFeeBillVO.billState}">
								<input type="button" class="input_button2" value="保存" onclick="updateBranchDeliveryFeeBill()" />
							</c:if>
						</td>
						<c:choose>
							<c:when test="${jiesuanAuthority==1}">
								<c:choose>
									<c:when test="${weiShenHeState==branchDeliveryFeeBillVO.billState}">
										<td align="right" colspan="2">
											<input type="button" class="input_button2" onclick="changeBillState('ShenHe')" value="审核" />
										</td>
									</c:when>
									<c:when test="${yiShenHeState==branchDeliveryFeeBillVO.billState}">
										<td align="right" colspan="2">
											<input type="button" class="input_button2" onclick="changeBillState('QuXiaoShenHe')" value="取消审核" />
											<input type="button" class="input_button2" onclick="changeBillState('HeXiaoWanCheng')" value="核销完成" />
										</td>
									</c:when>
								</c:choose>
							</c:when>
							<c:when test="${jiesuanAdvanceAuthority==1}">
								<c:if test="${yiHeXiaoState==branchDeliveryFeeBillVO.billState}">
									<td align="right" colspan="2">
										<input type="button" class="input_button2" onclick="changeBillState('QuXiaoHeXiao')" value="取消核销" />
									</td>
								</c:if>
							</c:when>
							<c:otherwise>
								<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;</td>
							</c:otherwise>
						</c:choose>
						<td colspan="2" align="right">
							<input type="button" class="input_button2" value="按明细导出" onclick="exportByDetail();" />
							<input type="button" class="input_button2" value="按客户导出" onclick="exportByCustomerPage();" />
						</td>
					</tr>
					<tr>
						<td align="left">账单批次</td>
						<td><input type="text" name="billBatch" readonly="readonly"
							value="${branchDeliveryFeeBillVO.billBatch}"
							style="background-color: #DCDCDC" /></td>
						<td align="left">账单状态</td>
						<td><select name="billState"
							style="background-color: #DCDCDC" disabled="disabled">
								<c:forEach var="item" items="${billStateMap}">
									<c:if test="${branchDeliveryFeeBillVO.billState==item.key}">
										<option value="${item.key}" selected="selected">${item.value}</option>
									</c:if>
									<c:if test="${branchDeliveryFeeBillVO.billState!=item.key}">
										<option value="${item.key}">${item.value}</option>
									</c:if>
								</c:forEach>
						</select></td>
						<td align="left">日期范围</td>
						<td><input type="text" name="beginDate" readonly="readonly"
							value="${branchDeliveryFeeBillVO.beginDate}"
							style="background-color: #DCDCDC" /> 至 <input type="text"
							name="endDate" readonly="readonly"
							value="${branchDeliveryFeeBillVO.endDate}"
							style="background-color: #DCDCDC" /></td>
					</tr>
					<tr>
						<td align="left">加盟站点名称</td>
						<td><input type="text" name="branchName" readonly="readonly"
							value="${branchDeliveryFeeBillVO.branchName}"
							style="background-color: #DCDCDC" /> <input type="hidden"
							name="branchId" value="${branchDeliveryFeeBillVO.branchId}" /></td>
						<td align="left">派费合计(元)</td>
						<td><input type="text" name="deliveryFee" readonly="readonly"
							value="${branchDeliveryFeeBillVO.deliveryFee}"
							style="background-color: #DCDCDC" /></td>
						<td align="left">对应订单数</td>
						<td><input type="text" name="cwbCount" readonly="readonly"
							value="${branchDeliveryFeeBillVO.cwbCount}"
							style="background-color: #DCDCDC" /></td>
					</tr>
					<tr>
						<td align="left">备注</td>
						<td colspan="5"><textarea rows="3" name="remark"
								style="width: 100%; resize: none;">${branchDeliveryFeeBillVO.remark}</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="8"><input type="hidden" name="id"
							value="${branchDeliveryFeeBillVO.id}"></td>
					</tr>
					<tr>
						<td colspan="8"><input type="hidden" name="cwbs"
							value="${branchDeliveryFeeBillVO.cwbs}"></td>
					</tr>
					<tr>
						<td colspan="8">
							<table width="100%" border="0" cellspacing="1" cellpadding="0"
								class="table_2" id="cwbOrderTable">
								<tr>
									<td height="30px" valign="middle"><input type="checkbox"
										name="checkAll" onclick="checkAll('cwbOrderTable')" /></td>
									<td align="center" valign="middle" style="font-weight: bold;">
										订单号</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										订单状态</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										订单类型</td>
									<!-- <td align="center" valign="middle" style="font-weight: bold;">
										到货时间</td> -->
									<td align="center" valign="middle" style="font-weight: bold;">
										发货时间</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										付款方式</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										签收日期</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										派费金额合计</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										基本派费</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										代收补助费</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										区域属性补助费</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										超区补助</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										业务补助</td>
									<td align="center" valign="middle" style="font-weight: bold;">
										拖单补助</td>
								</tr>
								<c:forEach var="detail"
									items="${branchDeliveryFeeBillVO.billDetailList}">
									<tr>
										<td height="30px" valign="middle">
											<input type="checkbox" name="checkBox" value="${detail.cwb}" />
										</td>
										<td align="center" valign="middle">${detail.cwb}</td>
										<td align="center" valign="middle">
											<c:forEach var="item" items="${cwbStateMap}">
												<c:if test="${detail.flowordertype==item.key}">${item.value}</c:if>
											</c:forEach>
										</td>
										<td align="center" valign="middle">
											<c:forEach var="item" items="${cwbOrderTypeMap}">
												<c:if test="${detail.cwbordertypeid==item.key}">${item.value}</c:if>
											</c:forEach>
										</td>
										<td align="center" valign="middle">${detail.emaildate}</td>
										<td align="center" valign="middle">
											<c:forEach var="item" items="${payTypeMap}">
												<c:if test="${detail.newpaywayid==item.key}">${item.value}</c:if>
											</c:forEach>
										</td>
										<td align="center" valign="middle">${detail.podtime}</td>
										<td align="center" name="sumFee" valign="middle">${detail.sumFee}</td>
										<td align="center" valign="middle">${detail.basicFee}</td>
										<td align="center" valign="middle">${detail.collectionSubsidyFee}</td>
										<td align="center" valign="middle">${detail.areaSubsidyFee}</td>
										<td align="center" valign="middle">${detail.exceedSubsidyFee}</td>
										<td align="center" valign="middle">${detail.businessSubsidyFee}</td>
										<td align="center" valign="middle">${detail.attachSubsidyFee}</td>
									</tr>
								</c:forEach>
							</table> <input type="button" class="input_button2"
							onclick="deleteCwbOrder()" value="移除" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</c:if>
	<!-- 查询层显示 -->
	<div id="queryPage" class="easyui-dialog" title="查询条件"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 220px;">
		<form
			action="<%=request.getContextPath()%>/branchDeliveryFeeBill/branchDeliveryFeeBillList/1"
			method="post" id="queryForm">
			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				style="margin-top: 10px; font-size: 10px;">
				<tr>
					<td align="left" style="width: 15%;">账单批次</td>
					<td style="width: 30%;"><input type="text" name="billBatch"
						value="${queryConditionVO.billBatch}" /></td>
					<td align="left" style="width: 15%;">账单状态</td>
					<td style="width: 30%;"><select name="billState">
							<option value="0">---全部---</option>
							<c:forEach var="item" items="${billStateMap}">
								<c:if test="${queryConditionVO.billState==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${queryConditionVO.billState!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td align="left">账单创建日期</td>
					<td><input type="text" name="createDateFrom"
						value="${queryConditionVO.createDateFrom}" class="easyui-my97"
						datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" /> 至 <input
						type="text" name="createDateTo"
						value="${queryConditionVO.createDateTo}" class="easyui-my97"
						datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" /></td>
					<td align="left">账单核销日期</td>
					<td><input type="text" name="heXiaoDateFrom"
						value="${queryConditionVO.heXiaoDateFrom}" class="easyui-my97"
						datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" /> 至 <input
						type="text" name="heXiaoDateTo"
						value="${queryConditionVO.heXiaoDateTo}" class="easyui-my97"
						datefmt="yyyy/MM/dd" data-options="width:95,prompt: ''" /></td>
				</tr>
				<tr>
					<td align="left">加盟商名称</td>
					<td><input type="text" name="branchName"
						value="${queryConditionVO.branchName}" /></td>
					<td align="left">订单类型</td>
					<td><select name="cwbType">
							<option value="0">---全部---</option>
							<c:forEach var="item" items="${cwbTypeMap}">
								<c:if test="${queryConditionVO.cwbType==item.key}">
									<option value="${item.key}" selected="selected">${item.value}</option>
								</c:if>
								<c:if test="${queryConditionVO.cwbType!=item.key}">
									<option value="${item.key}">${item.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td align="left">排序</td>
					<td><select name="contractColumn">
							<option
								<c:if test='${queryConditionVO.contractColumn == billBatch}'>selected="selected"</c:if>
								value="billBatch">账单批次</option>
							<option
								<c:if test='${queryConditionVO.contractColumn == billState}'>selected="selected"</c:if>
								value="billState">账单状态</option>
							<option
								<c:if test='${queryConditionVO.contractColumn == createDate}'>selected="selected"</c:if>
								value="createDate">账单创建日期</option>
							<option
								<c:if test='${queryConditionVO.contractColumn == heXiaoDate}'>selected="selected"</c:if>
								value="heXiaoDate">账单核销日期</option>
							<option
								<c:if test='${queryConditionVO.contractColumn == branchName}'>selected="selected"</c:if>
								value="branchName">加盟商名称</option>
					</select> <select name="contractColumnOrder">
							<option
								<c:if test='${queryConditionVO.contractColumnOrder == asc}'>selected="selected"</c:if>
								value="asc">升序</option>
							<option
								<c:if test='${queryConditionVO.contractColumnOrder == desc}'>selected="selected"</c:if>
								value="desc">降序</option>
					</select></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4" rowspan="2" align="center" valign="bottom"><input
						type="button" class="input_button2" value="查询"
						onclick="queryBillList()" /> <input type="button"
						class="input_button2" value="关闭"
						onclick="$('#queryPage').dialog('close');" /></td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 扣罚单finder层显示 -->
	<div id="branchListPage" class="easyui-dialog" title="扣罚单"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 600px;">
		<div style="width: 100%;">
			<form
				action="<%=request.getContextPath()%>/branchDeliveryFeeBill/branchList/1"
				method="post" id="queryBranchListForm">
				<table width="80%" style="margin-top: 10px; font-size: 10px;">
					<tr>
						<td align="left">机构名称</td>
						<td><input type="text" name="branchname" /></td>
						<td align="left">机构地址</td>
						<td><input type="text" name="branchaddress" /></td>
					</tr>
					<tr>
						<td colspan="4"><input type="hidden" name="branchName"
							value="" /> <input type="hidden" name="branchId" value="" /> <input
							type="hidden" name="cwbType" value="" /> <input type="hidden"
							name="dateType" value="" /> <input type="hidden" name="beginDate"
							value="" /> <input type="hidden" name="endDate" value="" /> <input
							type="hidden" name="remark" value="" /></td>
					</tr>
					<tr>
						<td colspan="4"><input class="input_button2" type="button"
							onclick="queryBranchList()" value="查询" /></td>
					</tr>
				</table>
			</form>
		</div>

		<div style="width: 100%;">
			<div style="overflow: auto;">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					class="table_2" id="branchListTable">
					<tr class="font_1">
						<td align="center" valign="middle" style="font-weight: bold;">
							机构名称</td>
						<td align="center" valign="middle" style="font-weight: bold;">
							机构编号</td>
						<td align="center" valign="middle" style="font-weight: bold;">
							地址</td>
						<td align="center" valign="middle" style="font-weight: bold;">
							联系人</td>
						<td align="center" valign="middle" style="font-weight: bold;">
							电话</td>
						<td align="center" valign="middle" style="font-weight: bold;">
							手机</td>
					</tr>
					<c:forEach items="${branchList}" var="list">
						<tr onclick="setBranchId('${list.branchid}','${list.branchname}')">
							<td align="center" valign="middle">${list.branchname}</td>
							<td align="center" valign="middle">${list.branchcode}</td>
							<td align="center" valign="middle">${list.branchaddress}</td>
							<td align="center" valign="middle">${list.branchcontactman}</td>
							<td align="center" valign="middle">${list.branchphone}</td>
							<td align="center" valign="middle">${list.branchmobile}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div>
				<input type="hidden" name="branchid" value=""> <input
					type="hidden" name="branchname" value=""> <input
					class="input_button2" type="button" onclick="selectBranch()"
					value="确认" /> <input class="input_button2" type="button"
					onclick="$('#branchListPage').dialog('close')" value="取消" />
			</div>
		</div>
		<c:if test='${page_obj.maxpage>1}'>
			<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					class="table_1">
					<tr>
						<td height="38" align="center" valign="middle" bgcolor="#eef6ff"
							style="font-size: 10px;"><a
							href="javascript:$('#queryBranchListForm').attr('action','1');$('#queryBranchListForm').submit();">第一页</a>
							<a
							href="javascript:$('#queryBranchListForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#queryBranchListForm').submit();">上一页</a>
							<a
							href="javascript:$('#queryBranchListForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#queryBranchListForm').submit();">下一页</a>
							<a
							href="javascript:$('#queryBranchListForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#queryBranchListForm').submit();">最后一页</a>
							共${page_obj.maxpage}页 共${page_obj.total}条记录 当前第<select
							id="selectPg"
							onchange="$('#queryBranchListForm').attr('action',$(this).val());$('#queryBranchListForm').submit()">
								<c:forEach var="i" begin="1" end="${page_obj.maxpage}">
									<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
								</c:forEach>
						</select>页</td>
					</tr>
				</table>
			</div>
		</c:if>
	</div>

	<!-- 导出派费汇总表显示 -->
	<!-- <div   style="width:700px;height:600px;"> -->
	<!-- <div id="exportByCustomerPage" class="easyui-dialog"
		title="扣罚单" data-options="iconCls:'icon-save'"
		style='overflow: auto;'>
	</div> -->
	<div id="exportByCustomerPage" class="easyui-dialog"
		title="派费汇总表" data-options="iconCls:'icon-save'" style="width:695px;height:600px;">
		<form id="exportBranchDeliveryFeeBillListForm">
			<table width="100%" border="0" cellspacing="5" cellpadding="0"
				style="margin-top: 10px; font-size: 10px; border-collapse: separate">
				<tr>
					<td colspan="4" align="left"><input type="button"
						class="input_button2" value="返回"
						onclick="$('#exportByCustomerPage').dialog('close');" /> <input type="button"
						class="input_button2" value="导出"
						onclick="exportByCustomer()" /></td>
				</tr>
			</table>
		</form>
		
		
		<div class=Section1 style='layout-grid: 15.6pt'>
		<p class=MsoNormal align=left style='text-align: left'>
			<!--[if gte vml 1]><v:shapetype
				 id="_x0000_t75" coordsize="21600,21600" o:spt="75" o:preferrelative="t"
				 path="m@4@5l@4@11@9@11@9@5xe" filled="f" stroked="f">
				 <v:stroke joinstyle="miter"/>
				 <v:formulas>
				  <v:f eqn="if lineDrawn pixelLineWidth 0"/>
				  <v:f eqn="sum @0 1 0"/>
				  <v:f eqn="sum 0 0 @1"/>
				  <v:f eqn="prod @2 1 2"/>
				  <v:f eqn="prod @3 21600 pixelWidth"/>
				  <v:f eqn="prod @3 21600 pixelHeight"/>
				  <v:f eqn="sum @0 0 1"/>
				  <v:f eqn="prod @6 1 2"/>
				  <v:f eqn="prod @7 21600 pixelWidth"/>
				  <v:f eqn="sum @8 21600 0"/>
				  <v:f eqn="prod @7 21600 pixelHeight"/>
				  <v:f eqn="sum @10 21600 0"/>
				 </v:formulas>
				 <v:path o:extrusionok="f" gradientshapeok="t" o:connecttype="rect"/>
				 <o:lock v:ext="edit" aspectratio="t"/>
				</v:shapetype><v:shape id="图片_x0020_2" o:spid="_x0000_s2050" type="#_x0000_t75"
				 style='position:absolute;margin-left:1.45pt;margin-top:4.7pt;width:43.1pt;
				 height:34pt;z-index:1;visibility:visible;mso-wrap-style:square;
				 mso-wrap-distance-left:9pt;mso-wrap-distance-top:0;mso-wrap-distance-right:9pt;
				 mso-wrap-distance-bottom:0;mso-position-horizontal:absolute;
				 mso-position-horizontal-relative:text;mso-position-vertical:absolute;
				 mso-position-vertical-relative:text'>
				 <v:imagedata src="exportBranchDeliveryFeeBillList/image001.png" o:title=""/>
				 <w:wrap type="square"/>
				</v:shape>
			<![endif]-->
			<![if !vml]>
			<img width=57 height=45 src="<%=request.getContextPath()%>/exportBranchDeliveryFeeBillList/image002.jpg"
				align=left hspace=12 v:shapes="图片_x0020_2">
			<![endif]>
			<span lang=EN-US
				style='mso-bidi-font-size: 10.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
		</p>

		<p class=MsoNormal align=left style='text-align: left'>&nbsp;&nbsp;</p>

		<p class=MsoNormal align=left style='text-align: left'>
			<b style='mso-bidi-font-weight: normal'> <span lang=EN-US
				style='font-size: 14.0pt; font-family: 黑体'> </span>
			</b> <b style='mso-bidi-font-weight: normal'> <u> <span
					lang=EN-US
					style='font-size: 14.0pt; font-family: 黑体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 黑体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>
						<span style='mso-spacerun: yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<span style='mso-spacerun: yes'>&nbsp;</span>
				</span>
			</u> <span style='font-size: 14.0pt; font-family: 黑体'>
					公司（加盟商）派费汇总表 <span lang=EN-US><o:p></o:p></span>
			</span>
			</b>
		</p>

		<!-- <p class=MsoNormal align=left style='text-align: left'>
			<span
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>&nbsp;&nbsp;<span
				lang=EN-US><o:p></o:p></span></span>
		</p> -->

		<p class=MsoNormal align=left style='text-align: left'>
			<span
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>加盟商（盖章）：<span
				lang=EN-US><o:p></o:p></span></span>
		</p>

		<p class=MsoNormal align=left style='text-align: left'>
			<span
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>联系人：<span
				lang=EN-US><o:p></o:p></span></span>
		</p>

		<p class=MsoNoSpacing>
			<span
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-font-family: Calibri; mso-ascii-theme-font: minor-latin; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-font-family: Calibri; mso-hansi-theme-font: minor-latin'>联系电话：</span><span
				lang=EN-US style='font-size: 7.5pt'><o:p></o:p></span>
		</p>

		<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
			width=660 id="branchFeeTable"
			style='width: 494.8pt; border-collapse: collapse; border: none; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 1.4pt 0cm 1.4pt'>
			<tr style='mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: 1.5pt; border-left: 1.5pt; border-bottom: 1.0pt; border-right: 1.0pt; border-color: black; border-style: solid; mso-border-top-alt: 1.5pt; mso-border-left-alt: 1.5pt; mso-border-bottom-alt: .5pt; mso-border-right-alt: .5pt; mso-border-color-alt: black; mso-border-themecolor: text1; mso-border-style-alt: solid; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 0cm 0cm 0cm; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>序号<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=50
					style='width: 37.85pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>结算项目</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=94 colspan=2
					style='width: 70.65pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>订单类型</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>单量<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>A<o:p></o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>基本派费<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>B<o:p></o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>代收补助费<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>C<o:p></o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>区域属性补助费<span
							lang=EN-US>D<o:p></o:p></span></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>超区补助<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>E<o:p></o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 0cm 0cm 0cm; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>业务补助<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>F<o:p></o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>拖单补助<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>G<o:p></o:p></span>
					</p>
				</td>
				<td width=81
					style='width: 60.75pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: solid black 1.5pt; mso-border-top-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>合计<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>H<o:p></o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: solid black 1.5pt; mso-border-top-themecolor: text1; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-top-alt: 1.5pt; mso-border-left-alt: .5pt; mso-border-bottom-alt: .5pt; mso-border-right-alt: 1.5pt; mso-border-color-alt: black; mso-border-themecolor: text1; mso-border-style-alt: solid; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>备注<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
			</tr>
			
			<tr style='mso-yfti-irow: 5; height: 19.95pt'>
				<td width=60
					style='width: 44.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>非代收订单</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 6; height: 19.95pt'>
				<td width=60
					style='width: 44.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; mso-border-right-alt: solid black .5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>计</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 7; height: 19.95pt'>
				<td width=94 colspan=2
					style='width: 70.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; mso-border-right-alt: solid black .5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>小计<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 8; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>2<o:p></o:p></span>
					</p>
				</td>
				<td width=145 colspan=3
					style='width: 108.5pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>分拣服务费</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 9; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>3<o:p></o:p></span>
					</p>
				</td>
				<td width=145 colspan=3
					style='width: 108.5pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>提货服务费</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 10; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>4<o:p></o:p></span>
					</p>
				</td>
				<td width=145 colspan=3
					style='width: 108.5pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>其他服务费</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=28
					style='width: 21.2pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=75
					style='width: 56.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.3pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=56
					style='width: 42.35pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=47
					style='width: 35.25pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 11; height: 19.95pt'>
				<td width=23 rowspan=3
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>5<o:p></o:p></span>
					</p>
				</td>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>本月<span
							lang=EN-US>KPI</span>得分：<span lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 12; height: 19.95pt'>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>KPI</span><span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>考核标准<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 13; height: 19.95pt'>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>本月<span
							lang=EN-US>KPI</span>超期望值奖励<span lang=EN-US>/</span>扣罚（正数为奖励，负数为扣罚）<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=81  class="editTd"
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 class="editTd"
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 14; height: 19.95pt'>
				<td width=23 rowspan=6 
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>6<o:p></o:p></span>
					</p>
				</td>
				<td width=50 rowspan=6 
					style='width: 37.85pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>扣减项目：
						</span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>本月包裹丢失索赔</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 15; height: 19.95pt'>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>本月货物残损索赔</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 16; height: 19.95pt'>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>本月超期买单索赔</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 17; height: 19.95pt'>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>本月超时投诉、服务投诉、违规操作扣罚、虚假信息扣罚</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 18; height: 19.95pt'>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>本月<span
							lang=EN-US>POS</span>机使用费（含丢失扣款）
						</span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 19; height: 19.95pt'>
				<td width=452 colspan=9
					style='width: 338.8pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; mso-border-right-alt: solid black .5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>小计</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 20; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: double windowtext 1.5pt; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; mso-border-bottom-alt: double windowtext 1.5pt; mso-border-right-alt: solid black .5pt; mso-border-right-themecolor: text1; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>7<o:p></o:p></span>
					</p>
				</td>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: double windowtext 1.5pt; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-bottom-alt: double windowtext 1.5pt; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>应付配送费金额（<span
							lang=EN-US>1+2+3+4+5-6</span>）【开发票金额】
						</span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: double windowtext 1.5pt; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-bottom-alt: double windowtext 1.5pt; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: double windowtext 1.5pt; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 21; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; mso-border-top-alt: double windowtext 1.5pt; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; mso-border-right-alt: solid black .5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>8<o:p></o:p></span>
					</p>
				</td>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>应扣货物保证金</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81  class="editTd"
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54 class="editTd"
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-top-alt: double windowtext 1.5pt; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 22; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>9<o:p></o:p></span>
					</p>
				</td>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>其他保证金金额（如<span
							lang=EN-US>POS</span>机押金等）
						</span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 23; mso-yfti-lastrow: yes; height: 19.95pt'>
				<td width=23
					style='width: 16.95pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.5pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: .5pt; mso-border-left-alt: 1.5pt; mso-border-bottom-alt: 1.5pt; mso-border-right-alt: .5pt; mso-border-color-alt: black; mso-border-themecolor: text1; mso-border-style-alt: solid; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-font-kerning: 0pt'>10<o:p></o:p></span>
					</p>
				</td>
				<td width=502 colspan=10
					style='width: 376.65pt; border-top: none; border-left: none; border-bottom: solid black 1.5pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-bottom-alt: solid black 1.5pt; mso-border-bottom-themecolor: text1; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; mso-font-kerning: 0pt'>实付配送费金额（<span
							lang=EN-US>4-5-6</span>）
						</span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p></o:p></span>
					</p>
				</td>
				<td width=81 
					style='width: 60.75pt; border-top: none; border-left: none; border-bottom: solid black 1.5pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-bottom-alt: solid black 1.5pt; mso-border-bottom-themecolor: text1; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=54
					style='width: 40.45pt; border-top: none; border-left: none; border-bottom: solid black 1.5pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; background: #EAF1DD; mso-background-themecolor: accent3; mso-background-themetint: 51; padding: 0cm 1.4pt 0cm 1.4pt; height: 19.95pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
		</table>

		<p class=MsoNormal
			style='line-height: 2.0pt; mso-line-height-rule: exactly'>
			<span lang=EN-US
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
		</p>


		<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
			width=660
			style='width: 495.05pt; border-collapse: collapse; border: none; mso-border-alt: solid black 1.5pt; mso-border-themecolor: text1; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 1.4pt 0cm 1.4pt'>
			<tr style='mso-yfti-irow: 0; mso-yfti-firstrow: yes; height: 26.45pt'>
				<td width=660 colspan=7
					style='width: 495.05pt; border: solid black 1.5pt; mso-border-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; mso-border-alt: solid black 1.5pt; mso-border-themecolor: text1; mso-border-bottom-alt: solid black .5pt; mso-border-bottom-themecolor: text1; background: #D9D9D9; mso-background-themecolor: background1; mso-background-themeshade: 217; padding: 0cm 1.4pt 0cm 1.4pt; height: 26.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<b style='mso-bidi-font-weight: normal'><span
							style='font-size: 9.0pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>货物保证金余额及应收加盟商欠货款余额对账单<span
								lang=EN-US><o:p></o:p></span></span></b>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 1; height: 29.45pt'>
				<td width=23 rowspan=2
					style='width: 17.1pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-left-alt: solid black 1.5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>1<o:p></o:p></span>
					</p>
				</td>
				<td width=158 rowspan=2
					style='width: 118.55pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>货物保证金<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'><o:p>&nbsp;</o:p></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>【合同保证金限额每月更新，每半年根据实际情况补扣或退回】<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=104
					style='width: 77.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>合同保证金限额<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>A<o:p></o:p></span>
					</p>
				</td>
				<td width=76
					style='width: 2.0cm; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>截止上月末已收取金额<span
							lang=EN-US><br> B</span></span><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
				<td width=113
					style='width: 3.0cm; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>本月收取金额<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>C</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
				<td width=186 colspan=2
					style='width: 139.7pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 29.45pt'>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>保证金差额<span
							lang=EN-US><br style='mso-special-character: line-break'>
								<![if !supportLineBreakNewLine]><br
								style='mso-special-character: line-break'> <![endif]>
								<o:p></o:p></span></span>
					</p>
					<p class=MsoNormal align=center
						style='text-align: center; line-height: 9.0pt; mso-line-height-rule: exactly'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>D</span><span
							lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black'><o:p></o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 2; height: 20.6pt'>
				<td width=104
					style='width: 77.95pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=76
					style='width: 2.0cm; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=113 class="editTd"
					style='width: 3.0cm; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
				<td width=186 colspan=2
					style='width: 139.7pt; border-top: none; border-left: none; border-bottom: solid black 1.0pt; mso-border-bottom-themecolor: text1; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; mso-border-alt: solid black .5pt; mso-border-themecolor: text1; mso-border-right-alt: solid black 1.5pt; mso-border-right-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 3; mso-yfti-lastrow: yes; height: 20.6pt'>
				<td width=23
					style='width: 17.1pt; border-top: none; border-left: solid black 1.5pt; mso-border-left-themecolor: text1; border-bottom: solid windowtext 1.5pt; border-right: solid black 1.0pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-top-alt: black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: black 1.5pt; mso-border-left-themecolor: text1; mso-border-bottom-alt: windowtext 1.5pt; mso-border-right-alt: black .5pt; mso-border-right-themecolor: text1; mso-border-style-alt: solid; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast'>2<o:p></o:p></span>
					</p>
				</td>
				<td width=158
					style='width: 118.55pt; border: none; border-bottom: solid windowtext 1.5pt; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; mso-border-left-alt: solid black .5pt; mso-border-left-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>欠货款余额<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=293 colspan=3
					style='width: 219.7pt; border: none; border-bottom: solid windowtext 1.5pt; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal align=center style='text-align: center'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>截止本月底，本加盟商应付<span
							lang=EN-US>****</span>品信有限公司代收货款余额为：<span lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=161
					style='width: 120.5pt; border: none; border-bottom: solid windowtext 1.5pt; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal align=right style='text-align: right'>
						<u><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black; position: relative; top: -3.0pt; mso-text-raise: 3.0pt; letter-spacing: -2.0pt'><span
								style='mso-spacerun: yes'> </span></span></u><u><span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><span
								style='mso-spacerun: yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span
								style='mso-spacerun: yes'>&nbsp;</span></span></u><span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'>元<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=26
					style='width: 19.2pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.5pt; border-right: solid black 1.5pt; mso-border-right-themecolor: text1; mso-border-top-alt: solid black .5pt; mso-border-top-themecolor: text1; padding: 0cm 1.4pt 0cm 1.4pt; height: 20.6pt'>
					<p class=MsoNormal align=right style='text-align: right'>
						<span lang=EN-US
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; color: black'><o:p>&nbsp;</o:p></span>
					</p>
				</td>
			</tr>
		</table>

		<p class=MsoNormal
			style='line-height: 5.0pt; mso-line-height-rule: exactly'>
			<span lang=EN-US
				style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'><o:p>&nbsp;</o:p></span>
		</p>

		<table class=MsoTableGrid border=0 cellspacing=0 cellpadding=0
			style='border-collapse: collapse; border: none; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt; mso-border-insideh: none; mso-border-insidev: none'>
			<tr style='mso-yfti-irow: 0; mso-yfti-firstrow: yes'>
				<td width=187 
					style='width: 140.1pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>运营副总：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=132 
					style='width: 99.2pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal align=left style='text-align: left'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>财务经理：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=161 
					style='width: 120.5pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal align=left style='text-align: left'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>结算经理：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=192 
					style='width: 144.2pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal align=left style='text-align: left'>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>区域经理：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
			</tr>
			<tr style='mso-yfti-irow: 1; mso-yfti-lastrow: yes'>
				<td width=187 
					style='width: 140.1pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>日<span
							lang=EN-US><span style='mso-spacerun: yes'>&nbsp;&nbsp;</span></span>期：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=132 
					style='width: 99.2pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>日<span
							lang=EN-US><span style='mso-spacerun: yes'>&nbsp;&nbsp;</span></span>期：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=161 
					style='width: 120.5pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>日<span
							lang=EN-US><span style='mso-spacerun: yes'>&nbsp;&nbsp;</span></span>期：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
				<td width=192 
					style='width: 144.2pt; padding: 0cm 5.4pt 0cm 5.4pt'>
					<p class=MsoNormal>
						<span
							style='font-size: 7.5pt; font-family: 宋体; mso-ascii-theme-font: minor-fareast; mso-fareast-font-family: 宋体; mso-fareast-theme-font: minor-fareast; mso-hansi-theme-font: minor-fareast; mso-bidi-font-family: 宋体; color: black; mso-font-kerning: 0pt'>日<span
							lang=EN-US><span style='mso-spacerun: yes'>&nbsp;&nbsp;</span></span>期：<span
							lang=EN-US><o:p></o:p></span></span>
					</p>
				</td>
			</tr>
		</table>
	</div>
	</div>

	<div id="updatePageDiv">
		<form
			action="<%=request.getContextPath()%>/branchDeliveryFeeBill/updateBranchDeliveryFeeBillPage"
			method="post" id="updatePageForm">
			<input type="hidden" name="id" value="">
		</form>
	</div>

	<div id="exportByDetailDiv">
		<form
			action="<%=request.getContextPath()%>/branchDeliveryFeeBill/exportByDetail"
			method="post" id="exportByDetailForm">
			<input type="hidden" name="cwbs" value="">
		</form>
	</div>
	
	<div class="jg_10"></div>
	<div class="jg_10"></div>
	<div class="iframe_bottom"> 
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
						<tr>
							<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
							<a href="javascript:$('#branchDeliveryFeeBillListForm').attr('action','1');$('#branchDeliveryFeeBillListForm').submit();" >第一页</a>　
							<a href="javascript:$('#branchDeliveryFeeBillListForm').attr('action','${page_obj.previous<1?1:page_obj.previous}');$('#branchDeliveryFeeBillListForm').submit();">上一页</a>　
							<a href="javascript:$('#branchDeliveryFeeBillListForm').attr('action','${page_obj.next<1?1:page_obj.next }');$('#branchDeliveryFeeBillListForm').submit();" >下一页</a>　
							<a href="javascript:$('#branchDeliveryFeeBillListForm').attr('action','${page_obj.maxpage<1?1:page_obj.maxpage}');$('#branchDeliveryFeeBillListForm').submit();" >最后一页</a>
							　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
							<select id="selectPg" onchange="$('#branchDeliveryFeeBillListForm').attr('action',$(this).val());$('#branchDeliveryFeeBillListForm').submit()">
								<c:forEach var="i" begin='1' end='${page_obj.maxpage}'>
									<option value='${i}' ${page==i?'selected=seleted':''}>${i}</option>
								</c:forEach>
							</select>页
							</td>
						</tr>
		</table>
	</div>
	<div>
		<form action="<%=request.getContextPath()%>/branchDeliveryFeeBill/branchDeliveryFeeBillList/1" method="post" id="branchDeliveryFeeBillListForm">
		</form>
	</div>
</body>
</html>