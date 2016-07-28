<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.b2c.tps.OrderTraceToTPSCfg"%>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<%
OrderTraceToTPSCfg orderTraceToTPS = (OrderTraceToTPSCfg)request.getAttribute("orderTraceToTPS");
List<Long> customeridList = (List<Long>)request.getAttribute("customeridList");
List<Customer> customerList = request.getAttribute("customerList") == null ? new ArrayList<Customer>() : (List<Customer>)request.getAttribute("customerList");
%>
<script type="text/javascript">
	/* $(function() {
		$("#customerids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择外单客户' });
	}); */
</script>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>推送订单轨迹给TPS设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/orderTraceToTPS/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(orderTraceToTPS != null){ %>
						<li><span>推送轨迹客户id：</span>
							<input type ="text" id="customerids" name ="customerids" value="<%=orderTraceToTPS.getCustomerids()%>" maxlength="500">*多个客户id之间以逗号隔开
						</li>
					    <!--add by 周欢 2016-07-15 -->
						<li><span>每次推送轨迹数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount" onblur="validate('sendMaxCount')"  value="<%=orderTraceToTPS.getSendMaxCount() %>"  maxlength="300">
						</li>
						<li><span>轨迹尝试推送次数：</span>
							<input type ="text" id="trackMaxTryTime" name ="trackMaxTryTime" value="<%=orderTraceToTPS.getTrackMaxTryTime()%>" maxlength="4" >*
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
						</li>
					
					<%}else{ %>
						<li><span>外单客户id：</span>
							<input type ="text" id="customerids" name ="customerids"  maxlength="500">*多个客户id之间以逗号隔开
						</li>
						<!--add by 周欢 2016-07-15 -->
						<li><span>每次推送轨迹数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount" onblur="validate('sendMaxCount')" maxlength="300">
						</li>
						<li><span>轨迹尝试推送次数：</span>
							<input type ="text" id="trackMaxTryTime" name ="trackMaxTryTime"  maxlength="4" >*
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
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

