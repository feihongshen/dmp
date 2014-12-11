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
List<JSONObject> objList = (List<JSONObject>)request.getAttribute("objList");
List<User> deliverList = (List<User>)request.getAttribute("deliverList");
/* List<Branch> branchList = (List<Branch>)request.getAttribute("branchList"); */
String deliverystate = request.getAttribute("deliverystate")==null?"":(String)request.getAttribute("deliverystate");
long deliverystateid = request.getAttribute("deliverystateid")==null?0:(Long)request.getAttribute("deliverystateid");
long paytype = request.getAttribute("paytype")==null?-1:(Long)request.getAttribute("paytype");
long successcount = request.getAttribute("successcount")==null?0:Long.parseLong(request.getAttribute("successcount").toString());

String showposandqita = request.getAttribute("showposandqita")==null?"no":(String)request.getAttribute("showposandqita");
Switch pl_switch = request.getAttribute("pl_switch")==null?null:(Switch) request.getAttribute("pl_switch");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量反馈-上门换订单</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript">
function dgetViewBox(key,durl){
	window.parent.getViewBoxd(key,durl);
}

$(function(){
	$("#right_hideboxbtn").click(function(){
			var right_hidebox = $("#right_hidebox").css("right")
			if(
				right_hidebox == -400+'px'
			){
				$("#right_hidebox").css("right","10px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn2.gif)");
				
			};
			
			if(right_hidebox == 10+'px'){
				$("#right_hidebox").css("right","-400px");
				$("#right_hideboxbtn").css("background","url(right_hideboxbtn.gif)");
			};
	});
	<%if(successcount>0){%>
		alert("成功反馈：<%=successcount %>单");
	<%}%>
});

function checkResult(){
	
}

