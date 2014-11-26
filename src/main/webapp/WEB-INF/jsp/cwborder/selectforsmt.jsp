<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = (List<CwbOrder>)request.getAttribute("cwbList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上门退订单交接单打印</title>
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
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});

function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要补打的订单！");
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
</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form action="1" method="post" id="searchForm" >
	      　　<input type="submit" id="find" value="刷新" class="input_button2" />
	      <%if(cwbList.size()>0){ %>
	      <input type="button" onclick="bdprint();" value="补单打印" class="input_button2" />
	      <a href ="../selectforsmtprint">未打印订单打印</a>
	      <%} %>
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
			<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">指定小件员</td>
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">客户要求</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">实际重量</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">取货数量</td>
			<td width="17%" align="center" valign="middle" bgcolor="#eef6ff">客户地址</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
			<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">打印时间</td>
		</tr>
		</table>
	<form action="<%=request.getContextPath() %>/cwborder/selectforsmtbdprint" method="post" id="selectforsmtbdprintForm" >
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		  <%for(CwbOrder c: cwbList){ %>
		 <tr>
		 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=c.getCwb() %>"/></td>
		    <td width="10%" align="center" valign="middle"><%=c.getCwb() %></td>
			<td width="4%" align="center" valign="middle">上门退</td>
			<td width="9%" align="center" valign="middle"><%=c.getEmaildate() %></td>
			<td width="5%" align="center" valign="middle"><%=c.getExceldeliver() %></td>
			<td width="7%" align="center" valign="middle"><%=c.getCwbremark() %></td>
			<td width="10%" align="center" valign="middle"><%=c.getCustomercommand() %></td>
			<td width="5%" align="center" valign="middle"><%=c.getCarrealweight()%></td>
			<td width="4%" align="center" valign="middle"><%=c.getBackcarnum() %></td>
			<td width="17%" align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
			<td width="10%" align="center" valign="middle">
			<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){
				if(ft.getValue()==c.getFlowordertype()){%>
					<%=ft.getText() %>
			<%}} %>
			</td>
			<td width="9%" align="center" valign="middle" ><%=c.getPrinttime() %></td>
		</tr>
		<%} %>
	</table>
	</form>
	<div class="jg_10"></div><div class="jg_10"></div>
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
$("#userid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("userid"))%>);
</script>
</body>
</html>
