<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.OperationSetTime"%>
<%@page import="cn.explink.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> bList = (List<Branch>)request.getAttribute("branchList");
Map<Long, Long> rukuMap = (Map<Long, Long>)request.getAttribute("ruku_time_out");
Map<Long, Long> zhongzhuanzhanrukuMap = (Map<Long, Long>)request.getAttribute("zhongzhuanzhan_ruku_time_out");
Map<Long, Long> daozhanMap = (Map<Long, Long>)request.getAttribute("daozhan_time_out");
Map<Long, Long> linghuoMap = (Map<Long, Long>)request.getAttribute("linghuo_time_out");
Map<Long, Long> tuihuozhanrukuMap = (Map<Long, Long>)request.getAttribute("tuihuozhanruku_time_out");
Map<Long, Long> chukuMap = (Map<Long, Long>)request.getAttribute("chuku_time_out");
Map<Long, Long> zhongzhuanzhanchukuMap = (Map<Long, Long>)request.getAttribute("zhongzhuanzhan_chuku_time_out");
Map<Long, Long> tuihuochuzhanMap = (Map<Long, Long>)request.getAttribute("tuihuo_chuzhan_time_out");
Map<Long, Long> zhiliuMap = (Map<Long, Long>)request.getAttribute("zhiliu_time_out");
Map<Long, Long> jushouMap = (Map<Long, Long>)request.getAttribute("jushou_time_out");
long A1  = request.getAttribute("A1") == null ?0L : (Long)request.getAttribute("A1");
long A2  = request.getAttribute("A2") == null ?0L : (Long)request.getAttribute("A2");
long A3  = request.getAttribute("A3") == null ?0L : (Long)request.getAttribute("A3");
long A4  = request.getAttribute("A4") == null ?0L : (Long)request.getAttribute("A4");
long A5  = request.getAttribute("A5") == null ?0L : (Long)request.getAttribute("A5");
long A6  = request.getAttribute("A6") == null ?0L : (Long)request.getAttribute("A6");
long A7  = request.getAttribute("A7") == null ?0L : (Long)request.getAttribute("A7");
long A8  = request.getAttribute("A8") == null ?0L : (Long)request.getAttribute("A8");
long A9  = request.getAttribute("A9") == null ?0L : (Long)request.getAttribute("A9");
long A10 = request.getAttribute("A10") == null ?0L : (Long)request.getAttribute("A10");
List<Customer> customerList = request.getAttribute("customerlist")==null?null:(List<Customer>)request.getAttribute("customerlist");
String starttime=request.getAttribute("begindate")==null?"":request.getAttribute("begindate").toString();
String endtime=request.getAttribute("enddate")==null?"":request.getAttribute("enddate").toString();

