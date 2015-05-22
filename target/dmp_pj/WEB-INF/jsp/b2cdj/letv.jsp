<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.letv.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Letv letv = (Letv)request.getAttribute("letvObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>乐视网接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/letv/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(letv != null){ %>
						
						<li><span>物流编号：</span>
	 						<input type ="text" id="expressid" name ="expressid"  maxlength="300" size="20" value="<%=letv.getExpressid()%>"  > 
						</li>
						<li><span>service_code：</span>
	 						<input type ="text" id="service_code" name ="service_code"  size="10"  maxlength="300"  value="<%=letv.getService_code()%>"   > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="10"  maxlength="300"  value="<%=letv.getCustomerid()%>"   > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=letv.getMaxCount()%>"  size="10"> 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=letv.getPrivate_key()%>"  size="40"> 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=letv.getSend_url()%>"  size="40"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流编号：</span>
	 						<input type ="text" id="expressid" name ="expressid"  maxlength="300" size="20" value=""  > 
						</li>
						<li><span>service_code：</span>
	 						<input type ="text" id="service_code" name ="service_code"  size="10"  maxlength="300"  value=""   > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="10"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="10"> 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  size="40"> 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value=""  size="40"> 
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

