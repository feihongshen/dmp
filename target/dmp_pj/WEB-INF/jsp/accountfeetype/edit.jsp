<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.AccountFeeType"%>
<%
	AccountFeeType accountFeeType = (AccountFeeType)request.getAttribute("accountFeeType");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改款项</h1>
		<form id="fee_type_Form" name="fee_type_Form" 
			 onSubmit="if(check_accountfeetype()){submitCreateFormAndCloseBox(this);}return false;" 
			 action="<%=request.getContextPath()%>/accountfeetype/save/${accountFeeType.feetypeid}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>款项类型：</span>
					<select name="feetype" id="feetype" style="width:150px;">
	                    <option value="0">==请选择==</option>
	                    <option value="1" <%=accountFeeType.getFeetype()==1?"selected":"" %>>对站点加款</option>
				    	<option value="2" <%=accountFeeType.getFeetype()==2?"selected":"" %>>对站点减款</option>
               		</select>*</li>
					<li><span>名称：</span><input maxlength="20" type="text" name="feetypename" value="${accountFeeType.feetypename}" id="feetypename"  maxlength="100">*</li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存"  class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
