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
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>结案重审</h1>		
			
			<form id="JieAnChongShenChangeComplaintStateF" enctype="multipart/form-data" method="post" onsubmit="if(JieAnChongShen())submitJieAnChongShen(this);return false;" action="<%=request.getContextPath()%>/workorder/JieAnChongShenChangeComplaintStateFile;jsessionid=<%=session.getId()%>" >	
				<div id="box_form">
					<ul>
					<li>
					工单号:<%=cca.getAcceptNo() %>
					工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %>
					订单号:<%=cca.getOrderNo() %>
					</li>
					<li>
						受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %>
						一级类型:<%=oneleave%>
						二级类型:<%=twoleave%>
					</li>
					<li>
						<span>被投诉机构:</span>
						<%for(Branch b:lb){ %>
						<%if(b.getBranchid()==cca.getCodOrgId()) {%>
								<%=b.getBranchname() %>
						<%} }%>
						
						<span>被投诉人:</span><%= cca.getComplaintUser()%>
					</li>
					<li>
						<span>订单操作状态:</span><%=FlowOrderTypeEnum.getText(co.getFlowordertype()).getText()%>
						<span>当前机构:</span><%=cca.getCurrentBranch() %>
						<span>客户名称:</span><%=co.getConsigneename() %>
					</li>
					<li>
						<span>来电人姓名:</span><%=cci.getName() %>
						<span>来电号码:</span><%=cci.getPhoneonOne() %>
						<span>收件人手机:</span><%=co.getConsigneemobile() %>
					</li>
					<li>
						<span>投诉内容:</span><%=cca.getContent()%>
					</li>
					<li>
						<span>受理时间:</span><%=cca.getAcceptTime() %>
						<span>受理人:</span><%=cca.getHandleUser() %>
					</li>
					<li>
						<span>核实内容:</span><%=cca.getRemark() %>
					</li>
					<li>
						<span>核实时间:</span><%=cca.getHeshiTime() %>
						<span>核实人:</span><%=cca.getHeshiUser() %>
						<span>附件</span>
					</li>
					
					<li>
							<span>处理结果:</span>
							<label><%=ComplaintResultEnum.getByValue(cca.getComplaintResult()).getText() %></label>
							<span>结案人:</span><%=cca.getJieanUser() %>
							<span>结案时间:</span><%=cca.getJieanTime()%>
								
					</li>
						<li>
								<span>结案备注:</span><%=cca.getJieanremark() %>
						</li>
						<li>
								<span>申诉内容:</span><%=cca.getShensuremark() %>
						</li>
						<li>
								<span>申诉时间:</span><%=cca.getComplaintTime() %>
								<span>申诉人:</span><%=cca.getShensuUser() %>
						</li>
						<li>
								<span>结案重审*:</span>
									<select class="select1" name="complaintResult">
										<option value="<%=ComplaintResultEnum.ChengLi.getValue() %>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
										<option value="<%=ComplaintResultEnum.BuChengLi.getValue() %>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>
										<option value="<%=ComplaintResultEnum.WeiChuLi.getValue() %>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
									</select>
								
						</li>
				
<hr>			
				<div>					
					<span>结案重审备注:</span>				
					<textarea style="width: 60%;height: 118px;margin-left: 60px" name="jieanchongshenremark" id="jieanchongshenremark"></textarea>
					<input type="hidden" name="complaintState" id="complaintState" value="<%=ComplaintStateEnum.YiJieShu.getValue()%>"/>
					<input type="hidden" value="<%=cca.getId()%>" name="id" id="id"/>
																								
				</div> 	
				
					<table>
					 	<tr class="font_1"><td colspan="2" align="left" valign="top"><span>上传文件：</span>
					 <iframe id="update" name="update" src="workorder/update?fromAction=JieAnChongShenChangeComplaintStateF&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" >
					 </iframe> </td></tr>
					 </table>
			</div> 	
					
		
				<div align="center"><font color="red"><input type="submit" value="结案重审" class="button" <%-- onclick="AlreadyVerify('<%=ComplaintStateEnum.YiJieShu.getValue()%>')" --%>/></font>
			 					
			 	</div>
		</div>
	</form>	
</div>
<div id="box_yy"></div>