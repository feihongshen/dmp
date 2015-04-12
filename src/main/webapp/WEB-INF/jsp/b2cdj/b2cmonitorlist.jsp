
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.b2c.tools.b2cmonntor.*"%>


 <%
 List<B2CMonitorData>  datalist=(List<B2CMonitorData>)request.getAttribute("monitorlist");
 List<Customer>  customerlist=(List<Customer>)request.getAttribute("customerlist");
 Page page_obj = (Page)request.getAttribute("page_obj");
 
 
 
 String cwb=request.getParameter("cwb")==null?"":request.getParameter("cwb");
 String customerid=request.getParameter("customerid")==null?"0":request.getParameter("customerid");
 String starttime=request.getAttribute("starttime")==null?"":request.getAttribute("starttime").toString();//DateTimeUtil.getDateBefore(7)
 String endtime=request.getAttribute("endtime")==null?"":request.getAttribute("endtime").toString();//DateTimeUtil.getNowTime()
 String send_b2c_flag=request.getParameter("send_b2c_flag")==null?"3":request.getParameter("send_b2c_flag");
 
 String hand_deal_flag=request.getParameter("hand_deal_flag")==null?"0":request.getParameter("hand_deal_flag");
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>B2C对接异常监控</title>
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
<script type="text/javascript">

      $(document).ready(function() {

   $("#btnval").click(function(){
       if(check()){
        $("#searchForm").submit();
       }
   });
   $("#btndeal").click(function(){
       if(checkDeal()){
    	   if(confirm("您确定要对所查询的结果做标记为已处理？")){
    		   $('#dealForm').submit();
		 }
       }
   });
   $("#btnexport").click(function(){
       if(checkExport()){
    	   $('#exportForm').submit();
       }
   });
   
      });
   
      
      function updateBranch(){
    		$.ajax({
    			url:"<%=request.getContextPath()%>/pospay/updatebranch",  //后台处理程序
    			type:"POST",//数据发送方式 
    			data:"branchid="+$("#branchid").val(),//参数
    			dataType:'json',//接受数据格式
    			success:function(json){
    				$("#deliverid").empty();//清空下拉框//$("#select").html('');
    				$("<option value='-1'>请选择POS小件员</option>").appendTo("#deliverid");//添加下拉框的option
    				for(var j = 0 ; j < json.length ; j++){
    					$("<option value='"+json[j].userid+"'>"+json[j].realname+"</option>").appendTo("#deliverid");
    				}
    			}		
    		});
    	}
      
      
      
      
