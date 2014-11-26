<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.logdto.*"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.domain.EmailDate"%>

<%
Long dp24,dp36,dp48,dpall; 
dp24 = request.getAttribute("dp24")==null?0L:(Long)request.getAttribute("dp24");
dp36 = request.getAttribute("dp36")==null?0L:(Long)request.getAttribute("dp36");
dp48 = request.getAttribute("dp48")==null?0L:(Long)request.getAttribute("dp48");
dpall = request.getAttribute("dpall")==null?0L:(Long)request.getAttribute("dpall");
String dp24cwbs,dp36cwbs,dp48cwbs,dpallcwbs;
dp24cwbs = request.getAttribute("dp24cwbs")==null?"":(String)request.getAttribute("dp24cwbs");
dp36cwbs = request.getAttribute("dp36cwbs")==null?"":(String)request.getAttribute("dp36cwbs");
dp48cwbs = request.getAttribute("dp48cwbs")==null?"":(String)request.getAttribute("dp48cwbs");
dpallcwbs = request.getAttribute("dpallcwbs")==null?"":(String)request.getAttribute("dpallcwbs");

int allNum =  request.getAttribute("allNum")==null?0:(Integer)request.getAttribute("allNum");

List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
List emaildateidList =(List) request.getAttribute("emaildateidStr");
List<Customer> customerlist = (List<Customer>)request.getAttribute("customerlist");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>按批次查询投递率</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#emaildateid").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择发货批次' });
})
function changeCustomerid(){
	if($("#customerid").val()==0){
		$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val("请选择发货批次");
		if(navigator.userAgent.indexOf("MSIE") >= 0){
			$("#fahuopici").next().next().next().html("");
		}else{
			$("#fahuopici").next().next().html("");
		}
		return;
	}
	
	//保存ajax之前的emaildate选择情况
	var emaildateCheckId = $("input[name='emaildateid']");
	var ids= new Array();
	for(var i=0;i<emaildateCheckId.length; i++){
		if(emaildateCheckId[i].checked){
			ids[i]=emaildateCheckId[i].value;
		}
	}
	
	$.ajax({
		url:"<%=request.getContextPath()%>/emaildate/getTowMonthEmailDateList",//后台处理程序
		type:"POST",//数据发送方式 
		data:{customerids:$("#customerid").val()},//参数
		dataType:'json',//接受数据格式
		success:function(json){
			var list = "";
			for(var i=0; i<json.length; i++){
				list += "<label class=\"\"><input type=\"checkbox\" onClick=\"clickEmaildate(this);\" value=\""+json[i].emaildateid+"\" name=\"emaildateid\">"+json[i].emaildatetime+(json[i].state==1?"（已到货）":"")+" ("+json[i].customername+"_"+json[i].warehousename+"_"+json[i].areaname+")</label>";
			}
			if(navigator.userAgent.indexOf("MSIE") >= 0){
				$("#fahuopici").next().next().next().html(list);
			}else{
				$("#fahuopici").next().next().html(list);
			}
			var txt = "";//用于存储显示批次文本框的文字
			for(var i=0;i<ids.length; i++){
				$("input[name='emaildateid'][value='"+ids[i]+"']").each(function(i){
					$(this).attr("checked","checked");
					var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
					txt = txt+pici+", ";
				});
				$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择发货批次":txt.substring(0,txt.length-2));
			}
			
		}		
	});
}

	
function clickEmaildate(t){
	$(t).parent().attr("class","checked");
	var txt = "";//用于存储显示批次文本框的文字
	$("input[name='emaildateid']").each(function(i){
		if($(this).parent().attr("class")=="checked"){
			var pici = $(this).parent().html().substring($(this).parent().html().indexOf(">")+1);
			txt = txt+pici+", ";
		}
	});
	$("input[name='emaildateid']").parent().parent().prev().children(".multiSelect_txt").val(txt==""?"请选择发货批次":txt.substring(0,txt.length-2));
}

