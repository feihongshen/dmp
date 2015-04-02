/**
 * begin
 */
!function ($) {
   var Container = function(element, option) {
       constructor: Container;
       this.$element = $(element);
       this.options = option || [];
       this.maxColumnCount = 0;
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
              this._div();
          }
       }
       , _table: function() {

       }
       , _div: function() {
           this._getMaxColumnInRow();
           this._drawDiv();
       }
       ,_drawDiv: function() {
           var rowData = this._getDataRows();
           if (rowData.length) {
               var drawTableArray = new Array();
               drawTableArray.push("<table class='table table-striped table-bordered table-hover table-condensed'>");
               for (var i = 0; i < rowData.length; i++) {
                   var thisRow = rowData[i];
                   if (i === 0) {
                       drawTableArray.push(this._drawBlankRows(1, thisRow.thisRowNum));
                   } else {
                       drawTableArray.push(this._drawBlankRows(rowData[i - 1].thisRowNum, thisRow.thisRowNum));
                   }
                   drawTableArray.push("<tr>");
                   var thisRowLabelCols = thisRow.rowData.length;
                   var maxColCount = this.maxColumnCount;
                   var thisRowColCount = 0;
                   var useColSpan = 0;
                   var usedColSpan = 0;
                   for (var j = 0; j < thisRowLabelCols; j++) {
                       thisRowColCount +=  parseInt(this._getTableCoo(thisRow.rowData[j].zb).colspan);
                   }
                   for (var j = 0; j < thisRowLabelCols; j++) {
                       var thisRowObj = thisRow.rowData[j];
                       drawTableArray.push("<td style='text-align: right;'>", thisRowObj.name ,"：</td>");
                       var thisColCount =  this._getTableCoo(thisRowObj.zb).colspan;
                       //last one
                       usedColSpan +=  useColSpan;
                       if (j === thisRowLabelCols - 1) useColSpan =  maxColCount - thisRowLabelCols - usedColSpan;
                       else useColSpan = Math.ceil(thisColCount / (thisRowColCount - usedColSpan) * (maxColCount - thisRowLabelCols - usedColSpan));
                       drawTableArray.push("<td code='" + thisRowObj.id + "' colspan='" + useColSpan + "'");
                       drawTableArray.push("rowspan='" + this._getTableCoo(thisRowObj.zb).rowspan+ "'>", this._drawItem(thisRowObj), "</td>");
                   }
                   drawTableArray.push("</tr>");
               }
               drawTableArray.push("</table>");
               this.$element.append(drawTableArray.join(""));
           }
       }
       , _drawItem: function(itemDef) {
           var itemArray = new Array();
           if (itemDef.type === "input") {
               itemArray.push("<div>");
               itemArray.push("<input type='text' value='' name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'>");
               itemArray.push("</div>");
           } else if (itemDef.type === "radio") {
               itemArray.push("<div>");
               //TODO
               itemArray.push("</div>");
           } else if (itemDef.type === "checkbox") {
               itemArray.push("<div>");
               //TODO
               itemArray.push("</div>");
           } else if (itemDef.type === "select") {
               itemArray.push("<div>");
               //TODO
               itemArray.push("</div>");
           } else if(itemDef.type === "textarea") {
               itemArray.push("<div>");
               itemArray.push("<textarea name='" + itemDef.id + "' code='" + itemDef.id + "' style='margin-bottom:0px;width:100%;padding-left:0px;padding-right:0px;'></textarea>");
               itemArray.push("</div>");
           }
           return itemArray.join("");
       }
       , _drawBlankRows: function(provIndex, index) {
           var blankRowCount = index - provIndex;
           var blankRow = new Array();
           if (blankRowCount > 1) {
               for (var i = 0; i < blankRowCount; i++) {
                   blankRow.push("<tr>");
                   for (var j = 0; j < this.maxColumnCount; j++) {
                       blankRow.push("<td style='border: 0px;'></td>");
                   }
                   blankRow.push("</tr>");
               }
           }
           return blankRow.join("");
       }
       , _getRowsNum: function() {
           var rowsNum = new Array();
           if (this.options && this.options.length) {
               for (var i = 0; i < this.options.length; i++) {
                   if (i === 0) rowsNum.push(this._getTableCoo(this.options[i].zb).row);
                   else if (this._getTableCoo(this.options[i].zb).row !== rowsNum[rowsNum.length -1]) rowsNum.push(this._getTableCoo(this.options[i].zb).row);
               }
           }
           return rowsNum;
       }
       , _getDataRows: function() {
           var rowList = new Array();
           if (this.options && this.options.length) {
               for (var i = 0; i < this._getRowsNum().length; i++) {
                   var thisRowNum =  this._getRowsNum()[i];
                   var row = new Array();
                   for (var j = 0; j < this.options.length; j++) {
                       if (thisRowNum === this._getTableCoo(this.options[j].zb).row) {
                           row.push(this.options[j]);
                       }
                   }
                   rowList.push({"thisRowNum": thisRowNum, "rowData": row});
               }
           }
           return rowList;
       }
       , _getMaxColumnInRow: function() {
           var maxColumnCount = 0;
           for (var i = 0; i < this._getDataRows().length; i++) {
               var thisRow =  this._getDataRows()[i];
               var thisColumnCount = thisRow.rowData.length;
               for (var j = 0; j < thisRow.rowData.length; j++) {
                   thisColumnCount += parseInt(this._getTableCoo(thisRow.rowData[j].zb).colspan);
               }
               maxColumnCount = maxColumnCount >  thisColumnCount ? maxColumnCount : thisColumnCount;
           }
           this.maxColumnCount =  maxColumnCount;
           return maxColumnCount;
       }
       , _getTableCoo: function(tableCoo) {
           if (tableCoo) {
               var tableCooArray = tableCoo.split(",");
               if (tableCooArray && tableCooArray.length === 4) {
                   return {
                       "row": tableCooArray[0],
                       "column": tableCooArray[1],
                       "rowspan": tableCooArray[2],
                       "colspan": tableCooArray[3]
                   };
               }
           }
           return null;
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
        var dataObj = $("body").data("container");
        if (!dataObj)
            $("body").data("container", (dataObj = {
                "div1": [
                    {"id": "kpCode", "type": "input", "name":"主键", "isShow":false, "zb": "1,1,1,1"}
                    ,{"id": "name", "type": "input", "name": "姓名", "isShow":true, "zb": "1,2,1,1"}
                    ,{"id": "age", "type": "input", "name": "年龄1", "isShow": true, "zb":"1,3,1,1"}
                    ,{"id": "age", "type": "input", "name": "年龄2", "isShow": true, "zb":"1,4,1,1"}
                    ,{"id": "sex", "type":"radio", "name": "性别", "isShow":true, "zb": "2,1,1,2", "dictData":
                        {"Y":"男", "N":"女"}
                    }
                    ,{"id": "gj", "type":"select", "name":"国籍", "isShow": true, "zb": "2,2,1,2"}
                    ,{"id": "address", "type":"textarea", "name": "地址", "isShow":true, "zb": "3,1,1,4"}
                    ,{"id": "memo", "type": "textarea", "name": "备注", "isShow": true, "zb": "4,1,1,4"}
                ]
            }));
        var $that = $(".eap-container");
        $that.container(dataObj);
    });
}(window.jQuery)