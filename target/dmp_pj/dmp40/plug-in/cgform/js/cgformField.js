var filedTypes = ['check', 'database', 'key', 'page'];
var headerTr = "template_header_";
var bodytr = "add_column_table_template_";
var iframeId = "iframe_";
var fieldData = [];// 字段的数据
var iframeLoadNumber = 0; // 当前加载的Iframe的数量
var rownumber = 0 ;//标识当前序号
var toDelete = [];  //保存表单待删除属性字段id
$(document).ready(iframeLoaded());
$("#iframe_check").load(iframeLoaded());
$("#iframe_database").load(iframeLoaded());
$("#iframe_key").load(iframeLoaded());
$("#iframe_page").load(iframeLoaded());

function iframeLoaded() {
	iframeLoadNumber++;
	if (iframeLoadNumber == 5) {
		$(".datagrid-toolbar").parent().css("width", "auto");
		setTimeout(initData, 500);
	}
}
var fixHelper = function(e, ui) {
    ui.children().each(function() {
        $(this).width($(this).width());
    });
    return ui;
};
setTimeout(function(){
	$("#tab_div_database tbody").sortable({
	    helper: fixHelper,
	    items: '> tr',
	    forcePlaceholderSize: true,
	    stop: function (event, ui) {
	    	var textContent = ui.item.context.innerText?
	    			ui.item.context.innerText.toString():
	    				ui.item.context.textContent.toString();
	    	var endString = textContent.indexOf("S")!=-1?textContent.indexOf("S"):textContent.length;
	    	var startRownum = textContent.substring(0,endString).replace(/(^\s*)|(\s*$)/g,'');
	    	var targetIndex = ui.item.context.rowIndex;
	    	for (var i = 0; i < filedTypes.length; i++) {
	    		if(filedTypes[i] == "database" && startRownum != (targetIndex + 1)) continue;
	    		if(startRownum == (targetIndex + 1)){  //处理当前行和下一行交换位置
	    			$("#tab_div_" + filedTypes[i]).find("tr").eq(startRownum).insertBefore($("#tab_div_" + filedTypes[i]).find("tr").eq(targetIndex));
	    		}else if(startRownum > targetIndex){
	    			$("#tab_div_" + filedTypes[i]).find("tr").eq(startRownum-1).insertBefore($("#tab_div_" + filedTypes[i]).find("tr").eq(targetIndex));
	    		} else {
	    			$("#tab_div_" + filedTypes[i]).find("tr").eq(startRownum-1).insertAfter($("#tab_div_" + filedTypes[i]).find("tr").eq(targetIndex));
	    		}
	    	}
	    	for (var i = 0; i < filedTypes.length; i++) {
	    		resetTrNum("#tab_div_" + filedTypes[i]);
	    	}
	    }
	});
	// .disableSelection()研究下这个的意义好像这里没啥作用,影响了火狐
}, 2000);
function initData() {
	addTableHead();
	$.get("cgFormHeadController.do?getColumnList&id=" + $("#id").val(),
			getDataHanlder);
	
}

/**
 * 添加表头
 */
function addTableHead() {
	for (var i = 0; i < filedTypes.length; i++) {
		var tr = $(getIframeDocument(filedTypes[i])).find("#"
				+ headerTr + filedTypes[i] + " tr").clone();
		$("#tab_div_" + filedTypes[i]+"_title").append(tr);
	}
}
// 兼容不同浏览器获取iframe 内容
//update-begin--Author:JueYue  Date:20140213 for：因为Iframe获取失败,而导致详情看不到的情况
//主要情况是ie11下的版本是火狐的标识倒是出差错
function getIframeDocument(id){
	if(window.frames["iframe_" + id].contentDocument){
		return window.frames["iframe_" + id].contentDocument;
	}
	return window.frames["iframe_" + id].document;
}
//update-begin--Author:JueYue  Date:20140213 for：因为Iframe获取失败,而导致详情看不到的情况

/**
 * 获取数据的回调
 * 
 * @param {}
 *            data
 */
