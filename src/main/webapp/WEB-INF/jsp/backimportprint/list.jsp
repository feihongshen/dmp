<%@page import="cn.explink.domain.Reason"%>
<%@page import="cn.explink.print.template.PrintTemplate"%>
<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.util.Page"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.explink.controller.OperateSelectView"%>
<%@page import="cn.explink.enumutil.FlowOrderTypeEnum"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.controller.CwbOrderView"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Reason> backreasonList = (List<Reason>)request.getAttribute("backreasonList");
  List<Reason> reasonList = (List<Reason>)request.getAttribute("reasonList");
  List<PrintTemplate> templetelist = (List<PrintTemplate>)request.getAttribute("templete");
  List<Branch> branchList = (List<Branch>)request.getAttribute("branches");
  List<Customer> customerList = (List<Customer>)request.getAttribute("customerList");
  List<Backintowarehouse_print> backIntoprintList = (List<Backintowarehouse_print>)request.getAttribute("backIntoprintList");
  List<User> userList = (List<User>)request.getAttribute("userList");
  List<User> driverList = (List<User>)request.getAttribute("driverList");
  List branchArrlist =(List) request.getAttribute("branchArrlist");
  String  driverid =request.getAttribute("driverid").toString();
  String  branchids =request.getAttribute("branchids").toString();
  String  flag =request.getAttribute("flag").toString();
  String  begincredate =request.getAttribute("begincredate").toString();
  String  endcredate =request.getAttribute("endcredate").toString();
  String reasoncontent = request.getAttribute("reasoncontent").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退货站交接单打印</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>

<script>
	$(function() {
		$("#begincredate").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#endcredate").datetimepicker({
			changeMonth : true,
			changeYear : true,
			hourGrid : 4,
			minuteGrid : 10,
			timeFormat : 'hh:mm:ss',
			dateFormat : 'yy-mm-dd'
		});
		$("#branchid").multiSelect({
			oneOrMoreSelected : '*',
			noneSelected : '请选择'
		});

	});
</script>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#find").click(
						function() {
							if ($("#begincredate").val() == '' || $("#endcredate").val() == '') {
								alert("请选择时间段！");
								return false;
							}
							if (!Days() || ($("#begincredate").val() == '' && $("#endcredate").val() != '')
									|| ($("#begincredate").val() != '' && $("#endcredate").val() == '')) {
								alert("时间跨度不能大于10天！");
								return false;
							}
							if ($("#begincredate").val() > $("#endcredate").val()) {
								alert("开始时间不能大于结束时间！");
								return false;
							}
							$("#isshow").val(1);
							$("#searchForm").attr('action', '1');
							$("#searchForm").submit();
							/*     	$("#find").attr("disabled","disabled");
							 $("#find").val("请稍等.."); */
						});

			});

	function Days() {
		var day1 = $("#begincredate").val();
		var day2 = $("#endcredate").val();
		var y1, y2, m1, m2, d1, d2;//year, month, day;   
		day1 = new Date(Date.parse(day1.replace(/-/g, "/")));
		day2 = new Date(Date.parse(day2.replace(/-/g, "/")));
		y1 = day1.getFullYear();
		y2 = day2.getFullYear();
		m1 = parseInt(day1.getMonth()) + 1;
		m2 = parseInt(day2.getMonth()) + 1;
		d1 = day1.getDate();
		d2 = day2.getDate();
		var date1 = new Date(y1, m1, d1);
		var date2 = new Date(y2, m2, d2);
		var minsec = Date.parse(date2) - Date.parse(date1);
		var days = minsec / 1000 / 60 / 60 / 24;
		if (days > 10) {
			return false;
		}
		return true;
	}
	$(document).ready(function() {
		isshow(
<%=flag%>
	);
		if (!
<%=backIntoprintList.size()%>
	> 0) {
			$("#table1").remove();
			$("#print").remove();
		}
	});
	function isshow(flag) {
		if (flag == 1) {
			$("#history").attr('style', 'display:none');
			$("#no").attr('style', 'display:');
		}
		if (flag == 0) {
			$("#history").attr('style', 'display:');
			$("#no").attr('style', 'display:none');
		}
		$("#flag").val(flag);
	}
	function bdprint() {
		var isprint = "";
		$('input[name="isprint"]:checked').each(function() { //由于复选框一般选中的是多个,所以可以循环输出
			isprint += "'" + $(this).val() + "',";
		});
		$("#printid").val($("#printtype").val());
		if (isprint == "") {
			alert("请选择要打印的订单！");
			return false;
		} else if ($("#printtype").val() == '0') {
			alert("请选择打印模版！");
			return false;
		} else {
			$("#printForm").submit();
		}
	}
	function isgetallcheck() {
		if ($('input[name="isprint"]:checked').size() > 0) {
			$('input[name="isprint"]').each(function() {
				$(this).attr("checked", false);
			});
		} else {
			$('input[name="isprint"]').attr("checked", true);
		}
	}
</script>

</head>

