
<%@page import="java.util.List"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	boolean addresstart = (Boolean) request.getAttribute("addressStart");
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branches");
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customers");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script>
	$(function() {
		$("#emaildate").datepicker();
		$("#shiptime").datepicker();
		$("#emailtimeflag").click(function(){
			if($(this).attr("checked")){
				$("#emaildate").show();
			}else{
				$("#emaildate").hide();
			}
		});
		$("#shiptimeflag").click(function(){
			if($(this).attr("checked")){
				$("#shiptime").show();
			}else{
				$("#shiptime").hide();
			}
		});
		$("#warehouseflag").click(function(){
			if($(this).attr("checked")){
				$("#warehouseidflag").show();
			}else{
				$("#warehouseidflag").hide();
			}
		});
		$("#serviceareaflag").click(function(){
			if($(this).attr("checked")){
				$("#serviceareaidflag").show();
			}else{
				$("#serviceareaidflag").hide();
			}
		});
		$("#customerid").change(function(){
			if($(this).val()==0){
				return;
			}
			$.ajax({
				url:"<%=request.getContextPath()%>/customerwarhouses/",
				data:{customerid:$("#customerid").val()},
				success:function(data){
					var optionstring="";
					for(var i=0;i<data.length;i++){
						optionstring+="<option value='"+data[i].warhouseid+"'>"+data[i].customerwarehouse+"</option>";
					}
					$("#warehouseidflag").html(optionstring);
				}
			});
			$.ajax({
				url:"<%=request.getContextPath()%>/accountareas/",
					data : {
						customerid : $("#customerid").val()
					},
					success : function(data) {
						var optionstring = "";
						for ( var i = 0; i < data.length; i++) {
							optionstring += "<option value='"+data[i].warhouseid+"'>"
									+ data[i].areaname
									+ "</option>";
						}
						$("#serviceareaidflag").html(optionstring);
					}
				});
		});
		
		$("#importButton").click(function(){
			if($("#customerid").val()=="0"){
				alert("请选择发货供货商");
				return;
			}
			if($("#txtFileName").val()==""){
				alert("还没有选择任何上传文件");
				return;
			}
			$(this).attr("disabled","disabled");
			$('#swfupload-control').swfupload('addPostParam','dizhikuflag',$("#dizhikuflag").val());
			$('#swfupload-control').swfupload('addPostParam','customerid',$("#customerid").val());
			$('#swfupload-control').swfupload('addPostParam','branchid',$("#branchid").val());
			$('#swfupload-control').swfupload('startUpload');
		});
		$('#swfupload-control').swfupload({
			upload_url: "excelimport;jsessionid=<%=session.getId() %>",
			file_size_limit : "10240",
			file_types : "*.xls;*.xlsx",
			file_types_description : "All Files",
			file_upload_limit : "0",
			file_queue_limit : "1",
			flash_url : "../js/swfupload/swfupload.swf",
			button_text : "选择文件",
			button_width : 61,
			button_height : 22,
			button_placeholder : $('#button')[0]
		}).bind('fileQueued', function(event, file) {
			$("#txtFileName").val(file.name);
		}).bind('fileQueueError', function(event, file, errorCode, message) {
		}).bind('fileDialogStart', function(event) {
		}).bind('fileDialogComplete',
				function(event, numFilesSelected, numFilesQueued) {
				}).bind('uploadStart', function(event, file) {
		}).bind('uploadProgress',
				function(event, file, bytesLoaded, bytesTotal) {
					var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
					$("#progressbar").progressbar({
						value : percent
					});
					$("#progressstatus").text(percent);
				}).bind('uploadSuccess', function(event, file, serverData) {
			$("#progressstatus").text("上传成功");
			function queryProgress() {
				$.ajax({
					url : "/dmp/result/" + serverData,
					success : function(data) {
						if (!data.finished) {
							setTimeout(queryProgress, 10)
						}else{
							$("#importButton").removeAttr("disabled"); 
						}
						$("#errorConsole").html();
						for ( var i = 0; i < data.errors.length; i++) {
							$("#errorConsole").append(data.errors[i]);
						}
					}
				});
			}
			setTimeout(queryProgress, 10);
		}).bind('uploadComplete', function(event, file) {
			$(this).swfupload('startUpload');
		}).bind('uploadError', function(event, file, errorCode, message) {
		});

	});
