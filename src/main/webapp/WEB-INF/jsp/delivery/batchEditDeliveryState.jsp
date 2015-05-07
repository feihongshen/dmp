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

List<Reason> backlist = (List<Reason>)request.getAttribute("backreasonlist");
List<Reason> firstchangereasonlist = (List<Reason>)request.getAttribute("firstchangereasonlist");

List<User> deliverList = (List<User>)request.getAttribute("deliverList");


List<Reason> leavedlist = (List<Reason>)request.getAttribute("leavedreasonlist");
List<Reason> weishuakareasonlist = (List<Reason>)request.getAttribute("weishuakareasonlist");

String deliverystate = request.getAttribute("deliverystate")==null?"":(String)request.getAttribute("deliverystate");
long leavedreasonid = request.getAttribute("leavedreasonid")==null?0:(Long)request.getAttribute("leavedreasonid");
long backreasonid = request.getAttribute("backreasonid")==null?0:(Long)request.getAttribute("backreasonid");
long deliverystateid = request.getAttribute("deliverystateid")==null?0:(Long)request.getAttribute("deliverystateid");
long paytype = request.getAttribute("paytype")==null?-1:(Long)request.getAttribute("paytype");
long successcount = request.getAttribute("successcount")==null?0:Long.parseLong(request.getAttribute("successcount").toString());
String deliverstateremark = request.getAttribute("deliverstateremark")==null?"":request.getAttribute("deliverstateremark").toString();

String showposandqita = request.getAttribute("showposandqita")==null?"no":(String)request.getAttribute("showposandqita");
String batchEditDeliveryStateisUseCash = request.getAttribute("batchEditDeliveryStateisUseCash")==null?"no":(String)request.getAttribute("batchEditDeliveryStateisUseCash");
String pl_switch = request.getAttribute("pl_switch")==null?"no":(String)request.getAttribute("pl_switch");

