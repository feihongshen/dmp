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
	jQuery("#weightNotice").text("正在称重中,请稍等......") ;
	var weightIntervalId = window.setInterval("setWeight()", 1);
    console.log("weightTime:" + weightTime) ;
    window.setTimeout(function waitForWeight(){
    	carrealweight = jQuery("#weightSpan").text(); // 获取电子秤重量
    	window.clearInterval(weightIntervalId) ;
    	if(carrealweight == undefined || parseFloat(carrealweight) <= 0){
    		alert(orderNumber + "(获取不到重量)，请手动输入重量！") ;
    		jQuery("#orderWeight").focus() ;
        	return false ;
    	}
    	updateCarrealweight(orderNumber , carrealweight) ;
    },(weightTime + 3) * 1000) ;	
}

function setWeight() {
	try{
		var weight = window.parent.document.getElementById("scaleApplet").getWeight();
        if (weight != null && weight != '') {
	       document.getElementById("weightSpan").innerHTML = weight;
         }
	}catch(e){
		jQuery("#weightSpan").text("0.00") ;
	}
	console.log("setWeight") ;
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
			jQuery("#weightNotice").text("");
			if(errorMsg != undefined && errorMsg != ""){
				$.messager.alert("提示" , errorMsg , "warning") ;
				return ;
			}
			EapTip.msgOk("订单号：" + orderNumber + "，补录成功!");
			jQuery("#orderNumber").val("") ;
			jQuery("#weightAmount").val("") ;
		}}) ;
}

/**
 * 获取手动录入的重量
 * @param keyCode
 */
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
