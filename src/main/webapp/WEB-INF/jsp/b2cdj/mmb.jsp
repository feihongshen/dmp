<%@page import="cn.explink.b2c.mmb.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Mmb mkl = (Mmb)request.getAttribute("mmbObject");
	
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>买卖宝反馈</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/mmb/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(mkl != null){ %>
						<li><span>唯一标识key：</span>
	 						<input type ="text" id="key" name ="key"  maxlength="300"  value="<%=mkl.getKey()%>"  size="15" > 
						</li>
						<li><span>公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value="<%=mkl.getCompanyname()%>"  size="15" > 
						</li>
						<li><span>推送URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300"  value="<%=mkl.getSend_url()%>"  size="15" > 
						</li>
						<li><span>customer_id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=mkl.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>返回单量最大值：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="30" size="20"  value="<%=mkl.getMaxCount()%>" > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>唯一标识key：</span>
	 						<input type ="text" id="key" name ="key"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>推送URL：</span>
	 						<input type ="text" id="send_url" name ="send_url"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>customer_id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>返回单量最大值：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="30" size="20"  value="" > 
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
