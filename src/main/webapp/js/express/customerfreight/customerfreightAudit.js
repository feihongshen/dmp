$(function(){
	//获得客户
	getCustomers();
	//为查询条件赋值
	valuation();
	
	//查询按钮
	$("#query").bind("click",query);
	//查询弹出框的查询按钮
	$("#confirmQuery").bind("click",confirmQuery);
	
	//编辑按钮
	$("#confirm_btn").bind("click",edit);
	//绑定返回按钮的点击事件
	$("#returnButton").bind("click",returnEditExpressBox);
	
	//绑定审核按钮的点击事件
	$("#auditButton").bind("click",audit);
	//绑定取消审核按钮的点击事件
	$("#cancalAuditButton").bind("click",cancalAudit);
	//绑定核销按钮的点击事件
	$("#checkAuditButton").bind("click",checkAudit);
	
	//查询弹出框的退出方法
	$("#cancleQuery").bind("click",cancleQuery);
});
		
var total_num, page_size, page_total_num;//总记录数,每页条数,总页数
var page_cur = 1; //当前页
var ids = "";//选中的id
total_num = 13;
page_size = 10;
page_total_num = 2;


/**
 * 获得客户，绑定到下拉框上
 */
function getCustomers(){
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/getCustomers",
		datatype : "json",
		success : function(result) {
			var selectorArray = [];
			$.each(result, function(key, value) {
				selectorArray.push("<option value='", value.hiddenValue, "'>", value.displayValue,
				"</option>");
			}); 
			$("#customreId").append(selectorArray.join(""));
		}
	});
	
}

//为查询条件赋值
function valuation(){
	$("#billNo").val($("#billNoValue").val());
	$("#billState").val($("#billStateValue").val());
	$("#startCreatTime").val($("#startCreatTimeValue").val());
	$("#endCreatTime").val($("#endCreatTimeValue").val());
	$("#startAuditTime").val($("#startAuditTimeValue").val());
	$("#endAuditTime").val($("#endAuditTimeValue").val());
	$("#customreId").val($("#customreIdValue").val());
	$("#sortOption").val($("#sortOptionValue").val());
	$("#sortRule").val($("#sortRuleValue").val());
	
}

/**
 * 设置颜色
 */
function initColor(td_$) {
	if (td_$.checked == null || td_$.checked == undefined) {
		td_$.checked = false;
	}
	if (td_$.checked) {
		$(td_$).parent().parent().css("background-color", "#FFD700");
	} else {
		$(td_$).parent().parent().css("background-color", "#FFFFFF");
	}
	var id = 'customerAuditTable';
	var ids = ""; 
	var count = 0;
	var chkBoxes = $("#" + id + " input[type='checkbox'][name='checkBoxx']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked) {
			count++;
			ids += $(this).val() + ",";
		}
	});
	if(count>1){
		ids = ids.replace(td_$.id,"");
		ids = ids.replaceAll(",","");
		$("#"+ids).parent().parent().css("background-color", "#FFFFFF");
		$("#"+ids).attr("checked",false);
	}
}

//	$("#selectPg").val(<%=request.getAttribute("page")%>);

//查询按钮的点击触发函数
function query() {
	$("#QueryExpressBox").dialog("open").dialog('setTitle', '查询条件');
}

//查询弹出框的确认查询方法
function confirmQuery() {
	$("#queryCondition").submit();
}

