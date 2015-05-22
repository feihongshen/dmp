<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.gome.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Gome gome = (Gome)request.getAttribute("gomeObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>国美接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/gome/saveGome/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(gome != null){ %>
						
						<li><span>tnt地址：</span>
	 						<input type ="text" id="ws_url" name ="tnt_url"  maxlength="300" size="50"  value="<%=gome.getTnt_url()%>"    > 
						</li>
						<li><span>full地址：</span>
	 						<input type ="text" id="ws_url" name ="full_url"  maxlength="300" size="50"  value="<%=gome.getFull_url()%>"    > 
						</li>
						<li><span>用户名：</span>
	 						<input type ="text" id="username" name ="username"  maxlength="300"  value="<%=gome.getUsername()%>"  size="15" > 
						</li>
						<li><span>密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value="<%=gome.getPassword()%>"  size="15" > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=gome.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=gome.getMaxCount()%>"  size="15" > 
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="maxCount" name ="checkCount"  maxlength="300"  value="<%=gome.getCheckCount()%>"  size="15" > 
						</li>
						<li><span>业务代码：</span>
	 						<input type ="text" id="businessCode" name ="businessCode"  maxlength="300"  value="<%=gome.getBusinessCode()%>"  size="50" > 
						</li>
						<li><span>物流公司编码：</span>
	 						<input type ="text" id="lspCode" name ="lspCode"  maxlength="300"  value="<%=gome.getLspCode()%>"  size="15" > 
						</li>
						<li><span>物流公司代码：</span>
	 						<input type ="text" id="lspAbbr" name ="lspAbbr"  maxlength="300"  value="<%=gome.getLspAbbr()%>"  size="50" >
						</li>
						<li><span>渠道代码：</span>
	 						<input type ="text" id="buid" name ="buid"  maxlength="300"  value="<%=gome.getBuid()%>"  size="50" > 
						</li>
						<li><span>物流公司名称：</span>
	 						<input type ="text" id="lspName" name ="lspName"  maxlength="300"  value="<%=gome.getLspName()%>"  size="45" >
						</li>
						<li><span>物流公司电话：</span>
	 						<input type ="text" id="lspPhoneNumber" name ="lspPhoneNumber"  maxlength="300"  value="<%=gome.getLspPhoneNumber()%>"  size="45" >
						</li>
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(gome.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>tnt地址：</span>
	 						<input type ="text" id="tnt_url" name ="tnt_url"  maxlength="300"  value=""   size="50"  > 
						</li>
						<li><span>full地址：</span>
	 						<input type ="text" id="full_url" name ="full_url"  maxlength="300"  value=""   size="50"  > 
						</li>
						<li><span>用户名：</span>
	 						<input type ="text" id="username" name ="username"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="maxCount" name ="checkCount"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>业务代码：</span>
	 						<input type ="text" id="businessCode" name ="businessCode"  maxlength="300"  value=""  size="50" > 
						</li>
						<li><span>物流公司编码：</span>
	 						<input type ="text" id="lspCode" name ="lspCode"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>物流公司代码：</span>
	 						<input type ="text" id="lspAbbr" name ="lspAbbr"  maxlength="300"  value=""  size="50" >
						</li>
						<li><span>渠道代码：</span>
	 						<input type ="text" id="buid" name ="buid"  maxlength="300"  value=""  size="50" > 
						</li>
						<li><span>物流公司名称：</span>
	 						<input type ="text" id="lspName" name ="lspName"  maxlength="300"  value=""  size="45" >
						</li>
						<li><span>物流公司电话：</span>
	 						<input type ="text" id="lspPhoneNumber" name ="lspPhoneNumber"  maxlength="300"  value=""  size="45" >
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

