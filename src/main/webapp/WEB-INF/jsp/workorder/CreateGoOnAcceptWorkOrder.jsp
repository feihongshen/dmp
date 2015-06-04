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
List<Reason> r = (List<Reason>)request.getAttribute("lr");
List<Reason> rs = (List<Reason>)request.getAttribute("lrs");
CsComplaintAccept a= (CsComplaintAccept)request.getAttribute("lcs");
String conmobile=(String)request.getAttribute("conMobile");
String rukutime=(String)request.getAttribute("RuKuTime");
List<Branch> b =(List<Branch>)request.getAttribute("lb");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/updateComplainWorkOrderF" id="UpdateComplainWorkOrderF">
				<table>
					<tr>
						<td>
						受理类型:
						<%=ComplaintTypeEnum.getByValue(a.getComplaintType()).getText()%>		
						<input type="hidden" value="<%=a.getComplaintType()%>" name="complaintType">									
						</td>
						<td>工单号:<%=a.getAcceptNo()%><input type="hidden" value="<%=a.getAcceptNo()%>" name="acceptNo"/></td>
						<td>订单号:<%=a.getOrderNo() %><input type="hidden" value="<%=a.getOrderNo() %>" name="orderNo"/></td>
					</tr>
					<tr>	
						<%for(CwbStateEnum c:CwbStateEnum.values()) {%>
						<%if(c.getValue()==a.getCwbstate()) {%>							
					<td>订单状态:<%=c.getText()%>
						<input type="hidden" value="<%=a.getCwbstate()%>"  name="cwbstate">
					</td>	
					<%}} %>
					<%-- <%for(CwbFlowOrderTypeEnum cf : CwbFlowOrderTypeEnum.values()){ %>
					<%if(cf.getValue()==a.getFlowordertype()) {%>
					<td>订单操作状态:<%=cf.getText()%>
					<input type="hidden" value="<%=a.getFlowordertype()%>"  name="flowordertype">
					</td>
						<%}} %> --%>
					<td>
						收件人手机:<%=conmobile%>
					</td>	
						<td><span>当前机构:<%=a.getCurrentBranch() %></span><input type="hidden" value="<%=a.getCurrentBranch() %>" name="currentBranch"/></td>
					</tr>	
					<tr>
						<td>
							<span>被投诉机构:</span>
							<select class="select1" name="codOrgId">
							<%for(Branch br:b){ %>
								<option value="<%=br.getBranchid()%>"><%=br.getBranchname() %></option>
							<%} %>	
							</select>					
						</td>
						<td>
						<span>被投诉人:</span>
						<%=a.getComplaintUser() %>
						<input type="hidden" name="ComplaintUser" value="<%=a.getComplaintUser() %>">
						</td>
						<td>入库时间:<%=rukutime %></td>
				</tr>
				<tr>
						<td>
							<span>一级分类:</span>
							<select class="select1" name="complaintOneLevel" id="ol">
							<%for(Reason reason:r){ %>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} %>
								</select>
						</td>
						<td>
							<span>二级分类:</span>
							<select class="select1" name="complaintTwoLevel" id="tl">
								<%for(Reason r1:rs){ %>
								<option value="<%=r1.getReasonid()%>"><%=r1.getReasoncontent()%></option>
								<%} %>
									</select>
						</td>
				</tr>		
							<td>
							<span>处理结果:</span>
							<select class="select1" name="complaintResult">
								<option value="<%=ComplaintResultEnum.WeiChuLi.getValue()%>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.ChengLi.getValue()%>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.BuChengLi.getValue()%>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>								
							</select>
							</td>
				<tr>
					<td>
					<span>投诉内容:</span><textarea style="width: 80%;height: 150px;margin-left: 20px" name="content"></textarea>
					
				</tr>
					</td>
					
				</table>
							<input type="hidden"  name="complaintState" id="cwo" value="">
							<input type="hidden"  name="phoneOne" value="<%=a.getPhoneOne()%>">
							<input type="hidden"  name="provence" value="<%=a.getProvence()%>">
				</form>					
			</div>
			<button class="button">发送催件短信</button>
			<button class="button" onclick="btnupdate('<%=ComplaintStateEnum.DaiChuLi.getValue()%>')">保存待处理</button>
			<button class="button" onclick="btnupdate('<%=ComplaintStateEnum.DaiHeShi.getValue()%>')">待机构核实</button>
			<button class="button" onclick="btnupdate('<%=ComplaintStateEnum.YiJieAn.getValue()%>')">结案</button>
			<button class="button" onclick="closeBox()">取消</button>
	</div>
</div>

<div id="box_yy"></div>