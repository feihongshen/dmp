<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title></title>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx_pat}/css/2.css" type="text/css">
<link rel="stylesheet" href="${ctx_pat}/css/index.css" type="text/css">
<%-- <link rel="stylesheet" href="${ctx_pat}/css/reset.css" type="text/css"> --%>
<script src="${ctx_pat}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="${ctx_pat}/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${ctx_pat}/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="${ctx_pat}/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="${ctx_pat}/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<script>
	function check() {
		if ($("#strtime").val() == "") {
			alert("请选择开始时间");
			return false;
		}
		if ($("#endtime").val() == "") {
			alert("请选择结束时间");
			return false;
		}
		if ($("#strtime").val() > $("#endtime").val()) {
			alert("开始时间不能大于结束时间");
			return false;
		}
		if (!Days() || ($("#strtime").val() == '' && $("#endtime").val() != '')
				|| ($("#strtime").val() != '' && $("#endtime").val() == '')) {
			alert("时间跨度不能大于7天！");
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
		$("#starttime").datetimepicker({
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
		$("#org").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

		$("#vender").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		
		$("#time_type").multiSelect({
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
</script>

<style>
.search_div {
	padding: 5px;
	z-index: 9;
	width: 100%;
	background: url(../images/repeatx.png) repeat-x 0 -485px;
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
</style>

</head>

<body style="background: #eef9ff">
	<div class="search_div">
		<form id="searchForm" name="searchForm"
			action="${pageContext.request.contextPath}/b2cdownloadmonitor/list/1" method="post">
			<div>
				操作时间：<select id="opt_time_type" name="opt_time_type">
					<c:forEach items="${time_type_map}" var="entry">
						<option value="${entry.key}" <c:if test="${entry.key == time_type}">selected</c:if>>${entry.value}</option>
					</c:forEach>
				</select><input type="text" name="starttime" id="starttime" /> 到 <input type="text" name="endtime"
					id="endtime" />机构名称: <select id="org" name="org" multiple="multiple" style="width: 100px;">
					<c:forEach items="${org_map}" var="entry">
						<option value="${entry.key}">${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('org',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('org',0,'请选择');">取消全选</a>] 供货商: <select id="vender"
					name="vender" multiple="multiple" style="width: 100px;">
					<c:forEach items="${vender_map}" var="entry">
						<option value="${entry.key}">${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('vender',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('vender',0,'请选择');">取消全选</a>]
			</div>
			<div>
				<input name="enable_sys_query" type="checkbox" class="sys_query_checkbox" />启用系统预置
			</div>
			<div>
				列表展示选择: <select id="time_type" name="time_type" multiple="multiple" style="width: 100px;">
					<c:forEach items="${vender_map}" var="entry">
						<option value="${entry.key}">${entry.value}</option>
					</c:forEach>
				</select> [<a href="javascript:multiSelectAll('vender',1,'请选择');">全选</a>] [<a
					href="javascript:multiSelectAll('vender',0,'请选择');">取消全选</a>] <input type="button" id="find"
					value="查询" class="input_button2" /> <input type="button" id="btnval" value="导出"
					class="input_button2" />
			</div>
		</form>
	</div>
	<div class="table_div">
		<div style="float: left; width: 20%">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
				id="static_table">
				<tr class="font_1">
					<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
					<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
					<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">生成单量</td>
				</tr>
			</table>
		</div>

		<div style="width: 80%; overflow: auto;">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" id="dynamic_table"
				class="table_2">
				<tr class="font_1">
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
					<td align="center" valign="middle" bgcolor="#eef6ff">1111111111111111111111</td>
				</tr>
			</table>
		</div>
	</div>


	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdue_ex_mo/list/1');$('#searchForm').submit();">第一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdue_ex_mo/list/');$('#searchForm').submit();">上一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdue_ex_mo/list/');$('#searchForm').submit();">下一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','${ctx_path}/overdue_ex_mo/list/');$('#searchForm').submit();">最后一页</a>
					共?页 共?条记录 当前第<select id="selectPg"
					onchange="$('#searchForm').attr('action','${ctx_path}/overdue_ex_mo/list/'+$(this).val());$('#searchForm').submit()">
				</select>页</td>
			</tr>
		</table>
	</div>
</body>
</html>






