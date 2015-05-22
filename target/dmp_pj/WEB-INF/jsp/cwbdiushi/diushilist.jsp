<%@page import="java.math.BigDecimal"%>
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

  List<CwbDiuShiView> cwbDiuShiViewList = (List<CwbDiuShiView>)request.getAttribute("cwbDiuShiViewList");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  
  List customeridList =(List) request.getAttribute("customeridStr");
  List branchidList =(List) request.getAttribute("branchidStr");
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  long count = (Long)request.getAttribute("count");
  CwbDiuShi cdsSum = request.getAttribute("sum")==null?new CwbDiuShi():(CwbDiuShi)request.getAttribute("sum");
  Page page_obj = (Page)request.getAttribute("page_obj");
  List<Role> roleList = request.getAttribute("roleList")==null?null:(List<Role>)request.getAttribute("roleList");
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>货物丢失订单处理</title>
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
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">
//获取下拉框的值
$(function(){
	$("#find").click(function(){
	     if(check()){
			$("#isshow").val(1);
			$("#searchForm").submit();
			$("#find").attr("disabled","disabled");
			$("#find").val("请稍等..");
	     }
	});
})

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
	if($("#endtime").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
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
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
	$("#branchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择机构' });
	
	
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
	$("#isaudit").val(-1);//审核状态
}

</script>
</head>

<body style="background:#f5f5f5" >
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:40px">
	<tr>
		<td align="left">
			审核为货物丢失时间：
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
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
		        
		        <br/>
		        站点名称：<input name="branchname" id="branchname" onkeyup="selectallnexusbranch('<%=request.getContextPath()%>','branchid',$(this).val());"/>
		           <label onclick="change();">
			<select name ="branchid" id ="branchid"  multiple="multiple" style="width: 320px;">
		          <%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){ %>
		          <option value ="<%=b.getBranchid() %>"<%if(!branchidList.isEmpty()) 
			            {for(int i=0;i<branchidList.size();i++){
			            	if(b.getBranchid()== new Long(branchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%}%>
			 </select>
			 </label>
			 处理状态：
			 <select name ="ishandle" id ="ishandle" >
			 	<option value="-1">全部</option>
			 	<%for(CwbDiuShiIsHandleEnum cd : CwbDiuShiIsHandleEnum.values()){ %>
		          <option value ="<%=cd.getValue() %>"<%if((request.getParameter("ishandle")==null?-1:Long.parseLong(request.getParameter("ishandle")))==cd.getValue()){%>selected="selected"<%}%>><%=cd.getText()%></option>
		         <%} %>
			 </select>
			<input type="button" id="find" value="查询" class="input_button2" />
			<%if(!cwbDiuShiViewList.isEmpty()){%>
			&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出" class="input_button1" onclick="exportField();"/>
			<%} %>
		</td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath()%>/cwbdiushi/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="ishandle1" id="ishandle1" value="<%=request.getParameter("ishandle")==null?"-1":request.getParameter("ishandle")%>"/>
		<div style="display: none;">
			<select name ="customerid1" id ="customerid1" multiple="multiple" >
		          <%if(customerlist!=null&&customerlist.size()>0)for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		            <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>
		           
		           
		           ><%=c.getCustomername() %></option>
		          <%} %>
	        </select>
	        <select name ="branchid1" id ="branchid1"   multiple="multiple" >
	          <%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){ %>
	          <option value ="<%=b.getBranchid() %>"<%if(!branchidList.isEmpty()) 
		            {for(int i=0;i<branchidList.size();i++){
		            	if(b.getBranchid()== new Long(branchidList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>><%=b.getBranchname()%></option>
	          <%}%>
		 	</select>
		</div>
	</form>
	</div>
	<div class="right_title">
	<div style="height:60px"></div><%if(cwbDiuShiViewList != null && cwbDiuShiViewList.size()>0){  %>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">代收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">应退金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">货物金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">赔偿金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">审核时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">审核人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">审核人职位</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">当前状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff">操作</td>
				
		</tr>
		
		<% for(CwbDiuShiView  cds : cwbDiuShiViewList){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=cds.getCwb() %>"><%=cds.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=cds.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=cds.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=cds.getReceivablefee()==null?BigDecimal.ZERO:cds.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=cds.getPaybackfee()==null?BigDecimal.ZERO:cds.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=cds.getCaramount()==null?BigDecimal.ZERO:cds.getCaramount() %></td>
					<td  align="center" valign="middle"><%=cds.getPayamount()==null?BigDecimal.ZERO:cds.getPayamount() %></td>
					<td  align="center" valign="middle"><%=cds.getShenhetime() %></td>
					<td  align="center" valign="middle"><%=cds.getShenheName() %></td>
					<td  align="center" valign="middle"><%if(roleList!=null&&roleList.size()>0){for(Role r : roleList){if(r.getRoleid()==cds.getShenheRole()){ %><%=r.getRolename() %><%}}} %></td>
					<td  align="center" valign="middle"><%=cds.getFlowordertypeMethod() %></td>
					<td  align="center" valign="middle"><%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(cds.getDeliverystate()==ds.getValue()){ %><%=ds.getText() %><%}} %></td>
					<td  align="center" valign="middle"><%for(CwbDiuShiIsHandleEnum ds : CwbDiuShiIsHandleEnum.values()){if(cds.getIshandle()==ds.getValue()){ %><%=ds.getText() %><%}} %></td>
				 </tr>
		 <% }%>
		 <%if(request.getAttribute("count")!= null){ %>
		<tr bgcolor="#FF3300">
			<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td class="high" align="center" valign="middle"><font color="red"><%=cdsSum.getReceivablefee()==null?BigDecimal.ZERO:cdsSum.getReceivablefee() %></font>&nbsp;元 </td>
			<td class="high" align="center" valign="middle"><font color="red"><%=cdsSum.getPaybackfee()==null?BigDecimal.ZERO:cdsSum.getPaybackfee() %></font>&nbsp;元 </td>
			<td class="high" align="center" valign="middle"><font color="red"><%=cdsSum.getCaramount()==null?BigDecimal.ZERO:cdsSum.getCaramount() %></font>&nbsp;元 </td>
			<td class="high" align="center" valign="middle"><font color="red"><%=cdsSum.getPayamount()==null?BigDecimal.ZERO:cdsSum.getPayamount() %></font>&nbsp;元 </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
		</tr>
		<%} %>
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
$("#ishandle").val(<%=request.getParameter("ishandle")==null?"-1":request.getParameter("ishandle") %>);

</script>

<script type="text/javascript">
function exportField(){
	if($("#isshow").val()=="1"&&<%=cwbDiuShiViewList != null && cwbDiuShiViewList.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval0").attr("disabled","disabled");
	 	$("#btnval0").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}
</script>


</body>
</html>

