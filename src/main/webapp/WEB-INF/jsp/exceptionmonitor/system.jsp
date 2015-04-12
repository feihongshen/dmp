<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch,cn.explink.domain.OperationSetTime"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branchList = request.getAttribute("branchList")==null?null:(List<Branch>)request.getAttribute("branchList");
  List<OperationSetTime> oList = (List<OperationSetTime>)request.getAttribute("oList");
  long sitetype=Long.parseLong(request.getParameter("sitetype")==null?"0":request.getParameter("sitetype").toString());
  String[] branchids=request.getParameterValues("branchids")==null?null:request.getParameterValues("branchids");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>超期异常时效设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/js.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'请选择机构类型' });
});

//停用启用
function del(id){
	 $.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/ExceptionMonitor/updatestate",
		data:{id:id},
		dataType:"json",
		success : function(data) {  
			location.href="<%=request.getContextPath()%>/ExceptionMonitor/system";
		}
	}); 
}

function edit(id){
	location.href="<%=request.getContextPath()%>/ExceptionMonitor/add?id="+id;
}

//改变机构类型
function changeSitetype(){
	$("#searchForm").submit();
}

</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
		<span><input name="" type="button" value="设置监控时长" class="input_button1" onclick="location='<%=request.getContextPath()%>/ExceptionMonitor/add'" />
		</span>
		<form action="" method="post" id="searchForm">
			超期异常名称
			<input type ="text" name ="modelname"  class="input_text1" value = "<%=request.getParameter("modelname")==null?"":request.getParameter("modelname") %>"/>
			机构类型
			<select name="sitetype" id="sitetype" class="select1" onchange="changeSitetype()">
	            <option value="0">全部</option>
	            <option value="<%=BranchEnum.KuFang.getValue()%>" <%=BranchEnum.KuFang.getValue()==sitetype?"selected":"" %>><%=BranchEnum.KuFang.getText()%></option>
	            <option value="<%=BranchEnum.ZhanDian.getValue()%>" <%=BranchEnum.ZhanDian.getValue()==sitetype?"selected":"" %>><%=BranchEnum.ZhanDian.getText()%></option>
	            <option value="<%=BranchEnum.ZhongZhuan.getValue()%>" <%=BranchEnum.ZhongZhuan.getValue()==sitetype?"selected":"" %>><%=BranchEnum.ZhongZhuan.getText()%></option>
	            <option value="<%=BranchEnum.TuiHuo.getValue()%>" <%=BranchEnum.TuiHuo.getValue()==sitetype?"selected":"" %>><%=BranchEnum.TuiHuo.getText()%></option>
           	</select>
           	机构名称
			<select name ="branchids" id ="branchids"  multiple="multiple" style="width:300px;">
	         <%if(branchList!=null&&!branchList.isEmpty()){
	         	for(Branch b : branchList){ %>
	          	<option value ="<%=b.getBranchid()%>" 
	          	<%if(branchids != null &&  branchids.length>0){
					for (String branchid : branchids) {
						if(b.getBranchid()==Long.parseLong(branchid)){
	          				out.print("selected=selected'");
	          			}
					}
				 }%>><%=b.getBranchname()%></option>
	          <%}}%>
			 </select>
			 [<a href="javascript:multiSelectAll('branchids',1,'请选择');">全选</a>]
			 [<a href="javascript:multiSelectAll('branchids',0,'请选择');">取消全选</a>]
			<input type="submit" id="find" value="查询"  class="input_button2" />
		</form>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
					<tr class="font_1">
						<td width="20%" align="center" valign="middle" bgcolor="#eef6ff">超期异常名称</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构类型</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
						<td width="40%" align="center" valign="middle" bgcolor="#eef6ff">超期异常时效</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
					</tr>
					<%if(oList!=null&&!oList.isEmpty()){
						for(int i=0;i<oList.size();i++){
							OperationSetTime o=oList.get(i);
					%>
						<tr>
							<td width="20%" align="center" valign="middle"><%=o.getName()%></td>
							<td width="10%" align="center" valign="middle"><%=o.getSitetypename()%></td>
							<td width="10%" align="center" valign="middle"><%=o.getBranchname()%></td>
							<td width="40%" align="center" valign="middle"><%=o.getValue()%></td>
							<td width="10%" align="center" valign="middle">
								[<a href="javascript:del(<%=o.getId()%>);"><%=(o.getState()==1?"停用":"启用") %></a>]
								[<a href="javascript:edit(<%=o.getId()%>);">修改</a>]
							</td>
						</tr>
					<%}}%>
					
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				</div>
			</div>
			
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>
