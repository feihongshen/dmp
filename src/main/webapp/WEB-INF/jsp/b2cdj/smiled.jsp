<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.smiled.*"%>
<%@page import="cn.explink.domain.Branch"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Smiled maisike = (Smiled)request.getAttribute("smiledObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>思迈下游对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/smiled/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(maisike != null){ %>
						<li><span>唯一标识：</span>
							<input type ="text" id="express_id" name ="express_id" value="<%=maisike.getExpress_id()%>" size="20" maxlength="300">
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value="<%=maisike.getPrivate_key() %>"  > 
						</li>
						<li><span>思迈下游URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300" size="20"  value="<%=maisike.getSend_url() %>"  > 
						</li>
						
						<li><span>接收回传URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value="<%=maisike.getFeedback_url()%>"   > 
						</li>
						
						<li><span>每次查询大小：</span>
							<input type ="text" id="maxCount" name ="maxCount" value="<%=maisike.getMaxCount()%>" size="20" maxlength="300">
						</li>
						<li><span>循环次数：</span>
							<input type ="text" id="loopcount" name ="loopcount" value="<%=maisike.getLoopcount()%>" size="20" maxlength="300">
						</li>
						<li><span>重发次数：</span>
							<input type ="text" id="resendcount" name ="resendcount" value="<%=maisike.getResendcount()%>" size="20" maxlength="300">
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>唯一标识：</span>
							<input type ="text" id="express_id" name ="express_id" value="" size="20" maxlength="300">
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value=""  > 
						</li>
						<li><span>思迈下游URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300" size="20"  value=""  > 
						</li>
						
						<li><span>接收回传URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value=""   > 
						</li>
						
						<li><span>每次查询大小：</span>
							<input type ="text" id="maxCount" name ="maxCount" value="" size="20" maxlength="300">
						</li>
						<li><span>循环次数：</span>
							<input type ="text" id="loopcount" name ="loopcount" value="" size="20" maxlength="300">
						</li>
						<li><span>重发次数：</span>
							<input type ="text" id="resendcount" name ="resendcount" value="" size="20" maxlength="300">
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

