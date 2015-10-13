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
List<Branch> kufangList = request.getAttribute("kufangList")==null?null:(List<Branch>)request.getAttribute("kufangList");
List<Branch> branchList = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
List<Customer> customerlist = request.getAttribute("customerlist")==null?null:( List<Customer>)request.getAttribute("customerlist");


Map<String, Long> customerMap = request.getAttribute("customerMap")==null?null:(Map<String, Long>)request.getAttribute("customerMap");
Map<Long, Long> customerAllMap = request.getAttribute("customerAllMap")==null?null:(Map<Long, Long>)request.getAttribute("customerAllMap");
Map<Long, Long> branchAllMap = request.getAttribute("branchAllMap")==null?null:(Map<Long, Long>)request.getAttribute("branchAllMap");


long width = customerlist.size()>0?customerlist.size()*100:0;
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
	
	return true;
}

function sub(nextbranchid,customerid){
	$('#nextbranchid').val(nextbranchid);
	$('#customerid').val(customerid);
	$('#show').submit();
}
</script>
</head>
<body style="background: #eef9ff"  onload="gologin();">
<div class="menucontant">
	<div class="form_topbg" style="height:40px">
		<form action="<%=request.getContextPath() %>/datastatistics/outwarehousecollectdata" method="post" onSubmit="if(checkParam()){submitSaveFormAndCloseBox('$(this)');}return false;">
		<input type="hidden" id="isshow" name="isshow" value="1" />
		发货时间
			<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
		到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
	   	 发货库房
		<select name ="kufangid" id ="kufangid">
	          <%if(kufangList!=null && kufangList.size()>0) {%>
	          <%for(Branch b : kufangList){ %>
		 		<option value ="<%=b.getBranchid() %>" 
					<%if(b.getBranchid()==(request.getParameter("kufangid")==null?0:Long.parseLong(request.getParameter("kufangid")))){%>selected="selected"<%}%>><%=b.getBranchname()%>
				</option>
	          <%}}%>
		</select>
		 <input type="submit" id="find" onClick="" value="查询" class="input_button2" />
		 数据最新更新时间：${lastupdatetime }
		</form>
		</div>
		<div class="right_title">
		<%if(customerlist!=null&&customerlist.size()>0&&!customerMap.isEmpty()){ %>
		<div style="position:relative; z-index:0; width:100% ">
			<div style="overflow-x:scroll; width:100%;">
			<table width="<%=width %>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tbody>
				<tr class="font_1" style="background-color: rgb(255, 255, 255); ">
					<td width="10" align="center" valign="middle" bgcolor="#eef6ff" >序号</td>
					<td width="30" align="center" valign="middle" bgcolor="#eef6ff" >站点</td>
					<%if(customerlist!=null&&customerlist.size()>0&&!customerMap.isEmpty()){for(Customer cus : customerlist){%>
					<td width="30" align="center" valign="middle" bgcolor="#eef6ff" ><%=cus.getCustomername() %></td>
					<%}} %>
					<td width="30" align="center" valign="middle" bgcolor="#eef6ff" >订单数总计</td>
				</tr>
				<%if(branchList!=null&&branchList.size()>0){for(Branch b : branchList){ %>
					<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
						<td align="center" valign="middle"><%=branchList.indexOf(b)+1 %></td>
						<td align="center" valign="middle"><%=b.getBranchname() %></td>
						<%if(customerlist!=null&&customerlist.size()>0){for(Customer c : customerlist){ %>
						<td align="center" valign="middle">
						<strong>
						<%long customeridbranchidcount = customerMap.get(b.getBranchid()+"_"+ c.getCustomerid())==null?0:(customerMap.get(b.getBranchid()+"_"+ c.getCustomerid())==null?0:customerMap.get(b.getBranchid()+"_"+ c.getCustomerid())); %>
						<%if(customeridbranchidcount>0){ %>
							<a href="javascript:sub(<%=b.getBranchid() %>,<%=c.getCustomerid() %>);"><%=customeridbranchidcount %></a>
						<%}else{ %>0<%} %>
						</strong>
						</td>
						<%}} %>
						<%if(!branchAllMap.isEmpty()){%>
							<td align="center" valign="middle" bgcolor="#f8f8f8" ><strong>
							<%long branchcount = branchAllMap.get(b.getBranchid())==null?0:branchAllMap.get(b.getBranchid()); %>
							<%if(branchcount>0){ %>
								<a href="javascript:sub(<%=b.getBranchid() %>,0);"><%=branchcount %></a>
							<%}else{ %>0<%} %>
							</strong></td>
						<%} %>
					</tr> 
				<%}} %>
			
				<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
					<td align="center" valign="middle" bgcolor="#eef6ff" >总计</td>
					<td align="center" valign="middle" bgcolor="#eef6ff" ><%=branchList.size() %>个</td>
					<%if(customerlist!=null&&customerlist.size()>0&&!customerAllMap.isEmpty()){for(Customer cus : customerlist){%>
						<td align="center" valign="middle" bgcolor="#eef6ff" >
						<strong>
						<%long customercount = customerAllMap.get(cus.getCustomerid())==null?0:customerAllMap.get(cus.getCustomerid()); %>
						<%if(customercount>0){ %>
							<a href="javascript:sub(0,<%=cus.getCustomerid() %>);"><%=customercount %></a>
						<%}else{ %>0<%} %>
						</strong>
						</td>
					<%}} %>
					<td align="center" valign="middle" bgcolor="#eef6ff" >&nbsp;</td>
				</tr>
			</tbody>
			</table>
			</div>
		</div>
		<%} %>
	</div>
	<form id="show" action="<%=request.getContextPath() %>/datastatistics/outwarehousecollectdatashow/1" method="post">
		<input name="customerid" id="customerid" value="0" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
		<input name="nextbranchid" id="nextbranchid" type="hidden" value="0"/>
		<input name="kufangid" id="kufangid" value="<%=request.getParameter("kufangid")==null?"0":request.getParameter("kufangid") %>" type="hidden"/>
	</form>
</div>
</body>
</html>
   
