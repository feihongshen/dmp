<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>受理投诉</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

</head>
<%
List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
Page page_obj = (Page)request.getAttribute("page_obj");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
%>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script>
$(function() {
	$("#createdate").datepicker();
});
</script>
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
});
function addInit(){
	//无处理
}
function addSuccess(data){
	//无处理
}
function editInit(){
	//无处理
}
function editSuccess(data){
	//无处理
}
function delSuccess(data){
	//无处理
}
</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	});
});

function clearSelect(){
	$("#cwbsid").val('');
	$("#consigneenameid").val('');
	$("#consigneephoneid").val('');
	$("#consigneeaddressid").val('');
	$("#strtime").val('');
	$("#exportbtn").hide();
}
function sub(){
	if($("#cwbsid").val().length==0&&$("#strtime").val().length==0){
		alert("请输入订单号或者必选发货时间");
		return false;
	}else if($("#cwbsid").val().length==0&&($("#consigneenameid").val().length>0||$("#consigneephoneid").val().length>0||$("#consigneeaddressid").val().length>0)&&$("#strtime").val().length==0){
		alert("请选择发货时间");
		return false;
	}else{
		$("#searchForm").submit();
		$("#exportbtn").show();
	}
	
}
</script>
</head>
<body onLoad="$(&#39;#orderSearch&#39;).focus();" marginwidth="0" marginheight="0">

<div class="inputselect_box" style="top: 0px; ">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" value="1" name="isshow" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td width="100" rowspan="2" valign="top" bgcolor="#eaefb5">订/运单号：</td>
		<td width="170" rowspan="2" valign="top" bgcolor="#eaefb5"><textarea name="cwbs" rows="3" id="cwbsid"></textarea></td>
		<td bgcolor="#eaefb5">
		发货时间：<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>(默认查询该时间为起点的10天之内的数据)
		<br/>
		姓名：
			<input type="text" name="consigneename" id="consigneenameid" value="<%if(request.getParameter("cwbs")==null||request.getParameter("cwbs").length()==0){ %><%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %><%}%>">
		手机号：
		<input type="text" name="consigneemobile" id="consigneephoneid" value="<%if(request.getParameter("cwbs")==null||request.getParameter("cwbs").length()==0){ %><%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %><%}%>">
		地址：
		<input type="text" name="consigneeaddress" id="consigneeaddressid" value="<%if(request.getParameter("cwbs")==null||request.getParameter("cwbs").length()==0){ %><%=request.getParameter("consigneeaddress")==null?"":request.getParameter("consigneeaddress") %><%}%>"></td>
	</tr>
	<tr>
		<td bgcolor="#eaefb5">
			<input type="button" name="button2"  value="查询" class="input_button2" onclick="sub();"/>
			<input type="button" name="button2"  value="重置" class="input_button2"  onclick="clearSelect();">
			<%if(orderlist!=null&&orderlist.size()>0){ %><input type="button" name="button2"  value="导出" class="input_button2" id="exportbtn" onclick="expotrfile();"><%} %>
		</td>
	</tr>
</table>
	</form>
	<form action="1" method="post" id="searchForm1">
		<input type="hidden" value="1" name="isshow" />
		<input type ="hidden" name ="begindate"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>
		<input type="hidden"  value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %>" name="cwbs" />
		<input type="hidden"  value="<%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %>" name="consigneename" />
		<input type="hidden"  value="<%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %>" name="consigneemobile" />
		<input type="hidden"  value="<%=request.getParameter("consigneeaddress")==null?"":request.getParameter("consigneeaddress") %>" name="consigneeaddress" />
	</form>
</div><%if(orderlist != null && orderlist.size()>0){ %>
<div class="right_title">
	<div style="height:100px"></div>
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table_2"  id="gd_table">
		<tbody>
			<tr class="font_1" height="30">
				<td width="80"  align="center" valign="middle" bgcolor="#E1F0FF">订单号</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">运单号</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">供货商</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">发货时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">收件人</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">地址</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">手机</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">当前状态</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">操作</td>
			</tr>
			
			<%for(CwbOrder c:orderlist){ %>
			<tr>
				<td width="80" align="center" bgcolor="#f1f1f1"><%=c.getCwb() %></td>
				<td align="center"><%=c.getTranscwb() %></td>
				<td align="center"><%=customerMap.get(c.getCustomerid())==null?"":customerMap.get(c.getCustomerid()).getCustomername() %></td>
				<td align="center"><%=c.getEmaildate() %></td>
				<td align="center"><%=c.getConsigneename() %></td>
				<td align="center"><%=c.getConsigneeaddress() %></td>
				<td align="center"><%=c.getConsigneemobile() %></td>
				<td align="center"><%if(CwbFlowOrderTypeEnum.getText(c.getFlowordertype()).getText()=="已审核"){%>
										审核为：<%= DeliveryStateEnum.getByValue(c.getDeliverystate()).getText() %><%}
									else if(CwbFlowOrderTypeEnum.getText(c.getFlowordertype()).getText()=="已反馈") {%>
										反馈为：<%= DeliveryStateEnum.getByValue(c.getDeliverystate()).getText() %><%}
									else{ %>
										<%=CwbFlowOrderTypeEnum.getText(c.getFlowordertype()).getText() %>
									<%} %></td>
				<td align="center"><a href="<%=request.getContextPath()%>/order/showOrder/<%=c.getCwb() %>">查看详情</a></td>
			</tr>
			<%} %>
		</tbody>
	</table>
	
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm1').attr('action','1');$('#searchForm1').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm1').submit();">上一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm1').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm1').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm1').attr('action',$(this).val());$('#searchForm1').submit()">
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
<%} %>
<form action="<%=request.getContextPath() %>/order/exportComplaint"  method="post" id="searchForm2">
		<input type="hidden"  value="<%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %>" name="cwbs" />
		<input type="hidden"  value="<%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %>" name="consigneename" />
		<input type="hidden"  value="<%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %>" name="consigneemobile" />
		<input type="hidden"  value="<%=request.getParameter("consigneeaddress")==null?"":request.getParameter("consigneeaddress") %>" name="consigneeaddress" />
		<input type="hidden"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" name="begindate" />
</form>
<script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page") %>);
	
	function expotrfile(){
		$("#searchForm2").submit();		
	}
	</script>
<!--投诉处理-->
</body></html>

