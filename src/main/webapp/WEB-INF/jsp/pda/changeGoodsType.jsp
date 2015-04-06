<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>入库扫描</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<style>
.orderTextArea {
	width: 280px;
	height: 210px;
	font-weight: bold;
	font-size: 18px;
	color: #F60;
	font-family: 微软雅黑, 黑体;
}

.container {
	height: 400px;
	vertical-align: middle;
	font-size: 16px;
}

.orderTextAreaContainer {
	width: 300px;
	margin-top: 20px;
	margin-left: 10px;
	border: 1px solid #CCC;
	float: left;
	height: 360px;
}

.orderNo {
	font-weight: bold;
	font-size: 15px;
	text-align: left;
	font-family: 微软雅黑;
}

.orderTextAreaContainer div {
	padding: 5px;
	vertical-align: middle;
	text-align: center;
	margin-bottom: 5px;
}

.resultContainer {
	width: 330px;
}

.result {
	height: 290px;
	overflow-y: auto;
	text-align: left;
	overflow-y: auto;
}

p {
	font-family: 微软雅黑;
	font-size: 15px;
	color: #036;
	text-align: left;
}
</style>

<script>
	$(function() {
		$("#order_text_area").focus();
		$("#save").click(function() {
			save();
		});
		$("#cancel").click(function() {
			cancel();
		});
	})

	function save() {
		var submitOrders = getOrderNos();
		var goodsType = getGoodsType();
		if (!validate(submitOrders, goodsType)) {
			return;
		}
		submitData(submitOrders, goodsType);
	}

	function submitData(submitOrders, goodsType) {
		$.ajax({
			type : "post",
			dataType : "json",
			async : false,
			url : "<%=request.getContextPath()%>/PDA/submitGoodsTypeChange",
			data : {
				goodsType : goodsType,
				orderNos : submitOrders
			},
			success : function(data) {
				outputSubmitResult(data);
				$("#order_text_area").val("");
				$("#type_area input[type='radio']:checked").attr("checked", false);
			}
		});
	}

	function outputSubmitResult(data) {
		var successedCount = data.successedCount;
		var totalCount = data.totalCount;
		var failedCount = totalCount - successedCount;
		var $result = $("#result");
		var errorMap = data.errorMap;
		for ( var tmp in errorMap) {
			var obj = errorMap[tmp];
			if (obj.length == 0) {
				continue;
			}
			appendResult($result, tmp);
			appendResult($result, "--------------------------");
			for (var i = 0; i < obj.length; i++) {
				appendResult($result, obj[i]);
			}
			appendResult($result, "--------------------------");
		}
		appendResult($result, "成功: " + successedCount + " 条," + "失败: "+ failedCount + " 条");
		appendResult($result, "--------------------------");
	}

	function appendResult($result, message) {
		$result.append("<p>" + message + "</p>");
	}

	function validate(submitOrders, goodsType) {
		var $result = $("#result");
		$result.empty();
		if (submitOrders.length == 0) {
			$result.append(" <p>请输入订单号!</p>");
			return false;
		}
		if (goodsType == undefined) {
			$result.append(" <p>请选择货物类型!</p>");
			return false;
		}
		return true;
	}

	function getOrderNos() {
		var $orderTextArea = $("#order_text_area");
		var orderNos = $.trim($orderTextArea.val());
		var orderArray = orderNos.replace(/[\n\r]/ig, " ").split(" ");
		var result = "";
		for (var i = 0; i < orderArray.length; i++) {
			if (orderArray[i].length == 0) {
				continue;
			}
			result += orderArray[i];
			if (i != orderArray.length - 1) {
				result += ",";
			}
		}
		return result;
	}

	function getGoodsType() {
		return $("#type_area input[type='radio']:checked").val();
	}

	function cancel() {
		$("#order_text_area").val("");
		$("#type_area input[type='radio']:checked").attr("checked", false);
		$("#result").empty();
	}
</script>






</head>

<body style="background: #f5f5f5">
	<div class="menucontant container">
		<div class="orderTextAreaContainer">
			<div id="type_area">
				<input type="radio" name="goodsType" value="1" />大件 <input type="radio" name="goodsType"
					value="2" />贵品 <input type="radio" name="goodsType" value="3" />大件+贵品 <input type="radio"
					name="goodsType" value="0" />普件
			</div>
			<hr />
			<div class="orderNo">订单号</div>
			<div>
				<textarea id="order_text_area" class="orderTextArea"></textarea>
			</div>
			<div>
				<input type="button" id="save" class="button" value="保存" /> <input type="button" id="cancel"
					class="button" value="取消" />
			</div>
		</div>
		<div class="orderTextAreaContainer resultContainer">
			<div class="orderNo">反馈结果</div>
			<hr />
			<div id="result" class="result"></div>
		</div>
	</div>
</body>
</html>
