
//退货站日志ajax动态加载========================
function getYingtuiCount(pname,htmlid){
	$.ajax({
		type: "POST",
		url:pname+"/tuihuoLog/getYingtuiCount",
		data:{
			a:"1"
		},
		dataType:"json",
		success : function(data) {
			var showhtml="";
			if(data.length!=0){
					showhtml="<strong><a href=\""+pname+"/tuihuoLog/show/zhandianyingtuihuo/1\">"+data.yingtuiCount+"</a></strong>" ;
			}
			if($("#"+htmlid,parent.document).length>0){
				$("#"+htmlid,parent.document).html("");
				$("#"+htmlid,parent.document).html(showhtml);
			}else{
				$("#"+htmlid).html("");
				$("#"+htmlid).html(showhtml);
			}
		}
	});
}

//退货站日志ajax动态加载========================