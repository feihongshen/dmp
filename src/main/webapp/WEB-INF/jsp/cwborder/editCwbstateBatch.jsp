<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.controller.QuickSelectView" %>
<%@page import="cn.explink.enumutil.*" %>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

	List<QuickSelectView> qsvList = request.getAttribute("qsvList")==null?new ArrayList<QuickSelectView>():(List<QuickSelectView>)request.getAttribute("qsvList");
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	Map<Long,Customer> customerMap = request.getAttribute("customerMap")==null?new HashMap<Long,Customer>():(Map<Long,Customer>)request.getAttribute("customerMap");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量设置订单状态</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function(){
	$("#search").click(function(){
		$("#searchForm").submit();
	});
	$("#saveButton").click(function(){
		if($("#cwbstateSelect").val()==-1){
			alert("请选择您要变更的订单类型！");
			return false;
		}
		$("#cwbstate").val($("#cwbstateSelect").val());
		$("#searchForm").submit();
	});
});
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box" style="height:89px">
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100" rowspan="3" valign="middle" >订单号/运单号：</td>
					<td width="350" rowspan="3" valign="top" >
					   <textarea rows="4" cols="35" id ="cwbs"  name ="cwbs"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs") %></textarea>
					</td>
					<td align="left">
					 <input type="button"  value="查询" class="input_button1"  id="search"/>
					</td>
				</tr>
				<tr>
					<td align="left">
					<input type="hidden" id="cwbstate" name="cwbstate" value="-1" />
					<select id="cwbstateSelect" >
						<option value="-1">请选择订单类型</option>
						<%for(CwbStateEnum ct : CwbStateEnum.values()){%>
							<option value="<%=ct.getValue()%>" ><%=ct.getText() %></option>
						<%} %>
					</select>
					 <input type="button"  value="批量修改订单状态"  id="saveButton" class="input_button1"/>
					</td>
				</tr>
				<tr>
					<td align="left"><B>${msg}</B></td>
				</tr>
				
			</table>
		</form>
	</div>
	<div style="height:100px">&nbsp;</div>
	<div class="right_title">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
			<tr class="font_1">
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">快件状态</td>
				<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商运单号</td>
				<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
				<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">代收款</td>
			</tr>
		<% for(QuickSelectView qsv : qsvList){ %>
			<tr>
				<td align="center" valign="middle"><%for(CwbStateEnum ct : CwbStateEnum.values()){if(qsv.getCwbstate()==ct.getValue()){ out.print(ct.getText());break;}} %></td>
				<td align="center" valign="middle"><%=DeliveryStateEnum.getByValue((int)qsv.getDeliverystate()).getText() %></td>
				<td align="center" valign="middle"><%=FlowOrderTypeEnum.getText((int)qsv.getFlowordertype()).getText() %></td>
				<td align="center" valign="middle"><%if(qsv.getCustomerid()!=0){%><%=customerMap.get(qsv.getCustomerid()).getCustomername() %><%} %></td>
				<td align="center" valign="middle"><%if(qsv.getCwbremark().length()>50){%>……<%=qsv.getCwbremark().substring(qsv.getCwbremark().length()-50, qsv.getCwbremark().length())%><%} else{%><%=qsv.getCwbremark()%><%} %></td>
				<td align="center" valign="middle"><%=qsv.getCwb()%></td>
				<td align="center" valign="middle"><%=qsv.getShipcwb()%></td>
				<td align="center" valign="middle"><%=qsv.getEmaildate()%></td>
				<td align="right" valign="middle"><%=qsv.getReceivablefee().subtract(qsv.getBackcaramount()) %></td>
				
			</tr>
		<%} %>
		</table>
	</div>
	</form>
	<div class="jg_30"></div>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr >
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页</td>
			</tr>
		</table>
	</div>
	<%} %>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>

</html>

