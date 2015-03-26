<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.enumutil.PunishtimeEnum"%>
<%@page import="cn.explink.enumutil.PunishlevelEnum"%>
<%@page import="cn.explink.domain.Punish"%>
<%@page import="cn.explink.domain.PunishType"%>
<%@page import="cn.explink.domain.User"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> branchlist = (List<Branch>)request.getAttribute("branchlist");
List<User> userList = (List<User>)request.getAttribute("userList");
List<User> userAll = (List<User>)request.getAttribute("userAll");
List<PunishType> punishTypeList = (List<PunishType>)request.getAttribute("punishTypeList");
List<Punish> punishList = request.getAttribute("punishList")==null?new ArrayList<Punish>():(List<Punish>)request.getAttribute("punishList");
List<Customer> customerList = request.getAttribute("customerList")==null?new ArrayList<Customer>():(List<Customer>)request.getAttribute("customerList");
Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
String cwb = request.getAttribute("cwb")==null?"":request.getAttribute("cwb").toString();
long userid=Long.parseLong(request.getAttribute("userid")==null?"0":request.getAttribute("userid").toString());
long branchid=Long.parseLong(request.getAttribute("branchid")==null?"0":request.getAttribute("branchid").toString());
long punishid=Long.parseLong(request.getAttribute("punishid")==null?"0":request.getAttribute("punishid").toString());
long punishlevel=Long.parseLong(request.getAttribute("punishlevel")==null?"0":request.getAttribute("punishlevel").toString());
long showData=Long.parseLong(request.getAttribute("showData")==null?"0":request.getAttribute("showData").toString());
int state=Integer.parseInt(request.getAttribute("state")==null?"0":request.getAttribute("state").toString());
int count=Integer.parseInt(request.getAttribute("count")==null?"0":request.getAttribute("count").toString());
long customerid=Long.parseLong(request.getAttribute("customerid")==null?"0":request.getAttribute("customerid").toString());
String punishcontent = request.getAttribute("punishcontent")==null?"":request.getAttribute("punishcontent").toString();
String starttime = request.getAttribute("starttime")==null?"":request.getAttribute("starttime").toString();
String endtime = request.getAttribute("endtime")==null?"":request.getAttribute("endtime").toString();


%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>扣罚类型登记</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/punish.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/highcharts.js"></script>
<script src="<%=request.getContextPath()%>/js/highcharts/exporting.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
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
	
});
function addInit(){
	//无处理
}
function fun(){
	if(<%=showData%>==1)
		{ alert("成功导入"+<%=count%>+"条数据");
		$("#searchForm").submit();}
}
function editInit(){
	//$("#searchForm").submit();
}
function editSuccess(data){
	$("#searchForm").submit();
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box textarea" , parent.document).val("");
	$("#alert_box select", parent.document).val("");
	$("#searchForm").submit();
}
function delData(id){
	$.ajax({
		type:"post",
		url:"<%=request.getContextPath()%>/punish/deletePunish",
		data:{"id":id},
		dataType:"json",
		success:function(data){
			alert(data.error);
			$("#searchForm").submit();
		}});
}
function state(id){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/punish/state",
		data:{"id":id},
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}
function importPage(){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/punish/importPage",
		dataType : "html",
		success : function(data) {
			// alert(data);
			$("#alert_box", parent.document).html(data);

		},
		complete : function() {
			addInit();// 初始化某些ajax弹出页面
			viewBox();
		}
	});
}
function showUp()
{
	$("#fileup").removeAttr('style');
	$("#top").removeAttr('style');
	$("#br").attr('style','display: none;');
	$("#imp").attr('disabled','disabled');
//	$("#box_form").removeAttr('style');
	}
function isgetallcheck(){
	if($('input[name="isprint"]:checked').size()>0){
		$('input[name="isprint"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="isprint"]').attr("checked",true);
	}
}
function stateBatch(state)
{
	var ids="";
	var isprint = "";
	var userid = "";
	var userids = "";
	$('input[name="isprint"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		isprint = $(this).val();
		userid = $(this).attr('userid');
		if($.trim(isprint).length!=0){
		ids+=""+isprint+",";
		userids+=""+userid+",";
		}
		});
	if(ids.length==0){
		alert("请选择！");
		return false;
	}
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/punish/stateBactch",
		data:{"ids":ids.substring(0, ids.length-1),"state":state,"userids":userids.substring(0, userids.length-1)},
		dataType : "json",
		success : function(data) {
			$(".tishi_box").html(data.error);
			$(".tishi_box").show();
			setTimeout("$(\".tishi_box\").hide(1000)", 2000);
			if (data.errorCode == 0) {
				$("#searchForm").submit();
			}
		}
	});
	}
	function showButton()
	{ if($("#filename").val().length>0)
		{
		$("#subfile").removeAttr('disabled');
		}
	}
	function checkData(state){
		if(state==1){
			alert("已审核的订单不允许修改！");
			return false;
		}
		return true;
	}
</script>
</head>

<body style="background:#eef9ff" onload="fun()">

