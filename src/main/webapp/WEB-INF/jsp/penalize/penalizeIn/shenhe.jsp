<%@page import="cn.explink.controller.PenalizeInsideView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.*"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
PenalizeInsideView penalizeInsideview=(PenalizeInsideView)request.getAttribute("penPunishinsideView");
String chuangjianfilepath=request.getAttribute("chuangjianfilepath").toString();
String shensuposition=request.getAttribute("shensuposition").toString();
String id=request.getAttribute("id").toString();
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>审核处理详情</h1>
		<form  enctype="multipart/form-data" method="post" id="form1"  action="<%=request.getContextPath()%>/inpunish/submitHandleShenheResultAddfile;jsessionid=<%=session.getId()%>" >
			<table width="600" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox">
				<tr>
					<td width="600" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="10" class="table_2" style="height:280px">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">扣罚单号：<strong><%=penalizeInsideview.getPunishno() %></strong>
							&nbsp;&nbsp;来源：<strong><%=penalizeInsideview.getCreatesourcename()%></strong>
							&nbsp;&nbsp;来源单号：<strong><%=penalizeInsideview.getSourceNo()%></strong>&nbsp;&nbsp;
						</td>
						</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">订单号：<strong><%=penalizeInsideview.getCwb() %></strong>
							&nbsp;&nbsp;订单状态：<strong><%=penalizeInsideview.getFlowordertypename() %></strong>
								&nbsp;&nbsp;责任机构:<strong><%=penalizeInsideview.getDutybranchname() %></strong>
							</td>
						
						</tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">扣罚大类：<strong><%=penalizeInsideview.getPunishbigsort() %></strong>
							&nbsp;&nbsp;扣罚小类：<strong><%=penalizeInsideview.getPunishsmallsort() %></strong>
								&nbsp;&nbsp;责任人:<strong><%=penalizeInsideview.getDutyperson() %></strong>
							</td>
						</tr>
							<tr class="font_1">
							<td  align="left" valign="top">订单金额：<strong><%=penalizeInsideview.getCwbprice()%></strong>
							&nbsp;&nbsp;扣罚金额：<strong><%=penalizeInsideview.getPunishInsideprice() %></strong>
							</td>
						</tr>
							<tr class="font_1">
							<td  align="left" valign="top">扣罚说明：<strong><%=penalizeInsideview.getPunishdescribe()%></strong>
							
								&nbsp;&nbsp;附件下载:<%if(!chuangjianfilepath.equals("")){if(chuangjianfilepath.split(",").length>=1){ %>
							
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=chuangjianfilepath.split(",")[0] %>" style="color: blue;">创建附件<%=chuangjianfilepath.split(",")[0] %></a><!-- &nbsp;&nbsp;<em id="morefilebutton"><a href="javascript:filedownloadwithquestionfile();" style="color: blue;">更多附件</a></em> -->
							<%} %>
							<%} %>
								<%-- <em id="morefiles"  hidden="hidden">
								<%if(chuangjianfilepath.split(",").length==2){ %>
								<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=chuangjianfilepath.split(",")[1] %>" style="color: blue;"><%=chuangjianfilepath.split(",")[1] %></a>
								<%}else{ %>
								<%for(int i=0;i<shensuposition.split(",").length;i++){ %>
								<%if(i>1){ %>
									<a  href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=chuangjianfilepath.split(",")[i] %>" style="color: blue;"><%=chuangjianfilepath.split(",")[1] %></a>
								<%} %>
								<%} %>
								<%}%>
							</em> --%>
							
							</td>
						</tr>
							<tr class="font_1">
							<td colspan="2" align="left" valign="top">申诉类型：<strong><%=penalizeInsideview.getShensutype()%></strong>
							&nbsp;&nbsp;申诉人：<strong><%=penalizeInsideview.getShensuUsername() %></strong>
							</td>
						</tr>
							<tr class="font_1">
							<td  align="left" valign="top">申诉说明：<strong><%=penalizeInsideview.getShensudescribe() %></strong>
							&nbsp;&nbsp;附件下载:<%if(!shensuposition.equals("")){if(shensuposition.split(",").length>=1){ %>
							
								&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=shensuposition.split(",")[0] %>" style="color: blue;">申诉附件<%=shensuposition.split(",")[0] %></a><!-- &nbsp;&nbsp;<em id="morefilebutton"><a href="javascript:filedownloadwithquestionfile();" style="color: blue;">更多附件</a></em> -->
							<%} %>
							<%} %>
						<%-- 		<em id="morefiles"  hidden="hidden">
								<%if(shensuposition.split(",").length==2){ %>
								<a href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=shensuposition.split(",")[1] %>" style="color: blue;"><%=shensuposition.split(",")[1] %></a>
								<%}else{ %>
								<%for(int i=0;i<shensuposition.split(",").length;i++){ %>
								<%if(i>1){ %>
									<a  href="<%=request.getContextPath()%>/abnormalOrder/download?filepathurl=<%=shensuposition.split(",")[i] %>" style="color: blue;"><%=shensuposition.split(",")[1] %></a>
								<%} %>
								<%} %>
								<%}%>
							</em> --%>
							</td>
						</tr>
						<tr  class="font_1">
						<td align="left" valign="top">
						扣罚金额<font color="red">*</font>:<input type="text" id="koufajine" name="koufajine"/>
						</td></tr>
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">审核备注<font color="red">*</font>：<textarea  onfocus="if(this.value == '最多100个字') this.value = ''" onblur="if(this.value == '') this.value = '最多100个字'" name="describe" id="describe" cols="40" rows="4" id="textfield">最多100个字</textarea></td>
						</tr>
						 <tr class="font_1">
							<td colspan="2" align="left" valign="top"> 
								<!-- 上传附件:<input type="file" name="file" id="file"/> -->
								上传附件：<iframe id="update" name="update" src="abnormalorder/update?fromAction=form1&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>    
							</td>
						</tr>
				
					</table>
					</td>
				</tr>
			</table>
			
			<input type="hidden" name="id" id="id" value="<%=id %> ">
			<input type="hidden" name="shenheresult" id="shenheresult" value="">
			<iframe name='post_frame' id="post_frame" style="display:none;" mce_style="display:none;"></iframe> 
			<div align="center">
			<input type="button" onclick="koufachengli();" value="扣罚成立" class="button" >
			<input type="button" onclick="chexiaokoufa();" value="撤销扣罚" class="button" >
			<input type="button"  onclick="closeBox()"   value="取消" class="button">
			</div>
		</form>
		
	</div>
</div>
