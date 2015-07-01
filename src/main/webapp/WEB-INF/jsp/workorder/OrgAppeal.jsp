<%-- <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<%@page import="cn.explink.domain.User"%>	
<%
List<User> alluser=request.getAttribute("alluser")==null?null:(List<User>)request.getAttribute("alluser");
CsComplaintAccept cca=request.getAttribute("cca")==null?null:(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=request.getAttribute("cci")==null?null:(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
String oneleave=request.getAttribute("OneLevel")==null?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==null?null:(String)request.getAttribute("TwoLevel");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>工单详情</h1>				
			<form id="ShenSuChangeComplaintStateF" enctype="multipart/form-data" method="post" onsubmit="if(IfShenSu())submitShenSu(this);return false;" action="<%=request.getContextPath()%>/workorder/ShenSuChangeComplaintStateFile;jsessionid=<%=session.getId()%>" >
				<div id="box_form">
			<ul>
					<li>
					工单号:<%=cca.getAcceptNo() %>&nbsp;
					工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %>&nbsp;
					订单号:<%=cca.getOrderNo() %>&nbsp;
					</li>
					<li>
						受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %>&nbsp;
						一级类型:<%=oneleave%>&nbsp;
						二级类型:<%=twoleave%>&nbsp;
					</li>
					<li>
					<%if(cca.getCodOrgId()!=-1){ %>
						<span>责任机构:</span>						
						<%for(Branch b:lb){ %>
						<%if(b.getBranchid()==cca.getCodOrgId()) {%>
								<%=b.getBranchname() %>
						<%} }%>
						<%if(cca.getComplaintUser()!=null){ %>
						<span>责任人:</span>						
						<%for(User u:alluser){ %>
							<%if(cca.getComplaintUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }}%>
						<%} %>
					</li>
					<li>
						<span>订单操作状态:</span><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%>
						<span>当前机构:</span><%=cca.getCurrentBranch() %>
						<span>客户名称:</span><%=co.getConsigneename() %>
					</li>
					<li>
						<%if(cci!=null){ %>
						<span>来电人姓名:</span><%=cci.getName()==null?"":cci.getName() %>
						<span>来电号码:</span><%=cci.getPhoneonOne()==null?"":cci.getPhoneonOne()%>
						<%}else{ %>
						<span>来电人姓名:</span>
						<span>来电号码:</span>
						<%} %>
						<span>收件人手机:</span><%=co.getConsigneemobile() %>
					</li>
					<li>
						<span>工单内容:</span><%=cca.getContent()%>
					</li>
					<li style="margin-top: 1cm">
						<span>受理时间:</span><%=cca.getAcceptTime() %>
						<span>受理人:</span>
						<%for(User u:alluser){ %>
							<%if(cca.getHandleUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }%>
						
					</li>
					<li><%if(cca.getRemark()!=null){ %>
						<span>核实内容:</span><%=cca.getRemark() %>
						<%} %>
					</li>
					<li>
						<%if(cca.getHeshiTime()!=null){ %>
						<span>核实时间:</span>
						<%=cca.getHeshiTime()%>
						<%} %>
						<%if(cca.getHeshiUser()!=null){ %>
						<span>核实人:</span>
						<%for(User u:alluser){ %>
							<%if(cca.getHeshiUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }}%>
						<%if(cca.getDownloadheshipath()!=null){ %>
						
						<span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadheshipath()%>">附件下载</a></span>
						<%} %> <!--cca.getHeshiTime()!=null&&cca.getHeshiUser()!=null&&cca.getRemark()!=null&&  -->
					</li>
					<%if(cca.getComplaintState()==ComplaintStateEnum.YiJieAn.getValue()&&cca.getJieanTime()!=null&&cca.getJieanUser()!=null){%>
					<li>					
					<span>结案处理结果:</span>
						<%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %>
						<span>结案时间:</span>
						<%=cca.getJieanTime() %>
						
					</li>
					<li>
					<span>结案人:</span>
					<%for(User u:alluser){ %>
							<%if(cca.getJieanUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }%>
					<span>结案备注:</span>
					<%=cca.getJieanremark() %>
					<%if(cca.getDownloadjieanpath()!=null){ %>
					<span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span>
					<%}%>
				</li>
				<%}else{%>
					<%if(cca.getComplaintState()!=ComplaintStateEnum.DaiHeShi.getValue()){ %>
					 <li>					
					<span>结案处理结果:</span>
						<%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %>
						<span>结案时间:</span>
						<%=cca.getAcceptTime()%>
						<span>结案人:</span>
					<%for(User u:alluser){ %>
							<%if(cca.getHandleUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }%>
					<%if(cca.getDownloadjieanpath()!=null){ %>						
					</li>
					<li>
					<span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span>
					<%}%>
				</li>
				<%} } %> 
				
					<!-- <li>
						<label>申诉内容*:</label>					
						<textarea  onkeyup="checkLen(this)" style="width: 60%;height: 118px;margin-left: 60px" name="shensuremark" id="shensuremark"></textarea>																	
						<div>您还可以输入<font id="count" color="red">150</font>个文字</div>
					</li> -->
					
				<input type="hidden" value="<%=ComplaintStateEnum.JieAnChongShenZhong.getValue()%>" name="complaintState" id="complaintState">
				<input type="hidden" value="<%=cca.getId()%>" name="id" id="id">
				<input type="hidden" value="<%=cca.getComplaintTime()%>" name="complaintTime">
				<input type="hidden" value="<%=cca.getShensuUser()%>" name="shensuUser">
			</div>	
			 		<table>
					 	<tr class="font_1"><td colspan="2" align="left" valign="top"><span>上传文件：</span>
					 <iframe id="update" name="update" src="workorder/update?fromAction=ShenSuChangeComplaintStateF&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" >
					 </iframe> </td></tr>
					 </table>
				
		
				 <div align="center">
						 <input type="submit" value="申诉" class="button" onclick="acceptcloseDiv()"onclick="AlreadyVerify('<%=ComplaintStateEnum.JieAnChongShenZhong.getValue()%>')"/>
						
					</div>
					<input type="hidden" value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>" id="dhsv"/>
					<input type="hidden" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>" id="yjav"/>
		</div>
	</form>	
</div>
<div id="box_yy"></div> --%>





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
<%@page import="cn.explink.domain.User"%>	
<%
List<User> alluser=request.getAttribute("alluser")==null?null:(List<User>)request.getAttribute("alluser");
CsComplaintAccept cca=request.getAttribute("cca")==null?null:(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=request.getAttribute("cci")==null?null:(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
String oneleave=request.getAttribute("OneLevel")==null?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==null?null:(String)request.getAttribute("TwoLevel");
String customerName=request.getAttribute("customerName")==null?null:(String)request.getAttribute("customerName");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>工单详情</h1>				
			<form id="ShenSuChangeComplaintStateF" enctype="multipart/form-data" method="post" onsubmit="if(IfShenSu())submitShenSu(this);return false;" action="<%=request.getContextPath()%>/workorder/ShenSuChangeComplaintStateFile;jsessionid=<%=session.getId()%>" >
				<div id="box_form">
					<table>
						<tr>
							<td align="right">工单号:</td><td><%=cca.getAcceptNo() %></td>
							<td align="right">工单状态:</td><td><%=ComplaintStateEnum.getByValue1(cca.getComplaintState()).getText() %></td>
							<td align="right">订单号:</td><td><%=cca.getOrderNo() %></td>
						</tr>
						<tr>
							
								<td align="right">一级类型:</td><td><%=oneleave%></td>
								<td align="right">二级类型:</td><td><%=twoleave%></td>
							
						</tr>	
						<tr>
							<%if(cca.getCodOrgId()!=-1){ %>
								<td align="right"><span>责任机构:</span></td>					
								<%for(Branch b:lb){ %>
								<%if(b.getBranchid()==cca.getCodOrgId()) {%>
										<td><%=b.getBranchname() %></td>
								<%} }%>
								<%if(cca.getComplaintUser()!=null){ %>
								<td align="right"><span>责任人:</span></td>					
								<%for(User u:alluser){ %>
									<%if(cca.getComplaintUser().equals(u.getUsername())){ %>
									<td><%=u.getRealname()%></td>
								<%} }}%>
								<%} %>
						</tr>
						<tr>
								<td align="right"><span>订单操作状态:</span></td><td><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%></td>
								<td align="right"><span>当前机构:</span></td><td><%=cca.getCurrentBranch() %></td>
								<td align="right"><span>客户名称:</span></td><td><%=customerName%></td>
						</tr>
						<tr>
								<%if(cci!=null){ %>
								<td align="right"><span>来电人姓名:</span></td><td><%=cci.getName()==null?"":cci.getName()%></td>
								<td align="right"><span>来电号码:</span></td><td><%=cci.getPhoneonOne()==null?"":cci.getPhoneonOne()%></td>
								<%}else{ %>
								<td align="right"><span>来电人姓名:</span><td>
								<td align="right"><span>来电号码:</span><td>
								<%} %>
								<td align="right"><span>收件人手机:</span></td>
								<td><%=co.getConsigneemobile() %></td>
						</tr>
							<tr>
								<td align="right"><span>工单内容:</span></td><td><%=cca.getContent()%></td>
							</tr>
							<tr>
								<td align="right"><span>受理时间:</span></td><td><%=cca.getAcceptTime() %></td>
								<td align="right"><span>受理人:</span></td>
								<%for(User u:alluser){ %>
									<%if(cca.getHandleUser().equals(u.getUsername())){ %>
										<td><%=u.getRealname()%></td>
								<%} }%>								
							</tr>
							<tr>
								<%if(cca.getRemark()!=null){ %>
								<td align="right"><span>核实内容:</span></td><td><%=cca.getRemark()%></td>
								<%} %>
							</tr>
							<tr>
								<%if(cca.getHeshiTime()!=null){ %>
								<td align="right"><span>核实时间:</span></td>
								<td><%=cca.getHeshiTime()%></td>
								<%} %>
								<%if(cca.getHeshiUser()!=null){ %>
								<td align="right"><span>核实人:</span></td>
								<%for(User u:alluser){ %>
									<%if(cca.getHeshiUser().equals(u.getUsername())){ %>
										<td><%=u.getRealname()%></td>
								<%} }}%>
								<%if(cca.getDownloadheshipath()!=null){ %>
								
								<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadheshipath()%>">附件下载</a></span></td>
								<%} %>
							</tr>
							<%if(cca.getComplaintState()==ComplaintStateEnum.YiJieAn.getValue()&&cca.getJieanTime()!=null&&cca.getJieanUser()!=null){%>
							<tr>					
								<td align="right"><span>结案处理结果:</span></td>
								<%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %>
								<td align="right"><span>结案时间:</span></td>
								<%=cca.getJieanTime() %>
								
							</tr>
							<tr>
								<td align="right"><span>结案人:</span></td>
								<%for(User u:alluser){ %>
										<%if(cca.getJieanUser().equals(u.getUsername())){ %>
											<td><%=u.getRealname()%></td>
									<%} }%>
								<td align="right"><span>结案备注:</span></td>
								<td><%=cca.getJieanremark() %></td>
								<%if(cca.getDownloadjieanpath()!=null){ %>
								<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span></td>
								<%}%>
							</tr>
						<%}else{%>
							<%if(cca.getComplaintState()!=ComplaintStateEnum.DaiHeShi.getValue()){ %>
							<tr>					
							<td align="right"><span>结案处理结果:</span></td>
								<td><%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %></td>
								<td align="right"><span>结案时间:</span></td>
								<td><%=cca.getAcceptTime()%></td>
								<td align="right"><span>结案人:</span></td>
							<%for(User u:alluser){ %>
									<%if(cca.getHandleUser().equals(u.getUsername())){ %>
										<td><%=u.getRealname()%></td>
								<%} }%>
							<%if(cca.getDownloadjieanpath()!=null){ %>						
							</tr>
							<tr>
							<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span></td>
							<%}%>
							</tr>
						<%} } %> 
							<input type="hidden" value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>" id="dhsv"/>
							<input type="hidden" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>" id="yjav"/>
						
				</table>
			</div>
		</form>	
	</div>
</div>
<div id="box_yy"></div>