<%@page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.OrderArriveTime"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<OrderArriveTime> oatList=request.getAttribute("oatList")==null?null:(List<OrderArriveTime>)request.getAttribute("oatList");
	Page page_obj = request.getAttribute("page_obj")==null?null:(Page)request.getAttribute("page_obj");
	String starttime=StringUtil.nullConvertToEmptyString(request.getParameter("starttime"));
	String endtime=StringUtil.nullConvertToEmptyString(request.getParameter("endtime"));
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>到车时间确认</title>
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
	$("#arrivetime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
})

//查询
function search(){
	if($("#starttime").val()>$("#endtime").val()){
		alert("开始时间要小于结束时间");
		return false;
	}
	
	$('#searchForm').attr('action',1);
	$('#searchForm').submit();
}

//到车时间提交
function submitF(){
	if($("#arrivetime").val()==""){
		alert("到车时间不能为空");
		return false;
	}
	
	var params=$('#searchForm').serialize();
	params+="&arrivetime="+$("#arrivetime").val();
	
	if(confirm("确认提交到车时间吗？")){
		$("#submitF").attr("disabled","disabled");
    	$("#submitF").val("请稍候");
    	$.ajax({
    		type: "POST",
    		url:'<%=request.getContextPath()%>/orderarrivetime/save',
    		data:params,
    		dataType : "json",
    		success : function(data) {
    			if(data.errorCode==0){
    				alert(data.error);
    				location.href="<%=request.getContextPath()%>/orderarrivetime/list/1";
    			}else{
    				alert(data.error);
    			}
    		}
    	});
	}
}


function exportField(){
	<%-- if(<%=orderbackList!=null&&!orderbackList.isEmpty()%>){ --%>
		$("#btnval").attr("disabled","disabled"); 
	 	$("#btnval").val("请稍后……");
		$("#exportForm").submit();	
	/* }else{
		alert("没有做查询操作，不能导出！");
	} */
}
</script>
</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
<div class="saomiao_box">
	<div class="inputselect_box" style="top: 0px ">
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			<table width="98%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="26%" rowspan="2" valign="top">订单号：
						<textarea name="cwb" rows="3" class="kfsh_text" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
					</td>
					<td>入库时间：<input type="text" name="starttime" id="starttime" value="<%=starttime%>" readonly="readonly"/>
						至<input type="text" name="endtime" id="endtime" value="<%=endtime%>" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td>设定状态：
						<select name="flag" id="flag">
							<option value='1'>未提交</option>
							<option value='2'>已提交</option>
						</select>
						<input name="button2" type="button" class="input_button2" id="button2" value="查询" onclick="search()"/>
						<%if(oatList!=null&&!oatList.isEmpty()){%>
						<input name="button2" type="button" class="input_button2" id="button3" value="导出" id="btnval" onclick="exportField();"/>
						<%} %>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<%if(oatList!=null&&!oatList.isEmpty()){%>
	<div style="height:85px"></div>
		到车时间：<input type="text" name="arrivetime" id="arrivetime" value="" readonly="readonly"/>
		<input name="button2" type="button" class="input_button2" onclick="submitF()" value="提交" />

	<table width="100%" border="0" cellspacing="10" cellpadding="0">
		<tr>
			<td colspan="2" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" id="gd_table" >
					<tr class="font_1">
						<td align="center" bgcolor="#f4f4f4">供货商</td>
						<td bgcolor="#f4f4f4">订单号</td>
						<td bgcolor="#f4f4f4">发货时间</td>
						<td bgcolor="#f4f4f4">入库时间</td>
						<td bgcolor="#f4f4f4">到车时间</td>
						<td bgcolor="#f4f4f4">件数</td>
						<td bgcolor="#f4f4f4">已入库件数</td>
						<td bgcolor="#f4f4f4">订单类型</td>
						<td bgcolor="#f4f4f4">发货仓库</td>
						<td bgcolor="#f4f4f4">入库库房</td>
					</tr>
					<%for(OrderArriveTime list:oatList){%>
					<tr>
						<td align="center"><%=StringUtil.nullConvertToEmptyString(list.getCustomername())%></td>
						<td><%=list.getCwb()%></td>
						<td><%=list.getOuttime()==null?"":list.getOuttime().substring(0,19)%></td>
						<td><%=list.getIntime()==null?"":list.getIntime().substring(0,19)%></td>
						<td><%=list.getArrivetime()==null?"":list.getArrivetime().substring(0,19)%></td>
						<td><%=list.getSendcarnum()%></td>
						<td><%=list.getScannum()%></td>
						<td><%=list.getCwbordertypename()%></td>
						<td><%=StringUtil.nullConvertToEmptyString(list.getOutbranchname())%></td>
						<td><%=StringUtil.nullConvertToEmptyString(list.getInbranchname())%></td>
					</tr>
					<%}%>
				</table></td>
		</tr>
	</table>
	</div>
	<%}%>
	<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj!=null&&page_obj.getMaxpage()>1){%>
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
	
<script language="javascript">
	$("#selectPg").val(<%=request.getAttribute("page") %>);
	$("#flag").val('<%=request.getParameter("flag")%>');
</script>

<form action="<%=request.getContextPath() %>/orderarrivetime/export" method="post" id="exportForm">
	<textarea style="display:none" name="cwb" id="cwb"><%=request.getParameter("cwb")==null?"":request.getParameter("cwb")%></textarea>
	<input type="hidden" value="<%=request.getParameter("flag")==null?"":request.getParameter("flag")%>" id="flag" name="flag"/>
	<input type="hidden" name="starttime" id="starttime" value="<%=starttime%>"/>
	<input type="hidden" name="endtime" id="endtime" value="<%=endtime%>"/>
</form>
</body>
</html>