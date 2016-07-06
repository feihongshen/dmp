<%@page import="java.util.Map"%>
<%@page import="cn.explink.domain.ExcelImportEdit"%>
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
	List<Customer> customerlist = (List<Customer>) request.getAttribute("customers");
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branchs");
	long emaildateidParam = request.getParameter("emaildate")==null?0:Long.parseLong(request.getParameter("emaildate").toString());
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
	List<Exportmould> exportmouldlist = (List<Exportmould>)request.getAttribute("exportmouldlist");
	List<ExcelImportEdit> SuccessAddress=(List<ExcelImportEdit>  )request.getAttribute("SuccessAddress");
	long SuccessCount=(Long )request.getAttribute("SuccessCount");
	long NotSuccess=(Long )request.getAttribute("NotSuccess");
	String type=(String)request.getAttribute("type");
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	List<CwbOrder>  List = request.getAttribute("Order")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("Order");
	long branchidParam = request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid").toString());
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
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

function selectPage(page){
		$("#page").val(page);
		$("#isshow").val("1");
		$("#editBranchForm").submit();
}
function resendaddressMsg(cwb,emaildate,customerid,addressCodeEditType,onePageNumber,isshow,branchid){
	if(emaildate==0){
		alert("请选择批次!");
		return false;
	}else{
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/dataimport/resendAddressmatch",
			data:{
				cwb:cwb,
				emaildate:emaildate,
				customerid:customerid,
				addressCodeEditType:addressCodeEditType,
				onePageNumber:onePageNumber,
				branchid:branchid,
				isshow:isshow
				},
			dataType : "json",
			success:function(data){
				alert(data.error);
			}
		});
	}
}

