<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.AccountFeeDetail"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<AccountFeeDetail> detailList=request.getAttribute("feeDetailList")==null?null:(List<AccountFeeDetail>)request.getAttribute("feeDetailList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>加减款明细</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">

</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page")%>" method="post" id="searchForm">
	</form>
</div>
<div style="height:35px"></div>
<form action="" method="post">
	<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr class="font_1">
			<td bgcolor="#f4f4f4">类型名称</td>
			<td bgcolor="#f4f4f4">金额</td>
			<td bgcolor="#f4f4f4">备注</td>
			<td bgcolor="#f4f4f4">操作人</td>
			<td bgcolor="#f4f4f4">操作时间</td>
		</tr>
		<%if(detailList!=null&&!detailList.isEmpty()){
			for(int i=0;i<detailList.size();i++){
				AccountFeeDetail list=detailList.get(i);
		%>
		<tr>
			<td><%=list.getFeetypename()%></td>
			<td align="right"><strong><%=list.getCustomfee()%></strong></td>
			<td><%=list.getDetailname()%></td>
			<td><%=list.getUsername()%></td>
			<td><%=list.getCreatetime()%></td>
		</tr>
		<%}} %>
	</table>
	
	
</form>
</body>
</html>