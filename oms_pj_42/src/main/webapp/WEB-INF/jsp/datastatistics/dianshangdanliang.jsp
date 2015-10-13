<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.User,cn.explink.domain.Customer"%>
<%@page import="java.math.BigDecimal" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Object starttime=request.getParameter("begindate")==null || "".equals(request.getParameter("begindate"))?new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getParameter("begindate");

Object endtime=request.getParameter("enddate")==null || "".equals(request.getParameter("enddate"))?new SimpleDateFormat("yyyy-MM-dd").format(new Date()):request.getParameter("enddate");
List<Branch> kufanglist = request.getAttribute("kufanglist")==null?null:(List<Branch>)request.getAttribute("kufanglist");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
Map<Long,Map<String, Long>> customMap = request.getAttribute("customMap")==null?null:(Map<Long, Map<String, Long>>)request.getAttribute("customMap");
Object type=request.getAttribute("type");
List<String> dateList =(List<String>)request.getAttribute("dateList")==null?null: (List<String>)request.getAttribute("dateList"); 
long width = customerList.size()>0?(customerList.size()+1):0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$("#strtime").datepicker({
	});
	$("#endtime").datepicker({
	});
});


function checkParam(){
	if($("#strtime").val()=='' || $("#endtime").val() ==''){
		alert("请选择时间！");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	
	return true;
}

function sub(customerid){
	$('#customerid').val(customerid);
	$('#show').submit();
}
</script>
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
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
	<div>
		<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getContextPath() %>/datastatistics/dianshangdanliang"  method="post" id="searchForm" />
	<input type="hidden" id="isshow" name="isshow" value="1" />
		&nbsp;<select name="select" id="select">
			<option value="1">客户发货时间(默认)</option>
			<option value="2" >库房入库时间</option>
			<option value="3">库房出库时间</option>
		</select>
时间：
<input type ="text" name ="begindate" id="strtime"  value ="<%=starttime %>">
至
<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
&nbsp;发货库房：<select name ="kufangid" id ="kufangid">
			<option value="0">请选择</option>
	          <%if(kufanglist!=null && kufanglist.size()>0) {%>
	          <%for(Branch b : kufanglist){ %>
		 		<option value ="<%=b.getBranchid() %>" 
					<%if(b.getBranchid()==(request.getParameter("kufangid")==null?0:Long.parseLong(request.getParameter("kufangid")))){%>selected="selected"<%}%>><%=b.getBranchname()%>
				</option>
	          <%}}%>
		</select>
<input name="button" type="submit" class="input_button2" id="button" value="查询" />
</form>
</div>
						<!-- for循环 -->
						<div class="right_title">
	<div class="jg_35"></div>
	<div style="width:100%; overflow-x:scroll">
	<%if(customerList != null && customerList.size()>0){ %>
	<table width="1920" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td  rowspan="2" align="center" valign="middle" bgcolor="#eef6ff" width="10%">供货商</td>
	   		<%for(String date:dateList){ %>
	   		<td rowspan="2"  align="center" valign="middle" bgcolor="#eef6ff" ><%=date %></td>
	   		<%} %>
	   	 <td  rowspan="2" align="center" valign="middle" bgcolor="#eef6ff">合计</td> 
	   		</tr>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
			</tr>
		<%if(customerList != null && customerList.size()>0){ %>
		<%Map<Long ,Map<String,Long>> yingtouMap = new  HashMap<Long ,Map<String,Long>>();
		 %>
		<%for(Customer b:customerList){ %>
		<tr style="background-color: rgb(249, 252, 253); "  class="font_1">
			<td style="color:#000" align="center" valign="middle"><%=b.getCustomername()%></td>
			<%
			Map<String ,Long> yingtou = new HashMap<String ,Long>();
			long all=0;
			long count=0;
			%>
			<%
			for(String date:dateList){%>
			
			<%if(customMap != null && customMap.size()>0){ %>
			<%
		
			 count=customMap.get(b.getCustomerid()).get(date)==null?0:(Long)customMap.get(b.getCustomerid()).get(date);
			
			%>
				<td align="center"  valign="middle"  bgcolor="#f8f8f8" ><a  <%if(count==0){%><% }else{ %>href="javascript:$('#select').val(<%=type %>);selectPage('<%=date%>',<%=b.getCustomerid()%>);" <%} %>><%=count%></a></td>
				
			<%}else{%>
				<td  align="center" valign="middle">0</td>
			<%} all+=count;
			yingtou.put(date, count);
			%>  
			<% } %>
			<td bgcolor="#FFE900" align="center" valign="middle"><%=all %> </td> 
			</tr>
			<%
			yingtouMap.put(b.getCustomerid(), yingtou);
			
			%>
		<%}%>
		<tr style="background-color: rgb(249, 252, 253); ">
				<td rowspan="2"  align="center" valign="middle" bgcolor="#f1f1f1">合计</td>
		<%for(String date:dateList){
			long yingtouAll = 0;
		for(Customer b:customerList){ 
		  yingtouAll += yingtouMap.get(b.getCustomerid()).get(date);
		} %>
				
			
				<td bgcolor="#FFE900" align="center" valign="middle"><%=yingtouAll %></td>
				<%
				}
				%>
				<td bgcolor="#FFE900" align="center" valign="middle" bgcolor="#f1f1f1"></td>
			</tr>
		<% } }%>
		  
		
		</tbody>
</table>
<div class="jg_35"></div>
</div>	
	</div>
						
						
						
						
						
						
						
						
						
						
						<form action="<%=request.getContextPath() %>/datastatistics/searchDataFor/1"  method="post" id="searchForm1" >
						<input type="hidden" id="select"  name="select" value="<%=type%>" >
						<input type="hidden" id="searchbranchid"  name="searchbranchid" value="<%=request.getParameter("kufangid")==null?-1:request.getParameter("kufangid")%>" >
						<input type="hidden" id="searchtime"  name="searchtime" value="1">
						<input type="hidden" id="searchcustid"  name="searchcustid" value="1">
						</form>
</div></div>
<script type="text/javascript">
function selectPage(a,b){
	$("#isshow").val("1");
	$("#searchtime").val(a);
	$("#searchcustid").val(b);
	$("#searchForm1").submit();
	 /* $("#editBranchForm").submit(); */ 
}
</script>
</body>
</html>
   
