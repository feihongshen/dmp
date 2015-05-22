<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.moonbasa.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Moonbasa mbs = (Moonbasa)request.getAttribute("moonbasaObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>梦芭莎接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/moonbasa/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(mbs != null){ %>
						
						
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="custcode" name ="custcode"  maxlength="300" size="15" value="<%=mbs.getCustcode()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value="<%=mbs.getCustomerid()%>"   > 
						</li>
						
						<li><span>查询密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value="<%=mbs.getPwd()%>"  size="15"> 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=mbs.getSearch_url()%>"  size="40"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="custcode" name ="custcode"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value=""   > 
						</li>
						
						<li><span>查询密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>提供URL：</span>
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

