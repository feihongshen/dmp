<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.ComplaintResultEnum"%>
<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<% 
CsComplaintAccept a= request.getAttribute("ca")==null?null:(CsComplaintAccept)request.getAttribute("ca");
List<Branch> b =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
List<Reason> r = request.getAttribute("lr")==null?null:(List<Reason>)request.getAttribute("lr");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建工单</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/saveComplainWorkOrderF" id="ComplainWorkOrderF">
				<table>
					<tr>
						<td>工单号:<%=a.getAcceptNo()%><input type="hidden" value="<%=a.getAcceptNo()%>" name="acceptNo"/></td>
						<td>订单号:<%=a.getOrderNo() %><input type="hidden" value="<%=a.getOrderNo() %>" name="orderNo"/></td>
						<%for(CwbStateEnum c:CwbStateEnum.values()) {%>
							<%if(c.getValue()==a.getCwbstate()) {%>							
								<td>订单状态:<%=c.getText()%>
									<input type="hidden" value="<%=a.getCwbstate()%>"  name="cwbstate">
								</td>	
						<%}} %>
					</tr>
					<tr>
						
					<%for(CwbFlowOrderTypeEnum cf : CwbFlowOrderTypeEnum.values()){ %>
						<%if(cf.getValue()==a.getFlowordertype()) {%>
							
								<td>订单操作状态:<%=cf.getText()%></td>
									<input type="hidden" value="<%=a.getFlowordertype()%>"  name="flowordertype">
							
						<%}} %>
						
						<td>
							当前机构:<%=a.getCurrentBranch() %>
							<input type="hidden" value="<%=a.getCurrentBranch() %>" name="currentBranch"/>
						</td>
					</tr>	
					<tr>
						<td>
							<span>责任机构:</span>
							<select class="select1" name="codOrgId" onchange="getComplaintUserValue()" id="codOrgIdValue">
								<option value="-1">请选择</option>
							<%for(Branch br:b){ %>
								<option value="<%=br.getBranchid()%>"><%=br.getBranchname() %></option>
							<%} %>	
							</select>
						</td>
						<td>
						<span>责任人:</span>
						
						<select name="ComplaintUser" id="ComplaintUseridValue" class="select1">
									
						
						
						</select>
						</td>
				</tr>
				<tr>
						<td>
							<span>一级分类:</span> 
							<select class="select1" name="complaintOneLevel" id="olreason" onchange="getReasonValue()">
							<option value="-1">请选择</option>
							<%if(r!=null){%>
							<%for(Reason reason:r){ %>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} }%>
								</select>
						</td>
						<td>
							<span>二级分类:</span>
							<select class="select1" name="complaintTwoLevel" id="tlreason">
									
							</select>
						</td>
				</tr>		
							<td>
							<span>处理结果:</span>
							<select class="select1" name="complaintResult" id="acceptresultC">  <!-- onblur="getSV(this.value)" -->
								<option value="<%=ComplaintResultEnum.WeiChuLi.getValue()%>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.ChengLi.getValue()%>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.BuChengLi.getValue()%>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>								
							</select>
							</td>
				<tr>
					<td>
					<span>工单内容:</span><textarea onfocus="this.value='' style="width: 80%;height: 150px;margin-left: 20px" name="content" id="compaltecontent">订单号+工单内容</textarea>
					<div>您还可以输入<font id="count" color="red">150</font>个文字</div> <!-- onkeyup="checkLen(this)" --> 
					</td>
				</tr>
					
					
				</table>
							<input type="hidden"  name="complaintState" id="cwo"/>
							<input type="hidden"  name="phoneOne" value="<%=a.getPhoneOne()%>">
							<input type="hidden"  name="provence" value="<%=a.getProvence()%>">
							<input type="hidden"  name="cuijianNum" value="0">
				</form>					
			</div>
			<div align="center">
			<%-- <button class="button" onclick="if(decidecomplain()){btnccwo('<%=ComplaintStateEnum.DaiChuLi.getValue()%>')}">保存待处理</button> --%>
			<button class="button" onclick="smsSend()">发送催件短信</button>
			<button class="button" onclick="if(decidecomplain()){btnccwo('<%=ComplaintStateEnum.DaiHeShi.getValue()%>')}">待机构核实</button>
			<button class="button" onclick="if(decidecomplain()){btnccwo(<%=ComplaintStateEnum.YiJieAn.getValue()%>)}">结案</button>
			<button class="button" onclick="closeBox()">取消</button>
			<input type="hidden" id="stateNum">
			<input type="hidden" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>" id="yja">
			<input type="hidden" value="<%=ComplaintResultEnum.BuChengLi.getValue()%>" id="bcl">
			</div>
	</div>
</div>

<div id="box_yy"></div>