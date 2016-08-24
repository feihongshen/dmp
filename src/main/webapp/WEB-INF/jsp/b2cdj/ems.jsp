<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.ems.*"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
EMS ems = (EMS)request.getAttribute("emsObject");
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
						<li><span>订单下发账号：</span>
							<input type ="text" id="sysAccount" name ="sysAccount" value="<%=ems.getSysAccount() %>"  maxlength="300">
						</li>
						<li><span>异常码提供方：</span>
							<input type ="text" id="supportKey" name ="supportKey" value="<%=ems.getSupportKey() %>"  maxlength="300">
						</li>
						<li><span>轨迹推送url：</span>
							<input type ="text" id="orderSendUrl" name ="orderSendUrl" value="<%=ems.getOrderSendUrl() %>"  maxlength="300">
						</li>
						<li><span>订单对接授权码：</span>
							<input type ="text" id="appKey" name ="appKey" value="<%=ems.getAppKey() %>"  maxlength="300">
						</li>
						<li><span>对接秘钥：</span>
							<input type ="text" id="encodeKey" name ="encodeKey" value="<%=ems.getEncodeKey()%>"  maxlength="300">
						</li>	
						<li><span>订单每次推送数量：</span>
							<input type ="text" id="sendOrderCount" name ="sendOrderCount" onblur="validate('sendOrderCount')" value="<%=ems.getSendOrderCount() %>"  maxlength="300">
						</li>
						<li ><span>ems运单获取url：</span>
							<input type ="text" id="emsTranscwbUrl" name ="emsTranscwbUrl" value="<%=ems.getEmsTranscwbUrl() %>"  maxlength="300">
						</li>
						<li style="display: none;"><span>状态回传url：</span>
							<input type ="text" id="emsStateUrl" name ="emsStateUrl"  value="<%=ems.getEmsStateUrl() %>"  maxlength="300">
						</li>
						<li><span>EMS站点编号：</span>
							<input type ="text" id="emsBranchid" name ="emsBranchid" onblur="validate('emsBranchid')"  value="<%=ems.getEmsBranchid() %>"  maxlength="300">
						</li>
						<li><span>EMS小件员id：</span>
							<input type ="text" id="emsDiliveryid" name ="emsDiliveryid" onblur="validate('emsDiliveryid')"  value="<%=ems.getEmsDiliveryid() %>"  maxlength="300">
						</li>
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%}else{ %>
						<li><span>订单下发账号：</span>
							<input type ="text" id="sysAccount" name ="sysAccount" value=""  maxlength="300">
						</li>
						<li><span>异常码提供方：</span>
							<input type ="text" id="supportKey" name ="supportKey" maxlength="300">
						</li>
						<li><span>轨迹推送url：</span>
							<input type ="text" id="orderSendUrl" name ="orderSendUrl" maxlength="300">
						</li>
						<li><span>订单对接授权码：</span>
							<input type ="text" id="appKey" name ="appKey" value=""  maxlength="300">
						</li>
						<li><span>对接秘钥：</span>
							<input type ="text" id="encodeKey" name ="encodeKey" maxlength="300">
						</li>	
						<li><span>订单每次推送数量：</span>
							<input type ="text" id="sendOrderCount" name ="sendOrderCount" onblur="validate('sendOrderCount')" maxlength="300">
						</li>
						<li><span>ems运单获取url：</span>
							<input type ="text" id="emsTranscwbUrl" name ="emsTranscwbUrl"  maxlength="300">
						</li>
						<li style="display: none;"><span>状态回传url：</span>
							<input type ="text" id="emsStateUrl" name ="emsStateUrl" maxlength="300">
						</li>
						<li><span>EMS站点编号：</span>
							<input type ="text" id="emsBranchid" name ="emsBranchid" onblur="validate('emsBranchid')"  maxlength="300">
						</li>
						<li><span>EMS小件员id：</span>
							<input type ="text" id="emsDiliveryid" name ="emsDiliveryid" onblur="validate('emsDiliveryid')" maxlength="300">
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

