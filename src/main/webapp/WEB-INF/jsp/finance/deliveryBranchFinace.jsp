

<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List,cn.explink.domain.Branch"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.explink.pos.unionpay.*" %>
<%
List<Branch> branchlist=(List<Branch>)request.getAttribute("branchlist");

String datadiff =request.getAttribute("datadiff")==null?"0":(String)request.getAttribute("datadiff");

long deliverybranchid =request.getParameter("deliverybranchid")==null?0:Long.valueOf(request.getParameter("deliverybranchid"));

String chongzhiAmount =request.getParameter("chongzhiAmount")==null?"0":request.getParameter("chongzhiAmount");

%>
<html>
<head>

<title>派送站点账务导出</title>
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
 <script type="text/javascript">

	 
 
 $(document).ready(function() {
	 $("#btn").click(function (){
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btn").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/download_detail');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 $("#btndiff").click(function (){
		
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/koukuan_diff');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 //中转退货明细记录
	 $("#btnzt").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/jushouzhongzhuan_detail');
		 $('#searchForm').submit();
		 
	 });
	 
	 //中转退货差异
	 $("#btnztdiff").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/jushouzhongzhuan_diff');
		 $('#searchForm').submit();
		 
	 });
	 
	 //
	 $("#btnkkztdiff").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/koukuanjushouzhongzhuan_diff');
		 $('#searchForm').submit();
		 
	 });
	 
	 $("#btnobj").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/testExport');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 $("#btnproduce").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 if($("#chongzhiAmount").val()==''){
			 alert('请录入充值总额!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/produceTxt');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 $("#btndifffinsh").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 if($("#chongzhiAmount").val()==''){
			 alert('请录入充值总额!');
			 return;
		 }
		 
		 if($("#datadiff").val()=='0'){
			 alert('没有计算出差异金额，不能导出!');
			 return;
		 }
		 
		 
		 $("#div_loading").show();
		 $("#btndifffinsh").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/exportdiff');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 
	
	 $("#btnkkqzck").click(function (){
			
		 $("#div_loading").hide();
		 if($("#deliverybranchid").val()=='0'){
			 alert('请选择导出站点!');
			 return;
		 }
		 
		 $("#div_loading").show();
		 $("#btndiff").attr("disabled","disabled");
	
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/finace/kkqzckdiffdetail_detail');
		 $('#searchForm').submit();
		 
	 });
	 
	 
	 
	 
	});
 </script>




</head>
<body style="background:#eef9ff">
<div class="menucontant">
	<font  color="red">派送站点账务导出</font>
	<br><br><br>
	<form  method="post" id="searchForm">
	
					选择站点:<select name="deliverybranchid" id=deliverybranchid>
										<option value="0">请选择</option>
										<%for(Branch b:branchlist){%>
										<option value="<%=b.getBranchid() %>" <%if(deliverybranchid==b.getBranchid()){%>selected<%} %> ><%=b.getBranchname() %></option>
										<%} %>
							  </select>
							  请录入充值总额:<input type="text"  name="chongzhiAmount" id="chongzhiAmount" value="<%=chongzhiAmount %>" />
							  <br>
						-----------------------------扣款----------------------------------------------
							  	派送结果:<select name="deliverystate" id=deliverystate>
										<option value="1">成功</option>
										<option value="8">丢失</option>
							  </select>
							  <br><input type="button" value="导出明细" id="btn" name="btn"/>&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value="导出差异(未扣款)" id="btndiff" name="btn2"/><br>
							  <br>
						 -----------------------------返款----------------------------------------------
							  	返款类型:<select name="flowordertype" id=flowordertype>
										<option value="11">中转</option>
										<option value="12">退货</option>
							  </select>
							  <br>
							  <input type="button" value="导出所有中转退货记录" id="btnzt" name="btn2"/>&nbsp;
							  <input type="button" value="导出所有扣款强制出库差异记录" id="btnkkqzck" name="btn2"/>
							  <br><br>
							<!--   <input type="button" value="导出中转退货(重复返款)记录" id="btnztdiff" name="btn2"/><br> -->
				        <!-------------------------------扣款未扣，中转和拒收重复--无视返款类型和配送结果-------------------------------------------->
							<!-- <br> <input type="button" value="导出扣款未扣，中转退货重复返款" id="btnkkztdiff" name="btn2"/> --> 
							
							<br><br>----------------------导出差异订单记录--------------------------------------
							<br><input type="button" value="导出差异订单记录" id="btndifffinsh" name="btndifffinsh"/>&nbsp;
							
						<br><br><br>=============================生成账务信息txt文件=================================
							  <br> <input type="button" value="生成账务信息txt文件" id="btnproduce" name="btn2"/>
		<br><br>
		<div id="div_loading" style="display:none" align="center"><IMG  src="<%=request.getContextPath()%>/images/dpfossloading.gif"> <font color="gray">正在下载订单,请稍后..</font></div>
		<br>
		<input type="hidden" name="datadiff" id="datadiff" value="<%=datadiff %>" />
		<textarea rows="20" cols="80">${showresult}</textarea>
	</form>
	
</div>

</body>
</html>
