<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.CwbError"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	boolean addresstart = (Boolean) request.getAttribute("addressStart");
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branches");
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customers");
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
	
	String emaildateidParam = request.getParameter("emaildateid")==null?"":request.getParameter("emaildateid");
	
	Page Successpage_obj = request.getAttribute("Successpage_obj")==null?new Page():(Page)request.getAttribute("Successpage_obj");
	List<CwbOrder> SuccessList = request.getAttribute("SuccessOrder")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("SuccessOrder");
	Page Failurespage_obj = request.getAttribute("Failurespage_obj")==null?new Page():(Page)request.getAttribute("Failurespage_obj");
	List<CwbError> FailuresList = request.getAttribute("FailuresOrder")==null?new ArrayList<CwbError>():(List<CwbError>)request.getAttribute("FailuresOrder");
	
	//判断是否为成功记录翻页操作
	String isSuccess = request.getParameter("isSuccess")==null?"":request.getParameter("isSuccess");
	
	String showphoneflag = session.getAttribute("showphoneflag")==null?"0":(String)session.getAttribute("showphoneflag");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script>
	$(function() {
		$("#reUpload").click(function(){
			
			if($("#emaildate").val()==""){
				alert("请选择发货批次");
				return;
			}
			if(<%=SuccessList.size()==0&&FailuresList.size()==0%>){
				alert("请先获取导入结果");
				return;
			}
			if($("#branchid").val()==null){
				alert("请先创建数据入库的仓库");
				return;		
			}
			if($("#customerid").val()=="0"){
				alert("请选择发货供货商");
				return;
			}
			if($("#txtFileName").val()==""){
				alert("还没有选择任何上传文件");
				return;
			}
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
					
							$("#reUpload").val("正在上传");
							$("#reUpload").attr("disabled","disabled");
							$("#stop").removeAttr("disabled"); 
							//$('#swfupload-control').swfupload('addPostParam','dizhikuflag',$("#dizhikuflag").val());
							$('#swfupload-control').swfupload('addPostParam','customerid',$("#customerid").val());
							$('#swfupload-control').swfupload('addPostParam','warehouseid',$("#warehouseidflag").val());
							$('#swfupload-control').swfupload('addPostParam','areaid',$("#serviceareaidflag").val());
							$('#swfupload-control').swfupload('addPostParam','branchid',$("#branchid").val());
							$('#swfupload-control').swfupload('addPostParam','emaildateid',$("#emaildate").val());
							$('#swfupload-control').swfupload('addPostParam','emaildate',$("#emaildate").find("option:selected").text().substring(0,19));
							$('#swfupload-control').swfupload('addPostParam','isReImport',"yes");
							$('#swfupload-control').swfupload('startUpload');
						}
					});
				}
			});
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
		}).bind('fileQueueError', function(event, file, errorCode, message) {
		}).bind('fileDialogStart', function(event) {
			$(this).swfupload('cancelQueue');
		}).bind('fileDialogComplete',
				function(event, numFilesSelected, numFilesQueued) {
				}).bind('uploadStart', function(event, file) {
		}).bind('uploadError',function(fileobject, errorcode, message){
			$("#importButton").removeAttr("disabled"); 
			$("#stop").attr("disabled","disabled");
			$("#txtFileName").val("");
			alert(errorCode+message);
		}).bind('uploadProgress',
				function(event, file, bytesLoaded, bytesTotal) {
					/* var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
					$("#progressbar").progressbar({
						value : percent
					});
					$("#progressstatus").text(percent); */
		}).bind('uploadSuccess', function(event, file, serverData) {
			$("#reUpload").val("正在导入");
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
							$("#reUpload").val("重新导入"); 
							$("#reUpload").removeAttr("disabled"); 
							if(!$("#stop").attr("disabled")){
								alert($("#txtFileName").val()+"导入完成");
							}
							$("#stop").attr("disabled","disabled");
							$("#txtFileName").val("");
							selectCwbByEmail(1,1);
							$("#successCount").html(data.successSavcNum);
							$("#failureCount").html(data.failSavcNum);
							return ;
						}
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
		 
		
		
		$("#SuccessListButton").click(function(){
			$('#failureOrder').hide();
			$('#view_Failurespage').hide();
			$('#successOrder').addClass('light');
			$('#successOrder').show();
			$('#view_Successpage').show();
			$("#FailureListButton").removeClass("light");
			$(this).addClass("light");
			return false;
		});
		$("#FailureListButton").click(function(){
			$('#successOrder').hide();
			$('#view_Successpage').hide();
			$('#failureOrder').addClass('light');
			$('#failureOrder').show();
			$('#view_Failurespage').show();
			$("#SuccessListButton").removeClass("light");
			$(this).addClass("light");
			return false;
		});
		
		$("#emaildate").change(function(){
			$("#reUpload").attr("disabled","disabled");
		});
		//初始化各种选择条件
		if($("#emaildate").val().length>0){
			$("#customerid").val($("#emaildate option[value='"+$("#emaildate").val()+"']").attr("customerid"));
		}else{
			$("#customerid").val(0);
		}
		changeCustomerid();
		if($("#emaildate").val().length==0){
			$("#reUpload").attr("disabled","disabled");
		}else{
			if($("#emaildate").find("option:selected").text().indexOf("已到货")>0){
				$("#reUpload").attr("disabled","disabled");
			}else{
				$("#reUpload").removeAttr("disabled");
			} 
		}
	});
