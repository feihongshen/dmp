<%@page import="cn.explink.domain.PrintView"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
List<PrintView> printList = request.getAttribute("printList")==null?null:(List<PrintView>)request.getAttribute("printList");
List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
String starttime=(String)request.getAttribute("begintime")==null?"":(String)request.getAttribute("begintime");
String endtime=(String)request.getAttribute("endtime")==null?"":(String)request.getAttribute("endtime");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-

transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员领货交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function() {
	$("#begintime").datetimepicker({
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

function cwbfind(){
	
	var deliverid = $("#deliverid").val();
	if(deliverid==0){
		alert("抱歉，请选择小件员！");
		return;
	}
	if($("#begintime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return ;
	}
		
	$("#searchForm").submit();
	
}


function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else if($("#printtemplateid").val()==0){
		alert("请选择打印模版！");
	}else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/outbillprinting_default");
		$("#selectforsmtbdprintForm").submit();
	}
}
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
$(function(){
	<%if(pList.size()==0){%>
		alert("您还没有设置模版，请先设置模版！");
	<%}%>
	$("#deliverid").change(function(){
		$("#currentdeliverid").val($(this).val());	
	});
	$("#templateid").change(function(){
		$("#printtemplateid").val($(this).val());	
	});
	
})
</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="1" method="post" id="searchForm">
					<input type="hidden" name="isshow" value="1"/>
					<div style="float:right">   
						打印模版：<select name="templateid" id="templateid" class="select1">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%}else if(pt.getTemplatetype()==4){ %>武汉飞远<%} %>）</option>
					  			<%} %>
							</select>
						<input type="button" onclick="bdprint();" value="打印" class="input_button2" />
						<a href="<%=request.getContextPath() %>/warehousegroup/historydeliverlist/1">历史打印列表>></a>
					</div>
					领货人：
					<select name="deliverid" id="deliverid" class="select1">
						<option value="0">请选择领货人</option>
						<%for(User u : deliverList){ %>
							<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%} %>
					</select>
					领货时间：
					<input type ="text" name ="begintime" id="begintime" value ="<%=starttime %>" class="input_text1">
                                                     到<input type ="text" name ="endtime" id="endtime" value ="<%=endtime%>" class="input_text1">
                                                     <input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
					<%if(printList!=null&&printList.size()>0){ %>
					<select name ="exportmould" id ="exportmould" class="select1">
			          <option value ="0">默认导出模板</option>
			          <%for(Exportmould e:exportmouldlist){%>
			           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
			          <%} %>
					</select>
					
					<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField('0','0');"/>
					<%} %>
					
					<!-- <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" /> -->
				</form>
		</div>
				<%-- <form action="" method="post" id="searchForm2">
					<input type="hidden" name="exportmould2" id="exportmould2" />
					<input type="hidden" name="deliverid" id="deliverid" value="<%=request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")%>"/>
				</form>
				</div> --%>
				<br/>
				<br/>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<div style="height:26px;"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
					</tr>
				</table>
				<form action="" method="post" id="selectforsmtbdprintForm" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<%for(PrintView pv:printList){ %>
					 <tr>
					 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=pv.getCwb() %>" checked="checked"/></td>
						<td width="25%" align="center" valign="middle"><%=pv.getCwb() %></td>
						<td width="25%" align="center" valign="middle">  
						<%for(User u : deliverList){
    					   if(pv.getDeliverid()==u.getUserid()){%>
					    		<%=u.getRealname()%>
					       <% }
					    }%>
					    </td>
					    <td width="20%" align="center" valign="middle"><%=pv.getFlowordertypeMethod()%></td>
					    <td width="20%" align="center" valign="middle"><%=pv.getOutstoreroomtime()%></td>
					</tr>
					<%} %>
					<input value="1" name="islinghuo"  id="islinghuo" type="hidden"/>
					<input type="hidden" name="exportmould2" id="exportmould2" />
					<input id="currentdeliverid" name="currentdeliverid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("deliverid"))%>" type="hidden"/>
					<input id="nextbranchid" name="nextbranchid" value="0" type="hidden"/>
					<input id="printtemplateid" name="printtemplateid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("templateid"))%>" type="hidden"/>
					<input id="strtime" name="strtime" value="${begintime }" type="hidden"/>
					<input id="strtime" name="endtime" value="${endtime }" type="hidden"/>
				</table>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
					
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#deliverid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("deliverid"))%>);
function exportField(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要导出的订单！");
	}else{
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval0").attr("disabled","disabled");
	 	$("#btnval0").val("请稍后……");
	 	$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath()%>/warehousegroup/exportExcleForDeliver");
	 	$("#selectforsmtbdprintForm").submit();
	}
	
}

</script>
</body>
</html>
