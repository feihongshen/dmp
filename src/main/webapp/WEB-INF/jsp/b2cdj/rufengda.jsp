<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.rufengda.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
Rufengda rfd = (Rufengda)request.getAttribute("rufengdaObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>如风达接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/rufengda/saveRufengda/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(rfd != null){ %>
						
						
						<li><span>分配唯一lcId：</span>
	 						<input type ="text" id="lcId" name ="lcId"  maxlength="300"  value="<%=rfd.getLcId()%>"  > 
						</li>
						<li><span>WebService地址：</span>
	 						<input type ="text" id="ws_url" name ="ws_url"  maxlength="300"  value="<%=rfd.getWs_url()%>"    > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=rfd.getCustomerid()%>"  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=rfd.getMaxCount()%>"  size="15" > 
						</li>
						<li><span> 每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  maxlength="300"  value="<%=rfd.getLoopcount()%>"  size="15" > 
						</li>
						<li><span>此时间后反馈：</span>
	 						<input type ="text" id="nowtime" name ="nowtime"  maxlength="300"  value="<%=rfd.getNowtime()%>"  size="15" > 
						</li>
						<li><span>modulePuk：</span>
	 						<input type ="text" id="modulePuk" name ="modulePuk"  maxlength="300"  value="<%=rfd.getModulePuk()%>"  size="50" > <font color="red">公钥Modulus节点</font>
						</li>
						<li><span>exponentPuk：</span>
	 						<input type ="text" id="exponentPublic" name ="exponentPublic"  maxlength="300"  value="<%=rfd.getExponentPublic()%>"  size="15" > <font color="red">公钥Exponent节点</font>
						</li>
						<li><span>modulePrk：</span>
	 						<input type ="text" id="modulePrk" name ="modulePrk"  maxlength="300"  value="<%=rfd.getModulePrk()%>"  size="50" > <font color="red">私钥Modulus节点</font>
						</li>
						<li><span>exponetPrk：</span>
	 						<input type ="text" id="exponetPrivate" name ="exponetPrivate"  maxlength="300"  value="<%=rfd.getExponetPrivate()%>"  size="50" > <font color="red">私钥D节点</font>
						</li>
						<li><span>DES_KEY：</span>
	 						<input type ="text" id="des_key" name ="des_key"  maxlength="300"  value="<%=rfd.getDes_key()%>"  size="45" > <font color="red">解密字段密钥</font>
						</li>
						<li><span>是否开启签名验证：</span>
	 						<input type ="radio" id="isopensignflag1" name ="isopensignflag"  <%if(rfd.getIsopensignflag()==1){%>checked<%} %>  value="1"  >开启 
	 						<input type ="radio" id="isopensignflag2" name ="isopensignflag"   <%if(rfd.getIsopensignflag()==0){%>checked<%} %> value="0" > 关闭
						</li>
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(rfd.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>分配唯一lcId：</span>
	 						<input type ="text" id="lcId" name ="lcId"  maxlength="300"  value=""  > 
						</li>
						<li><span>WebService地址：</span>
	 						<input type ="text" id="ws_url" name ="ws_url"  maxlength="300"  value=""    > 
						</li>
						<li><span>系统customerid：</span>
	 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>推送最大数量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span> 每次下载次数：</span>
	 						<input type ="text" id="loopcount" name ="loopcount"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>此时间后反馈：</span>
	 						<input type ="text" id="nowtime" name ="nowtime"  maxlength="300"  value=""  size="15" > 
						</li>
						<li><span>modulePuk：</span>
	 						<input type ="text" id="modulePuk" name ="modulePuk"  maxlength="300"  value=""  size="50" > <font color="red">公钥Modulus节点</font>
						</li>
						<li><span>exponentPuk：</span>
	 						<input type ="text" id="exponentPublic" name ="exponentPublic"  maxlength="300"  value=""  size="15" > <font color="red">公钥Exponent节点</font>
						</li>
						<li><span>modulePrk：</span>
	 						<input type ="text" id="modulePrk" name ="modulePrk"  maxlength="300"  value=""  size="50" > <font color="red">私钥Modulus节点</font>
						</li>
						<li><span>exponetPrk：</span>
	 						<input type ="text" id="exponetPrivate" name ="exponetPrivate"  maxlength="300"  value=""  size="50" > <font color="red">私钥D节点</font>
						</li>
						<li><span>DES_KEY：</span>
	 						<input type ="text" id="des_key" name ="des_key"  maxlength="300"  value=""  size="45" > <font color="red">解密字段密钥</font>
						</li>
						<li><span>是否开启签名验证：</span>
	 						<input type ="radio" id="isopensignflag1" name ="isopensignflag"    value="1"  >开启 
	 						<input type ="radio" id="isopensignflag2" name ="isopensignflag"   checked value="0" > 关闭
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

