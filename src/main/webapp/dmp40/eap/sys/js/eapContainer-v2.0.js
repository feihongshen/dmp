/**
 * begin
 */
!function ($) {
   var Container = function(element, option) {
       constructor: Container;
       this.$element = $(element);
       this.options = option || [];
       this.maxColumnCount = 0;
       this.maxRowCount = 0;
       this.dataGridUrl = null;
       this._show();
   };
    /**
     *Container  prototype
     * @type {{_show: Function, _table: Function, _div: Function}}
     */
   Container.prototype = {
       container: Container,
       _show: function() {
          if (this.$element.is("table")) { //grid
              this._table();
          } else if (this.$element.is("div")) { //div
        	  if (this.$element.attr("type") === "button") this._button();
        	  else this._div();
          }
       }
   	   , _button: function() {
   		   var btnArray = new Array();
   		   for (var i = 0; i < this.options.length; i++) {
   			   var thisObj = this.options[i];
   			   btnArray.push("<div class='btn btn-default' code='" + thisObj.code + "' style='margin-right:5px;' id='" + thisObj.code + "'>"
   					   +"<i class='icon-" + thisObj.icon + "'></i>" + thisObj.name + "</div>");
   		   }
   		   this.$element.append(btnArray.join(""));
   	   }
       , _table: function() {
    	   this.dataGridUrl = this.$element.attr("url");
    	   this.dataGridPkCode = this.$element.attr("pkCode");
    	   this.dataGridTitle = this.$element.attr("title");
    	   this.dataGridWidth = this.$element.attr("width");
    	   this.dataGridHeight = this.$element.attr("height");
    	   this.dataGridCk = this.$element.attr("ck");
    	   this.$element.datagrid({
    		   url:this.dataGridUrl,
    		   idField: this.dataGridPkCode,
    		   title: this.dataGridTitle,
    		   fit:false,
    		   width: this.dataGridWidth || 'auto',
    		   height: this.dataGridHeight || 'auto',
    		   fitColumns:false,
    		   pageSize: 10,
    		   pagination:true,
    		   singleSelect:this.dataGridSingleSelect,
    		   sortName:this.dataGridPkCode,
    		   pageList:[10,30,50,100],
    		   sortOrder:'desc',
    		   rownumbers:true,
    		   showFooter:true,
    		   frozenColumns:[[]],
    		   columns:this._getTableColumns()
    	   });
    	   this.$element.datagrid('getPager').pagination({
    		   pageSize: 10,//每页显示的记录条数，默认为10 
	   	       pageList: [10,30,50,100],//可以设置每页记录条数的列表 
	   	       beforePageText: '第',//页数文本框前显示的汉字 
	   	       afterPageText: '页    共 {pages} 页', 
	   	       displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
    		   showPageList:true,
    		   showRefresh:true,
    		   onBeforeRefresh:function(pageNumber, pageSize){
    			   $(this).pagination('loading');
    			   $(this).pagination('loaded'); 
    		   }
    	   });
       }
       , _getTableColumns: function() {
    	   var columns = new Array();
    	   if (!this.dataGridCk || this.dataGridCk=="true") {
    		   columns.push({
        		   field:'ck',checkbox:true
        	   });
    	   }
    	   columns.push({
	    	   field:this.dataGridPkCode,
	    	   title:"主键",
	    	   hidden:true
	       });
    	   for (var i = 0; i < this.options.length; i++) {
    		   var thisColObj = this.options[i];
    		   var dataGridObj = {};
    		   dataGridObj.field = thisColObj.id;
    		   dataGridObj.title = thisColObj.name;
    		   dataGridObj.hidden = thisColObj.listShow == "Y" ? false : true;
    		   dataGridObj.width = thisColObj.width || 100;
    		   dataGridObj.sortable = true;
    		   columns.push(dataGridObj);
    	   }
    	   var returnArray = new Array();
    	   returnArray.push(columns);
    	   return returnArray;
       }
       , _div: function() {
           this._drawDiv();
       }
       , _drawDiv: function() {
           this._getTableMaxRowMaxCol();
           this._drawBlankTable();
           this._drawDataTable();

       }
       , _drawDataTable: function() {
           var _self = this;
           for (var i = 0; i < _self._getDataRows().length; i++) {
               var thisRowObj = _self._getDataRows()[i];
               var thisRowData = thisRowObj.rowData;
               var thisRowNum = thisRowObj.rowNum;
               var thisRowDataLen = thisRowData.length;
               var maxColCount = _self.maxColumnCount;
               var thisRowColCount = 0;
               var useColSpan = 0;
               var usedColSpan = 0;
               for (var j = 0; j < thisRowDataLen; j++) {
                   thisRowColCount +=  _self._getTableCoordinate(thisRowData[j].tableCoordinate).colspan;
               }
               for (var j = 0; j < thisRowDataLen; j++) {
            	   //if (j === thisRowDataLen - 1) debugger;
                   var thisTdData = thisRowData[j];
                   var thisTdTableCoordinate = _self._getTableCoordinate(thisTdData.tableCoordinate);
                   usedColSpan +=  useColSpan;
                   if (j === thisRowDataLen - 1) useColSpan =  maxColCount - thisRowDataLen - usedColSpan;
                   else useColSpan = Math.ceil(thisTdTableCoordinate.colspan / (thisRowColCount - usedColSpan) * (maxColCount - thisRowDataLen - usedColSpan));
                   this.$element.find("td[used='false']").each(function() {
                       var rowNum = $(this).attr("row");
                       var colNum = $(this).attr("col");
                       var _thisItem = $(this);
                       if (rowNum == thisRowNum) { //in same tr
                           $(this).append(thisTdData.name + "：").attr("used", "true").css({"text-align":"right", "vertical-align": "middle"});
                           var nextObj =  null;
                           //add colspan
                           if (thisTdTableCoordinate.colspan === 1)  {
                               nextObj = $(this).next();
                               if (nextObj)
                            	   nextObj.append(_self._drawDataItem(thisTdData)).attr("used", "true").css({"vertical-align": "middle"});
                           }
                           else {
                               nextObj = $(this).nextAll().eq(0);
                               if (nextObj) {
	                               nextObj.append(_self._drawDataItem(thisTdData)).attr("colspan", useColSpan).css({"vertical-align": "middle"});
	                               var nextAllObjs = $(this).nextAll();
	                               for (var k = 1; k < useColSpan; k++) {
	                            	   if (nextAllObjs[k]) nextAllObjs[k].remove();
	                               }
	                               nextObj.attr("used", "true");
                               }
                           }
                       }
                       if (nextObj)
                    	   nextObj.attr("rowspan", thisTdTableCoordinate.rowspan);
                       $(this).attr("rowspan", thisTdTableCoordinate.rowspan);
                        var useTr = thisTdTableCoordinate.rowspan - 1 + parseInt(rowNum);
                        for (var i = useTr; i > parseInt(rowNum); i--) {
                            $(this).parent().nextAll().each(function() {
                                var thisRow = $(this).attr("row");
                                if (thisRow >= i) {
                                    $(this).find("td").each(function() {
                                        if (parseInt($(this).attr("col")) >= parseInt(colNum)
                                        		&& parseInt($(this).attr("col")) <= parseInt(colNum) + useColSpan) {
                                            $(this).remove();
                                        }
                                    });
                                }
                            });
                        }
                       return false;
                   });
               }
           }
       }
       , _getDataRows: function() {
           var rowList = new Array();
           if (this.options && this.options.length) {
               for (var i = 0; i < this._getRowsNum().length; i++) {
                   var thisRowNum =  this._getRowsNum()[i];
                   var row = new Array();
                   for (var j = 0; j < this.options.length; j++) {
                       if (thisRowNum === this._getTableCoordinate(this.options[j].tableCoordinate).row) {
                           row.push(this.options[j]);
                       }
                   }
                   rowList.push({"rowNum": thisRowNum, "rowData": row});
               }
           }
           return rowList;
       }
       , _quickSort: function(list) {
    	   var length = list.length;
    	   for (var i = 1; i < length; i++) {
    	   	  for (var j = 0; j < length - i; j++) {
    	   		  var listIndex = list[j];
    	   		  var listNextIndex = list[j + 1];
    	   		  if (listIndex > listNextIndex) {
    	   			  var temp = list[j];
    	   			  list[j] = list[j + 1];
    	   			  list[j + 1] = temp;
    	   		  }
    	   	  }
    	   }
       } 
       , _drawDataItem: function(itemDef) {
           var itemArray = new Array();
           if (itemDef.type === "text") {
               itemArray.push("<div>");
               itemArray.push("<input type='text' value='' id='" + itemDef.id + "' name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("</div>");
           } else if (itemDef.type === "date") {
        	   itemArray.push("<div>");
               itemArray.push("<input type='text' value='' id='" + itemDef.id + "' name='" + itemDef.id + "' code='" + itemDef.id + "' class='Wdate'" + " onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\""
            		   + " style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("</div>");
           } else if (itemDef.type === "datetime") {
        	   itemArray.push("<div>");
               itemArray.push("<input type='text' value='' id='" + itemDef.id + "' name='" + itemDef.id + "' code='" + itemDef.id + "' class='Wdate'" + " onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:dd'})\""
            		   + " style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("</div>");
           } else if (itemDef.type === "password") {
        	   itemArray.push("<div>");
               itemArray.push("<input type='password' id='" + itemDef.id + "' value='' name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("</div>");
           } else if (itemDef.type === "radio") {
               itemArray.push("<div>");
               itemArray.push("<div style='width: 100%; height: 26px;margin-top: -3px;'>");
               if (itemDef.dictData && itemDef.dictData.length) {
            	   for (var i = 0; i < itemDef.dictData.length; i++) {
            		   var thisDictData = itemDef.dictData[i];
            		   itemArray.push("<label class='radio inline'>", "<input type='radio' id='" + itemDef.id + "' name='", itemDef.id);
            		   itemArray.push("' value='", thisDictData.TYPECODE, "'>");
            		   itemArray.push("<span style='display: inline-block;'>", thisDictData.TYPENAME, "</span>");
            		   itemArray.push("</label>"); 
            	   }
               }
               itemArray.push("</div>");
               itemArray.push("</div>");
           } else if (itemDef.type === "checkbox") {
               itemArray.push("<div>");
               itemArray.push("<div style='width: 100%; height: 26px;margin-top: -3px;'>");
               if (itemDef.dictData && itemDef.dictData.length) {
            	   for (var i = 0; i < itemDef.dictData.length; i++) {
            		   var thisDictData = itemDef.dictData[i];
            		   itemArray.push("<label class='checkbox inline'>", "<input type='checkbox' id='" + itemDef.id + "' name='", itemDef.id);
            		   itemArray.push("' value='", thisDictData.TYPECODE, "'>");
            		   itemArray.push("<span style='display: inline-block;'>", thisDictData.TYPENAME, "</span>");
            		   itemArray.push("</label>"); 
            	   }
               }
               itemArray.push("</div>");
               itemArray.push("</div>");
           } else if (itemDef.type === "list") {
               itemArray.push("<div>");
               itemArray.push("<select id='" + itemDef.id + "' value='' name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("<option value='' selected='selected'>---请选择---</option>");
               if (itemDef.dictData && itemDef.dictData.length) {
            	   for (var i = 0; i < itemDef.dictData.length; i++) {
            		   var thisDictData = itemDef.dictData[i];
            		   itemArray.push("<option value='" + thisDictData.TYPECODE + "'>", thisDictData.TYPENAME, "</option>");  
            	   }
               }
               itemArray.push("</select>");
               itemArray.push("</div>");
           } else if(itemDef.type === "textarea") {
               itemArray.push("<div>");
               itemArray.push("<textarea id='" + itemDef.id + "' name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'></textarea>");
               itemArray.push("</div>");
           }
           return itemArray.join("");
       }
       , _drawBlankTable: function() {
           var tableArray = new Array();
           tableArray.push("<table class='table table-striped table-hover table-condensed' style='border: 0px;'>");
           for (var i = 1; i <= this.maxRowCount; i++) {
               tableArray.push("<tr row='" + i + "'>");
               for (var j = 1; j <= this.maxColumnCount; j++) {
                   tableArray.push("<td col='" + j + "' row='" + i + "' used='false' style='border:0px;'></td>");
               }
               tableArray.push("</tr>");
           }
           tableArray.push("</table>");
           this.$element.append(tableArray.join(""));

       }
       , _getTableMaxRowMaxCol: function() {
           var maxColCount = 0;
           for (var i = 0; i < this._getRowsNum().length; i++) {
               var thisRowCount = 0, thisColCount = 0;
               for (var j = 0; j < this.options.length; j++) {
                   var thisObj = this.options[j];
                   var thisTableCoordinate = this._getTableCoordinate(thisObj.tableCoordinate);
                   if (thisTableCoordinate.row === this._getRowsNum()[i]) {
                       thisColCount += thisTableCoordinate.colspan + 1;
                       thisRowCount = thisRowCount > 1 ? thisRowCount : 1;
                   }
               }
               if (i === 0) thisRowCount += this._getBlankRows(0, this._getRowsNum()[i]);
               else thisRowCount += this._getBlankRows(this._getRowsNum()[i - 1], this._getRowsNum()[i]);
               this.maxRowCount += thisRowCount;
               maxColCount = thisColCount > maxColCount ? thisColCount : maxColCount;
           }
           this.maxColumnCount = maxColCount;
       }
       , _getRowsNum: function() {
           var rowsNum = new Array();
           if (this.options && this.options.length) {
               for (var i = 0; i < this.options.length; i++) {
            	   var isExit = false;
            	   var tableCoordinate = this._getTableCoordinate(this.options[i].tableCoordinate);
            	   for (var j = 0; j < rowsNum.length; j++) {
            		   if (rowsNum[j] == tableCoordinate.row) {
            			   isExit = true;
            		   }
            	   }
            	   if (!isExit) {
            		   rowsNum.push(tableCoordinate.row);
            	   }
               }
           }
           this._quickSort(rowsNum);
           return rowsNum;
       }
       , _getBlankRows: function(prvrow, row) {
           return (row - prvrow) > 1 ? (row - prvrow - 1) : 0;
       }
       , _getTableCoordinate: function(tableCoordinate) {
           var tableCoordinateObj = {
               "row": 0,
               "col": 0,
               "rowspan": 0,
               "colspan": 0
           };
           if (tableCoordinate) {
               var tableCooArray = tableCoordinate.split(",");
               if (tableCooArray && tableCooArray.length === 4) {
                   tableCoordinateObj.row = parseInt(tableCooArray[0]);
                   tableCoordinateObj.col = parseInt(tableCooArray[1]);
                   tableCoordinateObj.rowspan = parseInt(tableCooArray[2]);
                   tableCoordinateObj.colspan = parseInt(tableCooArray[3]);
                   return tableCoordinateObj;
               }
           }
           return tableCoordinateObj;
       }
   };

    /**
     * jquery  container
     * @param option 参数
     * @returns {*} 当前对象
     */
    $.fn.container = function(option) {
        return this.each(function() {
            var $this = $(this),
            data = $this.data("container");
            if ($this.is("table") || $this.is("div")) {
                if (!data) {
                    for (var i  in option) {
                        var thisData = option[i];
                        if (i === $this.attr("id")) { //查找对应区域
                            $this.data("container", (data = new Container(this, thisData)));
                        }
                    }
                }
            }
        });
    };

    $.fn.constructor.defaults = {

    };

    $.fn.collapse.Constructor = Container;

    $(function() {
        var resultData = $("body").data("resultData");
 	   	if (!resultData) {
 		   var urlStr = getUrl();
 		   if (urlStr) {
 			   $.ajax({
 				   async: false
 				   , cache: false
 				   , type: "POST"
 				   , dataType: "json" 
 				   , url: urlStr// 请求的action路径
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
        var $that = $(".eap-container");
        $that.container(resultData.obj);
    });
}(window.jQuery)