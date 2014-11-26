<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Common> comList = (List<Common>)request.getAttribute("commList");
  List<WarehouseToCommen> commonList = (List<WarehouseToCommen>)request.getAttribute("wcommenList");
  Map<String,String> commenMap = (Map<String,String>)request.getAttribute("commenMap");
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>承运商管理(按站点)</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
$("document").ready(function(){  
	$("#btn1").click(function(){  
		if($("#btn1").attr("checked")){
			$("[name='checkboxup']").attr("checked",'true');//全选  
		}else{
		   $("[name='checkboxup']").removeAttr("checked");//取消全选  
		}	
	
	});
	$("#updateF").click(function(){
		$("#controlStr").val("");
	
		var str="["; 
		$("input[name='checkboxup']:checked").each(function(){  
			var id = $(this).val();
			
			str += "{common:\""+id+"\"},";
		}); 
		
		if(str!="["){
			str = str.substring(0, str.length-1)+"]";
			
		}
		$("#controlStr").val(str);
		if($("#controlStr").val()=="["){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		$("#updateForm").submit();
		$("#updateF").attr("disabled","disabled");
		$("#updateF").val("请稍等..");
	});
});
function isok(){
	if(<%=request.getAttribute("ok")!=null %>){
		alert('<%=request.getAttribute("ok")%>');
	}
}
</script>
</head>

<body style="background:#eef9ff" onload="isok();">

<div class="right_box">
	<div class="inputselect_box">
		<form action="<%=request.getContextPath()%>/wharehourecommon/showlist_branch" method="post" id="searchForm">
			承运商：<select name ="commonnumber" id ="commonnumber" >
		          <option value ="">请选择</option>
		          <%if(comList != null && comList.size()>0){ %>
		          <%for(Common c : comList){ %>
		           <option value =<%=c.getCommonnumber() %> 
		           <%if(c.getCommonnumber().equals(request.getParameter("commonnumber")) ){ %>selected="selected" <%} %> ><%=c.getCommonname() %></option>
		          <%} }%>
		        </select>
			<input type="submit"  value="查询" class="input_button2" />
		</form>
	</div>
	<div class="right_title">
	<div class="jg_10"></div><div class="jg_10"></div><div class="jg_10"></div>

	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	    <%if(commonList != null && commonList.size()>0 && commenMap != null ){ %>
	   <tr class="font_1">
	   <td width="30" align="center" valign="middle" ><input type="checkbox"  id="btn1" value="-1"/></td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">承运商</td>
			
			<td width="60%" align="center" valign="middle" bgcolor="#eef6ff">待确认单量</td>
		</tr>
		
		<% for(WarehouseToCommen c : commonList){ %>
		<%if(commenMap != null && commenMap.get(c.getCommencode()) != null){ %>
		<tr>
			<td align="center" valign="middle" > <input type="checkbox" name="checkboxup" value="<%=c.getCommencode() %>"> </td>
			<td width="20%" align="center" valign="middle"><%=commenMap.get(c.getCommencode())%></td>
			<td width="60%" align="center" valign="middle"><a href="<%=request.getContextPath()%>/wharehourecommon/show_branch/<%=c.getCommencode() %>/1"><%=c.getId()%></a></td>
			
		</tr>
		<%}} %>
		
		<tr valign="middle">
			<td colspan="3" align="center" valign="middle">
				<form id="updateForm"
					action="<%=request.getContextPath()%>/wharehourecommon/audit_branch"
					method="post">
					<input type="hidden" id="controlStr" name="controlStr" value="" />
					<input type="hidden" id="isbranchidflag" name="isbranchidflag" value="1" />
					<input type="button" id="updateF" value="确认出库" />
				</form>
			</td>
		</tr>
		<%} else{%>
		<tr class="font_1">
	      <td colspan="3" align="center" ><font color="red">暂无待出库给承运商的数据！</font></td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	
</div>
			
	<div class="jg_10"></div>

	<div class="clear"></div>


</body>
</html>

