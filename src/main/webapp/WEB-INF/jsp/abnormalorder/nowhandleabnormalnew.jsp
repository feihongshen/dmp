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
String url=request.getContextPath()+"/abnormalOrder/getbranchusers";
String username=request.getAttribute("username").toString();
String createname=request.getAttribute("createname").toString();
String jieanperson=request.getAttribute("jieanperson").toString();
String jieanbranch=request.getAttribute("jieanbranch").toString();
String lastdutybranch=request.getAttribute("lastdutybranch").toString();
String lastdutyperson=request.getAttribute("lastdutyperson").toString();
String jieantime=request.getAttribute("jieantime").toString();
String jieandescribe=request.getAttribute("jieandescribe").toString();
String dutybranch=request.getAttribute("dutybranch").toString();
String filepathsum=request.getAttribute("filepathsum").toString();
%>

<div id="box_bg"></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" >
		<h1><div id="close_box"  onclick="closeBox()" ></div>问题件处理详情</h1>
		<div style="height:300px;overflow-y:auto;overflow-x:auto">
		<form  enctype="multipart/form-data" method="post" id="form1"  action="<%=request.getContextPath()%>/abnormalOrder/submitHandleabnormalResult" target="post_frame">
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
							
							</td>
					
						</tr>
						<tr  class="font_1">
						<td align="left" valign="top">
						责任机构初判：<strong><%=dutybranch%></strong>
						&nbsp;&nbsp;
						责任人初判:<strong><%=username %></strong>
						&nbsp;&nbsp;
						客服处理人：<strong><%=username %></strong>
						&nbsp;&nbsp;
						客服处理意见：<strong><%=username %></strong>
						</td>
						</tr>
						<tr  class="font_1">
						<td align="left" valign="top">
						反馈人：<strong><%=createname %></strong>
						&nbsp;&nbsp;
						反馈时间：<strong><%=abnormalOrder.getCredatetime() %></strong>
						&nbsp;&nbsp;
						反馈内容：<strong><%=abnormalOrder.getDescribe() %></strong>
						</td>
						</tr>
						<tr class="font_1">
							<td align="left" valign="top">
								处理结果:<strong><%=abnormalOrder.getDealresult()=="1"?"问题成立":"问题不成立"%></strong>
							</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
							责任机构确认：<strong><%=lastdutybranch %></strong>
						&nbsp;&nbsp;
						责任人确认:<strong><%=lastdutyperson %></strong>
						&nbsp;&nbsp;
						结案人:<strong><%=jieanperson %></strong>
						&nbsp;&nbsp;
						结案时间:<strong><%=jieantime %></strong>
					&nbsp;&nbsp;
						结案说明:<strong><%=jieandescribe %></strong>
						</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
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
					</table>
					</td>
			</div>
		</form>
		</div>
	</div>
</div>
