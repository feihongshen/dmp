

<%@page import="cn.explink.b2c.tools.*,cn.explink.util.DateTimeUtil" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>新疆大晨报手机App测试平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>


<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");
String username = request.getAttribute("username")==null?"":(String)request.getAttribute("username");
String delivermobile = request.getAttribute("delivermobile")==null?"":(String)request.getAttribute("delivermobile");

%>
 

</head>
<body style="background:#f5f5f5">
<div class="menucontant">

	<form action="<%=request.getContextPath()%>/mobiledcb/feedback_test"  method="post" id="searchForm">
	<table >
		<tr>
		<td>参数cwb=</td>
		<td align="left"><input type="text" name="cwb" value=""/>*订单号</td>
		</tr>
		<tr>
		<td>参数delivermobile=</td>
		<td  align="left"><input type="text" name="delivermobile" value="<%=delivermobile%>"/>*小件员手机号</td>
		</tr>
		<tr>
		<td>参数username=</td>
		<td  align="left"><input type="text" name="username" value="<%=username%>"/>*登录名</td>
		</tr>
		<tr>
		<td>参数podresultid=</td>
		<td  align="left">
		<select name="podresultid">
			<option value="9">配送成功</option>
			<option value="2">拒收</option>
			<option value="1">滞留</option>
		</select>
		*配送结果</td>
		</tr>
		<tr>
		<td>参数backreasonid=</td>
		<td  align="left"><input type="text" name="backreasonid" value="0"/>*拒收原因码，见字典</td>
		</tr>
		<tr>
		<td>参数leavedreasonid=</td>
		<td  align="left"><input type="text" name="leavedreasonid" value="0"/>*滞留原因码，见字典</td>
		</tr>
		<tr>
		<td>参数receivedfeecash=</td>
		<td  align="left"><input type="text" name="receivedfeecash" value="0"/>*现金实收</td>
		</tr>
		<tr><td>参数receivedfeepos=</td>
		<td><input type="text" name="receivedfeepos" value="0"/>*POS实收</td>
		</tr>
		<tr><td>参数receivedfeecheque=</td>
		<td  align="left"><input type="text" name="receivedfeecheque" value="0"/>*支票实收</td>
		</tr>
		<tr><td>参数remark=</td>
		<td  align="left"><input type="text" name="remark" value="备注"/></td>
		</tr>
		
		<tr><td>参数operatedate=</td>
		<td  align="left"><input type="text" name="operatedate" value="<%=DateTimeUtil.getNowTime()%>"/></td>
		</tr>
		<tr>
		<td colspan="2"><input type="submit" /><br>
		返回的结果：<textarea rows="10" cols="50"><%=values%></textarea>
		</td>
		</tr>
		
		
		</table>
	</form>
	
</div>

</body>
</html>
