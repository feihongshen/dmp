/*
<input type="text" name="filter_EQL_supplierUsageId" class="easyui-validatebox" 
	initDataType="TABLE" 
	initDataKey="SupplierUsage" 
	viewField="usageCode,usageContent" 
	data-options="width:150,prompt: '供货商常用语'"
/>
<input type="text" name="filter_EQL_customerUsageId" class="easyui-validatebox" 
	initDataType="TABLE" 
	initDataKey="CommonUsage" 
	viewField="reasonId,reasonContent" 
	filterField="reasonType" 
	afterSelect="function1"
	filterVal="2"  
	data-options="width:150,prompt: '对接用户常用语'"
/>
 <input type="text" name="filter_EQL_complaintType" class="easyui-validatebox" 
 	data-options="width:150,prompt: '工单状态'"
 	initDataType="ENUM" 
 	initDataKey="cn.explink.enumutil.ComplaintTypeEnum"
 	viewField="text" 
 	saveField="value"
/>


function function1(curSel,curEle){
	//	curSel    当前选项
	//  cruEle    当前元素
	alert(obj);
}

 */


//前台全局枚举缓存
var enumCache = {};
var entityCache = {};
var queryInfo = {};

$(window).ready(function() {
	$("[initDataType='ENUM']").each(function() {
		initEnumSelect(this);
	});
	
	$("[initDataType='TABLE']").each(function() {
		initTABLESelect(this);
	});

});

/**
 * 动态加载select控件
 * @param id
 * @param type
 */
function initDynamicSelect(id,type){
	if( type == "TABLE" ){
		initTABLESelect($('#'+id));
	}else if( type == "ENUM" ){
		initEnumSelect($('#'+id));
	}
}

/**
 * 
 * @param data
 */
function initEnumSelect(data){
	var clear = $('<a href="javascript:void(0)">清空</a>'); 
	var s = $(data);
	var ifClear = s.attr("ifClear");
	var enumName = s.attr("initDataKey");
	var viewField = s.attr("viewField");
	var saveField = s.attr("saveField");
	var readonly = s.attr("readonly");
	var afterSelect = s.attr("afterSelect");
	var enumData = initEnumData(enumName,viewField,saveField);
	s.combobox({
	    valueField:saveField,
	    textField:viewField,
	    data:enumData,
	});  
	s.combobox({
		filter: function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) >= 0;
		}
	});
	if(!isNull(afterSelect)){
		s.combobox({
			onSelect:function(obj1){
	    		callFunByName(afterSelect,obj1,s)
			}
		})
	}
	if(!isNull(readonly)){
		s.combobox({
			disabled:true
		})
	}
	if(!isNull(ifClear)){
		s.after(clear);
		clear.bind('click',function(){
			s.combobox("clear")
		});
	}
}

/**
 * 
 * @param data
 */
