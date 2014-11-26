<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.ComplaintView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%
ComplaintView c  = (ComplaintView)request.getAttribute("complaintView");
%>

	<div id="box_bg" ></div>
	<div id="box_contant" >
		<div id="box_top_bg"></div>
		<div id="box_in_bg">
			<h1>
				<div id="close_box" onclick="closeBox()"></div>
				投诉详情</h1>
			<form id="complaint_update_Form" name="complaint_update_Form" onSubmit="if(check_complaint_update()){submitCreateFormAndCloseBox(this);}return false;" action="<%=request.getContextPath()%>/complaint/update"  method="post" >
				<input type="hidden" name="id" value="<%=c.getId() %>" />
				<table width="800" border="0" cellspacing="1" cellpadding="10" class="table_2">
						<tr >
							<td colspan="2" align="left" valign="top">订单号：<strong><%=c.getCwb() %></strong>
							&nbsp;&nbsp;当前状态：<strong><%=FlowOrderTypeEnum.getText(c.getOrderflowtype()).getText()  %></strong>&nbsp;&nbsp;供货商：<strong><%=c.getCustomername() %></strong>&nbsp;&nbsp;小件员：<strong><%=c.getCwbdelivername() %></strong></td>
					</tr>
						<tr >
							<td colspan="2" align="left" valign="top">入库时间：<strong><%=c.getInstoreroomtime() %></strong>&nbsp;&nbsp;到站时间：<strong><%=c.getInSitetime() %></strong></td>
					</tr>
						<tr >
							<td colspan="2" align="left" valign="top">投诉站点：
								<strong><%=c.getBranchname()%></strong>
								&nbsp;&nbsp;投诉小件员：<strong><%=StringUtil.nullConvertToEmptyString(c.getDelivername()) %></strong></td>
						</tr>
						<tr >
							<td width="100">投诉内容：</td>
							<td align="left"><strong><%=c.getContent() %></strong></td>
						</tr>
						<tr >
							<td width="100">审核结果：</td>
							<td align="left"><select name="auditType" id="auditType">
									<option value="-1">请选择</option>
									<option value="1" <%if(c.getAuditType()==1){ %>selected="selected" <%} %>>成立</option>
									<option value="2" <%if(c.getAuditType()==2){ %>selected="selected" <%} %>>失败</option>
							</select></td>
						</tr>
						<tr >
							<td width="100" valign="top">备注：</td>
							<td align="left"><textarea name="auditRemark" cols="80" rows="2" id="auditRemarkid"><%=c.getAuditRemark() %></textarea></td>
						</tr>
	</table>
				<div align="center">
					<input type="submit" value="确定" class="button">
					<input type="button" value="取消" class="button" onclick="closeBox()">
	</div>
			</form>
		</div>
	</div>
	<div id="box_yy"></div>
