<%@page import="cn.explink.vo.TPStranscwb"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<TPStranscwb> transcwbList = request.getAttribute("transcwbList")==null?null:(List<TPStranscwb>)request.getAttribute("transcwbList");
Integer printStatus= request.getParameter("printStatus")==null?-1:Integer.parseInt(request.getParameter("printStatus").toString());
Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>运单号打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>

<script type="text/javascript">
//全选
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}

//查询本地运单号
function search(){
	$("#searchForm").submit();
}

//从tps获取运单号
function getTpsTranscwbFromTps(){
	var num =$("#num").val();
	if(isNaN(num)){
		alert("请输入正确的数字！");
		return false;
	}
	if(num==0 || num<0){
		alert("请输入大于0的数字！");
		return false;
	}
	if(num>10000){
		alert("每次获取数量不能超过10000!");
		return false;
	}
	$("#findId").attr("disabled","disabled");
 	$("#findId").val("请稍后……");
	$("#getForm").submit();
}
//批量打印
function printbatchTranscwb(){
	var transcwbs="";
		var isprint = "";
		$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
			isprint = $(this).val();
			if($.trim(isprint).length!=0){
				transcwbs+=""+isprint+",";
			}
		});
		if(isprint==""){
			alert("请选择要打印的运单号！");
		}else{
			$("#printbatchTranscwbId").attr("disabled","disabled");
		 	$("#printbatchTranscwbId").val("请稍后……");
		 	$("#transcwbs").val(transcwbs.substring(0, transcwbs.length-1));
		 	$("#searchForm2").submit();
		}
}
//单条打印
function printOneTranscwb(transcwb){
 	$("#transcwbs").val(transcwb);
 	$("#searchForm2").submit();
}

</script>
</head>
<body style="background:#f5f5f5">
<div class="right_box">
	<div class="inputselect_box">
	<br/>
		<div id="manyCondations">
		 <form action="<%=request.getContextPath()%>/tpsTranscwbPrint/getTpsTranscwbFromTps" method="post" id="getForm" >
			 <font  style="font-size:18px">获取运单号数量:</font >
			 <input type ="text" name ="num" id="num"  value="0" class="input_text1" />
			 <input type="button" id="findId" value="获取" class="input_button2" onclick="getTpsTranscwbFromTps();"/>
		 </form>
		 <br/>
		 <form action="1" method="post" id="searchForm" >
			    打印状态：
			  <select id="printStatus" name="printStatus" class="select1" style="width:100px">
			    <option value="-1" <%if(printStatus==-1){ %>selected="selected"<%} %>>全部</option>
				<option value="0" <%if(printStatus==0){ %>selected="selected"<%} %>>未打印</option>
				<option value="1" <%if(printStatus==1){ %>selected="selected"<%} %>>已打印</option>
			  </select>
			  &nbsp;&nbsp; 
			    单号：
			  <input type ="text" name ="tpstranscwb" id="tpstranscwb"  value="<%=request.getParameter("tpstranscwb")==null?"":request.getParameter("tpstranscwb")%>" class="input_text1" style="width:138px"/>
			  </div>
			  <br/>
		      <input type="button" id="searchId" value="查询" class="input_button2" onclick="search();"/>
		      <input type="button" id="printbatchTranscwbId" value="批量打印" class="input_button2" onclick="printbatchTranscwb();"/>
		 </form>
	</div>
	<div class="right_title">
	<div style="height:150px;"></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr class="font_1">
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">运单号</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">打印状态</td>
			<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">打印人</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">打印时间</td>
			<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">打印</td>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
		<%if(transcwbList!=null&&transcwbList.size()>0)for(TPStranscwb t: transcwbList){ %>
			<tr>
			 	<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint" type="checkbox" value="<%=t.getTpstranscwb() %>"/></td>
			    <td width="20%" align="center" valign="middle"><%=t.getTpstranscwb()%></td>
				<td width="15%" align="center" valign="middle"><%=t.getPrintStatusTitle() %></td>
				<td width="15%" align="center" valign="middle"><%=t.getPrintUser() %></td>
				<td width="20%" align="center" valign="middle"><%=t.getPrintTime() %></td>
				<td width="20%" align="center" valign="middle"><a style="cursor:pointer;" onclick="printOneTranscwb(<%=t.getTpstranscwb()%>);" >打印</a></td>
			</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj!=null&&page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom" id="hidetable">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
	<tr>
		<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
			<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
			<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
			　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
					id="selectPg"
					onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
					<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
					<option value="<%=i %>"><%=i %></option>
					<% } %>
				</select>页
		</td>
	</tr>
	</table>
	</div>
	<%} %>
</div>			
	<div class="jg_10"></div>
	<div class="clear"></div>
	<form action="<%=request.getContextPath()%>/tpsTranscwbPrint/printTpstranscwbPage" method="post" id="searchForm2">
			<input type="hidden" name="transcwbs" id="transcwbs" value=""/>
	</form>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>
