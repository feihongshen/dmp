<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script language="javascript">
	alert("请重新登陆！");
	window.top.location.href = '<%= request.getAttribute("dmpUrl")%>';
</script>