function getCheckedValue(name) {
	var checkedValue = "";
	$("label[class*=checked]>input[name=" + name + "]").each(
		function(index){
			if (index != 0) {
				checkedValue += ",";
			}
			checkedValue += $(this).val();
	});
	return checkedValue;
}

/*
function getCheckedValue(name) {
	var checkedValue = "";
	var checkedNum = 0;
	$("input[name='" + name + "']").each(function(index) {
		if ($(this).parent().attr("class") && $(this).parent().attr("class").indexOf("checked") != -1) {
			if (checkedNum != 0) {
				checkedValue += ",";
			}
			checkedNum ++;
			checkedValue += $(this).val();
		}
	});
	return checkedValue;
}
*/