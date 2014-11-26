<%@page import="cn.explink.domain.ServiceArea"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> cwbOrderList = request.getAttribute("cwborderList")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwborderList");
  Page page_obj =request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
  
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<style type="text/css">
li{ float:left; padding:0px 5px; list-style:none; width: 100px; border:1px dashed blue;float:left;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
  $(function(){
	  $("#sel").click(function(){
		    var str= $("#ordercwb").val();
			if($.trim(str).length==0){
				alert("订单号不能为空");
				return false;
			}
	});
	  $("#delsel").click(function(){
		    var str= $("#ordercwb").val();
			if($.trim(str).length==0){
				alert("请选择要删除的数据");
				return false;
			}
	});

  });
</script>

</HEAD>
<BODY>
<div>
<%if(request.getAttribute("delremand")!=null){%>
  <script type="text/javascript">
   alert("删除成功！");
  </script>
<%} %>
<%if(request.getAttribute("saveremand")!=null){%>
  <script type="text/javascript">
   alert("修改成功！");
  </script>
<%} %>
</div>
<form id="searchForm" action ="<%=request.getContextPath() %>/cwborder/delandedit/1" method = "post">
订单号：
 <textarea rows="3" cols="12" name ="ordercwb" id ="ordercwb"><%=StringUtil.nullConvertToEmptyString(request.getParameter("ordercwb")) %></textarea>[多个订单号用回车键隔开]
<input type ="submit" id ="sel"  value ="查询">
</form>

<%if(page_obj !=null && page_obj.getTotal()>0){ %>
<a href ="../del/<%=StringUtil.nullConvertToEmptyString(request.getParameter("ordercwb")) %>" id ="delsel" >删除查询的数据</a>
<div>
<a href="javascript:;" onclick="$('#searchForm').attr('action','1');$('#searchForm').submit()">第一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
<a href="javascript:;" onclick="$('#searchForm').attr('action','<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
	<div> 共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　</div>
	第<%=request.getAttribute("page")%>页 
</div>
<%} %>
<% for(CwbOrder c : cwbOrderList){ %>
	<div id ="divpg" style ="width:730px;height:25px;">
		<li><%=c.getCwb()%></li>
		<li><%=c.getState()%></li><li><%=c.getConsigneename()%></li>
		<li><a href ="../edit/<%=c.getOpscwbid()%>">修改</a></li>
	</div>
<%} %>

</BODY>
</HTML>
