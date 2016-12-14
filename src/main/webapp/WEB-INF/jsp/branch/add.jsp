<%@page import="cn.explink.domain.PaiFeiRule"%>
<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.domain.AccountArea"%>
<%@ page import="cn.explink.domain.Branch,cn.explink.domain.Function,cn.explink.domain.SystemInstall,cn.explink.b2c.maisike.branchsyn_json.Stores"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.enumutil.BranchTypeEnum"%>
<%@page import="cn.explink.enumutil.BankEnum"%>
<%@page import="cn.explink.enumutil.TlAccountTypeEnum"%>
<%@page import="cn.explink.enumutil.CftAccountTypeEnum"%>
<%@page import="cn.explink.enumutil.PayCerTypeEnum"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.OrgPayInTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.explink.domain.VO.express.AdressVO" %>
<%
	List<Menu> menuPDAList = (List<Menu>) request.getAttribute("PDAmenu");
	List<Branch> zhongzhuanList = (List<Branch>) request.getAttribute("zhongzhuanList");
	List<Branch> tuihuoList = (List<Branch>) request.getAttribute("tuihuoList");
	List<Branch> caiwuList = (List<Branch>) request.getAttribute("caiwuList");
	SystemInstall bindmsksid=(SystemInstall)request.getAttribute("bindmsksid");
	List<Stores> mskbranchlist = (List<Stores>) request.getAttribute("mskbranchlist");
	List<PaiFeiRule> pfrulelist = (List<PaiFeiRule>) request.getAttribute("pfrulelist");
	List<Branch> accountbranchList = (List<Branch>) request.getAttribute("accountbranchList");//结算对象
	List<JSONObject> tlBankList = (List<JSONObject>)request.getAttribute("tlBankList");
	List<JSONObject> cftBankList = (List<JSONObject>)request.getAttribute("cftBankList");
    AdressVO province  = (AdressVO)request.getAttribute("province");
	List<AdressVO> cities = (List<AdressVO>)request.getAttribute("cities");
%>

