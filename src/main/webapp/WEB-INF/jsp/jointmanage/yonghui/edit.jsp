<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.b2c.yonghui.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
YongHui yonghui=(YongHui)request.getAttribute("yonghui");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
List<Customer> customers=(List<Customer>)request.getAttribute("customerlist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>永辉对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/yonghui/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>客户代码：</span>
 						<input type ="text" id="userCode" name ="userCode" value ="<%=StringUtil.nullConvertToEmptyString(yonghui.getUserCode())%>" maxlength="30"  > 
					</li>
					<li><span>分配密钥：</span>
 						<input type ="text" id="private_key" name ="private_key" value ="<%=StringUtil.nullConvertToEmptyString(yonghui.getPrivate_key())%>" maxlength="1000"  > 
					<li><span>用户代码：</span>
 						<input type ="text" id="clientID" name ="clientID" value ="<%=StringUtil.nullConvertToEmptyString(yonghui.getClient_id())%>" maxlength="1000"  > 
					</li>
					<li><span>用户密码：</span>
 						<input type ="text" id="clientSecret" name ="clientSecret" value ="<%=StringUtil.nullConvertToEmptyString(yonghui.getSecret())%>" maxlength="1000"  > 
					</li>
					<li><span>订单状态url：</span>
 						<input type ="text" id="orderState_url" name ="orderState_url" value ="<%=StringUtil.nullConvertToEmptyString(yonghui.getOrderState_url())%>" maxlength="1000"  > 
					</li>
				

					
					<li><span>推送数量：</span>
 						<input type ="text" id="orderStateCount" name ="orderStateCount" value ="<%=yonghui.getOrderStateCount()%>" maxlength="1000"  > 
					</li>
					
					<li><span>供货商：</span>
							<select name="customerid">
								<option value="0">请选择</option>
								<%for(Customer c:customers){
								%>
									<option value="<%=c.getCustomerid()%>" <%if(yonghui.getCustomerid()==c.getCustomerid()){%>selected<%}%>><%=c.getCustomername() %></option>
								<%}%>
							</select>
						</li>
					<li><span>订单导入库房：</span>
							<select name="warehouserid">
								<option value="0">请选择</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(yonghui.getWarehouserid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value ="" maxlength="30"  > 
					</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

