<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>邮政小包订单运单号关系查询</TITLE>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker4.8/WdatePicker.js"></script>
	
	<link type="text/css" href="<%=request.getContextPath()%>/css/2.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet" />
	<link type="text/css" href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" rel="stylesheet" media="all"/>
	<link type="text/css" href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
</HEAD>
<body class="easyui-layout" leftmargin="0" topmargin="0">
    <div data-options="region:'center'" style="height:100%;overflow-x: auto; overflow-y: auto;">
		<table id="dg"
				class="easyui-datagrid" toolbar="#cwb_toolbar" rownumbers="true" pagination="true" fit="true"
		  		url="" pageSize="10" pageList="[10,20,50,100]" showFooter="true" fitColumns="false" singleSelect="false" 
				width="100%">
				<thead>
					<tr>
						<th field="cwb" align="center" width="120px;">订单号</th>
						<th field="transcwb" align="center" width="120px;">运单号</th>
						<th field="email_num" align="center" width="120px;">邮政运单号</th>
						<th field="deliveryBranchName" align="center" width="150px;">配送站点</th>
						<th field="outWarehouseTime" align="center" width="120px;">出库时间</th>
						<th field="consigneename" align="center" width="100px;">收件人</th>
						<th field="consigneeaddress" align="center" width="450px;">收件地址</th>
					</tr>
				</thead>
			</table>
			<div id="cwb_toolbar">
				<div class="form-inline" style="padding:10px;">
				   <table  cellspacing="0" border="0" cellpadding="0" width="100%">
				     <div style="border: 1px;border-bottom-color:grey;">
				       <tr>
				          <td width="270px">
				           <label style="margin-left:20px;margin-right:50px;font-size:12px;"><input name="cwbType" type="radio" value="0" checked="checked" />订单号</label>
                           <label style="font-size:12px"><input name="cwbType" type="radio" value="1" />运单号 </label>
				          </td>
				          <td></td>
				       </tr>
				       <tr>
				          <td rowspan="2">
				          <label><textarea id="querycwb" rows="3" cols="20"></textarea></label>
				          </td>
				          <td style="text-align:left;vertical-align:middle;height:30px;font-size:12px;">
				                                   出库时间： <input type="text" name="starttime" id="starttime" style="width: 200px;height: 30px;"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 至
							   <input type="text" name="endtime" id="endtime" style="width: 200px;height: 30px;"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				          </td>
				          <td></td>
				       </tr>
				       <tr>
				          <td style="text-align:left;vertical-align:middle;height:40px;font-size:12px;">推送状态： <select id="status"><option value="0">全部</option><option value="1">未推送</option><option value="2">已推送</option></select></td>
				          <td></td>
				       </tr>
				      </div> 
				      <div style="border: 1px;border-bottom-color: grey;margin-top: 4px;margin-left: 5px;" >
					   <tr>
						<td>
							<div class="btn btn-default" onclick="query();" style="text-align: right; vertical-align: middle; margin-right:30px;"><i class="icon-search"></i>查询</div>
			          		<div class="btn btn-default" onclick="exportExcel();" style="margin-right:5px;"><i class="icon-file"></i>导出</div>
						</td>
						<td>
						</td>
					  </tr>
				      </div>
				    </table>
				</div>
		   </div>
	</div>
<script type="text/javascript">
 var _ctx = "<%=request.getContextPath()%>";
 function query () {
	var cwbType = $("input[name='cwbType']:checked").val();
	var querycwb = $("#querycwb").val()
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var status = $("#status").val();
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
	var url =  _ctx+"/emsSmallPackage/queryCwbList?cwbtype="+cwbType+"&cwb="+querycwb+"&starttime="+starttime+"&endtime="+endtime+"&status="+status;
	$.ajax({
		type: "POST",
		url:url,
		dataType : "json",
		success : function(data) {
			layer.close(layEle);
			var result = JSON.parse(data.result)
			var list = data.list;
			if (result.result == 'success') {
				initDataGrid(list);
			} else {
				alert(result.result);
			}
		}
	});
 }
 
 function initDataGrid(gridData) {
	  $("#dg").datagrid('loadData',[]); // 清空数据
	  $('#dg').datagrid({loadFilter:pagerFilter}).datagrid('loadData', gridData);//加载数据
 }

 function pagerFilter(data){
   if (typeof data.length == 'number' && typeof data.splice == 'function'){ // 判断数据是否是数组
	     data = {
	         total: data.length,
	         rows: data
	     }
	}
   var dg = $("#dg");
   var opts = dg.datagrid('options');
   var pager = dg.datagrid('getPager');
   pager.pagination({
	        onSelectPage:function(pageNum, pageSize){
	            opts.pageNumber = pageNum;
	            opts.pageSize = pageSize;
	            pager.pagination('refresh',{
	                pageNumber:pageNum,
	                pageSize:pageSize
	            });
	            dg.datagrid('loadData',data);
	     }
	});
   if (!data.originalRows){
	     data.originalRows = (data.rows);
   }
   var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
   var end = start + parseInt(opts.pageSize);
   data.rows = (data.originalRows.slice(start, end));
   return data;
 }
 
 function exportExcel() {
	var fieldDef = new Array();
	var fields = $('#dg').datagrid('getColumnFields');
	for (var i = 0; i < fields.length; i++) {
		var col = $('#dg').datagrid('getColumnOption', fields[i]);
		fieldDef.push({
			field:col.field,
			width:col.width,
			title:col.title,
			rowspan:col.rowspan,
			colspan:col.colspan
		})
	}
	
	var cwbType = $("input[name='cwbType']:checked").val();
	var querycwb = $("#querycwb").val()
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var status = $("#status").val();

	var form = $("<form>");   //定义一个form表单
	form.attr('style', 'display:none');   //在form表单中添加查询参数
	form.attr('target', 'exportFrame');
	form.attr('method', 'post');
	form.attr('action', _ctx + "/emsSmallPackage/export");

	var input1 = $('<input>');
	input1.attr('type', 'hidden');
	input1.attr('name', 'cwbType');
	input1.attr('value', cwbType);
	form.append(input1);
	
	var input2 = $('<input>');
	input2.attr('type', 'hidden');
	input2.attr('name', 'querycwb');
	input2.attr('value', querycwb);
	form.append(input2);	
	
	var input3 = $('<input>');
	input3.attr('type', 'hidden');
	input3.attr('name', 'starttime');
	input3.attr('value', starttime);
	form.append(input3);
	
	var input4 = $('<input>');
	input4.attr('type', 'hidden');
	input4.attr('name', 'endtime');
	input4.attr('value', endtime);
	form.append(input4);
	
	var input5 = $('<input>');
	input5.attr('type', 'hidden');
	input5.attr('name', 'status');
	input5.attr('value', status);
	form.append(input5);
	
	var input6 = $('<input>');
	input6.attr('type', 'hidden');
	input6.attr('name', 'columnDefs');
	input6.attr('value', JSON.stringify(fieldDef));
	form.append(input6);
	
	$('body').append(form); 
	form.submit();
} 
 
</script>
</body>
</HTML>