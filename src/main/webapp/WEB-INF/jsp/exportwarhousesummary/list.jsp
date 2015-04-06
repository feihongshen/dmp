<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="net.sf.json.JSONObject"%>
 <%

 List<Branch> warhouseList =(List<Branch>) request.getAttribute("warhouseList");
 List<Branch> zhandianes =(List<Branch>) request.getAttribute("zhandianes");

 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>按配送结果货款管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
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
function check(){
	if($(".multiSelectOptions input[name='warhouseid']:checked").size()==0){
		alert("请选择库房");
		return false;
	}
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于7天！");
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
	if(days>7){
		return false;
	}        
	return true;
}
</script>

<script>
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
	$("#warhouseid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择库房' });
	$("input[name='warhouseid']").click(function(){//获得库房点击后的 被选择库房的值，用于删选批次，和控制
		clickCustomerSelect();
	});
});
$(function(){
$("#find").click(function(){
	if(check())
		{$("#searchForm").submit();}
	});});
</script>   

</head>

<body style="background:#f5f5f5" >

	<div class="right_box" >
		<div class="inputselect_box">
			<form id="searchForm"
				action="<%=request.getContextPath()%>/exportwarhousesummary/search"
				method="post">
				库房：<select id="warhouseid" name="warhouseid" multiple="multiple"
					style="width: 100px;">
					<%if(warhouseList != null && warhouseList.size()>0){ %>
					<%for( Branch c:warhouseList){ %>
					<option value="<%= c.getBranchid()%>"><%=c.getBranchname()%></option>
					<%} }%>
				</select> [<a href="javascript:multiSelectAll('warhouseid',1,'请选择');">全选</a>]
				[<a href="javascript:multiSelectAll('warhouseid',0,'请选择');">取消全选</a>]

				出库时间： <input type="text" name="strtime" id="strtime">
				到 <input type="text" name="endtime" id="endtime">
			<button type="button" id="find">查询</button>
			</form>
			<div id="data">
				<div style="height: 5px"></div>
				<div style="position: relative; z-index: 0; width: 100%;margin-left: -11px">
					<table width="100%" border="0" cellspacing="1" cellpadding="0"
						class="table_2" style="height: 30px;">
						<tbody>
							<tr class="font_1" height="30"
								style="background-color: rgb(255, 255, 255);">
								<td colspan="4" align="center" valign="middle" bgcolor="#eef6ff">出库情况</td>
							</tr>
						</tbody>
					</table>
					<div
						style=" width: 220px; position: absolute; left: 0; top: 32px; overflow: hidden; z-index: 8; border-right: 1PX solid #9CC">
						<table width="220" border="0" cellspacing="1" cellpadding="0"
							class="table_2">
							<tbody>
								<tr class="font_1" height="30"
									style="background-color: rgb(255, 255, 255);">
									<td width="110px" align="center" valign="middle"bgcolor="#F1F1F1">日期</td>
									<td width="110px" align="center" valign="middle" bgcolor="#F1F1F1">入库数量</td>
								</tr>
								<tr class="font_1" height="30">
								</tr>
							</tbody>
						</table>
					</div>
				
					<div
						style="overflow-x: scroll; position: absolute; right: 0; top: 32px; width: 100%;"
						align="center">
						<table width="<%=zhandianes.size()*120 %>" border="0" cellspacing="1" cellpadding="0"
							class="table_2" id="gd_table"
							style="margin-left: 220px;">
							<tbody>
								</tr>
								<tr class="font_1" height="30"
									style="background-color: rgb(255, 255, 255);">
									<%for(Branch branch:zhandianes) {%>
									<td width="120px" align="center" valign="middle" bgcolor="#F1F1F1"><%=branch.getBranchname() %></td>
									<%} %>
									<td width="120px" align="center" valign="middle" bgcolor="#F1F1F1">合计</td>
								</tr>
								<tr class="font_1" height="30">
								</tr>
							</tbody>
						</table>
				</div>
			</div>
		</div>
		</div>
	</div>
</body>
</html>