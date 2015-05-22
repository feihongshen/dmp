<%@page import="cn.explink.controller.UserView"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Role,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	List<UserView> userList = (List<UserView>)request.getAttribute("userList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
	List<Role> roleList = (List<Role>)request.getAttribute("roles");
	Page page_obj = (Page)request.getAttribute("page_obj");
	long branchid=request.getParameter("branchid")==null?-1:Long.parseLong(request.getParameter("branchid")) ;
	long roleid=request.getParameter("roleid")==null?-1:Long.parseLong(request.getParameter("roleid")) ;
	long workstate=request.getParameter("workstate")==null?0:Long.parseLong(request.getParameter("workstate"));
	String sosStr=request.getParameter("sosStr")==null?"":request.getParameter("sosStr");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通讯录</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
function exportContact(){
	var flag=<%=userList==null?0:userList.size()%>;
	if(!flag>0){
		alert("没有数据不能导出");
		return;
	}
	$("#formForExport").submit();
}
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div style="padding:5px 10px;width:100%; background:#e0ecff;">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm" method="post" >
		<input type="text" id="branchoruser"  name="branchoruser"  value="请输入机构名" class="input_text1"  onfocus="if(this.value=='请输入机构名'){this.value=''}" onblur="if(this.value==''){this.value='请输入机构名'}" onkeyup="selectbranchbybranchname('<%=request.getContextPath()%>','branchid',$(this).val());"/>
		
		所属机构：<select name="branchid" id="branchid">
				 	<option value="-1">全部</option>
				 	<%if(branchList!=null&&branchList.size()>0){
				 		for(Branch b:branchList){
				 			%>
				 			<option value="<%=b.getBranchid()%>"  <%if(branchid==b.getBranchid()){%>selected <%} %> ><%=b.getBranchname() %></option>
				 		<%}
				 	} %>
				 	
				 </select>
		角    色：<select name="roleid">
				 	<option value="-1">全部</option>
				 	<%if(roleList!=null&&roleList.size()>0){
				 		for(Role r:roleList){
				 			%>
				 			<option value="<%=r.getRoleid()%>" <%if(roleid==r.getRoleid()){%>selected <%} %>><%=r.getRolename() %></option>
				 		<%}
				 	} %>
				 	
				 </select>	
		工作状态：<select name="workstate">
				<option value="0" <%if(workstate==0) {%>selected <%} %> >全部</option>
				<option value="<%=UserEmployeestatusEnum.GongZuo.getValue() %>" <%if(workstate==UserEmployeestatusEnum.GongZuo.getValue()) {%>selected <%} %>><%=UserEmployeestatusEnum.GongZuo.getText() %></option>
				<option value="<%=UserEmployeestatusEnum.XiuJia.getValue() %>" <%if(workstate==UserEmployeestatusEnum.XiuJia.getValue()) {%>selected <%} %> ><%=UserEmployeestatusEnum.XiuJia.getText() %></option>
				<option value="<%=UserEmployeestatusEnum.LiZhi.getValue() %>" <%if(workstate==UserEmployeestatusEnum.LiZhi.getValue()) {%>selected <%} %>><%=UserEmployeestatusEnum.LiZhi.getText() %></option>
		       </select>				  
				登录名或姓名：<input type="text" id="sosStr"  name="sosStr"  class="input_text1" value='<%=request.getParameter("sosStr")==null?"":request.getParameter("sosStr")%>' />
		<%-- 用户登录名：<input  class="input_text1" value="<%=request.getParameter("username")==null?"":request.getParameter("username") %>" name="username"/>
		姓    名：<input   class="input_text1" value="<%=request.getParameter("realname")==null?"":request.getParameter("realname") %>" name="realname"/>  --%>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查询" class="input_button2" />
		<%if(userList!=null&&userList.size()>0){ %>
		<input type="button" onclick="exportContact()" id="exportuser" value="导出" class="input_button2"/>
		<%} %>
		<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
	</form>
	</div>
	<div class="right_title" style="width:100%"><!--<div class="jg_10"></div> <div class="jg_10"></div> -->
	<div id="box_top_bg"></div>
	 <div style="width:100%">
			<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_2">
						<tr class="font_1">
							<td bgcolor="#F1F1F1">编号</td>
							<td bgcolor="#F1F1F1">姓名</td>
							<td bgcolor="#F1F1F1">所属机构</td>
							<td bgcolor="#F1F1F1">职位</td>
							<td bgcolor="#F1F1F1">手机</td>
							<td bgcolor="#F1F1F1">最后登陆时间</td>
							<td bgcolor="#F1F1F1">最后登陆IP</td>
							<td bgcolor="#F1F1F1">工作状态</td>
						</tr>
					
					
					<% for(UserView u : userList){ %>
						<tr>
							<td><%=u.getUserid() %></td>
							<td><%=u.getRealname() %></td>
							<td><%=u.getBranchname() %></td>
							<td><%=u.getRolename() %></td>
							<td><%=u.getUsermobile() %></td>
							<td><%=u.getLastLoginTime()%></td>
							<td><%=u.getLastLoginIp()%></td>
							<td><%=u.getEmployeestatusName() %></td>
						</tr>
						<%} %>
						
	</table>
</div>
	
	<div class="jg_10"></div>

	</div>
		<%if(workstate==0&&branchid==-1&&roleid==-1&&sosStr.length()==0) {%>
		<strong>工作状态统计 :公司目前工作人员共计<span style="color:red"><%=page_obj.getTotal() %></span>人</strong> 
		&nbsp;&nbsp;&nbsp;&nbsp;
		 在职人员:<a style="color:red"><%=request.getAttribute("count")==null?"0":request.getAttribute("count") %></a>
		 &nbsp;&nbsp;&nbsp;&nbsp;
		 休假人员：<a style="color:red" href="<%=request.getContextPath() %>/contact/list/1?workstate=<%=UserEmployeestatusEnum.XiuJia.getValue() %>"><%=request.getAttribute("holiday")==null?"0":request.getAttribute("holiday") %></a>
		 &nbsp;&nbsp;&nbsp;&nbsp;  
	 	 离职人员：<a  style="color:red" href="<%=request.getContextPath() %>/contact/list/1?workstate=<%=UserEmployeestatusEnum.LiZhi.getValue() %>" ><%=request.getAttribute("offline")==null?"0":request.getAttribute("offline") %></a>
	<%}else{ %>
		<strong>符合查询条件的共计<lable style="color:red"><%=page_obj.getTotal() %></lable>人</strong>
	<%} %>
	<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
			　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
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
			
	<div class="jg_10"></div>
	<div class="clear"></div>
	<form action="<%=request.getContextPath() %>/contact/exportuser" method="post" id="formForExport">
		<input type="hidden" name="workstate" value="<%=request.getParameter("workstate")==null?0:request.getParameter("workstate") %>"/>
		<input type="hidden" name="sosStr" value="<%=request.getParameter("sosStr")==null?"":request.getParameter("sosStr") %>"/>
		<input type="hidden" name="username" value="<%=request.getParameter("branchid")==null?-1:request.getParameter("branchid") %>"/>
		<input type="hidden" name="roleid" value="<%=request.getParameter("roleid")==null?-1:request.getParameter("roleid") %>"/>
	</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 创建订单类型的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/user/add" />
<!-- 修改订单类型的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/user/edit/" />
<!-- 删除订单类型的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/user/del/" />
</body>
</html>