String begindate=request.getAttribute("begindate")==null||"".equals(request.getAttribute("begindate").toString())?"-":request.getAttribute("begindate").toString();
String enddate=request.getAttribute("enddate")==null||"".equals(request.getAttribute("enddate").toString())?"-":request.getAttribute("enddate").toString();
long customerid = request.getAttribute("customerid") == null ?0L : (Long)request.getAttribute("customerid");
long modelid = request.getAttribute("modelid") == null ?0L : (Long)request.getAttribute("modelid");
List<OperationSetTime> modelList = request.getAttribute("modelList")==null?null:(List<OperationSetTime>)request.getAttribute("modelList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- saved from url=(0076)http://58.83.193.9/oms/order/select/1?dmpid=85C94DDF6073E6BE87A8C1577448EE08 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript">
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
$(function(){
	
	$("#saveButton").click(function(){
		$("#viewTitle").hide();
		$("#editTitle").show();
		$("#saveButton").hide();
		$("#viewButton").show();
	});
	$("#viewButton").click(function(){
		$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/operationTimeOut_back');
		$("#custID").val($("#customerid").val());
		$("#beginID").val($("#strtime").val());
		$("#endID").val($("#endtime").val());
		$("#modID").val($("#modelid").val());
		if($("#modelname").val()!='新增设定时间请输入新名' && $("#modelname").val()!=''){
			$("#modelnameID").val($("#modelname").val());
		}
		$("#isUpdate").val(1);
		$("#subForm").submit();
	});
	$("#show").click(function(){
		$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/operationTimeOut_back');
		$("#custID").val($("#customerid").val());
		$("#beginID").val($("#strtime").val());
		$("#endID").val($("#endtime").val());
		$("#modID").val($("#modelid").val());
		$("#subForm").submit();
	});
	$("#branchView").change(function(){
		if($("#branchView").val()==-1){
			$("tr[id^='branch']").show();
		}else{
			$("tr[id^='branch']").hide();
			$("#"+$(this).val()).show();
		}
	});
	$("#modelid").change(function(){
		$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/operationTimeOut_back');
		$("#custID").val($("#customerid").val());
		$("#beginID").val($("#strtime").val());
		$("#endID").val($("#endtime").val());
		$("#modID").val($("#modelid").val());
		$("#subForm").submit();
	});
	
	$("#delButton").click(function(){
		if(confirm("确定要删除所选的时间设置吗？"))
		{
		if($("#modelid").find("option:selected").text()=='默认'){
			alert("默认的选项不能删除");
			return false;
		}
		
		$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/del');
		$("#custID").val($("#customerid").val());
		$("#beginID").val($("#strtime").val());
		$("#endID").val($("#endtime").val());
		$("#modID").val($("#modelid").val());
		$("#subForm").submit();
		}
	});	
});

function count(branchid){
	$("#subForm").attr('action','<%=request.getContextPath()%>/ExceptionMonitor/showTimeOutByBranchid/'+branchid+'/1');
	$("#custID").val($("#customerid").val());
	$("#beginID").val($("#strtime").val());
	$("#endID").val($("#endtime").val());
	$("#modID").val($("#modelid").val());
	$("#subForm").submit();
}

</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="tabbox">
				<div style="position:relative; z-index:0 ">
					<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
						<div class="kfsh_search">
							<strong>超期信息监控</strong>　　
							<input type="text" onblur="if(this.value==''){this.value='新增设定时间请输入新名'}" onfocus="if(this.value=='新增设定时间请输入新名'){this.value=''}" value="新增设定时间请输入新名" id="modelname">
							<input type="button"  id="saveButton" value="设定时间" class="input_button2"/>
							<input type="button" style="display: none"   id="viewButton" value="保存设定" class="input_button2"/>
							<!-- <input type="button"  onclick="location.reload(true)" value="刷新"/> -->
							超期时间设置：
							<select name="modelid" id="modelid">
							  <%if(modelList!=null&&modelList.size()>0)for(OperationSetTime c : modelList){ %>
							  	<option value="<%=c.getId()%>"<%if(Long.parseLong(request.getAttribute("modelid")==null?"0":request.getAttribute("modelid").toString())==c.getId()){%>selected<%} %>><%=c.getName() %></option>
							  <%} %>
							</select>
							<input type="button"  id="delButton" value="删除所选" class="input_button2"/>
							&nbsp;供货商：
							<select name="customerid" id="customerid">
								<option value="0">请选择</option>
							  <%if(customerList!=null&&customerList.size()>0)for(Customer c : customerList){ %>
							  	<option value="<%=c.getCustomerid()%>"<%if(Long.parseLong(request.getParameter("customerid")==null?"0":request.getParameter("customerid").toString())==c.getCustomerid()){%>selected<%} %>><%=c.getCustomername() %></option>
							  <%} %>
							</select>
							入库时间：
								<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>"/>
							到
								<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>"/>
							<input type="button" id="show" value="查询" class="input_button2">
							</div>
						<form id="subForm" action="" method="POST">
						<input type="hidden" id="modID" name="modelid" value="<%=modelid%>"/>
						<input type="hidden" id="custID" name="customerid" value="<%=customerid%>"/>
						<input type="hidden" id="beginID" name="begindate" value="<%=begindate %>"/>
						<input type="hidden" id="endID" name="enddate" value="<%=enddate %>"/>
						<input type="hidden" id="isUpdate" name="isUpdate" value="0"/>
						<input type="hidden" id="modelnameID" name="modelname" value=""/>
						<table id="editTitle" style="display: none" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td align="center" valign="middle" bgcolor="#f3f3f3">对象名称11111111111111111</td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未出库<br/>
									<select name="A1">
									<option value="4" <%=A1== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A1== 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A1 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A1== 24?"selected":"" %>>1天</option>
									<option value="48" <%=A1 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A1 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A1 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A1 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A1 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A1 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到货<br/>
									<select name="A6">
									<option value="4" <%=A6== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A6 ==6?"selected":"" %>>6小时</option>
									<option value="12" <%=A6 ==12?"selected":"" %>>12小时</option>
									<option value="24" <%=A6 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A6 ==48?"selected":"" %>>2天</option>
									<option value="72" <%=A6 ==72?"selected":"" %>>3天</option>
									<option value="96" <%=A6 ==96?"selected":"" %>>4天</option>
									<option value="120" <%=A6 ==120?"selected":"" %>>5天</option>
									<option value="144" <%=A6 ==144?"selected":"" %>>6天</option>
									<option value="168" <%=A6 ==168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未领货<br/>
									<select name="A3">
									<option value="4" <%=A3== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A3 ==6?"selected":"" %>>6小时</option>
									<option value="12" <%=A3 ==12?"selected":"" %>>12小时</option>
									<option value="24" <%=A3 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A3 ==48?"selected":"" %>>2天</option>
									<option value="72" <%=A3 ==72?"selected":"" %>>3天</option>
									<option value="96" <%=A3 ==96?"selected":"" %>>4天</option>
									<option value="120" <%=A3 ==120?"selected":"" %>>5天</option>
									<option value="144" <%=A3 ==144?"selected":"" %>>6天</option>
									<option value="168" <%=A3 ==168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未归班<br/>
									<select name="A4">
									<option value="4" <%=A4== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A4 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A4 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A4 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A4 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A4 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A4 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A4 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A4 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A4 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期滞留<br/>
									<select name="A9">
									<option value="4" <%=A9== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A9 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A9 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A9 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A9 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A9 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A9 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A9 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A9 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A9 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退货<br/>
									<select name="A10">
									<option value="4" <%=A10== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A10 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A10 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A10 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A10 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A10 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A10 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A10 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A10 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A10 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到中转<br/>
									<select name="A7">
									<option value="4" <%=A7== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A7 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A7 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A7 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A7 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A7 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A7 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A7 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A7 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A7 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未中转<br/>
									<select name="A2">
									<option value="4" <%=A2== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A2 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A2 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A2 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A2 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A2 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A2 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A2 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A2 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A2 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到退货<br/>
									<select name="A8">
									<option value="4" <%=A8== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A8 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A8 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A8 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A8 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A8 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A8 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A8 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A8 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A8 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退供货商<br/>
									<select name="A5">
									<option value="4" <%=A5== 4?"selected":"" %>>4小时</option>
									<option value="6" <%=A5 == 6?"selected":"" %>>6小时</option>
									<option value="12" <%=A5 == 12?"selected":"" %>>12小时</option>
									<option value="24" <%=A5 == 24?"selected":"" %>>1天</option>
									<option value="48" <%=A5 == 48?"selected":"" %>>2天</option>
									<option value="72" <%=A5 == 72?"selected":"" %>>3天</option>
									<option value="96" <%=A5 == 96?"selected":"" %>>4天</option>
									<option value="120" <%=A5 == 120?"selected":"" %>>5天</option>
									<option value="144" <%=A5 == 144?"selected":"" %>>6天</option>
									<option value="168" <%=A5 == 168?"selected":"" %>>7天</option>
									</select></td>
									<td width="100" valign="middle" bgcolor="#E7F4E3" align="center">合计</td>
									<!-- <td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商超期未确认</td> -->
								</tr>
							</tbody>
						</table>
						</form>
						<table id="viewTitle"   width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tbody>
								<tr class="font_1" height="30" >
									<td align="center" valign="middle" bgcolor="#f3f3f3">
									<select  id="branchView" name="branchView"  >
									<option value="-1">对象名称 </option>
									<%for(Branch b : bList){ %>
									<option  value="branch<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
									<%} %>
									</select></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未出库111<br/><%=A1>23?(A1/24)+"天":A1+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到货<br/><%=A6>23?(A6/24)+"天":A6+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未领货<br/><%=A3>23?(A3/24)+"天":A3+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未归班<br/><%=A4>23?(A4/24)+"天":A4+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期滞留<br/><%=A9>23?(A9/24)+"天":A9+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退货<br/><%=A10>23?(A10/24)+"天":A10+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到中转<br/><%=A7>23?(A7/24)+"天":A7+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未中转<br/><%=A2>23?(A2/24)+"天":A2+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未到退货<br/><%=A8>23?(A8/24)+"天":A8+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">超期未退供货商<br/><%=A5>23?(A5/24)+"天":A5+"小时" %></td>
									<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">合计<br/></td>
									<!-- <td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商超期未确认</td> -->
								</tr>
							</tbody>
						</table>
					</div>
					<div style="height:90px"></div>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2" >
						<tbody>
						<%for(Branch b : bList){ %>
							<tr name="data" height="30" id="branch<%=b.getBranchid() %>">
								<td align="center" valign="middle" bgcolor="#f3f3f3"><strong><%=b.getBranchname() %></strong></td>
								<td  width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.KuFang.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A1 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.RuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%}%>
									<%=b.getSitetype()==BranchEnum.KuFang.getValue()?String.valueOf(rukuMap.get(b.getBranchid())==null?0:rukuMap.get(b.getBranchid())):"-" %>
									
									<%if(b.getSitetype()==BranchEnum.KuFang.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td  width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()||b.getSitetype()==BranchEnum.KuFang.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A6 %>/0/<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>/0/<%=b.getBranchid()%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%}%>
									<%=(b.getSitetype()==BranchEnum.ZhanDian.getValue()||b.getSitetype()==BranchEnum.KuFang.getValue())?String.valueOf(chukuMap.get(b.getBranchid())==null?0:chukuMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()||b.getSitetype()==BranchEnum.KuFang.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A3 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhanDian.getValue()?String.valueOf(daozhanMap.get(b.getBranchid())==null?0:daozhanMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A4 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhanDian.getValue()?String.valueOf(linghuoMap.get(b.getBranchid())==null?0:linghuoMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td align="center" width="100" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A9 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.YiFanKui.getValue() %>,<%=FlowOrderTypeEnum.YiShenHe.getValue() %>/<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhanDian.getValue()?String.valueOf(zhiliuMap.get(b.getBranchid())==null?0:zhiliuMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A10 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.YiFanKui.getValue() %>,<%=FlowOrderTypeEnum.YiShenHe.getValue() %>/<%=DeliveryStateEnum.JuShou.getValue() %>/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhanDian.getValue()?String.valueOf(jushouMap.get(b.getBranchid())==null?0:jushouMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhanDian.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhongZhuan.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A7 %>/0/<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>/0/<%=b.getBranchid()%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhongZhuan.getValue()?String.valueOf(zhongzhuanzhanchukuMap.get(b.getBranchid())==null?0:zhongzhuanzhanchukuMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhongZhuan.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.ZhongZhuan.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A2 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.RuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.ZhongZhuan.getValue()?String.valueOf(zhongzhuanzhanrukuMap.get(b.getBranchid())==null?0:zhongzhuanzhanrukuMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.ZhongZhuan.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.TuiHuo.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A8 %>/0/<%=FlowOrderTypeEnum.TuiHuoChuZhan.getValue() %>/0/<%=b.getBranchid()%>/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.TuiHuo.getValue()?String.valueOf(tuihuochuzhanMap.get(b.getBranchid())==null?0:tuihuochuzhanMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.TuiHuo.getValue()){ %>
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
									<%if(b.getSitetype()==BranchEnum.TuiHuo.getValue()){ %>
										<a href="<%=request.getContextPath()%>/ExceptionMonitor/showTimeOut/<%=A5 %>/<%=b.getBranchid()%>/<%=FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() %>/0/0/<%=customerid%>/<%=begindate%>/<%=enddate%>/1">
									<%} %>
									<%=b.getSitetype()==BranchEnum.TuiHuo.getValue()?String.valueOf(tuihuozhanrukuMap.get(b.getBranchid())==null?0:tuihuozhanrukuMap.get(b.getBranchid())):"-" %>
									<%if(b.getSitetype()==BranchEnum.TuiHuo.getValue()){ %>aaa
										</a>
									<%} %>
								</td>
								<td width="100" align="center" valign="middle">
								
								<a id="allcount<%=b.getBranchid()%>"  href="#" onclick="count(<%=b.getBranchid()%>)"  ></a>
								</td> 
							</tr>
						<%} %>	
						</tbody>
					</table>
				</div>
		</div>
	</div>
</div>

<script>
$(function(){
	
	<%for(Branch b : bList){ %>
		var count=0;
		$("tr[id='branch<%=b.getBranchid()%>'] a").each(function(){
			if($(this).attr("id")!="allcount<%=b.getBranchid()%>"){
				count+=parseInt($(this).html());
			}
		});
		$("#allcount<%=b.getBranchid()%>").html(count);
	<%}%>
	
	
});

</script>
</body>
</html>
   
