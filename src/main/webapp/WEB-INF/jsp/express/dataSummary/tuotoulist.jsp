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
  List customeridList =(List) request.getAttribute("customeridStr");
  List dispatchbranchidList =(List) request.getAttribute("dispatchbranchidStr");
  List cwbordertypeidList =(List) request.getAttribute("cwbordertypeidStr");
  
  List<User> deliverlist = request.getAttribute("deliverlist")==null?null:( List<User>)request.getAttribute("deliverlist");
  
  String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
  long count = (Long)request.getAttribute("count");
  List<User> userList = request.getAttribute("userList")==null?null:( List<User>)request.getAttribute("userList");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>妥投订单汇总</title>
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

<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>
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
//	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
	$("#dispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择配送站点' });
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
	$("#operationOrderResultType").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择' });
    $("#customerid").multipleSelect({
        placeholder: "请选择供货商",
        filter: true
    });
});
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}
function orderForm(ordername){
	if($("#orderByTypeId").val()=="ASC"){
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("DESC");
    }else {
    	$("#orderByNameId").val(ordername);
    	$("#orderByTypeId").val("ASC");
    }
	if(check()){
		$("#searchForm").submit();
	}
}



var i =<%=request.getAttribute("check")==null?0:Integer.parseInt(request.getAttribute("check").toString())%>;
function change(){
	i=0;
}
function changeBranchDeliver(){
	if(i==0){
		var checkval="";
		$("label[class=checked]>input[name=dispatchbranchid]").each(function(index){
		     //找到被选中的项
		        checkval+=$(this).val()+",";
	       });
			$.ajax({
				url:"<%=request.getContextPath()%>/datastatistics/updateDeliverByBranchids",//后台处理程序
				type:"POST",//数据发送方式 
				data:"branchid="+checkval,//参数
				dataType:'json',//接受数据格式
				success:function(json){
					$("#deliverid").empty();//清空下拉框//$("#select").html('');
					$("<option value='-1'>请选择</option>").appendTo("#deliverid");//添加下拉框的option
					for(var j = 0 ; j < json.length ; j++){
						if(json[j].userid == <%=request.getAttribute("deliverid")==null?-1:request.getAttribute("deliverid") %>){
							if(json[j].employeestatus==<%=UserEmployeestatusEnum.LiZhi.getValue()%>){
								$("<option value='"+json[j].userid+"' selected='selected'  >"+json[j].realname+"(离职)</option>").appendTo("#deliverid");
							}else{
								$("<option value='"+json[j].userid+"' selected='selected'  >"+json[j].realname+"</option>").appendTo("#deliverid");
							}
						}else{
							if(json[j].employeestatus==<%=UserEmployeestatusEnum.LiZhi.getValue()%>){
								$("<option value='"+json[j].userid+"'>"+json[j].realname+"(离职)</option>").appendTo("#deliverid");
							}else{
								$("<option value='"+json[j].userid+"'>"+json[j].realname+"</option>").appendTo("#deliverid");
							}
						}
					}
				}		
			});
			i++;
	}
}

function isauditEdit(){
	if($("#isauditTime").val()==1){
		$("#isaudit").val("-1");
		$("#isauditLabel").hide();
	}else{
		$("#isauditLabel").show();
	}
	
}
function clearSelect(){
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#startbranchid").val(-1);//站点名称
	$("#deliverid").val(-1);//小件员
	$("#isauditTime").val(0);//时间类型
	$("#ismohu").val(1);//站点名称
	$("#branchname").val("");
	$("#operationOrderResultType").val(1);//配送结果
	$("#isaudit").val(-1);//审核状态
	$("#paytype").val(-1);//支付类型
	$("#paybackfeeIsZero").val(-1);//应收金额
	$("#exportmould").val(0);
	multiSelectAll('dispatchbranchid',0,'请选择配送站点');//配送站点
	multiSelectAll('cwbordertypeid',0,'请选择');//订单类型
//	multiSelectAll('customerid',0,'请选择供货商');//供货商
	multiSelectAll('operationOrderResultType',0,'请选择');//应收金额
    $("#customerid").multipleSelect("uncheckAll").multipleSelect("refresh");
	
}
</script>
</head>

