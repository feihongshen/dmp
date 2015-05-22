<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<PrintView> printList = (List<PrintView>)request.getAttribute("printList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退供货商出库交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function cwbfind(){
	var customerid = $("#customerid").val();
	if(customerid==0){
		alert("抱歉，请选择供货商！");
	}else{
		$("#searchForm").submit();
	}
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
	<form action="1" method="post" id="searchForm" name="searchForm">
		<input type="hidden" name="isshow" value="1"/>
		<div style="float:right">  
			 打印模版：<select name="templateid" id="templateid">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} %>）</option>
					  			<%} %>
							</select>
			<input type="button" onclick="bdprint();" value="打印" class="input_button2" />
			<a href="<%=request.getContextPath() %>/warehousegroup/historybacktocustomerlist/1">历史打印列表>></a>
			</div>
		供货商：
		<select id="customerid" name="customerid">
			<option value="0">请选择</option>
			<%for(Customer c : customerlist){ %>
				<option value="<%=c.getCustomerid() %>" ><%=c.getCustomername() %></option>
			<%} %>
		</select>
		（未打印订单只保留15天）
			<input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
			
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr class="font_1">
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
				<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
			</tr>
		</table>
		<form action="<%=request.getContextPath() %>/warehousegroup/backtocustomerbillprinting_default" method="post" id="selectforsmtbdprintForm" >
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<%for(PrintView pv:printList){ %>
				 <tr>
				 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=pv.getCwb() %>" checked="checked"/></td>
					<td width="25%" align="center" valign="middle"><%=pv.getCwb() %></td>
					<td width="25%" align="center" valign="middle"><%=pv.getCustomername()%></td>
				    <td width="20%" align="center" valign="middle"><%=pv.getFlowordertypeMethod()%></td>
				    <td width="20%" align="center" valign="middle"><%=pv.getOutstoreroomtime()%></td>
				    <input id="printtemplateid" name="printtemplateid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("templateid"))%>" type="hidden"/>
				</tr>
			<%} %>
			</table>
		</form>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#customerid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("customerid"))%>);
$("#printtemplateid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("printtemplateid"))%>);
</script>
</body>
</html>