<%@page import="cn.explink.domain.ExcelColumnSet"%>
<%@page import="cn.explink.domain.Customer"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Customer> customerList = (List<Customer>)request.getAttribute("customers");
%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建设置</h1>
		<form id="excelcolumn_cre_Form" name="excelcolumn_cre_Form" method="POST" action="<%=request.getContextPath()%>/excelcolumn/create" onSubmit="if(check_excelcolumn()){submitCreateForm(this);}return false;" >
		<div id="box_form">
				<p  class="gysselect"><span>供货商选择：</span>
					<select id="customerid" name="customerid">
						<option value="-1">请选择</option>
						<% for(Customer w1 : customerList){ %>
					<option value=<%=w1.getCustomerid()%>><%=w1.getCustomername() %></option>
					<%} %>
					</select>*</p>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td>	
						
						<p><span>条码号/订单号：</span>
						<select id="cwbindex" name="cwbindex">
							 <option value="0" >未设置</option>
						     </select>*</p>
						     <p><span>承运商编号：</span>
						<select id="commonnumberindex" name="commonnumberindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				        <p><span>运单号：</span> 
						<select id="transcwbindex" name="transcwbindex">
							<option value="0" selected>未设置</option>
				        </select></p>
						<p><span>收件人姓名：</span>
						<select id="consigneenameindex" name="consigneenameindex">
							<option value="0" selected>未设置</option>
				        </select></p>
						<p><span>收件人地址：</span>
						<select id="consigneeaddressindex" name="consigneeaddressindex">
							<option value="0" selected>未设置</option>
				        </select><br/>
				        <p><span>邮编：</span>
						<select id="consigneepostcodeindex" name="consigneepostcodeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				         <p><span>电话：</span>
						<select id="consigneephoneindex" name="consigneephoneindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				        <p><span>手机：</span>
						<select id="consigneemobileindex" name="consigneemobileindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			        	<p><span>获取手机：</span>
				         	<input type="radio" name="getmobileflag" value="0" checked="checked"/>否　<input type="radio"  name="getmobileflag" value="1" >是
						</p>
				       <p><span>发出商品：</span>
						<select id="sendcargonameindex" name="sendcargonameindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				        <p><span>取回商品：</span>
						<select id="backcargonameindex" name="backcargonameindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				       <p><span>实际重量kg：</span> 
						<select id="cargorealweightindex" name="cargorealweightindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				        
			        
			        </td>
			        <td>
			        <p><span>应收金额：</span>
						<select id=receivablefeeindex name="receivablefeeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				        <p><span>应退金额：</span>
						<select id="paybackfeeindex" name="paybackfeeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			       		<p><span>备注信息：</span>
						<select id="cwbremarkindex" name="cwbremarkindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>指定小件员：</span>
						<select id="exceldeliverindex" name="exceldeliverindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>指定派送分站：</span>
						<select id="excelbranchindex" name="excelbranchindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>配送单号：</span> 
						<select id="shipcwbindex" name="shipcwbindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>收件人编号：</span>
						<select id="consigneenoindex" name="consigneenoindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>货物金额：</span>
						<select id="cargoamountindex" name="cargoamountindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>客户要求：</span> 
						<select id="customercommandindex" name="customercommandindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				         
			        	<p><span>货物类型：</span> 
						<select id="cargotypeindex" name="cargotypeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>货物尺寸：</span>
						<select id="cargosizeindex" name="cargosizeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>取回商品金额：</span>
						<select id="backcargoamountindex" name="backcargoamountindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			        </td>
					<td>
					<p><span>目的地：</span>
						<select id="destinationindex" name="destinationindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>运输方式：</span>
						<select id="transwayindex" name="transwayindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>发货数量：</span>
						<select id="sendcargonumindex" name="sendcargonumindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>取货数量：</span>
						<select id="backcargonumindex" name="backcargonumindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>省：</span> 
						<select id="cwbprovinceindex" name="cwbprovinceindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>城市：</span>
						<select id="cwbcityindex" name="cwbcityindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>区县：</span>
						<select id="cwbcountyindex" name="cwbcountyindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>发货仓库：</span>
						<select id="warehousenameindex" name="warehousenameindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>订单类型：</span>
						<select id="cwbordertypeindex" name="cwbordertypeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			          	<p><span>派送类型：</span>
						<select id="cwbdelivertypeindex" name="cwbdelivertypeindex">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	
			          	<!-- <p><span>发货时间：</span>
						<select id="emaildateindex" name="emaildateindex">
							<option value="0" selected>未设置</option>
				        </select></p> -->
			         	<p style ="display:none;"><span>修改时间：</span>
						<select id="updatetime" name="updatetime">
							<option value="0" selected>未设置</option>
				        </select></p>
			         	<p><span>结算区域：</span>
						<select id="accountareaindex" name="accountareaindex">
							<option value="0" selected>未设置</option>
				        </select></p>
				    </td>
				</tr>
			   </table>
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
