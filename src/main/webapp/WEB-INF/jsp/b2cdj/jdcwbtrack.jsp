<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.jd.cwbtrack.JdCwbTrackConfig"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
JdCwbTrackConfig jdCwbTrackConfig = (JdCwbTrackConfig)request.getAttribute("jdCwbTrackConfig");
%>

<script type="text/javascript">
</script>
<style>
#box_form span {
    width: 135px;
}
</style>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>京东_订单跟踪接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/jdCwbTrack/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(jdCwbTrackConfig != null){ %>
						
						
						<li><span>在配送公司中的ID：</span>
	 						<input type ="text" id="customerId" name ="customerId"  maxlength="300"  value="<%=jdCwbTrackConfig.getCustomerId()%>" onblur="javascript:validate('customerId')"  size="20"> 
						</li>
						<li><span>每次查询最大订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=jdCwbTrackConfig.getMaxCount()%>"  onblur="javascript:validate('maxCount')" size="20"> 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="privateKey" name ="privateKey"  maxlength="300"  value="<%=jdCwbTrackConfig.getPrivateKey()%>"   size="20"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
					    <li><span>在配送公司中的ID：</span>
	 						<input type ="text" id="customerId" name ="customerId"  maxlength="300"  value="" onblur="javascript:validate('customerId')" size="20"> 
						</li>
						<li><span>每次查询最大订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="" onblur="javascript:validate('maxCount')"  size="20"> 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="privateKey" name ="privateKey"  maxlength="300"  value=""   size="20"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
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