function searchForm(){
	
		$("#editBranchForm").submit();
}
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="editBranch">修改匹配信息</a></li>
			<li><a href="editBatchBranch">批量匹配</a></li>
			<li><a href="editBranchonBranch" class="light">匹配站按站</a></li>
			<li><a href="addresslibrarymatching">手动匹配</a></li>
			<!-- <li><a href="batchedit" >批量修改</a></li> -->
		</ul>
	</div>
	<form name="editBranchForm" id="editBranchForm" method="POST" action="editBranchonBranch"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">订单号：<input id="cwb" type="text" value="" name="cwb" />
					 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				每页<select name="onePageNumber" id="onePageNumber" onchange="searchForm();">
								<option value="10">10</option>
								<option value="30">30</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>行
							<input type="button" class="input_button2" value="查询" onclick="searchForm();">
							
							
				
				未匹配<a <%if(NotSuccess==0){ %> href="#" <%} else{%>href="javascript:$('#zlpop').val(1);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${NotSuccess}</a>
				已匹配<a <%if(SuccessCount==0){ %> href="#" <%} else{%>href="javascript:$('#zlpop').val(2);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${SuccessCount}</a>
				<%if(("1").equals(type)){%>
				<input type="button" id="btnval1" class="input_button1" value="导出未匹配" onclick="exportExcleNoPIPei();" />
				<%} %>
				<input type="hidden" id="zlpop" name="zlpop" value="<%=request.getParameter("zlpop")==null?0:request.getParameter("zlpop")%>" /><!-- 0为全部 1 为成功 2 为匹配 -->
					<input type="hidden" id="addressCodeEditType" name="addressCodeEditType" value="<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>" /><!-- 0为全部 1 为成功 2 为匹配 -->
					<input type="hidden" id="branchid" name="branchid" value="0" />
					
					<input type="hidden" id="page" name="page" value="1" />
					<input type="hidden" id="isshow" name="isshow" value="1" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td></tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					站点表：				
						<%for(Branch b : branchlist){%><a <%if(emaildateidParam>0){ %> href='javascript:$("#addressCodeEditType").val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$("#branchid").val(<%=b.getBranchid() %>);selectPage(1);' <%} %> style='background-color:#bbffaa;' ><%=b.getBranchname() %>(<%=b.getBranchcode()%>)</a> <%}%>
					</td>
					
				</tr>
			</table></form>
			
		<%if(("1").equals(type)&&List.size()>0){ %>
				<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="10%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="3%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">邮编</td>
						<td width="33%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">匹配到站（回车保存）</td>
						<!-- <td width="10%" align="center" height="38" align="center" valign="middle" bgcolor="#eef6ff">匹配到小件员</td> -->
						
<!-- 						<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
 -->					</tr>
					<%for(CwbOrder co : List){ %>
					
						<tr>
						<form id="f<%=co.getCwb() %>" method="POST" onSubmit="subEdit(this);return false;" action="editexcel/<%=co.getCwb() %>" >
							<td width="10%"  align="center" height="19" ><%=co.getCwb() %></td>
							<td width="10%"  align="center"  ><%=co.getConsigneename() %></td>
							
							<td width="10%"  align="center"  ><%=co.getConsigneemobile() %></td>
							<td width="8%"   align="center"  ><%=CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText() %></td>
							<td width="8%"   align="center"  ><%=CwbFlowOrderTypeEnum.getText(co.getFlowordertype()).getText() %></td>
							<td width="5%"   align="center"  ><%=co.getConsigneepostcode() %></td>
							<td width="29%"  align="left" id="add<%=co.getCwb() %>" ><%=co.getConsigneeaddress() %></td>
							<td width="10%"  align="center"  ><input type="text" name="excelbranch" value="<%=co.getExcelbranch() %>" onfocus="$('#add<%=co.getCwb() %>').css('background','#bbffaa');" onblur="$('#add<%=co.getCwb() %>').css('background','#ffffff');" /></td>
						</form>
						</tr>
						<%} %>
					</table>
					<%} %>
					<%if(("2").equals(type)&&SuccessAddress!=null){ %>
					<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="10%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">站点名称</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">已匹配数量</td>
						<td width="3%" align="center" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
				</tr>
					<%		int m=0;
					for(Branch b:branchlist){
						for(ExcelImportEdit s:SuccessAddress){
						if(b.getBranchid()==s.getExcelbranchid()){
							m++;
					%>
						<tr>
							<td width="10%"  align="center" height="19" ><%=m%></td>
							<td width="10%"  align="center"  ><%=b.getBranchname()%></td>
							<td width="10%"  align="center"  style="font-size:18;font-weight: bold;color:red;" ><%=s.getId() %>
							</td>
							<td width="8%"   align="center"  id="shownum"><input type="button" id="btnval<%=b.getBranchid() %>" class="input_button2" value="导出" onclick="exportField(<%=b.getBranchid() %>);" />
							</td>
						</tr>
					<%} %>
						
						<%}}%>
				</table>
					<%}%>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%if(page_obj.getMaxpage()>1&&!type.equals("0")){ %>
				<div class="iframe_bottom" id="view_page" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(1);" >第一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?4:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage($(this).val());">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>" ><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				
				<%} %>
		
</div>
<form action="<%=request.getContextPath()%>/dataimport/exportExcleInfo" method="post" id="searchForm2">
	<input type="hidden" name="addressCodeEditType1" id="addressCodeEditType1" value="<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>"/>
	<input type="hidden" name="exportmould2" id="exportmould2" value="0"/>
	<input type="hidden" name="branchid1" id="branchid1"  value="#branchid1"/>
</form>
<script type="text/javascript">
$(function(){
	$("#selectPg").val(<%=request.getAttribute("page") %>); 
	$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber") %>);
	$("#emaildate").val(<%=emaildateidParam%>);
	$("#branchid").val(<%=branchidParam%>);
});

function exportField(a){
	if($("#isshow").val()=="1"&&<%=List != null && List.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#branchid1").val(a);
		$("#btnval"+a).attr("disabled","disabled");
	 	$("#btnval"+a).val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};

}
function exportExcleNoPIPei(){
	$("#searchForm2").attr("action","<%=request.getContextPath()%>/dataimport/exportExcleToNoPIPei");
	$("#btnval1").attr("disabled","disabled");
	$("#btnval1").val("请稍后……");
	$("#searchForm2").submit();
}
</script>
</body>
</html>
