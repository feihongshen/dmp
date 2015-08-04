<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOXODetailBean"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.CwbOXOStateEnum"%>
<%@page import="cn.explink.enumutil.CwbOXOTypeEnum"%>
<%@page import="cn.explink.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOXODetailBean> cwboxodetailList = (List<CwbOXODetailBean>)request.getAttribute("cwboxodetailList");
  List<Customer> cumstrListAll = (List<Customer>) request.getAttribute("cumstrListAll"); 
  List customeridList =(List) request.getAttribute("customeridStr");
  Page page_obj = (Page)request.getAttribute("page_obj");
  
  String OXOValue = CwbOrderTypeIdEnum.OXO.getValue()+ "";
  String OXOJITValue = CwbOrderTypeIdEnum.OXO_JIT.getValue() + "";
  
  String OXOText = CwbOrderTypeIdEnum.OXO.getText();
  String OXOJITText = CwbOrderTypeIdEnum.OXO_JIT.getText();
  
  String OXOStateUnProcessedValue = CwbOXOStateEnum.UnProcessed.getValue() +""; 
  String OXOStateProcessingValue = CwbOXOStateEnum.Processing.getValue() +""; 
  String OXOStateProcessedValue = CwbOXOStateEnum.Processed.getValue() +""; 
  
  String OXOStateUnProcessedText = CwbOXOStateEnum.UnProcessed.getText(); 
  String OXOStateProcessingText = CwbOXOStateEnum.Processing.getText(); 
  String OXOStateProcessedText = CwbOXOStateEnum.Processed.getText();
  
  
  String OXOTypePickValue = CwbOXOTypeEnum.pick.getValue() +""; 
  String OXOTypeDeliveryValue = CwbOXOTypeEnum.delivery.getValue() +""; 
  
  String OXOTypePickText = CwbOXOTypeEnum.pick.getText(); 
  String OXOTypeDeliveryText = CwbOXOTypeEnum.delivery.getText(); 
  
  String startDate = request.getParameter("startDate")==null?"":request.getParameter("startDate");
  String endDate = request.getParameter("endDate")==null?"":request.getParameter("endDate");
  String executeType = request.getParameter("executeType")==null?"":request.getParameter("executeType");
  String executeStatus = request.getParameter("executeStatus")==null?"":request.getParameter("executeStatus");
  String cwbordertypeid = request.getParameter("cwbordertypeid")==null?"":request.getParameter("cwbordertypeid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>指令查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#startDate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endDate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供应商' });
	$("input[name='customerid']").click(function(){
		//clickCustomerSelect();
	});
});
</script>

</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<form action="<%=request.getAttribute("page")%>" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:80px">
        <tr>
			<td width="33%">生产时间<input type="text" name="startDate" class="input_text1" value="<%= startDate%>"
							 id="startDate"  /> 
						   到<input type="text" name="endDate" id="endDate" class="input_text1" value="<%= endDate %>"
								 />
			</td>
			<td width="20%">执行类型&nbsp;&nbsp;<select name="executeType" id="executeType" class="select1">
			                        <option value="0"  <% if("0".equals(executeType)){ %>selected="selected" <% }%>>请选择</option>
		                            <option value="<%= OXOTypePickValue %>"  <% if(OXOTypePickValue.equals(executeType)){ %>selected="selected" <% }%>><%= OXOTypePickText %></option>
		                            <option value="<%= OXOTypeDeliveryValue %>"  <% if(OXOTypeDeliveryValue.equals(executeType)){ %>selected="selected" <% }%>><%= OXOTypeDeliveryText %></option>
							 </select>
			</td>
			<td>执行状态&nbsp;&nbsp;<select name="executeStatus" id="executeStatus" class="select1">
		                            <option value="<%= OXOStateUnProcessedValue %>"  <% if(OXOStateUnProcessedValue.equals(executeStatus)){ %>selected="selected" <% }%>><%= OXOStateUnProcessedText %></option>
		                            <option value="<%= OXOStateProcessingValue %>"  <% if(OXOStateProcessingValue.equals(executeStatus)){ %>selected="selected" <% }%>><%= OXOStateProcessingText %></option>
		                            <option value="<%= OXOStateProcessedValue %>"  <% if(OXOStateProcessedValue.equals(executeStatus)){ %>selected="selected" <% }%>><%= OXOStateProcessedText %></option>
							</select>
			</td>
		</tr>
		<tr>
			<td>
			供应商&nbsp;&nbsp;<select id="customerid" name="customerid" multiple="multiple" style="width: 217px;" >
					<%if(cumstrListAll != null && cumstrListAll.size()>0){ %>
					<%for( Customer c : cumstrListAll){ %>
					<option value="<%= c.getCustomerid()%>" <%if (!customeridList.isEmpty()) { 
					    	for (int i = 0; i < customeridList.size(); i++) {//for (String customerSelected : customeridList) {
					    		if(c.getCustomerid() == new Long(customeridList.get(i).toString())) {%>
					    			selected="selected"
					    			<%break;
					    		}
					    	}
					    }%>><%=c.getCustomername()%></option>
					<%} }%>
				</select> [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
				[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
			</td>
			<td>订单业务&nbsp;&nbsp;<select name="cwbordertypeid" id="cwbordertypeid"
								class="select1">
									<option value="0"  <% if("0".equals(cwbordertypeid)){ %>selected="selected" <% }%>>请选择</option>
		                            <option value="<%= OXOValue %>"  <% if(OXOValue.equals(cwbordertypeid)){ %>selected="selected" <% }%>><%= OXOText %></option>
		                            <option value="<%= OXOJITValue%>"  <% if(OXOJITValue.equals(cwbordertypeid)){ %>selected="selected" <% }%>><%=OXOJITText %></option>
							</select>
			</td>
			<td><input type="submit" id="find"
								onclick="$('#searchForm').attr('action',1);return true;"
								value="查询" class="input_button2" />
			</td>
		</tr>
	</table>		
	</form>
	</div>
	<div style="height:90px;"></div>
	<div class="right_title">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
			<tr class="font_1">
			   <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">供应商</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">生成时间</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">执行状态</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">揽件/配送站点</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">支付方式</td>
	           <td width="12%" align="center" valign="middle" bgcolor="#eef6ff">执行类型</td>
			</tr>
			<% for(CwbOXODetailBean c : cwboxodetailList){ %>
		    <tr>
		       <td  align="center" valign="middle"><%=c.getCwb()%></td>
		       <td  align="center" valign="middle"><%=c.getCustomerName()%></td>
		       <td  align="center" valign="middle"><%=c.getCreDate()%></td>
		       <td  align="center" valign="middle"><%=c.getCwbState()%></td>
		       <td  align="center" valign="middle"><%=c.getReceivablefee()%></td>
		       <td  align="center" valign="middle"><%=c.getBranchName()%></td>
		       <td  align="center" valign="middle"><%=c.getPayWay()%></td>
		       <td  align="center" valign="middle"><%=c.getOxoType()%></td>
		    </tr>
		    <%} %>
		</table>
	<div class="jg_10"></div>
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
<div class="jg_10"></div>
<div class="clear"></div>
<script type="text/javascript">
   $("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>