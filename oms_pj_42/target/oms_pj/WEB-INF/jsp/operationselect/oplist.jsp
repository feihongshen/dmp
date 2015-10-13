<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<OrderFlow> orderFlowList = (List<OrderFlow>)request.getAttribute("orderFlowList");
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>反馈记录查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script>
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
<script>
function clearSelect(){
	$("#startbranchid").val(-1);//当前站点
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
}

function selsub1(){
	if($("#startbranchid").val()=="-1"){
	    alert("请选择站点");
	    return false;
	}if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	return true;
}

$(function(){
	$("#find").click(function(){
		if(selsub1()){
			$("#searchForm").submit();
		};
	});
});
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"  method="post" id="searchForm">
		<input type ="hidden" name ="isshow" value ="1"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:30px">
			<tr>
				<td align="left">
					　<select name ="startbranchid" id ="startbranchid">
					          <option value ="-1">请选择站点</option>
					          <%if(branchList!=null && branchList.size()>0){ %>
					          <%for(Branch c : branchList){ %>
					          <option value =<%=c.getBranchid() %> 
					           <%if(c.getBranchid()==Integer.parseInt(request.getParameter("startbranchid")==null?"-1":request.getParameter("startbranchid"))){ %>selected="selected" <%} %> ><%=c.getBranchname()%></option>
					          <%} }%>
					</select>
					操作反馈时间：<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>　
					到　<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>"/>
						<input type="button" id ="find"  value="查询" class="input_button2" />　
						<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
					
				</td>
			</tr>
	    </table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:40px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td width="25%"  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
			    <td width="35%"  align="center" valign="middle" bgcolor="#eef6ff" >订单状态</td>
				<td width="35%"  align="center" valign="middle" bgcolor="#eef6ff" >操作反馈时间</td>
		</tr>
		<%if(orderFlowList != null && orderFlowList.size()>0){  %>
		<% for(OrderFlow o : orderFlowList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=o.getCwb() %></td>
					<td  align="center" valign="middle"><%=o.getFlowordertypeText() %></td>
					<td  align="center" valign="middle"><%=o.getCredateStr() %></td>
				 </tr>
		 <%} }%>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	</div>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>

