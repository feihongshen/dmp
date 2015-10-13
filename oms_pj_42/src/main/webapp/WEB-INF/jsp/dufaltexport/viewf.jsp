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
 var initSetExportList = new Array();
<%for(int i =0;i<field.length;i++){%>initSetExportList[<%=i%>]="<%=field[i]%>";<%}%>
</script>

</head>
<body>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>默认模版对应字段详情</h1>
		<form id="setexport_save_Form" name="setexport_save_Form" method="post" action="<%=request.getContextPath()%>/setexportcwb/save/1" onSubmit="submitCreateForm(this);return false;" >
		<div id="box_form">
				<p  class="gysselect">
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
			        <%for(int i= 0;i<exportfieldlist.size();i++){ %> 
			        
			        <%if(i<15){ %>
				        <%if(i==0){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"  value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				        <%if(i==14){ %>
				        </td>
				        <%} %>
			        <%}else if(i>=15 && i<30){%>
			        	<%if(i==15){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"  value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				        <%if(i==29){ %>
				        </td>
				        <%} %>
					<%} else if( i>=30&&i<45){ %>
						<%if(i==30){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"  value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				        <%if(i==44){ %>
				        </td>
				        <%} %>
					<%} else if( i>=45&&i<60){ %>
						<%if(i==45){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"   value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				       <%if(i==59){ %>
				        </td>
				        <%} %>
					<%} else if( i>=60&&i<75){ %>
						<%if(i==60){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"   value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				       <%if(i==74){ %>
				        </td>
				        <%} %>
					<%} else if( i>=75&&i<90){ %>
						<%if(i==75){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  disabled="disabled"  value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				       <%if(i==89){ %>
				        </td>
				        <%} %>
					<%} else if(i>=90){ %>
						<%if(i==90){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>" disabled="disabled"  value ="<%= exportfieldlist.get(i).getId()%>" ><font id ="fc_<%= exportfieldlist.get(i).getId()%>"><%=exportfieldlist.get(i).getFieldname()%></font></p>
				       <%if(i==exportfieldlist.size()-1){ %>
				        </td>
				        <%} %>
				    <%} %> 
					
			       <%}%>
			        
				</tr>
			   </table>
		</div>
		 <div align="center"></div>
	</form>
	</div>
</div>
</body>
</html>