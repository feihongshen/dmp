<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.SmtOrderContainer"%>
<%@page import="cn.explink.domain.SmtOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<title>中转入库扫描（明细）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>

<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function addAndRemoval(cwb,tab,isRemoval){
	var trObj = $("#ViewList tr[cwb='"+cwb+"']");
	if(isRemoval){
		$("#"+tab).append(trObj);
	}else{
		$("#ViewList #errorTable tr[id='TR"+cwb+"error']").remove();
		trObj.clone(true).appendTo("#"+tab);
		$("#ViewList #errorTable tr[id='TR"+cwb+"']").attr("id",trObj.attr("id")+"error");
	}
}

	
function branchDeliver(pname,scancwb,deliverid,requestbatchno){
	if(deliverid==-1){
		$("#msg").html("扫描前请选择小件员");
		return ;
	}else if(scancwb.length>0){
		var allnum = 0;
		var isChaoqu = $("#isChaoqu").is(":checked");
		$.ajax({
			type: "POST",
			url:pname+"/PDA/cwbbranchdeliver/"+scancwb+"?deliverid="+deliverid+"&requestbatchno="+requestbatchno + "&isChaoqu=" + isChaoqu,
			dataType:"json",
			success : function(data) {
				$("#scancwb").val("");
				$("#pagemsg").html("");
				//var linghuoSuccessCount = deliverStr[deliverid].split(",").length-1;
				if(data.statuscode=="000000"){
					$("#msg").html(scancwb+data.errorinfo+"         （共"+data.body.cwbOrder.sendcarnum+"件，已扫"+data.body.cwbOrder.scannum+"件）");
					
					$("#scansuccesscwb").val(scancwb);
					$("#showcwb").html("订 单 号："+scancwb);
					$("#consigneeaddress").html("地 址："+data.body.cwbOrder.consigneeaddress);
					// 增加超区领货显示结果 add by chunlei05.li 2016/8/16
					if(data.body.isChaoqu == true) {
						if(data.body.matchDeliver == "") {
							$("#matchDeliver").html("尚未匹配小件员");
						} else {
							$("#matchDeliver").html("订单匹配小件员：" + data.body.matchDeliver);
						}
						$("#receiveDeliver").html("领货小件员：" + data.body.receiveDeliver);
					} else {
						$("#matchDeliver").html("");
						$("#receiveDeliver").html("");
					}
					if(data.body.cwbOrder.customercommand.indexOf('预约')>=0&&data.yuyuedaService=='yes')
					{	
						$("#customercommand").html("预约派送");
					}
					else{$("#customercommand").html("");}
					if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Peisong.getValue()%>){
						$("#cwbordertype").html("订单类型：配送订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()%>){
						$("#cwbordertype").html("订单类型：上门换订单");
						$("#fee").html("应收金额："+data.body.cwbOrder.receivablefee);
					}else if(data.body.cwbOrder.cwbordertypeid==<%=CwbOrderTypeIdEnum.Shangmentui.getValue()%>){
						$("#cwbordertype").html("订单类型：上门退订单");
						$("#fee").html("应退金额："+data.body.cwbOrder.paybackfee);
					}
					if(data.body.showRemark!=null){
					$("#cwbDetailshow").html("订单备注："+data.body.showRemark);
					}
					if(data.body.isChaoqu != true) {
						$("#exceldeliverid").html(data.body.cwbdelivername);
					} else {
						$("#exceldeliverid").html("");
					}
					$("#deliver").html("已领货（"+data.body.cwbdelivername+"）");
					afterDispatch(data);
				}else{
					$("#exceldeliverid").html("");
					$("#showcwb").html("");
					$("#consigneeaddress").html("");
					$("#matchDeliver").html("");
					$("#receiveDeliver").html("");
					$("#cwbordertype").html("");
					$("#cwbDetailshow").html("");
					$("#deliver").html("已领货");
					$("#fee").html("");
					$("#customercommand").html("");
					$("#msg").html(scancwb+"         （异常扫描）"+data.errorinfo);
					addAndRemoval(scancwb,"errorTable",false);
				}
				$("#responsebatchno").val(data.responsebatchno);
				batchPlayWav(data.wavList);
			}
		});
	}
}




 $(function(){
	 $("#today_checkbox").click(function(){
		var checked = $(this).attr("checked");
		checkedTableAllRow("today_table" ,checked);
	 });
	 
	 $("#history_checkbox").click(function(){
		var checked =  $(this).attr("checked");
		checkedTableAllRow("history_table" ,checked);
	});
 });
 
 function checkedTableAllRow(tableId,checked){
	 if(checked){
		 $("#" +tableId ).find("input[type='checkbox']").attr("checked" ,"checked");	 
	 }else{
		 $("#" +tableId ).find("input[type='checkbox']").removeAttr("checked");
	 }	 
 }
 
 
