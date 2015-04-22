<%@page import="cn.explink.controller.AbnormalView"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.AbnormalType"%>
<%@page import="cn.explink.domain.AbnormalOrder"%>
<%@page import="cn.explink.enumutil.*"%>
<%@page import="cn.explink.domain.Customer"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.explink.dao.CwbDAO"%>

<%@page import="cn.explink.util.Page"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<AbnormalType> abnormalTypeList = (List<AbnormalType>)request.getAttribute("abnormalTypeList");
List<AbnormalView> views=(List<AbnormalView>)request.getAttribute("views");
List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
Page page_obj = (Page)request.getAttribute("page_obj");
Object cwb = request.getAttribute("cwb")==null?"":request.getAttribute("cwb");
  ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
  String starttime=request.getParameter("begindate")==null?DateTimeUtil.getDateBefore(1):request.getParameter("begindate");
  String endtime=request.getParameter("enddate")==null?DateTimeUtil.getNowTime():request.getParameter("enddate");
  String showabnomal = request.getAttribute("showabnomal").toString();
  String ishandle = request.getAttribute("ishandle").toString();
  long customerid = Long.parseLong(request.getAttribute("customerid").toString());
  List<Customer> customerlist=(List<Customer>)request.getAttribute("customerlist");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />

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
	
})


