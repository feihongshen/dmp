<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快递揽件录入</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/extracedOrderInput.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/jquery.area.css" type="text/css" >
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/express/new-order.css" type="text/css" >
<link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/express/embraceInputExtra/base.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/express/embraceInputExtra/template-debug.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="<%=request.getContextPath()%>/js/swfupload/swfupload.js" type="text/javascript" ></script>
<script src="<%=request.getContextPath()%>/js/jquery.swfupload.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"  type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
<style>
.RadioClass{
	display: none;
}
.RadioLabelClass{
	background: url(<%=request.getContextPath()%>/images/express/btn_radio_off3.png) no-repeat;
	padding-left: 65px;
	padding-top: 25px;
	margin: 5px;
	height: 60px;
	width: 100px;
	display: block;
	float: left;
}
.RadioSelected{
	background: url(<%=request.getContextPath()%>/images/express/btn_radio_on3.png) no-repeat;
}
.parent { background:#FFF38F;cursor:pointer;} 
.selected{ background:#FF6500;color:#fff;} 
</style>
</head>
<body>
	<div id="flag" style="display:none;">${flag}</div>
	<div id="orderNoValue" style="display:none;"> ${orderNo}</div>
	<div id="BranchprovinceId" style="display:none;" > ${BranchprovinceId}</div>
	<div id="BranchcityId" style="display:none;"> ${BranchcityId}</div>
	<div id="BranchcountyId" style="display:none;"> ${BranchcountyId}</div>
	<div id="sender_townId" style="display:none;"> </div>
	<div id="receive_provinceId" style="display:none;"> </div>
	<div id="receive_cityId" style="display:none;"> </div>
	<div id="receive_countyId" style="display:none;"></div>
	<div id="receive_townId" style="display:none;"></div>
	
	<form id="PageFromW" action="<%=request.getContextPath()%>/embracedOrderInputController/extraInputSave" method="post" onsubmit="return toVaildForm();" style="margin: 10px;">
		<fieldset ID="weibulu" style="border: inset; border-width: 0px;">
			<table width="100%" class="table1" style="margin: 10px;background-color:yellow;margin:0px;">
				<tr style="height: 20px;">
					<td></td>
	                <td width="200px" class="tdrigth" style="padding-right: 180px;">未补录运单：<span
	                        style="color: red; text-decoration: underline; font-size: 25px;"><a
	                        style="font-size:25px;color:black;"
	                        onmouseover="this.style.cssText='font-size:25px;color:blue;'"
	                        onmouseout="this.style.cssText='font-size:25px;color:black;'"
	                        href="javascript:nonExtraInput(1);">${notExtraInputNumber}单</a></span></td>
	                <td width="100px">
	                    <button onclick="$('#importBox').dialog('open');initImportBox();return false;">运单导入</button>
	                </td>
	                <td width="100px">
	                    <button onclick="downloadExcel();return false;">导入模版</button>
	                </td>
				</tr>
			</table>
		</fieldset>
		<fieldset ID="head" style="border:solid; border-width: 1px;">
			<table width="99%" class="table1" style="margin: 5px;">
				<colgroup>
					<col width="7%">
					<col width="20%">
					<col width="8%">
					<col width="15%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="14%">
					<col width="15%">
				</colgroup>
				<tr style="line-height: 15px;">
					<td class="tdrigth">快递单号&nbsp<span style="color:#FF0000">*</span>:</td>
					<td class="tdleft"><input type="text" name="orderNo" id="orderNo_id" onchange="getCwbOrderEmbraced()" value="${orderNo}" style=" font-size:22px;color:red;height:30px;width:100%;"/></td>
					<td class="tdrigth">揽件站点<font>*</font>:</td>
					<td><input type="text" name="prestation" id="prestation_id"  readonly="readonly"  style="background:#EBEBE4;width:100%;" value="${branchname }"/></td>
					<td class="tdrigth">揽件员&nbsp<span style="color:#FF0000">*</span>:</td>
					<td>
						<select id="delivermanId_show" style="width:100%;">
						    <option value="">请选择</option>
							<c:forEach items="${deliveryMansList}" var="list">
								<option value="${list.userid}">${list.realname}</option>
							</c:forEach>
						</select>
					</td>
					<input type="hidden" name="delivermanId" id="delivermanId_id"/>
					<input type="hidden" name="delivermanName" id="delivermanName_id"/>
					<td></td>
					<td class="tdrigth">寄件人证件号：</td>
					<td><input type="text" name="sender_certificateNo" id="sender_certificateNo_id" style="width:100%;"/></td>
				</tr>
				<tr>
	                <td class="tdrigth">预约单号:</td>
	                <td class="tdleft"><input type="text" name="reserveOrderNo" id="reserveOrderNo"  
	                      readonly="readonly" style="background:#EBEBE4;width:100%;" /></td>
                    <td id="recordVersionTd" style="display:none;"><input type="text" name="recordVersion" id="recordVersion"/></td>
	                <td class="tdrigth">服务产品&nbsp<span style="color:#FF0000">*</span>:</td>
	                <td>
	                	<select name="express_product_type" id="express_product_type"  style="width:100%;" onchange="getFeeByCondition()">
							<option value="1" >标准</option>
							<option value="2" >次日达</option>
							<option value="3" >当日达</option>
						</select>
	                </td>
	            </tr>
			</table>
		</fieldset>
		<table width="99%" style="margin-top: 0px;">
			<tr style="line-height: 25px;">
				<td class="tdrigth"><input type="button" name="addImg" id="addImg" value="点击查看图片" onclick="openwin()"/></td>
			</tr>
		</table>
	   	<%-- <div id="showImg" style="display:none" ><img src="<%=request.getContextPath()%>/images/bg_3.gif "></div> --%>
	    <div id="img" style="display: none">
		</div>
		<!-- <div id="img" style="display: none" ><img src="http://10.199.195.25:8080/express/file/bookingOrder?date=2016-06-01&fileName=89890610006.jpg"></div> -->
		<table width="100%" class="table1">
			<colgroup>
				<col width="50%">
				<col width="50%">
			</colgroup>
			<tr>
				<td>
					<fieldset ID="sender" style="border:solid; border-width: 1px;background-color:rgb(0,204,255);margin-top: 5px;">
						<legend>
							<span style="color: red;font-size:16px;">&nbsp;&nbsp;寄件人信息</span>
						</legend>
						<table width="99%" class="table1" style="padding:10px;">
							<colgroup>
								<col width="20%">
								<col width="20%">
								<col width="20%">
								<col width="20%">
								<col width="19%">
							</colgroup>
							<tr style="line-height: 35px;">
								<td class="tdrigth" >寄件人<font>*</font>:</td>
								<td class="tdleft"><input type="text" name="sender_name" id="sender_name_id" /></td>
							</tr>
							<tr>
								<td class="tdrigth">手机：</td>
								<td class="tdleft"><input type="text" name="sender_cellphone" id="sender_cellphone_id" onblur="getReserveOrderBySenderPhone(this.value,1)"/></td>
								<td class="tdrigth">固话：</td>
								<td class="tdleft"><input type="text" name="sender_telephone" id="sender_telephone_id"  onblur="getReserveOrderBySenderPhone(this.value,2)"/></td>
								<td class="tdcenter" colspan="2">（手机、固话必填1个）</td>
							</tr>
							<tr>
								<td class="tdrigth">单位名称:</td>
								<td colspan="3" class="tdleft">
									<select name="sender_companyName" id="sender_companyName_id"  style="width:100%;" readonly="readonly" onchange="companyChange(this)">
										<c:forEach items="${customers}" var="list">
											<option value="${list.companyname}" code="${list.customercode}" id="${list.customerid}">${list.companyname}</option>
										</c:forEach>
									</select>
								</td>
								<td class="tdleft"><input type="text" name="sender_No" id="sender_No_id" style="background:#EBEBE4;height:15px;vertical-align:text-top;" disabled></td>								
								<td style="display:none;"><input type="text" name="sender_customerid" id="sender_customerid_id"/></td>
							</tr>
							<tr>
								<td class="tdrigth">始发地&nbsp<span style="color:#FF0000">*</span>:</td>
	                            <td colspan="2">
	                                <div class="col-xs-4">
	                                    <input readonly="readonly" name="sender_cityselect" placeholder="请选择地区" type="text"
	                                           style="width:100%" id="area_sender"/>
	                                </div>
	                                <input type="hidden" name="sender_provinceid" id="sender_provinceid_id"/>
	                                <input type="hidden" name="sender_provinceName" id="sender_provinceName_id"/>
	                                <input type="hidden" name="sender_cityid" id="sender_cityid_id"/>
	                                <input type="hidden" name="sender_cityName" id="sender_cityName_id"/>
	                                <input type="hidden" name="sender_countyid" id="sender_countyid_id"/>
	                                <input type="hidden" name="sender_countyName" id="sender_countyName_id"/>
	                                <input type="hidden" name="sender_townid" id="sender_townid_id"/>
	                                <input type="hidden" name="sender_townName" id="sender_townName_id"/>
	                            </td>
							</tr>
							<tr>
								<td class="tdleft">寄件地址<font>*</font>:</td>
								<td class="tdleft" colspan="4"><input type="text" name="sender_adress" id="sender_adress_id"style="width:100%;" value="${adressInfoDetailVO.getProvinceName()}${adressInfoDetailVO.getCityName()}" onchange="getFeeByCondition()"/></td>
							</tr>
							<tr><td colspan="6"><b>预约单：</b></font></td></tr>
							<tr>
							    <td colspan="6">
									<table class="table1" border="1px" id="reserveOrderTable" width="100%">
									  <thead>
									  	<tr >
											<th class="tdleft" width="20%">预约单号</th>
									    	<th class="tdleft" width="20%">寄件人</th>
									    	<th class="tdleft" width="35%">地址</th>
									    	<th class="tdleft" width="25%">寄件时间</th>
										</tr>
									  </thead>
									  
									  <tbody>
										   <tr class="parent" id="reserveOrderShow">
										   		<td colspan="6">点击展开/隐藏table内容</td>
										   </tr>
									  </tbody>
									 </table>
								</td>
							</tr>
							<tr><td colspan="6"><b>寄件人历史信息：</b></font></td></tr>
							<tr>
							    <td colspan="6">
									<table class="table1" border="1px" id="senderHistoryTable" width="100%">
									  <thead>
									  	<tr >
											<th class="tdleft" width="15%">寄件人</th>
									    	<th class="tdleft" width="15%">省</th>
									    	<th class="tdleft" width="15%">市</th>
									    	<th class="tdleft" width="15%">区</th>
									    	<th class="tdleft" width="15%">街道</th>
									    	<th class="tdleft" width="25%">详细地址</th>
									    	<th class="tdleft" style="display:none">订单id</th>
										</tr>
									  </thead>
									  <tbody>
										   <tr class="parent" id="senderHistory">
										   		<td colspan="6">点击展开/隐藏table内容</td>
										   </tr>
									  </tbody>
									 </table>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
				<td>
					<fieldset ID="consignee" style="border:solid; border-width: 1px;background-color:rgb(204,204,153);margin-top: 5px;">
						<legend>
							<span style="color: red;font-size:16px;">&nbsp;&nbsp;收件人信息</span>
						</legend>
						<table width="99%" class="table1" style="padding:10px;">
							<colgroup>
								<col width="20%">
								<col width="20%">
								<col width="20%">
								<col width="20%">
								<col width="19%">
							</colgroup>
							<tr style="line-height: 35px;">
								<td class="tdrigth">收件人<font>*</font>:</td>
								<td class="tdleft"><input type="text" name="consignee_name" id="consignee_name_id" /></td>
							</tr>
							<tr>
								<td class="tdrigth">手机：</td>
								<td class="tdleft"><input type="text" name="consignee_cellphone" id="consignee_cellphone_id" onblur="getReserveOrderByConsignPhone(this.value,3)"/></td>
								<td class="tdrigth">固话：</td>
								<td class="tdleft"><input type="text" name="consignee_telephone" id="consignee_telephone_id" onblur="getReserveOrderByConsignPhone(this.value,4)"/></td>
								<td class="tdcenter" colspan="2">（手机、固话必填1个）</td>					
							</tr>
							<tr>
								<td class="tdrigth">单位名称:</td>
								<td colspan="3" class="tdleft">
									<select name="consignee_companyName" id="consignee_companyName_id" style="width:100%;" readonly="readonly" onchange="companyChange(this)">
										<c:forEach items="${customers}" var="list">
											<option value="${list.companyname}" code="${list.customercode}" id="${list.customerid}">${list.companyname}</option>
										</c:forEach>
									</select>
								</td>
								<td class="tdleft">
									<input type="text" name="consignee_No" id="consignee_No_id" style="background:#EBEBE4;height:15px;vertical-align:text-top;" disabled>								
								<td style="display:none;"><input type="text" name="consignee_customerid" id="consignee_customerid_id"/></td>
							</tr>
							<tr>
								<td class="tdrigth">目的地&nbsp<span style="color:#FF0000">*</span>:</td>
								<td colspan="2">
									<div class="col-xs-4" >
										<input readonly="readonly"  name="consignee_cityselect" placeholder="请选择地区" type="text"   style="width:100%" id="area_consignee"/>
									</div>
									<input type="hidden" name="consignee_provinceid" id="consignee_provinceid_id"/>
									<input type="hidden" name="consignee_provinceName" id="consignee_provinceName_id"/>
									<input type="hidden" name="consignee_cityid" id="consignee_cityid_id"/>
									<input type="hidden" name="consignee_cityName" id="consignee_cityName_id"/>
									<input type="hidden" name="consignee_countyid" id="consignee_countyid_id"/>
									<input type="hidden" name="consignee_countyName" id="consignee_countyName_id"/>
									<input type="hidden" name="consignee_townid" id="consignee_townid_id"/>
									<input type="hidden" name="consignee_townName" id="consignee_townName_id"/>									
								</td>	
							</tr>
							<tr>
								<td class="tdrigth">收件地址<font>*</font>:</td>
								<td class="tdleft" colspan="6"><input type="text" name="consignee_adress" id="consignee_adress_id" style="width:100%;" onchange="getFeeByCondition()"/></td>
							</tr>
							<tr><td colspan="6"><b>收件人历史信息：</b></font></td></tr>
							<tr>
							    <td colspan="6">
									<table class="table1" border="1px" id="consigneeHistoryTable" width="100%">
									  <thead>
									  	<tr >
											<th class="tdleft" width="15%">收件人</th>
									    	<th class="tdleft" width="15%">省</th>
									    	<th class="tdleft" width="15%">市</th>
									    	<th class="tdleft" width="15%">区</th>
									    	<th class="tdleft" width="15%">街道</th>
									    	<th class="tdleft" width="25%">详细地址</th>
									    	<th class="tdleft" style="display:none">订单id</th>
										</tr>
									  </thead>
									  <tbody>
										   <tr class="parent" id="consigneeHistory">
										   		<td colspan="6">点击展开/隐藏table内容</td>
										   </tr>
									  </tbody>
									 </table>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		
		<fieldset ID="goodsInfo" style="border:solid; border-width: 1px;margin-top: 5px;">
			<legend>
				<span style="color: red;font-size:16px;">&nbsp;&nbsp;托物资料</span>
			</legend>
			<table width="100%" class="table1" style="padding:10px;">
				<colgroup>
					<col width="7%">
					<col width="10%">
					<col width="5%">
					<col width="5%">
					<col width="7%">
					<col width="5%">
					<col width="6%">
					<col width="4%">
					<col width="5%">
					<col width="4%">
					<col width="4%">
					<col width="6%">
					<col width="4%">
					<col width="6%">
					<col width="4%">
					<col width="4%">
					<col width="2%">
					<col width="6%">
					<col width="6%">
				</colgroup>
				<tr>
					<td class="tdleft">托物内容<font>*</font>:</td>
					<td class="tdleft"><input type="text" name="goods_name" id="goods_name_id"style="width:100%;" /></td>
					<td class="tdrigth">数量<font>*</font>:</td>
					<td class="tdleft"><input type="text" name="goods_number" id="goods_number_id" style="width:100%;"/></td>
					<td class="tdrigth">计费重量&nbsp<span style="color:#FF0000">*</span>:</td>
					<td class="tdleft"><input type="text" name="charge_weight" id="charge_weight_id" style="width:100%;" onchange="toFix(this,2)"/></td>
					<td class="tdrigth">实际重量&nbsp<span style="color:#FF0000">*</span>:</td>
					<td class="tdleft"><input type="text" name="actual_weight" id="actual_weight_id" style="width:100%;" onchange="toFix(this,2)"/></td>
					<td class="tdrigth"><input type="button" onclick="read()" value="读数"/></td>
					<td class="tdrigth">长:</td>
					<td class="tdleft"><input type="text" name="goods_longth" id="goods_longth_id" style="width:100%;background-color:rgb(0,255,255);" onblur="calculateKGS(this)"/></td>
					<td class="tdcenter">CM × 宽:</td>
					<td class="tdleft"><input type="text" name="goods_width" id="goods_width_id" style="width:100%;background-color:rgb(0,255,255);" onblur="calculateKGS(this)"/></td>
					<td class="tdcenter">CM × 高:</td>
					<td class="tdleft"><input type="text" name="goods_height" id="goods_height_id" style="width:100%;background-color:rgb(0,255,255);" onblur="calculateKGS(this)"/></td>
					<td class="tdleft">CM</td>
					<td rowspan="2" style="vertical-align:middle; text-align:center;">=</td>
					<td rowspan="2" style="vertical-align:middle; text-align:center;"><input type="text" name="goods_kgs" id="goods_kgs_id" class="input_text1" style="width:80%;margin-left:5px;background:#EBEBE4;"  readonly="readonly"/></td>
					<td rowspan="2" style="vertical-align:middle; text-align:left;">kgs</td>
				</tr>
				<tr>
					<td class="tdcenter">其他:</td>
					<td colspan="8"  class="tdrigth"><input type="text" name="goods_other" id="goods_other_id" style="width:90%;"/></td>
					<td colspan="7"  class="tdcenter" style="padding-left:23px;"><hr/>6000</td>
				</tr>
			</table>
		</fieldset>
		 <fieldset ID="businessType" style="border:solid; border-width: 1px;margin-top: 15px;">
			<table width="100%" class="table1" style="padding:10px;">
				<colgroup>
					<col width="7%">
					<col width="12%">
					<col width="5%">
					<col width="5%">
					<col width="7%">
					<col width="5%">
					<col width="7%">
					<col width="2%">
					<col width="9%">
					<col width="5%">
					<col width="7%">
					<col width="5%">
					<col width="9%">
					<col width="2%">
					<col width="8%">
					<col width="5%">
				</colgroup>
				<tr>
					<td class="tdcenter">费用合计：</td>
					<td class="tdleft"><input type="text" name="freight_total" id="freight_total_id"  readonly="readonly" style=" font-size:22px;color:red;height:30px;background:#EBEBE4;width:100%;"/></td>
					<td class="tdrigth">运费&nbsp<span style="color:#FF0000">*</span>:</td>
					<td class="tdleft"><input type="text" name="freight" id="freight_id" style="width:100%;" onchange="toFix(this,2)"/></td>
					<td class="tdrigth">包装费用:</td>
					<td class="tdleft"><input type="text" name="packing_amount" id="packing_amount_id" onblur="calculateFreight(this)" style="width:100%;" onchange="toFix(this,2)"/></td>
					<td class="tdrigth">是否保价:</td>
					<td class="tdleft"><input type="checkbox" name="insured_show" id="insured_show"  value="1" onchange="checkboxChange(this)"/></td>
					<input type="hidden" name="insured" id="insured_id" value="0"/>
					<td class="tdrigth">保价声明价值:</td>
					<td class="tdleft"><input type="text" name="insured_amount" id="insured_amount_id" style="width:100%;background:#EBEBE4;" readonly="true"onchange="toFix(this,2)"/></td>
					<td class="tdrigth">保价费用:</td>
					<td class="tdleft"><input type="text" name="insured_cost" id="insured_cost_id"  style="width:100%;" onblur="calculateFreight(this)" onchange="toFix(this,2)"/></td>
					<td class="tdrigth">是否代收货款:</td>
					<td class="tdleft"><input type="checkbox" name="collection_show" id="collection_show"  value="1" onchange="checkboxChange(this)"/></td>
					<input type="hidden" name="collection" id="collection_id" value="0"/>
					<td class="tdrigth">代收货款金额:</td>
					<td class="tdleft"><input type="text" name="collection_amount" id="collection_amount_id" style="background:#EBEBE4;" readonly="true" onchange="toFix(this,2)"/></td>
					<input type="hidden" name="isRead" id="isRead"_id" value="0"/>
				</tr>	
			</table>
		</fieldset>	
		
		<fieldset ID="businessType" style="border:solid; border-width: 1px;margin-top: 15px;">
			<table width="100%" class="table1" style="padding:10px;">
				<colgroup>
					<col width="15%">
					<col width="15%">
					<col width="15%">
					<col width="15%">
					<col width="10%">
					<col width="15%">
					<col width="15%">
				</colgroup>
					<tr>
						<td class="tdleft" style="vertical-align:middle; text-align:center;"><font size="3px" color="red">结算方式 *:</font></td>
						<td class="tdcenter"  style="vertical-align:middle; text-align:center;">
							<input id="Radio1" type="radio" class="RadioClass" name="payment_method"  value="1" onchange="getFeeByCondition()"/>
							<label id="Label1" for="Radio1" class="RadioLabelClass" ><font size="6px" >现付</font></label>
						</td>
						<!-- 暂时去掉快递到付类型 modify by vic.liang@pjbest.com 2016-08-08  -->
						<!-- <td class="tdcenter">
							<input id="Radio2" type="radio" class="RadioClass" name="payment_method"  onchange="getFeeByCondition()" value="2"  />
							<label id="Label2" for="Radio2" class="RadioLabelClass"><font size="6px">到付</font></label>
						</td> -->
						<td class="tdcenter">
							<input id="Radio3" type="radio" class="RadioClass" name="payment_method"  value="0"/>
							<label id="Label3" for="Radio3" class="RadioLabelClass"><font size="6px">月结</font></label>
						</td>
						<td class="tdcenter">
							<input id="Radio4" type="radio" class="RadioClass" name="payment_method"  value="3"/>
							<label id="Label4" for="Radio4" class="RadioLabelClass" style="width:200px;"><font size="6px">第三方支付</font></label>
						</td>
						
					</tr>
					<tr >
					   <td style="display:none;" id="paywayName" class="tdrigth" style="vertical-align:middle; text-align:center;">支付方式：</td>
					   <td style="display:none;" id="paywayidTd" > 
						   <select name="paywayid" id="paywayid"  style="width:100%;" >
								<option value="1" >现金</option>
								<option value="2" >pos支付扫描</option>
								<option value="5" >cod支付扫描</option>
							</select>
						</td>
						<td class="tdrigth" style="vertical-align:middle; text-align:center;">月结账号：</td>
						<td class="tdleft" style="vertical-align:middle; text-align:center;"><input type="text" name="monthly_account_number" id="monthly_account_number_id" style="width:100%;" /></td>
						<td class="tdcenter" style="vertical-align:middle; text-align:center;" ><font color="red">月结/第三方支付：月结/第三方支付账户必填</font></td>
					</tr>
				</table>
		</fieldset>	
		<fieldset ID="remarksInfo" style="border:solid; border-width: 1px;margin-top: 15px;">
			<table width="100%" class="table1" style="padding:10px;">
				<colgroup>
					<col width="5%">
					<col width="95%">
				</colgroup>
				<tr>
					<td class="tdleft">备注:</td>
					<td class="tdleft"><input type="text" name="remarks" id="remarks_id" style="width:60%;"/></td>					
					<input type="hidden" name="inputdatetime" id="inputdatetime_id"/>
					<input type="hidden" name="isadditionflag" id="isadditionflag_id"/>
				</tr>				
			</table>
		</fieldset>	
		<table width="99%" style="margin-top: 10px;">
			<tr style="line-height: 35px;">
				<td class="tdleft"><input type="submit" value="保存" style="height:60px;width:150px;margin-left:20px;" /></td>
			</tr>
		</table>
		<input type="hidden" name="number" id="number_id" value="1"/>	
		<input type="hidden" name="goods_weight" id="goods_weight_id"/>	
		<input type="hidden" name="origin_adress" id="origin_adress_id"/>	
		<input type="hidden" name="destination" id="destination_id"/>	
		<input type="hidden" name="consignee_certificateNo" id="consignee_certificateNo_id"/>	
		<%-- 	
				<td class="tdcenter">件数<font>*</font>:</td> 1
				<td class="tdcenter">始发地<font>*</font>:</td> 发件省编码
				<td class="tdcenter">目的地：</td>   收件省编码
				<td class="tdcenter">重量:</td> 等于实际重量
				<td class="tdcenter">派件员:</td> 不管，之前就不可填
				<td class="tdcenter">收件人证件号:</td> 不填
		--%>
	</form>
	 
	<div>
		<div id="alertBox" class="easyui-dialog" title="未补录揽收运单"  style="width: 1000px; height: 450px;  padding: 10px 10px;z-index:400px;" closed="true">
			<table width="100%" class="table1">
				<colgroup>
					<col width="10%">
					<col width="10%">
					<col width="60%">
				</colgroup>
				<tr>
					<td class="tdcenter"><button  onclick="closeClick()" >返回</button></td>
					<td class="tdcenter"><button  onclick="ExtraceOrder()" >补录</button></td>
					<td class="tdcenter"><button  onclick="exportAllExtraceOrder()" >导出全部</button></td>
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
	
	<div id="importBox" class="easyui-dialog" title="导入信息详情"  style="width: 600px; height: 450px;  padding: 10px 10px;z-index:400px;" closed="true">
		<table width="100%" class="table_2" >
			<tr>
				<td class="tdleft">
				
				</div>
					<div id="swfupload-control">
						<div style="float: left; overflow: auto;margin-top:5px;">
							<font color="red">&nbsp;&nbsp;Excel文件*</font>
							<input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF; font-size:12px; height:25px; width: 170px;" />
						</div>
						<div style="float: left; overflow: auto; margin-top:10px;">
							<input type="button" id="button" />
						</div>
					</div>
					<!-- EXCEL表格导入：<span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button"/></span>
				 --></td>
				<td class="tdcenter">
					<input name="button35" type="button" id="importButton" value="开始导入"  class="input_button2" style="float: left; overflow: auto;margin-bottom:10px; " title="此功能只会插入“新订单”记录或者更新“有货无单入库”记录" />
					<input type="hidden" id="anyid"/>
				</td>
			</tr>
		</table>
		<div id="importInfo" style="display:none;">
			<table  width="100%">
				<tr >
					<td style="padding:10px;"><font size="6";color="black">成功：<span style="color: black;  font-size: 6;" id="successCount" >0</span></font></td>
					<td style="padding:10px;"><font size="6";color="black">失败：<span style="color: black;  font-size: 6;" id="failureCount" >0</span></font></td>
				</tr>
			</table>
			<table width="100%" id="importInfoTable" border="0" cellspacing="1" cellpadding="0" class="table_2" >
				<colgroup>
					<col width="50%"/>
					<col width="50%"/>
				</colgroup>
				<tr class="font_1">
					<th bgcolor="#eef6ff">运单号</th>
					<th bgcolor="#eef6ff">失败原因</th>
				</tr>
			</table>			
			<table width="100%" class="table1">
				<tr>
					<td class="tdrigth"><button id="importConfirm" onclick="confirmImport()" >确认导入</button></td>
					<td class="tdleft"><button  id="importCancel" onclick="cancelImport()" >取消导入</button></td>
				</tr>
			</table>
		</div>
	</div>
	<form id="alertForm" action="" method="post" style="margin: 10px;display:none;">	
		<input name="cwb" id="alertForm_cwb"/>
	</form>
	<form id="exportExtraOrderForm" action="<%=request.getContextPath()%>/embracedOrderInputController/exportAllNotExtraOrder" method="" style="display:none;">
	</form>
	<input type="hidden" id="alertURL" value="<%=request.getContextPath()%>/embracedOrderInputController/nonExtraInput" />
	<input type="hidden" id="fatherURL" value="<%=request.getContextPath()%>/embracedOrderInputController/embracedOrderExtraInputInit" /> 
	<input type="hidden" id="province_path" value="<%=request.getContextPath()%>/embracedOrderInputController/getProvince"/>
	<input type="hidden" id="nextaddress_path" value="<%=request.getContextPath()%>/embracedOrderInputController/getNextAddress"/>
	<input type="hidden" id="import_path" value="<%=request.getContextPath()%>/embracedOrderInputController/import"/>
	<input type="hidden" id="import_save_path" value="<%=request.getContextPath()%>/embracedOrderInputController/saveImportInfo"/>
	<a id="downloada" href="<%=request.getContextPath()%>/embracedOrderInputController/downloadExcel?filePath=<%=request.getContextPath()%>" style="display:none;">下载模板</a> 
<script type="text/javascript">	
	//焦点在form中时，阻止enter键提交form
	var importDateid;
	var msgArr = [];
	$("#PageFromW").keypress(function(e) {
	  	if (e.keyCode == 13) {		
	      	return false;
	  	}
	}); 
	//焦点在运单号输入框时，enter键带出运单号，并阻止form提交
	$("#orderNo_id").keypress(function(e) {
		if (e.keyCode == 13) {
			$("#orderNo_id").blur();
			return false;
		}
	}); 
	
	$(".RadioClass").change(function(){
			if($(this).is(":checked")){
				$(".RadioSelected:not(:checked)").removeClass("RadioSelected");
				$(this).next("label").addClass("RadioSelected");
			}
			
			if($("input[name='payment_method']:checked").val() != 0 && $("input[name='payment_method']:checked").val() != 3){
				$("#monthly_account_number_id").css('background','#EBEBE4');
				$("#monthly_account_number_id").attr("readonly",true);
				$("#monthly_account_number_id").val("");
			}else{
				$("#monthly_account_number_id").css('background','#ffffff');
				$("#monthly_account_number_id").attr("readonly",false);
			}
			
			if($("input[name='payment_method']:checked").val() == 1){
				document.getElementById('paywayidTd').style.display = "";
				document.getElementById('paywayName').style.display = "";
			}else{
				document.getElementById('paywayidTd').style.display = "none";
				document.getElementById('paywayName').style.display = "none";
			}
	});
	
	function selected(value){
		var all_options = document.getElementById("express_product_type").options;
		for (i=0; i<all_options.length; i++){
			if (all_options[i].value == value) // 根据option标签的ID来进行判断 测试的代码这里是两个等号
			{
				all_options[i].selected = true;
			}
		}
	};
	
	$(function(){
		$("#addImg").hide(); 
		if($.trim($("#flag").html()) == "true"){
			alert("保存成功");
		}else if($.trim($("#flag").html()) == "false"){
			$.messager.alert("提示", "保存失败!", "warning"); 
		}else if($.trim($("#flag").html()) == "hasSaved"){
			$.messager.alert("提示", "该运单号已经录入过，保存失败!", "warning"); 
		}else if($.trim($("#flag").html()) == "tpsErr"){
			$.messager.alert("提示", "该运单号已经录入过，保存失败!", "warning"); 
		}
		if($.trim($("#orderNoValue").html()) != ""){
			$("#orderNo_id").val($.trim($("#orderNoValue").html()));
			$("#orderNo_id").change();
			$("#consignee_cityid_id").blur();
		}
		$("#sender_No_id").val($("#sender_companyName_id  option:selected").attr("code"));
		$("#consignee_No_id").val($("#consignee_companyName_id  option:selected").attr("code"));
		//bug修改
		/* $("#Radio1").attr('checked', 'checked'); 
		$(".RadioClass").change(); */
		
	});
	/*
	 *提交form前的检验
	 */
	function toVaildForm(){	
 		//校验揽件人信息
        if(!checkDeliverman()){
        	return false;
        } 
    	
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
       /*  if(!checkAreaCode()){
        	return false;
        } */
      	//校验付款方式
        if(!checkPayMethod()){
        	return false;
        }
    	
      	//校验备注
        if(!checkRemarksInfo()){
        	return false;
        }
      	if(msgArr.length > 0){
//       		msgArr.push("确认保存？");
//       		if(!confirm(msgArr.join(',')+"")){
//       			msgArr = [];
//         		return false;
//         	} 
			alert(msgArr.join(',')+"");
      		msgArr = [];
      		return false;
      	}
    	
    	/* $.messager.confirm("确认", msgArr.join('、')+"尚未填写" + ",确认保存？", function (r) {  
	        if (r) {  
	        	return true;
	        } 
	    }); */
        assignValue();
        return true;
        
    }
	//给与select对应的输入框赋值（因为要给后台传select对应的text）,给未填的int类型的默认值
	function assignValue(){
		$("#sender_customerid_id").val($("#sender_companyName_id  option:selected").attr("id"));	
		$("#consignee_customerid_id").val($("#consignee_companyName_id  option:selected").attr("id"));	
		$("#delivermanId_id").val($("#delivermanId_show  option:selected").val());
		$("#delivermanName_id").val($("#delivermanId_show  option:selected").text());
		$("#goods_weight_id").val($("#actual_weight_id").val());
		$("#paywayid").val($("#paywayid").val());
	}
	
	/*
	 *校验第一个框里的数据
	 */
	  function checkDeliverman(){
		var delivermanId = $("#delivermanId_show");
		
		//校验揽件员id是否为空
        if(!nullValidater(delivermanId,"揽件员")){
        	$.messager.alert("提示","揽件员不能为空！", "warning");
        	return false;
        }
		return true;
	}
	
	/*
	 *校验第一个框里的数据
	 */
	  function checkHead(){
		var orderNo = $("#orderNo_id");
		
		//校验运单号是否为空
        if(!nullValidater(orderNo,"运单号")){
        	$.messager.alert("提示","运单号不能为空！", "warning");
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
		 var sender_province = $("#sender_provinceid_id");
		 var sender_city = $("#sender_cityid_id");
		 var sender_county = $("#sender_countyid_id");
		 
		var sender_adress = $("#sender_adress_id");
		var sender_cellphone = $("#sender_cellphone_id");
		var sender_telephone = $("#sender_telephone_id");
		var sender_companyName = $("#sender_companyName_id");
		var sender_name = $("#sender_name_id");
		var sender_certificateNo = $("#sender_certificateNo_id");
		var sender_No = $("#sender_No_id");
		var payment_method = $("input[name='payment_method']:checked").val();//0为月结
		var countySelect = $("#sender_countyid_id");
		
		//如果不属于补录，则第一次只要求填写寄件三级地址
		if ($("#isadditionflag_id").val() == 0) {
			//校验寄件人地址
	        if(sender_province.val() == "" || sender_city.val() == "" || sender_county.val() == ""){
	        	//$.messager.alert("提示", "请完善寄件人地址", "warning");
				confirmFunction("始发地不完善");	
	        }
		}else{
			//校验寄件人客户编码是否超长
	      	/* if($.trim(sender_No.val()) != ""){
	      		if(!checkLength(sender_No, 10,"寄件人客户编码",10)){
	            	return false;
	            }
	      	} */
	       //如果为月结，校验寄件人公司是否为空
	      	if(payment_method == 0){
		        if(!nullValidater(sender_companyName,"寄件人公司")){
		        	confirmFunction("寄件人公司未填写");
		        }
	      	}
	      	//校验寄件人公司是否超长
	      	/* if(!checkLength(sender_companyName, 50, '寄件人公司', 50)){
	        	return false;
	        } */
	       	//校验寄件人是否为空
	       	
	        if(!nullValidater(sender_name,"寄件人")){
	        	confirmFunction("寄件人未填写");
	        }else
	      	//校验寄件人是否超长
	      	if(!checkLength(sender_name, 25, '寄件人', 25)){
	        	return false;
	        }
	      	//校验寄件人地址是否为空
	        if(!nullValidater(sender_adress,"寄件人地址")){
	        	confirmFunction("寄件人地址未填写");
	        }else
	      	//校验寄件人地址是否超长
	      	if(!checkLength(sender_adress, 100, '寄件人地址', 100)){
	        	return false;
	        }
	      	//校验寄件人电话和手机至少一个不为空
	      	var senderPhone = [sender_cellphone, sender_telephone];
	      	if(!multiNullValidater(senderPhone, "寄件人手机和固话")){
	      		confirmFunction("寄件人手机或固话未填写");
	        }
	      	//校验寄件人手机号是否正确
	      	/* if($.trim(sender_cellphone.val()) != ""){
	      		if(!cellPhoneValidater(sender_cellphone, "寄件人")){
	            	return false;
	            }
	      	} */
	      	//校验寄件人固话是否正确
	      	/* if($.trim(sender_telephone.val()) != ""){
	      		if(!phoneValidater(sender_telephone,"寄件人")){
	            	return false;
	            }
	      	} */
	      	//校验寄件人证件号是否超长
	      	if($.trim(sender_certificateNo.val()) != ""){
	      		if(!checkLength(sender_certificateNo, 25,"寄件人证件号",25)){
	            	return false;
	            }
	      	}
	      	
		}
			
      	return true;
	}

	/*
	 *校验收件人的数据
	 */
	function checkConsignee(){
		 var consignee_province = $("#consignee_provinceid_id");
		 var consignee_city = $("#consignee_cityid_id");
		 var consignee_county = $("#consignee_countyid_id");
		 
      	var consignee_adress = $("#consignee_adress_id");
		var consignee_cellphone = $("#consignee_cellphone_id");
		var consignee_telephone = $("#consignee_telephone_id");
		var consignee_companyName = $("#consignee_companyName_id");
		var consignee_name = $("#consignee_name_id");
		var consignee_certificateNo = $("#consignee_certificateNo_id");
		var consignee_No = $("#consignee_No_id");
		
		if($("#isadditionflag_id").val()==0){
			//校验收件人地址
	        if(consignee_province.val() == "" || consignee_city.val() == "" || consignee_county.val() == ""){
	        	//$.messager.alert("提示", "请完善收件人地址", "warning");
	        	confirmFunction("目的地不完善");
	        }
		}else{
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
	        	confirmFunction("收件人未填写");
	        }else
	      	//校验收件人是否超长
	      	if(!checkLength(consignee_name, 25, '收件人', 25)){
	        	return false;
	        }
	      	//校验收件人地址是否为空
	        if(!nullValidater(consignee_adress,"收件人地址")){
	        	confirmFunction("收件人地址未填写");
	        }else
	      	//校验收件人地址是否超长
	      	if(!checkLength(consignee_adress, 100, '收件人地址', 100)){
	        	return false;
	        }
	      	//校验收件人电话和手机至少一个不为空
	      	var consigneePhone = [consignee_cellphone, consignee_telephone];
	      	if(!multiNullValidater(consigneePhone, "收件人手机和固话")){
	      		confirmFunction("收件人手机或固话未填写");
	        }
	      	//校验收件人手机号是否正确
	      	/* if($.trim(consignee_cellphone.val()) != ""){
	      		if(!cellPhoneValidater(consignee_cellphone, "收件人")){
	            	return false;
	            }
	      	} */
	      	//校验收件人固话是否正确
	      	/* if($.trim(consignee_telephone.val()) != ""){
	      		if(!phoneValidater(consignee_telephone, "收件人")){
	            	return false;
	            }
	      	} */
	      //校验收件人证件号码是否超长
	      	if($.trim(consignee_certificateNo.val()) != ""){
	      		if(!checkLength(consignee_certificateNo, 25, "收件人证件号",25)){
	            	return false;
	            }
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
		
		if($("#isadditionflag_id").val()!=0){
			//校验件数是否为空
	        if(!nullValidater(goods_name,"托物内容/名称")){
	        	confirmFunction("托物内容/名称未填写");
	        }else 
			//校验件数是否超过长度
	        if(!checkLength(goods_name, 50, '托物内容/名称', 50)){
	        	return false;
	        }
	      	//校验数量是否为空
	        if(!nullValidater(goods_number,"数量")){
	        	confirmFunction("数量未填写");
	        }else
	      	//校验数量是否为0或正整数
	        if(!numberValidater(goods_number,'数量')){
	        	return false;
	        }else 
			//校验数量是否超过大小
	        if(!checkNumber(goods_number,1000000,'数量')){
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
		}
		return true;
	}
	/*
	 *校验业务类型
	 */
	function checkBusinessType(){
		if($("#isadditionflag_id").val()!=0){
			var collection_amount = $("#collection_amount_id");
			var packing_amount = $("#packing_amount_id");
			var insured_amount = $("#insured_amount_id");
			var insured_cost = $("#insured_cost_id");
			//如果保价声明价值可以填写，那么就悬着了保价，那么保价声明价值需要校验
			if($("#collection_amount_id").attr("readonly") != 'readonly'){
				//校验代收货款金额是否为空
		        if(!nullValidater(collection_amount,"代收货款金额")){
		        	confirmFunction("代收货款金额未填写");
		        }else
		      	//校验代收货款金额是否超过大小
		        if(!checkNumber(collection_amount,1000000,'代收货款金额')){
		        	return false;
		        }else
		      	//代收货款是否大于0
		      	if(collection_amount.val() < 0){
		      		$.messager.alert("提示", "代收货款应大于0", "warning");
		      		return false;
		      	}
			}
			//校验保证费用是否为大于0的数
			if($.trim(packing_amount.val()) != "" && !NonNegativeValidater(packing_amount,'包装费用')){
				return false;
			}
			
			if($("#insured_amount_id").attr("readonly") != 'readonly'){
				//校验价声明价值是否为空
		        if(!nullValidater(insured_amount,"保价声明价值")){
		        	confirmFunction("保价声明价值未填写");
		        }else
		      	//校验价声明价值是否超过大小
		        if(!checkNumber(insured_amount,1000000,'保价声明价值')){
		        	return false;
		        }
			}
			//校验保价费用是否为大于0的数
			if($.trim(insured_cost.val()) != "" && !NonNegativeValidater(insured_cost,'保价费用')){
				return false;
			}
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
        	confirmFunction("件数未填写");
        }else
		//校验件数是否为0或正整数
		
        if(!numberValidater(number,'件数')){
        	return false;
        }else
		//校验件数是否超过大小
        if(!checkNumber(number,1000000,'件数')){
        	return false;
        }
      	//校验计费重量是否为空
        if(!nullValidater(charge_weight,"计费重量")){
        	confirmFunction("计费重量未填写");
        }else
		//校验计费重量是否为大于0的数
        if(!NonNegativeValidater(charge_weight,'计费重量')){
        	return false;
        }else
		//校验计费重量是否超过大小
        if(!checkNumber(charge_weight,1000000,'计费重量')){
        	return false;
        }
		
      	//校验实际重量是否为空
        if(!nullValidater(actual_weight,"实际重量")){
        	confirmFunction("实际重量未填写");
        }else
		//校验实际重量是否为大于0的数
        if(!NonNegativeValidater(actual_weight,'计费重量')){
        	return false;
        }else
		//校验实际重量是否超过大小
        if(!checkNumber(actual_weight,1000000,'计费重量')){
        	return false;
        }
      	//校验运费是否为空
        if(!nullValidater(freight,"运费")){
        	confirmFunction("运费未填写");
        }else
      	//校验运费是否为非负数
        if(!NonNegativeValidater(freight,'运费')){
        	return false;
        }else
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
		/* var origin_adress = $("#origin_adress_id");
		var destination = $("#destination_id");
		//校验始发地是否为空
	 	if(!nullValidater(origin_adress,"始发地")){
	 		confirmFunction("始发地未填写");
	    }else
		//校验始发地是否超过长度
		if(!checkLength(origin_adress, 10, '始发点', 10)){
        	return false;
    	}
		//校验目的地是否为空
		if( $.trim(destination.val()) || !checkLength(destination, 10, '目的地', 10)){
			confirmFunction("目的地未填写");
    	} 
		return true; */
	}
	/*
	 *付款方式
	 */
	function checkPayMethod(){
		var payment_method = $("input[name='payment_method']:checked").val();
		console.log(payment_method);
		if(!payment_method){
			payment_method = "";
			confirmFunction("结算方式未填写");
		}else if(payment_method == 0 || payment_method == 3){
			//校验月结账号是否为空
			var monthly_account_number = $("#monthly_account_number_id");
	        if(!nullValidater(monthly_account_number,"月结账号")){
	        	confirmFunction("月结账号未填写");
	        }
	      	/* //校验月结账号是否为0或正整数
	        if(!numberValidater(monthly_account_number,'月结账号')){
	        	return false;
	        } */
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
		msg = msg + "只能由字母和数字组成";
		return formatRegValidater(el, msg, regExp);
	}
	
	/*
	 * 校验输入内容只能为数字的方法
	 */
	function numberOnlyValidater(el, msg){
		var regExp = "/^[0-9]*$/";
		msg = msg + "只能由数字组成";
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
		var regExp = "/^(0|[1-9]\\d*|\\d+\\.\\d+)$/";
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
			//$.messager.alert("提示", msg, "warning");
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
			//$.messager.alert("提示", msg, "warning");
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
		msg = msg + "电话格式不符合要求,格式为：区号是3位或4位,总长度为[12或13]位,区号后需加'-'";
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
	 *省份改变时，刷新市 已弃用
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
	 *市改变时，刷新区/县 已弃用
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
	 *区/县改变时，刷新街道 已弃用
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
		if(!numberOrLetterValidater($("#orderNo_id"),"运单号")){
			$("#orderNo_id").val("");
			return ;
		}
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
					if(typeof(data.embracedOrderVO)  == "undefined" || data.embracedOrderVO.orderNo == ""){						
						    $("#isadditionflag_id").attr("value",0);
				        	//根据运单号，去订单表查数据，带出小件员
				        	var orderNo = $.trim($("#orderNo_id").val());
				    		$.ajax({
				    			type : "POST",
				    			url : "<%=request.getContextPath()%>/embracedOrderInputController/getDeliveryManByOrderNo",
				    			dataType : "json",
				    			data:{
				    				"orderNo":orderNo
				    			},
				    			success : function(data) {
				    				var delivermanId = data.delivermanId;
				    				var delivermanIdSelect = $("#delivermanId_show");
				    				var delivermanIdSelectOptions = $("#delivermanId_show option");
				    				var flag = true;
				    				if(delivermanId != ""){
				    					for(var i = 0; i < delivermanIdSelectOptions.size(); i++){
				    					    if(delivermanIdSelectOptions[i].value == delivermanId) {
				    					    	delivermanIdSelect.val(delivermanId);
				    							delivermanIdSelect.css('background','#EBEBE4');
				    							delivermanIdSelect.attr("readonly",true);
				    							flag = false;
				    							break;
				    					    }
				    					  }
				    					if(flag){
				    						//alert("该运单号的小件员不属于本站点");
				    					}
				    				}else{
				    					delivermanIdSelect.css('background','#ffffff');
				    					delivermanIdSelect.attr("readonly",false);
				    				}
				    			}
				    		});
							if (data.isRepeatTranscwb) {
								$.messager.alert("提示", "该运单号与系统订单/运单重复", "warning");
								$("#orderNo_id").attr("value","");
							}
				        	return;
					}else if(data.embracedOrderVO.isadditionflag == 1){
						$.messager.alert("提示", "该运单号已经补录完成", "warning");
						$("#orderNo_id").attr("value","");
						return;
					}else if(data.embracedOrderVO.instationid != data.branchid){
						$.messager.alert("提示", "该运单号已经在其他站点入站", "warning");
						$("#orderNo_id").attr("value","");
						return;
					}
					
					$("#isadditionflag_id").attr("value",1);
					$("#sender_certificateNo_id").val(data.embracedOrderVO.sender_certificateNo);
					
					$("#sender_name_id").val(data.embracedOrderVO.sender_name);
					// 根据发件人的 $("#sender_customerid")，确定单位名称和companycode,完成
					$("#sender_companyName_id").find("option[id='"+data.embracedOrderVO.sender_customerid+"']").attr("selected",true);
					$("#sender_companyName_id").change();
					//  把寄件人的省市区街道给初始化上
					$("#BranchprovinceId").html(data.embracedOrderVO.sender_provinceid);
					$("#BranchcityId").html( data.embracedOrderVO.sender_cityid);
					$("#BranchcountyId").html( data.embracedOrderVO.sender_countyid);
					$("#sender_townId").html( data.embracedOrderVO.sender_townid); 
					$("#receive_provinceId").html(data.embracedOrderVO.consignee_provinceid);
					$("#receive_cityId").html(data.embracedOrderVO.consignee_cityid);
					$("#receive_countyId").html(data.embracedOrderVO.consignee_countyid);
					$("#receive_townId").html(data.embracedOrderVO.consignee_townid); 
					 initArea(); 
					 if(data.reserveOrder!=undefined){
						 $("#reserveOrderNo").val(data.reserveOrder.reserveOrderNo);
							$("#recordVersion").val(data.reserveOrder.recordVersion);  
					 }
					
					$("#sender_adress_id").val(data.embracedOrderVO.sender_adress);
					$("#sender_cellphone_id").val(data.embracedOrderVO.sender_cellphone);
					$("#sender_telephone_id").val(data.embracedOrderVO.sender_telephone);
					
					$("#consignee_name_id").val(data.embracedOrderVO.consignee_name);
					$("#consignee_companyName_id").find("option[id='"+data.embracedOrderVO.consignee_customerid+"']").attr("selected",true);
					$("#consignee_companyName_id").change();
					$("#consignee_adress_id").val(data.embracedOrderVO.consignee_adress);
					$("#consignee_cellphone_id").val(data.embracedOrderVO.consignee_cellphone);
					$("#consignee_telephone_id").val(data.embracedOrderVO.consignee_telephone);
					
					$("#goods_name_id").val(data.embracedOrderVO.goods_name);
					$("#goods_number_id").val(data.embracedOrderVO.goods_number);
					$("#goods_weight_id").val(data.embracedOrderVO.goods_weight);
					$("#charge_weight_id").val(data.embracedOrderVO.charge_weight);
					$("#actual_weight_id").val(data.embracedOrderVO.actual_weight);
					if(typeof(data.expressWeigh)  != "undefined" && data.expressWeigh.id != ""){
						$("#actual_weight_id").val(data.expressWeigh.weight);
					}
					$("#goods_other_id").val(data.embracedOrderVO.goods_other);
					$("#goods_longth_id").val(data.embracedOrderVO.goods_longth);
					$("#goods_longth_id").blur();
					$("#goods_width_id").val(data.embracedOrderVO.goods_width);
					$("#goods_width_id").blur();
					$("#goods_height_id").val(data.embracedOrderVO.goods_height);
					$("#goods_height_id").blur();
					
					$("#freight_total_id").val(data.embracedOrderVO.freight_total); 
					if(data.embracedOrderVO.freight != null && data.embracedOrderVO.freight != "0.00"){
						$("#freight_id").val(data.embracedOrderVO.freight);
						$("#freight_id").css('background','#EBEBE4');
						$("#freight_id").attr("readonly","readonly");
					}
					$("#inputdatetime_id").val(data.embracedOrderVO.inputdatetime);
					//把是否代收货款给选上 $("#collection_show").val(data.embracedOrderVO.collection);完成
					if(data.embracedOrderVO.collection == 1){
						$("[name='collection_show']").attr("checked",'true');
						$('#collection_show').change();
						if(data.embracedOrderVO.collection_amount == "" ){
							$("#collection_amount_id").val("");
						}else{
							$("#collection_amount_id").val(data.embracedOrderVO.collection_amount);
						}
					}
					//把是否保价给选上 $("#insured_show").val(data.embracedOrderVO.insured);完成
					if(data.embracedOrderVO.insured == 1){
						$("[name='insured_show']").attr("checked",'true');
						$('#insured_show').change();
						if(data.embracedOrderVO.insured_amount == ""){
							$("#insured_amount_id").val("");
						}else{
							$("#insured_amount_id").val(data.embracedOrderVO.insured_amount);
						}
					}
					if(data.embracedOrderVO.insured_cost == "" ){
						$("#insured_cost_id").val("");
					}else{
						$("#insured_cost_id").val(data.embracedOrderVO.insured_cost);
					}
					$("#insured_cost_id").attr("readonly","readonly");
					$("#insured_cost_id").css('background','#EBEBE4');					
					if(data.embracedOrderVO.packing_amount == "" ){
						$("#packing_amount_id").val("");
					}else{
						$("#packing_amount_id").val(data.embracedOrderVO.packing_amount);
					}
					$("#packing_amount_id").attr("readonly","readonly");
					$("#packing_amount_id").css('background','#EBEBE4');
					//把付款方式给选上 $("#payment_method_id").val(); 完成
					if(data.embracedOrderVO.payment_method == 0){
						$("#Radio3").attr("checked","checked");
						$(".RadioClass").change();
						$("#monthly_account_number_id").val(data.embracedOrderVO.monthly_account_number);
						$("#Radio1").attr("disabled","disabled");
						$("#Radio2").attr("disabled","disabled");
						$("#Radio4").attr("disabled","disabled");
					}else if(data.embracedOrderVO.payment_method == 1){
						$("#Radio1").attr("checked","checked");
						$(".RadioClass").change();
						$("#Radio3").attr("disabled","disabled");
						$("#Radio2").attr("disabled","disabled");
						$("#Radio4").attr("disabled","disabled");
					}else if(data.embracedOrderVO.payment_method == 2){
						$("#Radio2").attr("checked","checked");
						$(".RadioClass").change();
						$("#Radio1").attr("disabled","disabled");
						$("#Radio3").attr("disabled","disabled");
						$("#Radio4").attr("disabled","disabled");
					}else if(data.embracedOrderVO.payment_method == 3){
						$("#Radio4").attr("checked","checked");
						$("#monthly_account_number_id").val(data.embracedOrderVO.monthly_account_number);
						$(".RadioClass").change();
						$("#Radio1").attr("disabled","disabled");
						$("#Radio2").attr("disabled","disabled");
						$("#Radio3").attr("disabled","disabled");
					}
					$("#remarks_id").val(data.embracedOrderVO.remarks);
					
					
					var delivermanId = data.embracedOrderVO.delivermanId;
    				var delivermanIdSelect = $("#delivermanId_show");
    				var delivermanIdSelectOptions = $("#delivermanId_show option");
    				var flag = true;
    				if(delivermanId != ""){
    					for(var i = 0; i < delivermanIdSelectOptions.size(); i++){
    					    if(delivermanIdSelectOptions[i].value == delivermanId) {
    					    	delivermanIdSelect.val(delivermanId);
    							delivermanIdSelect.css('background','#EBEBE4');
    							delivermanIdSelect.attr("disabled","disabled");
    							flag = false;
    							break;
    					    }
    					  }
    					 if(flag && delivermanId != 0){
    						alert("该运单号的小件员不属于本站点");
    						$("#orderNo_id").attr("value","");
    					} 
    				}else{
    					delivermanIdSelect.css('background','#ffffff');
    					delivermanIdSelect.attr("readonly",false);
    				}
    				//图片按钮
    				if(data.expressImage == null){
    					$("#addImg").hide();
    				}else{
    					
    					$("#addImg").show();
    					var divshow = document.getElementById("img");
    					//获取指定ID的DOM对象
    					 var childnode=divshow.firstChild;
    					if(childnode!=null && childnode!=undefined){
    						//获取被删除的节点
       					   divshow.removeChild(childnode);
    					}
    					
    					var img = document.createElement("img");
    					img.src = data.expressImage
    				    //img.src = "http://10.199.195.25:8080/express/file/bookingOrder?date=2016-06-01&fileName=89890610006.jpg";
    				    divshow.appendChild(img);
    				}
    				
    				
    				//快递二期增加字段
    				/* selected(data.embracedOrderVO.express_product_type); */
    				
    				$("#express_product_type").val(data.embracedOrderVO.express_product_type);
    				$("#paywayid").val(data.embracedOrderVO.paywayid);
    				$("#paywayid").css('background','#EBEBE4')
    				$("#paywayid").attr("disabled","disabled");
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
		object.val(number.toFixed(2));
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
					getFeeByCondition();
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
		 getFreightToal()
	}
	
	function getFreightToal(){
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
		if(obj.id.indexOf("payment") != -1){
			
		}
	}
	function companyChange(obj){
		if(obj.id.indexOf("sender") >= 0){
			$("#sender_No_id").val($("#sender_companyName_id  option:selected").attr("code"));
			//如果是月结或者第三方支付，那么把寄件人公司的编码赋值给月结账号
			if($("input[name='payment_method']:checked").val() == 0 || $("input[name='payment_method']:checked").val() == 3){
				$("#monthly_account_number_id").val($("#sender_companyName_id  option:selected").attr("code"));
			}
			
			
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
	 *导出所有未补录运单事件
	 */
	 function exportAllExtraceOrder(){
// 		alert("我爱你");
		$('#exportExtraOrderForm').submit();	
		
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
		if(!NonNegativeValidater($("#"+obj.id),'内容')){
			$("#"+obj.id).val("");
	    	return;
	    }
		var obj_val = $("#"+obj.id).val();
		var number = new Number(obj_val);
		$("#"+obj.id).val(number.toFixed(x));
		if(obj.id=="actual_weight_id"){
			getFeeByCondition();
		}
		if(obj.id=="freight_id"){
			calculateFreight(obj);
		}
	}
	
	//uat后新加
	/*
	 *根据复选框内容，确定是否报价/是否代收货款是否可填，并给给其对应的数据库赋值
	 */
	function checkboxChange(obj){
		var name = obj.id.split("_")[0];
		if(obj.value==1){
			$("#"+name+"_id").val(1);
		}else{
			$("#"+name+"_id").val(0);
		}
		if(obj.id.indexOf("insured") != -1){
			if(!obj.checked){
				$("#insured_amount_id").css('background','#EBEBE4');
				$("#insured_amount_id").attr("readonly",true);
				$("#insured_amount_id").val("");
			}else{
				$("#insured_amount_id").css('background','#ffffff');
				$("#insured_amount_id").attr("readonly",false);
			}
		}else if(obj.id.indexOf("collection") != -1){
			if(!obj.checked){
				$("#collection_amount_id").css('background','#EBEBE4');
				$("#collection_amount_id").attr("readonly",true);
				$("#collection_amount_id").val("");
			}else{
				$("#collection_amount_id").css('background','#ffffff');
				$("#collection_amount_id").attr("readonly",false);
			}
		}
	}
	
	
	
	$(function() {
		$("#importButton").click(function(){	
			if($("#txtFileName").val() == ""){
				alert("请先选择文件");
				return ;		
			}
			$("#importButton").attr("disabled","disabled");
			$("#importButton").val("正在上传");
			/* $("#stop").removeAttr("disabled");  */
			//$('#swfupload-control').swfupload('addPostParam','number',1);可以给后台传参数
			$('#swfupload-control').swfupload('startUpload');
		});
		var swfurl = $("#import_path").val()+';jsessionid=<%=session.getId() %>';
		$('#swfupload-control').swfupload({
			upload_url: swfurl,
			file_size_limit : "102400",
			file_types : "*.xlsx;*.xls",
			file_types_description : "All Files",
			file_upload_limit : "0",
			file_queue_limit : "1",
			flash_url : "<%=request.getContextPath()%>/js/swfupload/swfupload.swf",
			button_image_url: "<%=request.getContextPath()%>/images/indexbg.png",
			button_text : '选择文件',
			button_width : 50,
			button_height : 20,
			button_placeholder : $('#button')[0]
		}).bind('fileQueued', function(event, file) {
			$("#txtFileName").val(file.name);
			file_id = $('#swfupload-control');
		}).bind('fileQueueError', function(event, file, errorCode, message) {
			
		}).bind('fileDialogStart', function(event) {
			$(this).swfupload('cancelQueue');
		}).bind('fileDialogComplete',
				function(event, numFilesSelected, numFilesQueued) {
		}).bind('uploadStart', function(event, file) {
		}).bind('uploadError',function(fileobject, errorcode, message){
			$("#importButton").removeAttr("disabled"); 
			/* $("#stop").attr("disabled","disabled"); */
			$("#txtFileName").val("");
			alert("excel数据格式异常，请将单元格设置为文本格式");
		}).bind('uploadProgress',function(event, file, bytesLoaded, bytesTotal) {
		}).bind('uploadSuccess', function(event, file, serverData) {
			$("#importButton").val("正在导入");
			//alert("测试-！"+serverData);					
			function queryProgress() {
				$.ajax({
					type: "POST",
					url : "<%=request.getContextPath()%>/result/EmbracedResult/" + serverData.split(",")[0],
					success : function(data) {
						document.getElementById('importInfo').style.display = "";
						$("#anyid").val(data.id);
						/* $("#successCount").html(data.successList.length);
						$("#failureCount").html(data.failList.length);
						importDate = data.analysisList; */
						if (!data.finished) {
							setTimeout(function(){queryProgress();}, 100);
						}else{
							//$("#importButton").removeAttr("disabled"); 
							//$("#importButton").val("开始导入"); 
							/* if(!$("#stop").attr("disabled")){
								alert($("#txtFileName").val()+"导入完成");
							}
							$("#stop").attr("disabled","disabled"); 
							*/
							//$("#txtFileName").val("");
							if(typeof(data.resultErrMsg) != "undefined" && data.resultErrMsg != null){
								alert(data.resultErrMsg);
							}
							if(typeof(data.successList) == "undefined" || data.successList == null){
								$("#successCount").html(0);
							}else{
								$("#successCount").html(data.successList.length);
							}
							if(typeof(data.failList) == "undefined" || data.failList == null){
								$("#failureCount").html(0);
							}else{
								$("#failureCount").html(data.failList.length);
								var table = $("#importInfoTable"); 
								for(var i = 0; i < data.failList.length; i ++){  	 
									var row = $("<tr></tr>");
																					
									var td1 = $("<td></td>");
									td1.html(data.failList[i].orderNo);
									row.append(td1);
									table.append(row);
									
									var td2 = $("<td></td>");
									td2.html(data.failList[i].errMsg);
									row.append(td2);
									table.append(row);
								}
							}
							importDateid = data.id;
						}
					}
				});
			}  
			setTimeout(function(){queryProgress();}, 100);
		}).bind('uploadComplete', function(event, file) {
			$(this).swfupload('startUpload');
		}).bind('uploadError', function(event, file, errorCode, message) {
		});
		
	   //表格收缩与展开
	   $('tr.parent').click(function(){   // 获取所谓的父行
		    $(this)
		    .toggleClass("selected")   // 添加/删除高亮  
		    .siblings('.child_'+this.id).toggle();  // 隐藏/显示所谓的子行
	   }).click();
       
	});
	
	//edit by zhouhuan 图片展示优化  2016-08-15
	 var selectImage;
	 function openwin(){
		 var imgW = document.getElementById("img").firstChild.width;
		 var imgH = document.getElementById("img").firstChild.height;
		 var maxW = 600;
		 var maxH = 600;

		 if (imgW > maxW || imgH > maxH) {
			var imgB = imgW / imgH;
			var maxB = maxW / maxH;
			if (imgB > maxB) {
				imgW = maxW;
				imgH = parseInt(imgW / imgB);
			} else {
				imgH = maxH;
				imgW = parseInt(imgH * imgB);
			}
			document.getElementById("img").firstChild.width=imgW;
			document.getElementById("img").firstChild.height=imgH;
		 }

		 //图片显示
         selectImage = $.layer({
         	  type: 1,
              title: '图片展示',
              shadeClose: true,
              maxmin: false,
              fix: false,
              shade: 0,
              area: [imgW,imgH+35],
              page: {
                  dom: '#img'
              } 
         }); 
	 }
	function confirmImport(){
		if($("#successCount").html() == 0){
			alert("没有可导入的数据！");
			return;
		}
		$("#importConfirm").attr("disabled",true);
		$("#importCancel").attr("disabled",true);
		$.ajax({
			type : "POST",
			url : $("#import_save_path").val(),
			dataType : "json",
			async: true,
			data:{	
				"id" : importDateid
			},
			success : function(data) {
				if(data.flag){
					alert("保存成功！");
					$('#importBox').dialog('close');
					initImportBox();
				}else{
					alert("保存失败！");
				}
			}
		}); 
	}
	function cancelImport(){
		$('#importBox').dialog('close');
		initImportBox();
		document.getElementById('importInfo').style.dispaly = "none";
	}
	function initImportBox(){
		$("#importButton").removeAttr("disabled"); 
		$("#importConfirm").removeAttr("disabled"); 
		$("#importCancel").removeAttr("disabled"); 
		$("#importButton").val("开始导入"); 
		$("#txtFileName").val("");
		$("#successCount").html(0);
		$("#failureCount").html(0);
		for(var i = $("#importInfoTable tr").length; i > 1; i--){
			$("#importInfoTable tr").eq(i-1).remove(); 
		}
		document.getElementById('importInfo').style.display = "none";
	}

	function downloadExcel(){
		$("#downloada").click(function(){
			var href=$("#downloada").attr("href");
			window.location.href=href;
			return false;		
		});
		
		var $a=$("#downloada");
		$a.trigger("click");		
	}
	function confirmFunction(msg){
		 /*  $.messager.confirm("确认", msg + ",确认保存？", function (r) {  
		        if (r) {  
		        	return true;
		        } 
		    });   */
		  // return  confirm(msg + ",确认保存？");
		msgArr.push(msg);
	}
	function read(){
		
		getWeightRepeatable();
	}
	
	/*
	 *电子称数据的读取需要的两个方法
	 */
	function getWeightRepeatable() {
		window.setInterval("setWeight()", 1);
		$("#isRead").val("1");
	}
	function setWeight() {
		var weight = window.parent.document.getElementById("scaleApplet").getWeight();
		if (weight != null && weight != '') {
			$("#actual_weight_id").val(weight);
		}
	}
	
	//根据寄件人手机号码获取对应的运单号
	function getReserveOrderBySenderPhone(phone,flag){
		var sender_phone = phone;
		var reserveOrderTableRows = document.getElementById("reserveOrderTable").rows.length-2;
		if(reserveOrderTableRows>0){
			for(var i=0;i<reserveOrderTableRows;i++){
				document.getElementById("reserveOrderTable").deleteRow(2);
			}
		}
		var senderHistoryTableRows = document.getElementById("senderHistoryTable").rows.length-2;
		if(senderHistoryTableRows>0){
			for(var i=0;i<senderHistoryTableRows;i++){
				document.getElementById("senderHistoryTable").deleteRow(2);
			}
		}
		if(phone.trim()==""){
			return;
		}
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/express2/reserveOrder/getReserveOrderBySenderPhone",
			dataType : "json",
			async: true,
			data:{	
				senderPhone : sender_phone,
				phoneFlag : flag
			},
			success : function(data) {
				if(data.reserveOrderList.length!=0){
					for(var i=0;i<data.reserveOrderList.length;i++){
						var reserveOrder=data.reserveOrderList[i];
						var requireTime = FormatDate(reserveOrder.requireTime);
						$('#reserveOrderTable').append('<tr class="child_reserveOrderShow" ondblclick="reserveTableDblClick(this)"><td>'+reserveOrder.reserveOrderNo+'</td><td>'+reserveOrder.cnorName+'</td>'
						+'<td>'+reserveOrder.cnorAddr+'</td><td>'+requireTime+'</td><td style="display:none">'+reserveOrder.recordVersion+'</td>'
						+'<td style="display:none">'+reserveOrder.cnorProvName+'</td><td style="display:none">'+reserveOrder.cnorProvCode+'</td>'
						+'<td style="display:none">'+reserveOrder.cnorCityName+'</td><td style="display:none">'+reserveOrder.cnorCityCode+'</td>'
						+'<td style="display:none">'+reserveOrder.cnorRegionName+'</td><td style="display:none">'+reserveOrder.cnorRegionCode+'</td>'
						+'<td style="display:none">'+reserveOrder.cnorTownName+'</td><td style="display:none">'+reserveOrder.cnorTownCode+'</td>'
						+'<td style="display:none">'+reserveOrder.cnorTel+'</td><td style="display:none">'+reserveOrder.cnorMobile+'</td></tr>');
					}
				}
				if(data.orderList.length!=0){
					for(var i=0;i<data.orderList.length;i++){
						var order=data.orderList[i];
						//$('#senderHistoryTable').append('<tr class="child_senderHistory"><td>'+order.senderprovince+'</td><td>'+order.sendercity+'</td><td>'+order.sendercounty+'</td><td>'+order.senderstreet+'</td><td>'+order.senderaddress+'</td></tr>');
						$('#senderHistoryTable').append('<tr class="child_senderHistory" ondblclick="senderHistoryTableDblClick(this)">'
								+'<td>'+order.sendername+'</td><td>'+order.senderprovince+'</td>'
								+'<td>'+order.sendercity+'</td><td>'+order.sendercounty+'</td>'
								+'<td>'+order.senderstreet+'</td><td>'+order.senderaddress+'</td>'
						        +'<td style="display:none">'+order.senderprovinceid+'</td>'
						        +'<td style="display:none">'+order.sendercityid+'</td>'
						        +'<td style="display:none">'+order.sendercountyid+'</td>'
						        +'<td style="display:none">'+order.senderstreetid+'</td>'
						        +'<td style="display:none">'+order.sendertelephone+'</td>'
						        +'<td style="display:none">'+order.sendercellphone+'</td></tr>'); 
					}
				}
			}
		}); 
	}
	
	//根据收件人手机号码获取对应的运单号
	function getReserveOrderByConsignPhone(phone,flag){
		var consign_phone = phone;
		var consigneeHistoryTableRows = document.getElementById("consigneeHistoryTable").rows.length-2;
		if(consigneeHistoryTableRows>0){
			for(var i=0;i<consigneeHistoryTableRows;i++){
				document.getElementById("consigneeHistoryTable").deleteRow(2);
			}
		}	
		if(phone.trim()==""){
			return;
		} 
		$.ajax({
			type : "POST",
			url : "<%=request.getContextPath()%>/express2/reserveOrder/getReserveOrderByConsignPhone",
			dataType : "json",
			async: true,
			data:{	
				consignPhone : consign_phone,
				phoneFlag : flag
			},
			success : function(data) {
				for(var i=0;i<data.length;i++){
					//$('#consigneeHistoryTable').append("<tr class='child_consigneeHistory' ondblclick='consigneeHistoryTableDblClick('+data[i].cwbprovince+')><td>'+data[i].consigneename+'</td><td>'+data[i].cwbprovince+'</td><td>'+data[i].cwbcity+'</td><td>'+data[i].cwbcounty+'</td><td>'+data[i].recstreet+'</td><td>'+data[i].consigneeaddress+'</td><td style="display:none">'+data[i].opscwbid+'</td></tr>'");
					$('#consigneeHistoryTable').append('<tr class="child_consigneeHistory" ondblclick="consigneeHistoryTableDblClick(this)">'
							+'<td>'+data[i].consigneename+'</td><td>'+data[i].cwbprovince+'</td>'
							+'<td>'+data[i].cwbcity+'</td><td>'+data[i].cwbcounty+'</td>'
							+'<td>'+data[i].recstreet+'</td><td>'+data[i].consigneeaddress+'</td>'
					+'<td style="display:none">'+data[i].recprovinceid+'</td><td style="display:none">'+data[i].reccityid+'</td>'
					+'<td style="display:none">'+data[i].reccountyid+'</td><td style="display:none">'+data[i].recstreetid+'</td>'
					+'<td style="display:none">'+data[i].consigneephone+'</td><td style="display:none">'+data[i].consigneemobile+'</td></tr>'); 
				}
			}
		}); 
	};
	
	/*****************edit by 周欢  修改双击tabel内容填充异常  2016-7-15 *******************/
	//收件人历史信息双击事件    
	function consigneeHistoryTableDblClick(consignee){
		var sender_adress = $("#sender_adress_id").val();
		var consigneeCellphone = $(consignee).find("td").eq(11).text();
		var consigneeTellphone = $(consignee).find("td").eq(10).text();
		var consigneeName = $(consignee).find("td").eq(0).text();
		$("#consignee_adress_id").val("");
		$("#area_consignee").val("");
		$("#receive_provinceId").html($(consignee).find("td").eq(6).text());
		$("#receive_cityId").html($(consignee).find("td").eq(7).text());
		$("#receive_countyId").html($(consignee).find("td").eq(8).text());
		$("#receive_townId").html($(consignee).find("td").eq(9).text()); 
		initArea(); 
		$("#consignee_provinceid_id").val($(consignee).find("td").eq(6).text());
		$("#consignee_provinceName_id").val($(consignee).find("td").eq(1).text());
		$("#consignee_cityid_id").val($(consignee).find("td").eq(7).text());
		$("#consignee_cityName_id").val($(consignee).find("td").eq(2).text());
		$("#consignee_countyid_id").val($(consignee).find("td").eq(8).text());
		$("#consignee_countyName_id").val($(consignee).find("td").eq(3).text());
		$("#consignee_townid_id").val($(consignee).find("td").eq(9).text());
		$("#consignee_townName_id").val($(consignee).find("td").eq(4).text());
		$("#consignee_adress_id").val($(consignee).find("td").eq(5).text());
		
		if(consigneeName != "null"){
			$("#consignee_name_id").val(consigneeName);
		}else{
			$("#consignee_name_id").val("");
		}
		if(consigneeCellphone != "null"){
			$("#consignee_cellphone_id").val(consigneeCellphone);
		}else{
			$("#consignee_cellphone_id").val("");
		}
		if(consigneeTellphone != "null"){
			$("#consignee_telephone_id").val(consigneeTellphone);
		}else{
			$("#consignee_telephone_id").val("");
		}
		//$("#consignee_name_id").val($(consignee).find("td").eq(0).text());
		
		$("#sender_adress_id").val(sender_adress);
	}
	
	/*****************edit by 周欢  修改双击tabel内容填充异常  2016-7-15 *******************/
	//寄件人历史信息双击事件
	function senderHistoryTableDblClick(sender){
		var consignee_adress = $("#consignee_adress_id").val();
		var senderTelephone = $(sender).find("td").eq(10).text();
		var senderCellphone = $(sender).find("td").eq(11).text();
		var senderName = $(sender).find("td").eq(0).text();
		$("#area_sender").val("")
		$("#sender_adress_id").val("");
		$("#BranchprovinceId").html($(sender).find("td").eq(6).text());
		$("#BranchcityId").html($(sender).find("td").eq(7).text());
		$("#BranchcountyId").html($(sender).find("td").eq(8).text());
		$("#sender_townId").html($(sender).find("td").eq(9).text()); 
		initArea(); 
		$("#sender_provinceid_id").val($(sender).find("td").eq(6).text());
		$("#sender_provinceName_id").val($(sender).find("td").eq(1).text());
		$("#sender_cityid_id").val($(sender).find("td").eq(7).text());
		$("#sender_cityName_id").val($(sender).find("td").eq(2).text());
		$("#sender_countyid_id").val($(sender).find("td").eq(8).text());
		$("#sender_countyName_id").val($(sender).find("td").eq(3).text());
		$("#sender_townid_id").val($(sender).find("td").eq(9).text());
		$("#sender_townName_id").val($(sender).find("td").eq(4).text());
		$("#sender_adress_id").val($(sender).find("td").eq(5).text());
		if(senderName != "null"){
			$("#sender_name_id").val(senderName);
		}else{
			$("#sender_name_id").val("");
		}
		if(senderCellphone != "null"){
			$("#sender_cellphone_id").val(senderCellphone);
		}else{
			$("#sender_cellphone_id").val("");
		}
		if(senderTelephone != "null"){
			$("#sender_telephone_id").val(senderTelephone);
		}else{
			$("#sender_telephone_id").val("");
		}
		$("#consignee_adress_id").val(consignee_adress);
	}
	
	/*****************edit by 周欢  修改双击预约单tabel内容填充异常  2016-7-18 *******************/
	//预约单信息双击事件
	function reserveTableDblClick(reserve){
		var consignee_adress = $("#consignee_adress_id").val();
		var senderTelephone = $(reserve).find("td").eq(13).text();
		var senderCellphone = $(reserve).find("td").eq(14).text();
		var senderName = $(reserve).find("td").eq(1).text()
		$("#area_sender").val("")
		$("#sender_adress_id").val("");
		$("#reserveOrderNo").val($(reserve).find("td").eq(0).text());
		$("#recordVersion").val($(reserve).find("td").eq(4).text());
		$("#BranchprovinceId").html($(reserve).find("td").eq(6).text());
		$("#BranchcityId").html($(reserve).find("td").eq(8).text());
		$("#BranchcountyId").html($(reserve).find("td").eq(10).text());
		$("#sender_townId").html($(reserve).find("td").eq(12).text()); 
		initArea(); 
		$("#sender_provinceid_id").val($(reserve).find("td").eq(6).text());
		$("#sender_provinceName_id").val($(reserve).find("td").eq(5).text());
		$("#sender_cityid_id").val($(reserve).find("td").eq(8).text());
		$("#sender_cityName_id").val($(reserve).find("td").eq(7).text());
		$("#sender_countyid_id").val($(reserve).find("td").eq(10).text());
		$("#sender_countyName_id").val($(reserve).find("td").eq(9).text());
		$("#sender_townid_id").val($(reserve).find("td").eq(12).text());
		$("#sender_townName_id").val($(reserve).find("td").eq(11).text());
		$("#sender_adress_id").val($(reserve).find("td").eq(2).text());
		if(senderName != "null"){
			$("#sender_name_id").val(senderName);
		}else{
			$("#sender_name_id").val("");
		}
		if(senderCellphone != "null"  && senderCellphone.trim() !="-"){
			$("#sender_cellphone_id").val(senderCellphone);
		}else{
			$("#sender_cellphone_id").val("");
		}
		if(senderTelephone != "null" && senderTelephone.trim() !="-"){
			$("#sender_telephone_id").val(senderTelephone);
		}else{
			$("#sender_telephone_id").val("");
		}
		//$("#consignee_adress_id").val(consignee_adress);
	}
	
	function getFeeByCondition(){
		if ($("#isadditionflag_id").val() == 0){
			var productType = $.trim($("#express_product_type").val());
			var actualWeight = $.trim($("#actual_weight_id").val());
			var goodsLongth = $.trim($("#goods_longth_id").val());
			var goodsWidth = $.trim($("#goods_width_id").val());
			var goodsHeight = $.trim($("#goods_height_id").val());
			var senderProvince = $.trim($("#sender_provinceName_id").val());
			var senderCity = $.trim($("#sender_cityName_id").val());
			var consigneeProvince = $.trim($("#consignee_provinceName_id").val());
			var consigneeCity = $.trim($("#consignee_cityName_id").val());
			var payMethod = $.trim($("input[name='payment_method']:checked").val());
			//始发省份名称不能为空
			if(senderProvince==""){
				return;
			}
			//始发城市名称不能为空
			if(senderCity==""){
				return;
			}
			//目的省份名称不能为空
			if(consigneeProvince==""){
				return;
			}
			//目的城市名称不能为空
			if(consigneeCity==""){
				return;
			}
			//服务产品编码不能为空
			if(productType==""){
				return;
			}
			//支付类型不能为空，且只能为现金和到付
			if(payMethod=="" || payMethod==0 || payMethod==3){
				return;
			}
			//重量和体积二者必填其一
			if((actualWeight==""||actualWeight<=0)&&($("#goods_kgs_id").val()==""||($("#goods_kgs_id").val()<=0))){
				return;
			}
			$("#freight_id").val("");
			$("#charge_weight_id").val("");
			$.ajax({
				type : "POST",
				url : "<%=request.getContextPath()%>/express2/reserveOrder/getFeeByCondition",
				dataType : "json",
				async: false,
				data:{
					"productType":productType,
				    "actualWeight":actualWeight,
				    "goodsLongth":goodsLongth,
				    "goodsWidth":goodsWidth,
				    "goodsHeight":goodsHeight,
				    "senderProvince":senderProvince,
				    "senderCity":senderCity, 
				    "consigneeProvince":consigneeProvince, 
				    "consigneeCity":consigneeCity,
			        "payMethod":payMethod 
				},
				success : function(data) {
					if(data!=null){
						$("#freight_id").val(data.price);
						$("#charge_weight_id").val(data.calWeight);
					}
					 getFreightToal();
				}
			});
		}
	}
	
	//日期格式化
	function FormatDate(strTime) {
	    var date = new Date(strTime);
	    var year = date.getFullYear(); //getFullYear getYear
	    var month = date.getMonth();
	    var day = date.getDate();
	    var hour = date.getHours();
	    var minu = date.getMinutes();
	    var sec = date.getSeconds();
	    month = month + 1;
	    if (month < 10) month = "0" + month;
	    if (day < 10) day = "0" + day;
	    if (hour < 10) hour = "0" + hour;
	    if (minu < 10) minu = "0" + minu;
	    if (sec < 10) sec = "0" + sec;
	    var time = "";
	    time = year + "-" + month + "-" + day + " " + hour + ":" + minu + ":" + sec;
	    return time;
	}
	
</script>
<script src="<%=request.getContextPath()%>/js/express/embraceInputExtra/jquery.area.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/express/embraceInputExtra/new-order.js" type="text/javascript"></script>

</body>
</html>
