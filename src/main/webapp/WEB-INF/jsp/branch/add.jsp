<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.domain.AccountArea"%>
<%@ page import="cn.explink.domain.Branch,cn.explink.domain.Function,cn.explink.domain.SystemInstall,cn.explink.b2c.maisike.branchsyn_json.Stores"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<AccountArea> accountAreaList = (List<AccountArea>) request.getAttribute("accontareaList");
	List<Menu> menuPDAList = (List<Menu>) request.getAttribute("PDAmenu");
	List<Branch> zhongzhuanList = (List<Branch>) request.getAttribute("zhongzhuanList");
	List<Branch> tuihuoList = (List<Branch>) request.getAttribute("tuihuoList");
	List<Branch> caiwuList = (List<Branch>) request.getAttribute("caiwuList");
	SystemInstall bindmsksid=(SystemInstall)request.getAttribute("bindmsksid");
	List<Stores> mskbranchlist = (List<Stores>) request.getAttribute("mskbranchlist");
	
	List<Branch> accountbranchList = (List<Branch>) request.getAttribute("accountbranchList");//结算对象
%>	

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建机构</h1>
		<form id="branch_cre_Form" name="branch_cre_Form" enctype="multipart/form-data"
			 onSubmit="if(check_branch(<%=BranchEnum.ZhanDian.getValue()%>,
			 <%=BranchEnum.YunYing.getValue()%>,<%=BranchEnum.KeFu.getValue()%>,
			 <%=BranchEnum.CaiWu.getValue()%>)){submitAddBranch(this);}return false;" 
			 action="<%=request.getContextPath()%>/branch/createFile;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<div id="la" hidden="true">请选择机构类型</div>
				<ul>					
					<li> 
					<span>机构类型：</span> 
					<select id ="sitetype" name ="sitetype" 
   			onChange="click_sitetype(<%=BranchEnum.ZhanDian.getValue()%>,
			 <%=BranchEnum.YunYing.getValue()%>,<%=BranchEnum.KeFu.getValue()%>,
			 <%=BranchEnum.CaiWu.getValue()%>)">
                   <option value ="-1">==请选择==</option>
                   <option value ="<%=BranchEnum.KuFang.getValue()%>"><%=BranchEnum.KuFang.getText()%></option>
                   <option value ="<%=BranchEnum.ZhanDian.getValue()%>"><%=BranchEnum.ZhanDian.getText()%></option>
                   <option value ="<%=BranchEnum.TuiHuo.getValue()%>"><%=BranchEnum.TuiHuo.getText()%></option>
                   <option value ="<%=BranchEnum.ZhongZhuan.getValue()%>"><%=BranchEnum.ZhongZhuan.getText()%></option>
                   <option value ="<%=BranchEnum.YunYing.getValue()%>"><%=BranchEnum.YunYing.getText()%></option>
                   <option value ="<%=BranchEnum.KeFu.getValue()%>"><%=BranchEnum.KeFu.getText()%></option>
                   <option value ="<%=BranchEnum.CaiWu.getValue()%>"><%=BranchEnum.CaiWu.getText()%></option>
                   <option value ="<%=BranchEnum.QiTa.getValue()%>"><%=BranchEnum.QiTa.getText()%></option>
                </select>*</li>
					
					
           		<li><span>机构名称：</span><input type="text" name="branchname" id="branchname" maxlength="50"/>*</li>
           		<li><span>机构编号：</span><input type="text" name="branchcode" id="branchcode" maxlength="50"/>*</li>
           		<li><span>负 责 人：</span><input type="text" name="branchcontactman"  id="branchcontactman" maxlength="50"/>*</li>
		        <li><span>固定电话：</span><input type="text" name="branchphone" id="branchphone" maxlength="50"/></li>
			    
			    <li><span>省份：</span><input type="text" name="branchprovince" id="branchprovince"  maxlength="50"/></li>
			    <li><span>城市：</span><input type="text" name="branchcity" id="branchcity"  maxlength="50"/></li>
			    <li><span>区/县：</span><input type="text" name="brancharea" id="brancharea"  maxlength="50"/></li>
			    <li><span>乡镇/街道：</span><input type="text" name="branchstreet" id="branchstreet"  maxlength="50"/></li>
			    <li><span>地址：</span><input type="text" name="branchaddress" id="branchaddress" maxlength="50"/></li>
			   <%--  <li><span>所属区域：</span><select name="accountarea" id="accountarea">
				    <option value ="0">请选择</option>
				    <%for(AccountArea accountarea:accountAreaList){ %>
				     <option value ="<%=accountarea.getAreaid()%>"><%=accountarea.getAreaname() %></option>
				    <%} %>
				  </select></li> --%>
				 <li><span>站点手机：</span><input type="text" name="branchmobile" id="branchmobile" maxlength="50"/></li>
				 <li><span>邮箱：</span><input type="text" name="branchemail" id="branchemail" maxlength="50"/></li>
				 <!-- <li><span>预付款后缴款设置：</span><input type="hidden" name="" class ="zhandian" /></li> -->
				 
				 
				 <li><span>结算类型：</span>
				 	<select id ="accounttype" name ="accounttype" class ="zhandian"  style="width:150px;">
				 	 <option value ="0">==请选择==</option> 
		             <option value ="1">买单结算</option> 
		             <option value ="2">配送结果结算</option>
		             <option value ="3">扣款结算</option>
		            </select>
				 </li>
				 <li>
				 	<span>结算对象：</span>
				 	<select id ="accountbranch" name ="accountbranch" class ="zhandian"  style="width:150px;">
				 	<option value ="0">==请选择==</option> 
				 	 <%for(Branch b : accountbranchList){ %>
		             	<option value ="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
		             <%} %>
		            </select>
				 </li>
		         <li><span>超额设置：</span>
		           <input type="text" name="accountexcessfee" id="accountexcessfee" value="0" class ="zhandian" />
		           <select id ="accountexcesstype" name ="accountexcesstype">
		             <option value ="1">元</option> 
		             <option value ="2">%</option>
		           </select>*
		         </li>
		         <li><span>信誉额度：</span>
		           <input type="text" name="credit" id="credit" value="0" class ="zhandian" />元*
		         </li>
		         <li><span>24小时时效：</span>
		           <input type="text" name="prescription24" id="prescription24" value="0" class ="zhandian" />小时
		         </li>
		         <li><span>48小时时效：</span>
		           <input type="text" name="prescription48" id="prescription48" value="0" class ="zhandian" />小时
		         </li>
		         <li><span>退货出站超时时效：</span>
		           	<select id ="backtime" name ="backtime" class ="zhandian"  style="width:150px;">
				 	 <option value ="0">==请选择==</option> 
		             <option value ="24">24</option> 
		             <option value ="72">72</option>
		            </select>小时
		         </li>
		         
		         <li id="pda_title" ><span>组织的货物操作权限</span>（PDA）：</li>
				<ul id="pda" class="checkedbox1"><%
							for (Menu menu : menuPDAList) {
						%><li><label> <input type="checkbox" name="functionids"
								value="<%=menu.getMenuno()%>" /> <%=menu.getName()%>
						</label></li><%
							}
				%></ul>
				
				<li><span>站点类型：</span><select id ="contractflag" name ="contractflag" onchange="changemskshow(this.value)">
		             <option value ="0">直营</option> 
		             <option value ="1">加盟</option>
		             <option value ="2">二级站</option>
		           </select>
		        </li>
		        
		        
		        
		        <%if(bindmsksid.getValue().equals("1")){ %>
		        	 <li><span>绑定迈思可站点：</span>
		        	 <select name="bindmsksid" id="bindmsksid">
		        	 	<option value="0">请选择 </option>
		        	 	<%for(Stores sotres:mskbranchlist){%>
		        	 		<option value="<%=sotres.getId()%>"><%=sotres.getSname() %> </option>
		        	 	<%} %>
		        	 	
		        	 </select>
		        	 </li>
		        <%} %>
		       
		        <li><span>银行卡号：</span><input type="text" name="bankcard" id="bankcard" maxlength="50"/></li>
				<li><span>中转站：</span><select id ="zhongzhuanid" name ="zhongzhuanid" >
		             <option value ="0">==请选择==</option> 
		             <%for(Branch b : zhongzhuanList){ %>
		             <option value ="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
		             <%} %>
		           </select>*</li>
				<li><span>退货站：</span><select id ="tuihuoid" name ="tuihuoid" >
		             <option value ="0">==请选择==</option> 
		             <%for(Branch b : tuihuoList){ %>
		             <option value ="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
		             <%} %>
		           </select>*</li>
				<li><span>交款财务：</span><select id ="caiwuid" name ="caiwuid" >
		             <option value ="0">==请选择==</option> 
		             <%for(Branch b : caiwuList){ %>
		             <option value ="<%=b.getBranchid() %>"><%=b.getBranchname() %></option>
		             <%} %>
		           </select>*</li>
					
				 <li><span>分拣线提示方式：</span><select id ="remandtype" name ="remandtype" >
		             <option value ="0">==请选择==</option> 
		             <option value ="<%=BranchEnum.BuQiYong.getValue()%>"><%=BranchEnum.BuQiYong.getText()%></option>
		             <option value ="<%=BranchEnum.TiaoMaDaYin.getValue()%>"><%=BranchEnum.TiaoMaDaYin.getText()%></option>
		             <option value ="<%=BranchEnum.YuYinTiXing.getValue()%>"><%=BranchEnum.YuYinTiXing.getText()%></option>
		             <option value ="<%=BranchEnum.YuYinAndTiaoMa.getValue()%>"><%=BranchEnum.YuYinAndTiaoMa.getText()%></option>
		           </select>*</li>
		         <li  id="wav" style="display: none;top:0px;"><span>上传声音文件：</span><iframe id="update" name="update" src="branch/update?fromAction=branch_cre_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe></li>     
				 <li><span>邮件：</span><input type="text" name="branchmatter" id="branchmatter"></li>
				 <!-- <li><span>导出信息设置：</span><input type="hidden" name="" class ="kefu" /></li> 
		         <li><span>查询统计内容设置：</span><input type="hidden" name="" class ="yunying" /></li>-->
	         </ul>
	         
			
		</div>
		<div align="center"><input type="submit" value="确认" class="button" id="sub" onclick="ghhg()"/></div>
		</form>	
	</div>
</div>

<div id="box_yy"></div>

