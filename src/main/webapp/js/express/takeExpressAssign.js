
//获取今日待揽收和今日已揽收数量
function getExpressCount(path,deliverid){
	$.ajax({
		type: "POST",
		url:path+"/stationOperation/getExpressCount",
		data:{"deliverid":deliverid},
		dataType:"json",
		success : function(data) {
			$("#todayToTakeCount").html(data.todayToTakeCount);
			$("#todayTakedCount").html(data.todayTakedCount);
		}
	});
}