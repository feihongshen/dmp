<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%	
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customerlist");
	long emaildateidParam = request.getParameter("emaildate")==null?0:Long.parseLong(request.getParameter("emaildate").toString());
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
	long branchidParam = request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid").toString());
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<%-- <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script> --%>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
function subEdit(form){
	if(form.excelbranch.value.length==0){
		alert("请输入站点");
		return false;
	}
	$(".tishi_box",parent.document).hide();
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
			if(data.errorCode==0){
				form.excelbranch.value=data.excelbranch;
				//form.exceldeliver.value=data.exceldeliver;
				var $inp = $('input:text');
				var nxtIdx = $inp.index(form.excelbranch) + 1;
	             $(":input:text:eq(" + nxtIdx + ")").focus();
			}else{
				form.excelbranch.value="";
			}
		}
	});
}

function searchForm(){
	if($.trim($("#cwbs").val()).length==0&&$("#emaildate").val()==0){
		alert("请选择批次或者输入订单号!");
		return false;
	}else{
		$("#reproducttranscwbForm").submit();
	}
}
$(function(){
	$("#customerid").combobox();
	$("div[class='combo-panel panel-body panel-body-noheader']").css({"height":"198px"});
})
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
		<div class="uc_midbg">
			<ul>
				<li><a href="#" class="light">运单号生成</a></li>
				<!-- <li><a href="addresslibrarymatching">手动匹配</a></li> -->
				<!-- <li><font color="red">地址库已开启</font></li> -->
			</ul>
		</div>
		<form name="reproducttranscwbForm" id="reproducttranscwbForm" method="POST" action="reproducttranscwb">
			<input type="hidden" id="isshow" name="isshow" value="1" />
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">
					订单号：<textarea cols="24" rows="4"  name ="cwb" id="cwbs" ></textarea>
					供货商：
						<select name="customerid" id="customerid" onchange="changeCustomerid()" class="select1" style="height: 20px;width: 280px">
							<option value="0">请选择</option>
							<%for(Customer c : customerlist){ %>
							<option value="<%=c.getCustomerid() %>" <% if((c.getCustomerid()+"").equals(request.getParameter("customerid"))){out.print("selected");} %>><%=c.getCustomername() %></option>
							<%} %>
						</select>
					发货批次：
						<select id="emaildate" name="emaildate" style="height: 20px;width: 280px" class="select1">
							<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option> 
							<%if(!emaildatelist.isEmpty())for(EmailDate e : emaildatelist){ %>
							<option value =<%=e.getEmaildateid() %>><%=e.getEmaildatetime()%><%=e.getState()==1?"（已到货）":"" %> <%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()%></option>
							<%}%>
						</select>
						
						<input type="button" class="input_button2" value="生成运单号" onclick="searchForm();">
						&nbsp;&nbsp;<font color="red"> ${msg}</font>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$("#emaildate").val(<%=emaildateidParam%>);
});


function changeCustomerid(){
	 $.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/emaildate/getEmailDateList",
		data:{customerids:$("#customerid").val()},
		success:function(data){
			var optionstring="";
			var high ="";
			optionstring+="<option value='0'>请选择(供货商_供货商仓库_结算区域)</option>";
			for(var i=0;i<data.length;i++){
				optionstring+="<option value='"+data[i].emaildateid+"'>"+
				data[i].emaildatetime+(data[i].state==1?"（已到货）":"")+" "+
				data[i].customername+"_"+data[i].warehousename+"_"+data[i].areaname
				+"</option>";
			}
			$("#emaildate").html(optionstring);
		}
	});
}
</script>
</body>
</html>
