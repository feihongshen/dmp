<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.maikaolin.Maikolin"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	Maikolin mkl = (Maikolin)request.getAttribute("maikaolinObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>麦考林接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/maikaolin/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(mkl != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value="<%=mkl.getUserCode() %>"  maxlength="300">
						</li>
						<li><span>key_set：</span>
	 						<input type ="text" id="key" name ="key" maxlength="300"   value="<%=mkl.getPrivate_key()%>"   > 
						</li>
						<li><span>推送URL：</span>
	 						<input type ="text" id="pushCwb_URL" name ="pushCwb_URL" maxlength="300"   value="<%=mkl.getPushCwb_URL()%>"   > 
						</li>
						<li><span>express_id：</span>
	 						<input type ="text" id="express_id" name ="express_id" maxlength="300"  size="15"  value="<%=mkl.getExpress_id()%>"  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value="<%=mkl.getCallBackCount()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=mkl.getCustomerids()%>"  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
						</li>
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(mkl.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value=""  maxlength="300">
						</li>
						<li><span>key_set：</span>
	 						<input type ="text" id="key" name ="key" maxlength="300"   value=""   > 
						</li>
						<li><span>推送URL：</span>
	 						<input type ="text" id="exportCwb_URL" name ="pushCwb_URL" maxlength="300"   value=""   > 
						</li>
						<li><span>express_id：</span>
	 						<input type ="text" id="express_id" name ="express_id" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>每次回调订单数量：</span>
	 						<input type ="text" id="callBackCount" name ="callBackCount" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
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
