<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@page import="cn.explink.domain.*"%>

<%
Complaint complaint=(Complaint)request.getAttribute("complaint");
%>
	<div id="box_bg" ></div>
	<div id="box_contant" >
		<div id="box_top_bg"></div>
		<div id="box_in_bg">
			<h1>
				<div id="close_box" onclick="closeBox()"></div>
				催单与投诉-处理回复</h1>
			<form method="post" action="<%=request.getContextPath()%>/complaint/updateZD/<%=complaint.getId() %> " >
				<table width="800" border="0" cellspacing="1" cellpadding="10" class="table_2">
						<tr class="font_1">
							<td colspan="2" align="left" valign="top">

							订单号：<strong><%=complaint.getCwb() %></strong>

					</tr>
						
						<tr class="font_1">
							<td width="100" valign="top">处理回复：</td>
							<td align="left"><textarea name="replyDetail" cols="80" rows="5" id="replyDetail"></textarea></td>
						</tr>
	</table>
				<div align="center">
					<input type="button" value="保存" class="button" onclick="setFlag('<%=request.getContextPath()%>/complaint/updateZD/<%=complaint.getId() %>')" />
					<input type="button" value="取消" class="button" onclick="closeBox()"/>
	</div>
			</form>
		</div>
	</div>
	<div id="box_yy"></div>
