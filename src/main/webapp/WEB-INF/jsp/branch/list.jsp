
<%@page import="cn.explink.util.Page"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.enumutil.BranchEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  List<Branch> branches = (List<Branch>)request.getAttribute("branches");
  Page page_obj = (Page)request.getAttribute("page_obj");
  long roleid = request.getAttribute("roleid") == null ? -1 : (Long)request.getAttribute("roleid");
%>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>机构管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jq-plugin/jquery.form.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript">
function thisCheck(){
	$("#branchname", parent.document).change(function(event){
		if($("#branchname", parent.document).val().length>0){
		$.ajax({type: "POST",
			url:"<%=request.getContextPath()%>/branch/branchnamecheck",
				data : {branchname : $("#branchname", parent.document).val()},
				success : function(data) {
					if (data == false) {
						alert("机构名称已存在");
					} else {
						//alert("机构名称可用");
					}
				}
			});
		}
	});
	$("#sitetype", parent.document).change(function(){//监控机构类型变化 对显示字段做相应处理
		var sitetype =  $("#sitetype", parent.document).val();
		init_branch();
		//库房 中转站 退货站 其他
		if(sitetype==<%=BranchEnum.ZhanDian.getValue()%>){//站点
			zhandianObj();
		}else if(sitetype==<%=BranchEnum.YunYing.getValue()%>){//运营
			yunyingObj();
		}else if(sitetype==<%=BranchEnum.KeFu.getValue()%>){//客服
			kefuObj();
		}else if(sitetype==<%=BranchEnum.CaiWu.getValue()%>){//财务
			caiwuObj();
		}else{
			qitaObj();
		}
	});
	
}
function addInit(){
	thisCheck();
	window.parent.init_branch();
	//window.parent.uploadFormInit("branch_cre_Form");
}
function addSuccess(data){
	thisCheck();
	window.parent.init_branch();
	$("#alert_box input[type='text']" , parent.document).val("");
	$("#sitetype", parent.document).val(-1);
	$("#accountarea", parent.document).val(0);
	$("#remandtype", parent.document).val(0);
	$("#searchForm").submit();
}
function editInit(){
    
	for(var i =0 ; i < initEditArray.length ; i ++){
		var value = initEditArray[i].split(",")[0];
		var name = initEditArray[i].split(",")[1];
		$("#"+name, parent.document).val(value);
	}
	thisCheck();
	window.parent.init_branch();
	window.parent.click_sitetype(<%=BranchEnum.ZhanDian.getValue()%>,<%=BranchEnum.YunYing.getValue()%>,<%=BranchEnum.KeFu.getValue()%>,<%=BranchEnum.CaiWu.getValue()%>);
	//window.parent.uploadFormInit("branch_save_Form");
	 if(initBranchList){
		for(var i=0 ; i<initBranchList.length ; i++){
			window.parent.initBranch(initBranchList[i]);
		}
	}
	 
		var sitetype = parent.window.$("#sitetype").val();
		if ('<%=BranchEnum.ZhanDian.getValue()%>' == sitetype) { //机构类型为站点
				//点击触发切换效果
				parent.window.$("input[name='payMethodType']:checked").click();
		}
	 	
}
function editSuccess(data){
	window.parent.closeBox();
	$("#searchForm").submit();
}
function delSuccess(data){
	$("#searchForm").submit();
}
$(function() {
	$("#sync_button").click(function() {
		if(!confirm("确定同步站点机构？")){
			return;
		}
		$("#exportForm").submit();
		alert("正在同步中，请稍等几分钟。同步结果将输出到excel文件中.");
	});
});
</script>
</head>

<body style="background:#f5f5f5">

<div class="right_box">
	<div class="inputselect_box">
	    <!-- 只有管理员才可以同步机构 -->
	    <% if(roleid == 0){%>
		<span>
			<form action="<%=request.getContextPath()%>/branch/syncAllBranchToOsp" method="post" id="exportForm">
				<!-- <input name="" type="button" value="同步机构" class="input_button1"  id="sync_button"  /> -->
			</form>
		</span>
		<%} %>
		<span><input name="" type="button" value="创建机构" class="input_button1"  id="add_button"  />
		</span>
		<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
			机构名称：<input type ="text" name ="branchname"  class="input_text1" value = "<%=request.getParameter("branchname")==null?"":request.getParameter("branchname") %>"/>
			机构地址：<input type ="text" name ="branchaddress"  class="input_text1" value = "<%=request.getParameter("branchaddress")==null?"":request.getParameter("branchaddress") %>"/>
			<input type="submit" id="find" onclick="$('#searchForm').attr('action',1);return true;" value="查询"  class="input_button2" />
			<input type="button"  onclick="location.href='1'" value="返回" class="input_button2" />
		</form>
	</div>
				<div class="right_title">
				<div class="jg_10"></div>
				<div class="jg_10"></div>
				<div class="jg_10"></div>

				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				<tr class="font_1">
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">机构名称</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">分拣码</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构编码</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">地址</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">联系人</td>
						<td width="15%" align="center" valign="middle" bgcolor="#eef6ff">电话</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">手机</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">机构类型</td>
						<td width="10%" align="center" valign="middle" bgcolor="#eef6ff">操作</td>
						
					</tr>
					  <% for(Branch b : branches){ %>
					<tr>
					 	<td width="15%" align="center" valign="middle"><%=b.getBranchname() %></td>
					 	<td width="10%" align="center" valign="middle"><%=b.getBranchcode() %></td>
					 	<td width="10%" align="center" valign="middle"><%=b.getTpsbranchcode() %></td>
						<td width="10%" align="left" valign="middle"><%=b.getBranchaddress() %></td>
						<td width="10%" align="center" valign="middle"><%=b.getBranchcontactman() %></td>
						<td width="15%" align="center" valign="middle"><%=b.getBranchphone() %></td>
						<td width="10%" align="center" valign="middle"><%=b.getBranchmobile() %></td>
						<td width="10%" align="center" valign="middle"><%=b.getSitetypeName() %></td>
						
						<td width="10%" align="center" valign="middle" >
						<%-- [<a href="javascript:if(confirm('确定要删除?')){del(<%=b.getBranchid()%>);}">删除</a>] --%>
						[<a href="javascript:del(<%=b.getBranchid() %>);"><%=(b.getBrancheffectflag().equals("1")?"停用":"启用") %></a>]
						[<a href="javascript:edit_button(<%=b.getBranchid()%>);">修改</a>]
						</td>
					</tr>
					<%} %>
				</table>
				<div class="jg_10"></div>
				<div class="jg_10"></div>
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
			</div>
			
	<div class="jg_10"></div>
	<div class="clear"></div>

<script type="text/javascript">
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
<!-- 创建的ajax地址 -->
<input type="hidden" id="add" value="<%=request.getContextPath()%>/branch/add" />
<!-- 修改的ajax地址 -->
<input type="hidden" id="edit" value="<%=request.getContextPath()%>/branch/edit/" />
<!-- 删除的ajax地址 -->
<input type="hidden" id="del" value="<%=request.getContextPath()%>/branch/del/" />
</body>
</html>
