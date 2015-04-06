<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrderView> cwbViewList = (List<CwbOrderView>)request.getAttribute("cwbViewList");
  
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  long count = (Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史退货订单申请</title>
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
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
	         var checkval="";
	         $("label").each(function(index){
			     //找到被选中的项
			     if($(this).attr("class")=="checked"){
			        checkval+=$(this).children().attr("value")+",";
			     }
	         });
	         $("#controlStr").val(checkval);
	         if(check()){
	            $("#isshow").val(1);
		    	$("#searchForm").submit();
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
	         }
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
	else{
		return true;
	}
}
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
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}

</script>
</head>

<body style="background:#f5f5f5" >
<div class="right_box">
<div style="background:#FFF">
	<div class="kfsh_tabbtn">
		<ul>
			<li><a href="<%=request.getContextPath() %>/cwbapply/applytoTuiHuo">退货订单申请</a></li>
			<li><a href="<%=request.getContextPath() %>/cwbapply/applytoTuiHuolist/1" class="light">历史申请退货订单</a></li>
		</ul>
	</div>
	<div class="inputselect_box">
		<form action="1" method="post" id="searchForm">
		<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
		<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
			<tr>
				<td align="left">
					退货订单申请时间:
						<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
					到
						<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
					审核状态：
					<select name ="ishandle" id ="ishandle">
				          <option value ="-1">全部</option>
				           <option value ="0">未处理</option>
				           <option value ="1">已处理</option>
				           <option value ="2">客服拒审</option>
				    </select>
					<input type="button" id="find" onclick="" value="查询" class="input_button2" />
					&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField('0','0');"/>
				</td>
			</tr>
		</table>
		</form>
		<form action="<%=request.getContextPath()%>/cwbapply/exportExcle" method="post" id="searchForm2">
			<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
			<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
			<input type="hidden" name="ishandle1" id="ishandle1" value="<%=request.getParameter("ishandle")==null?"-1":request.getParameter("ishandle")%>"/>
		</form>
	</div>
	
	<div class="right_title">
	<div style="height:60px"></div>
	<%if(cwbViewList != null && cwbViewList.size()>0){  %>
	<div style="overflow-x:scroll; width:100% " id="scroll">
		<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >当前站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >退货站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >小件员</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >审核状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >审核人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >退货审核备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >审核时间</td>
			</tr>
		<% for(CwbOrderView  c : cwbViewList){ %>
			<tr bgcolor="#FF3300">
				<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
				<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
				<td  align="center" valign="middle"><%=c.getCwbordertypeid() %></td>
				<td  align="center" valign="middle"><%=c.getCurrentbranchname() %></td>
				<td  align="center" valign="middle"><%=c.getApplytuihuobranchname() %></td>
				<td  align="center" valign="middle"><%=c.getDeliverStateText() %></td>
				<td  align="center" valign="middle"><%=c.getDelivername() %></td>
				<td  align="center" valign="middle"><%if(c.getApplyishandle()==0){%>未审核<%}else if(c.getApplyishandle()==1){ %>已审核<%}else{ %>客户拒审<%} %></td>
				<td  align="center" valign="middle"><%=c.getApplyhandleusername() %></td>
				<td  align="center" valign="middle"><%=c.getApplyhandleremark() %></td>
				<td  align="center" valign="middle"><%=c.getApplyhandletime() %></td>
			 </tr>
		 <% }%>
		</table>
	</div>
	<%} %>
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
</div>
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#ishandle").val(<%=request.getParameter("ishandle")==null?"-1":request.getParameter("ishandle") %>);
</script>

<script type="text/javascript">
function exportField(page,j){
	if($("#isshow").val()=="1"&&<%=cwbViewList != null && cwbViewList.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval"+j).attr("disabled","disabled");
	 	$("#btnval"+j).val("请稍后……");
	 	$("#begin").val(page);
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

</script>


</body>
</html>

