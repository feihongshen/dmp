<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
CsComplaintAccept cca=(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=(CwbOrder)request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb = (List<Branch>)request.getAttribute("lb");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>客服结案</h1>		
		<div id="box_form">
			<form action="">
				<table style="width: 100%">
		
					<tr>
					<td>工单号:<%=cca.getAcceptNo() %></td>
					<td>工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %></td>
					<td>订单号:<%=cca.getOrderNo() %></td>
					</tr>
					<tr>
					<td>受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %></td>
					<td>一级类型:<%=cca.getComplaintOneLevel() %></td>
					<td>二级类型:<%=cca.getComplaintTwoLevel() %></td>
					</tr>
					<tr>
					<td>被投诉机构:<%=cca.getCodOrgId() %></td>
					<td>被投诉人:<%= cca.getComplaintUser()%></td>
					</tr>
					<tr>
					<td>订单操作状态:<%=FlowOrderTypeEnum.getText(co.getFlowordertype())%></td>
					<td>当前机构:<%=cca.getCurrentBranch() %></td>
					<td>客户名称:<%=co.getConsigneename() %></td>
					</tr>
					<tr>
					<td>来电人姓名:<%=cci.getName() %></td>
					<td>来电号码:<%=cci.getPhoneonOne() %></td>
					<td>收件人手机:<%=co.getConsigneemobile() %></td>
					</tr>
					<tr>
					<td>投诉内容:<%=cca.getContent() %></td>
					</tr>
					<tr>
					<td>受理时间:<%=cca.getAcceptTime() %></td>
					</tr>
					<tr>
					<td><span>核实内容:</span></td>
					</tr>
					<tr>
					<td><span>核实时间:</span>
					<span>核实人:</span>
					<span>附件</span>
					</td>
					</tr>
					<tr>
					<td><span>处理结果:</span>
						<select class="select1">
							<option>成立</option>
						</select>
					</td>
					</tr>
				</table>
				<label>备注*:</label>
				<div>				
				<textarea style="width: 60%;height: 118px;margin-left: 60px"></textarea>																	
				</div>
				
				<div align="center"><input type="submit" value="结案" class="button"
				 <div align="center"><input type="submit" value="取消" class="button" onclick="closeBox()"/></div>
			</form>			
		</div>
		 
	</div>
</div>
<div id="box_yy"></div>