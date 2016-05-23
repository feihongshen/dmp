function updateOrderWeight(keyCode){
	var orderNumber = jQuery("#orderNumber").val().trim() ;
	if(keyCode != 13){
		return ;
	}
	if(orderNumber == ""){
		layer.tips('请输入订单号/运单号', jQuery("#orderNumber") , {guide: 0, time: 3});
		jQuery("#orderNumber").focus() ;
		return ;
	}
	var carrealweight = 0.00 ; // 获取电子秤数据
	if(carrealweight == undefined || carrealweight == null ||parseFloat(carrealweight) <= 0){
		$.messager.alert("提示" , orderNumber + "(获取不到重量)，请手动输入重量！" , "warning") ;
    	return false ;
	}
	updateCarrealweight(orderNumber , carrealweight) ;
}


function updateCarrealweight(orderNumber , carrealweight){
	var params = {
			orderNumber:orderNumber,
			carrealweight:carrealweight	
	};
	$.ajax({
		type: "get",
		url:"weightAgainManager.do?updateOrderWeight",
		data:params,
		dataType:"json",
		success : function(rs) {
			var errorMsg = rs.errorMsg ;
			if(errorMsg != undefined && errorMsg != ""){
				EapTip.msgError(errorMsg) ;
				return ;
			}
			EapTip.msgOk("订单号：" + orderNumber + "，补录成功!");
			jQuery("#orderNumber").val("") ;
			jQuery("#weightAmount").val("") ;
		}}) ;
}

function getHandInputCarrealweight(keyCode){
	var orderNumber = jQuery("#orderNumber").val().trim() ;
	var carrealweight = jQuery("#weightAmount").val().trim() ;
	if(keyCode != 13){
		return ;
	}
	if(orderNumber == ""){
		layer.tips('请输入订单号/运单号', jQuery("#orderNumber") , {guide: 0, time: 3});
		jQuery("#orderNumber").focus() ;
		return ;
	}
	var weightExp = /^\d+(\.\d+)?$/ ;
	if(!weightExp.test(carrealweight)){
		layer.tips('请输入重量', jQuery("#weightAmount") , {guide: 0, time: 3});
		jQuery("#weightAmount").focus() ;
		return ;
	}
	updateCarrealweight(orderNumber , carrealweight) ;
}