//编辑按钮的点击触发函数
function edit(id) {
	var count=0;
	ids = "";
	//拿到操作的id
	var chkBoxes = $("#" + id + " input[type='checkbox'][name='checkBoxx']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked) {
			count++;
			ids += $(this).val() + ",";
		}
	});
	if(count>1){
		$.messager.alert("提示", "当前操作只支持单条记录 ！", "warning");
		return;
	}
	//判断是否选中
	if (ids.length == 0) {
		$.messager.alert("提示", "请选中要操作的数据 ！", "warning");
		return;
	}
	//打开编辑框
	$("#EditExpressBox").dialog("open").dialog('setTitle', '编辑');
	ids = ids.substr(0, ids.length - 1);
	var params = {};
	params.id = ids;
	//编辑项数据的回显赋值以及按钮控制
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/edit",
		data : params,
		datatype : "json",
		success : function(result) {
			//为编辑弹出框赋值			
			$("#billNoEdit").val(result.billNo);
			$("#billStateEdit").val(result.billState);
			$("#closingDateEdit").val(result.closingDate);
			$("#customerNameEdit").val(result.customerName);
			$("#freightEdit").val(result.freight);
			$("#codEdit").val(result.cod);
			$("#createTimeEdit").val(result.createTime);
			$("#auditTimeEdit").val(result.auditTime);
			$("#cavTimeEdit").val(result.cavTime);
			$("#creatorNameEdit").val(result.creatorName);
			$("#auditorNameEdit").val(result.auditorName);
			$("#cavNameEdit").val(result.cavName);
			$("#remarkEdit").val(result.remark);
			//根据返回的账单状态显示弹出框的按钮
			//未审核
			if (result.billState == "未审核") {
				$("#returnButton").show();
				$("#auditButton").show();
				$("#cancalAuditButton").hide();
				$("#checkAuditButton").hide();
			}
			//已审核
			if (result.billState == "已审核") {
				$("#returnButton").show();
				$("#auditButton").hide();
				$("#cancalAuditButton").show();
				$("#checkAuditButton").show();
			}
			//已核销
			if (result.billState == "已核销") {
				$("#returnButton").show();
				$("#auditButton").hide();
				$("#cancalAuditButton").hide();
				$("#checkAuditButton").hide();
			}
		}
	});
	var table = $("#customerAuditEdit");
	//账单下的订单明细
	var params = {};
	params.id = ids;
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/orderDetailsPageForBill",
		data : params,
		datatype : "json",
		success : function(result) {
			$("#firstTR").nextAll().remove();
			var length = result.list.length;
			var list = result.list;
			var temp = "";
			for (var i = 0; i < length; i++) {
				temp += "<tr>";
				temp += "<td align='center' valign='middle' >"+ list[i].cwb + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].sendnum + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].collectorname + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].deliverid + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].totalfee + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].shouldfare + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].packagefee + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].insuredfee + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].receivablefee + "</td>";
				temp += "</tr>";
			}
			table.append(temp);
			total_num = result.totalCount;//总记录数
			page_size = result.pageSize;//每页数量
			page_cur = 1;//当前页
			/**
			 * 计算总页数
			 */
			if ((total_num % page_size) > 0) {
				page_total_num = parseInt(total_num / page_size) + 1;
			} else {
				page_total_num = total_num / page_size;
			}

		}
	});
	getPageBar();
}

/**
 * 绑定分页的单击事件
 */
$("#page a").live('click',function() { //live 向未来的元素添加事件处理器,不可用bind
	var page = $(this).attr("data-page");//获取当前页
	var params = {};
	params.id = ids;
	params.pageNo = page;
	var table = $("#customerAuditEdit");
	//账单下的订单明细
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx+ "/customerFreightAudit/orderDetailsPageForBill",
		data : params,
		datatype : "json",
		success : function(result) {
			$("#firstTR").nextAll().remove();
			var length = result.list.length;
			var list = result.list;
			var temp = "";
			for (var i = 0; i < length; i++) {
				temp += "<tr>";
				temp += "<td align='center' valign='middle' >"+ list[i].cwb + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].sendnum + "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].collectorname+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].deliverid+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].totalfee+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].shouldfare+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].packagefee+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].insuredfee+ "</td>";
				temp += "<td align='center' valign='middle' >"+ list[i].receivablefee+ "</td>";
				temp += "</tr>";
			}
			table.append(temp);
			total_num = result.totalCount;//总记录数
			page_size = result.pageSize;//每页数量
			page_cur = page;//当前页
			/**
			 * 计算总页数
			 */
			if ((total_num % page_size) > 0) {
				page_total_num = parseInt(total_num/ page_size) + 1;
			} else {
				page_total_num = total_num/ page_size;
			}

		}
	});
	getPageBar();

});

