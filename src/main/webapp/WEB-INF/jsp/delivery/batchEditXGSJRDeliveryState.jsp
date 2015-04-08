<%@page import="cn.explink.enumutil.ExceptionCwbErrorTypeEnum"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum,cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.DeliveryStateEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import=" cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Customer,cn.explink.domain.Switch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

String deliverystate = request.getAttribute("deliverystate")==null?"":(String)request.getAttribute("deliverystate");
long leavedreasonid = request.getAttribute("leavedreasonid")==null?0:(Long)request.getAttribute("leavedreasonid");
long backreasonid = request.getAttribute("backreasonid")==null?0:(Long)request.getAttribute("backreasonid");
long deliverystateid = request.getAttribute("deliverystateid")==null?0:(Long)request.getAttribute("deliverystateid");
long successcount = request.getAttribute("successcount")==null?0:Long.parseLong(request.getAttribute("successcount").toString());
String deliverstateremark = request.getAttribute("deliverstateremark")==null?"":request.getAttribute("deliverstateremark").toString();


String resendtime = StringUtil.nullConvertToEmptyString(request.getParameter("resendtime"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量反馈--修改收件人</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}
 //判断条件
function checkForReturn(cwb,path){
	$.ajax({
		url:path+"/delivery/checkBoxForff",//后台处理程序
		type:"POST",//数据发送方式 
		data:"cwbs="+cwb,//参数
		dataType:'json',//接受数据格式
		success:function(data){
			if(data==1){
				alert("不是反馈为配送成功状态的订单");
			}else if(data==2){
				alert("无此订单");
			}else{
				$("#subForm").submit();
			}
	}
	});

} 
$(function(){
	
	<%if(successcount>0){%>
		alert("成功反馈：<%=successcount %>单");
	<%}%>
});
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="right_box">
	
		<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/delivery/auditView" >归班审核</a></li>		
			<li><a href="#" class="light" >反馈批量</a></li>
		</ul>

	</div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="./batchEditDeliveryState" >配送订单</a></li>
				<li><a href="./batchEditSMHDeliveryState">上门换订单</a></li>
				<li><a href="./batchEditSMTDeliveryState">上门退订单</a></li>
				<li><a href="./batchEditXGSJRDeliveryState" class="light">修改签收人</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div class="kfsh_search">
			<form action="<%=request.getContextPath()%>/delivery/batchEditXGSJRDeliveryState" method="post" id="subForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px">
					<tr>
						<td valign="middle" >&nbsp;&nbsp;
							订单号： <label for="textfield"></label> 
							<input type="text" name="cwbs"  id="cwbs" style="vertical-align: middle"  />
						签收人：<input type="text" name="consignName" value="" id="consignName" />
						<input type="button" name="button" style="border-left-width: 60px" id="button" value="确定" class="input_button2" onclick="checkForReturn($('#cwbs').val(),'<%=request.getContextPath()%>');" />
					(只能单个反馈有配送结果的订单)
					</td></tr>
				</table>
			</form>
		</div>
		</div>
	</div>
</body>
</html>

