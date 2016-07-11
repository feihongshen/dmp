<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.vipshop.*"%>
<%@page import="cn.explink.domain.Branch" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%
VipShop vipshop = (VipShop)request.getAttribute("vipshopObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");

%>



<script type="text/javascript">

</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg" style="width: 500px;">
		<h1><div id="close_box" onclick="closeBox()"></div>TPS自动化对接设置</h1>
	    <form id="vipshop_save_Form" name="vipshop_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/vipshop/saveVipShop/${joint_num}" method="post">
	
		<input type ="hidden" id="forward_hours" name ="forward_hours" value="0"  maxlength="2">
<div id="box_form">
				<ul>
					<%if(vipshop != null){ %>
					    <li style="display: none;"><span>干线回单重发天数：</span>
							<input type ="text" id="daysno" name ="daysno" value="<%=vipshop.getDaysno() %>"  maxlength="300">
						</li>
						<li style="display: none;"><span>限制货态重发次数：</span>
							<input type ="text" id="selb2cnum" name ="selb2cnum" value="<%=vipshop.getSelb2cnum() %>"  maxlength="300">
						</li>
						
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no" value="<%=vipshop.getShipper_no() %>"  maxlength="300">
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key" value="<%=vipshop.getPrivate_key() %>"  maxlength="300">
						</li>
						<li style="display: none;"><span>每次获取订单数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount" onblur="validate('getMaxCount')"  value="1"  maxlength="300">
						</li>
						<li><span>每次推送订单数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount" onblur="validate('sendMaxCount')" value="<%=vipshop.getSendMaxCount() %>"  maxlength="300">
						</li>
						<li style="display: none;"><span>获取订单的URL：</span>
							<input type ="text" id="getCwb_URL" name ="getCwb_URL" value="<%=vipshop.getGetCwb_URL() %>"  maxlength="300">
						</li>
						<li><span>反馈URL：</span>
							<input type ="text" id="sendCwb_URL" name ="sendCwb_URL" value="<%=vipshop.getSendCwb_URL() %>"  maxlength="300">
						</li>
						<li><span>oxo揽收状态URL：</span>
							<input type ="text" id="oxoState_URL" name ="oxoState_URL" value="<%=vipshop.getOxoState_URL() %>"  maxlength="300">
						</li>
						<li><span>集包运单推送URL：</span>
							<input type ="text" id="transflowUrl" name ="transflowUrl" value="<%=vipshop.getTransflowUrl()%>"  maxlength="300">
						</li>
						
						<li style="display: none;"><span>当前SEQ：</span>
							<input type ="text" id="vipshop_seq" name ="vipshop_seq" onblur="validate('vipshop_seq')"  value="0"  maxlength="300">
						</li>
						<li><span>系统中客户id：</span>
							<input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')" value="<%=vipshop.getCustomerids() %>"  maxlength="300">
						</li>
						<li><span>乐蜂id：</span>
							<input type ="text" id="lefengCustomerid" name ="lefengCustomerid" value="<%=vipshop.getLefengCustomerid() %>"  maxlength="300">
						</li>
						<li style="display: none;">><span>是否订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1" <%if(vipshop.getIsopendownload()==1){%>checked<%}%>  >开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   <%if(vipshop.getIsopendownload()==0){%>checked<%}%>  >关闭
						</li>
						<li><span>托运模式：</span>
							<input type ="radio" id="isTuoYunDanFlag1" name ="isTuoYunDanFlag" value="1" disabled="disabled" >开启
							<input type ="radio" id="isTuoYunDanFlag2" name ="isTuoYunDanFlag" value="0"  checked="checked" >关闭
						</li>
						<li style="display: none;"><span>业务标识：</span>
							<input type ="radio" id="isShangmentuiFlag1" name ="isShangmentuiFlag" value="0" disabled="disabled" <%if(vipshop.getIsShangmentuiFlag()==0){%>checked<%}%>  >配送
							<input type ="radio" id="isShangmentuiFlag2" name ="isShangmentuiFlag" value="1" disabled="disabled" <%if(vipshop.getIsShangmentuiFlag()==1){%>checked<%}%>  >上门退
							<input type ="radio" id="isShangmentuiFlag3" name ="isShangmentuiFlag" value="2"   <%if(vipshop.getIsShangmentuiFlag()==2){%>checked<%}%>  >全部
						</li>
						<li><span>取消或拦截：</span>
							<input type ="radio" id="cancelOrIntercept1" name ="cancelOrIntercept" value="0" <%if(vipshop.getCancelOrIntercept()==0){%>checked<%}%>  >取消开启
							<input type ="radio" id="cancelOrIntercept2" name ="cancelOrIntercept" value="1" <%if(vipshop.getCancelOrIntercept()==1){%>checked<%}%>  >拦截开启
						</li>
						<li><span>只下载乐蜂：</span>
							<input type ="radio" id="isOpenLefengflag1" name ="isOpenLefengflag" value="0" <%if(vipshop.getIsOpenLefengflag()==0){%>checked<%}%>  >关闭
							<input type ="radio" id="isOpenLefengflag2" name ="isOpenLefengflag" value="1" <%if(vipshop.getIsOpenLefengflag()==1){%>checked<%}%>  >开启
						</li>
						<li><span>拒收原因回传：</span>
							<input type ="radio" id="resuseReasonFlag1" name ="resuseReasonFlag" value="0" <%if(vipshop.getResuseReasonFlag()==0){%>checked<%}%>  >回传
							<input type ="radio" id="resuseReasonFlag2" name ="resuseReasonFlag" value="1" <%if(vipshop.getResuseReasonFlag()==1){%>checked<%}%>  >不回传
						</li>
						<li><span>生成批次标识：</span>
							<input type ="radio" id="isCreateTimeToEmaildateFlag1" name ="isCreateTimeToEmaildateFlag" value="0" <%if(vipshop.getIsCreateTimeToEmaildateFlag()==0){%>checked<%}%>  >关闭
							<input type ="radio" id="isCreateTimeToEmaildateFlag2" name ="isCreateTimeToEmaildateFlag" value="1" <%if(vipshop.getIsCreateTimeToEmaildateFlag()==1){%>checked<%}%>  >开启（订单出仓时间作为标识,开启必须关闭托运模式）
						</li>
						
						</li>
						
						<li><span>是否开启集包：</span>
							<input type ="radio" id="openmpspackageflag1" name ="openmpspackageflag" value="0" <%if(vipshop.getOpenmpspackageflag()==0){%>checked<%}%>  >关闭
							<input type ="radio" id="openmpspackageflag2" name ="openmpspackageflag" value="1" <%if(vipshop.getOpenmpspackageflag()==1){%>checked<%}%>  >开启
						</li>
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(vipshop.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
					    <li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
					<%}else{ %>
						<li style="display: none;"><span>干线回单重发天数：</span>
							<input type ="text" id="daysno" name ="daysno"  maxlength="300">
						</li>
						<li style="display: none;"><span>限制货态重发次数：</span>
							<input type ="text" id="selb2cnum" name ="selb2cnum"   maxlength="300">
						</li>	
					
						<li><span>承运商编码：</span>
							<input type ="text" id="shipper_no" name ="shipper_no"  maxlength="300">
						</li>
						<li><span>加密秘钥：</span>
							<input type ="text" id="private_key" name ="private_key"  maxlength="300">
						</li>
						<li style="display: none;"><span>每次获取订单数量：</span>
							<input type ="text" id="getMaxCount" name ="getMaxCount" value="1"  maxlength="300" onblur="javascript:validate('getMaxCount')">
						</li>
						<li><span>每次推送订单数量：</span>
							<input type ="text" id="sendMaxCount" name ="sendMaxCount" value="1"  maxlength="300" onblur="validate('sendMaxCount')">
						</li>
						<li style="display: none;"><span>获取订单的URL：</span>
							<input type ="text" id="getCwb_URL" name ="getCwb_URL"   maxlength="300">
						</li>
						<li><span>反馈URL：</span>
							<input type ="text" id="sendCwb_URL" name ="sendCwb_URL"  maxlength="300"/>
						</li>
						<li><span>oxo揽收状态URL：</span>
							<input type ="text" id="oxoState_URL" name ="oxoState_URL" value=""  maxlength="300">
						</li>
						<li><span>集包运单推送URL：</span>
							<input type ="text" id="transflowUrl" name ="transflowUrl" value=""  maxlength="300">
						</li>
						
						<li style="display: none;"><span>当前SEQ：</span>
							<input type ="text" id="vipshop_seq" name ="vipshop_seq" onblur="validate('vipshop_seq')" value="0"  maxlength="300">
						</li>
						<li><span>系统中客户id：</span>
							<input type ="text" id="customerids" name ="customerids" onblur="validate('customerids')"  maxlength="300">
						</li>
						<li><span>乐蜂id：</span>
							<input type ="text" id="lefengCustomerid" name ="lefengCustomerid" value=""  maxlength="300">
						</li>
						<li style="display: none;"><span>是否订单下载：</span>
							<input type ="radio" id="isopendownload1" name ="isopendownload" value="1"  checked>开启
							<input type ="radio" id="isopendownload2" name ="isopendownload" value="0"   >关闭
						</li>
						<li><span>托运模式：</span>
							<input type ="radio" id="isTuoYunDanFlag1" name ="isTuoYunDanFlag" value="1" disabled="disabled" >开启
							<input type ="radio" id="isTuoYunDanFlag2" name ="isTuoYunDanFlag" value="0" checked>关闭
						</li>
						
					    <li style="display: none;"><span>业务标识：</span>
							<input type ="radio" id="isShangmentuiFlag1" name ="isShangmentuiFlag" value="0" disabled="disabled"  >配送
							<input type ="radio" id="isShangmentuiFlag2" name ="isShangmentuiFlag" value="1"  disabled="disabled"  >上门退
							<input type ="radio" id="isShangmentuiFlag3" name ="isShangmentuiFlag" value="2"  checked >全部
						</li>
						<li><span>取消或拦截：</span>
							<input type ="radio" id="cancelOrIntercept1" name ="cancelOrIntercept" value="0" checked  >取消开启
							<input type ="radio" id="cancelOrIntercept2" name ="cancelOrIntercept" value="1"  >拦截开启
						</li>
						<li><span>只下载乐蜂：</span>
							<input type ="radio" id="isOpenLefengflag1" name ="isOpenLefengflag" value="0" checked >关闭
							<input type ="radio" id="isOpenLefengflag2" name ="isOpenLefengflag" value="1"  >开启
						</li>
						<li><span>拒收原因回传：</span>
							<input type ="radio" id="resuseReasonFlag1" name ="resuseReasonFlag" value="0" checked>回传
							<input type ="radio" id="resuseReasonFlag2" name ="resuseReasonFlag" value="1" >不回传
						</li>
						<li><span>生成批次标识：</span>
							<input type ="radio" id="isCreateTimeToEmaildateFlag1" name ="isCreateTimeToEmaildateFlag" value="0" checked >关闭
							<input type ="radio" id="isCreateTimeToEmaildateFlag2" name ="isCreateTimeToEmaildateFlag" value="1"  >开启（订单生成时间作为标识）
						</li>
						
						<li><span>是否开启集包：</span>
							<input type ="radio" id="openmpspackageflag1" name ="openmpspackageflag" value="0" checked >关闭
							<input type ="radio" id="openmpspackageflag2" name ="openmpspackageflag" value="1"  >开启
						</li>
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
							
							</select>
						</li>
							
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"    size="20"> 
						</li>
						
					<%} %>
				</ul>
		</div>
		<div align="center"><input type="submit" value="保存" class="button"/></div>
		<input type="hidden" name="joint_num" value="${joint_num}"/>
	</form>
	</div>
</div>
<div id="box_yy"></div>

