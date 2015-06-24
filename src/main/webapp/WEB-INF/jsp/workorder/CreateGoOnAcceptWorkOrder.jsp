<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.ComplaintResultEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User"%>
<%
List<Reason> r =request.getAttribute("lr")==null?null:(List<Reason>)request.getAttribute("lr");
List<Reason> alltworeason =request.getAttribute("alltworeason")==null?null:(List<Reason>)request.getAttribute("alltworeason");
CsComplaintAccept a=request.getAttribute("lcs")==null?null: (CsComplaintAccept)request.getAttribute("lcs");
String conmobile=request.getAttribute("conMobile")==null?"":(String)request.getAttribute("conMobile");
String rukutime=request.getAttribute("RuKuTime")==null?"":(String)request.getAttribute("RuKuTime");
List<Branch> b =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
List<User> alluser=request.getAttribute("alluser")==null?null:(List<User>)request.getAttribute("alluser");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>继续受理工单</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/updateComplainWorkOrderF" id="UpdateComplainWorkOrderF">
				<table>
					<tr>
						<td>
						<%-- 受理类型:
						<%=ComplaintTypeEnum.getByValue(a.getComplaintType()).getText()%>		
						<input type="hidden" value="<%=a.getComplaintType()%>" name="complaintType">	 --%>
						<td><span style="color:orange;">继续受理工单</span>								
						</td>
						<td>工单号:<%=a.getAcceptNo()%><input type="hidden" id="transn" value="<%=a.getAcceptNo()%>" name="acceptNo"/></td>
						<td>订单号:<%=a.getOrderNo() %><input type="hidden" id="cwbn" value="<%=a.getOrderNo() %>" name="orderNo"/></td>
					</tr>
					<tr>	
						<%for(CwbStateEnum c:CwbStateEnum.values()) {%>
						<%if(c.getValue()==a.getCwbstate()) {%>							
					<td>订单状态:<%=c.getText()%>
						<input type="hidden" value="<%=a.getCwbstate()%>"  name="cwbstate">
					</td>	
					<%}} %>
					<td>
						收件人手机:<%=conmobile%>
					</td>	
						<td><span>当前机构:<%=a.getCurrentBranch() %></span><input type="hidden" value="<%=a.getCurrentBranch() %>" name="currentBranch"/></td>
					</tr>	
					<tr>
						<td>
							<span>被投诉机构:</span>
							<select style="border:1px solid #09C;width:150px; height:20px; line-height:20px;" name="codOrgId" disabled="disabled">
							<%for(Branch br:b){ %>
							<%if(a.getCodOrgId()==br.getBranchid() ) {%>
								<option value="<%=br.getBranchid()%>" selected="selected"><%=br.getBranchname() %></option>
							<%}else{%> 
								<option value="<%=br.getBranchid()%>"><%=br.getBranchname() %></option>
							<%	}}%>	
							</select>					
						</td>
						<td>
						<span>被投诉人:</span>
						<select style="border:1px solid #09C;width:150px; height:20px; line-height:20px;" disabled="disabled">
						<%for(User u:alluser){ %>
							<%if(a.getComplaintUser().equals(u.getUsername())){ %>
								<option><%=u.getRealname()%></option>
						<%} }%>
						</select>		
						<%-- <input type="hidden" name="ComplaintUser" value="<%=a.getComplaintUser() %>"> --%>
						</td>
						<td>入库时间:<%=rukutime %></td>
				</tr>
				<tr>
						<td>
							<span>一级分类:</span>
							<select style="border:1px solid #09C;width:150px; height:20px; line-height:20px;" name="complaintOneLevel" id="ol" disabled="disabled">
							<%if(r!=null){ %>
							<%for(Reason reason:r){ %>
								<%if(reason.getReasonid()==a.getComplaintOneLevel()) {%>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} }}%>
								</select>
						</td>
						<td>
							<span>二级分类:</span>
							<select style="border:1px solid #09C;width:150px; height:20px; line-height:20px;" disabled="disabled">
								<%if(r!=null){ %>
								<%for(Reason r1:alltworeason){ %>
								<%if(a.getComplaintTwoLevel()==r1.getReasonid()){%>
									<option><%=r1.getReasoncontent()%></option>
								<%}}} %>
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
					<span>投诉内容:</span><textarea onkeyup="checkLen(this)" style="width: 80%;height: 150px;margin-left: 20px" name="content"><%=a.getContent() %></textarea>
					
				</tr>
					</td>
					
				</table>
							<input type="hidden"  name="complaintState" id="cwo" value="">
							<input type="hidden"  name="phoneOne" value="<%=a.getPhoneOne()%>">
							<input type="hidden"  name="provence" value="<%=a.getProvence()%>">
				</form>					
			</div>
			<button class="button" onclick="smsSend()">发送催件短信</button>
			<%-- <button class="button" onclick="btnupdate('<%=ComplaintStateEnum.DaiChuLi.getValue()%>')">保存待处理</button> --%>
			<button class="button" onclick="btnupdate('<%=ComplaintStateEnum.DaiHeShi.getValue()%>')">待机构核实</button>
			<button class="button" onclick="btnupdate('<%=ComplaintStateEnum.YiJieAn.getValue()%>')">结案</button>
			<button class="button" onclick="closeBox()">取消</button>
	</div>
</div>

<div id="box_yy"></div>