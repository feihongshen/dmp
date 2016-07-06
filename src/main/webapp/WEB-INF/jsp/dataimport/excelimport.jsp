
<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%-- 
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
--%>
<%
	boolean addresstart = (Boolean) request.getAttribute("addressStart");
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branches");
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customers");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<%--  <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>  --%>
<script type="text/javascript"	src="<%=request.getContextPath()%>/dmp40/plug-in/My97DatePicker/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>

<%-- <script src="${ctx}/js/commonUtil.js" type="text/javascript"></script> --%>
<script>

var file_id;
	$(function() {
		<%--	$("#customerid").combobox({
			onChange: function (n,o) {
				//alert($('#customerid').combobox('getValue') );
				if($('#customerid').combobox('getValue')==0){
					return;
				}
				$.ajax({
					type: "POST",
					url:"<%=request.getContextPath()%>/customerwarehouses/",
					data:{customerid:$('#customerid').combobox('getValue')},
					success:function(data){
						var optionstring="";
						optionstring+="<option value='0'>请选择</option>";
						for(var i=0;i<data.length;i++){
							optionstring+="<option value='"+data[i].warehouseid+"'>"+data[i].customerwarehouse+"</option>";
						}
						$("#warehouseidflag").html(optionstring);
					}
				});
				$.ajax({
					type: "POST",
					url:"<%=request.getContextPath()%>/accountareas/",
						data : {
							customerid : $('#customerid').combobox('getValue')
						},
						success : function(data) {
							var optionstring = "";
							optionstring+="<option value='0'>请选择</option>";
							for ( var i = 0; i < data.length; i++) {
								optionstring += "<option value='"+data[i].areaid+"'>"
										+ data[i].areaname
										+ "</option>";
							}
							$("#serviceareaidflag").html(optionstring);
						}
					});
			}
			});
		--%>
		
		$("#emaildate").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		
		 $("#customerid").change(function(){
			if($(this).val()==0){
				return;
			}
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/customerwarehouses/",
				data:{customerid:$("#customerid").val()},
				success:function(data){
					var optionstring="";
					optionstring+="<option value='0'>请选择</option>";
					for(var i=0;i<data.length;i++){
						optionstring+="<option value='"+data[i].warehouseid+"'>"+data[i].customerwarehouse+"</option>";
					}
					$("#warehouseidflag").html(optionstring);
				}
			});
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/accountareas/",
					data : {
						customerid : $("#customerid").val()
					},
					success : function(data) {
						var optionstring = "";
						optionstring+="<option value='0'>请选择</option>";
						for ( var i = 0; i < data.length; i++) {
							optionstring += "<option value='"+data[i].areaid+"'>"
									+ data[i].areaname
									+ "</option>";
						}
						$("#serviceareaidflag").html(optionstring);
					}
				});
		}); 
		
		$("#importButton").click(function(){
			if($("#branchid").val()==null){
				alert("请先创建数据入库的仓库");
				return;		
			}
			if($('#customerid').val()=="0"){
				alert("请选择发货供货商");
				return;
			}
			if($("#txtFileName").val()==""){
				alert("还没有选择任何上传文件");
				return;
			}
			if($("#emaildate").val()!=""){
				var istrue = true;
				$.ajax({
					type: "POST",
					url:"<%=request.getContextPath()%>/dataimport/checkemaildate",
					dataType : "json",
					data : {
						emaildate : $("#emaildate").val(),
						customerid : $('#customerid').val(),
						warehouseid : $("#warehouseidflag").val(),
						areaid : $("#serviceareaidflag").val()
					},
					success : function(data) {
						if(data.errorCode != 0){
							alert(data.error);
							return ;
						}else{
							importExcel();
						}
					}
				});
			}else{
				importExcel();
			}
		});
		$('#swfupload-control').swfupload({
			upload_url: $("#uploadForm").attr("action"),
			file_size_limit : "10240",
			file_types : "*.xls;*.xlsx",
			file_types_description : "All Files",
			file_upload_limit : "0",
			file_queue_limit : "1",
			flash_url : "../js/swfupload/swfupload.swf",
			button_image_url: "<%=request.getContextPath()%>/images/indexbg.png",
			button_text : '选择文件',
			button_width : 50,
			button_height : 20,
			button_placeholder : $('#button')[0]
		}).bind('fileQueued', function(event, file) {
			$("#txtFileName").val(file.name);
			file_id = $('#swfupload-control');
		}).bind('fileQueueError', function(event, file, errorCode, message) {
		}).bind('fileDialogStart', function(event) {
			$(this).swfupload('cancelQueue');
			//file_id.cancelQueue();
		}).bind('fileDialogComplete',
				function(event, numFilesSelected, numFilesQueued) {
				}).bind('uploadStart', function(event, file) {
		}).bind('uploadError',function(fileobject, errorcode, message){
			$("#importButton").removeAttr("disabled"); 
			$("#stop").attr("disabled","disabled");
			$("#txtFileName").val("");
			alert("excel文件格式异常");
		}).bind('uploadProgress',
				function(event, file, bytesLoaded, bytesTotal) {
					/* var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
					$("#progressbar").progressbar({
						value : percent
					});
					$("#progressstatus").text(percent); */
		}).bind('uploadSuccess', function(event, file, serverData) {
			//alert("请求后返回的参数："+serverData);
			$("#importButton").val("正在导入");
					
			var emaildateId = "";
			function queryProgress() {
				$.ajax({
					type: "POST",
					url : "<%=request.getContextPath()%>/result/" + serverData.split(",")[0],
					success : function(data) {
						$("#anyid").val(data.id);
						$("#successCount").html(data.successSavcNum);
						$("#failureCount").html(data.failSavcNum);
						if (!data.finished) {
							setTimeout(function(){queryProgress();}, 100);
						}else{
							$("#importButton").removeAttr("disabled"); 
							$("#importButton").val("开始导入"); 
							if(!$("#stop").attr("disabled")){
								alert($("#txtFileName").val()+"导入完成");
							}
							$("#stop").attr("disabled","disabled");
							$("#txtFileName").val("");
							emaildateId=data.emaildateid;
							$("#Sto").attr("href","reexcelimportPage?emaildate="+serverData.split(",")[1]+"&isSuccess=1&emaildateid="+emaildateId);
							$("#Fto").attr("href","reexcelimportPage?emaildate="+serverData.split(",")[1]+"&isSuccess=0&emaildateid="+emaildateId);
							$("#successCount").html(data.successSavcNum);
							$("#failureCount").html(data.failSavcNum);
						}
						
						
						<%-- $.ajax({
							type: "POST",
							url : "<%=request.getContextPath()%>/result/getSuccessAndFailureCount/" + serverData.split(",")[1],
							success : function(dataSAndF) {
								$("#successCount").html(dataSAndF.SuccessCount);
								$("#failureCount").html(dataSAndF.FailureCount);
								emaildateId=dataSAndF.key;
							}
						}); --%>
					}
				});
			}
			setTimeout(function(){queryProgress();}, 100);
		}).bind('uploadComplete', function(event, file) {
			$(this).swfupload('startUpload');
		}).bind('uploadError', function(event, file, errorCode, message) {
		});

		$("#stop").click(function(){
			$("#stop").attr("disabled","disabled");
			$.ajax({
				type: "POST",
				url : "<%=request.getContextPath()%>/result/stop/" + $("#anyid").val(),
				success : function(data) {
					alert($("#txtFileName").val()+"已停止导入");
				},
				error : function(event){
					$("#stop").removeAttr("disabled");
				}
			});
		});
	});
	
	
	function importExcel(){
		$.ajax({
			type: "POST",
			url : "<%=request.getContextPath()%>/cwbordertype/count/",
			success : function(data) {
				if(data==0&&!confirm("目前系统中没有可用的“订单类型”，所有匹配不到订单类型的订单都将默认为“配送”类型订单，您确定要继续导入么？")){
					return ;
				}
				$.ajax({
					type: "POST",
					url : "<%=request.getContextPath()%>/payway/count/",
					success : function(data) {
						if(data==0&&!confirm("目前系统中没有可用的“支付方式”，所有匹配不到支付方式的订单都将默认为“现金”支付方式的订单，您确定要继续导入么？")){
							return ;
						}
							$("#importButton").attr("disabled","disabled");
							$("#importButton").val("正在上传");
							$("#stop").removeAttr("disabled"); 
							//$('#swfupload-control').swfupload('addPostParam','dizhikuflag',$("#dizhikuflag").val());
							$('#swfupload-control').swfupload('addPostParam','customerid',$('#customerid').val());
							$('#swfupload-control').swfupload('addPostParam','warehouseid',$("#warehouseidflag").val());
							$('#swfupload-control').swfupload('addPostParam','areaid',$("#serviceareaidflag").val());
							$('#swfupload-control').swfupload('addPostParam','branchid',$("#branchid").val());
							$('#swfupload-control').swfupload('addPostParam','emaildate',$("#emaildate").val());
							$('#swfupload-control').swfupload('startUpload');
						
					}
				});
				
			}
		});
	}
	$(function(){
		 $("#branchid").combobox();
		 /* $("#customerid").combobox(); */
		 $("#serviceareaidflag").combobox();
		/*  $("#warehouseidflag").combobox(); */
		$("div.panel.combo-p").css({"margin-top":"-78px","width":"155px"});
		$("div[class='combo-panel panel-body panel-body-noheader']").css({"height":"198px"});
	})
