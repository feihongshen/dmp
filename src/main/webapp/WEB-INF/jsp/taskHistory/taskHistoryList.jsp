<%@page import="cn.explink.controller.UserView"%>
<%@page import="cn.explink.enumutil.UserEmployeestatusEnum"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Role,cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	/* List<UserView> userList = (List<UserView>)request.getAttribute("userList");
	List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
	List<Role> roleList = (List<Role>)request.getAttribute("roles");
	Page page_obj = (Page)request.getAttribute("page_obj");
	long branchid=request.getParameter("branchid")==null?-1:Long.parseLong(request.getParameter("branchid")) ;
	long roleid=request.getParameter("roleid")==null?-1:Long.parseLong(request.getParameter("roleid")) ;
	long workstate=request.getParameter("workstate")==null?0:Long.parseLong(request.getParameter("workstate"));
	String sosStr=request.getParameter("sosStr")==null?"":request.getParameter("sosStr"); */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史版本 Update List</title>
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script> --%>
<script type="text/javascript">

</script>
</head>

<body style="background:#f5f5f5">
</body>
</html>