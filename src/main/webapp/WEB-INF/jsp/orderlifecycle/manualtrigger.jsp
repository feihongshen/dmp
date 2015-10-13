<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String nowdate = (String)request.getAttribute("nowdate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>订单生命周期报表手动触发测试页面</title>
	
	<script src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js" type="text/javascript"></script>
	
	<link href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	
	<script src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	
	
	<style type="text/css">
	body{
		border: 1px solid #eee;
		padding:10px;
	}
	.multiSelect{
		display: inline-block;
	}
	.multiSelect input{
	 	margin: 0px;
	  	padding: 0px;
	  	line-height: 15px;
	  	border: 0px;
		height:25px;
	}
</style>

	
</head>

<body class="container-fluid" >
	<h3>Lifecycle Report Generation Manager</h3>
	<div class="row-fluid">
		<div class="form-horizontal">
				<div class="control-group">
				 	 	<label class="control-label" for="querydate">batch size：</label>
				 	 	<div class="controls">
					  		<input type="text" name="batchsize" id="batchsize" placeholder="batch size" value="100"/>
				 	 	</div>
				</div>
				<div class="control-group">
				 	 	<label class="control-label" for="querydate">日期：</label>
				 	 	<div class="controls">
					  		<input type="text" name="gendate" id="gendate" value="<%=nowdate%>" onclick="WdatePicker({startDate: '%y-%M-%d',dateFmt:'yyyy-MM-dd'})" />
				 	 	</div>
				</div>
				<div class="control-group" >
						<div class="controls">
							<div class="btn btn-default" onclick="genSnapshot();" style="margin-right:5px;" ><i class="icon-search"></i>生成订单快照数据</div>
					 		<div class="btn btn-default" onclick="genReport();" style="margin-right:5px;" ><i class="icon-download-alt"></i>生成报表数据</div>
						</div>
				</div>
				<div class="control-group">
				 	 	<label class="control-label" for="querydate">调用结果：</label>
				 	 	<div class="controls">
				 	 		
					  		<textarea class="alert" id="result_console" rows="3" cols="100" readonly="readonly"></textarea>
				 	 	</div>
				</div>
		
		</div>
	</div>
	
	
	<script type="text/javascript">
	var _ctx = "<%=request.getContextPath()%>";
	
	
	function validate(){
		var result = true;

		var batchsize = $("#batchsize").val();
		if($.isNumeric(batchsize) == false){
			result = false
			alert("please input the batchsize in number.")
		}

		var gendate = $("#gendate").val();
		if(!gendate){
			alert("please select the date to generate data.")
		}

		return result;
	}

	function genSnapshot(){

		if(validate()){
			var param = {
				batchSize:  $("#batchsize").val(),
				date : $("#gendate").val()
			}
			var url = _ctx + "/orderlifecycle/gencwbdetails"
			ajax(param, url, function(result){
				$("#result_console").val(JSON.stringify(result))
			})
		}

	}
		
	function genReport(){
		if(validate()){
			var param = {
				date : $("#gendate").val()
			}
			var url =  _ctx + "/orderlifecycle/genrpt"
			ajax(param, url, function(result){
				$("#result_console").val(JSON.stringify(result))
			})
		}

	}


	function ajax(param, url, successCallback){

		//数据加载动画
		var layEle = layer.load({
			type:3
		});
		
		$.ajax({
		    type: "post",
		    async: false, //设为false就是同步请求
		    url: url,
		    data:param,
	        datatype: "json",
		    success: function (result) {
		    	layer.close(layEle);
		    	successCallback(result);
		    }
		});
	} 
	
	
	</script>
</body>
</html>