<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
List<JSONObject> viewList = request.getAttribute("viewList")==null?null:(List<JSONObject>)request.getAttribute("viewList");

List<Map<String, List<OrderFlowView>>> orderflowviewlist = request.getAttribute("orderflowviewlist")==null?null:(List<Map<String, List<OrderFlowView>>>)request.getAttribute("orderflowviewlist");



String isSearchFlag=request.getAttribute("isSearchFlag")==null?"":request.getAttribute("isSearchFlag").toString();

String remand = request.getAttribute("remand")==null?"":request.getAttribute("remand").toString();
%>
<head>
<title>订单查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script>

$(document).ready(function() {
	/* $("#cwb").keydown(function(event){
		if(event.keyCode==13) {
			if($("#cwb").val()==""){
				alert('请输入订单号');
				return false;
			}
			if($("#validateCode").val()==""){
				alert('请输入验证码');
				return false;
			}
			$("#seachForm").submit();
		}
	}); */
	$("#validateCode").keydown(function(event){
		if(event.keyCode==13) {
			if($("#cwb").val()==""){
				alert('请输入订单号');
				return false;
			}
			if($("#validateCode").val()==""){
				alert('请输入验证码');
				return false;
			}
			$("#seachForm").submit();
		}
	});
	$("#btnsearch").click(function(){
		if($("#cwb").val()==""){
			alert('请输入订单号');
			return;
		}
		if($("#validateCode").val()==""){
			alert('请输入验证码');
			return;
		}
		$("#seachForm").submit();
		
	});
});
function change(mag){
	var d=new Date();
	var imageUrl = "<%=request.getContextPath()%>/imageForOutside?a="+d.getDate()+Math.random();
	$("#"+mag).attr('src',imageUrl);
}
</script>
</head>

<body onLoad="$('#cwb').focus();">
<div class="" align="center">
<%if(ResourceBundleUtil.logotxd.equals("2")){ %>
			<img src="<%=request.getContextPath()%>/<%=ResourceBundleUtil.LOGOIMGURL %>" />
		<%} %>
</div>
<table width="628" border="0" cellspacing="0" cellpadding="0"  align="center" class="table_2" style="height:567px">
	<tr align="center">
		<td width="100%" valign="top">
		<div style="height:567px; font-size:12px; overflow-y:auto; overflow-x:hidden">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" style="font-size:12px">
			<tr class="font_1">
				<td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff" style ="font-size: 20px;">&nbsp;
				订单查询</td>
			</tr>
			<tr>
				<td width="100%" height="26" align="left" valign="middle" bgcolor="#eef6ff">
				<form action="<%=request.getContextPath()%>/order/orderFlowQueryForOutsideByCwb" id="seachForm" method="post">
				     &nbsp;请输入订单号：
					<label for="textfield2"></label>
					<!-- <input type="text" name="cwb" id="cwb"  class="input_text2"  /> -->　
					<textarea rows="3" cols="20" name="cwb" id="cwb" style="vertical-align:middle"></textarea>
					<label for="textfield">验证码：</label>
					<input name="validateCode" type="text"  id="validateCode" maxlength="30" size="6"/>
					<img src="<%=request.getContextPath()%>/imageForOutside?a=<%=System.currentTimeMillis() %>" id="magLogin">
					<a href="javascript:change('magLogin');" title="换一张">换一张</a>
					<input type="button" name="btnsearch" value="查询" id="btnsearch"/>
					</form>
				</td>
			</tr>
			<tr>
			  <td width="100%" height="26" align="center" valign="middle" bgcolor="#eef6ff">
			      <font color = "red"><%=remand %></font>
			  </td>
			</tr>
			<tr>
				<td width="100%" align="center" valign="top" valign="middle" >
					<%if(viewList != null){ %>
					<%for(JSONObject viewobj : viewList){ %>
					<table width="100%" border="0" cellspacing="1" cellpadding="0"   class="right_set2">
						<tr>
							<td valign="middle" colspan="3">
								<p>订单号：<font color="red"><%=viewobj.getString("cwb") %></font></p>
								<p><font color="red"><%=viewobj.getString("remand") %></font></p>
							</td>
						</tr>
						<tr>
							<td width="20%" align="left"  >操作时间</td>
							<td width="10%" align="center"  >操作人</td>
						    <td width="70%" align="left" >操作过程
						    </td>
                        </tr>
                        <%if(viewobj.getString("remand").equals("")&&orderflowviewlist!=null&&orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"))!=null){ %>
						 <% for(OrderFlowView flow:orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"))){if(flow.getDetail()!=null){%>
						<tr>
							<td align="left" ><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
							<td align="center" ><%=flow.getOperator().getRealname() %></td>
						    <td><%=flow.getDetail()%>
						    </td>
                        </tr>	
						<%}}}%>
						<tr>
						<p>----------------------------------------------------------------------------------------------------</p>
						</tr>
					</table>
					<%}}%>
				</td>
			</tr>
		</table>
		</div></td>
	</tr>
</table>
</body>
</html>