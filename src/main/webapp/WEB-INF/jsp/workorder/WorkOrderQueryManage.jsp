<%@page import="cn.explink.enumutil.ComplaintStateEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsComplaintAccept"%>
<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.enumutil.CwbFlowOrderTypeEnum"%>
<%@page import="cn.explink.enumutil.ComplaintTypeEnum"%>
<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.enumutil.ComplaintResultEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<%@page import="cn.explink.domain.CwbOrder"%>
<%@page import="cn.explink.domain.User"%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	List<User> alluser=request.getAttribute("alluser")==null?null:(List<User>)request.getAttribute("alluser");
	List<Reason> r =request.getAttribute("lr")==null?null:(List<Reason>)request.getAttribute("lr");
 	List<Reason> alltworeason =request.getAttribute("alltworeason")==null?null:(List<Reason>)request.getAttribute("alltworeason");
	List<CsComplaintAccept> a= request.getAttribute("lc")==null?null: (List<CsComplaintAccept>)request.getAttribute("lc"); 
	List<Branch> b =request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");	
	Map<String,String> connameList=request.getAttribute("connameList")==null?null:(Map<String,String>)request.getAttribute("connameList");
	Map<Long,String>  customernameList = (Map<Long,String>)request.getAttribute("customernameList");
	Integer heshiTime =(Integer)request.getAttribute("heshiTime");
	List<CsComplaintAccept> lcsa=request.getAttribute("lcsa")==null?null:(List<CsComplaintAccept>)request.getAttribute("lcsa");
	long currentuser =(Long)request.getAttribute("currentuser");
	
%> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/redmond/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">

