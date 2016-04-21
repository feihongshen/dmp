<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.b2c.tps.TpsCwbFlowCfg"%>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<%
TpsCwbFlowCfg tpsCwbFlowCfg = (TpsCwbFlowCfg)request.getAttribute("tpsCwbFlowCfg");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>TPS推送对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/tpsCwbFlow/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(tpsCwbFlowCfg != null){ %>
						<li><span>是否开启对接：</span>
							<input type ="radio" id="openFlag1" name ="openFlag" value="1"  <%if(tpsCwbFlowCfg.getOpenFlag()==1){%>checked<%}%> >开启
							<input type ="radio" id="openFlag2" name ="openFlag" value="0"  <%if(tpsCwbFlowCfg.getOpenFlag()==0){%>checked<%}%> >关闭
						</li>
						<li><span>尝试推送次数：</span>
							<input type ="text" id="maxTryTime" name ="maxTryTime" value="<%=tpsCwbFlowCfg.getMaxTryTime()%>" maxlength="4" onblur="validate('maxTryTime')">*
						</li>
						<li><span>唯品会客户id：</span>
							<input type ="text" id="customerids" name ="customerids" value="<%=tpsCwbFlowCfg.getCustomerids()%>" maxlength="500">*多个客户id之间以逗号隔开
						</li>
						<li><span>最大处理行数：</span>
							<input type ="text" id="maxDataSize" name ="maxDataSize" value="<%=tpsCwbFlowCfg.getMaxDataSize()%>" maxlength="4" >*
						</li>
						<li><span>数据保留天数：</span>
							<input type ="text" id="housekeepDay" name ="housekeepDay" value="<%=tpsCwbFlowCfg.getHousekeepDay()%>" maxlength="4">*
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
						</li>
					
					<%}else{ %>
					
						<li><span>是否开启对接：</span>
							<input type ="radio" id="openFlag1" name ="openFlag" value="1" >开启
							<input type ="radio" id="openFlag2" name ="openFlag" value="0" checked>关闭
						</li>
						<li><span>尝试推送次数：</span>
							<input type ="text" id="maxTryTime" name ="maxTryTime" value="" maxlength="4" onblur="validate('maxTryTime')">*
						</li>
						<li><span>唯品会客户id：</span>
							<input type ="text" id="customerids" name ="customerids" value="" maxlength="500">*多个客户id之间以逗号隔开
						</li>
						<li><span>最大处理行数：</span>
							<input type ="text" id="maxDataSize" name ="maxDataSize" value="" maxlength="4" >*
						</li>
						<li><span>数据保留天数：</span>
							<input type ="text" id="housekeepDay" name ="housekeepDay" value="" maxlength="4">*
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

