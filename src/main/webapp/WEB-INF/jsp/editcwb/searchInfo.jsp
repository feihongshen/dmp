<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.SearcheditInfo"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    Page page_obj = (Page)request.getAttribute("page_obj");
    List<SearcheditInfo> List = request.getAttribute("slist")==null?new ArrayList<SearcheditInfo>():(List<SearcheditInfo>)request.getAttribute("slist");
    List<User> uList = request.getAttribute("ulist")==null?new ArrayList<User>():(List<User>)request.getAttribute("ulist");
    String starttime=request.getParameter("strtime")==null?DateTimeUtil.getDateBefore(1):request.getParameter("strtime");
    String endtime=request.getParameter("endtime")==null?DateTimeUtil.getNowTime():request.getParameter("endtime");
  	List<Branch> branchs=(List<Branch>)request.getAttribute("branchs");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
</head>
<body>
<script type="text/javascript">
function exportField(){
	if(<%=List != null && List.size()>0 %>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}
	$(function() {
		$("#strtime").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		});
		$("#endtime").datetimepicker({
		    changeMonth: true,
		    changeYear: true,
		    hourGrid: 4,
			minuteGrid: 10,
		    timeFormat: 'hh:mm:ss',
		    dateFormat: 'yy-mm-dd'
		
	});
	});
	
	
	function  check(){
		if($("#strtime").val()==""){
			alert("请选择开始时间");
			return false;
		}
		if($("#endtime").val()==""){
			alert("请选择结束时间");
			return false;
		}
		if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
			alert("开始时间不能大于结束时间");
			return;
		}
		$("#searchForm1").attr("action","<%=request.getContextPath()%>/editcwb/toSearchCwb/1");
		$("#searchForm1").submit();
	}
	
	function editInit(){
		
	}
</script>



<div class="saomiao_box2"> 
	<div>
		<div class="kfsh_tabbtn">
			<ul>
				<li><a href="<%=request.getContextPath()%>/editcwb/editCwbInfo">订单信息修改</a></li>
				<li><a href="#" class="light">订单修改查询</a></li>
			</ul>
		</div>
		<div class="tabbox">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr >
							<td width="10%" height="26" align="left" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="5" class="table_5" >
									<tr>
										<td width="120" align="left" valign="top" bgcolor="#f5f5f5">
										<form action="1" method="post"  id="searchForm1">
										订单信息修改时间：
											
												<input type ="text" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>" class="input_text1" style="height:20px;"/>
										到
										<input type ="text" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>" class="input_text1" style="height:20px;"/>
										&nbsp;
						<input type="button"  id="button" value="查询"  class="input_button2" onclick="check();"/>
						<input type="hidden" value="1" name="isshow" id="isshow">
										<input type="button" name="export" id="button" value="导出"  class="input_button2"  onclick="exportField();"/>
										</form>
										</td>
									</tr>
								</table>
								<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
										<tr class="font_1">
											<td valign="middle"  align="center" width="15%" align="center" bgcolor="#e7f4e3">订单号</td>
											<td valign="middle" align="center"  width="5%"   align="center" bgcolor="#e7f4e3">收件人（修改）</td>
											<td valign="middle"  align="center" width="8%"   bgcolor="#e7f4e3">手机（修改）</td>
											<td valign="middle"  align="left"  bgcolor="#e7f4e3">地址（修改）</td>
											<!-- <td valign="middle"  align="center" width="10%"  bgcolor="#e7f4e3">配送时间（修改）</td> -->
											<td valign="middle"  align="center"   width="10%"  bgcolor="#e7f4e3">配送站点（修改）</td>
											<td valign="middle"  align="center"   width="10%"  bgcolor="#e7f4e3">小件员（修改）</td>
											<td valign="middle"  align="center"   width="10%"  bgcolor="#e7f4e3">电商要求（修改）</td>
											<td valign="middle"  align="center"   width="15%"  bgcolor="#e7f4e3">备注（修改）</td>
											<td valign="middle"  align="center" width="5%" bgcolor="#e7f4e3">修改人</td>
											<td valign="middle"  align="center" width="5%" bgcolor="#e7f4e3">修改时间</td>
											<td valign="middle"  align="center" width="5%" bgcolor="#e7f4e3">操作</td>
									</tr>
									<%for(SearcheditInfo sl:List){
										String user="";
									for(User u: uList){
										if(u.getUserid()==sl.getCrename()){
											user=u.getRealname();
										}
									}
									
									%>
									
									<tr>
									<td   valign="middle"  align="center" ><%=sl.getCwb() %></td>
									<td   valign="middle" align="center"  ><%=sl.getNewconsigneename() %></td>
									<td   valign="middle"  align="center" ><%=sl.getNewconsigneemobile() %></td>
									<td   valign="middle"  align="left"   ><%=sl.getNewconsigneeaddress() %></td>
									<%-- <td   valign="middle"  align="center" ><%=sl.getNewResendtime() %></td> --%>
									<td   valign="middle"  align="center" ><%for(Branch branch:branchs){
										if(sl.getNewbranchid()==branch.getBranchid()){
											
										
									 %>  <%=branch.getBranchname() %>    <%}} %></td>
									<td   valign="middle"  align="center" ><%=sl.getNewexceldeliver() == null ? "" : sl.getNewexceldeliver()%></td>
									<td   valign="middle"  align="center" ><%=sl.getNewcommand()%></td>
									<td   valign="middle"  align="center" ><textarea ><%=sl.getNewremark()%></textarea></td>
									<td   valign="middle"  align="center" ><%=user%></td>
									<td   valign="middle"  align="center" ><%=sl.getCretime()%></td>
									<td   valign="middle"  align="center" ><a href="javascript:edit_button('<%=sl.getCwb()%>');" id="cwbdetail" name="cwbdetail"  > 修改详情 </a> </td>
									</tr>
									<%} %>
										
								</table>
							</td>
						</tr>
					</tbody>
				</table></div></div>
				<br/>
				<br/>
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#searchForm1').attr('action','1');$('#searchForm1').submit();" >第一页</a>　
						<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm1').submit();">上一页</a>　
						<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm1').submit();" >下一页</a>　
						<a href="javascript:$('#searchForm1').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm1').submit();" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#searchForm1').attr('action',$(this).val());$('#searchForm1').submit()">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>"><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
			<%} %>
			</div>
			<form action="<%=request.getContextPath()%>/editcwb/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name=begindate id="begindate" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
	<input type="hidden" name="enddate" id="endtime" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/editcwb/findCwbDetail/" />

</body>
</html>