<%@page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<CwbOrderView> weipaiList=request.getAttribute("weipaiList")==null?null:(List<CwbOrderView>)request.getAttribute("weipaiList");
	List<JSONObject> objList =request.getAttribute("objList")==null?null:(List<JSONObject>)request.getAttribute("objList");
	String starttime=StringUtil.nullConvertToEmptyString(request.getParameter("starttime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getParameter("endtime"));
	String flag=request.getAttribute("flag")==null?"":(String)request.getAttribute("flag");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>委派撤销</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript">
$(function(){
	var $menuli = $(".kfsh_tabbtn ul li");
	var $menulilink = $(".kfsh_tabbtn ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
	$("#starttime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#endtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	if("<%=flag%>"==1){
		$("#li1").attr("class","light");
		$("#content2").attr("style","display:none");
		$("#content3").attr("style","display:none");
	}else{
		$("#li2").attr("class","light");
		$("#content1").attr("style","display:none");
		$("#content3").attr("style","display:none");
	}
})

function sub(){
	if($.trim($("#cwbs").val()).length==0){
		$("#msg").html("撤销前请输入订单号，多个订单用回车分割");
		return false;
	}
	$("#subButton").val("正在处理！请稍候...");
	$("#subButton").attr('disabled','disabled');
	$("#subForm").submit();
}


function search(){
	if($("#starttime").val()>$("#endtime").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	$('#searchForm').attr('action',1);
	$("#searchForm").submit();
}
</script>
</head>
<body style="background:#eef9ff" marginwidth="0" marginheight="0">
<div class="saomiao_box2">
  <div>
    <div class="kfsh_tabbtn">
      <ul>
        <li><a href="#" id="li1">委派撤销</a></li>
        <li><a href="#" id="li2">委派撤销查询</a></li>
      </ul>
    </div>
    <div class="tabbox">
      <li  id="content1">
      	<form id="subForm" action="<%=request.getContextPath() %>/orderdeliveryclient/delete/1" method="post">
		    <table width="100%" border="0" cellspacing="10" cellpadding="0">
				<tr>
					<td valign="top">订单号：
						<textarea name="cwbs" cols="45" rows="3" id="cwbs"></textarea>
						&nbsp;
						<input type="hidden" id="flag" name="flag" value="1"/>
						<input type="button" id="subButton" value="确定撤销" onclick="sub()" class="input_button1" />
					</td>
				</tr>
			</table>
		</form>
		<div class="saomiao_right2">
			<p id="msg" name="msg">${msg}</p>
		</div>
		<%if(objList!=null&&!objList.isEmpty()){%>
		<table width="100%" border="0" cellspacing="0" cellpadding="2" class="table_5" >
				<tr>
					<td width="120" align="center" bgcolor="#f1f1f1">订单号aaa</td>
					<td width="100" align="center" bgcolor="#f1f1f1">供货商</td>
					<td width="140" align="center" bgcolor="#f1f1f1">发货时间</td>
					<td width="100" align="center" bgcolor="#f1f1f1">收件人</td>
					<td width="100" align="center" bgcolor="#f1f1f1">代收金额</td>
					<td width="120" align="center" bgcolor="#f1f1f1">备注</td>
					<td width="450" align="center" bgcolor="#f1f1f1">地址</td>
					<td align="center" bgcolor="#f1f1f1">异常信息</td>
				</tr>
				<%for(JSONObject obj : objList){
						if(obj.get("errorcode").equals("000000")){
							JSONObject cwbOrder =obj.get("cwbOrder")==null?null:JSONObject.fromObject(obj.get("cwbOrder"));
				%>
				<tr>
					<td width="120" align="center"><%=cwbOrder.getString("cwb")%></td>
					<td width="100" align="center"><%=cwbOrder.getString("cwb")%></td>
					<td width="140" align="center"><%=cwbOrder.getString("emaildate")%></td>
					<td width="100" align="center"><%=cwbOrder.getString("consigneename")%></td>
					<td width="100"><%=cwbOrder.getString("receivablefee")%></td>
					<td width="120"><%=cwbOrder.getString("cwbremark")%></td>
					<td width="450"><%=cwbOrder.getString("consigneeaddress")%></td>
				<%}else{%>
				<tr>
					<td width="120" align="center"><%=obj.get("cwb")%></td>
					<td width="100" align="center"></td>
					<td width="140" align="center"></td>
					<td width="100" align="center"></td>
					<td width="100"></td>
					<td width="120"></td>
					<td width="450"></td>
				<%}%>
					<td><%=obj.get("error") %></td>
				</tr>
				<%}%>
			</table>
			<%}%>
	  </li>
	  
	  
      <li id="content2">
      <form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
      <table width="100%" border="0" cellspacing="10" cellpadding="0">
		<tr>
			<td>订单号：
				<textarea name="chexiaocwbs" id="chexiaocwbs" rows="4" style="width:220px"><%=request.getParameter("chexiaocwbs")==null?"":request.getParameter("chexiaocwbs")%></textarea>
				&nbsp;
				撤销时间：
				<input type="text" name="starttime" id="starttime" value="<%=starttime%>"/>
				至
				<input type="text" name="endtime" id="endtime" value="<%=endtime%>"/>
				<input type="hidden" id="flag" name="flag" value="2"/>
				<input type="button" class="input_button1" onclick="search()" value="确 认" />
			</td>
		</tr>
	</table>
	</form>
		<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
												<tr>
													<td align="center">订单号</td>
													<td align="center">小件员</td>
													<td>委派员</td>
													<td>撤销时间</td>
													<td>操作人</td>
												</tr>
												<%if(weipaiList!=null&&!weipaiList.isEmpty()){
													for(CwbOrderView list:weipaiList){
												%>
												<tr>
													<td align="center"><%=list.getCwb()%></td>
													<td align="center"><%=list.getDelivername()%></td>
													<td><%=list.getFdelivername()%></td>
													<td><%=list.getEdittime()%></td>
													<td><%=list.getEditman()%></td>
												</tr>
												<%}}%>
											</table>
	<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){%>
	<div class="iframe_bottom">
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
		</li>
		
		
    </div>
    

    
    
  </div>
</div>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>