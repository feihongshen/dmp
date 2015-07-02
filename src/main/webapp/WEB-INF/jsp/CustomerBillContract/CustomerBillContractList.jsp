<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
<script type="text/javascript">
	
	  $(function(){ 
		dg=$('#dg').datagrid({    
			method: "POST",
		    url:'', 
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
		        {field:'ck',checkbox:"true"},
		        {field:'id',title:'id',hidden:true},  
				{field:'billBatches',title:'账单批次',sortable:true,width:10,align:'center'},
				{field:'billState',title:'账单状态',sortable:true,width:10,align:'center'},
		        {field:'customerName',title:'客户名称',sortable:true,width:20,align:'center'},    
		        {field:'dateRange',title:'日期范围',sortable:true,align:'center',width:20},
		        {field:'correspondingCwbNum',title:'对应订单数',sortable:true,width:10,align:'center'},
		        {field:'deliveryMoney',title:'提货费',sortable:true,width:10,align:'center'},
		        {field:'distributionMoney',title:'配送费',sortable:true,width:15,align:'center'},
		        {field:'transferMoney',title:'中转费',sortable:true,width:15,align:'center'},
		        {field:'refuseMoney',title:'拒收派费',sortable:true,width:15,align:'center'},
		        {field:'totalCharge',title:'派费合计',sortable:true,width:15,align:'center'},
		        {field:'remark',title:'备注',sortable:true,width:30,align:'center'}
		    ]],
		    toolbar: '#toolbar'

		});

	});
	  
	  	function newBill(){
			$('#dlg').dialog('open').dialog('setTitle','新建表单');
			$('#fm').form('clear');
			url = '';
		}
	  	
	  	function saveBill(){
	  		$('#fm').form('submit',{
	  			url: url,
	  			onSubmit: function(){
	  				return $(this).form('validate');
	  			},
	  			success: function(result){
	  				var result = eval('('+result+')');
	  				if (result.errorMsg){
	  					$.messager.show({
	  						title: 'Error',
	  						msg: result.errorMsg
	  					});
	  				} else {
	  					$('#dlg').dialog('close');		// close the dialog
	  					$('#dg').datagrid('reload');	// reload the user data
	  				}
	  			}
	  		});
	  	}
	  	
	  	function newaddsearchbill(){
			$('#dlgserch').dialog('open').dialog('setTitle','查询表单');
			$('#fm1').form('clear');
			url = '';
		}
	  	
	  	function searchbill(){
	  		$('#fm1').form('submit',{
	  			url: url,
	  			onSubmit: function(){
	  				return $(this).form('validate');
	  			},
	  			success: function(result){
	  				var result = eval('('+result+')');
	  				if (result.errorMsg){
	  					$.messager.show({
	  						title: 'Error',
	  						msg: result.errorMsg
	  					});
	  				} else {
	  					$('#dlgserch').dialog('close');		// close the dialog
	  					$('#dg').datagrid('reload');	// reload the user data
	  				}
	  			}
	  		});
	  	}
	  	
	 	function neweditbill(){
			$('#dlgedit').dialog('open').dialog('setTitle','查看修改表单');
			$('#fm2').form('clear');
			url = '';
		}
	  	
	  	function editbill(){
	  		$('#fm2').form('submit',{
	  			url: url,
	  			onSubmit: function(){
	  				return $(this).form('validate');
	  			},
	  			success: function(result){
	  				var result = eval('('+result+')');
	  				if (result.errorMsg){
	  					$.messager.show({
	  						title: 'Error',
	  						msg: result.errorMsg
	  					});
	  				} else {
	  					$('#dlgserch').dialog('close');		// close the dialog
	  					$('#dg').datagrid('reload');	// reload the user data
	  				}
	  			}
	  		});
	  	}
	</script>
</head>
<body>
<div id="toolbar">
	<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="newBill()">新增</a>  
	<a id="btn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="neweditbill()">查看/修改</a>  
	<a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="removebill()">删除</a>  
	<a id="btn3" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="newaddsearchbill()">查询</a>  
</div>	
	<table id="dg"></table>	
	
<div id="dlg" class="easyui-dialog" style="width:730px;height:300px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
	<form id="fm" method="post">	
			<ul>
				<li>
						<div class="fitem">
							<label>客户名称:</label>
								<input type="text" name="customerId" class="easyui-validatebox" 
								 	data-options="width:150,prompt: '客户名称'"
								 	initDataType="TABLE" 
								 	initDataKey="Customer"
								 	viewField="customername" 
								 	saveField="customerid"
								 	value="-1"
								/>										
								<input type="text" name="dateState" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '审核日期'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.CwbDateEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="1"
								/>
							<input id="dd" type="text" class="easyui-datebox" required="required" style="width:100px;" name="startdate">
							至<input id="dd" type="text" class="easyui-datebox" required="required" style="width:100px;" name="enddate">
						</div>	
					</li>				
					<li style="padding-top: 0.5cm">
						<div class="fitem">
							<label>订单类型:</label>
								<input type="text" name="cwbOrderType" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '订单类型'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.CwbOrderTypeIdEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="-1"
								/>
						</div>
					</li>

					<li style="padding-top: 0.5cm">
						<div class="fitem">
							<label>备注:</label>	
							<input class="easyui-textbox" data-options="multiline:true" id="remark" style="width:600px;height:100px">
						</div>
					</li>
			</ul>		
	</form>
