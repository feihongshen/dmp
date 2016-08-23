<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
	List<PrintView> printList = (List<PrintView>)request.getAttribute("printList");
	List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
    Map usermap = (Map) session.getAttribute("usermap");
    String begindate=request.getParameter("begindate")==null?DateTimeUtil.getNowDate()+" 00:00:00":request.getParameter("begindate");
    String enddate=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货出站交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script type="text/javascript">
function cwbfind(){
	var begindate=$("#begindate").val();
	var enddate=$("#enddate").val();
	var branchid = $("#branchid").val();
	if(branchid==0){
		alert("抱歉，请选择下一站点！");
		return ;
	}
	if(begindate>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#begindate").val("<%=DateTimeUtil.getNowDate()+" 00:00:00"%>");
	}
	
	if(enddate>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#enddate").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	if(begindate>enddate){
		alert("开始时间不能大于结束时间");
		return;
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
$(function() {
	$("#begindate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#enddate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	<%if(pList.size()==0){%>
		alert("您还没有设置模版，请先设置模版！");
	<%}%>
	
	$("#branchid").change(function(){
		$("#nextbranchid").val($(this).val());	
	});
	
	$("#templateid").change(function(){
		$("#printtemplateid").val($(this).val());	
	});
	
});


function cwbexport(){
	var isprint="";
	$('input[name="isprint"]:checked').each(function(){
		isprint=$(this).val();
	});
	if(isprint==""){
		alert("请选择要导出的订单！");
	}else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/exportforoutwarehouse");
		$("#selectforsmtbdprintForm").submit();
	}
	
}
</script>
</head>
<body style="background:#f5f5f5">
				<div class="jg_10"></div><div class="jg_10"></div>
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="1" method="post" id="searchForm">
					<input type="hidden" name="isshow" value="1"/> 
					<div style="float:right">  
				        打印模版：<select name="templateid" id="templateid" class="select1">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} else if(pt.getTemplatetype()==4) { %>武汉飞远<%} %>）</option>
					  			<%} %>
							</select>
				      <input type="button" onclick="bdprint();" value="打印" class="input_button2" />
				      <a href="<%=request.getContextPath() %>/warehousegroupdetail/historyreturnlist/1">历史打印列表>></a>
				      </div>
					 下一站：<select name="branchid" id="branchid" class="select1">
			        <option value="0">请选择下一站</option>
				        <%for(Branch b :branchlist){ %>
				           <option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
				        <%} %>
			        </select>　
			        <input type="text" name="begindate" id="begindate"  value="<%=begindate %>" class="input_text1"/>
			        到
			        <input type="text" name="enddate" id="enddate" value="<%=enddate %>" class="input_text1"/>
			        	（未打印订单只保留15天）<input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
				       <%if(printList!=null&&printList.size()>0){ %>
					      　　<input type="button" id="forexport" onclick="cwbexport();" value="导出" class="input_button2" />
					   <%} %>
				</form>
				</div>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">出站站点</td>
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
							<td width="25%" align="center" valign="middle"><%=pv.getNextbranchname()%></td>
						    <td width="20%" align="center" valign="middle"><%=pv.getFlowordertypeMethod()%></td>
						    <td width="20%" align="center" valign="middle"><%=pv.getOutstoreroomtime()%></td>
						</tr>
					<%} %>
					<input value="1" name="isback"  id="isback" type="hidden"/>
					<input value="<%=begindate %>" name="starttime"  id="starttime" type="hidden"/>
					<input value="<%=enddate %>" name="endtime"  id="endtime" type="hidden"/>
					<input id="nextbranchid" name="nextbranchid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>" type="hidden"/>
					<input id="printtemplateid" name="printtemplateid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("templateid"))%>" type="hidden"/>
					</table>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
$("#printtemplateid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("printtemplateid"))%>);
</script>
</body>
</html>

