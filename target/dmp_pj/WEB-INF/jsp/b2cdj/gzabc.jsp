<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.gzabc.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
GuangZhouABC gzabc = (GuangZhouABC)request.getAttribute("gzabcObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>广州ABC接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/gzabc/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(gzabc != null){ %>
						
						
						<li><span>标识id-下单：</span>
	 						<input type ="text" id="shippedCode" name ="shippedCode"  maxlength="300" size="15" value="<%=gzabc.getShippedCode()%>"  > 
						</li>
						<li><span>标识id-反馈：</span>
	 						<input type ="text" id="logisticProviderID" name ="logisticProviderID"  maxlength="300" size="15" value="<%=gzabc.getLogisticProviderID()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value="<%=gzabc.getCustomerids()%>"   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=gzabc.getMaxCount()%>"  size="15"> 
						</li>
						<li><span>提供ABC-URL：</span>
	 						<input type ="text" id="requst_url" name ="requst_url"  maxlength="300"  value="<%=gzabc.getRequst_url()%>"  size="35"> 
						</li>
						<li><span>反馈ABC-URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url"  maxlength="300"  value="<%=gzabc.getFeedback_url()%>"  size="35"> 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=gzabc.getPrivate_key()%>"  size="35"> 
						</li>
						<li><span>订单导入库房：</span>
							<select name="exportbranchid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(gzabc.getExportbranchid()==b.getBranchid()){%>selected<%}%> ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>标识id-下单：</span>
	 						<input type ="text" id="shippedCode" name ="shippedCode"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>标识id-反馈：</span>
	 						<input type ="text" id="logisticProviderID" name ="logisticProviderID"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>提供ABC-URL：</span>
	 						<input type ="text" id="requst_url" name ="requst_url"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>反馈ABC-URL：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>订单导入库房：</span>
							<select name="exportbranchid">
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

