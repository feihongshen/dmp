<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%
List<CwbOrder> cwborderList = request.getAttribute("cwborderList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 

Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
Map<Long,CustomWareHouse> customerWarehouseMap = request.getAttribute("customerWarehouseMap")==null?new HashMap<Long,CustomWareHouse>():(Map<Long,CustomWareHouse>)request.getAttribute("customerWarehouseMap");
Map<Long,Branch> branchMap = request.getAttribute("branchMap")==null?new HashMap<Long,Branch>():(Map<Long,Branch>)request.getAttribute("branchMap");
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
	if($("#date").val() == 1){
		window.location.href="<%=request.getContextPath()%>/monitor/dateShowExport/3";
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
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		<input type ="button" id="back" value="返回" class="input_button2" onclick="javascript:location.href='<%=request.getContextPath()%>/logtoday/simpleSupervisory<%=request.getParameter("branchid").length()>0?"?branchid="+request.getParameter("branchid"):"" %>'"/>
		<font color="red">&nbsp;&nbsp;<%=request.getAttribute("flowTypeStr")==null?"":request.getAttribute("flowTypeStr")%></font>
		<input type='hidden' name="branchid" value="<%=request.getParameter("branchid").length()>0?request.getParameter("branchid"):"0" %>" />
		</td>
	</tr>
	
	</table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="5000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >批次</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >省份</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" >城市</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >地区</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >地址</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人手机号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收件人电话</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >收货邮编</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >签收人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >签收时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >入库库房</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >上一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >当前站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >下一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >小件员</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff"  >发货重量（kg）</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >货品备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >应收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >保价金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >应退金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >备注1</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >备注2</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >备注3</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >备注4</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >备注5</td>
		</tr>
		<% for(CwbOrder c : cwborderList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=customerMap.get(c.getCustomerid())==null?"":customerMap.get(c.getCustomerid()).getCustomername() %></td>
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate()%></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getCwbprovince()%></td>
					<td  align="center" valign="middle"><%=c.getCwbcity() %></td>
					<td  align="center" valign="middle"><%=c.getCwbcounty() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneemobile() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneephone() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneepostcode() %></td>
					<td  align="center" valign="middle"><%=c.getPodrealname() %></td>
					<td  align="center" valign="middle"><%=c.getPodtime() %></td>
					<td  align="center" valign="middle"><%=customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid()))==null?"":customerWarehouseMap.get( Long.parseLong(c.getCustomerwarehouseid()==null?"0":c.getCustomerwarehouseid())).getCustomerwarehouse() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(Long.valueOf(c.getCarwarehouse()))==null?"":branchMap.get(Long.valueOf(c.getCarwarehouse())).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getStartbranchid())==null?"":branchMap.get(c.getStartbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getCurrentbranchid())==null?"":branchMap.get(c.getCurrentbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(c.getNextbranchid())==null?"":branchMap.get(c.getNextbranchid()).getBranchname() %></td>
					<td  align="center" valign="middle"><%=c.getExcelbranch() %></td>
					<td  align="center" valign="middle"><%=c.getExceldeliver() %></td>
					<td  align="center" valign="middle"><%=c.getCarrealweight() %></td>
					<td  align="center" valign="middle"><%=c.getCwbremark()%></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getCaramount()%></td>
					<td  align="center" valign="middle"><%=c.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=PaytypeEnum.getByValue((int)c.getPaywayid()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getRemark1() %></td>
					<td  align="center" valign="middle"><%=c.getRemark2() %></td>
					<td  align="center" valign="middle"><%=c.getRemark3() %></td>
					<td  align="center" valign="middle"><%=c.getRemark4() %></td>
					<td  align="center" valign="middle"><%=c.getRemark5() %></td>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>




