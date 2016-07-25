<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.b2c.explink.core_down.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.domain.Customer,cn.explink.util.Page"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/js.js"></script>

<%
	List<EpaiApi> epailist = (List<EpaiApi>) request
			.getAttribute("epailist");
	List<Customer> customerlist = (List<Customer>) request
			.getAttribute("customerlist");
	List<Branch> warehouselist = (List<Branch>) request
			.getAttribute("warehouselist");
%>

<script type="text/javascript">
function addInit() {
		//无处理
	}
	function
	addSuccess(data) {
		$("#alert_box input[type='text']"
	, parent.document).val("");
		$("#alert_box select", parent.document).val(0);
		$("#searchForm").submit();
	}
	function
	editInit() {

	}
	function
	editSuccess(data) {
		$("#searchForm").submit();
	}
	function
	delSuccess(data) {
		$("#searchForm").submit();
	}

	function
	delsubmit(b2cid) {
		var pwd=prompt( "请输入密码:", "");
		if (pwd !=null)
	{ //有值 var parms=b2cid + "/" + pwd;
			$.ajax({
				type
	: "POST",
				url : $("#del").val() + parms,
				dataType
	: "json",
				success :
	function(data) {
					//alert(data.error);
					if (data.errorCode==0){delSuccess(data);
					}
	}
 		});
 	};
}


</script>
</head>
<body style="background: #f5f5f5">



	<div class="menucontant">
		<div class="uc_midbg">
			<ul>
				<li><a href="<%=request.getContextPath()%>/jointpower/">对接权限设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointb2c">电商对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointpos">POS对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1">异常码关联</a></li>
				<li><a href="#" class="light">系统环形对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/address/1">地址库同步</a></li>
			</ul>
		</div>
		<div class="right_box">
			<div class="inputselect_box">
				<form action="" method="post" id="searchForm">

					上游电商：<select name="customerid" id="customerid" class="select1">
						<option value="-1">全部</option>
						<%
							for (Customer cu : customerlist) {
						%>
						<option value="<%=cu.getCustomerid()%>"><%=cu.getCustomername()%></option>
						<%
							}
						%>

					</select> <input type="submit"
						onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/epaiApi/list');return true;"
						id="find" value="查询" class="input_button2" /> <span><input
						name="" type="button" value="新建设置" class="input_button1"
						id="add_button" /></span>
			</div>
			<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0"
					class="table_2" id="gd_table">
					<tr class="font_1">
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">用户编码</td>
						<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">指定供货商</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">订单下载URL</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">下载完成回调URL</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">每次下载数量</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">状态回传URL</td>
						<td width="6%" align="center" valign="middle" bgcolor="#eef6ff">订单导入库房</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">是否开启（下载）</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">是否开启（回传）</td>
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>

					</tr>
					<%
						if (epailist != null && epailist.size() > 0) {
							for (EpaiApi expt : epailist) {
								String postext = "";
					%>
					<tr>
						<td align="center" valign="middle"><%=expt.getB2cid()%></td>
						<%
							String customername = "";
									for (Customer customer : customerlist) {
										if (customer.getCustomerid() == expt.getCustomerid()) {
											customername = customer.getCustomername();
											break;
										}
									}
									String branchname = "";
									for (Branch branch : warehouselist) {
										if (branch.getBranchid() == expt.getWarehouseid()) {
											branchname = branch.getBranchname();
											break;
										}
									}
						%>
						<td align="center" valign="middle"><%=expt.getUserCode()%></td>
						<td align="center" valign="middle"><%=customername%></td>
						<td align="center" valign="middle"><%=expt.getGetOrder_url()%></td>
						<td align="center" valign="middle"><%=expt.getCallBack_url()%></td>
						<td align="center" valign="middle"><%=expt.getPageSize()%></td>
						<td align="center" valign="middle"><%=expt.getFeedback_url()%></td>
						<td align="center" valign="middle"><%=branchname%></td>
						<td align="center" valign="middle"><%=expt.getIsopenflag() == 1 ? "开启" : "关闭"%></td>
						<td align="center" valign="middle"><%=expt.getIsfeedbackflag() == 1 ? "开启" : "关闭"%></td>

						<td align="center" valign="middle">[<a
							href="javascript:edit_button('<%=expt.getB2cid()%>');">修改</a>] [<a
							href="javascript:delsubmit('<%=expt.getB2cid()%>');">删除</a>]
						</td>
					</tr>
					<%
						}
						}
					%>
				</table>
			</div>

		</div>
		</form>
		<div class="jg_10"></div>
		<div class="clear"></div>


		<!-- 创建常用于设置的ajax地址 -->
		<input type="hidden" id="add"
			value="<%=request.getContextPath()%>/epaiApi/add" />
		<!-- 修改常用于设置的ajax地址 -->
		<input type="hidden" id="edit"
			value="<%=request.getContextPath()%>/epaiApi/edit/" />
		<!-- 删除的ajax地址 -->
		<input type="hidden" id="del"
			value="<%=request.getContextPath()%>/epaiApi/del/" />
	</div>


</body>
</html>
