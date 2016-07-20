<%@page import="cn.explink.domain.AccountArea"%>
<%@page import="cn.explink.domain.CustomWareHouse"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
    String emaildateidParam = request.getParameter("emaildate")==null?"":request.getParameter("emaildate");
    List<CustomWareHouse> customwarehouselist = (List<CustomWareHouse>) request.getAttribute("customwarehouselist");
    List<AccountArea> accountarealist = (List<AccountArea>) request.getAttribute("accountarealist");
%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" typ	e="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script>
	$(function() {
		$("#editemaildate").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		});
	function checkSel(){
		if($("#emaildate").val().length==0){
			alert("未选择发货批次!");
			return false;
		}if($("#customerwarehouseid").val()=="0" && $("#serviceareaid").val()=="0" && $("#editemaildate").val().length == 0){
			alert("没有更改内容!");
			return false;
		}
		return true;
	}
	$(function(){
		$("#emaildate").change(function(){
			if($(this).val()==0){
				return;
			}
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/dataimport/batcheditselect",
				    data:"emaildate="+$(this).val(),
					success : function(data) {
						var optionstring = "";
						optionstring+="<option value='0'>请选择</option>";
						for ( var i = 0; i < data.length; i++) {
							optionstring += "<option value='"+data[i].warehouseid+"'>"
									+ data[i].customerwarehouse
									+ "</option>";
						}
						$("#customerwarehouseid").html(optionstring);
					}
				});
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/dataimport/batcheditselect1",
				    data:"emaildate="+$(this).val(),	
					success : function(data) {
						var optionstring1= "";
						optionstring1+="<option value='0'>请选择</option>";
						for ( var i = 0; i < data.length; i++) {
							optionstring1 += "<option value='"+data[i].areaid+"'>"
									+ data[i].areaname
									+ "</option>";
						}
						$("#serviceareaid").html(optionstring1);
					}
				});
		});
		$("#sub").click(function(){
		  if(checkSel()){
			$.ajax({
				type: "POST",
				url:"<%=request.getContextPath()%>/dataimport/savebatchedit",
				    data:"emaildate="+$("#emaildate").val()+"&customerwarehouseid="+$("#customerwarehouseid").val()+"&serviceareaid="+$("#serviceareaid").val()+"&editemaildate="+$("#editemaildate").val(),	
					success : function(data) {
						$("#remand").html(data);
						$("#remand").show();
						setTimeout("$(\"#remand\").hide(1000)", 2000);
					}
				});
		  }
		});
		
	});
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="editBranch">修改匹配信息</a></li>
			<li><a href="editBatchBranch">批量匹配</a></li>
			<li><a href="editBranchonBranch">匹配站按站</a></li>
			<li><a href="addresslibrarymatching">手动匹配</a></li>
			<!-- <li><a href="batchedit" class="light">批量修改</a></li> -->
		</ul>
	</div>
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			  <tr id="customertr" class=VwCtr >
			    <td width="400" colspan="3">发货批次
					<select id="emaildate" name="emaildate" >	
						<option value="">请选择(5天内)(供货商_供货商仓库_结算区域)</option>
						<%for (EmailDate e : emaildatelist) {%>
						<option value="<%=e.getEmaildateid()%>">
						<%=e.getEmaildatetime()%><%=e.getState()==1?"（已到货）":"" %>(<%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()  %>)
						</option>
						<%}%>
					</select>*
				 </td>
			  </tr>
			  <tr id="customertr" class=VwCtr >
			    <td width="30%">
			          【要修改的】*　　发货仓库：<select name="customerwarehouseid" id="customerwarehouseid" >
								<option value="0">请选择</option>
						     </select>
				 </td>
				 <td width="30%">
			                  　结算区域：<select name="serviceareaid" id="serviceareaid" >
							    <option value="0">请选择</option>
					          </select>
				 </td>
				 <td width="30%">
			                  　发货批次：<input type="text" id="editemaildate" name="editemaildate"/>
				 </td>
			  </tr>
			  <tr id="customertr" class=VwCtr>
			     <td width="400" colspan ="3" height="50px;"  align="center">
			       <input type="button" id ="sub"  value="保存修改"  class="input_button2" /><br/>
			       <font id ="remand" color="red" style="display: none"></font>
			     </td>
			  </tr>
			</table>
</body>
</html>
