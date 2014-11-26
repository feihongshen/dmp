<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="net.sf.json.JSONObject"%>

<%
List<CwbOrder> cwbList = request.getAttribute("cwbList")==null?null:(List<CwbOrder>)request.getAttribute("cwbList");
List<JSONObject> cList = request.getAttribute("cList")==null?null:(List<JSONObject>)request.getAttribute("cList");
Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
Map<Long,Customer>  custMap = ( Map<Long,Customer> )request.getAttribute("custMap");
List customeridList =request.getAttribute("customeridStr")==null?null:(List) request.getAttribute("customeridStr");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
String flowordertype=request.getParameter("flowordertype")==null?"":request.getParameter("flowordertype");


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
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
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
<div class="inputselect_box" style="top: 0px">
	<form action="deliveryPercentByemaildate" method="post" id="searchForm">
	<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%if(customerlist!=null&&customerlist.size()>0)for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(customeridList!=null&&!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=c.getCustomername() %></option>
		          <%} %>
		        </select>[<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
						&nbsp;&nbsp;客户发货时间：
		<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
			<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
		统计时间：
		<select id="flowordertype" name="flowordertype">
    		<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%=(FlowOrderTypeEnum.RuKu.getValue()+"").equals(request.getParameter("flowordertype"))?"selected":"" %>>首次入库时间</option>
    		<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%=(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()+"").equals(request.getParameter("flowordertype"))?"selected":"" %>>首次出库时间</option>
    		</select>
		<input type="button" id="find" onclick="submitselect();" class="input_button2" value="查看">
		

		
	</form>
</div>
	<div style="height:35px"></div>
	<div style="position:relative; z-index:0">
	<div style="position:absolute; left:0; top:0; z-index:99; width:115px; overflow:hidden">
	<table width="200" border="0" cellspacing="1" cellpadding="5" class="table_2">
		<tbody>
			<tr class="font_1" height="30">
				<td width="100" rowspan="2" bgcolor="#EFEFEF" style="vertical-align:middle">供货商</td>
				<td bgcolor="#eef6ff" >1</td>
				</tr>
			<tr class="font_1" height="30">
				<td bgcolor="#eef6ff" >1</td>
				</tr>
			<%if(cList!=null && cList.size()>0){ %>
			<%for(JSONObject json:cList){ %>
			<tr id="cr<%=cList.indexOf(json)%>" <%if(cList.indexOf(json)>=10){ %> style="display:none" <%} %>>
				<td bgcolor="#F7F7F7"><%=custMap.get(json.getLong("customerid"))==null?"":custMap.get(json.getLong("customerid")).getCustomername()%></td>
				<td>&nbsp;</td>
			</tr>
			<%}} %>
		</tbody>
	</table></div>
	<div style="width:100%; overflow-x:scroll">
	
	<table width="1900" border="0" cellspacing="1" cellpadding="5" class="table_2" id="gd_table">
		<tbody>
			<tr class="font_1" height="30">
				<td width="100" rowspan="2" bgcolor="#eef6ff">供货商</td>
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
			<%if(cList!=null && cList.size()>0){ %>
			<%for(JSONObject json:cList){ %>
			<tr id="tr<%=cList.indexOf(json)%>" <%if(cList.indexOf(json)>=10){ %> style="display:none" <%} %>>
				<td></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/all/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("cwbcount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("receivablefee") %></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/tuotou/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("tuotoucount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("tuotoureceivablefee") %></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/jushou/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("jushoucount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("jushoureceivablefee") %></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/zhiliu/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("zhiliucount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("zhiliureceivablefee") %></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/diushi/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("diushicount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("diushireceivablefee") %></td>
				<td><a href="<%=request.getContextPath()%>/percent/show/wujieguo/<%=starttime%>/<%=endtime%>/<%=json.getLong("customerid")%>/<%=flowordertype%>/1"><%=json.getLong("wujieguocount") %></a></td>
				<td align="right" valign="middle"><%=json.getString("wujieguoreceivablefee") %></td>
				<td><%=dp4Map.get(json.getLong("customerid"))==null?0:dp4Map.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dp4Map.get(json.getLong("customerid"))==null?0:dp4Map.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp12Map.get(json.getLong("customerid"))==null?0:dp12Map.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dp12Map.get(json.getLong("customerid"))==null?0:dp12Map.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp24Map.get(json.getLong("customerid"))==null?0:dp24Map.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dp24Map.get(json.getLong("customerid"))==null?0:dp24Map.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp36Map.get(json.getLong("customerid"))==null?0:dp36Map.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dp36Map.get(json.getLong("customerid"))==null?0:dp36Map.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dp72Map.get(json.getLong("customerid"))==null?0:dp72Map.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dp72Map.get(json.getLong("customerid"))==null?0:dp72Map.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
				<td><%=dpallMap.get(json.getLong("customerid"))==null?0:dpallMap.get(json.getLong("customerid")) %></td>
				<td><%=BigDecimal.valueOf(dpallMap.get(json.getLong("customerid"))==null?0:dpallMap.get(json.getLong("customerid"))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(json.getLong("cwbcount")),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
			</tr>
			<%}} %>
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