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
	List<CwbOrder> co=request.getAttribute("co")==null?null:(List<CwbOrder>)request.getAttribute("co"); 
	Map<Long,String>  customernameList = (Map<Long,String>)request.getAttribute("customernameList");
	Integer shensuendTime =(Integer)request.getAttribute("shensuendTime");
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


$(function() {
	
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
		if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()!=$('#DaiHeShi').val()){
			alert('本条数据状态不是待核实状态不能核实');
			return false;
		}
		getAddBoxx01();

	});
	$("#add_CUSA").click(function() {
		if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()==$('#JieAnChongShenZhong').val()||$('#ComStateV').val()==$('#YiJieShu').val()||$('#ComStateV').val()==$('#YiJieAn').val()||$('#ComStateV').val()==$('#DaiHeShi').val()){
			alert('本条数据状态不能结案');
			return false;
		}
	
		getAddBox2();

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
function getAddBox3() {
	if($('#FormV').val()==""){
		alert('请选择一条记录');
		return false;
	}
	$.ajax({
		type : "POST",
		data:'acceptNo='+$('#FormV').val(),
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
	$('#JieAnTimeValue').val(timev);
	$('#FormV').val("");
	$('#FormV').val(v);	
	
	$('#ComStateV').val("");
	$('#ComStateV').val(l);
}

function testCwbsIfNull(){
	if($('#orderNo').val()==""){
		alert('请输入至少一个订单号');
		return;
	}
	if($("#beginRangeTime").val()>$("#endRangeTime").val()){
		alert("开始时间不能大于结束时间");
		return;
	}
}
function decideShenSudate(){
	var str =$('#JieAnTimeValue').val();
	/*var str ='2015-05-12 23:13:15'; */
	str = str.replace(/-/g,"/");
	var date1 = new Date(str);	
	var str1=CurentTime();
	str1 = str1.replace(/-/g,"/");
	var date2 = new Date(str1);
	var date3=date2.getTime()-date1.getTime();  //时间差的毫秒数
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	var shensuendTime=$('#shensuendTime').val();
	if(days>shensuendTime){
		alert("抱歉，已经过了可申诉时间！");
		return false;
	}else if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()!=$('#YiJieAn').val()){
			alert('本条数据状态未结案不能申诉');
			return false;
		}
		getAddBox3();
	
}
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
			<form action="<%=request.getContextPath()%>/workorder/WorkOrderManageQuery" onsubmit="return testCwbsIfNull()">
				<tr>
					<td>
						订/运单号:<textarea rows="3" cols="16" name="orderNo" id="orderNo"></textarea>
					</td>		
					<td>
				工单类型:<select name="complaintType" class="select1">				
							<option value="-1">全部</option>									
							<option value="<%=ComplaintTypeEnum.DingDanChaXun.getValue()%>"><%=ComplaintTypeEnum.DingDanChaXun.getText()%></option>
							<option value="<%=ComplaintTypeEnum.CuijianTousu.getValue()%>"><%=ComplaintTypeEnum.CuijianTousu.getText()%></option>
						</select>
						
					</td>
					<td>
				工单状态:<select name="complaintState" class="select1">				
							<option value="-1">全部</option>
							<%for(ComplaintStateEnum c:ComplaintStateEnum.values()){ %>	
							<%if(c.getValue()!=0){ %>
							<%if(c.getValue()!=ComplaintStateEnum.DaiChuLi.getValue()){ %>
							<option value="<%=c.getValue()%>"><%=c.getText()%></option>
						<%} }}%>
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
							<select class="select1" name="codOrgId">
								<option value="-1">全部</option>
							<%for(Branch br:b){ %>
								<option value="<%=br.getBranchid()%>"><%=br.getBranchname() %></option>
							<%} %>	
							</select>
					</td>			
					<td>
				投诉处理结果:<select class="select1" name="complaintResult">
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
				是否扣罚:		<select class="select1" name="ifpunish">
							<option value="-1">全部</option>
							<option value="1">是</option>
							<option value="0">否</option>
							</select>
					</td>			
					<td>
							
				受理人:		<select class="select1" name="handleUser">
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
					工单受理时间:<input type="text" name="beginRangeTime" id="beginRangeTime" class="input_text1"/>—<input type="text" name="endRangeTime" id="endRangeTime" class="input_text1"/>
					</td>

				
					<td>
						<input type="submit" value="查询" class="input_button2">
						<input type="reset" value="重置" class="input_button2"/>
					</td>
				</tr>
				
			</form>	
			<tr>
				<td>
				<%if(currentuser==1){ %>
					<button id="add_CUSA" class="input_button2">客服结案</button>
					<button id="add_AdjudicateRetrial" class="input_button2">结案重审</button>
					 <%}else{ %>
					<button id="add_OrgVerify" class="input_button2">机构核实</button>
					<button id="add_OrgAppeal" class="input_button2" onclick="decideShenSudate()">机构申诉</button>
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
				<th bgcolor="#eef6ff">工单类型</th>
				<th bgcolor="#eef6ff">工单状态</th>
				<th bgcolor="#eef6ff">来电人姓名</th>
				<th bgcolor="#eef6ff">来电号码</th>
				<th bgcolor="#eef6ff">被投诉机构</th>
				<th bgcolor="#eef6ff">工单受理人</th>
				<th bgcolor="#eef6ff">受理时间</th>
				<th bgcolor="#eef6ff">投诉一级分类</th>
				<th bgcolor="#eef6ff">投诉二级分类</th>
				<th bgcolor="#eef6ff">投诉处理结果</th>
				<th bgcolor="#eef6ff">是否扣罚</th>
				<th bgcolor="#eef6ff">客户名称</th>	
				<th bgcolor="#eef6ff">催件次数</th>			
			</tr>
		<%if(a!=null){ %>			
			<%for(CsComplaintAccept c:a){ %>
			<%if(c.getComplaintState()!=ComplaintStateEnum.DaiChuLi.getValue()){ %>
			<tr onclick="getFomeV('<%=c.getAcceptNo() %>','<%=c.getComplaintState()%>','<%=c.getJieanTime()%>')" id="getFomeV">
				<%if(c.getComplaintState()==ComplaintStateEnum.YiJieShu.getValue()) {%>
				<td><a href="<%=request.getContextPath()%>/workorder/WorkorderDetail/<%=c.getAcceptNo() %>" target='_Blank'><%=c.getAcceptNo() %></a></td>
				<%}else{ %>
				<td><%=c.getAcceptNo() %></td>
				<%} %>
				<td><%=c.getOrderNo() %></td>
				<td><%=ComplaintTypeEnum.getByValue((long)c.getComplaintType()).getText()%></td>
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
					<%if(c.getIfpunish()==1){ %>
					<label>是</label>
					<%}else{ %>		
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
			<%} }}%>
		</table>
	</div>	

<!-- 	DaiChuLi(0, "待处理"),  YiJieAn(1, "已结案"),DaiHeShi(2,"待核实"),YiHeShi(3,"已核实"),
	YiJieShu(4,"已结束"),JieAnChongShenZhong(5,"结案重审中"); -->
	<input type="hidden" id="DaiChuLi" value="<%=ComplaintStateEnum.DaiChuLi.getValue()%>"/>
	<input type="hidden" id="YiJieAn" value="<%=ComplaintStateEnum.YiJieAn.getValue()%>"/>
	<input type="hidden" id="DaiHeShi" value="<%=ComplaintStateEnum.DaiHeShi.getValue()%>"/>
	<input type="hidden" id="YiHeShi" value="<%=ComplaintStateEnum.YiHeShi.getValue()%>"/>
	<input type="hidden" id="YiJieShu" value="<%=ComplaintStateEnum.YiJieShu.getValue()%>"/>
	<input type="hidden" id="JieAnChongShenZhong" value="<%=ComplaintStateEnum.JieAnChongShenZhong.getValue()%>"/>
	<input type="hidden" id="FormV"/>
	<input type="hidden" id="ComStateV"/>
	<input type="hidden" id="add_OrgVerifyV" value="<%=request.getContextPath()%>/workorder/OrgVerify"/>
	<input type="hidden" id="add_CSA" value="<%=request.getContextPath()%>/workorder/CustomerServiceAdjudicate">
	<input type="hidden" id="JieAnTimeValue"/>
	<input type="hidden" id="shensuendTime" value="<%=shensuendTime%>"/>
	
		
	
	
	
	<form action="<%=request.getContextPath()%>/workorder/exportWorkOrderExcle" id="WorkorderInfo">
		<input type="hidden" value="<%=request.getParameter("complaintType")%>" name="complaintType"/>
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
		<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo"/>
	</form>
	
	<script type="text/javascript">
	function exportWorkOrderInFoExcle(){
		if(<%=a != null && a.size()>0 %>){
		 	$("#exInfo").val("请稍后……");
		 	$("#WorkorderInfo").submit();
		}else{
			alert("没有做查询操作，不能导出！");
		};
		
	}	
	
	
	</script>
	
	
	
	
	
	
	
</body>
</html>