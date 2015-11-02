<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.vo.DeliverServerParamVO"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
DeliverServerParamVO deliverServerParamVO = null == request.getAttribute("deliverServerParamVO")? new DeliverServerParamVO():(DeliverServerParamVO)request.getAttribute("deliverServerParamVO");
%>

<script type="text/javascript">


</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
	<h1><div id="close_box" onclick="closeBox()"></div>派件服务对接设置</h1>
	<form id="deliverServer_save_Form" name="deliverServer_save_Form"  onSubmit="if(check_liebo()){submitCreateFormAndCloseBox(this);}return false;" action="<%=request.getContextPath()%>/deliverServer/saveParamVO/${joint_num}" method="post">
		<input type ="hidden" id="forward_hours" name ="forward_hours" value="0"  maxlength="2">
		<div id="box_form">
				<ul>
					<%if(deliverServerParamVO != null){ %>
						<li><span>接入公司code：</span>
							<input type ="text" id="code" name ="code" value="<%=deliverServerParamVO.getCode() %>"  maxlength="300">
						</li>
						<li><span>配送公司code：</span>
							<input type ="text" id="delivery_company_code" name ="delivery_company_code" value="<%=deliverServerParamVO.getDelivery_company_code()%>"  maxlength="300">
						</li>
						<li><span>接入公司token：</span>
							<input type ="text" id="token" name ="token" maxlength="300" value="<%=deliverServerParamVO.getToken()%>">
						</li>
						<li><span>派件通知url：</span>
							<input type ="text" id="deliverServerPushUrl" name ="deliverServerPushUrl" value="<%=deliverServerParamVO.getDeliverServerPushUrl() %>"  maxlength="300">
						</li>
						<li><span>派件员同步url：</span>
							<input type ="text" id="deliverSyncPushUrl" name ="deliverSyncPushUrl" value="<%=deliverServerParamVO.getDeliverSyncPushUrl() %>"  maxlength="300">
						</li>
							<li><span>POS刷卡通知URL：</span>
							<input type ="text" id="deliverPosSynUrl" name ="deliverPosSynUrl" value="<%=deliverServerParamVO.getDeliverPosSynUrl() %>" maxlength="300">
						</li>
						<li><span>当前流水号：</span>
							<input type ="text" id="tradeNum" name ="tradeNum" value="<%=deliverServerParamVO.getTradeNum() %>" maxlength="300" readonly="true">
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20"> 
						</li>
					<%}else{ %>
						<li><span>接入公司code：</span>
							<input type ="text" id="code" name ="code" maxlength="300">
						</li>
						<li><span>配送公司code：</span>
							<input type ="text" id="delivery_company_code" name ="delivery_company_code" value=""  maxlength="300">
						</li>
						<li><span>接入公司token：</span>
							<input type ="text" id="token" name ="token" maxlength="300">
						</li>
						<li><span>派件通知url：</span>
							<input type ="text" id="deliverServerPushUrl" name ="deliverServerPushUrl" maxlength="300">
						</li>
						<li><span>派件员同步url：</span>
							<input type ="text" id="deliverSyncPushUrl" name ="deliverSyncPushUrl" maxlength="300">
						</li>
						<li><span>POS刷卡通知URL：</span>
							<input type ="text" id="deliverPosSynUrl" name ="deliverPosSynUrl" maxlength="300">
						</li>
						<li><span>当前流水号：</span>
							<input type ="text" id="tradeNum" name ="tradeNum" value="<%=deliverServerParamVO.getTradeNum() %>" maxlength="300" readonly="true">
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password" maxlength="30" size="20"> 
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

