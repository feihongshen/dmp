<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>邮政小包打印面单/绑定运单号</TITLE>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/layer.min.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40/eap/sys/js/eapTools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/curdtools.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/dmp40//plug-in/tools/easyuiextend.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"></script>
	
	<link type="text/css" href="<%=request.getContextPath()%>/css/2.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/reset.css" rel="stylesheet"></link>
	<link type="text/css" href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet"/>
	<link type="text/css" href="<%=request.getContextPath()%>/js/multiSelcet/multiple-select.css" rel="stylesheet"/>
    <link type="text/css" href="<%=request.getContextPath()%>/css/easyui/themes/default/easyui.css" rel="stylesheet" media="all"/>
	<link type="text/css" href="<%=request.getContextPath()%>/dmp40/plug-in/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
	<link id="skinlayercss" href="<%=request.getContextPath()%>/dmp40/eap/sys/plug-in/layer/skin/layer.css" rel="stylesheet" type="text/css">
	<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0 >
		<param name="CompanyName" value="北京易普联科信息技术有限公司" />
		<param name="License" value="653717070728688778794958093190" />
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 companyname="北京易普联科信息技术有限公司" license="653717070728688778794958093190"></embed>
	</object>
	<script src="<%=request.getContextPath()%>/js/LodopFuncs.js" type="text/javascript"></script>
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
				<div class="form-inline" style="padding:10px">
				    <label style="width:400px;">订单/运单号：<input type="text" class="saomiao_inputtxt" id="scancwb" name="scancwb" value="" style="width:296px;height:45px;" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){scanCwbConfirm()}'/></label>
				    <label style="width:100px;"><input id="printButton" name="print" type="checkbox" value="0" onclick="checkIsInstall()" checked="checked"/>打印面单 </label>
				    <label style="width:100px;"><input name="print" type="checkbox" value="1" />重新绑定 </label>
				</div>
		</div>
	</div>
	<div id="dlg_Common" class="easyui-dialog" style="width: 500px; height: 250px; padding: 10px 10px"	closed="true" buttons="#dlgCommon-buttons">
		<div id="bingContainer">
			 <label style="height:40px;"></label>
			 <label>邮政运单号： <input type="text" class="saomiao_inputtxt" id="scanemscwb" name="scanemscwb" value="" style="width:296px;height:45px;" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){emsTransCwbConfirm()}'/></label>
		</div>
		<div id="rebingContainer">
		     <label style="height:10px;"></label> 
			 <label>已绑定运单号：<select id="emscwb_old" name="emscwb_old"></select></label>
			 <label style="height:20px;"></label> 
			 <label>邮政运单号： <input type="text" class="saomiao_inputtxt" id="scanemscwbrebing" name="scanemscwbrebing" value="" style="width:296px;height:45px;" onKeyDown='if(event.keyCode==13&&$(this).val().length>0){emsTransCwbConfirm()}'/></label>
		</div>
	</div>
	<div id="dlgCommon-buttons">
		<div class="btn btn-default" id="importButton2" onclick="closeWindow()">
			关闭
		</div>
	</div>
	<div id="downlodLodop">
		<h3><font color='#FF00FF'>打印控件未安装或需要升级！点击这里<a style="text-decoration:underline;" href="<%=request.getContextPath()%>/js/install_lodop32.exe" target="_self">执行安装</a>,安装或升级后请刷新页面或重新进入。</font></h3>
	</div>
