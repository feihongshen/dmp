<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OrderFlowView"%>
<%@page import="cn.explink.controller.QuickSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.orderflow.*"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
List<JSONObject> viewList = request.getAttribute("viewList")==null?null:(List<JSONObject>)request.getAttribute("viewList");

List<Map<String, List<OrderFlowView>>> orderflowviewlist = request.getAttribute("orderflowviewlist")==null?null:(List<Map<String, List<OrderFlowView>>>)request.getAttribute("orderflowviewlist");



String isSearchFlag=request.getAttribute("isSearchFlag")==null?"":request.getAttribute("isSearchFlag").toString();

String remand = request.getAttribute("remand")==null?"":request.getAttribute("remand").toString();
%>
<head>
<title>订单查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"></link>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"></link>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="./dmp_files/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="./dmp_files/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="./dmp_files/swfupload.js"></script>
<script type="text/javascript" src="./dmp_files/jquery.swfupload.js"></script>
<script src="jquery-1.7.1.min(1).js" type="text/javascript"></script>

<style type="text/css"> 
<!--
.bg_3{
    background: url(<%=request.getContextPath()%>/images/bg_3.gif) no-repeat;/*首页运单查询用bg_index.gif,快件查询用bg_3.gif*/
    padding-left: 141px;
    padding-top: 112px;	
}
.srk {
	background:#e6e6e6;
	border:none;
    height: 125px;
    width: 280px;
	padding:5px;
	margin: 0.5em 0;
}
.blank10 {
    clear: both;
    display: block;
    font-size: 1px;
    height: 10px;
    overflow: hidden;
}
.kjtj{background:url(<%=request.getContextPath()%>/images/tj.gif) no-repeat scroll 50%  transparent;border:0 none;}
.kjcz{background:url(<%=request.getContextPath()%>/images/cz.gif) no-repeat scroll 50%  transparent;border:0 none;}
-->
</style>


<script type="text/javascript">

function   submitcwb(){
	var str=$("#kjcx").val();
	$("#li").hide();
	$("#cxframe").show();

			if(trim(str).length==0){
				alert("请输入运单号");
				return false;
			
			}else{
				$("#form2").submit();
			}
	    
	  } 
function trim(str)  
{  
    // 用正则表达式将前后空格  
    // 用空字符串替代。  
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}
</script>
</head>

<body onLoad="$('#cwb').focus();">
<table width="628" border="0" cellspacing="0" cellpadding="0"  align="center" class="table_2" style="height:567px">
	<tr align="center">
		<td width="100%" valign="top">
		<div style="height: auto; font-size:12px; overflow-y:auto; overflow-x:hidden">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" style="font-size:12px">
			<tr>
				<td width="100%" height="26" align="left" valign="middle" bgcolor="#eef6ff">
				 
				
				<div style="background:url(bg_33.gif) repeat-x">
 <!-- <div style="width:900px; height:500px; margin:0 auto"> --> 
<div class="bg_3">
	  <form name="form2" id="form2"  method="post" action="<%=request.getContextPath()%>/order/norderFlowQueryForOutsideByCwb">
		<textarea id="kjcx" class="srk" rows="" name="cwb" ></textarea>
		<div style="width:300px;height:25px; margin-top:30px" class="an" >
		  <input type="button" style="width: 61px;height:25px;" onclick="submitcwb();" class="kjtj">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <input type="button" style="width: 61px;height:25px;" onclick="document.form2.reset()" class="kjcz">
		</div>  
	  </form>
	</div></div><!-- </div>  -->

				 </td>
			</tr> 
			<tr>
				<td width="100%" align="center" valign="top" valign="middle" >
					<%if(viewList != null){ %>
					<%for(JSONObject viewobj : viewList){ %>
					<table width="100%" border="0" cellspacing="1" cellpadding="0"   class="right_set2">
						<tr>
							<td valign="middle" colspan="3">
								<p>订单号：<font color="red"><%=viewobj.getString("cwb") %></font></p>
								<p><font color="red"><%=viewobj.getString("remand") %></font></p>
							</td>
						</tr>
						<tr>
							<td width="20%" align="left"  >操作时间</td>
							<td width="10%" align="center"  >操作人</td>
						    <td width="70%" align="left" >操作过程
						    </td>
                        </tr>
                        <%if(viewobj.getString("remand").equals("")&&orderflowviewlist!=null&&orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"))!=null){ %>
						 <% for(OrderFlowView flow:orderflowviewlist.get(viewList.indexOf(viewobj)).get(viewobj.getString("cwb"))){if(flow.getDetail()!=null){%>
						<tr>
							<td align="left" ><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateDate())%></td>
							<td align="center" ><%=flow.getOperator().getRealname() %></td>
						    <td><%=flow.getDetail().replaceAll("null", "无")%>
						    </td>
                        </tr>	
						<%}}}%>
						<tr>
						<p>----------------------------------------------------------------------------------------------------</p>
						</tr>
					</table>
					<%}}%>
				</td>
			</tr>
		</table>
		</div></td>
	</tr>
</table>
</body>
</html>