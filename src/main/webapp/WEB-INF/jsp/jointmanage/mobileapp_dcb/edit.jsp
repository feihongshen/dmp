<%@page import="cn.explink.util.StringUtil"%>

<%@page import="cn.explink.pos.mobileapp_dcb.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Mobiledcb mobile = (Mobiledcb)request.getAttribute("mobilecodapplist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>大晨报手机App对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/mobiledcb/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
				
					<li><span>手机端URL：</span>
 						<input type ="text" id="sender_url" name ="sender_url" size="45" value ="<%=StringUtil.nullConvertToEmptyString(mobile.getSender_url())%>" maxlength="1000"  > 
					</li>
					<li><span>派送员同步URL：</span>
 						<input type ="text" id="sender_usersyn" name ="sender_usersyn" size="45" value ="<%=StringUtil.nullConvertToEmptyString(mobile.getSender_usersyn())%>" maxlength="1000"  > 
					</li>
					<li><span>接收回传URL：</span>
 						<input type ="text" id="receiver_url" name ="receiver_url"  size="45"  value ="<%=StringUtil.nullConvertToEmptyString(mobile.getReceiver_url())%>" maxlength="1000"  > 
					</li>
					
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value ="" maxlength="30"  > 
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

