<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.ShiXiao"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = request.getAttribute("customerList")==null?null:(List<Customer>)request.getAttribute("customerList");
List<User> userList = request.getAttribute("userList")==null?null:(List<User>)request.getAttribute("userList");
List<ShiXiao> shixiaoList = (List<ShiXiao>)request.getAttribute("shixiaoList");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox ul li").eq(index).show().siblings().hide();
	});
	
})


function exportField(){
	if(<%=shixiaoList!=null&&shixiaoList.size()!=0%>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}


function checkParam(){
	if($("#strtime").val()==""&&$("#endtime").val()==""&&$("#cwbs").val()==""){
		alert("请选择任一条件");
	}else{
		$("#searchForm").submit();
	}
}

</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/losecwbBatch">数据失效</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/selectlosecwb/1" class="light">数据失效查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
			<div style="position:relative; z-index:0 " >
				<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
					<div class="kfsh_search">
						<form action="<%=request.getContextPath() %>/cwborder/selectlosecwb/1" method="POST" id="searchForm">
							订单号：
							<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs"><%=request.getParameter("cwbs")==null?"":request.getParameter("cwbs")%></textarea>
							失效时间：
							<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
							到
							<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
							
							<input type="button" value="确定" class="input_button2" onclick="checkParam();">
						</form>
					</div>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1" height="30" >
								<td width="15%" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
								<td width="20%" align="center" valign="middle" bgcolor="#E7F4E3">失效时间</td>
								<td width="20%" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
								<td align="center" valign="middle" bgcolor="#E7F4E3">操作人</td>
							</tr>
							
						</tbody>
					</table>
				</div>
				<div style="height:100px"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tbody>
					<%if(shixiaoList!=null&&shixiaoList.size()!=0){for(ShiXiao shixiao:shixiaoList){%>
						
						<tr height="30" >
							<td width="15%" align="center" valign="middle"><%=shixiao.getCwb() %></td>
							<td width="15%" align="center" valign="middle"><%if(customerList!=null){for(Customer c : customerList){if(shixiao.getCustomerid()==c.getCustomerid()){%><%=c.getCustomername() %><%}}} %></td>
							<td width="20%" align="center" valign="middle"><%=shixiao.getCretime() %></td>
							<td width="20%" align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if(shixiao.getFlowordertype()==ft.getValue()){%><%=ft.getText() %><%}} %></td>
							<td align="center" valign="middle"><%if(userList!=null){for(User u : userList){if(shixiao.getUserid()==u.getUserid()){ %><%=u.getRealname() %><%}}} %></td>
						</tr>
						<%}} %>
					</tbody>
				</table>
				<div style="height:40px"></div>
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
		</div>
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</BODY>
</HTML>
