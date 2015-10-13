<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.ModelEnum"%>
<%@page import="cn.explink.domain.DeliveryChuku"%>
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
List<CwbOrder> clist = request.getAttribute("orderlist")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("orderlist");
Page page_obj = (Page)request.getAttribute("page_obj"); 

String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
Long kufangid = request.getParameter("kufangid")==null?0:Long.parseLong(request.getParameter("kufangid"));
Long nextbranchid = request.getParameter("nextbranchid")==null?0:Long.parseLong(request.getParameter("nextbranchid"));
Long customerid = request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid"));
List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
long count = (Long)request.getAttribute("count");
DeliveryChuku deliveryChukusum = request.getAttribute("deliveryChukusum")==null?null:(DeliveryChuku)request.getAttribute("deliveryChukusum");

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
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form id="searchForm2" action ="<%=request.getContextPath()%>/datastatistics/exportExcle" method = "post" > 
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:10px">
			<tr>
				<td align="left">
				<input type ="button" id="back" value="返回" class="input_button2" onclick="location.href='<%=request.getContextPath() %>/datastatistics/outwarehousecollectdata?begindate=<%=starttime %>&enddate=<%=endtime %>&kufangid=<%=kufangid%>&isshow=1'"/>
				
				<select name ="exportmould2" id ="exportmould">
		          <option value ="0">默认导出模板</option>
		          <%for(Exportmould e:exportmouldlist){%>
		           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
		          <%} %>
				</select>
				<input type="hidden" name="begin" id="begin" value="0"/>
				<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
				<input name="kufangid" id="kufangid" value="<%=kufangid %>" type="hidden"/>
				<input name="nextbranchid" id="nextbranchid" value="<%=nextbranchid %>" type="hidden"/>
				<input name="begindate" value="<%=starttime %>" type="hidden"/>
				<input name="enddate" value="<%=endtime %>" type="hidden"/>
				<input type="hidden" name="sign" id="sign" value="<%=ModelEnum.KuFangChuKuHuiZong.getValue() %>"/>
				<%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('0','0');"/>
				<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>');"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=count %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%} %>
			<%}} %>
			</td>
			</tr>
			</table>
	</form>
	</div>
	<div class="right_title">
	<div style="height:40px"></div>
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff">站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">代收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">地址</td>
			</tr>
			<%if(clist!=null) for(CwbOrder c : clist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getCustomername() %></td>
					<td  align="center" valign="middle"><%=c.getCustomerwarehousename() %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
				 </tr>
			 <%} %>
			<%if(request.getAttribute("count")!= null){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
					<td  align="center" valign="middle">&nbsp;</td>
					<td  align="center" valign="middle">&nbsp;</td>
					<td  align="center" valign="middle">&nbsp;</td>
					<td  align="center" valign="middle">&nbsp;</td>
					<td  align="center" valign="middle">&nbsp;</td>
					<td  align="center" valign="middle" class="high"><font color="red"><%=deliveryChukusum.getReceivablefee()==null?BigDecimal.ZERO:deliveryChukusum.getReceivablefee() %></font>&nbsp;元 </td>
					<td  align="center" valign="middle">&nbsp;</td>
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
	<form  id="searchForm" action="<%=request.getContextPath() %>/datastatistics/outwarehousecollectdatashow/1" method="post">
		<input name="customerid" id="customerid" value="<%=customerid %>" type="hidden"/>
		<input name="kufangid" value="<%=kufangid %>" type="hidden"/>
		<input name="nextbranchid" value="<%=nextbranchid %>" type="hidden"/>
		<input name="begindate" value="<%=starttime %>" type="hidden"/>
		<input name="enddate" value="<%=endtime %>" type="hidden"/>
	</form>
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
function exportField(page,j){
	if(<%=clist != null && clist.size()>0  %>){
		$("#btnval"+j).attr("disabled","disabled");
		$("#btnval"+j).val("请稍后……");
		$("#begin").val(j);
		$.ajax({
			type: "POST",
			url:$("#searchForm2").attr("action"),
			data:$("#searchForm2").serialize(),
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					alert("已进入导出任务队列！");
					$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
						data:$("#searchForm2").serialize(),
						dataType:"json",
						success : function(data) {
							
						}
					});
				}else{
					alert(data.remark);
				}
			}
		});	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}
</script>
</body>
</html>