<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.AutomaticSet"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AutomaticSet> automaticSet = (List<AutomaticSet>)request.getAttribute("aslist");

String linkname = "";
for(AutomaticSet a : automaticSet){
	if(a.getIsauto()==1){
		linkname += a.getNowlinkname()+",";
	}
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

</head>
<body>
	<form action="<%=request.getContextPath()%>/JmsCenter/editlink" method="post" id="searchForm" >
		<div>
			<ul>
				<li><span>环节自动设置：</span></li>
			</ul>
			<ul class="checkedbox1">
				<%for (AutomaticSet as : automaticSet) {%>
					<li><label>
						<%if(as.getIsauto()==1){ %>
					 	<input type="checkbox" id="isautobutton<%=as.getNowlinkname() %>" name="isautobutton<%=as.getNowlinkname() %>" onclick="getcheck('<%=as.getNowlinkname() %>');" checked="checked" /> 
					 <%}else{ %>
					 	<input type="checkbox" id="isautobutton<%=as.getNowlinkname() %>" name="isautobutton<%=as.getNowlinkname() %>" onclick="getcheck('<%=as.getNowlinkname() %>');"/> 
					 <%}  for(FlowOrderTypeEnum fte : FlowOrderTypeEnum.values()){
					 %>
					 <p>当前环节为<%=fte.getText()%>，自动处理下一环节</p>
					</label></li>
				<%}}%>
			</ul>
			<input id="allParm" name="allParm" value="<%=linkname%>" type="hidden"/>
		</div>
		<div align="center"><input type="submit" value="保存" class="button" /></div>
	</form>
<script type="text/javascript">
var parmArr = $("#allParm").val();
function getcheck(nowlinkname){
	if($("#isautobutton"+nowlinkname).attr("checked")=="checked"){
		parmArr += nowlinkname+",";
	}else{
		if(parmArr.indexOf(nowlinkname)!=-1){
			parmArr = parmArr.replace(nowlinkname+",", "");
		}
	}
	$("#allParm").val(parmArr);
}
</script>
</body>
</html>