<div id="box_bg" style="z-index:9005"></div>
<div id="box_contant" style="z-index:9005">
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
           		<li><span>分拣码：</span><input type="text" name="branchcode" id="branchcode" maxlength="50" />*</li>
           		<li><span>机构编号：</span><input type="text" name="tpsbranchcode" id="tpsbranchcode" maxlength="50" />*</li>
           		<li><span>保证金：</span><input type="text" name="branchBail" id="branchBail" maxlength="50" onblur="isbranchnum(this)"/>元</li>
           		<li><span>负 责 人：</span><input type="text" name="branchcontactman"  id="branchcontactman" maxlength="50"/>*</li>
		        <li><span>固定电话：</span><input type="text" name="branchphone" id="branchphone" maxlength="50" onblur="isbranchnum(this)"/></li>
				<li><span>机构手机：</span><input type="text" name="branchmobile" id="branchmobile" maxlength="50" onblur="isbranchnum(this)"/></li>
			    
			    <%--<li><span>省份：</span><input type="text" name="branchprovince" id="branchprovince"  maxlength="50"/></li>
			    <li><span>城市：</span><input type="text" name="branchcity" id="branchcity"  maxlength="50"/></li>
			    <li><span>区/县：</span><input type="text" name="brancharea" id="brancharea"  maxlength="50"/></li>
                --%>
                    <li>
                        <span>省份：</span>
                        <select id="branchprovince" name="branchprovince" style="width: 100px;" disabled="disabled">
                            <option value="<%=province.getCode()%>"><%=province.getName()%></option>
                        </select>
                    </li>
                    <li>
                        <span>城市：</span>
                        <select id="branchcity" name="branchcity" style="width: 100px;"
                                                onclick="onCityChangeForBranch('<%=request.getContextPath()%>', this);">
                            <option value="">请选择</option>
                            <%
                                for (AdressVO city : cities) {
                            %>
                            <option value="<%=city.getId()%>"><%=city.getName()%></option>
                            <%
                                }
                            %>
                        </select>
                    </li>
                    <li><span>区/县：</span>
                        <select id="brancharea" name="brancharea"
                                style="width: 100px;">
                            <option value="">请选择</option>
                        </select>
                    </li>
			    <li><span>乡镇/街道：</span><input type="text" name="branchstreet" id="branchstreet"  maxlength="50"/></li>
			    <li><span>地址：</span><input type="text" name="branchaddress" id="branchaddress" maxlength="50"/></li>
				<li><span>邮箱：</span><input type="text" name="branchemail" id="branchemail" maxlength="50"/></li>
				 <!-- <li><span>预付款后缴款设置：</span><input type="hidden" name="" class ="zhandian" /></li> -->
				 <!--对应滑槽口 tps -->
				 <li><span>对应滑槽口号：</span><input type="text" name="outputno" id="outputno" maxlength="50"/></li>
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
				 	 <%
 				 	 	for(Branch b : accountbranchList){
 				 	 %>
		             	<option value ="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		             <%
		             	}
		             %>
		            </select>
				 </li>
		         <li><span>超额设置：</span>
		           <input type="text" name="accountexcessfee" id="accountexcessfee" value="0" class ="zhandian" onblur="isbranchnum(this)"/>
		           <select id ="accountexcesstype" name ="accountexcesstype">
		             <option value ="1">元</option> 
		             <option value ="2">%</option>
		           </select>*
		         </li>
		         <li><span>信誉额度：</span>
		           <input type="text" name="credit" id="credit" value="0" class ="zhandian" onblur="isbranchnum(this)"/>元*
		         </li>
		         <li><span>24小时时效：</span>
		           <input type="text" name="prescription24" id="prescription24" value="0" class ="zhandian" onblur="isbranchnum(this)"/>小时
		         </li>
		         <li><span>48小时时效：</span>
		           <input type="text" name="prescription48" id="prescription48" value="0" class ="zhandian" onblur="isbranchnum(this)"/>小时
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
					
		             <option value ="<%=BranchTypeEnum.ZhiYing.getValue()%>"><%=BranchTypeEnum.ZhiYing.getText()%></option> 
		             <option value ="<%=BranchTypeEnum.ErJiZhan.getValue()%>"><%=BranchTypeEnum.ErJiZhan.getText()%></option>
		             <option value ="<%=BranchTypeEnum.SanJiZhan.getValue()%>"><%=BranchTypeEnum.SanJiZhan.getText()%></option>
		             <option value ="<%=BranchTypeEnum.JiaMeng.getValue()%>"><%=BranchTypeEnum.JiaMeng.getText()%></option>
		             <option value ="<%=BranchTypeEnum.JiaMengErJi.getValue()%>"><%=BranchTypeEnum.JiaMengErJi.getText()%></option>
		             <option value ="<%=BranchTypeEnum.JiaMengSanJi.getValue()%>"><%=BranchTypeEnum.JiaMengSanJi.getText()%></option>
		           </select>
		        </li>
				<li><span>派费规则：</span>
				<select id ="pfruleid" name ="pfruleid" >
				<option value="0">请选择</option>
				<%
					for(PaiFeiRule pf:pfrulelist){
				%>
				<option value="<%=pf.getId()%>"><%=pf.getName()%></option>
					<%
						}
					%>
		           </select>
		        </li>
		        <%
		        	if(bindmsksid.getValue().equals("1")){
		        %>
		        	 <li><span>绑定迈思可站点：</span>
		        	 <select name="bindmsksid" id="bindmsksid">
		        	 	<option value="0">请选择 </option>
		        	 	<%
		        	 		for(Stores sotres:mskbranchlist){
		        	 	%>
		        	 		<option value="<%=sotres.getId()%>"><%=sotres.getSname()%> </option>
		        	 	<%
		        	 		}
		        	 	%>
		        	 	
		        	 </select>
		        	 </li>
		        <%
		        	}
		        %>
		       
		        <li><span>银行卡号：</span><input type="text" name="bankcard" id="bankcard" maxlength="50" onblur="isbranchnum(this)"/></li>
				<li><span>中转站：</span><select id ="zhongzhuanid" name ="zhongzhuanid" >
		             <option value ="0">==请选择==</option> 
		             <%
 		             	for(Branch b : zhongzhuanList){
 		             %>
		             <option value ="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		             <%
		             	}
		             %>
		           </select>*</li>
				<li><span>退货站：</span><select id ="tuihuoid" name ="tuihuoid" >
		             <option value ="0">==请选择==</option> 
		             <%
 		             	for(Branch b : tuihuoList){
 		             %>
		             <option value ="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		             <%
		             	}
		             %>
		           </select>*</li>
				<li><span>交款财务：</span><select id ="caiwuid" name ="caiwuid" >
		             <option value ="0">==请选择==</option> 
		             <%
 		             	for(Branch b : caiwuList){
 		             %>
		             <option value ="<%=b.getBranchid()%>"><%=b.getBranchname()%></option>
		             <%
		             	}
		             %>
		           </select>*</li>
				 <li><span>缴款方式：</span><select id ="payinType" name ="payinType" >
				    <%
				    	for(OrgPayInTypeEnum temp: OrgPayInTypeEnum.values()){%>
							<option value ="<%=temp.getValue()%>"><%=temp.getText()%></option>
					<%} %>
	
				  </select>*</li>	
				 <li><span>分拣线提示方式：</span><select id ="remandtype" name ="remandtype" >
		             <option value ="0">==请选择==</option> 
		             <option value ="<%=BranchEnum.BuQiYong.getValue()%>"><%=BranchEnum.BuQiYong.getText()%></option>
		             <option value ="<%=BranchEnum.TiaoMaDaYin.getValue()%>"><%=BranchEnum.TiaoMaDaYin.getText()%></option>
		             <option value ="<%=BranchEnum.YuYinTiXing.getValue()%>"><%=BranchEnum.YuYinTiXing.getText()%></option>
		             <option value ="<%=BranchEnum.YuYinAndTiaoMa.getValue()%>"><%=BranchEnum.YuYinAndTiaoMa.getText()%></option>
		           </select>*</li>
		         <li  id="wav" style="display: none;top:0px;"><span>上传声音文件：</span><iframe id="update" name="update" src="branch/update?fromAction=branch_cre_Form&a=<%=Math.random()%>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe></li>     
				 <li><span>邮件：</span><input type="text" name="branchmatter" id="branchmatter"></li>
				 <!-- <li><span>导出信息设置：</span><input type="hidden" name="" class ="kefu" /></li> 
		         <li><span>查询统计内容设置：</span><input type="hidden" name="" class ="yunying" /></li>-->
	         	<!-- 自动核销用到的站点账户信息 -->
	         	 <li><span><input name="payMethodType" id="tl" type="radio" value="0" checked="checked"  onclick="payMthodchange(this.id)"/>通联</span>
	         	 	<span><input name="payMethodType" id="cft" type="radio" value="1" onclick="payMthodchange(this.id)"/>财付通</span></li>
	         	 <li><span>银行卡账号：</span><input type="text" name="bankCardNo" id="bankCardNo" maxlength="50" onblur="isbranchnum(this)"/>*</li>
		         <li><span>银行代码：</span><select id="bankCode" name="bankCode" class="select1" >
					<%
						if(tlBankList!=null && tlBankList.size()>0){
							for(JSONObject bank : tlBankList){
					%>
								<option value="<%=bank.getString("tlBankCode")%>" ><%=bank.getString("bankName")%></option>
					<%
						}}
					%>
				</select>*</li>
	           <li><span>所有人姓名：</span><input type="text" name="ownerName" id="ownerName" maxlength="50"/>*</li>
	           <li><span>账户类型：</span><select id ="bankAccountType" name ="bankAccountType" >
	              <%
	              	for(TlAccountTypeEnum temp: TlAccountTypeEnum.getAllStatus()){
	              %>
	            	 <option value ="<%=temp.getValue()%>"><%=temp.getText()%></option>
	           	  <%
	           	  	}
	           	  %>
		       </select>*</li>
			      
			      
			   <li><span>银行卡账号：</span><input type="text" name="cftAccountNo" id="cftAccountNo" maxlength="50" onblur="isbranchnum(this)"/>*</li>
		       <li><span>银行代码：</span><select id="cftBankCode" name="cftBankCode" class="select1" >
				<%
					if(cftBankList!=null && cftBankList.size()>0){
						for(JSONObject bank : cftBankList){
				%>
				<option value="<%=bank.getString("cftBankCode")%>" ><%=bank.getString("bankName")%></option>
				<%
					}}
				%>
			  </select>*</li>
			  <li><span>所有人姓名：</span><input type="text" name="cftAccountName" id="cftAccountName" maxlength="50"/>*</li>
			  <li><span>账户类型：</span><select id ="cftAccountProp" name ="cftAccountProp" >
			    <%
			    	for(CftAccountTypeEnum temp: CftAccountTypeEnum.getAllStatus()){
			    %>
			  		 <option value ="<%=temp.getValue()%>"><%=temp.getText()%></option>
			 	<%} %>
			  </select>*</li>
			   <li><span>开户人证件号：</span><input type="text" name="cftCertId" id="cftCertId" maxlength="50"/>*</li>
			  <li><span>开户证件类型：</span><select id ="cftCertType" name ="cftCertType" >
			    <%
			    	for(PayCerTypeEnum temp: PayCerTypeEnum.getAllStatus()){
			    %>
			  		 <option value ="<%=temp.getValue()%>"><%=temp.getText()%></option>
			 	<%} %>
			  </select>*</li>
		      
         </ul>
	         
			
		</div>
		<div align="center"><input type="submit" value="确认" class="button" id="sub" onclick="ghhg()"/></div>
		</form>	
	</div>
</div>

<div id="box_yy"></div>

