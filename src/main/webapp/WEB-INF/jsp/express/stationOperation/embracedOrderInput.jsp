<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page  import="java.util.*"%>
<%@page  import="cn.explink.domain.VO.express.AdressVO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>揽件录入页面</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<style type="text/css">
.tdleft {
	text-align: left;
}

.tdcenter {
	text-align: center;
}

tr {
	line-height: 35px;
}

.input_text2 {
	border: 1px solid #09C;
	background: url(../images/repeatx.png) repeat-x 0 -175px;
	height: 16px;
	line-height: 16px;
	width: 90%;
}
.select1 { 
	border:1px solid #09C;
	background:url(../images/repeatx.png) repeat-x 0 -175px; 
	width:180px; 
	height:20px; 
	line-height:20px; 
	color:#000;
	}
</style>
<script type="text/javascript">	
	$(function(){
		if($.trim($("#flag").html()) == "true"){
			alert("保存成功");
		}else if($.trim($("#flag").val()) == "false"){
			alert("保存失败");
		}else if($.trim($("#flag").val()) == "hasSaved"){
			alert("该运单号已经录入过，保存失败");
		}
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
        //校验寄件人信息
        if(!checkConsignee()){
        	return false;
        }
        //校验费用信息
        if(!checkFeeDate()){
        	return false;
        }
       
        assignValue();
        orderflag = "";
        return true;
    }
	
	/*
	 *校验第一个框里的数据
	 */
	  function checkHead(){
		var orderNo = $("#orderNo_id");
		var delivermanId = $("#delivermanId_id");
		
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
		//校验小件员是否为空
        if(!nullValidater(delivermanId,"小件员")){
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
      		if(!phoneValidater(sender_telephone, "寄件人")){
            	return false;
            }
      	}
      	//校验寄件人地址是否为空
        if(!nullValidater(sender_adress,"寄件人地址")){
        	return false;
        }
      	//校验寄件人地址是否超长
      	if(!checkLength(sender_adress, 100, '寄件人地址', 100)){
        	return false;
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
     	//校验收件人地址是否为空
        if(!nullValidater(consignee_adress,"收件人地址")){
        	return false;
        }
      	//校验收件人地址是否超长
      	if(!checkLength(consignee_adress, 100, '收件人地址', 100)){
        	return false;
        }
      	return true;
	}
	
	/*
	 *校验费用（最后一个框）数据
	 */
	function checkFeeDate(){
		var number = $("#number_id");
		var reight = $("#freight_id");
		
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
      	//校验运费是否为空
        if(!nullValidater(reight,"运费")){
        	return false;
        }
      	//校验运费是否为非负数
        if(!NonNegativeValidater(reight,'运费')){
        	return false;
        }
		//校验运费是否超过大小
        if(!checkNumber(reight,1000000,'运费')){
        	return false;
        }
		return true;
	}
	
	/*
	 *校验长度
	 */
	function checkLength(el, maxlength, msg, showlength){
		var length = $.trim(el.val()).length;
		msg = msg + "最大长度不应超过" + showlength;
		if(length > maxlength){
			alert(msg);
			return false;
		}
		return true;
	}
		
	/*
	 *校验数字大小
	 */
	function checkNumber(el, maxnumber, msg){
		msg = msg + "最大不应超过" + maxnumber;
		if($.trim(el.val()) > maxnumber){
			alert(msg);
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
	 * 校验输入内容为0或正整数的方法
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
		var regExp = "/^(0|[1-9]\\d*)$/";
		msg = msg + "输入不为大于0的数";
		return formatRegValidater(el, msg, regExp);
	}
	/*
	 * 校验单个输入框不为空的方法
	 */
	 function nullValidater(el, msg){
		//var regExp = "\s";
		msg = msg + "不能为空";
		if($.trim(el.val()) == ""){
			alert(msg);
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
			if($.trim(arr[i].val()) != ""){
				flag = true;
			}
		}
		if(!flag){
			alert(msg);
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
			alert(msg);
			return false;
		}
		return true;
	}
	//给与select对应的输入框赋值（因为要给后台传select对应的text）
	function assignValue(){
		$("#delivermanName_id").val($("#delivermanId_id  option:selected").text());
		$("#sender_provinceName_id").val($("#sender_provinceid_id  option:selected").text());
		$("#sender_cityName_id").val($("#sender_cityid_id  option:selected").text());
		$("#consignee_provinceName_id").val($("#consignee_provinceid_id  option:selected").text());
		$("#consignee_cityName_id").val($("#consignee_cityid_id  option:selected").text());
	}
	/*
	 *省份改变时，刷新市
	 */
	function changeProvince(obj){		
		var citySelect;
		var provinceSelect;
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
			data:{
				"provinceCode":provinceCode
			},
			success : function(data) {
				var citylist = data.citylist;
				if(obj.id.indexOf("sender") != -1){
					citySelect = $("#sender_cityid_id");
				}else{
					citySelect = $("#consignee_cityid_id");
				}
				citySelect.empty();
				for(var i = 0; i < citylist.length; i++){
					citySelect.get(0).add(new Option(citylist[i].name,citylist[i].id));
				}
			}
		});
	}
	/*
	 *根据运单号，确定小件员
	 */
	function changeOrderNo(){
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
				var delivermanIdSelect = $("#delivermanId_id");
				var delivermanIdSelectOptions = $("#delivermanId_id option");
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
						alert("该运单号的小件员不属于本站点");
						$("#orderNo_id").attr("value","");
					}
				}else{
					delivermanIdSelect.css('background','#ffffff');
					delivermanIdSelect.attr("readonly",false);
				}
			}
		});
	}
	
	/*
	 *判断运单号是否已经保存
	 */
	function judgeCwbOrderSaved(){
		var orderNo = $.trim($("#orderNo_id").val());
		if(orderNo != ""){
			$.ajax({
				type : "POST",
				url : "<%=request.getContextPath()%>/embracedOrderInputController/judgeCwbOrderSaved",
				dataType : "json",
				async: false,
				data:{
					"orderNo":orderNo
				},
				success : function(data) {	
					var flag = data.flag;
					if(flag){
						alert("该运单号已经保存");
						$("#orderNo_id").val("");
					}
				}
			});
		}
	}
