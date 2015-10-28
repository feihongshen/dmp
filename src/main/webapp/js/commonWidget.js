


!(function($){	

  var Widget = window.Widget = window.Widget || {};

  Widget.Datagrid = {};

/**
* @param conifg - Object, example:
		{
			ajax:{
				url : App.ctx + "/PDA/getSortAndChangeOutQueListPage"
				,params : {
					page : nextPage
					,nextbranchid: getNextBranchId()
				}
			}
			,prefixKey : 'ypdj_lesscwb' // DOM的唯一表示
			,renderId : 'tb_ypdj_lesscwb' //表格渲染到DOM的ID
			,tpl_list : 'tpl_ypdj_lesscwb_list' //模板: 表格-表头数据
			,tpl_rows : 'tpl_ypdj_lesscwb_list_rows' //模板: 表格行数据
			,funcLoadMore : 'getchukucwbquejiandataList' //function name when '查看更多' 点击时候触发
		}
*/
Widget.Datagrid.ajaxLoadDataGrid = function (config){
	$.ajax({
		type:"post",
		url: config.ajax.url,
		data: config.ajax.params,
		success:function(rows){
			if(rows.length>0){

				var nextPage = config.ajax.params.page;

				var contentTableID = config.prefixKey + '_Table'
				 ,loadMoreButtonID = config.prefixKey + '_LoadMore';

				var content_data = _.extend(g_data,{
					domID:{
						contentTable: contentTableID
					}
				});

				if(nextPage == 1){
					var tmp_compiled = _.template($('#' + config.tpl_list).html());
					var render_html = tmp_compiled(content_data);
					$('#' + config.renderId).html(render_html);
				}

				//Render rows
				var row_data = _.extend(g_data,{
					domID:{loadMore : loadMoreButtonID }
					,funcName :{ loadMore : config.funcLoadMore}
					,rows:rows
					,nextPage: ++nextPage
				});
				var tmp_compiled_row = _.template($('#' + config.tpl_rows).html());
				var render_html_row = tmp_compiled_row(row_data);

				$("#" + loadMoreButtonID).remove();
				$("#" + contentTableID).append(render_html_row);

				
			}
		}
	});

}

})(jQuery);