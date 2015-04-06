<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OperateSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
  List<OperateSelectView> operateSelectViewList = (List<OperateSelectView>)request.getAttribute("operateSelectViewList");
  List branchArrlist =(List) request.getAttribute("branchArrlist");
  
  Page page_obj = (Page)request.getAttribute("page_obj");
  String autodetailchecked = (String)request.getAttribute("autodetailchecked");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd ");
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
		if(!Days()||($("#begincredate").val()=='' &&$("#endcredate").val()!='')||($("#begincredate").val()!='' &&$("#endcredate").val()=='')){
			alert("时间跨度不能大于10天！");
			return false;
		}
		if($("#begincredate").val()>$("#endcredate").val()){
			alert("开始时间不能大于结束时间！");
			return false;
		}
	 	$("#isshow").val(1);
	 	$("#searchForm").attr('action', '1');
    	$("#searchForm").submit();
    	$("#find").attr("disabled","disabled");
		$("#find").val("请稍等..");
	});	
	
	
	$("#download1").click(function(){		
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
		$("#searchForm").attr('action', 'downloadOperateSelect');		
		$("#searchForm").submit();
		$("#download").attr("disabled","disabled");
		$("#download").val("请稍等..");
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

<body style="background:#f5f5f5">
	<div class="right_box">
		<div class="inputselect_box">
		<form action="1" method="post" id="searchForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:30px">
				<tr>
					<td align="left">
						操作机构：
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
						操作类型：
						<select name ="flowordertype" id ="flowordertype">
						    <option value ="-1">-请选择-</option>
						    <%for(FlowOrderTypeEnum c : FlowOrderTypeEnum.values()){ %>
								<option value =<%=c.getValue() %> 
							   	 <%if(c.getValue()==Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype"))){ %>selected="selected" <%} %> ><%=c.getText()%>
							    </option>
					   		<%} %>
						</select>
						操作时间：
						<input type ="text" name ="begincredate" id="begincredate"  value="<%=request.getParameter("begincredate")==null?"":request.getParameter("begincredate") %>"/>
						    到    
						<input type ="text" name ="endcredate" id="endcredate"  value="<%=request.getParameter("endcredate")==null?"":request.getParameter("endcredate") %>"/>
						
						系统自动处理：
						<select name ="autodetail" id ="autodetail">
						
						    <option value ="1" <%if(autodetailchecked == null || autodetailchecked.equals("1")) {%> selected="selected"<%}%>>全部</option>						
						    <option value ="2" <%if(autodetailchecked.equals("2")) {%> selected="selected"<%}%>>是</option>
						    <option value ="3" <%if(autodetailchecked.equals("3")) {%> selected="selected"<%}%>>否</option>
						</select>
						<input type="hidden" name="isshow" id ="isshow" value ="1" />
						<input type="button" id="find" value="查询" class="input_button2" />
						<input type="button" id="download1" value="导出" class="input_button2"/>
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div class="right_title">
			<div style="height:40px"></div><%if(operateSelectViewList!=null && operateSelectViewList.size()>0){  %>
			<div style="overflow-x:scroll; width:100% " id="scroll">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				   <tr class="font_1">
				  		<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">操作状态</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">操作时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">操作站点</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">操作人</td>
					</tr>
					
					<% for(OperateSelectView  osv : operateSelectViewList){ %>
							<tr bgcolor="#FF3300">
								<td  align="center" valign="middle"><%=osv.getCustomername() %></td>
								<td  align="center" valign="middle"><%=osv.getCwb() %></td>
								<td  align="center" valign="middle"><%=osv.getCwbordertype() %></td>
								<td  align="center" valign="middle"><%=osv.getFlowordertype() %></td>
								<td  align="center" valign="middle"><%=osv.getCredate() %></td>
								<td  align="center" valign="middle"><%=osv.getBranchname() %></td>
								<td  align="center" valign="middle"><%=osv.getUsername() %></td>
							 </tr>
					 <% }%>
				</table>
				<div class="jg_10"></div><div class="jg_10"></div>
			</div><%} %>
			<div class="jg_10"></div><div class="jg_10"></div>
			<%if(page_obj.getMaxpage()>1){ %>
			<div class="iframe_bottom">
				<table align="center" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
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
</script>
</body>
</html>

