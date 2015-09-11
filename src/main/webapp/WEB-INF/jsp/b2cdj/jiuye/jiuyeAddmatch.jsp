<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.jiuye.addressmatch.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
JiuYeAddressMatch jiuye = (JiuYeAddressMatch)request.getAttribute("jiuyeAddMatch");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>九曳站点匹配设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/jiuyeaddressmatch/saveJiuye/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(jiuye != null){ %>
						<li><span>receiver_url:</span>
							<input type ="text" id="receiver_url" name ="receiver_url" value="<%=jiuye.getReceiver_url()==null?"":jiuye.getReceiver_url() %>"  >
						</li>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="customerid" name ="customerid" value="<%=jiuye.getCustomerid() %>"  maxlength="300">
						</li>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value="<%=jiuye.getPrivate_key()%>"   > 
						</li>
						<li><span>承运商编码：</span>
	 						<input type ="text" id="dmsCode" name ="dmsCode" maxlength="300"   value="<%=jiuye.getDmsCode()==null?"":jiuye.getDmsCode()%>"   > 
						</li>
						<li><span>每次最大请求数：</span>
	 						<input type ="text" id="maxCount" name ="maxCount" maxlength="4"  size="10"  value="<%=jiuye.getMaxCount()%>"  > 
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
					<li><span>receiver_url:</span>
							<input type ="text" id="ppUrl" name ="ppUrl" value=""  >
						</li>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="customerid" name ="customerid" value=""  maxlength="300">
						</li>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value=""   > 
						</li>
						<li><span>承运商编码：</span>
	 						<input type ="text" id="dmsCode" name ="dmsCode" maxlength="300"   value=""  > 
						</li>
						<li><span>每次最大请求数：</span>
	 						<input type ="text" id="maxCount" name ="maxCount" maxlength="4"  size="10"  value=""  > 
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

