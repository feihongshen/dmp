<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.pos.bill99.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Bill99 bill99 = (Bill99)request.getAttribute("bill99list");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>快钱对接设置</h1>
		<form id="bill99_save_Form" name="bill99_save_Form"  onSubmit="if(check_bill99()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/bill99/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<li><span>版本号：</span>
						<input type ="text" id="version" name ="version" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getVersion()) %>" maxlength="50"  >
					</li>
					<li><span>请求方：</span>
						<input type ="text" id="requester" name ="requester" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getRequester()) %>" maxlength="50"  >
					</li>
					<li><span>应答方：</span>
 						<input type ="text" id="targeter" name ="targeter" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getTargeter())%>" maxlength="50"  > 
					</li>
					<li><span>验证用户名：</span>
 						<input type ="text" id="ipodalias" name ="ipodalias" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getIpodalias())%>" maxlength="50"  > 
					</li>
					<li><span>验证密码：</span>
 						<input type ="text" id="ipodpassword" name ="ipodpassword" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getIpodpassword())%>" maxlength="50"  > 
					</li>
					<li><span>请求验证证书名称：</span>
 						<input size="15" type ="text" id="ipodrequestFileName" name ="ipodrequestFileName" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getIpodrequestFileName())%>" maxlength="50"  >.cer
					</li>
					<li><span>响应验证证书名称：</span>
 						<input size="15" type ="text" id="ipodresponseFileName" name ="ipodresponseFileName" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getIpodresponseFileName())%>" maxlength="50"  >.pfx
					</li>
					<li><span>是否限制他人刷卡：</span>
 						<select name="isotheroperator">
 							<option value="1" <%if(bill99.getIsotheroperator()==1){%>selected<%} %>>限制</option>
 							<option value="0" <%if(bill99.getIsotheroperator()==0){%>selected<%} %>>不限制</option>
 						</select> *只能刷分配自己名下派送员
					</li>
					<li><span>是否修改派送员：</span>
 						<select name="isupdateDeliverid">
 							<option value="1" <%if(bill99.getIsupdateDeliverid()==1){%>selected<%} %>>修改</option>
 							<option value="0" <%if(bill99.getIsupdateDeliverid()==0){%>selected<%} %>>不修改</option>
 						</select> *只能刷分配自己名下派送员
					</li>
					<li><span>请求URL：</span>
 						<input type ="text" id="request_url" name ="request_url" value ="<%=StringUtil.nullConvertToEmptyString(bill99.getRequest_url())%>" maxlength="50"  > 
					</li>
					<li><span>是否开启签名：</span>
 						<select name="isopensignflag">
 							<option value="1" <%if(bill99.getIsopensignflag()==1){%>selected<%} %>>开启</option>
 							<option value="0" <%if(bill99.getIsopensignflag()==0){%>selected<%} %>>关闭</option>
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

