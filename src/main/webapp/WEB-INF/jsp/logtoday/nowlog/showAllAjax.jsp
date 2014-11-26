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
List<CwbOrder> cwborderList = request.getAttribute("cwborderList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 

Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
Map<Long,CustomWareHouse> customerWarehouseMap = request.getAttribute("customerWarehouseMap")==null?new HashMap<Long,CustomWareHouse>():(Map<Long,CustomWareHouse>)request.getAttribute("customerWarehouseMap");
Map<Long,Branch> branchMap = request.getAttribute("branchMap")==null?new HashMap<Long,Branch>():(Map<Long,Branch>)request.getAttribute("branchMap");
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");

String flag=request.getParameter("flag")==null?"1":request.getParameter("flag");
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
<script type="text/javascript">
function check(){
	if(<%=page_obj.getTotal() > 0 %>){
		$('#searchForm2').submit();
		return true;
	}else{
		alert("没有数据，不能导出！");
		return false;
	}
}

</script>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getContextPath()%>/logtoday/exportExcel" method="post" id="searchForm2">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		
		<input type ="button" id="back" value="返回" class="input_button2" onclick="$('#searchForm1').submit()"/>
		&nbsp;<select name ="exportmould" id ="exportmould">
		          <option value ="0">默认导出模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
			</select>
			&nbsp;&nbsp;<input type ="button" id="btnval" value="导出excel" class="input_button2"  onclick="check();"  />
			<font color="red"><%=request.getAttribute("typeStr")==null?"":request.getAttribute("typeStr") %></font>
		</td>
	</tr>
	</table>
	<input type="hidden" name="type" value="<%=request.getAttribute("type")==null?"":request.getAttribute("type")%>" >
	<input type="hidden" name="branchid" value="<%=request.getAttribute("branchid")==null?"0":request.getAttribute("branchid")%>" >
	</form>
	<form id="searchForm1" action ="<%=request.getContextPath()%>/logtoday/todayArrivalAllAjax?flag=<%=flag%>" method = "post">
	<input type="hidden" name="branchid" value="<%=request.getAttribute("branchid")%>">
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
		<% for(CwbOrder c : cwborderList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=customerMap.get(c.getCustomerid())==null?"":customerMap.get(c.getCustomerid()).getCustomername() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid())==null?"配送":CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText(c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=DeliveryStateEnum.getByValue(c.getDeliverystate()).getText() %></td>
					<td  align="center" valign="middle"><%=customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid()))==null?"":customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid())).getCustomerwarehouse() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(Long.valueOf((c.getCarwarehouse()==null||"".equals(c.getCarwarehouse()))?"0":c.getCarwarehouse()))==null?"":branchMap.get(Long.valueOf((c.getCarwarehouse()==null||"".equals(c.getCarwarehouse()))?"0":c.getCarwarehouse())).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getStartbranchid())==null?"":branchMap.get(c.getStartbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getCurrentbranchid())==null?"":branchMap.get(c.getCurrentbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getNextbranchid())==null?"":branchMap.get(c.getNextbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getDeliverybranchid())==null?"":branchMap.get(c.getDeliverybranchid()).getBranchname() %></td>
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
	<form  id="searchForm" action="<%=request.getContextPath()%>/logtoday/show/<%=request.getAttribute("branchid")%>/<%=request.getAttribute("type")%>/1">
	</form>		
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>