</script>
<script>


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
<form  id="searchForm" name="searchForm" action="<%=request.getContextPath()%>/b2cjointmonitor/list/1" method = "post">
		
		
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="height:50px; font-size:12px"  >
			<tr><td>
				客户：<select id="customerid" name="customerid" class="select1">
		           <option value="0" >全部</option>
		           
		          	<%
		        	for(Customer cu:customerlist){
		          		
		          	for(B2cEnum em:B2cEnum.values()){
		          		if(!cu.getB2cEnum().equals(em.getKey()+"")){
		          			continue;
		          		}
		           %>
		            <option value="<%=cu.getCustomerid()%>" <%if(customerid.equals(cu.getCustomerid()+"")){%>selected<%} %>><%=cu.getCustomername()%></option>
		        <%} 	}%>
        	   </select>
        	   </td>
        	   <td>
        	      记录时间：
        	      <input type ="text" name ="starttime" id="starttime"  value ="<%=starttime%>" class="input_text1" style="height:20px;">
                                到     
                                <input type ="text" name ="endtime" id="endtime"  value ="<%=endtime%>" class="input_text1" style="height:20px;">
			 </td>
        	   <td>
        	      订单号：
        	      <input type="text" name="cwb" value="" class="input_text1" style="height:20px;"/>
                </td> </tr>
                
               <tr>
	               	<td>
					推送状态订单：
					<select name="send_b2c_flag" class="select1">
								<option value="-1" <%if(send_b2c_flag.equals("-1")){%>selected<%} %>>全部</option>
								<option value="3" <%if(send_b2c_flag.equals("3")){%>selected<%} %>>未推送、失败</option>
								<option value="0" <%if(send_b2c_flag.equals("0")){%>selected<%} %>>未推送</option>
								<option value="2" <%if(send_b2c_flag.equals("2")){%>selected<%} %>>推送失败</option>
								<option value="1"  <%if(send_b2c_flag.equals("1")){%>selected<%} %>>推送成功</option>
								
								
								</select>
								 </td>
        	   <td>
					<input type="checkbox" name="hand_deal_flag" value="1" <%if(hand_deal_flag.equals("1")){%>checked<%} %>
					onchange="$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/1');$('#searchForm').submit()"
					/> 
					查询已处理数据
										<input type="submit" id="btnval" value="查询" class="input_button2" />
	                <input type="button" id="btnexport" value="导出excel" class="input_button2" />	
					 </td>
        	   <td>

	                <input type="button" id="btndeal" value="标记当前查询订单为已处理" />	
	                </td>
                </tr>
           
                
		</table>
	</form>	
	<form id="exportForm" action="<%=request.getContextPath()%>/b2cjointmonitor/export">
	      <input type ="hidden" name ="starttime"   value ="<%=starttime%>">
	      <input type ="hidden" name ="starttime"   value ="<%=endtime%>">
	      <input type ="hidden" name ="customerid"   value ="<%=customerid%>">
	      <input type ="hidden" name ="cwb"   value ="<%=cwb%>">
	      <input type ="hidden" name ="send_b2c_flag"   value ="<%=send_b2c_flag%>">
	      <input type ="hidden" name ="hand_deal_flag"   value ="<%=hand_deal_flag%>">
	</form>
	<form id="dealForm" action="<%=request.getContextPath()%>/b2cjointmonitor/deal/1">
	      <input type ="hidden" name ="starttime"   value ="<%=starttime%>">
	      <input type ="hidden" name ="starttime"   value ="<%=endtime%>">
	      <input type ="hidden" name ="customerid"   value ="<%=customerid%>">
	      <input type ="hidden" name ="cwb"   value ="<%=cwb%>">
	      <input type ="hidden" name ="send_b2c_flag"   value ="<%=send_b2c_flag%>">
	      <input type ="hidden" name ="hand_deal_flag"   value ="<%=hand_deal_flag%>">
	</form>
		<%if(datalist!=null&&datalist.size()>0){
 	 %>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1"> 
			<td width="1%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">记录时间</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单当前状态</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">推送状态</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">异常原因</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">手动处理状态</td>
		</tr>
		<%
		int i=0;
		for(B2CMonitorData monitor:datalist){
			i++;
		%>
		<tr>
			<td width="2%" align="center" valign="middle" ><%=i%></td>
			<td  align="center" valign="middle">
			<%
			for(Customer cu:customerlist){
				if(cu.getCustomerid()!=monitor.getCustomerid()){
					continue;
				}
			%>	
			<%=cu.getCustomername() %>		
			<%  }%>
			</td>
			<td  align="center" valign="middle"><%=monitor.getCwb()%></td>
			<td  align="center" valign="middle"><%=monitor.getPosttime()%></td>
			<td  align="center" valign="middle">
			<%	String method="";
				for(CwbFlowOrderTypeEnum cc:CwbFlowOrderTypeEnum.values()){
					if(cc.getValue()==monitor.getFlowordertype()){
						method=cc.getText();
					}
				}
			%>
			
			<%=method%></td>
			<td  align="center" valign="middle"><%=monitor.getSend_b2c_flagStr()%></td>
			<td  align="center" valign="middle"><%=monitor.getExpt_reason()==null?"":monitor.getExpt_reason()%></td>
			<td  align="center" valign="middle"><%=monitor.getHand_deal_flagStr()%></td>
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
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/1');$('#searchForm').submit();" >第一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
					<a href="javascript:$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
					　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
							id="selectPg"
							onchange="$('#searchForm').attr('action','<%=request.getContextPath()%>/b2cjointmonitor/list/'+$(this).val());$('#searchForm').submit()">
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






