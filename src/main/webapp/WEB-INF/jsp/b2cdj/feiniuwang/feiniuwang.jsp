<%@page import="cn.explink.b2c.feiniuwang.FeiNiuWang"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.feiniuwang.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
FeiNiuWang smile = (FeiNiuWang)request.getAttribute("feiniuwang");
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
		<h1><div id="close_box" onclick="closeBox()"></div>飞牛网对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/feiniuwang/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(smile != null){ %>
						
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value="<%=smile.getFeedbackUrl()==null?"":smile.getFeedbackUrl()%>"  > 
						</li>
						<li><span>requestKey：</span>
	 						<input type ="text" id="requestKey" name ="requestKey"  maxlength="300"  value="<%=smile.getRequestKey()==null?"":smile.getRequestKey()%>"  > 
						</li>
						<li><span>responseKey：</span>
	 						<input type ="text" id="responseKey" name ="responseKey"  maxlength="300"  value="<%=smile.getResponseKey()==null?"":smile.getResponseKey()%>"  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=smile.getMaxCount()==null?"":smile.getMaxCount()%>"    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=smile.getCustomerid()==null?"":smile.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>承运商编码：</span>
	 						<input type ="text" id="dmsCode" name ="dmsCode"  maxlength="300"  value="<%=smile.getDmsCode()==null?"":smile.getDmsCode()%>"  size="15" > 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(smile.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value=""  > 
						</li>
						<li><span>requestKey：</span>
	 						<input type ="text" id="requestKey" name ="requestKey"  maxlength="300"  value=""  > 
						</li>
						<li><span>responseKey：</span>
	 						<input type ="text" id="responseKey" name ="responseKey"  maxlength="300"  value=""  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>承运商编码：</span>
	 						<input type ="text" id="dmsCode" name ="dmsCode"  maxlength="300"  value=""  size="15" > 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
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

