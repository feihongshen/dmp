<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.OutWarehouseGroup"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> driverList = (List<User>)request.getAttribute("driverList");
List<OutWarehouseGroup> owgAllList = (List<OutWarehouseGroup>)request.getAttribute("owgAllList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<title></title>
</head>
<body>
<form action ="<%=request.getContextPath() %>/warehousegroup/inboxlist/1" method="post" id="searchForm"> 
<div><%=(String)(request.getAttribute("errorState")==null?"":request.getAttribute("errorState")) %></div>
 驾驶员：<select name="userid" id="userid">
        <option value="0">请选择驾驶员</option>
        <%for(User u :driverList){ %>
           <option value="<%=u.getUserid()%>"><%=u.getUsername()%></option>
        <%} %>
      <input type="submit" value="查询"/>
 </form>

<%if(owgAllList!=null){ %>
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
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#userid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("userid"))%>);
</script>
<table>
  <tr>
    <td>id</td>
    <td>司机id</td>
  </tr>
  <%for(OutWarehouseGroup o:owgAllList){ %>
   <tr> 
    <td><%=o.getId()%></td><td><%=o.getDriverid()%></td>
    <td><a href ="../inboxsearchAndPrint/<%=o.getId()%>">入站交接单打印</a></td>
   </tr>
   <%} %>
  
</table>
<%} %>
</body>
</html>