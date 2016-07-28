
<%@page import="java.util.List"%>
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	List<JointEntity> posList = (List<JointEntity>) request
			.getAttribute("posList");
%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css"
	type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script src="<%=request.getContextPath()%>/js/js.js"
	type="text/javascript"></script>
<script type="text/javascript">
function addInit(){
	//无处理
}
function addSuccess(data){
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#alert_box select", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){

}
function editSuccess(data){
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
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
				<li><a href="#" class="light">POS对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1">异常码关联</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/epaiApi/1">系统环形对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/address/1">地址库同步</a></li>
			</ul>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>



				<form id="searchForm"
					action="<%=request.getContextPath()%>/jointManage/jointpos"
					method="post">
					<div class="right_box">
						<div class="right_title">
							<table width="100%" border="0" cellspacing="1" cellpadding="0"
								class="table_2" id="gd_table">
								<tr class="font_1">
									<td width="60%" align="center" valign="middle"
										bgcolor="#eef6ff">POS公司名称</td>
									<td width="40%" align="center" valign="middle"
										bgcolor="#eef6ff">操作</td>

								</tr>
								<%
									for (PosEnum em : PosEnum.values()) {
										String text = em.getText();
										int key = em.getKey();
								%>
								<tr>
									<td width="60%" align="center" valign="middle"><%=text%></td>
									<td width="40%" align="center" valign="middle">
										<%
											if (posList != null && posList.size() > 0) {
										%> <%
 	for (JointEntity jion : posList) {
 %>
										<%
											if (jion.getJoint_num() == key) {
										%> [<a
										href="javascript:changeUrl('<%=key%>',<%=(jion.getState() == 1 ? 0 : 1)%>);del('<%=key%>');"><font
											id="<%=key%>"><%=(jion.getState() == 1 ? "停用" : "启用")%></font></a>] <%
											}
													}
												}
										%> [<a
										href="javascript:changeUrl('<%=key%>','');edit_button('<%=key%>');">设置</a>]
									</td>
								</tr>
								<%
									}
								%>
							</table>
						</div>
					</div>
				</form>
				</td>
			</tr>
		</table>
		<!-- 创建常用于设置的ajax地址 -->
		<input type="hidden" id="add"
			value="<%=request.getContextPath()%>/alipay/edit/" />
		<!-- 修改常用于设置的ajax地址 -->
		<script>
	function changeUrl(obj,state){
		
		if(obj=='1001'){
			$("#edit").val('<%=request.getContextPath()%>/alipay/edit/');
			$("#del").val('<%=request.getContextPath()%>/alipay/del/'+state+'/');
		}else if(obj=='1002'){
			$("#edit").val('<%=request.getContextPath()%>/yeepay/edit/');
			$("#del").val('<%=request.getContextPath()%>/yeepay/del/'+state+'/');
		}else if(obj=='1003'){
			$("#edit").val('<%=request.getContextPath()%>/UnionPay/edit/');
			$("#del").val('<%=request.getContextPath()%>/UnionPay/del/'+state+'/');
		}else if(obj=='1004'){
			$("#edit").val('<%=request.getContextPath()%>/bill99/edit/');
			$("#del").val('<%=request.getContextPath()%>/bill99/del/'+state+'/');
		}else if(obj=='1005'){
			$("#edit").val('<%=request.getContextPath()%>/unionpay/edit/');
			$("#del").val('<%=request.getContextPath()%>/unionpay/del/'+state+'/');
		}else if(obj=='1006'){
			$("#edit").val('<%=request.getContextPath()%>/chinaums/edit/');
			$("#del").val('<%=request.getContextPath()%>/chinaums/del/'+state+'/');
		}else if(obj=='1007'){
			$("#edit").val('<%=request.getContextPath()%>/alipaycodapp/edit/');
			$("#del").val('<%=request.getContextPath()%>/alipaycodapp/del/'+state+'/');
		}
		else if(obj=='1008'){
			$("#edit").val('<%=request.getContextPath()%>/etong/edit/');
			$("#del").val('<%=request.getContextPath()%>/etong/del/'+state+'/');
		}
		else if(obj=='1009'){
			$("#edit").val('<%=request.getContextPath()%>/Yalian_go/edit/');
			$("#del").val('<%=request.getContextPath()%>/Yalian_go/del/'+state+'/');
		}
		else if(obj=='1010'){
			$("#edit").val('<%=request.getContextPath()%>/mobiledcb/edit/');
			$("#del").val('<%=request.getContextPath()%>/mobiledcb/del/'+state+'/');
		}
		else if(obj=='1011'){
			$("#edit").val('<%=request.getContextPath()%>/alipayapp/edit/');
			$("#del").val('<%=request.getContextPath()%>/alipayapp/del/'+state+'/');
		}
		else if(obj=='1012'){
			$("#edit").val('<%=request.getContextPath()%>/tlmpos/edit/');
			$("#del").val('<%=request.getContextPath()%>/tlmpos/del/' + state + '/');
		} 
		else if (obj =='1013'){
			$("#edit").val('<%=request.getContextPath()%>/weisuda/edit/');
			$("#del").val('<%=request.getContextPath()%>/weisuda/del/'+state+'/');
		}
		else if(obj=='1015'){
			$('#edit').val('<%=request.getContextPath()%>/deliverServer/show/');
			$('#del').val('<%=request.getContextPath()%>/deliverServer/del/'+state+'/');
		}
		else if(obj=='1020'){
			$('#edit').val('<%=request.getContextPath()%>/bjunion/show/');
			$('#del').val('<%=request.getContextPath()%>/bjunion/del/'+state+'/');
		}
	}
</script>


		<input type="hidden" id="edit"
			value="<%=request.getContextPath()%>/alipay/edit/" /> <input
			type="hidden" id="del"
			value="<%=request.getContextPath()%>/alipay/edit/" />
	</div>

</body>
</html>
