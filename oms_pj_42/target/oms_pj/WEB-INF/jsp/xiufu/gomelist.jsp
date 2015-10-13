<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.B2CData"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<%
   String results = request.getAttribute("result")==null?"":request.getAttribute("result").toString();
   List<B2CData> datalist= request.getAttribute("datalist")==null?null:(List<B2CData>)request.getAttribute("datalist");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<title>国美数据手工推送</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
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
		var str=""; 
		$("input[name='checkboxup']:checked").each(function(){  
			var id = $(this).val();
			str += "'"+id+"',";
		}); 
		str = str.length>0?str.substring(0,str.length-1):"";
		$("#controlStr").val(str);
		if($("#controlStr").val()==""){
			alert("没有勾选任何项,不能做此操作!");
			return false;
		}
		$("#updateForm").submit();
	});
}); 

</script>
</head>

<body style="background:#eef9ff">
<div class="right_box">
	<div class="right_title">		
		<form action ="<%=request.getContextPath() %>/gome/clrv" method ="post" id ="batchForm">
		<br>订单号：<br>
		&nbsp;&nbsp;&nbsp;<textarea name="cwb" cols="20" rows="10" ><%=request.getParameter("cwb")==null||"null".equals(request.getParameter("cwb"))?"":request.getParameter("cwb") %></textarea>
		<input type="submit" name="button3" id="btnval2" value="确定" class="button" />
		</form>
	</div>
	<%if(datalist != null && datalist.size()>0){  %>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	   <tr class="font_1">
	   <td height="38" width="30" align="center" valign="middle" bgcolor="#eef6ff" >
	   <input type="checkbox" id="btn1" value="-1"/></td>
				<td  align="center" valign="middle" bgcolor="#eef6ff" >订单号</td>
				<td  align="center" valign="middle" bgcolor="#eef6ff"  >推送结果</td>
		</tr>
		
		<% for(B2CData  c : datalist){ %>
			<tr bgcolor="#FF3300">
			<td align="center" valign="middle">
			<%if(c.getSend_b2c_flag()!=1 && c.getSend_b2c_flag()<3){ %>
									<input type="checkbox" name="checkboxup" value="<%=c.getCwb()%>">
				<%} %>
			</td>
				<td  align="center" valign="middle"><%=c.getCwb() %></td>
				<td  align="center" valign="middle"><%=(c.getSend_b2c_flag()==1 || c.getSend_b2c_flag()>3)?"推送成功":(c.getSend_b2c_flag()==0?"等待推送":c.getRemark())%></td>
			 </tr>
		 <%} %>
		 <tr valign="middle"><td colspan="3" align="center" valign="middle">
									<form id="updateForm"
										action="<%=request.getContextPath()%>/gome/update"
										method="post">
										<input type="hidden" id="controlStr" name="controlStr" value="" />
					                     <input type="button" id="updateF" value="取消拒收推送妥投" />
									</form>
								</td>
							</tr>
	</table>
	<%} %>
</div>
</body>

</HTML>
