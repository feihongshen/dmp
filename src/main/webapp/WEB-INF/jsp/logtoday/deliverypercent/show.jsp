<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum" %>
<%@page import="cn.explink.enumutil.PaytypeEnum" %>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum" %>
<%@page import="cn.explink.enumutil.DeliveryStateEnum" %>
<%
List<CwbOrder> cwborderList = request.getAttribute("cwborderList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwborderList");
Page page_obj = (Page)request.getAttribute("page_obj"); 
long count=page_obj.getTotal();
Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
Map<Long,Branch> branchMap = request.getAttribute("branchMap")==null?new HashMap<Long,Branch>():(Map<Long,Branch>)request.getAttribute("branchMap");
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
function exportField(count,page){
	
	if(<%=page_obj.getTotal() > 0 %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval"+page).attr("disabled","disabled");
		$("#btnval"+page).val("请稍后……");
	 	$("#begin").val(page);
	 	$("#count1").val(count);
		$("#searchForm2").submit();	
		$("#reExport").show();
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

</script>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getContextPath()%>/percent/exportExcel" method="post" id="searchForm2">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
	<tr>
		<td align="left">
		
		<input type ="button" id="back" value="返回" class="input_button2" onclick="$('#searchForm1').submit()"/>
		&nbsp;<%if(page_obj.getTotal()>0){  %>
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select>	
			<%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('<%=count %>','0');"/>
			<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=count %>','<%=j%>');"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=count %>','<%=j%>');"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=count %>" class="input_button1" onclick="exportField('<%=count %>','<%=j%>');"/>
				<%} %>
			<%}} %>
			<a id="reExport" href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" style="display:none" >重导</a>
			<%} %>
			
		</td>
	</tr>
	</table>
	<input type="hidden" name="type" value="<%=request.getAttribute("type")==null?"":request.getAttribute("type")%>" >
	<input type="hidden" name="customerid" value="<%=request.getAttribute("customerid")==null?"0":request.getAttribute("customerid")%>" >
	<input type="hidden" name="begindate" value="<%=request.getAttribute("begindate")==null?"":request.getAttribute("begindate")%>" >
	<input type="hidden" name="enddate" value="<%=request.getAttribute("enddate")==null?"":request.getAttribute("enddate")%>" >
	<input type="hidden" name="exportmould2" id="exportmould2"  value="<%=request.getAttribute("exportmould")==null?"":request.getAttribute("exportmould")%>">
	<input type="hidden" id="begin" name="page" value="0" >
	<input type="hidden" id="count1" name="count" value="0" >
	
	</form>
	<form id="searchForm1" action ="<%=request.getContextPath()%>/percent/deliveryPercentByemaildate" method = "post">
	<input type="hidden" name="begindate" value="<%=request.getAttribute("begindate")==null?"":request.getAttribute("begindate")%>" >
	<input type="hidden" name="enddate" value="<%=request.getAttribute("enddate")==null?"":request.getAttribute("enddate")%>" >
	<input type="hidden" name="flowordertype" value="<%=request.getAttribute("flowordertype")==null?"0":request.getAttribute("flowordertype")%>" >
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
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >入库仓库</td>
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
					<td  align="center" valign="middle"><%=CwbOrderTypeIdEnum.getByValue(c.getCwbordertypeid()).getText() %></td>
					<td  align="center" valign="middle"><%=FlowOrderTypeEnum.getText(c.getFlowordertype()).getText() %></td>
					<td  align="center" valign="middle"><%=DeliveryStateEnum.getByValue(c.getDeliverystate()).getText() %></td>
					<td  align="center" valign="middle"><%=branchMap.get(Long.valueOf((c.getCarwarehouse()==null||c.getCarwarehouse().equals(""))?"-1":c.getCarwarehouse()))==null?"":branchMap.get(Long.valueOf(c.getCarwarehouse())).getBranchname() %></td>
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

