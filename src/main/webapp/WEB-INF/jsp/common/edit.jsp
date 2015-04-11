<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Common common = (Common)request.getAttribute("common");
List<Branch> branchList = (List<Branch>) request.getAttribute("branchList");
List<User> nextBranchUserlist = ( List<User>)request.getAttribute("nextBranchUserlist");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改承运商</h1>
		<form id="common_save_Form" name="common_save_Form" onSubmit="if(check_common()){submitSaveForm(this);}return false;"  action="<%=request.getContextPath()%>/common/save/${common.id}" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>承运商名称：</span><input type ="text" id ="commonname" name ="commonname" value ="${common.commonname}"  maxlength="30" class="input_text1"/>*</li>
					<li><span>承运商编号：</span><input type ="text" id ="commonnumber" name ="commonnumber" value ="${common.commonnumber}"  maxlength="30" class="input_text1"/>*</li>
					<li><span>站点：</span>
					<select id ="commenbranchid" name ="branchid" class="select1" onchange="updateCommenDeliver('<%=request.getContextPath()%>');">
		             <option value ="0">==请选择==</option> 
		            <%if(branchList != null && branchList.size()>0){ %>
		             <%for(Branch b : branchList){ %>
		             <option value ="<%=b.getBranchid() %>" <%if(common.getBranchid()>0 && common.getBranchid() == b.getBranchid()){ %> selected="selected" <%} %>><%=b.getBranchname() %></option>
		             <%} }%>
		           </select></li>
					<li><span>站长：</span>
					    <select id="commenUserid" name ="userid" class="select1">
					    <option value ="0">==请选择==</option> 
					    	<%if(nextBranchUserlist != null && nextBranchUserlist.size()>0){ %>
				             <%for(User u : nextBranchUserlist){ %>
				             <option value ="<%=u.getUserid() %>" <%if(common.getUserid()>0 && common.getUserid() == u.getUserid()){ %> selected="selected" <%} %> ><%=u.getRealname() %></option>
				             <%} }%>
						</select>
				    </li>
				    <li><span>最大查询数量：</span><input type ="text" id ="pageSize" name ="pageSize" value="${common.pageSize}" size=15 maxlength="3" class="input_text1"/></li>
				    <li><span>失败重发次数：</span><input type ="text" id ="loopcount" name ="loopcount" value="${common.loopcount}"  size=15 maxlength="3" class="input_text1"/></li>
					<li><span>密钥：</span><input type ="text" id ="private_key" name ="private_key"   value="${common.private_key}" maxlength="100" class="input_text1"/></li>
					<li><span>是否异步回传：</span>
					<input type ="radio" id ="isasynchronous1" name ="isasynchronous" value="1"   <%if(common.getIsasynchronous()==1){%>checked<%}%>/>开启
					<input type ="radio" id ="isasynchronous2" name ="isasynchronous" value="0"  <%if(common.getIsasynchronous()==0){%>checked<%}%>/>关闭
					</li>
					<li><span>异步回传URL：</span><input type ="text" id ="feedback_url" name ="feedback_url" value="${common.feedback_url}"  maxlength="100" class="input_text1"/></li>
					<li><span>是否开启对接：</span>
					<input type ="radio" id ="isopenflag" name ="isopenflag" value="1" <%if(common.getIsopenflag()==1){%>checked<%}%> />开启
					<input type ="radio" id ="isopenflag" name ="isopenflag" value="0"   <%if(common.getIsopenflag()==0){%>checked<%}%>/>关闭
					</li>
					<li><span>查单电话：</span><input type ="text" id ="phone" name ="phone"  value ="${common.phone}"  maxlength="30" class="input_text1"/></li>
					<li><font color="red">说明：转件查单使用</font></li>
					<li><span>订单号前缀：</span><input type ="text" id ="orderprefix" name ="orderprefix"  value ="${common.orderprefix}"  maxlength="30" class="input_text1"/></li>
					<li><font color="red">说明：多个订单号前缀用英文逗号隔开</font></li>
				</ul>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
