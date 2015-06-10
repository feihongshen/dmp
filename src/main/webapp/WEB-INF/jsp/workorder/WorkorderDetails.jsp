<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.ComplaintResultEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%@page import="cn.explink.domain.CwbOrder"%>		
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>

<%
CsComplaintAccept cca=(CsComplaintAccept)request.getAttribute("cca")==null?null:(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=(CwbOrder)request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=(CsConsigneeInfo)request.getAttribute("cci")==null?null:(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb = (List<Branch>)request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
String oneleave=request.getAttribute("OneLevel")==null?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==null?null:(String)request.getAttribute("TwoLevel");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<body>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tr>
						<th colspan="3"><font color="red" size="5cm">工单详情</font></th>
					</tr>
					<tr>
							<td>工单号:<%=cca.getAcceptNo() %></td>
							<td>工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %></td>
							<td>订单号:<%=cca.getOrderNo() %></td>
					</tr>
					<tr>
						<td>受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %></td>
						<td>一级类型:<%=oneleave%></td>
						<td>二级类型:<%=twoleave%></td>
					</tr>
					<tr>
						<td><span>被投诉机构:</span>
						<%for(Branch b:lb){ %>
						<%if(b.getBranchid()==cca.getCodOrgId()) {%>
								<%=b.getBranchname() %>
						<%} }%></td>
						
						<td><span>被投诉人:</span><%= cca.getComplaintUser()%></td>
						<td><span>订单操作状态:</span><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%></td>
					</tr>
					<tr>
						
						<td><span>当前机构:</span><%=cca.getCurrentBranch() %></td>
						<td><span>客户名称:</span><%=co.getConsigneename() %></td>
						<td><span>来电人姓名:</span><%=cci.getName() %></td>
					</tr>
					<tr>
						
						<td><span>来电号码:</span><%=cci.getPhoneonOne() %></td>
						<td colspan="2"><span>收件人手机:</span><%=co.getConsigneemobile() %></td>
						
					</tr>
					<tr>
						<td><span>投诉内容:</span><%=cca.getContent()%></td>
						<td><span>受理时间:</span><%=cca.getAcceptTime() %></td>
						<td><span>受理人:</span><%=cca.getHandleUser() %></td>
						
					</tr>
					<tr>
						<td><span>核实内容:</span><%=cca.getRemark() %></td>	
						<td><span>核实时间:</span><%=cca.getHeshiTime() %></td>
						<td><span>核实人:</span><%=cca.getHandleUser() %><span>附件</span></td>
						
					</tr>
					<tr>
						<td><span>处理结果:</span>
							<label><%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %></label>
						</td>	
						<td><span>结案人</span><%=cca.getHandleUser() %></td>
						<td><span>结案时间</span><%=cca.getJieanTime() %></td>
					</tr>
					<tr>
						<td colspan="3"><span>结案备注:</span><%=cca.getJieanremark() %></td>
					</tr>
						<tr>
							<td>	<span>申诉时间:</span><%=cca.getComplaintTime() %></td>
							<td>	<span>申诉人:</span><%=cca.getHandleUser() %></td>
							<td>	<span>申诉内容:</span><%=cca.getShensuremark() %></td>
						</tr>
						
						<tr>				
						<td><span>结案重审结果:</span>				
						<%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %></td>
						<td><span>重审人:</span><%=cca.getHandleUser() %></td>
						<td><span>结案时间:</span><%=cca.getJieanchongshenTime() %></td>
						</tr>	
						<tr>
						<td colspan="3"><span>结案备注:</span><%=cca.getJieanchongshenremark()%></td>
						</tr>
						
						</table>
																						
	
</body>
</html>