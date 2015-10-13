<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
 Complaint complaint = (Complaint)request.getAttribute("Complaint");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>投诉人详情</h1>
		<div id="box_form">
			<%if(complaint!=null){ %>
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr>
					<td><table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
							<tr>
								<td width="400"><table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
									<tr>
										<td width="33%" align="center" valign="middle" bgcolor="#eef6ff" >投诉人姓名:</td>
										<td align="center" valign="middle" ><%=complaint.getComplaintcontactman()%></td>
									</tr>
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >投诉人电话:</td>
										<td align="center" valign="middle" ><%=complaint.getComplaintphone()%></td>
									</tr>	
									<tr>
										<td align="center" valign="middle" bgcolor="#eef6ff" >投诉内容:</td>
										<td align="center" valign="middle" ><%=complaint.getComplaintcontent()%></td>
									</tr>
								</table></td>
							</tr>
							<%if(complaint.getComplaintcontent() !=null){ %>
							   <%if(complaint.getComplaintflag() == 0) {%>
								<tr>
								   <td width="400" align="center" valign="middle"><font color ="red"><h3>未处理，请联系客户</h3></font> </td>
								</tr>
								<%} %>
							<%} if(complaint.getComplaintflag() == 1) {%>
								<tr>
								   <td width="400" align="center" valign="middle"><font><h3>已处理</h3></font> </td>
								</tr>
							<%} %>
			           </table>
			        </td>
				</tr>
			</table>                                                                                                       
			<%}else{ %>
			<div style= "width:160px;height:30px;padding-top: 20px;">
			  <center>查无此单!</center>
			</div>
			<% }%>
			
		</div>
	</div>
</div>
<div id="box_yy"></div>
