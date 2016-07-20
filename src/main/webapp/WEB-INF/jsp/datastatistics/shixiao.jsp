<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%
  String [] cwbordertypeidArray = request.getAttribute("cwbordertypeidStr")==null?new String[]{}:(String[])request.getAttribute("cwbordertypeidStr");

  String beginemaildate=request.getAttribute("beginemaildate")==null?"":(String)request.getAttribute("beginemaildate");
  String endemaildate=request.getAttribute("endemaildate")==null?"":(String)request.getAttribute("endemaildate");
  String beginupdatetime=request.getAttribute("beginupdatetime")==null?"":(String)request.getAttribute("beginupdatetime");
  String endupdatetime=request.getAttribute("endupdatetime")==null?"":(String)request.getAttribute("endupdatetime");
  long count = request.getAttribute("count")==null?0:(Long)request.getAttribute("count");
  
  String cwb = (String)request.getAttribute("cwb");
  Page page_obj = (Page)request.getAttribute("pageparm"); 
  List<CwbOrder> orderlist = (List<CwbOrder>)request.getAttribute("result"); 
  Map <Long,String> usermap=( Map <Long,String> )request.getAttribute("usermap");
  Map <Long,Customer> customermap=( Map <Long,Customer> )request.getAttribute("customermap");
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>失效订单查询</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	   //获取下拉框的值
	   $("#find").click(function(){
	         if(check()){
	        	 /*if($(":checked[name=dispatchbranchid]").length==0){
		        	 multiSelectAll('dispatchbranchid',1,'请选择');}
	        	  if($(":checked[name=curdispatchbranchid]").length==0){
		        	 multiSelectAll('curdispatchbranchid',1,'请选择');} */
	        	/*  if($(":checked[name=nextdispatchbranchid]").length==0){
		        	 multiSelectAll('nextdispatchbranchid',1,'请选择');} */
		    	$("#find").attr("disabled","disabled");
				$("#find").val("请稍等..");
		    	$("#searchForm").submit();
	         }
	   });
});

