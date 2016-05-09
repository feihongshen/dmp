<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.vipshop.VipShop"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
VipShop vipshop = (VipShop)request.getAttribute("vipshopOXOObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>唯品会_oxo接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/vipshopOXO/saveVipShop/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(vipshop != null){ %>
						
						
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no" value="<%=vipshop.getShipper_no() %>"  maxlength="64" >*
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key" value="<%=vipshop.getPrivate_key() %>"  maxlength="64">*
						</li>
						<li><span>每次获取订单数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount" onblur="validate('getMaxCount')"  value="<%=vipshop.getGetMaxCount() %>"  maxlength="6">*
						</li>
						<li><span>每次推送订单数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount" onblur="validate('sendMaxCount')"  value="<%=vipshop.getSendMaxCount() %>"  maxlength="6">
						</li>
						<li><span>获取订单的URL：</span>
							<input type ="text" id="getCwb_URL" name ="getCwb_URL" value="<%=vipshop.getGetCwb_URL() %>"  maxlength="300">*
						</li>
						<li><span>oxo揽收状态URL：</span>
							<input type ="text" id="oxoState_URL" name ="oxoState_URL" value="<%=vipshop.getOxoState_URL() %>"  maxlength="300">
						</li>
						<li style="display: none;"><span>反馈URL：</span>
							<input type ="text" id="sendCwb_URL" name ="sendCwb_URL" value="<%=vipshop.getSendCwb_URL() %>"  maxlength="300">*
						</li>
						<li><span>当前SEQ：</span>
							<input type ="text" id="vipshop_seq" name ="vipshop_seq" onblur="validate('vipshop_seq')"  value="<%=vipshop.getVipshop_seq() %>"  maxlength="11">
						</li>
						<li><span>系统中客户id：</span>
							<input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')" value="<%=vipshop.getCustomerids() %>"  maxlength="11">*
						</li>
						<li><span>是否开启订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1" <%if(vipshop.getIsopendownload()==1){%>checked<%}%>  >开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   <%if(vipshop.getIsopendownload()==0){%>checked<%}%>  >关闭
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(vipshop.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
					<%}else{ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no"  maxlength="64">*
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key"  maxlength="64">*
						</li>
						<li><span>每次获取订单数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount"   maxlength="6" onblur="javascript:validate('getMaxCount')">*
						</li>
						<li><span>每次推送订单数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount"  maxlength="6" onblur="validate('sendMaxCount')">
						</li>
						<li><span>获取订单的URL：</span>
							<input type ="text" id="getCwb_URL" name ="getCwb_URL"   maxlength="300">*
						</li>
						<li><span>oxo揽收状态URL：</span>
							<input type ="text" id="oxoState_URL" name ="oxoState_URL" value=""  maxlength="300">
						</li>
						<li style="display: none;"><span>反馈URL：</span>
							<input type ="text" id="sendCwb_URL" name ="sendCwb_URL"  maxlength="300"/>*
						</li>
						<li><span>当前SEQ：</span>
							<input type ="text" id="vipshop_seq" name ="vipshop_seq" onblur="validate('vipshop_seq')"  value="0"  maxlength="11">
						</li>
						<li><span>系统中客户id：</span>
							<input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')"  maxlength="11">*
						</li>
						<li><span>是否开启订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1"  checked>开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   >关闭
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" >* 
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
						
					<%} %>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"  /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

