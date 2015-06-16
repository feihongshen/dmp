<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%
CsComplaintAccept a= (CsComplaintAccept)request.getAttribute("cca");
%>



<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建查询工单</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/saveWorkOrderQueryF" id="WorkOrderQueryForm">
				<table id="tb01">					
					<tr>
						<td>受理类型:<span style="color:orange;">订单查询</span>
								<input type="hidden" name="complaintType" value="<%=ComplaintTypeEnum.DingDanChaXun.getValue()%>">
						</td>
					<td>工单号:<input type="text" value="<%=a.getAcceptNo()%>" disabled="disabled"/>
						<input type="hidden" value="<%=a.getAcceptNo()%>" id="transn" name="acceptNo"/>
					</td>
						<td>订单号:<input type="text" value="<%=a.getOrderNo()%>" disabled="disabled"/>
							<input type="hidden" value="<%=a.getOrderNo()%>" id="cwbn" name="orderNo" />
						</td>
					</tr>
					<%for(CwbStateEnum c:CwbStateEnum.values()) {%>
						<%if(c.getValue()==a.getCwbstate()) {%>								
					<tr><td>订单状态:<input type="text" value="<%=c.getText()%>" id="csn" disabled="disabled"/>
						<input type="hidden" value="<%=a.getCwbstate()%>" name="cwbstate"/>
					</td>	
					<%}} %>
					<%for(CwbFlowOrderTypeEnum cf : CwbFlowOrderTypeEnum.values()){ %>
					<%if(cf.getValue()==a.getFlowordertype()) {%>
					<td>订单操作状态:<input type="text" value="<%=cf.getText()%>" id="cwsn" disabled="disabled"/>
					<input type="hidden" value="<%=a.getFlowordertype()%>"  name="flowordertype" >
					</td>
						<%}} %>
					<td>
					当前机构:<input type="text" value="<%=a.getCurrentBranch()%>" disabled="disabled"/>	
							<input type="hidden" value="<%=a.getCurrentBranch()%>" id="no" name="currentBranch"/>				
					</td>
					</tr>
					<tr>
						<td>
							查询内容:<textarea onkeyup="checkLen(this)" style="width: 100%;height: 150px;margin-left: 50px" name="queryContent"></textarea>
							<div>您最多可以输入<span id="count">150</span>个文字</div> 
						</td>
				</tr>
													
				</table>
				<input type="hidden"  name="complaintState" id="cls">
				<input type="hidden"  name="phoneOne" value="<%=a.getPhoneOne()%>">
				<input type="hidden"  name="provence" value="<%=a.getProvence()%>">
				<input type="hidden"  name="cuijianNum" value="0">
				
			
			</form>	
			
				 <div align="center"><!-- <button class="button">发送催件短信</button> -->
						 <button class="button" id="btnnn" onclick="btnswd('<%=ComplaintStateEnum.DaiChuLi.getValue()%>')">保存待处理</button>
						 <button class="button" onclick="btnswd('<%=ComplaintStateEnum.YiJieShu.getValue()%>')" id="btnnnnn">结案</button>
						 <button class="button" onclick="closeBox()">取消</button>
				 </div>
		</div>
		
	</div>
</div>
<div id="box_yy"></div>
 
