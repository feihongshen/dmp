<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%
		List<CsConsigneeInfo> ccilist=(List<CsConsigneeInfo>)request.getAttribute("ccilist");
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
$(function(){
	$('#edit_but').click(function(){
				
		edit_button($('#callerphoneid').val());
		
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
					if(data.errorCode==0){
						alert(data.error);
						window.location.href='<%=request.getContextPath()%>/workorder/CallerArchivalRepository';
					}
					
					
				}
				
				
				
				
			});
	});
});
</script>
</head>
<body>
		<div>
			<table width="100%">
				<tr>
					<th>姓名:<input type="text"></th>
					<th>电话:<input type="text"></th>
					<th>
						公司:<select>
								<option>请选择公司</option>
							</select>
					</th>
					<th>
						客户:<select>
								<option>请选择客户</option>
							</select>
					</th>
				</tr>
				<tr>
					<td><button>查询</button></td>
					<td><button>重置</button></td>
					<td><button id="add_button">新增</button></td>
					<td><button id="edit_but">修改</button></td>
					<td><button id="remove_button">删除</button></td>
					<td><button>导出</button></td>
				</tr>
			</table>
			<hr>
			<table width="100%" border="1" id="callertb">
				<tr>
					<th>姓名</th>
					<th>性别</th>
					<th>电话1</th>
					<th>电话2</th>
					<th>邮箱</th>
					<th>省份</th>
					<th>城市</th>
					<th>公司</th>
					<th>客户分类</th>
					<th>最后联系时间</th>
					<th>联系次数</th>
				</tr>
				<%if(ccilist!=null){ %>
				<%for(CsConsigneeInfo ccf:ccilist){ %>
				<tr onclick="getPhoneonOne('<%=ccf.getPhoneonOne() %>','<%=ccf.getId()%>')">
					<td><%=ccf.getName() %></td>
					<td><%=ccf.getSex() %></td>
					<td><%=ccf.getPhoneonOne() %></td>
					<td><%=ccf.getPhoneonTwo() %></td>
					<td><%=ccf.getMailBox() %></td>				
					<td><%=ccf.getProvince() %></td>
					<td><%=ccf.getCity() %></td>
					<td><%-- <%= %> --%></td>
					<td><%-- <%= %> --%></td>
					<td><%=ccf.getContactLastTime() %></td>
					<td><%=ccf.getContactNum() %></td>				
				</tr>
				<%} }%>
			</table>
		</div>
<input type="hidden" id="add" value="<%=request.getContextPath()%>/workorder/NewAddMaintain"/>
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/workorder/EditEditMaintain"/>
<input type="hidden" id="callerphone" value=""/>
<input type="hidden" id="callerphoneid" value=""/>
</body>
</html>