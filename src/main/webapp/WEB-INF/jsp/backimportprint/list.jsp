<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OperateSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
  List<User> userList = (List<User>)request.getAttribute("userList");
  List branchArrlist =(List) request.getAttribute("branchArrlist");
  String  driverid =request.getAttribute("driverid").toString();
  

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作查询</title>
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
$(function() {
	$("#begincredate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endcredate").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	
});
</script>
<script type="text/javascript">
$(document).ready(function() {
	$("#find").click(function(){
		if($("#begincredate").val()=='' ||$("#endcredate").val()==''){
			alert("请选择时间段！");
			return false;
		}
	/* 	if(!Days()||($("#begincredate").val()=='' &&$("#endcredate").val()!='')||($("#begincredate").val()!='' &&$("#endcredate").val()=='')){
			alert("时间跨度不能大于10天！");
			return false;
		} */
		if($("#begincredate").val()>$("#endcredate").val()){
			alert("开始时间不能大于结束时间！");
			return false;
		}
	 	$("#isshow").val(1);
	 	$("#searchForm").attr('action', '1');
    	$("#searchForm").submit();
/*     	$("#find").attr("disabled","disabled");
		$("#find").val("请稍等.."); */
	});	
	
});

/* function downloadOperate(){
	if($("#begincredate").val()=='' ||$("#endcredate").val()==''){
		alert("请选择时间段！");
		return false;
	}
	if(!Days()||($("#begincredate").val()=='' &&$("#endcredate").val()!='')||($("#begincredate").val()!='' &&$("#endcredate").val()=='')){
		alert("时间跨度不能大于10天！");
		return false;
	}
	if($("#begincredate").val()>$("#endcredate").val()){
		alert("开始时间不能大于结束时间！");
		return false;
	}
 	$("#isshow").val(1);
 	alert(2222)
 	$("#searchForm").ajaxSubmit({
		type: "POST",
		url: "downloadOperateSelect",
		dataType:"html",
		success : function(data) {
			
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		}
	});
} */

function Days(){     
	var day1 = $("#begincredate").val();   
	var day2 = $("#endcredate").val(); 
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
	if(days>10){
		return false;
	}        
	return true;
}

</script>

</head>

<body style="background:#eef9ff">
	<div class="right_box">
		<div class="inputselect_box">
		<form action="1" method="post" id="searchForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:30px">
				<tr>
					<td align="left">
						上一站:：
						<select name ="branchid" id ="branchid"  multiple="multiple" style="width: 320px;">
					        <%if(branchList!=null && branchList.size()>0) {%>
					        <%for(Branch b : branchList){ %>
							    <option value ="<%=b.getBranchid() %>" 
					            	<%if(!branchArrlist.isEmpty()) 
							            {for(int i=0;i<branchArrlist.size();i++){
							            	if(b.getBranchid()== new Long(branchArrlist.get(i).toString())){
							            		%>selected="selected"<%
							            	 break;
							            	}
							            }
								     }%>><%=b.getBranchname()%></option>
				            <%} }%>
						</select>
						操作时间：
						<input type ="text" name ="begincredate" id="begincredate"  value="<%=request.getParameter("begincredate")==null?"":request.getParameter("begincredate") %>"/>
						    到    
						<input type ="text" name ="endcredate" id="endcredate"  value="<%=request.getParameter("endcredate")==null?"":request.getParameter("endcredate") %>"/>
						驾驶员:
						<select name="driverid" id="driverid">
						<option value="0">请选择</option>
						<%for(User driver:userList){ %>
						<option value="<%=driver.getUserid() %>" <%if((driver.getUserid()+"").equals(driverid)){ %> selected="selected" <%} %>><%=driver.getRealname() %></option>
						<%} %>
						</select>
						
						<input type="hidden" name="isshow" id ="isshow" value ="1" />
						<input type="button" id="find" value="查询" class="input_button2" />
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div class="right_title">
		
			<div class="jg_10"></div><div class="jg_10"></div>

		</div>
		
	</div>	
	<div class="jg_10"></div>
	<div class="clear"></div>

</body>
</html>

