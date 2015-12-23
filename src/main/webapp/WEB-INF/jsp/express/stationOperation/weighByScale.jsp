<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page
	import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.VO.express.DeliverSummaryItem,cn.explink.domain.VO.express.CombineQueryView"%>
<%@page import="cn.explink.domain.express.CwbOrderForCombineQuery"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String waybillNo = (String) request.getAttribute("waybillNo");
	Double weight = (Double) request.getAttribute("weight");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>电子称称重</title>
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

	function submitWeight(waybillNo) {
		var weight = $('#weightSpan').text();
		$('#weight').val($('#weightSpan').text());
		if (weight == null || weight == "") {
			showHintInfo("实际重量为空，检查电子称！");
			return;
		}
		$('#submitForm').attr("action", $("#submitWeight").val());
		$('#submitForm').submit();
	};
	function showHintInfo(info) {
		$(".tishi_box").html(info);
		$(".tishi_box").show();
		setTimeout("$(\".tishi_box\").hide(1000)", 2000);
	};
</script>
<script type="text/javascript">
	function getWeightRepeatable() {
		window.setInterval("setWeight()", 1);
	}
	function setWeight() {
		var weight = window.parent.document.getElementById("scaleApplet")
				.getWeight();

		if (weight != null && weight != '') {
			document.getElementById("weightSpan").innerHTML = weight;
		}
	}
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0" onload="getWeightRepeatable();">
	<div class="inputselect_box">
		<form method="post" id="submitForm">
			<table width="60%">
				<tr>
					<td>运单号<input type="text" id="waybillNo" name="waybillNo"
						style="height: 30px; font-size: x-large; font-weight: bold;"
						onKeyDown='if(event.keyCode==13&&$(this).val().length>0){submitWeight($(this).val());}' />
					</td>
					<td style="color: red; font-size: x-large; font-weight: bold;">实际重量（kg）：<span
						id="weightSpan">0.00</span> <input type="hidden" id="weight" name="weight" />
					</td>
				</tr>
			</table>
		</form>
		<hr />
		<h1 style="font-weight: bold; font-size: xx-large;">上一次扫描结果：</h1>
		<table width="50%">
			<tr>
				<td style="font-size: x-large;">运单号：<span id="waybillNoSpan"><%=waybillNo == null ? "" : waybillNo%></span>
				</td>
				<td style="font-size: x-large;">实际重量（kg）：<span id="lastWeightSpan"><%=weight == null ? "" : weight%></span>
				</td>
			</tr>
		</table>
	</div>

	<!-- 提交重量 -->
	<input type="hidden" id="submitWeight"
		value="<%=request.getContextPath()%>/stationOperation/submitWeight" />
	<!-- 查询运单是否存在 -->
	<input type="hidden" id="isExpressOrderExist"
		value="<%=request.getContextPath()%>/stationOperation/isExpressOrderExist" />
</body>
</html>