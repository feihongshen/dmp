<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.User,cn.explink.domain.Customer"%>
<%@page import="java.math.BigDecimal" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String starttime=request.getParameter("begindate")==null?DateTimeUtil.getNowDate()+" 00:00:00":request.getParameter("begindate");
String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowDate()+" 23:59:59":request.getParameter("enddate");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
List<User> deliverList = request.getAttribute("deliverList")==null?null:( List<User>)request.getAttribute("deliverList");
List<Customer> customerList = request.getAttribute("customerList")==null?null:( List<Customer>)request.getAttribute("customerList");
long deliveryid = request.getParameter("deliverid")==null?0:Long.parseLong(request.getParameter("deliverid"));;
List dispatchbranchidList =(List) request.getAttribute("dispatchbranchidStr");

Map<Long,Map<Long,BigDecimal>> countMap = request.getAttribute("count")==null?null:(Map<Long,Map<Long,BigDecimal>>)request.getAttribute("count");
Map<Long,Map<Long,BigDecimal>> amountNOPOSMap = request.getAttribute("amountNOPOSMap")==null?null:(Map<Long,Map<Long,BigDecimal>>)request.getAttribute("amountNOPOSMap");
Map<Long,Map<Long,BigDecimal>> amountPOSMap = request.getAttribute("amountPOSMap")==null?null:(Map<Long,Map<Long,BigDecimal>>)request.getAttribute("amountPOSMap");
Map<Long,Map<Long,BigDecimal>> amountPaybackMap = request.getAttribute("amountPaybackMap")==null?null:(Map<Long,Map<Long,BigDecimal>>)request.getAttribute("amountPaybackMap");


long width = 120+400*4+(customerList.size()>3?(customerList.size()-3)*400:0);

String branchids = "0";
if(!dispatchbranchidList.isEmpty()) 
      {for(int i=0;i<dispatchbranchidList.size();i++){
      	branchids = branchids + "," + dispatchbranchidList.get(i).toString();
      }
   }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- saved from url=(0076)http://58.83.193.9/oms/order/select/1?dmpid=85C94DDF6073E6BE87A8C1577448EE08 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#deliverystate").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择配送结果' });
})
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
	$("#dispatchbranchid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择配送站点' });
	
});

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
					$("<option value='-1'>请选择小件员</option>").appendTo("#deliverid");//添加下拉框的option
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

