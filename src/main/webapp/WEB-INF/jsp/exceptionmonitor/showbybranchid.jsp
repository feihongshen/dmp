<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%@page import="cn.explink.domain.Exportmould"%>
<%
List<CwbOrder> cwborderList = request.getAttribute("cwbList")==null?null:(List<CwbOrder>)request.getAttribute("cwbList");
Page page_obj = (Page)request.getAttribute("page_obj"); 
String cwbs=(String)request.getAttribute("cwbs");
Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
Map<Long,CustomWareHouse> customerWarehouseMap = request.getAttribute("customerWarehouseMap")==null?new HashMap<Long,CustomWareHouse>():(Map<Long,CustomWareHouse>)request.getAttribute("customerWarehouseMap");
Map<Long,Branch> branchMap = request.getAttribute("branchMap")==null?new HashMap<Long,Branch>():(Map<Long,Branch>)request.getAttribute("branchMap");

String begindate=request.getParameter("begindate")==null?"":request.getParameter("begindate").toString();
String enddate=request.getParameter("enddate")==null?"":request.getParameter("enddate").toString();
long customerid =Long.parseLong(request.getParameter("customerid")==null?"0":request.getParameter("customerid"));
long modelid =Long.parseLong(request.getParameter("modelid")==null?"0":request.getParameter("modelid"));
%>
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
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm2" action ="<%=request.getContextPath()%>/ExceptionMonitor/exportException" method = "post" > 
			<input type="hidden" name="cwbs" id="cwbs" value="<%=cwbs.length()==0?"0":cwbs%>"/>
			<input type="hidden" name="exportmould2" id="exportmould2" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
			<tr>
				<td align="left">
				<input type ="button" id="back" value="返回" class="input_button2" onclick="returnback();"/>
				<%if(cwborderList!=null&&!cwborderList.isEmpty()){%>
					<input type ="submit" id="export" value="导出" class="input_button2"/>
				<%}%>
			</td>
			</tr>
			</table>
	</form>
	<form id="searchForm" action ="<%=request.getContextPath()%>/ExceptionMonitor/operationTimeOut" method = "post">
				<input type="hidden" name="begindate" id="begindate" value="<%=begindate %>"/>
				<input type="hidden" name="enddate" id="enddate" value="<%=enddate %>"/>
				<input type="hidden" name="customerid" id="customerid" value="<%=customerid %>"/>
				<input type="hidden" name="modelid" id="modelid" value="<%=modelid %>"/>
	</form>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >当前状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >入库库房</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >上一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >当前站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >下一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
		</tr>
		<%if(cwborderList!=null&&!cwborderList.isEmpty()){
			for(CwbOrder c : cwborderList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=customerMap.get(c.getCustomerid())==null?"":customerMap.get(c.getCustomerid()).getCustomername() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText(c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=DeliveryStateEnum.getByValue(c.getDeliverystate()).getText() %></td>
					<td  align="center" valign="middle"><%=customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid()))==null?"":customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid())).getCustomerwarehouse() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(Long.valueOf((c.getCarwarehouse()==null||c.getCarwarehouse().equals(""))?"-1":c.getCarwarehouse()))==null?"":branchMap.get(Long.valueOf(c.getCarwarehouse())).getBranchname() %></td>					<td  align="center" valign="middle"><%=branchMap.get(c.getStartbranchid())==null?"":branchMap.get(c.getStartbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getCurrentbranchid())==null?"":branchMap.get(c.getCurrentbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getNextbranchid())==null?"":branchMap.get(c.getNextbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getDeliverybranchid())==null?"":branchMap.get(c.getDeliverybranchid()).getBranchname() %></td>
				 </tr>
		 <%}}%>
	</table>
	</div>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/<%=request.getAttribute("branchid") %>/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/<%=request.getAttribute("branchid") %>/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/<%=request.getAttribute("branchid") %>/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/<%=request.getAttribute("branchid") %>/'+$(this).val());$('#searchForm').submit()">
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
		<form  id="searchForm" action="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/<%=request.getAttribute("branchid") %>/1">
			<input type="hidden" value="<%=request.getParameter("A1") %>" name="A1"/>
			<input type="hidden" value="<%=request.getParameter("A2") %>" name="A2"/>
			<input type="hidden" value="<%=request.getParameter("A3") %>" name="A3"/>
			<input type="hidden" value="<%=request.getParameter("A4") %>" name="A4"/>
			<input type="hidden" value="<%=request.getParameter("A5") %>" name="A5"/>
			<input type="hidden" value="<%=request.getParameter("A6") %>" name="A6"/>
			<input type="hidden" value="<%=request.getParameter("A7") %>" name="A7"/>
			<input type="hidden" value="<%=request.getParameter("A8") %>" name="A8"/>
			<input type="hidden" value="<%=request.getParameter("A9") %>" name="A9"/>
			<input type="hidden" value="<%=request.getParameter("A10") %>" name="A10"/>
		
		</form>	
		
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function returnback(){
	$("#searchForm").submit();
}
</script>
</body>
</html>




