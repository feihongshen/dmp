<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.VO.express.DeliverSummaryItem,cn.explink.domain.VO.express.DeliverSummaryView"%>
<%@page import="cn.explink.domain.express.ExpressPreOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	//预订单列表
	DeliverSummaryView deliverSummaryView = (DeliverSummaryView)request.getAttribute("deliverSummaryView");

	String beginDate =request.getAttribute("beginDate")==null?"":(String)request.getAttribute("beginDate");
	
	String endDate =request.getAttribute("endDate")==null?"":(String)request.getAttribute("endDate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件分配/调整</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
	$(function() {
		var $menuli = $(".kfsh_tabbtn ul li");
		var $menulilink = $(".kfsh_tabbtn ul li a");
		$menuli.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})

	$(function() {
		var $menuli1 = $("#bigTag li");
		$menuli1.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
		});

		var $menuli2 = $("#smallTag li");
		$menuli2.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli2.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})

	function tabView(tab) {
		$("#" + tab).click();
	}

	function addAndRemoval(cwb, tab, isRemoval) {
		var trObj = $("#ViewList tr[id='TR" + cwb + "']");
		if (isRemoval) {
			$("#" + tab).append(trObj);
		} else {
			$("#ViewList #errorTable tr[id='TR" + cwb + "error']").remove();
			trObj.clone(true).appendTo("#" + tab);
			$("#ViewList #errorTable tr[id='TR" + cwb + "']").attr("id",
					trObj.attr("id") + "error");
		}
	}

	$(function() {
		$("#strtime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#endtime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#hiddenBeginDate").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#hiddenEndDate").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
	});
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="inputselect_box">
		<form action="<%=request.getContextPath()%>/stationOperation/exportSummary">
			<input type="hidden" id="hiddenBeginDate" name="beginDate" value="<%=beginDate%>" /> <input
				type="hidden" id="hiddenEndDate" name="endDate" value="<%=endDate%>" /> <span><input
				name="" type="submit" value="导出" class="input_button2" /> </span>
		</form>

		<form action="1" method="post" id="searchForm">
			交件时间： <input type="text" name="beginDate" id="strtime" value="<%=beginDate%>" /> 到 <input
				type="text" name="endDate" id="endtime" value="<%=endDate%>" /> <input type="submit" id="find"
				onclick="$('#searchForm').attr('action',1);return true;" value="查询" class="input_button2" />
		</form>
	</div>

	<%
		if (deliverSummaryView != null) {
			DeliverSummaryItem headSummary = deliverSummaryView.getHeadSummary();
	%>

	<div
		style="margin-top: 60px; border: 1px solid #000; height: 80px; text-align: center; vertical-align: middle; background: white; font-size: large;">
		<table style="margin-top: 10px;">
			<tr>
				<td>
					<div
						style="border: 1px solid #000; height: 80px; margin-left: -4px; margin-top: -4px; width: 300px;">
						<div style="float: right; margin-right: 50px; margin-top: 10px;">汇总运费</div>
						<div style="margin-left: 50px; margin-top: 10px;">汇总单量</div>
						<div style="float: right; margin-right: 50px; font-size: 28;">
							<%=headSummary.getSummaryFee()%></div>
						<div style="margin-left: 50px; font-size: 28;"><%=headSummary.getSummaryNum()%></div>

					</div>
				</td>
				<td>

					<div
						style="border: 1px solid #000; height: 80px; margin-left: -4px; margin-top: -4px; width: 300px; color: blue;">
						<div style="float: right; margin-right: 50px; margin-top: 10px;">现付汇总运费</div>
						<div style="margin-left: 50px; margin-top: 10px;">现付单量</div>
						<div style="float: right; margin-right: 50px; font-size: 28;"><%=headSummary.getNowPayTotalFee()%></div>
						<div style="margin-left: 50px; font-size: 28;"><%=headSummary.getNowPayNum()%></div>

					</div>
				</td>
				<td>

					<div
						style="border: 1px solid #000; height: 80px; margin-left: -4px; margin-top: -4px; width: 300px; color: green;">
						<div style="float: right; margin-right: 50px; margin-top: 10px;">到付汇总运费</div>
						<div style="margin-left: 50px; margin-top: 10px;">到付单量</div>
						<div id="arriveOrderCount" style="float: right; margin-right: 50px; font-size: 28;"><%=headSummary.getArrivePayTotalFee()%></div>
						<div id="arriveOrderFee" style="margin-left: 50px; font-size: 28;"><%=headSummary.getArrivePayNum()%></div>

					</div>
				</td>
				<td>
					<div
						style="border: 1px solid #000; height: 80px; margin-left: -4px; margin-top: -4px; width: 300px; color: red;">
						<div style="float: right; margin-right: 50px; margin-top: 10px;">月结汇总运费</div>
						<div style="margin-left: 50px; margin-top: 10px;">月结单量</div>
						<div style="float: right; margin-right: 50px; font-size: 28;"><%=headSummary.getMonthPayTotalFee()%></div>
						<div style="margin-left: 50px; font-size: 28;"><%=headSummary.getMonthPayNum()%></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="saomiao_box2">
		<div>
			<table width="100%" border="0" cellspacing="10" cellpadding="0" style="margin-top: 10px">
				<tbody>
					<tr>
						<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
								<tr>
									<td width="50" align="center" bgcolor="#f1f1f1">小件员</td>
									<td width="50" align="center" bgcolor="#f1f1f1">汇总单量</td>
									<td width="50" align="center" bgcolor="#f1f1f1">汇总运费</td>
									<td width="50" align="center" bgcolor="#f1f1f1">现付单量</td>
									<td width="50" align="center" bgcolor="#f1f1f1">现付汇总运费</td>
									<td width="50" align="center" bgcolor="#f1f1f1">到付单量</td>
									<td width="50" align="center" bgcolor="#f1f1f1">到付汇总运费</td>
									<td width="50" align="center" bgcolor="#f1f1f1">月结单量</td>
									<td width="50" align="center" bgcolor="#f1f1f1">月结汇总运费</td>

								</tr>
							</table>

							<table id="todayToTakeTable" width="100%" border="0" cellspacing="1" cellpadding="2"
								class="table_2">
								<%
									List<DeliverSummaryItem> bodySummaryList = deliverSummaryView.getBodySummaryList();
										if (bodySummaryList != null && !bodySummaryList.isEmpty())
											for (DeliverSummaryItem bodySummary : bodySummaryList) {
								%>
								<tr>
									<td width="50"><%=bodySummary.getDeliverName()%></td>
									<td width="50"><%=bodySummary.getSummaryNum()%></td>
									<td width="50"><%=bodySummary.getSummaryFee() == null ? 0 : bodySummary.getSummaryFee()%></td>
									<td width="50"><%=bodySummary.getNowPayNum()%></td>
									<td width="50"><%=bodySummary.getNowPayTotalFee() == null ? 0 : bodySummary.getNowPayTotalFee()%></td>
									<td width="50"><%=bodySummary.getArrivePayNum()%></td>
									<td width="50"><%=bodySummary.getArrivePayTotalFee() == null ? 0 : bodySummary.getArrivePayTotalFee()%></td>
									<td width="50"><%=bodySummary.getMonthPayNum()%></td>
									<td width="50"><%=bodySummary.getMonthPayTotalFee() == null ? 0 : bodySummary.getMonthPayTotalFee()%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	</div>
	<%
		}
	%>
</body>
</html>