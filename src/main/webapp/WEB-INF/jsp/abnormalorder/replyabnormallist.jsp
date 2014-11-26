<%@page import="cn.explink.dao.AbnormalWriteBackDAO"%>
<%@page import="cn.explink.domain.AbnormalWriteBack"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
List<AbnormalOrder> abnormalOrderList = (List<AbnormalOrder>)request.getAttribute("abnormalOrderList");

List<User> userList = (List<User>)request.getAttribute("userList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");
Page page_obj = (Page)request.getAttribute("page_obj");
  
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
  CwbDAO cwbDAO=ctx.getBean(CwbDAO.class);
  AbnormalWriteBackDAO abnormalWriteBackDAO = ctx.getBean(AbnormalWriteBackDAO.class);
  String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
})


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


function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于30天！");
		return false;
	}
	return true;
}

function Days(){     
	var day1 = $("#strtime").val();   
	var day2 = $("#endtime").val(); 
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>30){
		return false;
	}        
	return true;
}
function getThisBox(id){
	$.ajax({
		type: "POST",
		url:$("#handle"+id).val(),
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			viewBox();
		}
	});
}


function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}
function sumitForm(){
	if(check()){
		$("#searchForm").submit();
	}
}
</script>
</head>
<body style="background:#eef9ff;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="../toCreateabnormal">创建问题件</a></li>
				<li><a href="#"  class="light">问题件回复</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0; ">
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								问题件反馈时间：
									<label for="select2"></label>
									<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
									到
									<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
								&nbsp;&nbsp;处理结果：
								<select name="ishandle" id="ishandle">
									<option value="-1">全部</option>
									<option value="1">已回复</option>
									<option value="2">未回复</option>
								</select>
								<select name="abnormaltypeid" id="abnormaltypeid">
									<option value="0">请选择问题件类型</option>
									<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
										<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>25){%><%=at.getName().substring(0,25)%><%}else{%><%=at.getName() %><%} %></option>
									<%} %>
								</select>
								<input type="button" value="查询" class="input_button2" onclick="sumitForm();"/>
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="150" align="center" valign="middle" bgcolor="#E7F4E3">订单号</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
									<td width="110" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件类型</td>
									<td width="110" align="center" valign="middle" bgcolor="#E7F4E3">问题件反馈时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件说明</td>
									<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">回复人</td>
									<td width="110" align="center" valign="middle" bgcolor="#E7F4E3">回复时间</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:76px"></div>
					<div style="overflow-y:auto;height:400px;">
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
							<%if(abnormalOrderList!=null||abnormalOrderList.size()>0)for(AbnormalOrder abnormalOrder : abnormalOrderList){
								CwbOrder co = cwbDAO.getCwbOrderByOpscwbid(abnormalOrder.getOpscwbid());
								List<AbnormalWriteBack> abnormalWriteBackList = abnormalWriteBackDAO.getAbnormalOrderByOrderidAndType(abnormalOrder.getId(),AbnormalWriteBackEnum.HuiFu.getValue());
								AbnormalWriteBack abnormalWriteBack = abnormalWriteBackList.size()==0?null:abnormalWriteBackList.get(0);
								%>
							<tr height="30">
								<td width="150" align="center" valign="middle"><%=co.getCwb() %></td>
								<td width="100" align="center" valign="middle"><%if(customerlist!=null)for(Customer c : customerlist){if(co.getCustomerid()==c.getCustomerid()){ %><%=c.getCustomername() %><%}} %></td>
								<td width="110" align="center" valign="middle"><%=co.getEmaildate().substring(0, 19) %></td>
								<td width="100" align="center" valign="middle"><%if(abnormalTypeList!=null)for(AbnormalType at : abnormalTypeList){if(abnormalOrder.getAbnormaltypeid()==at.getId()){ %><%=at.getName() %><%}} %></td>
								<td width="110" align="center" valign="middle"><%=abnormalOrder.getCredatetime().substring(0, 19) %></td>
								<td width="100" align="center" valign="middle"><%=abnormalOrder.getDescribe() %></td>
								<td width="80" align="center" valign="middle"><%if(userList!=null)for(User u : userList){if(abnormalWriteBack!=null){if(abnormalWriteBack.getCreuserid()==u.getUserid()){ %><%=u.getRealname() %><%}}} %></td>
								<td width="110" align="center" valign="middle"><label for="select"></label>
									<label for="textfield"></label><%if(abnormalWriteBack!=null){ %><%=abnormalWriteBack.getCredatetime().substring(0, 19) %><%} %>
								</td>
								<td width="100" align="center" valign="middle">
								<input type="submit" name="button2" id="button2" value="查看" class="input_button2"  onclick="getThisBox('<%=abnormalOrder.getId()%>');"/>
								<input type="hidden" id="handle<%=abnormalOrder.getId()%>" value="<%=request.getContextPath()%>/abnormalOrder/getabnormalOrder/<%=abnormalOrder.getId()%>?type=2" />
								</td>
							</tr>
							<%} %>
						</tbody>
					</table>
					</div>
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
	</div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#abnormaltypeid").val(<%=request.getParameter("abnormaltypeid")==null?0:Long.parseLong(request.getParameter("abnormaltypeid"))%>);
$("#ishandle").val(<%=request.getParameter("ishandle")==null?0:Long.parseLong(request.getParameter("ishandle"))%>);
</script>
</body>
</html>

