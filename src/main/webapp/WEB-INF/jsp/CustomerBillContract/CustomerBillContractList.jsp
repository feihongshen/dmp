<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.enumutil.BillStateEnum"%>
<script src="${ctx}/js/commonUtil.js" type="text/javascript"></script>
<script src="${ctx}/js/easyui-extend/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}

.uploader{
position:relative;
display:inline-block;
overflow:hidden;
cursor:default;
padding:0;
margin:10px 0px;
-moz-box-shadow:0px 0px 5px #ddd;
-webkit-box-shadow:0px 0px 5px #ddd;
box-shadow:0px 0px 5px #ddd;

-moz-border-radius:5px;
-webkit-border-radius:5px;
border-radius:5px;
}

.filename{
float:left;
display:inline-block;
outline:0 none;
height:32px;
width:180px;
margin:0;
padding:8px 10px;
overflow:hidden;
cursor:default;
border:1px solid;
border-right:0;
font:9pt/100% Arial, Helvetica, sans-serif; color:#777;
text-shadow:1px 1px 0px #fff;
text-overflow:ellipsis;
white-space:nowrap;

-moz-border-radius:5px 0px 0px 5px;
-webkit-border-radius:5px 0px 0px 5px;
border-radius:5px 0px 0px 5px;

background:#f5f5f5;
background:-moz-linear-gradient(top, #fafafa 0%, #eee 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#fafafa), color-stop(100%,#f5f5f5));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#f5f5f5',GradientType=0);
border-color:#ccc;

-moz-box-shadow:0px 0px 1px #fff inset;
-webkit-box-shadow:0px 0px 1px #fff inset;
box-shadow:0px 0px 1px #fff inset;

-moz-box-sizing:border-box;
-webkit-box-sizing:border-box;
box-sizing:border-box;
}

.button{
float:left;
height:32px;
display:inline-block;
outline:0 none;
padding:8px 12px;
margin:0;
cursor:pointer;
border:1px solid;
font:bold 9pt/100% Arial, Helvetica, sans-serif;

-moz-border-radius:0px 5px 5px 0px;
-webkit-border-radius:0px 5px 5px 0px;
border-radius:0px 5px 5px 0px;

-moz-box-shadow:0px 0px 1px #fff inset;
-webkit-box-shadow:0px 0px 1px #fff inset;
box-shadow:0px 0px 1px #fff inset;
}

.uploader input[type=file]{
position:absolute;
top:0; right:0; bottom:0;
border:0;
padding:0; margin:0;
height:30px;
cursor:pointer;
filter:alpha(opacity=0);
-moz-opacity:0;
-khtml-opacity: 0;
opacity:0;
}

input[type=button]::-moz-focus-inner{padding:0; border:0 none; -moz-box-sizing:content-box;}
input[type=button]::-webkit-focus-inner{padding:0; border:0 none; -webkit-box-sizing:content-box;}
input[type=text]::-moz-focus-inner{padding:0; border:0 none; -moz-box-sizing:content-box;}
input[type=text]::-webkit-focus-inner{padding:0; border:0 none; -webkit-box-sizing:content-box;}

/* White Color Scheme ------------------------ */

.white .button{
color:#555;
text-shadow:1px 1px 0px #fff;
background:#ddd;
background:-moz-linear-gradient(top, #eeeeee 0%, #dddddd 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#eeeeee), color-stop(100%,#dddddd));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#eeeeee', endColorstr='#dddddd',GradientType=0);
border-color:#ccc;
}

.white:hover .button{
background:#eee;
background:-moz-linear-gradient(top, #dddddd 0%, #eeeeee 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#dddddd), color-stop(100%,#eeeeee));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#dddddd', endColorstr='#eeeeee',GradientType=0);
}

/* Blue Color Scheme ------------------------ */

.blue .button{
color:#fff;
text-shadow:1px 1px 0px #09365f;
background:#064884;
background:-moz-linear-gradient(top, #3b75b4 0%, #064884 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#3b75b4), color-stop(100%,#064884));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#3b75b4', endColorstr='#064884',GradientType=0);
border-color:#09365f;
}

.blue:hover .button{
background:#3b75b4;
background:-moz-linear-gradient(top, #064884 0%, #3b75b4 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#064884), color-stop(100%,#3b75b4));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#064884', endColorstr='#3b75b4',GradientType=0);
}

/* Green Color Scheme ------------------------ */

.green .button{
color:#fff;
text-shadow:1px 1px 0px #6b7735;
background:#7d8f33;
background:-moz-linear-gradient(top, #93aa4c 0%, #7d8f33 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#93aa4c), color-stop(100%,#7d8f33));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#93aa4c', endColorstr='#7d8f33',GradientType=0);
border-color:#6b7735;
}

.green:hover .button{
background:#93aa4c;
background:-moz-linear-gradient(top, #7d8f33 0%, #93aa4c 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#7d8f33), color-stop(100%,#93aa4c));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#7d8f33', endColorstr='#93aa4c',GradientType=0);
}

/* Red Color Scheme ------------------------ */

.red .button{
color:#fff;
text-shadow:1px 1px 0px #7e0238;
background:#90013f;
background:-moz-linear-gradient(top, #b42364 0%, #90013f 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#b42364), color-stop(100%,#90013f));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#b42364', endColorstr='#90013f',GradientType=0);
border-color:#7e0238;
}

.red:hover .button{
background:#b42364;
background:-moz-linear-gradient(top, #90013f 0%, #b42364 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#90013f), color-stop(100%,#b42364));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#90013f', endColorstr='#b42364',GradientType=0);
}

/* Orange Color Scheme ------------------------ */

.orange .button{
color:#fff;
text-shadow:1px 1px 0px #c04501;
background:#d54d01;
background:-moz-linear-gradient(top, #f86c1f 0%, #d54d01 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#f86c1f), color-stop(100%,#d54d01));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#f86c1f', endColorstr='#d54d01',GradientType=0);
border-color:#c04501;
}

.orange:hover .button{
background:#f86c1f;
background:-moz-linear-gradient(top, #d54d01 0%, #f86c1f 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#d54d01), color-stop(100%,#f86c1f));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#d54d01', endColorstr='#f86c1f',GradientType=0);
}

/* Black Color Scheme ------------------------ */

.black .button{
color:#fff;
text-shadow:1px 1px 0px #111111;
background:#222222;
background:-moz-linear-gradient(top, #444444 0%, #222222 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#444444), color-stop(100%,#222222));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#444444', endColorstr='#222222',GradientType=0);
border-color:#111111;
}

.black:hover .button{
background:#444444;
background:-moz-linear-gradient(top, #222222 0%, #444444 100%);
background:-webkit-gradient(linear, left top, left bottom, color-stop(0%,#222222), color-stop(100%,#444444));
filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#222222', endColorstr='#444444',GradientType=0);
}
</style>

<script type="text/javascript">
		
		
	  $(function(){ 
		  $("input[type=file]").change(function(){$(this).parents(".uploader").find(".filename").val($(this).val());});
			$("input[type=file]").each(function(){
			if($(this).val()==""){
				$(this).parents(".uploader").find(".filename").val("请选择要上传的文件！");}
			});
		
		/* $('#test').bind('click',form1Method); */
		
		dg=$('#dg').datagrid({    
			method: "POST",
		    url:'${pageContext.request.contextPath}/CustomerBillContract/AllBill', 
		    fit : true,
			fitColumns : true,
			border : true,
			striped:true,
			idField : 'id',
			loadMsg : '正在加载，请稍后！',
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
	  
	  /*<input id="crestartdatefm1" type="text" class="easyui-datebox" style="width:90px;" name="crestartdate">
									至<input id="creenddatefm1" name="creenddate" type="text" class="easyui-datebox" style="width:90px;">
								<label>账单核销日期:</label>
								<input id="verificationstratdatefm1" name="verificationstratdate" type="text" class="easyui-datebox" style="width:90px;">
									至<input id="verificationenddatefm1"   */
	  
 function form1Method(){
	var crestartdate=$('#crestartdatefm1').datebox("getValue");
	var creenddate=$('#creenddatefm1').datebox("getValue");
	var verificationstratdate=$('#verificationstratdatefm1').datebox("getValue");
	var verificationenddate=$('#verificationenddatefm1').datebox("getValue");
		if(crestartdate>creenddate||verificationenddate<verificationstratdate){
			 $.messager.alert('提示','结束时间不能小于开始时间！','info');   
				$.messager.progress('close');
				return false;
		}								
	 var jsoninfo =  JSON.stringify($('#fm1').serializeObject());
		 $('#dg').datagrid('load',{"jsoninfo":jsoninfo}); 
		 $('#dlgserch').dialog('close');
 	}
	  
	  	function newBill(){
			$('#dlg').dialog('open').dialog('setTitle','新增');
			$('#fm').form('clear');
		}
	  
	  	
	  	 function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
	         var  aDate,  oDate1,  oDate2,  iDays  
	         aDate  =  sDate1.split("-")  
	         oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])    //转换为12-18-2006格式  
	         aDate  =  sDate2.split("-")  
	         oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])  
	         iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
	         return  iDays  
	     }    
	  	 
	  	 
	  	 function datChongFuYanZheng(){
	  			var a=$('#dstart').datebox("getValue");
				var b=$('#dend').datebox("getValue");
				var d=DateDiff(a,b);
				if(b<a){
					 $.messager.alert('提示','结束时间不能小于开始时间！','info');   
						$.messager.progress('close');
						return false;
				}
				if(d>60){
					 $.messager.alert('提示','时间跨度不能大于60天！','info');   
					$.messager.progress('close');
					return false;
				}
				if(a==""||b==""){
					 $.messager.alert('提示','时间不能为空！','info');   
  					$.messager.progress('close');
  					return false;
				}

  				if($("input[name='crecustomerId']").val()==""){
  					 $.messager.alert('提示','客户名称不能为空！','info');   
	  					$.messager.progress('close');
	  					return false;
  				}
  				if($("input[name='dateState']").val()==""){
  					 $.messager.alert('提示','时间类型不能为空！','info');   
	  					$.messager.progress('close');
	  					return false;
  				}
	  		 $.ajax({
	  			 	type:'POST',
	  			 	data:$('#fm').serialize(),
	  			 	url:'${pageContext.request.contextPath}/CustomerBillContract/panDuanDateShiFouChongDie',
	  			 	dataType:'json',
	  			 	success:function(data){
	  			 		if(data.success==0){
	  			 		$.messager.confirm("操作提示", "该客户已经存在"+data.successdateType+"为"+data.successdata+"的派费账单,是否继续创建批次？", function (r) { 
	  			            if (r) { 
	  			            	$('#dlg').dialog('close');
	  			            	saveBill();  	
	  			            } 
	  			        }); 
     
	  			 		}else{
	  			 			$('#dlg').dialog('close');
	  			 			saveBill();  		
	  			 		}
	  			 	}  			 
	  		 });
	  	 } 

	  	function saveBill(){	  	
	  		$.messager.show({
	  			title:'温馨提示',
	  			msg:'正在为您创建账单,请耐心等候！',
	  			timeout:2000,
	  			showType:'slide'
	  		});
	  		$.messager.progress();
	  		
	  		$('#fm').form('submit',{
	  			url:'${pageContext.request.contextPath}/CustomerBillContract/addBill',
	  			onSubmit: function(){
	  				return true;
	  			},
	  			success: function(data){
	  				if(data!=""){
	  					var data = eval('('+data+')');	  				
	  					$('#dlg').dialog('close');
	  					$('#dg').datagrid('reload');
	  					$.messager.progress('close');	// 如果提交成功则隐藏进度条
	  					var billBatches=data.billBatches;
	  					neweditbill1(billBatches);	 
					}else{
							$.messager.progress('close');
						  $.messager.alert('提示','没有查询到相关订单，不能创建账单！','info');  
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
	  	/* function initMethod(){
	  		initDynamicSelect(customernames);
	  		initDynamicSelect(billState);
	  	} */
	 
	 	function neweditbill(){		
	 	 	var row = $('#dg').datagrid('getSelected');		
	 	 	
			if (row){
				
				$('#dlgedit').dialog('open').dialog('setTitle','查看修改表单');
				$('#fm2').form('load', {billBatches:row.billBatches});					
				$('#hv').val(row.billBatches);
				
		/* 		checkState(row.billState); */
			
				findCustomerBillContractByBatches();
				
				$('#dg').datagrid('reload');
				$('#dga').datagrid({    
					method: "POST",
				    url:'${pageContext.request.contextPath}/CustomerBillContract/editAboutContent?billBatches='+row.billBatches, 
				  /*   fit : true, */
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
				/*  $('#fm2').form('onLoadSuccess',initMethod());
				url = '${pageContext.request.contextPath}/CustomerBillContract/editBill?id='+row.id;  */
				} 
			


		}
	  	
	  	
	  	function neweditbill1(batches){		
				$('#dlgedit').dialog('open').dialog('setTitle','查看修改表单');
				$('#fm2').form('load', {billBatches:batches});					
				$('#hv').val(batches);
				
				/* checkState(billstate); */
			
				findCustomerBillContractByBatches();
				
				$('#dg').datagrid('reload');
				$('#dga').datagrid({    
					method: "POST",
				    url:'${pageContext.request.contextPath}/CustomerBillContract/editAboutContent?billBatches='+batches, 
				  /*   fit : true, */
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
				
				} 

	 	function findCustomerBillContractByBatches(){
	 		$('#fm2').form('submit', {
				url:'${pageContext.request.contextPath}/CustomerBillContract/findCustomerBillContractVOByBillBatches',
				onSubmit: function(){
					return true;
				},
				success: function(data){
					 var data = eval('(' + data + ')');  
					$('#billBatchess').val(data.billBatches);
  					$('#billState').val(data.billState);
  					$('#dateRange').val(data.dateRange);
  					$('#totalCharge').val(data.totalCharge);
  					$('#correspondingCwbNum').val(data.correspondingCwbNum);
  					$('#customernames').val(data.customername);
  					$('#deliveryMoney').val(data.deliveryMoney);
  					$('#distributionMoney').val(data.distributionMoney);
  					$('#transferMoney').val(data.transferMoney);
  					$('#remarkEdit').val(data.remark);	
  					
				
  					if(data.billState=="未审核"){ 						
 						$("#baocun").show();
 						 $("#shenhe").show();
 						 $("#fanhui").show();
 						$('#xscybg').show();
  						$('#khdddr').show();
  						$("#quxiaoshenhe").hide();
 						 $("#hexiaowancheng").hide();
 						$("#quxiaohexiao").hide();
 						$('#removebille').show();
						$('#addbille').show();
  					}else if(data.billState=="已审核"){
  						 $("#hexiaowancheng").show();
 						 $("#quxiaoshenhe").show();
 						 $("#fanhui").show();		
 						$("#baocun").hide();
						 $("#shenhe").hide();
						$('#xscybg').hide();
 						$('#khdddr').hide();
 						$("#quxiaohexiao").hide();
 						$('#removebille').hide();
						$('#addbille').hide();
  					} else if(data.billState=="已核销"){
  						 $("#quxiaohexiao").show();  
 						$("#quxiaoshenhe").hide();
  						 $("#hexiaowancheng").hide();
 						 $("#fanhui").hide();		
 						$("#baocun").hide();
						 $("#shenhe").hide();
						$('#xscybg').hide();
 						$('#khdddr').hide();
						$('#removebille').hide();
						$('#addbille').hide();
  					} 
  					
  					
  					
  					
  					
  					
  					
  					
				}
			});
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
	/* 	var jsoninfo =  JSON.stringify($('#fmAddofEdit').serializeObject()); */
		 dgofEdit=$('#dgofEdit').datagrid({    
			method: "POST",
		    url:'${pageContext.request.contextPath}/CustomerBillContract/newAddofEditList?billBatches='+$('#hv').val(), 
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
	
	
	
	 function addBillCwbNum(){
		 var row = $('#dgofEdit').datagrid('getSelected');
			
			if (row){
				$.messager.confirm('提示','你确定要添加订单吗?',function(r){
					if (r){
						$.post('${pageContext.request.contextPath}/CustomerBillContract/addCwbInEdit',{billBatches:$('#hv').val(),cwb:row.cwb},function(result){
							if (result.success==0){
	
								$.messager.show({	// show success message
									title: '添加 Success',
									msg: result.successdata
								});// reload the data
								$('#dg').datagrid('reload');	
								$('#dga').datagrid('reload');	
								$('#dlgAddofEditCwb').val("");
								$('#dgofEdit').datagrid('reload');
								findCustomerBillContractByBatches();
					
							} else if(result.success==1){
								$.messager.show({	// show success message
									title: '添加Error ',
									msg: result.successdata
								});// reload the data
								$('#dg').datagrid('reload');	
								$('#dga').datagrid('reload');	
								$('#dlgAddofEditCwb').val("");
								$('#dgofEdit').datagrid('reload');
								findCustomerBillContractByBatches();
						}			
							$('#dlgAddofEditCwb').val("");
							$('#dgofEdit').datagrid('reload');
							},'json');
						}
				});
			
			}
			
			$('#dlgAddofEdit').dialog('close');
		}
	
	function SerachAddofEdit(){
		
		 $('#dgofEdit').datagrid('load',{'cwb':$('#dlgAddofEditCwb').val()}); 
		 $('#dgofEdit').datagrid('reload'); 
		 $('#dlgAddofEditCwb').val("");
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
							findCustomerBillContractByBatches();
							$('#dga').datagrid('reload');		
							$('#dg').datagrid('reload'); 
							$('#fm2').form('reload');
							ChaYidl();
						},'json');
					}
				});
			}
		}
			
		
	 
	 function changeBillState(stateValue){
		 var statevalue=stateValue;
				$.messager.confirm('提示','你确定要改变账单状态吗?',function(r){
					if(r){
						$.post('${pageContext.request.contextPath}/CustomerBillContract/changeBillState',{billState:statevalue,billBatches:$('#hv').val()},function(result){
							if (result.success==0){
	
								$.messager.show({	// show success message
									title: '修改',
									msg: result.successdata
								});// reload the data
							
					
							} 							
							findCustomerBillContractByBatches();
							$('#dg').datagrid('reload');	
							$('#dga').datagrid('reload');						
							$('#fm2').form('reload');
						},'json');
					}
				});
			
			
		}
	 
	 function uploadExcel(){

		   var fileName= $('#uploadExcel').val();  
		   var a = $('#hv').val();
		   $('#uploadbillBatches').val(a);

              if(fileName==""){     
                 $.messager.alert('提示','请选择上传文件！','info');   
              }else{  
            	  var d1=/\.[^\.]+$/.exec(fileName);   
                          
            	  $('#questionTypesManage').form('submit',{
      	  			url:'${pageContext.request.contextPath}/CustomerBillContract/getupdateExcel',
      	  			onSubmit: function(){
      	  			if(d1==".xls"){      	  			 
      	  				return true;
      	  			}else if(d1==".xlsx"){
      	  			 $.messager.alert('提示','您的Excel文件版本过高！','info');   
      	  				return false;    	  				
      	  			}else{
	      	  			 $.messager.alert('提示','请上传Excel文件！','info');   
	   	  				return false;  
	      	  			$('#uploadExcel').val(''); 
	      	  			$('#dga').datagrid('reload');
      	  			}
      	  	},
      	  			success: function(data){
      	  				var data = eval('('+data+')');
      	  				if (data.success==0){
      	  					$.messager.show({
      	  		 			title: '上传成功',
      	  						msg: data.successdata
      	  					}); 
      	  					
      	  					$('#dga').datagrid('reload');
      	  				
      	  				}else if(data.success==1){
      	  				$.messager.show({
      	  		 			title: '上传失败',
      	  						msg: data.successdata
      	  					}); 
      	  					
      	  					$('#dga').datagrid('reload');
      	  				}else if(data.success==2){
          	  				$.messager.show({
          	  		 			title: '上传失败',
          	  						msg: data.successdata
          	  					}); 
          	  					
          	  					$('#dga').datagrid('reload');
          	  				}  
      	  			}
      	  		});
                 
              }    
         
	 }
	
	 function duiBiBillMoneyChaYi(){
		 $('#billMoneyDuiBi').dialog('open').dialog('setTitle','对比');
		 
		 $.ajax({    
				type: "POST",
				data:{'billBatches':$('#hv').val()},
			    url:'${pageContext.request.contextPath}/CustomerBillContract/billMoneyWithImportExcelChaYiDuiBi', 			   
				dataType:'json',
				success:function(data){
							$('#systemDateCount').html(data.systemDateCount);
							$('#importDateCount').html(data.importDateCount);
							$('#chaYiCount').html(data.chaYiCount);
							$('#systemDateMoney').html(data.systemDateMoney);
							$('#importDateMoney').html(data.importDateMoney);
							$('#chaYiMoney').html(data.chaYiMoney);
							$('#chaYiCwbsMoney').html(data.duibiCwbMoneyChaYi);
							$('#hv2').val(data.chaYiCount);
						
				}
			});
		 
	 }
	 
	 function ChaYidl(){
		 dgCountChaYi=$('#dgCountChaYi').datagrid({    
				method: "POST",
			    url:'${pageContext.request.contextPath}/CustomerBillContract/CountChaYi?billBatches='+$('#hv').val(), 
			    fit : true,
				fitColumns : true,
				border : true,
				striped:true,
				idField : 'id',
				rownumbers:true,
				singleSelect:true,
				columns:[[         
					        {field:'ck',checkbox:"true"},
					        {field:'id',title:'id',hidden:true},  
					        {field:'zhongLei',title:'种类',sortable:true,width:108,align:'center'},
							{field:'cwb',title:'订单号',sortable:true,width:100,align:'center'},	
							{field:'cwbstate',title:'订单状态',sortable:true,width:80,align:'center'},
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
	 
	 function CountChaYi(){ /*readonly="readonly" style="color: #C0C0C0"  */		 
		 if($('#hv2').val()!=0){
			$('#CountChaYi').dialog('open').dialog('setTitle','金额差异 ');
			ChaYidl();
		 }		
	 }
	 
	 function addBillCwbNumInChaYi(){
		 var row = $('#dgCountChaYi').datagrid('getSelected');
			
			if (row){
				$.messager.confirm('提示','你确定要添加订单吗?',function(r){
					if (r){
						$.post('${pageContext.request.contextPath}/CustomerBillContract/addCwbInEdit',{billBatches:$('#hv').val(),cwb:row.cwb},function(result){
							if (result.success==0){
	
								$.messager.show({	// show success message
									title: '添加 ',
									msg: result.successdata
								});// reload the data
							
					
							} else if(result.success==1){
								$.messager.show({	// show success message
									title: '添加Error ',
									msg: result.successdata
								});// reload the data
						}							
							findCustomerBillContractByBatches();
							$('#dg').datagrid('reload');	
							$('#dga').datagrid('reload');						
							$('#fm2').form('reload');
							$('#hv').val('');
							$('#hv1').val('');
							ChaYidl();
							
							
							},'json');
						}
				});
			
			}
			
			$('#dlgAddofEdit').dialog('close');
		}
		
	 
	 function removeofEditInChaYi(){
			var row = $('#dgCountChaYi').datagrid('getSelected');
			
			if (row){
				
				$.messager.confirm('提示','你确定要删除这条记录吗?',function(r){
					if (r){
						$.post('${pageContext.request.contextPath}/CustomerBillContract/removeBillInfoofEdit',{cwb:row.cwb,billBatches:$('#hv').val()},function(result){
							if (result.success==0){
	
								$.messager.show({	// show success message
									title: '删除成功',
									msg: result.successdata
								});// reload the data					
							findCustomerBillContractByBatches();
							$('#dga').datagrid('reload');		
							$('#dg').datagrid('reload'); 
							$('#dgCountChaYi').datagrid('reload');
							$('#fm2').form('reload');
							ChaYidl();
							}else{
								$.messager.show({	// show success message
									title: '删除失败',
									msg: result.successdata
								});// reload the data
								$('#dga').datagrid('reload');		
								$('#dg').datagrid('reload'); 
							} 
						},'json');
					}
				});
			}
		}
	function MoneyChaYi(){
		$('#MoneyChaYi').dialog('open').dialog('setTitle','金额差异 ');
		dgMoneyChaYi=$('#dgMoneyChaYi').datagrid({    
				method:"POST",
			    url:'${pageContext.request.contextPath}/CustomerBillContract/totalMoneyDuiBi?billBatches='+$('#hv').val(), 
			    fit : true,
				fitColumns : true,
				border : true,
				striped:true,
				idField :'id',
				rownumbers:true,
				singleSelect:true,
				columns:[[         
					        {field:'ck',checkbox:"true"},
					        {field:'id',title:'id',hidden:true},  
							{field:'cwb',title:'订单号',sortable:true,width:100,align:'center'},	
							{field:'cwbstate',title:'订单状态',sortable:true,width:80,align:'center'},
					        {field:'cwbOrderType',title:'订单类型',sortable:true,width:75,align:'center'},    
					        {field:'paywayid',title:'支付方式',sortable:true,align:'center',width:75},				    
					        {field:'deliveryMoney',title:'提货费(系统)',sortable:true,width:100,align:'center'},
					        {field:'distributionMoney',title:'配送费(系统)',sortable:true,width:100,align:'center'},
					        {field:'transferMoney',title:'中转费(系统)',sortable:true,width:100,align:'center'},
					        {field:'refuseMoney',title:'拒收派费(系统)',sortable:true,width:100,align:'center'},
					        {field:'totalCharge',title:'派费合计(系统)',sortable:true,width:100,align:'center'},	
					        {field:'importtotalCharge',title:'派费合计(导入)',sortable:true,width:100,align:'center',formatter:clickimportAllMoney}
					    ]],
			});		
	}		
	
function clickimportAllMoney(value, row, index){
		return "<a href='javascript:;' onclick='importAllMoney(\""+row.cwb+"\")'>"+value+"</a>";
}

function changeImportToDmpMoney(){
	 var row = $('#dgMoneyChaYi').datagrid('getSelected');
		if(row){ 

			$.messager.confirm('提示','你确定要改变这条记录吗?',function(r){
				if (r){
					$.post('${pageContext.request.contextPath}/CustomerBillContract/changeImportToSystemMoney',{cwb:row.cwb,billBatches:$('#hv').val(),importtotalCharge:row.importtotalCharge},function(result){
						if (result.success==0){

							$.messager.show({	// show success message
								title: '更改成功',
								msg: result.successdata
							});// reload the data
							duiBiBillMoneyChaYi();
							$('#dgMoneyChaYi').datagrid("reload");
						} 
						
					},'json');
				}
			});	
		}
}

function changeImportToDmpAllMoney(){
	

			$.messager.confirm('提示','你确定要改变这条记录吗?',function(r){
				if (r){
					$.post('${pageContext.request.contextPath}/CustomerBillContract/changeImportToSystemAllMoney',{billBatches:$('#hv').val()},function(result){
						if (result.success==0){

							$.messager.show({	// show success message
								title: '更改成功',
								msg: result.successdata
							});// reload the data
							duiBiBillMoneyChaYi();
							$('#dgMoneyChaYi').datagrid("reload");
						} 
						
					},'json');
				}
			});	
		
}


function importAllMoney(a){

			
		$('#importAllMoneydiv').dialog('open').dialog('setTitle','导入详情 ');
/* 
		 $('#dgimportAllMoney').datagrid({    
			method: "POST",
		    url:'${pageContext.request.contextPath}/CustomerBillContract/findImportBillExcelByCwb?cwb='+a, 
		  	fit:true, 
			fitColumns:true,
			border:true,
			striped:true,
			idField:'id',
					
			columns:[[         
				        {field:'id',title:'id',hidden:true},  
						{field:'cwb',title:'订单号',sortable:true,width:100,align:'center'},						 
				        {field:'jijiaMoney',title:'基价',sortable:true,width:75,align:'center'},    
				        {field:'xuzhongMoney',title:'续重',sortable:true,align:'center',width:75},				    
				        {field:'fandanMoney',title:'返单费',sortable:true,width:100,align:'center'},
				        {field:'fanchengMoney',title:'返程费',sortable:true,width:100,align:'center'},
				        {field:'daishoukuanshouxuMoney',title:'代收款手续费',sortable:true,width:100,align:'center'},
				        {field:'posShouxuMoney',title:'POS手续费',sortable:true,width:100,align:'center'},
				        {field:'baojiaMoney',title:'保价费',sortable:true,width:100,align:'center'},	
				        {field:'baozhuangMoney',title:'包装费',sortable:true,width:100,align:'center'},
				        {field:'ganxianbutieMoney',title:'干线补贴',sortable:true,width:100,align:'center'}
				    ]]
			});				
	 var row = $('#dgMoneyChaYi').datagrid('getSelected');
			if(row){  */ 
				
		$.ajax({
			type:'POST',			
			url:'${pageContext.request.contextPath}/CustomerBillContract/findImportBillExcelByCwb?cwb='+a,
			dataType:'json',
			async:false,
			success:function(data){
				$('#ddh').html(data.cwb);
				$('#jj').html(data.jijiaMoney);
				$('#xz').html(data.xuzhongMoney);
				$('#fdf').html(data.fandanMoney);
				$('#fcf').html(data.fanchengMoney);
				$('#dsk').html(data.daishoukuanshouxuMoney);
				$('#pos').html(data.posShouxuMoney);
				$('#bjf').html(data.baojiaMoney);
				$('#bzf').html(data.baozhuangMoney);
				$('#gxbt').html(data.ganxianbutieMoney);
			
				
			}
		}); 
	} 
/* } */	
		
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
<div style="margin-top: 3cm;margin-left: 3cm">	
	<div id="dlg" class="easyui-dialog" style="width:730px;height:300px;padding:10px 20px"
			closed="true" buttons="#dlg-buttons" >
		<form id="fm" method="post">	
				<ul>
					<li>
						<div class="fitem">
							<label>客户名称:</label>
								<input type="text" name="crecustomerId" id="crecustomerId" 
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
							<input id="dstart" type="text" class="easyui-datebox" required="required" style="width:100px;" name="startdate">
							至<input id="dend" type="text" class="easyui-datebox" required="required" style="width:100px;" name="enddate">
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
							<!-- <input class="easyui-textbox" data-options="multiline:true" id="remark" style="width:600px;height:100px" name="remark"/> -->
							<textarea id="remark" rows=3 class="textarea easyui-validatebox" style="width:600px;height:70px" name="remark" maxlength="200"></textarea>
						</div>
					</li>
				</ul>		
		</form>
	</div>
</div>	
	<!-- 新增一级弹窗  操作区域 -->
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:datChongFuYanZheng()">创建</a>
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
			
				<form id="questionTypesManage" method="post" enctype="multipart/form-data">  
						<input name="billBatches" id="uploadbillBatches" type="hidden"  > 
					<div class="uploader white">
							<input type="text" class="filename" readonly="readonly"/>
							<input type="button" name="file" class="button" value="浏 览"/>
							<input type="file" size="30" id="uploadExcel" name="uploadExcel" />&nbsp;&nbsp;&nbsp;
							<!-- <a href="#"  class="easyui-linkbutton" onclick="uploadExcel()" >客户订单导入</a> -->
					</div>
				</form>	
			
		<table>
			<tr>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:clearAndCloseEdit()" id="fanhui" style="display: none">返回</a></td>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="baocun" style="display: none" onclick="javascript:clearAndCloseEdit()">保存</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="shenhe" onclick="javascript:changeBillState('<%=BillStateEnum.YiShenHe.getValue()%>')" style="display: none">审核</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="quxiaoshenhe" onclick="javascript:changeBillState('<%=BillStateEnum.WeiShenHe.getValue()%>')" style="display: none">取消审核</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="hexiaowancheng" onclick="javascript:changeBillState('<%=BillStateEnum.YiHeXiao.getValue()%>')" style="display: none">核销完成</a></td>
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="quxiaohexiao" onclick="javascript:changeBillState('<%=BillStateEnum.YiShenHe.getValue()%>')" style="display: none">取消核销</a></td>				
				<td style="margin-left: 2cm"><a href="#" class="easyui-linkbutton" iconCls="icon-ok" id="xscybg" onclick="duiBiBillMoneyChaYi()" style="display: none">显示差异报告</a></td>
				<td><a href="#" class="easyui-linkbutton" iconCls="icon-save" id="khdddr" onclick="uploadExcel()" style="display: none">客户订单导入</a></td>
			</tr>
		</table>
		<!-- 新增一级弹窗查询数据回显（根据查询结果动态拼接） -->
		<form id="fm2" method="post">
				<table class="fitem">
						<tr>
							<td align="right" style="width:90px;">账单批次</td><td style="width:30px;" ><input name="billBatches" id="billBatchess" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
							<td align="right" style="width:90px;">账单状态</td><td style="width:30px;">
							<input name="billState" id="billState" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0">
							<!-- <input type="text" name="billState" id="billState1" class="easyui-validatebox" 
									 	data-options="width:150,prompt: '账单状态'"
									 	initDataType="ENUM" 
									 	initDataKey="cn.explink.enumutil.BillStateEnum"
									 	viewField="text" 
									 	saveField="value"
									 	value="-1"
									 	
								/> -->
							</td>
							<td align="right" style="width:90px;">日期范围</td><td style="width:30px;"><input name="dateRange" id="dateRange" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">派费合计(元)</td><td style="width:30px;"><input name="totalCharge" id="totalCharge" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
							<td align="right" style="width:90px;">对应订单数</td><td style="width:30px;"><input name="correspondingCwbNum" id="correspondingCwbNum" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
							<td align="right" style="width:90px;">客户名称</td><td style="width:30px;">
							<input name="customerId" id="customernames" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0">
							<!-- <input type="text" name="customerId" class="easyui-validatebox"						
									id="customernames"
								 	data-options="width:150"
								 	initDataType="TABLE" 
								 	initDataKey="Customer"
								 	viewField="customername" 
								 	saveField="customerid"
								 	value="-1"
								 	
								/> -->
							</td>
						</tr>
						<tr>
							<td align="right" style="width:90px;">提货费(元)</td><td style="width:30px;"><input name="deliveryMoney" id="deliveryMoney" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
							<td align="right" style="width:90px;">配送费(元)</td><td style="width:30px;"><input name="distributionMoney" id="distributionMoney" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
							<td align="right" style="width:90px;">中转费(元)</td><td style="width:30px;"><input name="transferMoney" id="transferMoney" class="easyui-validatebox" readonly="readonly" style="color: #C0C0C0"></td>
						</tr>
						<tr>
							<td align="right" style="width:90px;"><label>备注:</label></td>
							<td colspan="6" rowspan="5"><textarea id="remarkEdit" rows=3 class="textarea easyui-validatebox" style="width:600px;height:70px;color: #C0C0C0" name="remark" maxlength="200" readonly="readonly"></textarea></td>	<!-- 	 style="width:600px;height:100px" -->				
						</tr>
				</table>
		</form>
		<table id="dga"></table>
		<div id="tbNewAddofEdit">
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="dlgNewAddofEdit()" id="addbille">添加</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeofEdit()" id="removebille">移除</a>	
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
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="addBillCwbNum()">确认</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgAddofEdit').dialog('close')">取消</a>
	</div>
	
	<div id="billMoneyDuiBi" class="easyui-dialog" style="width:380px;height:218px;padding:10px 20px"
			closed="true">
		<table id="dgbillMoneyDuiBi" class="gridtable">
					<tr>
						<th></th>
						<th>系统数据</th>
						<th>导入数据</th>
						<th>差异</th>
					</tr>
					<tr>
						<td>记录总数</td>
						<td><label id="systemDateCount"></label></td>
						<td><label id="importDateCount"></label></td>
						<td><a href="#" onclick="javascript:CountChaYi()"><label id="chaYiCount"></label></a></td>
					</tr>
					<tr>
						<td>金额</td>
						<td><label id="systemDateMoney"></label></td>
						<td><label id="importDateMoney"></label></td>
						<td><label id="chaYiMoney"></label></td>
					</tr>
 					<tr>
						<td>派费金额不一致记录</td>
						<td></td>
						<td></td>
						<td><a href="#" onclick="javascript:MoneyChaYi()"><label id="chaYiCwbsMoney"></label></a></td>
					</tr>
		</table>
	</div>
	
		<div id="CountChaYi" class="easyui-dialog" style="width:800px;height:500px;padding:10px 20px"
			closed="true" buttons="#dlgAddChaYi-buttons">
		<table id="dgCountChaYi" class="fitem" border="1"></table>
		</div>
		<div id="dlgAddChaYi-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="addBillCwbNumInChaYi()" id="addCurrentBill">加入当前账单</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="removeofEditInChaYi()" id="removeCurrentBill">从当前账单移除</a>
		</div>
		
		<div id="MoneyChaYi" class="easyui-dialog" style="width:1100px;height:500px;padding:10px 20px" closed="true" buttons="#dlgAddMoneyChaYi-buttons">
			<table id="dgMoneyChaYi" class="fitem" border="1"></table>
		</div>
		<div id="dlgAddMoneyChaYi-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="changeImportToDmpAllMoney()">基于导入更新全部订单配费</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="changeImportToDmpMoney()">基于导入更新订单配费</a>
		</div>
		<div id="importAllMoneydiv" class="easyui-dialog" style="width:668px;height:135px;padding:10px 20px" closed="true">
			<!--  	 <table id="dgimportAllMoney" border="1"></table>  -->
		
			<table class="gridtable">
					<tr>
						<th>订单号</th>
						<th>基价</th>
						<th>续重</th>
						<th>返单费</th>
						<th>返程费</th>
						<th>代收款手续费</th>
						<th>POS手续费</th>
						<th>保价费</th>
						<th>包装费</th>
						<th>干线补贴</th>
					</tr>
					<tr>
						<td><label id="ddh"></label></td>
						<td><label id="jj"></label></td>
						<td><label id="xz"></label></td>
						<td><label id="fdf"></label></td>
						<td><label id="fcf"></label></td>
						<td><label id="dsk"></label></td>
						<td><label id="pos"></label></td>
						<td><label id="bjf"></label></td>
						<td><label id="bzf"></label></td>
						<td><label id="gxbt"></label></td>
					</tr>
					
		</table>
		</div>
		
		
<input type="hidden" id="hv"/> <!--批次  -->
<input type="hidden" id="hv1"/>
<input type="hidden" id="hv2"/>
<input type="hidden" id="hv3"/>

</body>
</html>