function selectCwbByEmail(Successpage,Failurespage){
	if($("#emaildate").val()==""){
		alert("请选择发货批次");
		return;
	}
	var isSuccess = 0;
	if(Successpage>0){
		$("#Successpage").val(Successpage);
		isSuccess = 1;
	}
	if(Failurespage>0){
		$("#Failurespage").val(Failurespage);
		isSuccess = 0;
	}
	$("#reUpload").removeAttr("disabled");
	location.href="reexcelimportPage?toSuccessPage="+$("#Successpage").val()+"&toFailuresPage="+$("#Failurespage").val()+"&emaildateid="+$("#emaildate").val()+"&isSuccess="+isSuccess;
}
function changeCustomerid(){
	if($("#customerid").val()==0){
		$("#").html("");
		$("#serviceareaidflag").html("");
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
			if($("#customerid").val()>0){
				$("#warehouseidflag").val($("#emaildate option[value='"+$("#emaildate").val()+"']").attr("warehouseid"));
			}
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
				if($("#customerid").val()>0){
					$("#serviceareaidflag").val($("#emaildate option[value='"+$("#emaildate").val()+"']").attr("areaid"));
				}
			}
		});
	
}
function editInit(){
	
}
function editSuccess(data){
	//selectCwbByEmail(1,1);
	$("#"+data.cwb+"_excelbranch").html(data.excelbranch);
	//alert($("#"+data.cwb+"_excelbranch").html());Timelimited
	$("#"+data.cwb+"_exceldeliver").html(data.exceldeliver);
	//$("#"+data.cwb+"_exceldeliver").html(data.exceldeliver);
}
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="excelimportPage" >导入数据</a></li>
			<li><a href="#" class="light">导入查询</a></li>
		</ul>
	</div>
	<form name="uploadForm" id="uploadForm" method="POST" action="excelimport;jsessionid=<%=session.getId() %>" enctype="multipart/form-data" >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="650" colspan="2">发货批次：
						<select id="emaildate" name="emaildate" class="select1" style="height:20px;width:300px">
						<option value="">请选择(5天内)(供货商_供货商仓库_结算区域)</option>
						<%for (EmailDate e : emaildatelist) {%>
						<option customerid="<%=e.getCustomerid()%>" warehouseid="<%=e.getWarehouseid() %>" areaid="<%=e.getAreaid() %>"  value="<%=e.getEmaildateid()%>" <%=(e.getEmaildateid()+"").equals(emaildateidParam)?"selected":"" %> 
						><%=e.getEmaildatetime()%><%=e.getState()==1?"（已到货）":"" %>(<%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()  %>)
						</option>
						<%}%>
						</select>*
						<input type="button" onclick="selectCwbByEmail(1,1)" value="获取导入结果" class="input_button1" />
						<input type="hidden" id="Failurespage" value="1" />
						<input type="hidden" id="Successpage" value="1" />
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr id="customertr" class=VwCtr style="display:">
					<td width="350">订单入库库房：
						<select id="branchid" name="branchid" class="select1">
						<%for (Branch branch : branchlist) {%>
						<option value="<%=branch.getBranchid()%>"><%=branch.getBranchname()%></option>
						<%}%>
						</select>
					</td>
					<td width="350">发件供货商：
						 <select name="customerid" id="customerid" class="select1" onchange="changeCustomerid()" >
							<option value="0">请选择</option>
							<%for (Customer customer : customerlist) {%>
							<option value="<%=customer.getCustomerid()%>"><%=customer.getCustomername()%></option>
							<%}%>
						</select>*
					</td>
					<td>发货仓库：
						<select name="warehouseid" id="warehouseidflag" class="select1">
								<option value="0">请选择</option>
						</select>
					</td>
					<td >结算区域：
					<span style="height: 25">
					<select name="serviceareaid" id="serviceareaidflag" class="select1">
							<option value="0">请选择</option>
					</select>
					</span></td>
				</tr>
				<tr>
					
					 <td>EXCEL表格导入：
						
						<label for="fileField"></label>
						<span id="swfupload-control"><input type="text" id="txtFileName" disabled="true" style="border: solid 1px; background-color: #FFFFFF;" /><input type="button" id="button" /></span>*</td>
					<td><input type="button" name="reUpload" id="reUpload" value="重新导入" title="此功能会更新“所有匹配到订单号的”记录，但不会修改订单当前的状态，除非匹配到的订单是“有货无单入库”" class="input_button2" />
					<input type="button" name="stop" id="stop" value="停止导入" disabled="disabled" class="input_button2" />
					<input type="hidden" id="anyid"/>
					</td>
					<td></td>
					<td></td>
				</tr>
			</table>
		</form>
				<div class="table_midbg">
							<ul>
								<li><a id="SuccessListButton" href="#" <%=isSuccess.equals("1")?"class=\"light\"":"" %> >成功(<label id="successCount" style="font-size:18;font-weight: bold;color:red;"><%=Successpage_obj.getTotal() %></label>)</a></li>
								<li><a id="FailureListButton" href="#" <%=!isSuccess.equals("1")?"class=\"light\"":"" %>>失败(<label id="failureCount" style="font-size:18;font-weight: bold;color:red;"><%=Failurespage_obj.getTotal() %></label>)</a></li>
							</ul>
						</div>
						<table id="failureOrder" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2"  <%if(isSuccess.equals("1")){ %>style="display: none"<%} %>>
							<tr class="font_1">
								<td width="30%" align="center" height="38" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td align="center" valign="middle" bgcolor="#eef6ff">错误描述</td>
								<!-- <td width="20%" align="center" valign="middle" bgcolor="#eef6ff">操作</td> -->
							</tr>
							<%for(CwbError ce : FailuresList){ %>
							<tr>
								<td width="30%"  align="center" height="38" ><%=ce.getCwb() %></td>
								<td align="center"  ><%=ce.getMessage() %></td>
								<!-- <td width="20%" align="center" >[<a href="#">删除</a>][<a href="#">修改</a>]</td> -->
							</tr>
							<%} %>
						</table>
						<table id="successOrder" width="100%" border="0" cellspacing="1" cellpadding="0" <%if(!isSuccess.equals("1")){ %>style="display: none"<%} %> class="table_2" >
							<tr class="font_1">
								<td width="10%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
								<td width="8%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
								<td width="20%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
								<td width="5%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">邮编</td>
								<td width="10%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
								<td width="10%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">匹配到站</td>
								<td width="7%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">匹配到小件员</td>
								<td width="5%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">时效</td>
								<td width="8%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
								<td width="8%" align="center" height="20" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
							</tr>
							<%for(CwbOrder co : SuccessList){ %>
							<tr>
								<td width="10%"  align="center" height="19" ><%=co.getCwb() %></td>
								<td width="8%"  align="center" ><%="1".equals(showphoneflag)||co.getConsigneename().length()<1?co.getConsigneename():"■"+(co.getConsigneename().substring(co.getConsigneename().length()-1)) %></td>
								<td width="20%"  align="left" ><%="1".equals(showphoneflag)||co.getConsigneeaddress().length()<4?co.getConsigneeaddress():(co.getConsigneeaddress().substring(0,4)+"■■■■■■") %></td>
								<td width="5%"   align="center" ><%=co.getConsigneepostcode() %></td>
								<td width="10%"  align="center" ><%="1".equals(showphoneflag)||co.getConsigneemobile().length()<4?co.getConsigneemobile():(co.getConsigneemobile().substring(0,4)+"■■■■■■") %></td>
								<td width="10%"  align="center" id="<%=co.getCwb() %>_excelbranch"><%=co.getExcelbranch() %></td>
								<td width="7%"   align="center" id="<%=co.getCwb() %>_exceldeliver"><%=co.getExceldeliver() %></td>
								<td width="5%"   align="center" id="<%=co.getCwb() %>_timelimited"><%=co.getTimelimited() %></td>
								<td width="8%"   align="center" ><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
								<td width="8%"   align="center" ><%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText() %></td>
								<td width="9%" align="center" ><!-- [<a href="#">删除</a>] -->[<a href="javascript:;" id="edit" onclick="getEditBox('<%=request.getContextPath()%>/dataimport/getcwbforeditexcel/<%=co.getCwb() %>')">修改</a>]</td>
							</tr>
							<%} %>
						</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%if(Failurespage_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_Failurespage" <%if(isSuccess.equals("1")){ %>style="display: none"<%} %>>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1"  >
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:selectCwbByEmail(0,1);" >第一页</a>　
						<a href="javascript:selectCwbByEmail(0,<%=Failurespage_obj.getPrevious()<1?1:Failurespage_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:selectCwbByEmail(0,<%=Failurespage_obj.getNext()<1?1:Failurespage_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:selectCwbByEmail(0,<%=Failurespage_obj.getMaxpage()<1?1:Failurespage_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=Failurespage_obj.getMaxpage() %>页　共<%=Failurespage_obj.getTotal() %>条记录 　当前第<select
								id="selectFailuresPg"
								onchange="selectCwbByEmail(0,$(this).val());">
								<%for(int i = 1 ; i <=Failurespage_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				<script type="text/javascript">
				$("#selectFailuresPg").val(<%=request.getAttribute("Failurespage") %>);
				</script>
				<%} %>
				<%if(Successpage_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_Successpage" <%if(!isSuccess.equals("1")){ %>style="display: none"<%} %>>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:selectCwbByEmail(1,0);" >第一页</a>　
						<a href="javascript:selectCwbByEmail(<%=Successpage_obj.getPrevious()<1?1:Successpage_obj.getPrevious() %>,0);">上一页</a>　
						<a href="javascript:selectCwbByEmail(<%=Successpage_obj.getNext()<1?1:Successpage_obj.getNext() %>,0);" >下一页</a>　
						<a href="javascript:selectCwbByEmail(<%=Successpage_obj.getMaxpage()<1?1:Successpage_obj.getMaxpage() %>,0);" >最后一页</a>
						　共<%=Successpage_obj.getMaxpage() %>页　共<%=Successpage_obj.getTotal() %>条记录 　当前第<select
								id="selectSuccessPg"
								onchange="selectCwbByEmail($(this).val(),0);">
								<%for(int i = 1 ; i <=Successpage_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>" ><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				<script type="text/javascript">
				$("#selectSuccessPg").val(<%=request.getAttribute("Successpage") %>);
				
				</script>
				<%} %>
		
</div>
</body>
</html>
