<%@page import="org.apache.axis.constants.Use"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.OutwarehousegroupOperateEnum"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> cwbList = request.getAttribute("cwbList")==null?null:(List<CwbOrder>)request.getAttribute("cwbList");
Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
List<Branch> bList = (List<Branch>)request.getAttribute("branchlist");
String type=(String)request.getParameter("leixing")==null?"":request.getParameter("leixing");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
List customeridList =request.getAttribute("customeridStr")==null?null:(List) request.getAttribute("customeridStr");
String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
List<User> userlist =request.getAttribute("userlist")==null?null:(List<User>) request.getAttribute("userlist");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>上门换订单交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>

<script type="text/javascript">
$(function(){
	$("#beginemaildate").datepicker();
	$("#endemaildate").datepicker();
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
});

function bdprint(){
	var isprint = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		});
	if(isprint==""){
		alert("请选择要打印的订单！");
	}else{
		$("#modal1").val($("#modal").val());
		$("#selectforsmtbdprintForm").submit();
	}
}

function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
function newprint(){
	var checkval="";
    $("label").each(function(index){
 //找到被选中的项
       if($(this).attr("class")=="checked"){
       checkval+=$(this).children().attr("value")+",";
       }
    });
    $("#customeridstr").val(checkval);
    $("#modal2").val($("#modal").val());
    $("#branchid2").val($("#branchid").val());
    $("#noPrintForm").submit();
}
function check(){
	if($("#printType").val()==1){
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
});
function sub(){
	if(check()){
		$("#searchForm").submit();
	}
}
</script>
</head>
<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form action="1" method="post" id="searchForm" >
		配送站点
		<select id="branchid" name="branchid">
			<%if(bList != null && bList.size()>0){ %>
				<%if(bList.size()>1){ %>
				<option value="-1">全部</option>
				<%} %>
				<%for(Branch b : bList){ %>
					<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
				<%} 
			}%>
		</select>
		供货商
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%if(customerlist!=null&&customerlist.size()>0)for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(customeridList!=null&&!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=c.getCustomername() %></option>
		          <%} %>
		        </select>[<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
						[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
		    <select id="leixing" name="leixing">
		    <option value="0">--请选择--</option>
			<option value="2" <%if("2".equals(type)){
			            		%>selected="selected"<%
			            	}%>>上门退</option>
			<option value="3" <%if("3".equals(type)){
			            		%>selected="selected"<%
			            	}%>>上门换</option>
		 </select>
		 <input type="hidden" id="isshow" name="isshow" value="1" />
	      　　<input type="button" id="find" value="查询" class="input_button2" onclick="sub();"/>
	      <select id="modal" name="modal">
			<option value="0">默认模版</option>
			<option value="3">乐蜂网模版</option>
		 </select>
	      <%if(cwbList!=null&&cwbList.size()>0){ %>
	      <input type="button" onclick="bdprint();" value="打印" class="input_button2" />
	      <%} %>
	</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr class="font_1">
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
			<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">指定小件员</td>
			<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">备注信息</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">实际重量</td>
			<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">取货数量</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">客户地址</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
			<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">打印时间</td>
		</tr>
	</table>
	<form action="<%=request.getContextPath() %>/cwborder/selectforsmhprint" method="post" id="noPrintForm" >
	<input type="hidden" id="modal2" name="modal" value="0"/>
	<input type="hidden" id="branchid2" name="nextbranchid" value="-1"/>
	<input type="hidden" id="customeridstr" name="customeridstr" value=""/>
	
	
	</form>	 
	<form action="<%=request.getContextPath() %>/cwborder/selectforsmhbdprint" method="post" id="selectforsmtbdprintForm" >
	<input type="hidden" id="modal1" name="modal" value="0"/>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<%if(cwbList!=null&&cwbList.size()>0)for(CwbOrder c: cwbList){ %>
			<tr>
			 	<td width="7%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=c.getCwb() %>"/></td>
			    <td width="10%" align="center" valign="middle"><%=c.getCwb() %></td>
				<td width="7%" align="center" valign="middle">
				<%for(Customer cu : customerlist){
			            	if(cu.getCustomerid()== c.getCustomerid()){%>
			            	<%=cu.getCustomername() %>
			            	<%
			            	 break;
			            	}
			            }%> 
			    </td>
				<td width="7%" align="center" valign="middle"><%for(Branch br : bList){
			            	if(br.getBranchid()== c.getDeliverybranchid()){%>
			            	<%=br.getBranchname() %>
			            	<%
			            	 break;
			            	}
			            }%> </td>
				<td width="9%" align="center" valign="middle"><%=c.getEmaildate() %></td>
				<td width="5%" align="center" valign="middle"><%for(User u: userlist){ if(u.getUserid()==c.getDeliverid()){%><%=u.getRealname()%> <%}} %></td>
				<td width="7%" align="center" valign="middle"><%=c.getCwbremark() %></td>
				<td width="5%" align="center" valign="middle"><%=c.getCarrealweight()%></td>
				<td width="4%" align="center" valign="middle"><%=c.getBackcarnum() %></td>
				<td width="20%" align="left" valign="middle"><%=c.getConsigneeaddress() %></td>
				<td width="10%" align="center" valign="middle">
				<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){
					if(ft.getValue()==c.getFlowordertype()){%>
						<%=ft.getText() %>
				<%}} %>
				</td>
				<td width="9%" align="center" valign="middle" ><%=c.getPrinttime() %></td>
			</tr>
		<%} %>
	</table>
	</form>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
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
$("#branchid").val(<%=request.getParameter("branchid") %>);
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>
