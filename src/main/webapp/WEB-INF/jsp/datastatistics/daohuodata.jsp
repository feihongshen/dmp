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
int loginUserType =  request.getAttribute("loginUserType")==null ? 0 : (Integer)request.getAttribute("loginUserType");
  List<CwbOrderView> orderlist = (List<CwbOrderView>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Common> commonlist = (List<Common>)request.getAttribute("commonlist");
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  List<Branch> kufanglist = (List<Branch>)request.getAttribute("kufangList");
  List<CustomWareHouse> customWareHouseList = ( List<CustomWareHouse>)request.getAttribute("customWareHouseList");
  List<User> startBranchUserlist = ( List<User>)request.getAttribute("startBranchUserlist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  List operationOrderResultTypeList =(List) request.getAttribute("operationOrderResultTypeStr");
  List cwbordertypeidList =(List) request.getAttribute("cwbordertypeidStr");
  List kufangidList =(List) request.getAttribute("kufangidStr");
  List currentBranchidList =(List) request.getAttribute("currentBranchidStr");
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  String customers=request.getParameter("customerid")==null?"":request.getParameter("customerid");
  long count = (Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>分站到货统计</title>
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
	         var checkval="";
	         $("label").each(function(index){
			     //找到被选中的项
			     if($(this).attr("class")=="checked"){
			        checkval+=$(this).children().attr("value")+",";
			     }
	         });
	         $("#controlStr").val(checkval);
	         if(check()){
	        	 if($(":checked[name=kufangid]").length==0){
	        	 multiSelectAll('kufangid',1,'请选择');}
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
	
	//Added by leoliao at 2016-07-29 限制只能查1个月内的数据
	var startTm = $("#strtime").val();
	var endTm   = $("#endtime").val();
	
	var dtStart = new Date(startTm);
	var dtEnd   = new Date(endTm);
	
	var fromM = dtStart.getMonth();
	var fromD = dtStart.getDate();
	
	var dtNext   = new Date(startTm);	
	dtNext.setMonth(fromM+1, fromD);
	
	/*
	alert("===dtStart==="+dtStart.toString() +
		  "\n===dtEnd===="+dtEnd.toString() +
		  "\n===dtNext==="+dtNext.toString()
	);
	*/
		
	if(dtNext < dtEnd){
		alert("每次只能查询1个月范围内的数据");
		return false;
	}
	//Added end
	
	return true;
		
}
function checkBeishu(id,num){
	// 如用户倍数框留空，光标离开倍数输入框，则倍数输入框默认为1.
	
	if($('#'+id).val()==''||$('#'+id).val()==undefined 
			|| $('#'+id).val()==null||Number($('#'+id).val())<0){
		$('#'+id).val('');
		$('#'+id).focus();
		$('#'+id).select();
	}
	//判断倍数是否在num倍之间
	if(Number($('#'+id).val())> num){
		   $('#'+id).val(num);
		   $('#'+id).focus();
		   $('#'+id).select();
		} 
	//自动转换为半角，不支持标点、小数点以及英文字母等其他输入。
	 var pattern = /^-?\d+$/;
	if(isNaN($('#'+id).val()) || $('#'+id).val().search(pattern)!=0){
	    $('#'+id).val('');
		$('#'+id).focus();
		$('#'+id).select();
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
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	$("#currentBranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择到货站点' });
	$("#kufangid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	
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
	$("#customerid").val(0);
	$("#isnowdata").val(0);
	multiSelectAll('kufangid',0,'请选择');
	multiSelectAll('cwbordertypeid',0,'请选择');
	multiSelectAll('currentBranchid',0,'请选择');
}
</script>
</head>

<body style="background:#f5f5f5" >
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:100px">
	<tr>
		<td align="left">
			到货时间<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1"/>
	</td>
		<td>
		发货库房
			<select name ="kufangid" id ="kufangid" multiple="multiple" style="width: 300px;">
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch b : kufanglist){ %>
			 		<option  value ="<%=b.getBranchid() %>" 
		          <%if(!kufangidList.isEmpty()) 
			            {for(int i=0;i<kufangidList.size();i++){
			            	if(b.getBranchid()== new Long(kufangidList.get(i).toString())){
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
	供货客户
			<select name ="customerid" id ="customerid" class="select1">
	          <option value ="0">请选择</option>
	          <%if(customerlist!=null&&customerlist.size()>0){ %>
	            <%for(Customer c : customerlist){ %>
		          <option value ="<%=c.getCustomerid()%>" <% if(customers!=null&&!"".equals(customers)){if(c.getCustomerid()==Long.parseLong(customers) ){%>selected="selected" <%}} %> ><%=c.getCustomername()%></option>
		        <%}}%>
			</select>
	 订单类型
			<select name ="cwbordertypeid" id ="cwbordertypeid" multiple="multiple">
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
		         <%--  <%if(c.getValue()!=6){ %> --%>
						<option value ="<%=c.getValue() %>" 
		           <%if(!cwbordertypeidList.isEmpty()) 
			            {for(int i=0;i<cwbordertypeidList.size();i++){
			            	if(c.getValue()== new Long(cwbordertypeidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=c.getText()%></option>
				     <%-- <%} %> --%>
		          <%} %>
			</select>
		
	</td>
	<td>
	到货站点
			<select name ="currentBranchid" id ="currentBranchid" multiple="multiple" style="width: 300px;">
		          <%for(Branch b : branchlist){ %>
		          <option value ="<%=b.getBranchid() %>" 
		           <%if(!currentBranchidList.isEmpty()) 
			            {for(int i=0;i<currentBranchidList.size();i++){
			            	if(b.getBranchid()== new Long(currentBranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%}%>
			 </select>
	</td>
	</tr>
	<tr>
	<td>
	只统计当前状态为到货
			<select name ="isnowdata" id ="isnowdata" class="select1" style="width:50px">
	          <option value ="0" <% if((request.getParameter("isnowdata")==null?"0":request.getParameter("isnowdata")).equals("0")){%>selected="selected" <%} %> >否</option>
	          <option value ="1" <% if((request.getParameter("isnowdata")==null?"0":request.getParameter("isnowdata")).equals("1")){%>selected="selected" <%} %> >是</option>
			</select>
				<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
	</td>
	<td>
	<%if(orderlist != null && orderlist.size()>0){  %>
	导出模板
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select>	<%} %>
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
		<input type="hidden" name="sign" id="sign" value="8"/>
		<input type="hidden" name="customerids" value="<%=customers %>"/>
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="isnowdata" id="isnowdata" value="<%=request.getParameter("isnowdata")==null?"0":request.getParameter("isnowdata") %>"/>
		<input type="hidden" name="count" id="count" value="<%=count %>"/>
		
		<div style="display: none;">
			<select name ="kufangid1" id ="kufangid1" multiple="multiple" style="width: 320px;">
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch b : kufanglist){ %>
			 			<option value ="<%=b.getBranchid() %>" 
		          <%if(!kufangidList.isEmpty()) 
			            {for(int i=0;i<kufangidList.size();i++){
			            	if(b.getBranchid()== new Long(kufangidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			</select>
			<select name ="currentBranchid1" id ="currentBranchid1"  multiple="multiple" style="width: 320px;">
		          <%if(branchlist!=null && branchlist.size()>0){ %>
		          <%for(Branch b : branchlist){ %>
						<option value ="<%=b.getBranchid() %>" 
		           <%if(!currentBranchidList.isEmpty()) 
			            {for(int i=0;i<currentBranchidList.size();i++){
			            	if(b.getBranchid()== new Long(currentBranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=b.getBranchname()%></option>
		          <%} }%>
			 </select>
			 <select name ="cwbordertypeid1" id ="cwbordertypeid1" multiple="multiple">
		          <option value ="-1">请选择</option>
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
						<option value ="<%=c.getValue() %>"
						<%if(!cwbordertypeidList.isEmpty()) 
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
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >发货库房</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >到货站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >到货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >地址</td>
				
		</tr>
		
		<% for(CwbOrderView  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getInhouse() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=c.getReceivablefee()%></td>
					<td  align="center" valign="middle"><%=c.getInSiteBranchname() %></td>
					<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
					<td  align="left" valign="middle"><%=c.getConsigneeaddress() %></td>
				 </tr>
		 <%} %>
		 <%if(request.getAttribute("count")!= null){ %>
		<tr bgcolor="#FF3300">
			<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle"></td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font></td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
		</tr>
		<%} %>
	</table>
		
	<%-- <tr>
	<td>代收金额总计：<font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font>&nbsp;元 </td>
	</tr>
	<tr>
	<td>代退金额总计：<font color="red"><%=request.getAttribute("paybackfeesum")==null?"0.00":request.getAttribute("paybackfeesum") %></font>&nbsp;元 </td>
	</tr> --%>
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
$("#isaudit").val(<%=request.getParameter("isaudit")==null?"-1":request.getParameter("isaudit") %>);

</script>

<script type="text/javascript">
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