function check(){
	if($("#strtime1").val()=="" &&  $.trim($("#cwb").val())==""){
		alert("请填写订单号或失效时间");
		return false;
	}
	if($("#endtime1").val()=="" &&  $.trim($("#cwb").val())==""){
		alert("请选择结束失效时间");
		return false;
	}
	if($("#strtime2").val()!="" && $("#endtime2").val()==""){
		alert("请选择结束发货时间");
		return false;
	}
	if($("#strtime2").val()=="" && $("#endtime2").val()!=""){
		alert("请选择开始发货时间");
		return false;
	}
	if($("#strtime2").val()>$("#endtime2").val() && $("#endtime2").val() !=''){
		alert("开始发货时间不能大于结束时间");
		return false;
	}
	if($("#strtime1").val()>$("#endtime1").val() && $("#endtime1").val() !=''){
		alert("开始失效时间不能大于结束时间");
		return false;
	}
	if($("#endtime2").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime2").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}
	if($("#endtime1").val()>"<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>"){
		$("#endtime1").val("<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>");
	}

    var dt1 = new Date(Date.parse($("#strtime1").val().substring(0,10)));
    var dt2 = new Date(Date.parse($("#endtime1").val().substring(0,10)));
    var dt3 = new Date(Date.parse($("#strtime2").val().substring(0,10)));
    var dt4 = new Date(Date.parse($("#endtime2").val().substring(0,10)));
    var diff = parseInt((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24)); //天数
    if(diff>31){
        alert("失效时间查询范围不能超过31天");
                return false;
    }
    diff = parseInt((dt4.getTime() - dt3.getTime()) / (1000 * 60 * 60 * 24)); //天数
    if(diff>31){
        alert("到货时间查询范围不能超过31天");
                return false;
    }
	return true;
}

$(function() {
	$("#strtime1").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime1").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#strtime2").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime2").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});  
	$("#cwbordertypeid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'全部' }); 
	$('#btnexp').click(function(){exportField()});
});
function searchFrom(){
	$('#searchForm').attr('action',1);return true;
}
function delSuccess(data){
	$("#searchForm").submit();
}

function clearSelect(){
	/*$("#strtime").val('');//开始时间
	$("#endtime").val('');//结束时间
	$("#startbranchid").val(-1);//站点名称
	$("#isaudit").val(-1);//审核状态
	$("#cwd_id").val('');//订单编号
	$("#paytype").val(-1);//支付类型
	$("#curpaytype").val(-1);//当前支付类型
	$("#cwbstate").val(-1);//订单状态
	$("#curquerytimetype").val('<%=""+FlowOrderTypeEnum.DaoRuShuJu.getValue()%>');//
	$("#branchname").val('输入站点名称自动匹配');//
	$("#curbranchname").val('输入站点名称自动匹配');//
	$("#nextbranchname").val('输入站点名称自动匹配');//
	$("#financeAuditStatus").val(-1);
	$("#goodsType").val(-1); */
	multiSelectAll('cwbordertypeid',0,'全部'); 
	
}
</script>
</head>


<body style="background: #fff" marginwidth="0" marginheight="0">
	<div class="inputselect_box" style="top: 0px">
		<form action="<%=request.getContextPath()%>/datastatistics/obsoleteOrder/1" method="post"
			id="searchForm">
			<input type="hidden" id="isshow" name="isshow" value="1" /> <input type="hidden" name="page"
				value="<%=request.getAttribute("page")==null?"1":request.getAttribute("page")%>" />
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tbody> 
					<tr>
						<td>订单号&nbsp;&nbsp;&nbsp;：<input type="text" id="cwb" name="cwb" value="<%=cwb==null?"":cwb%>" /> </td>
						<td>订单类型：<select name="cwbordertypeid" id="cwbordertypeid" multiple="multiple">
							<% for(CwbOrderTypeIdEnum c : CwbOrderTypeIdEnum.values()){ 
								if(c.getValue()==-1) continue;
							%>
							<option value="<%=c.getValue()%>"
								<%if(cwbordertypeidArray!=null&&cwbordertypeidArray.length>0) 
					            {for(int i=0;i<cwbordertypeidArray.length;i++){
					            	if(c.getValue()== new Long(cwbordertypeidArray[i])){%>
												selected="selected" <%break;
					            	}
					            }
						     }%>><%=c.getText()%></option>
							<%
								}
							%>
							</select> &nbsp; </td>
						<td>失效时间：<input type="text" name="beginupdatetime" id="strtime1" value="<%=beginupdatetime%>" /> 到 <input
							type="text" name="endupdatetime" id="endtime1"  value="<%=endupdatetime%>" /></td>
					</tr>
					<tr>
						<td>发货时间：<input type="text" name="beginemaildate" id="strtime2"  value="<%=beginemaildate%>" /> 到 <input
							type="text" name="endemaildate" id="endtime2" value="<%=endemaildate%>" /></td>
					</tr>
				</tbody>
			</table>
			<div>
				<br>
				<input size="10px" type="button" id="find" value="查  询" class="input_button2"  /> <input size="10px" type="button" id="btnexp"
							value="导  出" class="input_button2" onclick="exportField()" />
			</div>
			<input	type="hidden" name="begin" id="begin" value="0" />
		</form>
	</div>
	<div style="height: 120px"></div>
	<%
		if(orderlist != null && orderlist.size()>0){
	%>
	<div style="overflow-x: scroll; width: 100%" id="scroll">
		<table width="1200" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
			<tr class="font_1">
				<td align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
				<td align="center" valign="middle" bgcolor="#eef6ff" width="200">运单号</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">发货时间</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">发货<br>件数</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">发货客户</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">订单操作类型</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">配送状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">数据<br>状态</td>
				<td align="center" valign="middle" bgcolor="#eef6ff">失效时间</td>  
				<td align="center" valign="middle" bgcolor="#eef6ff">失效人</td>
			</tr>

			<% for(CwbOrder c:orderlist){ %>
			<tr bgcolor="#FF3300">
				<td align="center" valign="middle" nowrap><%=c.getCwb()%></td>
				<td align="center" valign="middle"><%=c.getTranscwb()%></td>
				<td align="center" valign="middle"><%=CwbOrderTypeIdEnum.getTextByValue(c.getCwbordertypeid())%></td>
				<td align="center" valign="middle"><%=c.getEmaildate()%></td>
				<td align="center" valign="middle"><%=c.getSendcarnum()%></td>
				<td align="center" valign="middle"><%=customermap.get(c.getCustomerid())==null?"":customermap.get(c.getCustomerid()).getCustomername()%></td>
				<td align="center" valign="middle"><%=CwbStateEnum.getTextByValue((int)c.getCwbstate())%></td>
				<td align="center" valign="middle"><%=FlowOrderTypeEnum.getByValue((int)c.getFlowordertype()).getText()%></td>
				<td align="center" valign="middle">
				<% if(c.getDeliverystate()!=-1){
				%> <%=DeliveryStateEnum.getByValue(c.getDeliverystate())==null?"":DeliveryStateEnum.getByValue(c.getDeliverystate()).getText()%>
				<% } %>
				</td>
				<td align="center" valign="middle">失效</td>
				<td align="center" valign="middle"><%=c.getPrinttime()%></td>
				<td align="center" valign="middle"><%=usermap.get(c.getDeliverid())==null?"":usermap.get(c.getDeliverid())%></td>
			</tr>
			<%} %>
		</table>
	</div>
	<%
		}
	%>

	<%
		if(page_obj!=null && page_obj.getMaxpage()>1){
	%>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
			<tr>
				<td height="38" align="center" valign="middle" bgcolor="#eef6ff"><a
					href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a> <a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious()%>');$('#searchForm').submit();">上一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext()%>');$('#searchForm').submit();">下一页</a>
					<a
					href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage()%>');$('#searchForm').submit();">最后一页</a>
					共<%=page_obj.getMaxpage()%>页 共<%=page_obj.getTotal()%>条记录 当前第<select id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
						<%
							for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {
						%>
						<option value="<%=i%>"><%=i%></option>
						<%
							}
						%>
				</select>页</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>

	<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page")%>);
		function exportField(){
			if($("#isshow").val()=="1"&&<%=orderlist != null && orderlist.size()>0%>){
				var $searchForm = $("#searchForm");
				//保存现场
				var action = $searchForm.attr("action");

				$("#btnexp" ).attr("disabled","disabled");
				$("#btnexp" ).val("请稍候……");
				$("#begin").val(1);
				
				$searchForm.attr("action", "<%=request.getContextPath()%>/datastatistics/obsoleteOrder/excel");
				$searchForm.submit();

				//恢复现场
				$searchForm.attr("action", action);
				$("#btnexp" ).attr("disabled",false);
				$("#btnexp" ).val("导  出");
				$("#begin").val(0);
			} else {
				alert("没有做查询操作，不能导出！");
			}
		}
	</script>
</body>
</html>

