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
	CombineQueryView combineQueryView = (CombineQueryView)request.getAttribute("combineQueryView");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>合包查询</title>
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

	function queryCombineInfo(packageNo) {
		$('#searchForm').attr("action", $("#combineQuery").val());
		$('#searchForm').submit();
	};
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="inputselect_box">
		<form method="post" id="searchForm" action="1">
			<table width="80%">
				<tr>
					<td>包号<input type="text" id="packageNo" name="packageNo"
						style="height: 30px; font-size: x-large; font-weight: bold;"
						onKeyDown='if(event.keyCode==13&&$(this).val().length>0){queryCombineInfo($(this).val());}' />
					</td>
					<td>运单总数<span style="font-size: x-large; font-weight: bold;"><%=combineQueryView == null ? 0 : combineQueryView.getWaybillTotalCount()%></span>
					</td>
					<td>总件数<span style="font-size: x-large; font-weight: bold;"><%=combineQueryView == null ? 0 : combineQueryView.getItemTotalCount()%></span>
					</td>
					<td>费用合计<span style="font-size: x-large; font-weight: bold;"><%=combineQueryView == null ? 0 : combineQueryView.getFeeTotalNum()%></span>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div class="saomiao_box2">
		<div style="overflow: scroll;">
			<table border="0" cellspacing="10" cellpadding="0" style="margin-top: 70px">
				<tbody>
					<tr>
						<td height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" style="white-space:nowrap">
								<tr style="font-weight: bold;">
									<td width="110" align="center" bgcolor="#f1f1f1">运单号</td>
									<td width="160" align="center" bgcolor="#f1f1f1">揽件入站时间</td>
									<td width="50" align="center" bgcolor="#f1f1f1">件数</td>
									<td width="50" align="center" bgcolor="#f1f1f1">小件员</td>
									<td width="60" align="center" bgcolor="#f1f1f1">付款方式</td>
									<td width="60" align="center" bgcolor="#f1f1f1">费用合计</td>
									<td width="50" align="center" bgcolor="#f1f1f1">运费</td>
									<td width="60" align="center" bgcolor="#f1f1f1">包装费用</td>
									<td width="60" align="center" bgcolor="#f1f1f1">保价费用</td>


									<td width="50" align="center" bgcolor="#f1f1f1">寄件人</td>
									<td width="60" align="center" bgcolor="#f1f1f1">寄件公司</td>
									<td width="60" align="center" bgcolor="#f1f1f1">寄件省份</td>
									<td width="60" align="center" bgcolor="#f1f1f1">寄件城市</td>
									<td width="70" align="center" bgcolor="#f1f1f1">寄件人手机</td>
									<td width="70" align="center" bgcolor="#f1f1f1">寄件人固话</td>

									<td width="50" align="center" bgcolor="#f1f1f1">收件人</td>
									<td width="60" align="center" bgcolor="#f1f1f1">收件公司</td>
									<td width="60" align="center" bgcolor="#f1f1f1">收件省份</td>
									<td width="60" align="center" bgcolor="#f1f1f1">收件城市</td>
									<td width="70" align="center" bgcolor="#f1f1f1">收件人手机</td>
									<td width="70" align="center" bgcolor="#f1f1f1">收件人固话</td>

									<td width="50" align="center" bgcolor="#f1f1f1">拖物内容名称</td>
								</tr>
								<%
									if (combineQueryView != null && combineQueryView.getCwbOrderList() != null && !combineQueryView.getCwbOrderList().isEmpty())
										for (CwbOrderForCombineQuery cwbOrder : combineQueryView.getCwbOrderList()) {
								%>
								
								<tr style="background:#fff">
									<td width="110" align="center"><%=cwbOrder.getCwb()%></td>
									<td width="160"><%=cwbOrder.getInstationdatetime() == null ? "" : cwbOrder.getInstationdatetime()%></td>
									<td width="50"><%=cwbOrder.getSendcarnum()%></td>
									<td width="50"><%=cwbOrder.getCollectorname() == null ? "" : cwbOrder.getCollectorname()%></td>
									<td width="60"><%=cwbOrder.getPayMethodName()%></td>
									<td width="60"><%=cwbOrder.getTotalfee() == null ? "" : cwbOrder.getTotalfee()%></td>
									<td width="50"><%=cwbOrder.getShouldfare() == null ? "" : cwbOrder.getShouldfare()%></td>
									<td width="60"><%=cwbOrder.getPackagefee() == null ? "" : cwbOrder.getPackagefee()%></td>
									<td width="60"><%=cwbOrder.getInsuredfee() == null ? "" : cwbOrder.getInsuredfee()%></td>

									<!-- 寄件人相关 -->
									<td width="50"><%=cwbOrder.getSendername() == null ? "" : cwbOrder.getSendername()%></td>
									<td width="60"><%=cwbOrder.getSenderCompanyName() == null ? "" : cwbOrder.getSenderCompanyName()%></td>
									<td width="60"><%=cwbOrder.getSenderprovince() == null ? "" : cwbOrder.getSenderprovince()%></td>
									<td width="60"><%=cwbOrder.getSendercity() == null ? "" : cwbOrder.getSendercity()%></td>
									<td width="70"><%=cwbOrder.getSendercellphone() == null ? "" : cwbOrder.getSendercellphone()%></td>
									<td width="70"><%=cwbOrder.getSendertelephone() == null ? "" : cwbOrder.getSendertelephone()%></td>

									<!-- 收件人相关 -->
									<td width="50"><%=cwbOrder.getConsigneename() == null ? "" : cwbOrder.getConsigneename()%></td>
									<td width="60"><%=cwbOrder.getReceiverCompany() == null ? "" : cwbOrder.getReceiverCompany()%></td>
									<td width="60"><%=cwbOrder.getReceiveProvinceName() == null ? "" : cwbOrder.getReceiveProvinceName()%></td>
									<td width="60"><%=cwbOrder.getReceiveCityName() == null ? "" : cwbOrder.getReceiveCityName()%></td>
									<td width="70"><%=cwbOrder.getConsigneemobile() == null ? "" : cwbOrder.getConsigneemobile()%></td>
									<td width="70"><%=cwbOrder.getConsigneephone() == null ? "" : cwbOrder.getConsigneephone()%></td>

									<td width="50"><%=cwbOrder.getEntrustname()==null?"":cwbOrder.getEntrustname()%></td>
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
	<!-- 查询 -->
	<input type="hidden" id="combineQuery"
		value="<%=request.getContextPath()%>/stationOperation/combineQuery/1?" />
</body>
</html>