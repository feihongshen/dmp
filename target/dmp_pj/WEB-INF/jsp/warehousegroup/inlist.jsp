<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<OrderFlow> oflist = (List<OrderFlow>)request.getAttribute("oflist");
List<User> userList = (List<User>)request.getAttribute("userList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入库交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script type="text/javascript">
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
});
function cwbfind(){
	var now = new Date();
	var year=now.getFullYear();
	var month=now.getMonth()+1<10?("0"+(now.getMonth()+1)):((now.getMonth()+1)+"");
	var day=now.getDate()<10?("0"+now.getDate()):(now.getDate()+"");
	var nowdate = year+""+month+""+day;
	var zuijin7tian = Number(nowdate) - 7;
	
	var beginemaildate = Number($("#beginemaildate").val().replace("-","").replace("-",""));
	var endemaildate = Number($("#endemaildate").val().replace("-","").replace("-",""));
	if((beginemaildate!=0&&beginemaildate<Number(zuijin7tian))||(endemaildate!=0&&endemaildate<Number(zuijin7tian))){
		alert("抱歉，只能选择最近一周内的时间！");
	}else{
		$("#searchForm").submit();
	}
}
function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else{
		$("#selectforsmtbdprintForm").submit();
	}
}
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form action="1" method="post" id="searchForm" >
		 操作人：<select name="userid" id="userid">
			        <option value="0">请选择操作人</option>
			        <%for(User u :userList){ %>
			           <option value="<%=u.getUserid()%>"><%=u.getRealname()%></option>
			        <%} %>
		        </select>　
		        入库时间段：
				 <input type ="text" name ="beginemaildate" id ="beginemaildate"  class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>" />&nbsp;到
				 <input type ="text" name= "endemaildate" id ="endemaildate"  class="input_text1" value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>"/>
	      　　   每页<select name="onePageNumber" id="onePageNumber">
								<option value="10">10</option>
								<option value="30">30</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>行
	      <input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
	      <input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	      <input type="button" onclick="bdprint();" value="打印" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">操作人</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作类型</td>
			<td width="30%" align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
		</tr>
		</table>
	<form action="<%=request.getContextPath() %>/warehousegroupdetail/inlistprint" method="post" id="selectforsmtbdprintForm" >
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<%for(OrderFlow of:oflist){ %>
			 <tr>
			 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=of.getCwb() %>" checked="checked"/></td>
				<td width="25%" align="center" valign="middle"><%=of.getCwb() %></td>
				<td width="25%" align="center" valign="middle">  
				<%for(User u : userList){
  					   if(of.getUserid()==u.getUserid()){%>
			    		<%=u.getRealname()%>
			       <% }
			    }%>
			    </td>
			    <td width="20%" align="center" valign="middle">
				<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){
  					   if(of.getFlowordertype()==ft.getValue()){%>
			    		  <%=ft.getText()%>
			       <% }
			    }%>
			    </td>
			    <td width="30%" align="center" valign="middle" ><%=of.getCredate()%></td>
			</tr>
		<%} %>
		</table>
	</form>
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
$("#userid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("userid"))%>);
</script>
</body>
</html>
