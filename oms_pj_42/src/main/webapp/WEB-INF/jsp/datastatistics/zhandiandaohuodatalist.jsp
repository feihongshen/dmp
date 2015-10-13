<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.User,cn.explink.domain.Customer"%>
<%@page import="java.math.BigDecimal" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String starttime=request.getParameter("begindate")==null?DateTimeUtil.getNowDate()+" 00:00:00":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowDate()+" 23:59:59":request.getParameter("enddate");
List<Branch> zhandianlist = request.getAttribute("zhandianlist")==null?null:(List<Branch>)request.getAttribute("zhandianlist");

Map<Long,Long> branchMap = request.getAttribute("branchMap")==null?null:(Map<Long,Long>)request.getAttribute("branchMap");

long width = zhandianlist.size()>0?zhandianlist.size()*200:0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
function gologin(){
	if(<%=request.getAttribute("nouser") != null%>){
		alert("登录已失效，请重新登录！");
	}
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


function checkParam(){
	if($("#strtime").val()=='' || $("#endtime").val() ==''){
		alert("请选择时间！");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	
	return true;
}
function findsubmit(){
	if(checkParam()){
		$("#findForm").submit();
	}
}
function sub(branchid){
	$('#branchid').val(branchid);
	$('#show').submit();
}
</script>
</head>
<body style="background: #eef9ff" onload="gologin();" >
<div class="menucontant">
	<div class="form_topbg" style="height:40px">
		<form action="<%=request.getContextPath() %>/datastatistics/zhandiandaohuodata" method="post" id="findForm" >
		<input type="hidden" id="isshow" name="isshow" value="1" />
		发货时间
			<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
		到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
		 <input type="button" id="find" onclick="findsubmit();" value="查询" class="input_button2" />
		&nbsp;&nbsp;数据最新更新时间：${lastupdatetime }
		</form>
		</div>
		<div class="right_title">
		<%if(zhandianlist!=null&&zhandianlist.size()>0&&!branchMap.isEmpty()){%>
		<div style="height:5px"></div>
		<div style="position:relative; z-index:0; width:100% ">
			<div style="overflow-x:scroll; width:100%;">
			<table width="<%=width %>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tbody>
				<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
				<%if(zhandianlist!=null&&zhandianlist.size()>0&&!branchMap.isEmpty()){for(Branch b : zhandianlist){%>
					<td align="center" valign="middle" bgcolor="#f8f8f8" ><%=b.getBranchname() %></td>
				<%}} %>
				</tr>
				<tr  class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
				<%if(zhandianlist!=null&&zhandianlist.size()>0&&!branchMap.isEmpty()){for(Branch b : zhandianlist){%>
					<td align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=b.getBranchid() %>);"><%=branchMap.get(b.getBranchid()) %></a></strong></td>
				<%}} %>
				</tr>
			</tbody>
		</table></div>
		</div>
		<%} %>
	</div>
	<form id="show" action="<%=request.getContextPath() %>/datastatistics/zhandiandaohuodatashow/1" method="post">
		<input name="branchid" id="branchid" value="0" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
	</form>
</div>
</body>
</html>
   