<body style="background:#f5f5f5" onload="isauditEdit();">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	
	<input type="hidden" id="orderByNameId" name="orderbyName" value="emaildate"/>
	<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:150px">
	<tr>
	<td>
	时间类型
			  <select id="isauditTime" name="isauditTime" onchange="isauditEdit()" class="select1">
			<option value="0" <%=request.getParameter("isauditTime")!=null&&request.getParameter("isauditTime").equals("0")?"selected":"" %>>反馈时间</option>
			<option value="1" <%=request.getParameter("isauditTime")!=null&&request.getParameter("isauditTime").equals("1")?"selected":"" %>>审核时间</option>
			</select>
				<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1"/>
			到
				<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1"/>
	</td>
	<td>
							<label id="isauditLabel">    归班审核状态
			<select name ="isaudit" id ="isaudit" class="select1">
		          <option value ="-1">全部</option>
		           <option value ="0">未审核</option>
		           <option value ="1">已审核</option>
		    </select>
		    </label>
	</td>
	</tr>
	<tr>
		<td align="left">
			站点名称
			<select name="ismohu" id="ismohu" class="select1">
					<option value ="1"<%if(1==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>模糊匹配</option>
					<option value ="2"<%if(2==(request.getParameter("ismohu")==null?1:Long.parseLong(request.getParameter("ismohu")))){%>selected="selected"<%}%>>精确匹配</option>
			 </select>
			 <input name="branchname" id="branchname" class="input_text1" onKeyDown="if(event.keyCode==13&&$(this).val().length>0){moHuOrJingQueSlect($('#ismohu').val(),'<%=request.getContextPath()%>','dispatchbranchid',$(this).val());}"/>
		</td>
		<td>			
		配送站点
			  <label onclick="change();">
			<select name ="dispatchbranchid" id ="dispatchbranchid"  multiple="multiple" style="width: 300px;">
		          <%for(Branch b : branchlist){ %>
		          <option value ="<%=b.getBranchid() %>" 
		           <%if(!dispatchbranchidList.isEmpty()) 
			            {for(int i=0;i<dispatchbranchidList.size();i++){
			            	if(b.getBranchid()== new Long(dispatchbranchidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>  ><%=b.getBranchname()%></option>
		          <%}%>
			 </select>
			 [<a href="javascript:multiSelectAll('dispatchbranchid',1,'请选择');">全选</a>]
			 [<a href="javascript:multiSelectAll('dispatchbranchid',0,'请选择');">取消全选</a>]
			 </label>
		</td>
	</tr>
	<tr>
	<td>
	  订单类型
			<select name ="cwbordertypeid" id ="cwbordertypeid" multiple="multiple" style="width:120px;">
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ 
		          if(c.getValue() == CwbOrderTypeIdEnum.Express.getValue()){%>
						<option value ="<%=c.getValue() %>" selected="selected" ><%=c.getText()%></option>
		          <%break;}} %>
			</select>
			 小件员
			<select name ="deliverid" id ="deliverid" onclick="changeBranchDeliver();" class="select1">
		          <option value ="-1">请选择</option>
		          <%if(deliverlist!=null&&deliverlist.size()>0)for(User u : deliverlist){ %>
		          	<option value="<%=u.getUserid() %>" <%if(u.getUserid()==(request.getParameter("deliverid")==null?-1:Long.parseLong(request.getParameter("deliverid").toString()))){ %>selected="selected"<%} %>><%=u.getRealname() %></option>
		          <%} %>
			 </select>
	
	</td>
	<td>
	供货客户
			<select name ="customerid" id ="customerid" multiple="multiple" style="width: 300px;">
		          <%for(Customer c : customerlist){ %>
		           <option value ="<%=c.getCustomerid() %>" 
		           <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%> ><%=c.getCustomername() %></option>
		          <%} %>
		    </select>
		    <%--[<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]--%>
			<%--[<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]--%>
	</td>
	</tr>
	<tr>
	<td>
	支付方式
			<select id="paytype" name ="paytype" class="select1">
		    <option value ="-1">请选择支付方式</option>
              <%for(PaytypeEnum c : PaytypeEnum.values()){ %>
				<option value ="<%=c.getValue() %>" 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("paytype")==null?"-1":request.getParameter("paytype"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		         <%} %>
			</select>
			 应收金额
			 <select name="paybackfeeIsZero" id="paybackfeeIsZero" class="select1">
						 <option value="-1" >全部</option>
						 <option value="0" <%=request.getParameter("paybackfeeIsZero")!=null&&request.getParameter("paybackfeeIsZero").equals("0")?"selected":"" %>>=0</option>
						 <option value="1" <%=request.getParameter("paybackfeeIsZero")!=null&&request.getParameter("paybackfeeIsZero").equals("1")?"selected":"" %>>>0</option>
						 </select>


	</td>
	<td>
			    配送结果
		    <select name =operationOrderResultType id ="operationOrderResultType" multiple="multiple" style="width: 300px;">
		    	<option value ="<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>"
		    	<%if(!operationOrderResultTypeList.isEmpty()) 
		            {for(int i=0;i<operationOrderResultTypeList.size();i++){
		            	if(DeliveryStateEnum.PeiSongChengGong.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>
		    	><%=DeliveryStateEnum.PeiSongChengGong.getText() %></option>
		    	<option value ="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue()%>"
		    	<%if(!operationOrderResultTypeList.isEmpty()) 
		            {for(int i=0;i<operationOrderResultTypeList.size();i++){
		            	if(DeliveryStateEnum.ShangMenHuanChengGong.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>
		    	><%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %></option>
		    	<option value ="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>"
		    	<%if(!operationOrderResultTypeList.isEmpty()) 
		            {for(int i=0;i<operationOrderResultTypeList.size();i++){
		            	if(DeliveryStateEnum.ShangMenTuiChengGong.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>
		    	><%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %></option>
		</select>
	</td>
	</tr>
	<tr>
	<td>
				<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
	</td>
	<td>
	<%if(orderlist != null && orderlist.size()>0){  %>
	导出模板
			<select name ="exportmould" id ="exportmould" class="select1">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
			</select><%} %>
			<%if(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval0" value="导出1-<%=count %>" class="input_button1" onclick="exportField('0','0');"/>
			<%}else{for(int j=0;j<count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>');"/>
				<%}else if(j!=0&&j!=(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%}else if(j==(count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input size="10px" type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=count %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%} %>
			<%}} %>
	</td>
	</tr>
</table>
	</form>
	<form action="<%=request.getContextPath()%>/datastatistics4express/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begin" id="begin" value="0"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="sign" id="sign" value="5"/>
		<input type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>"/>
		<input type="hidden" name="enddate1" id="enddate1" value="<%=endtime%>"/>
		<input type="hidden" name="isaudit1" id="isaudit1" value="<%=request.getParameter("isaudit")==null?"-1":request.getParameter("isaudit")%>"/>
		<input type="hidden" name="isauditTime1" id="isauditTime1" value="<%=request.getParameter("isauditTime")==null?"0":request.getParameter("isauditTime")%>"/>
		<input type="hidden" name="deliverid1" id="deliverid1" value="<%=request.getParameter("deliverid")==null?"-1":request.getParameter("deliverid")%>"/>
		<input type="hidden" name="paytype1" id="paytype1" value="<%=request.getParameter("paytype")==null?"-1":request.getParameter("paytype")%>"/>
		<input type="hidden" name="orderbyName1" id="orderbyName1" value="<%=request.getParameter("orderbyName")==null?"emaildate":request.getParameter("orderbyName")%>"/>
		<input type="hidden" name="orderbyType1" id="orderbyType1" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
		<input name="paybackfeeIsZero1" value="<%=request.getParameter("paybackfeeIsZero")!=null?request.getParameter("paybackfeeIsZero"):"-1"%>" type="hidden"/>
		<input type="hidden" name="count" id="count" value="<%=count %>"/>
		<div style="display: none;">
		<select name =operationOrderResultTypes1 id ="operationOrderResultTypes1" multiple="multiple" style="width: 320px;">
               	<%for(DeliveryStateEnum c : DeliveryStateEnum.values()){ %>
		          <option value ="<%=c.getValue() %>" 
		                   <%if(!operationOrderResultTypeList.isEmpty()) 
						            {for(int i=0;i<operationOrderResultTypeList.size();i++){
						            	if(c.getValue()== new Long(operationOrderResultTypeList.get(i).toString())){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
					     }%>><%=c.getText()%></option>
					<%} %>
		</select>
		<select name ="customerid1" id ="customerid1" multiple="multiple" >
		          <%for(Customer c : customerlist){ %>
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
	        <select name ="dispatchbranchid1" id ="dispatchbranchid1"   multiple="multiple" >
	          <%for(Branch b : branchlist){ %>
	          <option value ="<%=b.getBranchid() %>"
	          <%if(!dispatchbranchidList.isEmpty()) 
		            {for(int i=0;i<dispatchbranchidList.size();i++){
		            	if(b.getBranchid()== new Long(dispatchbranchidList.get(i).toString())){
		            		%>selected="selected"<%
		            	 break;
		            	}
		            }
			     }%>><%=b.getBranchname()%></option>
	          <%}%>
		 	</select>
		 	<select name ="cwbordertypeid1" id ="cwbordertypeid1" multiple="multiple" >
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
	<div style="height:160px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="1500" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('deliverybranchid');" >配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerid');" >客户</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('deliverid');" >小件员</td>
<!-- 				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >发货时间</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >应收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('paybackfee');" >应退金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbordertypeid');" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneeaddress');" >地址</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >原支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >现支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >审核状态</td>
				
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >代收货款</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('shouldfare');" >应收运费</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >付款方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >重量</td>
				
		</tr>
		
		<% for(CwbOrderView  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><a  target="_blank" href="<%=request.getContextPath()%>/order/queckSelectOrder/<%=c.getCwb() %>"><%=c.getCwb() %></a></td>
					<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getDelivername() %></td>
<%-- 					<td  align="center" valign="middle"><%=c.getEmaildate() %></td> --%>
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneeaddress() %></td>
					<td  align="center" valign="middle"><%=c.getPaytype_old() %></td>
					<td  align="center" valign="middle"><%=c.getPaytype()%></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getAuditstateStr() %></td>
					
					<td  align="center" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="center" valign="middle"><%=c.getShouldfare() %></td>
					<td  align="center" valign="middle"><%=c.getExpressPayWay() %></td>
					<td  align="center" valign="middle"><%=c.getRealweight() %></td>
				 </tr>
		 <%} %>
		 <%if(request.getAttribute("count")!= null){ %>
		<tr bgcolor="#FF3300">
			<td  align="center" valign="middle" class="high">合计：<font color="red"><%=count %></font>&nbsp;单  </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font>&nbsp;元 </td>
			<td  align="center" valign="middle" class="high"><font color="red"><%=request.getAttribute("paybackfeesum")==null?"0.00":request.getAttribute("paybackfeesum") %></font>&nbsp;元 </td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			<td  align="center" valign="middle">&nbsp;</td>
			
			<td  align="center" valign="middle">&nbsp;</td>
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

