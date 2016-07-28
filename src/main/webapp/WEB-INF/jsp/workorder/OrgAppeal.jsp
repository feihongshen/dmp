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
<%@page import="cn.explink.domain.CsShenSuChat"%>
<%
List<User> alluser=request.getAttribute("alluser")==null?null:(List<User>)request.getAttribute("alluser");
CsComplaintAccept cca=request.getAttribute("cca")==null?null:(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=request.getAttribute("cci")==null?null:(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
String oneleave=request.getAttribute("OneLevel")==null?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==null?null:(String)request.getAttribute("TwoLevel");
String customerName=request.getAttribute("customerName")==null?null:(String)request.getAttribute("customerName");
List<CsShenSuChat> cschatlist=request.getAttribute("cschatlist")==null?null:(List<CsShenSuChat>)request.getAttribute("cschatlist");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>工单详情</h1>
		<div id="box_form">				
			<table>
			<tr>
				<td>		
						<form id="ShenSuChangeComplaintStateF" enctype="multipart/form-data" method="post" onsubmit="if(IfShenSu())submitShenSu(this);return false;" action="<%=request.getContextPath()%>/workorder/ShenSuChangeComplaintStateFile;jsessionid=<%=session.getId()%>" >
					<table>
						<tr>
							<td align="right">工单号:</td><td><font color="croci"><%=cca.getAcceptNo() %></font></td>
							<td align="right">工单状态:</td><td><font color="croci"><%=ComplaintStateEnum.getByValue1(cca.getComplaintState()).getText() %></font></td>
							<td align="right">订单号:</td><td><font color="croci"><%=cca.getOrderNo() %></font></td>				
						</tr>
						<hr>
						<tr>
							
								<td align="right">一级类型:</td><td><font color="croci"><%=oneleave%></font></td>
								<td align="right">二级类型:</td><td><font color="croci"><%=twoleave%></font></td>
							
						</tr>
						
						<tr>
							<%if(cca.getCodOrgId()!=-1){ %>
								<td align="right"><span>责任机构:</span></td>					
								<%for(Branch b:lb){ %>
								<%if(b.getBranchid()==cca.getCodOrgId()) {%>
										<td><font color="croci"><%=b.getBranchname() %></font></td>
								<%} }%>
								<%if(cca.getComplaintUser()!=null){ %>
								<td align="right"><span>责任人:</span></td>					
								<%for(User u:alluser){ %>
									<%if(cca.getComplaintUser().equals(u.getUsername())){ %>
									<td><font color="croci"><%=u.getRealname()%></font></td>
								<%} }}%>
								<%} %>
						</tr>
						
						<tr>
								<td align="right"><span>订单操作状态:</span></td><td><font color="croci"><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%></font></td>
								<td align="right"><span>当前机构:</span></td><td><font color="croci"><%=cca.getCurrentBranch() %></font></td>
								<td align="right"><span>客户名称:</span></td><td><font color="croci"><%=customerName%></font></td>
						</tr>
					
						<tr>
								<%if(cci!=null){ %>
								<td align="right"><span>来电人姓名:</span></td><td><font color="croci"><%=cci.getName()==null?"":cci.getName()%></font></td>
								<td align="right"><span>来电号码:</span></td><td><font color="croci"><%=cci.getPhoneonOne()==null?"":cci.getPhoneonOne()%></font></td>
								<%}else{ %>
								<td align="right"><span>来电人姓名:</span><td>
								<td align="right"><span>来电号码:</span><td>
								<%} %>
								<td align="right"><span>收件人手机:</span></td>
								<td><font color="croci"><%=co.getConsigneemobile() %></font></td>
						</tr>
						
						
						<tr>
								<%if(cci!=null){ %>
								<td align="right"><span>联系邮箱:</span></td><td><font color="croci"><%=cci.getMailBox()==null?"":cci.getMailBox()%></font></td>
								<%}else{ %>
								<td align="right"><span>联系邮箱:</span><td>
								<%} %>
								<td align="right"><span>收件人:</span></td>
								<td><font color="croci"><%=co.getConsigneename() %></font></td>
								<td></td> 
								<td></td> 
						</tr>
						
						
						
						
						
						
						
						
							<tr>
								<td align="right"><span>工单内容:</span></td>
								<td><font color="croci"><textarea cols="10"><%=cca.getContent()%></textarea></font></td>
							</tr>
						
							<tr>
								<td align="right"><span>受理时间:</span></td><td><font color="croci"><%=cca.getAcceptTime() %></font></td>
								<td align="right"><span>受理人:</span></td>
								<%for(User u:alluser){ %>
									<%if(cca.getHandleUser().equals(u.getUsername())){ %>
										<td><font color="croci"><%=u.getRealname()%></font></td>
								<%} }%>								
							</tr>
							
							<tr>
								<%if(cca.getRemark()!=null){ %>
								<td align="right"><span>核实内容:</span></td>
								<td><font color="croci"><textarea cols="10"><%=cca.getRemark()%></textarea></font></td>
								<%} %>
							</tr>
						
							<tr>
								<%if(cca.getHeshiTime()!=null){ %>
								<td align="right"><span>核实时间:</span></td>
								<td><font color="croci"><%=cca.getHeshiTime()%></font></td>
								<%} %>
								<%if(cca.getHeshiUser()!=null){ %>
								<td align="right"><span>核实人:</span></td>
								<%for(User u:alluser){ %>
									<%if(cca.getHeshiUser().equals(u.getUsername())){ %>
										<td><font color="croci"><%=u.getRealname()%></font></td>
								<%} }}%>
								<%if(cca.getDownloadheshipath()!=null){ %>
								
								<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadheshipath()%>">附件下载</a></span></td>
								<%} %>
							</tr>
						
							<%if(cca.getComplaintState()==ComplaintStateEnum.YiJieAn.getValue()&&cca.getJieanTime()!=null&&cca.getJieanUser()!=null){%>
							<tr>					
								<td align="right"><span>结案处理结果:</span></td>
								<td><font color="croci"><%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText()%></font></td>
								<td align="right"><span>结案时间:</span></td>
								<td><font color="croci"><%=cca.getJieanTime() %></font></td>
								
							</tr>
							
							<tr>
								<td align="right"><span>结案人:</span></td>
								<%for(User u:alluser){ %>
										<%if(cca.getJieanUser().equals(u.getUsername())){ %>
											<td><font color="croci"><%=u.getRealname()%></font></td>
									<%} }%>
								<td align="right"><span>结案备注:</span></td>
								<td><font color="croci"><textarea cols="10"><%=cca.getJieanremark() %></textarea></font></td>
								<%if(cca.getDownloadjieanpath()!=null){ %>
								<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span></td>
								<%}%>
							</tr>
					
						<%}else{%>
							<%if(cca.getComplaintState()!=ComplaintStateEnum.DaiHeShi.getValue()){ %>
							<tr>					
								<td align="right"><span>结案处理结果:</span></td>
								<td><font color="croci"><%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %></font></td>
								<td align="right"><span>结案时间:</span></td>
								<td><font color="croci"><%=cca.getAcceptTime()%></font></td>
								<td align="right"><span>结案人:</span></td>
								<td><%for(User u:alluser){ %>
									<%if(cca.getHandleUser().equals(u.getUsername())){ %>
										<font color="croci"><%=u.getRealname()%></font>
								<%} }%></td>
							<%if(cca.getDownloadjieanpath()!=null){ %>						
							</tr>
							<tr>
							<td><span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadjieanpath()%>">附件下载</a></span></td>
							<%}%>
							</tr>
						<%} } %> 
						
							<input type="hidden" value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>" id="dhsv"/>
							<input type="hidden" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>" id="yjav"/>
						
		</form>
				</table>
					</td>
					<td valign="top">													
								<div id="chatcontent" hidden="true" class="chat_listtxt">	
								<table height=100% style=border-color:000000;border-left-style:solid;border-width:1px>
									<tr>
									<td valign=top>			
										<table style="color: orange;">
										
										<%if(cschatlist!=null){ %>
											<%for(CsShenSuChat cs:cschatlist){ %>
											<tr>
												<%for(User u:alluser){%>
													<%if(u.getUserid()==cs.getCreUser()){%>
													<td><%=u.getRealname()%></td>													
												<%} }%>
													
													<td><%=cs.getCreTime()%></td>
											</tr>
											<tr>
													<td colspan="2"><%=cs.getCreChatContent()%></td>
											</tr>
											<br>
											<%}} %>
											
												</table>
											</td>
										</tr>
									</table>
								</div>
							
							</td>
						
				</tr>	
			
				<tr>
					<td colspan="3" rowspan="3">
						<button onclick="showAndoffHuiFuChatWin()" class="input_button2">回复</button>
							<button onclick="showAndoffChatWin()" class="input_button2">交谈记录</button>	
											<div id="ifShowTiJaoContent" hidden="true">
											
													<form id="chatcontentForm">
																<textarea id="chattextareaid" cols="40" rows="4" name="chatcontenttext" onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'" maxlength="100" style="margin: 3px 0px 0px; width: 687px; height: 53px;"></textarea>
																<input id="chatacceptnoid" type="hidden" value="<%=cca.getAcceptNo()%>" name="acceptNo">
													</form>
												<input type="button" value="发送内容" onclick="submitchatcontent()" class="input_button2">
											</div>

		
							<input type="hidden" value="<%=request.getContextPath()%>/workorder/huiFuChat;jsessionid=<%=session.getId()%>" id="chatcontentFormUrl">
					</td>
				</tr>
						
		</table>
			
						
	
			</div>
	</div>
</div>
<div id="box_yy"></div>