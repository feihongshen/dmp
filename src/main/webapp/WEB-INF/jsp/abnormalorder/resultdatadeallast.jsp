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
String branchid=request.getAttribute("branchid")==null?"0":request.getAttribute("branchid").toString();
List<User> userList = (List<User>)request.getAttribute("userList");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerList");

HashMap<Long, String> branchMap = (HashMap<Long, String>)request.getAttribute("branchMap");

List<AbnormalWriteBack> abnormalWriteBackList= (List<AbnormalWriteBack>)request.getAttribute("abnormalWriteBackList");
String url=request.getContextPath()+"/abnormalOrder/getbranchusers";
String branchdutyuser=request.getAttribute("branchdutyuser")==null?"":request.getAttribute("branchdutyuser").toString();
String branchname=request.getAttribute("branchname")==null?"":request.getAttribute("branchname").toString();
String filepathsum=request.getAttribute("filepathsum").toString();
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>问题件处理详情</h1>
		<form  enctype="multipart/form-data" method="post" id="form1" onsubmit="if(check_dealJieanResulet())submitAbnormalLast(this);return false;" action="<%=request.getContextPath()%>/abnormalOrder/submitHandleabnormalResultAdd;jsessionid=<%=session.getId()%>" >
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
								<%if(i>1){ %>
									<a  href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=filepathsum.split(",")[i] %>" style="color: blue;"><%=filepathsum.split(",")[1] %></a>
								<%} %>
								<%} %>
								<%}%>
							</em>
							</td>
						
						</tr>
						<tr  class="font_1">
						<td align="left" valign="top">
						责任机构初判：<strong><%=branchname%></strong>
						&nbsp;&nbsp;
						责任人初判:<strong><%=branchdutyuser %></strong>
						</td></tr>
						<tr class="font_1">
							<td align="left" valign="top">
						处理结果：<select id="dealresult" name="dealresult">
								<option value="0">请选择处理结果</option>
								<option value="1">问题成立</option>
								<option value="2">问题不成立</option>
							</select>
							</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">责任机构确认:<select  id="dutybranchid" name="dutybranchid" onchange="selectbranchUsers('dutyname','dutybranchid');">
							<option value="0">请选择责任机构</option>
							<%if(branchList!=null){for(Branch branch:branchList){ %>
							<option value="<%=branch.getBranchid() %>"><%=branch.getBranchname() %></option>
							<% }}%>
						</select>
					
						责任人确认:<select  id="dutyname" name="dutyname">
						<option value ='0'>==请选择机构责任人==</option>
						</select>
						</td>
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
							<td colspan="2" align="left" valign="top">处理说明：<textarea  onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'" name="describe" id="describe" cols="40" rows="4" id="textfield">最多100个字</textarea></td>
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
			
			<input type="hidden" name="id" id="id" value="<%=abnormalOrder.getId() %> ">
			<input type="hidden" name="cwb" id="cwb" value="<%=cwborder.getCwb() %>">
			<input type="hidden" name="getbranchusers" id="getbranchusers" value="<%=url %>">
			<iframe name='post_frame' id="post_frame" style="display:none;" mce_style="display:none;"></iframe> 
			<div align="center">
			<input type="submit" value="处理" class="button" >
			<input type="button"  onclick="closeBox()"   value="取消" class="button">
			</div>
		</form>
		
	</div>
</div>
