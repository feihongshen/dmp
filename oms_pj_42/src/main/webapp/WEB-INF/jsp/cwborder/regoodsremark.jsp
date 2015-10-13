<%@page import="cn.explink.domain.CwbOrder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<CwbOrder> remarkbyId = (List<CwbOrder>)request.getAttribute("remarkbyId");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>退货备注</h1>
		<form id="order_save_Form" name="order_save_Form" onSubmit="submitSaveForm(this);return false;"  action="<%=request.getContextPath()%>/order/saveremark/<%=remarkbyId.get(0).getCwb() %>" method="post"  >
		<div id="box_form">
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
			<tr>
				<td width="10%" align="left" valign="middle" >
				<table width="100%" border="0" cellspacing="5" cellpadding="0" class="right_set1" style="height:227px">
					<tr>
					 <td>历史备注：</td>
						<td>
							<textarea rows="10" cols="60" readonly="readonly"  style="width:100%;height:64px"><%=remarkbyId.get(0).getReturngoodsremark()%></textarea>
							</td>
						</tr>
					<tr>
					    <td>退货备注：</td>
						<td>
							<textarea name="returngoodsremark" rows="10" cols="60"  style="width:100%;height:64px"></textarea></td>
					</tr>
					
				</table></td>
			</tr>
		</table>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
