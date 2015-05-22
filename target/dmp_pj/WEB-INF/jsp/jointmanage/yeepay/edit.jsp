<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.pos.yeepay.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
YeePay yeePay = (YeePay)request.getAttribute("yeepaylist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>易宝支付对接设置</h1>
		<form id="yeepay_save_Form" name="yeepay_save_Form"  onSubmit="if(check_yeepay()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yeepay/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					
					<li><span>请求方：</span>
						<input type ="text" id="requester" name ="requester" value ="<%=StringUtil.nullConvertToEmptyString(yeePay.getRequester()) %>" maxlength="30"  >
					</li>
					<li><span>应答方：</span>
 						<input type ="text" id="targeter" name ="targeter" value ="<%=StringUtil.nullConvertToEmptyString(yeePay.getTargeter())%>" maxlength="30"  > 
					</li>
					<li><span>加密密钥：</span>
 						<input type ="text" id="privatekey" name ="privatekey" value ="<%=StringUtil.nullConvertToEmptyString(yeePay.getPrivatekey())%>" maxlength="300"  > 
					</li>
					<li><span>他人刷卡限制：</span>
						<input type="radio" name="isotheroperator" <% if(yeePay.getIsotheroperator()==1){ %>checked="checked"<%} %> value="1">开启
						<input type="radio" name="isotheroperator" <%  if(yeePay.getIsotheroperator()==0){ %>checked="checked"<%} %> value="0">关闭
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">校验他人是否可以刷卡</font>
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

