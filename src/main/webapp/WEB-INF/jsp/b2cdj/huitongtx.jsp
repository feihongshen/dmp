<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.huitongtx.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Huitongtx tmall = (Huitongtx)request.getAttribute("huitongtxObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>汇通天下对接设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/httx/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(tmall != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="app_key" name ="app_key" value="<%=tmall.getApp_key() %>"  maxlength="300">
						</li>
					
						<li><span>服务编码：</span>
	 						<input type ="text" id="service_code" name ="service_code"  maxlength="300"  value="<%=tmall.getService_code() %>"  > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=tmall.getPrivate_key()%>"  > 
						</li>
						<li><span>推送状态URL：</span>
	 						<input type ="text" id="post_url" name ="post_url"  maxlength="300"  value="<%=tmall.getPost_url()%>" size="35"   > 
						</li>
						
						<li><span>自己接口URL：</span>
	 						<input type ="text" id="getInfo_url" name ="getInfo_url"  maxlength="300"  value="<%=tmall.getGetInfo_url()%>" size="35"  > 
						</li>
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="selectMaxCount" name ="selectMaxCount"  maxlength="300"  value="<%=tmall.getSelectMaxCount()%>" size="35"  > 
						</li>
						
						
						<li><span>地址匹配开启：</span>
	 						<input type ="radio" id="isopenaddress" name ="isopenaddress"  maxlength="300"  value="1" <%if(tmall.getIsopenaddress()==1){%>checked<%}%>  > 开启
	 						<input type ="radio" id="isopenaddress" name ="isopenaddress"  maxlength="300"  value="0"  <%if(tmall.getIsopenaddress()==0){%>checked<%}%>  > 关闭 
						</li>
						<li><span>地址匹配请求URL：</span>
	 						<input type ="text" id="addressReceiver_url" name ="addressReceiver_url"  maxlength="300"  value="<%=tmall.getAddressReceiver_url()%>" size="35"  > 
						</li>
						<li><span>匹配结果回传URL：</span>
	 						<input type ="text" id="addressSender_url" name ="addressSender_url"  maxlength="300"  value="<%=tmall.getAddressSender_url()%>" size="35"  > 
						</li>
						<li><span>匹配每次传输最大：</span>
	 						<input type ="text" id="addressMaxCount" name ="addressMaxCount"  maxlength="300"  value="<%=tmall.getAddressMaxCount()%>" size="10"  > 
						</li>
						
						
						
						
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=tmall.getCustomerids()%>"  size="15" > 
						</li>
						<li><span>客服电话：</span>
	 						<input type ="text" id="serviceTel" name ="serviceTel"  maxlength="300"  value="<%=tmall.getServiceTel()%>"  size="15" > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(tmall.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="app_key" name ="app_key"   maxlength="300"  >
						</li>
						
						<li><span>服务编码：</span>
	 						<input type ="text" id="service_code" name ="service_code"  maxlength="300"    > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"   > 
						</li>
						<li><span>推送订单URL：</span>
	 						<input type ="text" id="post_url" name ="post_url"  maxlength="300"  size="35"  > 
						
						<li><span>自己接口URL：</span>
	 						<input type ="text" id="getInfo_url" name ="getInfo_url"  maxlength="300"    size="35" > 
						</li>
						
						<li><span>每次查询大小：</span>
	 						<input type ="text" id="selectMaxCount" name ="selectMaxCount"  maxlength="300"  value="" size="35"  > 
						</li>
						
						<li><span>地址匹配开启：</span>
	 						<input type ="radio" id="isopenaddress" name ="isopenaddress"  maxlength="300"  value="1"  > 开启
	 						<input type ="radio" id="isopenaddress" name ="isopenaddress"  maxlength="300"  value="0"  checked > 关闭 
						</li>
						<li><span>地址匹配请求URL：</span>
	 						<input type ="text" id="addressReceiver_url" name ="addressReceiver_url"  maxlength="300"  value="" size="35"  > 
						</li>
						<li><span>匹配结果回传URL：</span>
	 						<input type ="text" id="addressSender_url" name ="addressSender_url"  maxlength="300"  value="" size="35"  > 
						</li>
						<li><span>匹配每次传输最大：</span>
	 						<input type ="text" id="addressMaxCount" name ="addressMaxCount"  maxlength="300"  value="" size="10"  > 
						</li>
						
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"    size="15" > 
						</li>
						<li><span>客服电话：</span>
	 						<input type ="text" id="serviceTel" name ="serviceTel"  maxlength="300"  value=""  size="15" > 
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
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
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

