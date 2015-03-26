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
List<Reason> losereasonlist = (List<Reason>)request.getAttribute("losereasonlist");
List<Reason> leavedlist = (List<Reason>)request.getAttribute("leavedreasonlist");
long leavedreasonid = request.getAttribute("leavedreasonid")==null?0:(Long)request.getAttribute("leavedreasonid");
String batchEditDeliveryStateisUseCash = request.getAttribute("batchEditDeliveryStateisUseCash")==null?"no":(String)request.getAttribute("batchEditDeliveryStateisUseCash");
String resendtime = StringUtil.nullConvertToEmptyString(request.getParameter("resendtime"));
//上面为添加的



List<JSONObject> objList = (List<JSONObject>)request.getAttribute("objList");
List<Reason> backlist = (List<Reason>)request.getAttribute("backreasonlist");
/* List<Branch> branchList = (List<Branch>)request.getAttribute("branchList"); */
List<User> deliverList = (List<User>)request.getAttribute("deliverList");

String deliverystate = request.getAttribute("deliverystate")==null?"":(String)request.getAttribute("deliverystate");
long deliverystateid = request.getAttribute("deliverystateid")==null?0:(Long)request.getAttribute("deliverystateid");
long backreasonid = request.getAttribute("backreasonid")==null?0:(Long)request.getAttribute("backreasonid");
long losereasonid = request.getAttribute("losereasonid")==null?0:(Long)request.getAttribute("losereasonid");
long successcount = request.getAttribute("successcount")==null?0:Long.parseLong(request.getAttribute("successcount").toString());
String deliverstateremark = request.getAttribute("deliverstateremark")==null?"":request.getAttribute("deliverstateremark").toString();
String isReasonRequired = request.getAttribute("isReasonRequired")==null?"no":request.getAttribute("isReasonRequired").toString();

String showposandqita = request.getAttribute("showposandqita")==null?"no":(String)request.getAttribute("showposandqita");
Switch pl_switch = request.getAttribute("pl_switch")==null?null:(Switch) request.getAttribute("pl_switch");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量反馈-上门退订单</title>
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
function changeTag(id)
 {
	if(id==<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue()%>){
		$("#backreasonid").parent().hide();
		$("#backreasonid").val(0);
		$("#leavedreasonid").parent().hide();
		$("#resendtime").parent().hide();
		$("#zhiliuremark").parent().hide();
		$("#losereasonid").parent().hide();

		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#losereasonid").val(0);
	
    }else
	 if(id==<%=DeliveryStateEnum.ShangMenJuTui.getValue()%>){
		$("#backreasonid").parent().show();
		$("#backreasonid").val(0);
		$("#leavedreasonid").parent().hide();
		$("#resendtime").parent().hide();
		$("#zhiliuremark").parent().hide();
		$("#losereasonid").parent().hide();

		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		$("#losereasonid").val(0);
		
		
	}else if(id==<%=DeliveryStateEnum.FenZhanZhiLiu.getValue()%>){
		$("#leavedreasonid").parent().show();
		$("#resendtime").parent().show();
		$("#zhiliuremark").parent().show();
		$("#losereasonid").parent().hide();
		$("#losereasonid").val(0);
		$("#backreasonid").parent().hide();
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
		
		
	}else if(id==<%=DeliveryStateEnum.HuoWuDiuShi.getValue()%>){
		$("#losereasonid").parent().show();
		$("#losereasonid").val(0);
		$("#leavedreasonid").parent().hide();
		$("#resendtime").parent().hide();
		$("#zhiliuremark").parent().hide();
		$("#backreasonid").parent().hide();
		$("#backreasonid").val(0);
		$("#deliverstateremark").val("");
		$("#leavedreasonid").val(0);
		$("#resendtime").val("");
		$("#zhiliuremark").val("");
	}
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
		$("#right_hideboxbtn").click(function() {
			var right_hidebox = $("#right_hidebox").css("right")
			if (right_hidebox == -400 + 'px') {
				$("#right_hidebox").css("right", "10px");
				$("#right_hideboxbtn").css("background", "url(right_hideboxbtn2.gif)");

			}
			;

			if (right_hidebox == 10 + 'px') {
				$("#right_hidebox").css("right", "-400px");
				$("#right_hideboxbtn").css("background", "url(right_hideboxbtn.gif)");
			}
			;
		});
<%if(successcount>0){%>
		alert("成功反馈：<%=successcount %>单");
	<%}%>
});

function checkResult(){
	
}

