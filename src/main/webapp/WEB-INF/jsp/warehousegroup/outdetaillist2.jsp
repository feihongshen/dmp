<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
    List<PrintView> printList = (List<PrintView>)request.getAttribute("printList");
    List<PrintTemplate> pList = (List<PrintTemplate>)request.getAttribute("printtemplateList");
    String strtime=request.getParameter("strtime")==null?"":(String)request.getParameter("strtime");
    String endtime=request.getParameter("endtime")==null?"":(String)request.getParameter("endtime");
    String[] branchidList=request.getParameterValues("branchid");
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出库交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
function cwbfind(){
	var branchid = $('input[name="branchid"]:checked').length;
	 
	if($("#endtime").val() !=''&& $("#strtime").val() !=''&&$("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(branchid>0){
		$("#searchForm").submit();
	}else{
		alert("抱歉，请选择下一站点！");
	}
}
function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else if($("#printtemplateid").val()==0){
		alert("请选择打印模版！");
	}else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/outbillprinting_defaultnew");
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

$(function(){
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
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择下一站' });
	
	<%if(pList.size()==0){%>
		alert("您还没有设置模版，请先设置模版！");
	<%}%>
	$("#branchid").change(function(){
		$("#nextbranchid").val($(this).val());	
	});
	$("#templateid").change(function(){
		$("#printtemplateid").val($(this).val());	
	});
	
})

function cwbexport(){
	var isprint="";
	$('input[name="isprint"]:checked').each(function(){
		isprint=$(this).val();
	});
	if(isprint==""){
		alert("请选择要导出的订单！");
	}else{
		$("#selectforsmtbdprintForm").attr("action","<%=request.getContextPath() %>/warehousegroupdetail/exportforoutwarehouse");
		$("#selectforsmtbdprintForm").submit();
		
	}
	
}


</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
				<span>
				</span>
				<form action="1" method="post" id="searchForm">
					<input type="hidden"  name="isshow" value="1"  />
					<div style="float:right">   
				     	 打印模版：<select name="templateid" id="templateid">
					  			<%for(PrintTemplate pt : pList){ %>
					  				<option value="<%=pt.getId()%>"><%=pt.getName() %>（<%if(pt.getTemplatetype()==1){ %>按单<%}else if(pt.getTemplatetype()==2){ %>汇总<%} %>）</option>
					  			<%} %>
							</select>
				      <input type="button" onclick="bdprint();" value="打印" class="input_button2" />
				      <a href="<%=request.getContextPath() %>/warehousegroupdetail/historyoutlist/1/${type}">历史打印列表>></a>
					</div>
					 下一站：<select name="branchid" id="branchid" multiple="multiple" style="width:180px;">
				        <%for(Branch b :branchlist){ %>
				           <option value="<%=b.getBranchid()%>" 
				           <%if(branchidList!=null&&branchidList.length>0){
				        	for(int i=0;i<branchidList.length;i++){
			            	if(b.getBranchid()==Long.parseLong(branchidList[i])){
			            		%>selected="selected"<%
			            	     break;
			            	}}}%>><%=b.getBranchname()%></option>
				        <%} %>
			        </select>
			        [<a href="javascript:multiSelectAll('branchid',1,'请选择');">全选</a>]
					[<a href="javascript:multiSelectAll('branchid',0,'请选择');">取消全选</a>]
			      <%=request.getAttribute("time") %>
				<input type ="text" name ="strtime" id="strtime"  value="<%=strtime %>"/>
				到
				<input type ="text" name ="endtime" id="endtime"  value="<%=endtime %>"/>
				（未打印订单只保留15天）
		      　　<input type="button" id="find" onclick="cwbfind();" value="查询" class="input_button2" />
		      <%if(printList!=null&&printList.size()>0){ %>
		      　　<input type="button" id="forexport" onclick="cwbexport();" value="导出" class="input_button2" />
		      <%} %>
				</form>
				</div>
				<div class="right_title">
				<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
					<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="25%" align="center" valign="middle" bgcolor="#eef6ff">出库站点</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">出库时间</td>
					</tr>
				</table>
				<form action="" method="post" id="selectforsmtbdprintForm" >
				<input type="hidden"  name="flowordertype" value="<%=request.getAttribute("flowordertype") %>" />
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<%for(PrintView pv:printList){ %>
						 <tr>
						 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=pv.getCwb() %>" checked="checked"/></td>
							<td width="25%" align="center" valign="middle"><%=pv.getCwb() %></td>
							<td width="25%" align="center" valign="middle"><%=pv.getNextbranchname()%></td>
						    <td width="20%" align="center" valign="middle"><%=pv.getFlowordertypeMethod()%></td>
						    <td width="20%" align="center" valign="middle"><%=pv.getOutstoreroomtime()%></td>
						</tr>
					<%} %>
					</table>
					<input value="<%=strtime %>" name="starttime"  id="starttime" type="hidden"/>
					<input value="<%=endtime %>" name="endtime"  id="endtime" type="hidden"/>
					<input id="nextbranchid" name="nextbranchid" value="<%=request.getAttribute("branchids")%>" type="hidden"/>
					<input id="printtemplateid" name="printtemplateid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("templateid"))%>" type="hidden"/>
					<input name="type" value="<%=request.getAttribute("type") %>" type="hidden"/>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>




<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#printtemplateid").val(<%=StringUtil.nullConvertToEmptyString(request.getParameter("printtemplateid"))%>);

</script>
</body>
</html>