function sub(){
	if($("#cwbs").val()==""){
		alert("请扫描订单号");
		return false;
	}
	if($("#cwbs").val().split("\n").length>500){
		alert("订单不允许超过500单");
		return false;
	}
	var result = 0;
	$("input[name='deliverystate']").each(function(){
		if($(this).attr("checked")=="checked"){
			result=1;
		}
	});
	if(result==0){
		alert("请选择配送结果");
		return;
	}
	$("input[name='deliverystate']").each(function(){
		if($(this).attr("checked")=="checked"){
			if($(this).val()==<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>&&$("#paytype").val()==-1){
				alert("请选择支付方式");
				return false;
			}else{
				$("#subForm").submit();
				return;
			}
		}
	});
}
function resub(form){
	var datavalue = "[";
	
	<%for(JSONObject obj:objList){if("555555".equals(obj.getString("errorcode"))){ %>
		datavalue = datavalue +"\"<%=obj.getString("cwb") %>_s_"+$("#deliverid<%=obj.getString("cwb") %>").val()+"\",";
	<%}}%>
	if(datavalue.length>1){
		datavalue= datavalue.substring(0, datavalue.length-1);
	}else{
		alert("无可提交订单！");
		return false;
	}
	datavalue= datavalue + "]";
	
	
	$("#cwbdetails").val(datavalue);
	
	$("#sub").submit();
	
}
</script>
</head>
<body style="background: #eef9ff" marginwidth="0" marginheight="0">
	<div class="right_box">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="./batchEditDeliveryState">配送订单</a></li>
				<li><a href="./batchEditSMHDeliveryState" class="light">上门换订单</a></li>
				<li><a href="./batchEditSMTDeliveryState">上门退订单</a></li>
				<li><a href="./batchEditXGSJRDeliveryState">修改签收人</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div class="kfsh_search">
			<form action="<%=request.getContextPath()%>/delivery/batchEditSMHDeliveryState" method="post" id="subForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px">
					<tr>
						<td valign="middle" >&nbsp;&nbsp; 
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.ShangMenHuanChengGong.getValue() %>" checked="checked"/> <%=DeliveryStateEnum.ShangMenHuanChengGong.getText() %>
						&nbsp;&nbsp; 
						支付方式：
							<select name="paytype" id="paytype">
								<option value="5" <%if(5==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>>默认支付方式</option>
								<option value="<%=PaytypeEnum.Xianjin.getValue()%>" <%if(PaytypeEnum.Xianjin.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Xianjin.getText()%></option>
								<%if(pl_switch!=null&&pl_switch.getState()!=null&&pl_switch.getState().equals(SwitchEnum.PiLiangFanKuiPOS.getInfo())){ %>
									<option value="<%=PaytypeEnum.Pos.getValue()%>" <%if(PaytypeEnum.Pos.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Pos.getText()%></option>
								<%} %>
								<option value="<%=PaytypeEnum.Zhipiao.getValue()%>" <%if(PaytypeEnum.Zhipiao.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Zhipiao.getText()%></option>
								 <%if(showposandqita.equals("yes")){ %>
									<option value="<%=PaytypeEnum.Qita.getValue()%>" <%if(PaytypeEnum.Qita.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Qita.getText()%></option>
								<%} %>
					        </select>
						</td>
					</tr>
					<tr>
						<td valign="middle" >&nbsp;&nbsp;
							订单号： <label for="textfield"></label> 
							<textarea name="cwbs" cols="25" rows="4" id="cwbs" style="vertical-align: middle;height:60px"  ></textarea>
							<input type="button" name="button" id="button" value="确定" class="input_button2" onclick="sub()" />
							(只能反馈上门换类型的订单)
						</td>
					</tr>
				</table>
			</form>
		</div>
			<%if(objList!=null&&objList.size()>0) {%>
			<!-- 以下订单请选择领货人: -->&nbsp;&nbsp;&nbsp;&nbsp;
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
					<td bgcolor="#F3F3F3">订单号</td>
					<td bgcolor="#F3F3F3">供货商</td>
					<td bgcolor="#F3F3F3">发货时间</td>
					<td bgcolor="#F3F3F3">配送站点</td>
					<td bgcolor="#F3F3F3">到站时间</td>
					<td bgcolor="#F3F3F3">小件员</td>
					<td bgcolor="#F3F3F3">反馈结果</td>
					<td bgcolor="#F3F3F3">异常原因</td>
				</tr>
				<%for(JSONObject obj:objList){
					if("000000".equals(obj.getString("errorcode"))){
						successcount ++;
						continue;
					}
				%>
				<tr>
					<td align="center" valign="middle"><%=obj.getString("cwb") %></td>
					<td align="center" valign="middle"><%=obj.get("customer")==null?"":obj.getString("customer") %></td>
					<td align="center" valign="middle"><%=obj.get("cwbOrder")==null?"":obj.getJSONObject("cwbOrder").getString("emaildate") %></td>
					<td align="center" valign="middle"><%=obj.get("cwbOrder")==null?"":obj.getJSONObject("cwbOrder").getString("excelbranch") %></td>
					<td align="center" valign="middle"><%=obj.getString("daohuoTime")==null?"":obj.getString("daohuoTime") %></td>
					<td align="center" valign="middle">
						<label for="select2"></label>
						<%if("555555".equals(obj.getString("errorcode"))){ %>
							<select name="deliverid<%=obj.getString("cwb") %>" id="deliverid<%=obj.getString("cwb") %>">
									<option value="0">请选择</option>
									<%for(User u : deliverList){ %>
										<option value="<%=u.getUserid()%>"><%=u.getRealname() %></option>
									<%} %>
							</select>
						<%}else{ %>
						无
						<%} %>
					</td>
					<td align="center" valign="middle"><%=deliverystate %></td>
					<td align="center" valign="middle"><%=obj.getString("errorinfo") %></td>
				</tr>
				<%} %>
			</table><%} %>
			
			<form  action="<%=request.getContextPath()%>/delivery/rebatchEditSMHDeliveryState" method="post" id="sub">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
				<tr class="font_1">
					<td>
					<input type="hidden" id="cwbdetails" name="cwbdetails" value="[]" />
					<input type="hidden" name="deliverystate" value="<%=deliverystateid%>" />
					<input type="hidden" name="paytype" value="<%=paytype%>" />
					<%if(objList!=null&&objList.size()>0) {%>
					<input type="button" name="button2" id="button2" value="提交" class="button" onclick="resub();"/><%} %></td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>

