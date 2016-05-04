<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.ems.*"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
EMS ems = (EMS)request.getAttribute("emsObject");
String a = ems.getEmsStateUrl();
%>



<script type="text/javascript">


</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>EMS对接设置</h1>
		<form id="ems_save_Form" name="ems_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/ems/save/${joint_num}" method="post">
	
		<input type ="hidden" id="forward_hours" name ="forward_hours" value="0"  maxlength="2">
<div id="box_form">
				<ul>
					<%if(ems != null){ %>
						<li><span>异常码提供方：</span>
							<input type ="text" id="supportKey" name ="supportKey" value="<%=ems.getSupportKey() %>"  maxlength="300">
						</li>
						<li><span>订单推送url：</span>
							<input type ="text" id="orderSendUrl" name ="orderSendUrl" value="<%=ems.getOrderSendUrl() %>"  maxlength="300">
						</li>
						<li><span>订单接口秘钥：</span>
							<input type ="text" id="orderPrivateKey" name ="orderPrivateKey" value="<%=ems.getOrderPrivateKey()%>"  maxlength="300">
						</li>	
						<li><span>订单每次推送数量：</span>
							<input type ="text" id="sendOrderCount" name ="sendOrderCount" onblur="validate('sendOrderCount')" value="<%=ems.getSendOrderCount() %>"  maxlength="300">
						</li>
						<li><span>订单重推次数上限：</span>
							<input type ="text" id="resendOrderCount" name ="resendOrderCount" onblur="validate('resendOrderCount')" value="<%=ems.getResendOrderCount() %>"  maxlength="300">
						</li>
						<li><span>ems运单获取url：</span>
							<input type ="text" id="emsTranscwbUrl" name ="emsTranscwbUrl" value="<%=ems.getEmsTranscwbUrl() %>"  maxlength="300">
						</li>
						<li><span>ems运单接口秘钥：</span>
							<input type ="text" id="emsTranscwbPrivateKey" name ="emsTranscwbPrivateKey" onblur="validate('emsTranscwbPrivateKey')"  value="<%=ems.getEmsTranscwbPrivateKey()%>"  maxlength="300">
						</li>
						<li><span>ems运单抓取数量：</span>
							<input type ="text" id="emsTranscwbCount" name ="emsTranscwbCount" onblur="validate('emsTranscwbCount')"  value="<%=ems.getEmsTranscwbCount() %>"  maxlength="300">
						</li>
						<li><span>状态回传url：</span>
							<input type ="text" id="emsStateUrl" name ="emsStateUrl" onblur="validate('emsStateUrl')"  value="<%=ems.getEmsStateUrl() %>"  maxlength="300">
						</li>
						<li><span>状态回传接口秘钥：</span>
							<input type ="text" id="emsStateKey" name ="emsStateKey" value="<%=ems.getEmsStateKey() %>"  maxlength="300">
						</li>
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%}else{ %>
						<li><span>异常码提供方：</span>
							<input type ="text" id="supportKey" name ="supportKey" maxlength="300">
						</li>
						<li><span>订单推送url：</span>
							<input type ="text" id="orderSendUrl" name ="orderSendUrl" maxlength="300">
						</li>
						<li><span>订单接口秘钥：</span>
							<input type ="text" id="orderPrivateKey" name ="orderPrivateKey"  maxlength="300">
						</li>	
						<li><span>订单每次推送数量：</span>
							<input type ="text" id="sendOrderCount" name ="sendOrderCount" onblur="validate('sendOrderCount')" maxlength="300">
						</li>
						<li><span>订单重推次数上限：</span>
							<input type ="text" id="resendOrderCount" name ="resendOrderCount" onblur="validate('resendOrderCount')" maxlength="300">
						</li>
						<li><span>ems运单获取url：</span>
							<input type ="text" id="emsTranscwbUrl" name ="emsTranscwbUrl"  maxlength="300">
						</li>
						<li><span>ems运单接口秘钥：</span>
							<input type ="text" id="emsTranscwbPrivateKey" name ="emsTranscwbPrivateKey" onblur="validate('emsTranscwbPrivateKey')"   maxlength="300">
						</li>
						<li><span>ems运单抓取数量：</span>
							<input type ="text" id="emsTranscwbCount" name ="emsTranscwbCount" onblur="validate('emsTranscwbCount')"  maxlength="300">
						</li>
						<li><span>状态回传url：</span>
							<input type ="text" id="emsStateUrl" name ="emsStateUrl" onblur="validate('emsStateUrl')"  maxlength="300">
						</li>
						<li><span>状态回传接口秘钥：</span>
							<input type ="text" id="emsStateKey" name ="emsStateKey" maxlength="300">
						</li>
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%} %>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

