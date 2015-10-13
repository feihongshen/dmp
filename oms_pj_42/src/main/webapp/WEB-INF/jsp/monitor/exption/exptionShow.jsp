
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorOrderDTO"%>
<%@page import="cn.explink.util.StringUtil"%>
<%List<MonitorOrderDTO> showDateList =(List<MonitorOrderDTO>) request.getAttribute("showDateList"); 
int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());%> 
<%Page page_obj = (Page)request.getAttribute("page_obj"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<script type="text/javascript">
function check(){
	if($("#date").val() == 1){
		window.location.href="<%=request.getContextPath()%>/monitor/dateShowExport/7";
		return true;
	}else{
		alert("没有数据，不能导出！");
		return false;
	}
}

</script>
<body style="background:#eef9ff">
    <input type="hidden" id="date" value="<%=(showDateList != null && showDateList.size()>0)?1:0%>">
   <div class="menucontant">
		<form name="form1" id="searchForm" action ="<%=request.getContextPath()%>/order/select/1" method = "post">					
           <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%">
					  <input type ="button" id="btnval" value="导出excel" class="input_button2" onclick="check();"/>
		              <input type ="button" id="btnval" value="返回" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/monitor/back/7'"/>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					   <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单编号</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">商品名称</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">货物价钱</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收货人姓名</td>
								<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">收货人手机</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">站名</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
								<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
							</tr>
						 <% int i=1; %>
					    <% for(MonitorOrderDTO mo : showDateList){ %>
							<tr>
								<td><%=mo.getCwb() %></td>
								<td><%=mo.getSendcarname() %></td>
								<td><%=mo.getCaramount() %></td>
								<td><%=mo.getConsigneename() %></td>
								<td>
								<%if(usershowphoneflag==1){ %>
								<%=mo.getConsigneemobile() %>
								<%}else{%>
								  [不可见]
								<%} %>
								</td>
								<td><%=mo.getBranchname() %></td>
								<td><%=mo.getRealname() %></td>
								<td><%=mo.getEmaildate() %></td>
							</tr>
						<%} %>
						  <%if(page_obj.getMaxpage()>1){ %>
							<tr valign="middle">
								<td colspan="8" align="center" valign="middle" >
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
								    共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　第<%=request.getAttribute("page")%>页 
                                </td>
							</tr>
							<%} %>
					   </table>
					</td>
				</tr>
			</table>
	   </form>			
   </div> 

</body>
</html>

