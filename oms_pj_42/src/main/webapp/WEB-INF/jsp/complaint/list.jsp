<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.domain.Complaint"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Complaint> complaintList = (List<Complaint>)request.getAttribute("complaintList");
  int usershowphoneflag = Integer.parseInt(request.getAttribute("usershowphoneflag").toString());
  Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/explink.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/omsTable.css" type="text/css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/omsTable.js"></script> --%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>

<script>
function check(){
	if($("#strtime").val()>$("#endtime").val()){
		alert("开始时间不能大于结束时间");
		return false;
	}
	else{
		return true;
	}
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
</script>

<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(-1);
	$("#searchForm").submit();
}
function editInit(){
	//无处理
}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
</script>
</HEAD>
<body style="background:#eef9ff" >
   <div class="menucontant">
   
		<form name="form1" id = "searchForm" action ="<%=request.getContextPath()%>/complaint/list/1" method ="post">					
           <table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1" border="1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%">投诉类型     : <select name ="complainttypeid" id="complainttypeid1">
									   <option value ="-1">全部</option>
						               <option value ="<%=ComplaintTypeEnum.Order.getValue()%>"  
						               <%if(Integer.parseInt(request.getParameter("complainttypeid")==null?"-1":request.getParameter("complainttypeid").toString()) == ComplaintTypeEnum.Order.getValue()) { %>selected="selected" <%} %>
						               ><%=ComplaintTypeEnum.Order.getText() %></option>
						               <option value ="<%=ComplaintTypeEnum.Courier.getValue()%>" 
						               <%if(Integer.parseInt(request.getParameter("complainttypeid")==null?"-1":request.getParameter("complainttypeid").toString()) == ComplaintTypeEnum.Courier.getValue()) { %>selected="selected" <%} %>
						               ><%=ComplaintTypeEnum.Courier.getText()%></option>
						               <option value ="<%=ComplaintTypeEnum.Site.getValue()%>" 
						               <%if(Integer.parseInt(request.getParameter("complainttypeid")==null?"-1":request.getParameter("complainttypeid").toString()) == ComplaintTypeEnum.Site.getValue()) { %>selected="selected" <%} %>
						               ><%=ComplaintTypeEnum.Site.getText()%></option>
						              </select>
					审核状态     :<select name ="checkflag" id="checkflag1">
						               <option value ="-1"
						               >全部</option>
						               <option value ="0"
						               <%if(Integer.parseInt(request.getParameter("checkflag")==null?"-1":request.getParameter("checkflag").toString()) == 0) { %>selected="selected" <%} %>
						               >未审核</option>
						               <option value ="1"
						               <%if(Integer.parseInt(request.getParameter("checkflag")==null?"-1":request.getParameter("checkflag").toString()) == 1) { %>selected="selected" <%} %>
						               >已审核</option>
					              </select>
					投诉处理状态      : <select name ="complaintflag" id="complaintflag1">
							               <option value ="-1">全部</option>
							               <option value ="0"
							               <%if(Integer.parseInt(request.getParameter("complaintflag")==null?"-1":request.getParameter("complaintflag").toString()) == 0) { %>selected="selected" <%} %>
						               >未处理</option>
							               <option value ="1"
							               <%if(Integer.parseInt(request.getParameter("complaintflag")==null?"-1":request.getParameter("complaintflag").toString()) == 1) { %>selected="selected" <%} %>
						               >已处理</option>
							            </select>
				     是否属实      : <select name ="issure" id="issure">
							               <option value ="-1">全部</option>
							               <option value ="1"
							               <%if(Integer.parseInt(request.getParameter("issure")==null?"-1":request.getParameter("issure").toString()) == 1) { %>selected="selected" <%} %>
						               >属实</option>
							               <option value ="0"
							               <%if(Integer.parseInt(request.getParameter("issure")==null?"-1":request.getParameter("issure").toString()) == 0) { %>selected="selected" <%} %>
						               >不属实</option>
							            </select>
					投诉人编号:<input type ="text" size="8" name ="complaintcustomerid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintcustomerid")) %>"/>
					投诉人姓名：<input type ="text" size="8" name ="complaintcontactman" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintcontactman")) %>" />
					投诉人手机：<input type ="text" name ="complaintphone" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintphone")) %>" /></td>
			  </tr>
			  <tr>
					<td width="100%">被投诉人编号：<input type ="text" size="8" name ="complaintuserid" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintuserid")) %>"/>
					被投诉人备注（模糊查询）：<input type ="text" name ="complaintuserdesc" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintuserdesc")) %>"/>
					投诉订单号：<input type ="text" name ="complaintcwb" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("complaintcwb")) %>" />
					投诉时间：
						<input type ="text" name ="beginemaildate" id="strtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("beginemaildate")) %>">　到　
						<input type ="text" name= "endemaildate" id="endtime"  value ="<%=StringUtil.nullConvertToEmptyString(request.getParameter("endemaildate")) %>">
					<input value="查询" type="submit" class="input_button2" />
					<input value="新建" id ="add_button" type="button" class="input_button2"/>
					<input value="导出"  type="button" class="input_button2"  onclick="exportField();"/>
					</td>
				</tr>
				<tr>
					<td colspan="6">
					   <table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
							<tr class="font_1">
								<td width="5%"  align="center" valign="middle" bgcolor="#eef6ff">投诉类型 </td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">投诉单号</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">投诉人编号</td>
								<td width="4%" align="center" valign="middle" bgcolor="#eef6ff">投诉人</td>
								<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">投诉人联系电话</td>
								<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">被投诉人编号</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">被投诉人备注</td>
								<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">初次受理人</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">投诉记录时间</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">是否属实</td>
								<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">审核状态</td>
								<td width="7%" align="center" valign="middle" bgcolor="#eef6ff">投诉处理状态</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">投诉内容</td>
								<td width="9%" align="center" valign="middle" bgcolor="#eef6ff">投诉处理结果</td>
								<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
							</tr>
							  <% for(Complaint c : complaintList){ %>
							<tr valign="middle" id="t<%=c.getComplaintid()%>">
										<td align="center" valign="middle" ><% if(c.getComplainttypeid()==ComplaintTypeEnum.Order.getValue()){%> <%=ComplaintTypeEnum.Order.getText()%>
																			<%} else  if(c.getComplainttypeid()==ComplaintTypeEnum.Site.getValue()){%> <%=ComplaintTypeEnum.Site.getText()%> 
																			<%} else{%> <%=ComplaintTypeEnum.Courier.getText()%> <% } %></td>
										<td align="center" valign="middle" ><%=c.getComplaintcwb()%></td>
										<td align="center" valign="middle" ><%=c.getComplaintcustomerid()%> </td>
										<td align="center" valign="middle" ><a href ="javascript:getViewBox(<%=c.getComplaintid()%>)"><%=c.getComplaintcontactman()%></a></td>
										<td align="center" valign="middle" ><%=c.getComplaintphone()%></td>
										<td align="center" valign="middle" ><%=c.getComplaintuserid()%></td>
										<td align="center" valign="middle" ><%=c.getComplaintuserdesc()%></td>
										<td align="center" valign="middle"><%=c.getComplaintcreateuser()%></td>
										<td align="center" valign="middle"><%=c.getComplainttime()%></td>
										<td align="center" valign="middle"><span id="issu<%=c.getComplaintid()%>" <%if(c.getIssure() == 0){ %>style ="color:red;"<%} %>><% if(c.getIssure() == 0){%>不属实<%} else{%>属实<%} %></span></td>
										<td align="center" valign="middle"><span id="p<%=c.getComplaintid()%>"><% if(c.getCheckflag() == 0){%><font color= "red">未审核</font><%} else{%>已审核<%} %></span></td>
										<td align="center" valign="middle"><span id="d<%=c.getComplaintid()%>"><% if(c.getComplaintflag() == 0){%><font color= "red">未处理</font><%} else{%>已处理<%} %></span></td>
										<td align="center" valign="middle"><%=c.getComplaintcontent()%></td>
										<td align="center" valign="middle"><textarea rows="2" cols="24" id ="<%=c.getComplaintid()%>" <% if(c.getComplaintflag() == 1){%>disabled="disabled"<%}%>><%=c.getComplaintresult()%></textarea></td>
										<td align="center" valign="middle" >
										    <input type ="button"  id="b<%=c.getComplaintid()%>" value ="审核" onclick="update('<%=c.getComplaintid()%>')"  <% if(c.getCheckflag() == 1){%>disabled="disabled"<%}%>  >
											<input type ="button"  id="q<%=c.getComplaintid()%>"  value ="处理投诉" onclick="deal('<%=c.getComplaintid()%>',$('#<%=c.getComplaintid()%>').val())" <% if(c.getComplaintflag() == 1){%>disabled="disabled"<%}%> >
											<input type ="button"  id="sur<%=c.getComplaintid()%>" value ="<%if(c.getIssure()==1){ %>撤销属实<%}else{ %>属实<%} %>" onclick="issure1('<%=c.getComplaintid() %>')" >
											<input type ="button"  value ="删除" onclick="del('<%=c.getComplaintid()%>')">
										</td>
									</tr>
							 <%} %> 
							<%if(page_obj.getMaxpage()>1){ %> 
							<tr valign="middle">
								<td colspan="15" align="center" valign="middle">
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/complaint/list/1');$('#searchForm').submit()">第一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/complaint/list/<%=page_obj.getPrevious() %>');$('#searchForm').submit()">上一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/complaint/list/<%=page_obj.getNext() %>');$('#searchForm').submit()" >下一页</a>
								   <a href="javascript:;" onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/complaint/list/<%=page_obj.getMaxpage() %>');$('#searchForm').submit()" >最后一页</a>
								    共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录　第<%=request.getAttribute("page")%>页 
                                </td>
							</tr>
							  <%} %>
					   </table>
					</td>
				</tr>
			</table>
	   </form>	
	   <form action="<%=request.getContextPath()%>/complaint/exportExcle" method="post" id="searchForm2">
	</form>		
   </div> 
  
