<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%@page import="cn.explink.enumutil.*,cn.explink.util.Page"%>
<%
		List<CsConsigneeInfo> ccilist=request.getAttribute("ccilist")==null?null:(List<CsConsigneeInfo>)request.getAttribute("ccilist");
		Page page_obj =request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">


function getPhoneonOne(phone,id){
	$('#callerphone').val(phone);
	$('#callerphoneid').val(id);
}
function addInit(){
	
}

function editInit(){
	
}
$(function(){
	
	
	
	$('#edit_but').click(function(){
		if($('#callerphoneid').val()==""){
			alert('请选择一行数据' );
		}	else{	
		edit_button($('#callerphoneid').val());
		}
	});
	
	
	$("table#callertb tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});
	$('#remove_button').click(function(){
			$.ajax({
				type:'POST',
				data:'phoneone='+$('#callerphone').val(),
				url:'<%=request.getContextPath()%>/workorder/removeCallerArchival',
				dataType:'json',
				success:function(data){
					   $(".tishi_box").html(data.error);
					   $(".tishi_box").show();
					   setTimeout("$(\".tishi_box\").hide(1000)", 2000);
					   if (data.errorCode == 0){
						 <%--   window.location.href="<%=request.getContextPath()%>/workorder/CallerArchivalRepository/1" --%>
						   $('#PageFromW').submit();
					   } 					
				}
			});
			$('#callerphoneid').val('');
	});
});

function deleteidValue(){
	$('#callerphone').val('');
	$('#callerphoneid').val('');
}

</script>
</head>
<body>
	<div>	<%-- <%=request.getContextPath()%>/workorder/QueryCallerInfo/ --%>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" id=PageFromW><%-- action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" --%>
			<table>
				<tr>
					<td>姓名:<input type="text" name="name" id="name"/></td>
					<td>电话:<input type="text" name="phoneonOne" id="phoneonOne"/></td>				
					<td>
						客户:<select name="consigneeType" id="svc" class="select1" >
								<option value="-1">请选择</option>
								<option value="1">普通客户</option>
								<option value="2">VIP客户</option>								
							</select>
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="查询" class="input_button2" onclick="$('#PageFromW').attr('action',1);return true;"/>
						<input type="reset" value="重置" class="input_button2"/>
					</td>
				</tr>	
				</table>				
		</form>
		
			<table>
				<tr>
					<td><button id="add_button" class="input_button2" onclick="deleteidValue()">新增</button></td>
					<td><button id="edit_but" class="input_button2">修改</button></td>
					<td><button id="remove_button" class="input_button2">删除</button></td>
					<td><button class="input_button2" onclick="exportInFoExcle()" id="exinfo">导出</button></td>
				</tr>
			</table>
			<hr>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="callertb">
				<tr class="font_1">
					<th bgcolor="#eef6ff">姓名</th>
					<th bgcolor="#eef6ff">性别</th>
					<th bgcolor="#eef6ff">电话1</th>
					<th bgcolor="#eef6ff">电话2</th>
					<th bgcolor="#eef6ff">邮箱</th>
					<th bgcolor="#eef6ff">省份</th>
					<th bgcolor="#eef6ff">城市</th>
					<th bgcolor="#eef6ff">客户分类</th>
					<th bgcolor="#eef6ff">最后联系时间</th>
					<th bgcolor="#eef6ff">联系次数</th>
				</tr>
				<%if(ccilist!=null){ %>
				<%for(CsConsigneeInfo ccf:ccilist){ %>
				<tr onclick="getPhoneonOne('<%=ccf.getPhoneonOne() %>','<%=ccf.getId()%>')">
					<td><%=ccf.getName() %><%--  ${ccilist[0].name}</td>--%>
					<%if(ccf.getSex()==1){ %>
					<td>男</td>
					<%}else{%>
					<td>女</td>
					<%} %>
					<td><%=ccf.getPhoneonOne() %></td>
					<td><%=ccf.getPhoneonTwo()==null?"": ccf.getPhoneonTwo()%></td>
					<td><%=ccf.getMailBox()==null?"":ccf.getMailBox() %></td>				
					<td><%=ccf.getProvince()==null?"":ccf.getProvince() %></td>
					<td><%=ccf.getCity()==null?"": ccf.getCity()%></td>
					<%if(ccf.getConsigneeType()==2){ %>
					<td><label>VIP客户</label></td>
					<%}else if(ccf.getConsigneeType()==1){ %>
					<td><label>普通客户</label></td>
					<%}else if(ccf.getConsigneeType()==-1){  %>
					<td></td>
					<%} %>
					<td><%=ccf.getContactLastTime()==null?"":ccf.getContactLastTime() %></td>
					<td><%=ccf.getContactNum()%></td>				
				</tr>
				<%} }%>
			</table>
		</div>
<input type="hidden" id="add" value="<%=request.getContextPath()%>/workorder/NewAddMaintain"/>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/workorder/EditEditMaintain/"/>
<input type="hidden" id="callerphone" value=""/>
<input type="hidden" id="callerphoneid" value=""/>

	<form action="<%=request.getContextPath()%>/workorder/exportExcle" id="CallerInfoForm">
		<input type="hidden" name="name" value="<%=request.getParameter("name")==null?"":request.getParameter("name")%>">
		<input type="hidden" name="phoneonOne" value="<%=request.getParameter("phoneonOne")==null?"":request.getParameter("phoneonOne")%>">
		<input type="hidden" name="consigneeType" value="<%=request.getParameter("consigneeType")==null?"-1":request.getParameter("consigneeType")%>">	
	</form>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#PageFromW').attr('action','1');$('#PageFromW').submit();" >第一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#PageFromW').submit();">上一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#PageFromW').submit();" >下一页</a>　
				<a href="javascript:$('#PageFromW').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#PageFromW').submit();" >最后一页</a>
				　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#PageFromW').attr('action',$(this).val());$('#PageFromW').submit()">
						<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
						<option value="<%=i %>"><%=i %></option>
						<% } %>
					</select>页
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
			
		function exportInFoExcle(){
			if(<%=ccilist != null && ccilist.size()>0 %>){
			 	$("#exinfo").val("请稍后……");
			 	$("#CallerInfoForm").submit();
			}else{
				alert("没有做查询操作，不能导出！");
			};
			
		}	
		$("#selectPg").val(<%=request.getAttribute("page") %>);
	</script>
	<div class="tishi_box"></div>
</body>
</html>