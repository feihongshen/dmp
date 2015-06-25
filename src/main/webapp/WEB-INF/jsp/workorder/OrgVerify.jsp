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

%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>机构核实</h1>		
		
			<form enctype="multipart/form-data" id="ChangeComplaintStateF" name="ChangeComplaintStateF" onsubmit="if(heshiremarkV()){submitHeShi(this);} return false;"  method="post"  action="<%=request.getContextPath()%>/workorder/HeshiChangeComplaintStateFile;jsessionid=<%=session.getId()%>">
			<div id="box_form">
				<ul>
					<li>
					工单号:<%=cca.getAcceptNo() %>&nbsp;
					工单状态:<%=ComplaintStateEnum.getByValue(cca.getComplaintState()).getText() %>&nbsp;
					订单号:<%=cca.getOrderNo() %>&nbsp;
					</li>
					<li>
						<%-- 受理类型:<%=ComplaintTypeEnum.getByValue(cca.getComplaintType()).getText() %>&nbsp; --%>
						一级类型:<%=oneleave%>&nbsp;
						二级类型:<%=twoleave%>&nbsp;
					</li>
					<li>
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
						<span>收件人手机:</span><%=co.getConsigneemobile()%>
					</li>
					<li>
						<span>投诉内容:</span><%=cca.getContent()%>
					</li>
					<li>
						<span>受理时间:</span><%=cca.getAcceptTime() %>
					</li>
					<li>
						核实内容*:				
						<textarea onkeyup="checkLen(this)" style="width: 60%;height: 118px;margin-left: 60px" name="remark" id="remark"></textarea>																	
						<div>您还可以输入<font id="count" color="red">150</font>个文字</div>
					</li>
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
<div id="box_yy"></div>