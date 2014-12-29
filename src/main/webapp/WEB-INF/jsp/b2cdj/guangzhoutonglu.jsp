<%@page import="cn.explink.b2c.gztl.Gztl"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.lefengdms.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Gztl gztl = (Gztl)request.getAttribute("guangzhoutongluObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>广州通路接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/gztl/saveGuangzhoutonglu/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(gztl != null){ %>
					
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=gztl.getCustomerids()%>"  size="30" > 
						</li>
						<li><span>提供URL：</span>
	 						<input type ="text" id="search_url" name ="search_url"  maxlength="300"  value="<%=gztl.getSearch_url()%>"  size="30" > 
						</li>
							<li><span>查询条数：</span>
	 						<input type ="text" id="search_number" name ="search_number"  maxlength="300"  value="<%=gztl.getSearch_number()%>"  size="30"> 
						</li>
						<li><span>MD5加密字段：</span>
	 						<input type ="text" id="sign" name ="sign"  maxlength="300"  value="<%=gztl.getSign()%>"  size="30"> 
						</li>
							<li><span>客户编码：</span>
	 						<input type ="text" id="code" name ="code"  maxlength="300"  value="<%=gztl.getCode()%>"  size="30"> 
						</li>
							<li><span>接口名称(FBI)：</span>
	 						<input type ="text" id="invokeMethod" name ="invokeMethod"  maxlength="300"  value="<%=gztl.getInvokeMethod()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商id号：</span>
	 						<input type ="text" id="agentId" name ="agentId"  maxlength="300"  value="<%=gztl.getAgentId()%>"  size="30"> 
						</li>
						</li>
							<li><span>承运商名：</span>
	 						<input type ="text" id="agentName" name ="agentName"  maxlength="300"  value="<%=gztl.getAgentName()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商电话：</span>
	 						<input type ="text" id="agentPhone" name ="agentPhone"  maxlength="300"  value="<%=gztl.getAgentPhone()%>"  size="30"> 
						</li>
							</li>
							<li><span>承运商网站：</span>
	 						<input type ="text" id="agentWebsite" name ="agentWebsite"  maxlength="300"  value="<%=gztl.getAgentWebsite()%>"  size="30"> 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(gztl.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
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
						<li><span>MD5加密字段：</span>
	 						<input type ="text" id="sign" name ="sign"  maxlength="300"  value=""  size="30"> 
						</li>
						<li><span>客户编码：</span>
	 						<input type ="text" id="code" name ="code"  maxlength="300"  value=""  size="30"> 
						</li>
							<li><span>接口名称(FBI)：</span>
	 						<input type ="text" id="invokeMethod" name ="invokeMethod"  maxlength="300"  value=""  size="30"> 
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
							<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
							</select>
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

