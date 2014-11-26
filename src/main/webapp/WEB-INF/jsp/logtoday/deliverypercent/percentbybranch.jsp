<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="net.sf.json.JSONObject"%>
<%
Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");

List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
List currentBranchidList =(List) request.getAttribute("currentBranchidStr");
Map<Long,String>  branchNameMap = ( Map<Long,String> )request.getAttribute("branchNameMap");

Map<Long,Long>  allMap = ( Map<Long,Long> )request.getAttribute("allMap");
Map<Long,Long>  tuotouMap = ( Map<Long,Long> )request.getAttribute("tuotouMap");
Map<Long,Long>  jushouMap = ( Map<Long,Long> )request.getAttribute("jushouMap");
Map<Long,Long>  zhiliuMap = ( Map<Long,Long> )request.getAttribute("zhiliuMap");
Map<Long,Long>  diushiMap = ( Map<Long,Long> )request.getAttribute("diushiMap");
Map<Long,Long>  wujieguoMap = ( Map<Long,Long> )request.getAttribute("wujieguoMap");

Map<Long,BigDecimal>  allMyMap = ( Map<Long,BigDecimal> )request.getAttribute("allMyMap");
Map<Long,BigDecimal>  tuotouMyMap = ( Map<Long,BigDecimal> )request.getAttribute("tuotouMyMap");
Map<Long,BigDecimal>  jushouMyMap = ( Map<Long,BigDecimal> )request.getAttribute("jushouMyMap");
Map<Long,BigDecimal>  zhiliuMyMap = ( Map<Long,BigDecimal> )request.getAttribute("zhiliuMyMap");
Map<Long,BigDecimal>  diushiMyMap = ( Map<Long,BigDecimal> )request.getAttribute("diushiMyMap");
Map<Long,BigDecimal>  wujieguoMyMap = ( Map<Long,BigDecimal> )request.getAttribute("wujieguoMyMap");

