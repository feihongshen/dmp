<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.pos.tools.PosEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.pos.tools.PosTradeDetail"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
 List<PosTradeDetail> pospaylist =(List<PosTradeDetail>) request.getAttribute("pospaylist");
 Page page_obj = (Page)request.getAttribute("page_obj");
 List<User> userList = (List<User>)request.getAttribute("userList");
 List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
 List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
 Branch nowbranch = (Branch)request.getAttribute("nowbranch");
 List<User> deliverList =(List<User>)request.getAttribute("deliverlist");
 String  pos_code=request.getParameter("pos_code")!=null&&!"".equals(request.getParameter("pos_code").toString())?request.getParameter("pos_code").toString():"";
 String  starttime=request.getParameter("starttime")!=null&&!"".equals(request.getParameter("starttime").toString())?request.getParameter("starttime").toString():new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 00:00:00";
 String  endtime=request.getParameter("endtime")!=null&&!"".equals(request.getParameter("endtime").toString())?request.getParameter("endtime").toString():new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 long  isSuccessFlag=request.getParameter("isSuccessFlag")!=null?Long.parseLong(request.getParameter("isSuccessFlag").toString()):0;
 long deliveryid = request.getParameter("deliveryid")==null?-1:Long.parseLong(request.getParameter("deliveryid").toString());
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>pos款项查询</title>
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
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			  订单号：<input type="text" name="cwb" />
						      支付方： <select id="pos_code" name="pos_code">
			                		<option value="">全部</option>
					                  <%for(PosEnum em:PosEnum.values()){ 
											String text=em.getText();
											String method=em.getMethod();
												%>
					                	<option value="<%=method%>" <%if(pos_code.equals(method)){%>selected<%} %>><%=text %></option>
					                   <%} %>
			                		</select>
		                交易时间：<input type ="text" name ="starttime" id="strtime"  value ="<%=starttime%>">
                                                        到     <input type ="text" name ="endtime" id="endtime"  value ="<%=endtime%>">
                                      撤销状态：
	                <select name="isSuccessFlag">
	                	<option value="0">--全部--</option>
	                	<option value="1" <%if(isSuccessFlag==1){%>selected<%} %>>交易成功</option>
	                	<option value="2" <%if(isSuccessFlag==2){%>selected<%} %>>撤销</option>
	                </select><br/>
		                站点：<select id="branchid" name="branchid" onchange="updateBranch()">
					<%if(nowbranch!=null){%>
						 <option value="<%=nowbranch.getBranchid()%>" ><%=nowbranch.getBranchname() %></option>
					<%}else if(branchlist != null && branchlist.size()>0){ %>
			             <option value="-1" >全部</option>
			            <%for(Branch u:branchlist){ %>
				             <option value="<%=u.getBranchid()%>" <%if(u.getBranchid() == Integer.parseInt(request.getParameter("branchid")==null?"-1":request.getParameter("branchid").toString()) ) {%>selected="selected"<%}%>><%=u.getBranchname()%></option>
			            <%} } else{%>
						 <option value="-1">请选择</option>
						<%} %>
        	   		</select>
        	   	POS小件员：<select id="deliverid" name="deliveryid" >
							<option value="-1" >请选择POS小件员</option>
								<%if(nowbranch!=null||deliveryid>0){ 
									if(deliverList != null && deliverList.size()>0){ %>
							           <%for( User u:deliverList){ %>
							           		 <option value="<%=u.getUserid()%>" <%if(u.getUserid() ==deliveryid) {%>selected="selected"<%}%>><%=u.getRealname()%></option>
							           	<%} 
					         		 }
								} %>	
        	   				</select>
						  <input type="hidden" id="isshow" name="isshow" value="1" />
						  <input type="button" id="btnval" value="查询" class="input_button2" />
						  <input type ="button" id="btnval2" value="导出excel" class="input_button2" onclick="exportField()"/>
		</form>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div style="overflow-x:scroll; width:100% " id="scroll">
				<table width="1600" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tr class="font_1"> 
						<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">支付方</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">付款金额</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">交易时间</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">小件员</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">交易备注</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">签收人</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">签收类型</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">签收时间</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">终端号</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">是否撤销</td>
						<td  align="center" valign="middle" bgcolor="#eef6ff">签收备注</td>
		
					</tr>
				<% if(pospaylist!=null&&pospaylist.size()>0){ %>
					<%for(PosTradeDetail pe:pospaylist){%>
					<tr>
						<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=pe.getCwb() %>"><%=pe.getCwb() %></a></td>
						<td  align="center" valign="middle"><%for(Customer u:customerList){ if(pe.getCustomerid()==u.getCustomerid()){%><%=u.getCustomername()%>  <%break;}} %></td>
						<td  align="center" valign="middle"><%=pe.getPos_codeText()%></td>
						<td  align="center" valign="middle"><%=pe.getPayAmount()%></td>
						<td  align="center" valign="middle"><%=pe.getTradeTime()%></td>   
						<td  align="center" valign="middle"><%for(User u:userList){ if(pe.getTradeDeliverId()==u.getUserid()){%><%=u.getRealname()%>  <%break;}} %></td>   
						<td  align="center" valign="middle"><%=pe.getPayRemark()%></td>   
						<td  align="center" valign="middle"><%=pe.getSignName()%></td>   
						<td  align="center" valign="middle"><%=pe.getIsSuccessFlag()==2?"":pe.getSigntypeid()==1?"本人签收":"他人签收"%></td>   
						<td  align="center" valign="middle"><%=pe.getSignTime()%></td>   
						<td  align="center" valign="middle"><%=pe.getTerminal_no()%></td>   
						<td  align="center" valign="middle"><%=pe.getIsSuccessFlag()==2?"已撤销":"交易成功"%></td>               
						<td  align="center" valign="middle"><%=pe.getPayDetail()%></td>   
					</tr>
					<%}}%>
				</table>
				</div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
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
				<%}%>
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>






