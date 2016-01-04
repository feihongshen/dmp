<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.pos.unionpay.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
UnionPay unionpay = (UnionPay)request.getAttribute("unionpay");
%>
	
<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>银联商务对接设置</h1>
		<form id="unionpay_save_Form" name="unionpay_save_Form"  onSubmit="submitSaveForm(this); return false;" action="<%=request.getContextPath()%>/unionpay/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>密钥：</span>
						<input type ="text" id="private_key" name ="private_key" value ="<%=StringUtil.nullConvertToEmptyString(unionpay.getPrivate_key()) %>" maxlength="50"  >
					</li>
					<li><span>请求URL：</span>
						<input type ="text" id="request_url" name ="request_url" value ="<%=StringUtil.nullConvertToEmptyString(unionpay.getRequest_url()) %>" maxlength="50"  >
					</li>
					<li><span>他人刷卡限制：</span>
						<input type="radio" name="isotherdeliveroper" <% if(unionpay.getIsotherdeliveroper()==1){ %>checked="checked"<%} %> value="1">开启
						<input type="radio" name="isotherdeliveroper" <%  if(unionpay.getIsotherdeliveroper()==0){ %>checked="checked"<%} %> value="0">关闭
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">校验他人是否可以刷卡</font>
					</li>
					<li><span>请求银联URL：</span>
						<input type ="text" id="requestPosUrl" name ="requestPosUrl" value ="<%=StringUtil.nullConvertToEmptyString(unionpay.getRequestPosUrl()) %>" maxlength="50"  >
					</li>
					<li><span>限制查询：</span>
 						<input type ="text" id="resultCustomerid" name ="resultCustomerid" value ="<%=StringUtil.nullConvertToEmptyString(unionpay.getResultCustomerid())%>" maxlength="50"  > 
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

