<%@page import="java.util.Map"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cn.explink.domain.Notify"%>
<%@page import="java.util.List"%>
<%
	List<Notify> nfs=request.getAttribute("notifylist")==null?new ArrayList<Notify>():(List<Notify>)request.getAttribute("notifylist");
	Page page_obj = (Page)request.getAttribute("page_obj");
	long currentpage=(Long)request.getAttribute("page");
	Map<Long,String> usermap=(Map<Long,String>)request.getAttribute("usermap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath() %>/js/js.js" language="javascript"></script>
   
 
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
	$("#selectPg").val(<%=currentpage%>);
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right");
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	})
	
	
	
});
function addInit(){
}


function del(){
	var ids="";
	$('input[type="checkbox"]:checked').each(
		function(){
			ids+=$(this).val()+",";
		}		
	
	);
	if(ids.length>0){
		var flag= confirm("确定删除？");
		if(flag){
			ids=ids.substring(0,ids.length-1);
			$.ajax({
				type:"post",
				url:$("#del").val()+ids,
				dataType:"json",
				success:function(data){
					$(".tishi_box").html(data.error);
					$(".tishi_box").show();
					setTimeout("$(\".tishi_box\").hide(10000)", 2000);
				},
			complete:function(){
				$("#refrash").click();
			}
			});
		}
	
	}else{
		alert("请选择你要删除的通知");
		return;
		
	}
	
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<input type="hidden" id="refrash" onclick="location.href='<%=request.getContextPath() %>/notify/managelist/1'" />
<div class="kfsh_tabbtn" style="top: 0px; height:40px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm"></form>
		<table width="100%" style="padding:10px">
		<tr>
			<td align="left">
				<input type="button" class="input_button2" onclick="location.href='<%=request.getContextPath() %>/notify/add'" value="新增">
				<input type="button" class="input_button2" onclick="del();" value="删除">
			</td>
		</tr>
		</table>
</div>
	<div style="height:20px"></div>
	<form action="" method="get"><table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2" id="gd_table">
		<tbody>
			<tr class="font_1" height="30">
				<td width="30" bgcolor="#eef6ff">全选</td>
				<td width="50" bgcolor="#eef6ff">序号</td>
				<td width="80" bgcolor="#eef6ff" >类别</td>
				<td bgcolor="#eef6ff" >主题</td>
				<td width="120" bgcolor="#eef6ff" >发布时间</td>
				<td width="80" bgcolor="#eef6ff" >发布人</td>
				<td width="120" bgcolor="#eef6ff" >修改时间</td>
				<td width="80" bgcolor="#eef6ff" >修改人</td>
				<td width="80" bgcolor="#eef6ff" >操作</td>
			</tr>
			<%if(nfs!=null&&nfs.size()>0){  long count=(currentpage-1)*Page.ONE_PAGE_NUMBER ;for(Notify nf:nfs){
				count++;
			%>
			<tr>
				<td><input type="checkbox" name="checkbox" value="<%=nf.getId() %>">
				<label for="checkbox"></label></td>
				<td><%=count %></td>
				<td><%=nf.getType()==1?"公告":"通知" %></td>
				<td><a href="<%=request.getContextPath() %>/notify/showmanage/<%=nf.getId() %>" title="<%=nf.getTitle() %>"><%=nf.getTitle().length()>30?nf.getTitle().substring(0,30)+"...":nf.getTitle() %></a></td>
				<td><p><%=nf.getCretime() %></p></td>
				<td><%if(usermap!=null&&usermap.containsKey(nf.getCreuserid())){%><%=usermap.get(nf.getCreuserid())%><%} %></td>
				<td><%=StringUtil.nullConvertToEmptyString(nf.getEdittime())%></td>
				<td><%if(nf.getEdituserid()>0){ if(usermap!=null&&usermap.containsKey(nf.getEdituserid())){%><%=usermap.get(nf.getEdituserid()) %><%}} %></td>
				<td>
					[<a href="<%=request.getContextPath() %>/notify/edit/<%=nf.getId() %>">修改</a>]
					<%if(nf.getType()==1&&nf.getIstop()==0){ %>
					[<a href="<%=request.getContextPath() %>/notify/totop/<%=nf.getId() %>">置顶</a>]
					<%} %>
				</td>
			</tr>
			<%} }%>
			
		</tbody>
	</table></form>
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
	
	<input type="hidden" value="<%=request.getContextPath() %>/notify/add"  id="add"/>
	<input type="hidden" value="<%=request.getContextPath() %>/notify/del/"  id="del"/>
	<input type="hidden" value="<%=request.getContextPath() %>/notify/edit"  id="edit"/>
</body>
</html>
