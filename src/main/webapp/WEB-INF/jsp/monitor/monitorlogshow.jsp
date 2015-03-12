<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%@page import="cn.explink.domain.Exportmould"%>
<%
List<CwbOrderView> cwborderList = request.getAttribute("cwborderList")==null?new ArrayList<CwbOrderView>():(List<CwbOrderView>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 

Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
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
	<form id="searchForm1" action ="<%=request.getContextPath()%>/logtoday/todayArrival" method = "post">
	<input type="hidden" name="branchid" value="<%=request.getAttribute("branchid")%>">
	</form>
	</div>
	<div class="right_title">
	<div style="height:20px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >客户</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">派送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >代收货款</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >实退款</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >差异金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >支付意愿</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >付款方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >订单状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >签收人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >签收日期</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >发货日期</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >入库日期</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >到站日期</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >领货日期</td>
				
		</tr>
		<% for(CwbOrderView c : cwborderList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getCustomername() %></td>
					<td  align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(Integer.parseInt(c.getCwbordertypeid())).getText() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee().subtract(c.getPaybackfee()) %></td>
					<td  align="center" valign="middle"><%=c.getPaytype_old()%></td>
					<td  align="center" valign="middle"><%=c.getPaytypeName() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText(c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=DeliveryStateEnum.getByValue((int)c.getDeliverystate()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getSigninman()%></td>
					<td  align="center" valign="middle"><%=c.getSignintime() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getInstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
					<td  align="center" valign="middle"><%=c.getPickGoodstime() %></td>
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




