<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.pos.chinaums.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
ChinaUms chinaums = (ChinaUms)request.getAttribute("chinaums");
%>
	
<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>北京银联商务对接设置</h1>
		<form id="unionpay_save_Form" name="unionpay_save_Form"  onSubmit="submitSaveForm(this); return false;" action="<%=request.getContextPath()%>/chinaums/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>密钥：</span>
						<input type ="text" id="private_key" name ="private_key" value ="<%=StringUtil.nullConvertToEmptyString(chinaums.getPrivate_key()) %>" maxlength="50"  >
					</li>
					
					<li><span>请求URL：</span>
						<input type ="text" id="request_url" name ="request_url" value ="<%=StringUtil.nullConvertToEmptyString(chinaums.getRequest_url()) %>" maxlength="100"  >
					</li>
					<li><span>他人刷卡限制：</span>
						<input type="radio" name="isotherdeliveroper" <% if(chinaums.getIsotherdeliveroper()==1){ %>checked="checked"<%} %> value="1">开启
						<input type="radio" name="isotherdeliveroper" <%  if(chinaums.getIsotherdeliveroper()==0){ %>checked="checked"<%} %> value="0">关闭
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">校验他人是否可以刷卡</font>
					</li>
					<li><span>商户号：</span>
						<input type ="text" id="mer_id" name ="mer_id" value ="<%=StringUtil.nullConvertToEmptyString(chinaums.getMer_id()) %>" maxlength="50"  >
					</li>
					<li><span>是否允许转发：</span>
						<input type="radio" name="isforward" <% if(chinaums.getIsForward()==1){ %>checked="checked"<%} %> value="1">允许
						<input type="radio" name="isforward" <%  if(chinaums.getIsForward()==0){ %>checked="checked"<%} %> value="0">禁止
					</li>
					
					
					<li><span>转发URL：</span>
						<input type ="text" id="forward_url" name ="forward_url" value ="<%=StringUtil.nullConvertToEmptyString(chinaums.getForward_url()) %>" maxlength="50"  >
					</li>
					
					<li><span>版本：</span>
						<input type="radio" name="version" <% if(chinaums.getVersion()==0){ %>checked="checked"<%} %> value="0">默认
						<input type="radio" name="version" <%  if(chinaums.getVersion()==1){ %>checked="checked"<%} %> value="1">西安品信
						<input type="radio" name="version" <%  if(chinaums.getVersion()==2){ %>checked="checked"<%} %> value="2">安达信
					</li>
					
					
					<li><span>是否补充流程：</span>
						<input type="radio" name="isAutoSupplementaryProcess" <% if(chinaums.getIsAutoSupplementaryProcess()==0){ %>checked="checked"<%} %> value="0">默认
						<input type="radio" name="isAutoSupplementaryProcess" <%  if(chinaums.getIsAutoSupplementaryProcess()==1){ %>checked="checked"<%} %> value="1">开启
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

