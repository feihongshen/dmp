<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.controller.ComplaintView"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>投诉处理</title>
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
</head>
<%
List<ComplaintView> complaintList  = (List<ComplaintView>)request.getAttribute("complaintlist");
Map customerMap = (Map)request.getAttribute("customerMap");
Page page_obj = (Page)request.getAttribute("page_obj");
	String cwb1="";
	String cwb="";
if(!complaintList.isEmpty()){
	for(ComplaintView list: complaintList){
		cwb1+=list.getCwb();
		cwb1+=",";
	}
	 cwb=cwb1.equals("")?"":cwb1.substring(0,cwb1.length()-1);
}
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
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#searchForm").submit();
}
function editInit(){
	//无处理
}
function editSuccess(data){
	//无处理
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
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

function clearSelect(){
	$("#cwbsid").val('');
	$("#strtime").val('');
	$("#endtime").val('');
	$("#typeid").val(-1);
	$("#auditTypeid").val(-1);
}
</script>
</head>

<body onLoad="$(&#39;#orderSearch&#39;).focus();" marginwidth="0" marginheight="0">

<div class="inputselect_box" style="top: 0px; ">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" value="1" name="isshow" id="isshow"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_1">
	<tr>
		<td width="100" rowspan="2" valign="top" bgcolor="#eaefb5">订/运单号：</td>
		<td width="170" rowspan="2" valign="top" bgcolor="#eaefb5"><textarea name="cwbs" rows="3" id="cwbsid" ></textarea></td>
		<td bgcolor="#eaefb5">投诉类型：
			<select name="type" id="typeid">
			<option value="-1">请选择</option>
		          <%for(ComplaintTypeEnum c : ComplaintTypeEnum.values()){ %>
			<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("type")==null?"-1":request.getParameter("type"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		          <%} %>
			</select>
投诉时间：
<label for="select2"></label>
<input type ="text" name ="starteTime" id="strtime"  value="<%=request.getParameter("starteTime")==null?"":request.getParameter("starteTime") %>"/>
至
<input type ="text" name ="endTime" id="endtime"  value="<%=request.getParameter("endTime")==null?"":request.getParameter("endTime") %>"/>
审核状态：
<label for="select4"></label>
<select name="auditType" id="auditTypeid">
	<option value="-1">请选择</option>
	     <%for(ComplaintAuditTypeEnum c : ComplaintAuditTypeEnum.values()){ %>
			<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("auditType")==null?"-1":request.getParameter("auditType"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		    <%} %>
</select>
</td>
	</tr>
	<tr>
<td bgcolor="#eaefb5"><input type="submit" name="button2"  value="查询" class="input_button2">
<input type="button" name="button2" id="clear" value="重置" class="input_button2" onclick="clearSelect();">
<%if(complaintList != null && complaintList.size()>0){ %>
<input type ="button" id="btnval" value="导出" class="input_button1" onclick="exportField();"/><%} %></td>
	</tr>
</table>
	</form>
	<form action="1" method="post" id="searchForm1">
	</form>
</div><%if(complaintList != null && complaintList.size()>0){ %>
<div class="right_title">
	<div style="height:70px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1410" border="0" cellpadding="0" cellspacing="1" class="table_2"  id="gd_table">
		<tbody>
			<tr class="font_1" height="30">
				<td width="80"  align="center" valign="middle" bgcolor="#E1F0FF">订单号</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">小件员</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">供货商</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">发货时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">入库时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">到站时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">归班时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">投诉时间</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">配送结果</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">当前状态</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">收件人</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">受理人</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">投诉类型</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">审核状态</td>
				<td align="center" valign="middle" bgcolor="#E1F0FF">操作</td>
			</tr>
			
			<%for(ComplaintView c:complaintList){ %>
			<tr>
				<td width="80" align="center" bgcolor="#f1f1f1"><%=c.getCwb() %></td>
				<td align="center"><%=c.getDelivername() %></td>
				<td align="center"><%=c.getCustomername() %></td>
				<td align="center"><%=c.getEmaildate() %></td>
				<td align="center"><%=c.getInstoreroomtime() %></td>
				<td align="center"><%=c.getInSitetime() %></td>
				<td align="center"><%=c.getGoclasstime() %></td>
				<td align="center"><%=c.getCreateTime() %></td>
				<td align="center"><%=DeliveryStateEnum.getByValue((int)c.getDeliverystate()).getText()  %></td>
				<td align="center"><%=FlowOrderTypeEnum.getText(c.getOrderflowtype()).getText()  %></td>
				<td align="center"><%=c.getConsigneename() %></td>
				<td align="center"><%=c.getCreateUser() %></td>
				<td align="center"><%=ComplaintTypeEnum.getByValue(c.getType()).getText()  %></td>
				<td align="center"><%=ComplaintAuditTypeEnum.getByValue(c.getAuditType()).getText()  %></td>
				<td align="center">
				[<a href="javascript:getSetExportViewBox(<%=c.getId()%>);" >查看详情</a>]
				[<a href="javascript:if(confirm('确定要删除?')){del(<%=c.getId()%>);}">删除</a>]
				</td>
			</tr>
			<%} %>
		</tbody>
	</table>
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
    
</div><%} %>

<form action="<%=request.getContextPath()%>/complaint/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="cwbStrs" id="cwbStrs" value="0"/>
		<input type="hidden" name="cwbs1" id="cwbs1" value="<%=cwb %>"/>
		<input type="hidden" name="starteTime1" id="starteTime1" value="<%=request.getParameter("starteTime")==null?"":request.getParameter("starteTime")%>"/>
		<input type="hidden" name="endTime1" id="endTime1" value="<%=request.getParameter("endTime")==null?"":request.getParameter("endTime")%>"/>
		<input type="hidden" name="type1" id="type1" value="<%=request.getParameter("type")==null?"-1":request.getParameter("type")%>"/>
		<input type="hidden" name="auditType1" id="auditType1" value="<%=request.getParameter("auditType")==null?"-1":request.getParameter("auditType")%>"/>
	</form>
  <script type="text/javascript">
	$("#selectPg").val(<%=request.getAttribute("page") %>);
	function exportField(){
		if($("#isshow").val()=="1"&&<%=complaintList != null && complaintList.size()>0 %>){
			$("#btnval").attr("disabled","disabled");
		 	$("#btnval").val("请稍后……");
		 	$("#searchForm2").submit();
		}else{
			alert("没有做查询操作，不能导出！");
		};
	}

	</script>
<input type="hidden" id="view" value="<%=request.getContextPath()%>/complaint/show/" />
<input type="hidden" id="del" value="<%=request.getContextPath()%>/complaint/delete/" />
<!--投诉处理-->
</body></html>