<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.tmall.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Tmall tmall = (Tmall)request.getAttribute("tmallObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>天猫对接设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/tmall/saveTmall/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(tmall != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="partner" name ="partner" value="<%=tmall.getPartner() %>"  maxlength="300">
						</li>
						<li><span>物流公司编号：</span>
	 						<input type ="text" id="tms_service_code" name ="tms_service_code" maxlength="300" size="15"  value="<%=tmall.getTms_service_code()%>"   > 
						</li>
						<li><span>外部业务编码：</span>
	 						<input type ="text" id="out_biz_code" name ="out_biz_code" maxlength="300"   value="<%=tmall.getOut_biz_code() %>"  > 
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
						<li><span>异常回传URL：</span>
	 						<input type ="text" id="expt_url" name ="expt_url"  maxlength="300"  value="<%=tmall.getExpt_url()%>" size="35"  > 
						</li>
						<li><span>参数格式：</span>
	 						<select name="data_format">
	 							<option value="XML" <%if("XML".equals(tmall.getData_format())){%>selected <%}%>>XML</option>
	 							<option value="JSON"  <%if("JSON".equals(tmall.getData_format())){%>selected <%}%>>JSON</option>
	 						</select>
						</li>
						<li><span>是否回传揽收：</span>
	 						<select name="isCallBackAccept">
	 							<option value="1" <%if(tmall.getIsCallBackAccept()==1){%>selected <%}%>>回传</option>
	 							<option value="0"  <%if(tmall.getIsCallBackAccept()==0){%>selected <%}%>>不回传</option>
	 						</select>
						</li>
						<li><span>揽收标准：</span>
	 						<select name="acceptflag">
	 							<option value="1" <%if(tmall.getAcceptflag()==1){%>selected <%}%>>以数据导入作为揽收标注</option>
	 							<option value="4" <%if(tmall.getAcceptflag()==4){%>selected <%}%>>以库房入库作为揽收标准</option>
	 						</select>
						</li>
						
						<li><span>TMS_ERROR：</span>
						<input type="radio" name="isCallBackError"  id="isCallBackError1"  value="0" <%if(tmall.getIsCallBackError()==0){%>checked<%} %> />回传
						<input type="radio" name="isCallBackError"  id="isCallBackError2"   <%if(tmall.getIsCallBackError()==1){%>checked<%} %>  value="1"/>不回传&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;代表新的异常推送模式
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
							<input type ="text" id="partner" name ="partner"   maxlength="300"  >
						</li>
						<li><span>物流公司编号：</span>
	 						<input type ="text" id="tms_service_code" name ="tms_service_code" maxlength="300"  size="15"   > 
						</li>
						<li><span>外部业务编码：</span>
	 						<input type ="text" id="out_biz_code" name ="out_biz_code" maxlength="300"    > 
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
						<li><span>异常回传URL：</span>
	 						<input type ="text" id="expt_url" name ="expt_url"  maxlength="300"  value="" size="35"  > 
						</li>
						
						<li><span>是否回传揽收：</span>
	 						<select name="isCallBackAccept">
	 							<option value="1">回传</option>
	 							<option value="0" >不回传</option>
	 						</select>
						</li>
						<li><span>揽收标准：</span>
	 						<select name="acceptflag">
	 							<option value="1">以数据导入作为揽收标注</option>
	 							<option value="4">以库房入库作为揽收标准</option>
	 						</select>
						</li>
						<li><span>TMS_ERROR：</span>
						<input type="radio" name="isCallBackError"  id="isCallBackError1" value="0"  checked  />回传
						<input type="radio" name="isCallBackError"  id="isCallBackError2"   value="1" />不回传&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;代表新的异常推送模式
						</li>
						
						<li><span>参数格式：</span>
	 						<select name="data_format">
	 							<option value="XML">XML</option>
	 							<option value="JSON">JSON</option>
	 						</select>
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

