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
List<Branch> kufanglist = request.getAttribute("kufanglist")==null?null:(List<Branch>)request.getAttribute("kufanglist");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");

Map<Long,Long> customMap = request.getAttribute("customMap")==null?null:(Map<Long,Long>)request.getAttribute("customMap");

long width = customerList.size()>0?customerList.size()*200:0;
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
	
	return true;
}

function sub(customerid){
	$('#customerid').val(customerid);
	$('#show').submit();
}
</script>
</head>
<body style="background: #f5f5f5" >
<div class="menucontant">
	<div class="form_topbg" style="height:40px">
		<form action="<%=request.getContextPath() %>/datastatistics/customerfahuodata" method="post" onSubmit="if(checkParam()){submitSaveFormAndCloseBox('$(this)');}return false;">
		<input type="hidden" id="isshow" name="isshow" value="1" />
		发货时间
			<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
		到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
	   	 发货库房
		<select name ="kufangid" id ="kufangid">
			<option value="0">请选择</option>
	          <%if(kufanglist!=null && kufanglist.size()>0) {%>
	          <%for(Branch b : kufanglist){ %>
		 		<option value ="<%=b.getBranchid() %>" 
					<%if(b.getBranchid()==(request.getParameter("kufangid")==null?0:Long.parseLong(request.getParameter("kufangid")))){%>selected="selected"<%}%>><%=b.getBranchname()%>
				</option>
	          <%}}%>
		</select>
		 <input type="submit" id="find" onClick="" value="查询" class="input_button2" />
		</form>
		</div>
		<div class="right_title">
		<%if(customerList!=null&&customerList.size()>0&&!customMap.isEmpty()){%>
		<div style="height:5px"></div>
		<div style="position:relative; z-index:0; width:100% ">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:30px">
				<tbody>
					<tr class="font_1" style="background-color: rgb(255, 255, 255); ">
						<td align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
					</tr>
				</tbody>
			</table>
			<div style="overflow-x:scroll; width:100%;">
			<table width="<%=width %>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tbody>
				<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
				<%if(customerList!=null&&customerList.size()>0&&!customMap.isEmpty()){for(Customer cus : customerList){%>
					<td align="center" valign="middle" bgcolor="#f8f8f8" ><%=cus.getCustomername() %></td>
				<%}} %>
				</tr>
				<tr  class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
				<%if(customerList!=null&&customerList.size()>0&&!customMap.isEmpty()){for(Customer cus : customerList){%>
					<td align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=cus.getCustomerid() %>);"><%=customMap.get(cus.getCustomerid()) %></a></strong></td>
				<%}} %>
				</tr>
			</tbody>
		</table></div>
		</div>
		<%} %>
	</div>
	<form id="show" action="<%=request.getContextPath() %>/datastatistics/customerfahuodatashow/1" method="post">
		<input name="customerid" id="customerid" value="0" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
		<input name="kufangid" id="kufangid" value="<%=request.getParameter("kufangid")==null?"0":request.getParameter("kufangid") %>" type="hidden"/>
	</form>
</div>
</body>
</html>
   