function getDataHanlder(data) {
	data = eval("(" + data + ")");
	// 兼容之前order最小为0的问题
	var orderMin = data[0].orderNum == 0;
	$.each(data, function(idx, item) {
		rownumber = idx;//存储当前序号
		for (var i = 0; i < filedTypes.length; i++) {
			initTrData(item, filedTypes[i], orderMin);
		}
	});
	jformTypeChange();
	fixTab();
}
/**
 * 添加行数据
 * 
 * @param {}
 *            item 这个数据
 * @param {}
 *            filedType 这一行的类型
 */
function initTrData(item, filedType, orderMin) {
	var tr = $(getIframeDocument(filedType)).find("#" + bodytr
			+ filedType + " tr").clone();
 	var isId = item.fieldName == "id";
	$(':input, select,a', $(tr)).each(function() {
		var $this = $(this), name = $this.attr('name'), val = $this.val();
		if(isId){setAttrForThis($this);}
		//自定义一个序号<a> 按名字进行获取对象，并进行序号指定
		if(name.indexOf("#rindex#") > 0){
			$this.attr("name", name.replace('#rindex#',rownumber));
			$this.html(rownumber+1);
		}
		if (name.indexOf("#index#") > 0) {
			var fieldName = name.replace("columns[#index#].", "");
			$this.attr("name", name.replace('#index#',rownumber));
			
			if (item[fieldName] != "Y" && item[fieldName] != "N") {
				$this.val(item[fieldName]);
			} else {
				item[fieldName] == "Y" ? $this.attr("checked", true) : $this
						.attr("checked", false);
			}
		} else if (name != "ck") {
			$this.attr("name", name.replace('_index',rownumber));
			$this.val(name.indexOf("columnsfieldName") != -1
					? item.fieldName
					: item.content);
		}
		else{
			$this.val(item.id);
		}
	});
	isQueryItemEvent(rownumber, filedType, jQuery(tr));
	$("#tab_div_" + filedType).append(tr);
}

/**
 * 是否查询字段配置单击触发事件
 * @param rownumber 行号
 * @param fieldName key值
 * @param thisJqueryObj 当前jquery对象
 */
function isQueryItemEvent(rownumber, filedType, trObj) {
	if (rownumber === 0) {
		trObj.find("input[name='columns[0].tableCoordinate']").hide();
		return;
	}
	if (filedType === "page") { //是否查询
		var thisJqueryObj = trObj.find("input[name='columns[" + rownumber + "].isQuery']").eq(0);//是否查询判断对象
		var tableCoordinate = trObj.find("input[name='columns[" + rownumber + "].tableCoordinate']").eq(0); //表坐标输入对象
		if (thisJqueryObj.is(":checked")) { //选中状态
			tableCoordinate.show();
		} else {
			tableCoordinate.hide();
		}
	} else if (filedType === "database") { //是否自定义字段
		var thisJqueryObj = trObj.find("input[name='columns[" + rownumber + "].isAutoItem']").eq(0);//是否自定义字段
		var ownerTable = trObj.find("input[name='columns[" + rownumber + "].ownerTable']").eq(0); //对应表名
		var ownerItem = trObj.find("input[name='columns[" + rownumber + "].ownerItem']").eq(0); //对应表字段
		if (thisJqueryObj.is(":checked")) { //选中状态
			ownerTable.show();
			ownerItem.show();
		} else {
			ownerTable.hide();
			ownerItem.hide();
		}
	}
}

/***
 * 绑定是否显示查询区域坐标输入框事件
 * @param thisObj 是否查询字段
 */
function showtableCoordinateItem(thisObj) {
	var thisJqueryObj = null;
	jQuery(thisObj).parent().nextAll().each(function() {
		var tableCoordinateArray = jQuery(this).find("input[code='tableCoordinate']");
		if (tableCoordinateArray.length > 0) {
			thisJqueryObj = tableCoordinateArray.eq(0);
		}
	});
	if (jQuery(thisObj).is(":checked")) { //选中状态
		thisJqueryObj.show();
	} else {
		thisJqueryObj.hide();
	}
}

