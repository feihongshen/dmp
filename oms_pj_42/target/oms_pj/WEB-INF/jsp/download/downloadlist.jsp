<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.DateDayUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<DownloadManager> downloadlist = (List<DownloadManager>)request.getAttribute("downloadList");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
Page page_obj = (Page)request.getAttribute("page_obj");
int fileDay = Integer.parseInt(request.getAttribute("fileDay")==null?"7":request.getAttribute("fileDay").toString());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>下载管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
function gologin(){
	if(<%=request.getAttribute("nouser") != null%>){
		alert("登录已失效，请重新登录！");
	}
}
$(document).ready(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
		    	$("#searchForm").submit();
	   });
	});
$(function() {
	$("#strtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	
});

function exportField(id){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/download/chongxinxiazaiCheck/"+id,
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					$("#S"+id).html("<font color='gray'>排队中</font>");
					$("#T"+id).html("[<a href=\"javascript:;\" onclick=\"dell("+id+")\" >删除</a>]");
					$.ajax({
						type: "POST",
						url:"<%=request.getContextPath()%>/download/chongxinxiazai/"+id,
						dataType:"json",
						success : function(data) {
							
						}
					});
				}else{
					alert(data.remark);
				}
			}
		});	
	
}

function end(id){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/download/zhongzhi/"+id,
			dataType:"json",
			success : function(data) {
				if(data.errorCode==0){
					$("#S"+id).html("<font color='red'>暂停</font>");
					$("#T"+id).html("[<a href=\"javascript:;\" onclick=\"exportField("+id+")\" >重新导出</a>][<a href=\"javascript:;\" onclick=\"dell("+id+")\" >删除</a>]");
				}else{
					if(data.errorCode==2){
						alert("已完成导出,不能暂停！");
                        $("#S"+id).html("<font color='green'>完成</font>");
						$("#T"+id).html("[<a href=\"<%=request.getContextPath()%>/download/xiazai/"+id+"\">下载</a>][<a href=\"javascript:;\" onclick=\"dell("+id+")\" >删除</a>]");
					
					}else{
						alert(data.remark);
					}
					
				}
			}
		});	
	
}

function dell(id){
	if(confirm("确定删除？")){
		$.ajax({
			url:"<%=request.getContextPath()%>/download/del/"+id,//后台处理程序
			type:"POST",//数据发送方式 
			dataType:'json',//接受数据格式
			success:function(json){
				 alert("删除成功！");
				 $("#tableList").children().find("#TR"+id).remove();
				}
			   
		});
	}
}
 function show(id){
	 //alert(id);
 }
 
 function editInit(){
		//无处理
}
function editSuccess(data){

}

function edit_button2(key){
	$.ajax({
		type: "POST",
		url:$("#edit").val()+key,
		dataType:"html",
		success : function(data) {
			$("#alert_box").html(data);
		},
		complete:function(){
			editInit();//初始化ajax弹出页面
			$("#alert_box").show();
			window.parent.centerBox();
		}
	});
}
</script>
</head>

<body style="background:#eef9ff" onload="gologin();">
<div class="right_box">
	<div class="inputselect_box">
	<form action="" method="post" id="searchForm">
			导出时间：
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
			状态：
			<select name ="state" id ="state">
		           <option value ="-2">全部</option>
		           <option value ="-1" <%=request.getParameter("state")!=null&&request.getParameter("state").equals("-1")?"selected":"" %>>排队中</option>
		           <option value ="0" <%=request.getParameter("state")!=null&&request.getParameter("state").equals("0")?"selected":"" %>>导出中</option>
		           <option value ="1" <%=request.getParameter("state")!=null&&request.getParameter("state").equals("1")?"selected":"" %>>完成</option>
		           <option value ="2" <%=request.getParameter("state")!=null&&request.getParameter("state").equals("2")?"selected":"" %>>暂停</option>
		           <option value ="3" <%=request.getParameter("state")!=null&&request.getParameter("state").equals("3")?"selected":"" %>>已下载</option>
		    </select>
		    	
			<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			</form>
	</div>
	<div class="right_title">
	<div style="height:30px"></div><%if(downloadlist != null && downloadlist.size()>0){  %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  id="tableList">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >导出模块</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >导出时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >导出文件名</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >导出状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >文件有效期剩余(天)</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >操作</td>
		</tr>
		
		<% for(DownloadManager  c : downloadlist){ %>
				<tr bgcolor="#FF3300" id="TR<%=c.getId()%>" >
					<td  align="center" valign="middle"><%=ModelEnum.getByValue(c.getModelid()).getText() %></td>
					<td  align="center" valign="middle"><%=c.getCreatetime()  %></td>
					<td  align="center" valign="middle" ><%=c.getCnfilename() %></td>
					<td  align="center" valign="middle" id="S<%=c.getId()%>"><%=c.getStateStr() %></td>
					<td  align="center" valign="middle" ><%=(c.getState()==1 || c.getState()==3)?(fileDay -(DateDayUtil.getDaycha(c.getCreatetime(), new SimpleDateFormat("yyyy-MM-dd").format(new Date())))<0?
						0:fileDay -(DateDayUtil.getDaycha(c.getCreatetime(), new SimpleDateFormat("yyyy-MM-dd").format(new Date())))):"" %></td>
					<td  align="center" valign="middle" id="T<%=c.getId()%>" >
					<%if(c.getState()==0){ %>
					[<a href="javascript:;" onclick="end(<%=c.getId()%>)" >暂停</a>]
					<%}else if(c.getState()==1 || c.getState()==3){ %>
					[<a href="<%=request.getContextPath()%>/download/xiazai/<%=c.getId()%>">下载</a>]
					<%}else if(c.getState()==2){ %>
					[<a href="javascript:;" onclick="exportField(<%=c.getId()%>)" >重新导出</a>]
					<%} %>
					[<a href="javascript:;" onclick="dell(<%=c.getId()%>)" >删除</a>]
					</td>
					
				 </tr>
		 <%} %>
	</table>
	
	</div><%} %>
	<div class="jg_10"></div><div class="jg_10"></div>
	
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
	
	<div id="alert_box">
    </div>
<!-- 修改的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/download/show/" />
</body>
</html>

