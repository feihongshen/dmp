//倍数验证  百分数  并且计算金额
function chackNum(id,defultNum){
	// 如用户倍数框留空，光标离开倍数输入框，则倍数输入框默认为1.
	var  reg=/^(-)?(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,2})?$/;    
	
	        if(!reg.test($('#'+id).val())){   
	        	$('#'+id).val(defultNum);
	    		$('#'+id).focus();
	    		$('#'+id).select(); 
	    		return false;
	        }
	
	
	return true;
}