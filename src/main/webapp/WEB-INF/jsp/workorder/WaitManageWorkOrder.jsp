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
		List<CsComplaintAccept> l=(List<CsComplaintAccept>)request.getAttribute("lc");
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
$("table#refreshWO tr").click(function(){
	$(this).css("backgroundColor","violet");
	$(this).siblings().css("backgroundColor","#ffffff");
	});
$("#goonacceptinmanage").click(function(){
	getAddBoxx2();
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


</script>
</head>
<body>
	 <!-- onclick="ctrlQJBL()" -->
	<div>
		<button id="goonacceptinmanage">继续受理</button>
		<form action="<%=request.getContextPath()%>/workorder/refreshwo">
			<input value="<%=ComplaintStateEnum.DaiChuLi.getValue()%>" name="complaintState" type="hidden">
			<input type="submit" value="刷新表单" />
		</form>
		<!-- <button id="btwo">刷新表单</button> -->
	
		</br>
		</br>
		<div>
		<table width="100%" border="1" id="refreshWO">
			<tr>
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
			<tr onclick="getGoonacceptWO('<%=c.getAcceptNo()%>')">
				<td><%=c.getAcceptNo() %></td>
				<td><%=c.getOrderNo() %></td>
				<td><%=c.getPhoneOne() %></td>
				<td><%=c.getProvence() %></td>
				<td><%=c.getAcceptTime() %></td>
				<td><%=ComplaintTypeEnum.getByValue(c.getComplaintType()).getText()%></td>
				<td><%=c.getContent() %></td>
				<%for(CwbStateEnum cc:CwbStateEnum.values()){%>
							<%if(cc.getValue()==c.getCwbstate()){%>
								<td><%=cc.getText() %></td>	
					
				<% }}%>				
			</tr>
			<%}} %>
		</table>
		</div>
	</div>
	<input type="hidden" id="GoOnacceptWo" value="<%=request.getContextPath()%>/workorder/GoOnacceptWo"/>
</body>
</html>