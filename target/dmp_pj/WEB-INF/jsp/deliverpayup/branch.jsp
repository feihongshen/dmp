<%@page import="cn.explink.util.Page"%>
<%@page import="groovy.json.JsonOutput"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="cn.explink.controller.DeliveryStateView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.controller.DeliveryStateDTO"%>
<%@page import=" cn.explink.domain.DeliveryState"%>
<%@page import=" cn.explink.domain.Customer"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import=" cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import=" cn.explink.enumutil.DeliveryTongjiEnum"%>
<%@page import=" cn.explink.domain.*"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.DeliverPayupArrearageapprovedEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

List<JSONObject> viewList = request.getAttribute("viewList")==null?null:(List<JSONObject>)request.getAttribute("viewList");

String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
List<User> deliverList = request.getAttribute("deliverList")==null?null:( List<User>)request.getAttribute("deliverList");
Map<Long ,String> deliverMap = request.getAttribute("deliverMap")==null?null:( Map<Long ,String>)request.getAttribute("deliverMap");
List<GotoClassAuditing> gcaList = request.getAttribute("gcaList")==null?null:( List<GotoClassAuditing>)request.getAttribute("gcaList");
Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>站点交款</title>
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
<script>
$(function(){

	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
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
function check(){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}

$(function(){
	$("#find").click(function(){
		if(check()){
			$("#searchForm").submit();
		}
	});
});
function updataDiv(id){
	$('#div'+id).css('display','');
}

function checkPrice(price){
	  return (/^(([1-9]\d*)|\d)(\.\d*)?$/).test(price.toString());
	}
	
function updata(id){
	$("#money").val("");
	$("#gcaid").val("");
	if(!checkPrice($('#Money'+id).val() )){
		alert("输入金额格式有误！");
		return false;
	}
	
	$("#money").val($('#Money'+id).val());
	$("#gcaid").val(id);
	if(confirm("确定修改金额重新提交？")){
    	$("#updateForm").submit();
	}
	
}
</script>
</head>

<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
		<form action="<%=request.getContextPath() %>/deliverpayup/branch/1" method="post" id="searchForm" >
			<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
			&nbsp;&nbsp;归班时间：
			<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
			
			<select name ="deliverealuser" id ="deliverid">
						          <option value ="-1">请选择小件员</option>
						           <%if(deliverList!=null&&deliverList.size()>0)for(User u : deliverList){ %>
						          <option value ="<%=u.getUserid() %>" 
						           <%if(u.getUserid()==Integer.parseInt(request.getParameter("deliverealuser")==null?"-1":request.getParameter("deliverealuser"))){ %>selected="selected" <%} %> ><%=u.getRealname()%></option>
						          <%}%>
							 </select>
							 财务审核状态:
					审核状态：
			<select name ="deliverpayupapproved" id ="deliverpayupapproved">
						          <option value ="-1">请选择</option>
						         <%for(DeliverPayupArrearageapprovedEnum c : DeliverPayupArrearageapprovedEnum.values()){ %>
				<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("deliverpayupapproved")==null?"-1":request.getParameter("deliverpayupapproved"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		         <%} %>
							 </select>			 
		<input id="find" value="查询" class="input_button2"/>
		</form>
		<form id="updateForm" action ="<%=request.getContextPath()%>/deliverpayup/updateMoney"  method = "post">
			<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
            <input type="hidden" id="money" name="money" value=""/>
            <input type="hidden" id="gcaid" name="gcaid" value=""/>
            <input type="hidden" name="deliverealuser" value="<%=request.getParameter("deliverealuser")==null?"-1":request.getParameter("deliverealuser")%>"/>
            <input type="hidden" name="begindate" value="<%=starttime %>"/>
            <input type="hidden" name="enddate" value="<%=endtime %>"/>
            <input type="hidden" name="deliverpayupapproved1" id="deliverpayupapproved1" value="<%=request.getParameter("deliverpayupapproved")==null?"-1":request.getParameter("deliverpayupapproved")%>"/>
        </form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="10%" align="center" valign="middle" bgcolor="#f3f3f3">小件员</td>
	   		<td align="center" valign="middle" bgcolor="#EEF6FF">交款方式</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">现金应收</td>
			<td align="center" valign="middle" bgcolor="#E7F4E3">现金已上交</td>
			<td align="center" valign="middle" bgcolor="#E7F4E3">交款时余额</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">现金欠款</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">POS应收</td>
			<td align="center" valign="middle" bgcolor="#E7F4E3">POS已上交</td>
			<td align="center" valign="middle" bgcolor="#E7F4E3">交款时POS余额</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">POS欠款</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">归班时间</td>
			<td align="center" valign="middle" bgcolor="#EEF6FF">财务审核备注</td>
			<td  align="center" valign="middle" bgcolor="#FFEFDF">财务审核状态</td>
		</tr>
		<%if(gcaList!=null && gcaList.size()>0){%>
			<% for(GotoClassAuditing view : gcaList){ %>
		
		<tr style="background-color: rgb(249, 252, 253); ">
			<td align="center" valign="middle" bgcolor="#f3f3f3"><%=deliverMap.get(view.getDeliverealuser()) %></td>
			<td align="center" valign="middle"><%=view.getDeliverpayuptypeStr() %></td>
			
			<td align="center" valign="middle"><%=view.getPayupamount() %></td>
			<td align="center" valign="middle"><%=view.getDeliverpayupamount()%></td>
			<td align="center" valign="middle"><%=view.getDeliverAccount()%></td>
			<td align="center" valign="middle"><%=view.getDeliverpayuparrearage().negate() %></td>
			
			<td align="center" valign="middle"><%=view.getPayupamount_pos() %></td>
			<td align="center" valign="middle"><%=view.getDeliverpayupamount_pos()%></td>
			<td align="center" valign="middle"><%=view.getDeliverPosAccount()%></td>
			<td align="center" valign="middle"><%=view.getDeliverpayuparrearage_pos().negate() %></td>
			<td align="center" valign="middle"><%=view.getAuditingtime() %></td>
			<td align="center" valign="middle"><%=view.getCheckremark()%></td>
			<td align="center" valign="middle">
			<%if(view.getDeliverpayupapproved() ==DeliverPayupArrearageapprovedEnum.WeiTongGuo.getValue() ) {%>
			<input type="button" value="未通过" onclick="updataDiv(<%=view.getId()%>);" class="input_button2" />
			<div style="display: none;" id="div<%=view.getId()%>"> <input type="text" id="Money<%=view.getId()%>" ></input> <input type="button" value="修改金额提交" onclick="updata(<%=view.getId()%>);" class="input_button2" /> </div>
			<%} else{%>
			<%=view.getDeliverpayupapprovedStr()%>
			<%} %>
			</td>
		</tr>
		<%}} %>
		</tbody>
	</table>
	</div>
	
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
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
						<select id="selectPg" onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
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
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>