$(function() {
	$("#strtime").datetimepicker({
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
	$("#chuangjianstrtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	$("#chuangjianendtime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	
	
	checkstate();
	
});

function getThisBoxList(id){
	$.ajax({
		type: "POST",
		url:$("#handle"+id).val(),
		dataType:"html",
		success : function(data) {
			$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			viewBox();
		}
	});
}

function check(){
	if($.trim($("#cwb").val()).length==0)
	{
	if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.yichuli.getValue()%>||$("#ishandle").val()==<%=AbnormalOrderHandleEnum.chulizhong.getValue()%>){
	if($("#strtime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if(!Days($("#strtime").val(),$("#endtime").val())||($("#strtime").val()=='' &&$("#endtime").val()!='')||($("#strtime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度不能大于31天！");
		return false;
	}
	if($("#strtime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return;
	}
	}
	else{
		if($("#chuangjianstrtime").val()==""){
			alert("请选择开始时间");
			return false;
		}
		if($("#chuangjianendtime").val()==""){
			alert("请选择结束时间");
			return false;
		}
		/* if(!Days($("#chuangjianstrtime").val(),$("#chuangjianendtime").val())||($("#chuangjianstrtime").val()=='' &&$("#chuangjianendtime").val()!='')||($("#chuangjianstrtime").val()!='' &&$("#chuangjianendtime").val()=='')){
			alert("时间跨度不能大于3天！");
			return false;
		} */
		if($("#chuangjianstrtime").val()>$("#chuangjianendtime").val() && $("#chuangjianendtime").val() !=''){
			alert("开始时间不能大于结束时间");
			return;
		}
		
	}
	}
	$("#searchForm").submit();
	
}
function Days(day1,day2){     
	var y1, y2, m1, m2, d1, d2;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	var date1 = new Date(y1, m1, d1);            
	var date2 = new Date(y2, m2, d2);   
	var minsec = Date.parse(date2) - Date.parse(date1);          
	var days = minsec / 1000 / 60 / 60 / 24;  
	if(days>31){
		return false;
	}        
	return true;
}	
		
function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}		
function checkstate(){
	if($("#ishandle").val()==<%=AbnormalOrderHandleEnum.WeiChuLi.getValue()%>){
		$("#chuangjianstrtime").show();
		$("#chuangjianendtime").show();
		$("#strtime").hide();
		$("#endtime").hide();
		$("#chuli").html("创建时间");
	}else{
		$("#chuangjianstrtime").hide();
		$("#chuangjianendtime").hide();
		$("#strtime").show();
		$("#endtime").show();
		$("#chuli").html("处理时间");
	}
}
function isgetallcheck(){
	if($('input[name="id"]:checked').size()>0){
		$('input[name="id"]').each(function(){
			$(this).attr("checked",false);
		});
	}else{
		$('input[name="id"]').attr("checked",true);
	}
}
function stateBatch(state)
{
	var ids="";
	$('input[name="id"]:checked').each(function(){ //由于复选框一般选中的是多个,所以可以循环输出
		id=$(this).val();
		if($.trim(id).length!=0){
		ids+=id+",";
		}
		});
	if(ids.length==0){
		alert("请选择！");
		return false;
	}
	if(ids.indexOf(",")>0){
	$.ajax({
		type : "POST",
		url:"<%=request.getContextPath()%>/abnormalOrder/gotoBatch",
		data:{"ids":ids.substring(0, ids.length-1)},
		dataType : "html",
		success : function(data) {$("#alert_box",parent.document).html(data);
			
		},
		complete:function(){
			viewBox();
		}
	});
	}
	}

</script>
</head>
<body style="background:#eef9ff;overflow: hidden;" marginwidth="0" marginheight="0">
<div class="right_box">
	<div style="background:#FFF">
		<div style="position:relative; z-index:0;" >
			<div style="position:absolute;  z-index:99; width:100%" class="kf_listtop">
				<div class="kfsh_search">
							<form action="1" method="post" id="searchForm">
								<p>订单号：
									<textarea id="cwb" class="kfsh_text" rows="3" name="cwb" ><%=cwb%></textarea>
								&nbsp;机构名称：
									<label for="select2"></label>
									<select name="branchid" id="branchid" style="width: 120px;">
										<option value="0">请选择</option>
										<%if(branchList!=null||branchList.size()!=0){for(Branch b : branchList){ %>
											<option value="<%=b.getBranchid()%>"><%=b.getBranchname() %></option>
										<%}} %>
									</select>
									供货商:<select name ="customerid" id ="customerid"  style="width: 120px;">
									<option value="-1">请选择</option>
		          						<%for(Customer c : customerlist){ %>
		      					     <option value ="<%=c.getCustomerid() %>"  <%if(c.getCustomerid()==customerid){%>  selected="selected"<%}%>><%=c.getCustomername() %></option>
		         						 <%} %>
		      						  </select>
									<label for="select3"></label>
									<select name="abnormaltypeid" id="abnormaltypeid"  style="width: 120px;">
										<option value="0">请选择问题件类型</option>
										<%if(abnormalTypeList!=null||abnormalTypeList.size()>0)for(AbnormalType at : abnormalTypeList){ %>
										<option title="<%=at.getName() %>" value="<%=at.getId()%>"><%if(at.getName().length()>25){%><%=at.getName().substring(0,25)%><%}else{%><%=at.getName() %><%} %></option>
										<%} %>
									</select>
									&nbsp;处理结果：
									<label for="select4"></label>
									<select name="ishandle" id="ishandle" onchange="checkstate()">
										<!-- <option value="-1">全部</option> -->
										<option value="<%=AbnormalOrderHandleEnum.WeiChuLi.getValue()%>"><%=AbnormalOrderHandleEnum.WeiChuLi.getText() %></option>
										<%if(showabnomal.equals("1")){%>
										<option value="<%=AbnormalOrderHandleEnum.chulizhong.getValue()%>"><%=AbnormalOrderHandleEnum.chulizhong.getText() %></option>
										<option value="<%=AbnormalOrderHandleEnum.yichuli.getValue()%>"><%=AbnormalOrderHandleEnum.yichuli.getText() %></option>
										<%} %>
										<%if(!showabnomal.equals("1")){%>
										<option value="<%=AbnormalOrderHandleEnum.yichuli.getValue()%>"><%=AbnormalOrderHandleEnum.yichuli.getText() %></option>
										<%} %>
									</select>
									<strong id="chuli" >处理时间：</strong>
									<input type ="text"  style="width: 120px;" name ="begindate" id="strtime"  value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate") %>"/>
									<input type ="text"  style="width: 120px;" name ="chuangjianbegindate" id="chuangjianstrtime"  value="<%=request.getAttribute("chuangjianbegindate")==null?"":request.getAttribute("chuangjianbegindate") %>"/>
									<strong id="chulidown">到</strong>
									<input type ="text"  style="width: 120px;" name ="enddate" id="endtime"  value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate") %>"/>
									<input type ="text"   style="width: 120px;" name ="chuangjianenddate" id="chuangjianendtime"  value="<%=request.getAttribute("chuangjianenddate")==null?"":request.getAttribute("chuangjianenddate") %>"/>
									<input type="hidden" name="isshow" value="1"/>
									<input type="button"  onclick="check()" value="查询" class="input_button2">
									<%if(views != null && views.size()>0){ %>
										<input type ="button" value="批量处理" class="input_button2"  onclick="stateBatch();"/>
										<input type ="button" id="btnval" value="导出" class="input_button1" onclick="exportField();"/>
									<%} %>
								</p>
							</form>
				</div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tbody>
						<tr class="font_1" height="30" >
							<td width="30"  align="center" valign="middle" bgcolor="#f3f3f3"><a style="cursor: pointer;" onclick="isgetallcheck();">全选</a></td>
							<td width="120" align="center" valign="middle" bgcolor="#f3f3f3">订单号</td>
							<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">供货商</td>
							<td width="120" align="center" valign="middle" bgcolor="#E7F4E3">发货时间</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">当时状态</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">配送站点</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">反馈人</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件类型</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件反馈时间</td>
							<td width="100" align="center" valign="middle" bgcolor="#E7F4E3">问题件说明</td>
							<td width="80" align="center" valign="middle" bgcolor="#E7F4E3">操作</td>
						</tr>
					</tbody>
				</table>
				</div>
			<div style="height: 500px; overflow-y: scroll">
		<div style="height: 120px;"></div>
			<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
				<tbody>
					<%if(views!=null||views.size()>0)for(AbnormalView view : views){ %>
					<tr height="30" >
						<td width="30" align="center" valign="middle" bgcolor="#eef6ff">
						<%if(view.getIshandle()!=3) {%>
						<input id="id" type="checkbox" value="<%=view.getId()%>"  name="id"/>
						<%} %>
						</td>
						<td width="120" align="center" valign="middle"><%=view.getCwb() %></td>
						<td width="120" align="center" valign="middle"><%=view.getCustomerName() %></td>
						<td width="120" align="center" valign="middle"><%=view.getEmaildate() %></td>
						<td width="100" align="center" valign="middle"><%=view.getFlowordertype() %></td>
						<td width="100" align="center" valign="middle"><%=view.getBranchName()  %></td>
						<td width="100" align="center" valign="middle"><%=view.getCreuserName() %></td>
						<td width="100" align="center" valign="middle"><%=view.getAbnormalType() %></td>
						<td width="100" align="center" valign="middle"><%=view.getCredatetime() %></td>
						<td width="100" align="center" valign="middle"><%=view.getDescribe() %></td>
						<td width="80" align="center" valign="middle">
						<%if(!ishandle.equals(AbnormalOrderHandleEnum.yichuli.getValue()+"")){ %>
						<input type="button" name="" id="" value="处理" class="input_button2" onclick="getThisBoxList('<%=view.getId() %>');"/></td>
						<%}else{
							%>
						<input type="button" name="" id="" value="查看" class="input_button2" onclick="getThisBoxList('<%=view.getId() %>');"/></td>
							
						<%} %>
						<input type="hidden" id="handle<%=view.getId() %>" value="<%=request.getContextPath()%>/abnormalOrder/getabnormalOrder/<%=view.getId() %>?type=1" />
					</tr>
					<%} %>
				</tbody>
			</table>
			</div>
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
</div>
<form action="<%=request.getContextPath()%>/abnormalOrder/exportExcle" method="post" id="searchForm2">
	<input type="hidden" name="cwbStrs" id="cwbStrs" value="<%=request.getParameter("cwb")==null?"":request.getParameter("cwb") %>"/>
	<input type="hidden" name="branchid1" id="branchid1" value="<%=request.getParameter("branchid")==null?"0":request.getParameter("branchid")%>"/>
	<input type="hidden" name="abnormaltypeid1" id="abnormaltypeid1" value="<%=request.getParameter("abnormaltypeid")==null?"0":request.getParameter("abnormaltypeid")%>"/>
	<input type="hidden" name="ishandle1" id="ishandle1" value="<%=request.getParameter("ishandle")==null?"0":request.getParameter("ishandle")%>"/>
	<input type="hidden" name="begindate1" id="bedindate1" value="<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>"/>
	<input type="hidden" name="enddate1" id="enddate1" value="<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>"/>
	<input type="hidden" name="chuangjianbegindate1" id="chuangjianbedindate1" value="<%=request.getParameter("chuangjianbegindate")==null?"":request.getParameter("chuangjianbegindate")%>"/>
	<input type="hidden" name="chuangjianenddate1" id="chuangjianenddate1" value="<%=request.getParameter("chuangjianenddate")==null?"":request.getParameter("chuangjianenddate")%>"/>
	<input type="hidden" name="customerid" id="customerid" value="<%=request.getParameter("customerid")==null?"":request.getParameter("customerid")%>"/>
</form>
<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
$("#abnormaltypeid").val(<%=request.getParameter("abnormaltypeid")==null?0:Long.parseLong(request.getParameter("abnormaltypeid"))%>);
$("#ishandle").val(<%=request.getParameter("ishandle")==null?0:Long.parseLong(request.getParameter("ishandle"))%>);
$("#branchid").val(<%=request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid"))%>);
$("#strtime").val('<%=request.getParameter("begindate")==null?"":request.getParameter("begindate")%>');
$("#endtime").val('<%=request.getParameter("enddate")==null?"":request.getParameter("enddate")%>');
$("#customerid").val('<%=request.getParameter("customerid")==null?"":request.getParameter("customerid")%>');
function exportField(){
	if(<%=views != null && views.size()>0 %>){
		$("#btnval").attr("disabled","disabled");
	 	$("#btnval").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};
}

</script>
</body>
</html>

