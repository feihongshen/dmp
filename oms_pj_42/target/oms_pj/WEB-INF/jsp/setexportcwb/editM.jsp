<%@page import="cn.explink.domain.Exportmould"%>
<%@page import="cn.explink.domain.Role"%>
<%@page import="cn.explink.domain.SetExportField"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<SetExportField> exportfieldlist = (List<SetExportField>)request.getAttribute("listSetExportField");
Exportmould exportmould = (Exportmould)request.getAttribute("exportmould");
List<Role> roleList = (List<Role>)request.getAttribute("roleList");
String field[] = (String [])request.getAttribute("field");
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
		<h1><div id="close_box" onclick="closeBox()"></div>修改导出模版</h1>
		<form id="setexport_save_Form" name="setexport_save_Form" method="post" action="<%=request.getContextPath()%>/setexportcwb/save/<%=exportmould.getId() %>" onSubmit="if(checkMould()){submitSaveForm(this)};return false;" >
		<div id="box_form">
				<p  class="gysselect">
				 模板名称：<input type ="text" name ="mouldname" id ="mouldname" value ="${exportmould.mouldname}">*
				 请选择角色：<select id="roleid" name="roleid">
								<option value="-1">请选择</option>
								<% for(Role r : roleList){ %>
							     <option value=<%=r.getRoleid()%> <%if(exportmould.getRoleid()==r.getRoleid()){ %> selected="selected" <%}%>><%=r.getRolename() %></option>
							    <%} %>
					        </select>*</p>
				<table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
			       <%for(int i= 0;i<exportfieldlist.size();i++){ %> 
			        <%if(i<14){ %>
				        <%if(i==0){ %>
				        <td>
				        <%}%> 
				        <%if(i==0){ %>
				         <p><input type ="checkbox" id ="checkall" >全选</p>
				        <%} %>  
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				        <%if(i==13){ %>
				        </td>
				        <%} %>
			        <%}else if(i>=14 && i<29){ %>
			        	<%if(i==14){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				        <%if(i==28){ %>
				        </td>
				        <%} %>
					<%} else if( i>=29&&i<44){ %>
						<%if(i==29){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				        <%if(i==43){ %>
				        </td>
				        <%} %>
					<%} else if( i>=44&&i<59){ %>
						<%if(i==44){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				       <%if(i==58){ %>
				        </td>
				        <%} %>
					<%} else if( i>=59&&i<74){ %>
						<%if(i==59){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				       <%if(i==73){ %>
				        </td>
				        <%} %>
					<%} else if( i>=74&&i<89){ %>
						<%if(i==74){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
				       <%if(i==88){ %>
				        </td>
				        <%} %>
					<%} else if(i>=89){ %>
						<%if(i==89){ %>
				        <td>
				        <%} %>
				        <p><input type ="checkbox" name="fieldid" id ="se_<%= exportfieldlist.get(i).getId()%>"  value ="<%= exportfieldlist.get(i).getId()%>" ><%=exportfieldlist.get(i).getFieldname()%></p>
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
<%-- <input type ="checkbox" name="fieldid" value ="<%= s.getId()%>" <%if(s.getExportstate()==1){%> checked="checked"<%} %>><%=s.getFieldname()%><br/> --%>
<!-- <input type ="checkbox" name="fieldname" value ="订单类型">订单类型<br/>
<input type ="checkbox" name="fieldname" value ="发货单号">发货单号<br/>
<input type ="checkbox" name="fieldname" value ="修改时间">修改时间<br/>
<input type ="checkbox" name="fieldname" value ="结算区域">结算区域<br/> -->
</body>
</HTML>