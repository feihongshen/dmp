<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.TransCwbStateEnum"%>
<%@page import="cn.explink.domain.TransCwbStateControl"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <% Map<Integer,List<TransCwbStateControl>> mlist=(Map<Integer,List<TransCwbStateControl>>)request.getAttribute("Mlist"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运单状态设置</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/index.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

</script>
</head>
<body style="background:#f5f5f5" >
<div class="right_box">
<div class="inputselect_box"></div>
<div class="right_title"></div>
<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
<div>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
<tr class="font_1"><th width="15%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</th>
	<th width="70%" align="center" valign="middle" bgcolor="#eef6ff">可操作的环节</th>
	<th width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</th></tr>
<% for(TransCwbStateEnum em:TransCwbStateEnum.values()){ %>
<tr>
<td width="15%" align="center" valign="middle" ><%=em.getText() %></td>
<td width="70%" align="center" valign="middle"><%for(TransCwbStateControl tsc:mlist.get(em.getValue())){
	for(FlowOrderTypeEnum fte:FlowOrderTypeEnum.values()){
		if(tsc.getToflowtype()==fte.getValue()){
			%><%=fte.getText()+" | "%><% }
		
	}
} %></td>
<td width="10%" align="center" valign="middle"><a href="<%=request.getContextPath() %>/transCwbStateControl/edit/<%=em.getValue()%>" >[修改]</a></td>
</tr>
<%} %>
</table>
</div>

</div>
</body>
</html>