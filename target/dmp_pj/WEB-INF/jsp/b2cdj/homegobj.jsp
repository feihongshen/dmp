<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.homegobj.*"%>
<%@page import="cn.explink.domain.Branch"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Homegobj efast = (Homegobj)request.getAttribute("homegobjObject");
List<Branch> warehouselist=(List<Branch>)request.getAttribute("warehouselist");
%>

<script type="text/javascript">



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>家有购物（北京）对接设置</h1>
		<form id="yixun_save_Form" name="yixun_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/homegobj/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(efast != null){ %>
						
						
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="express_id" name ="express_id"  maxlength="300" size="20"  value="<%=efast.getExpress_id() %>"  > 
						</li>
						
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value="<%=efast.getPrivate_key() %>"  > 
						</li>
						
						<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;获取&nbsp;
							<input type ="text" id="beforhours" name ="beforhours" size="5" value="<%=efast.getBeforhours() %>"  maxlength="4">&nbsp;小时前的数据
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  size="20"  value="<%=efast.getLoopcount() %>"  > 
						</li>
						<li><span>每页条数：</span>
	 						<input type ="text" id="pageSize" name ="pageSize"  size="20"  value="<%=efast.getPageSize() %>"  > 
						</li>
						
						<li><span>获取订单url：</span>
	 						<input type ="text" id="getOrder_url" name ="getOrder_url" maxlength="300"   value="<%=efast.getGetOrder_url()%>"   > 
						</li>
						<li><span>状态回传url：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value="<%=efast.getFeedback_url()%>"   > 
						</li>
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=efast.getCustomerid()%>" size="20"   > 
						</li>
						<li><span>每次最大查询量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=efast.getMaxCount()%>" size="20"   > 
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(efast.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
						
						
						
					<%}else{ %>
						<li><span>快递公司标识：</span>
	 						<input type ="text" id="express_id" name ="express_id"  maxlength="300" size="20"  value=""  > 
						</li>
						
						<li><span>密钥信息：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"  size="20"  value=""  > 
						</li>
						
						<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;获取&nbsp;
							<input type ="text" id="beforhours" name ="beforhours" size="5" value=""  maxlength="4">&nbsp;小时前的数据
						</li>
						<li><span>每次循环次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  size="20"  value=""  > 
						</li>
						
						<li><span>获取订单url：</span>
	 						<input type ="text" id="getOrder_url" name ="getOrder_url" maxlength="300"   value=""   > 
						</li>
						<li><span>状态回传url：</span>
	 						<input type ="text" id="feedback_url" name ="feedback_url" maxlength="300"   value=""   > 
						</li>
						<li><span>供货商ID：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="" size="20"   > 
						</li>
						
						<li><span>每次最大查询量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="" size="20"   > 
						</li>
						<li><span>每页条数：</span>
	 						<input type ="text" id="pageSize" name ="pageSize"  size="20"  value=""  > 
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

