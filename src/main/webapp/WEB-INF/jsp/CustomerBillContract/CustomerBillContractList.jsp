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
		
		/* $('#test').bind('click',form1Method); */
		
		dg=$('#dg').datagrid({    
			method: "POST",
		    url:'${pageContext.request.contextPath}/CustomerBillContract/AllBill', 
		    fit : true,
			fitColumns : true,
			border : true,
			striped:true,
			idField : 'id',
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			singleSelect:true,
		    columns:[[         
		        {field:'ck',checkbox:"true"},
		        {field:'id',title:'id',hidden:true},  
				{field:'billBatches',title:'账单批次',sortable:true,width:16,align:'center'},
				{field:'billState',title:'账单状态',sortable:true,width:10,align:'center',formatter:billStateFormatter},
		        {field:'customerId',title:'客户名称',sortable:true,width:15,align:'center',formatter:customerFormatter},    
		        {field:'dateRange',title:'日期范围',sortable:true,align:'center',width:25},
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
	  
/* 	  
 function form1Method() {
	 
	 var dataParam = {};
	 dataParam['billBatches'] = $('#billBatchesfm1').val()=="" ? ""  : $('#billBatchesfm1').val() ;
	 dataParam['billState'] = $('#billStatefm1').val()=="" ? "-1" :$('#billStatefm1').val();
	 dataParam['crestartdate'] = $('#crestartdatefm1').val()=="" ? "" : $('#crestartdatefm1').val();
	 dataParam['creenddate'] = $('#creenddatefm1').val()=="" ? ""  : $('#creenddatefm1').val();
	 dataParam['verificationstratdate'] = $('#verificationstratdatefm1').val();
	 dataParam['verificationenddate'] = $('#verificationenddatefm1').val();
	 dataParam['customerId'] = $('#customerIdfm1').val()=="" ? "-1" :$('#customerIdfm1').val();
	 dataParam['cwbOrderType'] = $('#cwbOrderTypefm1').val()=="" ? "-1" :$('#cwbOrderTypefm1').val();
	 dataParam['condition'] = $('#conditionfm1').val()=="" ? ""  : $('#conditionfm1').val();
	 dataParam['sequence'] = $('#sequencefm1').val();
	 $('#dg').datagrid('load',dataParam);     
 }
 */
 
 function form1Method(){
	 var jsoninfo =  JSON.stringify($('#fm1').serializeObject());
	 $('#dg').datagrid('load',{"jsoninfo":jsoninfo}); 
	 $('#dlgserch').dialog('close');
 	}
	  
	  	function newBill(){
			$('#dlg').dialog('open').dialog('setTitle','新增');
			$('#fm').form('clear');
		}
	  
	  	function saveBill(){
	  		$('#fm').form('submit',{
	  			url:'${pageContext.request.contextPath}/CustomerBillContract/addBill',
	  			onSubmit: function(){
	  				if($('#remark').val()==''){
	  					return false;
	  				}
	  				return true;
	  			},
	  			success: function(data){
	  				var data = eval('('+data+')');
	  				if (data.success==0){
	  					$.messager.show({
	  		 			title: '成功',
	  						msg: data.successdata
	  					}); 
	  					$('#dlg').dialog('close');
	  					$('#dg').datagrid('reload');
	  				/* 	$('#dlg').dialog('close');
	  					$('#billBatchess').val(data.billBatches);
	  					$('#billState').val(data.billState);
	  					$('#dateRange').val(data.dateRange);
	  					$('#totalCharge').val(data.totalCharge);
	  					$('#correspondingCwbNum').val(data.correspondingCwbNum);
	  					$('#customernames').val(data.customername);
	  					$('#deliveryMoney').val(data.deliveryMoney);
	  					$('#distributionMoney').val(data.distributionMoney);
	  					$('#transferMoney').val(data.transferMoney);
	  					$('#remark').val(data.remark);
	  					$('#dlg').dialog('close');		// close the dialog
	  					$('#dg').datagrid('reload'); */
	  				} 
	  			}
	  		});
	  	}
	  	
	  	function newaddsearchbill(){
			$('#dlgserch').dialog('open').dialog('setTitle','查询表单');
			$('#fm1').form('clear');
		}
	  	
	    	
	  	/**
	  	编辑框弹出  初始化
	  	*/
	  	function initMethod(){
	  		initDynamicSelect(customernames);
	  		initDynamicSelect(billState);
	  	}
	  	
	 	function neweditbill(){		
	 	 	var row = $('#dg').datagrid('getSelected');			
			if (row){
				$('#dlgedit').dialog('open').dialog('setTitle','查看修改表单');
				$('#fm2').form('load', {billBatches:row.billBatches});
				$('#fm2').form('submit', {
					url:'${pageContext.request.contextPath}/CustomerBillContract/findCustomerBillContractVOByBillBatches',
					onSubmit: function(){
						return true;
					},
					success: function(data){
						 var data = eval('(' + data + ')');  
						$('#billBatchess').val(data.billBatches);
	  					$('#billState1').val(data.billState);
	  					$('#dateRange').val(data.dateRange);
	  					$('#totalCharge').val(data.totalCharge);
	  					$('#correspondingCwbNum').val(data.correspondingCwbNum);
	  					$('#customernames').val(data.customerId);
	  					$('#deliveryMoney').val(data.deliveryMoney);
	  					$('#distributionMoney').val(data.distributionMoney);
	  					$('#transferMoney').val(data.transferMoney);
	  					$('#remark').val(data.remark);	
					}
				});
				
				$('#dga').datagrid({    
					method: "POST",
				    url:'${pageContext.request.contextPath}/CustomerBillContract/editAboutContent?billBatches='+row.billBatches, 
				    fit : true,
					fitColumns : true,
					border : true,
					striped:true,
					idField : 'id',
					pagination:true,
					rownumbers:true,
					pageNumber:1,
					pageSize : 10,
					pageList : [ 10, 20, 30, 40, 50 ],
					singleSelect:true,
				    columns:[[         
				        {field:'ck',checkbox:"true"},
				        {field:'id',title:'id',hidden:true},  
						{field:'cwb',title:'订单号',sortable:true,width:75,align:'center'},
						{field:'cwbstate',title:'订单状态',sortable:true,width:75,align:'center'},
				        {field:'cwbOrderType',title:'订单类型',sortable:true,width:75,align:'center'},    
				        {field:'paywayid',title:'支付方式',sortable:true,align:'center',width:75},				    
				        {field:'deliveryMoney',title:'提货费',sortable:true,width:75,align:'center'},
				        {field:'distributionMoney',title:'配送费',sortable:true,width:75,align:'center'},
				        {field:'transferMoney',title:'中转费',sortable:true,width:75,align:'center'},
				        {field:'refuseMoney',title:'拒收派费',sortable:true,width:75,align:'center'},
				        {field:'totalCharge',title:'派费合计',sortable:true,width:75,align:'center'},		
				        {field:'billBatches',title:'billBatches',hidden:true}
				    ]],
				    
				    toolbar:'#tbNewAddofEdit'
				});
				/* $('#fm2').form('onLoadSuccess',initMethod());
				url = '${pageContext.request.contextPath}/CustomerBillContract/editBill?id='+row.id; */
				} 
			


		}
	
	  	function clearAndCloseEdit(){
	  		$('#dlgedit').dialog('close');
			$('#fm2').form('clear');
	  	}
	  	
	  	
	 /* 删除账单  */ 
 function removeBill(){
	var row = $('#dg').datagrid('getSelected');
	if (row){
		$.messager.confirm('提示','你确定要删除这个帐单吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/CustomerBillContract/removeBill',{id:row.id},function(result){
					if (result.success==0){
						$('#dg').datagrid('reload');
						$.messager.show({	// show success message
							title: '删除成功',
							msg: result.successdata
						});// reload the data
					} 
				},'json');
			}
		});
	}
}
	 
	 
 function customerFormatter(value, row, index){
		return entityFormatter("Customer", value, "customername", "customerid");
	}
 
 function billStateFormatter(value, row, index){
		return enumFormatter("cn.explink.enumutil.BillStateEnum", value, "text" , "value");
	}
 
	
	function dlgNewAddofEdit(){
		$('#dlgAddofEdit').dialog('open').dialog('setTitle','查询表单');
		$('#fmAddofEdit').form('clear');
		/*  var jsoninfo =  JSON.stringify($('#fmAddofEdit').serializeObject());
		 $('#dlgAddofEdit').datagrid('load',{"cwb":jsoninfo});  */
		 dgofEdit=$('#dgofEdit').datagrid({    
			method: "POST",
		    url:'${pageContext.request.contextPath}/CustomerBillContract/newAddofEditList', 
		    fit : true,
			fitColumns : true,
			border : true,
			striped:true,
			idField : 'cwb',
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			singleSelect:true,
			columns:[[         
				        {field:'ck',checkbox:"true"},
				        {field:'id',title:'id',hidden:true},  
						{field:'cwb',title:'订单号',sortable:true,width:75,align:'center'},	
						{field:'cwbstate',title:'订单状态',sortable:true,width:75,align:'center'},
				        {field:'cwbOrderType',title:'订单类型',sortable:true,width:75,align:'center'},    
				        {field:'paywayid',title:'支付方式',sortable:true,align:'center',width:75},				    
				        {field:'deliveryMoney',title:'提货费',sortable:true,width:75,align:'center'},
				        {field:'distributionMoney',title:'配送费',sortable:true,width:75,align:'center'},
				        {field:'transferMoney',title:'中转费',sortable:true,width:75,align:'center'},
				        {field:'refuseMoney',title:'拒收派费',sortable:true,width:75,align:'center'},
				        {field:'totalCharge',title:'派费合计',sortable:true,width:75,align:'center'},				        
				    ]],
		   

		});		               
	}
	
	function SerachAddofEdit(){
		
		 $('#dgofEdit').datagrid('load',{'cwb':$('#dlgAddofEditCwb').val()}); 
		 $('#dgofEdit').datagrid('reload'); 
		
	}
	
	 function removeofEdit(){
			var row = $('#dga').datagrid('getSelected');
			
			if (row){
				
				$.messager.confirm('提示','你确定要删除这条记录吗?',function(r){
					if (r){
						$.post('${pageContext.request.contextPath}/CustomerBillContract/removeBillInfoofEdit',{cwb:row.cwb,billBatches:row.billBatches},function(result){
							if (result.success==0){
	
								$.messager.show({	// show success message
									title: '删除成功',
									msg: result.successdata
								});// reload the data
							
					
							} 
							$('#dga').datagrid('reload');
							$('#dg').datagrid('reload');
							$('#fm2').form('reload');
						},'json');
					}
				});
			}
		}
			
		
	</script>
</head>
<body>
	<!-- datagrid 操作区域 -->
	<div id="toolbar">
		<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="newBill()">新增</a>  
		<a id="btn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="neweditbill()">查看/修改</a>  
		<a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="removeBill()">删除</a>  
		<a id="btn3" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="newaddsearchbill()">查询</a>  
	</div>	
	
	<!-- datagrid list -->
	<table id="dg"></table>	
	
	<!-- 新增一级 弹窗 -->
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
							<input class="easyui-textbox" data-options="multiline:true" id="remark" style="width:600px;height:100px" name="remark"/>
						</div>
					</li>
				</ul>		
		</form>
	</div>
	<!-- 新增一级弹窗  操作区域 -->
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:saveBill()">创建</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">返回</a>
	</div>
	
	<!-- 查询条件弹出框  -->
	<div id="dlgserch" class="easyui-dialog" style="width:800px;height:280px;padding:10px 20px"
			closed="true" buttons="#dlg-buttonsserch">
		<form id="fm1" method="post">
			<ul>
					<li>
						<div class="fitem">
							<label>账单批次:</label>
							<input id="billBatchesfm1" name="billBatches" class="easyui-validatebox" style="width:200px;">		
							<label>账单状态:</label>
							<input type="text" name="billState" id="billStatefm1" class="easyui-validatebox" 
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
								<input id="crestartdatefm1" type="text" class="easyui-datebox" style="width:90px;" name="crestartdate">
									至<input id="creenddatefm1" name="creenddate" type="text" class="easyui-datebox" style="width:90px;">
								<label>账单核销日期:</label>
								<input id="verificationstratdatefm1" name="verificationstratdate" type="text" class="easyui-datebox" style="width:90px;">
									至<input id="verificationenddatefm1" name="verificationenddate" type="text" class="easyui-datebox" style="width:90px;">
						</div>
					</li>
					<li style="padding-top: 0.5cm">
						<div class="fitem">
							<label>客户名称:</label>
								 <input type="text" name="customerId" id="customerIdfm1" class="easyui-validatebox" 
								 	data-options="width:150,prompt: '客户名称'"
								 	initDataType="TABLE" 
								 	initDataKey="Customer"
								 	viewField="customername" 
								 	saveField="customerid"
								 	value="-1"
								/>
								
							<label>订单类型:</label>								
								 <input type="text" name="cwbOrderType" id="cwbOrderTypefm1" class="easyui-validatebox" 
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
								<input type="text" name="condition" id="conditionfm1" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '账单批次'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.SortReasonEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="bill_batches"
								/>
								
								<input type="text" name="sequence" id="sequencefm1" class="easyui-validatebox" 
									 	data-options="width:66,prompt: '排序'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.SequenceEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="ASC"
								/>
						</div>
					</li>		
			</form>
	</div>
	<!-- 查询弹窗 操作区域 -->
	<div id="dlg-buttonsserch">
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="newaddsearchbill()">查询</a> -->
		<a href="#" onclick="form1Method()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>		
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgserch').dialog('close')">关闭</a>
	</div>

	<!-- 新增、编辑界面 -->
	<div id="dlgedit" class="easyui-dialog" style="width:900px;height:600px;padding:10px 20px" closed="true">
		<!-- 操作按钮区域 -->
		<table>
			<tr>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:clearAndCloseEdit()">返回</a></td>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:editbill()">保存</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">审核</a>OR
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok">取消审核</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">核销完成</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok">客户订单导入</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok">显示差异报告</a></td>
			</tr>
		</table>
		<!-- 新增一级弹窗查询数据回显（根据查询结果动态拼接） -->
		<form id="fm2" method="post">
				<table class="fitem">
						<tr>
							<td align="right" style="width:90px;">账单批次</td><td style="width:30px;" ><input name="billBatches" id="billBatchess" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">账单状态</td><td style="width:30px;">
							<!-- <input name="billState" id="billState" class="easyui-validatebox" readonly="readonly"> -->
							<input type="text" name="billState" id="billState1" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '账单批次'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.BillStateEnum"
									 	viewField="text" 
									 	saveField="value"
									 	
								/>
							</td>
							<td align="right" style="width:90px;">日期范围</td><td style="width:30px;"><input name="dateRange" id="dateRange" class="easyui-validatebox" readonly="readonly"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">派费合计(元)</td><td style="width:30px;"><input name="totalCharge" id="totalCharge" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">对应订单数</td><td style="width:30px;"><input name="correspondingCwbNum" id="correspondingCwbNum" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">客户名称</td><td style="width:30px;">
							<!-- <input name="customerId" id="customernames" class="easyui-validatebox" readonly="readonly"> -->
							<input type="text" name="customerId" class="easyui-validatebox"						
									id="customernames"
								 	data-options="width:150,prompt: '客户名称'"
								 	initDataType="TABLE" 
								 	initDataKey="Customer"
								 	viewField="customername" 
								 	saveField="customerid"
								 	
								/>
							</td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">提货费(元)</td><td style="width:30px;"><input name="deliveryMoney" id="deliveryMoney" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">配送费(元)</td><td style="width:30px;"><input name="distributionMoney" id="distributionMoney" class="easyui-validatebox" readonly="readonly"></td>
							<td align="right" style="width:90px;">中转费(元)</td><td style="width:30px;"><input name="transferMoney" id="transferMoney" class="easyui-validatebox" readonly="readonly"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;"><label>备注:</label></td>
							<td colspan="6" rowspan="5"><input class="easyui-textbox" data-options="multiline:true" id="remark" style="width:540px;height:100px"></td>	<!-- 	 style="width:600px;height:100px" -->				
						</tr>
				</table>
		</form>
		<table id="dga" style="width:800px;height:118px"></table>
		<div id="tbNewAddofEdit">
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="dlgNewAddofEdit()">添加</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeofEdit()">移除</a>	
		</div>
	</div>
	
	
	
		<div id="dlgAddofEdit" class="easyui-dialog" style="width:730px;height:300px;padding:10px 20px"
			closed="true" buttons="#dlgAddofEdit-buttons">
		<form id="fmAddofEdit" method="post">	
				<ul>
					<li>
						<div class="fitem">
							订单号:</td><td style="width:30px;"><input name="cwb" id="dlgAddofEditCwb" class="easyui-validatebox">
							<a id="AddofEditSerach" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="SerachAddofEdit()">查询</a>
						</div>	
					</li>					
				</ul>		
		</form>
		<table id="dgofEdit"></table>
	</div>
	<!-- 新增一级弹窗  操作区域 -->
	<div id="dlgAddofEdit-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:">确认</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAddofEdit').dialog('close')">取消</a>
	</div>

</body>
</html>