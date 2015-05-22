<%@page import="cn.explink.util.Page"%>
<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Notify"%>
<%@page import="java.util.List"%>
<%
	List<Notify> nfs=request.getAttribute("notifylist")==null?new ArrayList<Notify>():(List<Notify>)request.getAttribute("notifylist");
	Page page_obj = (Page)request.getAttribute("page_obj");
	long currentpage=(Long)request.getAttribute("page");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath() %>/js/js.js" language="javascript"></script>
<style >
a{
	 text-decoration: none;
}
</style>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
	$("#now_path",parent.document).html("公告区");
	$(".gonggaolist2 li:odd").css("backgroundColor","#f9fcfd");
	$(".gonggaolist2 li:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$(".gonggaolist2 li:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});
	
	
	$("#selectPg").val(<%=currentpage%>);
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(<%=request.getContextPath() %>/images/right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(<%=request.getContextPath() %>/images/right_hideboxbtn.gif)");
			};
	})
});

/*控制通知展开和隐藏*/
$(function (){
	$(".gonggao_txt").hide();
	$(".gonggaolist2 li").click(function(){
		$(this).children(".gonggao_txt").toggle(200);
	});
});
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="kfsh_tabbtn" style="top: 0px; height:50px ">
	<table width="100%" style="padding:10px">
		<tr>
			<td align="center"><h1><b>公告区</b></h1></td>
		</tr>
	
	</table>
	
</div>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="get" id="searchForm"></form>
		<div style="margin-left:150px;margin-right:150px;margin-top:20px">
		<ul class="gonggaolist2" >
		<%if(nfs!=null&&nfs.size()>0){  long count=(currentpage-1)*Page.ONE_PAGE_NUMBER; for(Notify nf:nfs){ count++;%>
				<li  style="margin:5px;height:30px;">
					<h2>&nbsp;&nbsp;<%=count %>.&nbsp;&nbsp;<b><a title="<%=nf.getTitle() %>"  href="<%=request.getContextPath() %>/notify/show/<%=nf.getId() %>"  target="WORK_AREA" class="red" > <%=nf.getTitle().length()>=30?nf.getTitle().substring(0,30 )+"...":nf.getTitle() %></a></b></h2>
				</li>
		<%} }%>
		</ul>
		</div>
	
<!--底部翻页 -->
<div class="jg_35"></div>
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
</body>
</html>

