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

 <% 
	List<Reason> r = (List<Reason>)request.getAttribute("lr")==null?null:(List<Reason>)request.getAttribute("lr");
	List<Reason> rs = (List<Reason>)request.getAttribute("lrs")==null?null: (List<Reason>)request.getAttribute("lrs");
	List<CsComplaintAccept> a= request.getAttribute("lc")==null?null: (List<CsComplaintAccept>)request.getAttribute("lc"); 
	List<Branch> b =(List<Branch>)request.getAttribute("lb")==null?null:(List<Branch>)request.getAttribute("lb");
	 String uname=(String)request.getAttribute("username")==null?null:(String)request.getAttribute("username"); 
	/* Map<String,List<CsConsigneeInfo>>  maplist=(Map<String,List<CsConsigneeInfo>>)request.getAttribute("maplist")==null?null:(Map<String,List<CsConsigneeInfo>>)request.getAttribute("maplist"); */
	Map<String,String> connameList=(Map<String,String>)request.getAttribute("connameList");
	List<CwbOrder> co=(List<CwbOrder>)request.getAttribute("co")==null?null:(List<CwbOrder>)request.getAttribute("co"); 
	Map<Long,String>  customernameList = (Map<Long,String>)request.getAttribute("customernameList");
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
		}else if($('#ComStateV').val()==$('#JieAnChongShenZhong').val()||$('#ComStateV').val()==$('#YiJieShu').val()||$('#ComStateV').val()==$('#YiJieAn').val()){
			alert('本条数据状态不能结案');
			return false;
		}
	
		getAddBox2();

	});
	$("#add_OrgAppeal").click(function() {
		if($('#FormV').val()==""){
			alert('请选择一条记录');
			return false;
		}else if($('#ComStateV').val()!=$('#YiJieAn').val()){
			alert('本条数据状态未结案不能申诉');
			return false;
		}
		getAddBox3();

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

function getFomeV(v,l){
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
							<%for(ComplaintTypeEnum c:ComplaintTypeEnum.values()){ %>		
							<option value="<%=c.getValue()%>"><%=c.getText()%></option>
								<%} %>
						</select>
						
					</td>
					<td>
				工单状态:<select name="complaintState" class="select1">				
							<option value="-1">全部</option>
							<%for(ComplaintStateEnum c:ComplaintStateEnum.values()){ %>	
							<option value="<%=c.getValue()%>"><%=c.getText()%></option>
						<%} %>
						</select>
					</td>
				</tr>	
				<tr>			
					<td>
					投诉一级分类:<select class="select1" name="complaintOneLevel" id="ol">
								<option value="-1">全部</option>
								<%for(Reason reason:r){ %>
								<option value="<%=reason.getReasonid()%>"><%=reason.getReasoncontent()%></option>
								<%} %>
								</select>
					</td>	
					<td>
				被投诉机构:		<select class="select1" name="codOrgId">
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
				投诉二级处理:<select class="select1" name="complaintTwoLevel" id="tl">
								<option value="-1">全部</option>
									<%for(Reason r1:rs){ %>
								<option value="<%=r1.getReasonid()%>"><%=r1.getReasoncontent()%></option>
								<%} %>
									</select>
					</td>						
					<td>
				是否扣罚:		<select class="select1">
							<option>全部</option>
							</select>
					</td>			
					<td>
				受理人:		<select class="select1">
							<option>全部</option>
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
					<button id="add_CUSA" class="input_button2">客服结案</button>
					<button id="add_AdjudicateRetrial" class="input_button2">结案重审</button>
					<button id="add_OrgVerify" class="input_button2">机构核实</button>
					<button id="add_OrgAppeal" class="input_button2">机构申诉</button>
					<button class="input_button2">导出</button>
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
			<tr onclick="getFomeV('<%=c.getAcceptNo() %>','<%=c.getComplaintState()%>')">
				<td><%=c.getAcceptNo() %></td>
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
				<td><%=uname%></td>					<!--受理人  -->
				<td><%=c.getAcceptTime() %></td>    <!--受理时间  -->
				<td><%for(Reason rsn:r){ %>
								<%if(rsn.getReasonid()==c.getComplaintOneLevel()){ %>  <!-- 一级原因 -->
									<%=rsn.getReasoncontent()==null?"":rsn.getReasoncontent()%>
								<%} }%>
				</td>
				<td><%for(Reason rsn1:rs){ %>
						<%if(rsn1.getReasonid()==c.getComplaintTwoLevel()){ %> 	
							<%=rsn1.getReasoncontent()==null?"":rsn1.getReasoncontent() %><!--二级原因  -->
						<%} }%>
				</td>
				<td><%=ComplaintResultEnum.getByValue((long)c.getComplaintResult()).getText()==null?"":ComplaintResultEnum.getByValue((long)c.getComplaintResult()).getText()%></td><!-- 投诉结果 -->
				<td>
					<label>是</label>					
				</td>
				<td><%=customernameList.get(c.getCustomerid())%> 
				</td>	
				<td>
					<label>催件次数</label>
				</td>
		</tr>
			<%} }%>
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
</body>
</html>