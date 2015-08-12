<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.lefengdms.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Lefengdms lefeng = (Lefengdms)request.getAttribute("lefengObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>乐蜂网接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/lefengdms/saveLefeng/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(lefeng != null){ %>
					
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=lefeng.getCustomerids()%>"  size="30" > 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=lefeng.getSearch_url()%>"  size="30" > 
						</li>
							<li><span>查询条数：</span>
	 						<input type ="text" id="search_number" name ="search_number"  maxlength="300"  value="<%=lefeng.getSearch_number()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商id号：</span>
	 						<input type ="text" id="agentId" name ="agentId"  maxlength="300"  value="<%=lefeng.getAgentId()%>"  size="30"> 
						</li>
						</li>
							<li><span>承运商名：</span>
	 						<input type ="text" id="agentName" name ="agentName"  maxlength="300"  value="<%=lefeng.getAgentName()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商电话：</span>
	 						<input type ="text" id="agentPhone" name ="agentPhone"  maxlength="300"  value="<%=lefeng.getAgentPhone()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商网站：</span>
	 						<input type ="text" id="agentWebsite" name ="agentWebsite"  maxlength="300"  value="<%=lefeng.getAgentWebsite()%>"  size="30"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="300" size="30" > 
						</li>
					<%}else{ %>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="30" > 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value=""  size="30" > 
						</li>
							<li><span>查询条数：</span>
	 						<input type ="text" id="search_number" name ="search_number"  maxlength="300"  value=""  size="30"> 
						</li>
						</li>
							<li><span>承运商id号：</span>
	 						<input type ="text" id="agentId" name ="agentId"  maxlength="300"  value=""  size="30"> 
						</li>
						</li>
							<li><span>承运商名：</span>
	 						<input type ="text" id="agentName" name ="agentName"  maxlength="300"  value=""  size="30"> 
						</li>
							</li>
							<li><span>承运商电话：</span>
	 						<input type ="text" id="agentPhone" name ="agentPhone"  maxlength="300"  value=""  size="30"> 
						</li>
							</li>
							<li><span>承运商网站：</span>
	 						<input type ="text" id="agentWebsite" name ="agentWebsite"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="300" size="30" > 
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

