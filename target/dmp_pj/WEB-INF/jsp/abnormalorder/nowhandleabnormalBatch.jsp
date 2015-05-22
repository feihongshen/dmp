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

String ids = request.getAttribute("ids").toString();

%>
<div id="box_bg" ></div>
<div id="box_contant" >
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>批量处理问题件</h1>
		<form method="post" id="form1" onSubmit="if(check_describe()){submitSaveFormAndCloseBox(this);}return false;" action="<%=request.getContextPath()%>/abnormalOrder/SubmitHandleabnormalBatch">
			<table width="600" border="0" cellspacing="0" cellpadding="0" id="chatlist_alertbox" class="table_2">
						<tr class="font_1">
							<td colspan="2" align="left" valign="middle">处理内容：</td>
							<td><textarea name="describe" id="describe" cols="80%" rows="4" id="textfield"></textarea></td>
						</tr>
					</table>
			<div align="center">
			<input type="hidden" id="ids" name="ids" value="<%=ids%>"/>
				<input type="submit" value="处理" class="button">
				</div>
		</form>
	</div>
</div>
<div id="box_yy"></div>