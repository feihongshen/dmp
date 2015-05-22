<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.dangdang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
DangDang dangdang = (DangDang)request.getAttribute("dangdangObject");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>当当对接设置(状态反馈)</h1>
		<form id="dangdang_save_Form" name="dangdang_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/dangdang/saveDangdang/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(dangdang != null){ %>
						<li><span>快递公司标识：</span>
							<input type ="text" id="express_id" name ="express_id" value="<%=dangdang.getExpress_id() %>"  maxlength="300"  >
						</li>
						<li><span>每次最大发送数量：</span>
	 						<input type ="text" id="count" name ="count" maxlength="30"  value="<%=dangdang.getCount() %>"   > 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value="<%=dangdang.getPrivate_key() %>"  > 
						</li>
						<li><span>入库接口URL：</span>
	 						<input type ="text" id="ruku_url" name ="ruku_url"  maxlength="300"  value="<%=dangdang.getRuku_url() %>"  > 
						</li>
						<li><span>出库接口URL：</span>
	 						<input type ="text" id="chukuPaiSong_url" name ="chukuPaiSong_url"  maxlength="300"  value="<%=dangdang.getChukuPaiSong_url()%>"  > 
						</li>
						<li><span>配送结果接口URL：</span>
	 						<input type ="text" id="deliverystate_url" name ="deliverystate_url"  maxlength="300"  value="<%=dangdang.getDeliverystate_url()%>" > 
						</li>
						<li><span>跟踪日志接口：</span>
	 						<input type ="text" id="trackinfo_url" name ="trackinfo_url"  maxlength="300" value="<%=dangdang.getTrackinfo_url()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=dangdang.getCustomerids()%>"  > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  > 
						</li>
					<%}else{ %>
						<li><span>快递公司标识：</span>
							<input type ="text" id="express_id" name ="express_id"   maxlength="300"  >
						</li>
						<li><span>每次最大发送数量：</span>
	 						<input type ="text" id="count" name ="count" maxlength="30" value="0"  > 
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  > 
						</li>
						<li><span>入库接口URL：</span>
	 						<input type ="text" id="ruku_url" name ="ruku_url"  maxlength="300"  > 
						</li>
						<li><span>出库接口URL：</span>
	 						<input type ="text" id="chukuPaiSong_url" name ="chukuPaiSong_url"  maxlength="300"  > 
						</li>
						<li><span>配送结果接口URL：</span>
	 						<input type ="text" id="deliverystate_url" name ="deliverystate_url"  maxlength="300"  > 
						</li>
						<li><span>跟踪日志接口：</span>
	 						<input type ="text" id="trackinfo_url" name ="trackinfo_url"  maxlength="300"  > 
						</li>
						<li><span>在配送公司id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  > 
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  > 
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

