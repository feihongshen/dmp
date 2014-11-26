<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.haoxgou.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
HaoXiangGou hxg = (HaoXiangGou)request.getAttribute("hxgObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>好享购接口设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/hxg/save/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(hxg != null){ %>
						
						
						<li><span>标识id：</span>
	 						<input type ="text" id="dlver_cd" name ="dlver_cd"  maxlength="300" size="15" value="<%=hxg.getDlver_cd()%>"  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value="<%=hxg.getCustomerids()%>"   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=hxg.getMaxCount()%>"  size="15"> 
						</li>
						<li><span>WebService地址：</span>
	 						<input type ="text" id="requst_url" name ="requst_url"  maxlength="300"  value="<%=hxg.getRequst_url()%>"  size="35"> 
						</li>
						<li><span>访问密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value="<%=hxg.getPassword()%>"  size="35"> 
						</li>
						<li><span>DES密钥：</span>
	 						<input type ="text" id="des_key" name ="des_key"  maxlength="300"  value="<%=hxg.getDes_key()%>"  size="35"> 
						</li>
						<li><span>支付公司代码：</span>
	 						<input type ="text" id="pospaycode" name ="pospaycode"  maxlength="300"  value="<%=hxg.getPospaycode()%>"  size="35"> 
						</li>
						<li><span>查询时间段：</span>
	 						<input type ="text" id="selectHours" name ="selectHours"  maxlength="300"  value="<%=hxg.getSelectHours()%>"  size="35"> 
						</li>
						
						<li><span>开启指定时间：</span>
	 						<input type ="radio" id="isopentestflag1" name ="isopentestflag"  value="1" <%if(hxg.getIsopentestflag()==1){%>checked<%}%>  > 开启
	 						<input type ="radio" id="isopentestflag2" name ="isopentestflag"  value="0"  <%if(hxg.getIsopentestflag()==0){%>checked<%}%> > 关闭
						</li>
						<li><span>指定开始时间：</span>
	 						<input type ="text" id="starttime" name ="starttime"  maxlength="300"  value="<%=hxg.getStarttime()%>"  size="35"> 
						</li>
						<li><span>指定结束时间：</span>
	 						<input type ="text" id="endtime" name ="endtime"  maxlength="300"  value="<%=hxg.getEndtime()%>"  size="35"> 
						</li>
						
						<li><span>订单导入库房：</span>
							<select name="warehouseid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(hxg.getWarehouseid()==b.getBranchid()){%>selected<%}%> ><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30" size="20" > 
						</li>
					<%}else{ %>
						
						<li><span>标识id：</span>
	 						<input type ="text" id="dlver_cd" name ="dlver_cd"  maxlength="300" size="15" value=""  > 
						</li>
						<li><span>在配送公司中id：</span>
	 						<input type ="text" id="customerids" name ="customerids"  size="15"  maxlength="300"  value=""   > 
						</li>
						<li><span>每次查询订单量：</span>
	 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""  size="15"> 
						</li>
						<li><span>WebService地址：</span>
	 						<input type ="text" id="requst_url" name ="requst_url"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>访问密码：</span>
	 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>DES密钥：</span>
	 						<input type ="text" id="des_key" name ="des_key"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>支付公司代码：</span>
	 						<input type ="text" id="pospaycode" name ="pospaycode"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>查询时间段：</span>
	 						<input type ="text" id="selectHours" name ="selectHours"  maxlength="300"  value=""  size="35"> 
						</li>
						
						<li><span>开启指定时间：</span>
	 						<input type ="radio" id="isopentestflag1" name ="isopentestflag"  value="1"   > 开启
	 						<input type ="radio" id="isopentestflag2" name ="isopentestflag"  value="0"  > 关闭
						</li>
						<li><span>指定开始时间：</span>
	 						<input type ="text" id="starttime" name ="starttime"  maxlength="300"  value=""  size="35"> 
						</li>
						<li><span>指定结束时间：</span>
	 						<input type ="text" id="endtime" name ="endtime"  maxlength="300"  value=""  size="35"> 
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

