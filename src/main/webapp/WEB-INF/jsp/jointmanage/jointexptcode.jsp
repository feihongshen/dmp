
<%@page import="cn.explink.b2c.tools.*"%>
<%@page import="cn.explink.b2c.tools.poscodeMapp.*"%>
<%@page import="cn.explink.pos.tools.*"%>
<%@page import="cn.explink.enumutil.*"%>

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
List<ExptCodeJoint> exptmatchlist =(List<ExptCodeJoint>)request.getAttribute("exptmatchlist");

int expt_type=Integer.parseInt(request.getAttribute("expt_type")!=null?request.getAttribute("expt_type").toString():"0");
List<Customer> customerlist =(List<Customer>)request.getAttribute("customerlist");
int supportkey=Integer.parseInt(request.getAttribute("support_key")!=null?request.getAttribute("support_key").toString():"0");

//Added by leoliao at 2016-07-01 解决POSEnum枚举类里面的编码与客户ID冲突
String support_key_selected = (request.getAttribute("support_key_selected")==null?"0" : request.getAttribute("support_key_selected").toString());
%>

<script type="text/javascript">
	function addInit() {
		//无处理
	}
	function addSuccess(data) {
		$("#alert_box input[type='text']", parent.document).val("");
		$("#alert_box select", parent.document).val(0);
		$("#searchForm").submit();
	}
	function editInit() {

	}
	function editSuccess(data) {
		$("#searchForm").submit();
	}
	function delSuccess(data) {
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
				<li><a
					href="<%=request.getContextPath()%>/jointManage/jointpos">POS对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/poscodemapp/1">POS/商户映射</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/exptreason/1">异常码设置</a></li>
				<li><a href="#" class="light">异常码关联</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/epaiApi/1">系统环形对接</a></li>
				<li><a
					href="<%=request.getContextPath()%>/jointManage/encodingsetting/1">供货商编码设置</a></li>
				<li><a href="<%=request.getContextPath()%>/jointManage/address/1">地址库同步</a></li>
			</ul>
		</div>
		<div class="right_box">
			<div class="inputselect_box">
				<form
					action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"
					method="post" id="searchForm">

					异常原因类型： <select name="expt_type" id="expt_type" class="select1">
						<option value="-1">全部</option>
						<option value="<%=ReasonTypeEnum.BeHelpUp.getValue()%>"
							<%if(expt_type==ReasonTypeEnum.BeHelpUp.getValue()){%> selected
							<%} %>><%=ReasonTypeEnum.BeHelpUp.getText()%></option>
						<option value="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"
							<%if(expt_type==ReasonTypeEnum.ReturnGoods.getValue()){%>
							selected <%} %>><%=ReasonTypeEnum.ReturnGoods.getText()%></option>
						<option value="<%=ReasonTypeEnum.WeiShuaKa.getValue()%>"
							<%if(expt_type==ReasonTypeEnum.WeiShuaKa.getValue()){%> selected
							<%} %>><%=ReasonTypeEnum.WeiShuaKa.getText()%></option>
						<option value="<%=ReasonTypeEnum.DiuShi.getValue()%>"
							<%if(expt_type==ReasonTypeEnum.DiuShi.getValue()){%> selected
							<%} %>><%=ReasonTypeEnum.DiuShi.getText()%></option>
					</select> 异常码提供方：<select name="support_key" id="support_key" class="select1">
						<option value="-1">全部</option>
						<%for(Customer en : customerlist){ %>
						<option value="1_<%=en.getCustomerid()%>"
							<%if(("1_"+en.getCustomerid()).equals(support_key_selected)){%> selected <%} %>><%=en.getCustomername() %></option>
						<%} %>
						<%for(PosEnum p : PosEnum.values()){ %>
						<option value="<%=p.getKey()%>" <%if(String.valueOf(p.getKey()).equals(support_key_selected)){%>
							selected <%} %>><%=p.getText() %></option>
						<%} %>
					</select> <input type="submit"
						onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/exptcodejoint/list/1');return true;"
						id="find" value="查询" class="input_button2" /><span><input
						name="" type="button" value="新建异常配对	" class="input_button1"
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
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">异常原因类型</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">异常原因内容</td>
						<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">对应提供方异常码及描述(提供方_异常码_描述)</td>
						<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
					</tr>
					<%if(exptmatchlist!=null&&exptmatchlist.size()>0){ 
					for(ExptCodeJoint exptcode:exptmatchlist){
						long reasontype=exptcode.getExpt_type();
						String expt_typename="";
						if(reasontype==ReasonTypeEnum.BeHelpUp.getValue()){
							expt_typename=ReasonTypeEnum.BeHelpUp.getText();
						}else if(reasontype==ReasonTypeEnum.ReturnGoods.getValue()){
							expt_typename=ReasonTypeEnum.ReturnGoods.getText();
						}else if(reasontype==ReasonTypeEnum.WeiShuaKa.getValue()){
							expt_typename=ReasonTypeEnum.WeiShuaKa.getText();
						}else if(reasontype==ReasonTypeEnum.DiuShi.getValue()){
							expt_typename=ReasonTypeEnum.DiuShi.getText();
						}
						String support="";
						long support_key=exptcode.getSupport_key();
						String exptsupportname="";
						if(exptcode.getCustomerid()==0){
							for(PosEnum b:PosEnum.values()){
								if(b.getKey()==exptcode.getSupport_key()){
									exptsupportname=b.getText();
								}
							}
						}else{ //b2c
							if(customerlist!=null&&customerlist.size()>0){
								for(Customer c:customerlist){
									if(c.getCustomerid()==exptcode.getCustomerid()){
										exptsupportname=c.getCustomername();
										break;
									}
								}
							}
						}
						
						String contents=exptsupportname+"__"+exptcode.getExpt_code()+"__"+exptcode.getExpt_msg();
				%>
					<tr>
						<td align="center" valign="middle"><%=exptcode.getExptcodeid()%></td>
						<td align="center" valign="middle"><%=expt_typename%></td>
						<td align="center" valign="middle"><%=exptcode.getReasoncontent()%>(<%=exptcode.getReasonid()%>)</td>
						<td align="center" valign="middle"><%=contents %></td>
						<td align="center" valign="middle">[<a
							href="javascript:edit_button('<%=exptcode.getExptcodeid() %>');">修改</a>]
							[<a
							href="javascript:if(confirm('删除后不可恢复,是否确定删除?'))del('<%=exptcode.getExptcodeid() %>');">删除</a>]
						</td>

					</tr>
					<%} }%>
				</table>
			</div>

		</div>

		</form>
		<div class="jg_10"></div>
		<div class="clear"></div>


		<!-- 创建常用于设置的ajax地址 -->
		<input type="hidden" id="add"
			value="<%=request.getContextPath()%>/exptcodejoint/add" />
		<!-- 修改常用于设置的ajax地址 -->
		<input type="hidden" id="edit"
			value="<%=request.getContextPath()%>/exptcodejoint/edit/" />
		<!-- 删除的ajax地址 -->
		<input type="hidden" id="del"
			value="<%=request.getContextPath()%>/exptcodejoint/del/" />
	</div>
	<script type="text/javascript">
		$("#selectPg").val(
	<%=request.getAttribute("page") %>
		);
	</script>

</body>
</html>
