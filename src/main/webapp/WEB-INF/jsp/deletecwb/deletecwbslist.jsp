<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.DeleteCwbRecord"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerlist = request.getAttribute("customerlist")==null?null:(List<Customer>)request.getAttribute("customerlist");
List<User> userlist = request.getAttribute("userlist")==null?null:( List<User>)request.getAttribute("userlist");
List<DeleteCwbRecord> deleteCwbRecordlist = request.getAttribute("deleteCwbRecordlist")==null?null:(List<DeleteCwbRecord>)request.getAttribute("deleteCwbRecordlist");
Page page_obj = (Page)request.getAttribute("page_obj");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
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

function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	return true;
}
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
function formsub(){
	if($.trim($("#cwbs").val()).length==0){
		if($("#strtime").val()==""&&$("#endtime").val()==""){
			alert("请输入订单号或者选择时间");
			return false;
		}
		if(check()){
			$("#searchForm").submit();
		}
	}else{
		$("#searchForm").submit();
	}
}
</script>
</HEAD>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box"> 
<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath()%>/deletecwb/deletecwbs" >订单删除</a></li>
				<li><a href="<%=request.getContextPath()%>/deletecwb/deletecwbslist/1" class="light">订单删除查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
			<div class="kfsh_search">
				<form action="1" method="post" id="searchForm">
				订单号：
				<textarea name="cwbs" rows="3" id="cwbs" style="vertical-align:middle"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %></textarea>
				删除时间：
					<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
				到
					<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
				<input type="button" name="button" id="button" value="查询" onclick="formsub();"/>
				</form>
			</div>
			<%if(deleteCwbRecordlist!=null&&deleteCwbRecordlist.size()>0){ %>
			<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
					<tr>
						<td align="center">订单号</td>
						<td align="center">供货商</td>
						<td>删除时间</td>
						<td>当前状态</td>
						<td>操作人</td>
				</tr>
				<%for(DeleteCwbRecord deleteCwbRecord : deleteCwbRecordlist){ %>
					<tr>
						<td align="center"><%=deleteCwbRecord.getCwb() %></td>
						<td align="center"><%if(customerlist!=null&&customerlist.size()>0){for(Customer c : customerlist){if(c.getCustomerid()==deleteCwbRecord.getCustomerid()){ %><%=c.getCustomername() %><%break;}}} %></td>
						<td><%=deleteCwbRecord.getDeletetime() %></td>
						<td><%=FlowOrderTypeEnum.getText(deleteCwbRecord.getFlowordertype()).getText() %></td>
						<td><%if(userlist!=null&&userlist.size()>0){for(User u : userlist){if(u.getUserid()==deleteCwbRecord.getUserid()){ %><%=u.getRealname() %><%break;}}} %></td>
					</tr>
				<%} %>
			</table>
			<%} %>
			<div style="height:40px"></div>
			<%if(page_obj.getMaxpage()>1){ %>
			<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr>
						<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
							<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
							<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
							<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
							<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
							　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
									id="selectPg"
									onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
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
		</div>
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</HTML>
