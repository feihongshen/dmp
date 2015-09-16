<%@page import="cn.explink.util.StringUtil"%>

<%@page import="cn.explink.b2c.weisuda.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%

Weisuda weisuda=(Weisuda)request.getAttribute("weisudalist");

%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>唯速达对接设置</h1>
		<form id="alipay_save_Form" name="alipay_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/weisuda/save/${joint_num}" method="post">
		<div id="box_form" style="width: 400px;">
				<ul>
					<li><span>承运商代码：</span>
 						<input type ="text" id="code" name ="code" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getCode())%>" maxlength="30"  > 
					</li>
					<li><span>API协议版本：</span>
 						<input type ="text" id="v" name ="v" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getV())%>" maxlength="1000"  > 
					</li>
					<li><span>分配密钥：</span>
 						<input type ="text" id="secret" name ="secret" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getSecret())%>" maxlength="1000"  > 
					</li>
						<li><span>绑定关系通知：</span>
 						<input type ="text" id="pushOrders_URL" name ="pushOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getPushOrders_URL())%>" maxlength="1000"  > 
					</li>
						<li><span>取消绑定通知：</span>
 						<input type ="text" id="unboundOrders_URL" name ="unboundOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getUnboundOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>签收信息同步：</span>
 						<input type ="text" id="UnVerifyOrders_URL" name ="UnVerifyOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getUnVerifyOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>签收信息结果回调：</span>
 						<input type ="text" id="updateUnVerifyOrders_URL" name ="updateUnVerifyOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getUpdateUnVerifyOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>签收信息修改：</span>
 						<input type ="text" id="updateOrders_URL" name ="updateOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getUpdateOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>站点更新：</span>
 						<input type ="text" id="siteUpdate_URL" name ="siteUpdate_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getSiteUpdate_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>站点删除：</span>
 						<input type ="text" id="siteDel_URL" name ="siteDel_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getSiteDel_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>快递员更新：</span>
 						<input type ="text" id="courierUpdate_URL" name ="courierUpdate_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getCourierUpdate_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>快递员删除：</span>
 						<input type ="text" id="carrierDel_URL" name ="carrierDel_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getCarrierDel_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>揽退绑定：</span>
 						<input type ="text" id="getback_boundOrders_URL" name ="getback_boundOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getGetback_boundOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>揽退签收：</span>
 						<input type ="text" id="getback_getAppOrders_URL" name ="getback_getAppOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getGetback_getAppOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>揽退签收回调：</span>
 						<input type ="text" id="getback_confirmAppOrders_URL" name ="getback_confirmAppOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getGetback_confirmAppOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>揽退签收修改：</span>
 						<input type ="text" id="getback_updateOrders_URL" name ="getback_updateOrders_URL" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getGetback_updateOrders_URL())%>" maxlength="1000"  > 
					</li>
					<li><span>接收单数：</span>
 						<input type ="text" id="nums" name ="nums" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getNums())%>" maxlength="1000"  > 
					</li>
					<li><span>接收次数：</span>
 						<input type ="text" id="count" name ="count" value ="<%=StringUtil.nullConvertToEmptyString(weisuda.getCount())%>" maxlength="1000"  > 
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

