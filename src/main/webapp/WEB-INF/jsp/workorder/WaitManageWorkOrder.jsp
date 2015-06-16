<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.ComplaintResultEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%
		List<CsComplaintAccept> l=request.getAttribute("lc")==null?null:(List<CsComplaintAccept>)request.getAttribute("lc");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">


$(function(){
	if($('#WaitAcceptNo').val()==""){
		$('#goonacceptinmanage').hide();
	}
	
		
	
$("table#refreshWO tr").click(function(){
	$(this).css("backgroundColor","yellow");
	$(this).siblings().css("backgroundColor","#ffffff");
	});
$("#goonacceptinmanage").click(function(){
	if($('#workordertypeV').val()==$('#ComplaintTypeEnumTouSu').val()){
	getAddBoxx2($('#WaitAcceptNo').val());
	}else if($('#workordertypeV').val()==$('#ComplaintTypeEnumChaXun').val()){
		getAddBoxGoOnAcceptQueryWo($('#WaitAcceptNo').val());
		
	}
});
	
});
function ctrlQJBL(){
	if(woNum==null){
		alert('请选择一条表单数据');
		return false;
	}
}
function addInit(){
	
	
}

function saveAcceptNoV(NoV,Wotype){
	$('#WaitAcceptNo').val(NoV);
	$('#workordertypeV').val(Wotype);
	$('#goonacceptinmanage').show();
	 getGoonacceptWO($('#WaitAcceptNo').val());
	 
	
}

function getAddBoxGoOnAcceptQueryWo(valWo) {
	 
	$.ajax({
		type : "POST",
		data:"workorder="+valWo,
		url : $("#GoOnacceptWoQuery").val(),
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);
			
		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面			
			viewBox();
		}
	});
	}



</script>
</head>
<body>
	<div>
			
		<form action="<%=request.getContextPath()%>/workorder/refreshwo">
			<input class="input_text1" value="<%=ComplaintStateEnum.DaiChuLi.getValue()%>" name="complaintState" type="hidden">
			<input type="submit" value="刷新表单" class="input_button2"/>
		</form>
		<hr>
		<button id="goonacceptinmanage" class="input_button2">继续受理</button><br>
		</br>
		<hr>
		<div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="refreshWO">
				<tr class="font_1">
					<th>工单号</th>
					<th>订单号</th>
					<th>来电号码</th>				
					<th>归属地</th>
					<th>受理时间</th>
					<th>工单类型</th>				
					<th>受理内容</th>				
					<th>工单状态</th>			
				</tr>
			<%if(l!=null){ %>
			<%for(CsComplaintAccept c:l){ %>
				<tr onclick="saveAcceptNoV('<%=c.getAcceptNo()%>',<%=c.getComplaintType()%>)">
					<td><%=c.getAcceptNo() %></td>
					<td><%=c.getOrderNo() %></td>
					<td><%=c.getPhoneOne() %></td>
					<td><%=c.getProvence() %></td>
					<td><%=c.getAcceptTime() %></td>
					<td><%=ComplaintTypeEnum.getByValue(c.getComplaintType()).getText()%></td>
					<%if(c.getComplaintType()==ComplaintTypeEnum.CuijianTousu.getValue()){ %>
						<%if(c.getContent().length()>20) {%>
					<td><%=c.getContent().substring(0, 20) %></td>
						<%}else{ %>
						<td><%=c.getContent()%></td>
					<%} %>
					<%}else if(c.getComplaintType()==ComplaintTypeEnum.DingDanChaXun.getValue()){ %>
						<%if(c.getQueryContent().length()>20){ %>
					<td><%=c.getQueryContent().substring(0, 20) %></td>
						<%}else{ %>
						<td><%=c.getQueryContent()%></td>
						<%} %>
					<%} %>		
					<td><%=ComplaintStateEnum.DaiChuLi.getText()%></td>				
				</tr>
			<%}} %>
		</table>
		</div>
	</div>
			<input type="hidden" id="GoOnacceptWo" value="<%=request.getContextPath()%>/workorder/GoOnacceptWo"/>
			<input type="hidden" id="WaitAcceptNo"/>
			<input type="hidden" id="workordertypeV"/>
			<input type="hidden" id="ComplaintTypeEnumChaXun" value="<%=ComplaintTypeEnum.DingDanChaXun.getValue()%>"/>
			<input type="hidden" id="ComplaintTypeEnumTouSu" value="<%=ComplaintTypeEnum.CuijianTousu.getValue()%>"/>
				<input type="hidden" id="GoOnacceptWoQuery" value="<%=request.getContextPath()%>/workorder/GoOnacceptWoQuery"/>
</body>
</html>