function submitselect(){
	if($(".multiSelectOptions input[name='emaildateid']:checked").size()==0){
		alert("请选择发货批次");
		return false;
	}else{
		$("#searchForm").submit();
	}
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="right_box">
	<div class="inputselect_box" style="top: 0px; ">
			<form action="emaildateDeliveryPercent" method="post" id="searchForm">
		供货商：
			<select name ="customerid" id ="customerid" onchange="changeCustomerid()">
				<option value="0">请选择</option>
	          <%if(customerlist!=null&&customerlist.size()>0){for(Customer c : customerlist){ %>
	           <option value ="<%=c.getCustomerid() %>" 
	            <%if(c.getCustomerid()==(request.getParameter("customerid")==null?0:Long.parseLong(request.getParameter("customerid").toString()))){
		            %>selected="selected"<%}%>><%=c.getCustomername() %></option>
	          <%}} %>
	        </select>
    	<lable id="fahuopici">选择日期：</lable> 
			<select name="emaildateid" id="emaildateid" multiple="multiple" style="width: 400px;">
	        	<%for(EmailDate ed : emaildatelist){ %>
			 	<option value="<%=ed.getEmaildateid() %>" <%
			 		if(!emaildateidList.isEmpty()) //当提交了emaildateid这个参数再回到这个页面时，应该赋予默认值
			            {for(int i=0;i<emaildateidList.size();i++){
			            	if(ed.getEmaildateid()== new Long(emaildateidList.get(i).toString())){
			            		%>selected="selected"<%
			            	 break;
			            	}
			            }
				     }%>
			 	><%=ed.getEmaildatetime() %><%=ed.getState()==1?"（已到货）":"" %>(<%=ed.getCustomername()+"_"+ed.getWarehousename()+"_"+ed.getAreaname()  %>)</option>
			 	<%} %>
	        </select>
    统计起始时间：<select id="flowordertype" name="flowordertype">
    		<option value="<%=FlowOrderTypeEnum.RuKu.getValue() %>" <%=(FlowOrderTypeEnum.RuKu.getValue()+"").equals(request.getParameter("flowordertype"))?"selected":"" %>>首次入库时间</option>
    		<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue() %>" <%=(FlowOrderTypeEnum.ChuKuSaoMiao.getValue()+"").equals(request.getParameter("flowordertype"))?"selected":"" %>>首次出库时间</option>
    		</select>
			<input type="button" onclick="submitselect();" class="input_button2" value="查看">
	  </form>
	</div>
	<div class="right_title">
	<div class="jg_35"></div>
	<div style="width:100%; overflow-x:scroll">
	<%if(allNum>0){ %>
	<table width="80%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<tbody>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="25%" colspan="2" align="center" valign="middle" bgcolor="#eef6ff">24小时投递率</td>
	   		<td width="25%" colspan="2" align="center" valign="middle" bgcolor="#eef6ff">36小时投递率</td>
	   		<td width="25%" colspan="2" align="center" valign="middle" bgcolor="#eef6ff">48小时投递率</td>
	   		<td width="25%" colspan="2" align="center" valign="middle" bgcolor="#eef6ff">48小时以上</td>
	   	</tr>
	   	<tr class="font_1" height="30" style="background-color: rgb(255, 255, 255); ">
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">单数</td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">投递率</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">单数</td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">投递率</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">单数</td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">投递率</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff">单数</td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff">投递率</td>
	   	</tr>
	   
		<tr style="background-color: rgb(249, 252, 253); ">
			<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"><%=dp24 %></td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff"><%=BigDecimal.valueOf(dp24).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allNum),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"><%=dp36 %></td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff"><%=BigDecimal.valueOf(dp36).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allNum),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"><%=dp48 %></td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff"><%=BigDecimal.valueOf(dp48).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allNum),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
	   		<td width="12%" align="center" valign="middle" bgcolor="#eef6ff"><%=dpall %></td>
	   		<td width="13%" align="center" valign="middle" bgcolor="#eef6ff"><%=BigDecimal.valueOf(dpall).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(allNum),2,BigDecimal.ROUND_HALF_UP).doubleValue() %>%</td>
		</tr>
		</tbody>
	</table>
	<%} %>
<div class="jg_35"></div>
</div>	
	</div>

</div>
</body>
</html>