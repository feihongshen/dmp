
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>财务数据迁移</title>
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
	
$(function() {
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
	
	
});

</script>
</head>

<body style="background:#f5f5f5">
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="jg_10"></div>
<div class="right_box">
	<form action="<%=request.getContextPath()%>/changeLog/accountNeedDatamoveByEmaildate" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
		<tr>
			<td align="left">
				发货时间
					<input type ="text" name ="startTime" id="strtime"  value="<%=request.getParameter("startTime")==null?"":request.getParameter("startTime")%>"/>
				到
					<input type ="text" name ="endTime" id="endtime"  value="<%=request.getParameter("endTime")==null?"":request.getParameter("endTime")%>"/>
					
				<br/>
				account路径：	<input type ="text" size=100 name ="url" value="<%=request.getParameter("url")==null?"":request.getParameter("url")%>"/>(例如：http://123.178.27.74/account7180)
				<br/>
				<%-- 操作：
				<select id="flowordertype" name="flowordertype">
					<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>" <%if((request.getParameter("flowordertype")==null?"":request.getParameter("flowordertype")).equals(FlowOrderTypeEnum.ChuKuSaoMiao.getValue())){ %>selected<%} %>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText()%></option>
					<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>,<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()%>"  <%if((request.getParameter("flowordertype")==null?"":request.getParameter("flowordertype")).equals(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())){ %>selected<%} %>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText()%></option>
				</select> --%>
				<input type="submit" id="find" onclick="" value="迁移数据" class="input_button2" />
			</td>
		</tr>
	</table>
	</form>
	
	 <br/>
	<form action="<%=request.getContextPath()%>/changeLog/accountNeedDataMoveBycwbs" method="post" >
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			订单号：<textarea cols="24" rows="3"  name ="cwbs" id="batchcwb" ></textarea>
			<br/>
			account路径：	<input type ="text" size=100 name ="url" value="<%=request.getParameter("url")==null?"":request.getParameter("url")%>"/>(例如：http://123.178.27.74/account7180)
			<br/>
			<%-- 操作：
			<select id="flowordertype" name="flowordertype">
				<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>" <%if((request.getParameter("flowordertype")==null?"":request.getParameter("flowordertype")).equals(FlowOrderTypeEnum.ChuKuSaoMiao.getValue())){ %>selected<%} %>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText()%></option>
				<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>,<%=FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()%>"  <%if((request.getParameter("flowordertype")==null?"":request.getParameter("flowordertype")).equals(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue())){ %>selected<%} %>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText()%></option>
			</select> --%>
			<input type="submit" id="find2" onclick="" value="迁移数据" class="input_button2" />
		</td>
	</tr>
    </table>
	</form>
	<br></br>
	<br></br>
	<br></br>
	
	<form action="<%=request.getContextPath()%>/changeLog/accountNeedDataChangestop" method="post" >
	<input type="submit"  value="停止迁移" class="input_button2" /></form>
	<%=request.getAttribute("msg") %> 
</div>
			
<div class="jg_10"></div>

<div class="clear"></div>

</body>
</html>

