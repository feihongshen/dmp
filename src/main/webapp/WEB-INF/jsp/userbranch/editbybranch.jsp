
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.UserBranch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> userlist = (List<User>)request.getAttribute("userlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<UserBranch> userbranchList = (List<UserBranch>)request.getAttribute("userBranchList");
int branchid = Integer.parseInt(request.getAttribute("branchid")==null?"0":request.getAttribute("branchid").toString()) ;
%>
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
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script>
$(function(){
	$("#userid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'----请选择----' });
});
function check_cwbstatecontrol(form){
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {   
			if(data.errorCode == 1){
				alert(data.error);
			}else{
			     alert("修改成功!");
			}
		}
	});
}

</script>

<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="location='<%=request.getContextPath()%>/userBranchControl/listbybranch/1'"></div>修改用户区域权限</h1>
		<form id="userbranch_cre_Form" name="userbranch_cre_Form"
			 onSubmit="check_cwbstatecontrol(this);return false;" 
			 action="<%=request.getContextPath()%>/userBranchControl/savebybranch/<%=branchid%>" method="post"  >
			<div id="box_form">
				<ul>
					<li><span>可查询站点：</span>
						<%for(Branch b : branchlist){ %>
							 <%if(branchid==b.getBranchid()){%><%=b.getBranchname() %>
						<% break;
						}} %>
					</li>
					<li><span>用户：</span>
	           			<select id="userid"  name="userid" multiple="multiple" style="height 100px;width: 600px" >
							<%for(User u : userlist){ %>
								<option value="<%=u.getUserid() %>" 
								<% if(userbranchList != null && userbranchList.size()>0){%>
								<%for(UserBranch userbranch:userbranchList){ 
								    if(userbranch.getUserid()==u.getUserid()){ %>selected<%
									break;
								    }%>
								<% }}%>
								>(<%for(Branch b : branchlist){if(u.getBranchid()==b.getBranchid()){ out.print(b.getBranchname()); break;}} %>)<%=u.getRealname() %></option>
							<%} %>
						</select>*[<a href="javascript:multiSelectAll('userid',1,'请选择');">全选</a>][<a href="javascript:multiSelectAll('userid',0,'请选择');">取消全选</a>]
					</li>
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/userBranchControl/listbybranch/1'" /></div>
	</form>
	</div>
</div>
</head>
</html>

