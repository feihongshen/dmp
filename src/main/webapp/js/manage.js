$(function(){
	function resetData(method, type){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:method,
			type:"POST",
			data:{cwbs:cwbs,type:type},
			success:function(data){
				alert(data)
			}
		});
	}
	
	$("#btnResendFlowJms").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:"resendFlowJms",
			type:"POST",
			data:{cwbs:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});
	
	$("#btnResendFlowJmsEnd").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:"resendFlowJmsEnd",
			type:"POST",
			data:{cwbs:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});
	
	$("#btnResendGotoClassJms").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:"resendGotoClassJms",
			type:"POST",
			data:{gcaids:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});
	
	$("#btnResendPayup").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:"resendPayup",
			type:"POST",
			data:{ids:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});
	
	$("#btnResendPdaDeliverJms").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		if(!cwbs){
			alert("别玩了,都没有输入内容！");
			return;
		}
		$.ajax({
			url:"resendPdaDeliverJms",
			type:"POST",
			data:{flowids:cwbs},
			success:function(data){
				alert(data)
			}
		});
	});
	//重推tpo_send_do_inf	
	$("#btnTpoSendDoInfAll").click(function(){
		resetData("reTpoSendDoInf", "all");		
	});
	$("#btnTpoSendDoInfFail").click(function(){
		resetData("reTpoSendDoInf", "fail");		
	});
	
	//重推tpo_other_order_track
	$("#btnTpoOtherOrderTrackAll").click(function(){
		resetData("reTpoOtherOrderTrack", "all");		
	});
	$("#btnTpoOtherOrderTrackFail").click(function(){
		resetData("reTpoOtherOrderTrack", "fail");		
	});
		
	//重推express_send_b2c_data
	$("#btnOpsSendB2cDataAll").click(function(){
		resetData("reOpsSendB2cData", "all");		
	});
	$("#btnOpsSendB2cDataFail").click(function(){
		resetData("reOpsSendB2cData", "fail");		
	});
	
	$("#btnFormatString").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		cwbs = cwbs.replace(/(?:\S+)(?=\n?)/g,"'$&',");
		cwbs = cwbs.substring(0, cwbs.length - 1);
		$("#cwbs").textbox("setText", cwbs);
	});
	$("#btnFormatNum").click(function(){
		var cwbs=$("#cwbs").textbox("getText");
		cwbs = cwbs.replace(/(?:\S+)(?=\n)/g,'$&,');
		$("#cwbs").textbox("setText", cwbs);
	});
	
});