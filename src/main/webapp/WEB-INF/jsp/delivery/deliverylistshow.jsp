<%@page import="cn.explink.domain.CwbSearchDelivery"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="javax.print.DocFlavor.STRING"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
    <%
    List<CwbSearchDelivery> deliverList1 = request.getAttribute("deliverylist1")==null?null:( List<CwbSearchDelivery>)request.getAttribute("deliverylist1");
    Page page_obj = (Page)request.getAttribute("page_obj");
    long state=(Long)request.getAttribute("deliverystate");
    List<Customer> list=request.getAttribute("customers")==null?null:( List<Customer>)request.getAttribute("customers");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员领货查询</title>
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
<script type="text/javascript">

$(function() {
	$("#startid").datetimepicker({
		
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	}
	);
	$("#endid").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
});

</script>
<script type="text/javascript">

$(document).ready(function() {
	$("#find").click(function(){
		if($("#startid").val()=='' ||$("#endid").val()==''){
			alert("请选择时间段！");
			return false;
		}
		if(!Days()||($("#startid").val()=='' &&$("#endid").val()!='')||($("#startid").val()!='' &&$("#endid").val()=='')){
			alert("时间跨度为31天！！！");
			return false;
		}
		if($("#startid").val()>$("#endid").val()){
			alert("开始时间不能大于结束时间！");
			return false;
		}
	 	$("#isshow").val(1);
    	$("#searchForm").submit();
    	$("#find").attr("disabled","disabled");
		$("#find").val("请稍等..");
	});
});
function Days(){     
	var day1 = $("#startid").val();   
	var day2 = $("#endid").val(); 
	var y1, y2, m1, m2, d1, d2,min;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	min=m2*31-m1*31-d1+d2;
	if(min>31){
		return false;
	}        
	return true;
}
</script>
</head>

<body style="background:#eef9ff;color:#0000FFF;">
	<div class="inputselect_box"   >
	<form action="<%=request.getAttribute("page")==null?1:request.getAttribute("page") %>" method="post" id="searchForm">
		货物状态：
		<select name ="deliverystate" id ="deliverystate"   style="height 30px;width: 180px">
		<option value="-1" selected="selected">--请选择--</option>
					          <%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){
					        	  if(ds.getValue()!=DeliveryStateEnum.WeiFanKui.getValue()&&ds.getValue()!=
					        			  DeliveryStateEnum.ShangMenHuanChengGong.getValue()&&ds.getValue()!=
					        			  DeliveryStateEnum.ShangMenTuiChengGong.getValue()&&ds.getValue()!=
					        			  DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()){ %>
					          <option value ="<%=ds.getValue() %>" <%if(ds.getValue()==state){
			            		%>selected="selected"<%} %> >
					        		    <%=ds.getText()%></option>
					          <%}if(ds.getValue()==DeliveryStateEnum.WeiFanKui.getValue()){%>
					        	  <option value ="<%=ds.getValue() %>" <%if(ds.getValue()==state){
			            		%>selected="selected"<%} %> >已领货 
								           </option>
								          <%} 
					        	  
					          }%>
					          
		</select>
		
		
		领货时间：<input type ="text" value="<%=request.getAttribute("startid")==null?"":request.getAttribute("startid") %>" id="startid" name="startid"/>到：<input type ="text" value="<%=request.getAttribute("endid")==null?"":request.getAttribute("endid") %>" id="endid" name="endid"/>
		<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询" class="input_button2" />
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff"> 供货商</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">领货时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">代收金额</td>
			<td width="42%" align="center" valign="middle" bgcolor="#eef6ff">地址</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">货物状态</td>
		</tr>
		
		
		<%
		if(deliverList1!=null&&deliverList1.size()>0){
			for(CwbSearchDelivery dl:deliverList1){
				
				String customername="";
				String status="";
				for(Customer clist : list){
					if(clist.getCustomerid()==dl.getCustomerid()){
						customername=clist.getCustomername();
					}
				}
		%>
		<tr >
			<td  align="center" valign="middle"><%=dl.getCwb()%></td>
			<td  align="center" valign="middle"><%=customername%></td>
			<td  align="center" valign="middle"><%=dl.getCreatetime()%></td>
			<td  align="center" valign="middle"><%=dl.getConsigneename()%></td>
			<td  align="center" valign="middle"><%=dl.getReceivablefee() %></td>
			<td  align="center" valign="middle"><%=dl.getConsigneeaddress()%></td>
			<td  align="center" valign="middle"><%=dl.getStatus()%></td>
		</tr>
		
		<%}}%>
		
	</table>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>