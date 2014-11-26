<%@page language="java" import="java.util.*,java.math.BigDecimal" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.Branch"%>
<%@page import="cn.explink.util.StringUtil"%>
<%@page import="cn.explink.util.Page"%>
<%
	List<Branch> branchList = (List<Branch>)request.getAttribute("branchList");
	Page page_obj = (Page)request.getAttribute("page_obj");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户管理</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<style>

/*账户信息CSS*/
.uc_midbg,.ucmidbg_mid { position:relative; background:#AFD6EB; height:37px; z-index:1 }
.uc_midbg,.ucmidbg_mid ul { position:absolute; z-index:99; height:31px; left:0; bottom:0px; padding-left:6px; }
.uc_midbg,.ucmidbg_mid li { float:left; display:block; width:80px; height:31px; line-height:31px; text-align:center; margin-right:8px }
.uc_midbg,.ucmidbg_mid li a { display:block; line-height:31px; color:#666; font-size:12px; background:url(../../images/uc_menubg.png) no-repeat }
.uc_midbg,.ucmidbg_mid li a:hover { text-decoration:none; color:#C30; background-position:0px -31px }
.uc_midbg,.ucmidbg_mid .light { background-position:0px -62px }
/*通用弹出框*/
.alert_box {display:none}
.box_bg { position:absolute; z-index:99999999; width:100%; height:100%; background:#000; filter:alpha(opacity=30); opacity:0.3; -moz-opacity:0.3 }
.box_contant {position:absolute; z-index:99999999999}
.box_contant2 {position:absolute; z-index:99999999999}
.box_in_bg {overflow:hidden; border:1px solid #004f77; padding-bottom:5px; background:#7ed0ff }
.box_contant2 h1 { font-size:12px; line-height:28px; text-indent:5px; font-weight:bold }
.box_contant h1 { font-size:12px; line-height:28px; text-indent:5px; font-weight:bold }
.close_box { float:right; width:16px; height:16px; cursor:pointer; background:url(../../images/indexbg.png) no-repeat 0px -150px; margin-top:5px; margin-right:9px; display:inline }
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">

//充值 弹出窗口
function czF(branchid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/accountdeductrecord/recharge/"+branchid,
		//data:$('#createForm').serialize(),
		dataType : "json",
		success : function(data) {
			if(data.errorCode==0){
				$("#cz_branchid").val(branchid);
				$("#cz_branchname").html(data.branchname);//站点
				$("#cz_credit").html(data.credit);//信誉额度
				$("#cz_balance").html(data.balance);//余额
				$("#cz_debt").html(data.debt);//欠款
				$("#cz_fee").val("");
				$("#tanchu1").show();
				centerBox()
			}
		}
	});
}
//调账弹出窗口
function tzF(branchid){
	$.ajax({
		type: "POST",
		url:"<%=request.getContextPath()%>/accountdeductrecord/recharge/"+branchid,
		dataType : "json",
		success : function(data) {
			if(data.errorCode==0){
				$("#tz_branchid").val(branchid);
				$("#tz_branchname").html(data.branchname);//站点
				$("#tz_credit").html(data.credit);//信誉额度
				$("#tz_balance").html(data.balance);//余额
				$("#tz_debt").html(data.debt);//欠款
				$("#tz_fee").val("");
				$("#tz_memo").val("");
				$("#tanchu2").show();
				centerBox()
			}
		}
	});
}
//充值保存
function czSubmit(){
	if($("#cz_fee").val()==""){
		alert("请填写充值金额");
		return false;
	}
	
	if(!isFloat($("#cz_fee").val())){
		alert("充值金额应为数字");
		return false;
	}
	if(confirm("确定充值吗？")){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/accountdeductrecord/saveRecharge",
			data:{
				branchid:$("#cz_branchid").val(),
				fee:$("#cz_fee").val()
			},
			dataType : "json",
			success : function(data) {
				alert(data.error);				
				location.href="<%=request.getContextPath()%>/accountdeductrecord/branchList/1";
			}
		});
	}
}


//充值保存
function tzSubmit(){
	if($("#tz_fee").val()==""){
		alert("请填写调整现金");
		return false;
	}
	
	if(!isFloat($("#tz_fee").val())){
		alert("调整现金应为数字");
		return false;
	}
	if(confirm("确定调账吗？")){
		$.ajax({
			type: "POST",
			url:"<%=request.getContextPath()%>/accountdeductrecord/saveTiaoZhang",
			data:{
				branchid:$("#tz_branchid").val(),
				fee:$("#tz_fee").val(),
				memo:$("#tz_memo").val()
			},
			dataType : "json",
			success : function(data) {
				alert(data.error);				
				location.href="<%=request.getContextPath()%>/accountdeductrecord/branchList/1";
			}
		});
	}
}


//=======导出===========
function exportField(){
	<%if(branchList!=null&&!branchList.isEmpty()){%>
	 	$("#clickExport").attr("disabled","disabled");
		$("#clickExport").val("请稍后");
	 	$("#exportForm").submit(); 
	<%}%>
}
</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<!--弹窗开始1 -->
<div class="alert_box" id="tanchu1" style="display:none">
  <div class="box_bg"></div>
  <div class="box_contant" >
    <div class="box_top_bg"></div>
    <div class="box_in_bg">
      <h1>
        <div class="close_box" onclick="closeBox()"></div>
        充值</h1>
     <form method="post">
        <div class="right_title" style="padding:10px">
          <table width="500" border="0" cellspacing="1" cellpadding="2" class="table_2" >
            <tr>
              <td width="20%" align="right">站点：</td>
              <td width="20%" align="left" id='cz_branchname'></td>
            </tr>
            <tr>
              <td width="20%" align="right">信用额度[元]：</td>
              <td width="20%" align="left"><strong id='cz_credit'></strong></td>
            </tr>
            <tr>
            	<td align="right">帐户余额[元]：</td>
            	<td align="left"><strong id='cz_balance'></strong></td>
           	</tr>
           	<tr>
            	<td align="right">欠款[元]：</td>
            	<td align="left"><strong id='cz_debt'></strong></td>
           	</tr>
            <tr>
              <td align="right">充值金额[元]：</td>
              <td align="left"><input type="text" name="cz_fee" id="cz_fee" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
            </tr>
            <tr>
              <td colspan="2" align="center" bgcolor="#F4F4F4">
              	<div class="jg_10"></div>
              	<input type="hidden" name="cz_branchid" id="cz_branchid" />
              	<input type="button" class="input_button1" onclick="czSubmit()" value="确认" />
                  <div class="jg_10"></div>
                </td>
            </tr>
          </table>
        </div>
      </form>
    </div>
  </div>
  <div class="box_yy"></div>
</div>
<!--弹窗结束1-->

<!--弹窗开始2 -->
<div class="alert_box" id="tanchu2" style="display:none">
  <div class="box_bg"></div>
  <div class="box_contant2" >
    <div class="box_top_bg"></div>
    <div class="box_in_bg">
      <h1>
        <div class="close_box" onclick="closeBox()"></div>
              调账</h1>
      <form method="post">
        <div class="right_title" style="padding:10px">
          <table width="500" border="0" cellspacing="1" cellpadding="2" class="table_2" >
            <tr>
              <td width="20%" align="right">站点：</td>
              <td width="20%" align="left" id='tz_branchname'></td>
            </tr>
            <tr>
              <td width="20%" align="right">信用额度[元]：</td>
              <td width="20%" align="left"><strong id='tz_credit'></strong></td>
            </tr>
            <tr>
            	<td align="right">帐户余额[元]：</td>
            	<td align="left"><strong id='tz_balance'></strong></td>
           	</tr>
           	<tr>
            	<td align="right">欠款[元]：</td>
            	<td align="left"><strong id='tz_debt'></strong></td>
           	</tr>
            <tr>
              <td align="right">说明：</td>
              <td align="left">输入负数为扣减，输入正数为加款！</td>
            </tr>
            <tr>
            	<td align="right">调整现金[元]：</td>
            	<td align="left">
           		<input type="text" name="tz_fee" id="tz_fee" /></td>
				</tr>
            <tr>
              <td align="right">备注：</td>
              <td align="left"><input type="text" name="tz_memo" id="tz_memo" /></td>
            </tr>
            <tr>
              <td colspan="2" align="center" bgcolor="#F4F4F4">
              	<div class="jg_10"></div>
              	<input type="hidden" name="tz_branchid" id="tz_branchid" />
              	<input type="button" class="input_button1" onclick="tzSubmit()" value="确认" />
                  <div class="jg_10"></div>
                </td>
            </tr>
          </table>
        </div>
      </form>
    </div>
  </div>
  <div class="box_yy"></div>
</div>
<!--弹窗结束2-->

<div class="inputselect_box" style="top: 0px ">
	<form action="<%=request.getAttribute("page")==null?"1":request.getAttribute("page") %>" method="post" id="searchForm">
		站点名称：<input type="text" name="branchname" id="branchname" value="<%=StringUtil.nullConvertToEmptyString(request.getParameter("branchname"))%>"/>
		<input type="submit" onclick="$('#searchForm').attr('action',1);return true;" id="find" value="查 询" class="input_button2" />
		<%if(branchList!=null&&!branchList.isEmpty()){%>
			<input type="button" class="input_button2" id="clickExport"  onclick="exportField();"  value="导出Excel" />
		<%}%>
	</form>
</div>
<div style="height:35px"></div>
<form action="" method="get">
	<table width="100%" border="0" cellspacing="1" cellpadding="2" class="table_2" >
		<tr class="font_1">
			<td bgcolor="#f4f4f4">站点</td>
			<td bgcolor="#f4f4f4">信用额度（元）</td>
			<td bgcolor="#f4f4f4">可用额度（元）</td>
			<td bgcolor="#f4f4f4">预扣帐户余额（元）</td>
			<td bgcolor="#f4f4f4">预扣欠款（元）</td>
			<td bgcolor="#f4f4f4">实扣帐户余额（元）</td>
			<td bgcolor="#f4f4f4">实扣欠款（元）</td>
			<td bgcolor="#f4f4f4">操作</td>
		</tr>
		<%if(branchList!=null&&!branchList.isEmpty()){
			for(Branch b: branchList){
			//可用额度
			BigDecimal credituse=b.getCredit().add(b.getBalance()).subtract(b.getDebt());
			
		%>
		<tr>
			<td><%=b.getBranchname()%></td>
			<td><strong><%=b.getCredit()%></strong></td>
			<td><strong><%=credituse%></strong></td>
			<td><strong><%=b.getBalance()%></strong></td>
			<td><strong><%=b.getDebt()%></strong></td>
			<td><strong><%=b.getBalancevirt()%></strong></td>
			<td><strong><%=b.getDebtvirt()%></strong></td>
			<td>[<a href="#" onClick="tzF('<%=b.getBranchid()%>')">调账</a>] [<a href="#" onClick="czF('<%=b.getBranchid()%>')">充值</a>]</td>
		</tr>
		<%}}%>
	</table>
	<!--底部翻页 -->
	<div class="jg_35"></div>
	<%if(page_obj.getMaxpage()>1){%>
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

<form action="<%=request.getContextPath() %>/accountdeductrecord/exportBranchList" method="post" id="exportForm">
</form>

<script type="text/javascript">
//站点下拉框赋值
$("#selectPg").val(<%=request.getAttribute("page") %>);
</script>
</body>
</html>