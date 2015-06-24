/**
 * 页面缓存
 */
var dg;//dataGrid
var d;//dialog

/**
 * 初始渲染
 */
$(function(){ 
	dg=$('#dg').datagrid({    
		method: "POST",
	    url:App.ctx + '/csPushSms/json', 
	    fit : true,
		fitColumns : true,
		border : true,
		striped:true,
		idField : 'id',
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
			{field:'id',title:'id',hidden:true},  
			{field:'cwbOrderNo',title:'订单号',sortable:true,width:25},
			{field:'workOrderNo',title:'工单号',sortable:true,width:25},
	        {field:'complaintState',title:'工单状态',sortable:true,width:10,formatter:complaintStateFormatter},    
	        {field:'handler',title:'工单创建人',sortable:15,formatter:useridFormatter},
	        {field:'complianBranchId',title:'责任机构',sortable:true,width:15,formatter:branchFormatter},
	        {field:'complianUserName',title:'责任人',sortable:true,width:15,formatter:usernameFormatter},
	        {field:'sendTime',title:'短信发送时间',sortable:true,width:20},
	        {field:'smsContent',title:'短信内容',sortable:true,width:60},
	        {field:'mobileNo',title:'收信手机号',sortable:true,width:15}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: true,
	    rowTooltip: true,
	    toolbar:'#tb'
	});
});

/**
 * dataGrid新增方法
 */
function add() {
	d=$("#dlg").dialog({   
	    title: '添加用户',    
	    width: 380,    
	    height: 250,    
	    href:App.ctx + '/csPushSms/add',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform").submit(); 
			}
		},{
			text:'取消',
			handler:function(){
					d.panel('close');
				}
		}]
	});
}
		
/**
 * dataGrid删除方法
 */
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:App.ctx + '/csPushSms/delete/' + row.id,
				success: function(data){
					successTip(data,dg);
				}
			});
		} 
	});
}
		
/**
 *dataGrid修改方法
 */
function upd(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	d=$("#dlg").dialog({   
	    title: '修改用户',    
	    width: 380,    
	    height: 250,    
	    href:App.ctx + 'csPushSms/update/'+row.id,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'修改',
			handler:function(){
				$('#mainform').submit(); 
			}
		},{
			text:'取消',
			handler:function(){
					d.panel('close');
				}
		}]
	});
}
		
/**
 * dataGrid查询方法
 */
function cx(){
	var obj=$('#searchFrom').serializeObject();
	dg.datagrid('reload',obj); 
}

/**
 * dataGrid数据Formatter
 */
function complaintTypeFormatter(value, row, index){
	return enumFormatter("cn.explink.enumutil.ComplaintTypeEnum", value, "text" , "value");
}
function complaintStateFormatter(value, row, index){
	return enumFormatter("cn.explink.enumutil.ComplaintStateEnum", value, "text" , "value");
}
function usernameFormatter(value, row, index){
	return entityFormatter("User", value, "realname", "username");
}
function useridFormatter(value, row, index){
	return entityFormatter("User", value, "realname", "userid");
}
function branchFormatter(value, row, index){
	return entityFormatter("Branch", value, "branchname", "branchid");
}