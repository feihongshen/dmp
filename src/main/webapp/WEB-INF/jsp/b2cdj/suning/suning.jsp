<%@page import="cn.explink.b2c.suning.SuNing"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
SuNing suning = (SuNing)request.getAttribute("suning");
List<Branch> warehouselist = (List<Branch>)request.getAttribute("warehouselist");

%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>苏宁易购对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/suning/save/${joint_num}" method="post">
		<div id="box_form">
			<ul>
				<%if(suning != null){ %>
					
					<li><span>线程处理条数：</span>
 						<input type ="text" id="everythreaddonum" name ="everythreaddonum"  maxlength="300"  value="<%=suning.getEverythreaddonum()%>"  > 
					</li>
					<li><span>私钥信息：</span>
 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="<%=suning.getPrivate_key()%>"  > 
					</li>
					<li><span>苏宁id：</span>
 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=suning.getCustomerid()%>"  size="15" > 
					</li>
					<li><span>苏宁合作商编码：</span>
 						<input type ="text" id="spCode" name ="spCode"  maxlength="300"  value="<%=suning.getSpCode()%>"  size="15" > 
					</li>
					
					
					
					<li><span>货态反馈数量：</span>
 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="<%=suning.getMaxcount()%>" > 
					</li>
					<li><span>接收URL：</span>
 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value="<%=suning.getImportUrl()%>" /> 
					</li>
					<li><span>反馈URL：</span>
 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value="<%=suning.getFeedbackUrl()%>" /> 
					</li>
					<li><span>订单导入库房：</span>
						<select name="warehouseid">
							<option value="0">请选择库房</option>
							<%for(Branch b:warehouselist){%>
								<option value="<%=b.getBranchid()%>" <%if(suning.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
							<%}%>
						</select>
					</li>
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value="explink" maxlength="30" size="20" > 
					</li>
				<%}else{ %>
					<li><span>线程处理条数：</span>
 						<input type ="text" id="everythreaddonum" name ="everythreaddonum"  maxlength="300"  value="" > 
					</li>
					<li><span>私钥信息：</span>
 						<input type ="text" id="private_key" name ="private_key"  maxlength="300"  value="" > 
					</li>
					<li><span>苏宁id：</span>
 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
					</li>
					<li><span>苏宁合作商编码：</span>
 						<input type ="text" id="spCode" name ="spCode"  maxlength="300"  value=""  size="15" > 
					</li>
					<li><span>货态反馈数量：</span>
 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300" value="" > 
					</li>
					<li><span>接收URL：</span>
 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value="" > 
					</li>
					<li><span>反馈URL：</span>
 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value="" > 
					</li>
					<li><span>订单导入库房：</span>
						<select name="warehouseid">
							<option value="0">请选择库房</option>
							<%for(Branch b:warehouselist){%>
								<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
							<%}%>
						</select>
					</li>
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value="explink"  maxlength="30" size="20" > 
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

