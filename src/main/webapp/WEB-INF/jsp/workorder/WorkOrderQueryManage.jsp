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
	List<Reason> r = (List<Reason>)request.getAttribute("lr");
	List<Reason> rs = (List<Reason>)request.getAttribute("lrs");
	List<CsComplaintAccept> a= request.getAttribute("lc")==null?null: (List<CsComplaintAccept>)request.getAttribute("lc"); 
	List<Branch> b =(List<Branch>)request.getAttribute("lb");
	 String uname=(String)request.getAttribute("username"); 
	Map<String,List<CsConsigneeInfo>>  maplist=(Map<String,List<CsConsigneeInfo>>)request.getAttribute("maplist");
	List<CwbOrder> co=(List<CwbOrder>)request.getAttribute("co"); 
%> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">


$(function() {
	$("#add_OrgVerify").click(function() {
		getAddBoxx01();

	});
	$("#add_CUSA").click(function() {
		getAddBox2();

	});
	$("#add_OrgAppeal").click(function() {
		getAddBox3();

	});
	$("#add_AdjudicateRetrial").click(function() {
		getAddBox4();

	});
	
	$("table#waitacceptDg tr").click(function(){
		$(this).css("backgroundColor","blue");
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

function getFomeV(v){
	$('#FormV').val(v);
}

</script>
</head>
<body>  
 
<div>		
	<div style="margin-left: 10px;margin-top: 20px">
		<table style="float: left">				
			<form action="<%=request.getContextPath()%>/workorder/WorkOrderManageQuery">
				<tr>
					<td>
						订/运单号:<textarea rows="3" cols="16" name="orderNo"></textarea>
					</td>		
					<td>
				工单类型:<select name="complaintType">				
							<option value="-1">全部</option>
							<%for(ComplaintTypeEnum c:ComplaintTypeEnum.values()){ %>		
							<option value="<%=c.getValue()%>"><%=c.getText()%></option>
								<%} %>
						</select>
						
					</td>
					<td>
				工单状态:<select name="complaintState">				
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
				是否扣罚:		<select>
							<option>全部</option>
							</select>
					</td>			
					<td>
				受理人:		<select>
							<option>全部</option>
							</select>
					</td>
				</tr>
				<tr>			
					<td>
					工单受理时间:<input type="text" name="beginRangeTime"/>——<input type="text" name="endRangeTime"/>
					</td>

				
					<td>
						<input type="submit" value="查询"><input type="reset" value="重置"/>
					</td>
				</tr>
				
			</form>	
			<tr>
				<td>
					<button id="add_CUSA">客服结案</button>
					<button id="add_AdjudicateRetrial">结案重审</button>
					<button id="add_OrgVerify">机构核实</button><button id="add_OrgAppeal">机构申诉</button>
					<button>导出</button>
				</td>
			</tr>
		</table>		
		</div>	
	</div>	
	<div>
		<table border="1" width="100%" id="waitacceptDg">
			<tr>
				<th>工单号</th>
				<th>订单号</th>
				<th>工单类型</th>
				<th>工单状态</th>
				<th>来电人姓名</th>
				<th>来电号码</th>
				<th>被投诉机构</th>
				<th>工单受理人</th>
				<th>受理时间</th>
				<th>投诉一级分类</th>
				<th>投诉二级分类</th>
				<th>投诉处理结果</th>
				<th>是否扣罚</th>
				<th>客户名称</th>	
				<th>催件次数</th>			
			</tr>
		<%if(a!=null){ %>
			<%for(CsComplaintAccept c:a){ %>
			<tr onclick="getFomeV('<%=c.getAcceptNo() %>')">
				<td><%=c.getAcceptNo() %></td>
				<td><%=c.getOrderNo() %></td>
				<td><%=ComplaintTypeEnum.getByValue((long)c.getComplaintType()).getText()%></td>
				<td><%=CwbStateEnum.getByValue((long)c.getComplaintState()).getText() %></td>
				<%
				List<CsConsigneeInfo> csConsigneeList= maplist.get(c.getPhoneOne());
				if(csConsigneeList!=null||csConsigneeList.size()!=0){
					for(CsConsigneeInfo cc:csConsigneeList){ %>
					<td><%=cc.getName()==null?null:cc.getName()%></td>
					<%} 
					
				}%>
				<td><%=c.getPhoneOne() %></td>
				<%for(Branch br:b){ %>
					<%if(br.getBranchid()==c.getCodOrgId()){ %>
						<td><%=br.getBranchname()%></td>
				<%} }%>
				<td><%=uname%></td>
				<td><%=c.getAcceptTime() %></td>
				<td><%=c.getComplaintOneLevel() %></td>
				<td><%=c.getComplaintTwoLevel() %></td>
				<td><%=ComplaintResultEnum.getByValue((long)c.getComplaintResult()).getText()%></td>
				<td></td>
				 <%for(CwbOrder cod:co){ %>
		
				<td><%=cod.getConsigneename()%></td>
				<%} %> 
				<td></td>
			-

				
			</tr>
			<%} }%>
		</table>
	</div>	
	<input type="hidden" id="FormV" value=""/>
	<input type="hidden" id="add_OrgVerifyV" value="<%=request.getContextPath()%>/workorder/OrgVerify"/>
	<input type="hidden" id="add_CSA" value="<%=request.getContextPath()%>/workorder/CustomerServiceAdjudicate">	
</body>
</html>