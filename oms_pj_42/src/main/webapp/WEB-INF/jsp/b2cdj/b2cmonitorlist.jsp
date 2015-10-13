
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.domain.*"%>


 <%
 List<B2CData>  datalist=(List<B2CData>)request.getAttribute("monitorlist");
 List<Customer>  customerlist=(List<Customer>)request.getAttribute("customerlist");
 Page page_obj = (Page)request.getAttribute("page_obj");
 
 
 
 String cwb=request.getParameter("cwb")==null?"":request.getParameter("cwb");
 String endtime=request.getParameter("endtime")==null?"":request.getParameter("endtime");
 String starttime=request.getParameter("starttime")==null?"":request.getParameter("starttime");
 int flowordertypeid=request.getParameter("flowordertypeid")==null?0:Integer.parseInt(request.getParameter("flowordertypeid").toString());
 String customerid=request.getParameter("customerid")==null?"0":request.getParameter("customerid");
 String customername = "";
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>B2C对接异常监控</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
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
	if($("#cwb").val().split("\n").length>10000){
		alert("查询不能多于10000条");
		return false;
	}
		return true;
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
	if($("#customerid1").val() == 0 ){
		alert("每次只能操作一个供货商，请先选择一个供货商查询结果，再做操作");
		return false;
	}
	if(<%=page_obj.getTotal()==0 %>){
		alert("没有查到数据，不能做操作");
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
        $("#btnval").attr("disabled","disabled");
		$("#btnval").val("请稍等..");
       }
   });
   $("#btndeal").click(function(){
       if(checkDeal()){
    	   if(confirm("您确定要推送所查询结果中等待推送的订单？")){
    		   $('#dealForm').submit();
    		   $("#btndeal").attr("disabled","disabled");
			   $("#btndeal").val("请稍等..");
		 }
       }
   });
   $("#btndeal2").click(function(){
       if(checkDeal()){
    	   if(confirm("您确定要推送所查询结果中包含推送失败的订单？")){
    		   $('#dealForm2').submit();
    		   $("#btndeal2").attr("disabled","disabled");
			   $("#btndeal2").val("请稍等..");
		 }
       }
   });
   $("#btndeal3").click(function(){
       if(checkDeal()){
    	   if(confirm("您确定标记所有推送失败的订单？")){
    		   $('#dealForm3').submit();
    		   $("#btndeal3").attr("disabled","disabled");
			   $("#btndeal3").val("请稍等..");
		 }
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
     function setFlag(b2cid){
   	  $.ajax({
  			url:"<%=request.getContextPath()%>/b2cjointmonitor/setFlag/"+b2cid,  //后台处理程序
  			type:"POST",//数据发送方式 
  			dataType:'html',//接受数据格式
  			success:function(html){
  				if(html=="ok"){
  					alert("标记成功");
  					location.reload();
  				}
  			}		
  		});
      }
     function resetFlag(b2cid){
   	  $.ajax({
  			url:"<%=request.getContextPath()%>/b2cjointmonitor/resetFlag/"+b2cid,  //后台处理程序
  			type:"POST",//数据发送方式 
  			dataType:'html',//接受数据格式
  			success:function(html){
  				if(html=="ok"){
  					alert("重置成功");
  					location.reload();
  				}
  			}		
  		});
      }
      function batchreset(){
    	  $.ajax({
  			url:"<%=request.getContextPath()%>/b2cjointmonitor/reset",
  			type:"post",
  			data:{"cwbs":$("#cwb").val()},
  			dataType:"json",
  			success:function(data){
  				if(data.errorCode==0){
  					$("#reset").hide();
  				}
  				alert(data.error);
  			}
  		});  
      }
</script>
</head>

<body style="background:#eef9ff"><div class="inputselect_box">
<form  id="searchForm" name="searchForm" action="<%=request.getContextPath()%>/b2cjointmonitor/list/1" method = "post">
		
	   <input type="hidden" id="showflag" name="showflag" value="1" />
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="height:50px; font-size:12px"  >
			<tr><td>订单号：<textarea cols="24" rows="4"  name ="cwb" id="cwb" ><%if(cwb!=null&&cwb.length()>0){ %><%=cwb %><%} %></textarea>
				供货商：<select id="customerid" name="customerid" >
		           <option value="0" >全部</option>
		          	<%
		          	if(customerlist != null && customerlist.size()>0){
		        	for(Customer cu:customerlist){
		          		
		          	for(B2cEnum em:B2cEnum.values()){
		          		if(!cu.getB2cEnum().equals(em.getKey()+"")){
		          			continue;
		          		}
		           %>
		            <option value="<%=cu.getCustomerid()%>" <%if(customerid.equals(cu.getCustomerid()+"")){ customername=cu.getCustomername(); %>selected<%} %>><%=cu.getCustomername()%></option>
		        <%} 	}}%>
		        
        	   </select>
        	订单操作状态:   <select name="flowordertypeid" id="flowordertypeid">
        	   <option value="0">请选择</option>
        	   <option value="<%=FlowOrderTypeEnum.YiShenHe.getValue()%>" <%if(FlowOrderTypeEnum.YiShenHe.getValue()==flowordertypeid){ %>selected="selected"<%} %>><%=FlowOrderTypeEnum.YiShenHe.getText()%></option>
        	   <option value="<%=FlowOrderTypeEnum.RuKu.getValue()%>" <%if(FlowOrderTypeEnum.RuKu.getValue()==flowordertypeid){ %>selected="selected"<%} %>><%=FlowOrderTypeEnum.RuKu.getText()%></option>
        	   <option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>" <%if(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()==flowordertypeid){ %>selected="selected"<%} %>><%=FlowOrderTypeEnum.ChuKuSaoMiao.getText()%></option>
        	   <option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>" <%if(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()==flowordertypeid){ %>selected="selected"<%} %>><%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText()%></option>
        	   <option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>" <%if(FlowOrderTypeEnum.FenZhanLingHuo.getValue()==flowordertypeid){ %>selected="selected"<%} %>><%=FlowOrderTypeEnum.FenZhanLingHuo.getText()%></option>
        	   </select>
        	   订单操作时间：<input type ="text" name ="starttime" id="strtime"  value="<%=request.getParameter("starttime")==null?"":request.getParameter("starttime") %>"/>
        	  			 到
        	   		  <input type ="text" name ="endtime" id="endtime"  value="<%=request.getParameter("endtime")==null?"":request.getParameter("endtime") %>"/>
					<input type="button" id="btnval" value="搜索" class="input_button2" />
					<%if(datalist!=null&&datalist.size()>0&&customername.length()==0){ %>
						<input type="button" id="reset" value="批量重置" class="input_button2" onclick="batchreset();"/>
					<%} %>
	                <%if("".equals(request.getParameter("cwb"))){%>
	                <input type="button" id="btnexport" class="input_button2" value="导出excel"  />
	                <%if(customername.length()>0){ %><br>
	                <input type="button" id="btndeal"   value="批量推送[<%=customername %>]等待推送的订单"  />	
	                <%-- <input type="button" id="btndeal2"  value="批量推送[<%=customername %>]推送失败与等待推送的订单"  />	 --%>
	                <%-- <input type="button" id="btndeal3"  value="批量标记[<%=customername %>]推送失败的订单为屏蔽推送"  /> --%>
	                <%}} %>
	                </td>
                </tr>
           
                
		</table>
	</form>	
	<form id="exportForm" action="<%=request.getContextPath()%>/b2cjointmonitor/export">
	      <input type ="hidden" name ="cwb"     value ="<%=cwb%>">
	      <input type ="hidden" name ="customerid"     value ="<%=customerid%>">
	      <input type ="hidden" name ="flowordertypeid"     value ="<%=flowordertypeid%>">
	      <input type ="hidden" name ="starttime"     value ="<%=starttime%>">
	      <input type ="hidden" name ="endtime"     value ="<%=endtime%>">
	</form>
	<form id="dealForm" action="<%=request.getContextPath()%>/b2cjointmonitor/send">
	      <input type ="hidden" name ="customerid" id="customerid"   value ="<%=customerid%>">
	</form>
	<form id="dealForm2" action="<%=request.getContextPath()%>/b2cjointmonitor/send">
	      <input type ="hidden" name ="customerid"  id="customerid1"  value ="<%=customerid%>">
	      <input type ="hidden" name ="type" id='type'  value ="1">
	</form>
	<form id="dealForm3" action="<%=request.getContextPath()%>/b2cjointmonitor/send">
	      <input type ="hidden" name ="customerid"   value ="<%=customerid%>">
	      <input type ="hidden" name ="type" id='type'  value ="2">
	</form>
		<%if(datalist!=null&&datalist.size()>0){
 	 %>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1"> 
			<td width="1%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单操作时间</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单操作状态</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">推送电商状态</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">延迟时长（分钟）</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">推送异常原因</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
		</tr>
		<%
		for(B2CData monitor:datalist){
		%>
		<tr>
			<td width="2%" align="center" valign="middle" ><%=monitor.getB2cid()%></td>
			<td  align="center" valign="middle">
			<%
			if(customerlist != null && customerlist.size()>0){
			for(Customer cu:customerlist){
				if(cu.getCustomerid()!=monitor.getCustomerid()){
					continue;
				}
			%>	
			<%=cu.getCustomername() %>		
			<%  }}%>
			</td>
			<td  align="center" valign="middle"><%=monitor.getCwb()%></td>
			<td  align="center" valign="middle"><%=monitor.getPosttime()%></td>
			<td  align="center" valign="middle"><%	String method="";
				for(FlowOrderTypeEnum cc:FlowOrderTypeEnum.values()){
					if(cc.getValue()==monitor.getFlowordertype()){
						method=cc.getText();
					}
				}
			%>
			
			<%=method%></td>
			<td  align="center" valign="middle">
			<%
			switch((int)monitor.getSend_b2c_flag()){
			case 0 :out.print("等待推送");break;
			case 1 :out.print("推送成功");break;
			case 2 :out.print("<font color='red'>推送失败</font>");break;
			case 3 :out.print("标识屏蔽推送");break;
			default:out.print("推送成功");
			}
			
			%></td>
			<td  align="center" valign="middle">
			<%if(monitor.getSend_b2c_flag()==0){
			   if( DateDayUtil.getQuotMin(monitor.getPosttime()) >= 60){%>
			        <%="<font color='red'>"+DateDayUtil.getQuotMin(monitor.getPosttime())/60+"小时"+(DateDayUtil.getQuotMin(monitor.getPosttime())%60)+"分</font>"%>
			   <%}else{%>
			       <%=DateDayUtil.getQuotMin(monitor.getPosttime())+"分"%>
			   <%}
			}
			%></td>
			<td  align="center" valign="middle"><a href="javascript:;" title="<%=monitor.getRemark()==null?"":monitor.getRemark()%>"><%=monitor.getRemark()==null?"":
				(monitor.getRemark().length()>20?monitor.getRemark().substring(0, 20):monitor.getRemark())%></a>
				</td>
			<td  align="center" valign="middle"><%if(monitor.getSend_b2c_flag()==2){ %>
			【<a href="javascript:setFlag(<%=monitor.getB2cid() %>);" >屏蔽推送</a>】
			【<a href="javascript:resetFlag(<%=monitor.getB2cid() %>);" >重置</a>】
			
			<%} %></td>
		</tr>
		<%} %>
		
	</table>
	<%} %>
	</div>

	<%if(page_obj.getMaxpage()>1){%>
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






