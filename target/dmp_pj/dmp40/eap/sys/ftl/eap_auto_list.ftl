${config_iframe}
<#--获取容器对象-->
<#if containerMap??> <#--容器数据存在-->
	<div class="accordion" id="${config_id}_accordion">
		<#list containerMap?keys as key>
			<#-- 查询区域-->
			 <div class="accordion-group" id="${config_id}_${key}_query">
			 	 <div class="accordion-heading" id="${config_id}_${key}_query_head">
			 	 	 <a class="accordion-toggle" data-toggle="collapse" data-parent="#${config_id}_accordion" href="#${config_id}_${key}_query_body">area1</a>
			 	 </div>
			 	  <div id="${config_id}_${key}_query_body" class="accordion-body in collapse" style="height: auto;">
			 	  	<div class="accordion-inner">
		 	  			<#--查询区域-->
			 	  	</div>
			 	  </div>
			 </div>
			 <#-- 按钮区域-->
			 <div class="accordion-group" id="${config_id}_${key}_button">
			 	 <div class="accordion-heading" id="${config_id}_${key}_button_head">
			 	 	 <a class="accordion-toggle" data-toggle="collapse" data-parent="#${config_id}_accordion" href="#${config_id}_${key}_button_body">area1</a>
			 	 </div>
			 	  <div id="${config_id}_${key}_button_body" class="accordion-body in collapse" style="height: auto;">
			 	  	<div class="accordion-inner">
		 	  			<#--按钮区域-->
		 	  			<#if containerMap.get(key)??>
		 	  				<#list containerMap.get(key) as containerObj>
			 	  					<#if containerObj.type == "button">
			 	  						<div class="btn btn-default" code="${containerObj.code}" style="margin-right:5px;">
			 	  							<i class="icon-${containerObj.icon}"></i>${containerObj.name}
			 	  						</div>		
			 	  					</#if>
		 	  					</div>
		 	  				</#list>
		 	  			</#if>
			 	  	</div>
			 	  </div>
			 </div>
			 
			 <#-- 列表区域-->
			 <div class="accordion-group" id="${config_id}_${key}_list">
			 	 <div class="accordion-heading" id="${config_id}_${key}_list_head">
			 	 	 <a class="accordion-toggle" data-toggle="collapse" data-parent="#${config_id}_accordion" href="#${config_id}_${key}_list_body">area1</a>
			 	 </div>
			 	  <div id="${config_id}_${key}_button_body" class="accordion-body in collapse" style="height: auto;">
			 	  	<div class="accordion-inner">
		 	  			<#--列表区域-->
		 	  			<#if containerMap.get(key)??>
		 	  				<#list containerMap.get(key) as containerObj>
			 	  					<#if containerObj.listShow == "true">
			 	  						<#assign configDef>
			 	  							
			 	  						</#assign>			
			 	  					</#if>
		 	  					</div>
		 	  				</#list>
		 	  			</#if>
			 	  	</div>
			 	  </div>
			 </div>
		</#list>
	</div>
</#if>
<script type="text/javascript">
	<#--数据初始化-->
	(function(){
		createDataGrid${config_id}();
	}());
	
	<#--初始化列表-->
	function createDataGrid${config_id}(containerObj, configDef){
		containerObj.datagrid({
			var initUrl = encodeURI(configDef.url);
			$('#t_demoList').datagrid({
				url:initUrl,
				idField: configDef.idField,
				title: configDef.title,
				fit:configDef.fit || true,
				fitColumns:configDef.fitColumns || false,
				pageSize: configDef.pageSize || 10,
				pagination:configDef.pagination || true,
				singleSelect:configDef.singleSelect || true,
				sortName:configDef.sortName,
				pageList:configDef.pageList || [10,30,50,100],
				sortOrder:configDef.sortOrder || 'desc',
				rownumbers:configDef.rownumbers || true,
				showFooter:configDef.showFooter || true,
				frozenColumns:configDef.frozenColumns || [[]],
				columns:[[
						 {	field:'id',
						 	title:'主键',
						 	hidden:true,
						 	sortable:true,
						 	width:120
						 },
						 {	field:'username',
						 	title:'用户名',
						 	sortable:true,
						 	width:120
						 },
						 {	field:'password',
						 	title:'密码',
						 	sortable:true,
						 	width:120
						 },
						 {	field:'sex',
						 	title:'性别',
						 	sortable:true,
						 	width:120
						 },
						{	field:'opt',
							title:'操作',
							width:200,
							formatter:function(value,rec,index){
								if(!rec.id){
									return '';
								}
								var href='';
								href+="[<a href='#' onclick=delObj('cgAutoListController.do?del&configId=t_demo&id="+rec.id+"','t_demoList')>";
								href+="删除</a>]";
								return href;
							}
						}
					]],
				});
				<#--$('#t_demoList').datagrid('getPager').pagination({beforePageText:'',afterPageText:'/{pages}',displayMsg:'{from}-{to}共{total}条',showPageList:true,showRefresh:true});
				$('#t_demoList').datagrid('getPager').pagination({onBeforeRefresh:function(pageNumber, pageSize){ $(this).pagination('loading');$(this).pagination('loaded'); }});-->
	}
</script>
<#-- 添加扩展js引用标签-->
<#list listJs as jsObj>
	${jsObj}
</#list>