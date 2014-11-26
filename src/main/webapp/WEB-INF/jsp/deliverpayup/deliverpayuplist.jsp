<%@page import="java.math.BigDecimal"%>
<%@page import="groovy.json.JsonOutput"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="cn.explink.controller.DeliveryStateView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.controller.DeliveryStateDTO"%>
<%@page import=" cn.explink.domain.DeliveryState"%>
<%@page import=" cn.explink.domain.Customer"%>
<%@page import=" cn.explink.domain.Reason"%>
<%@page import=" cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import=" cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import=" cn.explink.enumutil.DeliveryTongjiEnum"%>
<%@page import=" cn.explink.domain.*"%>
<%@page import=" net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.DeliverPayupArrearageapprovedEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<JSONObject> viewList = request.getAttribute("viewList")==null?null:(List<JSONObject>)request.getAttribute("viewList");
List<Branch> bList = request.getAttribute("bList")==null?null:(List<Branch>)request.getAttribute("bList");

String starttime=request.getParameter("begindate")==null?"":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?"":request.getParameter("enddate");
List<User> delivers = (List<User>)request.getAttribute("delivers");
long selectedDeliver = request.getAttribute("deliver") == null?0:((Long)request.getAttribute("deliver")).longValue();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小件员交款财务</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script>
$(function(){

	
	$("table#gd_table tr:odd").css("backgroundColor","#f9fcfd");
	$("table#gd_table tr:odd").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#f9fcfd");	
	});
	$("table#gd_table tr:even").hover(function(){
		$(this).css("backgroundColor","#fff9ed");									  
	},function(){
		$(this).css("backgroundColor","#fff");	
	});//表单颜色交替和鼠标滑过高亮
	
	
	$("table#gd_table2 tr").click(function(){
			$(this).css("backgroundColor","#FFA500");
			$(this).siblings().css("backgroundColor","#ffffff");
		});
	
	$(".index_dh li").mouseover(function(){
		//$(this).show(0)
		var $child = $(this).children(0);
		$child.show();
	});
	$(".index_dh li").mouseout(function(){
 
		$(".menu_box").hide();
	});
	
});

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
	if($("#branchid").val()==0&&$("#branchname").val()==""){
		alert("请选择审核站点");
		return false;
	}
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
}

$(function(){
	$("#find").click(function(){
		if(check()){
			$("#searchForm").submit();
		}
	});
});
$("document").ready(function(){  
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkbox']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkbox']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#updateTongguo").click(function(){//
		$("#controlStr").val("");
		$("#mackStr").val("");
		var str=""; 
		var mackStr = "";
		$("input[name='checkbox']:checkbox:checked").each(function(){  
			var id = $(this).val();
			str+=$(this).val()+";";
			
			mackStr += id +"T:T"+$("#aremark"+id).val()+"P:P"; 
		}); 
		$("#controlStr").val(str);
		$("#mackStr").val(mackStr);
		$("#deliverpaystate").val(1);
		$("#selectedDeliver").val($("#deliver").val());
		
		if($("#mackStr").val()==""){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		if(confirm("确定将勾选的审核为已通过？")){
	    	$("#updateForm").submit();
		}
	});
	$("#updateButongguo").click(function(){//
		$("#controlStr").val("");
		$("#mackStr").val("");
		var str=""; 
		var mackStr = "";
		$("input[name='checkbox']:checkbox:checked").each(function(){  
			var id = $(this).val();
			str+=$(this).val()+";";
			
			mackStr += id +"T:T"+$("#aremark"+id).val()+"P:P"; 
		}); 
		$("#controlStr").val(str);
		$("#mackStr").val(mackStr);
		$("#deliverpaystate").val(2);
		$("#selectedDeliver").val($("#deliver").val());
		
		if($("#mackStr").val()==""){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		if(confirm("确定将勾选的审核为未通过？")){
	    	$("#updateForm").submit();
		}
	});
	
	$("#branchid").change(function(){
		var branchId = $("#branchid").val();
		$.ajax({
			  type: 'POST',
			  url: '<%=request.getContextPath()%>' + "/PDA/getBranchDeliver",
				data : {
					branchid : branchId
				},
				dataType : "JSON",
				success : function(data) {
					var $deliver = $("#deliver");
					$deliver.empty();
					$deliver.append(createOption('0','请选择'));
					for(var i = 0; i< data.length;i++){
						$deliver.append(createOption(data[i].userid , data[i].realname));
					}
				}
});
		});
	});
	
	function createOption(value ,text){
		return '<option value="' +value + '">' + text + '</option>';
	}
