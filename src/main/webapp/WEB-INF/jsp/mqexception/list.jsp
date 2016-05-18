<%@page import="cn.explink.domain.MqException"%>
<%@page import="cn.explink.domain.MqExceptionBuilder.MessageSourceEnum"%>
<%@page import="cn.explink.enumutil.*,cn.explink.util.Page,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<MqException> siList = (List<MqException>)request.getAttribute("siList");
	Page page_obj = (Page)request.getAttribute("page_obj");
	long selectPg = (Long)request.getAttribute("page");
	String exceptionCode = (String)request.getAttribute("exceptionCode");
	String topic = (String)request.getAttribute("topic");
	String handleFlag = (String)request.getAttribute("handleFlag");
	String messageSource = (String)request.getAttribute("messageSource");
	String isAutoResend = (String)request.getAttribute("isAutoResend");
	String createdDtmLocStart = (String)request.getAttribute("createdDtmLocStart");
	String createdDtmLocEnd = (String)request.getAttribute("createdDtmLocEnd");
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/My97DatePicker/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function editSuccess(data){
	$("#searchForm").submit();
}

function loadExceptionCode(){
	var exceptionCode = '<%=exceptionCode %>';
	$.ajax({
		type : "POST",
		url : '<%=request.getContextPath()%>'+ '/mqexception/loadExceptionCode',
		dataType : "json",
		data:{},
		success : function(datas) {
			if($("#exceptionCode")){
				 $.each(datas, function(i, item) {
	                var option = $("<option></option>")
	                    .val(item)
	                    .text(item);
	                option.appendTo($("#exceptionCode"));
	                option.attr("width",150);
	                if(exceptionCode == item){
	                	option.attr("selected",true);//选中
	                }
	            });
			}
		}
	});
}

//提交函数
function submitFun(){
	var createdDtmLocStart = $("#createdDtmLocStart").val();
	if(createdDtmLocStart && createdDtmLocStart.length != 19){
		alert("日期格式有问题,不是{yyyy-MM-dd HH:mm:ss}，请检查！");
		return false;
	}
	var createdDtmLocEnd = $("#createdDtmLocEnd").val();
	if(createdDtmLocEnd && createdDtmLocEnd.length != 19){
		alert("日期格式有问题,不是{yyyy-MM-dd HH:mm:ss}，请检查！");
		return false;
	}
	$('#searchForm').attr('action',1);
	return true;
}

</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box" style="position:static;">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		编码：<select id="exceptionCode" name="exceptionCode" class="input_text1" style="height:21px;width:180px;word-wrap:normal;">
		    <option value=""></option>
		 </select> &nbsp;&nbsp;&nbsp;
		主题：<input type="text" id="topic" name="topic" class="input_text1"/>&nbsp;&nbsp;
		处理结果：<select id="handleFlag" name="handleFlag" class="input_text1" style="width:80px;height:21px;">
		        <option value="">所有</option>
				<option value="1">成功</option>
				<option value="0">失败</option>
			  </select>&nbsp;&nbsp;
			  
	             消息来源：<select id="messageSource" name="messageSource" class="input_text1" style="width:80px;height:21px;">
		        <option value="">所有</option>
				<option value="sender">发送端</option>
				<option value="receiver">接收端</option>
			  </select>&nbsp;&nbsp;
	            自动发送：<select id="isAutoResend" name="isAutoResend" class="input_text1" style="width:80px;height:21px;">
		        <option value="">所有</option>
				<option value="1">是</option>
				<option value="0">否</option>
			  </select>&nbsp;&nbsp;
	    <br/>
		时间：<input type="text" id="createdDtmLocStart" name="createdDtmLocStart" class="input_text1"/>	  &nbsp;&nbsp;
		至：<input type="text" id="createdDtmLocEnd" name="createdDtmLocEnd" class="input_text1"/>	  &nbsp;&nbsp;&nbsp;&nbsp;

		<input type="submit" onclick="return submitFun();" id="find" value="查询" class="input_button2" />
		<input type="button" id="updateHandleCount" value="重置次数" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	<tr class="font_1">
	        <td width="5%" align="center" valign="middle" ><input type="checkbox"  id="btn1" value="-1"/></td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">编码</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">主题</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">处理次数</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">结果</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">处理时间</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">消息来源</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">异常时间</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">自动重发</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">修改人</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">修改备注</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		 <% for(MqException si : siList){ %>
		<tr>
		    <td width="5%" align="center" valign="middle" > <input type="checkbox" name="checkboxup" value="<%=si.getId() %>"> </td>
			<td width="15%" align="center" valign="middle" ><%=si.getExceptionCode() %></td>
			<td width="12%" align="center" valign="middle" ><%=si.getTopic() %></td>
			<td width="5%" align="center" valign="middle" ><%=si.getHandleCount() %></td>
			<td width="5%" align="center" valign="middle" ><%=
			       (si.isHandleFlag() ? "成功" : "失败") 
			%></td>
			<td width="12%" align="center" valign="middle" ><%=(null != si.getHandleTime() ? dateFormat.format(si.getHandleTime()) : "") %></td>
			<td width="5%" align="center" valign="middle" ><%=
					MessageSourceEnum.getMessageSourceEnum(si.getMessageSource()).getName()
			%></td>
			<td width="12%" align="center" valign="middle" ><%=(null != si.getCreatedDtmLoc() ? dateFormat.format(si.getCreatedDtmLoc()) : "") %></td>
			<td width="5%" align="center" valign="middle" ><%=
			       (si.isAutoResend() ? "是" : "否") 
			%></td>
			<td width="6%" align="center" valign="middle" ><%=si.getUpdatedByUser() %></td>
			<td width="15%" align="center" valign="middle" ><%=si.getRemarks() %></td>
			<td width="5%" align="center" valign="middle" >
			[<a href="<%=request.getContextPath()%>/mqexception/edit/<%=si.getId() %>">修改</a>]
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
$(document).ready(function(){
	$("#selectPg").val('<%=selectPg %>');
	$("#exceptionCode").val('<%=exceptionCode %>');
	$("#topic").val('<%=topic %>');
	$("#messageSource").val('<%=messageSource %>');
	$("#handleFlag").val('<%=handleFlag %>');
	$("#isAutoResend").val('<%=isAutoResend %>');
	$("#createdDtmLocStart").val('<%=createdDtmLocStart %>');
	$("#createdDtmLocEnd").val('<%=createdDtmLocEnd %>');
	loadExceptionCode();
	
	//日期控件
	$("#createdDtmLocStart").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    readOnly: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#createdDtmLocEnd").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    readOnly: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	//全选功能
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkboxup']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkboxup']").removeAttr("checked");//取消全选  
		}	
	
	});
	
	//重置次数
	$("#updateHandleCount").click(function(){
		
		var ids = ","; 
		$("input[name='checkboxup']:checked").each(function(){  
			var id = $(this).val();
			ids = ids + id + ","
		}); 
		
		if(ids == ","){
			alert("无选中项！");
			return false;
		}else{
			ids = ids.substring(1, ids.length);//去掉前面的,
			ids = ids.substring(0, ids.length -1);//去掉前面的,
		}
		$.ajax({
			type: "POST",
			url:'<%=request.getContextPath()%>' + '/mqexception/updateHandleCount',
			data:{
				ids:ids
			},
			dataType:"json",
			async: false, //设为false就是同步请求
			success : function(data) {
			},
			error:function(data){
				if(data.readyState == 4){
					alert(data.responseText);
					$("#searchForm").submit();//重新查询
				}
			}
		});
	});
});
</script>
</body>
</html>