var jsonArray = [];





//DataGird 数据初始化
function dataInit(){
	
	var thStr = "<tr>" +
				"<th>订单号</th>" +
				"<th>运单号</th>" +
				"<th>供货商</th>" +
				"<th>发货时间</th>" +
				"<th>收件人名称</th>" +
				"<th>收货地址</th>" +
				"<th>手机</th>" +
				"<th>当前状态</th></tr>";
	$.each(jsonArray,function(ind,ele){
		var dataTrStr = "";
		dataTrStr = "<tr onclick='gettrValue("+ele.id+")' id='eleid'>" +
				"<td>"+ele.cwb+"</td>" +
				"<td>"+ele.transcwb+"</td>" +
				"<td>"+ele.customername+"</td>" +
				"<td>"+ele.emaildate+"</td>" +
				"<td>"+ele.consigneename+"</td>" +
				"<td>"+ele.consigneeaddress+"</td>" +
				"<td>"+ele.consigneemobile+"</td>" +
				"<td>"+ele.cwbstate+"</td></tr>";
		thStr += dataTrStr;
		
	});
	$("#CsAcceptDg").html(thStr);
	selectcolor();
}