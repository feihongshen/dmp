
<%@page import="cn.explink.enumutil.ChangeFunctionidEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上门退订单迁移</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
	
/* $(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	
}); */

</script>
</head>

<body style="background:#f5f5f5">
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="right_box">
	<form action="<%=request.getContextPath()%>/changeLog/shangmentuiDataMoveByEmaildateid" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			发货时间id：
				<%-- <input type ="text" name ="startTime" id="strtime"  value="<%=request.getParameter("startTime")==null?"":request.getParameter("startTime")%>"/> --%>
				<input type ="text" name ="startTimeid" id="strtimeid"  value="<%=request.getParameter("startTimeid")==null?"":request.getParameter("startTimeid")%>"/>
			到
				<%-- <input type ="text" name ="endTime" id="endtime"  value="<%=request.getParameter("endTime")==null?"":request.getParameter("endTime")%>"/> --%>
				<input type ="text" name ="endTimeid" id="endtimeid"  value="<%=request.getParameter("endTimeid")==null?"":request.getParameter("endTimeid")%>"/>
				
			<input type="submit" id="find" onclick="" value="迁移数据" class="input_button2" />
		</td>
	</tr>
</table>
	</form>
	
	 </br></br></br></br></br></br>
	<form action="<%=request.getContextPath()%>/changeLog/shangmentuiDataMoveByCwbs" method="post" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			订单号：<textarea cols="24" rows="3"  name ="cwbs" id="batchcwb" ></textarea>
			<input type="submit" id="find2" onclick="" value="迁移数据" class="input_button2" />
		</td>
	</tr>
    </table>
	</form>
	
	
	
	</br></br>
	</br></br>
	</br></br>
	
	<form action="<%=request.getContextPath()%>/changeLog/stop" method="post" >
	<input type="submit"  value="停止迁移" class="input_button2" /></form>
	<%=request.getAttribute("msg") %> 
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>



</body>
</html>

