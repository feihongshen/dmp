<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.tps.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
TPSCarrierOrderStatus tpsCarrierOrderStatus = (TPSCarrierOrderStatus)request.getAttribute("tpsCarrierOrderStatus");
%>

<script type="text/javascript">


</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 350px;">
		<h1><div id="close_box" onclick="closeBox()"></div>唯品会_TPS运单状态接口对接设置</h1>
		<form id="save_Form" name="save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/tpsCarrierOrderStatus/save/${joint_num}" method="post">
	
			<div id="box_form">
				<ul>
					<%if(tpsCarrierOrderStatus != null){ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no" value="<%=tpsCarrierOrderStatus.getShipper_no() %>"  maxlength="300">
						</li>
						<li><span>每次获取订单数：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount" value="<%=tpsCarrierOrderStatus.getGetMaxCount() %>"  maxlength="300" onblur="javascript:validate('getMaxCount')">
						</li>
						<li><span>当前SEQ：</span>
							<input type ="text" id="seq" name ="seq" onblur="validate('seq')"  value="<%=tpsCarrierOrderStatus.getSeq() %>"  maxlength="300">
						</li> 
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%}else{ %>
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no"  maxlength="300">
						</li>
						<li><span>每次获取订单数：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount"   maxlength="300" onblur="javascript:validate('getMaxCount')">
						</li>
						<li><span>当前SEQ：</span>
							<input type ="text" id="seq" name ="seq" onblur="validate('seq')" value=""  maxlength="300">
						</li>
							
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
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

