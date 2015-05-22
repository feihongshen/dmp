<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
CwbOrder co = (CwbOrder)request.getAttribute("Order");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
function subEdit(form){
	if(form.cwbstate.value==0){
		alert("请输入订单状态");
		return false;
	}
	$(".tishi_box",parent.document).hide();
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
			if(data.errorCode==0){
				form.cwbstate.value=data.cwbstate;
				//form.exceldeliver.value=data.exceldeliver;
				var $inp = $('input:text');
				var nxtIdx = $inp.index(form.excelbranch) + 1;
	             $(":input:text:eq(" + nxtIdx + ")").focus();
			}else{
				form.cwbstate.value="0";
			}
		}
	});
}

function selectPage(page){
		$("#page").val(page);
		$("#editCwbstateForm").submit();
}

</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<form name="editCwbstateForm" id="editCwbstateForm" method="POST" action="geteditCwbstate"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">订单号：<input id="cwb" type="text" value="" name="cwb" />
						<input type="submit" class="input_button2" value="查询"><br/>
					</td>
					
				</tr>
			</table>
		</form>
				<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="10%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">邮编</td>
						<td width="29%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">修改订单状态</td>
						<!-- <td width="10%" align="center" height="38" align="center" valign="middle" bgcolor="#eef6ff">匹配到小件员</td> -->
						
<!-- 						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
 -->					</tr>
					<%if(co!=null){ %>
					
						<tr>
						<form id="f<%=co.getCwb() %>" method="POST" onSubmit="subEdit(this);return false;" action="editCwbstate/<%=co.getCwb() %>" >
							<td width="10%"  align="center" height="19" ><%=co.getCwb() %></td>
							<td width="10%"  align="center"  ><%=co.getConsigneename() %></td>
							
							<td width="10%"  align="center"  ><%=co.getConsigneemobile() %></td>
							<%for(CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()){if(ct.getValue()==co.getCwbordertypeid()){ %>
								<td width="8%"   align="center"  ><%=ct.getText() %></td>
							<%}} %>
							
							
							<%for(CwbStateEnum c : CwbStateEnum.values()){if(co.getCwbstate()==c.getValue()){ %>
								<td width="8%"   align="center"  ><%=c.getText() %></td>
							<%}} %>
							<td width="5%"   align="center"  ><%=co.getConsigneepostcode() %></td>
							<td width="29%"  align="left" id="add<%=co.getCwb() %>" ><%=co.getConsigneeaddress() %></td>
							
							
							
								<td width="10%"  align="center"  >
								<select id="cwbstate" name="cwbstate">
									<option value="0">请选择</option>
									<%for(CwbStateEnum ct : CwbStateEnum.values()){%>
										<option value="<%=ct.getValue()%>" <%if(co.getCwbstate()==ct.getValue()){ %>selected<%} %>><%=ct.getText() %></option>
									<%} %>
								</select>
								<input type="submit" name="" value="保存" onfocus="$('#add<%=co.getCwb() %>').css('background','#bbffaa');" onblur="$('#add<%=co.getCwb() %>').css('background','#ffffff');" /></td>
							
							
						</form>
						</tr>
					
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
		
</div>
</body>
</html>
