<%@page import="cn.explink.dao.AbnormalWriteBackDAO"%>
<%@page import="cn.explink.domain.ApplyEditDeliverystate"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<ApplyEditDeliverystate> applyEditDeliverystateList = (List<ApplyEditDeliverystate>)request.getAttribute("applyEditDeliverystateList");
List<User> userList = (List<User>)request.getAttribute("userList");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
Page page_obj = (Page)request.getAttribute("page_obj");
  
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
  CwbDAO cwbDAO=ctx.getBean(CwbDAO.class);
  String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
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
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
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

function sub(){
	if(check()){
		//applyeditdeliverystate
		$("#searchForm").submit();
	}
	
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath()%>/applyeditdeliverystate/toCreateApplyEditDeliverystate/1" >订单修改申请</a></li>
				<li><a href="#" class="light">历史申请记录</a></li>
				<li><a href="<%=request.getContextPath()%>/editcwb/start">重置反馈状态</a></li>
				<li><a href="<%=request.getContextPath()%>/editcwb/editCwbInfo">订单信息修改</a></li>
				<li><a href="<%=request.getContextPath()%>/editcwb/toSearchCwb/1">订单修改查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<div style="position:relative; z-index:0 " >
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								申请时间：
								<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1" style="height:20px;"/>
									到
									<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1" style="height:20px;"/>
								处理状态：
								<select name="ishandle" id="ishandle" class="select1">
									<option value="-1">请选择</option>
									<option value="<%=ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()%>">未处理</option>
									<option value="<%=ApplyEditDeliverystateIshandleEnum.YiChuLi.getValue()%>">已处理</option>
								</select>
								<input type="button" value="查询" class="input_button2" onclick="sub();">
							</form>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
							<tbody>
								<tr class="font_1" height="30" >
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">申请时间</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">当前站点</td>
									<td width="120" align="center" valign="middle" bgcolor="#eef6ff">申请站点</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">处理状态</td>
									<td width="100" align="center" valign="middle" bgcolor="#eef6ff">处理人</td>
									<td align="center" valign="middle" bgcolor="#eef6ff">修改配送结果</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:76px"></div>
					
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
						<%for(ApplyEditDeliverystate adse : applyEditDeliverystateList){ %>
						<% CwbOrder co = cwbDAO.getCwbByCwb(adse.getCwb()); %>
							<tr height="30" >
								<td width="120" align="center" valign="middle"><%=adse.getCwb() %></td>
								<td width="120" align="center" valign="middle"><%=adse.getApplytime() %></td>
								<td width="120" align="center" valign="middle"><%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){if(co.getStartbranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
								<td width="120" align="center" valign="middle"><%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){if(adse.getApplybranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
								<td width="100" align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(adse.getNowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></td>
								<td width="100" align="center" valign="middle"><%if(userList!=null&&userList.size()>0)for(User u : userList){if(adse.getDeliverid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></td>
								<td width="100" align="center" valign="middle">
								<%if(adse.getIshandle()==ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue())
									{ 
											if(adse.getAudit()==0){out.print("未处理");}
										else if(adse.getAudit()==1){out.print("审核已通过");}
										else if(adse.getAudit()==2){out.print("审核未通过");}
									}
								else if(adse.getIshandle()==ApplyEditDeliverystateIshandleEnum.YiChuLi.getValue())
									{ out.print("<font color='red'>已处理</font>");}
									%>
								</td>
								<td width="100" align="center" valign="middle"><%if(userList!=null&&userList.size()>0)for(User u : userList){if(adse.getEdituserid()==u.getUserid()){ %><font color="red"><%=u.getRealname() %></font><%}} %></td>
								<td align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(adse.getEditnowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></td>
							</tr>
						<%} %>
					</table>
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
$("#ishandle").val(<%=request.getParameter("ishandle")==null?-1:Long.parseLong(request.getParameter("ishandle"))%>);
$("#applybranchid").val(<%=request.getParameter("applybranchid")==null?0:Long.parseLong(request.getParameter("applybranchid"))%>);
</script>
</body>
</html>

