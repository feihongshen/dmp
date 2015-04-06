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
		$("#searchForm").submit();
	}
}


function thisCheck_edit(){
	//$("#podresultid", parent.document).change(function(){//监控配送状态变化 对显示字段做相应处理
		var podresultid =  $("#podresultid", parent.document).val();
		init_deliverystate_edit();
		
		if(podresultid==<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>){//配送成功
			peisongObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>){//上门退成功
			shagnmentuiObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>){//上门换成功
			shangmenhuanObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.JuShou.getValue()%>){//拒收
			quantuiObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.BuFenTuiHuo.getValue()%>){//部分退货
			bufentuihuoObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>){//分站滞留
			zhiliuObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.ShangMenJuTui.getValue()%>){//上门拒退
			shangmenjutuiObj_edit();
		}else if(podresultid==<%=DeliveryStateEnum.HuoWuDiuShi.getValue()%>){//货物丢失
			huowudiushiObj_edit();
		}
	//});
}

function editInit(){
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
	thisCheck_edit();
	window.parent.click_podresultid_edit(<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>,<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>,
   			<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>,<%=DeliveryStateEnum.JuShou.getValue()%>,
   			<%=DeliveryStateEnum.BuFenTuiHuo.getValue() %>,<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>,
   			<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>,<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>,
   			$("#backreasonid", parent.document).val(),$("#leavedreasonid", parent.document).val(),$("#podremarkid", parent.document).val(),$("#newpaywayid", parent.document).val());
	$("input[type='text']", parent.document).focus(function(){
		$(this).select();
	});
}
</script>
</head>
<body style="background:#f5f5f5;overflow: hidden;" >
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="1" method="post" id="searchForm">
			&nbsp;&nbsp;订单号：
			<input type="text" name="cwb" id="cwb" value="" onKeyDown='if(event.keyCode==13){sub();}'>
			申请时间：
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
			申请机构：
				<select name="applybranchid" id="applybranchid">
					<option value="0">请选择</option>
					<%for(Branch b : branchlist){ %>
					<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
					<%} %>
				</select>
			处理状态：
				<select name="ishandle" id="ishandle">
					<option value="-1">请选择</option>
					<option value="<%=ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()%>">未处理</option>
					<option value="<%=ApplyEditDeliverystateIshandleEnum.YiChuLi.getValue()%>">已处理</option>
				</select>
			<input type="button"  value="筛选" class="input_button2" onclick="sub();">
		</form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
	<%if(applyEditDeliverystateList!=null&&applyEditDeliverystateList.size()>0){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody><tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   	<td width="10%" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">申请时间</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">申请人</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">订单类型</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">申请机构</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">当前配送结果</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">小件员</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">处理结果</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">申请修改配送结果</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">已修改结果</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">原因备注</td>
			<td align="center" valign="middle" bgcolor="#F3F3F3">操作</td>
		</tr>
		<%for(ApplyEditDeliverystate adse : applyEditDeliverystateList){ %>
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><%=adse.getCwb() %></strong></td>
			<td align="center" valign="middle"><%=adse.getApplytime() %></td>
			<td align="center" valign="middle"><%for(User u : userList){if(adse.getApplyuserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></td>
			<td align="center" valign="middle"><%for(CwbOrderTypeIdEnum coti : CwbOrderTypeIdEnum.values()){if(adse.getCwbordertypeid()==coti.getValue()){ %><%=coti.getText() %><%}} %></td>
			
			<td align="center" valign="middle"><%for(Branch b : branchlist){if(adse.getApplybranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></td>
			<td align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(adse.getNowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></td>
			<td align="center" valign="middle"><%for(User u : userList){if(adse.getDeliverid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></td>
			<td align="center" valign="middle"><%if(adse.getIshandle()==ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()){ %>未处理<%}else{ %>已处理<%} %></td>
			<td align="center" valign="middle"><%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(adse.getEditnowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %></td>
			<td align="center" valign="middle">
			<%for(DeliveryStateEnum dse : DeliveryStateEnum.values()){if(adse.getEditnowdeliverystate()==dse.getValue()){ %><%=dse.getText() %><%}} %>
			</td>
			<td align="center" valign="middle"><%=adse.getEditreason() %></td>
			<td align="center" valign="middle"><!-- <input type="button" name="button2" id="button2" value="修改"> -->
			<%if(adse.getIshandle()==ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()){ %>
			<input type="button" name="button2" id="button2" value="修改" onclick="edit_button(<%=adse.getId()%>);" class="input_button2">
			<%}else{ %>
			<input type="button" name="button2" id="button2" value="已修改" >
			<%} %>
		</tr>
		<%} %>
		</tbody>
	</table><%} %>
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
</div>
<input type="hidden" id="edit"  value="<%=request.getContextPath()%>/applyeditdeliverystate/handleApplyEditDeliverystateByid/"/>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#ishandle").val(<%=request.getParameter("ishandle")==null?-1:Long.parseLong(request.getParameter("ishandle"))%>);
$("#applybranchid").val(<%=request.getParameter("applybranchid")==null?0:Long.parseLong(request.getParameter("applybranchid"))%>);
</script>
</body>
</html>

