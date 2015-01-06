<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title></title>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx_path}/css/2.css" type="text/css">
<link rel="stylesheet" href="${ctx_path}/css/index.css" type="text/css">
<script src="${ctx_path}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="${ctx_path}/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${ctx_path}/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="${ctx_path}/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="${ctx_path}/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${ctx_path}/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<script>
	function check() {
		if ($("#startTime").val() == "") {
			alert("请选择开始时间");
			return false;
		}
		if ($("#endTime").val() == "") {
			alert("请选择结束时间");
			return false;
		}
		if ($("#starTime").val() > $("#endTime").val()) {
			alert("开始时间不能大于结束时间");
			return false;
		}

		return true;
	}
	function Days() {
		var day1 = $("#strtime").val();
		var day2 = $("#endtime").val();
		var y1, y2, m1, m2, d1, d2;//year, month, day;   
		day1 = new Date(Date.parse(day1.replace(/-/g, "/")));
		day2 = new Date(Date.parse(day2.replace(/-/g, "/")));
		y1 = day1.getFullYear();
		y2 = day2.getFullYear();
		m1 = parseInt(day1.getMonth()) + 1;
		m2 = parseInt(day2.getMonth()) + 1;
		d1 = day1.getDate();
		d2 = day2.getDate();
		var date1 = new Date(y1, m1, d1);
		var date2 = new Date(y2, m2, d2);
		var minsec = Date.parse(date2) - Date.parse(date1);
		var days = minsec / 1000 / 60 / 60 / 24;
		if (days > 7) {
			return false;
		}
		return true;
	}
</script>

<script>
	$(function() {
		$("#startTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#endTime").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#orgs").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

		$("#venders").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

		$("#delivers").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

	});
	$(function() {
		$("#find").click(function() {
			if (check()) {
				$("#searchForm").submit();
			}
		});
	});

	$(function() {
		var branchIds = getDeliverIds();
		var venderIds = getVenderIds();
		var condVO = getSearchFormValueObject();
		for (var i = 0; i < branchIds.length; i++) {
			sendRequest(branchIds[i], venderIds[i], condVO);
		}
	});

	function getDeliverIds() {
		var delivers = $("td[id='deliver_id']");
		var deliverIds = [];
		for (var i = 0; i < delivers.length; i++) {
			deliverIds.push($(delivers[i]).html());
		}
		return deliverIds;
	}

	function getVenderIds() {
		var venders = $("td[id='vender_id']");
		var venderIds = [];
		for (var i = 0; i < venders.length; i++) {
			venderIds.push($(venders[i]).html());
		}
		return venderIds;
	}

	function sendRequest(branchId, venderId, condVO) {
		$.ajax({
			type : "post",
			url : "${ctx_path}/smtfaresettle/getdeliverdata/" + branchId + "/"
					+ venderId + "?" + Math.random(),
			dataType : "json",
			async : true,
			data : {
				cond : JSON.stringify(condVO)
			},
			success : function(result) {
				showRowData(result);
			}
		});
	}

	function showRowData(result) {
		var deliverId = result.deliverId;
		var venderId = result.venderId;
		var trs = $('#static_table tr');
		for (var i = 1; i < trs.length; i++) {
			var $tr = $(trs[i]);
			var cond1 = $tr.find("td").eq(0).html() == deliverId;
			var cond2 = $tr.find("td").eq(2).html() == venderId;
			if (cond1 && cond2) {
				var tds = $tr.find("td");
				$(tds[4]).html(result.deliverPickingCnt);
				$(tds[5]).html(result.smtSuccessedCnt);
				$(tds[6]).html(result.shouldFee);
				$(tds[7]).html(result.receivedFee);
			}
		}
	}

	function getSearchFormValueObject() {
		var $searchForm = $("#searchForm");
		var vo = {};
		vo.optTimeType = $("#optTimeType").val();
		vo.startTime = $("#startTime").val();
		vo.endTime = $("#endTime").val();

		return vo;
	}

	function getMultiSelectValues(name) {
		var $fields = $("input[name='" + name + "']");
		var fieldIds = [];
		for (var i = 0; i < $fields.length; i++) {
			if ($($fields[i]).attr("checked") != undefined) {
				fieldIds.push($($fields[i]).val());
			}
		}
		return fieldIds;
	}
