<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.vipshop.address.*"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	VipShopAddress vipshop = (VipShopAddress)request.getAttribute("vipshopObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>唯品会地址库对接设置</h1>
		<form id="vipshop_save_Form" name="vipshop_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/address/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(vipshop != null){ %>
						<li><span>承运商账户：</span>
							<input type ="text" id="shipper_no" name ="shipper_no" value="<%=vipshop.getShipper_no() %>"  maxlength="300">
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key" value="<%=vipshop.getPrivate_key() %>"  maxlength="300">
						</li>
						<li><span>每次获取数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount" value="<%=vipshop.getGetMaxCount() %>"  maxlength="300">
						</li>
						
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%}else{ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no"  maxlength="300">
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key"  maxlength="300">
						</li>
						<li><span>每次获取数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount"   maxlength="300">
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