function checkParam(){
	if($("#flowordertype").val()==-1){
		alert("请选择操作");
		return false;
	}
	
	if($("#strtime").val()=='' || $("#endtime").val() ==''){
		alert("请选择时间！");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	
	var year=$("#strtime").val().substring(0,4);
	var mm=$("#strtime").val().substring(5,7);
	var dd=$("#strtime").val().substring(8,10);
	var hh=$("#strtime").val().substring(11,13);
	var min=$("#strtime").val().substring(14,16);
	var ss=$("#strtime").val().substring(17,19);
	var year1=$("#endtime").val().substring(0,4);
	var mm1=$("#endtime").val().substring(5,7);
	var dd1=$("#endtime").val().substring(8,10);
	var hh1=$("#endtime").val().substring(11,13);
	var min1=$("#endtime").val().substring(14,16);
	var ss1=$("#endtime").val().substring(17,19);
	var beginemaildate =new Date(year,mm,dd,hh,min,ss);
	var endemaildate=new Date(year1,mm1,dd1,hh1,min1,ss1);
	if((endemaildate.getTime()-beginemaildate.getTime())/24/60/60/1000>60){
		alert("抱歉，只能选择跨度为60天以内的时间！");
		return false;
	}
	
	return true;
}

function sub(customerid,deliverid_show,dispatchbranchid_show){
	$('#customerid').val(customerid);
	$('#deliverid_show').val(deliverid_show);
	$('#dispatchbranchid_show').val(dispatchbranchid_show);
	$('#paybackfeeIs').val($('#paybackfeeIsZero').val());
	$('#show').submit();
}

function exportForDelivery(){
	var bool=<%=(countMap==null||countMap.isEmpty())%>;
	if(bool){
		alert("数据无意义，不能导出 ");
		return;
	}
	$("#export").submit();
	
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0" >
<div>
	<div style="background:#FFF">
		<div>
			<div>
				<div class="right_box">
					<div class="inputselect_box">
						<form action="<%=request.getContextPath() %>/deliverycash/list" method="post" onSubmit="if(checkParam()){submitSaveFormAndCloseBox('$(this)');}return false;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height:80px;font-size:13px;">
						<tr>
    <td >
    站点名称
						<input name="branchname" id="branchname" style="height:20px;" class="input_text1" onkeyup="selectallnexusbranch('<%=request.getContextPath()%>','dispatchbranchid',$(this).val());"/>
						 <label onClick="change();">
						 <select name ="dispatchbranchid" id ="dispatchbranchid"  multiple="multiple" style="width: 300px;height:20px;">
					          <%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){ %>
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
						 </label>
						  小件员
    <select name ="deliverid" id ="deliverid" onclick="changeBranchDeliver();" class="select1">
					          <option value ="-1">请选择小件员</option>
					           <%if(deliverList!=null&&deliverList.size()>0)for(User u : deliverList){ %>
					          <option value ="<%=u.getUserid() %>" 
					           <%if(u.getUserid()==Integer.parseInt(request.getParameter("deliverid")==null?"-1":request.getParameter("deliverid"))){ %>selected="selected" <%} %> ><%=u.getRealname()%></option>
					          <%}%>
						 </select>
						     配送结果
    						 <select name ="deliverystate" id ="deliverystate" multiple="multiple"  style="width: 180px">
					          <%for(DeliveryStateEnum ds : DeliveryStateEnum.values()){if(ds.getValue()!=DeliveryStateEnum.WeiFanKui.getValue()){ %>
					          <option value ="<%=ds.getValue() %>" 
					           <% if((request.getAttribute("deliverystate")==null?"":(String)request.getAttribute("deliverystate")).indexOf(","+ds.getValue()+",")>-1){ %>selected="selected" <%} %> ><%=ds.getText()%></option>
					          <%}}%>
						 </select>
    </td>
    </tr>
    <tr>
    <td>
    操作类型
    						 <select name ="flowordertype" id ="flowordertype" class="select1">
					          <option value ="-1">请选择操作</option>
					          <option value ="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue() %>"<%if(FlowOrderTypeEnum.FenZhanLingHuo.getValue()==Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype"))){ %>selected="selected" <%} %> ><%=FlowOrderTypeEnum.FenZhanLingHuo.getText()%></option>
					          <option value ="<%=FlowOrderTypeEnum.YiFanKui.getValue() %>"<%if(FlowOrderTypeEnum.YiFanKui.getValue()==Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype"))){ %>selected="selected" <%} %> ><%=FlowOrderTypeEnum.YiFanKui.getText()%></option>
					          <option value ="<%=FlowOrderTypeEnum.YiShenHe.getValue() %>"<%if(FlowOrderTypeEnum.YiShenHe.getValue()==Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype"))){ %>selected="selected" <%} %> ><%=FlowOrderTypeEnum.YiShenHe.getText()%></option>
						 </select>
    						操作时间
							<input type ="text" name ="begindate" id="strtime"  value="<%=starttime %>" class="input_text1" style="height:20px;"/>
						到
							<input type ="text" name ="enddate" id="endtime"  value="<%=endtime %>" class="input_text1" style="height:20px;"/>
							 应收金额
    						<select name="paybackfeeIsZero" id="paybackfeeIsZero" class="select1">
						 <option value="-1" >全部</option>
						 <option value="0" <%=request.getParameter("paybackfeeIsZero")!=null&&request.getParameter("paybackfeeIsZero").equals("0")?"selected":"" %>>=0</option>
						 <option value="1" <%=request.getParameter("paybackfeeIsZero")!=null&&request.getParameter("paybackfeeIsZero").equals("1")?"selected":"" %>>>0</option>
						 </select>
    </td>
    </tr>
    <tr>
    <td>
        						 <input type="submit" id="find"  value="查询" class="input_button2" />
						 <input type="button" id="exp" onclick="exportForDelivery()"  value="导出" class="input_button2" />
    </td>
    </tr>
						</table>
						</form>
						</div>
						<div style="height:90px"></div>
						<div style="position:relative; z-index:0; width:100% ">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_6" style="border-left:1px solid #CCC; border-top:1px solid #CCC">
								<tbody>
									<tr class="font_1" style="background-color: rgb(255, 255, 255); ">
										<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">小件员</td>
										<td align="center" valign="middle" bgcolor="#eef6ff" >客户</td>
									</tr>
								</tbody>
							</table>
							
							<div style="width: 122px; position: absolute; left: 0; top: 33px; overflow: hidden; z-index: 8; border-left: 1PX solid #CCC; border-right:1px solid #ccc">
								<table width="150" border="0" cellspacing="0" cellpadding="0" class="table_6">
									<tbody>
									<%if(deliverList!=null&&deliverList.size()>0){ 
										for(User u : deliverList){ 
											if((deliveryid>0&&u.getUserid()==deliveryid)||deliveryid<=0){ %>
										<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
											<td width="120" rowspan="3" align="center" valign="middle"><%=u.getRealname() %></td>
											<td align="center" valign="middle" bgcolor="#FFFFFF" >0</td>
										</tr> 
										<tr class="font_1" height="30">
											<td align="center" valign="middle">0</td>
										</tr>
										<tr class="font_1" height="30">
											<td align="center" valign="middle"><strong>0</strong></td>
										</tr>
									<%}}} %>
										<tr height="30" class="font_1" style="background-color: rgb(255, 255, 255); ">
											<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">合计</td>
											<td align="center" valign="middle" bgcolor="#eef6ff" >&nbsp;</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div style="overflow-x:scroll; width:100%;">
								<table width="<%=width %>" border="0" cellspacing="0" cellpadding="0"  >
									<tbody>
									<tr class="font_1" style="background-color: rgb(255, 255, 255); ">
										<td width="120" align="center" valign="middle" bgcolor="#EEF6FF">&nbsp;</td>
										<td colspan="3" align="left" valign="top" bgcolor="#eef6ff" >
										<%if(customerList!=null&&customerList.size()>0){for(Customer cus : customerList){%>
										<table width="400" border="0" cellspacing="0" cellpadding="0" class="table_6" style="float:left">
											<tbody>
											<%
												Map<Long,Long> totalToUserCount = new HashMap<Long,Long>();
												Map<Long,BigDecimal> totalToUserNoPOSAmount = new HashMap<Long,BigDecimal>();
												Map<Long,BigDecimal> totalToUserPOSAmount = new HashMap<Long,BigDecimal>();
												Map<Long,BigDecimal> totalToUserPaybackAmount = new HashMap<Long,BigDecimal>();
												
												if(deliverList!=null&&deliverList.size()>0){ 
													for(User u : deliverList){ 
														if((deliveryid>0&&u.getUserid()==deliveryid)||deliveryid<=0){
														Long totalCountToCustomer = 0L;
														BigDecimal nopostotalAmountToCustomer = BigDecimal.ZERO;
														BigDecimal postotalAmountToCustomer = BigDecimal.ZERO;
														BigDecimal paybacktotalAmountToCustomer = BigDecimal.ZERO;
											%>
											<% 
												Long c = countMap==null?0:countMap.get(u.getUserid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid()).longValue();
												BigDecimal nopos = amountNOPOSMap==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid());
												BigDecimal pos = amountPOSMap==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid());
												BigDecimal paybackfees = amountPaybackMap==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid());
												totalCountToCustomer+=c;
												nopostotalAmountToCustomer = nopostotalAmountToCustomer.add(nopos);
												postotalAmountToCustomer = postotalAmountToCustomer.add(pos);
												paybacktotalAmountToCustomer = paybacktotalAmountToCustomer.add(paybackfees);
												totalToUserCount.put(cus.getCustomerid(), (totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()))+c);
												totalToUserNoPOSAmount.put(cus.getCustomerid(), (totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())).add(nopos));
												totalToUserPOSAmount.put(cus.getCustomerid(), (totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())).add(pos));
												totalToUserPaybackAmount.put(cus.getCustomerid(), (totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())).add(paybackfees));
											%>
												<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
													<td colspan="4" align="center" valign="middle" bgcolor="#f8f8f8" ><%=cus.getCustomername() %></td>
													</tr>
												<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
													<td align="center" valign="middle" bgcolor="#f8f8f8">投递量</td>
													<td align="center" valign="middle" bgcolor="#f8f8f8">非POS</td>
													<td align="center" valign="middle" bgcolor="#f8f8f8">POS(含扫码)</td>
													<td align="center" valign="middle" bgcolor="#f8f8f8">应退款</td>
													</tr>
												<tr height="30" style="background-color: rgb(249, 252, 253); ">
												     
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=c %></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=nopos.doubleValue() %></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=pos.doubleValue() %></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=paybackfees.doubleValue() %></a></strong></td>
												</tr>
												<%} %>
												<%}} %>
												
												<%long allcustomersendnum =0;
													BigDecimal allcustomersendnoposmoney = BigDecimal.ZERO;
													BigDecimal allcustomersendposmoney = BigDecimal.ZERO;
													BigDecimal allcustomersendpaybackmoney = BigDecimal.ZERO;
													long count = totalToUserCount==null?0:(totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()) );
													BigDecimal noposamount = totalToUserNoPOSAmount==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid());
													BigDecimal posamount = totalToUserPOSAmount==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid());
													BigDecimal paybackfessamount = totalToUserPaybackAmount==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid());
													allcustomersendnum +=  count;
													allcustomersendnoposmoney = allcustomersendnoposmoney.add(noposamount);
													allcustomersendposmoney = allcustomersendposmoney.add(posamount);
													allcustomersendpaybackmoney = allcustomersendpaybackmoney.add(paybackfessamount);
												%>
												<tr height="30" style="background-color: rgb(249, 252, 253); ">
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=count%></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=noposamount %></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=posamount %></a></strong></td>
													<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=paybackfessamount %></a></strong></td>
												</tr>
											</tbody>
										</table>
										<%}} %>
										
										<table width="400" border="0" cellspacing="0" cellpadding="0" class="table_6" id="gd_table2" style="float:left">
										<tbody>
											<%
											Map<Long,Long> totalToUserCount = new HashMap<Long,Long>();
											Map<Long,BigDecimal> totalToUserNoPOSAmount = new HashMap<Long,BigDecimal>();
											Map<Long,BigDecimal> totalToUserPOSAmount = new HashMap<Long,BigDecimal>();
											Map<Long,BigDecimal> totalToUserPaybackAmount = new HashMap<Long,BigDecimal>();
											if(deliverList!=null){ 
												for(User u : deliverList){ 
													if((deliveryid>0&&u.getUserid()==deliveryid)||deliveryid<=0){
													Long totalCountToCustomer = 0L;
													BigDecimal nopostotalAmountToCustomer = BigDecimal.ZERO;
													BigDecimal postotalAmountToCustomer = BigDecimal.ZERO;
													BigDecimal paybacktotalAmountToCustomer = BigDecimal.ZERO;
												for(Customer cus : customerList){ 
													Long c = countMap==null?0:countMap.get(u.getUserid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid()).longValue();
													BigDecimal nopos = amountNOPOSMap==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid());
													BigDecimal pos = amountPOSMap==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid());
													BigDecimal paybackfees = amountPaybackMap==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid());
													totalCountToCustomer+=c;
													nopostotalAmountToCustomer = nopostotalAmountToCustomer.add(nopos);
													postotalAmountToCustomer = postotalAmountToCustomer.add(pos);
													paybacktotalAmountToCustomer = paybacktotalAmountToCustomer.add(paybackfees);
													totalToUserCount.put(cus.getCustomerid(), (totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()))+c);
													totalToUserNoPOSAmount.put(cus.getCustomerid(), (totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())).add(nopos));
													totalToUserPOSAmount.put(cus.getCustomerid(), (totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())).add(pos));
													totalToUserPaybackAmount.put(cus.getCustomerid(), (totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())).add(paybackfees));
												}
											%>
											<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
												<td colspan="4" align="center" valign="middle" bgcolor="#f8f8f8" >合计</td>
											</tr>
											<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
												<td align="center" valign="middle" bgcolor="#f8f8f8">投递量</td>
												<td align="center" valign="middle" bgcolor="#f8f8f8">非POS</td>
												<td align="center" valign="middle" bgcolor="#f8f8f8">POS(含扫码)</td>
												<td align="center" valign="middle" bgcolor="#f8f8f8">应退款</td>
											</tr>
											<tr height="30" style="background-color: rgb(249, 252, 253); ">
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=totalCountToCustomer %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=nopostotalAmountToCustomer.doubleValue() %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=postotalAmountToCustomer.doubleValue() %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=paybacktotalAmountToCustomer.doubleValue() %></a></strong></td>
											</tr>
											<%}}} %>
											<%long allcustomersendnum =0;
												BigDecimal allcustomersendnoposmoney = BigDecimal.ZERO;
												BigDecimal allcustomersendposmoney = BigDecimal.ZERO;
												BigDecimal allcustomersendpaybackmoney = BigDecimal.ZERO;
												%>
												<%if(customerList!=null&&customerList.size()>0){%>
												<%for(Customer cus : customerList){ %>
												<%long count = totalToUserCount==null?0:(totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()) );
												BigDecimal noposamount = totalToUserNoPOSAmount==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid());
												BigDecimal posamount = totalToUserPOSAmount==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid());
												BigDecimal paybackfessamount = totalToUserPaybackAmount==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid());
												allcustomersendnum +=  count;
												allcustomersendnoposmoney = allcustomersendnoposmoney.add(noposamount);
												allcustomersendposmoney = allcustomersendposmoney.add(posamount);
												allcustomersendpaybackmoney = allcustomersendpaybackmoney.add(paybackfessamount);
												}}
											%>
											<tr height="30" style="background-color: rgb(249, 252, 253); ">
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendnum %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendnoposmoney %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendposmoney %></a></strong></td>
												<td width="100" align="center" valign="middle"><strong><a href="javascript:;" onClick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendpaybackmoney %></a></strong></td>
											</tr>
										</tbody>
									</table>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</div>
							
			<%-- <table width="<%=request.getAttribute("width")==null?800:request.getAttribute("width") %>" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
						<tbody>
							<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
								<td width="100px" rowspan="2" align="center" valign="middle" bgcolor="#f3f3f3">姓名</td>
								<%if(customerList!=null&&customerList.size()>0)for(Customer c : customerList){ %>
									<td colspan="4" align="center" valign="middle" bgcolor="#F3F3F3"><%=c.getCustomername() %></td>
								<%} %>
								<td colspan="4" align="center" valign="middle" bgcolor="#F3F3F3">合计</td>
							</tr>
						
							<tr style="background-color: rgb(249, 252, 253); ">
								<%if(customerList!=null&&customerList.size()>0)for(Customer c : customerList){ %>
									<td align="center" width="150px" valign="middle">投递量</td>
									<td align="center" width="200px" valign="middle">非POS</td>
									<td align="center" width="130px" valign="middle">POS</td>
									<td align="center" width="130px" valign="middle">应退款</td>
								<%} %>
								<td align="center" width="150px" valign="middle">投递量</td>
								<td align="center" width="200px" valign="middle">非POS</td>
								<td align="center" width="130px" valign="middle">POS</td>
								<td align="center" width="130px" valign="middle">应退款</td>
							</tr>
						<%
						Map<Long,Long> totalToUserCount = new HashMap<Long,Long>();
						Map<Long,BigDecimal> totalToUserNoPOSAmount = new HashMap<Long,BigDecimal>();
						Map<Long,BigDecimal> totalToUserPOSAmount = new HashMap<Long,BigDecimal>();
						Map<Long,BigDecimal> totalToUserPaybackAmount = new HashMap<Long,BigDecimal>();
						if(deliverList!=null){ 
							for(User u : deliverList){ 
								if((deliveryid>0&&u.getUserid()==deliveryid)||deliveryid<=0){
								Long totalCountToCustomer = 0L;
								BigDecimal nopostotalAmountToCustomer = BigDecimal.ZERO;
								BigDecimal postotalAmountToCustomer = BigDecimal.ZERO;
								BigDecimal paybacktotalAmountToCustomer = BigDecimal.ZERO;
							%>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td align="center" valign="middle" bgcolor="#f3f3f3" id="user<%=deliveryid %>"><%=u.getRealname() %></td>
								<%for(Customer cus : customerList){ 
									Long c = countMap==null?0:countMap.get(u.getUserid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid())==null?0:countMap.get(u.getUserid()).get(cus.getCustomerid()).longValue();
									BigDecimal nopos = amountNOPOSMap==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountNOPOSMap.get(u.getUserid()).get(cus.getCustomerid());
									BigDecimal pos = amountPOSMap==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPOSMap.get(u.getUserid()).get(cus.getCustomerid());
									BigDecimal paybackfees = amountPaybackMap==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid())==null?BigDecimal.ZERO:amountPaybackMap.get(u.getUserid()).get(cus.getCustomerid());
									totalCountToCustomer+=c;
									nopostotalAmountToCustomer = nopostotalAmountToCustomer.add(nopos);
									postotalAmountToCustomer = postotalAmountToCustomer.add(pos);
									paybacktotalAmountToCustomer = paybacktotalAmountToCustomer.add(paybackfees);
									totalToUserCount.put(cus.getCustomerid(), (totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()))+c);
									totalToUserNoPOSAmount.put(cus.getCustomerid(), (totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())).add(nopos));
									totalToUserPOSAmount.put(cus.getCustomerid(), (totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())).add(pos));
									totalToUserPaybackAmount.put(cus.getCustomerid(), (totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())).add(paybackfees));
								%>
									<td align="center" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=c %></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=nopos.doubleValue() %></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=pos.doubleValue() %></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=paybackfees.doubleValue() %></a></td>
								<%} %>
								<td align="center" valign="middle"><a href="javascript:;" onclick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=totalCountToCustomer %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=nopostotalAmountToCustomer.doubleValue() %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=postotalAmountToCustomer.doubleValue() %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=u.getUserid() %>,<%=u.getBranchid()%>);"><%=paybacktotalAmountToCustomer.doubleValue() %></a></td>
							</tr>
						<%}}} %>
							<tr style="background-color: rgb(249, 252, 253); ">
								<td align="center" valign="middle" bgcolor="#f3f3f3" >合计</td>
								<%long allcustomersendnum =0;
								BigDecimal allcustomersendnoposmoney = BigDecimal.ZERO;
								BigDecimal allcustomersendposmoney = BigDecimal.ZERO;
								BigDecimal allcustomersendpaybackmoney = BigDecimal.ZERO;
								%>
								<%if(customerList!=null&&customerList.size()>0){%>
								<%for(Customer cus : customerList){ %>
								<%long count = totalToUserCount==null?0:(totalToUserCount.get(cus.getCustomerid())==null?0:totalToUserCount.get(cus.getCustomerid()) );
								BigDecimal noposamount = totalToUserNoPOSAmount==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserNoPOSAmount.get(cus.getCustomerid());
								BigDecimal posamount = totalToUserPOSAmount==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPOSAmount.get(cus.getCustomerid());
								BigDecimal paybackfessamount = totalToUserPaybackAmount==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid())==null?BigDecimal.ZERO:totalToUserPaybackAmount.get(cus.getCustomerid());
								allcustomersendnum +=  count;
								allcustomersendnoposmoney = allcustomersendnoposmoney.add(noposamount);
								allcustomersendposmoney = allcustomersendposmoney.add(posamount);
								allcustomersendpaybackmoney = allcustomersendpaybackmoney.add(paybackfessamount);
								%>
									<td align="center" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=count%></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=noposamount %></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=posamount %></a></td>
									<td align="right" valign="middle"><a href="javascript:;" onclick="sub(<%=cus.getCustomerid() %>,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=paybackfessamount %></a></td>
								<%}}else{ %>
									<td align="center" valign="middle"></td>
									<td align="right" valign="middle"></td>
									<td align="right" valign="middle"></td>
									<td align="right" valign="middle"></td>
								<%} %>
								<td align="center" valign="middle"><a href="javascript:;" onclick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendnum %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendnoposmoney %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendposmoney %></a></td>
								<td align="right" valign="middle"><a href="javascript:;" onclick="sub(0,<%=Integer.parseInt(request.getParameter("deliverid")==null?"0":request.getParameter("deliverid")) %>,'<%=branchids %>');"><%=allcustomersendpaybackmoney %></a></td>
							</tr>
						</tbody>
					</table>
					</div> --%>
					<form id="show" action="<%=request.getContextPath() %>/deliverycash/show/1" method="post">
					<input name="customerid" id="customerid" value="0" type="hidden"/>
					<input name="dispatchbranchid" id="dispatchbranchid_show" value="<%=branchids %>" type="hidden"/>
					<input name="deliverid" id="deliverid_show" value="<%=deliveryid %>" type="hidden"/>
					<input name="paybackfeeIs" id="paybackfeeIs"  type="hidden"/>
					<input name="flowordertype" value="<%=Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype")) %>" type="hidden"/>
					<input name="begindate" value="<%=starttime %>" type="hidden"/>
					<input name="enddate" value="<%=endtime %>" type="hidden"/>
					<%if(request.getParameterValues("deliverystate")!=null ){ %>
					<%for(String ds :request.getParameterValues("deliverystate")){ %>
					<input name="deliverystate" value="<%=ds%>" type="hidden"/>
					<%} }%>
					<div style="display: none;">
					<select name ="dispatchbranchidArr"  multiple="multiple" style="width: 320px;">
						          <%if(branchlist!=null&&branchlist.size()>0)for(Branch b : branchlist){ %>
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
					</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<form action="<%=request.getContextPath() %>/deliverycash/exportForDelivery" method="post"  id="export">
		  <input  name="dispatchbranchid1" type="hidden" value="<%=branchids %>"  />
		  <input name="deliverid1"  value="<%=deliveryid %>" type="hidden"/>
		  <input name="flowordertype1" value="<%=Integer.parseInt(request.getParameter("flowordertype")==null?"-1":request.getParameter("flowordertype")) %>" type="hidden"/>
		  <input name="begindate1" value="<%=starttime %>" type="hidden"/>
		  <input name="enddate1" value="<%=endtime %>" type="hidden"/>
		<%if(request.getParameterValues("deliverystate")!=null ){ %>
		<%for(String ds :request.getParameterValues("deliverystate")){ %>
		<input name="deliverystate1" value="<%=ds%>" type="hidden"/>
		<%} }%>
		<input name="paybackfeeIsZero1" value="<%=request.getParameter("paybackfeeIsZero")!=null?request.getParameter("paybackfeeIsZero"):"-1"%>" type="hidden"/>
</form>



</body>
</html>
   
