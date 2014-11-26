<%@page import="cn.explink.util.StringUtil"%>

<%@page import="cn.explink.pos.tonglianpos.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Tlmpos alipay = (Tlmpos)request.getAttribute("tlmposlist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>通联POS对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="if(check_alipay()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/tlmpos/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					
					<li><span>请求方：</span>
						<input type ="text" id="requester" name ="requester" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getRequester()) %>" maxlength="30"  >
					</li>
					<li><span>应答方：</span>
 						<input type ="text" id="targeter" name ="targeter" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getTargeter())%>" maxlength="30"  > 
					</li>
					<li><span>加密私钥：</span>
 						<input type ="text" id="privateKey" name ="privateKey" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getPrivateKey())%>" maxlength="1000"  > 
					</li>
					<li><span>解密公钥：</span>
 						<input type ="text" id="publicKey" name ="publicKey" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getPublicKey())%>" maxlength="1000"  > 
					</li>
					
					<li><span>请求URL：</span>
 						<input type ="text" id="request_url" name ="request_url" value ="<%=StringUtil.nullConvertToEmptyString(alipay.getRequest_url())%>" maxlength="300"  > 
					</li>
					<li><span>是否更新派送员：</span>
 						<select name="isotherdeliverupdate">
 							<option value="1" <%if(alipay.getIsotherdeliverupdate()==1){%>selected<%} %>>更新</option>
 							<option value="0" <%if(alipay.getIsotherdeliverupdate()==0){%>selected<%} %>>不更新</option>
 						</select> *刷卡是否更新派送员
					</li>
					
					<li><span>是否限制他人刷卡：</span>
 						<select name="isotheroperator">
 							<option value="1" <%if(alipay.getIsotheroperator()==1){%>selected<%} %>>限制</option>
 							<option value="0" <%if(alipay.getIsotheroperator()==0){%>selected<%} %>>不限制</option>
 						</select> *只能刷分配自己名下派送员
					</li>
					<li><span>是否开启签名验证：</span>
 						<input type="radio" name="isValidateSign" value="1"  <%if(alipay.getIsValidateSign()==1){%>checked<%} %>>限制
 						<input type="radio" name="isValidateSign"  value="0" <%if(alipay.getIsValidateSign()==0){%>checked<%} %>>不限制
					</li>
					<li><span>是否允许撤销：</span>
 						<input type="radio" name="isbackout" value="1"  <%if(alipay.getIsbackout()==1){%>checked<%} %>>允许
 						<input type="radio" name="isbackout"  value="0" <%if(alipay.getIsbackout()==0){%>checked<%} %>>不允许
					</li>
					<li><span>是否显示电话：</span>
 						<input type="radio" name="isshowPhone" value="1"  <%if(alipay.getIsshowPhone()==1){%>checked<%} %>>显示
 						<input type="radio" name="isshowPhone"  value="0" <%if(alipay.getIsshowPhone()==0){%>checked<%} %>>不显示
					</li>
					<li><span>是否显示支付方式：</span>
 						<input type="radio" name="isshowPaytype" value="1"  <%if(alipay.getIsshowPaytype()==1){%>checked<%} %>>显示
 						<input type="radio" name="isshowPaytype"  value="0" <%if(alipay.getIsshowPaytype()==0){%>checked<%} %>>不显示
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