</script>
</head>
<body  style="background:#f5f5f5">


<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="#" class="light">导入数据</a></li>
			<li><a href="reexcelimportPage" >导入查询</a></li>
		</ul>
	</div>
	<form name="uploadForm" id="uploadForm" method="POST" action="excelimport;jsessionid=<%=session.getId() %>" enctype="multipart/form-data" >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="350"><div style="float: left;">订单入库库房：
						<select id="branchid" name="branchid" class="select1">
						<%for (Branch branch : branchlist) {%>
						<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
						<%}%>
						</select></div>
					</td>
					<td width="300"><div style="float: left;">发件供货商：
						<%--  <select name="customerid" id="customerid" class="select1">
							<option value="0">请选择</option>
							<%for (Customer customer : customerlist) {%>
							<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
							<%}%>
						</select>* --%>
								<select name="customerid" id="customerid" style="width:150px;"  >
									<option value="0">请选择</option>
									<%for (Customer customer : customerlist) {%>
										<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
									<%}%>
								</select>*</div>
					</td>
					<td><div style="float: left;">发货仓库：
						<select name="warehouseid" id="warehouseidflag" class="select1">
								<option value="0">请选择</option>
						</select></div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>结算区域：
					
					<span style="height: 25">
					<select name="serviceareaid" id="serviceareaidflag" class="select1">
							<option value="0">请选择</option>
					</select>
					</span></td>
					<td>发货批次：
					
					<span style="height: 25">
					<input type="text" id="emaildate" name="emaildate" >
					</span></td>
					<td>EXCEL表格导入：
						<label for="fileField"></label>
						<span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button" /></span>*</td>
					<td><input name="button35" type="button" id="importButton" value="开始导入" class="input_button2" title="此功能只会插入“新订单”记录或者更新“有货无单入库”记录" />
					<input type="button" name="stop" id="stop" value="停止导入" disabled="disabled" class="input_button2" />
					<input type="hidden" id="anyid"/>
					</td>
				</tr>
			</table>
		</form>
				<div class="table_midbg">
							<ul>
								<li><a id="Sto" href="#">成功(<label id="successCount" style="font-size:18;font-weight: bold;color:red;">0</label>)</a></li>
								<li><a id="Fto" href="#" >失败(<label id="failureCount" style="font-size:18;font-weight: bold;color:red;">0</label>)</a></li>
							</ul>
						</div>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" style="display: none" class="table_2" >
							<tr class="font_1">
								<td width="30%" align="center" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">错误描述</td>
								<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
							</tr>
						</table>
						<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" style="display: none"  id="messages">
							<tr>
								<td width="30%"  align="center" height="38" >订单号</td>
								<td align="center"  >错误描述</td>
								<td width="20%" align="center" >【<a href="#">删除</a>】【<a href="#">修改</a>】</td>
							</tr>
						</table>
			
				
</div>

</body>
</html>
