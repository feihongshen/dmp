<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.FundsEnum"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.FinanceAuditTypeEnum"%>
 <%
 
 List<JSONObject> daohuojsonList = (List<JSONObject>)request.getAttribute("daohuojsonList");
  Map<Long ,String> cumstrMap  = (Map<Long ,String>)request.getAttribute("cumstrMap");
  Map cwbsDateMap  = (Map)request.getAttribute("cwbsDateMap");
 List<Branch> branchnameList =(List<Branch>) request.getAttribute("branchnameList");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 List<Branch> branchList =(List<Branch>) request.getAttribute("branchList");
 double countfee =  request.getAttribute("countfee")==null?0:(Double) request.getAttribute("countfee");
JSONObject daohuolist = (JSONObject) request.getAttribute("daohuolist");
List customeridList =(List) request.getAttribute("customeridStr");


int count = daohuolist.isEmpty()?0:daohuolist.getInt("cwbcount");
 Page page_obj = (Page)request.getAttribute("page_obj");
 long totalcount = daohuojsonList.size();
 double totalamount = 0;
 for(int i=0;i<daohuojsonList.size();i++){
	 totalamount += daohuojsonList.get(i).getDouble("receivablefee");
 }
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>退货款结算</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css"> --%>
<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script> --%>
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
function check(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}if($("#customerid").val()==-1){
		alert("请选择供货商");
		return false;
	}
	else{
		return true;
	}
}
</script>
<script type="text/javascript">

      $(document).ready(function() {
   //获取下拉框的值
   $("#btnval").click(function(){
       if(check()){
    	   $("#isshow").val(1);
        $("#searchForm").submit();
       }
   });
      });
   
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
	$("#customerid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择供货商' });
	$("input[name='customerid']").click(function(){//获得供货商点击后的 被选择供货商的值，用于删选批次，和控制
		clickCustomerSelect();
	});
});
var isInit = 0;
function updateCustomerwarehouse(customerid){
	if(customerid==0){
		return;
	}
	$.ajax({
		url:"<%=request.getContextPath()%>/advancedquery/updateCustomerwarehouse",//后台处理程序
		type:"POST",//数据发送方式 
		data:"customerid="+customerid,//参数
		dataType:'json',//接受数据格式
		success:function(json){
			emptyCustomerWarehouse();
			for(var j = 0 ; j < json.length ; j++){
				$("<option value='"+json[j].warehouseid+"'>"+json[j].customerwarehouse+"</option>").appendTo("#customerwarehouseid");
				}
			if(isInit==0){
				isInit=1;
				$('#customerwarehouseid').val(<%=request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid") %>);
			}
		}		
	});
}
function emptyCustomerWarehouse(){
	$("#customerwarehouseid").empty();//清空下拉框//$("#select").html('');
	$("<option value='-1'>请选择</option>").appendTo("#customerwarehouseid");//添加下拉框的option
}
function clickCustomerSelect(){
	var num = 0 ;//选择次数
	var selectCustomer = "";
	$("input[name='customerid']").each(function(i){
		if($(this).parent().attr("class")&&$(this).parent().attr("class").indexOf("checked")!=-1){
			num=num+1;
			selectCustomer=$(this).val();
		}
	 });
	if(num==0){//如果没有选择任何一个供货商则隐藏所有的批次
		emptyCustomerWarehouse();
	}else if(num==1){//当只选择一个供货商的时候，将联动供货商仓库
		updateCustomerwarehouse(selectCustomer);
	}else{//选择一个以上供货商的时候，将隐藏供货商仓库
		emptyCustomerWarehouse();
	}
}
</script>   

</head>

<body style="background:#eef9ff" onload="clickCustomerSelect();">

<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form  id="searchForm" action ="<%=request.getContextPath()%>/funds/paymentBack/1" method = "post">
		供货商：<select id="customerid" name="customerid" multiple="multiple" style="width: 200px;">
		          <%if(cumstrListAll != null && cumstrListAll.size()>0){ %>
		           <%for( Customer c:cumstrListAll){ %>
		            <option value="<%= c.getCustomerid()%>" 
		            <%if(!customeridList.isEmpty()) 
			            {for(int i=0;i<customeridList.size();i++){
			            	if(c.getCustomerid()== new Long(customeridList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>><%=c.getCustomername()%></option>
		           <%} }%>
        	   </select>
        	   [<a href="javascript:multiSelectAll('customerid',1,'请选择');">全选</a>]
			   [<a href="javascript:multiSelectAll('customerid',0,'请选择');">取消全选</a>]
        	    供货商发货仓库:<select id="customerwarehouseid" name="customerwarehouseid">
                 <option value='-1'>请选择</option></select>
                供货商确认时间：<input type ="text" name ="emailStartTime" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">
                                到     <input type ="text" name ="eamilEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
		
				出入账：
			<select name ="isout" id ="isout">
		           <option value ="0" <%="0".equals(request.getParameter("isout"))?"selected":"" %>>出账</option>
		           <option value ="1" <%="1".equals(request.getParameter("isout"))?"selected":"" %>>入账</option>
		    </select>
		          <%-- 结算状态：<select name="auditState" >
             		<option value="0" <%="0".equals(request.getParameter("auditState"))?"selected":"" %>>未结算</option>
             		<option value="1" <%="1".equals(request.getParameter("auditState"))?"selected":"" %>>已结算</option>
             	</select>  --%>
                <%--   导出状态：<select name ="exportType" id="exportType">
	               <option value="-1">不限 </option>
	               <option value ="0" <%if(0 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>未导出</option>
	               <option value ="1" <%if(1 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出未审核</option>
	               <option value ="2" <%if(2 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出已审核</option>
                </select>  --%>
                 <input type="hidden" id="exportType" name="exportType" value="-1" /> 
                 <input type="hidden" id="isshow" name="isshow" value="1" />  
		<input type="button" id="btnval" value="查询" class="input_button2" />
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
	</form>
	 <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_30"></div>
    <% if(daohuojsonList!=null && daohuojsonList.size()>0){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商确认时间</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">货物金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff"><%="0".equals(request.getParameter("isout"))?"出账":"入账" %>金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">配送结果</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">结算状态</td>
		</tr>
		<%
		for (int j=0;j<daohuojsonList.size();j++) {
		%>
		<tr>
			<td width="10%" align="center" valign="middle" ><%=daohuojsonList.get(j).getString("cwb") %></td>
			<td width="10%" align="center" valign="middle"><%=cumstrMap.get(daohuojsonList.get(j).getLong("customerid")) %></td>              
			<td width="10%" align="center" valign="middle">
			<%for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ %>
			     <%if(c.getValue()==daohuojsonList.get(j).getInt("cwbordertypeid")){  %>
			     <%=c.getText() %>
			     <% 
			     break;
			     } %>
			<%} %>
			</td>              
			<td width="10%"  align="center" valign="middle"><%=daohuojsonList.get(j).getString("emaildate") %></td>              
			<td width="10%"  align="center" valign="middle"><%=cwbsDateMap.get(daohuojsonList.get(j).getString("cwb")) %></td>              
			<td width="10%"  align="center" valign="middle"><%=daohuojsonList.get(j).getDouble("caramount") %></td>
			<td width="10%"  align="center" valign="middle"><%="1".equals(request.getParameter("isout"))?daohuojsonList.get(j).getDouble("returnedfee"):daohuojsonList.get(j).getDouble("receivedfee") %></td>
			<td width="10%"align="center" valign="middle" >
				<%for(Branch b : branchList){if(daohuojsonList.get(j).getLong("deliverybranchid")==b.getBranchid()){ %>
				<%=b.getBranchname() %>
				<%}} %>
			</td>  
			<td width="10%"  align="center" valign="middle">
			<%for(DeliveryStateEnum c : DeliveryStateEnum.values()){ %>
			     <%if(c.getValue()==daohuojsonList.get(j).getInt("deliverystate")){  %>
			     <%=c.getText() %>
			     <% 
			     break;
			     } %>
			<%} %>
			</td>  
			<td width="10%"  align="center" valign="middle">
			 未结算 <%-- <%="0".equals(request.getParameter("auditState"))?"未结算":"已结算" %>  --%>
			</td>  
		</tr>
		<%
		} %>
		
		<tr>
		    <td>合计：</td>
			<td colspan="5"><strong><%=count%>[票]</strong></td>              
			<td align="center"><strong>
			<%if(daohuolist.has("receivablefees")&&daohuolist.has("paybackfees")){ %>
			<%if("1".equals(request.getParameter("isout"))){ %>
				<%= daohuolist.getString("receivablefees")%>
			<%}else{ %>
				<%= daohuolist.getString("paybackfees")%>
			<%}}else{ %>
			0.00
			<%} %>
			
			[元]
			</strong></td>
			<td colspan="3"></td>
			
	   </tr>
	  <%--  <tr>
		    <td colspan="13">
		    <%if(customeridList.size()==1 &&"0".equals(request.getParameter("auditState"))){ %>
		    <form action="<%=request.getContextPath()%>/funds/financeauditBackconfirm" method="POST">
		    	<input type="hidden" name="cwbs" value="<%=request.getAttribute("cwbs")==null?"":request.getAttribute("cwbs").toString() %>"/>
		    	<input type="hidden" name="auditCustomerid" value="<%=customeridList.get(0) %>"/>
		    	<input type="hidden" name="auditCustomerwarehouseid" value="<%=request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid")  %>"/>
		    	<input type="hidden" name="isout" value="<%=request.getParameter("isout")==null?0:request.getParameter("isout") %>"/>
		    	<input name="button" type="submit" class="input_button1" id="button" value="审核结算">
		    </form>
		   
		    <%}else{ %>
		    <font color="red">只有某一个供货商的未审核的订单才能在此结算</font>
		    <%} %>
			</td>
	   </tr> --%> 
	</table>
	<%if(page_obj.getMaxpage()>1){ %>
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
				<%} %>
	<div class="jg_10"></div><div class="jg_10"></div>
	
	</div>
	<!-- <div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					<input type ="button"  value="导出excel" class="input_button2" onclick="excelExp();"/>&nbsp;&nbsp;<input type ="button"  value="导出excel并审核" class="input_button1" onclick="excelExpAndCheck();"/>
				</td>
			</tr>
		</table>
	</div> -->
	<%} else{%>
	   <center>
	        <font color ="red">当前查询暂无数据！</font>
	   </center>
	<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>
	
	<form id="excelData" action="<%=request.getContextPath()%>/funds/deliverystatesearchdetailback_excel">
		<input type="hidden" name="begin" id="begin" value=""/>
		<div style="display: none;">
		<select name ="customerid1" id ="customerid1" multiple="multiple" >
			<%if(cumstrListAll != null && cumstrListAll.size()>0){ %>
	          <%for(Customer c : cumstrListAll){ %>
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
	          <%}} %>
        </select>
           供货商发货仓库:<input id="customerwarehouseid1" name="customerwarehouseid1" value="<%=request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid") %>">
             反馈时间：<input type ="text" name ="emailStartTime1" id="strtime1"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("emailStartTime")) %>">
                                到     <input type ="text" name ="eamilEndTime1" id="endtime1"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("eamilEndTime")) %>">
		
				出入账：
			<select name ="isout1" id ="isout1">
		           <option value ="1" <%="1".equals(request.getParameter("isout"))?"selected":"" %>>出账</option>
		           <option value ="0" <%="0".equals(request.getParameter("isout"))?"selected":"" %>>入账</option>
		    </select>
		          结算状态：<select name="auditState1">
             		<option value="0" <%="0".equals(request.getParameter("auditState"))?"selected":"" %>>未结算</option>
             		<option value="1" <%="1".equals(request.getParameter("auditState"))?"selected":"" %>>已结算</option>
             	</select>
        </div>
	</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#datetype").val(<%=request.getParameter("datetype")==null?"1":request.getParameter("datetype")%>);
$("#deliverystate").val(<%=request.getParameter("deliverystate")==null?"0":request.getParameter("deliverystate")%>);
$("#isaudit").val(<%=request.getParameter("isaudit")==null?"-1":request.getParameter("isaudit") %>);

function exportField(page,j){
	if(<%=daohuojsonList!=null&&daohuojsonList.size()!=0%>){
		$("#btnval"+j).attr("disabled","disabled");
		$("#btnval"+j).val("请稍后……");
		$("#begin").val(page);
		$("#excelData").submit();
		return true;
	}else{
		alert("没有做查询操作，不能导出！");
		return false;
	}
}


</script>
</body>
</html>






