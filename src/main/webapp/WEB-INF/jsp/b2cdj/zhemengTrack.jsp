<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.b2c.zhemeng.track.ZhemengTrack"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
ZhemengTrack jx = (ZhemengTrack)request.getAttribute("zhemengObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>哲盟_轨迹对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/zhemengTrack/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(jx != null){ %>
						<li><span>承运商code：</span>
							<input type ="text" id="tms_service_code" name ="tms_service_code" value="<%=jx.getTms_service_code()%>" size="20" maxlength="300">
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value="<%=jx.getPrivate_key() %>"  > 
						</li>
						<li><span>推送轨迹url：</span>
	 						<input type ="text" id="send_url" name ="send_url" maxlength="300"   value="<%=jx.getSend_url()%>"   > 
						</li>
						
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=jx.getCustomerid()%>" size="20"   > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						<li><span>承运商code：</span>
							<input type ="text" id="tms_service_code" name ="tms_service_code" value="" size="20" maxlength="300">
						</li>
						<li><span>密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value=""  > 
						</li>
						<li><span>推送轨迹url：</span>
	 						<input type ="text" id="send_url" name ="send_url" maxlength="300"   value=""   > 
						</li>
						
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="" size="20"   > 
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

