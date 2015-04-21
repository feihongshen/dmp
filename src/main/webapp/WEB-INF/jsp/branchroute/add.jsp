<%@page import="cn.explink.domain.CwbStateControl,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改订单状态设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Role,cn.explink.enumutil.BranchRouteEnum"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
%>
<script>
$(function(){
	$("#toBranchId").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择操作下一环节' });
})

function afterSumit(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {                                                                                                                                                                                                                                      
			location.href='<%=request.getContextPath()%>/branchRouteControl/list/1?';
		}
	});
}
</script>

</head>
<body>
<div style="background:#f5f5f5">
		<form id="branchroute_cre_Form" name="branchroute_cre_Form"
			 onSubmit="afterSumit(this);return false;" 
			 action="<%=request.getContextPath()%>/branchRouteControl/create;jsessionid=<%=session.getId()%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>当前站点：</span>
						<select id="fromBranchId" name="fromBranchId" class="select1">
							<option value="0" selected>----请选择----</option> 
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>"> <%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
					
	           		<li><span>目的站点：</span>
	           			<select id="toBranchId"  name="toBranchId" class="select1" multiple="multiple">
							<%for(Branch b : branchlist){ %>
						<option value="<%=b.getBranchid() %>" ><%=b.getBranchname() %></option>
							<%} %>
						</select>*
					</li>
					<li><span>流向方向：</span>
	           			<select id="type"  name="type" class="select1">
							<option value="0" selected>----请选择----</option>
							<%for(BranchRouteEnum br : BranchRouteEnum.values()){ %>
								<option value="<%=br.getValue() %>" ><%=br.getText() %></option>
							<%} %>
						</select>*
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" onclick="check_branchroute()"/></div>
	</form>
	</div>
</body>

