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
long iskefu = request.getAttribute("iskefu")==null?0:Long.parseLong(request.getAttribute("iskefu").toString());

List<User> userList = (List<User>)request.getAttribute("userList");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");

HashMap<Long, String> branchMap = (HashMap<Long, String>)request.getAttribute("branchMap");

List<AbnormalWriteBack> abnormalWriteBackList= (List<AbnormalWriteBack>)request.getAttribute("abnormalWriteBackList");
String url=request.getContextPath()+"/abnormalOrder/getbranchusers";
String dutybranch=request.getAttribute("dutybranch").toString();
String username=request.getAttribute("username").toString();
String filepathsum=request.getAttribute("filepathsum").toString();
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>问题件处理</h1>
		<form method="post" id="form1" onSubmit="if(check_describeAndDutyInfo()){submitAbnormalHH(this);}return false;" action="<%=request.getContextPath()%>/abnormalOrder/SubmitHandleabnormal;jsessionid=<%=session.getId()%>" enctype="multipart/form-data">
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
							&nbsp;&nbsp;问题件描述：<strong><%=abnormalOrder.getDescribe() %></strong>
							&nbsp;&nbsp;附件下载:<%if(!filepathsum.equals("")){if(filepathsum.split(",").length>=1){ %>
							
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=filepathsum.split(",")[0] %>" style="color: blue;"><%=filepathsum.split(",")[0] %></a>&nbsp;&nbsp;<em id="morefilebutton"><a href="javascript:filedownloadwithquestionfile();" style="color: blue;">更多附件</a></em>
							<%} %>
							<%} %>
								<em id="morefiles"  hidden="hidden">
								<%if(filepathsum.split(",").length==2){ %>
								<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=filepathsum.split(",")[1] %>" style="color: blue;"><%=filepathsum.split(",")[1] %></a>
								<%}else{ %>
								<%for(int i=0;i<filepathsum.split(",").length;i++){ %>
								<%if(i>=1){ %>
									<a  href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=filepathsum.split(",")[i] %>" style="color: blue;"><%=filepathsum.split(",")[i] %></a>
								<%} %>
								<%} %>
								<%}%>
							</em>
							</td>
						
						</tr>
						
				<%if((dutybranch!=""&&username!="")||iskefu==0){ %>
				<tr  class="font_1">
				<td align="left" valign="top">
					责任机构初判:<strong><%=dutybranch %>  </strong>
					&nbsp;&nbsp;责任人初判:<strong><%=username %>  </strong>
				</td>
				</tr>
				
				
				<%}else{ %>
						<tr class="font_1" <%if(isfind==1){ %> style="display: none"  <%} %>>
						<td align="left" valign="top">责任机构初判:<select  id="dutybranchid" name="dutybranchid" onchange="selectbranchUsers('dutyname','dutybranchid');">
							<option value="0" selected="selected">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
					
						责任人初判:<select  id="dutyname" name="dutyname">
						<option value ='0' selected="selected">==请选择机构责任人==</option>
						</select>
						</td>
						</tr>
				<%} %>
						
						<tr>
							<td colspan="2" align="left" valign="top"><span class="font_1">回复内容：</span> 
								<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){if(abnormalOrder.getAbnormaltypeid()==at.getId()){ %><%=at.getName() %><%}} %>
								<%if(abnormalWriteBackList!=null&&abnormalWriteBackList.size()>0) %>
								<%=abnormalWriteBackList.get(abnormalWriteBackList.size()-1).getDescribe() %>
								<input name="" type="button" value="查看交流记录" id="showchatlist" onclick='$("#right_chatlist").show();$("#chatlist_alertbox").width(900);'/>
								</td>
						</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">处理内容：<textarea name="describe" id="describe" cols="40" rows="4" id="textfield" onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'"></textarea></td>
						</tr>
						 <tr class="font_1">
							<td colspan="2" align="left" valign="top"> 
								<!-- 上传附件:<input type="file" name="file" id="file"/> -->
								上传附件：<iframe id="update" name="update" src="abnormalorder/update?fromAction=form1&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>    
							</td>
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
			<input type="hidden" name="id" id="id" value="<%=abnormalOrder.getId() %>"/>
			<input type="hidden" name="cwb" id="cwb" value="<%=cwborder.getCwb() %>">
			<input type="hidden" value="0" id="isfind" name="isfind"/>
			<input type="hidden" value="" id="filepathurl" name="filepathurl"/>
			<input type="hidden" value="1" id="isonlineopen" name="isonlineopen"/>
			<input type="hidden" id="filepathsumsize" name="filepathsumsize" value="<%=filepathsum.split(",").length %>"/>
			
			<input type="hidden" name="getbranchusers" id="getbranchusers" value="<%=url %>">
			<div align="center">
		<%-- 		<% if(isfind==1){
					if(abnormalOrder.getIshandle()==AbnormalOrderHandleEnum.yichuli.getValue()){ } 
					else if(abnormalOrder.getIshandle()!=AbnormalOrderHandleEnum.yichuli.getValue()){
				%>
				<input type="submit" value="回复" class="button">
				<input type="reset"   value="取消" class="button">
				<%}}
				else{
				 if(abnormalOrder.getIshandle()==AbnormalOrderHandleEnum.yichuli.getValue()){ } 
				 else if(showabnomal.equals("1")) {%>
				<input type="submit" value="处理中" class="button">
				<input type="button" value="完成处理" class="button" onclick="yichuli1();">
				<input type="reset"   value="取消" class="button">
				<%} 
				else if(showabnomal.equals("0")) {%> --%>
				<input type="submit" value="完成处理" class="button">
				<input type="button" onclick="closeBox()"   value="取消" class="button">
				<!-- <input type="button" value="完成处理" class="button" onclick="yichuli1();"> -->
				<%-- <%} 
				else  { %>
				<input type="submit" value="处理" class="button">
				<input type="reset"   value="取消" class="button">
				<%}} %> --%>
			</div>
		</form>
		<form id="form2" action="<%=request.getContextPath()%>/abnormalOrder/SubmitOverabnormal/<%=abnormalOrder.getId() %>" method="post">
		<input type="hidden" name="cwb" value="<%=cwborder.getCwb() %>">
		<input type="hidden" name="describe2" id="describe2" >
		</form>
	</div>
</div>
