<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.MatchExceptionOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchList = (List<Branch>) request.getAttribute("branchList");
	Integer tWaitTraOrdCnt = (Integer) request.getAttribute("tWaitTraOrdCnt");
	Integer hWaitTraOrdCnt = (Integer) request.getAttribute("hWaitTraOrdCnt");
	Integer tWaitMatOrdCnt = (Integer) request.getAttribute("tWaitMatOrdCnt");
	Integer hWaitMatOrdCnt = (Integer) request.getAttribute("hWaitMatOrdCnt");
	Integer tTraOrdCnt = (Integer) request.getAttribute("tTraOrdCnt");
	Integer tMatOrdCnt = (Integer) request.getAttribute("tMatOrdCnt");
	
	List<MatchExceptionOrder> tWaitHanOrdList = (List<MatchExceptionOrder>) request.getAttribute("tWaitHanOrdList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
<title>异常匹配处理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

	function redistributionBranch() {
		var branchid = $("#branchid").val();
		var $msg = $("#msg");
		var $order_no = $("#order_no");
		if(branchid == "-1"){
			$msg.html("请选择站点");
			$order_no.val("");
			return;
		}

		var orderNo = $order_no.val();
		$.ajax({
			type:"post",
			dataType:"json",
			async:true,
			url:"<%=request.getContextPath()%>/meh/redistributionbranch",
			data:{
				cwb : orderNo,
				branchid:branchid
			},
			success:function(data){
				showResult($order_no ,$msg , data);
			},
			error:function(data){
				
			}
		});
	}

	
	function showResult($order_no,$msg , data){
		$msg.html(data.message);
	 	$order_no.val("");
		if(!data.successed){
			 addExceptionData(data);
			 return;
		}
		var isToday = data.today;
		var outarea = data.outarea;
		var rSpan = "";
		var aSpan = "today_";
		if(isToday){
			rSpan +="today_";
		}
		else{
			rSpan +="history_";
		}
		if(outarea){
			rSpan+="transfer_";
			aSpan +="transfer_";
		}else{
			rSpan +="normal_";	
			aSpan+="normal_";
		}
		var $rSpan = $("#" +rSpan + "not_matched");
		var $aSpan = $("#" +aSpan + "matched");
		 $rSpan.html(parseInt($rSpan.html()) - 1);
	 	$aSpan.html(parseInt($aSpan.html()) + 1);
	}
	
	function addExceptionData(data){
		if(data.meo == undefined){
			return;
		}
		var $exceptionTable = $("#exception_table");
		if($exceptionTable.find("tr[cwb=" +data.meo.cwb + "]").length != 0){
			return;
		}
		$exceptionTable.append(createTR(data.meo));
	}
	

	function showTab(index) {
		var $tab = $(".saomiao_tab2 li").eq(index);
		$tab.children().addClass("light");
		$tab.siblings().children().removeClass("light");
		$(".tabbox li").eq(index).show().siblings().hide();
	}
	
	function queryOrder(tableId , tabIndex , today , transfer , match)
	{
		$.ajax({
			type:"post",
			async:true,
			url:"<%=request.getContextPath()%>/meh/querymatchexceptionorder",
			dataType : "json",
			data : {
				today : today,
				transfer : transfer,
				match : match
			},
			success : function(data) {
				refreshTableData(tableId, data, tabIndex);
				recordCurTabCond(today, transfer, match);
			}
		});
	}

	function recordCurTabCond(today, transfer, match) {
		var $form = $("#exportForm");
		$("#today", $form).val(today);
		$("#transfer", $form).val(transfer);
		$("#match", $form).val(match);

	}
	function refreshTableData(tableId, data, tabIndex) {
		showTab(tabIndex);
		addRowData(tableId, data);
	}

	function addRowData(tableId, data) {
		var $table = $("#" + tableId);
		$table.empty();
		for (var i = 0; i < data.length; i++) {
			$table.append(createTR(data[i]));
		}
	}

	function createTR(data) {
		var tr = "<tr cwb="+ data.cwb +">";
		tr += createTD("center", "100px", data.cwb);
		tr += createTD("center", "150px", data.reportOutAreaTime);
		tr += createTD("center", "100px", data.reportOutAreaBranchName);
		tr += createTD("center", "100px", data.reportOutAreaUserName);
		tr += createTD("center", "100px", data.matchBranchName);
		tr += createTD("center", "100px", data.customerName);
		tr += createTD("center", "150px", data.customerPhone);
		tr += createTD("right", "100px", data.receivedFee);
		tr += createTD("center", null, data.customerAddress);
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

	function exportData() {
		$("#exportForm").submit();
	}

	function exportExceptionnData() {
		var $exceptionTable = $("#exception_table");
		var rows = [];
		$exceptionTable.find("tr").each(function() {
			var $tr = $(this);
			var row = [];
			$tr.find("td").each(function(){
				var $td = $(this);
				row.push($td.html());
			});
			rows.push(row);
		});
		var exeData = JSON.stringify(rows);
		var $exeForm = $("#exportExceptionForm");
		$("#data", $exeForm).val(exeData);
		$exeForm.submit();
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
<body style="background: #eef9ff" marginwidth="0" marginheight="0">
	<div class="saomiao_box2">

		<div class="saomiao_topnum2">
			<dl class="blue">
				<dt>
					<span>今日待转单</span><span>今日待匹配</span>
				</dt>
				<dd style="cursor: pointer">
					<span onclick="queryOrder('t_wait_handle_table',0,true,false,null)"><a href="#"
						id="today_transfer_not_matched"><%=tWaitTraOrdCnt%></a></span> <span
						onclick="queryOrder('t_wait_handle_table',0,true,null,false)"><a href="#"
						id="today_normal_not_matched"><%=tWaitMatOrdCnt%></a></span>
				</dd>
			</dl>


			<dl class="yellow">
				<dt>
					<span>历史待转单</span><span>历史待匹配</span>
				</dt>
				<dd>
					<span onclick="queryOrder('h_wait_handle_table',1,false,false,null)"><a href="#"
						id="history_transfer_not_matched"><%=hWaitTraOrdCnt%></a></span> <span
						onclick="queryOrder('h_wait_handle_table',1,false,null,false)"><a href="#"
						id="history_normal_not_matched"><%=hWaitMatOrdCnt%></a></span>
				</dd>
			</dl>

			<dl class="green">
				<dt>
					<span>今日已转单</span><span>今日已匹配</span>
				</dt>
				<dd>
					<span onclick="queryOrder('t_handle_table',2,true,true,null)"><a href="#"
						id="today_transfer_matched"><%=tTraOrdCnt %></a></span> <span
						onclick="queryOrder('t_handle_table',2,true,null,true)"><a href="#"
						id="today_normal_matched"><%=tMatOrdCnt %></a></span>
				</dd>
			</dl>

			<input type="button" id="refresh" value="刷新"
				onclick="location.href='<%=request.getContextPath()%>/meh/matchexceptionhandle'"
				style="float: left; width: 100px; height: 65px; cursor: pointer; border: none; background: url(../images/buttonbgimg1.gif) no-repeat; font-size: 18px; font-family: '微软雅黑', '黑体'" />
			<br clear="all" />
		</div>

		<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_tab">
					<ul id="bigTag">
						<li><a href="#" id="scancwbTag" class="light">扫描订单</a></li>
					</ul>
				</div>
				<div class="saomiao_selet2">
					站&#12288;点：<select id="branchid" name="branchid">
						<option value="-1" selected>请选择</option>
						<%
							for (Branch b : branchList) {
						%>
						<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%>
						</option>
						<%
							}
						%>
					</select>
				</div>
				<div class="saomiao_inwrith2">
					<div class="saomiao_left2">
						<p>
							订单号：<input type="text" class="saomiao_inputtxt" id="order_no" name="order_no" value=""
								onKeyDown='if(event.keyCode==13&&$(this).val().length>0){redistributionBranch()}' />
						</p>
					</div>
					<div class="saomiao_right2">
						<p id="msg" name="msg"></p>
					</div>
				</div>
			</div>
		</div>

		<div>
			<div class="saomiao_tab2">
				<ul>
					<li><a href="#" onclick="queryOrder('t_wait_handle_table',0,true,false,false)"
						class="light">今日待处理</a></li>
					<li><a href="#" onclick="queryOrder('h_wait_handle_table',1,false,false,false)">历史待处理</a></li>
					<li><a href="#" onclick="queryOrder('t_handle_table',2,true,true,true)">今日已处理</a></li>
					<li><a href="#" onclick="showTab(3)">异常单明细</a></li>
				</ul>
			</div>
			<div id="ViewList" class="tabbox">
				<li><input type="button" id="btnval0" value="导出Excel" class="input_button1"
					onclick='exportData()' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="150" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="t_wait_handle_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
											<%
												for (MatchExceptionOrder meo : tWaitHanOrdList) {
											%>
											<tr cwb="<%=meo.getCwb()%>" class="cwbids">
												<td width="100" align="center"><%=meo.getCwb()%></td>
												<td width="150" align="center"><%=meo.getReportOutAreaTime()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaBranchName()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaUserName()%></td>
												<td width="100" align="center"><%=meo.getReportOutAreaBranchName()%></td>
												<td width="100" align="center"><%=meo.getCustomerName()%></td>
												<td width="150" align="center"><%=meo.getCustomerPhone()%></td>
												<td width="100" align="right"><%=meo.getReceivedFee()%></td>
												<td align="center"><%=meo.getCustomerAddress()%></td>
											</tr>
											<%
												}
											%>

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
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="150" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="h_wait_handle_table" width="100%" border="0" cellspacing="1" cellpadding="2"
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
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="150" align="center" bgcolor="#f1f1f1">处理时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">处理站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">处理人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
											<td align="center" bgcolor="#f1f1f1">取件地址</td>
										</tr>
									</table>
									<div style="height: 160px; overflow-y: scroll">
										<table id="t_handle_table" width="100%" border="0" cellspacing="1" cellpadding="2"
											class="table_2">
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table></li>

				<li style="display: none"><input type="button" id="btnval0" value="导出Excel"
					class="input_button1" onclick='exportExceptionnData()' />
					<table width="100%" border="0" cellspacing="10" cellpadding="0">
						<tbody>
							<tr>
								<td width="10%" height="26" align="left" valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
										<tr>
											<td width="100" align="center" bgcolor="#f1f1f1">订单号</td>
											<td width="150" align="center" bgcolor="#f1f1f1">上报超区时间</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">上报超区人</td>
											<td width="100" align="center" bgcolor="#f1f1f1">分配站点</td>
											<td width="100" align="center" bgcolor="#f1f1f1">退件人姓名</td>
											<td width="150" align="center" bgcolor="#f1f1f1">联系方式</td>
											<td width="100" align="center" bgcolor="#f1f1f1">应收运费</td>
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
			</div>
		</div>
	</div>
	<form id="exportForm" action="<%=request.getContextPath() + "/meh/exportdata"%>" method="post">
		<input type="hidden" id="today" name="today" value="true" /> <input type="hidden" id="transfer"
			value="false" name="transfer" /> <input type="hidden" id="match" name="match" value="false" />
	</form>
	<form id="exportExceptionForm" action="<%=request.getContextPath() + "/meh/exportexceptiondata"%>"
		method="post">
		<input type="hidden" id="data" name="data" />
	</form>
</body>
</html>
