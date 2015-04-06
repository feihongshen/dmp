
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.b2c.tools.b2cmonntor.*"%>
<%@page import="cn.explink.b2c.explink.core_down.*"%>


 <%
 List<B2cAutoDownloadMonitor>  datalist=(List<B2cAutoDownloadMonitor>)request.getAttribute("downloadlist");
 List<Customer>  customerlist=(List<Customer>)request.getAttribute("customerlist");
 List<EpaiApi>  epailist=(List<EpaiApi>)request.getAttribute("epailist");
 Page page_obj = (Page)request.getAttribute("page_obj");
 
 String customerid=request.getParameter("customerid")==null?"0":request.getParameter("customerid");
 String starttime=request.getAttribute("starttime")==null?"":request.getAttribute("starttime").toString();//DateTimeUtil.getDateBefore(7)
 String endtime=request.getAttribute("endtime")==null?"":request.getAttribute("endtime").toString();//DateTimeUtil.getNowTime()

 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>B2C下载订单记录</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
function check(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}
function checkExport(){
	if(<%=page_obj.getTotal()==0 %>){
		alert("没有查到数据，不能导出");
		return false;
	}
	else{
		return true;
	}
}
function checkDeal(){
	if(<%=page_obj.getTotal()==0 %>){
		alert("没有查到数据，不能做标记");
		return false;
	}
	else{
		return true;
	}
}


function exportField(){
	document.location='<%=request.getContextPath()%>/pospay/save_excel';
}
<% String msg="";  if (request.getAttribute("msg")!=null){  

    msg=(String)request.getAttribute("msg"); 

%> 

alert('<%=msg%>');  

<%}%> 
</script>

<script>
$(document).ready(function() {

	   $("#btnval").click(function(){
	       if(check()){
	        $("#searchForm").submit();
	        $("#btnval").attr("disabled","disabled");
			$("#btnval").val("请稍等..");
	       }
	   });
	  
	   $("#btnexport").click(function(){
	       if(checkExport()){
	    	   $('#exportForm').submit();
	    	   $("#btnexport").attr("disabled","disabled");
			   $("#btnexport").val("请稍等..");
	       }
	   });
	   
	      });

$(function() {
	$("#starttime").datetimepicker({
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
</script>   

</head>

<body style="background:#f5f5f5"><div class="inputselect_box">
<form  id="searchForm" name="searchForm" action="<%=request.getContextPath()%>/b2cdownloadmonitor/list/1" method = "post">
		
		
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="height:50px; font-size:12px"  >
			<tr><td>
				供货商：<select id="customerid" name="customerid" >
		           <option value="" >全部</option>
		           
		          	<%
		        	for(Customer cu:customerlist){
		          		
			          	for(B2cEnum em:B2cEnum.values()){
			          		if(!cu.getB2cEnum().equals(em.getKey()+"")){
			          			continue;
			          		}
			            %>
			            <option value="<%=cu.getCustomerid()%>" <%if(customerid.equals(cu.getCustomerid()+"")){%>selected<%} %>><%=cu.getCustomername()%></option>
			            <%} %>
			            
			            
			            <%
			            if(epailist!=null&&epailist.size()>0){
			            	for(EpaiApi epai:epailist){
			            		if(epai.getCustomerid()==cu.getCustomerid()){
			            			%>
			            			  <option value="<%=cu.getCustomerid()%>" <%if(customerid.equals(cu.getCustomerid()+"")){%>selected<%} %>><%=cu.getCustomername()%></option>
			            			<%
			            		}
			            	}
			            }
			            
			            %>
			            

		          <%}%>
        	   </select>
        	      下载时间：<input type ="text" name ="starttime" id="starttime"  value ="<%=starttime%>">
                                到     <input type ="text" name ="endtime" id="endtime"  value ="<%=endtime%>">
			
        <input type="button" id="btnval" value="查询" class="input_button2" />
                </td> </tr>
                
                
		</table>
	</form>	
	<form id="exportForm" action="<%=request.getContextPath()%>/b2cdownloadmonitor/export">
	      <input type ="hidden" name ="starttime"   value ="<%=starttime%>">
	      <input type ="hidden" name ="starttime"   value ="<%=endtime%>">
	      <input type ="hidden" name ="customerid"   value ="<%=customerid%>">
	     
	</form>

		<%if(datalist!=null&&datalist.size()>0){
 	 %>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1"> 
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">下载时间</td>
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">下载记录数</td>
			<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
		</tr>
		<%
		int i=0;
		for(B2cAutoDownloadMonitor monitor:datalist){
			i++;
			String customername="";
			String customerids=monitor.getCustomerid();
			for(Customer cust:customerlist){
				String custArrs[]=customerids.split(",");
				for(int k=0;k<custArrs.length;k++){
					if(custArrs[k]==null){
						continue;
					}
					if(String.valueOf(cust.getCustomerid()).equals(custArrs[k].toString())){
						customername+=cust.getCustomername()+",";
					}
					
				}
				
			}
			customername=customername.indexOf(",")>0?customername.substring(0,customername.length()-1):customername;
		%>
		<tr>
			<td width="2%" align="center" valign="middle" ><%=i%></td>
		
			<td  align="center" valign="middle"><%=customername%></td>
			<td  align="center" valign="middle"><%=monitor.getCretime()%></td>
			<td  align="center" valign="middle"><%=monitor.getCount()%></td>
			<td  align="center" valign="middle"><%=monitor.getRemark()%></td>
		</tr>
		<%} %>
		
	</table>
	<%} %>
	</div>

	<%if(page_obj.getMaxpage()>1){
		
		
		%>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cdownloadmonitor/list/1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cdownloadmonitor/list/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cdownloadmonitor/list/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cdownloadmonitor/list/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cdownloadmonitor/list/'+$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>" ><%=i %></option>
							<% } %>
						</select>页
						
				</td>
			</tr>
		</table>
	</div>
	
	<%} else{%>
	   <center>
	        <font color ="red">当前查询暂无数据！</font>
	   </center>
	<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>
	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>