</script>
</head>
<body>

	<div id="breakcrumbar">当前位置：Excel导入 -->导入数据</div>
	<form name="form1" method="POST" action="commonupload" enctype="multipart/form-data" target="_self">
		<table width="100%" class="tr-gra" cellspacing="1" height="23">

			<tr id="customertr" class=VwCtr>
				<td align="right"><B><FONT SIZE=2> <input name="dizhikuflag" type="checkbox" id="dizhikuflag" value="1" <%if (addresstart) {%> checked="checked" <%}%>>
					</FONT></B></td>
				<td align="left"><span class="STYLE1"><strong>按地址库关键词匹配</strong></span><strong> <B><FONT SIZE=2></td>
			</tr>

			<tr id="customertr" class=VwCtr style="display:">
				<td width="20%" align="right">订单入库站点：</td>
				<td width="89%" align="left"><select id="branchid" name="branchid">
						<%
							for (Branch branch : branchlist) {
						%>
						<option value="<%=branch.getBranchid()%>">
							<%=branch.getBranchname()%></option>
						<%
							}
						%>
				</select> 发件供货商： <select name="customerid" id="customerid">
						<option value="0">请选择</option>
						<%
							for (Customer customer : customerlist) {
						%>
						<option value="<%=customer.getCustomerid()%>">
							<%=customer.getCustomername()%></option>
						<%
							}
						%>
				</select></td>
			</tr>
			<tr>
				<td align="right">发货仓库：<input type="checkbox" name="warehouseflag" id="warehouseflag" />
				</td>
				<td><select name="warehouseid" id="warehouseidflag" style="display: none">
						<option value="0">请选择</option>
				</select></td>
			</tr>
			<tr>
				<td align="right">结算区域：<input type="checkbox" name="serviceareaflag" id="serviceareaflag" /></td>
				<td><select name="serviceareaid" id="serviceareaidflag" style="display: none">
						<option value="0">请选择</option>
				</select></td>
			</tr>
			<tr>
				<td align="right">邮件时间：<input name="emailtimeflag" type="checkbox" id="emailtimeflag">
				</td>
				<td style="height: 25"><input type="text" id="emaildate" name="emaildate" style="display: none"></td>
			</tr>
			<tr class=VwCtr id="" style="display:">
				<td align="right">供货商发货时间：<input name="shiptimeflag" type="checkbox" id="shiptimeflag" value="1">
				</td>
				<td style="height: 25"><input type="text" id="shiptime" name="shiptime" style="display: none"></td>
				</td>
			</tr>
			<tr class=VwCtr id="cwbonetr1" style="">
				<td align="right">Excel文件：&nbsp;&nbsp;<font color="red"> </font></td>
				<td align="left"> <span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button" /></span>
					<div id="progressstatus"></div>
					<div id="progressbar"></div> <span class="STYLE1">Excel导入注意事项</span> <input type="checkbox" name="wuyoujiantableflag2" value="1">
					<div id="showone"></div>
				</td>
			</tr>
			<tr class=VwCtr id="cwbonetr2" style="display: none">
				<td align="right">文本文件：&nbsp;&nbsp;<font color="red"> </font></td>
				<td align="left"><textarea name="importtxtinfo" id="importtxtinfo" wrap="off" cols="88" rows="8"></textarea></td>
			</tr>

			<tr>
				<td colspan="2" align="center"><input name="button35" type="button" id="importButton" class="button" value="数据导入" style="width: 120px; height: 35px; font: 14px"></td>
			</tr>
		</table>
	</form>
	<div id="errorConsole"></div>
	<div id="cwbtr133111" style="display: none">
		[注意：<br> 1 导入数据必须要对Excel文件做预处理，Excel请使用Excel2000或以上版本<br> 2 每个导入文件中只能有一个sheet，请预先替换掉无效字符，类似&lt;、|、?、!、~、 ^.。<br> 3 第一行为表头，表头不能为空，条码号只能由数字和字母组成<br> 4 第二行开始为实际需要导入的数据，导入数据的第一列不能为空<br> 5 指定了派送分站和派送员的订单，请检查名称是否与系统设置的完全一致，名称中不能含有任何空格和其他字符，否则无法正确匹配 <br> 6
		导入成功的数据表示excel数据已导入数据库，导入失败的数据请检查Excel数据后重新导入]
	</div>
</body>
</html>
