<body style="background: #eef9ff">
	<div class="right_box">
		<div class="inputselect_box">
			<form action="1" method="post" id="searchForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="height: 30px">
					<tr>
						<td align="left">上一站: <select name="branchid" id="branchid" multiple="multiple"
							style="width: 220px;">
								<%
									if (branchList != null && branchList.size() > 0) {
								%>
								<%
									for (Branch b : branchList) {
								%>
								<option value="<%=b.getBranchid()%>"
									<%if (!branchArrlist.isEmpty()) {
						for (int i = 0; i < branchArrlist.size(); i++) {
							if (b.getBranchid() == new Long(branchArrlist.get(i).toString())) {%>
									selected="selected" <%break;
							}
						}
					}%>><%=b.getBranchname()%></option>
								<%
									}
									}
								%>
						</select> [<a href="javascript:multiSelectAll('branchid',1,'请选择');">全选</a>] [<a
							href="javascript:multiSelectAll('branchid',0,'请选择');">取消全选</a>] 操作时间： <input type="text"
							name="begincredate" id="begincredate"
							value="<%=request.getParameter("begincredate") == null ? "" : request.getParameter("begincredate")%>" />
							到 <input type="text" name="endcredate" id="endcredate"
							value="<%=request.getParameter("endcredate") == null ? "" : request.getParameter("endcredate")%>" />
							驾驶员: <select name="driverid" id="driverid">
								<option value="0">请选择</option>
								<%
									for (User driver : driverList) {
								%>
								<option value="<%=driver.getUserid()%>"
									<%if ((driver.getUserid() + "").equals(driverid)) {%> selected="selected" <%}%>><%=driver.getRealname()%></option>
								<%
									}
								%>
						</select> <input type="hidden" name="isshow" id="isshow" value="1" /> 
						备注：<select id="comment" name="comment">
								 <option value="">请选择</option>
								 <%for(Reason r:backreasonList){ %>
									 <option value="<%=r.getReasoncontent()%>"
									 	<%if (r.getReasoncontent().equals(reasoncontent)) {%> selected="selected" <%}%>
									 	>
									 	<%=r.getReasoncontent()%>
									 </option>
								 <%} %>
						 	</select>
						<input type="button" id="find" value="查询" class="input_button2" /> 
						打印模版:<select id="printtype" name="printtype">
								<option value="0">请选择</option>
								<%
									for (PrintTemplate pt : templetelist) {
								%>
								<option value="<%=pt.getId()%>"><%=pt.getName()%></option>
								<%
									}
								%>
						</select> <input type="button" id="print" value="打印" onclick="bdprint()" class="input_button2" /> <input
							name="flag" id="flag" value="<%=flag%>" type="hidden" /> <a id="history"
							href="javascript:isshow(1)">>>历史打印列表</a> <a id="no" href="javascript:isshow(0)">>>返回未打印打印列表</a>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<br></br>
		<form action="<%=request.getContextPath()%>/backimportprint/print" id="printForm">
			<div class="right_title" style="overflow: auto; height: 100%; margin-top: 10px">
				<table id="table1" width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
					<tr class="font_1">
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">序号
						<a style="cursor: pointer;" onclick="isgetallcheck();">（全选）</a></td>
						<td align="center" valign="middle" bgcolor="#eef6ff">订单号</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">运单号</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">上一站</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">扫描人</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">退货站入库时间</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">退货原因</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">退货站入库备注</td>
						<td align="center" valign="middle" bgcolor="#eef6ff">司机</td>
					</tr>
					<%
						int i = 0;
						for (Backintowarehouse_print pv : backIntoprintList) {
							i++;
					%>
					<tr>
						<td width="10%" align="center" valign="middle"><input id="isprint" name="isprint"
							type="checkbox" value="<%=pv.getCwb()%>" checked="checked" /><%=i%></td>
						<td align="center" valign="middle"><%=pv.getCwb()%></td>
						<td align="center" valign="middle"><%=pv.getTranscwb()%></td>
						<td align="center" valign="middle">
							<%
								for (Customer c : customerList) {
										if (c.getCustomerid() == pv.getCustomerid())
											out.print(c.getCustomername());
									}
							%>
						</td>
						<td align="center" valign="middle">
							<%
								for (Branch b : branchList) {
										if (b.getBranchid() == pv.getStartbranchid())
											out.print(b.getBranchname());
									}
							%>
						</td>
						<td align="center" valign="middle">
							<%
								for (User u : userList) {
										if (u.getUserid() == pv.getUserid())
											out.print(u.getRealname());
									}
							%>
						</td>
						<td align="center" valign="middle"><%=pv.getCreatetime()%></td>
						<td align="center" valign="middle">
							<%
								for (Reason r : reasonList) {
									if (r.getReasonid() == pv.getBackreasonid())
										out.print(r.getReasoncontent());
								}
							%>
						</td>
						<td align="center" valign="middle">
							<%
							/* if(reasoncontent.equals(pv.getBreasonremark())){
								out.print(reasoncontent);	
							}  */
							for (Reason r : backreasonList) {
								if (r.getReasoncontent().equals(pv.getBreasonremark()))
								out.print(r.getReasoncontent());
							}
							%>
						</td>
						<td align="center" valign="middle">
							<%
								for (User u : userList) {
										if (u.getUserid() == pv.getDriverid())
											out.print(u.getRealname());
									}
							%>
						</td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
			<input type="hidden" value="<%=begincredate%>" name="starttime" id="starttime" /> 
			<input type="hidden" value="<%=endcredate%>" name="endtime" id="endtime" />
			<input type="hidden" value="<%=branchids%>" name="branchids" id="branchids" /> 
			<input type="hidden" value="<%=driverid%>" name="driverid" id="driverid" /> 
			<input type="hidden" value="<%=flag%>" name="flag" id="flag" />
			<input type="hidden"  name="printid" id="printid"/>
		</form>
	</div>
</body>
</html>