</div>
<div id="dlg-buttons">
	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveBill()">创建</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">返回</a>
</div>


<div id="dlgserch" class="easyui-dialog" style="width:800px;height:280px;padding:10px 20px"
		closed="true" buttons="#dlg-buttonsserch">
	<form id="fm1" method="post">
		<ul>
					<li>
						<div class="fitem">
							<label>账单批次:</label>
								<input id="billBatches" name="billBatches" class="easyui-validatebox" required="true" style="width:200px;">		
							<label>账单状态:</label>
							<input type="text" name="billState" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '账单状态'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.BillStateEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="-1"
								/>
						</div>
					</li>
					<li style="padding-top: 0.5cm">
						<div class="fitem">
								<label>账单创建日期:</label>
								<input id="dd" type="text" class="easyui-datebox" required="required" style="width:90px;">
									至<input id="dd" type="text" class="easyui-datebox" required="required" style="width:90px;">
								<label>账单核销日期:</label>
								<input id="dd" type="text" class="easyui-datebox" required="required" style="width:90px;">
									至<input id="dd" type="text" class="easyui-datebox" required="required" style="width:90px;">
						</div>
					</li>
					<li style="padding-top: 0.5cm">
						<div class="fitem">
							<label>客户名称:</label>
								 <input type="text" name="customerName" class="easyui-validatebox" 
								 	data-options="width:150,prompt: '客户名称'"
								 	initDataType="TABLE" 
								 	initDataKey="Customer"
								 	viewField="customername" 
								 	saveField="customerid"
								 	value="-1"
								/>
								
							<label>订单类型:</label>								
								 <input type="text" name="cwbOrderType" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '订单类型'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.CwbOrderTypeIdEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="-1"
								/>
							</div>
					</li>
					<li style="padding-top: 0.5cm">
						<div class="fitem">
							<label>排序:</label>
								<select id="customerName" class="easyui-combobox" name="customerName" style="width:118px;">
								    <option value="aa">aitem1</option>
								    <option>bitem2</option>
								    <option>bitem3</option>
								    <option>ditem4</option>
								    <option>eitem5</option>
								</select>
								<select id="sort" class="easyui-combobox" name="sort" style="width:66px;">
								   		<option value="aa">阿斯顿</option>
									    <option>bitem2</option>
									    <option>bitem3</option>
									    <option>ditem4</option>
									    <option>eitem5</option>
								</select>
						</div>
					</li>		
			</form>
	</div>

<div id="dlg-buttonsserch">
	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="newaddsearchbill()">查询</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgserch').dialog('close')">关闭</a>
</div>


<div id="dlgedit" class="easyui-dialog" style="width:900px;height:600px;padding:10px 20px"
		closed="true">
		<table>
			<tr>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgedit').dialog('close')">返回</a></td>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-ok">保存</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">审核</a>OR
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok">取消审核</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">核销完成</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">客户订单导入</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok">显示差异报告</a></td>
			</tr>
	</table>
		<form id="fm1" method="post">
				<table class="fitem">
						<tr>
							<td align="right" style="width:90px;">账单批次</td><td style="width:30px;" ><input name="billBatches" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">账单状态</td><td style="width:30px;"><input name="billState" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">日期范围</td><td style="width:30px;"><input name="dateRange" class="easyui-validatebox" readonly="readonly"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">派费合计(元)</td><td style="width:30px;"><input name="totalCharge" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">对应订单数</td><td style="width:30px;"><input name="correspondingCwbNum" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">客户名称</td><td style="width:30px;"><input name="customerId" class="easyui-validatebox" readonly="readonly"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">提货费(元)</td><td style="width:30px;"><input name="deliveryMoney" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">配送费(元)</td><td style="width:30px;"><input name="distributionMoney" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">中转费(元)</td><td style="width:30px;"><input name="transferMoney" class="easyui-validatebox" readonly="readonly"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;"><label>备注:</label></td>
							<td colspan="6" rowspan="5"><input class="easyui-textbox" data-options="multiline:true" id="remark" style="width:540px;height:100px"></td>	<!-- 	 style="width:600px;height:100px" -->				
						</tr>
				</table>
		</form>


<table id="dga" title="My Users" class="easyui-datagrid" style="width:550px;height:250px"
		url=""
		toolbar="#toolbarinedit"
		rownumbers="true" fitColumns="true" singleSelect="true">
	<thead>
		<tr>
			<th><input type="checkbox"/></th>
			<th field="firstname" width="50">First Name</th>
			<th field="lastname" width="50">Last Name</th>
			<th field="phone" width="50">Phone</th>
			<th field="email" width="50">Email</th>
		</tr>
	</thead>
</table>
<div id="toolbarinedit">
	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">New User</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">Edit User</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">Remove User</a>
</div>
		
</div>

</body>
</html>