<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.TransCwbStateControl"%>
<%@page import="java.util.List"%>
<%@page import="cn.explink.enumutil.TransCwbStateEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <% Integer transcwbstate=(Integer)request.getAttribute("transcwbstate");
    	List<TransCwbStateControl> transList=(List<TransCwbStateControl>)request.getAttribute("transcwbstateList");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运单状态修改</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
	$("#toflowtype").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择可操作的环节' }); 
	<%--  $("#button").click(function(){	
		$.post($("#from").attr("action"),{"toflowtypes":$("#from").serialize()},
				function(data,state){
			alert(data)
			window.location.href='<%=request.getContextPath()%>/transCwbStateControl/list';
		}	
		,"json");	
	});   --%>
});

 function buttonSave(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			location.href='<%=request.getContextPath()%>/transCwbStateControl/list';
		}
	});
} 

</script>
</head>
<body style="background:#f5f5f5">
<div id="box_in_bg">
<h2>运单状态修改</h2>
</div> 
<form id="from" onSubmit="buttonSave(this);return false;"   action="<%=request.getContextPath() %>/transCwbStateControl/save/<%=transcwbstate %>" method="post">
<div id="box_form">
<ul>
<li><span >当前状态：</span><% for(TransCwbStateEnum em:TransCwbStateEnum.values()){ if(em.getValue()==transcwbstate){%><%=em.getText() %><% } }%></li>
<li><span>可操作的环节：</span>
<select id="toflowtype" multiple="multiple" name="toflowtype"  style="height: 30px;width: 500px" >
<% for(FlowOrderTypeEnum fte:FlowOrderTypeEnum.values()){ %> <option value="<%=fte.getValue()%>"  <%for(TransCwbStateControl tsc:transList){ if(tsc.getToflowtype()==fte.getValue()){%>selected="selected"<%}} %> ><%=fte.getText()%></option> <%} %>
</select>*
</li>
</ul>
</div>
<div align="center">
<input type="submit" value="确认" class="button" id="button" />
<input type="button" value="返回" class="button" onclick="location='<%=request.getContextPath()%>/transCwbStateControl/list'">
</div>
</form>
</body>
</html>