<%@page import="cn.explink.b2c.benlaishenghuo.BenLaiShengHuo"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
	BenLaiShengHuo mkl = (BenLaiShengHuo)request.getAttribute("BenLaiObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>本来生活_接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/benlaishenghuo/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(mkl != null){ %>
					<li><span>User_key：</span>
	 						<input type ="text" id="userkey" name ="userkey"  maxlength="300"  value="<%=mkl.getUserkey()%>"  size="15" > 
						</li>
						<li><span>sShippedCode：</span>
	 						<input type ="text" id="sShippedCode" name ="sShippedCode"  maxlength="300"  value="<%=mkl.getsShippedCode()%>"  size="15" > 
						</li>
						<li><span>接收订单URL：</span>
	 						<input type ="text" id="pushCwb_URL" name ="pushCwb_URL" maxlength="300"   value="<%=mkl.getPost_url()%>"   > 
						</li>
						<li><span>发送的URL：</span>
	 						<input type ="text" id="Track_url" name ="Track_url" maxlength="300"  size="15"  value="<%=mkl.getTrack_url()%>"  > 
						</li>
						<li><span>customer_id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value="<%=mkl.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){%>
									<option value="<%=b.getBranchid()%>" <%if(mkl.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
					<li><span>User_key：</span>
	 						<input type ="text" id="userkey" name ="userkey"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>sShippedCode：</span>
	 						<input type ="text" id="sShippedCode" name ="sShippedCode"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>接收订单URL：</span>
	 						<input type ="text" id="pushCwb_URL" name ="pushCwb_URL" maxlength="300"   value=""   > 
						</li>
						<li><span>发送的URL：</span>
	 						<input type ="text" id="Track_url" name ="Track_url" maxlength="300"  size="15"  value=""  > 
						</li>
						<li><span>customer_id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>是否开启订单下载：</span>
	 						<input type="radio" name="isopenDataDownload" value="1" checked/> 开启
	 						<input type="radio" name="isopenDataDownload" value="0" /> 关闭
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
