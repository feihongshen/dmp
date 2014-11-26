<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.SmsSendManageEnum"%>
<%@page import="cn.explink.domain.SmsSend"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<SmsSend> smsSendList = (List<SmsSend>)request.getAttribute("SendSmsList");
	Map<Long,User> userMap = (Map<Long,User>)request.getAttribute("userMap");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户发货统计</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#startSenddate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#stopSenddate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
});

	//两个日期的差值(d1 - d2 <limit).
	function DateDiff(d1, d2,limit) {
		var day = 24 * 60 * 60 * 1000;
		try {
			var dateArr = d1.substring(0, 10).split("-");
			var checkDate = new Date();
			checkDate.setFullYear(dateArr[0], dateArr[1] - 1, dateArr[2]);
			var checkTime = checkDate.getTime();

			var dateArr2 = d2.substring(0, 10).split("-");
			var checkDate2 = new Date();
			checkDate2.setFullYear(dateArr2[0], dateArr2[1] - 1, dateArr2[2]);
			var checkTime2 = checkDate2.getTime();

			var cha = (checkTime - checkTime2) / day;
			if(cha<=limit){
				return true;
			}else{
				alert("时间段不能超过"+limit+"天");
				return false;
			}
		} catch (e) {
			alert("时间格式不正确！");
			return false;
		}
	}//end fun
	function searchFrom() {
		$("#searchForm").attr("action","sendList");
		if($("#consigneemobile").val().length>0){
			$("#searchForm").submit();
		}else if(DateDiff($("#stopSenddate").val(),$("#startSenddate").val(),30) ){
			$("#searchForm").submit();
		}
		return false;
	}
	
	function exportFrom() {
		$("#searchForm").attr("action","excelSmsSendList");
		if($("#consigneemobile").val().length>0){
			$("#searchForm").submit();
		}else if(DateDiff($("#stopSenddate").val(),$("#startSenddate").val(),30) ){
			$("#searchForm").submit();
		}
		return false;
	}
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="sendList" method="post" id="searchForm" >
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			<input type="hidden" id="page" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
			发送时间：<input type ="text" name ="startSenddate" id="startSenddate"  value="<%=request.getAttribute("startSenddate")==null?"":(String)request.getAttribute("startSenddate") %>"/>
			到 <input type ="text" name ="stopSenddate" id="stopSenddate"  value="<%=request.getAttribute("stopSenddate")==null?"":(String)request.getAttribute("stopSenddate")  %>"/>
		 	手机号：<input type ="text" name ="consigneemobile" id="consigneemobile"  value="<%=request.getAttribute("consigneemobile")==null?"":(String)request.getAttribute("consigneemobile")  %>"/>
		    订单类型
			<select name ="sendstate" id ="sendstate" >
				<option value ="-1" selected="selected">全部</option>
				<option value ="0" >成功</option>
				<option value ="1" >失败</option>
				<option value ="2" >发送中</option>
			</select>
			<input type="button" id="find" onclick="$('#page').val('1');searchFrom()" value="查询" class="input_button2" />
			<input type="button" id="export" onclick="exportFrom()" value="导出" class="input_button2" />
		</td>
	</tr>
</table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:60px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1200" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1" >
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发送时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收信人姓名</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收信人电话</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发送内容</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >分条</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发送状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >失败原因</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发送人用户</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发送人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >IP</td>
				
		</tr>
		<% for(SmsSend  sms : smsSendList){ %>
				<tr bgcolor="#FF3300">
					<td height="60px" align="center" valign="middle"><%=sms.getSenddate() %></td>
					<td  align="center" valign="middle"><%=sms.getRecipients()  %></td>
					<td  align="center" valign="middle"><%=sms.getConsigneemobile() %></td>
					<td width="200px" align="center" valign="middle"><%=sms.getSenddetail() %></td>
					<td  align="center" valign="middle"><%=sms.getNum() %></td>
					<td  align="center" valign="middle"><%=SmsSendManageEnum.getText(sms.getSendstate()).getText() %></td>
					<td width="200px"  align="center" valign="middle"><%=sms.getErrorDetail() %></td>
					<td  align="center" valign="middle"><%=sms.getUserid()>-1?userMap.get(sms.getUserid()).getUsername():"系统自动发送" %></td>
					<td  align="center" valign="middle"><%=sms.getUserid()>-1?userMap.get(sms.getUserid()).getRealname():"系统自动发送" %></td>
					<td  align="center" valign="middle"><%=sms.getIp() %></td>
				 </tr>
		 <%} %>
	</table>
		
	</div>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#page').val('1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#page').val('<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#page').val('<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#page').val('<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#page').val($(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div>
    <%} %>
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>

