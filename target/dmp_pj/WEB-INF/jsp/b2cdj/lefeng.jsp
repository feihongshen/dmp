<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.lefeng.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
LefengT lefeng = (LefengT)request.getAttribute("lefengObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>乐蜂网接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/lefeng/saveLefeng/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(lefeng != null){ %>
						
						
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="code" name ="code"  maxlength="300"  value="<%=lefeng.getCode()%>"  > 
						</li>
						<li><span>快递公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value="<%=lefeng.getCompanyname()%>"  > 
						</li>
						<li><span>快递公司电话：</span>
	 						<input type ="text" id="companyphone" name ="companyphone"  maxlength="300"  value="<%=lefeng.getCompanyphone()%>"  > 
						</li>
						<li><span>网站地址：</span>
	 						<input type ="text" id="website" name ="website"  maxlength="300"  value="<%=lefeng.getWebsite()%>"  > 
						</li>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="appkey" name ="appkey"  maxlength="300"  value="<%=lefeng.getAppkey()%>"    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=lefeng.getCustomerids()%>"  size="15" > 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=lefeng.getSearch_url()%>"  size="45" > 
						</li>
						
						<li><span>签名验证：</span>
	 						<input type ="radio" id="issignflag" name ="issignflag"  maxlength="300"  value="1"  <%if(lefeng.getIssignflag()==1){%>checked<%} %> > 开启
	 						<input type ="radio" id="issignflag" name ="issignflag"  maxlength="300"  value="0" <%if(lefeng.getIssignflag()==0){%>checked<%} %>  > 关闭
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="code" name ="code"  maxlength="300"  value=""  > 
						</li>
						<li><span>快递公司名称：</span>
	 						<input type ="text" id="companyname" name ="companyname"  maxlength="300"  value=""  > 
						</li>
						<li><span>快递公司电话：</span>
	 						<input type ="text" id="companyphone" name ="companyphone"  maxlength="300"  value=""  > 
						</li>
						<li><span>网站地址：</span>
	 						<input type ="text" id="website" name ="website"  maxlength="300"  value=""  > 
						</li>
						
						
						<li><span>密钥信息：</span>
	 						<input type ="text" id="appkey" name ="appkey"  maxlength="300"  value=""    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value=""  size="45" > 
						</li>
						<li><span>签名验证：</span>
	 						<input type ="radio" id="issignflag" name ="issignflag"  maxlength="300"  value="1"   > 开启
	 						<input type ="radio" id="issignflag" name ="issignflag"  maxlength="300"  value="0"  checked  > 关闭
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

