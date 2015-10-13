<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.OrderFlow"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<%
List<CwbOrder> oList =request.getAttribute("cwborder")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("cwborder");

String orderFlowStr =request.getAttribute("flowOrderStr")==null?"":request.getAttribute("flowOrderStr").toString();

 String isSearchFlag=request.getAttribute("isSearchFlag")==null?"":request.getAttribute("isSearchFlag").toString();
%>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>

$(document).ready(function() {
	$("#cwb").keydown(function(event){
		if(event.keyCode==13) {
			if($(this).val()==""){
				alert('请输入订单号');
				return false;
			}else{
				window.location.href="<%=request.getContextPath()%>/order/orderFlowQueryByCwb/?cwb="+this.value+"&isSearchFlag=1";
			}
		}
	});
	$("#btnsearch").click(function(){
		if($("#cwb").val()==""){
			alert('请输入订单号');
			return;
		}
		window.location.href="<%=request.getContextPath()%>/order/orderFlowQueryByCwb/?cwb="+$("#cwb").val()+"&isSearchFlag=1";
		
	});
});
function dgetViewBox(key){
	var durl = $("#view").val();
	window.parent.dgetViewBox(key,durl);
}
function gotoForm(cwb){
	window.location.href="<%=request.getContextPath()%>/order/selectOrder/1?cwb="+cwb;
}

</script>
</HEAD>

<body onload="$('#cwb').focus();">
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="height:495px">
	<tr align="center">
		<td width="420" valign="top">
			<div style="height:467px;  overflow-y:auto; overflow-x:hidden">
				<table width="100%" border="0" cellspacing="1" cellpadding="0"  >
					<tr class="font_1">
						<td width="100%" height="26" align="center" valign="middle" colspan ="2"  bgcolor="#eef6ff" style="font-size:20;">&nbsp;订单查询</td>
					</tr>
					<tr>
						<td width="100%" height="26" align="center" valign="middle" colspan ="2" bgcolor="#eef6ff">&nbsp;请输入订单号：
							<label for="textfield2"></label>
							<input type="hidden" name="isSearchFlag" value="1">
							<input type="text" name="cwb" id="cwb"  class="input_text2"  />
							<input type="button" name="btnsearch" value="查询" id="btnsearch"/>
						</td>
					</tr>
					<tr>
					    <td width="20%"> 　</td>
						<td width="80%" align="center" valign="top" valign="middle" >
							<table width="100%" border="0"  class="right_set2">
								<%if(oList!=null && oList.size()>0){ %>
								<% for(CwbOrder o:oList){%>
								<%if(o.getFlowordertypeMethod() != null){ %>
								<tr>
									<td width="15%" valign="middle">
										<p>订单状态：<font color="red"><%=o.getFlowordertypeMethod() %></font></p>
									</td>
								</tr>
								<%} %>
								<%=orderFlowStr %>
								<%break;}}else{ 
									if("1".equals(isSearchFlag)){ %>
										<center><font color="red">没有检索到数据!</font></center>
									<%}} %>
							</table>
						
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>
<!-- 操作日志的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/order/makelog/" />	
</body>
</html>