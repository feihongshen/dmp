<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%

String cwb=request.getAttribute("cwb").toString();
String describeinfo=request.getAttribute("describeinfo").toString();
String filepathsum=request.getAttribute("filepathsum").toString();
%>

<div id="box_bg"></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg" >
		<h1><div id="close_box"  onclick="closeBox()" ></div>丢失件找回详情</h1>
		<div style="height:150px;overflow-y:auto;overflow-x:auto">
		<form  enctype="multipart/form-data" method="post" id="form1"  action="<%=request.getContextPath()%>/abnormalOrder/submitHandleabnormalResult" >
					<td><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:150px">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">订单号：<strong><%=cwb%></strong>
							</td>
						</tr>
						<tr class="font_1">
						<td align="left" valign="top">
						找回说明：<strong><%=describeinfo %></strong>
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
			<input type="hidden" id="filepathsumsize" name="filepathsumsize" value="<%=filepathsum.split(",").length %>"/>
		</form>
		</div>
	</div>
</div>