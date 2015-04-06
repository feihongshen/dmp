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
	long SuccessAddress=(Long )request.getAttribute("SuccessAddress");
	long NotSuccess=(Long )request.getAttribute("NotSuccess");
	long SuccessEdit=(Long )request.getAttribute("SuccessEdit");
	long SuccessNew=(Long )request.getAttribute("SuccessNew");
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	List<CwbOrder> List = request.getAttribute("Order")==null?new ArrayList<CwbOrder>():(List<CwbOrder>)request.getAttribute("Order");
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
		$("#btnval1").attr("disabled","disabled");
		$("#btnval1").val("请稍后……");
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
	if($("#cwb").val().length==0&&$("#emaildate").val()==0){
		alert("请选择批次!");
		return false;
	}else{
		$("#editBranchForm").submit();
	}
}
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="excelimportPage" >导入数据</a></li>
			<li><a href="reexcelimportPage" >导入查询</a></li>
			<!-- <li><a href="batchedit" >批量修改</a></li> -->
			<li><a href="datalose" >数据失效</a></li>
			<li><a href="#" class="light">修改匹配站</a></li>
			<!-- <li><a href="editBranchonBranch" >匹配站按站</a></li> -->
			<li><a href="editBatchBranch" >批量匹配站</a></li>
			<li><a href="reproducttranscwb" >运单号生成</a></li>
			<li><font color="red">地址库已开启</font></li>
		</ul>
	</div>
	<form name="editBranchForm" id="editBranchForm" method="POST" action="editBranch"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">订单号：<input id="cwb" type="text" value="" name="cwb" />
					供货商：<select name="customerid" id="customerid" onchange="changeCustomerid()" style="height: 20px;width: 280px">
							<option value="0">请选择</option>
							<%for(Customer c : customerlist){ %>
							<option value="<%=c.getCustomerid() %>" <% if((c.getCustomerid()+"").equals(request.getParameter("customerid"))){out.print("selected");} %>><%=c.getCustomername() %></option>
							<%} %>
							</select>
					发货批次：
						<select id="emaildate" name="emaildate" style="height: 20px;width: 280px">
							<option value='0' id="option2">请选择(供货商_供货商仓库_结算区域)</option> 
							<%if(!emaildatelist.isEmpty())for(EmailDate e : emaildatelist){ %>
							<option value =<%=e.getEmaildateid() %>><%=e.getEmaildatetime()%><%=e.getState()==1?"（已到货）":"" %> <%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()%></option>
							<%}%>
						</select>
					 每页<select name="onePageNumber" id="onePageNumber">
								<option value="10">10</option>
								<option value="30">30</option>
								<option value="50">50</option>
								<option value="100">100</option>
							</select>行
							<input type="button" class="input_button2" value="查询" onclick="searchForm();">
							<select name ="exportmould" id ="exportmould">
					          <option value ="0">默认导出模板</option>
					          <%for(Exportmould e:exportmouldlist){%>
					           <option value ="<%=e.getMouldfieldids()%>"><%=e.getMouldname() %></option>
					          <%} %>
							</select>
							<input type="button" id="btnval0" class="input_button2" value="导出" onclick="exportField();"/><br/>
					(共<a href="javascript:$('#addressCodeEditType').val(-1);selectPage(1);" style="font-size:18;font-weight: bold;color:red;" >${AllAddress}</a>单
				地址库匹配<a <%if(SuccessAddress==0){ %> href="#" <%} else{%>href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue() %>);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${SuccessAddress}</a>
				修改匹配<a <%if(SuccessEdit==0){ %> href="#" <%} else{%> href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue() %>);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${SuccessEdit}</a>
				人工匹配<a <%if(SuccessNew==0){ %> href="#" <%} else{%> href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.RenGong.getValue() %>);selectPage(1);" <%} %>style="font-size:18;font-weight: bold;color:red;" >${SuccessNew}</a>
				未匹配<a <%if(NotSuccess==0){ %> href="#" <%} else{%>href="javascript:$('#addressCodeEditType').val(<%=CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue() %>);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${NotSuccess}</a>)
				<input type="button" id="btnval1" class="input_button2" value="重新匹配" onclick='resendaddressMsg("<%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%>",<%=emaildateidParam %>,<%=request.getParameter("customerid")==null?0:request.getParameter("customerid")%>,<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>,<%=request.getParameter("onePageNumber")==null?10:request.getParameter("onePageNumber")%>,1,<%=branchidParam%>);'>
					</td>
					<input type="hidden" id="addressCodeEditType" name="addressCodeEditType" value="-1" /><!-- 0为全部 1 为成功 2 为匹配 -->
					<input type="hidden" id="branchid" name="branchid" value="0" />
					
					<input type="hidden" id="page" name="page" value="1" />
					<input type="hidden" id="isshow" name="isshow" value="1" />
				</tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					站点表：
					<%for(Branch b : branchlist){%><a <%if(emaildateidParam>0){ %> href='javascript:$("#addressCodeEditType").val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$("#branchid").val(<%=b.getBranchid() %>);selectPage(1);' <%} %> style='background-color:#bbffaa;' ><%=b.getBranchname() %>(<%=b.getBranchcode()%>)</a> <%}%>
					</td>
					
				</tr>
			</table>
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
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_page" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(1);" >第一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#addressCodeEditType').val(<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>);$('#branchid').val(<%=branchidParam%>);selectPage($(this).val());">
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
<form action="<%=request.getContextPath()%>/dataimport/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name="cwb1" id="cwb1" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%>"/>
	<input type="hidden" name="emaildate1" id="emaildate1" value="<%=emaildateidParam%>"/>
	<input type="hidden" name="customerid1" id="customerid1" value="<%=request.getParameter("customerid")==null?"0":request.getParameter("customerid")%>"/>
	<input type="hidden" name="addressCodeEditType1" id="addressCodeEditType1" value="<%=request.getParameter("addressCodeEditType")==null?-1:request.getParameter("addressCodeEditType")%>"/>
	<input type="hidden" id="branchid1" name="branchid1" value="<%=branchidParam%>" />
	<input type="hidden" name="exportmould2" id="exportmould2" />
</form>
<script type="text/javascript">
$(function(){
	$("#selectPg").val(<%=request.getAttribute("page") %>); 
	$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber") %>);
	$("#emaildate").val(<%=emaildateidParam%>);
	$("#branchid").val(<%=branchidParam%>);
});

function exportField(){
	if($("#isshow").val()=="1"&&<%=List != null && List.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval0").attr("disabled","disabled");
	 	$("#btnval0").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};

}

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
