<%@page import="cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderTypeIdEnum"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.b2c.huitongtx.addressmatch.HttxEditBranch"%>
<%@page import="cn.explink.b2c.huitongtx.addressmatch.MatchTypeEnum"%>
<%@page import="java.util.List,java.util.ArrayList"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.Page"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%	
	
	List<Branch> branchlist = (List<Branch>) request.getAttribute("branchlist");
	
	long SuccessAddress=(Long )request.getAttribute("SuccessAddress");
	long NotSuccess=(Long )request.getAttribute("NotSuccess"); 

	long SuccessRenGong=(Long )request.getAttribute("SuccessRenGong");  //人工匹配
	long sendSuccess=(Long )request.getAttribute("sendSuccess");  //已推送
	
	long sendflag=request.getParameter("sendflag")==null?0:Long.valueOf(request.getParameter("sendflag"));  //推送状态
	
	
	
	Page page_obj = request.getAttribute("page_obj")==null?new Page():(Page)request.getAttribute("page_obj");
	List<HttxEditBranch> httxList = request.getAttribute("httxBranchList")==null?new ArrayList<HttxEditBranch>():(List<HttxEditBranch>)request.getAttribute("httxBranchList");
	long branchidParam = request.getParameter("branchid")==null?0:Long.parseLong(request.getParameter("branchid").toString());
	
	  String starttime=request.getParameter("starttime")==null?(String)request.getAttribute("starttime"):request.getParameter("starttime");
	  String endtime=request.getParameter("endtime")==null?(String)request.getAttribute("endtime"):request.getParameter("endtime");
	  
	  int addressMatchType=request.getParameter("addressCodeEditType")==null?-1:Integer.valueOf(request.getParameter("addressCodeEditType"));
	  
	  int matchtype=Integer.valueOf(request.getParameter("matchtype")==null?("-1"):request.getParameter("matchtype"));
	//  if(addressMatchType!=-1){
		  matchtype=addressMatchType;
	//  } 
	  
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script>
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




function subEdit(form){
	if(form.excelbranch.value.length==0){
		alert("请输入站点");
		return false;
	}
	$(".tishi_box",parent.document).hide();
	$.ajax({
		type: "POST",
		url:$(form).attr("action"),
		data:$(form).serialize(),
		dataType:"json",
		success : function(data) {
			$(".tishi_box",parent.document).html(data.error);
			$(".tishi_box",parent.document).show();
			setTimeout("$(\".tishi_box\",parent.document).hide(1000)", 2000);
			if(data.errorCode==0){
				form.excelbranch.value=data.excelbranch;
				//form.exceldeliver.value=data.exceldeliver;
				var $inp = $('input:text');
				var nxtIdx = $inp.index(form.excelbranch) + 1;
	             $(":input:text:eq(" + nxtIdx + ")").focus();
			}else{
				form.excelbranch.value="";
			}
		}
	});
}



function selectPage(page){
		$("#page").val(page);
		$("#editBranchForm").submit();
}


function searchForm(){
	$("#branchid").val("0");  //重置查询站点的条件
	$("#addressCodeEditType").val($("#matchtype").val());
	if(check()){
		$("#editBranchForm").submit();
	}
}


function calcCount(matchtype,starttime,endtime){
	var senddataCount=0;
	$.ajax({
		type: "POST",
		url:'<%=request.getContextPath()%>'+'/httxeditbranch/selectCount',
		data : {
			matchtype : matchtype,
			starttime : starttime,
			endtime : endtime
		},
		dataType:"json",
		async: false,
		success : function(data) {
			senddataCount=data.selectCount;
		}
	});
	return senddataCount;
} 


function submitMatchResult(){
	if(!check()){
		return;	
	}
	var matchtype=$("#matchtype").val();
	if(matchtype==-1){
		alert('请至少选择一种匹配状态回传！');
		return ;
	}
	
	var sendflag=$("#sendflag").val();
	
	if(sendflag>2){
		alert('已推送成功订单不能再次推送！');
		return ;
	}
	
	var starttime=$("#starttime").val();
	var endtime=$("#endtime").val();
	var senddataCount=calcCount(matchtype,starttime,endtime);
	
	if(<%=httxList == null || httxList.size()==0  %>||senddataCount==0){
		alert('当前没有待回传站点匹配结果的信息');
		return;
	}
	
	
	var matchtype_text=$("#matchtype option:selected").text();
	if(confirm('您当前选择匹配类型为【'+matchtype_text+'】,待回传数量为【'+senddataCount+'】个,确定要手动回传匹配结果么？')){
		
			$("#btnsendflag").attr("disabled","disabled");
			$("#div_loading").show();
			$("#editBranchForm").attr("action","<%=request.getContextPath() %>/httxeditbranch/matchresultsubmit");
			$("#editBranchForm").submit();
		
	}
	
	
}