//绑定返回按钮的点击事件
function returnEditExpressBox(){
	$("#EditExpressBox").dialog("close");
}

//绑定审核按钮的点击事件
function audit(){
	ids = getIds();
	var state = $("#billStateEdit").val();
	var params = {};
	params.id = ids;
	params.state = state;
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/audit",
		data : params,
		datatype : "json",
		success : function(result) {
			if (result == 1) {
				$.messager.alert("提示", "审核成功 ！", "warning");
				cancleEdit();
			}
			if (result == 0) {
				$.messager.alert("提示", "审核失败 ！", "warning");
			}
		}
	});

}
//绑定取消审核按钮的点击事件
function cancalAudit(){
	ids = getIds();
	var state = $("#billStateEdit").val();
	var params = {};
	params.id = ids;
	params.state = state;
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/cancelAudit",
		data : params,
		datatype : "json",
		success : function(result) {
			if (result == 1) {
				$.messager.alert("提示", "取消审核成功 ！", "warning");
				cancleEdit();
			}
			if (result == 0) {
				$.messager.alert("提示", "取消审核失败 ！", "warning");
			}
		}
	});

}


//绑定核销按钮的点击事件
function checkAudit(){
	ids = getIds();	
	var state = $("#billStateEdit").val();
	var params = {};
	params.id = ids;
	params.state = state;
	$.ajax({
		type : "post",
		async : false, // 设为false就是同步请求
		url : App.ctx + "/customerFreightAudit/checkAudit",
		data : params,
		datatype : "json",
		success : function(result) {
			if (result == 1) {
				$.messager.alert("提示", "核销成功 ！", "warning");
				cancleEdit();
			}
			if (result == 0) {
				$.messager.alert("提示", "核销失败 ！", "warning");
			}
		}
	});

}
//得到id
function getIds(){
	//表格的id
	var id = "customerAuditTable";
	ids = "";
	//拿到操作的id
	var chkBoxes = $("#" + id
			+ " input[type='checkbox'][name='checkBoxx']");
	$(chkBoxes).each(function() {
		if ($(this)[0].checked) {
			ids += $(this).val() + ",";
		}
	});
	ids = ids.substr(0, ids.length - 1);
	return ids;
}

//编辑弹出框的退出方法
function cancleEdit() {
	$("#EditExpressBox").dialog("close");
}
//查询弹出框的退出方法
function cancleQuery() {
	$("#QueryExpressBox").dialog("close");
}

/**
 * 生成分页按钮
 */
function getPageBar() { //js生成分页
	if (page_cur > page_total_num)
		page_cur = page_total_num;//当前页大于最大页数
	if (page_cur < 1)
		page_cur = 1;//当前页小于1
	page_str = "<span>共" + total_num + "条</span><span>" + page_cur + "/"
			+ page_total_num + "</span>";
	if (page_cur == 1) {//若是第一页
		page_str += "<span>首页</span><span>上一页</span>";
	} else {
		page_str += "<span><a href='javascript:void(0)' style='color: #08c;' data-page='1'>首页</a></span><span><a href='javascript:void(0)' style='color: #08c;' data-page='"
				+ (page_cur - 1) + "'>上一页</a></span>";
	}
	if (page_cur >= page_total_num) {//若是最后页
		page_str += "<span>下一页</span><span>尾页</span>";
	} else {
		page_str += "<span><a href='javascript:void(0)' style='color: #08c;' data-page='"+ (parseInt(page_cur) + 1)+ "'>下一页</a></span><span><a href='javascript:void(0)' style='color: #08c;' data-page='"+ page_total_num + "'>尾页</a></span>";
	}
	$("#page").html(page_str);
}