function loadSmtOrder(dataType , timeType , dispatched , page , tableId , tabIndex){
	 $.ajax({
		 url:"<%=request.getContextPath()%>/smt/querysmtorder" + "?timestamp=" + new Date().getTime(),
			dataType : "json",
			async : true,
			data:{
				dataType:dataType,
				timeType:timeType,
				dispatched:dispatched,
				tableId:tableId,
				page:page,
				deliverid:"${deliverid}"
			},
			success : function(data) {
				var dataList = data.smtOrderList;
				if(tableId == "today_table" || tableId =="history_table"){
 					refreshTable(tableId,dataList,tabIndex ,true,false);
				}
				else{
 					refreshTable(tableId,dataList,tabIndex ,false,false);
				}
				setCurrentDataFilterCond(dataType , timeType , dispatched);
			},
			error : function(data) {
			}
		});
}

function setCurrentDataFilterCond(dataType , timeType , dispatched)
{
	var $from = $("#exportForm");
	$("#dataType" ,$from).val(dataType);
	$("#timeType" ,$from).val(timeType);
	$("#dispatched" , $from).val(dispatched);
}

function loadTodayOutAreaOrder(){
	document.getElementById("todayOutData").style.display="";
	getOutAreaData();
	 $.ajax({
		 type:"post",
		 url:'<%=request.getContextPath()+ "/smt/querytodayoutareaorder"%>'  + "?timestamp=" + new Date().getTime(),
			dataType : "json",
			async : true,
			success : function(data) {
				var dataList = data.smtOrderList;
				refreshTable("out_area_table", dataList, 4, false,true);
			},
			error : function(data) {
			}
		});
	}

	function refreshTable(tableId, dataList, tableIndex, withCheckBox,outArea) {
		var $table = $("#" + tableId);
		$table.empty();
		if (dataList) {
			var dataHtml = createTableRowData(dataList, withCheckBox,outArea);
			$table.append(dataHtml);
		}
		showTab(tableIndex);
	}

	function createTableRowData(dataList, withCheckBox,outArea) {
		var allRow = "";
		var length = dataList.length;
		for (var i = 0; i < length; i++) {
			var tr = createTR(dataList[i], withCheckBox,outArea);
			allRow += tr;
		}
		return allRow;
	}

	function showTab(index) {
		var $tab = $(".saomiao_tab2 li").eq(index);
		$tab.children().addClass("light");
		$tab.siblings().children().removeClass("light");
		$(".tabbox li").eq(index).show().siblings().hide();
	}

	function createTR(data, widthCheckBox ,outArea) {
		var tr = "<tr cwb="+ data.cwb +">";
		if (widthCheckBox) {
			tr += createTD("center", "40px", "<input type='checkbox'></input>");
		}
		tr += createTD("center", "120px", data.cwb);
		tr += createTD("center", "100px", data.matchBranch==null?"":data.matchBranch);
		tr += createTD("right", "100px", data.receivedFee);
		if (data.strDeliver != undefined && !widthCheckBox && !outArea) {
			tr += createTD("center", "100px", data.strDeliver);
		}
		tr += createTD("center", "100px", data.customerName);
		tr += createTD("center", "150px", data.phone);
		tr += createTD("center", null, data.address);
		tr += "</tr>";
		return tr;
	}

	function createTD(align, width, content) {
		var td = "<td align='" + align + "'";
		if (width) {
			td += " width='" + width + "'";
		}
		td += ">" + content + "</td>"
		return td;
	}

	var currentOutAreaTableId;
	var currentCwbs;
	function outArea(tableId) {
		var cwbs = this.getSelectedCwbs(tableId);
		if (cwbs.length == 0) {
			showTipDialog();
			return;
		}
		currentOutAreaTableId = tableId;
		currentCwbs = cwbs;
		showConfrimDialog();
	}
	
	function getSelectedCwbs(tableId) {
		var $qickOutAreaField = $("#"+tableId + "_quick");
		var qCwb =  $qickOutAreaField.val();
		var cwbs = [];
		if(qCwb.length != 0){
			cwbs.push(qCwb);
		}
		else{
			var $table = $("#" + tableId);
			$table.find("input[type='checkbox']").filter(":checked").each(
			function() {
				var cwb = $(this).closest("tr").attr("cwb");
				cwbs.push(cwb);
			});
		}
		return cwbs;
	}

	function confirmOutArea() {
		$.ajax({
			type:"post",
			url:'<%=request.getContextPath() + "/smt/smtorderoutarea"%>'+ "?timestamp=" + new Date().getTime(),
			dataType : "json",
			async : false,
			data : {
				cwbs : currentCwbs.join(",")
			},
			success : function(data) {
				handleOutAreaSuccess(data , currentOutAreaTableId, currentCwbs);
			},
			error : function(data) {
			}
		});
	}

	function handleOutAreaSuccess(data , tableId, cwbs) {
		if(data.successed){		
			var $table = $("#" + tableId);
			for (var i = 0; i < cwbs.length; i++) {
				$table.find("tr[cwb='" + cwbs[i] + "']").remove();
			}
			var length = cwbs.length;
			var $tOutArea = $("#t_out_area");
			var oriCnt = $tOutArea.html();
			if(oriCnt != "点击查询"){
				$tOutArea.html(parseInt(oriCnt) + length);
				reduceNotHandleNumber(length);
			}
		}
		closeConfirmDialog();
		$("#" + tableId + "_quick").val("");
		$("#" + tableId + "_msg").html(data.msg);
	}

	function afterDispatch(data) {
		if(data.body.cwbOrder.cwbordertypeid != 2){
			return;
		}
		if (data.body.isRepeatPicking) {
			return;
		}
		var today = data.body.isTodayFlow;
		var outarea = data.body.cwbOrder.outareaflag;
		var rSpan = "";
		var aSpan = "today_";
	if (today) {
			rSpan += "today_";
		} else {
			rSpan += "history_";
		}
		if (outarea == 0) {
			rSpan += "normal_";
			aSpan += "normal_"
		} else {
			rSpan += "transfer_";
			aSpan += "transfer_"
		}
		var $RSpan = $("#" + rSpan + "not_dispatched");
		var $ASpan = $("#" + aSpan + "dispatched");
		
		var $Aimg = $ASpan.find("img");
		if($Aimg.length == 0){
			$ASpan.html(parseInt($ASpan.html()) + 1);
		}
		
		var $Rimg = $RSpan.find("img");
		if($Rimg.length == 0){
			$RSpan.html(parseInt($RSpan.html()) - 1);
		}
		removeScanCwb(data);
	}
	
	function removeScanCwb(data){
		var cwb= data.body.cwbOrder.cwb;
		var $today_table = $("#today_table");
		var $history_table = $("#history_table");
		removeTableData($today_table , cwb);
		removeTableData($history_table , cwb);
	}
	
	
	function removeTableData($table , cwb){
		var $tr = $table.find("tr[cwb=" + cwb + "]");
		$tr.remove();
	}
	
	function reduceNotHandleNumber(length) {
		var dataType = $("#dataType").val();
		var timeType = $("#timeType").val();
		var dispatched = $("#dispatched").val();
		var spanId = timeType + "_" + dataType;
		if (dispatched == "true") {
			spanId += "_dispatched";
		} else {
			spanId += "_not_dispatched";
		}
		var $span = $("#" + spanId);
		var oriVal = parseInt($span.html());
		$span.html(oriVal - length);
	}

	function showTipDialog() {
		var $box = $("#tip_dialog");
		$box.show();
		//innerCenterBox($("#box_contant"));
	}

	function closeTipDialog() {
		$("#tip_dialog").hide();
	}

	function showConfrimDialog() {
		var $box = $("#confirm_dialog");
		$box.show();
	}

	function closeConfirmDialog() {
		$("#confirm_dialog").hide();
	}

	function exportData() {
		$("#exportForm").submit();
	}

	function exportTodayOutAreaData() {
		$("#exportTodayOutAreaForm").submit();
	}

	function exportExceptionData() {
		var cwbs = '';
		var $from = $("#exportTodayOutAreaForm");
		$("#exception_table tr").each(function() {
			var cwb = $(this).attr("cwb");
			cwbs += "'" + cwb + "',";
		});
		$("#cwbs", $from).val(cwbs);
		$from.submit();
	}

	$(function() {
		$.ajax({
			type : "post",
			dataType : "json",
			url : '<%=request.getContextPath() + "/smt/querysmthistoryordercount"%>'+ "?timestamp=" + new Date().getTime(),
					data : {deliverid: "${deliverid}"},
					async : true,
					success : function(data) {
						$("#history_normal_not_dispatched").empty();
						$("#history_normal_not_dispatched").html(
								data.hNorNotDisCnt);
						$("#history_transfer_not_dispatched").empty();
						$("#history_transfer_not_dispatched").html(
								data.hTraNotDisCnt);
					},
					error : function(data) {
					}

				});
	});
	
	
	$(function() {
		$.ajax({
			type : "post",
			dataType : "json",
			url : '<%=request.getContextPath() + "/smt/querysmttodaynotdisordercount"%>'+ "?timestamp=" + new Date().getTime(),
					data : {deliverid: "${deliverid}"},
					async : true,
					success : function(data) {
						$("#today_normal_not_dispatched").empty();
						$("#today_normal_not_dispatched").html(data.tNorNotDisCnt);
						$("#today_transfer_not_dispatched").empty();
						$("#today_transfer_not_dispatched").html(data.tTraNotDisCnt);
						refreshTable("today_table",data.tNotDisData.smtOrderList,0 ,true,false);
					},
					error : function(data) {
					}

				});
	});
	
	
	$(function() {
		$.ajax({
			type : "post",
			dataType : "json",
			url : '<%=request.getContextPath() + "/smt/querysmttodaydisordercount"%>'+ "?timestamp=" + new Date().getTime(),
					data : {deliverid: "${deliverid}"},
					async : true,
					success : function(data) {
						$("#today_normal_dispatched").empty();
						$("#today_normal_dispatched").html(data.tNorDisCnt);
						$("#today_transfer_dispatched").empty();
						$("#today_transfer_dispatched").html(data.tTraDisCnt);
					},
					error : function(data) {
					}
				});
	});
	
	
	function getOutAreaData(){
		$.ajax({
			type : "post",
			dataType : "json",
			url : '<%=request.getContextPath() + "/smt/querysmttodayoutareacount"%>'+ "?timestamp=" + new Date().getTime(),
					data : {},
					async : true,
					success : function(data) {
						$("#t_out_area").empty();
						$("#t_out_area").html(data.tOutAreaCnt);
					},
					error : function(data) {
					}

				});
	}
	
	$(function() {
		$("#t_out_area").empty();
		$("#t_out_area").html("点击查询");
	});
	
	// 页面刷新 add by chunlei05.li 2016/8/16
	function refresh() {
		var deliverid = $("#deliverid").val();
		if (deliverid == -1) {
			location.href="<%=request.getContextPath()%>/smt/smtorderdispatch";
		} else {
			console.log("<%=request.getContextPath()%>/smt/smtorderdispatch?deliverid=" + deliverid);
			location.href="<%=request.getContextPath()%>/smt/smtorderdispatch?deliverid=" + deliverid;
		}
	}
