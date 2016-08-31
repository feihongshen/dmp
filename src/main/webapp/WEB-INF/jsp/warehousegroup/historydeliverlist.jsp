<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
List<OutWarehouseGroup> outwarehousegroupList = (List<OutWarehouseGroup>)request.getAttribute("outwarehousegroupList");
List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-

transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员领货交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$(function() {
	$("#beginemaildate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endemaildate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	<%if(pList.size()==0){%>
		alert("您还没有设置模版，请先设置模版！");
	<%}%>
	
});

function bdprint(id,printtemplateid){
	if(printtemplateid==null){
		alert("请选择打印模版！");
	}else{
		location.href="<%=request.getContextPath() %>/warehousegroupdetail/outbillprinting_history/"+id+"?printtemplateid="+printtemplateid+"&islinghuo=1";
	}
}
</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="1" method="post" id="searchForm">
					领货人：
					<select name="deliverid" id="deliverid" class="select1">
						<option value="0">请选择领货人</option>
						<%for(User u : deliverList){ %>
							<option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
						<%} %>
					</select>
					        
					  领货时间段：
				 	<input type ="text" name ="beginemaildate" id ="beginemaildate"  class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>" />&nbsp;到
					<input type ="text" name= "endemaildate" id ="endemaildate"  class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"/>
					<input type="submit" id="find"  value="查询" class="input_button2" />
					<a href="<%=request.getContextPath() %>/warehousegroup/deliverlist/1">返回未打印列表 >></a>
				</form>
				</div>
				
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr class="font_1">
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">打印交接单</td>
					</tr>
					</table>
				<form action="<%=request.getContextPath() %>/warehousegroup/searchAndPrintFordeliver_history" method="post" id="selectforsmtbdprintForm" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<% for(OutWarehouseGroup og : outwarehousegroupList){ %>
					 <tr>
					 	<td width="20%" align="center" valign="middle">
					 	<%for(User deliver:deliverList){if(deliver.getUserid()==og.getDriverid()){ %>
							<%=deliver.getRealname() %>
					     <%}} %>
					     </td>
						<td width="20%" align="center" valign="middle" ><%=og.getCredate() %></td>
						  <%for(OutwarehousegroupOperateEnum ooe : OutwarehousegroupOperateEnum.values()){
    	                     if(og.getOperatetype()==ooe.getValue()){%>
					    		<td width="20%" align="center" valign="middle">  <%=ooe.getText()%></td>
					       <% }
					    }%>
						<td width="20%" align="center" valign="middle" >
							<select name="printtemplateid<%=og.getId() %>" id="printtemplateid<%=og.getId() %>">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%}else if(pt.getTemplatetype()==4){ %>武汉飞远<%} %>）</option>
					  			<%} %>
							</select>
							<a href ="javascript:;" onclick="bdprint(<%=og.getId() %>,$('#printtemplateid<%=og.getId() %>').val());">领货交接单打印</a>
							<%if(og.getPrinttime().length()!=0){ %>
							<%} %>
						</td>
					</tr>
					<%} %>
				</table>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
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
$("#deliverid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("deliverid"))%>);
</script>
</body>
</html>
