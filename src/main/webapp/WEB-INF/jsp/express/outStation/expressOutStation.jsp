<%@page import="cn.explink.domain.CwbDetailView"%>
<%@page import="cn.explink.enumutil.switchs.SwitchEnum"%>
<%@page import="cn.explink.enumutil.CwbOrderPDAEnum,cn.explink.util.ServiceUtil"%>
<%@page import="cn.explink.domain.User,cn.explink.domain.Branch,cn.explink.domain.Truck,cn.explink.domain.Bale,cn.explink.domain.Switch"%>
<%@page import="cn.explink.domain.CwbOrder,cn.explink.domain.Customer"%>
<%@ include file="/WEB-INF/jsp/commonLib/easyui.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
List<Branch> bList = request.getAttribute("branchlist")==null?new ArrayList<Branch>():(List<Branch>)request.getAttribute("branchlist");
List<User> uList = request.getAttribute("userList")==null?new ArrayList<User>():(List<User>)request.getAttribute("userList");
List<Truck> tList = request.getAttribute("truckList")==null?new ArrayList<Truck>():(List<Truck>)request.getAttribute("truckList");
Map usermap = (Map) session.getAttribute("usermap");
String did=request.getParameter("branchid")==null?"0":request.getParameter("branchid");
long branchid=Long.parseLong(did);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽件出站</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

</head>
<body style="background:#f5f5f5" marginwidth="0" marginheight="0">
	<div class="saomiao_info2">
			<div class="saomiao_inbox2">
				<div class="saomiao_tab">
					<ul id="bigTag"></ul>
				</div>
				<div class="saomiao_righttitle2" id="pagemsg"></div>
				<div class="saomiao_selet2">
					<p>&nbsp;</p>
					下一站： <select id="nextBranch" name="nextBranch" class="select1">
							<%
								for(Branch b : bList){
							%>
							<%
								if(b.getBranchid()!=Long.parseLong(usermap.get("branchid").toString())){
							%>
							<option value="<%=b.getBranchid()%>"
								<%if(branchid==b.getBranchid()) {%> selected <%}%>><%=b.getBranchname()%></option>
							<%
								}}
							%>
						</select>
					<span style="margin-left: 10px;">&nbsp;</span>
					驾驶员：<select id="driverId" name="driverId" class="select1">
							<option value="-1" selected>请选择</option>
							<%for(User u : uList){ %>
								<option value="<%=u.getUserid() %>" ><%=u.getRealname() %></option>
							<%} %>
				        </select>
				    <span style="margin-left: 10px;">&nbsp;</span>
					车牌号：<select id="vehicleId" name="vehicleId" class="select1">
							<option value="-1" selected>请选择</option>
							<%for(Truck t : tList){ %>
								<option value="<%=t.getTruckid() %>" ><%=t.getTruckno() %></option>
							<%} %>
				        </select>
				    <span style="margin-left: 10px;">&nbsp;</span>
				 	车型：<input id="vehicleType" name="vehicleType" style="margin-top: -5px;" maxlength="50"/>
				 	<p>&nbsp;</p>
				</div>
				
				<!-- 扫描开始  -->
				<div class="saomiao_inwrith2">
					<!-- 左边输入框 -->
					<div class="saomiao_left2">
						<p>&nbsp;</p>
						<p>
							<span>运单号/包号：</span> 
							<input type="text" class="saomiao_inputtxt2" value="" id="scanNo" name="scanNo" />
						</p>
						<p>&nbsp;</p>
						<p>&nbsp;</p>
					</div>
					
					<!-- 右边的提示信息 -->
					<div class="saomiao_right2">
						<p>&nbsp;</p>
						<p id="msg" name="msg"></p><!-- 记录异常信息或成功信息总览 -->
						<p id="showScanNo_msg" name="showScanNo_msg"></p><!--订单号  -->
						<p id="destBranch_msg" name="destBranch_msg"></p><!-- 目的站 -->
						<p id="nextBranch_msg" name="nextBranch_msg"></p><!-- 下一站 -->
					</div>
			  </div>
		</div>
	</div>
	<script src="<%=request.getContextPath()%>/js/express/outStation/outStation.js" type="text/javascript"></script>
</body>
</html>
