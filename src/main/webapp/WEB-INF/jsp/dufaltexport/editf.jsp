<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.Role"%>
<%@page import="cn.explink.domain.SetExportField"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<SetExportField> exportfieldlist = (List<SetExportField>)request.getAttribute("listSetExportField");
int field[] = (int [])request.getAttribute("field");
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$("document").ready(function(){  
	
	$("#columnSetting input[class='downButton']",parent.document).click(function(){
		var p=$(this).parents("p");
		var next=p.next();
		if(next!=null){
			p.insertAfter(next);
		}
	});
	
	$("#columnSetting input[class='upButton']",parent.document).click(function(){
		var p=$(this).parents("p");
		var prev=p.prev();
		if(prev!=null){
			p.insertBefore(prev);
		}
	});
	
	$("#checkall",parent.document).click(function(){  
	  if($("#checkall",parent.document).attr("checked")){
			$("[name='fieldid']",parent.document).attr("checked",'true');//全选  
		}else{
		   $("[name='fieldid']",parent.document).removeAttr("checked");//取消全选  
		}	
	});
 });
</script>
<script type="text/javascript">
 var initSetExportList = new Array();
<%for(int i =0;i<field.length;i++){%>initSetExportList[<%=i%>]="<%=field[i]%>";<%}%>
</script>
</head>
<body>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改默认导出模版</h1>
		<form id="setdefault_export_save_Form" name="setdefault_export_save_Form" method="post" action="<%=request.getContextPath()%>/setdefaultexport/save" onSubmit="submitSaveForm(this);return false" >
		<div id="box_form" style = "width:1200px;">
				<p><input type ="checkbox" id ="checkall" >全选</p>
				<p  class="gysselect">
				<table width="100%" border="0" cellspacing="10" cellpadding="0" id="columnSetting">
				<tr>
			       <%for(int i= 0;i<exportfieldlist.size();i++){ %> 
			        <%if(i<15){ %>
				        <%if(i==0){ %>
				        <td>
				        <%}%> 
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				        <%if(i==14){ %>
				        </td>
				        <%} %>
			        <%}else if(i>=15 && i<30){ %>
			        	<%if(i==15){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				        <%if(i==29){ %>
				        </td>
				        <%} %>
					<%} else if( i>=30&&i<45){ %>
						<%if(i==30){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				        <%if(i==44){ %>
				        </td>
				        <%} %>
					<%} else if( i>=45&&i<60){ %>
						<%if(i==45){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				       <%if(i==59){ %>
				        </td>
				        <%} %>
					<%} else if( i>=60&&i<75){ %>
						<%if(i==60){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				       <%if(i==74){ %>
				        </td>
				        <%} %>
					<%} else if( i>=75&&i<90){ %>
						<%if(i==75){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				       <%if(i==89){ %>
				        </td>
				        <%} %>
					<%} else if(i>=90){ %>
						<%if(i==90){ %>
				        <td>
				        <%} %>
				        <p>
				        	<input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%>
				        	<br/>
				        	<input type="button" class="upButton" value="上移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',-1);"/>
				        	<input type="button" class="downButton" value="下移" onclick="upOrDown(<%= exportfieldlist.get(i).getId()%>,'<%=request.getContextPath()%>',1);"/>
				        </p>
				       <%if(i==exportfieldlist.size()-1){ %>
				        </td>
				        <%} %>
				    <%} %> 

			       <%}%>
	
				</tr>
			   </table>
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" /></div>
	</form>
	</div>
</div>
</body>
</HTML>