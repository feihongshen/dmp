<%@page import="cn.explink.domain.CwbStateControl,cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>二级分拣库站点设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>

<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>

<%@page import="cn.explink.domain.Branch"%>

<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
Map<Long,String> kfMap = (Map<Long,String>)request.getAttribute("kfMap");
Long fromBranchId = (Long)request.getAttribute("fromBranchId");
%>
<script>
$(function(){
	//$("#toBranchId").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择操作下一环节' });
    $("#fromBranchId").multipleSelect({
        placeholder: "----请选择----",
        filter: true,
        single: true
    });
    $("#toBranchId").multipleSelect({
        placeholder: "----请选择----",
        filter: true
    });
})


 function check_branchroute01() {
	
	if ($("#fromBranchId").val() == 0) {
		alert("分拣库不能为空");
		return false;
	}
	
	if ($("#toBranchId").val() == null || $("#toBranchId").val().length<=0) {
		alert("配送站点不能为空");
		return false;
	}

	return true;
} 

 function afterSumit(form){
	
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {   
			alert(data.error); 
			if (data.errorCode == 0) {
				location.href='<%=request.getContextPath()%>/kufangBranchMap/list?';
			}
		}
	});
}  
</script>

</head>
<body>
<div style="background:#f5f5f5">
		<form id="branchroute_cre_Form" name="branchroute_cre_Form"
			onSubmit="if(check_branchroute01()){afterSumit(this);} return false;"
			 action="<%=request.getContextPath()%>/kufangBranchMap/create;jsessionid=<%=session.getId()%>" method="post"  >
			
			<table width="70%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="gd_table">
				<tr class="font_1">
			        <td width="15%"  bgcolor="#eef6ff">
					<span>&nbsp&nbsp&nbsp&nbsp分拣库：</span>
						<select id="fromBranchId" name="fromBranchId">
							<option value="0" selected>----请选择----</option> 
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" <%if(fromBranchId!=null&&fromBranchId.longValue()==b.getBranchid()){ %>selected<%}%>> <%=b.getBranchname() %></option>
							<%} %>
						</select>
					</td>
					<td width="15%"  bgcolor="#eef6ff">
	           		<span>&nbsp&nbsp&nbsp&nbsp配送站点：</span>
	           			<select id="toBranchId"  name="toBranchId"  multiple="multiple" > 
							<%for(Branch b : branchlist){ %>
						<option value="<%=b.getBranchid() %>"  <%if(kfMap!=null&&kfMap.containsKey(new Long(b.getBranchid()))){ %>selected<%}%> ><%=b.getBranchname() %></option>
							<%} %>
						</select>
							</td>

					<td  width="10%" bgcolor="#eef6ff">
			&nbsp&nbsp&nbsp&nbsp<input type="submit" value="提交" class="input_button2" id="sub" /></div>
        	&nbsp&nbsp&nbsp&nbsp<a href='<%=request.getContextPath()%>/kufangBranchMap/list?'>返回</a>
					</td>
					</tr>
			</table>

		<div align="center">
		<input type="hidden" name="edit" value="<%if(fromBranchId!=null&&fromBranchId.longValue()>0){ %>1<%}else {%>0<%}%>" />
        
	</form>
	<div id="div1"></div>
	</div>
</body>