/***
 * 绑定是否自定义字段事件
 * @param thisObj 是否自定义字段
 */
function isAutoItemEvent(thisObj) {
	var ownerTableObj = null;
	var ownerItemObj = null;
	jQuery(thisObj).parent().nextAll().each(function() {
		var ownerTableArray = jQuery(this).find("input[code='ownerTable']");
		if (ownerTableArray.length > 0) {
			ownerTableObj = ownerTableArray.eq(0);
		}
		var ownerItemObjArray = jQuery(this).find("input[code='ownerItem']");
		if (ownerItemObjArray.length > 0) {
			ownerItemObj = ownerItemObjArray.eq(0);
		}
	});
	if (jQuery(thisObj).is(":checked")) { //选中状态
		ownerTableObj.show();
		ownerItemObj.show();
	} else {
		ownerTableObj.hide();
		ownerItemObj.hide();
	}
}

function setAttrForThis($this){
	if($this.is('select')){
		$this.attr("onfocus","this.defOpt=this.selectedIndex");
		$this.attr("onchange","this.selectedIndex=this.defOpt;");
	}else if($this.is('input')&&$this.attr('type')=='text'){
		$this.attr("readonly","readonly");
	}else if($this.is('input')&&$this.attr('type')=='checkbox'){
		$this.attr("onclick","return false;");
	}
}

/**
 * 添加行
 */
function addColumnBtnClick(autoTr, name, content) {
	for (var i = 0; i < filedTypes.length; i++) {
		addTrToTable(filedTypes[i], autoTr, name, content);
	}
}
function addTrToTable(filedType, autoTr, name, content) {
	var tr = null;
	if (autoTr && filedType === "database") {
		tr = autoTr;
	} else {
		tr = $(getIframeDocument(filedType)).find("#" + bodytr
				+ filedType + " tr").clone();
		tr.find("input").each(function() {
			if (jQuery(this).attr("code") === "item_name") {
				jQuery(this).val(name);
			} else if (jQuery(this).attr("code") === "item_content") {
				jQuery(this).val(content);
			}
		});
	}
	$("#tab_div_" + filedType).append(tr);
	resetTrNum('#tab_div_' + filedType);
}
function deleteUnUsedFiled(){
	if(toDelete && toDelete.length>0){
		for(index in toDelete){
			if(toDelete[index] == "on") continue;
			$.post("cgFormHeadController.do?delField&id="+ toDelete[index]);
		}
	}
	return true;
}
/**
 * 删除行
 */
function delColumnBtnClick() {
	// 获取当前的check的行并进行遍历
	$("#tab_div_database").find("input[name='ck']:checked").parent().parent("tr").each(function(index, ele){
		//$.post("cgFormHeadController.do?delField&id="+ $("#tab_div_database").find("input[name='ck']:checked").val());
		toDelete.push($(this).find("input[name='ck']:checked").val());
		var selectIndex = ele.rowIndex;
		for (var i = 0; i < filedTypes.length; i++) {
			$("#tab_div_" + filedTypes[i]).find("tr").eq(selectIndex).remove();
		}
	})
	for (var i = 0; i < filedTypes.length; i++) {
		resetTrNum("#tab_div_" + filedTypes[i]);
	}
}

/***
 * 同步数据库表数据到页面
 */
