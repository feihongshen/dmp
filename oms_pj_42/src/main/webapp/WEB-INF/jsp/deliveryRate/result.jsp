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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>

<head>
<div></div>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<%
	//设置缓存为空  
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<link
	href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js"
	type="text/javascript"></script>
<script language="javascript">
	$(function() {
		var $menuli = $(".cg_tabbtn ul li");
		var $menulilink = $(".cg_tabbtn ul li a");
		$menuli.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})
	$(function() {
		var $menuli = $(".uc_midbg ul li");
		var $menulilink = $(".uc_midbg ul li a");
		$menuli.click(function() {
			$(this).children().addClass("light");
			$(this).siblings().children().removeClass("light");
			var index = $menuli.index(this);
			$(".tabbox li").eq(index).show().siblings().hide();
		});
	})
	$(function() {
		$("#branchId").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		$("#customerId").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		$("#timeType").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		$("#timeType2").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});
		$("#startDate").datepicker();
		$("#endDate").datepicker();
		$("#startDate2").datepicker();
		$("#endDate2").datepicker();
	})
</script>

</head>
<script type="text/javascript">
	function download(id,tabId) {
		window.open("downloadDeliveryRateResult?downloadRequestId=" + id);
	}
</script>
<body style="background: #eef9ff" marginwidth="0" marginheight="0">
	<c:set var="queryType" value="${drAgg.queryType}" />
	<c:set var="timeTypeSize" value="${fn:length(drAgg.timeTypes)}" />
	<c:set var="bocSize" value="${fn:length(drAgg.branchOrCustomerAggMap)}" />
	<c:set var="dateSize" value="${fn:length(drAgg.allDate)}" />
	<c:set var="tableWidth" value="${dateSize * 300 + 560}" />
	<div class="right_box">
		<div class="inputselect_box" style="top: 0px;">
			<span><input name="button9" type="submit"
				class="input_button1" id="button9" value="导出EXCEL" onclick="download(${downloadRequestId})"> <input
				name="button9" type="submit" class="input_button1" id="button9"
				value="返回" onclick="window.close();"></span>
		</div>
		<input name = "tabID" type="hidden" value="${tabID}">
		<div class="right_title">
			<div class="jg_35"></div>
			<form name="form1" method="post" action="">
				<div style="width: 100%; overflow-x: scroll">
					<table border="0" cellspacing="1" cellpadding="4" class="table_2">
						<tbody>	
						    <c:if test="${tabID == 3}">	
						    <tr class="font_1">
						    <td></td><td></td>
						    <c:set var="array" value="${fn:length(drAgg.allDate)+1}" />
						    <c:set var="headmap" value="${drAgg.branchOrCustomerAggMap}" />
							<c:forEach items="${headmap}" varStatus="0" var="listmap" end="0">								
							<c:forEach items="${listmap.value}" varStatus="k" var="listmapvalue">
									<td colspan="${array*3+1}" bgcolor="#f4f4f4">${bocNameMap[listmapvalue.key]}</td>
							</c:forEach>
							</c:forEach>
						    </tr>
						    </c:if>
							<tr class="font_1">
								<td rowspan="2" valign="middle" bgcolor="#f4f4f4">${queryType.desc}</td>
								<c:choose>
									<c:when test="${drAgg.customization}">
										<td bgcolor="#f4f4f4">统计日期</td>
									</c:when>
									<c:otherwise>
										<td bgcolor="#f4f4f4">${queryType.title}</td>
									</c:otherwise>
								</c:choose>
								<c:set var="headmap" value="${drAgg.branchOrCustomerAggMap}" />
								<c:forEach items="${headmap}" varStatus="0" var="listmap" end="0">								
								<c:forEach items="${listmap.value}" varStatus="k" var="listmapvalue">
								<c:forEach items="${drAgg.allDate}" varStatus="i" var="date">
									<td colspan="3" bgcolor="#f4f4f4">${date}</td>
								</c:forEach>
								
								<td colspan="3" bgcolor="#f4f4f4">整体</td>
								<td rowspan="2" valign="middle" bgcolor="#f4f4f4">操作</td>
								</c:forEach>
								</c:forEach>
							</tr>
							<tr class="font_1">
								<td bgcolor="#f4f4f4">时效</td>
								<c:set var="headmap" value="${drAgg.branchOrCustomerAggMap}" />
								<c:forEach items="${headmap}" varStatus="0" var="listmap" end="0">
								<c:forEach items="${listmap.value}" varStatus="k" var="listmapvalue">
								<c:forEach items="${drAgg.allDate}" varStatus="i" var="date">
									<c:choose>
										<c:when test="${drAgg.customization}">
											<td bgcolor="#f4f4f4">统计单量</td>
											<td bgcolor="#f4f4f4">妥投单量</td>
											<td bgcolor="#f4f4f4">妥投率</td>
										</c:when>
										<c:otherwise>
											<td bgcolor="#f4f4f4">${queryType.title2}</td>
											<td bgcolor="#f4f4f4">时效内妥投量</td>
											<td bgcolor="#f4f4f4">时效内妥投率</td>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<c:choose>
									<c:when test="${drAgg.customization}">
										<td bgcolor="#f4f4f4">合计单量</td>
										<td bgcolor="#f4f4f4">合计妥投单量</td>
										<td bgcolor="#f4f4f4">整体妥投率</td>
									</c:when>
									<c:otherwise>
										<td bgcolor="#f4f4f4">${queryType.title2}</td>
										<td bgcolor="#f4f4f4">时效内妥投量合计</td>
										<td bgcolor="#f4f4f4">整体妥投率</td>
									</c:otherwise>
								</c:choose>
								</c:forEach>
								</c:forEach>
							</tr>

							<!-- 站点或物流商行 -->
							<c:set var="bocAggMap" value="${drAgg.branchOrCustomerAggMap}" />
							<c:forEach items="${bocAggMap}" varStatus="i" var="bocPair">
								<%-- <c:set var="dateAggMap" value="${bocPair.value.dateAggMap}" />
								<c:set var="date" value="${drDateAggMap.key}" />
								<c:set var="dateAggTotal" value="${bocPair.value.total}" /> --%>
								<c:choose>
									<c:when test="${i.index % 2 == '0'}">
										<c:set var="bgcolor" value="" />
									</c:when>
									<c:otherwise>
										<c:set var="bgcolor" value="#f4f4f4" />
									</c:otherwise>
								</c:choose>
								<c:forEach items="${drAgg.timeTypes}" varStatus="j"
									var="timeType">
									<tr>
										<c:if test="${j.index == '0'}">
											<!-- 站点或物流商名字 -->
											<td bgcolor="${bgcolor}" rowspan="${timeTypeSize}" valign="middle" class="font_1">${bocNameMap[bocPair.key]}</td>
										</c:if>

										<td valign="middle" bgcolor="${bgcolor}">${timeType.desc}</td>
										<c:forEach items="${bocPair.value}" varStatus="n" var="objectMap">
										<c:set var="objectlist" value="${objectMap.value}" />
										<c:set var="dateAggMap" value="${objectlist.dateAggMap}" />
								        <c:set var="date" value="${drDateAggMap.key}" />
								        <c:set var="dateAggTotal" value="${objectlist.total}" />
										<c:forEach items="${drAgg.allDate}" varStatus="k" var="date">
											<c:set var="dateAgg" value="${dateAggMap[date]}" />
											<c:if test="${j.index == '0'}">
												<c:choose>
													<c:when test="${dateAgg != null}">
														<td bgcolor="${bgcolor}" rowspan="${timeTypeSize}" valign="middle"><strong>${dateAgg.total}</strong></td>
													</c:when>
													<c:otherwise>
														<td bgcolor="${bgcolor}" rowspan="${timeTypeSize}" valign="middle"><strong>0</strong></td>
													</c:otherwise>
												</c:choose>
											</c:if>
											
											<c:choose>
												<c:when test="${dateAgg != null}">
													<td valign="middle" bgcolor="${bgcolor}"
														<c:if test="${dateAgg.timeTypeMap[timeType].highlight}">
															style="color:red"
														</c:if>
													>${dateAgg.timeTypeMap[timeType].count}</td>
													<td valign="middle" bgcolor="${bgcolor}"
														<c:if test="${dateAgg.timeTypeMap[timeType].highlight != null && dateAgg.timeTypeMap[timeType].highlight}">
														 	style="color:red"
														 </c:if>
													>${dateAgg.timeTypeMap[timeType].rate}</td>
												</c:when>
												<c:otherwise>
													<td valign="middle" bgcolor="${bgcolor}">0</td>
													<td valign="middle" bgcolor="${bgcolor}">0.0%</td>
												</c:otherwise>
											</c:choose>
										</c:forEach>

										<!-- 整体3列 -->
										<c:if test="${j.index == '0'}">
											<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}"><strong>${dateAggTotal.total}</strong></td>
										</c:if>
										<c:choose>
											<c:when test="${dateAggTotal.timeTypeMap[timeType] != null}">
												<td valign="middle" bgcolor="${bgcolor}" class="font_1"
													<c:if test="${dateAggTotal.timeTypeMap[timeType].highlight}">
													 	style="color:red"
													</c:if>
												>${dateAggTotal.timeTypeMap[timeType].count}</td>
												<td valign="middle" bgcolor="${bgcolor}" class="font_1"
													<c:if test="${dateAggTotal.timeTypeMap[timeType].highlight}">
													 	style="color:red"
													</c:if>
												>${dateAggTotal.timeTypeMap[timeType].rate}</td>
											</c:when>
											<c:otherwise>
												<td valign="middle" bgcolor="${bgcolor}" class="font_1">0</td>
												<td valign="middle" bgcolor="${bgcolor}" class="font_1">0.0%</td>
											</c:otherwise>
										</c:choose>

										<c:if test="${j.index == '0'}">
											<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}">
												<p style="word-wrap:normal">
													<input name="button" type="button" class="input_button2"
														id="button" value="查看柱形图" onclick="window.open('deliveryRateChart?type=column&downloadRequestId=${downloadRequestId}&bocId=${bocPair.key}&customerid=${objectMap.key}');">
												</p>
												<p>&nbsp;</p>
												<p style="word-wrap:normal">
													<input name="button2" type="button" class="input_button2"
														id="button2" value="查看折线图" onclick="window.open('deliveryRateChart?type=line&downloadRequestId=${downloadRequestId}&bocId=${bocPair.key}&customerid=${objectMap.key}');">
												</p></td>
										</c:if>
										</c:forEach>
									</tr>
								</c:forEach>
							</c:forEach>

							<c:choose>
								<c:when test="${bocSize % 2 == '0'}">
									<c:set var="bgcolor" value="" />
								</c:when>
								<c:otherwise>
									<c:set var="bgcolor" value="#f4f4f4" />
								</c:otherwise>
							</c:choose>
							<c:set var="bocAggTotalMap" value="${drAgg.total}" />
											<c:forTokens items="" delims=""></c:forTokens>	
							<c:forEach items="${drAgg.timeTypes}" varStatus="j"
								var="timeType">
								<tr class="font_1">
									<c:if test="${j.index == '0'}">
										<td rowspan="${timeTypeSize}" valign="middle"
											bgcolor="${bgcolor}">整体</td>
									</c:if>
									<td valign="middle" bgcolor="${bgcolor}">${timeType.desc}</td>									
									<c:set var="headmap" value="${drAgg.branchOrCustomerAggMap}" />
									<c:set var="equalfalg" value="0"/>
								    <c:forEach items="${headmap}" varStatus="0" var="listmap" end="0">
								    <c:forEach items="${listmap.value}" varStatus="k" var="listmapvalue">
									<%-- <c:forEach items="${bocAggTotalMap}" varStatus="p" var="bocAggPairTotal"> --%>
									<c:set var="bocAggTotal" value="${bocAggTotalMap[listmapvalue.key]}" />
									<%-- <c:set var="bocAggTotal" value="${bocAggPairTotal.value}" /> --%>
									<c:set var="dateAggTotal" value="${bocAggTotal.total}" />														    
									<c:forEach items="${drAgg.allDate}" varStatus="k" var="date">
									   
									    <c:set var="totalkey" value="${date}"></c:set>									    
										<c:set var="dateAgg" value="${bocAggTotal.dateAggMap[totalkey]}" />
										<c:if test="${j.index == '0'}">
											<c:choose>
												<c:when test="${dateAgg != null}">
													<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}"><strong>${dateAgg.total}</strong></td>
												</c:when>
												<c:otherwise>
													<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}"><strong>0</strong></td>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:choose>
											<c:when test="${dateAgg != null && dateAgg.total != 0}">
												<td valign="middle" bgcolor="${bgcolor}"
													<c:if test="${dateAgg.timeTypeMap[timeType].highlight}">
													 	style="color:red"
													</c:if>
												>${dateAgg.timeTypeMap[timeType].count}</td>
												<td valign="middle" bgcolor="${bgcolor}"
													<c:if test="${dateAgg.timeTypeMap[timeType].highlight}">
													 	style="color:red"
													</c:if>
												>${dateAgg.timeTypeMap[timeType].rate}</td>
											</c:when>
											<c:otherwise>
												<td valign="middle" bgcolor="${bgcolor}">0</td>
												<td valign="middle" bgcolor="${bgcolor}">0.0%</td>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:if test="${j.index == '0'}">
										<c:choose>
											<c:when test="${dateAggTotal != null}">
												<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}"><strong>${dateAggTotal.total}</strong></td>
											</c:when>
											<c:otherwise>
												<td rowspan="${timeTypeSize}" valign="middle" bgcolor="${bgcolor}"><strong>0</strong></td>
											</c:otherwise>
										</c:choose>
									</c:if>
									<c:choose>
										<c:when test="${dateAggTotal.timeTypeMap[timeType] != null}">
											<td valign="middle" bgcolor="${bgcolor}"
												<c:if test="${dateAggTotal.timeTypeMap[timeType].highlight}">
												 	style="color:red"
												</c:if>
											>${dateAggTotal.timeTypeMap[timeType].count}</td>
											<td valign="middle" bgcolor="${bgcolor}"
												<c:if test="${dateAggTotal.timeTypeMap[timeType].highlight}">
												 	style="color:red"
												</c:if>
											>${dateAggTotal.timeTypeMap[timeType].rate}</td>
										</c:when>
										<c:otherwise>
											<td valign="middle" bgcolor="${bgcolor}">0</td>
											<td valign="middle" bgcolor="${bgcolor}">0.0%</td>
										</c:otherwise>
									</c:choose>
										
									<c:if test="${j.index == '0'}">
										<td rowspan="${timeTypeSize}" valign="middle"
											 bgcolor="${bgcolor}">
											<p style="word-wrap:normal">
												<input name="button10" type="button" class="input_button2"
													id="button5" value="查看柱形图" onclick="window.open('deliveryRateChart?type=column&downloadRequestId=${downloadRequestId}&bocId=total&customerid=${listmapvalue.key}');">
											</p>
											<p>&nbsp;</p>
											<p style="word-wrap:normal">
												<input name="button10" type="button" class="input_button2"
													id="button5" value="查看折线图" onclick="window.open('deliveryRateChart?type=line&downloadRequestId=${downloadRequestId}&bocId=total&customerid=${listmapvalue.key}');">
											</p></td>
									</c:if>	
									</c:forEach>
									</c:forEach>
								</tr>
							</c:forEach>
							<tr>
								<td valign="middle" bgcolor="${bgcolor}">&nbsp;</td>
								<td bgcolor="${bgcolor}">&nbsp;</td>
								<c:set var="headmap" value="${drAgg.branchOrCustomerAggMap}" />
								<c:forEach items="${headmap}" varStatus="0" var="listmap" end="0">
								<c:forEach items="${listmap.value}" varStatus="k" var="listmapvalue">
								<c:forEach begin="0" end="${dateSize}" step="1" varStatus="i">
									<td valign="middle" bgcolor="${bgcolor}">&nbsp;</td>
									<td bgcolor="${bgcolor}">&nbsp;</td>
									<td bgcolor="${bgcolor}"><input name="button3" type="botton"
										class="input_button2" id="button10" value="查看图表"  onclick="window.open('deliveryRateChart?type=bar&downloadRequestId=${downloadRequestId}&dateSeq=${i.index}&customerid=${listmapvalue.key}');"></td>
								</c:forEach>
								
								<td bgcolor="${bgcolor}">&nbsp;</td>
								</c:forEach>
								
									</c:forEach>
							</tr>
						</tbody>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>

