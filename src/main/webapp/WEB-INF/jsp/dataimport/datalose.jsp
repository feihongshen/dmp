<%@page import="cn.explink.domain.EmailDate"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	List<EmailDate> emaildatelist = (List<EmailDate>) request.getAttribute("emaildateList");
    String emaildateidParam = request.getParameter("emaildate")==null?"":request.getParameter("emaildate");
%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script>
function checkSel(){
	if($("#emaildate").val().length==0){
		alert("未选择发货批次!");
		return false;
	}
	return true;
}
</script>
</head>
<body  style="background:#eef9ff">

<div class="right_box">
	<div class="menucontant">
	<div class="uc_midbg">
		<ul>
			<li><a href="excelimportPage" >导入数据</a></li>
			<li><a href="reexcelimportPage" >导入查询</a></li>
			<!-- <li><a href="batchedit">批量修改</a></li> -->
			<li><a href="#" class="light">数据失效</a></li>
			<li><a href="editBranch" >修改匹配站</a></li>
			<li><a href="editBranchonBranch" >匹配站按站</a></li>
			<li><a href="editBatchBranch" >批量匹配站</a></li>
			<li><a href="reproducttranscwb" >运单号生成</a></li>
			<li><font color="red">地址库已开启</font></li>
		</ul>
	</div>
	<form name="uploadForm" id="uploadForm" method="POST" action="datalose" onsubmit="return checkSel()" enctype="multipart/form-data" >
			<table width="100%" height="23" border="0" cellpadding="0" cellspacing="5" class="right_set1">
			  <tr id="customertr" class=VwCtr style="display:">
			    <td width="100%" align="center">发货批次：
					<select id="emaildate" name="emaildate" >
						<option value="">请选择(5天内)(供货商_供货商仓库_结算区域)</option>
						<%for (EmailDate e : emaildatelist) {%>
						<%if(e.getState()!=1){ %>
						<option value="<%=e.getEmaildateid()%>"  <%=(e.getEmaildateid()+"").equals(emaildateidParam)?"selected":"" %>>
						<%=e.getEmaildatetime()%>(<%=e.getCustomername()+"_"+e.getWarehousename()+"_"+e.getAreaname()  %>)
						</option>
						<%}}%>
					</select>*
				 </td>
			  </tr>
			  <tr id="customertr" class=VwCtr >
			    <td width="100%"  align="center">
					<input type ="hidden" value="yes" name ="isEdit">
			       <input type="submit"  id ="btn"  value="数据失效"  class="input_button2" /><br/>
			       <font color="red"> ${ReturnMessage}</font>
				</td>
			  </tr>
			</table>
		</form>
</body>
</html>
