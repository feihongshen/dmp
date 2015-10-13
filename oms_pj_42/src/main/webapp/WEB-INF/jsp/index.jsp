<%@page import="cn.explink.domain.Menu"%>
<%@ page language="java" import="java.util.List,java.util.ArrayList,java.util.Map" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Map usermap = (Map) session.getAttribute("usermap");
	String username = usermap.get("username").toString();
	List<Menu> menutopList = (List<Menu>) request.getAttribute("MENUPARENTLIST");
	
	List<Menu> menus2 = (List<Menu>)request.getAttribute("menus2");
	List<Menu> menus3 = (List<Menu>)request.getAttribute("menus3");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script> 
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript">
	$(function(){
		$("cwb").mouseover(function(){});
	});

	function querycwb(form) {
		var cwb = trim(form.cwb.value);
		form.action = "common?action=opscwbtoview&checkinbranchflag=1&cwb="
				+ cwb;
		form.submit();

	}

	function queryshipcwb(form) {
		var shipcwb = trim(form.shipcwb.value);
		form.action = "common?action=opsshipcwbtoview&checkinbranchflag=1&shipcwb="
				+ shipcwb;
		form.submit();

	}
	
//底部信息位置
$(function(){
	$(window).keydown(function(event){
		switch(event.keyCode) {
			case (event.keyCode=27):closeBox();break;
		}
	});
	$(window).resize(function(){
		var $scroll_hei=document.documentElement.clientHeight;
		$("#WORK_AREA").height($scroll_hei-160);
		$(".bottom_box").css("top",$scroll_hei-50);
	});
	var $scroll_hei=document.documentElement.clientHeight;
	//alert($scroll_hei);
	$("#WORK_AREA").height($scroll_hei-160);
	$(".bottom_box").css("top",$scroll_hei-50);
	$("#playSearch").keydown(function(event){
		if(event.keyCode==13) {
			$("#WORK_AREA")[0].contentWindow.location.href='<%=request.getContextPath() %>/order/select/1?cwb='+this.value;
			$(this).val("");
		}
	});
});


</script>
</head>
<body style="background:#eef9ff">



<!--添加下级帐户 -->
<div id="alert_box">

</div>
<div class="tishi_box"></div>
<!--添加下级帐户-->





<div class="top_line"></div>

	

<div class="head_box">
	<ul class="index_dh fl">
	<% for(Menu menu:menutopList){ %>
	<li>
	<div class="menu_box">
		<ul>
		<% for(Menu menu2:menus2){ %>
			<% if(menu2.getParentid()==menu.getId()){ %>
				<h3><%=menu2.getName()%></h3>
				<% for(Menu menu3:menus3){ %>
					<%if(menu3.getParentid()==menu2.getId()){ %>
				<p><a href="<%=request.getContextPath()+menu3.getUrl() %>" onclick='$("#now_path").html("<%=menu.getName() %> &gt; <%=menu2.getName()%> &gt; <%=menu3.getName()%>");return true;' target="WORK_AREA"><%=menu3.getName()%></a></p>
				<%}	} %>
			<%}}%>
		</ul>
	</div>
	<a href="#"><img src="<%=menu.getImage()%>" width="27" height="30" /><%=menu.getName() %></a></li>
	<%}%>
	</ul>
	
	
	
	
	<ul class="index_dh2 fr">
		<li><a target="WORK_AREA" href="<%=request.getContextPath() %>/passwordupdate"><img src="<%=request.getContextPath()%>/images/dhbtn9.png"   width="27" height="30" />修改密码</a></li>
		<li><a href="#"><img src="<%=request.getContextPath()%>/images/dhbtn10.png" width="27" height="30" />帮助中心</a></li>
		<li><a href="<%=request.getContextPath() %>/resources/j_spring_security_logout" target="_top"  onClick="return confirm('确定要重新登录吗？');"><img src="<%=request.getContextPath()%>/images/dhbtn8.png" width="27" height="30" />退出</a></li>
	</ul>
	<div class="clear"></div>
</div>

<div class="main_box">
	<div class="address_nav"><span class="fr">快速查询：<input id="playSearch"  name="" type="text" value="输入后按回车查询" class="input_text1" onfocus="if(this.value=='输入后按回车查询'){this.value=''}" onblur="if(this.value==''){this.value='输入后按回车查询'}"/>
	用户：<%=usermap.get("realname") %>&nbsp;&nbsp;部门：<font color="red"><%=usermap.get("branchname")==null?"-":usermap.get("branchname") %></font></span>
	当前位置：<a href="#" id="now_path">系统首页 </a></div>

	<div class="right_box">
		<iframe id="WORK_AREA" name="WORK_AREA" src="iframe_jigou.html" width="100%" height="500px"   frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" allowtransparency="yes" ></iframe>
	</div>
			
	<div class="jg_10"></div>

	
	
	
	<div class="clear"></div>
</div>


<div class="bottom_box">
	物流查询系统v1.0
</div>
</body>
</html>
