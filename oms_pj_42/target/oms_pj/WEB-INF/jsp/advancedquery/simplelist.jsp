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
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("orderlist");
  List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
  List<Common> commonlist = (List<Common>)request.getAttribute("commonlist");
  List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
  List<Branch> kufanglist = (List<Branch>)request.getAttribute("kufangList");
  List<CustomWareHouse> customWareHouseList = ( List<CustomWareHouse>)request.getAttribute("customWareHouseList");
  List<User> startBranchUserlist = ( List<User>)request.getAttribute("startBranchUserlist");
  List<User> nextBranchUserlist = ( List<User>)request.getAttribute("nextBranchUserlist");
  List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
  List orderResultTypelist =(List) request.getAttribute("orderResultTypeStr");
  long count = (Long)request.getAttribute("count");
  Page page_obj = (Page)request.getAttribute("page_obj");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
	   // Options displayed in comma-separated list
	   $("#orderResultType").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择当前配送结果' });
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
	            $("#isshow").val(1);
			 	$("#cwbandtranscwbId").val('');
		    	$("#searchForm").submit();
	         }
	   });
	});
</script>
<script type="text/javascript">

function check(){
	/* if($("#kufangid").val()==-1 && $("#currentbranchid").val()==-1 && $("#dispatchbranchid").val()==-1){
		alert("请选择库房或站点");
		return false;
	} */
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
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
</script>
<script>
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
function updateDispatchBranchDeliver(){
	$.ajax({
		url:"<%=request.getContextPath()%>/advancedquery/updateDeliver",//后台处理程序
		type:"POST",//数据发送方式 
		data:"branchid="+$("#dispatchbranchid").val(),//参数
		dataType:'json',//接受数据格式
		success:function(json){
			$("#dispatchdeliveryid").empty();//清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择小件员</option>").appendTo("#dispatchdeliveryid");//添加下拉框的option
			for(var j = 0 ; j < json.length ; j++){
				$("<option value='"+json[j].userid+"'>"+json[j].realname+"</option>").appendTo("#dispatchdeliveryid");
				}
			}		
	});
}
function updateCustomerwarehouse(){
	$.ajax({
		url:"<%=request.getContextPath()%>/advancedquery/updateCustomerwarehouse",//后台处理程序
		type:"POST",//数据发送方式 
		data:"customerid="+$("#customerid").val(),//参数
		dataType:'json',//接受数据格式
		success:function(json){
			$("#customerwarehouseid").empty();//清空下拉框//$("#select").html('');
			$("<option value='-1'>请选择发货仓库</option>").appendTo("#customerwarehouseid");//添加下拉框的option
			for(var j = 0 ; j < json.length ; j++){
				$("<option value='"+json[j].warehouseid+"'>"+json[j].customerwarehouse+"</option>").appendTo("#customerwarehouseid");
				}
			}		
	});
}
function clearSelect(){
	$("#customerid").val(0);//供货商
	$("#startbranchid").val(-1);//上一站
	$("#nextbranchid").val(-1);//下一站
	$("#kufangid").val(-1);//库房
	$("#datetype").val(0);//时间类型
	$("#paytype").val(-1);//支付类型
	$("#flowordertype").val(-1);//订单状态
	$("#cwbordertypeid").val(-1);//订单类型
	$("#beginWeight").val('');//重量
	$("#endWeight").val('');//重量
	$("#beginsendcarnum").val('');//件数
	$("#endsendcarnum").val('');//件数
	$("#cwbandtranscwbId").val('');//订单号
	$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#consigneename").val('');//收件人
	$("#packagecode").val('');//收件人
	$("#consigneemobile").val('');//收件人手机
	$("#carsize").val('');//体积
	$("#dispatchdeliveryid").empty();//配送小件员
	$("#customerwarehouseid").empty();//发货仓库
	$("#currentbranchid").val(-1);//当前站点
	$("#dispatchbranchid").val(-1);//配送站点
	$("<option value='-1'>请选择发货仓库</option>").appendTo("#customerwarehouseid");//添加下拉框的option
	$("<option value='-1'>请选择配送小件员</option>").appendTo("#dispatchdeliveryid");//添加下拉框的option
}
</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="inputselect_box">
	<form action="1" method="post" id="searchForm">
	<input type="hidden" id="orderByNameId" name="orderbyName" value="emaildate"/>
	<input type="hidden" id="orderByTypeId" name="orderbyType" value="<%=request.getParameter("orderbyType")==null?"DESC":request.getParameter("orderbyType") %>"/>
	<input type="hidden" id="isshow" name="isshow" value="<%=request.getParameter("isshow")==null?"0":request.getParameter("isshow") %>" />
	<input type="hidden" name="page" value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:90px">
	<tr>
		<td align="left">
			<select name ="customerid" id ="customerid" onchange="updateCustomerwarehouse();">
		          <option value ="0">请选择供货商</option>
		          <%for(Customer c : customerlist){ %>
		           <option value =<%=c.getCustomerid() %> 
		           <%if(c.getCustomerid() == Integer.parseInt(request.getParameter("customerid")==null?"0":request.getParameter("customerid"))  ){ %>selected="selected" <%} %> ><%=c.getCustomername() %></option>
		          <%} %>
		        </select>
		   	<select name ="cwbordertypeid" id ="cwbordertypeid">
		          <option value ="-1">请选择订单类型</option>
		          <%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
			<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("cwbordertypeid")==null?"-2":request.getParameter("cwbordertypeid"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		          <%} %>
			</select>	
		   <select name ="customerwarehouseid" id ="customerwarehouseid">
		          <option value ="-1">请选择发货仓库</option>

		          <%for(CustomWareHouse c : customWareHouseList){ %>
			 <option value =<%=c.getWarehouseid() %> 
		           <%if(c.getWarehouseid()==Integer.parseInt(request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid"))){ %>selected="selected" <%} %> ><%=c.getCustomerwarehouse()%></option>
		          <%}%>
			</select>
			<select name ="kufangid" id ="kufangid">
		          <option value ="-1">请选择库房</option>
		          <%if(kufanglist!=null && kufanglist.size()>0) {%>
		          <%for(Branch c : kufanglist){ %>
			 <option value =<%=c.getBranchid() %> 
		          <%if(c.getBranchid()==Integer.parseInt(request.getParameter("kufangid")==null?"-1":request.getParameter("kufangid"))){ %>selected="selected" <%} %> ><%=c.getBranchname()%></option>
		          <%} }%>
			</select>
			<select name ="flowordertype" id ="flowordertype">
		          <option value ="-1">请选择订单状态</option>
		          <%for(FlowOrderTypeEnum c : FlowOrderTypeEnum.values()){ %>
			<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		          <%} %>
			</select>
			
			<select name ="dispatchbranchid" id ="dispatchbranchid" onchange="updateDispatchBranchDeliver();">
		          <option value ="-1">请选择配送站点</option>
		          <%for(Branch c : branchlist){ %>
		          <option value =<%=c.getBranchid() %> 
		           <%if(c.getBranchid()==Integer.parseInt(request.getParameter("dispatchbranchid")==null?"-1":request.getParameter("dispatchbranchid"))){ %>selected="selected" <%} %> ><%=c.getBranchname()%></option>
		          <%}%>
			 </select>
			<select id="dispatchdeliveryid" name ="dispatchdeliveryid">
		    <option value ="-1">请选择配送小件员</option>
		          <%for(User c : nextBranchUserlist){ %>
			 <option value =<%=c.getUserid() %> 
		           <%if(c.getUserid()==Integer.parseInt(request.getParameter("dispatchdeliveryid")==null?"-1":request.getParameter("dispatchdeliveryid"))){ %>selected="selected" <%} %> ><%=c.getRealname()%></option>
		          <%}%>
			</select>

			</td>
	</tr>
	<tr>
		<td align="left">
		    
			包号:<input type="text" name="packagecode" size="8" value="<%=request.getParameter("packagecode")==null?"":request.getParameter("packagecode") %>" id="packagecode"/>
			货物重量(kg)：<input type="text" name="beginWeight" id="beginWeight"
			onkeyup="value=value.replace('０','0').replace('１','1').replace('２','2').replace('３', '3').replace('４','4').replace('５','5').replace('６','6').replace('７', '7').replace('８','8').replace('９','9');checkBeishu('beginWeight',999999);" 
			value="<%=request.getParameter("beginWeight")==null ||Integer.parseInt(request.getParameter("beginWeight").equals("")?"-1":request.getParameter("beginWeight"))==-1 ?"":request.getParameter("beginWeight") %>" size="8"/>到
			<input type="text" name="endWeight" id="endWeight"
			onkeyup="value=value.replace('０','0').replace('１','1').replace('２','2').replace('３', '3').replace('４','4').replace('５','5').replace('６','6').replace('７', '7').replace('８','8').replace('９','9');checkBeishu('endWeight',999999);"
			value="<%=request.getParameter("endWeight")==null ||Integer.parseInt(request.getParameter("endWeight").equals("")?"-1":request.getParameter("endWeight"))==-1 ?"":request.getParameter("endWeight") %>" size="8"/>

			   发货件数： <input type="text" name="beginsendcarnum" id="beginsendcarnum"
			onkeyup="value=value.replace('０','0').replace('１','1').replace('２','2').replace('３', '3').replace('４','4').replace('５','5').replace('６','6').replace('７', '7').replace('８','8').replace('９','9');checkBeishu('beginsendcarnum',999999);" 
			value="<%=request.getParameter("beginsendcarnum")==null ||Integer.parseInt(request.getParameter("beginsendcarnum").equals("")?"-1":request.getParameter("beginsendcarnum"))==-1 ?"":request.getParameter("beginsendcarnum") %>" size="4"/>到
			<input type="text" name="endsendcarnum" id="endsendcarnum"
			onkeyup="value=value.replace('０','0').replace('１','1').replace('２','2').replace('３', '3').replace('４','4').replace('５','5').replace('６','6').replace('７', '7').replace('８','8').replace('９','9');checkBeishu('endsendcarnum',999999);"
			value="<%=request.getParameter("endsendcarnum")==null ||Integer.parseInt(request.getParameter("endsendcarnum").equals("")?"-1":request.getParameter("endsendcarnum"))==-1 ?"":request.getParameter("endsendcarnum") %>" size="4"/>
		    
	             货物尺寸:<select id="carsize" name ="carsize">
		    <option value ="">请选择</option>
			 <option value ="超大" 
		           <%if("超大".equals(request.getParameter("carsize")==null?"-1":request.getParameter("carsize"))){ %>selected="selected" <%} %> >超大</option>
		     <option value ="大"  <%if("大".equals(request.getParameter("carsize")==null?"-1":request.getParameter("carsize"))){ %>selected="selected" <%} %> >大</option>
		     <option value ="中"  <%if("中".equals(request.getParameter("carsize")==null?"-1":request.getParameter("carsize"))){ %>selected="selected" <%} %> >中</option>
		     <option value ="小"  <%if("小".equals(request.getParameter("carsize")==null?"-1":request.getParameter("carsize"))){ %>selected="selected" <%} %> >小</option>
			</select>
		收件人:<input type="text" name="consigneename" size="8" value="<%=request.getParameter("consigneename")==null?"":request.getParameter("consigneename") %>" id="consigneename"/>
		收件人手机/电话:<input type="text" name="consigneemobile" value="<%=request.getParameter("consigneemobile")==null?"":request.getParameter("consigneemobile") %>" id="consigneemobile" />

			
			</td>
	</tr>
	<tr><td align="left">操作信息
		<select id="datetype" name ="datetype">
			<option value="0">请选择操作</option>
            <option value="1">发货时间</option>
            <option value="2">入库时间</option>
            <option value="3">出库时间</option>
            <option value="4">到站时间</option>
            <option value="5">领货时间</option>
            <option value="7">反馈时间</option>
            <option value="8">归班时间</option>
		</select>
			<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>
			到
			<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>"/>
		 当前订单配送结果:
		<select name =orderResultType id ="orderResultType" multiple="multiple" style="width: 320px;">
               	<%for(DeliveryStateEnum c : DeliveryStateEnum.values()){ %>
		          <option value =<%=c.getValue() %> 
		                   <%if(!orderResultTypelist.isEmpty()) 
						            {for(int i=0;i<orderResultTypelist.size();i++){
						            	if(c.getValue()== new Long(orderResultTypelist.get(i).toString())){
						            		%>selected="selected"<%
						            	 break;
						            	}
						            }
					     }%>><%=c.getText()%></option>
					<%} %>
		</select>
		</td>
	</tr>
	<tr>
	<td align="left">
	  
		<select id="paytype" name ="paytype">
		    <option value ="-1">请选择支付方式</option>
              <%for(PaytypeEnum c : PaytypeEnum.values()){ %>
				<option value =<%=c.getValue() %> 
		           <%if(c.getValue()==Integer.parseInt(request.getParameter("paytype")==null?"-1":request.getParameter("paytype"))){ %>selected="selected" <%} %> ><%=c.getText()%></option>
		         <%} %>
			</select>
		<input type="button" id="find" onclick="" value="查询" class="input_button2" />
			&nbsp;&nbsp;<input type="reset"  value="清空" onclick="clearSelect();" class="input_button2" />
			<select name ="exportmould" id ="exportmould">
	          <option value ="0">默认导出模板</option>
	          <%for(Exportmould e:exportmouldlist){%>
	           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
	          <%} %>
		</select>
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
	<form action="<%=request.getContextPath()%>/advancedquery/exportExcle" method="post" id="searchForm2">
		<input type="hidden" name="begin" id="begin" value="0"/>
		<input type="hidden" name="exportmould2" id="exportmould2" />
		<input type="hidden" name="exportmould2" value="<%=request.getAttribute("sum")%>"  />
		<input type="hidden" name="datetype"  value="<%=request.getAttribute("datetype")%>"  />
	    <input type="hidden" name="begindate"  value="<%=request.getAttribute("begindate")%>"  />
	    <input type="hidden" name="enddate"  value="<%=request.getAttribute("enddate")%>"  />
	    <input type="hidden" name="customerid"  value="<%=request.getAttribute("customerid")%>"  />
	    <input type="hidden" name="commonnumber"  value="<%=request.getAttribute("commonnumber")%>"  />
	    <input type="hidden" name="orderbyName"  value="<%=request.getAttribute("orderbyName")%>"  />
	    <input type="hidden" name="customerwarehouseid"  value="<%=request.getAttribute("customerwarehouseid")%>"  />
	    <input type="hidden" name="startbranchid"  value="<%=request.getAttribute("startbranchid")%>"  />
	    <input type="hidden" name="nextbranchid"  value="<%=request.getAttribute("nextbranchid")%>"  />
	    <input type="hidden" name="cwbordertypeid"  value="<%=request.getAttribute("cwbordertypeid")%>"  />
	    <input type="hidden" name="deliverycwbs"  value="<%=request.getAttribute("deliverycwbs")%>"  />
	    <input type="hidden" name="currentBranchid"  value="<%=request.getAttribute("currentBranchid")%>"  />
	    <input type="hidden" name="dispatchbranchid"  value="<%=request.getAttribute("dispatchbranchid")%>"  />
	    <input type="hidden" name="kufangid"  value="<%=request.getAttribute("kufangid")%>"  />
	    <input type="hidden" name="paywayid"  value="<%=request.getAttribute("paywayid")%>"  />
	    <input type="hidden" name="dispatchdeliveryid"  value="<%=request.getAttribute("dispatchdeliveryid")%>"  />
	    <input type="hidden" name="consigneename"  value="<%=request.getAttribute("consigneename")%>"  />
	    <input type="hidden" name="consigneemobile"  value="<%=request.getAttribute("consigneemobile")%>"  />
	    <input type="hidden" name="beginWatht"  value="<%=request.getAttribute("beginWatht")%>"  />
	    <input type="hidden" name="endWatht"  value="<%=request.getAttribute("endWatht")%>"  />
	    <input type="hidden" name="beginsendcarnum" value="<%=request.getAttribute("beginsendcarnum")%>"  />
	    <input type="hidden" name="endsendcarnum" value="<%=request.getAttribute("endsendcarnum")%>"  />
	    <input type="hidden" name="carsize" value="<%=request.getAttribute("carsize")%>"  />
	    <input type="hidden" name="flowordertype" value="<%=request.getAttribute("flowordertype")%>"  />
	    <input type="hidden" name="packagecode" value="<%=request.getAttribute("packagecode")%>"  />
	    <div style="display: none;">
			<select name =deliverystates id ="deliverystates" multiple="multiple" style="width: 320px;">
               	<%for(DeliveryStateEnum c : DeliveryStateEnum.values()){ %>
		          <option value =<%=c.getValue() %> 
		                   <%if(!orderResultTypelist.isEmpty()) 
						            {for(int i=0;i<orderResultTypelist.size();i++){
						            	if(c.getValue()== new Long(orderResultTypelist.get(i).toString())){
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
	<div style="height:120px"></div>
	<div style="overflow-x:scroll; width:100% " id="scroll">
	<table width="5000" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >订单号</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >批次</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('emaildate');" >发货时间</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('carrealweight');" >发货重量（kg）</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('carsize');" >货物尺寸</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('sendcarnum');" >发货件数</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbordertypeid');" >订单类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneeaddress');" >地址</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneename');" >收件人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneemobile');" >收件人手机号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneephone');" >收件人电话</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneepostcode');" >收货邮编</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('consigneename');" >签收人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >签收时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerid');" >供货商</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('customerwarehouseid');" >发货仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >入库仓库</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('currentbranchid');" >当前站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('startbranchid');" >上一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('nextbranchid');" >下一站</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('deliverybranchid');" >配送站点</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('exceldeliver');" >小件员</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >称重重量（kg）</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >货品备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('receivablefee');" >应收金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('caramount');" >保价金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('paybackfee');" >应退金额</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >支付方式</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark1');" >备注1</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark2');" >备注2</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark3');" >备注3</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark4');" >备注4</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('remark5');" >备注5</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >当前订单状态</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >退货备注</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('auditstate');" >退货是否审核</td> -->
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('auditor');" >审核人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('audittime');" >审核时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflag');" >是否标记</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflagmen');" >标记人</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('marksflagtime');" >标记时间</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >入库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >出库时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >到站时间</td>
			    <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >小件员领货时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >配送成功时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >反馈时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >归班时间</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >最后更新时间</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('backreasonid');" >退货原因id</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >退货原因</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('leavedreasonid');" >滞留原因id</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >滞留原因</td>
				<!-- <td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('expt_code');" >供货商异常原因编号</td> -->
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >供货商异常原因</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >配送结果</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >配送结果备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >入库备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >POS备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >支票号备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >反馈结果备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwb');" >供货商拒收反库备注</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cartype');" >货物类型</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" onclick="orderForm('cwbdelivertypeid');" >配送类型</td>
		</tr>
		<%if(orderlist != null && orderlist.size()>0){  %>
		<% for(CwbOrder  c : orderlist){ %>
				<tr bgcolor="#FF3300">
					<td  align="center" valign="middle"><%=c.getCwb() %></td>
					<%-- <td  align="center" valign="middle"><%=c.getEmaildate()%></td> --%>
					<td  align="center" valign="middle"><%=c.getEmaildate() %></td>
					<td  align="center" valign="middle"><%=c.getCarrealweight() %></td>
					<td  align="center" valign="middle"><%=c.getCarsize() %></td>
					<td  align="center" valign="middle"><%=c.getSendcarnum() %></td>
					<td  align="center" valign="middle"><%=c.getOrderType() %></td>
					<td  align="left" valign="middle"><%=c.getConsigneeaddress() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneename() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneemobile() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneephone() %></td>
					<td  align="center" valign="middle"><%=c.getConsigneepostcode() %></td>
					<td  align="center" valign="middle"><%=c.getSigninman() %></td>
					<td  align="center" valign="middle"><%=c.getSignintime() %></td>
					<td  align="center" valign="middle"><%=c.getCustomername()  %></td>
					<td  align="center" valign="middle"><%=c.getCustomerwarehousename()%></td>
					<td  align="center" valign="middle"><%=c.getInhouse() %></td>
					<td  align="center" valign="middle"><%=c.getCurrentbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getStartbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getNextbranchname() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverybranch() %></td>
					<td  align="center" valign="middle"><%=c.getDelivername() %></td>
					<td  align="center" valign="middle"><%=c.getRealweight() %></td>
					<td  align="center" valign="middle"><%=c.getGoodsremark()%></td>
					<td  align="right" valign="middle"><%=c.getReceivablefee() %></td>
					<td  align="right" valign="middle"><%=c.getCaramount()%></td>
					<td  align="right" valign="middle"><%=c.getPaybackfee() %></td>
					<td  align="center" valign="middle"><%=c.getPaytype()%></td>
					<td  align="center" valign="middle"><%=c.getRemark1() %></td>
					<td  align="center" valign="middle"><%=c.getRemark2() %></td>
					<td  align="center" valign="middle"><%=c.getRemark3() %></td>
					<td  align="center" valign="middle"><%=c.getRemark4() %></td>
					<td  align="center" valign="middle"><%=c.getRemark5() %></td>
					<td  align="center" valign="middle"><%=c.getFlowordertypeMethod() %></td>
					<td  align="center" valign="middle"><%=c.getReturngoodsremark() %></td>
					<%-- <td  align="center" valign="middle"><%=c.getAuditstate()==1?"是":"否" %></td> --%>
					<%-- <td  align="center" valign="middle"><%=c.getAuditor() %></td>
					<td  align="center" valign="middle"><%=c.getAudittime() %></td>
					<td  align="center" valign="middle"><%=c.getMarksflag()==1?"是":"否" %></td>
					<td  align="center" valign="middle"><%=c.getMarksflagmen()  %></td>
					<td  align="center" valign="middle"><%=c.getMarksflagtime()  %></td> --%>
					<td  align="center" valign="middle"><%=c.getInstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getOutstoreroomtime() %></td>
					<td  align="center" valign="middle"><%=c.getInSitetime() %></td>
					<td  align="center" valign="middle"><%=c.getPickGoodstime()  %></td>
					<td  align="center" valign="middle"><%=c.getSendSuccesstime() %></td>
					<td  align="center" valign="middle"><%=c.getGobacktime() %></td>
					<td  align="center" valign="middle"><%=c.getGoclasstime()%></td>
					<td  align="center" valign="middle"><%=c.getNowtime() %></td>
					<%-- <td  align="center" valign="middle"><%=c.getBackreasonid() %></td> --%>
					<td  align="center" valign="middle"><%=c.getBackreason() %></td>
					<%-- <td  align="center" valign="middle"><%=c.getLeavedreasonid() %></td> --%>
					<td  align="center" valign="middle"><%=c.getLeavedreasonStr() %></td>
					<%-- <td  align="center" valign="middle"><%=c.getExpt_code() %></td> --%>
					<td  align="center" valign="middle"><%=c.getExpt_msg() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverStateText() %></td>
					<td  align="center" valign="middle"><%=c.getPodremarkStr() %></td>
					<td  align="center" valign="middle"><%=c.getInwarhouseremark() %></td>
					<td  align="center" valign="middle"><%=c.getPosremark() %></td>
					<td  align="center" valign="middle"><%=c.getCheckremark() %></td>
					<td  align="center" valign="middle"><%=c.getDeliverstateremark() %></td>
					<td  align="center" valign="middle"><%=c.getCustomerbrackhouseremark() %></td>
					<td  align="center" valign="middle"><%=c.getCartype() %></td>
					<td  align="center" valign="middle"><%=c.getCwbdelivertypeStr() %></td>
				 </tr>
		 <%} }%>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	<%if(request.getAttribute("count")!= null){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" >
	<tr>
	<td>订单总计：<font color="red"><%=count %></font>&nbsp;单  </td>
	</tr>
	<tr>
	<td>代收金额总计：<font color="red"><%=request.getAttribute("sum")==null?"0.00":request.getAttribute("sum") %></font>&nbsp;元 </td>
	</tr>
	<tr>
	<td>代退金额总计：<font color="red"><%=request.getAttribute("paybackfeesum")==null?"0.00":request.getAttribute("paybackfeesum") %></font>&nbsp;元 </td>
	</tr>
	</table>
	<%} %>
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
$("#datetype").val(<%=request.getParameter("datetype")==null?"0":request.getParameter("datetype")%>);
</script>

<script type="text/javascript">

function exportField(page,j){
	
		if($("#isshow").val()=="1"){
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

