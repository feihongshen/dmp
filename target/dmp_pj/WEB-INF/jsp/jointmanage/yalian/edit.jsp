<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.pos.yalian.YalianApp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	YalianApp mobile = (YalianApp)request.getAttribute("mobilecodapplist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>亚联CODAPP对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/Yalian_go/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
				
					<li><span>对方URL：</span>
 						<input type ="text" id="request_url" name ="request_url" size="45" value ="<%=StringUtil.nullConvertToEmptyString(mobile.getRequest_url())%>" maxlength="1000"  > 
					</li>
					<li><span>我方URL：</span>
 						<input type ="text" id="trick_url" name ="trick_url"  size="45"  value ="<%=StringUtil.nullConvertToEmptyString(mobile.getTrick_url())%>" maxlength="1000"  > 
					</li>
					<li><span>电信id：</span>
 						<input type ="text" id="dianxin" name ="dianxin"  size="45"  value ="<%=StringUtil.nullConvertToEmptyString(mobile.getCode_dianxin())%>" maxlength="1000"  > 
					</li>
					<li><span>移动id：</span>
 						<input type ="text" id="yidong" name ="yidong"  size="45"  value ="<%=StringUtil.nullConvertToEmptyString(mobile.getCode_yidong())%>" maxlength="1000"  > 
					</li>
					<li><span>联通id：</span>
 						<input type ="text" id="liantong" name ="liantong"  size="45"  value ="<%=StringUtil.nullConvertToEmptyString(mobile.getCode_liantong())%>" maxlength="1000"  > 
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

