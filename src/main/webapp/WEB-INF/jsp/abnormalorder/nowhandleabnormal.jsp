<%@page import="cn.explink.domain.AbnormalWriteBack"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
AbnormalOrder abnormalOrder = (AbnormalOrder)request.getAttribute("abnormalOrder");
CwbOrder cwborder = (CwbOrder)request.getAttribute("cwborder");
Role role = (Role)request.getAttribute("role");
String showabnomal = request.getAttribute("showabnomal").toString();
long isfind = request.getAttribute("isfind")==null?0:Long.parseLong(request.getAttribute("isfind").toString());

List<User> userList = (List<User>)request.getAttribute("userList");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");

HashMap<Long, String> branchMap = (HashMap<Long, String>)request.getAttribute("branchMap");

List<AbnormalWriteBack> abnormalWriteBackList= (List<AbnormalWriteBack>)request.getAttribute("abnormalWriteBackList");
  
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>问题件处理详情</h1>
		<form method="post" id="form1" onSubmit="if(check_describe()){submitSaveFormAndCloseBox(this);}return false;" action="<%=request.getContextPath()%>/abnormalOrder/SubmitHandleabnormal/<%=abnormalOrder.getId() %>">
			<table width="600" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">订单号：<strong><%=cwborder.getCwb() %></strong>
							&nbsp;&nbsp;供货商：<strong><%if(customerlist!=null||customerlist.size()>0)for(Customer c : customerlist){if(cwborder.getCustomerid()==c.getCustomerid()){ %><%=c.getCustomername() %><%}} %></strong>
							&nbsp;&nbsp;创建人：<strong><%if(userList!=null||userList.size()>0)for(User u : userList){if(abnormalOrder.getCreuserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %></strong>&nbsp;&nbsp;
							创建人所在机构：<strong><%if(branchList!=null||branchList.size()>0)for(Branch b : branchList){if(abnormalOrder.getBranchid()==b.getBranchid()){ %><%=b.getBranchname() %><%}} %></strong></td>
						</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">问题件类型：<strong><%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){if(abnormalOrder.getAbnormaltypeid()==at.getId()){ %><%=at.getName() %><%}} %></strong>
							&nbsp;&nbsp;问题件描述：<strong><%=abnormalOrder.getDescribe() %></strong></td>
						</tr>
						<tr>
							<td colspan="2" align="left" valign="top"><span class="font_1">回复内容：</span> 
								<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){if(abnormalOrder.getAbnormaltypeid()==at.getId()){ %><%=at.getName() %><%}} %>
								<%if(abnormalWriteBackList!=null&&abnormalWriteBackList.size()>0) %>
								<%=abnormalWriteBackList.get(abnormalWriteBackList.size()-1).getDescribe() %>
								<input name="" type="button" value="查看交流记录" id="showchatlist" onclick='$("#right_chatlist").show();$("#chatlist_alertbox").width(900);'/>
								</td>
						</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">处理内容：<textarea name="describe" id="describe" cols="40" rows="4" id="textfield"></textarea></td>
						</tr>
					</table>
					</td>
					<td valign="top">
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="right_chatlist" style="height:280px; display:none">
							<tr>
								<td><div class="chat_listbox">
									<div class="chat_listclose" onclick='$("#right_chatlist").hide();$("#chatlist_alertbox").width(600);'>></div>
									<div class="chat_listtxt">
									<%-- <%if(abnormalWriteBackList!=null&&abnormalWriteBackList.size()>0)for(AbnormalWriteBack aw : abnormalWriteBackList){ %>
									<%if(aw.getType()==AbnormalWriteBackEnum.ChuangJian.getValue()||aw.getType()==AbnormalWriteBackEnum.HuiFu.getValue()){ %>
										<p><%if(userList!=null||userList.size()>0)for(User u : userList){if(abnormalOrder.getCreuserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %>&nbsp;&nbsp;<%=aw.getCredatetime() %>：<%=aw.getDescribe() %></p>
									<%}else if(aw.getType()==AbnormalWriteBackEnum.ChuLi.getValue()){ %>
										<p>客服-<%if(userList!=null||userList.size()>0)for(User u : userList){if(aw.getCreuserid()==u.getUserid()){ %><%=u.getRealname() %><%}} %>&nbsp;&nbsp;<%=aw.getCredatetime() %>：<%=aw.getDescribe() %></p>
									<%} %>
									<%} %> --%>
									<%if(abnormalWriteBackList!=null&&abnormalWriteBackList.size()>0)
										for(AbnormalWriteBack aw : abnormalWriteBackList){ %>
									
										<p><%if(userList!=null||userList.size()>0)
											for(User u : userList){
												if(aw.getCreuserid()==u.getUserid())
												{ out.print(branchMap.get(u.getBranchid())+"-"+u.getRealname());%><%}
											} %>&nbsp;&nbsp;
												<%=aw.getCredatetime() %>：<%=aw.getDescribe() %></p>
									<%} %>
									</div>
								</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="cwb" value="<%=cwborder.getCwb() %>">
			<div align="center">
			
				<% if(isfind==1){
					if(abnormalOrder.getIshandle()==AbnormalOrderHandleEnum.yichuli.getValue()){ } 
					else if(abnormalOrder.getIshandle()!=AbnormalOrderHandleEnum.yichuli.getValue()){
				%>
				<input type="hidden" value="1" name="isfind"/>
				<input type="submit" value="回复" class="button">
				<%}}
				else{
				 if(abnormalOrder.getIshandle()==AbnormalOrderHandleEnum.yichuli.getValue()){ } 
				 else if(showabnomal.equals("1")) {%>
				<input type="submit" value="处理中" class="button">
				<input type="button" value="完成处理" class="button" onclick="yichuli1();">
				<%} 
				else if(showabnomal.equals("0")) {%>
				<input type="submit" value="完成处理" class="button">
				<!-- <input type="button" value="完成处理" class="button" onclick="yichuli1();"> -->
				<%} 
				else  { %>
				<input type="submit" value="处理" class="button">
				<%}} %>
			</div>
		</form>
		<form id="form2" action="<%=request.getContextPath()%>/abnormalOrder/SubmitOverabnormal/<%=abnormalOrder.getId() %>" method="post">
		<input type="hidden" name="cwb" value="<%=cwborder.getCwb() %>">
		<input type="hidden" name="describe2" id="describe2" >
		</form>
	</div>
</div>
