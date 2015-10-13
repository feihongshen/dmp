
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.MonitorDTO"%>
<%@page import="cn.explink.util.*"%>

<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.pos.*"%>

<%@page import="cn.explink.dto.*"%>
 <%

  List<PosPayEntity> pospaylist =(List<PosPayEntity>) request.getAttribute("pospaylist");
 Page page_obj = (Page)request.getAttribute("page_obj");
 
 
 List<User> deliverlist =(List<User>) request.getAttribute("deliverlist");
 List<Branch> branchlist =(List<Branch>) request.getAttribute("branchlist");
 Branch  nowbranch=request.getAttribute("nowbranch")!=null?(Branch)request.getAttribute("nowbranch"):null;
 List<Customer> customerlist =(List<Customer>) request.getAttribute("customerlist");
 int customerid=Integer.parseInt(request.getAttribute("customerid")==null?"-1":request.getAttribute("customerid").toString());
 
 String starttime=request.getAttribute("starttime")!=null&&!"".equals(request.getAttribute("starttime").toString())?request.getAttribute("starttime").toString():DateTimeUtil.getNowDate()+" 00:00:00";
 String endtime=request.getAttribute("endtime")!=null&&!"".equals(request.getAttribute("endtime").toString())?request.getAttribute("endtime").toString():DateTimeUtil.getNowTime();
 int pos_signtypeid=Integer.parseInt(request.getAttribute("pos_signtypeid")!=null&&!"".equals(request.getAttribute("pos_signtypeid").toString())?request.getAttribute("pos_signtypeid").toString():"0");
 String  pos_code=request.getAttribute("pos_code")!=null&&!"".equals(request.getAttribute("pos_code").toString())?request.getAttribute("pos_code").toString():"";
 
 String  cwb=request.getAttribute("cwb")!=null&&!"".equals(request.getAttribute("cwb").toString())?request.getAttribute("cwb").toString():"";
 int deliverid=Integer.parseInt(request.getAttribute("deliverid")==null?"-1":request.getAttribute("deliverid").toString());
 
 String starttime_sp=request.getAttribute("starttime_sp")!=null&&!"".equals(request.getAttribute("starttime_sp").toString())?request.getAttribute("starttime_sp").toString():DateTimeUtil.getNowDate()+" 00:00:00";
 String endtime_sp=request.getAttribute("endtime_sp")!=null&&!"".equals(request.getAttribute("endtime_sp").toString())?request.getAttribute("endtime_sp").toString():DateTimeUtil.getNowTime();
 
 int pos_backoutflag=Integer.parseInt(request.getAttribute("pos_backoutflag")!=null&&!"".equals(request.getAttribute("pos_backoutflag").toString())?request.getAttribute("pos_backoutflag").toString():"0");
 
 
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>货款管理</title>
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

