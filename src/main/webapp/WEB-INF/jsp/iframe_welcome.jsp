<%@page import="cn.explink.domain.Notify"%>
<%@page import="cn.explink.domain.Menu"%>
<%@page import="cn.explink.util.ServiceUtil"%>
<%@ page language="java" import="java.util.List,java.util.Map" pageEncoding="UTF-8"%>
<%
	Map usermap = (Map) session.getAttribute("usermap");
	String username = usermap.get("username").toString();
    List<Menu> menutopList = (List<Menu>) request.getAttribute("MENUPARENTLIST");
	
	List<Menu> menus2 = (List<Menu>)request.getAttribute("menus2");
	List<Menu> menus3 = (List<Menu>)request.getAttribute("menus3");
	List<Notify> notifies=(List<Notify>)request.getAttribute("notifies");
	Notify topnf=(Notify)request.getAttribute("topnf");
	
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询系统</title>
<link href="css/reset.css" rel="stylesheet" type="text/css" />
<link href="css/index.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<style type="text/css">
	.gonggao_box{ position:absolute; z-index:9; left: 800px; top: 108px; border:1px solid #999999; background:#f4f7f9;overflow:hidden;height:305px;}
	ul.gonggaolist {overflow:hidden; display:block; width:300px; padding:10px 0}
	ul.gonggaolist li{display:block; line-height:32px; padding-left:10px}
	ul.gonggaolist li a{display:block; padding-left:10px; padding-right:10px; text-decoration:none}
	ul.gonggaolist li a.red {font-weight:bold; color:#C00}
	ul.gonggaolist li a.more{text-align:right; color:#06C; background:none}
	ul.gonggaolist li a:hover.more{color:blue}

</style>
<script>
function menuToLine(id){
<% for(Menu menu:menutopList){ %><% for(Menu menu2:menus2){ %><% if(menu2.getParentid()==menu.getId()){ %><% for(Menu menu3:menus3){ %><%if(menu3.getParentid()==menu2.getId()){ %>
	if(id==<%=menu3.getId() %>){
 		$("#now_path",parent.document).html("<%=menu.getName() %> &gt; <%=menu2.getName()%> &gt; <%=menu3.getName()%>");
	}
<%}	} %><%}}%><%}%>
}
</script>
</head>

<body style="background:#eef9ff">
			<div class="menucontant2">
			<div class="welcome_bg">
 				<div class="welcome_box">
					<div style="height:33px; background:url(<%=request.getContextPath() %>/images/repeatx.png) repeat-x 0 -700px; border-bottom:1px solid #999999">
						<div style="padding-left:30px; background:url(<%=request.getContextPath() %>/images/ksdh.png) no-repeat 12px 12px; line-height:33px">快速导航</div>
					</div>
					<ul class="welcomebtn" >
					<% for(Menu menu3:menus3){ %>
					<li>
						<a href="<%=menu3.getUrl()+"?dmpid="+session.getId() %>" onclick='menuToLine(<%=menu3.getId()%>);return true;'>
							<img src="<%=menu3.getImage() %>" width="52" height="52" />
							<%=menu3.getName()%>
						</a>
					</li>
					<%	} %>
					</ul>
				</div>
				<div class="gonggao_box" >
					<div style="height:33px; background:url(<%=request.getContextPath() %>/images/repeatx.png) repeat-x 0 -700px; border-bottom:1px solid #999999">
						<div style="padding-left:30px; background:url(<%=request.getContextPath() %>/images/ksdh.png) no-repeat 12px 12px; line-height:33px">公告
						</div>
					</div>
					<ul class="gonggaolist" >
					<li><a href="<%if(topnf!=null){%><%=request.getContextPath()%>/notify/show/<%=topnf.getId() %><%}else{ %>#<%} %>"  target="WORK_AREA" class="red"><%=topnf==null?"重要通知":topnf.getTitle() %></a></li>
					<%if(notifies!=null&&notifies.size()>0){ long count=0l; for(Notify nf:notifies ){count++ ;%>
						<li><a title="<%=nf.getTitle() %>" href="<%=request.getContextPath()%>/notify/show/<%=nf.getId() %>"  target="WORK_AREA"><%=count %>.&nbsp;&nbsp;<%=nf.getTitle().length()>20?nf.getTitle().substring(0,20)+"...":nf.getTitle() %></a></li>
					<%} }%>
					<%if(notifies!=null&&notifies.size()==5){ %>
					<li><a href="<%=request.getContextPath() %>/notify/list/1"  target="WORK_AREA" class="more">查看更多……</a></li>
					<%} %>
					
					</ul>
				</div>
			</div>
			
			</div>

</body>
</html>