function syncDbTableBtnClick(tableName) {
	var lastTableName = jQuery("input[id='tableName']").val();
	if (!lastTableName) {
		tip("请填写表名称！");
		return;
	}
	//获取当前已经存在属性
	var isExitItems = new Array();
	jQuery("table[id='tab_div_database']").find("input[class='fieldNameInput']").each(function() {
		isExitItems.push(jQuery(this).val());
	});
	
	//请求数据
	jQuery.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : "cgAutoListController.do?syncDbTable&tableName=" + lastTableName + "&isExitItems=" + isExitItems.join(","),// 请求的action路径
		error : function() {// 请求失败处理函数
		},
		success : function(data) {
			var d = jQuery.parseJSON(data);
			if (d.success) {
				var dataArray = new Array();
				for (var i = 0; i < d.obj.length; i++) {
					var thisObj = d.obj[i];
					var thisArray = new Array();
					var dataObj = {};
					thisArray.push("<tr><td><a name='rownumber[#rindex#]'></a></td>");
					thisArray.push("<td style='display: none;'><input style='display: none;' name='columns[#index#].id' value=''></td>");
					thisArray.push("<td align='left'><input style='width: 20px;' type='checkbox' name='ck' /></td>");
					thisArray.push("<td align='left'><input style='width: 120px;' type='text' name='columns[#index#].fieldName' nullmsg='请输入字段名称！'")
					thisArray.push(" datatype='s1-20,/^[a-z\d_]+$/' errormsg='字段名称为1到20位且不能有大写字母' class='fieldNameInput'");
					thisArray.push(" value='", thisObj.name, "'/>");
					thisArray.push("<input style='width: 120px;' type='hidden' name='columns[#index#].oldFieldName'/></td>");
					thisArray.push("<td align='left'><input style='width: 120px;' type='text' name='columns[#index#].content' nullmsg='请输入字段注释！'");
					thisArray.push("value='", thisObj.content, "' ");
					thisArray.push("datatype='s2-14' errormsg='字段注释为2到14位' class='contentInput' /></td>");
					thisArray.push("<td align='left'><input name='columns[#index#].length' nullmsg='请输入字段长度！' datatype='n1-4' errormsg='字段长度为1到4位'");
					thisArray.push("maxlength='4' type='text' value='", thisObj.size,"' style='width: 60px;'></td>");
					thisArray.push("<td align='left'><input name='columns[#index#].pointLength' nullmsg='请输入小数点长度！' datatype='n1-2' errormsg='小数点长度为1到2位'");
					thisArray.push("type='text' value='", thisObj.scale, "' style='width: 60px;'></td>");
					thisArray.push("<td align='left'><input name='columns[#index#].fieldDefault' datatype='*1-20' errormsg='默认值最长为20！' ignore='ignore'");
					thisArray.push("type='text' value='", thisObj.fieldDefault, "' style='width: 60px;'></td>");
					thisArray.push("<td align='left'><select name='columns[#index#].type'>");
					thisArray.push("<option value='string'");
					if (thisObj.type === "string") {
						thisArray.push(" ", "selected='selected'");
					} 
					thisArray.push(">String</option>");
					thisArray.push("<option value='double'");
					if (thisObj.type === "double") {
						thisArray.push(" ", "selected='selected'");
					} 
					thisArray.push(">Double</option>");
					thisArray.push("<option value='int'");
					if (thisObj.type === "int") {
						thisArray.push(" ", "selected='selected'");
					}
					thisArray.push(">Integer</option>");
					thisArray.push("<option value='Date'");
					if (thisObj.type === "Date") {
						thisArray.push(" ", "selected='selected'");
					}
					thisArray.push(">Date</option>");
					thisArray.push("<option value='BigDecimal'");
					if (thisObj.type === "BigDecimal") {
						thisArray.push(" ", "selected='selected'");
					}
					thisArray.push(">BigDecimal</option>");
					thisArray.push("<option value='Text'");
					if (thisObj.type === "Text") {
						thisArray.push(" ", "selected='selected'");
					}
					thisArray.push(">Text</option>");
					thisArray.push("<option value='Blob'");
					if (thisObj.type === "Blob") {
						thisArray.push(" ", "selected='selected'");
					}
					thisArray.push(">Blob</option>");
					thisArray.push("</select></td>");
					thisArray.push("<td align='left'><input type='checkbox' style='width: 20px;' name='columns[#index#].isKey'");
					if (thisObj.isKey == "Y") {
						thisArray.push(" ", "checked='checked'");
					}
					thisArray.push("></td>");
					thisArray.push("<td align='left' style='display: none;'><input name='columns[#index#].orderNum' axlength='2' nullmsg='请输入字段顺序！' datatype='n1-2'");
					thisArray.push("errormsg='字段顺序为1到2位' type='hidden' value='0' style='display: none;'></td>");
					thisArray.push("<td align='left'>");
					thisArray.push("<input type='checkbox' style='width: 50px;' name='columns[#index#].isNull'");
					if (thisObj.isNull === "Y") {
						thisArray.push(" checked='checked'>");
					} else {
						thisArray.push(">");
					}
					thisArray.push("</td><td align='left'><input type='checkbox' style='width: 50px;' name='columns[#index#].isAutoItem' onclick='isAutoItemEvent(this);'>");
					thisArray.push("</td><td align='left' style='width: 145px;'>");
					thisArray.push("<input name='columns[#index#].ownerTable' code='ownerTable' maxlength='100'  type='text' value='' style='display: none;'>");
					thisArray.push("</td><td align='left' style='width: 145px;'>");
					thisArray.push("<input name='columns[#index#].ownerItem'  code='ownerItem' maxlength='100' type='text' value='' style='display: none;'>");
					thisArray.push("</td></tr>");
					dataObj.htmlStr = thisArray.join("");
					dataObj.name = thisObj.name;
					dataObj.content = thisObj.content;
					dataArray.push(dataObj);
				}
				if (dataArray && dataArray.length) {
					for (var i = 0; i < dataArray.length; i++) {
						var thisTr = dataArray[i];
						addColumnBtnClick(thisTr.htmlStr, thisTr.name, thisTr.content);
					}
					tip(d.msg);
				} else {
					tip("没有要同步的字段数据！");
				}
			}
		}
	});
}

