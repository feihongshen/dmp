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
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branchs");
	
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	List<CwbOrder> List = request.getAttribute("Order")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("Order");
	//String showphoneflag = session.getAttribute("showphoneflag")==null?"0":(String)session.getAttribute("showphoneflag");
	String error = request.getAttribute("error")==null?"":request.getAttribute("error").toString();
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
function showMsg(){
		if(<%="1".equals(request.getAttribute("msg")) %>){
			alert("无此站点");
		}
}
 function selectForm(){
	 if($("#cwbs").val() == ''){
		 alert("请输入订单号");
		 return false;
	 }
	 var branchid=$("#branchcode").val();
	 var branch=$("#excelbranch").val().replace(/(^\s*)|(\s*$)/g,"");
	 if(branchid.length>0&&branch.length>0){
		 if(branch!='输入站点名称或者站点编号'){
			 alert("下拉框与输入框只能选一个");
			 return false;
		 }
		
	 } 
	 if(branch == '输入站点名称或者站点编号' || $("#excelbranch").val() == '' ){
		 if(!branchid.length>0){
			 alert("请输入站点或者站点编号");
			 return false;
		 }
	 }
	 $("#selectButton").attr("disabled","disabled");
	  $("#selectButton").val("请稍候");
	 $("#editBranchForm").submit();
 }
 $(function(){
		$("#excelbranch").keydown(function(event){
			if(event.keyCode==13) {
				return false;
			}
		});
		
	});
</script>
</head>
<body  style="background:#f5f5f5" onload="showMsg()">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>	
			<li><a href="editBranch">修改匹配站</a></li>
			<li><a href="editBatchBranch" class="light">批量匹配站</a></li>
			<li><a href="editBranchonBranch">匹配站按站</a></li>
			<li><a href="addresslibrarymatching">手动匹配</a></li>
			<!-- <li><a href="batchedit" >批量修改</a></li> -->
		</ul>
	</div>
	<form name="editBranchForm" id="editBranchForm" method="POST" action="editBatchBranch"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">
					订单号：<textarea cols="24" rows="4"  name ="cwbs" id="cwbs" ><%if("1".equals(request.getAttribute("msg"))){ %><%=request.getParameter("cwbs")%><%} %></textarea>
					匹配站点：<%if(branchlist!=null&&branchlist.size()>0){%>
								<select  id="branchcode" name="branchcode">
								<option value="">--请选择站点-- </option>
								<%for( Branch b:branchlist){ %>
									<option value="<%=b.getBranchcode() %>"><%=b.getBranchname() %></option>
								<%}%>
								</select>
															
							<%}%>
						<input type="text" id="excelbranch" name="excelbranch" size="40" 
							onblur="if(this.value==''){this.value='输入站点名称或者站点编号'}"
							onfocus="if(this.value=='输入站点名称或者站点编号'){this.value=''}" value="输入站点名称或者站点编号"  />
							<input type="button"  class='input_button2' value="确认匹配"  onclick="selectForm();" id="selectButton"/>
							<!-- <input style="width: 130px; height:20px; border:none; background-color: #ADEAEA ; cursor:pointer; padding:0; text-align:left;" type="button" id="btnval0" value="导出所有未匹配订单" onclick="exportField();"/><br/> -->
					<%if(error.length()>0 ){ %><br/><font color="red"><%=error %> </font><%} %>
					<%if("1".equals(request.getParameter("isshow")) ){ %>已匹配 <font color="red"><%=request.getAttribute("count") %> </font>单<%} %>	
					</td>
				</tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					站点表：
					<%for(Branch b : branchlist){ out.print("<span  style='background-color:#eef6ff;' >"+b.getBranchname()+"("+b.getBranchcode()+")</span>　"); }%>
					</td>
					
				</tr>
			</table>
			<input type="hidden" id="isshow" name="isshow" value="1" />
		</form>
				<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="10%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件人</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单类型</td>
						<td width="8%" align="center" align="center" valign="middle" bgcolor="#eef6ff">订单状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">邮编</td>
						<td width="29%" align="center" align="center" valign="middle" bgcolor="#eef6ff">收件地址</td>
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
							<%-- <td width="10%"   align="center" height="38" ><input type="text" name="exceldeliver" value="<%=co.getExceldeliver() %>" /></td> --%>
							<!-- <td width="9%" align="center" ><input type="submit"  value="保存" class="input_button2"></td> -->
						</form>
						</tr>
					
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%-- <%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_page" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:selectPage(1);" >第一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="selectPage($(this).val());">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>" ><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				<script type="text/javascript">
				$(function(){
					$("#selectPg").val(<%=request.getAttribute("page") %>);
					$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber") %>);
				});
				</script>
				<%} %> --%>
		
</div>
<form action="<%=request.getContextPath()%>/dataimport/exportExcleNoPIPei" method="post" id="searchForm2">
</form>
<script type="text/javascript">
function exportField(){
	$("#exportmould2").val($("#exportmould").val());
	$("#btnval0").attr("disabled","disabled");
 	$("#btnval0").val("请稍后……");
 	$("#searchForm2").submit();
}

</script>
</body>
</html>
