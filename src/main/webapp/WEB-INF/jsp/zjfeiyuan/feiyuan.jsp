<%@page import="cn.explink.b2c.zjfeiyuan.jspbean.FeiYuan"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
FeiYuan fy = (FeiYuan)request.getAttribute("feiyuan");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>飞远站点匹配设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/fyAddress/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(fy != null){ %>
						
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=fy.getPrivate_key()%>"  > 
						</li>
						<li><span>userCode：</span>
	 						<input type ="text" id="userCode" name ="userCode"  maxlength="300"  value="<%=fy.getUsercode()%>"  size="15" > 
						</li>
						<li><span>batchno：</span>
	 						<input type ="text" id="batchno" name ="batchno"  maxlength="300"  value="<%=fy.getBatchno()%>"  size="15" > 
						</li>
						<li><span>请求URL：</span>
	 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value="<%=fy.getRequestUrl()%>"  > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  value="${password}" size="20" > 
						</li>
					<%}else{ %>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  > 
						</li>
						<li><span>userCode：</span>
	 						<input type ="text" id="userCode" name ="userCode"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>batchno：</span>
	 						<input type ="text" id="batchno" name ="batchno"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>请求URL：</span>
	 						<input type ="text" id="requestUrl" name ="requestUrl"  maxlength="300"  value=""  > 
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

