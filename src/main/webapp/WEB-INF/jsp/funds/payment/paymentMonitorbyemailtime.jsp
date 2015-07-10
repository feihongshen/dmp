<%@page import="cn.explink.enumutil.FinanceAuditTypeEnum"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.util.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="net.sf.json.JSONObject"%>
 <%
 
 List<CwbOrder> daohuojsonList = (List<CwbOrder>)request.getAttribute("daohuojsonList");
  Map<Long ,String> cumstrMap  = (Map<Long ,String>)request.getAttribute("cumstrMap");
 List<Branch> branchList =(List<Branch>) request.getAttribute("branchList");
 List<CustomWareHouse>  cwhList =(List<CustomWareHouse>) request.getAttribute("customWareHose");
 List<Customer> cumstrListAll =(List<Customer>) request.getAttribute("cumstrListAll"); 
 BigDecimal countfee =  request.getAttribute("countfee")==null?BigDecimal.ZERO:(BigDecimal) request.getAttribute("countfee");
 int all = request.getAttribute("all")==null?0:(Integer) request.getAttribute("all");
 
 List customeridList =(List) request.getAttribute("customeridStr");
 List emaildateidList =(List) request.getAttribute("emaildateidStr");
 List<EmailDate> emaildateList =request.getAttribute("emaildateList")==null?new ArrayList<EmailDate>():(List<EmailDate>) request.getAttribute("emaildateList");
 Page page_obj = (Page)request.getAttribute("page_obj");
 long totalcount = daohuojsonList.size();
 double totalamount = 0;
 for(CwbOrder c : daohuojsonList){
	 totalamount+=c.getReceivablefee().doubleValue();
 }
 %>
    <html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<title>货款管理</title>
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
	if(!isSelect('customerid')){
		alert("请选择供货商");
		return false;
	}else if(!isSelect('emaildateid')){
		alert("请选择发货批次");
		return false;
	}
	else{
		return true;
	}
}
function isSelect(name){
	var is = false;
	$("input[name='"+name+"']").each(function(i){
		if($(this).parent().attr("class")&&$(this).parent().attr("class").indexOf("checked")!=-1){
			is = true;
		}
	});
	return is;
}
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
	$("<option value='-1'>发货仓库</option>").appendTo("#customerwarehouseid");//添加下拉框的option
}
/* function clickCustomerSelect(){
	var num = 0 ;//选择次数
	var selectCustomer = "";
	$("input[name='emaildateid']").parent().hide();
	$("input[name='customerid']").each(function(i){
		
		if($(this).parent().attr("class")&&$(this).parent().attr("class").indexOf("checked")!=-1){
			num=num+1;
			selectCustomer=$(this).val();
			var customername = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
			$("input[name='emaildateid']").each(function(i){
				if($(this).parent().html().indexOf(customername)!=-1){
					$(this).parent().show();
				}
			});
		}
	 });
	if(num==0){//如果没有选择任何一个供货商则隐藏所有的批次
		$("input[name='emaildateid']").each(function(i){
			$(this).parent().attr("class","");
			$(this).parent().hide();
		});
		emptyCustomerWarehouse();
	}else if(num==1){//当只选择一个供货商的时候，将联动供货商仓库
		updateCustomerwarehouse(selectCustomer);
	}else{//选择一个以上供货商的时候，将隐藏供货商仓库
		emptyCustomerWarehouse();
	}
	var txt = "";//用于存储显示批次文本框的文字
	$("input[name='emaildateid']").each(function(i){
		if($(this).parent()[0].style.display=="none"){
			$(this).parent().attr("class","");
			$(this).attr("checked",false);
		}
		if($(this).parent().attr("class")=="checked"){
			var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
			txt = txt+pici+", ";
		}
	});
	$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择发货批次":txt);
} */
</script>
<script type="text/javascript">

$(document).ready(function() {
	//获取下拉框的值
	$("#btnval").click(function(){
	    if(check()){
	     $("#searchForm").submit();
	    }
	});
});
   
