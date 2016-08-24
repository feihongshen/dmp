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
.remarkTextArea {
	width: 280px;
	height: 60px;
	font-size: 15px;
	font-family: 微软雅黑, 黑体;
}

.container {
	height: 500px;
	vertical-align: middle;
	font-size: 16px;
}

.orderTextAreaContainer {
	width: 350px;
	margin-top: 20px;
	margin-left: 10px;
	border: 1px solid #CCC;
	float: left;
	height: 460px;
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
	width: 440px;
}

.result {
	height: 400px;
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
		$("#reset").click(function() {
			reset();
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
		var remark = $("#remark_text_area").val();
		var $result = $("#result");
		appendResult($result, "正在处理，请稍等。。。");
		$("#save").attr("disabled",true);
		$.ajax({
			type : "post",
			dataType : "json",
			url : "<%=request.getContextPath()%>/applyediteditcartype/applyByCwbs",
			data : {
				cartype : goodsType,
				cwbs : submitOrders,
				remark: remark
			},
			success : function(data) {
				outputSubmitResult(data);
				reset();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				appendResult($result, "错误信息如下，请重试，如仍然出现，请联系技术人员："+errorThrown);
			},
			complete : function(){
				$("#save").attr("disabled",false);
			}
		});
	}

	function outputSubmitResult(data) {
		var succList = null;
		var succList = null;
		if(data!=null && data.succList!=null){
			succList = data.succList;
		}
		if(data!=null && data.failList!=null){
			failList = data.failList;
		}
		var succCount = 0;
		var failCount = 0;
		if(succList!=null){
			succCount = succList.length;
		}
		if(failList!=null){
			failCount = failList.length;
		}
		var $result = $("#result");
		$result.empty();
		
		appendResult($result, "提交成功: " + succCount + " 条," + "失败: "+ failCount + " 条");
		$result.append("<hr style='height:1px;border:none;border-top:1px solid #86AFD5;' />");
		
		appendResult($result, "成功: " + succCount + " 条:");

		if(succCount>0){
			for(var i=0; i<succCount ; i++){
				var opsResult = succList[i];
				$result.append("<div style='width:90%;text-align:left'><p>"+opsResult.cwb+"</p></div>");
			}
		}
		$result.append("<hr style='height:1px;border:none;border-top:1px solid #86AFD5;' />");
		
		appendResult($result, "失败: " + failCount + " 条:");

		if(failCount>0){
			for(var i=0; i<failCount ; i++){
				var opsResult = failList[i];
				$result.append("<div style='width:45%;text-align:left;float:left'><p>"+opsResult.cwb+"</p></div>");
				$result.append("<div style='width:50%;text-align:left;float:left'><p>"+opsResult.remark+"</p></div>");
			}
		}
		
		
		$result.append("<hr style='height:1px;border:none;border-top:1px solid #86AFD5;' />");
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
		var orderArray = orderNos.replace(/[\n\r\t]/ig, " ").split(" ");
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

	function reset() {
		$("#order_text_area").val("");
		$("#remark_text_area").val("");
		//$("#type_area input[type='radio']:checked").attr("checked", false);
		//$("#result").empty();
	}
</script>






</head>

<body style="background: #f5f5f5">
	<div class="menucontant container">
		<div class="orderTextAreaContainer">
			<div id="type_area">
				<input type="radio" name="goodsType" value="大件" checked="checked" />大件 
				
			</div>
			<hr />
			<div class="orderNo">订单号</div>
			<div>
				<textarea id="order_text_area" class="orderTextArea"></textarea>
				<div class="orderNo">备注<font color="red">(限50字内)</font></div>
				<textarea id="remark_text_area" class="remarkTextArea" onkeyup="this.value = this.value.substring(0, 50)"></textarea>
			</div>
			<div>
				<input type="button" id="save" class="button" value="提交" /> <input type="button" id="reset"
					class="button" value="重置" />
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
