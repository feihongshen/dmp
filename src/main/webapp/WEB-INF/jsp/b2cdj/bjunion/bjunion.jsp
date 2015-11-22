<%@page import="cn.explink.b2c.bjUnion.BJUnion"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	BJUnion smile = (BJUnion)request.getAttribute("bjunion");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>北京银联设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/bjunion/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(smile != null){ %>
						<li>
							<span>requestUrl：</span>
	 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value="<%=smile.getRequestUrl()%>"  > 
						</li>
						<li>
							<span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=smile.getPrivate_key()%>"  > 
						</li>
						<li>
							<span>客户id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=smile.getCustomerid()%>"  > 
						</li>
						<li>
							<span>是否校验加密：</span>
	 						<input type ="text" id="ischecksign" name ="ischecksign"  maxlength="300"  value="<%=smile.getIschecksign()%>"  >(yes:校验加密) 
						</li>
						<li>
							<span>密码：</span>
	 						<input type ="password" id="password" name ="password" value="explink" maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li>
							<span>requestUrl：</span>
	 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value=""  > 
						</li>
						<li>
							<span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  > 
						</li>
						<li>
							<span>客户id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  > 
						</li>
						<li>
							<span>是否校验加密：</span>
	 						<input type ="text" id="ischecksign" name ="ischecksign"  maxlength="300"  value="" >(yes:校验加密) 
						</li>
						<li>
							<span>密码：</span> 
	 						<input type ="password" id="password" name ="password" value="explink" maxlength="30" size="20" > 
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