</script>
<script>
$(function() {
	$("#startime").datetimepicker({
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
	$("#emaildateid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择发货批次' });
	$("input[name='customerid']").click(function(){//获得供货商点击后的 被选择供货商的值，用于删选批次，和控制
		//clickCustomerSelect();
		//保存ajax之前的emaildate选择情况
		var emaildateCheckId = $("input[name='emaildateid']");
		var ids= new Array();
		for(var i=0;i<emaildateCheckId.length; i++){
			if(emaildateCheckId[i].checked){
				ids[i]=emaildateCheckId[i].value;
			}
		}
		
		var num = 0 ;//选择次数
		var customerids = "";
		$("input[name='customerid']").each(function(i){
			if($(this).parent().attr("class")&&$(this).parent().attr("class").indexOf("checked")!=-1){
				customerids+=$(this).val()+",";
				num=num+1;
			}
		 });
		if(customerids==""){
			$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val("");
			if(navigator.userAgent.indexOf("MSIE") >= 0){
				$("#fahuopici").next().next().next().html("");
			}else{
				$("#fahuopici").next().next().html("");
			}
			return;
		}
		$.ajax({
			url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",//后台处理程序
			type:"POST",//数据发送方式 
			data:"customerids="+customerids,//参数
			dataType:'json',//接受数据格式
			success:function(json){
				var list = "";
				for(var i=0; i<json.length; i++){
					list += "<label class=\"\"><input type=\"checkbox\" onClick=\"clickEmaildate(this);\" value=\""+json[i].emaildateid+"\" name=\"emaildateid\">"+json[i].emaildatetime+"("+json[i].customername+")</label>";
				}
				if(navigator.userAgent.indexOf("MSIE") >= 0){
					$("#fahuopici").next().next().next().html(list);
				}else{
					$("#fahuopici").next().next().html(list);
				}
				var txt = "";//用于存储显示批次文本框的文字
				for(var i=0;i<ids.length; i++){
					$("input[name='emaildateid'][value='"+ids[i]+"']").each(function(i){
						$(this).attr("checked","checked");
						var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
						txt = txt+pici+", ";
					});
					$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择发货批次":txt.substring(0,txt.length-2));
				}
				
			}		
		});
		
		if(num==1){//当只选择一个供货商的时候，将联动供货商仓库
			updateCustomerwarehouse(customerids.substring(0,customerids.length-1));
		}else{//选择一个以上供货商的时候，将隐藏供货商仓库
			emptyCustomerWarehouse();
		}
		
	});
	//默认供货商仓库查询后进入页面显示
	var customerwarehouseid_old = <%=request.getParameter("customerwarehouseid")==null?"0":request.getParameter("customerwarehouseid") %>;//0表示没有选择这样一个仓库
	if(customerwarehouseid_old>0){
		var num = 0 ;//选择次数
		var customerids = "";
		$("input[name='customerid']").each(function(i){
			if($(this).parent().attr("class")&&$(this).parent().attr("class").indexOf("checked")!=-1){
				customerids+=$(this).val()+",";
				num=num+1;
			}
		 });
		if(customerids==""){
			$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val("");
			if(navigator.userAgent.indexOf("MSIE") >= 0){
				$("#fahuopici").next().next().next().html("");
			}else{
				$("#fahuopici").next().next().html("");
			}
			return;
		}
		updateCustomerwarehouse(customerids.substring(0,customerids.length-1));
	}
	
});
function clickEmaildate(t){
	$(t).parent().attr("class","checked");
	var txt = "";//用于存储显示批次文本框的文字
	$("input[name='emaildateid']").each(function(i){
		if($(this).parent().attr("class")=="checked"){
			var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
			txt = txt+pici+", ";
		}
	});
	$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择发货批次":txt.substring(0,txt.length-2));
}
</script>   

</head>

<body style="background:#f5f5f5" >

<div class="right_box">
	<div class="inputselect_box">
	<span>
	</span>
	<form  id="searchForm" action ="<%=request.getContextPath()%>/funds/paymentMonitorbyemailtime/1"  method = "post">
		
		供货商：<select id="customerid" name="customerid"  multiple="multiple" style="width: 200px;">
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
		 <lable id="fahuopici">发货批次：</lable><select id="emaildateid" name="emaildateid" multiple="multiple" style="width: 400px;">
					<%for(EmailDate ed : emaildateList){ %>
				 	<option value="<%=ed.getEmaildateid() %>" <%
				 		if(!emaildateidList.isEmpty()) //当提交了emaildateid这个参数再回到这个页面时，应该赋予默认值
				            {for(int i=0;i<emaildateidList.size();i++){
				            	if(ed.getEmaildateid()== new Long(emaildateidList.get(i).toString())){
				            		%>selected="selected"<%
				            	 break;
				            	}
				            }
					     }%>
				 	><%=ed.getEmaildatetime() %><%
					 	for(Customer c:cumstrListAll){//产生供货商名称，同时这个名称用于筛选下拉列表与供货商之间的联动
					 		if(ed.getCustomerid()==c.getCustomerid()){
					 			out.print("("+c.getCustomername()+")");
					 		}
				 		}%></option>
				 	<%} %>
				 </select>
                 供货商发货仓库:<select id="customerwarehouseid" name="customerwarehouseid">
                 <option value='-1'>发货仓库</option></select>
      <br/> 入库时间：<input type ="text" name ="rukuStartTime" id="startime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("rukuStartTime")) %>">
                                到     <input type ="text" name ="rukuEndTime" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("rukuEndTime")) %>">
                <input type="reset" value="清空" onclick="$('#startime').val('');$('#endtime').val('');" class="input_button2" />
                订单类型：<select name="cwbOrderType">
                	<option value="<%=CwbOrderTypeIdEnum.Peisong.getValue()  %>" <%="1".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
                	<option value="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()  %>" <%="2".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
                	<option value="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()  %>" <%="3".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
                </select>
          <%--   结算状态：<select name="auditState">
             		<option value="0" <%="0".equals(request.getParameter("auditState"))?"selected":"" %>>未结算</option>
             		<option value="1" <%="1".equals(request.getParameter("auditState"))?"selected":"" %>>已结算</option>
             	</select> --%> 
                <%--   导出状态：<select name ="exportType" id="exportType">
	               <option value="-1">不限 </option>
	               <option value ="0" <%if(0 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>未导出</option>
	               <option value ="1" <%if(1 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出未审核</option>
	               <option value ="2" <%if(2 == new Long(request.getParameter("exportType")==null?"0":request.getParameter("exportType"))) {%>selected="selected"<%} %>>导出已审核</option>
                </select>  --%>
                 <input type="hidden" id="exportType" name="exportType" value="-1" /> 
		<input type="button" id="btnval" value="查询" class="input_button2" />
		<%if(all/Page.EXCEL_PAGE_NUMBER+(all%Page.EXCEL_PAGE_NUMBER>0?1:0)==1){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval0" value="导出1-<%=all %>" class="input_button1" onclick="exportField('0','0');"/>
			<%}else{for(int j=0;j<all/Page.EXCEL_PAGE_NUMBER+(all%Page.EXCEL_PAGE_NUMBER>0?1:0);j++){ %>
				<%if(j==0){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出1-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('0','<%=j%>');"/>
				<%}else if(j!=0&&j!=(all/Page.EXCEL_PAGE_NUMBER+(all%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=((j+1)*Page.EXCEL_PAGE_NUMBER)/10000.0 %>万" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%}else if(j==(all/Page.EXCEL_PAGE_NUMBER+(all%Page.EXCEL_PAGE_NUMBER>0?1:0)-1)){ %>
				&nbsp;&nbsp;<input type ="button" id="btnval<%=j %>" value="导出<%=j*Page.EXCEL_PAGE_NUMBER+1 %>-<%=all %>" class="input_button1" onclick="exportField('<%=j*Page.EXCEL_PAGE_NUMBER %>','<%=j%>');"/>
				<%} %>
			<%}} %>
	</form>
	 <input type="hidden" id="controlStr" name="controlStrCustomer" value=""/>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>
    <% if(daohuojsonList!=null && daohuojsonList.size()>0){ %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">供货商仓库</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">应收金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">配送站点</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">结算状态</td>
		</tr>
		<%for (CwbOrder co :daohuojsonList) {%>
		<tr>
			<td width="10%" align="center" valign="middle" ><%=co.getCwb() %></td>
			<td width="5%" align="center" valign="middle" >
				<%for(CustomWareHouse cwh : cwhList){if(Long.parseLong(co.getCustomerwarehouseid())==cwh.getWarehouseid()){ %>
				<%=cwh.getCustomerwarehouse() %>
				<%}} %>
			</td>
			<td width="10%" align="center" valign="middle"><%=cumstrMap.get(co.getCustomerid()) %></td>              
			<td width="8%"  align="center" valign="middle"><%=co.getEmaildate() %></td>              
			<td width="13%"  align="center" valign="middle"><%=co.getReceivablefee().subtract(co.getPaybackfee()) %></td>
			<td width="10%"align="center" valign="middle" >
				<%for(CwbOrderTypeIdEnum cy : CwbOrderTypeIdEnum.values()){if(co.getCwbordertypeid()==cy.getValue()){ %>
				<%=cy.getText() %>
				<%}} %>
			</td>             
			<td width="8%"  align="center" valign="middle">
				<%for(Branch b : branchList){if(co.getDeliverybranchid()==b.getBranchid()){ %>
				<%=b.getBranchname() %>
				<%}} %>
			</td>
		<%-- 	<td width="8%"  align="center" valign="middle">
			<%for(PaytypeEnum pe : PaytypeEnum.values()){if(Long.parseLong(co.getNewpaywayid())==pe.getValue()){ %>
				<%=pe.getText() %>
				<%}} %>
			</td> --%>
			<td width="8%"  align="center" valign="middle">
			 未结算 <%-- <%="0".equals(request.getParameter("auditState"))?"未结算":"已结算" %>  --%>
			</td>
			
		</tr>
		<%
		} %>
		
		<tr>
		    <td>合计：</td>
		    <td></td>
			<td colspan="2"><strong><%=all%>[票]</strong></td>              
			<td align="center"><strong><%= countfee%>[元]</strong></td>
			<td colspan="3"></td>
			
	   </tr>
	  <%--  <tr>
		    <td colspan="8">
		    <%if("1".equals(request.getParameter("cwbOrderType"))&&"0".equals(request.getParameter("auditState"))&&customeridList.size()==1){ %>
		    <form action="<%=request.getContextPath()%>/funds/financeauditconfirm" method="POST">
		    	<input type="hidden" name="cwbs" value="<%=request.getAttribute("cwbs")==null?"":request.getAttribute("cwbs").toString() %>"/>
		    	<input type="hidden" name="auditCustomerid" value="<%=customeridList.get(0) %>"/>
		    	<input type="hidden" name="auditCustomerwarehouseid" value="<%=request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid")  %>"/>
		    	<input type="hidden" name="auditCwbOrderType" value="<%=request.getParameter("cwbOrderType")==null?"1":request.getParameter("cwbOrderType")  %>"/>
		    	<input type="hidden" name="type" value="<%=FinanceAuditTypeEnum.AnFaHuoShiJian.getValue()  %>"/>
		    	<input name="button" type="submit" class="input_button1" id="button" value="审核结算">
		    </form>
		   
		    <%}else{ %>
		    <font color="red">只有某一个供货商的未审核的配送订单才能在此结算</font>
		    <%} %>
			</td>
	   </tr> --%> 
	   
	</table>
	
	
	</div>
	<%} else{%>
	   <center>
	        <font color ="red">当前查询暂无数据！</font>
	   </center>
	<%} %>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="jg_35"></div>
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
	<form id="excelData" action="<%=request.getContextPath()%>/funds/newsearchdetail_excel">
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
		 发货批次：<select id="emaildateid1" name="emaildateid1" multiple="multiple" >
					<%for(EmailDate ed : emaildateList){ %>
				 	<option value="<%=ed.getEmaildateid() %>" <%
				 		if(!emaildateidList.isEmpty()) //当提交了emaildateid这个参数再回到这个页面时，应该赋予默认值
				            {for(int i=0;i<emaildateidList.size();i++){
				            	if(ed.getEmaildateid()== new Long(emaildateidList.get(i).toString())){
				            		%>selected="selected"<%
				            	 break;
				            	}
				            }
					     }%>
				 	><%=ed.getEmaildatetime() %><%
					 	for(Customer c:cumstrListAll){//产生供货商名称，同时这个名称用于筛选下拉列表与供货商之间的联动
					 		if(ed.getCustomerid()==c.getCustomerid()){
					 			out.print("("+c.getCustomername()+")");
					 		}
				 		}%></option>
				 	<%} %>
				 </select>
                 供货商发货仓库:<input id="customerwarehouseid1" name="customerwarehouseid1" value="<%=request.getParameter("customerwarehouseid")==null?"-1":request.getParameter("customerwarehouseid") %>">
      <br/> 入库时间：<input type ="text" name ="rukuStartTime1" id="startime1"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("rukuStartTime")) %>">
                                到     <input type ="text" name ="rukuEndTime1" id="endtime1"  value ="<%=StringUtil.nullConvertToEmptyString(request.getAttribute("rukuEndTime")) %>">
                订单类型：<select name="cwbOrderType1">
                	<option value="<%=CwbOrderTypeIdEnum.Peisong.getValue()  %>" <%="1".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Peisong.getText()%></option>
                	<option value="<%=CwbOrderTypeIdEnum.Shangmentui.getValue()  %>" <%="2".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Shangmentui.getText()%></option>
                	<option value="<%=CwbOrderTypeIdEnum.Shangmenhuan.getValue()  %>" <%="3".equals(request.getParameter("cwbOrderType"))?"selected":"" %>><%=CwbOrderTypeIdEnum.Shangmenhuan.getText()%></option>
                </select>
             审核状态：<select name="auditState1">
             		<option value="0" <%="0".equals(request.getParameter("auditState"))?"selected":"" %>>未审核</option>
             		<option value="1" <%="1".equals(request.getParameter("auditState"))?"selected":"" %>>已审核</option>
             	</select>
        </div>
	</form>
	
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#datetype").val(<%=request.getParameter("datetype")==null?"1":request.getParameter("datetype")%>);
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