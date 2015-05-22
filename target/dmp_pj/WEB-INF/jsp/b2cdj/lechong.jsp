<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.lechong.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Lechong lechong = (Lechong)request.getAttribute("lechongObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
String checkMd5="";
if(lechong!=null){
	checkMd5=lechong.getCheckMd5()==null||lechong.getCheckMd5().equals("")?"no":lechong.getCheckMd5();
}%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>乐宠对接设置</h1>
		<form id="lechong_save_Form" name="lechong_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/lechong/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(lechong != null){ %>
						<li><span>配送公司标识：</span>
	 						<input type ="text" id="dcode" name ="dcode"  maxlength="300"  value="<%=lechong.getDcode()%>"  size="15" > 
						</li>
						<li><span>配送公司名称：</span>
	 						<input type ="text" id="dname" name ="dname"  maxlength="300"  value="<%=lechong.getDname()%>"  size="15" > 
						</li>
						<li><span>是否进行MD5验证：</span>
	 						是<input type ="radio" id="checkMd51" name ="checkMd5"  maxlength="300"  value="yes" <%if(checkMd5.equals("yes")) {%>checked='checked'<%} %>> 
	 						否<input type ="radio" id="checkMd52" name ="checkMd5"  maxlength="300"  value="no" <%if(checkMd5.equals("no")) {%>checked='checked'<%} %>> 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value="<%=lechong.getImportUrl()%>"  > 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value="<%=lechong.getFeedbackUrl()%>"  > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="secretKey" name ="secretKey"  maxlength="300"  value="<%=lechong.getSecretKey() %>"  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value="<%=lechong.getMaxCount()%>"    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=lechong.getCustomerids()%>"  size="15" > 
						</li>
					
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(lechong.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>配送公司标识：</span>
	 						<input type ="text" id="dcode" name ="dcode"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>配送公司名称：</span>
	 						<input type ="text" id="dname" name ="dname"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否进行MD5验证：</span>
	 						是<input type ="radio" id="checkMd51" name ="checkMd5"  maxlength="300"  value="yes"  > 
	 						否<input type ="radio" id="checkMd52" name ="checkMd5"  maxlength="300"  value="no" > 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="importUrl" name ="importUrl"  maxlength="300"  value=""  > 
						</li>
						<li><span>反馈URL：</span>
	 						<input type ="text" id="feedbackUrl" name ="feedbackUrl"  maxlength="300"  value=""  > 
						</li>
						<li><span>私钥信息：</span>
	 						<input type ="text" id="secretKey" name ="secretKey"  maxlength="300"  value=""  > 
						</li>
						<li><span>每次反馈数量：</span>
	 						<input type ="text" id="maxcount" name ="maxcount"  maxlength="300"  value=""    > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
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

