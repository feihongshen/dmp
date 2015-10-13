<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2">
	<tr class="font_1">
		<td align="center" bgcolor="#F4F4F4">查询模块</td>
		<td align="center" bgcolor="#F4F4F4">查询时间</td>
		<td bgcolor="#F4F4F4" width="900">查询条件</td>
		<td bgcolor="#F4F4F4">查询状态</td>
		<td bgcolor="#F4F4F4">操作</td>
	</tr>
	<c:forEach items="${downloadRequestList}" varStatus="i" var="downloadRequest">
	<tr class="downloadRequestTr"
		<c:if test="${tabId == '1' && ((!downloadRequest.branchTab && downloadRequest.custemerTab) || (downloadRequest.branchTab && downloadRequest.custemerTab))}">
			style="display: none"
		</c:if>
		<c:if test="${tabId == '2' && ((downloadRequest.branchTab && !downloadRequest.custemerTab) || (downloadRequest.branchTab && downloadRequest.custemerTab))}">
			style="display: none"
		</c:if>
		<c:if test="${tabId == '3' && ((!downloadRequest.branchTab && downloadRequest.custemerTab) || (downloadRequest.branchTab && !downloadRequest.custemerTab))}">
			style="display: none"
		</c:if>
	>
		<td align="center">妥投率查询</td>
		<td align="center">${downloadRequest.createtime}</td>
		<td>${downloadRequest.query}</td>
		<td>${downloadRequest.stateDesc}</td>
		<td>
			<c:if test="${downloadRequest.state != 10}">
				[<a target="_black" href="deliveryRateResult?downloadRequestId=${downloadRequest.id}&tabID=${tabId}">查看</a>]
			</c:if>
			[<a href="#" onclick="deleteDownloadRequest(${downloadRequest.id}
			, 
			<c:choose>
				<c:when test="${downloadRequest.branchTab && !downloadRequest.custemerTab}">1</c:when>
				<c:when test="${!downloadRequest.branchTab && downloadRequest.custemerTab}">2</c:when>
				<c:otherwise>3</c:otherwise>
			</c:choose>
			)">删除</a>]
		</td>
	</tr>
	</c:forEach>
</table>

