
<%@page import="cn.explink.domain.Common"%>
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
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>


<%
List<CommonJoint> exptmatchlist =(List<CommonJoint>)request.getAttribute("exptmatchlist");
Page page_obj = (Page)request.getAttribute("page_obj");
int expt_type=Integer.parseInt(request.getAttribute("expt_type")!=null?request.getAttribute("expt_type").toString():"0");
List<Common> customerlist =(List<Common>)request.getAttribute("commonlist");
int supportkey=Integer.parseInt(request.getAttribute("support_key")!=null?request.getAttribute("support_key").toString():"0");
%>

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
<body style="background:#eef9ff">

	

<div class="menucontant">
<div class="uc_midbg">
		<ul>
			<li><a href="<%=request.getContextPath()%>/jointManage/jointpos" >POS对接</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/jointb2c" >B2C对接</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/exptreason/1" >异常码设置</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/exptcodejoint/1" >异常码关联</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/getchengyunshang/1" >异常码设置-承</a></li>
			<li><a href="#"  class="light">异常码关联-承</a></li>
			<li><a href="<%=request.getContextPath()%>/jointpower/" >对接权限设置</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/poscodemapp/1"  >POS/商户映射</a></li>
			<li><a href="<%=request.getContextPath()%>/jointManage/epaiApi/1"  >e派系统对接</a></li>
		</ul>
	</div>
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
	
		 请选择异常原因类型：
		 			<select name ="expt_type" id="expt_type">
		               <option value ="-1">全部</option>
		               <option value="<%=ReasonTypeEnum.BeHelpUp.getValue()%>" <%if(expt_type==ReasonTypeEnum.BeHelpUp.getValue()){%>selected<%} %>><%=ReasonTypeEnum.BeHelpUp.getText()%></option>
		           	   <option value="<%=ReasonTypeEnum.ReturnGoods.getValue()%>"  <%if(expt_type==ReasonTypeEnum.ReturnGoods.getValue()){%>selected<%} %>><%=ReasonTypeEnum.ReturnGoods.getText()%></option>
		           	   <option value="<%=ReasonTypeEnum.WeiShuaKa.getValue()%>"  <%if(expt_type==ReasonTypeEnum.WeiShuaKa.getValue()){%>selected<%} %>><%=ReasonTypeEnum.WeiShuaKa.getText()%></option>
		           	   <option value="<%=ReasonTypeEnum.DiuShi.getValue()%>"  <%if(expt_type==ReasonTypeEnum.DiuShi.getValue()){%>selected<%} %>><%=ReasonTypeEnum.DiuShi.getText()%></option>
		           </select>
		 请选择异常码提供方：<select name ="support_key" id="support_key">
		               <option value ="-1">全部</option>
		              <%for(Common en : customerlist){ %>
		               <option value ="<%=en.getId()%>" <%if(en.getId()==supportkey){%>selected<%} %>><%=en.getCommonname()%></option>
		               <%} %>
		               
		           </select>
		 
		 <input type="submit"  onclick="$('#searchForm').attr('action','<%=request.getContextPath()%>/Commreason/show/1');return true;"  id="find" value="查询" class="input_button2" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><input name="" type="button" value="新建异常配对	" class="input_button1"  id="add_button"  /></span>
<div class="right_box">
				<div class="right_title">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
						<td width="5%" align="center" valign="middle" bgcolor="#eef6ff">序号</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">异常原因类型</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">异常原因内容</td>
						<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">对应提供方异常码及描述(提供方_异常码_描述)</td>
						<td width="50%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
					</tr>
					<%long pageOne=(Long)request.getAttribute("page"); %>
				<%if(exptmatchlist!=null&&exptmatchlist.size()>0){ 
					long i=1; 
					if(pageOne>1){
						i=i+10*(pageOne-1);
					}
					for(CommonJoint exptcode:exptmatchlist){
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
						String exptsupportname="";
						if(customerlist!=null&&customerlist.size()>0){
							for(Common c:customerlist){
								if(c.getId()==exptcode.getExptid()){
									exptsupportname=c.getCommonname();
								}
							}
						}
						String contents=exptcode.getCommonname()+"__"+exptcode.getExpt_code()+"__"+exptcode.getExpt_msg();
				%>
					<tr>
					 	<td  align="center" valign="middle"><%=i%></td>
					 	<td  align="center" valign="middle"><%=expt_typename%></td>
					 	<td  align="center" valign="middle"><%=exptcode.getReasoncontent()%>(<%=exptcode.getReasonid()%>)</td>
					 	<td  align="center" valign="middle"><%=contents %></td>
					 	<td  align="center" valign="middle" >
							[<a href="javascript:edit_button('<%=exptcode.getExptcodeid() %>');">修改</a>]
							[<a href="javascript:if(confirm('删除后不可恢复,是否确定删除?'))del('<%=exptcode.getExptcodeid() %>');">删除</a>]
						</td>
					 	
					</tr><%i++; %>
				<%} }%>
				</table>
				</div>
				
			</div>
		<%if(page_obj.getMaxpage()>1){ %>
	<div class="iframe_bottom">
		<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_1">
		<tr>
			<td height="38" align="center" valign="middle" bgcolor="#eef6ff">
				<a href="javascript:$('#searchForm').attr('action','1');$('#searchForm').submit();" >第一页</a>　
				<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getPrevious()<1?1:page_obj.getPrevious() %>');$('#searchForm').submit();">上一页</a>　
				<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getNext()<1?1:page_obj.getNext() %>');$('#searchForm').submit();" >下一页</a>　
				<a href="javascript:$('#searchForm').attr('action','<%=page_obj.getMaxpage()<1?1:page_obj.getMaxpage() %>');$('#searchForm').submit();" >最后一页</a>
				　共<%=page_obj.getMaxpage() %>页　共<%=page_obj.getTotal() %>条记录 　当前第<select
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
	<%} %>
			</form>
	<div class="jg_10"></div>
	<div class="clear"></div>


<!-- 创建常用于设置的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/Commreason/addjoint/" />
<!-- 修改常用于设置的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/Commreason/editjoint/" />
<!-- 删除的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/Commreason/deljoint/" />
</div>
<script type="text/javascript">
		$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>

</body>
</html>
