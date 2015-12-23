var startProvince=[];
var startCity=[];
$(function(){
	
	initArea();
	//initBtn();
	//initForm();
	initAvailableArea();
});

function initBtn(){
	$('#fail-confirm').click(function(){
		$('#result-fail').hide();
	});
	$('#success-confirm').click(function(){
		location.reload();
	});
	
	

}
function initForm(){
	$('#doSubmit').click(function(){
		$('.errorMessage').html('');
	});
	$("#input-form").validate({
		onkeyup: false,
		onclick: false,
		onfocusout: false,
		ignore: '',
		rules: { 
			cnorName: {
				required:true,
				maxlength:64
			},
			custName: {
				maxlength:64
			},
			cnorProv: {
				required:true
			},
			cnorCity: {
				required:true
			},
			
			cnorAddr:{
				required:true,
				maxlength:256
			},
			cnorMobile:{
				required:true,
				isMobile:true
			},
			cnorTel:{
				isPhone:true
			},
			weight:{
				required:true
			},
			cnorRemark:{
				maxlength:256
			},
			requireTime:{
				required:true
			}
			
		},messages: {
			cnorName: {
				required:'请填写寄件人姓名',
				maxlength:'寄件人姓名长度不能超过64'
			},
			custName: {
				maxlength:'公司名称长度不能超过64'
			},
			cnorProv: {
				required:'请选择所在省市'
			},
			cnorCity: {
				required:'请选择所在省市'
			},		
			cnorAddr:{
				required:'请填写街道及详细地址',
				maxlength:'街道及详细地址长度不能超过256'
			},
			cnorMobile:{
				required:'请填写手机号码',
				isMobile:'请填写正确的手机号码'
			},
			cnorTel:{
				isPhone:'请填写正确的电话'
			},
			weight:{
				required:'请选择重量'
			},
			cnorRemark:{
				maxlength:'备注最大长度为256'
			},
			requireTime:{
				required:'请选择寄件时间'
			}
		},showErrors: function(errorMap, errorList) {
			
			if(errorList.length>0){
				for(var i=0;i<errorList.length;i++){
					var e=errorList[i],
					ele=$(e.element),
					target=ele.parent().siblings('.col-xs-7').find('.errorMessage');
					target.html(e.message).show();
					ele.parent().siblings('.col-xs-7').find('.tipMessage').hide();
				}
				
			}
		},submitHandler:function(form){
			doAjaxSubmit(form,function(result){
				if(!result.result){
					$('#result-fail').show();
				}else{
					$('#book-order-no').html(result.reserveOrderNo);
					$('#result-success').show();
					
					ZeroClipboard.setMoviePath(contextPath+'/plugins/zero-clipboard/ZeroClipboard.swf' );
					var clip = new ZeroClipboard.Client();
					clip.setText($('#book-order-no').html());
					clip.glue('copy',$('#result-success')[0]); 
					clip.addEventListener('complete', function (client, text) {
						alert('预约单号以复制成功，ctrl+v可粘贴运单号');
					});
				}
			});
		}
	});
}

function initAvailableArea(){
	//var url=contextPath+'/express/setting/price-time/startArea';
	//$.get(url,function(list){
		
		//for(var i=0;i<list.length;i++){
		//	var area=list[i];
		//	startProvince.push(area.provinceCode);	
		//	startCity.push(area.cityCode);
		//}	
	//});
	var startArea='[ { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101101", "cityName" : "成都市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101119", "cityName" : "巴中市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101117", "cityName" : "达州市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101106", "cityName" : "德阳市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101114", "cityName" : "眉山市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101108", "cityName" : "广元市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101116", "cityName" : "广安市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101111", "cityName" : "乐山市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101105", "cityName" : "泸州市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101107", "cityName" : "绵阳市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101113", "cityName" : "南充市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101110", "cityName" : "内江市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101104", "cityName" : "攀枝花市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101109", "cityName" : "遂宁市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101134", "cityName" : "凉山彝族自治州" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101118", "cityName" : "雅安市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101115", "cityName" : "宜宾市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101103", "cityName" : "自贡市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101120", "cityName" : "资阳市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102101", "cityName" : "贵阳市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102104", "cityName" : "安顺市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102124", "cityName" : "毕节地区" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102127", "cityName" : "黔南布依族苗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102126", "cityName" : "黔东南苗族侗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102102", "cityName" : "六盘水市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102122", "cityName" : "铜仁市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102123", "cityName" : "黔西南布依族苗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102103", "cityName" : "遵义市" } ]';
	var list=JSON.parse(startArea);
	for(var i=0;i<list.length;i++){
			var area=list[i];
			startProvince.push(area.provinceCode);	
			startCity.push(area.cityCode);
		}	
	
	var destArea='[ { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101101", "cityName" : "成都市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101119", "cityName" : "巴中市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101117", "cityName" : "达州市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101106", "cityName" : "德阳市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101114", "cityName" : "眉山市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101108", "cityName" : "广元市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101116", "cityName" : "广安市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101111", "cityName" : "乐山市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101105", "cityName" : "泸州市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101107", "cityName" : "绵阳市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101113", "cityName" : "南充市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101110", "cityName" : "内江市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101104", "cityName" : "攀枝花市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101109", "cityName" : "遂宁市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101134", "cityName" : "凉山彝族自治州" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101118", "cityName" : "雅安市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101115", "cityName" : "宜宾市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101103", "cityName" : "自贡市" }, { "provinceCode" : "105101", "provinceName" : "四川省", "cityCode" : "105101120", "cityName" : "资阳市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102101", "cityName" : "贵阳市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102104", "cityName" : "安顺市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102124", "cityName" : "毕节地区" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102127", "cityName" : "黔南布依族苗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102126", "cityName" : "黔东南苗族侗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102102", "cityName" : "六盘水市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102122", "cityName" : "铜仁市" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102123", "cityName" : "黔西南布依族苗族自治州" }, { "provinceCode" : "105102", "provinceName" : "贵州省", "cityCode" : "105102103", "cityName" : "遵义市" } ]';
	var list1=JSON.parse(startArea);
		var provinceMap={};
		for(var i=0;i<list1.length;i++){
			var area1=list1[i];
			provinceMap[area1.provinceName]=1;	
		
		}
		var provinces=[];
		for(var province in provinceMap){
			provinces.push(province);
		}
		//$('#dest-area-tips').html('目前仅开通'+provinces.join('、')+'的快递业务');
	
	//var urlDest=contextPath+'/express/setting/price-time/destArea';
	//$.get(url,function(list){
	//	var provinceMap={};
	//	for(var i=0;i<list.length;i++){
	//		var area=list[i];
	//		provinceMap[area.provinceName]=1;	
		
	//	}
	//	var provinces=[];
	//	for(var province in provinceMap){
	//		provinces.push(province);
	//	}
	//	$('#dest-area-tips').html('目前仅开通'+provinces.join('、')+'的快递业务');
	//});
}
function initArea(){
	$('#area_sender').areaexp({
		filter:function(area){
			if(area.addressLevel==1){//省
				return startProvince.contains(area.addressCode);
			}else if(area.addressLevel==2){ //市
				return startCity.contains(area.addressCode);
			}
			return true;
		}
	});
	$('#area_consignee').areaexp({
		filter:function(area){
			if(area.addressLevel==1){//省
				return startProvince.contains(area.addressCode);
			}else if(area.addressLevel==2){ //市
				return startCity.contains(area.addressCode);
			}
			return true;
		}
	});
}

var picker,busEnd,busStart;