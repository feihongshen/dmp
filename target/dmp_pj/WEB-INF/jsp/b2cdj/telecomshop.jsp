<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.telecomsc.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Telecomshop lt = (Telecomshop)request.getAttribute("telecomObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>电信商城接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/telecom/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(lt != null){ %>
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value="<%=lt.getCustomerid()%>"   > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value="<%=lt.getPrivate_key()%>"  > 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="receiver_url" name ="receiver_url"  maxlength="300" size="40"  value="<%=lt.getReceiver_url()%>"  > 
						</li>
						<li><span>回传URL：</span>
	 						<input type ="text" id="sender_url" name ="sender_url"  maxlength="300"  value="<%=lt.getSender_url()%>"  size="40"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=lt.getMaxCount()%>"  size="15"> 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(lt.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
							<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="receiver_url" name ="receiver_url"  maxlength="300" size="40" value=""  > 
						</li>
						<li><span>回传URL：</span>
	 						<input type ="text" id="sender_url" name ="sender_url"  maxlength="300"  value=""  size="40"> 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15"> 
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

