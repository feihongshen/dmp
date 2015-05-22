<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%
List<CwbOrder> clist = request.getAttribute("clist")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("clist");
Page page_obj = (Page)request.getAttribute("page_obj"); 

String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
Long kufangid = request.getParameter("kufangid")==null?0:Long.parseLong(request.getParameter("kufangid"));
Long customerid = request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"));
List<Branch> kufanglist = request.getAttribute("kufanglist")==null?null:(List<Branch>)request.getAttribute("kufanglist");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm2" action ="<%=request.getContextPath()%>/datastatistics/customerfahuodata_excel" method = "post" > 
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
			<tr>
				<td align="left">
				<input type ="button" id="back" value="返回" class="input_button2" onclick="location.href='<%=request.getContextPath() %>/datastatistics/customerfahuodata?begindate=<%=starttime %>&enddate=<%=endtime %>&kufangid=<%=kufangid%>&isshow=1'"/>
				
				<select name ="exportmould" id ="exportmould">
		          <option value ="0">默认导出模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
				</select>
				<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
				<input name="kufangid" id="kufangid" value="<%=kufangid %>" type="hidden"/>
				<input name="begindate" value="<%=starttime %>" type="hidden"/>
				<input name="enddate" value="<%=endtime %>" type="hidden"/>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField();"/>
			</td>
			</tr>
			</table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:40px"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">发货库房</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单当前状态</td>
		</tr>
		<%if(clist!=null) for(CwbOrder c : clist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%if(customerList!=null&&customerList.size()>0)for(Customer customer: customerList){if(c.getCustomerid()==customer.getCustomerid()){%><%=customer.getCustomername() %><%}} %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%if(kufanglist!=null&&kufanglist.size()>0)for(Branch b : kufanglist){if(Long.parseLong(c.getCarwarehouse())==b.getBranchid()){%><%=b.getBranchname() %><%}} %></td>
					<td  align="center" valign="middle"><%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){if(c.getCwbordertypeid()==ct.getValue()){ %><%=ct.getText() %><%}} %></td>
					<td  align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(c.getFlowordertype()==ft.getValue()){%><%=ft.getText() %><%}} %></td>
				 </tr>
		 <%} %>
	</table>
	<div class="jg_10"></div>
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
	<form  id="searchForm" action="<%=request.getContextPath() %>/datastatistics/customerfahuodatashow/1" method="post">
		<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
		<input name="kufangid" value="<%=kufangid %>" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
	</form>
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function exportField(){
	if(<%=clist!=null&&clist.size()!=0%>){
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