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
String oneleave=request.getAttribute("OneLevel")==""?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==""?null:(String)request.getAttribute("TwoLevel");

%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>机构核实</h1>		
		
			<form enctype="multipart/form-data" id="ChangeComplaintStateF" name="ChangeComplaintStateF" onsubmit="if(heshiremarkV()){submitHeShi(this);} return false;"  method="post"  action="<%=request.getContextPath()%>/workorder/HeshiChangeComplaintStateFile;jsessionid=<%=session.getId()%>">
			<div id="box_form">
				<ul>
					<tr>
					工单号:<%=cca.getAcceptNo() %>&nbsp;
					工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %>&nbsp;
					订单号:<%=cca.getOrderNo() %>&nbsp;
					</tr>
					<tr>
						受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %>&nbsp;
						一级类型:<%=oneleave%>&nbsp;
						二级类型:<%=twoleave%>&nbsp;
					</tr>
					<tr>
					<span>责任机构:</span>	
						<%for(Branch b:lb){ %>
						<%if(b.getBranchid()==cca.getCodOrgId()) {%>
								<%=b.getBranchname() %>
						<%} }%>
						
						<span>责任人:</span>
						<%for(User u:alluser){ %>
							<%if(cca.getComplaintUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }%>
					</tr>
					<tr>
						<span>订单操作状态:</span><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%>
						<span>当前机构:</span><%=cca.getCurrentBranch() %>
						<span>客户名称:</span><%=co.getConsigneename() %>
					</tr>
					<tr>
						<%if(cci!=null){ %>
						<span>来电人姓名:</span><%=cci.getName()==null?"":cci.getName() %>
						<span>来电号码:</span><%=cci.getPhoneonOne()==null?"":cci.getPhoneonOne()%>
						<%}else{ %>
						<span>来电人姓名:</span>
						<span>来电号码:</span>
						<%} %>
						<span>收件人手机:</span><%=co.getConsigneemobile()%>
					</tr>
					<tr>
						<span>投诉内容:</span><%=cca.getContent()%>
					</tr>
					<tr>
						<span>受理时间:</span><%=cca.getAcceptTime() %>
					</tr>
					<tr>
						核实内容*:				
						<textarea onkeyup="checkLen(this)" style="width: 60%;height: 118px;margin-left: 60px" name="remark" id="remark"></textarea>																	
						<div>您还可以输入<font id="count" color="red">150</font>个文字</div>
					</tr>
						<input type="hidden" value="<%=ComplaintStateEnum.YiHeShi.getValue()%>" name="complaintState" id="complaintState">
						<input type="hidden" value="<%=cca.getId()%>" name="id" id="id">

					 </ul> 
					 <table>
					 	<tr class="font_1"><td colspan="2" align="left" valign="top"><span>上传文件：</span>
					 <iframe id="update" name="update" src="workorder/update?fromAction=ChangeComplaintStateF&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" >
					 </iframe> </td></tr>
					 </table>
			</div>
					<div align="center" >
				 	<input type="submit" value="核实提交" class="button" />
				
				 </div>	 	
		</div>	
		
	</form>	
</div>
<div id="box_yy"></div> --%>




<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%-- <%@page import="cn.explink.enumutil.ComplaintTypeEnum"%> --%>
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
String oneleave=request.getAttribute("OneLevel")==""?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==""?null:(String)request.getAttribute("TwoLevel");
String customerName=request.getAttribute("customerName")==null?null:(String)request.getAttribute("customerName");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>机构核实</h1>		
		
			<form enctype="multipart/form-data" id="ChangeComplaintStateF" name="ChangeComplaintStateF" onsubmit="if(heshiremarkV()){submitHeShi(this);} return false;"  method="post"  action="<%=request.getContextPath()%>/workorder/HeshiChangeComplaintStateFile;jsessionid=<%=session.getId()%>">
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
						<td align="right"><span>来电人姓名:</span></td><td><%=cci.getName()==null?"":cci.getName() %></td>
						<td align="right"><span>来电号码:</span></td><td><%=cci.getPhoneonOne()==null?"":cci.getPhoneonOne()%></td>
						<%}else{ %>
						<td align="right"><span>来电人姓名:</span></td>
						<td align="right"><span>来电号码:</span></td>
						<%} %>
						<td align="right"><span>收件人手机:</span></td><td><%=co.getConsigneemobile()%></td>
					</tr>
					<tr>
						<td align="right"><span>工单内容:</span></td><td><%=cca.getContent()%></td>
					</tr>
					<tr>
						<td align="right"><span>受理时间:</span></td><td><%=cca.getAcceptTime()%></td>
						<td align="right"><span>受理人:</span></td>
								<%for(User u:alluser){ %>
									<%if(cca.getHandleUser().equals(u.getUsername())){ %>
										<td><%=u.getRealname()%></td>
								<%} }%>
					</tr>
					<tr>
						<td align="right">核实内容*:</td>				
						<td><textarea onkeyup="checkLen(this)" style="width: 100%;" rows="10" cols="7" name="remark" id="remark"></textarea></td>																		
					</tr>
					<tr><td>您还可以输入<font id="count" color="red">150</font>个文字</td></tr>
					 </table> 
					 <table>
					 	<tr class="font_1"><td colspan="2" align="left" valign="top"><span>上传文件：</span>
					 <iframe id="update" name="update" src="workorder/update?fromAction=ChangeComplaintStateF&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" >
					 </iframe> </td></tr>
					 </table>
				</div>
					<div align="center" >
				 	<input type="submit" value="核实提交" class="button" />				
				 </div>	
				 		<input type="hidden" value="<%=ComplaintStateEnum.YiHeShi.getValue()%>" name="complaintState" id="complaintState">
						<input type="hidden" value="<%=cca.getId()%>" name="id" id="id">
				  		
			</form>	
		</div>			
	</div>
<div id="box_yy"></div>