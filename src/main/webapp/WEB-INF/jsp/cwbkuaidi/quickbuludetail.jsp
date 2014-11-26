<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.SignStateEnmu"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String newcwbs=request.getAttribute("newcwbs")==null?"":request.getAttribute("newcwbs").toString();
	CwbKuaiDi kd=(CwbKuaiDi)request.getAttribute("kd");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽收订单快速补录信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function checkcheck(){
	var cwb=$("#cwbs").val();
	if(cwb.length>0){ 
		$("#searchForm").submit();
	 }else{ 
		alert("订单号不为空");
	} ;  
}
function  buluOne(){
		if($("#signstate").val()==0){
			alert("请选择签收状态！");
		}else if($("#signstate").val()==<%=SignStateEnmu.TaRenDaiQian.getKey() %>&&$("#signman").val().length==0){
			alert("请填写签收人");
		}else{
			$.ajax({
				type:"post",
				url:$("#saveform").attr("action"),
				dataType:"json",
				data:$("#saveform").serialize(),
				success:function(){
					$("#searchForm").submit();
				}
			});
		}
}

</script>
</head>
<body>
<div id="notify" style="position:absolute;height:30px;top:200px;left:400px;right:400px;display:none;border:2px solid #FF3300;text-align:center">
	<strong id="cont" style="font-size:20px;text-align:center;margin-top:5px"></strong>
</div>


<form action="<%=request.getContextPath() %>/cwbkuaidi/quickbuludetail" method="post" id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="10" class="table_5" >
		<tbody>
			<tr>
				<td align="left" valign="top" bgcolor="#EAF1B1">快递单号：(未补录订单不可快速补录)
					<p>请输入运单号，多个运单号同时输入时，请用回车键隔开</p>
					<textarea name="cwbs" cols="30" rows="5" id="cwbs" style="vertical-align:middle"><%=newcwbs%></textarea>
				<input type="button" class="input_button2"  onclick="checkcheck();"  value="查询" /></td>
			</tr>
		</tbody>
</form>
<%if(kd!=null){ %>
<form action="<%=request.getContextPath() %>/cwbkuaidi/savequickbulu" method="post" id="saveform">
	</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="10" class="table_5" >
			<tbody>
				<tr>
					<td width="200" colspan="2" align="left" valign="top" bgcolor="#F8F8F8"><table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td colspan="2" align="left" bgcolor="#F4F4F4"><h1><strong>快速补录：</strong></h1></td>
						</tr>
						<tr>
							<td colspan="2" align="left" valign="top" bgcolor="#FFFFFF">单号：
								<label for="select2">
									<input type="text" name="cwb" readonly="readonly" value="<%=kd.getCwb() %>"/>
								</label></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">签收状态：
									<label for="select2"></label>
								<select name="signstate" id="signstate"  <%if(kd.getSignstate()>0){ %>readonly="readonly"<%} %>>
								<%for(SignStateEnmu se:SignStateEnmu.values()){ %>
									<option value="<%=se.getKey() %>"  <%if(se.getKey()==kd.getSignstate()){ %>selected="selected"<%} %>  ><%=se.getValue() %></option>
								<%} %>
							</select></td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">签收人：
								<label for="textfield41"></label>
							<input type="text" name="signman" id="signman"  value="<%=StringUtil.nullConvertToEmptyString(kd.getSignman()) %>"  <%if(kd.getSignman()!=null&&kd.getSignman().length()>0){ %>readonly="readonly"<%} %>/></td>
						
						</tr>
						<tr>
							<td align="left" valign="top" bgcolor="#FFFFFF">签收时间：
								<input type="text" name="signtime" id="signtime" value="<%=StringUtil.nullConvertToEmptyString(kd.getSigntime()) %>"  <%if(kd.getSigntime()!=null&&kd.getSigntime().length()>0){ %>readonly="readonly"<%} %>/></td>
							<td width="25%" align="left" valign="top" bgcolor="#FFFFFF">备注：
							<textarea name="remark" cols="50" rows="5" id="remark"  <%if(kd.getRemark()!=null&&kd.getRemark().length()>0){ %>readonly="readonly"<%} %> title="最多可输入150个字"><%=kd.getRemark() %></textarea></td>
						</tr>
					</table></td>
				</tr>
				<tr>
					<td colspan="2" align="left" valign="top" bgcolor="#F8F8F8">
						<input name="button" type="button" class="input_button1" id="button"  onclick="buluOne();" value="确认补录" />
					</td>
				</tr>
			</tbody>
		</table>
</form>

<%} %>

</body>
</html>

