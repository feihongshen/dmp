<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.domain.PaiFeiRule"%>
<%@page import="cn.explink.domain.AccountArea"%>
<%@ page import="cn.explink.domain.Branch,cn.explink.domain.Function,cn.explink.util.ServiceUtil,cn.explink.domain.SystemInstall,cn.explink.b2c.maisike.branchsyn_json.Stores"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="cn.explink.util.ResourceBundleUtil"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.enumutil.BranchTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	Branch branch = (Branch)request.getAttribute("b");
	List<AccountArea> accountAreaList = (List<AccountArea>)request.getAttribute("accontareaList");
	List<Menu> menuPDAList = (List<Menu>)request.getAttribute("PDAmenu");
	List<Branch> zhongzhuanList = (List<Branch>) request.getAttribute("zhongzhuanList");
	List<Branch> tuihuoList = (List<Branch>) request.getAttribute("tuihuoList");
	List<Branch> caiwuList = (List<Branch>) request.getAttribute("caiwuList");
	SystemInstall bindmsksid=(SystemInstall)request.getAttribute("bindmsksid");
	List<Stores> mskbranchlist = (List<Stores>) request.getAttribute("mskbranchlist");
	List<Branch> accountbranchList = (List<Branch>) request.getAttribute("accountbranchList");//结算对象
	List<PaiFeiRule> pfrulelist = (List<PaiFeiRule>) request.getAttribute("pfrulelist");
%>

<script type="text/javascript" >
	var initEditArray = new Array();
	initEditArray[0]="<%=branch.getCheckremandtype()%>,remandtype";
	initEditArray[1]="<%=branch.getAccountareaid()%>,accountarea";
	initEditArray[2]="<%=branch.getSitetype()%>,sitetype";
	initEditArray[3]="<%=branch.getZhongzhuanid()%>,zhongzhuanid";
	initEditArray[4]="<%=branch.getTuihuoid()%>,tuihuoid";
	initEditArray[5]="<%=branch.getCaiwuid()%>,caiwuid";
</script>

<script type="text/javascript">
	var initBranchList = new Array();
	<%int i=0 ; for(String f:branch.getFunctionids().split(",")){%>
		initBranchList[<%=i++ %>]="<%=f %>";
	<%}%>
</script>

<div id="box_bg"></div>