function sub(){
	var isReasonRequired="<%=isReasonRequired%>";
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
		alert("请选择上门退配送结果");
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
				
			}else if($(this).val()==<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>&&($("#backreasonid").val()==0||$("#backreasonid").val()==null)){
				alert("请选择拒退原因");
				return false;
			}else if($(this).val()==<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>&&$("#losereasonid").val()==0){
				alert("请选择货物丢失原因");
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
<body style="background: #eef9ff" marginwidth="0" marginheight="0" >
	<div class="right_box">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="./batchEditDeliveryState">配送订单</a></li>
				<li><a href="./batchEditSMHDeliveryState">上门换订单</a></li>
				<li><a href="./batchEditSMTDeliveryState" class="light">上门退订单</a></li>
				<li><a href="./batchEditXGSJRDeliveryState">修改签收人</a></li>
			</ul>
		</div>
		<div class="tabbox">
		<div class="kfsh_search">
			<form action="<%=request.getContextPath()%>/delivery/batchEditSMTDeliveryState" method="post" id="subForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 12px">
					<tr>
						<td valign="middle" >&nbsp;&nbsp; 
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.ShangMenTuiChengGong.getValue() %>);"/> <%=DeliveryStateEnum.ShangMenTuiChengGong.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.ShangMenJuTui.getValue() %>);"/> <%=DeliveryStateEnum.ShangMenJuTui.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.FenZhanZhiLiu.getValue() %>);"/> <%=DeliveryStateEnum.FenZhanZhiLiu.getText() %>
							<input type="radio" name="deliverystate" id="deliverystate" value="<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>" onclick="changeTag(<%=DeliveryStateEnum.HuoWuDiuShi.getValue() %>);"/> <%=DeliveryStateEnum.HuoWuDiuShi.getText() %>
						</td>
					</tr>
					<tr>
					<td>&nbsp;&nbsp; 	
						<em style="display:none">	
							拒退原因：
							 <select name="backreasonid" id="backreasonid">
					        	<option value ="0" selected="selected">请选择</option>
					        	<%for(Reason r : backlist){ %>
			           				<option value="<%=r.getReasonid()%>" <%if((backreasonid==r.getReasonid())&&r.getReasoncontent()!=null){%> selected="selected"<%} %>><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
						
						拒退备注输入内容：
							<input type="text" name="deliverstateremark" id="deliverstateremark" value ="<%=deliverstateremark%>" maxlength="50" />
							</em>
							<em style="display:none">
							滞留原因：
							 <select name="leavedreasonid" id="leavedreasonid">
					        	<option value ="0">请选择</option>
					        	<%for(Reason r : leavedlist){ %>
			           				<option value="<%=r.getReasonid()%>"><%=r.getReasoncontent() %></option>
			           			<%} %>
					        </select>
						</em>
						<em style="display:none">
							再次配送时间：<input type ="text" name ="resendtime" id="resendtime" readonly="readonly"  value="<%=resendtime %>"/>
						</em>
						<em style="display:none">
							滞留备注：<input type ="text" name ="zhiliuremark" id="zhiliuremark"  value=""/>
						</em>
						<em style="display:none">
							货物丢失原因：
			       			 <select name="losereasonid" id="losereasonid">
			        			<option value ="0">请选择</option>
			        				<%for(Reason r : losereasonlist){ %>
	           						<option value="<%=r.getReasonid()%>"  title="<%=r.getReasoncontent() %>"><%if(r.getReasoncontent()!=null&&r.getReasoncontent().length()>10){ %><%=r.getReasoncontent().substring(0,10) %>...<%}else{ %><%=r.getReasoncontent()%><%} %></option>
	           				<%} %>
			      			  </select>
						</em>
						</td>
					</tr>
					<tr>
						<td valign="middle" >&nbsp;&nbsp;
							订单号： <label for="textfield"></label> 
							<textarea name="cwbs" cols="25" rows="4" id="cwbs" style="vertical-align: middle;height:60px"  ></textarea>
							<input type="button" name="button" id="button" value="确定" class="input_button2" onclick="sub()" />
							(只能反馈上门退类型的订单)
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
					/* String branchname="";
					long branchid=obj.get("cwbOrder")==null?0:obj.getJSONObject("cwbOrder").getLong("deliverybranchid");
					for(Branch b:branchList)
					{
						if(branchid==b.getBranchid())
						{
							branchname=b.getBranchname();
						}
					} */
				%>
				<tr>
					<td align="center" valign="middle"><%=obj.getString("cwb") %></td>
					<td align="center" valign="middle"><%=obj.get("customer")==null?"":obj.getString("customer") %></td>
					<td align="center" valign="middle"><%=obj.get("cwbOrder")==null?"":obj.getJSONObject("cwbOrder").getString("emaildate") %></td>
					<td align="center" valign="middle"><%=obj.get("cwbOrder")==null?"":obj.getJSONObject("cwbOrder").getString("excelbranch")  %></td>
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
			
			<form  action="<%=request.getContextPath()%>/delivery/rebatchEditSMTDeliveryState" method="post" id="sub">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
				<tr class="font_1">
					<td>
					<input type="hidden" id="cwbdetails" name="cwbdetails" value="[]" />
					<input type="hidden" name="deliverystate" value="<%=deliverystateid%>" />
					<input type="hidden" name="backreasonid" value="<%=backreasonid%>" />
					<input type="hidden" name="deliverstateremark" value="<%=deliverstateremark%>" />
					<input type="hidden" name="leavedreasonid" value="<%=leavedreasonid%>" />
					<input type="hidden" name="losereasonid" value="<%=losereasonid%>" />
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

