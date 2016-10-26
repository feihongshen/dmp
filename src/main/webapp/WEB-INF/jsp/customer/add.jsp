<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.PaiFeiRule"%>
<%
List<PaiFeiRule> pfrulelist = (List<PaiFeiRule>) request.getAttribute("pfrulelist");
%>


<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商</h1>
		<form id="customer_cre_Form" name="customer_cre_Form" onSubmit="if(check_customer()){submitAddCustomer(this);}return false;" action="<%=request.getContextPath()%>/customer/createFile;jsessionid=<%=session.getId()%>" method="post"  >
		<div id="box_form">
				<ul style="width:450px;height: 400px;overflow-y: scroll;">
					<li><span>客户名称：</span><input type ="text" id ="customername" name ="customername" maxlength="30" class="input_text1"/>*</li>
					<li><span>公司名称：</span><input type ="text" id ="companyname" name ="companyname" maxlength="30" class="input_text1"/>*</li>
					<li><span>客户编码：</span><input type ="text" id ="customercode" name ="customercode" maxlength="30" class="input_text1"/>*</li>
					<!-- <li><span>对接枚举编号：</span><input type ="text" id ="b2cEnum" name ="b2cEnum" maxlength="30"/></li> -->
					<li><span>地址：</span><input type ="text" id ="customeraddress" name ="customeraddress"  maxlength="100" class="input_text1"/></li>
					<li><span>联系人：</span><input type ="text" id ="customercontactman" name ="customercontactman"  maxlength="30" class="input_text1"/></li>
					<li><span>电话：</span><input type ="text" id ="customerphone" name ="customerphone"  maxlength="30" class="input_text1"/></li>
					<li><span>结算类型：</span>
						<select id ="paytype" name ="paytype" class="select1">
							<option value="0">请选择</option>
							<option value="1">按发货时间结算</option>
							<option value="2">按配送结果结算</option>
						</select>*
					</li>					
					<li><span>派费规则：</span>
					<select id ="pfruleid" name ="pfruleid" >
					<option value="0">请选择</option>
					<%for(PaiFeiRule pf:pfrulelist){ %>
					<option value="<%=pf.getId()%>"><%=pf.getName() %></option>
						<%} %>
			           </select>
			        </li>
			       
					<li><span>一票多件用运单号：</span>
						<select id ="isypdjusetranscwb" name ="isypdjusetranscwb" class="select1" onchange="transcwbswitch()">
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</li>
					<li><span>是否扫描运单号：</span>
						<select id ="isUsetranscwb" name ="isUsetranscwb" class="select1">
							<option value="0">是</option>
							<option value="2">否</option>
						</select>
					</li>
					<li><span>退货订单申请：</span>
						<select id ="needchecked" name ="needchecked" class="select1">
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</li>
					<li><span>是否进行返单操作：</span>
						<select id ="isFeedbackcwb" name ="isFeedbackcwb" class="select1">
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</li>
					<li><span>是否生成订/运单号:</span>
						<select id ="isAutoProductcwb" name ="isAutoProductcwb" class="select1" onchange='if($(this).val()==1){$("#auto").show()}else{$("#auto").hide();$("#autoProductcwbpre").val("");}'>
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</li>
					
					<li><span>揽退单是否自动到货</span>
						<select id ="autoArrivalBranchFlag" name ="autoArrivalBranchFlag" class="select1" >
							<option  value="0" selected>否</option>
							<option  value="1" >是</option>
						</select>
					</li>
					
					<div id="auto" style="display: none;">
						<li style="color:#900">
							模板要求：
							<br>1.替换字符：（订单号-[cwb]，发货件数-[sendcarnum]，第几件-[index]，件数超过10是否显示为010-[0]）
							<br>2.示例：JD[cwb]_[sendcarnum]_[0][index]
							<br>3.导入订单:8888,件数11,所得运单号：JD8888_2_01,JD8888_2_011
						</li>
						<li></li>
						<li></li>
						<li>
							<span>订/运单号前缀：</span>
							<input type ="text" id ="autoProductcwbpre" name ="autoProductcwbpre" maxlength="50" class="input_text1"/>
						</li>
					</div>
					<li>
						<span>短信渠道：</span>
						<select id ="smschannel" name ="smschannel" class="select1" >
							<option value="0">默认</option>
							<option value="1">亿美</option>
						</select>
					</li>
					<li>
						<span>单号是否区分大小写</span>
						<select id ="isqufendaxiaoxie" name ="isqufendaxiaoxie" class="select1" >
							<option value="0">不区分</option>
							<option value="1">区分</option>
						</select>
					</li>
					
					<li>
						<span>上传声音文件：</span>
						<iframe id="update" name="update" src="customer/update?fromAction=customer_cre_Form&wavFilePath=&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
					</li>
					 <li><span>是否启用集单模式：</span>
						<select id="ifjidan" name="ifjidan" class="select1" disabled="disabled" onchange="changejd()">
							<option value="0">否</option>
							<option value="1">是</option>							
						</select>*
					</li>
					<li style="display: none"><input type="radio" name="mpsswitch" value="1" id="jdType1" checked="checked"/>库房集单&nbsp;&nbsp;&nbsp;<input type="radio" name="mpsswitch" value="2" id="jdType2"/>站点集单</li>  
				</ul>
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
