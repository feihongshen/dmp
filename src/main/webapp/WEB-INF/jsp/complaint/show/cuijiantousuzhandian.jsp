<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.enumutil.ComplaintAuditTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Complaint"%>
<%@page import="javax.print.DocFlavor.STRING"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
    <%
    long type=(Long)request.getAttribute("auditType");
    List<Branch> deliverList1 = request.getAttribute("branchList")==null?null:( List<Branch>)request.getAttribute("branchList");
    Page page_obj = (Page)request.getAttribute("page_obj");
    long state=(Long)request.getAttribute("deliverystate");
    
    List<Complaint> list=request.getAttribute("List")==null?null:( List<Complaint>)request.getAttribute("List");
    List<User> nowUserList=request.getAttribute("nowUserList")==null?null:( List<User>)request.getAttribute("nowUserList");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>催件投诉</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">

$(function() {
	$("#startid").datetimepicker({
		
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	}
	);
	$("#endid").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
});

</script>
<script type="text/javascript">
function setFlag(b2cid){
 	  $.ajax({
			url:"<%=request.getContextPath()%>/complaint/updateZD/"+b2cid,  //后台处理程序
			type:"POST",//数据发送方式 
			dataType:'json',//接受数据格式
			success:function(html){
				if(html.errorCode==0){
					alert(html.error);
					location.reload();
				}
			}		
		});
    }
$(document).ready(function() {
	$("#find").click(function(){
		if($("#startid").val()=='' ||$("#endid").val()==''){
			alert("请选择时间段！");
			return false;
		}
		if(!Days()||($("#startid").val()=='' &&$("#endid").val()!='')||($("#startid").val()!='' &&$("#endid").val()=='')){
			alert("时间跨度为31天！！！");
			return false;
		}
		if($("#startid").val()>$("#endid").val()){
			alert("开始时间不能大于结束时间！");
			return false;
		}
	 	$("#isshow").val(1);
    	$("#searchForm").submit();
    	$("#find").attr("disabled","disabled");
		$("#find").val("请稍等..");
	});
	
});
function edit(data){
	alert(data);
	alert($("#edit").val()+data);
	$("#searchForm").attr('action',$("#edit").val()+data);
	$("#searchForm").submit();
}
function Days(){     
	var day1 = $("#startid").val();   
	var day2 = $("#endid").val(); 
	var y1, y2, m1, m2, d1, d2,min;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	min=m2*31-m1*31-d1+d2;
	if(min>31){
		return false;
	}        
	return true;
}
</script>
</head>

<body style="background:#f5f5f5;color:#0000FFF;">
	<div class="inputselect_box"   >
	<form action="<%=request.getAttribute("page")==null?1:request.getAttribute("page") %>" method="post" id="searchForm">
	<label>
	订单号<input type="text" value="" id="cwb" name="cwb" ></input></label>
		订单审核状态：
		<select name="auditType" >
		<option value="-1" <%if(type==-1){%>selected<%} %>>--请选择--</option>
		<option value="1" <%if(type==1){%>selected<%} %>>已处理</option>
		<option value="0" <%if(type==0){%>selected<%} %>>未处理</option>
		</select>
		催件时间：<input type ="text" value="<%=request.getAttribute("startid")==null?"":request.getAttribute("startid") %>" id="startid" name="startid"/>到：<input type ="text" value="<%=request.getAttribute("endid")==null?"":request.getAttribute("endid") %>" id="endid" name="endid"/>
		<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">处理人</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"> 生成时间</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">审核状态</td>
			<td width="42%" align="center" valign="middle" bgcolor="#eef6ff">客户投诉内容</td>
			<td width="22%" align="center" valign="middle" bgcolor="#eef6ff">处理</td>
		</tr>
		
		<%for(Complaint c:list){ %>
		 <tr >
			<td  align="center" valign="middle"><%=c.getCwb()%></td>
			<td  align="center" valign="middle"><%if(0==c.getAuditUser()){%>无<%}else{%><%=nowUserList.get(0).getRealname()%><%}%></td>
			<td  align="center" valign="middle"><%=c.getCreateTime()%></td>
			<td  align="center" valign="middle" id="thisshow"><%for(ComplaintAuditTypeEnum ct : ComplaintAuditTypeEnum.values()){if(c.getAuditType()==ct.getValue()){ %><%=ct.getText()%><%}} %></td>
			<td  align="center" valign="middle"><%=c.getContent()%></td>
			<td  align="center" valign="middle"><%if(0==c.getAuditUser()){%>[<a href="javascript:setFlag(<%=c.getId() %>);">处理</a>] <%}else{ %>已处理<%} %></td>
			<input type="hidden" id="id" name="id" value="<%=c.getId()%>">
		</tr>
		<%} %>
		
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	 <%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<!-- 删除问题件类型的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/complaint/updateZD/" />
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>