/**
 * 重设table的order
 * 
 * @param {}
 *            tableId
 */
function resetTrNum(tableId) {
	$(tableId + " tbody tr").each(function(i) {
		$(':input, select,a', this).each(function() {
					var $this = $(this), name = $this.attr('name'), val = $this
							.val();
					if (name != null && name.indexOf("#index#") >= 0) {
						$this.attr("name", name.replace('#index#', i));
						/*if (name.indexOf('orderNum') >= 0) {  Date20131212 liuht取消重置orderNumber
							$this.val(getMaxNum());
						}*/
					} else if (name != null && name.indexOf("_index") >= 0) {
						$this.attr("name", name.replace('_index', i));
					} else if (name != null && name != "ck") {
						$this.attr("name", name.replace(getNowIndex(name), i));
					}
					//代码移动位置，优化调整     Date20131212
					if (name != null && name.indexOf("rownumber") >= 0) {
						$this.html(i+1);   // 移动tr|新加行|删除行 ---重置 rownumber值
					}
					if (name != null && name.indexOf("orderNum") >= 0) {
						$this.val(i+1);   // 移动tr|新加行|删除行 ---重置 orderNumber值
					}
					
				});
	});
	jformTypeChange();
}
/**
 * 获取最大的 orderNum
 */
function getMaxNum() {
	var maxNum = 0;
	$("input[name*='orderNum']").each(function() {
				maxNum = parseInt($(this).val()) > maxNum
						? $(this).val()
						: maxNum;
			});
	return parseInt(maxNum) + 1;
}

/**
 * 同步fieldName
 */
$(document).on('change', '.fieldNameInput','columnsfieldName',valueChange);

/**
 * 同步content
 */
$(document).on('change', '.contentInput','columnscontent',valueChange);


function valueChange(e){
	var val = $(this).val();
	var index = getNowIndex($(this).attr('name'));
	for (var i = 0; i < filedTypes.length; i++) {
		$("#tab_div_" + filedTypes[i]).find("input[name='"+e.data+ index + "']").val(val);
	}
}

/**
 * 获取当前的索引值
 * 
 * @param {}
 *            name 这个元素的Name
 * @return {}
 */
function getNowIndex(name) {
	var s = name.indexOf("[");
	var e = name.indexOf("]");
	if(s>=0 && e>=0){
		return name.substring(s + 1, e);
	} else if(name.indexOf("columnsfieldName")>=0){
		return name.substring(16);
	}else if(name.indexOf("columnscontent")>=0){
		return name.substring(14);   //修改字段备注无法同步问题
	}
}

