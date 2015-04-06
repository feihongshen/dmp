<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%
List<CwbOrder> clist = (List<CwbOrder>)request.getAttribute("cwbList");
Branch b = (Branch)request.getAttribute("branch");
Page page_obj =(Page)request.getAttribute("page_obj");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>货物查询</title>
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
</script>
</head>
<body style="background:#f5f5f5">
	<div class="right_box">
		<div class="inputselect_box">
			<span>
			</span>
			<form action="1" method="post" id="searchForm">
				订单类型：
				<select name="cwbordertypeid" id="cwbordertypeid">
					<option value="0">请选择订单类型</option>
					<%for(CwbOrderTypeIdEnum cti : CwbOrderTypeIdEnum.values()){ %>
						<option value="<%=cti.getValue()%>"><%=cti.getText() %></option>
					<%} %>
				</select>
				 发货时间段：
				 <input type ="text" name ="beginemaildate" id ="beginemaildate"  class="input_text1" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>" />&nbsp;到
				 <input type ="text" name= "endemaildate" id ="endemaildate"  class="input_text1" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"/>
				 状态：
				 <select name="flowordertype" id="flowordertype">
				 	<option value="0">请选择状态</option>
					<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){ %>
						<option value="<%=ft.getValue()%>"><%=ft.getText() %></option>
					<%} %>
				</select>
				<input type="submit" id="find"  value="查询" class="input_button2" />
			</form>
		</div>
		<div class="right_title">
			<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr class="font_1">
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
					<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
					<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">指定派送员</td>
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">客户要求</td>
					<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">实际重量</td>
					<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">取货数量</td>
					<td width="27%" align="center" valign="middle" bgcolor="#eef6ff">客户地址</td>
					<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
				</tr>
			</table>
			<form action="1" method="post" id="searchForm" name="searchForm">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<%for(CwbOrder c : clist){%>
						<tr>
							<td width="10%" align="center" valign="middle"><%=c.getCwb() %></td>
							<%if(c.getCwbordertypeid()!=-1){
								for(CwbOrderTypeIdEnum cot : CwbOrderTypeIdEnum.values()){
									if(c.getCwbordertypeid()==cot.getValue()){%>
										<td width="6%" align="center" valign="middle"><%=cot.getText() %></td>
									<%}
								}
							}else{%>
								<td width="6%" align="center" valign="middle">非正常订单</td>
							<%}%>
							<td width="10%" align="center" valign="middle"><%=c.getEmaildate() %></td>
							<td width="6%" align="center" valign="middle"><%=c.getExceldeliver() %></td>
							<td width="10%" align="center" valign="middle"><%=c.getCwbremark() %></td>
							<td width="10%" align="center" valign="middle"><%=c.getCustomercommand() %></td>
							<td width="6%" align="center" valign="middle"><%=c.getCarrealweight()%></td>
							<td width="5%" align="center" valign="middle"><%=c.getBackcarnum() %></td>
							<td width="27%" align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
							<td width="10%" align="center" valign="middle">
							<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){
								if(ft.getValue()==c.getFlowordertype()){%>
									<%=ft.getText() %>
							<%}} %>
							</td>
						</tr>
					<%}%>
				</table>
				<input type="submit" style="display: none;">			
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
$("#cwbordertypeid").val(<%=request.getParameter("cwbordertypeid")%>);
$("#flowordertype").val(<%=request.getParameter("flowordertype")%>);
</script>
</body>
</html>