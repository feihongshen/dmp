<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<OutWarehouseGroup> outwarehousegroupList = (List<OutWarehouseGroup>)request.getAttribute("outwarehousegroupList");
    List<Branch>  branchList = (List<Branch>)request.getAttribute("branchList");
    List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
    
    Page page_obj =(Page)request.getAttribute("page_obj");
    Map usermap = (Map) session.getAttribute("usermap");
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库交接单打印</title>
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
$(function(){
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
		location.href="<%=request.getContextPath() %>/warehousegroupdetail/outbillprinting_history/"+id+"?printtemplateid="+printtemplateid+"&isback=1";
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
					下一站：<select id ="branchid" name ="branchid" class="select1"> 
					              <option value ="0">全部</option>
					              <%for(Branch b:branchList){ %>
					                <option value ="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
					              <%} %>
					            </select>　
					        出库时间：<input type ="text" name ="beginemaildate"  class="input_text1" id ="beginemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>"/>
					        到
					              <input type ="text" name= "endemaildate"  class="input_text1" id ="endemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"/><input type="submit" id="find" value="查询" class="input_button2" />
				      <a href="<%=request.getContextPath() %>/warehousegroupdetail/returnlist/1">返回未打印列表 >></a>
				</form>
				</div>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr class="font_1">
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">发往</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">打印交接单</td>
					</tr>
					</table>
				
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					   <% for(OutWarehouseGroup og : outwarehousegroupList){ %>
					 <tr>
					 	<td width="20%" align="center" valign="middle">
					 	<%for(Branch branch:branchList){if(og.getBranchid()==branch.getBranchid()){ %>
							<%=branch.getBranchname() %>
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
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} else if (pt.getTemplatetype() == 4) {%>武汉飞远<%} %>）</option>
					  			<%} %>
							</select>
							<a href ="javascript:;" onclick="bdprint(<%=og.getId() %>,$('#printtemplateid<%=og.getId() %>').val());">退货出站交接单打印</a>
							<%if(og.getPrinttime().length()!=0){ %>
							（<%=og.getPrinttime() %>已打印）
							<%} %>
						</td>
					</tr>
					<%} %>
				</table>
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
$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
</script>
</body>
</html>

