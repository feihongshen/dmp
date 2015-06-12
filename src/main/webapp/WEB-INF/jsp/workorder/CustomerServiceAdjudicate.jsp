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
CsComplaintAccept cca=request.getAttribute("cca")==null?null:(CsComplaintAccept)request.getAttribute("cca");
CwbOrder co=request.getAttribute("co")==null?null:(CwbOrder)request.getAttribute("co");
CsConsigneeInfo cci=request.getAttribute("cci")==null?null:(CsConsigneeInfo)request.getAttribute("cci");
List<Branch> lb = request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
String oneleave=request.getAttribute("OneLevel")==null?null:(String)request.getAttribute("OneLevel");
String twoleave=request.getAttribute("TwoLevel")==null?null:(String)request.getAttribute("TwoLevel");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>客服结案</h1>		<%-- action="<%=request.getContextPath()%>/workorder/ChangeComplaintState" --%>
	
			<form  id="CustomerServiceChangeComplaintStateF" enctype="multipart/form-data" method="post"  action="<%=request.getContextPath()%>/workorder/JieAnChangeComplaintStateFile;jsessionid=<%=session.getId()%>" onsubmit="if(jieanremarkV()){submitJieAn(this);} return false;">
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
						<span>核实人:</span><%=cca.getHeshiUser()%>
						<span><a href="<%=request.getContextPath()%>/workorder/download?filepathurl=<%=cca.getDownloadheshipath()%>">附件</a></span>
					</li>
					<li>
							<span>处理结果:</span>
							<select class="select1" name="complaintResult" id="acceptresult" onblur="decideV()">
								<option value="<%=ComplaintResultEnum.WeiChuLi.getValue()%>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.ChengLi.getValue()%>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.BuChengLi.getValue()%>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>								
							</select>
					</li>
					<li>
							<label>备注*:</label>									
								<textarea style="width: 60%;height: 118px;margin-left: 60px" name="jieanremark" id="jieanremark"></textarea>																	
					</li>
					<li>
					
				</li>
						<input type="hidden" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>" name="complaintState" id="complaintState" />
						<input type="hidden" value="<%=cca.getId()%>" name="id" id="id"/>
						<%-- <input type="hidden" value="<%=cca.getJieanTime()%>" name="jieanTime">
						<input type="hidden" value="<%=cca.getJieanUser()%>" name="jieanUser"> --%>
				</ul>
				 <table>
					 	<tr class="font_1"><td colspan="2" align="left" valign="top"><span>上传声音文件：</span>
					 <iframe id="update" name="update" src="workorder/update?fromAction=CustomerServiceChangeComplaintStateF&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" >
					 </iframe> </td></tr>
					 </table>
				
			</div>
						
				<div align="center">
						<input type="submit" value="结案" class="button" <%-- onclick="AlreadyVerifyJieAn('<%=ComplaintStateEnum.YiJieAn.getValue()%>')" --%>/>						
				</div>		
				<input type="hidden" value="<%=ComplaintStateEnum.YiJieShu.getValue()%>" id="acceptresultV">	
				<input type="hidden" value="<%=ComplaintResultEnum.BuChengLi.getValue()%>" id="acceptresultVl"> 
	</div>
		</form>
</div>
<div id="box_yy"></div>