function check1(){
	if($("#strtime_sp").val()>$("#endtime_sp").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}
function exportField(){
	document.location='<%=request.getContextPath()%>/pospay/save_excel';
}
</script>
<script type="text/javascript">

      $(document).ready(function() {
   //获取下拉框的值
   $("#btnval").click(function(){
       if(check()&&check1()){
    	   
        $("#searchForm").submit();
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

$(function() {
	$("#starttime_sp").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime_sp").datetimepicker({
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

<body style="background:#eef9ff">

		<div class="inputselect_box">
		<div style="border-right:1px solid #86AFD5; border-left:1px solid #86AFD5; padding:0 10px">


	<form  id="searchForm" name="searchForm" action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method = "post">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="height:50px; font-size:12px"  >
			<tr><td>
			供货商：<select id="customerid" name="customerid" >
		           <option value="-1" >全部</option>
		           <%for(Customer u:customerlist){ %>
		            <option value="<%=u.getCustomerid()%>" <%if(u.getCustomerid() == customerid) {%>selected="selected"<%}%>><%=u.getCustomername()%></option>
		           <%}
				 %>
        	   </select>
        	      发货时间：<input type ="text" name ="starttime_sp" id="starttime_sp"  value ="<%=starttime_sp%>">
                                到     <input type ="text" name ="endtime_sp" id="endtime_sp"  value ="<%=endtime_sp%>">
        	   
			站点：<select id="branchid" name="branchid" onchange="updateBranch()">
					<%if(nowbranch!=null){%>
						 <option value="<%=nowbranch.getBranchid()%>" ><%=nowbranch.getBranchname() %></option>
					<%}else if(branchlist != null && branchlist.size()>0){ %>
		          <% %>
		           <option value="-1" >全部</option>
		           <%for(Branch u:branchlist){ %>
		            <option value="<%=u.getBranchid()%>" <%if(u.getBranchid() == Integer.parseInt(request.getAttribute("branchid")==null?"-1":request.getAttribute("branchid").toString()) ) {%>selected="selected"<%}%>><%=u.getBranchname()%></option>
		           <%} } else{%>
					<option value="-1">请选择</option>
					<%} %>
        	   </select>
		POS小件员：<select id="deliverid" name="deliverid" >
					<option value="-1" >请选择POS小件员</option>
						<%if(nowbranch!=null||deliverid>0){ 
							if(deliverlist != null && deliverlist.size()>0){ %>
					           <%for( User u:deliverlist){ %>
					           		 <option value="<%=u.getUserid()%>" <%if(u.getUserid() ==deliverid ) {%>selected="selected"<%}%>><%=u.getRealname()%></option>
					           	<%} 
			         		 }
						} %>	
						
					
					
		         
        	   </select>
        	   订单号：<input type="text" name="cwb" value="<%=cwb %>" />
        	 
               
                </td> </tr>
                <tr><td>
               支付方： <select id="pos_code" name="pos_code">
                	<option value="">全部</option>
                  <%for(PosEnum em:PosEnum.values()){ 
						String text=em.getText();
						String method=em.getMethod();
							%>
                	<option value="<%=method%>" <%if(pos_code.equals(method)){%>selected<%} %>><%=text %></option>
                	<%} %>
                </select>
                
                   签收状态： <select id="pos_signtypeid" name="pos_signtypeid">
                	<option value="-1">全部</option>
                	<option value="0"  <%if(pos_signtypeid==0){%>selected<%} %>>未签收</option>
                	<option value="1" <%if(pos_signtypeid==1){%>selected<%} %>>已签收</option>
                </select>
                撤销状态：
                <select name="pos_backoutflag">
                	<option value="-1">--全部--</option>
                	<option value="0" <%if(pos_backoutflag==0){%>selected<%} %>>未撤销</option>
                	<option value="1" <%if(pos_backoutflag==1){%>selected<%} %>>已撤销</option>
                </select>
                	   付款人：<input type="text" name="payname" size="10" />
        
                 付款时间：<input type ="text" name ="starttime" id="strtime"  value ="<%=starttime%>">
                                到     <input type ="text" name ="endtime" id="endtime"  value ="<%=endtime%>">
                	  <input type="button" id="btnval" value="搜索" class="input_button2" />
                	  <input type="hidden" id="isshow" name="isshow" value="isshow" />
					&nbsp;&nbsp;
			<input type ="button" id="btnval2" value="导出excle" class="input_button2" onclick="exportField()"/>
                </td></tr>
                
		</table>
			
	</form>
	</div>

	<form action="<%=request.getContextPath()%>/pospay/save_excel" method="post" id="searchForm2">
	</form>
	</div>

	 <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>

	<div class="right_title">
	<div style="height:77px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	
    <% if(pospaylist!=null&&pospaylist.size()>0){ %>
	<table width="1800" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1"> 
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">支付方</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">付款金额</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">付款凭证号</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">付款日期</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">付款人</td>
			<td width="24%" align="center" valign="middle" bgcolor="#eef6ff">POS备注</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">签收人</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">签收类型</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">签收时间</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">签收备注</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">是否撤销</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">POS小件员</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">所属站点</td>
	
		</tr>
		<%
		int jj=0;
		for(PosPayEntity pe:pospaylist){
			jj++;
			String pos_name="";
			String pos_code1=pe.getPos_code();
			for (PosEnum f : PosEnum.values()) {
	  			if (f.getMethod().equals(pos_code1)) {
	  				pos_name =f.getText();
	     			break;
	     		}
	     	}
		%>
		<tr>
			<td width="2%" align="center" valign="middle" ><%=jj %></td>
			<td  align="center" valign="middle"><%=pe.getCustomername()%></td>
			<td  align="center" valign="middle"><%=pe.getShiptime()%></td>
			<td  align="center" valign="middle"><%=pe.getCwb()%></td>
			<td  align="center" valign="middle"><%=pos_name%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_money()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_document()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_paydate()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_payname()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_remark()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_signname()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_signtype()==0?"未签收":"已签收"%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_signtime()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_signremark()%></td>   
			<td  align="center" valign="middle"><%=pe.getPos_backoutflag()==1?"已撤销":""%></td> 
			<td  align="center" valign="middle"><%=pe.getPos_deliveryname()%></td>
			<td  align="center" valign="middle"><%=pe.getBranchname()%></td>                 
			
		</tr>
		<%
		} %>
		
	</table>
	</div>
	<%if(page_obj.getMaxpage()>1){
		
		
		%>
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
							<option value="<%=i %>" ><%=i %></option>
							<% } %>
						</select>页
						
				</td>
			</tr>
		</table>
	</div>
	
	<%}} else{%>
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






