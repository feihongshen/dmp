<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<OutWarehouseGroup> outwarehousegroupList = (List<OutWarehouseGroup>)request.getAttribute("outwarehousegroupList");
    List<Branch>  branchList = (List<Branch>)request.getAttribute("branchList");
    Page page_obj =(Page)request.getAttribute("page_obj");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />

<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
$(function(){

	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});

</script>
</HEAD>
<BODY>

<form action ="<%=request.getContextPath() %>/warehousegroup/changelist/1" method ="post" id ="searchForm" name ="searchForm">
   中转的站点：<select id ="branchid" name ="branchid"> 
              <option value ="0">全部</option>
              <%for(Branch branch:branchList){ %>
                <option value ="<%=branch.getBranchid()%>"><%=branch.getBranchname() %></option>
              <%} %>
            </select><br/>
    出库时间：从<input type ="text" name ="beginemaildate" id ="beginemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>">到
     <input type ="text" name= "endemaildate" id ="endemaildate" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"><br/>        
        <input type= "submit" value ="搜索">
</form>
<%if(page_obj.getTotal()>0){ %>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　</div>
	第<%=request.getAttribute("page")%>页 

	<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
	</select>

<table style ="width:500px;border:1px solid blue;">
  <tr>
    <td>ID</td>
    <td>时间</td>
    <td>司机</td>
    <td>车辆</td>
    <td>操作</td>
  </tr>
 <% for(OutWarehouseGroup og : outwarehousegroupList){ %>
  <tr>
    <td><%=og.getId() %></td>
    <td><%=og.getCredate() %></td>
    <td><%=og.getDriverid()%></td>
    <td><%=og.getTruckid() %></td>
    <td><a href ="../changebillprinting_default/<%=og.getId()%>">打印中转出站交接单</a></td>
  </tr>
  <%} %>
</table>
<%} %>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#branchid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchid"))%>);
</script>

</BODY>
</HTML>