<div class="right_box">
	<div class="inputselect_box">
	<span>
		</span>
	<form action="<%=request.getContextPath()%>/punish/punishOfbranch/1" method="post" id="searchForm">
		<table><tr><td>
		扣罚类型:<select id="punishid" name="punishid"  style="width: 80px">
		<option value="0">请选择</option>
		<%for(PunishType p:punishTypeList){ %>
		<option value="<%=p.getId() %>" <%if(p.getId()==punishid){ %>selected="selected"<%} %>><%=p.getName() %></option>
		<%} %>
		</select>
		责任人:<select id="userid" name="userid"  style="width: 80px">
		<option value="0">请选择</option>
		<%for(User u:userList){ %>
		<option value="<%=u.getUserid()%>" <%if(u.getUserid()==userid) {%>selected="selected"<%} %>><%=u.getRealname() %></option>
		<%} %>
		</select>
		订单号:<input type="text" name="cwb" id="cwb" value="<%=cwb%>"/>
		优先级别:<select id="punishlevel" name="punishlevel"  style="width: 80px">
		<option value="0">请选择</option>
		</select>
		审核状态:<select id="state" name="state">
		<option value="-1">请选择</option>
		<option value="0" <%if(state==0) {%>selected="selected"<%} %>>未审核</option>
		<option value="1" <%if(state==1) {%>selected="selected"<%} %>>已审核</option>
		</select>
		供货商:<select id="customerid" name="customerid" style="width: 80px">
		<option value="-1">请选择</option>
		<%for(Customer cus:customerList){ %>
		<option value="<%=cus.getCustomerid()%>" <%if(cus.getCustomerid()==customerid) {%>selected="selected"<%} %>><%=cus.getCustomername() %></option>
		<%} %>
		</select>
		扣罚内容:<input type="text" name="punishcontent" id="punishcontent" value="<%=punishcontent%>"/>
		</td></tr><tr><td>
创建时间：<input type ="text" name ="starttime" id="starttime"  value="<%=starttime%>"/>
			到
		<input type ="text" name ="endtime" id="endtime"  value="<%=endtime %>"/>
		<input type="hidden" id="isnow" name="isnow" value="1"/>
		<input type="submit" value="查询"/>
		<input type="button" value="导出" onclick="javascript:$('#exportExcle').submit()" <%if(punishList.size()==0){ %>disabled="disabled" <%} %>/>
		</td></tr></table>
	</form>

	</div>
<div id="top"style="display: none;" >
		<div id="box_form" >
		</div>
		<div id="box_form" >
		</div>
		<div id="box_form" >
		</div>
		</div>
		<div id="br"><br></br><br></br></div>
		<div id="br"><br></br></div>
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
	  <tr class="font_1">
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">扣罚类型</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">责任部门</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">责任人</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">扣罚时效</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">优先级别</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">扣罚金额</td>
			<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">扣罚内容</td>
			<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">状态</td>
			<td width="8%" align="center" valign="middle" bgcolor="#eef6ff">创建人</td>
			<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
		</tr>
		 <% for(Punish p : punishList){ %>
		<tr>
			<td width="8%" align="center" valign="middle"><%=p.getCwb()%></td>
			<td width="8%" align="center" valign="middle">
			<%for(Customer cus:customerList){ if(cus.getCustomerid()==p.getCustomerid()){out.print(cus.getCustomername());}}%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(PunishType pt:punishTypeList){ if(pt.getId()==p.getPunishid()){out.print(pt.getName());}}%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(Branch b:branchlist){ if(b.getBranchid()==p.getBranchid()){out.print(b.getBranchname());}}%>
			</td>
			<td width="6%" align="center" valign="middle">
			<%for(User u:userAll){ if(u.getUserid()==p.getUserid()){out.print(u.getRealname());}}%>
			</td>
			<td width="5%" align="center" valign="middle">
			<%=PunishtimeEnum.getText(p.getPunishtime()).getText() %>
			</td>
			<td width="8%" align="center" valign="middle">
			<%=PunishlevelEnum.getText(p.getPunishlevel()).getText() %>
			</td>
			<td width="5%" align="center" valign="middle">
			<%=p.getPunishfee() %>
			</td>
			<td width="10%" align="center" valign="middle">
			<%=p.getPunishcontent() %>
			</td>
			<td width="5%" align="center" valign="middle">
			<%=p.getState()==1?"已审核":"未审核"%>
			</td>
			<td width="8%" align="center" valign="middle">
			<%for(User u:userAll){ if(u.getUserid()==p.getCreateuser()){out.print(u.getRealname());}}%>
			</td>
			<td width="8%" align="center" style="font-size: 11px" valign="middle">
			<%=p.getCreatetime() %>
			</td>
		</tr>
		<%} %>
	</table>
	<div class="jg_10"></div><div class="jg_10"></div>
	</div>
	<%if(page_obj.getMaxpage()>1){ %>
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
</div>
<form id="exportExcle" action="<%=request.getContextPath()%>/punish/exportExcle" method="post" >
<input type="hidden" name="cwb" value="<%=cwb%>"/>
<input type="hidden" name="branchid" value="<%=branchid%>"/>
<input type="hidden" name="userid" value="<%=userid%>"/>
<input type="hidden" name="punishid" value="<%=punishid%>"/>
<input type="hidden" name="punishlevel" value="<%=punishlevel%>"/>
<input type="hidden" name="state" value="<%=state%>"/>
<input type="hidden" name="customerid" value="<%=customerid%>"/>
<input type="hidden" name="starttime" value="<%=starttime%>"/>
<input type="hidden" name="endtime" value="<%=endtime%>"/>
</form>
		<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page")==null?0:request.getAttribute("page") %>);
</script>	
	<div class="jg_10"></div>

	<div class="clear"></div>
	<input type="hidden" id="add" value="<%=request.getContextPath()%>/punish/add" />
	<input type="hidden" id="dmpurl" value="<%=request.getContextPath()%>" />
	<input type="hidden" id="edit" value="<%=request.getContextPath()%>/punish/edit/" />
</body>
</html>


