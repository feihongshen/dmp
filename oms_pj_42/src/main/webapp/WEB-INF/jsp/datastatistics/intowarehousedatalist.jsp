<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Common"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Customer> customerMarkList = (List<Customer>)request.getAttribute("customerMarkList");
  List<Branch> kufanglist = (List<Branch>)request.getAttribute("kufangList");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  String emaildatebegin=request.getParameter("emaildatebegin")==null?"":request.getParameter("emaildatebegin");
  String emaildateend=request.getParameter("emaildateend")==null?"":request.getParameter("emaildateend");
  long count = request.getAttribute("count")==null?0:(Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List customeridList =(List) request.getAttribute("customeridStr");
  String isyiruku=request.getParameter("isruku")==null?"false":request.getParameter("isruku");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库房入库统计</title>
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
<script type="text/javascript">
	$(document).ready(function() {
		if(<%=request.getAttribute("nouser") != null%>){
			alert("登录已失效，请重新登录！");
		}
		
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
		$("#emaildatebegin").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		$("#emaildateend").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
			timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
		
		if(<%=isyiruku.equals("true")%>){
			$("#date").show();	
		}else{
			$("#date").hide();
		}
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
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	if($("#emaildateend").val().length>0&&$("#emaildateend").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#emaildateend").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	if($("#isruku").val()=='true'){
		if($("#strtime").val().length==0){
			alert("请选择入库开始时间");
			return false;
		}
		if($("#endtime").val().length==0){
			alert("请选入库结束时间");
			return false;
		}
		if($("#strtime").val()>$("#endtime").val()){
			alert("开始时间不能大于结束时间");
			return false;
		}
		if(($("#emaildatebegin").val().length==0&&$("#emaildateend").val().length>0)||($("#emaildatebegin").val().length>0&&$("#emaildateend").val().length==0)){
			alert("请选择一个时间段");
			return false;
		}
		
		
	}else{
		if($("#emaildatebegin").val().length==0){
			alert("请选择发货开始时间");
			return false;
		}
		if($("#emaildateend").val().length==0){
			alert("请选择发货结束时间");
			return false;
		}
		if($("#emaildatebegin").val()>$("#emaildateend").val()){
			alert("开始时间不能大于结束时间");
			return false;
		}
	}
	
	if($("#kufangid").val()==0){
		alert("请选择入库库房");
		return false;
	}
	return true;
}

function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}
function clearSelect(){
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#emaildatebegin").val('');//发货开始时间
	$("#emaildateend").val('');//发货结束时间
}


function isshowdate(){
	var val=$("#isruku").val();
	if(val=='true'){
		$("#date").show();
	}else{
		clearSelect();
		$("#date").hide();
	}
}
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			供货商：
				<select name ="customerid" id ="customerid" multiple="multiple" style="width: 200px;">
		          <%if(customerlist != null && customerlist.size()>0){ %>
		           <%for( Customer c:customerlist){ %>
		            <option value="<%= c.getCustomerid()%>"
		            <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getCustomername()%></option>
		           <%} }%>
		        </select>
		        [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
				[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
				发货时间
				<input type ="text" name ="emaildatebegin" id="emaildatebegin"  value="<%=emaildatebegin %>"/>
				到
				<input type ="text" name ="emaildateend" id="emaildateend"  value="<%=emaildateend %>"/>
		        <strong id="date" >
				入库时间
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
				到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
				</strong>	
			<br />
			入库库房：
		    <select name ="kufangid" id ="kufangid" >
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch b : kufanglist){ %>
			 			<option value ="<%=b.getBranchid() %>" 
					<%if(b.getBranchid()== Long.parseLong(request.getParameter("kufangid")==null?"-1":request.getParameter("kufangid")) ){
			            		%>selected="selected"<%
			            	}
				     %>><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
			 订单类型
			<select name ="cwbordertypeid" id ="cwbordertypeid" >
				<option value="-2">请选择</option>
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
						<option value ="<%=c.getValue() %>" <%if(c.getValue()== (request.getParameter("cwbordertypeid")==null?0:Long.parseLong(request.getParameter("cwbordertypeid")))){%>selected="selected"<%}%> ><%=c.getText()%></option>
		          <%} %>
			</select>
			<select name ="isruku" id ="isruku"  onchange="isshowdate()">
				<option value ="false" <%if("false".equals(request.getParameter("isruku")==null?"false":request.getParameter("isruku"))){%>selected="selected"<%}%> >未入库</option>
				<option value ="true" <%if("true".equals(request.getParameter("isruku")==null?"false":request.getParameter("isruku"))){%>selected="selected"<%}%> >已入库</option>
			</select>	
			
			<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			&nbsp;&nbsp;<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
			<%if(orderlist != null && orderlist.size()>0){  %>
			<select name ="exportmould" id ="exportmould">
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
	<form action="<%=request.getContextPath()%>/datastatistics/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begin" id="begin" value="0"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="sign" id="sign" value="<%=ModelEnum.KuFangRuKuTongJi.getValue() %>"/>
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="emaildatebegin1" id="emaildatebegin1" value="<%=emaildatebegin%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="emaildateend1" id="emaildateend1" value="<%=emaildateend%>"/>
		<input type="hidden" name="kufangid1" id="kufangid1" value="<%=request.getParameter("kufangid")==null?"0":request.getParameter("kufangid")%>"/>
		<input type="hidden" name="cwbordertypeid1" id="cwbordertypeid1" value="<%=request.getParameter("cwbordertypeid")==null?"-2":request.getParameter("cwbordertypeid")%>"/>
		<input type="hidden" name="isruku1" id="isruku1" value="<%=request.getParameter("isruku")==null?"false":request.getParameter("isruku")%>"/>
		<div style="display: none;">
			    <select name ="customerids1" id ="customerids1" multiple="multiple" style="width: 200px;">
		          <%if(customerMarkList != null && customerMarkList.size()>0){ %>
		           <%for( Customer c:customerMarkList){ %>
		            <option value="<%= c.getCustomerid()%>"selected="selected"><%=c.getCustomername()%></option>
		           <%} }%>
		        </select>
		
		</div>
	</form>
	</div>
	<div class="right_title">
	<div style="height:60px"></div><%if(orderlist != null && orderlist.size()>0){  %>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >入库操作人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >入库时间</td>
		</tr>
		
		<% for(CwbOrder  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getAttribute("dmpUrl")%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
					<td  align="center" valign="middle"><%=c.getOperatorName()%></td>
					<td  align="center" valign="middle"><%=c.getInstoreroomtime()==null?"":c.getInstoreroomtime()%></td>
				 </tr>
		 <% }%>
		<tr bgcolor="#FF3300">
			<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
		</tr>
	</table>
	</div><%} %>
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
</script>

<script type="text/javascript">
function exportField(page,j){
	
		if($("#isshow").val()=="1"&&<%=orderlist != null && orderlist.size()>0  %>){
			$("#exportmould2").val($("#exportmould").val());
			$("#btnval"+j).attr("disabled","disabled"); 
			$("#btnval"+j).val("请稍后……");
			$("#begin").val(page);
		 	$.ajax({
				type: "POST",
				url:$("#searchForm2").attr("action"),
				data:$("#searchForm2").serialize(),
				dataType:"json",
				success : function(data) {
					if(data.errorCode==0){
						alert("已进入导出任务队列！");
						$("#downCount",parent.document).html(Number($("#downCount",parent.document).html())+1);
						$.ajax({
							type: "POST",
							url:"<%=request.getContextPath()%>/datastatistics/commitExportExcle",
							data:$("#searchForm2").serialize(),
							dataType:"json",
							success : function(data) {
								
							}
						});
					}else{
						alert(data.remark);
					}
				}
			});
			
			
			
			
		}else{
			alert("没有做查询操作，不能导出！");
		}
}

</script>


</body>
</html>