</script>

</head>
<body onkeydown="if(event.keyCode==13){return false;}">
	<div id="flag" style="display:none;">${flag }</div>
	<form id="PageFromW" action="<%=request.getContextPath()%>/embracedOrderInputController/inputSave" method="post" onsubmit="return toVaildForm()" style="margin: 10px;">
		<fieldset ID="first" style="border: inset; border-width: 3px;">
			<table width="100%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="10%">
					<col width="20%">
					<col width="10%">
					<col width="20%">
					<col width="40%">
				</colgroup>
				<tr>
					<td class="tdcenter">运单号<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="orderNo" id="orderNo_id" class="input_text1" onchange="changeOrderNo()" onblur="judgeCwbOrderSaved()"/></td>
					<td class="tdcenter">小件员<font color="red">*</font>:</td>
					<td class="tdleft">
						<select name="delivermanId" id="delivermanId_id" class="select1">
							<c:forEach items="${deliveryMansList}" var="list">
								<option value="${list.userid}">${list.realname}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="delivermanName" id="delivermanName_id"/></td>
					<td></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="sender" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">寄件人信息</span>
			</legend>
			<table width="100%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="5%">
					<col width="15%">
					<col width="5%">
					<col width="15%">
					<col width="15%">
				</colgroup>
				<tr>
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
							<select name="sender_provinceid" id="sender_provinceid_id" class="select1" readonly="readonly"  style="background:#EBEBE4;" readonly="true" onchange="changeProvince(this)">
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
							<select name="sender_cityid" id="sender_cityid_id" class="select1" readonly="readonly">
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
					<td></td>
					<td class="tdcenter">手机：</td>
					<td class="tdleft"><input type="text" name="sender_cellphone" id="sender_cellphone_id" class="input_text1" /></td>
					<td class="tdcenter">固话：</td>
					<td class="tdleft"><input type="text" name="sender_telephone" id="sender_telephone_id" class="input_text1" /></td>
					<td><font color="red" size="2">手机和固话至少填写一个</font></td>
				</tr>
				<tr>
					<td class="tdcenter">地址<font color="red">*</font>:</td>
					<td colspan="8" class="tdleft"><input type="text" name="sender_adress" id="sender_adress_id" class="input_text2" /></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="consignee" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">收件人信息</span>
			</legend>
			<table width="100%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="10%">
					<col width="5%">
					<col width="5%">
					<col width="15%">
					<col width="5%">
					<col width="15%">
					<col width="15%">
				</colgroup>
				<tr>
					<td class="tdcenter">省<font color="red">*</font></span>:</td>
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
						<select name="consignee_cityid" id="consignee_cityid_id" class="select1" readonly="readonly">
							<c:forEach items="${citylist}" var="list">
								<option value="${list.id}" code="${list.code}">${list.name}</option>
							</c:forEach>
						</select>
					</td>
					<td style="display:none;"><input type="text" name="consignee_cityName" id="consignee_cityName_id"/></td>
					<td></td>
					<td class="tdcenter">手机：</td>
					<td class="tdleft"><input type="text" name="consignee_cellphone" id="consignee_cellphone_id" class="input_text1" /></td>
					<td class="tdcenter">固话：</td>
					<td class="tdleft"><input type="text" name="consignee_telephone" id="consignee_telephone_id" class="input_text1" /></td>
					<td><font color="red" size="2">手机和固话至少填写一个</font></td>
				</tr>
				<tr>
					<td class="tdcenter">地址<font color="red">*</font>：</td>
					<td colspan="8" class="tdleft"><input type="text" name="consignee_adress" id="consignee_adress_id" class="input_text2" /></td>
				</tr>
			</table>
		</fieldset>

		<fieldset ID="feeDate" style="border: inset; border-width: 3px; margin-top: 10px;">
			<legend>
				<span style="color: red;">费用</span>
			</legend>
			<table width="100%" class="table1" style="margin: 10px;">
				<colgroup>
					<col width="10%">
					<col width="20%">
					<col width="10%">
					<col width="20%">
					<col width="40%">
				</colgroup>
				<tr>
					<td class="tdcenter">件数<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="number" id="number_id" class="input_text1" /></td>
					<td class="tdcenter">运费<font color="red">*</font>:</td>
					<td class="tdleft"><input type="text" name="freight_total" id="freight_id" class="input_text1" /></td>
					<td></td>
				</tr>
			</table>
		</fieldset>

		<table width="100%" style="margin-top: 10px;">
			<tr>
				<td class="tdcenter"><input type="submit" value="保存" class="input_button2" /></td>
			</tr>
		</table>
	</form>
	
</body>
</html>