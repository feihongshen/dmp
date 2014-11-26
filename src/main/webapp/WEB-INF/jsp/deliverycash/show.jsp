<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.DeliveryCash"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%
List<DeliveryCash> list = request.getAttribute("list")==null?new ArrayList<DeliveryCash>():(List<DeliveryCash>)request.getAttribute("list");
Page page_obj = (Page)request.getAttribute("page_obj"); 

String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
List<User> deliverList = request.getAttribute("deliverList")==null?null:( List<User>)request.getAttribute("deliverList");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
Long deliveryid = request.getParameter("deliverid")==null?0:Long.parseLong(request.getParameter("deliverid"));
String[] dispatchbranchidArr =(String[]) request.getAttribute("dispatchbranchidArr");
String dispatchbranchids = (String) request.getAttribute("dispatchbranchids");
Long flowordertype = request.getParameter("flowordertype")==null?0:Long.parseLong(request.getParameter("flowordertype"));
Long customerid = request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"));
String [] deliverystate = (String[])request.getAttribute("deliverystate");
List<Branch> branchList = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");


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
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm2" action ="<%=request.getContextPath()%>/deliverycash/deliverycashsearchdetail_excel" method = "post" > 
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
			<tr>
				<td align="left">
				<input type ="button" id="back" value="返回" class="input_button2" onclick="location.href='<%=request.getContextPath() %>/deliverycash/list?deliverid=0<%for(String branchid : dispatchbranchidArr){ %>&dispatchbranchid=<%=branchid%><%} %>&flowordertype=<%=flowordertype%>&begindate=<%=starttime %>&enddate=<%=endtime %><%for(String ds :deliverystate){ %>&deliverystate=<%=ds%><%} %>'"/>
				
				<select name ="exportmould2" id ="exportmould2">
		          <option value ="0">默认导出模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
				</select>
				<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
				<input name="dispatchbranchid" id="dispatchbranchid" value="<%=dispatchbranchids %>" type="hidden"/>
				<input name="deliverid" value="<%=deliveryid %>" type="hidden"/>
				<input name="flowordertype" value="<%=flowordertype %>" type="hidden"/>
				<input name="begindate" value="<%=starttime %>" type="hidden"/>
				<input name="paybackfeeIs" value="${paybackfeeIs}" type="hidden"/>
				<input name="enddate" value="<%=endtime %>" type="hidden"/>
				<%for(String ds :deliverystate){ %><input name="deliverystate" value="<%=ds%>" type="hidden"/><%} %>
				
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField();"/>
			</td>
			</tr>
			</table>
	</form>
	
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">反馈时间</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff">归班状态</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">应收款</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">应退款</td>
				
		</tr>
		<%if(list!=null) for(DeliveryCash dc : list){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=dc.getCwb() %>"><%=dc.getCwb() %></a></td>
					<td  align="center" valign="middle">
					<%if(deliverList!=null&&deliverList.size()>0)for(User u : deliverList){if(dc.getDeliverid()==u.getUserid()){%>
					<%=u.getRealname() %>
					<%if(u.getEmployeestatus()==UserEmployeestatusEnum.LiZhi.getValue()){ %>
					(离职)
					<%} %>
					<%}} %></td>
					<td  align="center" valign="middle"><%if(customerList!=null&&customerList.size()>0)for(Customer c: customerList){if(c.getCustomerid()==dc.getCustomerid()){%><%=c.getCustomername() %><%}} %></td>
					<td  align="center" valign="middle"><%if(branchList!=null&&branchList.size()>0)for(Branch b : branchList){if(dc.getDeliverybranchid()==b.getBranchid()){%><%=b.getBranchname() %><%}} %></td>
					<td  align="center" valign="middle"><%=dc.getLinghuotime() %></td>
					<td  align="center" valign="middle"><%=dc.getFankuitime() %></td>
					<%-- <td  align="center" valign="middle"><%=dc.getGuibantime() %></td> --%>
					<td  align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(dc.getDeliverystate()==dse.getValue()){%><%=dse.getText() %><%}} %></td>
					<td  align="center" valign="middle"><%=dc.getReceivableNoPosfee().add(dc.getReceivablePosfee()) %></td>
					<td  align="center" valign="middle"><%=dc.getPaybackfee() %></td>
				 </tr>
		 <%} %>
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
	<form  id="searchForm" action="<%=request.getContextPath() %>/deliverycash/show/1" method="post">
		<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
		<input name="dispatchbranchid" value="<%=dispatchbranchids %>" type="hidden"/>
		<input name="deliverid" value="<%=deliveryid %>" type="hidden"/>
		<input name="paybackfeeIs" value="${paybackfeeIs}" type="hidden"/>
		<input name="flowordertype" value="<%=Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype")) %>" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
		<%for(String ds :deliverystate){ %><input name="deliverystate" value="<%=ds%>" type="hidden"/><%} %>
	</form>
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function exportField(){
	if(<%=list!=null&&list.size()!=0%>){
		$("#btnval").attr("disabled","disabled");
		$("#btnval").val("请稍后……");
		$('#searchForm2').submit();
		return true;
	}else{
		alert("没有做查询操作，不能导出！");
		return false;
	}
}
</script>
</body>
</html>