function initTABLESelect(data){
	var clear = $('<a href="javascript:void(0)">清空</a>'); 
	var s = $(data);
	var ifClear = s.attr("ifClear");
	var entityName = s.attr("initDataKey");
	var viewField = s.attr("viewField");
	var saveField = s.attr("saveField");
	var filterField = s.attr("filterField");
	var filterVal = s.attr("filterVal");
	var linkageEleId = s.attr("linkageEleId");
	var linkageField = s.attr("linkageField");
	var afterSelect = s.attr("afterSelect");
	var readonly = s.attr("readonly");
	var entityData = initEntityData(entityName);
	var viewData = [];
	var uniqueValidate = {};
	$.each(entityData,function(index,valObj){
		//条件初始化
		if( !isNull(filterField) && !isNull(filterVal) ){
			var filterValArr = filterVal.split(",");
			var viewFlag = false;
			$.each(filterValArr,function(filterIndex,filterValObj){
				if(valObj[filterField]+"" == filterValObj){
					viewFlag = true;
					return false;
				}
			});
			if(!viewFlag){
				return true;
			}
		}
		var tmp = {};
		var viewNameArr = viewField.split(",");
		tmp['id'] = valObj[saveField];
		var valueText = "";
		$.each(viewNameArr,function(indexx,valObjj){
			valueText += valObj[valObjj] + "_";	
		});
		valueText = valueText.substring(0,valueText.length-1);
		tmp['value'] = valueText; 
		if(uniqueValidate[valObj[saveField]] != "true"){
			viewData.push(tmp);
			uniqueValidate[valObj[saveField]] = "true";
		}
	});
	if( isNull(linkageEleId) && isNull(linkageEleId) ){
			s.combobox({
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].indexOf(q) >= 0;
				},
				valueField:'id',
				textField:'value',
				data:viewData
			});
			s.combobox({
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].indexOf(q) >= 0;
				}
			});
			if(!isNull(afterSelect)){
				s.combobox({
					onSelect:function(obj1){
			    		callFunByName(afterSelect,obj1,s)
					}
				})
			}
			if(!isNull(readonly)){
				s.combobox({
					disabled:true
				})
			}
			if(!isNull(ifClear)){
				s.after(clear);
				clear.bind('click',function(){
					s.combobox("clear")
				});
			}
	}else{
		s.combobox({
			onSelect:function(selectObj){
				$('#'+linkageEleId).attr({"filterField" : linkageField , "filterVal" : selectObj['id']});
				initTABLESelect($('#'+linkageEleId));
				$('#'+linkageEleId).combobox("clear");
			},
			valueField:'id',
			textField:'value',
			data:viewData
		});
		s.combobox({
			filter: function(q, row){
				var opts = $(this).combobox('options');
				return row[opts.textField].indexOf(q) >= 0;
			}
		});
		if(!isNull(afterSelect)){
			s.combobox({
				onSelect:function(obj1){
		    		callFunByName(afterSelect,obj1,s)
				}
			})
		}
		if(!isNull(readonly)){
			s.combobox({
				disabled:true
			})
		}
		if(!isNull(ifClear)){
			s.after(clear);
			clear.bind('click',function(){
				s.combobox("clear")
			});
		}
	}
}

/**
 * 枚举缓存加载
 * @param enumName
 */
function initEnumData(enumName,viewField,saveField){
	if( isNull(enumCache[enumName.split(".").pop()]) ){
		$.ajax({
			type : "POST",
			url : App.ctx + '/commonFun/getEmumByName?fullClassName=' + enumName + '&viewField=' + viewField + '&saveField=' + saveField,
			async : false,
			success : function(data) {
				if (!$.isEmptyObject(data)) {
					enumCache[enumName.split(".").pop()] = data;
				}
			}
		});
	}
	return enumCache[enumName.split(".").pop()];
}

/**
 * 实体缓存加载
 * @param enumName
 */
function initEntityData(entityName){
	
	if( isNull(entityCache[entityName]) ){
		$.ajax({
			type : "POST",
			url : App.ctx + '/commonFun/getJoinByName?entityName=' + entityName,
			async : false,
			success : function(data) {
				if (!$.isEmptyObject(data)) {
					entityCache[entityName] = data;
				}
			}
		});
	}
	return entityCache[entityName];
}

/**
 * 枚举formatter
 * @param enumName
 * @param index
 * @returns
 */
function enumFormatter(enumName, value, viewField, saveField){
	
	var enumArray = initEnumData(enumName,viewField,saveField);
	var result = value;
	$(enumArray).each(function(){
		var e = $(this)[0];
		if( value == e[saveField] ){
			result = e[viewField];
			return false;
		}
	});
	return result;
}

/**
 * 实体formatter
 * @param entityName
 * @param id
 */
function entityFormatter(entityName, value, viewField, saveField){
	
	var entityArray = initEntityData(entityName);
	var result = value;
	$(entityArray).each(function(){
		var e= $(this)[0];
		if( value == e[saveField]){
			var viewText = "";
			var viewFieldArr = viewField.split(",");
			$.each(viewFieldArr,function(index,viewFieldTemp){
				viewText += e[viewFieldTemp] + "_";
			});
			viewText = viewText.substring(0,viewText.length-1);
			result = viewText;
		}
	});
	return result;
}

/**
 * 对象判空
 * @param obj
 * @returns {Boolean}
 */
function isNull(obj){
	if(typeof(obj) == "undefined" || "" == obj || "undefined" == obj){
		return true;
	}else{
		return false;
	}
}

/**
 * 根据方法名判断并且调用方法
 * @param fn
 * @param args
 */
function callFunByName(fnName,arg1,arg2){ 
    try {
    	fnName = eval(fnName);
    } catch(e) {
        console.log(e);
    }
    if (typeof fnName === 'function'){
    	fnName.call(this,arg1,arg2); 
    }    
}