<script type="text/javascript">
 var _ctx = "<%=request.getContextPath()%>";
 var cwb;
 var transcwb;
 var scancwb;
 var isrebing = false;
 function showWindow() {
	if (isrebing) {
		$('#dlg_Common').dialog('open').dialog('setTitle', '重新绑定');
		$('#scanemscwbrebing').val('');
		$('#scanemscwbrebing').focus();
	} else {
		$('#dlg_Common').dialog('open').dialog('setTitle', '邮政运单绑定');
		$('#scanemscwb').val('');
		$('#scanemscwb').focus();
	}
 }  
 function closeWindow() {
	$('#dlg_Common').dialog('close');
	$('#scancwb').val('');
	$('#scancwb').focus();
 }
 function initShowWindow(list) {
	if (isrebing) {
	    $("#emscwb_old").empty();
	    $.each(list, function(index,item) {
		   $("#emscwb_old").append("<option value='"+item.email_num+"'>"+item.email_num+"</option>");
	    });
	    $("#rebingContainer").show();
	    $("#bingContainer").hide();
	} else {
		$("#rebingContainer").hide();
		$("#bingContainer").show();
    }
 }
 function scanCwbConfirm() {
	scancwb = $('#scancwb').val();
	if (!scancwb) {
		alert("请输入订单/运单号！");
		return;
	}
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
	var url =  _ctx+"/emsSmallPackage/queryCwb?scancwb="+scancwb;
	$("input[name='print']:checkbox").each(function(){ 
		var value = $(this).val();
		var check = $(this).prop('checked');
		if (check && value=='0') {
			url += "&printcwb=1"
		}
		if (value=='1') {
			if (check) {
				url += "&rebingcwb=1"
				isrebing = true;
			} else {
				isrebing = false;
			}
		}
	});
	$.ajax({
		type: "POST",
		url:url,
		dataType : "json",
		success : function(data) {
			layer.close(layEle);
			var cwbOrder = data.cwbOrder;
			var list = data.list;
			var result = JSON.parse(data.result);
			if (result.result == 'success') {
				if (cwbOrder) {
					cwb = cwbOrder.cwb;
					transcwb = cwbOrder.transcwb;
					initShowWindow(list);
					showWindow();
					// 打印
					if(data.isPrint == true) {
						printEmsLabel(cwbOrder);
					}
				} 
			} else {
				alert(result.result);
				$('#scancwb').val('');
				$('#scancwb').focus();
			}
		}
	});
 }
 function emsTransCwbConfirm() {
	//var scancwb = $('#scancwb').val();
	var scanemscwb;
	var url;
	if (isrebing) {//重新绑定
		scanemscwb = $('#scanemscwbrebing').val();
		var emscwbOld = $("#emscwb_old").val();
	    if (!emscwbOld) {
	    	alert("请选择已绑定的邮政运单号！");
	    	return;
	    }
	    url = _ctx+"/emsSmallPackage/rebingCwb?emscwb="+scanemscwb+"&emscwbOld="+emscwbOld; 
	} else {
	    scanemscwb = $('#scanemscwb').val();
	    url = _ctx+"/emsSmallPackage/bingCwb?scancwb="+cwb+"&scantranscwb="+scancwb+"&emsscancwb="+scanemscwb;
	}
	if (!scanemscwb) {
		alert("请输入邮政运单号！");
		return;
	}
	//数据加载动画
	var layEle = layer.load({
		type:3
	});
	$.ajax({
		type: "POST",
		url: url,
		dataType : "json",
		success : function(data) {
			layer.close(layEle);
			var result = JSON.parse(data.result);
			var list = data.list;
			if (result.result == 'success') {
				closeWindow();
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
 
 	// 打印
 	function printEmsLabel(cwbOrder){
 		var LODOP=getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM')); 
 		if(!LODOP.VERSION) {
 			downlodLodop();
 			return;
 		}
 		LODOP.PRINT_INITA(0,0,"230mm", "127.2mm","邮政面单打印");
 		LODOP.SET_PRINT_PAGESIZE(0, 2300, 1272, "A4");
 		LODOP.SET_PRINT_STYLE("FontSize",12);
 		LODOP.NewPage();
		LODOP.ADD_PRINT_TEXT(78,24,150,20, "武汉飞远");
		LODOP.ADD_PRINT_TEXT(78,179,150,20, "027-82668066");
		LODOP.ADD_PRINT_TEXT(105,24,150,20, "武汉飞远");
		LODOP.ADD_PRINT_TEXT(105,259,150,20, "42010301326000");
		LODOP.ADD_PRINT_TEXT(128,24,150,20, "武汉市");
		LODOP.ADD_PRINT_TEXT(151,24,150,20, cwbOrder.cwb);
		LODOP.ADD_PRINT_TEXT(188,24,150,20, cwbOrder.consigneename);
		var mobile = cwbOrder.consigneemobile; // 联系方式手机优先，电话其次
		if (mobile == null || mobile == "") {
			mobile = cwbOrder.consigneephone;
		}
		LODOP.ADD_PRINT_TEXT(188,179,150,20, mobile);
		LODOP.ADD_PRINT_TEXT(236,24,300,20, cwbOrder.consigneeaddress);
		LODOP.PRINT();
	}
 	
 	// 检测打印控件是否有效
 	function checkIsInstall() {
 		if($("#printButton").is(':checked')) {
	 		var LODOP = getLodop("<%=request.getContextPath()%>",document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
	 		if(!LODOP.VERSION) {
	 			downlodLodop();
	 		}
 		}
	}
	
 	// 提示打印控件失效并提供下载链接
	function downlodLodop() {
		$.layer({
          	type: 1,
            title: "打印控件未安装或需要升级！",
            shadeClose: true,
            maxmin: false,
            fix: false,
            area: [400, 200],
            page: {
                dom: '#downlodLodop'
            }
        });
	}

</script>
</body>
</HTML>