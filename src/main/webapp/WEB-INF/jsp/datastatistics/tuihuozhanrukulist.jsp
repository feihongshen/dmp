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
  List<CwbOrderView> orderlist = (List<CwbOrderView>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");
  List<Branch> branchnameList = (List<Branch>)request.getAttribute("branchnameList");
  List branchidStr = request.getAttribute("branchidStr")==null?null:(List) request.getAttribute("branchidStr");
  List customeridList = request.getAttribute("customeridStr")==null?null:(List) request.getAttribute("customeridStr");
  List cwbordertypeidList = request.getAttribute("cwbordertypeidStr")==null?null:(List) request.getAttribute("cwbordertypeidStr");
  
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  long count = request.getAttribute("count")==null?0:(Long)request.getAttribute("count");
  Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货站入库统计</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
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
	return true;
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
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'全部' });
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'全部' });
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'全部' });
});
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}
function clearSelect(){
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
}
</script>
</head>

<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:100px">
	<tr>
		<td align="left">
			入库时间<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1"/>
		</td>
		<td>
		退货站点
		    <select name ="branchid" id ="branchid" multiple="multiple" style="width: 300px;">
		          <%if(branchnameList!=null && branchnameList.size()>0) {%>
		          <%for(Branch b : branchnameList){ %>
			 			<option value ="<%=b.getBranchid() %>" 
		          <%if(!branchidStr.isEmpty()) 
			            {for(int i=0;i<branchidStr.size();i++){
			            	if(b.getBranchid()== new Long(branchidStr.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
		</td>
	</tr>
	<tr>
	<td>
	订单类型
			<select name ="cwbordertypeid" id ="cwbordertypeid" multiple="multiple" >
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
						<option value ="<%=c.getValue() %>"  <%if(cwbordertypeidList!=null) 
			            {for(int i=0;i<cwbordertypeidList.size();i++){
			            	if(c.getValue()== new Long(cwbordertypeidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getText()%></option>
		          <%} %>
			</select>
	</td>
	<td>
	供货客户
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%if(customerlist!=null&&customerlist.size()>0)for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getCustomername() %></option>
		          <%} %>
		        </select>
		        [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
				[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
	</td>
	</tr>
	<tr>
	<td>
			<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
			
	</td>
	<td>
	<%if(exportmouldlist!=null&&exportmouldlist.size()>0){%>
	导出模板
	<select name ="exportmould" id ="exportmould" class="select1">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select><%} %>
			 <%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('0','0');"/>
			<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>');"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=count %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%} %>
			<%}} %>
	</td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath()%>/datastatistics/tuihuoexportExcle" method="post" id="searchForm2">
		<input type="hidden" name="sign" id="sign" value="tuihuozhanruku"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input  type="hidden" name="istuihuozhanruku1" value="1"/>
		<input type="hidden" name="tuihuotype" value="2"/>
		<div style="display: none;">
			<select name ="branchid1" id ="branchid1"  multiple="multiple" style="width: 320px;">
		          <%if(branchnameList!=null && branchnameList.size()>0){ %>
		          <%for(Branch b : branchnameList){ %>
						<option value ="<%=b.getBranchid() %>" 
		           <%if(!branchidStr.isEmpty()) 
			            {for(int i=0;i<branchidStr.size();i++){
			            	if(b.getBranchid()== new Long(branchidStr.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			 </select>
			 <select name ="customerid1" id ="customerid1" multiple="multiple" style="width: 300px;">
				<%if(customerlist!=null&&customerlist.size()>0)for(Customer c : customerlist){ %>
				 <option value ="<%=c.getCustomerid() %>" 
				  <%if(!customeridList.isEmpty()) 
				   {for(int i=0;i<customeridList.size();i++){
				   	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
				   		%>selected="selected"<%
				      	 break;
				      	}
				      }
				}%>><%=c.getCustomername() %></option>
				<%} %>
	        </select>
	        <select name ="cwbordertypeid1" id ="cwbordertypeid1" multiple="multiple" >
	          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
					<option value ="<%=c.getValue() %>"  <%if(cwbordertypeidList!=null) 
		            {for(int i=0;i<cwbordertypeidList.size();i++){
		            	if(c.getValue()== new Long(cwbordertypeidList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>><%=c.getText()%></option>
	          <%} %>
		</select>
		</div>
	</form>
	</div>
	<div class="right_title">
	<div style="height:110px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >客户</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >退货入库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >退货站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >当前状态</td>
		</tr>
		<% for(CwbOrderView  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getTuihuozhaninstoreroomtime()  %></td>
					<td  align="center" valign="middle"><%=c.getStartbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getCwbordertypeid()  %></td>
					<td  align="center" valign="middle"><%=c.getFlowordertypeMethod() %></td>
				</tr>
		 <%}%>
	</table>
	</div>
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
			
	<div class="jg_10"></div>

	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);

function exportField(page,j){
	if($("#isshow").val()=="1"&&<%=orderlist != null && orderlist.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval"+j).attr("disabled","disabled");
		$("#btnval"+j).val("请稍后……");
		$("#begin").val(page);
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}

</script>
</body>
</html>

