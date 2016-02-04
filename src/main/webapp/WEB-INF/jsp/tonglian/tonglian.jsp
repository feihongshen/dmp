<%@page import="cn.explink.b2c.tonglian.TongLian"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
TongLian tongLian = (TongLian)request.getAttribute("tongLian");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>通联对接设置</h1>
		<form id="smile_save_Form">
		<div id="box_form">
			<ul>
				<%if(tongLian != null){ %>
					
					<li><span>用户名：</span>
 						<input type ="text" id="userName" name ="userName"  maxlength="100"  value="<%=tongLian.getUserName()%>"  > 
					</li>
					<li><span>用户密码：</span>
 						<input type ="password" id="userPass" name ="userPass"  maxlength="50"  value="<%=tongLian.getUserPass()%>"  > 
					</li>
					<li  id="cer" style="top:0px;">
						<span>上传安全证书：</span>
						<input type="file" id="tongliancer" name="tongliancer" value="<%=tongLian.getTltcerPath()%>"/>
						<%-- <iframe id="tongliancer" name="tongliancer" src="tonglian/tongliancer?&tltcerPath=<%=tongLian.getTltcerPath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe> --%>
					</li>
					<li  id="pfx" style="top:0px;">
						<span>上传商户证书：</span>
						<input type="file" id="tonglianpfx" name="tonglianpfx" value="<%=tongLian.getPfxPath()%>"/>
						<%-- <iframe id="tonglianpfx" name="tonglianpfx" src="tonglian/tonglianpfx?pfxPath=<%=tongLian.getPfxPath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe> --%>
					</li>
					<li><span>商户证书密码：</span>
 						<input type ="password" id="pfxPassword" name ="pfxPassword"  maxlength="50"  value="<%=tongLian.getPfxPassword()%>"  > 
					</li>
					<li><span>商户代码：</span>
 						<input type ="text" id="merchantId" name ="merchantId"  maxlength="100"  value="<%=tongLian.getMerchantId()%>" > 
					</li>
				<%}else{ %>
				<li><span>用户名：</span>
 						<input type ="text" id="userName" name ="userName"  maxlength="100"  > 
					</li>
					<li><span>用户密码：</span>
 						<input type ="password" id="userPass" name ="userPass"  maxlength="50"   > 
					</li>
					<li  id="cer" style="top:0px;">
						<span>上传安全证书：</span>
						<input type="file" id="tongliancer" name="tongliancer"/>
						<%-- <iframe id="tongliancer" name="tongliancer" src="b2cdj/tonglian/tongliancer?tltcerPath=<%=tongLian.getTltcerPath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe> --%>
					</li>
					<li  id="pfx" style="top:0px;">
						<span>上传商户证书：</span>
						<input type="file" id="tonglianpfx" name="tonglianpfx"/>
						<%-- <iframe id="tonglianpfx" name="tonglianpfx" src="b2cdj/tonglian/tonglianpfx?fromAction=branch_cre_Form&pfxPath=<%=tongLian.getPfxPath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe> --%>
					</li>
					<li><span>商户证书密码：</span>
 						<input type ="password" id="pfxPassword" name ="pfxPassword"  maxlength="50"   > 
					</li>
					<li><span>商户代码：</span>
 						<input type ="text" id="merchantId" name ="merchantId"  maxlength="100" > 
					</li>
				<%} %>
			</ul>
		</div>
		<div align="center"><input type="button" value="保存" class="button" onclick="tongLianSaveForm('smile_save_Form')" /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
		<input type="hidden" id="savePath" name="savePath" value="<%=request.getContextPath()%>/tonglian/save/${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

