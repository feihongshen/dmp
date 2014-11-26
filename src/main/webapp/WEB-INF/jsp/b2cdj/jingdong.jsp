<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.jingdong.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
JingDong jingdong = (JingDong)request.getAttribute("jingdongObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>京东接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/jingdong/saveJingDong/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(jingdong != null){ %>
						
						
						<li><span>京东请求URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300" size="45" value="<%=jingdong.getSearch_url()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value="<%=jingdong.getCustomerids()%>"   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="<%=jingdong.getMaxcount()%>"  size="15"> 
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>京东请求URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300" size="45" value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value=""  size="15"> 
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

