<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Customer customer = (Customer)request.getAttribute("customer");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改供货商</h1>
		<form id="customer_save_Form" name="customer_save_Form" onSubmit="if(check_customer()){submitEditCustomer(this,${customer.customerid });}return false;"  action="<%=request.getContextPath()%>/customer/saveFile/${customer.customerid }" method="post"  >
		<div id="box_form">
				<ul style="width:450px;height: 400px;overflow-y: scroll;">
					<li><span>客户名称：</span><input type ="text" id ="customername" name ="customername" value ="${customer.customername}"  maxlength="30"/>*</li>
					<li><span>公司名称：</span><input type ="text" id ="companyname" name ="companyname" maxlength="30" value="${customer.companyname}"/>*</li>
					<li><span>客户编码：</span><input type ="text" id ="customercode" name ="customercode" value ="${customer.customercode}"  maxlength="30"/>*</li>
					<%-- <li><span>对接枚举编号：</span><input type ="text" id ="b2cEnum" name ="b2cEnum" value ="${customer.b2cEnum}"  maxlength="30"/></li> --%>
					<li><span>地址：</span><input type ="text" id ="customeraddress" name ="customeraddress" value ="${customer.customeraddress}"  maxlength="100"/></li>
					<li><span>联系人：</span><input type ="text" id ="customercontactman" name ="customercontactman"  value ="${customer.customercontactman}"  maxlength="30"/></li>
					<li><span>电话：</span><input type ="text" id ="customerphone" name ="customerphone" value ="${customer.customerphone}" maxlength="30"/> </li>
					<li><span>结算类型：</span>
						<select id ="paytype" name ="paytype">
							<option value="0">请选择</option>
							<option value="1" <%if(customer.getPaytype()==1){ %>selected<%} %>>按发货时间结算</option>
							<option value="2" <%if(customer.getPaytype()==2){ %>selected<%} %>>按配送结果结算</option>
						</select>*
					</li>
					<li><span>一票多件用运单号：</span>
						<select id ="isypdjusetranscwb" name ="isypdjusetranscwb">
							<option value="0" <%if(customer.getIsypdjusetranscwb()==0){ %>selected<%} %>>否</option>
							<option value="1" <%if(customer.getIsypdjusetranscwb()==1){ %>selected<%} %>>是</option>
						</select>
					</li>
					<li><span>是否扫描运单号：</span>
						<select id ="isUsetranscwb" name ="isUsetranscwb">
							<option value="0" <%if(customer.getIsUsetranscwb()==0){ %>selected<%} %>>是</option>
							<option value="2" <%if(customer.getIsUsetranscwb()==2){ %>selected<%} %>>否</option>
						</select>
					</li>
					<li><span>退货订单申请：</span>
						<select id ="needchecked" name ="needchecked">
							<option value="0" <%if(customer.getNeedchecked()==0){ %>selected<%} %>>否</option>
							<option value="1" <%if(customer.getNeedchecked()==1){ %>selected<%} %>>是</option>
						</select>
					</li>
					<li><span>是否进行返单操作：</span>
						<select id ="isFeedbackcwb" name ="isFeedbackcwb">
							<option value="0" <%if(customer.getIsFeedbackcwb()==0){ %>selected<%} %>>否</option>
							<option value="1" <%if(customer.getIsFeedbackcwb()==1){ %>selected<%} %>>是</option>
						</select>
					</li>
					<li><span>是否生成订/运单号</span>
						<select id ="isAutoProductcwb" name ="isAutoProductcwb" onchange='if($(this).val()==1){$("#auto").show()}else{$("#auto").hide();$("#autoProductcwbpre").val("");}'>
							<option  value="0"<%if(customer.getIsAutoProductcwb()==0){ %>selected<%} %>>否</option>
							<option  value="1"<%if(customer.getIsAutoProductcwb()==1){ %>selected<%} %>>是</option>
						</select>
					</li>
					<div id="auto" <%if(customer.getIsAutoProductcwb()==0){ %>style="display: none;"<%} %>>
						<li style="color:#900">
							<span>模板要求：</span>
							1.替换字符：（订单号-[cwb]，发货件数-[sendcarnum]，第几件-[index]，件数超过10是否显示为010-[0]）
							&nbsp;2.示例：JD[cwb]_[sendcarnum]_[0][index]
							&nbsp;3.导入订单:8888,件数11,所得运单号：JD8888_2_01,JD8888_2_011
						</li>
						<li>&nbsp;</li>
						<li >
							<span>订/运单号前缀：</span>
							<input type ="text" id ="autoProductcwbpre" name ="autoProductcwbpre"  maxlength="50" value="<%=customer.getAutoProductcwbpre()%>"/>
						</li>
					</div>
					<li>
						<span>短信渠道：</span>
						<select id ="smschannel" name ="smschannel" >
							<option value="0"<%if(customer.getSmschannel()==0){ %>selected<%} %>>默认</option>
							<option value="1"<%if(customer.getSmschannel()==1){ %>selected<%} %>>亿美</option>
						</select>
					</li>
					<li>
						<span>单号是否区分大小写</span>
						<select id ="isqufendaxiaoxie" name ="isqufendaxiaoxie" >
							<option value="0" <%if(customer.getIsqufendaxiaoxie()==0){ %>selected<%} %>>不区分</option>
							<option value="1" <%if(customer.getIsqufendaxiaoxie()==1){ %>selected<%} %>>区分</option>
						</select>
					</li>
					<li>
						<span>上传声音文件：</span>
						<iframe id="update" name="update" src="customer/update?fromAction=customer_save_Form&wavFilePath=<%=customer.getWavFilePath()==null?"":customer.getWavFilePath()%>&a=<%=Math.random() %>" width="240px" height="25px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
					</li>  
				</ul>
		</div>
		 <div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
	</div>
</div>
<div id="box_yy"></div>
