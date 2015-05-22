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
<script src="${ctx_path}/js/json2.min.js" type="text/javascript"></script>

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

.search_form {
	margin-top: 10px;
}
</style>
<script>
	function exportData() {
		var action = "${ctx_path}/smtfaresettle/export/${result.detail}";
		$("#searchForm").attr("action", action);
		$("#searchForm").submit();
	}
</script>
</head>

<body style="background: #f5f5f5">
	<div class="search_div">
		<form id="searchForm" name="searchForm" class="search_form"
			action="${ctx_path}/smtfaresettle/export/${result.detail}" method="post">
			<input type="hidden" name="optTimeType" value="${cond.optTimeType}" /> <input type="hidden"
				name="startTime" value="${cond.startTime}" /> <input type="hidden" name="endTime"
				value="${cond.endTime}" /> <input type="hidden" name="stationId" value="${cond.stationId}" />
			<input type="hidden" name="deliverId" value="${cond.deliverId}" /> <input type="hidden"
				name="venderId" value="${cond.venderId}" /> <input type="hidden" name="type"
				value="${cond.type}" /> <input type="button" id="btnval" value="返回" class="input_button2"
				onclick="javascript:history.back(-1);" /> <input type="button" id="btnval" value="导出"
				class="input_button2" onclick="exportData()" />

		</form>
	</div>
	<div class="table_div">
		<div style="width: 100%">
			<table style="width: 100%" border="0" cellspacing="1" cellpadding="0" class="table_2"
				id="static_table">
				<tr class="font_1">
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">应交运费</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">实交运费</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">支付方式</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">反馈结果</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">反馈时间</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">系统接收时间</td>
					<td width="100" align="center" valign="middle" bgcolor="#eef6ff">订单生成时间</td>
				</tr>
				<c:forEach items="${result.resultList}" var="tmp">
					<tr>
						<td width="100">${tmp.stationName}</td>
						<td width="100">${tmp.deliverName}</td>
						<td width="100">${tmp.cwb}</td>
						<td width="100">${tmp.shouldFee}</td>
						<td width="100">${tmp.receivedFee}</td>
						<td width="100">${tmp.payType}</td>
						<td width="100">${tmp.feedbackResult}</td>
						<td width="100">${tmp.feedbackTime}</td>
						<td width="100">${tmp.systemAcceptTime}</td>
						<td width="100">${tmp.createTime}</td>
					</tr>
				</c:forEach>
			</table>
		</div>




		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/${result.detail}/1');$('#searchForm').submit();">第一页</a>
						<a
						<c:choose>  
                <c:when test="${result.page != 1}">href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/${result.detail}/${result.page - 1}');$('#searchForm').submit();"</c:when>  
                <c:otherwise>href="#"</c:otherwise>  
        		</c:choose>>上一页</a>
						<a
						<c:choose>  
                <c:when test="${(result.page != result.pageCount)}">href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/${result.detail}/${result.page + 1}');$('#searchForm').submit();"</c:when>  
                <c:otherwise>href="#"</c:otherwise>  
        		</c:choose>>下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','${ctx_path}/smtfaresettle/${result.detail}/${result.pageCount}');$('#searchForm').submit();">最后一页</a>
						共${result.pageCount}页 共${result.count}条记录 当前第<select id="select"
						onchange="$('#searchForm').attr('action','${ctx_path}/smtfaresettle/${result.detail}/'+$(this).val());$('#searchForm').submit()">
							<c:forEach var="i" begin="1" end="${result.pageCount}" step="1">
								<option value="${i}" <c:if test="${result.page == i}">selected</c:if>>${i}</option>
							</c:forEach>
					</select>页</td>
				</tr>
			</table>
		</div>
</body>
</html>






