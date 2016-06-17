String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
(function($){	
	$('body').append('<div id="mark"></div>');
	loadTemplate('picker_template'); 
	//loadTemplate('area_template');
	function loadTemplate(templateName){

		$('<div id="'+templateName+'" ></div>').hide().appendTo($('body'));
	    var t='<div class="location-picker"><ul class="area-tabs"><li data-execute="loadMainlandProvince" class="selected">境内</li><li data-execute="loadOutlandProvince" >境外</li></ul><ul class="level-tabs clear-fix"><li data-execute="loadMainlandProvince" class="province selected" >省/直辖市</li><li class="city" >市</li><li class="district" >县/区</li><li class="street" >街道</li></ul><ul class="area-info clear-fix" ></ul><div class="area-confirm clear-fix"><a  class="btn btn-confirm confirm-area">确定</a><a  class="btn btn-confirm clear-area">清空</a></div></div>';
		$('#'+templateName).html(t);

	}
	
	var province_path = $("#province_path").val();
	var nextaddress_path = $("#nextaddress_path").val();
	var defaultOpt={
			onFinish:function(){},
			endLevel:4,
			filter:function(){
				return true;
			}
	}
	
	var init=function(that,opt){
		that.wrap('<div class="input-group"></div>');
		
		var areaInfo={},
		submitInfo={},
		setting=$.extend({}, defaultOpt, opt),
		html=template('picker_template',{});
		that.parent().append(html);	
		
		
		$('.area-tabs,.level-tabs,.area-info').on('click.effect','li',function(){
			if($(this).hasClass('disabled')){
				return;
			}
			var that=$(this);
			that.siblings().removeClass('selected');
			that.addClass('selected')	
		});	
		initAreaTabs(); //港澳台、大陆选择		
		initSelectBtn();//确定区域
		initLevelTabs();//省/直辖市、市、区、街道选择
		initAreaInfoBtn();//选中地区
	
		initDcClick(); //显示picker
		
		function getContext(){
			return that.parent();
		}
		
		function initDcClick(){		
			that.click(function(){			
				$('.location-picker',getContext()).show();
				$('#mark').show().click(function(){
					$('.location-picker').hide();
					$(this).hide();
				});
				var selectTab=$('.level-tabs .selected',getContext()),
				dataExecute=selectTab.attr('data-execute'),
				dataParent=selectTab.attr('data-parent');
				executeMethod[dataExecute]&&executeMethod[dataExecute](dataParent);
				return false;
			});		
		}
		function initLevelTabs(){
			$('.level-tabs',getContext()).on('click','li',function(){
				var that=$(this),
				dataExecute=that.attr('data-execute');		
				that.nextAll('.city,.district,.street').hide();
				executeMethod[dataExecute]&&executeMethod[dataExecute](that.attr('data-parent'));
				return false;	
			});
		}
		function initSelectBtn(){
			$('.confirm-area',getContext()).click(function(){
				finishSelectArea();
			});		
			$('.clear-area',getContext()).click(function(){
				//debugger
				areaInfo={};				
				finishSelectArea();
				$('.area-tabs li[data-execute="loadMainlandProvince"]').trigger('click');
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_provinceid_id").val("");
					$("#sender_provinceName_id").val("");
					$("#sender_cityid_id").val("");
					$("#sender_cityName_id").val("");
					$("#sender_countyid_id").val("");
					$("#sender_countyName_id").val("");
					$("#sender_townid_id").val("");
					$("#sender_townName_id").val("");
					$("#origin_adress_id").val("");
				}else{
					$("#consignee_provinceid_id").val("");
					$("#consignee_provinceName_id").val("");
					$("#consignee_cityid_id").val("");
					$("#consignee_cityName_id").val("");
					$("#consignee_countyid_id").val("");
					$("#consignee_countyName_id").val("");
					$("#consignee_townid_id").val("");
					$("#consignee_townName_id").val("");
					$("#destination_id").val("");
				}
			});	
		}
		function finishSelectArea(){
			$('.location-picker',getContext()).hide();
			$('#mark').hide();
			submitInfo={};
			submitInfo.provinceCode=areaInfo.provinceCode;
			submitInfo.cityCode=areaInfo.cityCode;
			submitInfo.regionCode=areaInfo.regionCode;
			submitInfo.streetCode=areaInfo.streetCode;
			//生成显示数据
			var showInfo=[];
			if(areaInfo.provinceName&&areaInfo.isDirectly!='true'){
				showInfo.push(areaInfo.provinceName);
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_provinceid_id").val(areaInfo.provinceId.split("_")[0]);
					$("#origin_adress_id").val(areaInfo.provinceCode);
					$("#sender_provinceName_id").val(areaInfo.provinceName);
				}else{
					$("#consignee_provinceid_id").val(areaInfo.provinceId.split("_")[0]);
					$("#destination_id").val(areaInfo.provinceCode);
					$("#consignee_provinceName_id").val(areaInfo.provinceName);
				}
			}else{
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_provinceid_id").val("");
					$("#origin_adress_id").val("");
					$("#sender_provinceName_id").val("");
				}else{
					$("#consignee_provinceid_id").val("");
					$("#destination_id").val("");
					$("#consignee_provinceName_id").val("");
				}
			}
			if(areaInfo.cityName){
				showInfo.push(areaInfo.cityName);
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_cityid_id").val(areaInfo.cityId.split("_")[0]);
					$("#sender_cityName_id").val(areaInfo.cityName);
				}else{
					$("#consignee_cityid_id").val(areaInfo.cityId.split("_")[0]);
					$("#consignee_cityName_id").val(areaInfo.cityName);
				}
				
			}else{
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_cityid_id").val("");
					$("#sender_cityName_id").val("");
				}else{
					$("#consignee_cityid_id").val("");
					$("#consignee_cityName_id").val("");
				}
			}
			if(areaInfo.regionName){
				showInfo.push(areaInfo.regionName);
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_countyid_id").val(areaInfo.regionId.split("_")[0]);
					$("#sender_countyName_id").val(areaInfo.regionName);
				}else{
					$("#consignee_countyid_id").val(areaInfo.regionId.split("_")[0]);
					$("#consignee_countyName_id").val(areaInfo.regionName);
				}
				
			}else{
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_countyid_id").val("");
					$("#sender_countyName_id").val("");
				}else{
					$("#consignee_countyid_id").val("");
					$("#consignee_countyName_id").val("");
				}
			}
			if(areaInfo.streetName){
				showInfo.push(areaInfo.streetName);
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_townid_id").val(areaInfo.streetId.split("_")[0]);
					$("#sender_townName_id").val(areaInfo.streetName);
				}else{
					$("#consignee_townid_id").val(areaInfo.streetId.split("_")[0]);
					$("#consignee_townName_id").val(areaInfo.streetName);
				}
			}else{
				if(that.context.id.indexOf("sender") != -1){
					$("#sender_townid_id").val("");
					$("#sender_townName_id").val("");
				}else{
					$("#consignee_townid_id").val("");
					$("#consignee_townName_id").val("");
				}
			}
			that.val(showInfo.join('-'));
			setting.onFinish.call(that,areaInfo);
		}
	
		
		function initAreaTabs(){
			$('.area-tabs li',getContext()).click(function(){		
				var that=$(this);
				var dataExecute=that.attr('data-execute');
				if(dataExecute=="loadMainlandProvince"){//大陆
					//$('.common',getContext()).show().addClass('selected');			
					$('.province',getContext()).html('省/直辖市').removeClass('selected').attr('data-execute',dataExecute);
					$('.district,.city,.street',getContext()).removeClass('selected').hide();
					dataExecute='loadMainlandProvince';
				}else{//港澳台
					//$('.common',getContext()).hide();			
					$('.province',getContext()).addClass('selected').html('境外地址').attr('data-execute',dataExecute);	
					$('.district,.city,.street',getContext()).removeClass('selected').hide();
				}
				executeMethod[dataExecute]&&executeMethod[dataExecute]();
			});
		}	
		function fillAreaInfoData(r){
			var html=fillAreaData(r);
			
			$('.area-info',getContext()).html(html);
			$('.area-info li',getContext()).removeClass('selected');
			$('.area-info li[data-address-code="'+areaInfo.streetCode+'"],.area-info li[data-address-code="'+areaInfo.regionCode+'"],.area-info li[data-address-code="'+areaInfo.cityCode+'"],.area-info li[data-address-code="'+areaInfo.provinceCode+'"]',getContext()).addClass('selected')
		}

		function doAreaLoad(url,param){
			$('.area-info',getContext()).html('loading...');
			$.post(url,param,function(result){
				fillAreaInfoData(result);
			});
		}
		function newdoAreaLoad(url,InnerOrOut){
			$.ajax({
				type : "POST",
				url : url,
				dataType : "json",
				async: false,
				data:{
					InnerOrOut:InnerOrOut
				},
				success : function(data) {
					var array = JSON.stringify(data).split("[");
					var result = "[" + array[1].substring(0,array[1].length-1);
					doAreaLoadClient(JSON.parse(result));
				}
			});
		}
		function doNextAreaLoad(url,param){
			$.ajax({
				type : "POST",
				url : url,
				dataType : "json",
				async: false,
				data: param,
				success : function(data) {
					var array = JSON.stringify(data).split("[");
					var result = "[" + array[1].substring(0,array[1].length-1);
					doAreaLoadClient(JSON.parse(result));
				}
			});
		}
		
		function doAreaLoadClient(result)
		{
			fillAreaInfoData(result);
		}
		
		
		function fillAreaData(list){
			var s=[]
			if(list&&list.length>0){
				var selectName;
				if(that.context.id.indexOf("sender") != -1){
					selectName = "sender";
				}else{
					selectName = "consignee";
				}
				for(var i=0;i<list.length;i++){
					var a=list[i];
					
					if(setting.filter(a)){
						var str='<li data-address-id="'+a.addressId+'_'+selectName+'"  data-address-code="'+a.addressCode+'" data-address-name="'+a.addressName+'" data-level="'+a.addressLevel+'" 	data-directly="'+a.isDirectly+'"  data-parent-name="'+a.parentName+'"    data-parent-code="'+a.parentCode+'" 	title="'+a.addressName+'">'+a.addressName+'</li>';
						s.push(str);
					}else{
						//var str='<li class="disabled"  data-address-id="'+a.addressId+'"  data-address-code="'+a.addressCode+'" data-address-name="'+a.addressName+'" data-level="'+a.addressLevel+'" 	data-directly="'+a.isDirectly+'"  data-parent-name="'+a.parentName+'"    data-parent-code="'+a.parentCode+'" 	title="'+a.addressName+'">'+a.addressName+'</li>'
						var str='<li data-address-id="'+a.addressId+'_'+selectName+'"  data-address-code="'+a.addressCode+'" data-address-name="'+a.addressName+'" data-level="'+a.addressLevel+'" 	data-directly="'+a.isDirectly+'"  data-parent-name="'+a.parentName+'"    data-parent-code="'+a.parentCode+'" 	title="'+a.addressName+'">'+a.addressName+'</li>'
						s.push(str);
					}
					
				}
			}
			if(s.length==0){
				var str='<li style="cursor: default;" >无相关地市</li>';
				s.push(str);
			}
			return s.join('');
		}
		

		function initAreaInfoBtn(){
			$('.area-info',getContext()).on('click','li',function(){
			
				var that=$(this),
				addressId=that.attr('data-address-id'),
				addressCode=that.attr('data-address-code'),
				addressName=that.attr('data-address-name'),
				level=that.attr('data-level'),
				directly=that.attr('data-directly'),
				parentCode=that.attr('data-parent-code'),
				parentName=that.attr('data-parent-name');
				if(that.hasClass('disabled')){
					return;
				}
				if(level==4){//选中街道
					areaInfo.streetCode=addressCode;
					areaInfo.streetName=addressName;
					areaInfo.streetId=addressId;
				
					$('.province',getContext()).html(areaInfo.provinceName);
					$('.city',getContext()).html(areaInfo.cityName);
					$('.district',getContext()).html(areaInfo.districtName);
					$('.street',getContext()).show().html(addressName);
					if(setting.endLevel==level){
						finishSelectArea();
					}
				}else if(level==3){//选中区
					areaInfo.regionCode=addressCode;
					areaInfo.regionName=addressName;
					areaInfo.regionId=addressId;
					delete areaInfo.streetCode;
					delete areaInfo.streetName;
					delete areaInfo.streetId;
					
					$('.province',getContext()).html(areaInfo.provinceName);
					$('.city',getContext()).html(areaInfo.cityName);
					$('.street',getContext()).html('请选择').show().trigger('click.effect').trigger('click.effect').attr('data-execute','loadByParent').attr('data-parent',addressCode);
					$('.district',getContext()).show().html(addressName);
					executeMethod.loadByParent(addressCode);
					if(setting.endLevel==level){
						finishSelectArea();
					}
					if(setting.endLevel==level){
						finishSelectArea();
					}
				}else if(level==2){//选中市
					areaInfo.cityCode=addressCode;
					areaInfo.cityName=addressName;
					areaInfo.cityId=addressId;
					delete areaInfo.streetCode;
					delete areaInfo.streetName;
					delete areaInfo.streetId;
					delete areaInfo.regionCode;
					delete areaInfo.regionName;
					delete areaInfo.regionId;
					
					$('.province',getContext()).html(areaInfo.provinceName);
					$('.district',getContext()).html('请选择').show().trigger('click.effect').attr('data-execute','loadByParent').attr('data-parent',addressCode);
					$('.street',getContext()).html('请选择').hide();
					if(directly=='1'){//直辖市
						areaInfo.isDirectly='true';		
						$('.city',getContext()).hide();
					}else{	//普通市
						areaInfo.isDirectly='false';		
						$('.city',getContext()).html(addressName).show();					
					}
					
					executeMethod.loadByParent(addressCode);
					if(setting.endLevel==level){
						finishSelectArea();
					}
				}else if(level==1){ //选中省
					areaInfo.provinceCode=addressCode;
					areaInfo.provinceName=addressName;
					areaInfo.provinceId=addressId;
					delete areaInfo.streetCode;
					delete areaInfo.streetName;
					delete areaInfo.streetId;
					delete areaInfo.regionCode;
					delete areaInfo.regionName;
					delete areaInfo.regionId;
					delete areaInfo.cityCode;
					delete areaInfo.cityName;
					delete areaInfo.cityId;
					delete areaInfo.isDirectly;
					
					$('.province',getContext()).html(addressName);
					$('.city',getContext()).html('请选择').show().trigger('click.effect').attr('data-execute','loadByParent').attr('data-parent',addressCode);
					$('.district',getContext()).html('请选择').hide();	
					$('.street',getContext()).html('请选择').hide();
					executeMethod.loadByParent(addressCode);
					
				}
			});
		}
		
		var executeMethod={
				loadCommonArea:function(){
					//var url=contextPath+'/deliverRegionQuery/loadCommonCity';
					//doAreaLoad(url);
					var result='[ { "addressCode" : "104104103", "addressName" : "深圳", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "103101101", "addressName" : "上海市", "addressLevel" : 2, "isDirectly" : 1, "parentCode" : "103101", "parentName" : "上海市" }, { "addressCode" : "104104101", "addressName" : "广州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "101101101", "addressName" : "北京市", "addressLevel" : 2, "isDirectly" : 1, "parentCode" : "101101", "parentName" : "北京市" }, { "addressCode" : "103103101", "addressName" : "杭州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "104104119", "addressName" : "东莞市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "103102105", "addressName" : "苏州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103102", "parentName" : "江苏省" }, { "addressCode" : "103103104", "addressName" : "嘉兴市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "103103103", "addressName" : "温州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "103105105", "addressName" : "泉州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103105", "parentName" : "福建省" }, { "addressCode" : "103103102", "addressName" : "宁波市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "104104106", "addressName" : "佛山市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "103102101", "addressName" : "南京市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103102", "parentName" : "江苏省" }, { "addressCode" : "103103107", "addressName" : "金华市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "103107102", "addressName" : "青岛市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103107", "parentName" : "山东省" }, { "addressCode" : "104102101", "addressName" : "武汉市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104102", "parentName" : "湖北省" }, { "addressCode" : "105101101", "addressName" : "成都市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "105101", "parentName" : "四川省" }, { "addressCode" : "103105102", "addressName" : "厦门市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103105", "parentName" : "福建省" }, { "addressCode" : "101102101", "addressName" : "天津市", "addressLevel" : 2, "isDirectly" : 1, "parentCode" : "101102", "parentName" : "天津市" }, { "addressCode" : "103105101", "addressName" : "福州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103105", "parentName" : "福建省" }, { "addressCode" : "103102102", "addressName" : "无锡市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103102", "parentName" : "江苏省" }, { "addressCode" : "103103110", "addressName" : "台州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "104103101", "addressName" : "长沙市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104103", "parentName" : "湖南省" }, { "addressCode" : "104104120", "addressName" : "中山市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "104101101", "addressName" : "郑州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104101", "parentName" : "河南省" }, { "addressCode" : "103105103", "addressName" : "莆田市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103105", "parentName" : "福建省" }, { "addressCode" : "103103106", "addressName" : "绍兴市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103103", "parentName" : "浙江省" }, { "addressCode" : "103107101", "addressName" : "济南市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103107", "parentName" : "山东省" }, { "addressCode" : "102101104", "addressName" : "抚顺市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "102101", "parentName" : "辽宁省" }, { "addressCode" : "105100101", "addressName" : "重庆市", "addressLevel" : 2, "isDirectly" : 1, "parentCode" : "105100", "parentName" : "重庆市" }, { "addressCode" : "104104113", "addressName" : "惠州市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "104104", "parentName" : "广东省" }, { "addressCode" : "103104101", "addressName" : "合肥市", "addressLevel" : 2, "isDirectly" : 0, "parentCode" : "103104", "parentName" : "安徽省" } ]';
					doAreaLoadClient(JSON.parse(result));
				//	console.log('common');
				},
				loadMainlandProvince:function(){
					newdoAreaLoad(province_path,"Inner");
				},
				loadOutlandProvince:function(){
					newdoAreaLoad(province_path,"Out");
				},
				loadByParent:function(addressCode){
					doNextAreaLoad(nextaddress_path,{parentCode:addressCode});
				}
		};
		initAddress();
		function initAddress(){
			//debugger
			if(that.context.id.indexOf("sender") != -1){
				selectName = "sender";
			}else{
				selectName = "consignee";
			}
			if(that.context.id.indexOf("sender") != -1){
				if($.trim($("#BranchprovinceId").html()) != 0){		
					that.click();
					areaInfo={};				
					finishSelectArea();
					$('.area-tabs li[data-execute="loadMainlandProvince"]').trigger('click');
					$('.area-info li[data-address-id="'+$.trim($("#BranchprovinceId").html())+'_'+selectName+'"]').trigger('click');
					if($.trim($("#BranchcityId").html()) != 0){
						$('.area-info li[data-address-id="'+$.trim($("#BranchcityId").html())+'_'+selectName+'"]').trigger('click');
					}
					if($.trim($("#BranchcountyId").html()) != 0){
						$('.area-info li[data-address-id="'+$.trim($("#BranchcountyId").html())+'_'+selectName+'"]').trigger('click');
					}
					if($.trim($("#sender_townId").html()) != 0){
						$('.area-info li[data-address-id="'+$.trim($("#sender_townId").html())+'_'+selectName+'"]').trigger('click');
					}
					$('.confirm-area',getContext()).click();
				}
			}
			else if(that.context.id.indexOf("consignee") != -1){
				if($.trim($("#receive_provinceId").html()) != 0){		
					that.click();
					areaInfo={};
					//debugger
					finishSelectArea();
					$('.area-tabs li[data-execute="loadMainlandProvince"]').trigger('click');
					$('.area-info li[data-address-id="'+$.trim($("#receive_provinceId").html())+'_'+selectName+'"]').trigger('click');
					if($.trim($("#receive_cityId").html()) != 0){
						$('.area-info li[data-address-id="'+$.trim($("#receive_cityId").html())+'_'+selectName+'"]').trigger('click');
					}
					if($.trim($("#receive_countyId").html()) != 0){
						$('.area-info li[data-address-id="'+$.trim($("#receive_countyId").html())+'_'+selectName+'"]').trigger('click');
					}
					if($.trim($("#receive_townId").html()) != 0){
						var arr = $('.area-info li[data-address-id="'+$.trim($("#receive_townId").html())+'_'+selectName+'"]');
						arr.trigger('click');
					}
					$('.confirm-area',getContext()).click();
				}
			}
		}
	}
	

	$.fn.areaexp=function(opt){
		return this.each(function(){
			var that=$(this);
			init(that,opt);	
		});
	};
	
})(jQuery);



