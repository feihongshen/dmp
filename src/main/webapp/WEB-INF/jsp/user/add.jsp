<%@page import="cn.explink.enumutil.JiesuanstateEnum"%>
<%@page import="cn.explink.enumutil.Sexenum"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.PaiFeiRule"%>
<%
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches") ;
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
	List<PaiFeiRule> pfrulelist = (List<PaiFeiRule>) request.getAttribute("pfrulelist");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建用户</h1>
		<form id="user_cre_Form" name="user_cre_Form" enctype="multipart/form-data"
			 onSubmit="if(check_user()){submitAddUser(this);}return false;" 
			 action="<%=request.getContextPath()%>/user/createFile;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
				<ul>
					<li><span>姓名：</span>
						<input type="text" id="realname" name="realname" value="" maxlength="50"/>*
						<input type="hidden" id="creandsave" name="creandsave" value="0"/>
					</li>
					<li><span>性别：</span>
						<select id="sex" name="sex">
							<option value="-1" selected>----请选择----</option>
							<option value="<%=Sexenum.Man.getValue() %>" ><%=Sexenum.Man.getText() %></option>
							<option value="<%=Sexenum.Woman.getValue() %>" ><%=Sexenum.Woman.getText() %></option>
						</select>*
					</li>
	           		<li><span>所属机构：</span>
	           		<select id="branchid"  name="branchid">
						<option value="-1" selected>----请选择----</option>
						<%for(Branch b : branchList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
						<%} %></select>*
					</li>
	           		<li><span>用户角色：</span>
						<select id="roleid" name="roleid" onchange="roleChange()">
							<option value="-1" selected>----请选择----</option>
							<%for(Role r : roleList){ %>
							<option value="<%=r.getRoleid() %>" ><%=r.getRolename() %></option>
							<%} %>
				        </select>*
					</li>
					<li>
					<span>是否拥有强制出库权</span>
						<select id ="isImposedOutWarehouse " name ="isImposedOutWarehouse">
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
					</li>
					<li><span>派费规则：</span>
					<select id ="pfruleid" name ="pfruleid" >
						<option value="0">请选择</option>
						<%if(pfrulelist!=null&&!pfrulelist.isEmpty()){
							for(PaiFeiRule pf:pfrulelist){ %>
							<option value="<%=pf.getId()%>"><%=pf.getName() %></option>
						<%} }%>
			        </select>
			        </li>
					<li id="pda_title" ><span>用户对货物操作权限</span>（PDA）：(机构的“组织的货物操作权限”与角色的“货物操作权限”的交集)</li>
					<ul id="pda" class="checkedbox1"></ul>
					<li><span>登录用户名：</span><input type="text" id="username" name="username" value="" maxlength="50" />*</li>
			        <li><span>网页登录密码：</span><input type="password" id="webPassword" name="webPassword" value="" maxlength="50"/>*</li>
			        <li><span>确认网页登录密码：</span><input type="password" id="webPassword1" name="webPassword1" value="" maxlength="50"/>*</li>
			        <div id="pdaPwdDiv" style="display:none">
					<li><span>POS登录密码：</span><input type="password" id="password" name="password" value="" maxlength="50"/>*</li>
			        <li><span>确认POS登录密码：</span><input type="password" id="password1" name="password1" value="" maxlength="50"/>*</li>
			        </div>
	           		<li><span>上传声音文件：</span><iframe id="update" name="update" src="user/update?fromAction=user_cre_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe></li>
	           	<!-- 	<li><span>导出时联系方式：</span>
	           			<select id="showphoneflag" name="showphoneflag">
							<option value="-1" selected>----请选择----</option>
							<option value="0">不可见</option>
							<option value="1">可见</option>
						</select>*
					</li> -->
					<li><span>信息是否可见：</span>
					 <input  type="checkbox" value="1" id="consigneename" name="consigneename">收件人</input>
					 <input type="checkbox"  value="1" id="consigneephone" name="consigneephone">收件人电话</input>
					 <input type="checkbox"  value="1" id="consigneemobile" name="consigneemobile">收件人手机</input>
					</li>
					<li><span>　</span>不勾选 ，则订单的收件人在页面显示时全部隐藏</li>
			        <li><span>工作状态：</span>
						<select id="employeestatus" name="employeestatus" onchange="changeJSstate('<%=request.getContextPath()%>/user/getjiesuanstate');">
							<option value="<%=UserEmployeestatusEnum.GongZuo.getValue() %>" ><%=UserEmployeestatusEnum.GongZuo.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.XiuJia.getValue() %>" ><%=UserEmployeestatusEnum.XiuJia.getText() %></option>
							<option value="<%=UserEmployeestatusEnum.LiZhi.getValue() %>" ><%=UserEmployeestatusEnum.LiZhi.getText() %></option>
				       		<option value="<%=UserEmployeestatusEnum.DaiLiZhi.getValue() %>" ><%=UserEmployeestatusEnum.DaiLiZhi.getText() %></option>
				        </select>*
					</li>
					<li><span>结算状态：</span>
	             		<select id="jiesuanstate" name="jiesuanstate">
	             			<option value="<%=JiesuanstateEnum.ZhengchangJiesuan.getValue()%>"><%=JiesuanstateEnum.ZhengchangJiesuan.getText() %></option>
				        </select>*
	             	</li>
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="" maxlength="50"/>*(必填项)</li>
				    <!-- <li><span>基本工资：</span><input type="text"  id="usersalary" name="usersalary" value="" /></li> -->
					
					 <!-- <li><span>电话：</span><input type="text" id="userphone"  name="userphone" value="" /></li> -->
					 <li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="" maxlength="50"/>*(必填项)<span style="text-align: left;" name="tip" id="tip"></span></li>
					 <li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="" /></li>
			        <!--  <li><span>联系地址：</span><input type="text" id="useraddress" name="useraddress" value="" /></li> -->
			        <!-- <li><span>备注信息：</span><input type="text" id="userremark" name="userremark" value="" /></li> -->
			        
			         <li><span>入职日期：</span><input type="text"  id="startworkdate" name="startworkdate" value="" maxlength="50"/>*(无需填写,点击确认时自动生成入职日期!)</li>
			         <li><span>工号：</span><input type="text"  id="jobnum" name="jobnum" value="" maxlength="50"/></li>
	             	 <li><span>最高扣款额度：</span><input type="text"  id="maxcutpayment" name="maxcutpayment" value="" maxlength="50"/></li>
	             	 <li><span>基础预付款：</span><input type="text"  id="basicadvance" name="basicadvance" value="" maxlength="50"/></li>
	             	 <li><span>后期预付款：</span><input type="text"  id="lateradvance" name="lateradvance" value="" maxlength="50"/>*(无需填写,动态变化的结果!)</li>
	                 <li><span>固定预付款：</span><input type="text"  id="fixedadvance" name="fixedadvance" value="" maxlength="50"/></li>
	                 <li><span>保底单量：</span><input type="text"  id="fallbacknum" name="fallbacknum" value="" maxlength="50"/></li>
	                 <li><span>基础派费：</span><input type="text"  id="basicfee" name="basicfee" value="" maxlength="50"/></li>
	                 <li><span>区域派费：</span><input type="text"  id="areafee" name="areafee" value="" maxlength="50"/></li>
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="usercustomerid" name="usercustomerid" value="0" /><!-- 0表示为内部员工 -->
        <input type="submit" value="确认" class="button" id="sub" /></div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>
