<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> cwbOrderList = (List<CwbOrder>)request.getAttribute("cwborderList");
int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
  Page page_obj =(Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script> --%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});
</script>
</HEAD>
<body style="background:#eef9ff">
   <div class="menucontant">
   
   <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr>
					<td colspan="4">
					   <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单编号</td>
								<td width="10%"align="center" valign="middle" bgcolor="#eef6ff">商品名称</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">货物金额</td>
								<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">收件人姓名</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人电话/手机</td>
								<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">收件人地址</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
							</tr>
							<% for(CwbOrder c : cwbOrderList){ %>
							<tr valign="middle">
										<td align="center" valign="middle" ><a href ="javascript:getViewCwbBox(<%=c.getCwb()%>);"><%=c.getCwb()%></a></td>
										<td align="center" valign="middle" ><%=c.getSendcarname()%></td>
										<td align="center" valign="middle" ><%=c.getCaramount()%></td>
										<td align="center" valign="middle" ><%=c.getConsigneename()%> </td>
										<td align="center" valign="middle" >
										<%if(usershowphoneflag==1){ %>
										  <%if(c.getConsigneephone() != null && !"".equals(c.getConsigneephone())){%><%=c.getConsigneephone()%> / <%}%><%=c.getConsigneemobile()%> 
										<%}else{ %>[不可见]
										<%} %>
										</td>
										<td align="left" valign="middle" ><%=c.getConsigneeaddress()%></td>
										<td align="center" valign="middle" ><%=c.getEmaildate()%></td>
									</tr>
							 <%} %> 
							<tr valign="middle">
								<td colspan="10" align="center" valign="middle" >
								   <a href="<%=request.getContextPath()%>/funds/paymentShowExp?controlStr=<%=request.getAttribute("controlStr")%>"> 导出excel</a>
								   &nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/funds/paymentShow/1?controlStr=<%=request.getAttribute("controlStr")%>');$('#searchForm').submit()">第一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/funds/paymentShow/<%=page_obj.getPrevious() %>?controlStr=<%=request.getAttribute("controlStr")%>');$('#searchForm').submit()">上一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/funds/paymentShow/<%=page_obj.getNext() %>?controlStr=<%=request.getAttribute("controlStr")%>');$('#searchForm').submit()" >下一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/funds/paymentShow/<%=page_obj.getMaxpage() %>?controlStr=<%=request.getAttribute("controlStr")%>');$('#searchForm').submit()" >最后一页</a>
								    共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　第<%=request.getAttribute("page")%>页 
                                </td>
							</tr>
					   </table>
					</td>
				</tr>
			</table>
   </div> 
   <form name="form1" id = "searchForm" action ="<%=request.getContextPath()%>/funds/paymentShow/1?controlStr=<%=request.getAttribute("controlStr")%>" method ="post"></form>
   <div align="center"> <a href="<%=request.getContextPath()%>/funds/back" >返回</a></div>
</body>

</HTML>
