<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.*"%>

<%
Complaint com  = request.getAttribute("complaint")==null?null:(Complaint)request.getAttribute("complaint");
CwbOrderView order  = request.getAttribute("cwbOrderView")==null?null:(CwbOrderView)request.getAttribute("cwbOrderView");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchList");
List<User> userList = request.getAttribute("userList")==null?null:( List<User>)request.getAttribute("userList");
%>

	<div id="box_bg" ></div>
	<div id="box_contant" >
		<div id="box_top_bg"></div>
		<div id="box_in_bg">
			<h1>
				<div id="close_box" onclick="closeBox()"></div>
				投诉录入-客服备注</h1>
			<form method="post" id="complaint_cuijian_Form" name="complaint_cre_Form" onSubmit="if(check_complaint_create()){submitCreateFormAndCloseBox(this);}return false;" action="<%=request.getContextPath()%>/complaint/create"  >
				<table width="800" border="0" cellspacing="1" cellpadding="10" class="table_2">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">
							<input type="hidden" value="0" name="smschack" id="smsid" />
							<input type="hidden" value="<%=ComplaintTypeEnum.KefuBeizhu.getValue() %>" name="type" id="smsid" />
							<input type="hidden" value="<%=order.getCwb() %>" name="cwb"/>
							订单号：<strong><%=order.getCwb() %></strong>
							&nbsp;&nbsp;当前状态：<strong><%=order.getFlowordertypeMethod()  %></strong>
							&nbsp;&nbsp;当前站点：<strong><%=order.getCurrentbranchname() %></strong>
							&nbsp;&nbsp;小件员：<strong><%=order.getDelivername() %></strong>
							&nbsp;&nbsp;收件人电话：<strong><%=order.getConsigneemobile() %>/<%=order.getConsigneephone() %></strong>
							</td>
					</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">入库时间：<strong><%=order.getInstoreroomtime() %></strong>&nbsp;&nbsp;到货时间：<strong><%=order.getInSitetime() %></strong></td>
					</tr>
					
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">
							<%if(com != null&&com.getId()!=0 ){ %>
							<input type="hidden" value="<%=com.getId() %>" name="id"/>
							<%} %>
							投诉机构：
							<select name ="branchid" id ="cbranchid" onchange="updateBranchDeliver('<%=request.getContextPath()%>');">
					          <option value ="-1">请选择</option>
					          <%if(branchlist!=null && branchlist.size()>0){ %>
					          <%for(Branch c : branchlist){ %>
					          <option value =<%=c.getBranchid() %> 
					          <%if(c.getBranchid()==order.getCurrentbranchid()){ %>selected="selected" <%} %>><%=c.getBranchname()%></option>
					          <%} }%>
					          
						   </select>
								投诉对象：
								<select id="cdeliveryid" name ="deliveryid" >
							    <option value ="-1">请选择</option>
							     <%if(userList != null && userList.size()>0){ %> 
							          <%for(User c : userList){ %>
								 <option value =<%=c.getUserid() %> 
							           <%if(c.getUserid()== order.getDeliverid()){ %>selected="selected" <%} %> ><%=c.getRealname()%></option>
							          <%}
							          }%>
								</select>
							<%if(com != null&&com.getCreateTime()!=null ){ %>
							在<%=com.getCreateTime() %>已经创建过客服备注
							<%} %>
							</td>
						</tr>
						<tr class="font_1">
							<td width="100" valign="top">客户要求：</td>
							<td align="left"><textarea name="content" cols="80" rows="5" id="contentid"><%if(com != null&&com.getContent()!=null ){ %><%=com.getContent() %><%} %></textarea></td>
						</tr>
					
	</table>
				<div align="center">
					<input type="button" value="保存" class="button" onclick="updateComplaintFrom(2);">
					<input type="button" value="取消" class="button" onclick="closeBox()">
	</div>
			</form>
		</div>
	</div>
	<div id="box_yy"></div>
