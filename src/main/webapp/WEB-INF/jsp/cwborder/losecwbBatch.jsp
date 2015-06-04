<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
List<JSONObject> cwbList = (List<JSONObject>)request.getAttribute("cwbList");
long successCount = request.getAttribute("successCount")==null?0:(Long)request.getAttribute("successCount");
long failCount= request.getAttribute("failCount")==null?0:(Long)request.getAttribute("failCount");
List<Reason> shixiaoReasons=(List<Reason>)request.getAttribute("shixiaoreasons");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox ul li").eq(index).show().siblings().hide();
	});
	
})


function exportField(){
	if(<%=cwbList!=null&&cwbList.size()!=0%>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#searchForm2").submit();	
	}else{
		alert("没有做查询操作，不能导出！");
	}
}
function checkloseeffect(){
	var flag=false;
	console.info($("#cwbs").val().split("\n").length);
	console.info($.trim($("#cwbs").val())=="");
	if($.trim($("#cwbs").val())==""||$("#cwbs").val()=="每次输入的订单不超过100个"){
		$("#msg").html("*请输入订单号！(订单号不能为空噢）");
		return flag;
	}
	if($("#cwbs").val().split("\n").length>100){
		$("#msg").html("*亲，每次操作的订单数量不能超过100个！");
		return flag;
	}
	if($("#loseeffect").val()!=-1){
		flag=true;
	}else{
		$("#msg").show();
		$("#msg").html("*请选择失效原因(未选择失效原因不允许失效）");
	}

	return flag;
}

function clearMsg(){
	$("#msg").html("");
}
</script>
</HEAD>
<BODY style="background:#f5f5f5"  marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath() %>/cwborder/losecwbBatch" class="light">数据失效</a></li>
				<li><a href="<%=request.getContextPath() %>/cwborder/selectlosecwb/1">数据失效查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
			<div style="position:relative; z-index:0 " >
				<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
					<div class="kfsh_search">
						<form action="<%=request.getContextPath() %>/cwborder/losecwbBatch" method="POST" id="searchForm" onsubmit="return checkloseeffect();">
							
							订单号*：
							<textarea name="cwbs" rows="3" class="kfsh_text" id="cwbs" style="color:#CCCCCC;" onfocus="javascript:this.style.color='#000000';if(this.value=='每次输入的订单不超过100个')this.value='';" onblur="javascript:if(this.value==''){this.value='每次输入的订单不超过100个';this.style.color='#CCCCCC';}">每次输入的订单不超过100个</textarea>
							&nbsp;&nbsp;失效原因*：
							<select id="loseeffect" name="loseeffect">
							<option value="-1">请选择失效原因</option>
							<%if(shixiaoReasons!=null){ %>
								<%for(Reason reason:shixiaoReasons){ %>
								<option value="<%=reason.getReasonid() %>"><%=reason.getReasoncontent() %></option>
									<%} %>
									<% }%>
									</select>
									&nbsp;&nbsp;<input type="submit" value="确定"  class="input_button2" >&nbsp;&nbsp;&nbsp;<input type="reset" value="清空" onclick="clearMsg();" class="input_button2" >
									<div  style="height:1px;color: red;">
									<p id="msg" name="msg" ></p>
									</div>
									<br>
									<%if(cwbList.size()>0) {%>共 <%=cwbList.size() %>单,失效成功<%=successCount%>单,失败<%=failCount %>单。<%} %>
						</form>
					</div>
					<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
						<tbody>
							<tr class="font_1" height="30" >
								<td width="15%" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
								<td width="15%" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
								<td width="20%" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
								<td width="20%" align="center" valign="middle" bgcolor="#E7F4E3">当前状态</td>
								<td align="center" valign="middle" bgcolor="#E7F4E3">异常原因</td>
							</tr>
							
						</tbody>
					</table>
				</div>
				<%if(cwbList!=null&&cwbList.size()>0){ %>
				<div style="height:100px"></div>
				<br><br>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tbody>
				<%for(JSONObject obj:cwbList){
							if("000000".equals(obj.getString("errorcode"))){
								continue;
							}
							JSONObject co = obj.get("cwbOrder")==null?null:obj.getJSONObject("cwbOrder");
						%>
						
						<tr height="30" >
							<td width="15%" align="center" valign="middle"><%=obj.getString("cwb") %></td>
							<td width="15%" align="center" valign="middle"><%=obj.getString("customername") %></td>
							<td width="20%" align="center" valign="middle"><%=co==null?"":co.getString("emaildate") %></td>
							<td width="20%" align="center" valign="middle"><%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){if((co==null?0:co.getLong("flowordertype"))==ft.getValue()){%><%=ft.getText() %><%}} %></td>
							<td align="center" valign="middle"><%=obj.getString("errorinfo") %></td>
						</tr>
						<%} %>
					</tbody>
				</table>
			</div><%} %>
		</div>
	</div>
</div>
</BODY>
</HTML>
