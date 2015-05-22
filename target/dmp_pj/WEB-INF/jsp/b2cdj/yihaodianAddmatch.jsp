<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.yihaodian.addressmatch.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
YihaodianAddMatch yihaodian = (YihaodianAddMatch)request.getAttribute("yihaodianObject");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>一号店站点匹配设置</h1>
		<form id="tmall_save_Form" name="tmall_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/yhdAddressmatch/saveYihaodian/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(yihaodian != null){ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value="<%=yihaodian.getUserCode() %>"  maxlength="300">
						</li>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value="<%=yihaodian.getPrivate_key()%>"   > 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="receiver_url" name ="receiver_url" maxlength="300"  size="45"  value="<%=yihaodian.getReceiver_url()%>"  > 
						</li>
						<li><span>每次最大请求数：</span>
	 						<input type ="text" id="maxCount" name ="maxCount" maxlength="4"  size="10"  value="<%=yihaodian.getMaxCount()%>"  > 
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>物流公司id标识：</span>
							<input type ="text" id="userCode" name ="userCode" value=""  maxlength="300">
						</li>
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value=""   > 
						</li>
						<li><span>接收URL：</span>
	 						<input type ="text" id="receiver_url" name ="receiver_url" maxlength="300"  size="45"  value=""  > 
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