</script>
<style>
dl dt span {
	width: 50%;
	display: inline-block;
}

dl dd span {
	width: 46%;
	display: inline-block;
}

.blue a {
	color: #336699;
}

.yellow a {
	color: #ff6600;
}

.green a {
	color: #339900
}

.red a {
	color: #666633;
}

.input_button1 {
	margin: 10px 0px 0px 10px;
}

.saomiao_tab {
	height: 17px;
}
</style>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_box2">

		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>
					<span>今日新单待分派</span><span>今日转单待分派</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="loadSmtOrder('normal','today',false, 1 ,'today_table',0)"><a href="#"
						id="today_normal_not_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span> <span
						onclick="loadSmtOrder('transfer','today',false, 1 ,'today_table',0)"><a href="#"
						id="today_transfer_not_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span>
				</dd>
			</dl>


			<dl class="yellow">
				<dt>
					<span>历史新单待分派</span><span>历史转单待分派</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="loadSmtOrder('normal','history',false,1,'history_table',1)"><a href="#"
						id="history_normal_not_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span> <span
						onclick="loadSmtOrder('transfer','history',false,1,'history_table',1)"><a href="#"
						id="history_transfer_not_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span>
				</dd>
			</dl>

			<dl class="green">
				<dt>
					<span>今日分派新单</span><span>今日分派转单</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="loadSmtOrder('normal','today',true,1,'today_dispatch_table',2)"><a
						href="#" id="today_normal_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span> <span
						onclick="loadSmtOrder('transfer','today',true,1,'today_dispatch_table',2)"><a href="#"
						id="today_transfer_dispatched"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span>
				</dd>
			</dl>


			<dl class="red">
				<dt>今日站点超区</dt>
				<dd style="cursor: pointer">
					<span onclick="loadTodayOutAreaOrder()"><a id="t_out_area" href="#"><img
							src="<%=request.getContextPath()%>/images/loading_small.gif" /></a></span>
				</dd>
			</dl>
			<input type="button" id="refresh" value="刷新"
				onclick="refresh()"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_tab">
					<ul id="bigTag">
						<li><a href="#" id="scancwbTag"
							onclick="clearMsg();$(function(){$('#baleno').parent().hide();$('#finish').parent().hide();$('#baleno').val('');$('#scancwb').val('');$('#scancwb').parent().show();$('#scancwb').show();$('#scancwb').focus();})"
							class="light">扫描订单</a></li>
					</ul>
				</div>
				<div class="saomiao_selet2">
					小件员：<select id="deliverid" name="deliverid" class="select1" onchange="refresh()">
						<option value="-1">请选择</option>
						<c:forEach var="deliver" items="${deliverList }">
							<option value="${deliver.userid }" <c:if test="${deliver.userid eq deliverid }">selected</c:if>>${deliver.realname }</option>
						</c:forEach>
					</select>
					快捷站点超区：<input type="text" class="input_text1" id="today_table_quick" name="today_table_quick" value=""
						onKeyDown="if(event.keyCode==13&&$(this).val().length>0){outArea('today_table');}" /> <label
						id="today_table_msg" style="color: red"></label>
					超区领货：<input type="checkbox" id="isChaoqu" name="isChaoqu"/>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							订单号：<input type="text" class="saomiao_inputtxt2" id="scancwb" name="scancwb" value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){branchDeliver("<%=request.getContextPath()%>",$(this).val(),$("#deliverid").val(),$("#requestbatchno").val());}' />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
						<p id="cwbordertype" name="cwbordertype"></p>
						<p id="showcwb" name="showcwb"></p>
						<p id="cwbgaojia" name="cwbgaojia" style="display: none">高价</p>
						<p id="consigneeaddress" name="consigneeaddress"></p>
						<p id="matchDeliver" name="matchDeliver"></p>
						<p id="receiveDeliver" name="receiveDeliver"></p>
						<p id="fee" name="fee"></p>
						<p id="exceldeliverid" name="exceldeliverid"></p>
						<p id="cwbDetailshow" name="cwbDetailshow"></p>
						<p id="customercommand" name="customercommand"></p>
					</div>
					<input type="hidden" id="requestbatchno" name="requestbatchno" value="0" /> <input
						type="hidden" id="scansuccesscwb" name="scansuccesscwb" value="" />
				</div>
			</div>
		</div>

		<div>
			<div class="saomiao_tab2">
				<ul>
					<li><a href="#" class="light"
						onclick="loadSmtOrder('all','today',false,1,'today_table',0)">今日待分配</a></li>
					<li><a href="#" onclick="loadSmtOrder('all','history',false,1,'history_table',1)">历史待分派</a></li>
					<li><a href="#" onclick="loadSmtOrder('all','today',true,1,'today_dispatch_table',2)">今日已分派</a></li>
					<li><a href="#" onclick="showTab(3)">异常单明细</a></li>
					<li><a id="todayOutData" style="display:none;" href="#" onclick="loadTodayOutAreaOrder()">今日站点超区</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportData()' /> <input type="button" id="btnval0" value="站点超区" class="input_button1"
					onclick="outArea('today_table')" />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="40" align="center" bgcolor="#f1f1f1"><input id="today_checkbox"
												type="checkbox"></input></td>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 170px; overflow-y: scroll">
										<table id="today_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportData()' /> <input type="button" id="btnval0" value="站点超区"
					class="input_button1" onclick="outArea('history_table')" /> <label style="margin-left: 20px">快捷站点超区：</label>
					<input type="text" class="input_text1" id="history_table_quick" name="history_table_quick" value=""
					onKeyDown="if(event.keyCode==13&&$(this).val().length>0){outArea('history_table');}" /> <label
					id="history_table_msg" style="color: red"></label>
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="40px" align="center" bgcolor="#f1f1f1"><input id="history_checkbox"
												type="checkbox"></input></td>
											<td width="120px" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100px" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150px" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="history_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportData()' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="today_dispatch_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportExceptionData()' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="exception_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportTodayOutAreaData()' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="100" align="center" bgcolor="#f1f1f1">匹配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="out_area_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>
			</div>
		</div>
	</div>
	<input type="hidden" id="edit" value="<%=request.getContextPath()%>/smt/showconfirmdialog" />

	<div id="confirm_dialog" style="display: none">
		<div id="box_contant" style="width: 270px">
			<div id="box_top_bg"></div>
			<div id="box_in_bg">
				<h1>
					<div id="close_box" onclick="closeConfirmDialog()"></div>
					提示
				</h1>
				<form>
					<div id="box_form" style="font-size: 13px">
						<ul>
							<li>请确认是否将选中数据处理为站点超区.</li>
						</ul>
					</div>
					<div align="center" style="margin-left: 4px">
						<span><input type="button" value="确认" class="button" onclick="confirmOutArea()" /></span> <span><input
							type="button" value="取消" class="button" onclick="closeConfirmDialog()" /></span>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="tip_dialog" style="display: none;">
		<div id="box_contant" style="width: 200px">
			<div id="box_top_bg"></div>
			<div id="box_in_bg">
				<h1>
					<div id="close_box" onclick="closeTipDialog()"></div>
					提示
				</h1>
				<form>
					<div id="box_form" style="font-size: 20px; margin-left: 4px">
						<ul>
							<li id="content">请选中数据</li>
						</ul>
					</div>
					<div align="center">
						<input type="button" value="确认" class="button" onclick="closeTipDialog()" />
					</div>
				</form>
			</div>
		</div>
	</div>
	<form action='<%=request.getContextPath() + "/smt/exportdata"%>' method="post" id="exportForm"
		style="padding: 10px">
		<input type="hidden" id="dataType" name="dataType" value="all" /> <input type="hidden"
			id="timeType" name="timeType" value="today" /> <input type="hidden" id="dispatched"
			name="dispatched" value="false" />
	</form>

	<form action='<%=request.getContextPath() + "/smt/exportexceptiondata"%>' method="post"
		id="exportExceptionDataForm" style="padding: 10px">
		<input type="hidden" id="cwbs" name="cwbs" />
	</form>


	<form action='<%=request.getContextPath() + "/smt/exporttodayoutareadata"%>' method="post"
		id="exportTodayOutAreaForm" style="padding: 10px"></form>



</body>
</html>
