<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<title></title>
<c:set var="ctx_path" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx_path}/css/reset.css" type="text/css" />
<link rel="stylesheet" href="${ctx_path}/css/index.css" type="text/css" />
<script src="${ctx_path}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="${ctx_path}/js/js.js"></script>

<link rel="stylesheet"
	href="${ctx_path}/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<style type="text/css">
.form {
	margin-top: 20px;
}

.time {
	width: 70px;
}

.btn_div {
	margin-top: 5px;
	margin-bottom: 5px;
}

.td_hidden {
	display: none;
}
</style>

</head>

<body style="background: #f5f5f5">

	<form class="form" action="${ctx_path}/time_effective/submit" method="post">
		<table width="50%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="modelAndView">
			<tr class="font_1" style="background-color: rgb(255, 255, 255);">
				<td align="center" valign="middle" bgcolor="#eef6ff">时效名称</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">时效类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">小时</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">分钟</td>
			</tr>
			<c:forEach items="${teTypeList}" var="p">
				<tr>
					<td>${p.name}</td>
					<td><select name="te_timetype">
							<c:forEach items="${teTimeTypeEnumMap}" var="entry">
								<option value="${entry.key}" <c:if test="${p.timeTypeOrdinal==entry.key}">selected</c:if>>${entry.value}</option>
							</c:forEach>
					</select></td>
					<td>
					<fmt:parseNumber var="index" value="${(p.scope / 3600)}" integerOnly="true" /> 
					<select name="te_hour" class="time">
							<c:forEach var="i" begin="0" end="72" step="1">
								<option value="${i}" <c:if test="${index == i}">selected</c:if>>${i}</option>
							</c:forEach>
					</select></td>
					<td>
					<fmt:parseNumber var="index" value="${(p.scope%3600)/60}" integerOnly="true" /> 
					<select name="te_minute" class="time">
							<c:forEach var="i" begin="0" end="59" step="1">
								<option value="${i}" <c:if test="${index == i}">selected</c:if>>${i}</option>
							</c:forEach>
					</select></td>
					<td class="td_hidden"><input type="hidden" name="te_id" value="${p.id}" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan=4>
					<div class="btn_div">
						<input type="submit" id="btnval" value="保存" class="input_button2"> <input
							type="button" id="btnval" value="重置" class="input_button2"
							onclick="location.href='${ctx_path}/time_effective/'">
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>