String resendtime = StringUtil.nullConvertToEmptyString(request.getParameter("resendtime"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量反馈</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
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

$(function() {
	$("#resendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	
	
});
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
function changeTag(id){
	if(id==<%=DeliveryStateEnum.JuShou.getValue()%>){
		$("#leavedreasonid").parent().hide();
		$("#resendtime").parent().hide();
		$("#zhiliuremark").parent().hide();
		
		$("#backreasonid").parent().show();
		$("#deliverstateremark").parent().show();
		$("#paytype").parent().hide();
		
		$("#backreasonid").val(0);
		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#firstchangereasonid").parent().hide();
		$("#changereasonid").parent().hide();
		$("#paytype").val(-1);
	}else if(id==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>){
		$("#leavedreasonid").parent().show();
		$("#resendtime").parent().show();
		$("#zhiliuremark").parent().show();
		$("#backreasonid").parent().hide();
		$("#deliverstateremark").parent().hide();
		$("#paytype").parent().hide();
		
		$("#backreasonid").val(0);
		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#firstchangereasonid").parent().hide();
		$("#changereasonid").parent().hide();
		$("#paytype").val(-1);
	}else if(id==<%=DeliveryStateEnum.PeiSongChengGong.getValue()%>){
		$("#paytype").parent().show();
		$("#backreasonid").parent().hide();
		$("#deliverstateremark").parent().hide();
		$("#leavedreasonid").parent().hide();
		$("#resendtime").parent().hide();
		$("#zhiliuremark").parent().hide();
		
		$("#backreasonid").val(0);
		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#firstchangereasonid").parent().hide();
		$("#changereasonid").parent().hide();
		<%if(batchEditDeliveryStateisUseCash.equals("no")){ %>
			$("#paytype").val(-1);
		<%}%>
	}else if(id==<%=DeliveryStateEnum.DaiZhongZhuan.getValue()%>){
		$("#firstchangereasonid").parent().show();
		$("#changereasonid").parent().show();
		
		$("#backreasonid").parent().hide();
		$("#deliverstateremark").parent().hide();
		$("#paytype").parent().hide();
		$("#leavedreasonid").parent().hide();
	
		$("#zhiliuremark").parent().hide();
		$("#backreasonid").val(0);
		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#paytype").val(-1);
	}
	
	
} 

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
			if($(this).val()==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>){
				
				if($("#leavedreasonid").val()==0){
					alert("请选择滞留原因");
					return false;
				}
				var myDate = new Date();
				var myDatetime = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
				var myDatetimeArr = myDatetime.split('-');
				var myDateTimes = new Date(myDatetimeArr[0],myDatetimeArr[1],myDatetimeArr[2]).getTime();
				
				if($("#resendtime").val()!=''){
					var selecttimeArr = $("#resendtime").val().split('-');
					var selecttimedates = new Date(selecttimeArr[0],selecttimeArr[1],selecttimeArr[2].substring(0,2)).getTime();
					if($("#resendtime").val()!=""&&selecttimedates<myDateTimes){
						alert("再次配送时间不能早于当前时间");
						return false;
					}
				}
				
				$("#subForm").submit();
				return;
				
			}else if($(this).val()==<%=DeliveryStateEnum.JuShou.getValue() %>&&$("#backreasonid").val()==0){
				alert("请选择拒收原因");
				return false;
			}else if($(this).val()==<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>&&$("#firstchangereasonid").val()==0){
				alert("请选择一级中转原因");
				return false;
			}else if($(this).val()==<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>&&$("#changereasonid").val()==0){
				alert("请选择二级中转原因");
				return false;
			}
			
			
			else if($(this).val()==<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>&&$("#paytype").val()==-1){
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
	<%}if(obj.getLong("errorcode")==ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin.getValue()){%>
		datavalue = datavalue +"\"<%=obj.getString("cwb") %>_s_w"+$("#weishuakareasonid<%=obj.getString("cwb") %>").val()+"\",";
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
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
		
	<div class="right_box">
	
	
	<div class="saomiao_tab2">
		<ul>
			<li><a href="<%=request.getContextPath()%>/delivery/auditView" >归班反馈</a></li>		
			<li><a href="#" class="light" >批量反馈</a></li>
		</ul>

	</div>
	
		<div class="saomiao_tab2">		
			<ul>
				<li><a href="./batchEditDeliveryState" class="light">配送订单</a></li>
				<li><a href="./batchEditSMHDeliveryState">上门换订单</a></li>
				<li><a href="./batchEditSMTDeliveryState">上门退订单</a></li>
				<li><a href="./batchEditXGSJRDeliveryState">修改签收人</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div class="kfsh_search">
			<form action="<%=request.getContextPath()%>/delivery/batchEditDeliveryState" method="post" id="subForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px;height:150px">
					<tr>
						<td valign="middle" >&nbsp;&nbsp; 
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.JuShou.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.JuShou.getValue() %>);"/> <%=DeliveryStateEnum.JuShou.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>);"/> <%=DeliveryStateEnum.FenZhanZhiLiu.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.PeiSongChengGong.getValue() %>);"/> <%=DeliveryStateEnum.PeiSongChengGong.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate1" value="<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.DaiZhongZhuan.getValue() %>);"/> <%=DeliveryStateEnum.DaiZhongZhuan.getText() %>
						</td>
					</tr>
					<tr>
					<td>&nbsp;&nbsp; 
						<em style="display:none">
							滞留原因：
							 <select name="leavedreasonid" id="leavedreasonid" class="select1">
					        	<option value ="0">请选择</option>
					        	<%for(Reason r : leavedlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
						</em>
						<em style="display:none">
							再次配送时间：<input type ="text" name ="resendtime" id="resendtime" readonly="readonly"  value="<%=resendtime %>" class="input_text1"/>
						</em>
						<em style="display:none">
							滞留备注：<input type ="text" class="input_text1" name ="zhiliuremark" id="zhiliuremark"  value=""/>
						</em>
						<em style="display:none">
							拒收原因：
							 <select name="backreasonid" id="backreasonid" class="select1">
					        	<option value ="0">请选择</option>
					        	<%for(Reason r : backlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
						</em>
						
						<em style="display:none">
							一级原因：
							 <select name="firstchangereasonid" id="firstchangereasonid" class="select1"  onchange="updaterelatelevel('<%=request.getContextPath()%>/delivery/getChangeReason',this.value)">
					        	<option value ="0">请选择</option>
					        	<%for(Reason r : firstchangereasonlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
						</em>
						<em style="display:none">
							二级原因：
							 <select name="changereasonid" id="changereasonid" class="select1">
					        	<option value ="0">请选择</option>
					        </select>
						</em>
						
						
						
						<%if(batchEditDeliveryStateisUseCash.equals("yes")){ %>
							<em style="display:none"><input name="paytype" id="paytype" value="<%=PaytypeEnum.Xianjin.getValue()%>" type="hidden"/></em>
						<%}else{ %>
							<em style="display:none">支付方式：
								<select name="paytype" id="paytype" class="select1">
									
								<%-- 	<option value="5" <%if(5==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>>默认支付方式</option> --%>
									<option value="<%=PaytypeEnum.Xianjin.getValue()%>" <%if(PaytypeEnum.Xianjin.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Xianjin.getText()%></option>
									<%if(pl_switch.equals("yes")){ %>
										<option value="<%=PaytypeEnum.Pos.getValue()%>" <%if(PaytypeEnum.Pos.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Pos.getText()%></option>
									<%} %>
									<option value="<%=PaytypeEnum.Zhipiao.getValue()%>" <%if(PaytypeEnum.Zhipiao.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Zhipiao.getText()%></option>
									 <%if(showposandqita.equals("yes")){ %>
										<option value="<%=PaytypeEnum.Qita.getValue()%>" <%if(PaytypeEnum.Qita.getValue()==(request.getParameter("paytype")==null?5:Integer.parseInt(request.getParameter("paytype")))){ %>selected="selected" <%} %>><%=PaytypeEnum.Qita.getText()%></option>
									<%} %>
						        </select>
							</em>
						<%} %>
						<em style="display:none">拒收备注输入内容：
							<input type="text" name="deliverstateremark" class="input_text1" id="deliverstateremark" value ="<%=request.getParameter("deliverstateremark")==null?"":request.getParameter("deliverstateremark")%>" maxlength="50" />
						</em>
						</td>
					</tr>
					<tr>
						<td valign="middle" >&nbsp;&nbsp;
							订单号： <label for="textfield"></label> 
							<textarea name="cwbs" cols="25" rows="4" id="cwbs" style="vertical-align: middle;height:60px"  ></textarea>
							<input type="button" name="button" id="button" value="确定" class="input_button2" onclick="sub()" />
							(只能反馈配送类型的订单)
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
					<td bgcolor="#F3F3F3">未刷卡原因</td>
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
					<td align="center" valign="middle">
						<label for="select2"></label>
						<%if(obj.getLong("errorcode")==ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin.getValue()){ %>
							<select name="weishuakareasonid<%=obj.getString("cwb") %>" id="weishuakareasonid<%=obj.getString("cwb") %>">
									<option value="0">请选择</option>
									<%for(Reason r : weishuakareasonlist){ %>
										<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
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
			
			<form  action="<%=request.getContextPath()%>/delivery/rebatchEditDeliveryState" method="post" id="sub">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
				<tr class="font_1">
					<td>
					<input type="hidden" id="cwbdetails" name="cwbdetails" value="[]" />
					<input type="hidden" name="deliverystate" value="<%=deliverystateid%>" />
					<input type="hidden" name="backreasonid" value="<%=backreasonid%>" />
					<input type="hidden" name="deliverstateremark" value="<%=deliverstateremark%>" />
					<input type="hidden" name="paytype" value="<%=paytype%>" />
					<input type="hidden" name="leavedreasonid" value="<%=leavedreasonid%>" />
					<input type="hidden" name="resendtime" value="<%=request.getParameter("resendtime") %>" />
					<%if(objList!=null&&objList.size()>0) {%>
					<input type="button" name="button2" id="button2" value="提交" class="button" onclick="resub();"/><%} %></td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>