function getReasonValueV(){
	/* var jsonData = {};
	jsonData['complaintOneLevel'] = $('#olreasonV').val(); */
	$.ajax({
		type:'POST',
		data:'complaintOneLevel='+$('#olreasonV').val(),
		url:"<%=request.getContextPath()%>/workorder/getTwoValueByOneReason",
		dataType:'json',
		success:function(data){
			var Str = "<option value='-1'>全部</option>";
			$.each(data,function(ind,ele){
				var dataTrStr = "<option value='" + ele.reasonid + "'>" + ele.reasoncontent+ "</option>";
				Str+=dataTrStr;
			});
			
			$('#tlreasonV').html(Str);
		}
	
	});	
}
function getAddBox3(FormV) {
	$.ajax({
		type : "POST",
		data:{"acceptNo":FormV},	
		url :"<%=request.getContextPath()%>/workorder/OrgAppeal",
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

$(function() {
	
	$('#complaintState').val($('#complaintStateVL').val());
	$('#olreasonV').val($('#complaintOneLevelVL').val());
	$('#tlreasonV').val($('#complaintTwoLevelVL').val());
	$('#codOrgId').val($('#codOrgIdVL').val());
	$('#complaintResult').val($('#complaintResultVL').val());
	$('#ifpunish').val($('#ifpunishVL').val());
	$('#handleUser').val($('#handleUserVL').val());
	$('#beginRangeTime').val($('#beginRangeTimeVL').val());
	$('#endRangeTime').val($('#endRangeTimeVL').val());
	
	$("#beginRangeTime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#endRangeTime").datetimepicker({
	    changeMonth: true,
	    changeYear: true,
	    hourGrid: 4,
		minuteGrid: 10,
	    timeFormat: 'hh:mm:ss',
	    dateFormat: 'yy-mm-dd'
	});
	
	$("#add_OrgVerify").click(function() {
		var str =$('#ShouLiTimeValue').val();
		/*var str ='2015-05-12 23:13:15'; */
		str = str.replace(/-/g,"/");
		var date1 = new Date(str);	
		var str1=CurentTime();
		str1 = str1.replace(/-/g,"/");
		var date2 = new Date(str1);
		var date3=date2.getTime()-date1.getTime();  //时间差的毫秒数
		var hours=Math.floor((date3 % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		var heshiTime=$('#heshiTime').val();
		if(hours>heshiTime){
			alert("此工单已经过了可核实时间 ！ ");
			return false;
		}else if($('#FormV').val()==""){
				alert('请选择一条记录');
				return false;
		}else if($('#ComStateV').val()!=$('#DaiHeShi').val()){
			alert('本条数据状态不是待核实状态不能核实');
			return false;
		}
		getAddBoxx01();

	});
	$("#add_CUSA").click(function() {
		var str =$('#ShouLiTimeValue').val();
		/*var str ='2015-05-12 23:13:15'; */
		str = str.replace(/-/g,"/");
		var date1 = new Date(str);	
		var str1=CurentTime();
		str1 = str1.replace(/-/g,"/");
		var date2 = new Date(str1);
		var date3=date2.getTime()-date1.getTime();  //时间差的毫秒数
		//计算出相差hours
		var hours=Math.floor((date3 % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
		var heshiTime=$('#heshiTime').val();
		if(hours>heshiTime){
			if($('#FormV').val()==""){
				alert('请选择一条记录');
				return false;
			}
			getAddBox2();
		}else{
		if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()==$('#JieAnChongShenZhong').val()||$('#ComStateV').val()==$('#YiJieShu').val()||$('#ComStateV').val()==$('#YiJieAn').val()||$('#ComStateV').val()==$('#DaiHeShi').val()){
			alert('本条数据未核实不能结案');
			return false;
		}
		getAddBox2();
		}
	});
	
	$("#add_AdjudicateRetrial").click(function() {
		if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()!=$('#JieAnChongShenZhong').val()){
			alert('本条数据状态不能结案重审');
			return false;
		}
		
		getAddBox4();

	});
	
	$("table#waitacceptDg tr").click(function(){
		$(this).css("backgroundColor","yellow");
		$(this).siblings().css("backgroundColor","#ffffff");
	});

});

function getAddBoxx01() {
	$.ajax({
		type : "POST",
		data:'acceptNo='+$('#FormV').val(),
		url : $("#add_OrgVerifyV").val(),
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
function getAddBox2() {
	if($('#FormV').val()==""){
		
		alert('请选择一条记录');
		return false;
	}
	$.ajax({
		type : "POST",
		data:'acceptNo='+$('#FormV').val(),
		url : $("#add_CSA").val(),
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

function getAddBox4() {
	$.ajax({
		type : "POST",
		data:'acceptNo='+$('#FormV').val(),
		url :"<%=request.getContextPath()%>/workorder/AdjudicateRetrial",
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
function addInit(){
	
}

function getFomeV(v,l,timev){
	$('#ShouLiTimeValue').val(timev);
	$('#FormV').val("");
	$('#FormV').val(v);	
	
	$('#ComStateV').val("");
	$('#ComStateV').val(l);
}

function testCwbsIfNull(){
	if($("#beginRangeTime").val()>$("#endRangeTime").val()){
		alert("开始时间不能大于结束时间");
		return;
	}
}
/* function decideHeShidate(){
	var str =$('#ShouLiTimeValue').val();
	var str ='2015-05-12 23:13:15';
	str = str.replace(/-/g,"/");
	var date1 = new Date(str);	
	var str1=CurentTime();
	str1 = str1.replace(/-/g,"/");
	var date2 = new Date(str1);
	var date3=date2.getTime()-date1.getTime();  //时间差的毫秒数
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	var heshiTime=$('#heshiTime').val();
	if(days>heshiTime){
		alert("抱歉，已经过了可核实时间！ ");
		return false;
	}else if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()!=$('#DaiHeShi').val()){
			alert('本条数据状态未结案不能核实');
			return false;
		}
		getAddBox3();
	
} */
function CurentTime()   //计算当天时间
{ 
    var now = new Date();
    
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();           //秒
    
    var clock = year + "-";
    
    if(month < 10)
        clock += "0";
    
    clock += month + "-";
    
    if(day < 10)
        clock += "0";
        
    clock += day + " ";
    
    if(hh < 10)
        clock += "0";
        
    clock += hh + ":";
    if (mm < 10) clock += '0'; 
    clock += mm + ":"; 
     
    if (ss < 10) clock += '0'; 
    clock += ss; 
    return(clock); 
}

</script>
</head>
<body>  
 
<div>		
	<div style="margin-left: 10px;margin-top: 20px">
		<table style="float: left">	
			<form action="${ requestScope.page == null ? '1' : requestScope.page }" id="workorderquerymanageid" method="post"/>
				<tr>
					<td>
						订/运单号:<textarea rows="3" cols="16" name="orderNo" id="orderNo"><%=request.getParameter("orderNo")==null?"":request.getParameter("orderNo")%></textarea>
					</td>	
					<td>
						工单号:<textarea rows="3" cols="16" name="acceptNo" id="acceptNo" ><%=request.getParameter("acceptNo")==null?"":request.getParameter("acceptNo")%></textarea>
					</td>	
					<td>
				工单状态:<select name="complaintState" class="select1" id="complaintState">				
							<option value="-1">全部</option>
							<option value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>"><%=ComplaintStateEnum.DaiHeShi.getText()%></option>
							<option value="<%=ComplaintStateEnum.YiHeShi.getValue()%>"><%=ComplaintStateEnum.YiHeShi.getText()%></option>
							<option value="<%=ComplaintStateEnum.YiJieAn.getValue()%>"><%=ComplaintStateEnum.YiJieAn.getText()%></option>
						</select>
					</td>
				</tr>	
				<tr>			
					<td>
					<span>投诉一级分类:</span> 
							<select class="select1" name="complaintOneLevel" id="olreasonV" onchange="getReasonValueV()">
								<option value="-1">全部</option>
								<%if(r!=null){%>
								<%for(Reason reason:r){ %>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} }%>
								</select>
					</td>	
					<td>
							<span>被投诉机构:</span>
							<select class="select1" name="codOrgId" id="codOrgId">
								<option value="-1">全部</option>
							<%for(Branch br:b){ %>
								<option value="<%=br.getBranchid()%>"><%=br.getBranchname() %></option>
							<%} %>	
							</select>
					</td>			
					<td>
				投诉处理结果:<select class="select1" name="complaintResult" id="complaintResult">
								<option value="-1">全部</option>
								<option value="<%=ComplaintResultEnum.WeiChuLi.getValue()%>"><%=ComplaintResultEnum.WeiChuLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.ChengLi.getValue()%>"><%=ComplaintResultEnum.ChengLi.getText() %></option>
								<option value="<%=ComplaintResultEnum.BuChengLi.getValue()%>"><%=ComplaintResultEnum.BuChengLi.getText() %></option>								
							</select>
					</td>	
				<tr>		
					<td>
							<span>投诉二级处理:</span>
							<select class="select1" name="complaintTwoLevel" id="tlreasonV">
							
							</select>
					</td>						
					<td>
				是否扣罚:		<select class="select1" name="ifpunish" id="ifpunish">
							<option value="-1">全部</option>
							<option value="1">否</option>
							<option value="2">是</option>
							</select>
					</td>			
					<td>
							
				受理人:		<select class="select1" name="handleUser" id="handleUser">
							<option value="">全部</option>
							<%if(lcsa!=null){ %>
							<%for(CsComplaintAccept c:lcsa) {%>
							<option value="<%=c.getHandleUser() %>"><%=c.getHandleUser() %></option>
							<% }}%>
							</select>
					</td>
				</tr>
				<tr>			
					<td>
					工单受理时间:<input type="text" name="beginRangeTime" id="beginRangeTime" class="input_text1"/>—<input type="text" name="endRangeTime" id="endRangeTime" class="input_text1" onblur="testCwbsIfNull() "/>
					</td>

				
					<td>
						<input type="submit" onclick="$('#workorderquerymanageid').attr('action',1);return true;" value="查 询" class="input_button2" />
						<input type="reset" value="重置" class="input_button2"/>
					</td>
				</tr>
				
			</form>	
			<tr>
				<td>
				<%if(currentuser==1){ %>
					<button id="add_CUSA" class="input_button2">客服结案</button>
					<!-- <button id="add_AdjudicateRetrial" class="input_button2">结案重审</button> -->
					 <%}else{ %>
					<button id="add_OrgVerify" class="input_button2">机构核实</button>
				<!-- 	<button id="add_OrgAppeal" class="input_button2" onclick="decideShenSudate()">机构申诉</button> -->
					 <%} %>
					<button class="input_button2" onclick="exportWorkOrderInFoExcle()" id="exInfo">导出</button>
				</td>
			</tr>
		</table>		
	</div>	
		<br>
		<table  width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="waitacceptDg">
			<tr class="font_1">
				<th bgcolor="#eef6ff">工单号</th>
				<th bgcolor="#eef6ff">订单号</th>
				<!-- <th bgcolor="#eef6ff">工单类型</th> -->
				<th bgcolor="#eef6ff">工单状态</th>
				<th bgcolor="#eef6ff">来电人姓名</th>
				<th bgcolor="#eef6ff">来电号码</th>
				<th bgcolor="#eef6ff">被投诉机构</th>
				<th bgcolor="#eef6ff">工单受理人</th>
				<th bgcolor="#eef6ff">受理时间</th>
				<th bgcolor="#eef6ff">投诉一级分类</th>
				<th bgcolor="#eef6ff">投诉二级分类</th>
				<th bgcolor="#eef6ff">投诉处理结果</th>
				<th bgcolor="#eef6ff">是否关联扣罚单</th>
				<th bgcolor="#eef6ff">客户名称</th>	
				<th bgcolor="#eef6ff">催件次数</th>			
			</tr>
		<%if(a!=null){ %>			
			<%for(CsComplaintAccept c:a){ %>
			<tr onclick="getFomeV('<%=c.getAcceptNo() %>','<%=c.getComplaintState()%>','<%=c.getAcceptTime()%>')" id="getFomeV">
				<%-- <%if(c.getComplaintState()==ComplaintStateEnum.YiJieShu.getValue()||c.getComplaintState()==ComplaintStateEnum.YiJieAn.getValue()) {%> --%>
				<td><a href="javascript:getAddBox3('<%=c.getAcceptNo() %>')"><%=c.getAcceptNo() %></a></td>
			<%-- 	<%}else{ %>
				<td><%=c.getAcceptNo() %></td> --%>
				<%-- <%} %> --%>
				<td><%=c.getOrderNo() %></td>
				<%-- <td><%=ComplaintTypeEnum.getByValue((long)c.getComplaintType()).getText()%></td> --%>
				<td><%=ComplaintStateEnum.getByValue(c.getComplaintState()).getText() %></td>
				<td><%=connameList.get(c.getPhoneOne())%>
				</td>				
				<td><%=c.getPhoneOne() %></td>
				<td><%for(Branch br:b){ %>
					<%if(br.getBranchid()==c.getCodOrgId()){ %>  <!-- 被投诉机构 -->
						<%=br.getBranchname()%>
					<%} }%>
				<td>
						<%for(User u:alluser){ %>
							<%if(c.getHandleUser().equals(u.getUsername())){ %>
								<%=u.getRealname()%>
						<%} }%>
				</td>					<!--受理人  -->
				<td><%=c.getAcceptTime() %></td> 
				   <!--受理时间  -->
				   <%if(r!=null){ %>
				<td><%for(Reason rsn:r){ %>
								<%if(rsn.getReasonid()==c.getComplaintOneLevel()){ %>  <!-- 一级原因 -->
									<%=rsn.getReasoncontent()==null?"":rsn.getReasoncontent()%>
								<%}} }%>
				</td>
				<td>
					<%if(alltworeason!=null){ %>
						<%for(Reason rsn1:alltworeason){ %>
						<%if(rsn1.getReasonid()==c.getComplaintTwoLevel()){ %> 	
							<%=rsn1.getReasoncontent()==null?"":rsn1.getReasoncontent() %><!--二级原因  -->
						<%} }}%>
				</td>
				<td><%=ComplaintResultEnum.getByValue((long)c.getComplaintResult()).getText()==null?"":ComplaintResultEnum.getByValue((long)c.getComplaintResult()).getText()%></td><!-- 投诉结果 -->
				<td>
					<%if(c.getIfpunish()==2){ %>
					<label>是</label>
					<%}else{%>
					<label>否</label>
					<%} %>			
				</td>
				<td><%=customernameList.get(c.getCustomerid())%> 
				</td>	
				<td>
					<%if(c.getCuijianNum()>0) {%>
					<%=c.getCuijianNum() %>
					<%}else{ %>
					<label>0</label>
					<%} %>
				</td>
		</tr>
			<%} }%>
		</table>
	</div>	

<!-- 	DaiChuLi(0, "待处理"),  YiJieAn(1, "已结案"),DaiHeShi(2,"待核实"),YiHeShi(3,"已核实"),
	YiJieShu(4,"已结束"),JieAnChongShenZhong(5,"结案重审中"); -->
	<input type="hidden" id="YiJieAn" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>"/>
	<input type="hidden" id="DaiHeShi" value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>"/>
	<input type="hidden" id="YiHeShi" value="<%=ComplaintStateEnum.YiHeShi.getValue()%>"/>
	<input type="hidden" id="YiJieShu" value="<%=ComplaintStateEnum.YiJieShu.getValue()%>"/>
	<input type="hidden" id="JieAnChongShenZhong" value="<%=ComplaintStateEnum.JieAnChongShenZhong.getValue()%>"/>
	<input type="hidden" id="FormV"/>
	<input type="hidden" id="ComStateV"/>
	<input type="hidden" id="add_OrgVerifyV" value="<%=request.getContextPath()%>/workorder/OrgVerify"/>
	<input type="hidden" id="add_CSA" value="<%=request.getContextPath()%>/workorder/CustomerServiceAdjudicate">
	<input type="hidden" id="ShouLiTimeValue"/>
	<input type="hidden" id="heshiTime" value="<%=heshiTime%>"/>
	
		
	
	
	
	<form action="<%=request.getContextPath()%>/workorder/exportWorkOrderExcle" id="WorkorderInfo">
		<%-- <input type="hidden" value="<%=request.getParameter("complaintType")%>" name="complaintType"/> --%>
		<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo"/>
		<input type="hidden" value="<%=request.getParameter("complaintState") %>" name="complaintState"/>
		<input type="hidden" value="<%=request.getParameter("complaintOneLevel")==null?"-1":request.getParameter("complaintOneLevel") %>" name="complaintOneLevel"/>
		<input type="hidden" value="<%=request.getParameter("codOrgId")%>" name="codOrgId"/>
		<input type="hidden" value="<%=request.getParameter("complaintResult") %>" name="complaintResult"/>
		<input type="hidden" value="<%=request.getParameter("complaintTwoLevel")==null?"-1":request.getParameter("complaintTwoLevel") %>" name="complaintTwoLevel"/>
		<input type="hidden" value="<%=request.getParameter("beginRangeTime")%>" name="beginRangeTime"/>
		<input type="hidden" value="<%=request.getParameter("endRangeTime") %>" name="endRangeTime"/>
		<input type="hidden" value="<%=request.getParameter("handleUser") %>" name="handleUser"/>
		<input type="hidden" value="<%=request.getParameter("ifpunish")%>" name="ifpunish"/>	
		<input type="hidden" value="<%=request.getParameter("acceptNo")%>" name="acceptNo"/>
	</form>
	
	<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1" id="pageid">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#workorderquerymanageid').attr('action','1');$('#workorderquerymanageid').submit();" >第一页</a>　
				<a href="javascript:$('#workorderquerymanageid').attr('action','${page_obj.previous < 1 ? 1 : page_obj.previous}');$('#workorderquerymanageid').submit();">上一页</a>　
				<a href="javascript:$('#workorderquerymanageid').attr('action','${page_obj.next < 1 ? 1 : page_obj.next}');$('#workorderquerymanageid').submit();" >下一页</a>　
				<a href="javascript:$('#workorderquerymanageid').attr('action','${page_obj.maxpage < 1 ? 1 : page_obj.maxpage}');$('#workorderquerymanageid').submit();" >最后一页</a>
				　共${page_obj.maxpage}页　共${page_obj.total}条记录 　当前第
					<select
						id="selectPg"
						onchange="$('#workorderquerymanageid').attr('action',$(this).val());$('#workorderquerymanageid').submit()">
						<c:forEach begin="1" end="${page_obj.maxpage}" var="i">
							<option value="${i}">${i}</option>
						</c:forEach>
					</select>页
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
			function exportWorkOrderInFoExcle(){
				if(<%=a != null && a.size()>0 %>){
				 	$("#exInfo").val("请稍后……");
				 	$("#WorkorderInfo").submit();
				}else{
					alert("没有做查询操作，不能导出！");
				};
				
			}	
			
			var sv='${requestScope.page}';
			$("#selectPg").val(sv);
	</script>
	
		<input type="hidden" id="complaintStateVL" value="<%=request.getParameter("complaintState")==null?"":request.getParameter("complaintState") %>">
		<input type="hidden" id="complaintOneLevelVL" value="<%=request.getParameter("complaintOneLevel")==null?"":request.getParameter("complaintOneLevel") %>">
		<input type="hidden" id="complaintTwoLevelVL" value="<%=request.getParameter("complaintTwoLevel")==null?"":request.getParameter("complaintTwoLevel") %>">
		<input type="hidden" id="codOrgIdVL" value="<%=request.getParameter("codOrgId")==null?"":request.getParameter("codOrgId") %>">
		<input type="hidden" id="complaintResultVL" value="<%=request.getParameter("complaintResult")==null?"":request.getParameter("complaintResult") %>">
		<input type="hidden" id="ifpunishVL" value="<%=request.getParameter("ifpunish")==null?"":request.getParameter("ifpunish") %>">
		<input type="hidden" id="handleUserVL" value="<%=request.getParameter("handleUser")==null?"":request.getParameter("handleUser") %>">
		<input type="hidden" id="beginRangeTimeVL" value="<%=request.getParameter("beginRangeTime")==null?"":request.getParameter("beginRangeTime") %>">
		<input type="hidden" id="endRangeTimeVL" value="<%=request.getParameter("endRangeTime")==null?"":request.getParameter("endRangeTime") %>">
	</body>
</html>