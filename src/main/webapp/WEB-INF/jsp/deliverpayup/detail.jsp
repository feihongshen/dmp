<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.*"%>
<%
	List<Map<String, Object>> rowData = (List<Map<String, Object>>) request.getAttribute("rowData");
	List<Exportmould> exportmouldlist = (List<Exportmould>) request.getAttribute("exportmouldlist");
	String gcaid = request.getAttribute("gcaid").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

</head>
<body style="background: #eef9ff" marginwidth="0" marginheight="0">
	<div>
		<form action='<%=request.getContextPath() + "/deliverpayup/exportDetail?gcaid=" + gcaid%>'
			method="post" id="searchForm2" style="padding: 10px">
			<input type="submit" id="btnval0" value="导出Excel" class="button" />
		</form>
	</div>
	<table width="100%" border="0" cellspacing="10" cellpadding="0">
		<tbody>
			<tr>
				<td width="10%" height="26" align="left" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5">
						<tr>
							<td width="120" align="center" bgcolor="#f1f1f1">订单号</td>
							<td width="100" align="center" bgcolor="#f1f1f1">小件员</td>
							<td width="100" align="center" bgcolor="#f1f1f1">交款时间</td>
							<td width="140" align="center" bgcolor="#f1f1f1">交款方式</td>
							<td width="100" align="center" bgcolor="#f1f1f1">应收款</td>
							<td width="100" align="center" bgcolor="#f1f1f1">实收款</td>
							<td width="100" align="center" bgcolor="#f1f1f1">审核状态</td>
							<td width="100" align="center" bgcolor="#f1f1f1">审核备注</td>
						</tr>
					</table>

					<div style="height: 500px; overflow-y: autoScroll">
						<table id="weirukuTable" width="100%" border="0" cellspacing="1" cellpadding="2"
							class="table_2">
							<%
								for (Map<String, Object> row : rowData) {
							%>
							<tr>
								<td width="120" align="center" bgcolor="#f1f1f1"><%=row.get("cwb")%></td>
								<td width="100" align="center" bgcolor="#f1f1f1"><%=row.get("deliver")%></td>
								<td width="100" align="center" bgcolor="#f1f1f1"><%=row.get("payupTime")%></td>
								<td width="140" align="center" bgcolor="#f1f1f1"><%=row.get("payupType")%></td>
								<td width="100" align="right" bgcolor="#f1f1f1"><%=row.get("businessFee")%></td>
								<td width="100" align="right" bgcolor="#f1f1f1"><%=row.get("receivedFee")%></td>
								<td width="100" align="center" bgcolor="#f1f1f1"><%=row.get("auditState")%></td>
								<td width="100" align="center" bgcolor="#f1f1f1"><%=row.get("auditRemark")%></td>
							</tr>
							<%
								}
							%>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>

</body>
</html>
