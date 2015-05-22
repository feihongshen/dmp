<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.yixun.*"%>
<%@page import="cn.explink.domain.Branch"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
YiXun yixun = (YiXun)request.getAttribute("yixunObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>易迅网对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yixun/saveYiXun/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(yixun != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="apikey" name ="apikey" value="<%=yixun.getApikey() %>"  maxlength="300">
						</li>
						<li><span>接收订单URL：</span>
	 						<input type ="text" id="request_url" name ="request_url" maxlength="300"   value="<%=yixun.getRequest_url()%>"   > 
						</li>
						<li><span>反馈状态URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value="<%=yixun.getFeedback_url() %>"  > 
						</li>
						<li><span>每次推送数量：</span>
	 						<input type ="text" id="count" name ="count"  maxlength="300" size="15"  value="<%=yixun.getCount() %>"  > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="secret" name ="secret"  maxlength="300"  value="<%=yixun.getSecret()%>"  > 
						</li>
						<li><span>customerids：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=yixun.getCustomerids()%>" size="35"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(yixun.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="apikey" name ="apikey" value=""  maxlength="300">
						</li>
						<li><span>接收订单URL：</span>
	 						<input type ="text" id="request_url" name ="request_url" maxlength="300"   value=""   > 
						</li>
						<li><span>反馈状态URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value=""  > 
						</li>
						<li><span>每次推送数量：</span>
	 						<input type ="text" id="count" name ="count"  maxlength="300"  size="15" value=""  > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="secret" name ="secret"  maxlength="300"  value=""  > 
						</li>
						<li><span>customerids：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="" size="35"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ></option>
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