Map<Long , Long>  dp4Map = ( Map<Long , Long> )request.getAttribute("dp4Map");
Map<Long , Long>  dp12Map = ( Map<Long , Long> )request.getAttribute("dp12Map");
Map<Long , Long>  dp24Map = ( Map<Long , Long> )request.getAttribute("dp24Map");
Map<Long , Long>  dp36Map = ( Map<Long , Long> )request.getAttribute("dp36Map");
Map<Long , Long>  dp72Map = ( Map<Long , Long> )request.getAttribute("dp72Map");
Map<Long , Long>  dpallMap = ( Map<Long , Long> )request.getAttribute("dpallMap");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>按发货时间查询投递率</title>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
</script>
<script type="text/javascript">
$(function(){
$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	})
});
$(function(){
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择站点' });
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
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(Days($("#endtime").val(), $("#strtime").val())>31){
		alert("时间跨度不能大于31天");
		return false;
	}
	else{
		return true;
	}
}

function Days(day2,day1){     
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
	  
	return days;
}
function submitselect(){
	if(check()){
		$("#searchForm").submit();
		$("#find").attr("disabled","disabled");
		$("#find").val("请稍等..");
	}
}

function pageselect(page,all){
	for(var i=0;i<all;i++){
		$("#cr"+i).hide();
		$("#tr"+i).hide();
	}
	for(var i=0;i<10;i++){
		$("#cr"+((page-1)*10+i)).show();
		$("#tr"+((page-1)*10+i)).show();
	}
	$("#selectPg").val(page);
}


</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<div class="inputselect_box" style="top: 0px; height:26px ">
	<form action="deliveryPercentBybranch" method="post" id="searchForm">
	<select name ="branchid" id ="branchid" multiple="multiple" style="width: 320px;">
		          <%for(Branch b : branchlist){ %>
		          <option value ="<%=b.getBranchid() %>" 
		           <%if(!currentBranchidList.isEmpty()) 
			            {for(int i=0;i<currentBranchidList.size();i++){
			            	if(b.getBranchid()== new Long(currentBranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%}%>
			 </select>[<a href="javascript:multiSelectAll('branchid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('branchid',0,'请选择');">取消全选</a>]
						&nbsp;&nbsp;到货时间：
		<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
		<input type="button" id="find" onclick="submitselect();" class="input_button2" value="查看">
	</form>
</div>
	<div style="height:35px"></div>
	<div style="position:relative; z-index:0">
	<div style="position:absolute; left:0; top:0; z-index:99; width:115px; overflow:hidden">
	<table width="200" border="0" cellspacing="1" cellpadding="5" class="table_2">
		<tbody>
			<tr class="font_1" height="30">
				<td width="100" rowspan="2" bgcolor="#EFEFEF" style="vertical-align:middle">站点</td>
				<td bgcolor="#eef6ff" >1</td>
				</tr>
			<tr class="font_1" height="30">
				<td bgcolor="#eef6ff" >1</td>
				</tr>
			<%if(allMap!=null && allMap.size()>0){ %>
			<%int i=0; %>
			<%for (long deliverybranchid : allMap.keySet()) {%>
			<tr id="cr<%=i%>" <%if(i>=10){ %> style="display:none" <%} %>>
				<td bgcolor="#F7F7F7"><%=branchNameMap.get(deliverybranchid)==null?"":branchNameMap.get(deliverybranchid)%></td>
				<td>&nbsp;</td>
			</tr>
			<%  i++;}} %>
		</tbody>
	</table></div>
	<div style="width:100%; overflow-x:scroll">
	
	<table width="1900" border="0" cellspacing="1" cellpadding="5" class="table_2" id="gd_table">
		<tbody>
			<tr class="font_1" height="30">
				<td width="100" rowspan="2" bgcolor="#eef6ff">站点</td>
				<td colspan="2" bgcolor="#eef6ff" >客户发货数量</td>
				<td colspan="2" bgcolor="#eef6ff" >妥投单量</td>
				<td colspan="2" bgcolor="#eef6ff" >拒收单量</td>
				<td colspan="2" bgcolor="#eef6ff" >滞留单量</td>
				<td colspan="2" bgcolor="#eef6ff" >丢失单量</td>
				<td colspan="2" bgcolor="#eef6ff" >无结果单量</td>
				<td colspan="2" bgcolor="#eef6ff" >4小时投递率</td>
				<td colspan="2" bgcolor="#eef6ff" >12小时投递率</td>
				<td colspan="2" bgcolor="#eef6ff" >24小时投递率</td>
				<td colspan="2" bgcolor="#eef6ff" >36小时投递率</td>
				<td colspan="2" bgcolor="#eef6ff" >72小时投递率</td>
				<td colspan="2" bgcolor="#eef6ff" >72小时以上</td>
			</tr>
			<tr class="font_1" height="30">
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >金额</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
				<td bgcolor="#eef6ff" >单数</td>
				<td bgcolor="#eef6ff" >投递率</td>
			</tr>
			<%if(allMap!=null && allMap.size()>0){ %>
			<%int i=0; %>
			<%for (long deliverybranchid : allMap.keySet()) {%>
			<tr id="tr<%=i%>" <%if(i>=10){ %> style="display:none" <%} %>>
				<td></td>
				<td><%=allMap.get(deliverybranchid)==null?0:allMap.get(deliverybranchid)  %></td>
				<td align="right" valign="middle"><%=allMyMap.get(deliverybranchid)==null?0:allMyMap.get(deliverybranchid) %></td>
				<td><%=tuotouMap.get(deliverybranchid)==null?0:tuotouMap.get(deliverybranchid) %></td>
				<td align="right" valign="middle"><%=tuotouMyMap.get(deliverybranchid)==null?0:tuotouMyMap.get(deliverybranchid) %></td>
				<td><%=jushouMap.get(deliverybranchid)==null?0:jushouMap.get(deliverybranchid) %></td>
				<td align="right" valign="middle"><%=jushouMyMap.get(deliverybranchid)==null?0:jushouMyMap.get(deliverybranchid) %></td>
				<td><%=zhiliuMap.get(deliverybranchid)==null?0:zhiliuMap.get(deliverybranchid) %></td>
				<td align="right" valign="middle"><%=zhiliuMyMap.get(deliverybranchid)==null?0:zhiliuMyMap.get(deliverybranchid) %></td>
				<td><%=diushiMap.get(deliverybranchid)==null?0:diushiMap.get(deliverybranchid) %></td>
				<td align="right" valign="middle"><%=diushiMyMap.get(deliverybranchid)==null?0:diushiMyMap.get(deliverybranchid) %></td>
				<td><%=wujieguoMap.get(deliverybranchid)==null?0:wujieguoMap.get(deliverybranchid) %></td>
				<td align="right" valign="middle"><%=wujieguoMyMap.get(deliverybranchid)==null?0:wujieguoMyMap.get(deliverybranchid) %></td>
				<td><%=dp4Map.get(deliverybranchid)==null?0:dp4Map.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dp4Map.get(deliverybranchid)==null?0:dp4Map.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp12Map.get(deliverybranchid)==null?0:dp12Map.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dp12Map.get(deliverybranchid)==null?0:dp12Map.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp24Map.get(deliverybranchid)==null?0:dp24Map.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dp24Map.get(deliverybranchid)==null?0:dp24Map.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp36Map.get(deliverybranchid)==null?0:dp36Map.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dp36Map.get(deliverybranchid)==null?0:dp36Map.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp72Map.get(deliverybranchid)==null?0:dp72Map.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dp72Map.get(deliverybranchid)==null?0:dp72Map.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dpallMap.get(deliverybranchid)==null?0:dpallMap.get(deliverybranchid) %></td>
				<td><%=BigDecimal.valueOf(dpallMap.get(deliverybranchid)==null?0:dpallMap.get(deliverybranchid)).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allMap.get(deliverybranchid)),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
			</tr>
			<%} i++;} %>
		</tbody>
	</table>
	</div></div>
<!--底部翻页 -->
<div class="jg_35"></div>
<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:pageselect(1,<%=page_obj.getTotal() %>);" >第一页</a>　
					<a href="javascript:pageselect(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>,<%=page_obj.getTotal() %>);">上一页</a>　
					<a href="javascript:pageselect(<%=page_obj.getNext()<1?1:page_obj.getNext() %>,<%=page_obj.getTotal() %>);" >下一页</a>　
					<a href="javascript:pageselect(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>,<%=page_obj.getTotal() %>);" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="pageselect($(this).val(),<%=page_obj.getTotal() %>);">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
						</select>页
				</td>
			</tr>
		</table>
	</div>
    <%} %>
</body>
</html>