<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1>
			<div id="close_box" onclick="closeBox()"></div>
			修改机构
		</h1>
		<form id="branch_save_Form" name="branch_save_Form" enctype="multipart/form-data"
			 onSubmit="if(check_branch(<%=BranchEnum.ZhanDian.getValue()%>,
			 <%=BranchEnum.YunYing.getValue()%>,<%=BranchEnum.KeFu.getValue()%>,
			 <%=BranchEnum.CaiWu.getValue()%>)){submitEditBranch(this,<%=branch.getBranchid() %>);}return false;" 
			 action="<%=request.getContextPath()%>/branch/saveFile/<%=branch.getBranchid()%>;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
			<ul>
				<li><span>机构类型：</span> 
					<select id ="sitetype" name ="sitetype"
						onChange="click_sitetype(
						<%=BranchEnum.ZhanDian.getValue()%>,
						<%=BranchEnum.YunYing.getValue()%>,
						<%=BranchEnum.KeFu.getValue()%>,
						<%=BranchEnum.CaiWu.getValue()%>)"
					>
		                <option value ="-1">==请选择==</option>
		                <option value ="<%=BranchEnum.KuFang.getValue()%>"><%=BranchEnum.KuFang.getText() %></option>
		                <option value ="<%=BranchEnum.ZhanDian.getValue() %>"><%=BranchEnum.ZhanDian.getText() %></option>
		                <option value ="<%=BranchEnum.TuiHuo.getValue() %>"><%=BranchEnum.TuiHuo.getText() %></option>
		                <option value ="<%=BranchEnum.ZhongZhuan.getValue() %>"><%=BranchEnum.ZhongZhuan.getText() %></option>
		                <option value ="<%=BranchEnum.YunYing.getValue() %>"><%=BranchEnum.YunYing.getText() %></option>
		                <option value ="<%=BranchEnum.KeFu.getValue() %>"><%=BranchEnum.KeFu.getText() %></option>
		                <option value ="<%=BranchEnum.CaiWu.getValue() %>"><%=BranchEnum.CaiWu.getText() %></option>
		                <option value ="<%=BranchEnum.QiTa.getValue() %>"><%=BranchEnum.QiTa.getText() %></option>
	               	</select>
	               	*
	            </li>
          		<li>
          			<span>机构名称：</span>
          			<input type="text" name="branchname" id="branchname" value ="<%=branch.getBranchname() %>" maxlength="50"/>
          			*
          		</li>
          		<li>
          			<span>分拣码：</span>
          			<input type="text" name="branchcode" id="branchcode" value ="<%=branch.getBranchcode() %>" maxlength="50"/>
          			*
       			</li>
       			<li>
          			<span>机构编码：</span>
          			<input type="text" name="tpsbranchcode" id="tpsbranchcode" value ="<%=branch.getTpsbranchcode() %>" maxlength="50"/>
          			*
       			</li>
       			<li>
          			<span>保证金：</span>
          			<input type="text" name="branchBail" id="branchBail" value ="<%=branch.getBranchBail().equals("null")?"0.00":branch.getBranchBail() %>" maxlength="50" onblur="if(!isFloat($(this).val())){ alert('金额输入有误！');$(this).val('0.00');}"/>元
       			</li>
		        <li>
		        	<span>负 责 人：</span>
		        	<input type="text" name="branchcontactman" id="branchcontactman" value ="<%=branch.getBranchcontactman() %>" maxlength="50"/>
		        	*
	        	</li>
		        <li>
		        	<span>固定电话：</span>
		        	<input type="text" name="branchphone" id="branchphone" value ="<%=branch.getBranchphone()%>" maxlength="50"/>
		        </li>
		        <li>
			 		<span>机构手机：</span>
			 		<input type="text" name="branchmobile" id="branchmobile" value ="<%=branch.getBranchmobile()%>" maxlength="50"/>
			 	</li>
			    <li>
				    <span>省份：</span>
				    <input type="text" name="branchprovince" id="branchprovince" value ="<%=branch.getBranchprovince()%>" maxlength="50"/>
				</li>
			    <li>
			    	<span>城市：</span>
			    	<input type="text" name="branchcity" id="branchcity" value ="<%=branch.getBranchcity()%>" maxlength="50"/>
			    </li>
			    <li>
			    	<span>区/县：</span>
			    	<input type="text" name="brancharea" id="brancharea" value ="<%=branch.getBrancharea()%>" maxlength="50"/>
			    </li>
			    <li>
			    	<span>乡镇/街道：</span>
			    	<input type="text" name="branchstreet" id="branchstreet" value ="<%=branch.getBranchstreet()%>" maxlength="50"/>
		    	</li>
			    <li>
			    	<span>地址：</span>
			    	<input type="text" name="branchaddress" id="branchaddress" value ="<%=branch.getBranchaddress()==null?"":(branch.getBranchaddress().length()>0?branch.getBranchaddress().replace("\"", "&quot;"):branch.getBranchaddress())%>" maxlength="50"/>
		    	</li>
		   		<%--  
		   		<li>
			   		<span>所属区域：</span>
			   		<select name="accountarea" id="accountarea">
				    <option value ="0">请选择</option>
				    <%for(AccountArea accountarea:accountAreaList){ %>
				     <option value ="<%=accountarea.getAreaid()%>"><%=accountarea.getAreaname() %></option>
				    <%} %>
				  	</select>
			  	</li> 
			  	--%>
			 	<li>
			 		<span>邮箱：</span>
			 		<input type="text" name="branchemail" id="branchemail" value ="<%=branch.getBranchemail()%>" maxlength="50"/>
			 	</li>
				<!--
				<li>
					<span>预付款后缴款设置：</span>
					<input type="hidden" name="" class ="zhandian" />
				</li>
			 	<li>
			 		<span>账户设置：</span>
			 		<input type="hidden" name="" class ="zhandian" />
			 	</li>
	         	<li>
	         		<span>额度设置：</span>
	         		<input type="hidden" name="" class ="zhandian" />
	         	</li>
	         	-->
	         <li>
		         <span>结算类型：</span>
				 <select id ="accounttype" name ="accounttype" class ="zhandian"  style="width:150px;">
				 	 <option value ="0">==请选择==</option> 
		             <option value ="1" <%=branch.getAccounttype()==1?"selected":"" %>>买单结算</option> 
		             <option value ="2" <%=branch.getAccounttype()==2?"selected":"" %>>配送结果结算</option>
		             <option value ="3" <%=branch.getAccounttype()==3?"selected":"" %>>扣款结算</option>
		         </select>
			 </li>
			 <li>
			 	<span>结算对象：</span>
			 	<select id ="accountbranch" name ="accountbranch" class ="zhandian"  style="width:150px;">
			 		<option value ="0">==请选择==</option> 
				 	<%for(Branch b : accountbranchList){ %>
		            	<option value ="<%=b.getBranchid() %>" <%=branch.getAccountbranch()==b.getBranchid()?"selected":"" %>><%=b.getBranchname() %></option>
		            <%} %>
	            </select>
			 </li>
	         <li>
	         	<span>超额设置：</span>
	           	<input type="text" name="accountexcessfee" id="accountexcessfee" value="<%=StringUtil.nullConvertToBigDecimal(branch.getAccountexcessfee()) %>" class ="zhandian" />
	           	<select id ="accountexcesstype" name ="accountexcesstype">
					<option value ="0">==请选择==</option> 
					<option value ="1" <%=branch.getAccountexcesstype()==1?"selected":"" %>>元</option> 
					<option value ="2" <%=branch.getAccountexcesstype()==2?"selected":"" %>>%</option>
				</select>
	         </li>
	         <li>
	         	<span>信誉额度：</span>
				<input type="text" name="credit" id="credit" value="<%=StringUtil.nullConvertToBigDecimal(branch.getCredit()) %>" class ="zhandian" />元
	         </li>
				<li><span>24小时时效：</span>
	           <input type="text" name="prescription24" id="prescription24" value="<%=branch.getPrescription24() %>" class ="zhandian" />小时
	         </li>
	         <li><span>48小时时效：</span>
	           <input type="text" name="prescription48" id="prescription48" value="<%=branch.getPrescription48() %>" class ="zhandian" />小时
	         </li>
	         <li><span>退货出站超时时效：</span>
			 	<select id ="backtime" name ="backtime" class ="zhandian"  style="width:150px;">
			 	 <option value ="0">==请选择==</option> 
	             <option value ="24" <%=branch.getBacktime()==24?"selected":"" %>>24</option> 
	             <option value ="72" <%=branch.getBacktime()==72?"selected":"" %>>72</option>
	            </select>小时
			 </li>
	        <li id="pda_title" ><span>组织的货物操作权限</span>（PDA）：</li>
			<ul id="pda"  class="checkedbox1"><%
						for (Menu menu : menuPDAList) {
					%><li><label> <input type="checkbox" id="cb_<%=menu.getMenuno()%>"  name="functionids"
							value="<%=menu.getMenuno()%>" /> <%=menu.getName()%>
					</label></li><%
						}
			%></ul>
			<li><span>站点类型：</span><select id ="contractflag" name ="contractflag"  onchange="changemskshow(this.value)">
	             <%-- <option value ="0" <%=branch.getContractflag().equals("0")?"selected":"" %>>直营</option> 
	             <option value ="1" <%=branch.getContractflag().equals("1")?"selected":"" %> >加盟</option>
	             <option value ="2" <%=branch.getContractflag().equals("2")?"selected":"" %>>二级站</option> --%>
	             <option value ="<%=BranchTypeEnum.ZhiYing.getValue()%>" <%=branch.getContractflag().equals("0")?"selected":"" %>><%=BranchTypeEnum.ZhiYing.getText()%></option> 
	             <option value ="<%=BranchTypeEnum.ErJiZhan.getValue()%>" <%=branch.getContractflag().equals("1")?"selected":"" %>><%=BranchTypeEnum.ErJiZhan.getText()%></option>
	             <option value ="<%=BranchTypeEnum.SanJiZhan.getValue()%>" <%=branch.getContractflag().equals("2")?"selected":"" %>><%=BranchTypeEnum.SanJiZhan.getText()%></option>
	             <option value ="<%=BranchTypeEnum.JiaMeng.getValue()%>" <%=branch.getContractflag().equals("3")?"selected":"" %>><%=BranchTypeEnum.JiaMeng.getText()%></option>
	             <option value ="<%=BranchTypeEnum.JiaMengErJi.getValue()%>" <%=branch.getContractflag().equals("4")?"selected":"" %>><%=BranchTypeEnum.JiaMengErJi.getText()%></option>
	             <option value ="<%=BranchTypeEnum.JiaMengSanJi.getValue()%>" <%=branch.getContractflag().equals("5")?"selected":"" %>><%=BranchTypeEnum.JiaMengSanJi.getText()%></option>
	           </select></li>
	           	<li><span>派费规则：</span>
				<select id ="pfruleid" name ="pfruleid" >
				<option value="0">请选择</option>
				<%for(PaiFeiRule pf:pfrulelist){ %>
				<option value="<%=pf.getId()%>" <%if(branch.getPfruleid()==pf.getId()){%>selected="selected"<%} %>><%=pf.getName() %></option>
					<%} %>
		           </select>
		        </li>
	           <%if(bindmsksid.getValue().equals("1")){ %>
	        	 <li><span>绑定迈思可站点：</span>
	        	 <select name="bindmsksid" id="bindmsksid">
	        	 	<option value="0">请选择 </option>
	        	 	<%for(Stores sotres:mskbranchlist){%>
	        	 		<option value="<%=sotres.getId()%>" <%if(branch.getBindmsksid()==sotres.getId()){%>selected<%} %>><%=sotres.getSname() %> </option>
	        	 	<%} %>
	        	 	
	        	 </select>
	        	 </li>
	        <%} %>
	        <li><span>银行卡号：</span><input type="text" name="bankcard" id="bankcard" maxlength="50" value="<%=branch.getBankcard() %>"/></li>
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
	             <option value ="<%=BranchEnum.BuQiYong.getValue()%>"><%=BranchEnum.BuQiYong.getText() %></option>
	             <option value ="<%=BranchEnum.TiaoMaDaYin.getValue()%>"><%=BranchEnum.TiaoMaDaYin.getText() %></option>
	             <option value ="<%=BranchEnum.YuYinTiXing.getValue()%>"><%=BranchEnum.YuYinTiXing.getText() %></option>
	             <option value ="<%=BranchEnum.YuYinAndTiaoMa.getValue()%>"><%=BranchEnum.YuYinAndTiaoMa.getText()%></option>
	           </select>*</li>
	         <li id="wav" style="display: none"><span>上传声音文件：</span>
		         <iframe id="update" name="update" src="branch/update?fromAction=branch_save_Form&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
		         <%if(branch.getBranchwavfile()!=null&&branch.getBranchwavfile().length()>4){ %>
		         <a href="#" onclick="	
			         	var audioElement = document.createElement('audio');
			        	audioElement.setAttribute('src', '<%=request.getContextPath()+ServiceUtil.wavPath+branch.getBranchwavfile() %>');
			     		audioElement.load();
			     		audioElement.play();
		     		">点击测试</a>
		         <%} %>
	         </li>
	         <input type="hidden" id ="wavh" name="wavh" value ="<%=branch.getBranchwavfile() %>"  />
	         <EMBED NAME='music1' SRC='<%=request.getContextPath()+ServiceUtil.wavPath+branch.getBranchwavfile() %>' LOOP=false AUTOSTART=false MASTERSOUND HIDDEN=true WIDTH=0 HEIGHT=0></EMBED>
			 <li><span>邮件：</span><input type="text" name="branchmatter" id="branchmatter" value ="<%=branch.getBranchmatter()%>" maxlength="50" /></li>
			<!--  <li><span>导出信息设置：</span><input type="hidden" name="" class ="kefu" /></li>
	         <li><span>查询统计内容设置：</span><input type="hidden" name="" class ="yunying" /></li> -->
         </ul>
	         
	         <input type="hidden" name="branchid" value ="<%=branch.getBranchid() %>"  />
	         
		</div>
		<div align="center">
		<input type="submit" value="保存" class="button" id="sub" /></div>
	</form>
	</div>
</div>