</script>
</head>

<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
			<form action="<%=request.getContextPath()%>/deliverpayup/deliverpayuplist" method="post"
				id="searchForm">
				审核站点： <select name="branchid" id="branchid">
				<option value="0">请选择</option>
					<%
						if(bList!=null && bList.size()>0) {
					%>
					<%
						for(Branch b : bList){
					%>
			 		<option value ="<%=b.getBranchid() %>" 
						<%if(b.getBranchid()==Integer.parseInt(request.getParameter("branchid")==null?"-1":request.getParameter("branchid"))){%>
						selected="selected" <%}%>><%=b.getBranchname()%></option>
					<%
						} }
					%>
				</select> 小件员： <select name="deliver" id="deliver">
					<option value="0">请选择</option>
					<%
						if(delivers != null)
						{
							for(User u : delivers){
					%>
					<option value=<%=u.getUserid()%>
						<%if(u.getUserid()==selectedDeliver){%>
						selected="selected" <%}%>><%=u.getRealname()%></option>
					<%
						}}
					%>
					
				</select> &nbsp;&nbsp;交款时间： <input type="text" name="begindate" id="strtime" value="<%=starttime%>" /> 到
				<input type="text" name="enddate" id="endtime" value="<%=endtime%>" /> 审核状态： <select
					name="deliverpayupapproved" id="deliverpayupapproved">
	          	<option value ="-1">请选择</option>
					<%
						for(DeliverPayupArrearageapprovedEnum c : DeliverPayupArrearageapprovedEnum.values()){
					%>
				<option value =<%=c.getValue() %> 
						<%if(c.getValue()==Integer.parseInt(request.getParameter("deliverpayupapproved")==null?"-1":request.getParameter("deliverpayupapproved"))){%>
						selected="selected" <%}%>><%=c.getText()%></option>
					<%
						}
					%>
				</select> <input id="find" value="查询" class="input_button2" /> <input type="button" id="btnval"
					value="导出" class="input_button1" onclick="exportField();" />
		</form>
		<form id="updateForm" action ="<%=request.getContextPath()%>/deliverpayup/update"  method = "post">
				<input type="hidden" id="controlStr" name="controlStr" value="" /> <input type="hidden"
					id="mackStr" name="mackStr" value="" /> <input type="hidden" id="deliverpaystate"
					name="deliverpaystate" value="" /> <input type="hidden" name="branchid"
					value="<%=request.getParameter("branchid")==null?"-1":request.getParameter("branchid")%>" /> <input
					type="hidden" name="begindate" value="<%=starttime%>" /> <input type="hidden" name="enddate"
					value="<%=endtime%>" /> <input type="hidden" name="deliverpayupapproved1"
					id="deliverpayupapproved1"
					value="<%=request.getParameter("deliverpayupapproved")==null?"-1":request.getParameter("deliverpayupapproved")%>" />
				<input type="hidden" name="branchname1" id="branchname1"
					value="<%=request.getParameter("branchname")==null?"":request.getParameter("branchname")%>" />
		</form>
	</div>
	<div class="right_title"  >
	<div style=" height:60px; overflow:hidden; clear:both "></div>
			<div style="height: 20px; overflow: hidden; clear: both">现金欠款=现金应收款-现金已交款-小件员交款时现金余额
				POS欠款=POS应收款-POS已交款-小件员交款时POS余额</div>
	<div style="width:100%;">
				<table style="width: 100%;" border="0" style="margin-right:30px" cellspacing="1" cellpadding="0"
					class="table_2" id="gd_table2">
	   <tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
							<th width="38px" height="38px" align="center" valign="middle" bgcolor="#eef6ff"><input
								type="checkbox" name="checkboxAll" id="btn1" /></th>
	   		<th width="100px" align="center" valign="middle" bgcolor="#f3f3f3">站点</th>
	   		<th width="70px" align="center" valign="middle" bgcolor="#f3f3f3">小件员</th>
			<th align="center" width="60px" valign="middle" bgcolor="#FFEFDF">交款时间</th>
			<th align="center" width="34px" valign="middle" bgcolor="#E7F4E3">交款方式</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">现金应收款</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">现金已交款</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">小件员交款时现金余额</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">现金欠款</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">POS应收款</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">POS已交款</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">小件员交款时POS余额</th>
			<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">POS欠款</th>
							<th align="center" width="70px" valign="middle" bgcolor="#E7F4E3">支付宝COD扫码支付已交款</th>
			<th align="center" width="157px" valign="middle" bgcolor="#FFEFDF">审核备注</th>
			<th align="center" width="60px" valign="middle" bgcolor="#FFEFDF">审核状态</th>
							<th align="center" valign="middle" bgcolor="#E7F4E3">交易流水号 / 交款地址</th>
			<td align="center" width="60px" valign="middle" bgcolor="#E7F4E3">改单明细</td>
		</tr>
		</tbody>
	</table>
		<div style="width:100%;overflow:auto;max-height:380px;">
					<table style="width: 100%;" border="0" cellspacing="1" cellpadding="0" class="table_2"
						id="gd_table2">
		<tbody>
		<%
		BigDecimal payupamountTotal = BigDecimal.ZERO;//现金应交款总额
									BigDecimal deliverPayupamountTotal = BigDecimal.ZERO;//现金已交款总额
									BigDecimal deliverAccountTotal = BigDecimal.ZERO;//现金已交款总额
									BigDecimal deliverPayuparrearageTotal = BigDecimal.ZERO;//现金欠款总额
									BigDecimal payupamount_posTotal = BigDecimal.ZERO;//POS应交款总额
									BigDecimal deliverPayupamount_posTotal = BigDecimal.ZERO;//POS已交款总额
									BigDecimal deliverPosAccountTotal = BigDecimal.ZERO;//现金已交款总额
									BigDecimal deliverPayuparrearage_posTotal = BigDecimal.ZERO;//POS欠款总额
									BigDecimal codTotal = BigDecimal.ZERO;
									if(viewList!=null)for(JSONObject view : viewList){
								payupamountTotal=payupamountTotal.add(BigDecimal.valueOf(view.getDouble("payupamount")));
								deliverPayupamountTotal=deliverPayupamountTotal.add(BigDecimal.valueOf(view.getDouble("deliverPayupamount")));
								deliverAccountTotal=deliverAccountTotal.add(BigDecimal.valueOf(view.getDouble("deliverAccount")));
								deliverPayuparrearageTotal=deliverPayuparrearageTotal.add(BigDecimal.valueOf(view.getDouble("deliverPayuparrearage")));
								payupamount_posTotal=payupamount_posTotal.add(BigDecimal.valueOf(view.getDouble("payupamount_pos")));
								deliverPayupamount_posTotal=deliverPayupamount_posTotal.add(BigDecimal.valueOf(view.getDouble("deliverPayupamount_pos")));
								deliverPosAccountTotal =deliverPosAccountTotal.add(BigDecimal.valueOf(view.getDouble("deliverPosAccount")));
								deliverPayuparrearage_posTotal=deliverPayuparrearage_posTotal.add(BigDecimal.valueOf(view.getDouble("deliverPayuparrearage_pos")));
								codTotal = codTotal.add(BigDecimal.valueOf(view.getDouble("codpossum")));
							%>
		<tr style="width:100%;background-color: rgb(249, 252, 253); ">
								<td align="center" width="38px" valign="middle"><input type="checkbox" name="checkbox"
									value="<%=view.get("gcaid")%>" /></td>
			<td align="center"width="100px" valign="middle" bgcolor="#f3f3f3"><%=view.getString("branchName") %></td>
			<td align="center" width="70px" valign="middle" bgcolor="#f3f3f3"><%=view.getString("deliverName") %></td>
			<td align="center" width="60px" valign="middle"><%=view.get("auditingtime") %></td>
			<td align="center" width="34px" valign="middle"><strong style="color:blue;"><%=view.getString("deliverpayuptype") %></strong></td>
								<td align="right" width="70px" valign="middle"><a href='<%=request.getContextPath() +"/deliverpayup/showdetail?id="+view.get("id")%>'><strong style="color: blue;"><%=view.get("payupamount")%></strong></a></td>
			<td align="right" width="70px" valign="middle"><strong style="color:blue;"><%=view.get("deliverPayupamount") %></strong></td>
			<td align="right" width="70px" valign="middle"><strong style="color:green;"><%=view.get("deliverAccount") %></strong></td>
								<td align="right" width="70px" valign="middle"><strong
									style="color:<%=view.getString("deliverPayuparrearage").indexOf("-")>-1||view.getString("deliverPayuparrearage").equals("0.0")?"blue":"red"%>;"><%=view.get("deliverPayuparrearage")%></strong></td>
			<td align="right" width="70px" valign="middle"><strong style="color:blue;"><%=view.get("payupamount_pos") %></strong></td>
			<td align="right" width="70px" valign="middle"><strong style="color:blue;"><%=view.get("deliverPayupamount_pos") %></strong></td>
			<td align="right" width="70px" valign="middle"><strong style="color:green;"><%=view.get("deliverPosAccount") %></strong></td>
								<td align="right" width="70px" valign="middle"><strong
									style="color:<%=view.getString("deliverPayuparrearage_pos").indexOf("-")>-1||view.getString("deliverPayuparrearage_pos").equals("0.0")?"blue":"red"%>;"><%=view.get("deliverPayuparrearage_pos")%></strong></td>
								<td align="right" width="70px" valign="middle"><strong style="color: blue;"><%=view.get("codpossum")%></strong></td>
								<td align="center" width="1px" valign="middle"><input type="text"
									value="<%=view.get("checkremark")%>" id="aremark<%=view.get("gcaid")%>" /></td>
			<td align="center" width="60px" valign="middle"><strong style="color:blue;"><%=view.getString("shenheState") %></strong></td>
								<td align="left" valign="middle"><%=view.getString("payupNo")%> / <%=view.get("payupaddress")%></td>
								<td align="center" valign="middle">
									<%
										if(view.get("updateTime")!=null&&!view.getString("updateTime").equals("")){
									%> [<a
									href="javascript:getEditOrderList('<%=request.getContextPath()%>/editcwb/getList?gcaid=<%=view.get("gcaid")%>');">改单详情</a>]
									<%
										}
									%>
			</td>
		</tr>
							<%
								}
							%>
		</tbody>
		</table>
		</div>
				<table style="width: 100%;" border="0" cellspacing="1" cellpadding="0" class="table_2"
					id="gd_table2">
	   <tbody>
	   	<tr style="background-color: rgb(249, 252, 253); ">
	   		<td align="center" width="38px" valign="middle" bgcolor="#f3f3f3">总计</td>
			<td align="center" width="100px" valign="middle">&nbsp;</td>
			<td align="center" width="70px" valign="middle">&nbsp;</td>
			<td align="center" width="60px" valign="middle">&nbsp;</td>
			<td align="center" width="34px" valign="middle">&nbsp;</td>
			<td align="center" width="70px" valign="middle"><%=payupamountTotal %></td>
			<td align="center" width="70px" valign="middle"><%=deliverPayupamountTotal %></td>
			<td align="center" width="70px" valign="middle"><%=deliverAccountTotal %></td>
			<td align="center" width="70px" valign="middle"><%=deliverPayuparrearageTotal %></td>
			<td align="center" width="70px" valign="middle"><%=payupamount_posTotal %></td>
			<td align="center" width="70px" valign="middle"><%=deliverPayupamount_posTotal %></td>
			<td align="center" width="70px" valign="middle" ><%=deliverPosAccountTotal %></td>
			<td align="center" width="70px" valign="middle"><%=deliverPayuparrearage_posTotal %></td>
							<td align="right" width="70px" valign="middle"><%=codTotal%></td>
			<td align="center" width="157px" valign="middle">&nbsp;</td>
			<td align="center" width="60px"  valign="middle">&nbsp;</td>
			<td align="center" valign="middle">&nbsp;</td>
							<td align="center" valign="middle" width="60px">&nbsp;</td>
		</tr>
		<tr >
							<td align="center" valign="middle" colspan="17" bgcolor="#ffffff"><input type="button"
								value="已通过" id="updateTongguo" class="input_button2" /> <input type="button" value="未通过"
								id="updateButongguo" class="input_button2" /></td>
		</tr>
		</tbody>
	</table>
	</div>
	</div>
	<div class="jg_35"></div>
		<form action="<%=request.getContextPath()%>/deliverpayup/exportExcle" method="post"
			id="searchForm2">
			<input type="hidden" name="branchid1" id="branchid1"
				value="<%=request.getParameter("branchid")==null?"0":request.getParameter("branchid")%>" /> <input
				type="hidden" name="begindate1" id="begindate1" value="<%=starttime%>" /> <input type="hidden"
				name="enddate1" id="enddate1" value="<%=endtime%>" /> <input type="hidden"
				name="deliverpayupapproved1" id="deliverpayupapproved1"
				value="<%=request.getParameter("deliverpayupapproved")==null?"-1":request.getParameter("deliverpayupapproved")%>" />
			<input type="hidden" name="branchname1" id="branchname1"
				value="<%=request.getParameter("branchname")==null?"":request.getParameter("branchname")%>" />
	</form>
</div>
<script type="text/javascript">
function exportField(){
			if (
	<%=viewList != null && viewList.size()>0%>
		) {
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
}
			;
		}
</script>
</body>

</html>
