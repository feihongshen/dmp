
<%@ page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<Branch> branchList = (List<Branch>) request.getAttribute("branchList");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建承运商</h1>
		<form id="common_cre_Form" name="common_cre_Form" onSubmit="if(check_common()){submitCreateForm(this);}return false;" action="<%=request.getContextPath()%>/common/create" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>承运商名称：</span><input type ="text" id ="commonname" name ="commonname" maxlength="30"/>*</li>
					<li><span>承运商编号：</span><input type ="text" id ="commonnumber" name ="commonnumber"  maxlength="30"/>*</li>
					<li><span>站点：</span><select id ="commenbranchid" name ="branchid" onchange="updateCommenDeliver('<%=request.getContextPath()%>');">
		             <option value ="0">==请选择==</option> 
		            <%if(branchList != null && branchList.size()>0){ %>
		             <%for(Branch b : branchList){ %>
		             <option value ="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
		             <%} }%>
		           </select></li>
					<li><span>站长：</span>
					    <select id="commenUserid" name ="userid" >
					    	<option value ="0">==请选择==</option>
						</select>
				    </li>
					
					<li><span>最大查询数量：</span><input type ="text" id ="pageSize" name ="pageSize" value="0"  size=15 maxlength="3"/></li>
					<li><span>失败重发次数：</span><input type ="text" id ="loopcount" name ="loopcount" value="0"  size=15 maxlength="3"/></li>
					<li><span>密钥：</span><input type ="text" id ="private_key" name ="private_key"   maxlength="100"/></li>
					<li><span>是否异步回传：</span>
					<input type ="radio" id ="isasynchronous1" name ="isasynchronous" value="1" />开启
					<input type ="radio" id ="isasynchronous2" name ="isasynchronous" value="0" checked/>关闭
					</li>
					<li><span>异步回传URL：</span><input type ="text" id ="feedback_url" name ="feedback_url"   maxlength="100"/></li>
					<li><span>是否开启对接：</span>
					<input type ="radio" id ="isopenflag1" name ="isopenflag" value="1" />开启
					<input type ="radio" id ="isopenflag2" name ="isopenflag" value="0" checked/>关闭
					</li>
					<li><span>查单电话：</span><input type ="text" id ="phone" name ="phone"  maxlength="30"/></li>
					<li><font color="red">说明：转件查单使用</font></li>
					<li><span>订单号前缀：</span><input type ="text" id ="orderprefix" name ="orderprefix"  maxlength="30"/></li>
					<li><font color="red">说明：多个订单号前缀用英文逗号隔开 ，密钥上游自定义提供给下游</font></li>
				</ul>
		</div>
		<div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
