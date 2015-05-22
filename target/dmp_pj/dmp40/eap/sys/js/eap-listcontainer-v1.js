/**********************************************************/
	//add eap list container jquery plug-in
    //author hedy 2015-1-15 13:20
/*********************************************************/
!function ($) {
  "use strict";
 /* EAPCONTAINER PUBLIC CLASS DEFINITION
  * ================================ */
  var EapContainer = function(element, option) {
	  this.$element = $(element);
	  //$.extend({}, $.fn.eapcontainer.defaults, option)
	  this.options = option || []; //OBJECT ARRAY
	  this.firstTrNum = 0;
	  this._show();
  };
  
  EapContainer.prototype = {
	  constructor: EapContainer
	  /*SHOW CONTAINER
	   * ===================================*/
	  , _show: function() {
		  if (this.$element.is("table")) this._dataGrid();
		  else if (this.$element.is("div")) this._div();
		  else alert("请确定容器类型为table/div");
	  }
  	  /*CREATE DATAGRID CONTAINER
  	   * ===================================*/
  	  , _dataGrid: function() {
  		  
  	  }
  	  /*CREATE DIV CONTAINER
  	   * ====================================*/
  	  , _div: function() {
  		  this._drawMetadata2Div();
  	  }
  	  /*DRAW METADATA
  	   * ===================================*/
  	  , _drawMetadata2Div: function() {
  		  var returnJqueryObj = null;
  		  if (this.options && this.options.length) {
  			  var tableObj = $("<table class='table table-striped table-hover table-condensed'></table>");
  			  tableObj.append(this._drawBlankTr2Div());
  			  var thisTrNum = this.firstTrNum;
  			  for (var j = 0; j < this._getTrNumArray().length; j++) {
  				var thisTrJqueryObj = jQuery("<tr></tr>");
  				for (var i = 0; i < this.options.length; i++) {
	  				var thisConfigDef = this.options[i];
	  				var thisTableCoordinateObj = this._tableCoordinate2Div(thisConfigDef.tableCoordinate);
	  				if (j == thisTableCoordinateObj.row) {
	  					thisTrJqueryObj.append("<td label='" + thisConfigDef.fieldName + "' style='border:0px;text-align: right;'>" + thisConfigDef.content + ":</td>");
		  				var contentArray = new Array();
		  				contentArray.push("<td code='", thisConfigDef.fieldName, " ", "'style='border:0px;text-align:left;'", " ");
		  				contentArray.push("rowspan=", thisTableCoordinateObj.rowspan, " ", "colspan=", thisTableCoordinateObj.colspan, ">");
		  				contentArray.push(this._installTd2Div(thisConfigDef));
		  				contentArray.push("</td>");
		  				thisTrJqueryObj.append(contentArray.join(""));
	  				}
  				}
  				thisTrJqueryObj.appendTo(tableObj);
  			  }
  			tableObj.appendTo(this.$element);
  		  }
  	  }
  	  /*DRAW BLANK TR
  	   * ==================================*/
  	  , _drawBlankTr2Div: function() {
  		  var blankTrObj = this._blankTrInfo2Div(); //获取空白行信息对象
  		  var blankTrCount = blankTrObj.firstTrNum || 0; //获取最小行号
  		  var blankTdCount = blankTrObj.blankTdCount || 0; //获取每行单元格和控件label显示td数量
  		  var returnTrArray = new Array(); //声明空白行数组
  		  //画出空白行
  		  for (var i = blankTrCount - 1; i > 0; i--) {
  			  returnTrArray.push("<tr>");
  			  for (var j = 0; j < blankTdCount; j++) returnTrArray.push("<td style='border:0px;'></td>");
  			  returnTrArray.push("</tr>");
  		  }
  		  return returnTrArray.join("");
  	  }
  	  /*GET BLANK TR INFO
  	   * ===================================*/
  	  , _blankTrInfo2Div: function() {
  		  var blankTrInfoObj = {}; //空白行行列信息
  		  var tdCountOfSameTr = 0; //同一行数据单元格数量
  		  var tdLabelCount = 0; //控件label显示td
		  for (var i = 0; i < this.options.length; i++) {
			  var thisConfigDef = this.options[i];
			  var tableCoordinateArray = (thisConfigDef.tableCoordinate || "").split(","); //获取表格坐标
			  if (tableCoordinateArray && tableCoordinateArray.length === 4) {
				  if (i === 0) blankTrInfoObj.firstTrNum = tableCoordinateArray[0]; //获取开发设置最小行号
				  //获取每一行单元格数量 
				  if (blankTrInfoObj.firstTrNum === tableCoordinateArray[0]) {
					  tdCountOfSameTr += tableCoordinateArray[3];
					  tdLabelCount += 1;
				  }
			  }
		  }
		  this.firstTrNum = blankTrInfoObj.firstTrNum;
		  blankTrInfoObj.blankTdCount = tdCountOfSameTr + tdLabelCount;
		  return blankTrInfoObj; //返回空白行数和每行单元格数量
  	  }
  	  /*TABLECOORDINATE GET COORDINATE OBJ
  	   * ===================================*/
  	  , _tableCoordinate2Div: function(tableCoordinate) {
		  var tableCoordinateArray = (tableCoordinate || "").split(","); //获取表格坐标
		  var returnObj = {};
		  if (tableCoordinateArray && tableCoordinateArray.length === 4) {
			  returnObj.row = tableCoordinateArray[0];
			  returnObj.col = tableCoordinateArray[1];
			  returnObj.rowspan = tableCoordinateArray[2];
			  returnObj.colspan = tableCoordinateArray[3];
		  }
		  return returnObj;
  	  }
  	  /*_GETTRNUMARRAY
  	   * ===================================*/
  	  , _getTrNumArray: function() {
  		  var trNumArray = new Array();
  		  if (this.options && this.options.length) {
  			  for (var i = 0; i < this.options.length; i++) {
  				  var thisCoordinate = this._tableCoordinate2Div(this.options[i].tableCoordinate);
  				  if (i === 0) trNumArray.push(thisCoordinate.row);
  				  else if (trNumArray[i - 1] !== thisCoordinate.row) trNumArray.push(thisCoordinate.row);
  			  }
  		  }
  		  return trNumArray;
  	  }
  	  /*INSTALLITEM2DIV
  	   *==================================== */
  	  , _installTd2Div: function(thisConfigDef) {
  		  var contentArray = new Array();
  		  if (thisConfigDef.showType === "input") {
  			contentArray.push("<input type='text' value='", thisConfigDef.fieldDefault || "" ,"' name='", thisConfigDef.fieldName, "'");
  			contentArray.push(" ", "code='",thisConfigDef.fieldName, "' style='margin-bottom:0px; width:99%;'>");
  		  } else if (thisConfigDef.showType === "button") {
  			contentArray.push("<div class='btn btn-default' code='", thisConfigDef.fieldName, "'>");
  			contentArray.push("<i class='icon-", thisConfigDef.icon || "th", "'></i>", thisConfigDef.content, "</div>");
  		  } else if (thisConfigDef.showType === "radio") {
  			  
  		  } else if (thisConfigDef.showType === "checkbox") {
  			  
  		  } else if (thisConfigDef.showType === "select") {
  			  
  		  } else if (thisConfigDef.showType === "textarea") {
  			contentArray.push("<textarea", " ", "code='", thisConfigDef.fieldName, "'", " ", "name='", thisConfigDef.fieldName, "'");
  			contentArray.push(" ", "style='margin-bottom:0px; width:99%;'>", thisConfigDef.content, "</textarea>");
  		  }
  		  return contentArray.join("");
  	  }
  };
  
  /* EAPCONTAINER PLUGIN DEFINITION
   * ============================== */
   $.fn.eapcontainer = function () {
	   var resultData = $("body").data("resultData");
	   if (!resultData) {
		   var urlStr = getUrl();
		   if (urlStr) {
			   $.ajax({
				   async: false
				   , cache: false
				   , type: "POST"
				   , dataType: "json" 
				   , url: getUrl()// 请求的action路径
				   , error: function(msg) {// 请求失败处理函数
					   throw new Error(msg.responseText || "error");
				   }
				   , success: function(data) { //请求成功
					   resultData = {};
			           resultData = data;
			           if (typeof data === "string") {//判断返回数据类型
			        	   resultData = $.parseJSON(data);         
			           }
			           try {
						 $("body").data("resultData", resultData);
					   } catch (e) {
		                 alert("数据异常：" + e);
					   }  
				   }
			   });
		   }
	   }
	   return this.each(function() {
		   var $this = $(this)
		   , data = $this.data('eapcontainer');
		   if ($this.attr("container")) { //是容器属性
			   var option = null; //容器属性对象
			   for(var i in resultData) {
				   if (i === $this.attr("id")) {
					   option =  resultData[i];
					   break;
				   }
			   }
			   if (!data) $this.data('eapcontainer', (data = new EapContainer(this, option)));
		   }
	   });
   };

   $.fn.eapcontainer.defaults = {
     //
   };

   $.fn.eapcontainer.Constructor = EapContainer;

 /* COLLAPSIBLE DATA-API
  * ==================== */
  $(function () {
	  var $this = $(".container");
	  $this.eapcontainer();
  });
}(window.jQuery);