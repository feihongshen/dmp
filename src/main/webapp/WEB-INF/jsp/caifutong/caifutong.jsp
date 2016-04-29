<%@page import="cn.explink.b2c.caifutong.CaiFuTong"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
CaiFuTong caifutong = (CaiFuTong)request.getAttribute("caiFuTong");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>财付通对接设置</h1>
		<form id="smile_save_Form">
		<div id="box_form">
			<ul>
				<%if(caifutong != null){ %>
					
					<li><span>商户号：</span>
 						<input type ="text" id="partner" name ="partner"  maxlength="100"  value="<%=caifutong.getPartner()%>"  > 
					</li>
					<li><span>商户号密钥：</span>
 						<input type ="password" id="key" name ="key"  maxlength="50"  value="<%=caifutong.getKey()%>"  > 
					</li>
					<li><span>操作员帐号：</span>
 						<input type ="text" id="opUser" name ="opUser"  maxlength="100"  value="<%=caifutong.getOpUser()%>"  > 
					</li>
					<li><span>操作员密码：</span>
 						<input type ="password" id="opPasswd" name ="opPasswd"  maxlength="50"  value="<%=caifutong.getOpPasswd()%>"  > 
					</li>
					<li  id="ca" style="top:0px;">
						<span>上传CA证书证书：</span>
						<input type="file" id="caifutongca" name="caifutongca" value="<%=caifutong.getCaInfo()%>"/>
					</li>
					<li  id="cer" style="top:0px;">
						<span>上传商户证书：</span>
						<input type="file" id="caifutongcer" name="caifutongcer" value="<%=caifutong.getCertInfo()%>"/>
						<%-- <iframe id="tonglianpfx" name="tonglianpfx" src="tonglian/tonglianpfx?pfxPath=<%=tongLian.getPfxPath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe> --%>
					</li>
					<li><span>商户证书密码：</span>
 						<input type ="password" id="certInfoPasswd" name ="certInfoPasswd"  maxlength="50"  value="<%=caifutong.getCertInfoPasswd()%>"  > 
					</li>
				<%}else{ %>
					<li><span>商户号：</span>
 						<input type ="text" id="partner" name ="partner"  maxlength="100"   > 
					</li>
					<li><span>商户号密钥：</span>
 						<input type ="password" id="key" name ="key"  maxlength="50"   > 
					</li>
					<li><span>操作员帐号：</span>
 						<input type ="text" id="opUser" name ="opUser"  maxlength="100"  > 
					</li>
					<li><span>操作员密码：</span>
 						<input type ="password" id="opPasswd" name ="opPasswd"  maxlength="50"    > 
					</li>
					<li  id="ca" style="top:0px;">
						<span>上传CA证书证书：</span>
						<input type="file" id="caifutongca" name="caifutongca"/>
					</li>
					<li  id="cer" style="top:0px;">
						<span>上传商户证书：</span>
						<input type="file" id="caifutongcer" name="caifutongcer" />
					</li>
					<li><span>商户证书密码：</span>
 						<input type ="password" id="certInfoPasswd" name ="certInfoPasswd"  maxlength="50"  > 
					</li>
				<%} %>
			</ul>
		</div>
		<div align="center"><input type="button" value="保存" class="button" onclick="tongLianSaveForm('smile_save_Form')" /></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
		<input type="hidden" id="savePath" name="savePath" value="<%=request.getContextPath()%>/caifutong/save/${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