function check(){
	if($("#starttime").val()==""){
		alert("请选择开始时间");
		return false;
	}
	if($("#endtime").val()==""){
		alert("请选择结束时间");
		return false;
	}
	if($("#starttime").val()>$("#endtime").val() && $("#endtime").val() !=''){
		alert("开始时间不能大于结束时间");
		return false;
	}
	if(!Days()||($("#starttime").val()=='' &&$("#endtime").val()!='')||($("#starttime").val()!='' &&$("#endtime").val()=='')){
		alert("时间跨度为31天！！！");
		return false;
	}
	else{
		return true;
	}
}

function Days(){     
	var day1 = $("#starttime").val();   
	var day2 = $("#endtime").val(); 
	var y1, y2, m1, m2, d1, d2,min;//year, month, day;   
	day1=new Date(Date.parse(day1.replace(/-/g,"/"))); 
	day2=new Date(Date.parse(day2.replace(/-/g,"/")));
	y1=day1.getFullYear();
	y2=day2.getFullYear();
	m1=parseInt(day1.getMonth())+1 ;
	m2=parseInt(day2.getMonth())+1;
	d1=day1.getDate();
	d2=day2.getDate();
	min=m2*31-m1*31-d1+d2;
	if(min>31){
		return false;
	}        
	return true;
}
</script>
</head>
<body  style="background:#f5f5f5">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="#" class="light">匹配地址</a></li>
			
		</ul>
	</div>
	<form name="editBranchForm" id="editBranchForm" method="POST" action="editBranch"  >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
				<tr id="customertr" class=VwCtr style="display:">
					<td width="100%" colspan="2">
						创建时间
						<input type ="text" name ="starttime" id="starttime"  value="<%=starttime %>"/>
							到
						<input type ="text" name ="endtime" id="endtime"  value="<%=endtime %>"/>
					 每页<select name="onePageNumber" id="onePageNumber">
								<option value="10">10</option>
								<option value="30">30</option>
								<option value="50">50</option>	
								<option value="100">100</option>
							</select>行
							匹配状态:
							<select name="matchtype" id="matchtype">
										<option value="-1" <%if(matchtype==-1){ %>selected<%} %>>全部</option>
										<%
										for(MatchTypeEnum enums:MatchTypeEnum.values()){
										%>
										<option value="<%=enums.getValue()%>" <%if(matchtype==enums.getValue()){ %>selected<%} %>><%=enums.getText() %></option>
										<%}%>
							</select>
							推送状态:
							<select name="sendflag" id="sendflag">
										<option value="0" <%if(sendflag==0){%>selected<%} %>>未推送</option>
										<option value="3" <%if(sendflag>2){%>selected<%} %>>已推送</option>
										<option value="2" <%if(sendflag==2){%>selected<%} %>>推送失败</option>
							</select>
							<input type="button" class="input_button2" value="查询" onclick="searchForm();">
							
					(共<a href="javascript:$('#addressCodeEditType').val(-1);selectPage(1);" style="font-size:18;font-weight: bold;color:red;" >${AllAddress}</a>单
				地址库自动匹配<a <%if(SuccessAddress==0){ %> href="#" <%} else{%>href="javascript:$('#addressCodeEditType').val(<%=MatchTypeEnum.DiZhiKu.getValue() %>);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${SuccessAddress}</a>
				人工匹配	<a <%if(SuccessRenGong==0){ %> href="#" <%} else{%> href="javascript:$('#addressCodeEditType').val(<%=MatchTypeEnum.RenGong.getValue() %>);selectPage(1);" <%} %>style="font-size:18;font-weight: bold;color:red;" >${SuccessRenGong}</a>
				未匹配<a <%if(NotSuccess==0){ %> href="#" <%} else{%>href="javascript:$('#addressCodeEditType').val(<%=MatchTypeEnum.WeiPiPei.getValue() %>);selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${NotSuccess}</a>
				已推送<a <%if(sendSuccess==0){ %> href="#" <%} else{%>href="javascript:$('#addressCodeEditType').val(-1); $('#sendflag').val(3); selectPage(1);"<%} %> style="font-size:18;font-weight: bold;color:red;" >${sendSuccess}</a>)
				)
					</td>
					<input type="hidden" id="addressCodeEditType" name="addressCodeEditType" value="-1" /><!-- 0为全部 1 为成功 2 为匹配 -->
					<input type="hidden" id="branchid" name="branchid" value="0" />
					
					<input type="hidden" id="page" name="page" value="1" />
					<input type="hidden" id="isshow" name="isshow" value="1" />
				</tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					站点表：
					<%for(Branch b : branchlist){%><a  href='javascript:$("#addressCodeEditType").val(<%=addressMatchType%>);$("#branchid").val(<%=b.getBranchid() %>);selectPage(1);' style='background-color:#bbffaa;' ><%=b.getBranchname() %>(<%=b.getBranchcode()%>)</a> <%}%>
					</td>
					
				</tr>
				<tr class=VwCtr style="display:">
					<td width="100%" colspan="2">
					
					<input type="button" class="input_button1" id="btnsendflag" name="btnsendflag" value="确认匹配结果批量推送" onclick="submitMatchResult()">
					<div id="div_loading" style="display:none" align="center"><IMG  src="<%=request.getContextPath()%>/images/dpfossloading.gif"> <font color="gray">正在处理,请稍后..</font></div>
					</td>
					
				</tr>
			</table>
		</form>
				<table id="Order" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" >
					<tr class="font_1">
						<td width="3%" align="center" height="19" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">快行线唯一编号</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">创建时间</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">匹配时间</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">匹配状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">推送状态</td>
						<td width="5%" align="center" align="center" valign="middle" bgcolor="#eef6ff">城市</td>
						<td width="30%" align="center" align="center" valign="middle" bgcolor="#eef6ff">详细地址</td>
						<td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">匹配到站（回车保存）</td>
					 	<!-- <td width="10%" align="center" align="center" valign="middle" bgcolor="#eef6ff">备注（回车保存）</td> --> 
			
					<%for(HttxEditBranch httx : httxList){
						String sendflagname="";
						if(httx.getSendflag()==0){
							sendflagname="未推送";
						}else if(httx.getSendflag()==2){
							sendflagname="推送失败";
						}else if(httx.getSendflag()>2){
							sendflagname="推送成功";
						}
						%>
						<tr>
						<form id="f<%=httx.getId() %>" method="POST" onSubmit="subEdit(this);return false;" action="editmatchbranch/<%=httx.getId() %>" >
							<td  align="center"  ><%=httx.getId() %></td>
							<td  align="center"  ><%=httx.getTaskcode() %></td>
							<td  align="center"  ><%=httx.getCretime() %></td>
							<td  align="center"  ><%=httx.getDealtime()%></td>
							<td  align="center"  ><%=MatchTypeEnum.getText(httx.getMatchtype()).getText() %></td>
							<td  align="center"  ><%=sendflagname%></td>
							<td  align="center"  ><%=httx.getReceiver_city() %></td>
							<td  align="left" id="add<%=httx.getId() %>" ><%=httx.getReceiver_address() %></td>
							
							<td  align="center"  ><input type="text" name="excelbranch" value="<%=httx.getMatchbranch() %>"  onfocus="$('#add<%=httx.getId() %>').css('background','#bbffaa');" onblur="$('#add<%=httx.getId() %>').css('background','#ffffff');" /></td>
							
						</form>
						</tr>
					
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
				<%if(page_obj.getMaxpage()>1){ %>
				<div class="iframe_bottom" id="view_page" >
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a href="javascript:$('#addressCodeEditType').val(<%=addressMatchType%>);$('#branchid').val(<%=branchidParam%>);selectPage(1);" >第一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=addressMatchType%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>);">上一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=addressMatchType%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getNext()<1?1:page_obj.getNext() %>);" >下一页</a>　
						<a href="javascript:$('#addressCodeEditType').val(<%=addressMatchType%>);$('#branchid').val(<%=branchidParam%>);selectPage(<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>);" >最后一页</a>
						　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
								id="selectPg"
								onchange="$('#addressCodeEditType').val(<%=addressMatchType%>);$('#branchid').val(<%=branchidParam%>);selectPage($(this).val());">
								<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
								<option value="<%=i %>" ><%=i %></option>
								<% } %>
							</select>页
					</td>
				</tr>
				</table>
				</div>
				
				<%} %>
		
</div>
<form action="<%=request.getContextPath()%>/dataimport/exportExcle" method="post" id="searchForm2">
		
	<input type="hidden" name="addressCodeEditType1" id="addressCodeEditType1" value="<%=addressMatchType%>"/>
	<input type="hidden" id="branchid1" name="branchid1" value="<%=branchidParam%>" />

</form>
<script type="text/javascript">
$(function(){
	$("#selectPg").val(<%=request.getAttribute("page") %>); 
	$("#onePageNumber").val(<%=request.getParameter("onePageNumber")==null?"10":request.getParameter("onePageNumber") %>);
	$("#branchid").val(<%=branchidParam%>);
});

function exportField(){
	if($("#isshow").val()=="1"&&<%=httxList != null && httxList.size()>0  %>){
		$("#exportmould2").val($("#exportmould").val());
		$("#btnval0").attr("disabled","disabled");
	 	$("#btnval0").val("请稍后……");
	 	$("#searchForm2").submit();
	}else{
		alert("没有做查询操作，不能导出！");
	};

}

</script>
</body>
</html>
