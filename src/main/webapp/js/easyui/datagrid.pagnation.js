(function ($) {
    $.DgPage = function () { };
    $.DgPage.defaults = {
    	_dgId:            "datagrid",
      	_formId:          "searchForm",
      	_url:             "",
      	_pageParamName:	  "page",
      	_pageSizeParamName: "pageSize"
      	//_formatDgPageData :  this.formatDgPageData 	
    };
    $.extend($.DgPage,
        
        { 
    		init: function (param) { 
    			if(param!=null){
	    			_dgId = param.dgId || _dgId;
	    			_formId = param.formId || _formId;
	    			_url = param.url || _url;
	    			_pageParamName = param.pageParamName || "page";
	    			_pageSizeParamName = param.pageSizeParamName || "pageSize";
	    			//_formatDgPageData = param.formatDgPageData || _formatDgPageData;
	    			var $dg = $('#'+_dgId);
	    			$dg.datagrid({ 
	    	            onLoadSuccess: function (data) {
	    	            	$dg.datagrid('loaded');
	    	            } 
	    	        });
	    			if(param.loadNow){
	    				this.getDataByPage(1,null,3);
	    			}
    			}
        	},
        	
	    	getDataByPage: function (pageNum, rowsLimit){  
	    	    pageNum = pageNum || 1;     // 设置默认的页号  
	    	    rowsLimit = rowsLimit || 10;// 设置默认的每页记录数  
	    	    var postData = $('#'+_formId).serialize();
	    	    var $dg = $('#'+_dgId);
	    	    var pageQueryStr = "&"+_pageParamName+"="+pageNum+"&"+_pageSizeParamName+"="+rowsLimit;
	    	    postData = postData+pageQueryStr;
	    	    $dg.datagrid("loading");
	    	    $.ajax({
	    			type: "POST",
	    			url:_url,
	    			data: postData, 
	    			dataType:"json",
	    			success : function(data) {
	    				var dgPageData = $.DgPage.formatDgPageData(data);
	    				$dg.datagrid("loadData" , dgPageData) ;
	    				$dg.datagrid("getPager").pagination({
	    	    			onSelectPage : function(pageNumber, pageSize) {
	    	    				$.DgPage.getDataByPage(pageNumber, pageSize);
	    	    			}
	    	    		});
	    			},
	    			complete:function(){
	    			}
	    		});
	    	          
	    	},
	    	//API获取数据后默认的数据处理方式
	    	formatDgPageData : function (data){
	    		return {
	    			total:data.total ,
    	    		rows: data.list
	    		};
	    	},
        	//当form里的参数发生变化时调用的方法
        	searchPage: function(){
        		var pageSize = $('#'+_dgId).datagrid('getPager').data("pagination").options.pageSize;
        		this.getDataByPage(1, pageSize);
        	}
    	}
    );
})(jQuery);

//jquery.datagrid 扩展,
(function (){
$.extend($.fn.datagrid.methods, {
	//ajax请求前，显示遮罩
	loading: function(jq){
		return jq.each(function(){
			$(this).datagrid("getPager").pagination("loading");
			var opts = $(this).datagrid("options");
			var wrap = $.data(this,"datagrid").panel;
			if(opts.loadMsg)
			{
				$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:wrap.width(),height:wrap.height()}).appendTo(wrap);
				$("<div class=\"datagrid-mask-msg\"></div>").html(opts.loadMsg).appendTo(wrap).css({display:"block",left:(wrap.width()-$("div.datagrid-mask-msg",wrap).outerWidth())/2,top:(wrap.height()-$("div.datagrid-mask-msg",wrap).outerHeight())/2});
			}
		});
	},
	//加载完成，隐藏遮罩
	loaded: function(jq){
		return jq.each(function(){
			$(this).datagrid("getPager").pagination("loaded");
			var wrap = $.data(this,"datagrid").panel;
			wrap.find("div.datagrid-mask-msg").remove();
			wrap.find("div.datagrid-mask").remove();
			});
		}
	});
})(jQuery); 