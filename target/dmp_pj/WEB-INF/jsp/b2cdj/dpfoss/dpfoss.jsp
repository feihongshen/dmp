<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.dpfoss.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Dpfoss dpfoss = (Dpfoss)request.getAttribute("dpfossobject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>德邦物流接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/dpfoss/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(dpfoss != null){ %>
						
						
						<li><span>代理公司名称：</span>
	 						<input type ="text" id="agentCompanyName" name ="agentCompanyName"  maxlength="300" size="15" value="<%=dpfoss.getAgentCompanyName()%>"  > 
						</li>
						<li><span>代理公司编码：</span>
	 						<input type ="text" id="agentCompanyCode" name ="agentCompanyCode"  maxlength="300" size="15" value="<%=dpfoss.getAgentCompanyCode()%>"  > 
						</li>
						<li><span>网点名称：</span>
	 						<input type ="text" id="agentOrgName" name ="agentOrgName"  maxlength="300" size="15" value="<%=dpfoss.getAgentOrgName()%>"  > 
						</li>
						<li><span>网点编码：</span>
	 						<input type ="text" id="agentOrgCode" name ="agentOrgCode"  maxlength="300" size="15" value="<%=dpfoss.getAgentOrgCode()%>"  > 
						</li>
						<li><span>服务编码(外发单)：</span>
	 						<input type ="text" id="serviceCode_queryWaybill" name ="serviceCode_queryWaybill"  maxlength="300" size="35" value="<%=dpfoss.getServiceCode_queryWaybill()%>"  > 
						</li>
						<li><span>请求URL(外发单)：</span>
	 						<input type ="text" id="queryWaybill_url" name ="queryWaybill_url"  maxlength="300" size="35" value="<%=dpfoss.getQueryWaybill_url()%>"  > 
						</li>
						<li><span>服务编码(签收单)：</span>
	 						<input type ="text" id="serviceCode_uploadSign" name ="serviceCode_uploadSign"  maxlength="300" size="35" value="<%=dpfoss.getServiceCode_uploadSign()%>"  > 
						</li>
						<li><span>请求URL(签收单)：</span>
	 						<input type ="text" id="uploadSign_url" name ="uploadSign_url"  maxlength="300" size="35" value="<%=dpfoss.getUploadSign_url()%>"  > 
						</li>
						<li><span>服务编码(跟踪)：</span>
	 						<input type ="text" id="serviceCode_uploadTrack" name ="serviceCode_uploadTrack"  maxlength="300" size="35" value="<%=dpfoss.getServiceCode_uploadTrack()%>"  > 
						</li>
						<li><span>请求URL(跟踪)：</span>
	 						<input type ="text" id="uploadTrack_url" name ="uploadTrack_url"  maxlength="300" size="35" value="<%=dpfoss.getUploadTrack_url()%>"  > 
						</li>
						
						
						<li><span>用户名：</span>
	 						<input type ="text" id="agent" name ="agent"  maxlength="300" size="15" value="<%=dpfoss.getAgent()%>"  > 
						</li>
						<li><span>访问密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300" size="15" value="<%=dpfoss.getPwd()%>"  > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value="<%=dpfoss.getPrivate_key()%>"  > 
						</li>
						
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value="<%=dpfoss.getCustomerids()%>"   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=dpfoss.getMaxCount()%>"  size="15"> 
						</li>
						
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(dpfoss.getWarehouseid()==b.getBranchid()){%>selected<%}%> ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>代理公司名称：</span>
	 						<input type ="text" id="agentCompanyName" name ="agentCompanyName"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>代理公司编码：</span>
	 						<input type ="text" id="agentCompanyCode" name ="agentCompanyCode"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>网点名称：</span>
	 						<input type ="text" id="agentOrgName" name ="agentOrgName"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>网点编码：</span>
	 						<input type ="text" id="agentOrgCode" name ="agentOrgCode"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>服务编码(外发单)：</span>
	 						<input type ="text" id="serviceCode_queryWaybill" name ="serviceCode_queryWaybill"  maxlength="300" size="35" value=""  > 
						</li>
						<li><span>请求URL(外发单)：</span>
	 						<input type ="text" id="queryWaybill_url" name ="queryWaybill_url"  maxlength="300" size="35" value=""  > 
						</li>
						<li><span>服务编码(签收单)：</span>
	 						<input type ="text" id="serviceCode_uploadSign" name ="serviceCode_uploadSign"  maxlength="300" size="35" value=""  > 
						</li>
						<li><span>请求URL(签收单)：</span>
	 						<input type ="text" id="uploadSign_url" name ="uploadSign_url"  maxlength="300" size="35" value=""  > 
						</li>
						<li><span>服务编码(跟踪)：</span>
	 						<input type ="text" id="serviceCode_uploadTrack" name ="serviceCode_uploadTrack"  maxlength="300" size="35" value=""  > 
						</li>
						<li><span>请求URL(跟踪)：</span>
	 						<input type ="text" id="uploadTrack_url" name ="uploadTrack_url"  maxlength="300" size="35" value=""  > 
						</li>
						
						<li><span>用户名：</span>
	 						<input type ="text" id="agent" name ="agent"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>访问密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key"  maxlength="300" size="15" value=""  > 
						</li>
						
						
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15"> 
						</li>
						
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>"  ><%=b.getBranchname() %></option>
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

