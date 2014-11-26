<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.efast.*"%>
<%@page import="cn.explink.domain.Branch"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Efast efast = (Efast)request.getAttribute("efastObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>中兴云购对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/efast/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(efast != null){ %>
						<li><span>app_key：</span>
							<input type ="text" id="app_key" name ="app_key" value="<%=efast.getApp_key()%>" size="20" maxlength="300">
						</li>
						<li><span>app_secret(密钥)：</span>
	 						<input type ="text" id="app_secret" name ="app_secret" maxlength="300"  size="20"  value="<%=efast.getApp_secret() %>"  > 
						</li>
						<li><span>app_nick(标识)：</span>
	 						<input type ="text" id="app_nick" name ="app_nick"  maxlength="300" size="20"  value="<%=efast.getApp_nick() %>"  > 
						</li>
						
						<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;获取&nbsp;
							<input type ="text" id="beforhours" name ="beforhours" size="5" value="<%=efast.getBeforhours() %>"  maxlength="4">&nbsp;小时前的数据
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  size="20"  value="<%=efast.getLoopcount() %>"  > 
						</li>
						
						<li><span>获取订单url：</span>
	 						<input type ="text" id="app_url" name ="app_url" maxlength="300"   value="<%=efast.getApp_url()%>"   > 
						</li>
						<li><span>配送方式code：</span>
	 						<input type ="text" id="shipping_code" name ="shipping_code" maxlength="300"   value="<%=efast.getShipping_code()%>"   > 
						</li>
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=efast.getCustomerid()%>" size="20"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(efast.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>ERP枚举：</span>
	 						<input type ="text" id="erpEnum" name ="erpEnum"  maxlength="300"  value="<%=efast.getErpEnum()%>" size="20"   > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>app_key：</span>
							<input type ="text" id="app_key" name ="app_key" value="" size="20" maxlength="300">
						</li>
						<li><span>app_secret(密钥)：</span>
	 						<input type ="text" id="app_secret" name ="app_secret" maxlength="300" size="20"  value=""  > 
						</li>
						<li><span>app_nick(标识)：</span>
	 						<input type ="text" id="app_nick" name ="app_nick"  maxlength="300" size="20"  value=""  > 
						</li>
						<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;获取&nbsp;
							<input type ="text" id="beforhours" name ="beforhours" size="5" value=""  maxlength="4">&nbsp;小时前的数据
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  size="5"  value="0"  > 
						</li>
						<li><span>获取订单url：</span>
	 						<input type ="text" id="app_url" name ="app_url" maxlength="300"   value=""   > 
						</li>
						<li><span>配送方式code：</span>
	 						<input type ="text" id="shipping_code" name ="shipping_code" maxlength="300"   value="01"   > 
						</li>
						
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="" size="20"  > 
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
						<li><span>ERP枚举：</span>
	 						<input type ="text" id="erpEnum" name ="erpEnum"  maxlength="300"  value="" size="20"   > 
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

