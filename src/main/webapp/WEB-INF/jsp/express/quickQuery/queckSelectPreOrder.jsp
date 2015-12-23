<%@page import="cn.explink.domain.VO.express.ExpressQuickQueryView"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
String preOrder = request.getAttribute("preOrder")==null?"":(String)request.getAttribute("preOrder");
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/kuaijie.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
</HEAD>

<body onLoad="$(&#39;#orderSearch&#39;).focus();" marginwidth="0" marginheight="0">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" style="font-size:14">
		<tbody>
		<tr>
			<td valign="top" ><iframe src="<%=request.getContextPath()%>/preOrderSelect/queckSelectPreOrderLeft/<%=preOrder %>" width="100%" height="920"  id="queckSelectOrder_AREA_LEFT"></iframe>
			</td>
			<!-- <td valign="top" width="50%" >
			<iframe src="" width="100%" height="920"  id="queckSelectOrder_AREA_RIGHT"></iframe>
			</td> -->
		</tr>
		</tbody>
	</table>
</body>
</html>