</script>

<style>
.search_div {
	padding: 5px;
	z-index: 9;
	width: 100%;
	background: url(/images/repeatx.png) repeat-x 0 -485px;
	border: 1px solid #86AFD5;
}

.table_div {
	margin-top: 5px;
}

.sys_query_checkbox {
	vertical-align: middle
}

.search_div div {
	padding: 3px;
}

.hide_td {
	display: none;
}
</style>

</head>

<body style="background: #eef9ff">
	<div class="search_div">
		<form id="searchForm" name="searchForm"
			action="${pageContext.request.contextPath}/smtfaresettle/deliver/1" method="post">
			<div>
				操作时间：<select id="optTimeType" name="optTimeType">
					<c:forEach items="${const.timeTypeMap}" var="entry">
						<option value="${entry.key}" <c:if test="${entry.key == cond.optTimeType}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select><input type="text" name="startTime" id="startTime" value="${cond.startTime}" /> 到 <input
					type="text" name="endTime" id="endTime" value="${cond.endTime}" /> 站点： <select id="orgs"
					name="orgs" multiple="multiple" style="width: 100px;">
					<c:forEach items="${const.orgMap}" var="entry">
						<option value="${entry.key}" <c:if test="${fn:contains(cond.orgs,entry.key)}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('orgs',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('orgs',0,'请选择');">取消全选</a>] 供货商：<select id="venders"
					name="venders" style="width: 100px;" multiple="multiple">
					<c:forEach items="${const.venderMap}" var="entry">
						<option value="${entry.key}"
							<c:if test="${fn:contains(cond.venders,entry.key)}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('venders',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('venders',0,'请选择');">取消全选</a>]
			</div>
			<div>
				小件员：<select id="delivers" name="delivers" multiple="multiple" style="width: 100px;">
					<c:forEach items="${const.deliverMap}" var="entry">
						<option value="${entry.key}"
							<c:if test="${fn:contains(cond.delivers,entry.key)}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('delivers',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('delivers',0,'请选择');">取消全选</a>] <input type="button" id="find"
					value="查询" class="input_button2" /> <input type="button" id="btnval" value="导出"
					class="input_button2" />
			</div>
		</form>
	</div>
	<div class="table_div">
		<div style="width: 100%">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
				id="static_table">
				<tr class="font_1">
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">领件单量</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">上门退成功单量</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">应收运费</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">实收运费</td>
				</tr>
				<c:forEach items="${result.resultList}" var="tmp">
					<tr>
						<td id="deliver_id" class="hide_td">${tmp.deliverId}</td>
						<td width="100">${tmp.deliverName}</td>
						<td id="vender_id" class="hide_td">${tmp.venderId}</td>
						<td width="100">${tmp.venderName}</td>
						<td width="100">&nbsp;</td>
						<td width="100">&nbsp;</td>
						<td width="100">&nbsp;</td>
						<td width="100">&nbsp;</td>
					</tr>
				</c:forEach>
			</table>
		</div>



		<div class="iframe_bottom">

			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
				id="total_table">
				<tr class="font_1">
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">领件单量</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">上门退成功单量</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">应收运费</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">实收运费</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/deliver/1');$('#searchForm').submit();">第一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/deliver/${result.page - 1}');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/deliver/${result.page + 1}');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/deliver/${result.pageCount}');$('#searchForm').submit();">最后一页</a>
						共${result.pageCount}页 共${result.count}条记录 当前第<select id="select"
						onchange="$('#searchForm').attr('action','${ctx_path}/smtfaresettle/deliver/'+$(this).val());$('#searchForm').submit()">
							<c:forEach var="i" begin="1" end="${result.pageCount}" step="1">
								<option value="${i}" <c:if test="${result.page == i}">selected</c:if>>${i}</option>
							</c:forEach>
					</select>页</td>
				</tr>
			</table>
		</div>
</body>
</html>






