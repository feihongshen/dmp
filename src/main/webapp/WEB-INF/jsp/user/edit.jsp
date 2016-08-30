<%@page import="cn.explink.enumutil.JiesuanstateEnum"%>
<%@page import="cn.explink.enumutil.Sexenum"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.UserEmployeestatusEnum,cn.explink.domain.Branch,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@page import="cn.explink.domain.PaiFeiRule"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	User u = (User)request.getAttribute("user"); 
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches") ;
	List<Role> roleList = (List<Role>)request.getAttribute("roles") ;
	List<PaiFeiRule> pfrulelist = (List<PaiFeiRule>) request.getAttribute("pfrulelist");
	long loginForbiddenPleaseWaitMinutes = Long.valueOf(request.getAttribute("loginForbiddenPleaseWaitMinutes").toString());
%>

<%

if(request.getAttribute("user")!=null){ 
	User user = (User)request.getAttribute("user");
%>
<script type="text/javascript">
var initUser = new Array();
initUser[0]="<%=user.getBranchid() %>,branchid";
initUser[1]="<%=user.getShowphoneflag() %>,showphoneflag";
initUser[2]="<%=user.getEmployeestatus() %>,employeestatus";
initUser[3]="<%=user.getRoleid() %>,roleid";
initUser[4]="<%=user.getShowphoneflag() %>,showphoneflag";
</script>
<%} %>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改用户</h1>
		<form id="user_save_Form" name="user_save_Form" enctype="multipart/form-data"
			 onSubmit="if(check_user()){submitSaveUser(this,<%=u.getUserid() %>);}return false;" 
			 action="<%=request.getContextPath()%>/user/saveFile/<%=u.getUserid() %>;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
					<li><span>姓名：</span>
						<input type="text" id="realname" name="realname" value="<%=u.getRealname() %>" maxlength="50"/>*
						<input type="hidden" id="creandsave" name="creandsave" value="1"/>
					</li>
					<li><span>性别：</span>
						<select id="sex" name="sex">
							<option value="-1" selected>----请选择----</option>
							<option value="<%=Sexenum.Man.getValue() %>" <%if(Sexenum.Man.getValue()==u.getSex()){ %>selected = "selected" <%}%>><%=Sexenum.Man.getText() %></option>
							<option value="<%=Sexenum.Woman.getValue() %>" <%if(Sexenum.Woman.getValue()==u.getSex()){ %>selected = "selected" <%}%> ><%=Sexenum.Woman.getText() %></option>
						</select>*
					</li>
	           		<li><span>所属机构：</span>
	           		<select id="branchid" name="branchid">
						<option value="-1" selected>----请选择----</option>
						<%for(Branch b : branchList){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
						<%} %>
					</select>*
					</li>
	           		<li><span>用户角色：</span>
						<select id="roleid" name="roleid" onchange="roleChange()">
							<option value="-1" selected>----请选择----</option>
							<%for(Role r : roleList){ %>
							<option value="<%=r.getRoleid() %>" ><%=r.getRolename() %></option>
							<%} %>
				        </select>*
					</li>
					<li >
					<span>是否拥有强制出库权</span>
						<select id ="isImposedOutWarehouse " name ="isImposedOutWarehouse">
							<option value="1" <%if(u.getIsImposedOutWarehouse()==1){ %>selected<%} %>>是</option>
							<option value="0" <%if(u.getIsImposedOutWarehouse()==0){ %>selected<%} %>>否</option>
						</select>
					</li>
					<li><span>派费规则：</span>
					<select id ="pfruleid" name ="pfruleid" >
					<option value="0">请选择</option>
					<%if(pfrulelist!=null&&!pfrulelist.isEmpty()){
						for(PaiFeiRule pf:pfrulelist){ %>
						<option value="<%=pf.getId()%>" <%if(u.getPfruleid()==pf.getId()){%>selected="selected"<%} %>><%=pf.getName() %></option>
						<%} }%>
			           </select>
			        </li>
					<li id="pda_title" ><span>用户对货物操作权限</span>（PDA）：(机构的“组织的货物操作权限”与角色的“货物操作权限”的交集)</li>
					<ul id="pda" class="checkedbox1"></ul>
					 <li><span>登录用户名：</span><input type="text" id="username" name="username" value="<%=u.getUsername() %>" maxlength="50" />*</li>
					 <li><span>　</span>密码不可查看，如忘记请直接改为新密码</li>
			         <li><span>网页登录密码：</span><input type="password" id="webPassword" name="webPassword" value="<%=u.getWebPassword() %>" maxlength="50"/>*</li>
			         <li><span>确认网页登录密码：</span><input type="password" id="webPassword1" name="webPassword1" value="<%=u.getWebPassword() %>" maxlength="50"/>*</li>
			         <div id="pdaPwdDiv" style="display:none">
					 <li><span>POS登录密码：</span><input type="password" id="password" name="password" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
			         <li><span>确认POS登录密码：</span><input type="password" id="password1" name="password1" value="<%=u.getPassword() %>" maxlength="50"/>*</li>
			         </div>
	           		<li><span>上传声音文件：</span><iframe id="update" name="update" src="user/update?fromAction=user_save_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
			         <%if(u.getUserwavfile()!=null&&u.getUserwavfile().length()>4){ %>
		         	<a href="#" onclick="	
			         	var audioElement = document.createElement('audio');
			        	audioElement.setAttribute('src', '<%=request.getContextPath()+ServiceUtil.wavPath+u.getUserwavfile() %>');
			     		audioElement.load();
			     		audioElement.play();
		     		">点击测试</a>
		         <%} %>
	           		</li>
	           		
	           		<!-- <li><span>导出时联系方式：</span>
	           			<select id="showphoneflag" name="showphoneflag">
							<option value="-1" selected>----请选择----</option>
							<option value="0">不可见</option>
							<option value="1">可见</option>
						</select>*
					</li> -->
					<li><span>信息是否可见：</span>
					 <input  type="checkbox" value="1" id="consigneename" name="consigneename" <%if(u.getShownameflag()==1){ %> checked="checked"<%} %>>收件人</input>
					 <input type="checkbox"  value="1" id="consigneephone" name="consigneephone" <%if(u.getShowphoneflag()==1){ %> checked="checked"<%} %>>收件人电话</input>
					 <input type="checkbox"  value="1" id="consigneemobile" name="consigneemobile" <%if(u.getShowmobileflag()==1){ %> checked="checked"<%} %>>收件人手机</input>
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
				    <li><span>身份证号：</span><input type="text" id="idcardno" name="idcardno" value="<%=u.getIdcardno() %>" maxlength="50"/>*</li>
				    <%-- <li><span>基本工资：</span><input type="text"  id="usersalary" name="usersalary" value="<%=u.getUsersalary() %>" /></li>
					
					 <li><span>电话：</span><input type="text" id="userphone"  name="userphone" value="<%=u.getUserphone() %>" /></li> --%>
					 <li><span>手机：</span><input type="text" id="usermobile"  name="usermobile" value="<%=u.getUsermobile() %>" maxlength="50"/><span style="text-align: left;" name="tip" id="tip">*</span></li>
					 <li><span>Email/QQ/MSN：</span><input type="text"  id="useremail" name="useremail" value="<%=u.getUseremail() %>" maxlength="50"/></li>
			         <%-- <li><span>联系地址：</span><input type="text" id="useraddress" name="useraddress" value="<%=u.getUseraddress() %>" /></li>
			         <li><span>备注信息：</span><input type="text" id="userremark" name="userremark" value="<%=u.getUserremark() %>" /></li> --%>
			         
			         <li><span>入职日期：</span><input type="text"  id="startworkdate" name="startworkdate" value="<%=u.getStartworkdate()==null?"":u.getStartworkdate() %>" maxlength="50"/></li>
			         <li><span>工号：</span><input type="text"  id="jobnum" name="jobnum" value="<%=u.getJobnum()==null?"":u.getJobnum() %>" maxlength="50"/></li>
	             	 <li><span>最高扣款额度：</span><input type="text"  id="maxcutpayment" name="maxcutpayment" value="<%=u.getMaxcutpayment()==null?"":u.getMaxcutpayment() %>" maxlength="50"/></li>
	             	 <li><span>基础预付款：</span><input type="text"  id="basicadvance" name="basicadvance" value="<%=u.getBasicadvance()==null?"":u.getBasicadvance() %>" maxlength="50"/></li>
	             	 <li><span>后期预付款：</span><input type="text"  id="lateradvance" name="lateradvance" value="<%=u.getLateradvance()==null?"":u.getLateradvance() %>" maxlength="50"/></li>
	                 <li><span>固定预付款：</span><input type="text"  id="fixedadvance" name="fixedadvance" value="<%=u.getFixedadvance()==null?"":u.getFixedadvance() %>" maxlength="50"/></li>
	                 <li><span>保底单量：</span><input type="text"  id="fallbacknum" name="fallbacknum" value="<%=u.getFallbacknum() %>" maxlength="50"/></li>
	                 <li><span>基础派费：</span><input type="text"  id="basicfee" name="basicfee" value="<%=u.getBasicfee() %>" maxlength="50"/></li>
	                 <li><span>区域派费：</span><input type="text"  id="areafee" name="areafee" value="<%=u.getAreafee() %>" maxlength="50"/></li>
	                 <%if(loginForbiddenPleaseWaitMinutes>0){ %>
			         <li><span>登录次数限制：</span>
						<label>此用户已被禁止登录，<%=loginForbiddenPleaseWaitMinutes %>分钟后会自动解禁。</label>
						<input type="button"  value="提前解除登录封禁" id="liftLoginForbiddanceButton" onclick="liftLoginForbiddance('<%=u.getUserid() %>', '<%=request.getContextPath()%>')"/><br/><br/>
						</span>
					 </li>
			         <%} %>
			         
	         </ul>
		</div>
		<div align="center">
        <input type="hidden" id="usercustomerid" name="usercustomerid" value="0" /><!-- 0表示为内部员工 -->
        <input type="submit"  value="保存" class="button" id="sub" /><br/><br/>
        </div>
	</form>
	</div>
</div>

<div id="box_yy"></div>

</BODY>
</HTML>

</script>