/**
 * 主键策略的改变,控制序列的输入
 */
function jformPkTypeChange(){
	var pkType = $("#jformPkType").val();
	var $idInput=null;
	$("[name$='fieldName']").each(function(){
		var fieldv = $(this).val();
		if(fieldv && fieldv=="id"){
			$idInput=$(this);
		}
	});
	var $idInput_type=$idInput.parent().parent().find("select[name$='type']");
	var $idInput_length=$idInput.parent().parent().find("input[name$='length']");
	$("#jformPkSequence").val("");
	if(pkType && pkType=="SEQUENCE"){
		$("#jformPkTypeTd").attr("colspan","1");
		$("#jformPkSequenceV").attr("style","");
		$("#jformPkSequenceN").attr("style","");
		$("#jformPkSequence").attr("datatype","*");
		$idInput_type.val("int");
		$idInput_length.val("20");
	}else{
		$("#jformPkSequenceV").attr("style","display: none;");
		$("#jformPkSequenceN").attr("style","display: none;");
		$("#jformPkTypeTd").attr("colspan","3");
		$("#jformPkSequence").removeAttr("datatype");
		if(pkType=="NATIVE"){
			$idInput_type.val("int");
			$idInput_length.val("20");
		}else if(pkType=="UUID"){
			$idInput_type.val("string");
			$idInput_length.val("36");
		}
	}
}
/**
 * 表类型的改变,附表才可以设置主表
 */
function jformTypeChange(){
	openOrCloseSetKeyOp($("#jformType").val() == 3);
	openOrCloseRelationTypeDisplay($("#jformType").val() == 3);
}
//控制：只有附表才可以选择附表关联类型
function openOrCloseRelationTypeDisplay(boo){
	if(boo){
		$("#relation_type_div").attr("style","display: block;");
	}else{
		$("#relation_type_div").attr("style","display: none;");
	}
}

function openOrCloseSetKeyOp(boo){
	$("#tab_div_key tbody tr").each(function(i) {
		$(':input', this).each(function() {
					var $this = $(this), name = $this.attr('name');
					if (name != null && (name.indexOf("mainTable") >= 0
					||name.indexOf("mainField") >= 0)&&
					name!="columns[0].mainTable"&&name!="columns[0].mainField") {
						boo?$this.removeAttr("readonly"):
							$this.attr("readonly","readonly");
					}
				});
	});
}


/**
 * fix修复
 */
function fixTab(){
	$('#tabs').tabs({
	    onSelect:function(title){
	        if(title=="数据库属性"){fix("database");}
	        else if(title=="页面属性"){fix("page");}
	        else if(title=="校验字典"){fix("check");}
	        else if(title=="外键"){fix("key");}
	        $('#tabs .panel-body').css('width','auto');
	    }
	});
	$('#t_table_database').scroll(function(){
 		 $('#tab_div_database_title').css('margin-left',-($('#t_table_database').scrollLeft()));
	});
	$('#t_table_page').scroll(function(){
 		 $('#tab_div_page_title').css('margin-left',-($('#t_table_page').scrollLeft()));
	});
	$('#t_table_check').scroll(function(){
 		 $('#tab_div_check_title').css('margin-left',-($('#t_table_check').scrollLeft()));
	});
	$('#t_table_key').scroll(function(){
 		 $('#tab_div_key_title').css('margin-left',-($('#t_table_key').scrollLeft()));
	});
}

//利用js让头部与内容对应列宽度一致。
function fix(type){
	for(var i=0;i<=$('#tab_div_'+type+' tr:last').find('td:last').index();i++){
  			$('#tab_div_'+type+'_title th').eq(i).css('width',
  				$('#tab_div_'+type+' tr:last').find('td').eq(i).width());
 	}
 	$('#tab_div_'+type+'_title').css('width',
 	 		$('#tab_div_'+type+' tr:last').width());
}



