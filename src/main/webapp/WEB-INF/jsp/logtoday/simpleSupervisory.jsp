<%@page import="java.math.BigDecimal"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderListTypeEnum"%>
<%
	List<Branch> bList = (List<Branch>) request.getAttribute("branchList");
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点信息监控</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
</head>
<body style="background: #eef9ff">
	<div class="right_box">
		<div class="inputselect_box">
			站点： <select name="branchid" id="branid" onchange="location.href='simpleSupervisory?branchid='+$(this).val();">
			<%for(Branch b : bList){ %>
				<option value="<%=b.getBranchid() %>" <%=(Long)request.getAttribute("branchid")==b.getBranchid()?"selected":"" %>><%=b.getBranchname() %></option>
			<%} %>
			</select>
		</div>
		<div class="right_title">
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div class="jg_10"></div>
			<div >
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					class="table_2" id="gd_table">
					<tr class="font_1">
						<td colspan="3"  align="center" valign="middle" bgcolor="#eef6ff">应到货在途</td>
						<td colspan="3" align="center" valign="middle" bgcolor="#eef6ff">配送中</td>
						<td colspan="3" align="center" valign="middle" bgcolor="#eef6ff">库存</td>

					</tr>
					<tr>
						<td>单数</td>
						<td>应收金额</td>
						<td>应退金额</td>
						<td>单数</td>
						<td>应收金额</td>
						<td>应退金额</td>
						<td>单数</td>
						<td>应收金额</td>
						<td>应退金额</td>
					</tr>
					<tr>
						<td><a
							href="<%=request.getContextPath()%>/cwborder/detaillist/<%=CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_ZAI_TU.getValue() %>/1?branchid=${branchid}">${zaitu.num}</a>
						</td>
						<td align ="right">${zaitu.receivablefee}</td>
						<td align ="right">${zaitu.paybackfee}</td>
						<td><a
							href="<%=request.getContextPath()%>/cwborder/detaillist/<%=CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_PAI_SONG_ZHONG.getValue() %>/1?branchid=${branchid}">${paisongzhong.num}</a>
						</td>
						<td align ="right">${paisongzhong.receivablefee }</td>
						<td align ="right">${paisongzhong.paybackfee }</td>
						<td><a
							href="<%=request.getContextPath()%>/cwborder/detaillist/<%=CwbOrderListTypeEnum.ZHAN_DIAN_JIAN_KONG_KU_CUN.getValue() %>/1?branchid=${branchid}">${kucun.num}</a>
						</td>
						<td align ="right">${kucun.receivablefee}</td>
						<td align ="right">${kucun.paybackfee}</td>
					</tr>
				</table>
			</div>

			<div class="jg_10"></div>
			<div class="jg_10"></div>
		</div>
		<div class="jg_10"></div>
		<div class="clear"></div>
	</div>
</body>
</html>