<script type="text/javascript">
	function exportField(){
		$("#searchForm2").submit();	
	}
	function del(id,parameters){
		if (confirm("确定要删除该投诉记录吗？")	){
		$.ajax({
				url:"<%=request.getContextPath()%>/complaint/del",//后台处理程序
				type:"POST",//数据发送方式
				data:"id="+id,//参数
				dataType:'html',//接受数据格式
				async:false,//同步执行还是异步执行
				success:function(data){					 
					if(<%=request.getAttribute("page")%> == 1 ){
						$("tr[id=t"+id+"]").remove();
						alert("删除成功！");
					}else{
						alert("系统异常，删除失败！");
					}		
				},
				error:function(){
					alert("系统异常，删除失败！");
				}
			});
		}
	}
	function update(id,parameters){
		$.ajax({
				url:"<%=request.getContextPath()%>/complaint/updatetype",//后台处理程序
				type:"POST",//数据发送方式
				data:"id="+id,//参数
				dataType:'html',//接受数据格式
				async:false,//同步执行还是异步执行
				success:function(data){					 
					if(<%=request.getAttribute("page")%> == 1 ){
						$("#p"+id).html("已审核");
						$("#b"+id).attr("disabled","disabled");
						alert("审核成功！");
						
					}else{
						alert("系统异常，保存失败！");
					}		
				},
				error:function(){
					alert("修改保存失败!系统出异常！");
				}
			});
	}
	function deal(id,parameters){
		if(parameters.length == 0){
			alert("请填写处理结果！");
			return false;
		}
		$("#d"+id).html("已处理");
		$.ajax({
				url:"<%=request.getContextPath()%>/complaint/deal",//后台处理程序
				type:"POST",//数据发送方式
				data:"id="+id+"&p="+parameters,//参数
				dataType:'html',//接受数据格式
				async:false,//同步执行还是异步执行
				success:function(data){					 
					if(<%=request.getAttribute("page")%> == 1 ){
						$("#p"+id).html("已审核");
						$("#d"+id).html("已处理");
						$("#b"+id).attr("disabled","disabled");
						$("#q"+id).attr("disabled","disabled");
						$("#"+id).attr("disabled","disabled");
						alert("保存投诉处理成功！");
					}else{
						alert("系统异常，保存失败！");
					}		
				},
				error:function(){
					alert("修改保存失败!系统出异常！");
				}
			});
	}
	function issure1(id){
		$.ajax({
				url:"<%=request.getContextPath()%>/complaint/issure",//后台处理程序
				type:"POST",//数据发送方式
				data:"id="+id,//参数
				dataType:'html',//接受数据格式
				async:false,//同步执行还是异步执行
				success:function(data){					 
					if(<%=request.getAttribute("page")%> == 1 ){
						if($("#issu"+id).html().length<3){
							$("#issu"+id).html("不属实");
							$("#issu"+id).html("不属实").css("color","red");
							$("#sur"+id).val("属实");
						}else if($("#issu"+id).html().length>=3){
							$("#issu"+id).html("属实");
							$("#issu"+id).html("属实").css("color","black");
							$("#sur"+id).val("撤销属实");
						}
					    alert("修改成功！");
					}else{
						alert("系统异常，保存失败！");
					}		
				},
				error:function(){
					alert("修改保存失败!系统出异常！");
				}
			});
	}
</script>
<!-- 创建投诉处理的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/complaint/add" />
<!-- 投诉人信息的ajax地址 -->
<input type="hidden" id="view" value="<%=request.getContextPath()%>/complaint/view/" />
</body>
</HTML>
