<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>揽收运单补录页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/extracedOrderInput.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<style type="text/css">
.select1 { 
	border:1px solid #09C;
	background:url(../images/repeatx.png) repeat-x 0 -175px; 
	width:180px; 
	height:20px; 
	line-height:20px; 
	color:#000;
	}
</style>
</head>
<body>
	<div id="flag" style="display:none;">${flag }</div>
	<div id="orderNoValue" style="display:none;"> ${orderNo}</div>
	<form id="PageFromW" action="<%=request.getContextPath()%>/embracedOrderInputController/extraInputSave" method="post" onsubmit="return toVaildForm();" style="margin: 10px;">
		<fieldset ID="head" style="border: inset; border-width: 3px;">
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="10%">
					<col width="30">
					<col width="60%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">快递单号<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="orderNo" id="orderNo_id" class="input_text2" onblur="getCwbOrderEmbraced()" value="${orderNo}"/></td>
					<td class="tdrigth" style="padding-right: 60px;">未补录运单：<span style="color: red; text-decoration: underline; font-size: 30px;"><a style="font-size:30px;color:red;" onmouseover="this.style.cssText='font-size:30px;color:blue;'" onmouseout="this.style.cssText='font-size:30px;color:red;'" href="javascript:nonExtraInput(1);">${notExtraInputNumber}单</a></span></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="sender" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">寄件人信息</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="10%">
					<col width="28%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">客户编码:</td>
					<td colspan="3" class="tdleft"><input type="text" name="sender_No" id="sender_No_id" class="input_text2" style="background:#EBEBE4;" readonly="true""/></td>
					<td class="tdcenter">公司:</td>
					<td colspan="3" class="tdleft">
						<select name="sender_companyName" id="sender_companyName_id" class="select2" readonly="readonly" onchange="companyChange(this)">
							<c:forEach items="${customers}" var="list">
								<option value="${list.companyname}" code="${list.customercode}" id="${list.customerid}">${list.companyname}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="sender_customerid" id="sender_customerid_id"/></td>
					<td class="tdcenter">寄件人<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="sender_name" id="sender_name_id" class="input_text2" /></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">省<font color="red">*</font>:</td>
					<td class="tdleft">
						<c:if test="${BranchprovinceId == 0}">
							<select name="sender_provinceid" id="sender_provinceid_id" class="select1" readonly="readonly" onchange="changeProvince(this)">
								<c:forEach items="${provincelist}" var="list">
									<option value="${list.id}" code="${list.code}">${list.name}</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${BranchprovinceId != 0}">
							<select name="sender_provinceid" id="sender_provinceid_id" class="select1" readonly="readonly"  style="background:#EBEBE4;" disabled="disabled" onchange="changeProvince(this)">
								<c:forEach items="${provincelist}" var="list">
									<c:if test="${BranchprovinceId == list.id}">
										<option value="${list.id}" code="${list.code}" selected="selected">${list.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</c:if>
					</td>
					<td style="display:none;"><input type="text" name="sender_provinceName" id="sender_provinceName_id"/></td>
					<td class="tdcenter">市<font color="red">*</font>:</td>
					<td class="tdleft">
						<c:if test="${BranchcityId == 0}">
							<select name="sender_cityid" id="sender_cityid_id" class="select1" readonly="readonly" onchange="changeCity(this)">
								<c:forEach items="${citylist}" var="list">
									<option value="${list.id}" code="${list.code}">${list.name}</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${BranchcityId != 0}">
							<select name="sender_cityid" id="sender_cityid_id" class="select1" readonly="readonly"  style="background:#EBEBE4;" readonly="true">
								<c:forEach items="${citylist}" var="list">
									<c:if test="${BranchcityId == list.id}">
										<option value="${list.id}" code="${list.code}" selected="selected">${list.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</c:if>
					</td>
					<td style="display:none;"><input type="text" name="sender_cityName" id="sender_cityName_id"/></td>
					<td class="tdcenter">区/县<font color="red">*</font>:</td>
					<td class="tdleft">
						<select name="sender_countyid" id="sender_countyid_id" class="select1" readonly="readonly" onclick="selectClick(this)" onchange="changeCounty(this)">
							<option value="defaultValue"></option>
							<c:forEach items="${countylist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="sender_countyName" id="sender_countyName_id"/></td>
					<td class="tdcenter">街道:</td>
					<td class="tdleft">
						<select name="sender_townid" id="sender_townid_id" class="select1">
							<option value="defaultValue"></option>
							<c:forEach items="${townlist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="sender_townName" id="sender_townName_id"/></td>
					<td class="tdcenter">地址<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="sender_adress" id="sender_adress_id" class="input_text2" /></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">手机：</td>
					<td colspan="3" class="tdleft"><input type="text" name="sender_cellphone" id="sender_cellphone_id" class="input_text2" /></td>
					<td class="tdcenter">固话：</td>
					<td colspan="3" class="tdleft"><input type="text" name="sender_telephone" id="sender_telephone_id" class="input_text2" /></td>
					<td class="tdcenter">寄件人证件号:</td>
					<td class="tdleft"><input type="text" name="sender_certificateNo" id="sender_certificateNo_id" class="input_text2" /></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="consignee" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">收件人信息</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="10%">
					<col width="28%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">客户编码:</td>
					<td colspan="3" class="tdleft"><input type="text" name="consignee_No" id="consignee_No_id" class="input_text2" style="background:#EBEBE4;" readonly="true"/></td>
					<td class="tdcenter">公司:</td>
					<td colspan="3" class="tdleft">
						<select name="consignee_companyName" id="consignee_companyName_id" class="select2" readonly="readonly" onchange="companyChange(this)">
							<c:forEach items="${customers}" var="list">
								<option value="${list.companyname}" code="${list.customercode}" id="${list.customerid}">${list.companyname}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="consignee_customerid" id="consignee_customerid_id"/></td>
					<td class="tdcenter">收件人<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="consignee_name" id="consignee_name_id" class="input_text2" /></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">省<font color="red">*</font>:</td>
					<td class="tdleft">
						<c:if test="${BranchprovinceId == 0}">
							<select name="consignee_provinceid" id="consignee_provinceid_id" class="select1" readonly="readonly" onchange="changeProvince(this)">
								<c:forEach items="${provincelist}" var="list">
									<option value="${list.id}" code="${list.code}">${list.name}</option>
								</c:forEach>
							</select>
						</c:if>
						<c:if test="${BranchprovinceId != 0}">
							<select name="consignee_provinceid" id="consignee_provinceid_id" class="select1" readonly="readonly" onchange="changeProvince(this)">
								<c:forEach items="${provincelist}" var="list">
									<c:if test="${BranchprovinceId == list.id}">
										<option value="${list.id}" code="${list.code}" selected="selected">${list.name}</option>
									</c:if>
									<c:if test="${BranchprovinceId != list.id}">
										<option value="${list.id}" code="${list.code}">${list.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</c:if>
					</td>
					<td style="display:none;"><input type="text" name="consignee_provinceName" id="consignee_provinceName_id"/></td>
					<td class="tdcenter">市<font color="red">*</font>:</td>
					<td class="tdleft">
						<select name="consignee_cityid" id="consignee_cityid_id" class="select1" readonly="readonly" onchange="changeCity(this)">
							<c:forEach items="${citylist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="consignee_cityName" id="consignee_cityName_id"/></td>
					<td class="tdcenter">区/县<font color="red">*</font>:</td>
					<td class="tdleft">
						<select name="consignee_countyid" id="consignee_countyid_id" class="select1" readonly="readonly" onclick="selectClick(this)" onchange="changeCounty(this)">
							<option value="defaultValue"></option>
							<c:forEach items="${countylist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="consignee_countyName" id="consignee_countyName_id"/></td>
					<td class="tdcenter">街道:</td>
					<td class="tdleft">
						<select name="consignee_townid" id="consignee_townid_id" class="select1">
							<option value="defaultValue"></option>
							<c:forEach items="${townlist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="consignee_townName" id="consignee_townName_id"/></td>
					<td class="tdcenter">地址<font color="red">*</font>：</td>
					<td class="tdleft"><input type="text" name="consignee_adress" id="consignee_adress_id" class="input_text2" /></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">手机：</td>
					<td colspan="3" class="tdleft"><input type="text" name="consignee_cellphone" id="consignee_cellphone_id" class="input_text2" /></td>
					<td class="tdcenter">固话：</td>
					<td colspan="3" class="tdleft"><input type="text" name="consignee_telephone" id="consignee_telephone_id" class="input_text2" /></td>
					<td class="tdcenter">收件人证件号:</td>
					<td class="tdleft"><input type="text" name="consignee_certificateNo" id="consignee_certificateNo_id" class="input_text2" /></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="goodsInfo" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">托物资料</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="10%">
					<col width="7%">
					<col width="7%">
					<col width="7%">
					<col width="7%">
					<col width="6%">
					<col width="3%">
					<col width="10%">
					<col width="10%">
					<col width="7%">
					<col width="26%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">托物内容/名称<font color="red">*</font>:</td>
					<td colspan="3" class="tdleft"><input type="text" name="goods_name" id="goods_name_id" class="input_text2" /></td>
					<td class="tdcenter">数量<font color="red">*</font>:</td>
					<td colspan="4" class="tdleft"><input type="text" name="goods_number" id="goods_number_id" class="input_text2" /></td>
					<td class="tdcenter">重量:</td>
					<td class="tdleft"><input type="text" name="goods_weight" id="goods_weight_id" class="input_text2" onchange="toFix(this,2)"/></td>
				</tr>
				<tr  style="line-height-bottom: 0px;">
					<td class="tdcenter">长:</td>
					<td class="tdleft"><input type="text" name="goods_longth" id="goods_longth_id" class="input_text1" onblur="calculateKGS(this)"/></td>
					<td class="tdcenter">CM × 宽:</td>
					<td class="tdleft"><input type="text" name="goods_width" id="goods_width_id" class="input_text1" onblur="calculateKGS(this)"/></td>
					<td class="tdcenter">CM × 高:</td>
					<td class="tdleft"><input type="text" name="goods_height" id="goods_height_id" class="input_text1" onblur="calculateKGS(this)"/></td>
					<td class="tdcenter">CM</td>
					<td rowspan="3"  class="tdcenter" style="vertical-align:middle; text-align:left;">=<input type="text" name="goods_kgs" id="goods_kgs_id" class="input_text1" style="width:80%;margin-left:5px;background:#EBEBE4;"  readonly="readonly"/></td>
					<td rowspan="3"  class="tdleft" style="vertical-align:middle;">KGS</td>
					<td rowspan="3"  class="tdcenter" style="vertical-align:middle; text-align:center;">其他:</td>
					<td rowspan="3"  class="tdleft" style="vertical-align:middle;"><input type="text" name="goods_other" id="goods_other_id" class="input_text2"/></td>
				</tr>
				<tr  style="line-height:1px;">
					<td colspan="7" class="tdcenter"><hr width="100%"/></td>
				</tr>
				<tr style="line-height-top:5px;">
					<td colspan="7" class="tdcenter">6000</td>
				</tr>
				
			</table>
		</fieldset>
			
		<fieldset ID="businessType" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">业务类型</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="10%">
					<col width="7%">
					<col width="7%">
					<col width="6%">
					<col width="10%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="7%">
					<col width="28%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">是否代收货款:</td>
					<td class="tdleft"><input type="radio" name="collection" id="collection_id" onchange="radioChange(this)"  value="1"/>是</td>
					<td class="tdcenter"><input type="radio" name="collection" id="collection_id" onchange="radioChange(this)" value="0" checked/>否</td>
					<td ></td>
					<td class="tdcenter">代收货款金额:</td>
					<td colspan="3" class="tdleft"><input type="text" name="collection_amount" id="collection_amount_id" class="input_text2" style="background:#EBEBE4;" readonly="true" onchange="toFix(this,2)"/></td>
					<td class="tdcenter">包装费用:</td>
					<td class="tdleft"><input type="text" name="packing_amount" id="packing_amount_id" class="input_text2" onblur="calculateFreight(this)" onchange="toFix(this,2)"/></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">是否保价:</td>
					<td class="tdleft"><input type="radio" name="insured" id="insured_id"  onchange="radioChange(this)" value="1" />是</td>
					<td class="tdcenter"><input type="radio" name="insured" id="insured_id"  onchange="radioChange(this)" value="0" checked/>否</td>
					<td ></td>
					<td class="tdcenter">保价声明价值:</td>
					<td colspan="3" class="tdleft"><input type="text" name="insured_amount" id="insured_amount_id" class="input_text2" style="background:#EBEBE4;" readonly="true" onchange="toFix(this,2)"/></td>
					<td class="tdcenter">保价费用:</td>
					<td class="tdleft"><input type="text" name="insured_cost" id="insured_cost_id" class="input_text2" onblur="calculateFreight(this)" onchange="toFix(this,2)"/></td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset ID="feeDate" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">费用</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="9%">
					<col width="27%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">件数<font color="red">*</font>:</td>
					<td colspan="3" class="tdleft"><input type="text" name="number" id="number_id" class="input_text2" /></td>
					<td class="tdcenter">计费重量<font color="red">*</font>：</td>
					<td colspan="3" class="tdleft"><input type="text" name="charge_weight" id="charge_weight_id" class="input_text2" onchange="toFix(this,2)"/></td>
					<td class="tdcenter">实际重量/重量<font color="red">*</font>：</td>
					<td class="tdleft"><input type="text" name="actual_weight" id="actual_weight_id" class="input_text2" onchange="toFix(this,2)"/></td>
				</tr>
				<tr style="line-height: 35px;">
					<td class="tdcenter">运费<font color="red">*</font>:</td>
					<td colspan="3" class="tdleft"><input type="text" name="freight" id="freight_id" class="input_text2" onblur="calculateFreight(this)" onchange="toFix(this,2)"/></td>
					<td class="tdcenter">费用合计：</td>
					<td colspan="3" class="tdleft"><input type="text" name="freight_total" id="freight_total_id" class="input_text2" readonly="readonly" style="background:#EBEBE4;"/></td>
					<td></td>
					<td  colspan="3"></td>
				</tr>			
			</table>
		</fieldset>	
		
		<fieldset ID="areaCode" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">地区代码</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="9%">
					<col width="27%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">始发点<font color="red">*</font>:</td>
					<td colspan="3" class="tdleft"><input type="text" name="origin_adress" id="origin_adress_id" class="input_text2" /></td>
					<td class="tdcenter">目的地：</td>
					<td colspan="3" class="tdleft"><input type="text" name="destination" id="destination_id" class="input_text2" /></td>
					<td class="tdcenter"></td>
					<td class="tdleft"></td>
				</tr>				
			</table>
		</fieldset>		
		
		<fieldset ID="payMethod" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">付款方式</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="8%">
					<col width="8%">
					<col width="9%">
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="9%">
					<col width="27%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">付款方式:</td>
					<td class="tdleft"><input type="radio" name="payment_method" id="payment_method_id"  value="1" onchange="radioChange(this)"/>现付</td>
					<td class="tdleft"><input type="radio" name="payment_method" id="payment_method_id"  value="2" onchange="radioChange(this)" />到付</td>
					<td class="tdleft"><input type="radio" name="payment_method" id="payment_method_id"  value="0" onchange="radioChange(this)"  checked/>月结</td>
					<td class="tdcenter">月结账号：</td>
					<td colspan="3" class="tdleft"><input type="text" name="monthly_account_number" id="monthly_account_number_id" class="input_text2" /></td>
					<td class="tdcenter"></td>
					<td class="tdleft"><font color="red">月结：月结账户必填</font></td>
				</tr>				
			</table>
		</fieldset>	
		
		<fieldset ID="embracerAndDelivery" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">收派员信息</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="7%">
					<col width="8%">
					<col width="8%">
					<col width="9%">
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="9%">
					<col width="27%">
				</colgroup>
				<tr style="line-height: 35px;">
					<td class="tdcenter">收件员:</td>
					<td colspan="3" class="tdleft"><input type="text" name="delivermanName" id="receiver_id" class="input_text2" readonly="readonly" style="background:#EBEBE4;"/></td>
					<td class="tdcenter">派件员:</td>
					<td colspan="3" class="tdleft"><input type="text" name="delivername" id="delivery_name_id" class="input_text2" readonly="readonly" style="background:#EBEBE4;"/></td>
					<td class="tdcenter"></td>
					<td class="tdleft"></td>
				</tr>				
			</table>
		</fieldset>		
		
		<fieldset ID="remarksInfo" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">备注：</span>
			</legend>
			<table width="99%" class="table1" style="margin: 10px;">
				<tr style="line-height: 35px;">
					<td><textarea name="remarks" id="remarks_id" style="width:100%;height:100px;"></textarea></td>					
					<input type="hidden" name="inputdatetime" id="inputdatetime_id"/>
				</tr>				
			</table>
		</fieldset>	
			
		<table width="99%" style="margin-top: 10px;">
			<tr style="line-height: 35px;">
				<td class="tdcenter"><input type="submit" value="保存" class="input_button2" /></td>
			</tr>
		</table>
	</form>
	
	<div>
		<div id="alertBox" class="easyui-dialog" title="未补录揽收运单"  style="width: 1000px; height: 450px; top:100px;left:100px; padding: 10px 10px;z-index:400px;" closed="true">
			<table width="100%" class="table1">
				<colgroup>
					<col width="10%">
					<col width="10%">
					<col width="60%">
				</colgroup>
				<tr>
					<td class="tdcenter"><button  class="input_button2" onclick="closeClick()" >返回</button></td>
					<td class="tdcenter"><button  class="input_button2" onclick="ExtraceOrder()" >补录</button></td>
					<td></td>
				</tr>
			</table>
				
			<table width="100%" id="nonExtracedTable" border="0" cellspacing="1" cellpadding="0" class="table_2" >
				<colgroup>
					<col width="15%">
					<col width="20%">
					<col width="20%">
					<col width="20%">
					<col width="25%">
				</colgroup>
				<tr class="font_1">
					<th style="display:none;">订单id</th>
					<th bgcolor="#eef6ff"></th>
					<th bgcolor="#eef6ff">运单号</th>
					<th bgcolor="#eef6ff">件数</th>
					<th bgcolor="#eef6ff">小件员</th>
					<th bgcolor="#eef6ff">揽件时间</th>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="alertpageid">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
					
					</td>
				</tr>
			</table>
		</div>
	</div>
	<form id="alertForm" action="" method="post" style="margin: 10px;display:none;">	
		<input name="cwb" id="alertForm_cwb"/>
	</form>
	<input type="hidden" id="alertURL" value="<%=request.getContextPath()%>/embracedOrderInputController/nonExtraInput" />
	<input type="hidden" id="fatherURL" value="<%=request.getContextPath()%>/embracedOrderInputController/embracedOrderExtraInputInit" /> 
	
<script type="text/javascript">	
	//焦点在form中时，阻止enter键提交form	
	$("#PageFromW").keypress(function(e) {
		debugger
	  	if (e.keyCode == 13) {		
	      	return false;
	  	}
	}); 
	//焦点在运单号输入框时，enter键带出运单号，并阻止form提交
	$("#orderNo_id").keypress(function(e) {
		debugger
		if (e.keyCode == 13) {
			getCwbOrderEmbraced();	
			return false;
		}
	}); 
	$(function(){
		if($.trim($("#flag").html()) == "true"){
			alert("保存成功");
		}else if($.trim($("#flag").html()) == "false"){
			$.messager.alert("提示", "保存失败!", "warning"); 
		}else if($.trim($("#flag").html()) == "hasSaved"){
			$.messager.alert("提示", "该运单号已经录入过，保存失败!", "warning"); 
		}
		if($.trim($("#orderNoValue").html()) != ""){
			$("#orderNo_id").val($.trim($("#orderNoValue").html()));
			$("#orderNo_id").focus();
			$("#consignee_cityid_id").blur();
		}
		$("#sender_No_id").val($("#sender_companyName_id  option:selected").attr("code"));
		$("#consignee_No_id").val($("#consignee_companyName_id  option:selected").attr("code"));
	});
	/*
	 *提交form前的检验
	 */
	function toVaildForm(){		
        //校验第一个框里的信息
        if(!checkHead()){
        	return false;
        } 
        //校验寄件人信息
        if(!checkSender()){
        	return false;
        }
        //校验收件人信息
        if(!checkConsignee()){
        	return false;
        }
        //校验托物资料信息
        if(!checkGoodsInfo()){
        	return false;
        }
        //校验业务类型
        if(!checkBusinessType()){
        	return false;
        }
     	//校验校验校验费用
        if(!checkFeeDate()){
        	return false;
        }
      	//校验地区代码
        if(!checkAreaCode()){
        	return false;
        }
      	//校验付款方式
        if(!checkPayMethod()){
        	return false;
        }
      	debugger
      	//校验备注
        if(!checkRemarksInfo()){
        	return false;
        }
        assignValue();
        return true;
        
    }
	//给与select对应的输入框赋值（因为要给后台传select对应的text）,给未填的int类型的默认值
	function assignValue(){
		$("#sender_provinceName_id").val($("#sender_provinceid_id  option:selected").text());
		$("#sender_cityName_id").val($("#sender_cityid_id  option:selected").text());
		$("#sender_countyName_id").val($("#sender_countyid_id  option:selected").text());
		$("#sender_townName_id").val($("#sender_townid_id  option:selected").text());
		$("#consignee_provinceName_id").val($("#consignee_provinceid_id  option:selected").text());
		$("#consignee_cityName_id").val($("#consignee_cityid_id  option:selected").text());
		$("#consignee_countyName_id").val($("#consignee_countyid_id  option:selected").text());
		$("#consignee_townName_id").val($("#consignee_townid_id  option:selected").text());	
		debugger;
		$("#sender_customerid_id").val($("#sender_companyName_id  option:selected").attr("id"));	
		$("#consignee_customerid_id").val($("#consignee_companyName_id  option:selected").attr("id"));	
	}
	/*
	 *校验第一个框里的数据
	 */
	  function checkHead(){
		var orderNo = $("#orderNo_id");
		
		//校验运单号是否为空
        if(!nullValidater(orderNo,"运单号")){
        	return false;
        }
        //校验运单号是否只由字母和数字组成
        if(!numberOrLetterValidater(orderNo,"运单号")){
        	return false;
        } 
		//校验运单号是否超长
        if(!checkLength(orderNo, 100, '运单号', 100)){
        	return false;
        } 
		return true;
	}
	
	/*
	 *校验寄件人的数据
	 */
	 function checkSender(){
		var sender_adress = $("#sender_adress_id");
		var sender_cellphone = $("#sender_cellphone_id");
		var sender_telephone = $("#sender_telephone_id");
		var sender_companyName = $("#sender_companyName_id");
		var sender_name = $("#sender_name_id");
		var sender_certificateNo = $("#sender_certificateNo_id");
		var sender_No = $("#sender_No_id");
		var payment_method = $("input[name='payment_method']:checked").val();//0为月结
		var countySelect = $("#sender_countyid_id");
		
		//校验寄件人区/街道是否为空				
		if(countySelect.val() == "defaultValue"){
			$.messager.alert("提示", "请输入寄件人区/街道", "warning");
			return false;
		}
		
		//校验寄件人客户编码是否超长
      	/* if($.trim(sender_No.val()) != ""){
      		if(!checkLength(sender_No, 10,"寄件人客户编码",10)){
            	return false;
            }
      	} */
       //如果为月结，校验寄件人公司是否为空
      	if(payment_method == 0){
	        if(!nullValidater(sender_companyName,"寄件人公司")){
	        	return false;
	        }
      	}
      	//校验寄件人公司是否超长
      	/* if(!checkLength(sender_companyName, 50, '寄件人公司', 50)){
        	return false;
        } */
       	//校验寄件人是否为空
        if(!nullValidater(sender_name,"寄件人")){
        	return false;
        }
      	//校验寄件人是否超长
      	if(!checkLength(sender_name, 25, '寄件人', 25)){
        	return false;
        }
      	//校验寄件人地址是否为空
        if(!nullValidater(sender_adress,"寄件人地址")){
        	return false;
        }
      	//校验寄件人地址是否超长
      	if(!checkLength(sender_adress, 100, '寄件人地址', 100)){
        	return false;
        }
      	//校验寄件人电话和手机至少一个不为空
      	var senderPhone = [sender_cellphone, sender_telephone];
      	if(!multiNullValidater(senderPhone, "寄件人手机和固话")){
        	return false;
        }
      	//校验寄件人手机号是否正确
      	if($.trim(sender_cellphone.val()) != ""){
      		if(!cellPhoneValidater(sender_cellphone, "寄件人")){
            	return false;
            }
      	}
      	//校验寄件人固话是否正确
      	if($.trim(sender_telephone.val()) != ""){
      		if(!phoneValidater(sender_telephone,"寄件人")){
            	return false;
            }
      	}
      	//校验寄件人证件号是否超长
      	if($.trim(sender_certificateNo.val()) != ""){
      		if(!checkLength(sender_certificateNo, 25,"寄件人证件号",25)){
            	return false;
            }
      	}
      	
      	return true;
	}

	/*
	 *校验收件人的数据
	 */
	function checkConsignee(){
      	var consignee_adress = $("#consignee_adress_id");
		var consignee_cellphone = $("#consignee_cellphone_id");
		var consignee_telephone = $("#consignee_telephone_id");
		var consignee_companyName = $("#consignee_companyName_id");
		var consignee_name = $("#consignee_name_id");
		var consignee_certificateNo = $("#consignee_certificateNo_id");
		var consignee_No = $("#consignee_No_id");
		
		var countySelect = $("#consignee_countyid_id");
		
		//校验寄件人区/街道是否为空				
		if(countySelect.val() == "defaultValue"){
			$.messager.alert("提示", "请输入收件人区/街道", "warning");
			return false;
		}
		//校验收件人客户编码是否超长
      	/* if($.trim(consignee_No.val()) != ""){
      		if(!checkLength(consignee_No, 10,"收件人客户编码",10)){
            	return false;
            }
      	}
		//校验收件人公司是否为空
        if(!nullValidater(consignee_companyName,"收件人公司")){
        	return false;
        } 
      	//校验收件人公司是否超长
      	if(!checkLength(consignee_companyName, 50, '收件人公司', 50)){
        	return false;
        }*/
       	//校验收件人是否为空
        if(!nullValidater(consignee_name,"收件人")){
        	return false;
        }
      	//校验收件人是否超长
      	if(!checkLength(consignee_name, 25, '收件人', 25)){
        	return false;
        }
      	//校验收件人地址是否为空
        if(!nullValidater(consignee_adress,"收件人地址")){
        	return false;
        }
      	//校验收件人地址是否超长
      	if(!checkLength(consignee_adress, 100, '收件人地址', 100)){
        	return false;
        }
      	//校验收件人电话和手机至少一个不为空
      	var consigneePhone = [consignee_cellphone, consignee_telephone];
      	if(!multiNullValidater(consigneePhone, "收件人手机和固话")){
        	return false;
        }
      	//校验收件人手机号是否正确
      	if($.trim(consignee_cellphone.val()) != ""){
      		if(!cellPhoneValidater(consignee_cellphone, "收件人")){
            	return false;
            }
      	}
      	//校验收件人固话是否正确
      	if($.trim(consignee_telephone.val()) != ""){
      		if(!phoneValidater(consignee_telephone, "收件人")){
            	return false;
            }
      	}
      //校验收件人证件号码是否超长
      	if($.trim(consignee_certificateNo.val()) != ""){
      		if(!checkLength(consignee_certificateNo, 25, "收件人证件号",25)){
            	return false;
            }
      	}
      	return true;
	}
	
	/*
	 *校验托物资料
	 */
	function checkGoodsInfo(){
		var goods_name = $("#goods_name_id");
		var goods_number = $("#goods_number_id");
		var goods_weight = $("#goods_weight_id");
		var goods_other = $("#goods_other_id");
		
		//校验件数是否为空
        if(!nullValidater(goods_name,"托物内容/名称")){
        	return false;
        }
		//校验件数是否超过长度
        if(!checkLength(goods_name, 50, '托物内容/名称', 50)){
        	return false;
        }
      	//校验运费是否为空
        if(!nullValidater(goods_number,"数量")){
        	return false;
        }
      	//校验运费是否为0或正整数
        if(!numberValidater(goods_number,'数量')){
        	return false;
        }
		//校验运费是否超过大小
        if(!checkNumber(goods_number,1000000,'运费')){
        	return false;
        }
        //校验件数是不是大于0的数
        if($.trim(goods_weight.val()) !="" && !NonNegativeValidater(goods_weight,'重量')){
        	return false;
        }
      //校验其他是否超过长度
        if($.trim(goods_other.val()) !="" && !checkLength(goods_other,50,'其他',50)){
        	return false;
        }
		return true;
	}
	/*
	 *校验业务类型
	 */
	function checkBusinessType(){
		var collection = $("input[name='collection']:checked").val();
		var collection_amount = $("#collection_amount_id");
		var packing_amount = $("#packing_amount_id");
		var insured = $("input[name='insured']:checked").val();
		var insured_amount = $("#insured_amount_id");
		var insured_cost = $("#insured_cost_id");
		
		if(collection == 1){
			//校验代收货款金额是否为空
	        if(!nullValidater(collection_amount,"代收货款金额")){
	        	return false;
	        }
	      	//校验代收货款金额是否超过大小
	        if(!checkNumber(collection_amount,1000000,'代收货款金额')){
	        	return false;
	        }
	      	//代收货款是否大于0
	      	if(collection_amount.val() <= 0){
	      		$.messager.alert("提示", "代收货款应大于0", "warning");
	      		return false;
	      	}
		}
		//校验保证费用是否为大于0的数
		if($.trim(packing_amount.val()) != "" && !NonNegativeValidater(packing_amount,'包装费用')){
			return false;
		}
		
		if(insured == 1){
			//校验价声明价值是否为空
	        if(!nullValidater(insured_amount,"保价声明价值")){
	        	return false;
	        }
	      	//校验价声明价值是否超过大小
	        if(!checkNumber(insured_amount,1000000,'保价声明价值')){
	        	return false;
	        }
		}
		//校验保价费用是否为大于0的数
		if($.trim(insured_cost.val()) != "" && !NonNegativeValidater(insured_cost,'保价费用')){
			return false;
		}
		return true;
	}
	
	/*
	 *校验费用
	 */
	function checkFeeDate(){
		var number = $("#number_id");
		var charge_weight = $("#charge_weight_id");
		var actual_weight = $("#actual_weight_id");
		var freight = $("#freight_id");
		//校验件数是否为空
        if(!nullValidater(number,"件数")){
        	return false;
        }
		//校验件数是否为0或正整数
        if(!numberValidater(number,'件数')){
        	return false;
        }
		//校验件数是否超过大小
        if(!checkNumber(number,1000000,'件数')){
        	return false;
        }
		
      	//校验计费重量是否为空
        if(!nullValidater(charge_weight,"计费重量")){
        	return false;
        }
		//校验计费重量是否为大于0的数
        if(!NonNegativeValidater(charge_weight,'计费重量')){
        	return false;
        }
		//校验计费重量是否超过大小
        if(!checkNumber(charge_weight,1000000,'计费重量')){
        	return false;
        }
		
      	//校验实际重量是否为空
        if(!nullValidater(actual_weight,"实际重量")){
        	return false;
        }
		//校验实际重量是否为大于0的数
        if(!NonNegativeValidater(actual_weight,'计费重量')){
        	return false;
        }
		//校验实际重量是否超过大小
        if(!checkNumber(actual_weight,1000000,'计费重量')){
        	return false;
        }
      	//校验运费是否为空
        if(!nullValidater(freight,"运费")){
        	return false;
        }
      	//校验运费是否为非负数
        if(!NonNegativeValidater(freight,'运费')){
        	return false;
        }
		//校验运费是否超过大小
        if(!checkNumber(freight,1000000,'运费')){
        	return false;
        }
		return true;
	}
	
	/*
	 *校验地区代码
	 */
	function checkAreaCode(){
		var origin_adress = $("#origin_adress_id");
		var destination = $("#destination_id");
		//校验始发地是否为空
		if(!nullValidater(origin_adress,"始发点")){
	        	return false;
	    }
		//校验始发地是否超过长度
		if(!checkLength(origin_adress, 10, '始发点', 10)){
        	return false;
    	}
		//校验目的地是否为空
		if( $.trim(destination.val()) && !checkLength(destination, 10, '目的地', 10)){
        	return false;
    	}
		return true;
	}
	/*
	 *付款方式
	 */
	function checkPayMethod(){
		var payment_method = $("input[name='payment_method']:checked").val();
		var monthly_account_number = $("#monthly_account_number_id");
		
		if(payment_method == 0){
			//校验月结账号是否为空
	        if(!nullValidater(monthly_account_number,"月结账号")){
	        	return false;
	        }
	      	//校验月结账号是否为0或正整数
	        if(!numberValidater(monthly_account_number,'月结账号')){
	        	return false;
	        }
		}
		return true;
	}
	/*
	 *备注
	 */
	function checkRemarksInfo(){
		var remarks = $("#remarks_id");
		
		//校验备注是否超过长度
		if(!checkLength(remarks, 250, '备注', 250)){
        	return false;
    	}
		return true;
	}
	
	/*
	 *校验长度
	 */
	function checkLength(el, maxlength, msg, showlength){
		var length = el.val().length;
		msg = msg + "最大长度不应超过" + showlength;
		if(length > maxlength){
			$.messager.alert("提示", msg, "warning");
			return false;
		}
		return true;
	}
		
	/*
	 *校验数字大小
	 */
	function checkNumber(el, maxnumber, msg){
		msg = msg + "最大不应超过" + maxnumber;
		if(el.val() > maxnumber){
			$.messager.alert("提示", msg, "warning");
			return false;
		}
		return true;
	}
	
	/*
	 * 校验输入内容为数字或字母组成的方法
	 */
	function numberOrLetterValidater(el, msg){
		var regExp = "/^[A-Za-z0-9]+$/";
		debugger;
		msg = msg + "只能由字母和数字组成";
		return formatRegValidater(el, msg, regExp);
	}
	
	/*
	 * 校验输入内容位0或正整数的方法
	 */
	function numberValidater(el, msg){
		var regExp = "/^(0|[1-9]\\d*)$/";
		msg = msg + "输入不合法";
		return formatRegValidater(el, msg, regExp);
	}
	
	/*
	 * 校验输入内容为非负数
	 */
	function NonNegativeValidater(el, msg){
		var regExp = "/^([1-9]\\d*|\\d+\\.\\d+)$/";
		msg = msg + "输入不为大于0的数";
		return formatRegValidater(el, msg, regExp);
	}
	/*
	 * 校验单个输入框不为空的方法
	 */
	 function nullValidater(el, msg){
		//var regExp = "\s";
		msg = msg + "不能为空";
		if(el.val() == ""){
			$.messager.alert("提示", msg, "warning");
			return false;
		}
		return true;
	}
	
	/*
	 * 校验多个输入框不同时为空的方法
	 */
	function multiNullValidater(arr, msg){
		var flag = false;
		msg = msg + "不能同时为空";
		for(var i = 0; i < arr.length; i++){
			if(arr[i].val() != ""){
				flag = true;
			}
		}
		if(!flag){
			$.messager.alert("提示", msg, "warning");
			return false;
		}else{
			return true;
		}
	}
	
	/*
	 * 手机号码校验
	 * eg:15001288615或+8615001288615
	 */
	function cellPhoneValidater(el, msg){
		var regExp = "/^([+][8][6])?1\\d{10}$/";
		msg = msg + "手机格式不符合要求";
		return formatRegValidater(el, msg, regExp);
	}
    
	/*
	 * 电话的校验 
	 * 3-4位区号格式 eg:011-11111111,0100-1111111
	 */
	function phoneValidater(el, msg){
		var regExp = "/^\\d{3,4}[-]\\d{7,8}$/";
		msg = msg + "电话格式不符合要求,格式为：区号是3位或4位,总长度为[12或13]位";
		return formatRegValidater(el, msg, regExp);
	}
	
	/*
	 * 根据传入的id，出错时的提示信息、正则表达式规则完成校验，并返回true或者false
	 */
	function formatRegValidater(el, msg,regexExpression){
		var regCode = eval(regexExpression);
		if(!regCode.test($.trim(el.val()))){
			$.messager.alert("提示", msg, "warning");
			return false;
		}
		return true;
	}
	/*
	 *省份改变时，刷新市
	 */
	function changeProvince(obj){
		if(obj.id.indexOf("sender") != -1){
			provinceSelect = $("#sender_provinceid_id");
		}else{
			provinceSelect = $("#consignee_provinceid_id");
		}
		var provinceCode = provinceSelect.find("option:selected").attr("code");
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/embracedOrderInputController/getCityByProvince",
			dataType : "json",
			async: false,
			data:{
				"provinceCode":provinceCode
			},
			success : function(data) {
				var citylist = data.citylist;
				var countylist = data.countylist;
				var townlist = data.townlist;
				var citySelect;
				var countySelect;
				var townSelect;
				if(obj.id.indexOf("sender") != -1){
					citySelect = $("#sender_cityid_id");
					countySelect = $("#sender_countyid_id");
					townSelect = $("#sender_townid_id");
				}else{
					citySelect = $("#consignee_cityid_id");
					countySelect = $("#consignee_countyid_id");
					townSelect = $("#consignee_townid_id");
				}
				citySelect.empty();
				countySelect.empty();
				townSelect.empty();
				for(var i = 0; i < citylist.length; i++){
					citySelect.append("<option value='" + citylist[i].id + "' code='" + citylist[i].code + "'>" + citylist[i].name +"</option>");
				}
				for(var i = 0; i < countylist.length; i++){
					countySelect.append("<option value='" + countylist[i].id + "' code='" + countylist[i].code + "'>" + countylist[i].name +"</option>");
				}
				for(var i = 0; i < townlist.length; i++){
					townSelect.append("<option value='" + townlist[i].id + "' code='" + townlist[i].code + "'>" + townlist[i].name +"</option>");
				}
			}
		});
	}
	/*
	 *市改变时，刷新区/县
	 */
	function changeCity(obj){
		var citySelect;
		if(obj.id.indexOf("sender") != -1){
			citySelect = $("#sender_cityid_id");
		}else{
			citySelect = $("#consignee_cityid_id");
		}
		var cityCode = citySelect.find("option:selected").attr("code");
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/embracedOrderInputController/getCountyByCity",
			dataType : "json",
			async: false,
			data:{
				"cityCode":cityCode
			},
			success : function(data) {
				var countylist = data.countylist;
				var townlist = data.townlist;
				var countySelect;
				var townSelect;
				if(obj.id.indexOf("sender") != -1){
					countySelect = $("#sender_countyid_id");
					townSelect = $("#sender_townid_id");
				}else{
					countySelect = $("#consignee_countyid_id");
					townSelect = $("#consignee_townid_id");
				}
				countySelect.empty();
				townSelect.empty();
				
				countySelect.append("<option value='defaultValue'></option>");
				townSelect.append("<option value='defaultValue'></option>");
				for(var i = 0; i < countylist.length; i++){
					countySelect.append("<option value='" + countylist[i].id + "' code='" + countylist[i].code + "'>" + countylist[i].name +"</option>");
				}
				for(var i = 0; i < townlist.length; i++){
					townSelect.append("<option value='" + townlist[i].id + "' code='" + townlist[i].code + "'>" + townlist[i].name +"</option>");
				}
			}
		});
	}
	/*
	 *区/县改变时，刷新街道
	 */
	function changeCounty(obj){		
		var countySelect;
		if(obj.id.indexOf("sender") != -1){
			countySelect = $("#sender_countyid_id");
		}else{
			countySelect = $("#consignee_countyid_id");
		}
		var countyCode = countySelect.find("option:selected").attr("code");
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/embracedOrderInputController/getTownOfCounty",
			dataType : "json",
			async: false,
			data:{
				"countyCode":countyCode
			},
			success : function(data) {
				var townlist = data.townlist;
				var townSelect;
				if(obj.id.indexOf("sender") != -1){
					townSelect = $("#sender_townid_id");
				}else{
					townSelect = $("#consignee_townid_id");
				}
				townSelect.empty();
				for(var i = 0; i < townlist.length; i++){
					townSelect.append("<option value='" + townlist[i].id + "' code='" + townlist[i].code + "'>" + townlist[i].name +"</option>");
				}
			}
		});
	}
	/*
	 *点击区/县时，删除空表option
	 */
	function selectClick(obj){		
		var countySelect;
		if(obj.id.indexOf("sender") != -1){
			countySelect = $("#sender_countyid_id");
		}else{
			countySelect = $("#consignee_countyid_id");
		}
		 
		var countyCode = countySelect.val();
		if(countyCode == "defaultValue"){
			var index = obj.selectedIndex;
			obj.options.remove(index);
			changeCounty(obj);
		}
	}
	/*
	 *获取运单号对应的信息，并判断是否已经补录
	 */
	function getCwbOrderEmbraced(){
		var orderNo = $.trim($("#orderNo_id").val());
		if(orderNo != ""){
			$.ajax({
				type : "POST",
				url : "<%=request.getContextPath()%>/embracedOrderInputController/getCwbOrderEmbraced",
				dataType : "json",
				async: false,
				data:{
					"orderNo":orderNo
				},
				success : function(data) {	
					if(data.orderNo == ""){
						$.messager.alert("提示", "该运单号尚未录入", "warning");
						$("#orderNo_id").attr("value","");
						return;
					}else if(data.isadditionflag == 1){
						$.messager.alert("提示", "该运单号已经补录完成", "warning");
						$("#orderNo_id").attr("value","");
						return;
					}
					$("#sender_provinceid_id").val(data.sender_provinceid);
					$("#sender_provinceid_id").change();
					$("#sender_cityid_id").val(data.sender_cityid);
					$("#sender_cityid_id").change();
					$("#sender_adress_id").val(data.sender_adress);
					$("#sender_cellphone_id").val(data.sender_cellphone);
					$("#sender_telephone_id").val(data.sender_telephone);
					
					$("#consignee_provinceid_id").val(data.consignee_provinceid);
					$("#consignee_provinceid_id").change();
					$("#consignee_cityid_id").val(data.consignee_cityid);
					$("#consignee_cityid_id").change();
					$("#consignee_adress_id").val(data.consignee_adress);
					$("#consignee_cellphone_id").val(data.consignee_cellphone);
					$("#consignee_telephone_id").val(data.consignee_telephone);
					
					$("#number_id").val(parseFloat(data.number));
					$("#freight_total_id").val(data.freight_total); 
					$("#freight_id").val(data.freight);
					$("#receiver_id").val(data.delivermanName);
					$("#delivery_name_id").val(data.delivername);
					$("#inputdatetime_id").val(data.inputdatetime);
				}
			});
		}
	}

	/*
	 *对长/宽/高的校验和kgs的计算
	 */
	function calculateKGS(obj){
		var object;
		var msg;
		if(obj.id.indexOf("longth") != -1){
			object = $("#goods_longth_id");
			msg = "长";
		}else if(obj.id.indexOf("width") != -1){
			object = $("#goods_width_id");
			msg = "宽";
		}else{
			object = $("#goods_height_id");
			msg = "高";
		}
		var number = new Number(object.val());
		//object.val(number.toFixed(2));
		//如果没有输入，则什么都不做
		if($.trim(object.val())==""){
			return;
		}
		//校验长/宽/高为非负数
		if(!NonNegativeValidater(object,msg)){
			object.val("");
			return;
		}
		//校验长/宽/高的最大值
		if(!checkNumber(object, 1000000, msg)){
			object.val("");
			return;
		}
		//计算kgs
		var goods_longth = $.trim($("#goods_longth_id").val());
		var goods_width = $.trim($("#goods_width_id").val());
		var goods_height = $.trim($("#goods_height_id").val());
		if(goods_longth != ""){
			if(goods_width != ""){
				if(goods_height != ""){
					var kgs = (parseFloat(goods_longth)*parseFloat(goods_width)*parseFloat(goods_height))/6000;
					$("#goods_kgs_id").val(kgs.toFixed(2));
				}
			}
		}
	}
	/*
	 * 对包装费用/保价费用/运费的校验和费用合计的计算
	 */
	function calculateFreight(obj){
		var object;
		var msg;
		if(obj.id.indexOf("packing") != -1){
			object = $("#packing_amount_id");
			msg = "包装费用";
		}else if(obj.id.indexOf("insured") != -1){
			object = $("#insured_cost_id");
			msg = "保价费用";
		}else{
			object = $("#freight_id");
			msg = "运费";
		}
		//如果没有输入，则什么都不做
		if($.trim(object.val())==""){
			return;
		}
		//校验包装费用/保价费用/运费为非负数
		if(!NonNegativeValidater(object,msg)){
			object.val("");
			return;
		}
		//校验包装费用/保价费用/运费的最大值
		if(!checkNumber(object, 1000000, msg)){
			object.val("");
			return;
		}
		//计算运费合计
		var packing_amount = $.trim($("#packing_amount_id").val());
		var insured_cost = $.trim($("#insured_cost_id").val());
		var freight = $.trim($("#freight_id").val());
		if(packing_amount == ""){
			packing_amount = 0;
		}
		if(insured_cost == ""){
			insured_cost = 0;
		}
		if(freight == ""){
			freight = 0;
		}
		var freight_total = parseFloat(packing_amount) + parseFloat(insured_cost) + parseFloat(freight);
		$("#freight_total_id").val(freight_total.toFixed(2));
	}
	/*
	 * 单选改变事件
	 */
	function radioChange(obj){
		if(obj.id.indexOf("collection") != -1){
			var collection = $("input[name='collection']:checked").val();
			if(collection == 0){
				$("#collection_amount_id").css('background','#EBEBE4');
				$("#collection_amount_id").attr("readonly",true);
				$("#collection_amount_id").val("");
			}else{
				$("#collection_amount_id").css('background','#ffffff');
				$("#collection_amount_id").attr("readonly",false);
			}
		}else if(obj.id.indexOf("insured") != -1){
			var insured = $("input[name='insured']:checked").val();
			if(insured == 0){
				$("#insured_amount_id").css('background','#EBEBE4');
				$("#insured_amount_id").attr("readonly",true);
				$("#insured_amount_id").val("");
			}else{
				$("#insured_amount_id").css('background','#ffffff');
				$("#insured_amount_id").attr("readonly",false);
			}
		}else{
			var payment_method = $("input[name='payment_method']:checked").val();
			if(payment_method != 0){
				$("#monthly_account_number_id").css('background','#EBEBE4');
				$("#monthly_account_number_id").attr("readonly",true);
				$("#monthly_account_number_id").val("");
			}else{
				$("#monthly_account_number_id").css('background','#ffffff');
				$("#monthly_account_number_id").attr("readonly",false);
			}
		}
		
	}
	function companyChange(obj){
		if(obj.id.indexOf("sender") >= 0){
			$("#sender_No_id").val($("#sender_companyName_id  option:selected").attr("code"));
		}else{
			$("#consignee_No_id").val($("#consignee_companyName_id  option:selected").attr("code"));
		}
	}
	/*
	 * 补录按钮事件
	 */
	function ExtraceOrder(){
		var orderNo = $("input[name='selectRadio']:checked").val();
		if(orderNo == undefined){
			$.messager.alert("提示", "请选择需要补录的运单!", "warning"); 
			return;
		}
		var url = $("#fatherURL").val()+"?orderNo="+orderNo;
		$("#alertForm").attr('action',url);
		$('#alertForm').submit();		
	}
	/*
	 *点击未补录订单时，弹出未补录界面
	 */
	function nonExtraInput(page){
		var url = $('#alertURL').val();
		$.ajax({
			type : "POST",
			url : url+"/"+page,
			dataType : "json",
			async: false,
			data:{				
			},
			success : function(data) {
				$("#alertBox").dialog("open");
				var alert_infoMap = data.infoMap;
				var alert_page_obj = data.page_obj;
				var alert_page = data.page;
				var table = $('#nonExtracedTable');
				for(var i = $("#nonExtracedTable tr").length; i > 1; i--){
					$("#nonExtracedTable tr").eq(i-1).remove(); 
				}
				//构建信息表
				buildTable(table,alert_infoMap);
				//构建分页表
				var pagetable = $('#alertpageid');
				var pagetd=$("#alertpageid tr:eq(0) td:eq(0)");
				var previousPage;
				var nextPage;
				var maxPage;
				if(alert_page_obj.previous < 1){
					previousPage = 1;
				}else{
					previousPage = alert_page_obj.previous;
				}
				if(alert_page_obj.next < 1){
					nextPage = 1;
				}else{
					nextPage = alert_page_obj.next;
				}
				if(alert_page_obj.maxpage < 1){
					maxPage = 1;
				}else{
					maxPage = alert_page_obj.maxpage;
				}
			
				var htmlText = "<a href='javascript:nonExtraInput(1);'>第一页</a>";
				htmlText = htmlText + "<a href='javascript:nonExtraInput(" + previousPage +")'>上一页</a>";
				htmlText = htmlText + "<a href='javascript:nonExtraInput(" + nextPage +")'>下一页</a>";
				htmlText = htmlText + "<a href='javascript:nonExtraInput(" + maxPage + ")'>最后一页</a>共"; 
				htmlText = htmlText + maxPage;
				htmlText = htmlText + "页 共"; 
				htmlText = htmlText + alert_page_obj.total + "条记录 当前第";
				htmlText = htmlText + "<select id='selectPg' onchange='nonExtraInput($(this).val())'>";
				var optioinText = "";
				for(var i = 1; i <= maxPage; i++){
					if(i == alert_page){
						optioinText = optioinText + "<option value='"+ i +"' selected='true'>" + i + "</option>";
					}else{
						optioinText = optioinText + "<option value='"+ i +"'>" + i + "</option>";
					}
				}
				htmlText = htmlText + optioinText;
				htmlText = htmlText + "</select>页 ";
				pagetd.html(htmlText);
			}
		});
	}
	/*
	 * jquery动态构建表格
	 */
	function buildTable(table,alert_infoMap){
		for(var i = 0; i < alert_infoMap.length; i ++){  	 
			var row = $("<tr></tr>");
			var td1 = $("<td style='display:none;'></td>");
			td1.html(alert_infoMap[i].opscwbid);
			row.append(td1);
			table.append(row);
							
			var td2 = $("<td></td>");
			var radioHtml = "<input type='radio' name='selectRadio' value='" + alert_infoMap[i].cwb + "'/>";
			td2.html(radioHtml);
			row.append(td2);
			table.append(row);
			
			var td3 = $("<td></td>");
			td3.html(alert_infoMap[i].cwb);
			row.append(td3);
			table.append(row);
			
			var td4 = $("<td></td>");
			td4.html(alert_infoMap[i].sendcarnum);
			row.append(td4);
			table.append(row);
			
			var td5 = $("<td></td>");
			td5.html(alert_infoMap[i].collectorname);
			row.append(td5);
			table.append(row);
			
			var td6 = $("<td></td>");
			td6.html(alert_infoMap[i].inputdatetime);
			row.append(td6);
			table.append(row);				
		}
	}
	function closeClick(){
		$("#alertBox").dialog("close");
	}
	function toFix(obj,x){
		if(!numberValidater($("#"+obj.id),'输入内容')){
			$("#"+obj.id).val("");
	    	return;
	    }
		var obj_val = $("#"+obj.id).val();
		var number = new Number(obj_val);
		$("#"+obj.id).val(number.toFixed(x));
	}
</script>

</body>
</html>