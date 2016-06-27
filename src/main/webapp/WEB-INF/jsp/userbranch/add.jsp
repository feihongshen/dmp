<%@page import="cn.explink.domain.User,cn.explink.domain.Branch"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<User> userlist = (List<User>)request.getAttribute("userlist");
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改订单状态设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<link href="<%=request.getContextPath()%>/css/multiple-select.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiple.select.js" type="text/javascript"></script>

<script>
$(function(){
	/* $("#branchid").multiSelect({fasd: true,oneOrMoreSelected: '*',noneSelected:'----请选择站点----'});
	
	$("#userid").multiSelect({oneOrMoreSelected: '*',noneSelected:'----请选择用户----'}); */
	 $("#branchid").multipleSelect({
	        placeholder: "----请选  择站点----",
	        filter: true
	    });
	 $("#userid").multipleSelect({
	        placeholder: "----请选择用户----",
	        filter: true
	    }); 
});

function check(){
	var userid=$(".multiSelectOptions  input[name='userid']:checked ").size();
	var branchid=$(".multiSelectOptions  input[name='branchid']:checked ").size();
	if(!userid>0){
		alert("请选择用户");
		return false;	
	}
	if(!branchid>0){
		alert("请选择站点");
		return false;
	}
		return true;
}
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
			     alert("创建成功!");
			}
		}
	}); 
}

</script>

<div style="background:#f5f5f5">
		<h2><div id="close_box" onclick="location='<%=request.getContextPath()%>/userBranchControl/list/1'"></div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;创建用户区域权限</h2><br />
		<form id="userbranch_cre_Form" name="userbranch_cre_Form"
			 onSubmit="if(check()){check_cwbstatecontrol(this);}return false;" 
			 action="<%=request.getContextPath()%>/userBranchControl/create;jsessionid=<%=session.getId()%>" method="post"  >
					<div style="text-align: center"><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户：<select id="userid" name="userid" multiple="multiple" class="select1" style="height 30px;width: 600px" >
							<%for(User u : userlist){ %>
								<option value="<%=u.getUserid() %>" >(<%for(Branch b : branchlist){if(u.getBranchid()==b.getBranchid()){ out.print(b.getBranchname()); break;}} %>)<%=u.getRealname() %></option>
							<%} %>
						</select>*</span></div><br/>
	           		<div style="text-align: center"><span>可查询站点：</span>
	           			<select id="branchid"  name="branchid" multiple="multiple" class="select1" style="height 30px;width: 600px" >
							<%for(Branch b : branchlist){ %>
								<option value="<%=b.getBranchid() %>" selected="selected"  ><%=b.getBranchname() %></option>
							<%} %>
						</select>*</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/userBranchControl/list/1'" /></div>
	</form>
</div>
</head>
</html>
