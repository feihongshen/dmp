<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.b2c.dangdang_dataimport.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
DangDangDataSyn dangdang = (DangDangDataSyn)request.getAttribute("dangdangdatasynObject");
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
		<h1><div id="close_box" onclick="closeBox()"></div>当当对接设置(订单导入)</h1>
		<form id="dangdang_save_Form" name="dangdang_save_Form"  onSubmit="if(check_liebo()){submitSaveForm(this);}return false;" action="<%=request.getContextPath()%>/dangdangdatasyn/saveDangdang/${joint_num}" method="post">
		<div id="box_form">
				<ul>
					<%if(dangdang != null){ %>
						<li><span>快递公司标识：</span>
							<input type ="text" id="express_id" name ="express_id" value="<%=dangdang.getExpress_id() %>"  maxlength="300"  >
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value="<%=dangdang.getPrivate_key() %>"  > 
						</li>
						
						<li><span>自己接口URL：</span>
	 						<input type ="text" id="own_url" name ="own_url"  maxlength="300" value="<%=dangdang.getOwn_url()%>"  size="50"> (提供给当当)
						</li>
						<li><span>自测接口URL：</span>
	 						<input type ="text" id="own_url_test" name ="own_url_test"  maxlength="300" value="<%=dangdang.getOwn_url_test()%>" size="50" > (测试用)
						</li>
						<li><span>当当图书(自营)：</span>
	 						<input type ="text" id="customerid_tushu" name ="customerid_tushu"  maxlength="300"  value="<%=dangdang.getCustomerid_tushu()%>"  size="10" >
						</li>
						<li><span>当当百货(自营)：</span>
	 						<input type ="text" id="customerid_baihuo" name ="customerid_baihuo"  maxlength="300"  value="<%=dangdang.getCustomerid_baihuo()%>"  size="10"  >
						</li>
						<li><span>当当下午达(自营)：</span>
	 						<input type ="text" id="customerid_dangrida" name ="customerid_dangrida"  maxlength="300"  value="<%=dangdang.getCustomerid_dangrida()%>"   size="10" >
						</li>
						<li><span>外部商家(招商)：</span>
	 						<input type ="text" id="customerid_zhaoshang" name ="customerid_zhaoshang"  maxlength="300"  value="<%=dangdang.getCustomerid_zhaoshang()%>"  size="10"  >
						</li>
						<li><span>是否开启当日递</span>
	 						<input type ="radio" name ="isopen_drdflag"  <%if(dangdang.getIsopen_drdflag()==1){%>checked<%}%>  value="1"  > 开启&nbsp;<input type ="radio" name ="isopen_drdflag"    <%if(dangdang.getIsopen_drdflag()==0){%>checked<%}%>   value="0"  > 关闭
							&nbsp;&nbsp;<font color="red">*开启则按照时间段来卡当日递订单</font>
						</li>
						<li><span>当日递时间段：</span>
	 						<input type ="text" id="drd_starttime" name ="drd_starttime"  maxlength="300"  value="<%=dangdang.getDrd_starttime()%>"  size="10"  >到
	 						<input type ="text" id="drd_endtime" name ="drd_endtime"  maxlength="300"  value="<%=dangdang.getDrd_endtime()%>"  size="10"  >
	 						&nbsp;&nbsp;<font color="red">*填入数字，如08,12</font>
						</li>
						<li><span>批次生成时间：</span>
	 						<input type ="text" id="ruleEmaildateHours" name ="ruleEmaildateHours"  maxlength="300" value="<%=dangdang.getRuleEmaildateHours()%>"  size="10"> <font color="red">*(请设置为小时，如3表示凌晨3点，凌晨3点之前来的数据全部属于前一天的批次)</font>
						</li>
						
						
						
						<li><span>编码方式：</span>
	 						<select name="charcode">
	 							<option value="UTF-8" <%if("UTF-8".equals(dangdang.getCharcode())){%>selected<%}%>>UTF-8</option>
	 							<option value="GBK"  <%if("GBK".equals(dangdang.getCharcode())){%>selected<%}%>>GBK</option>
	 						</select>
						</li>
						<li><span>订单导入库房：</span>
							<select name="branchid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" <%if(dangdang.getBranchid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
								<%}%>
							</select>
						</li>
						<li><span>密码：</span>
	 						<input type ="password" id="password" name ="password"  maxlength="30"  > 
						</li>
					<%}else{ %>
						<li><span>快递公司标识：</span>
							<input type ="text" id="express_id" name ="express_id" value=""  maxlength="300"  >
						</li>
						<li><span>加密密钥：</span>
	 						<input type ="text" id="private_key" name ="private_key" maxlength="300"   value=""  > 
						</li>
						
						<li><span>自己接口URL：</span>
	 						<input type ="text" id="own_url" name ="own_url"  maxlength="300" value=""  size="50"> (提供给当当)
						</li>
						<li><span>自测接口URL：</span>
	 						<input type ="text" id="own_url_test" name ="own_url_test"  maxlength="300" value=""  size="50"> (测试用)
						</li>
						<li><span>当当图书(自营)：</span>
	 						<input type ="text" id="customerid_tushu" name ="customerid_tushu"  maxlength="300"  value=""  size="10"  >
						</li>
						<li><span>当当百货(自营)：</span>
	 						<input type ="text" id="customerid_baihuo" name ="customerid_baihuo"  maxlength="300"  value=""   size="10" >
						</li>
						<li><span>当当下午达(自营)：</span>
	 						<input type ="text" id="customerid_dangrida" name ="customerid_dangrida"  maxlength="300"  value=""   size="10" >
						</li>
						<li><span>外部商家(招商)：</span>
	 						<input type ="text" id="customerid_zhaoshang" name ="customerid_zhaoshang"  maxlength="300"  value=""  size="10"  >
						</li>
						<li><span>是否开启当日递</span>
	 						<input type ="radio" name ="isopen_drdflag"    value="1"  > 开启&nbsp;<input type ="radio" name ="isopen_drdflag"  checked   value="0"  > 关闭
							&nbsp;&nbsp;<font color="red">*开启则按照时间段来卡当日递订单</font>
						</li>
						<li><span>当日递时间段：</span>
	 						<input type ="text" id="drd_starttime" name ="drd_starttime"  maxlength="300"  value=""  size="15"  >到
	 						<input type ="text" id="drd_endtime" name ="drd_endtime"  maxlength="300"  value=""  size="15"  >
	 						&nbsp;&nbsp;<font color="red">*填入数字，如08,12</font>
						</li>
						<li><span>批次生成时间：</span>
	 						<input type ="text" id="ruleEmaildateHours" name ="ruleEmaildateHours"  maxlength="300" value=""  size="10"> <font color="red">*(请设置为小时，如3表示凌晨3点，凌晨3点之前来的数据全部属于前一天的批次)</font>
						</li>
						
						<li><span>编码方式：</span>
	 						<select name="charcode">
	 							<option value="UTF-8">UTF-8</option>
	 							<option value="GBK">GBK</option>
	 						</select>
						</li>
						<li><span>订单导入库房：</span>
							<select name="branchid">
								<option value="0">请选择库房</option>
								<%for(Branch b:warehouselist){
								%>
									<option value="<%=b.getBranchid()%>" ><%=b.getBranchname() %></option>
								<%}%>
							</select>
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

