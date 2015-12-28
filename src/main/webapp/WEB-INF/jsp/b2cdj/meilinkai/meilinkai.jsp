<%@page import="cn.explink.b2c.meilinkai.MeiLinKai"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.ReasonTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<%
MeiLinKai meilinkai = request.getAttribute("meilinkai")==null?null:((MeiLinKai)request.getAttribute("meilinkai"));
List<Branch> warehouselist = (List<Branch>)request.getAttribute("warehouselist");
%>

<script type="text/javascript">   



</script>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>玫琳凯对接设置</h1>
		<form id="smile_save_Form" name="smile_save_Form"  onSubmit="submitSaveForm(this);return false;" action="<%=request.getContextPath()%>/meilinkai/save/${joint_num}" method="post">
		<div id="box_form">
			<ul>
				<%if(meilinkai != null){ %>
					<li><span>用户名：</span>
 						<input type ="text" id="usrename" name ="usrename"  maxlength="300"  value="<%=meilinkai.getUsrename()%>"  > 
					</li>
					<li><span>密码：</span>
 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value="<%=meilinkai.getPwd()%>"  > 
					</li>
					<li><span>用户验证方法：</span>
 						<input type ="text" id="checkUsermethod" name ="checkUsermethod"  maxlength="300"  value="<%=meilinkai.getCheckUsermethod()%>"  > 
					</li>
					<li><span>回传货态方法：</span>
 						<input type ="text" id="hdtolipsmethod" name ="hdtolipsmethod"  maxlength="300"  value="<%=meilinkai.getHdtolipsmethod()%>"  > 
					</li>
					<li><span>电商ID：</span>
 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value="<%=meilinkai.getCustomerid()%>"  > 
					</li>
					<li><span>回传货态URL：</span>
 						<input type ="text" id="hdtolipsUrl" name ="hdtolipsUrl"  maxlength="300"  value="<%=meilinkai.getHdtolipsUrl()%>"  size="15" > 
					</li>
					<li><span>最大查询量：</span>
 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value="<%=meilinkai.getMaxCount()%>"    > 
					</li>
					<li><span>承运商编码：</span>
 						<input type ="text" id="shipJDECarrierNum" name ="shipJDECarrierNum"  maxlength="300"  value="<%=meilinkai.getShipJDECarrierNum()%>"    > 
					</li>
					<li><span>下载订单URL：</span>
 						<input type ="text" id="lipstohdUrl" name ="lipstohdUrl"  maxlength="300"  value="<%=meilinkai.getLipstohdUrl()%>"  size="15" > 
					</li>
					<li><span>订单导入库房：</span>
						<select name="warehouseid">
							<option value="0">请选择库房</option>
							<%for(Branch b:warehouselist){%>
								<option value="<%=b.getBranchid()%>" <%if(meilinkai.getWarehouseid()==b.getBranchid()){%>selected<%}%>><%=b.getBranchname() %></option>
							<%}%>
						</select>
					</li>
					
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value="explink" maxlength="30" size="20" > 
					</li>
				<%}else{ %>
					<li><span>用户名：</span>
 						<input type ="text" id="usrename" name ="usrename"  maxlength="300"  value=""  > 
					</li>
					<li><span>密码：</span>
 						<input type ="text" id="pwd" name ="pwd"  maxlength="300"  value=""  > 
					</li>
					<li><span>用户验证方法：</span>
 						<input type ="text" id="checkUsermethod" name ="checkUsermethod"  maxlength="300"  value="" > 
					</li>
					<li><span>回传货态方法：</span>
 						<input type ="text" id="hdtolipsmethod" name ="hdtolipsmethod"  maxlength="300"  value="" > 
					</li>
					<li><span>电商ID：</span>
 						<input type ="text" id="customerid" name ="customerid"  maxlength="300"  value=""  > 
					</li>
					<li><span>回传货态URL：</span>
 						<input type ="text" id="hdtolipsUrl" name ="hdtolipsUrl"  maxlength="300"  value=""  size="15" > 
					</li>
					<li><span>最大查询量：</span>
 						<input type ="text" id="maxCount" name ="maxCount"  maxlength="300"  value=""    > 
					</li>
					<li><span>承运商编码：</span>
 						<input type ="text" id="shipJDECarrierNum" name ="shipJDECarrierNum"  maxlength="300"  value="" > 
					</li>
					<li><span>下载订单URL：</span>
 						<input type ="text" id="lipstohdUrl" name ="lipstohdUrl"  maxlength="300"  value=""  size="15" > 
					</li>
				
					<li><span>订单导入库房：</span>
						<select name="warehouseid">
							<option value="0">请选择库房</option>
							<%for(Branch b:warehouselist){
							%>
								<option value="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
							<%}%>
						</select>
					</li>
					<li><span>密码：</span>
 						<input type ="password" id="password" name ="password" value="explink" maxlength="30" size="20" > 
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

