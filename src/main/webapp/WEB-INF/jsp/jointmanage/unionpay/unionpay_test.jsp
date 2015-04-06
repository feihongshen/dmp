

<%@page import="cn.explink.b2c.tools.*" %>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.explink.pos.unionpay.*" %>
<%
String values = request.getAttribute("values")==null?"":(String)request.getAttribute("values");
String requestjson = request.getAttribute("requestjson")==null?"":(String)request.getAttribute("requestjson");
String command =request.getParameter("command")==null?"":request.getParameter("command");
String username =request.getParameter("username")==null?"":request.getParameter("username");
String password =request.getParameter("password")==null?"":request.getParameter("password");
String cwb =request.getParameter("cwb")==null?"":request.getParameter("cwb");
String pay_type =request.getParameter("pay_type")==null?"":request.getParameter("pay_type");
String sign_type =request.getParameter("sign_type")==null?"":request.getParameter("sign_type");
List<ExptReason> exptlist=request.getAttribute("exptlist")==null?null:(List<ExptReason>)request.getAttribute("exptlist");

%>
<html>
<head>

<title>银联商务测试平台</title>
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
			
		 if($("#command").val()==''){
			 alert('请选择请求接口！');
			 $("#command").focus();
			 return;
		 }
		 if(($("#command").val()=='0102'||$("#command").val()=='0103')&&$("#cwb").val()==''){
			 alert('请输入订单号！');
			 $("#command").focus();
			 return;
		 }
		 if($("#command").val()=='0103'&&$("#pay_type").val()==''){
			 alert('请选择交易状态！');
			 $("#pay_type").focus();
			 return;
		 }
		
		 
		 
		 $('#searchForm').attr('action','<%=request.getContextPath()%>/unionpay_test/request');
		 $('#searchForm').submit();
	});
	 
	 $("#command").change(function (){
		 if($("#command").val()=='0100'||$("#command").val()=='0101'){
			 $("#search").hide();
			 $("#login").show();
			 $("#pay_type1").hide();
		 }
		 if($("#command").val()=='0102'){
			 $("#login").show();
			 $("#search").show();
			 $("#pay_type1").hide();
		 }
		 
		 if($("#command").val()=='0103'){   //运单处理结果报告
			 $("#login").show();
			 $("#search").show();
			 $("#pay_type1").show();
			 
		 }
		 
		 
		
		 
		 
		 
	 });
	 
	 $("#pay_type").change(function (){
		 if($("#pay_type").val()=='20'||$("#pay_type").val()=='40'||$("#pay_type").val()=='70'){
			 $("#sign").hide();
			 if($("#pay_type").val()=='20'||$("#pay_type").val()=='40'){
				 $("#expt_code1").show();
			 }
			 
		 }else{
			 $("#sign").show();
		 }
		 
	 });
		
	 $("#sign_type").change(function (){
		 if($("#sign_type").val()=='2'){
			 $("#instead").show();
		 }else{
			 $("#instead").hide();
		 }
		 
	 });
	 
	 
	 
	 
	 
 });
	 

 
 </script>




</head>
<body style="background:#f5f5f5">
<div class="menucontant">
	<font  color="red">请在对接管理设置配置信息</font>
	<br><br><br>
	<form  method="post" id="searchForm">
		请选择请求接口:Command=<select name="command" id="command">
									<option value="">请选择</option>
									<%for(UnionPayEnum e:UnionPayEnum.values()){
									%>
									<option value="<%=e.getCommand()%>" <%if(command.equals(e.getCommand())){%>selected<%} %>><%=e.getDecribe() %></option>
									<% } %>
							  </select>
		<br><br>
		<div id="login" <%if(username.equals("")) {%>style="display:none"<%}%> >
		登录名：<input type="text" name="username" value="<%=username%>"/>&nbsp;&nbsp;&nbsp;密码：<input type="password" name="password" value="<%=password%>"/>
		</div>
		<div id="search" <%if(cwb.equals("")||command.equals("0100")||command.equals("0101")) {%>style="display:none"<%}%>>
		请输入订单号：<input type="text" name="cwb" value="<%=cwb%>"/>
		</div>
		<div id="pay_type1" <%if(!command.equals("0103")) {%>style="display:none"<%}%>>
		请选择交易状态：<select name="pay_type" id="pay_type">
									<option value="">请选择</option>
									<%for(UnionPayTradetypeEnum e:UnionPayTradetypeEnum.values()){
									%>
									<option value="<%=e.getType()%>" <%if(command.equals(e.getDecribe())){%>selected<%} %>><%=e.getDecribe() %></option>
									<% } %>
							  </select>
		请输入支付金额：<input type="text" name="payamount" value="" size="8"/>交易状态为非付款可忽略
		</div>
		<div id="sign" <%if(pay_type.equals("20")||pay_type.equals("40")||pay_type.equals("70")||pay_type.equals("")) {%>style="display:none"<%}%>>
		请选择签收类型：<select name="sign_type" id="sign_type">
									<option value="1">本人签收</option>
									<option value="2">他人代收</option>
							  </select>
		</div>
		<div id="instead" <%if(sign_type.equals("1")||sign_type.equals("")) {%>style="display:none"<%}%>>
		请输入签收人：<input type="text" name="signman" value=""/>
		</div>
		<div id="expt_code1" <%if(pay_type.equals("20")||pay_type.equals("40")||pay_type.equals("")) {%>style="display:none"<%}%>>
		请选择异常原因：<select name="expt_code" id="expt_code">
									<option value="">请选择异常原因</option>
									<%if(exptlist!=null&&exptlist.size()>0){
										for(ExptReason ex:exptlist){%>
									 <option value="<%=ex.getExpt_code()%>"><%=ex.getExpt_msg() %></option>
									<%}} %>
					   </select>
		</div>
		
		<br><input type="button" value="提交请求" id="btn" name="btn"/><br>
		请求的JSON:<textarea rows="10" cols="50"><%=requestjson%></textarea>
		返回的JSON：<textarea rows="10" cols="50"><%=values%></textarea>
		<br>
		<font  color="red">注：登出不需要密码验证</font>
	</form>
	
</div>

</body>
</html>
