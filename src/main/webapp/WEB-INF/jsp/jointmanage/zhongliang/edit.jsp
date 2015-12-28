<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.b2c.zhongliang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%

Zhongliang zhongliang=(Zhongliang)request.getAttribute("zhongliang");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
List<Customer> customerList=(List<Customer>)request.getAttribute("customerList");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>中粮对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/zhongliang/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>客户代码：</span>
 						<input type ="text" id="clientId" name ="clientId" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getClientId())%>" maxlength="30"  > 
					</li>
					<li><span>客户标识：</span>
 						<input type ="text" id="clientFlag" name ="clientFlag" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getClientFlag())%>" maxlength="1000"  > 
					</li>
					<li><span>分配密钥：</span>
 						<input type ="text" id="clientKey" name ="clientKey" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getClientKey())%>" maxlength="1000"  > 
					</li>
						<li><span>常量：</span>
 						<input type ="text" id="clientConst" name ="clientConst" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getClientConst())%>" maxlength="1000"  > 
					</li>
					<li><span>订单接收url：</span>
 						<input type ="text" id="waitOrder_url" name ="waitOrder_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getWaitOrder_url())%>" maxlength="1000"  > 
					</li>
					<li><span>订单取消url：</span>
 						<input type ="text" id="cancleOrder_url" name ="cancleOrder_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getCancleOrder_url())%>" maxlength="1000"  > 
					</li>
					<li><span>订单状态url：</span>
 						<input type ="text" id="orderStatus_url" name ="orderStatus_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getOrderStatus_url())%>" maxlength="1000"  > 
					</li>
					<li><span>意向单接收url：</span>
 						<input type ="text" id="backOrder_url" name ="backOrder_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getBackOrder_url())%>" maxlength="1000"  > 
					</li>
					<li><span>意向单取消url：</span>
 						<input type ="text" id="backCancel_url" name ="backCancel_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getBackCancel_url())%>" maxlength="1000"  > 
					</li>
					<li><span>意向单状态url：</span>
 						<input type ="text" id="backOrderStatus_url" name ="backOrderStatus_url" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getBackOrderStatus_url())%>" maxlength="1000"  > 
					</li>
					
					<li><span>推送数量：</span>
 						<input type ="text" id="nums" name ="nums" value ="<%=StringUtil.nullConvertToEmptyString(zhongliang.getNums())%>" maxlength="1000"  > 
					</li>
					
					<li><span>供货商ID：</span>
						<select name="customerid" id="customerid">
								<option value="0">请选择供货商</option>
								<%for(Customer c:customerList){
								%>
									<option value="<%=c.getCustomerid()%>" <%if(StringUtil.nullConvertToEmptyString(zhongliang.getCustomerid()).equals(c.getCustomerid()+"")){%>selected<%}%>><%=c.getCustomername() %></option>
								<%}%>
							</select>
					</li>
					<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(zhongliang.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
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

