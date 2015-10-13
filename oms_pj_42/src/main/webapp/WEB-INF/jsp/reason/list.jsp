<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
  Page page_obj = (Page)request.getAttribute("page_obj");
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

</HEAD>
<BODY>
<form action ="1" method ="post" id ="searchForm">
请选择类型：<select name ="reasontype" id="reasontype">
               <option value ="-1">全部</option>
               <option value ="<%=ReasonTypeEnum.ChangeTrains.getValue()%>"><%=ReasonTypeEnum.ChangeTrains.getText() %></option>
               <option value ="<%=ReasonTypeEnum.BeHelpUp.getValue()%>"><%=ReasonTypeEnum.BeHelpUp.getText()%></option>
               <option value ="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"><%=ReasonTypeEnum.ReturnGoods.getText()%></option>
               <option value ="<%=ReasonTypeEnum.GiveResult.getValue()%>"><%=ReasonTypeEnum.GiveResult.getText()%></option>
           </select>
    <input type ="submit" value ="查询">
    <a href = "../add">新建</a>
</form>
<%if(page_obj.getTotal()!=0){ %>
<div>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
</div>
<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　</div>
	<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
		<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
			<option value="<%=i %>"><%=i %></option>
		<% } %>
	</select>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#reasontype").val(<%=request.getAttribute("reasontype") %>);
</script>
  <table>
   <%for(Reason r:reasonList){ %>
    <tr>
      <td><%=r.getReasonid() %></td>
      <td><%=r.getReasoncontent()%></td>
      <td><%=r.getReasontype() %></td>
      <td><a href ="../edit/<%=r.getReasonid() %>">修改</a></td>
    </tr>
    <%} %>
  </table>
 <%} %>
</BODY>
</HTML>
