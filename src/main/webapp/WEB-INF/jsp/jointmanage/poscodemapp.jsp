
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
List<PoscodeMapp> exptreasonlist =(List<PoscodeMapp>)request.getAttribute("poscodelist");
Page page_obj = (Page)request.getAttribute("page_obj");
int posenum=Integer.parseInt(request.getAttribute("posenum")!=null?request.getAttribute("posenum").toString():"0");
List<Customer> customerlist =(List<Customer>)request.getAttribute("customerlist");
 int customerid=Integer.parseInt(request.getAttribute("customerid")!=null?request.getAttribute("customerid").toString():"0");
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
				<li><a href="#" class="light">POS/商户映射</a></li>
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
		<div class="right_box">
			<div class="inputselect_box">
				<form
					action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>"
					method="post" id="searchForm">

					POS支付方：<select name="posenum" id="posenum" class="select1">
						<option value="-1">全部</option>
						<% for(PosEnum pe:PosEnum.values()){%>
						<option value="<%=pe.getKey()%>" <%if(posenum==pe.getKey()){%>
							selected <%}%>><%=pe.getText() %></option>
						<% }%>

					</select> <input type="submit"
						onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/poscodemapp/list/1');return true;"
						id="find" value="查询" class="input_button2" /> <span><input
						name="" type="button" value="新建编码" class="input_button1"
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
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">POS支付方</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">供货商编码</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">备注</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>

					</tr>
					<%if(exptreasonlist!=null&&exptreasonlist.size()>0){ 
					for(PoscodeMapp expt:exptreasonlist){
						String postext="";
				%>
					<tr>
						<td align="center" valign="middle"><%=expt.getPoscodeid() %></td>

						<%for(PosEnum p : PosEnum.values()){
			               if(expt.getPosenum()==p.getKey()){
			            	   postext=p.getText();
			            	   break;
			               }}
			               %>
						<%
			               String customername="";
			               for(Customer customer: customerlist){
			            	   if(customer.getCustomerid()==expt.getCustomerid()){
			            		   customername=customer.getCustomername();
			            		   break;
			            	   }
			               }
			               %>

						<td align="center" valign="middle"><%=postext%></td>
						<td align="center" valign="middle"><%=customername%></td>
						<td align="center" valign="middle"><%=expt.getCustomercode()==null?"":expt.getCustomercode()%></td>
						<td align="center" valign="middle"><%=expt.getRemark()%></td>

						<td align="center" valign="middle">[<a
							href="javascript:edit_button('<%=expt.getPoscodeid() %>');">修改</a>]
							[<a
							href="javascript:if(confirm('删除后不可恢复,是否确定删除?'))del('<%=expt.getPoscodeid() %>');">删除</a>]
						</td>
					</tr>
					<%} }%>
				</table>
			</div>

		</div>
		<%if(page_obj.getMaxpage()>1){ %>
		<div class="iframe_bottom">
			<table width="100%" border="0" cellspacing="1" cellpadding="0"
				class="table_1">
				<tr>
					<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
						<a
						href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();">第一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();">下一页</a>
						<a
						href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();">最后一页</a>
						共<%=page_obj.getMaxpage() %>页 共<%=page_obj.getTotal() %>条记录 当前第<select
						id="selectPg"
						onchange="$('#searchForm').attr('action',$(this).val());$('#searchForm').submit()">
							<%for(int i = 1 ; i <=page_obj.getMaxpage() ; i ++ ) {%>
							<option value="<%=i %>"><%=i %></option>
							<% } %>
					</select>页
					</td>
				</tr>
			</table>
		</div>
		<%}%>
		</form>
		<div class="jg_10"></div>
		<div class="clear"></div>


		<!-- 创建常用于设置的ajax地址 -->
		<input type="hidden" id="add"
			value="<%=request.getContextPath()%>/poscodemapp/add" />
		<!-- 修改常用于设置的ajax地址 -->
		<input type="hidden" id="edit"
			value="<%=request.getContextPath()%>/poscodemapp/edit/" />
		<!-- 删除的ajax地址 -->
		<input type="hidden" id="del"
			value="<%=request.getContextPath()%>/poscodemapp/del/" />
	</div>
	<script type="text/javascript">
		$("#selectPg").val(
	<%=request.getAttribute("page") %>
		);
	</script>

</body>
</html>
