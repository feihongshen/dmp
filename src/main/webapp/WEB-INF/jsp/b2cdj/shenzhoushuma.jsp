<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.shenzhoushuma.ShenZhouShuMa"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
ShenZhouShuMa ShenZhouShuMa = (ShenZhouShuMa)request.getAttribute("shenZhouShuMa");
%>

<script type="text/javascript">
</script>
<style>
#box_form span {
    width: 95px;
}
</style>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>神州数码接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/shenzhoushuma/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(ShenZhouShuMa != null){ %>
						<li><span>代运简称：</span>
	 						<input type ="text" id="logisticProvider" name ="logisticProvider"  maxlength="300"  value="<%=ShenZhouShuMa.getLogisticProvider()%>"   size="20"> 
						</li>
						<li><span>代运编码：</span>
	 						<input type ="text" id="logisticProviderId" name ="logisticProviderId"  maxlength="300"  value="<%=ShenZhouShuMa.getLogisticProviderId()%>"   size="20"> 
						</li>
						<li><span>系统客户id：</span>
	 						<input type ="text" id="customerId" name ="customerId"  maxlength="300"  value="<%=ShenZhouShuMa.getCustomerId()%>" onblur="javascript:validate('customerId')"  size="20"> 
						</li>
						<li><span>状态反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=ShenZhouShuMa.getMaxCount()%>"  onblur="javascript:validate('maxCount')" size="20"> 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedBackUrl" name ="feedBackUrl"  maxlength="300"  value="<%=ShenZhouShuMa.getFeedBackUrl()%>"   size="20"> 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="privateKey" name ="privateKey"  maxlength="300"  value="<%=ShenZhouShuMa.getPrivateKey()%>"   size="20"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>代运简称：</span>
	 						<input type ="text" id="logisticProvider" name ="logisticProvider"  maxlength="300"  value=""   size="20"> 
						</li>
						<li><span>代运编码：</span>
	 						<input type ="text" id="logisticProviderId" name ="logisticProviderId"  maxlength="300"  value=""   size="20"> 
						</li>
					    <li><span>系统客户id：</span>
	 						<input type ="text" id="customerId" name ="customerId"  maxlength="300"  value="" onblur="javascript:validate('customerId')" size="20"> 
						</li>
						<li><span>状态反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="" onblur="javascript:validate('maxCount')"  size="20"> 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedBackUrl" name ="feedBackUrl"  maxlength="300"  value=""   size="20"> 
						</li>
						<li><span>密钥：</span>
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

