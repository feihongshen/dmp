<%@page import="cn.explink.domain.*"%>
<%@page import="cn.explink.enumutil.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	SmsConfig sms = (SmsConfig)request.getAttribute("smsconfig");
int channel = (Integer)request.getAttribute("channel");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>短信设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/jquery.bgiframe.min.js"
	type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<link href="<%=request.getContextPath()%>/js/multiSelcet/jquery.multiSelect.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css"
	media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.message.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/multiSelcet/MyMultiSelect.js" type="text/javascript"></script>
<script>
  $(document).ready(function() {
	   // Options displayed in comma-separated list
	   $("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'站点' });
	   $("#customerids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'供应商' });
	});
	 
function showBranch(){
	if($("#flowordertype").val() == <%=FlowOrderTypeEnum.RuKu.getValue()%> || $("#flowordertype").val() ==<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>  ){
		$("#branchid").css("display", "none");
		return false;
	}else{
		$("#branchid").css("display", "");
		return true;
	}
}	
function updateCustomerids(){
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/getCustomerids",//后台处理程序
		type:"POST",//数据发送方式 
		data:"flowordertype="+$("#flowordertype").val(),//参数
		dataType:'json',//接受数据格式
		success:function(json){
			$("#customerid").html("");//清空下拉框//$("#select").html('');
			str="<select name =\"customerids\"  id=\"customerids\" multiple=\"multiple\" style=\"width: 180px;\">";
			for(var j = 0 ; j < json.length ; j++){
				if(json[j].ifeffectflag==10000){
					str +="<option value='"+json[j].customerid+"' selected=\"selected\" >"+json[j].customername+"  </option>";
				}else{
					str +="<option value='"+json[j].customerid+"'>"+json[j].customername+" </option>";
				}
			   }
			 str += "</select>";
			 $("#customerid").html(str);
			 
			 $("#customerids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'供应商' });
			}
	});
	return true;
}
function updateBranchids(){
	showBranch();
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/getBranchids",//后台处理程序
		type:"POST",//数据发送方式 
		data:"flowordertype="+$("#flowordertype").val(),//参数
		dataType:'json',//接受数据格式
		success:function(json){
			$("#branchid").html("");//清空下拉框//$("#select").html('');
			str="<select name =\"branchids\"  id=\"branchids\" multiple=\"multiple\" style=\"width: 180px;\">";
			for(var j = 0 ; j < json.length ; j++){
				if(json[j].sitetype==10000){
					str +="<option value='"+json[j].branchid+"' selected=\"selected\" >"+json[j].branchname+"  </option>";
				}else{
					str +="<option value='"+json[j].branchid+"'>"+json[j].branchname+" </option>";
				}
			   }
			 str += "</select>";
			 $("#branchid").html(str);
			 $("#branchids").multiSelect({ oneOrMoreSelected: '*',noneSelected:'站点' });
			}
	});
	return true;
}
function getSms(){
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/getSms",//后台处理程序
		type:"POST",//数据发送方式 
		data:"flowordertype="+$("#flowordertype").val(),//参数
		dataType:'json',//接受数据格式
		success:function(json){
			$("#templatecontent").val('');
			$("#templatecontent").val(json.templatecontent);
			$("#money").empty();//清空下拉框//$("#select").html('');
			$("<option value='-1'>金额</option>").appendTo("#money");//添加下拉框的option
				if(json.money==100){
					$("<option value='100'  selected=\"selected\"  >100元</option>").appendTo("#money");
				}else{
					$("<option value='100'>100元</option>").appendTo("#money");
				}
				if(json.money==200){
					$("<option value='200'  selected=\"selected\"  >200元</option>").appendTo("#money");
				}else{
					$("<option value='200'>200元</option>").appendTo("#money");
				}
				if(json.money==500){
					$("<option value='500'  selected=\"selected\"  >500元</option>").appendTo("#money");
				}else{
					$("<option value='500'>500元</option>").appendTo("#money");
				}
				if(json.money==1000){
					$("<option value='1000'  selected=\"selected\"  >1000元</option>").appendTo("#money");
				}else{
					$("<option value='1000'>1000元</option>").appendTo("#money");
				}
				if(json.money==5000){
					$("<option value='5000'  selected=\"selected\"  >5000元</option>").appendTo("#money");
				}else{
					$("<option value='5000'>5000元</option>").appendTo("#money");
				}
	       		
			}
		   
	});
}
function saveSms(){
	if($("#flowordertype").val() == -1){
		alert("请选择操作节点");
		return false;
	}
		var params=$('#saveSMS').serialize();
	$.ajax({
		url:"<%=request.getContextPath()%>/smsconfigmodel/saveSms",//后台处理程序
		type:"POST",//数据发送方式 
		data:params,//参数
		dataType:'json',//接受数据格式
		success:function(json){
			alert(json.error);
			getSms();
			}
	});
}

function disableUselessElement(){
	if($("#flowordertype").val()==100){
		$("#customerid").css("display", "none");
		$("#branchid").css("display", "none");
		$("#moneyTd").css("display", "none");
   	    
   	    $('#flowordertypeDiv').hide();
   	    $('#payPlatformDiv').show();
	}else{
		$("#customerid").css("display", "");
		$("#branchid").css("display", "");
		$("#moneyTd").css("display", "");
		
		$('#payPlatformDiv').hide();
	   	$('#flowordertypeDiv').show();
	}
}

</script>
<script language="javascript">
$(function(){
	var $menuli = $(".uc_midbg ul li");
	var $menulilink = $(".uc_midbg ul li a");
	$menuli.click(function(){
		$(this).children().addClass("light");
		$(this).siblings().children().removeClass("light");
		var index = $menuli.index(this);
		$(".tabbox li").eq(index).show().siblings().hide();
	});
	
})
</script>
<script>
function saveSuccess(data){
	$("#searchForm").submit();
}
</script>
<script language="javascript" type="text/javascript">
    $(function () {
        $("#closeOrOpenId").click(function () {
        	if( $("#closeOrOpenId").val()=="关闭"){
                 $("#yujing").css("display", "none");
                 $("#shuliang").css("display", "none");
                 $("#shouji").css("display", "none");
                 $("#yujingneirong").css("display", "none");
                 $("#updateSms").css("display", "none");
                 $("#baocunId").css("display", "none");
                 $("#closeOrOpenId").val('开启');
                 $("#stateId").html('关闭');
                 $("#zhanghu").css("display", "none");
                 $("#mima").css("display", "none");
                 $("#baocun").css("display", "none");
                 $.ajax({
                   	 url:"<%=request.getContextPath()%>/smsconfigmodel/updateOpen",//后台处理程序
                		type:"POST",//数据发送方式 
                		data:"isOpen=0&channel=<%=channel%>",//参数
                		dataType:'json',//接受数据格式
                		success:function(json){
                			}
                	});
        	}else{
                 $("#yujing").css("display", "");
                 $("#updateSms").css("display", "");
                if($("#checkboxM").is(":checked")){
 	                $("#shuliang").css("display", "");
 	                $("#shouji").css("display", "");
 	                $("#yujingneirong").css("display", "");
 	                $("#baocunId").css("display", "");
                }
                $("#closeOrOpenId").val('关闭');
                $("#stateId").html('开启');
                $.ajax({
                  	 url:"<%=request.getContextPath()%>/smsconfigmodel/updateOpen",//后台处理程序
               		type:"POST",//数据发送方式 
               		data:"isOpen=1&channel=<%=channel%>",//参数
               		dataType:'json',//接受数据格式
               		success:function(json){
               			}
               		   
               	});
        	}
        });
        $("#updateSms").click(function () {
                 $("#zhanghu").css("display", "");
                 $("#mima").css("display", "");
                 $("#baocun").css("display", "");
        });
        
        $("#checkboxM").click(function () {
            if ($("#checkboxM").is(":checked")) {
                $("#shuliang").css("display", "");
                $("#shouji").css("display", "");
                $("#yujingneirong").css("display", "");
                $("#baocunId").css("display", "");
                $.ajax({
                	 url:"<%=request.getContextPath()%>/smsconfigmodel/updateMonitor",//后台处理程序
             		type:"POST",//数据发送方式 
             		data:"isMonitor=1&channel=<%=channel%>",//参数
             		dataType:'json',//接受数据格式
             		success:function(json){
             			}
             		   
             	});
            }
            else {
                $("#shuliang").css("display", "none");
                $("#shouji").css("display", "none");
                $("#yujingneirong").css("display", "none");
                $("#baocunId").css("display", "none");
                $.ajax({
                 	 url:"<%=request.getContextPath()%>/smsconfigmodel/updateMonitor",//后台处理程序
              		type:"POST",//数据发送方式 
              		data:"isMonitor=0&channel=<%=channel%>",//参数
              		dataType:'json',//接受数据格式
              		success:function(json){
              			}
              		   
              	});
            }
        });
        
        $("#regist").click(function () {
             $.ajax({
              	 url:"<%=request.getContextPath()%>/smsconfigmodel/regist",//后台处理程序
				type : "POST",//数据发送方式 
				dataType : 'html',//接受数据格式
				success : function(html) {
					if (html == 0) {
						alert("激活成功");
					}
				}

			});
		});
	});
	function onblursms() {
		if ($("#smsRemackID").val().length > 100) {
			alert("已超过100个字");
			$("#smsRemackID").val($("#smsRemackID").val().substring(0, 100));
			$("#smscount").html(100);
			return false;
		} else {
			$("#smscount").html($("#smsRemackID").val().length);
			return true;
		}
	}
	function onblurwang() {
		if ($("#smsWangID").val().length > 70) {
			alert("已超过70个字");
			$("#smsWangID").val($("#smsWangID").val().substring(0, 70));
			$("#wangcount").html(70);
			return false;
		} else {
			$("#wangcount").html($("#smsWangID").val().length);
			return true;
		}
	}
	function showAll() {
		$("#showAllForm").submit();
	}
</script>
</head>
<body style="background: #f5f5f5" marginwidth="0" marginheight="0">
	<div class="right_title">
		<div style="background: #FFF">
			基本信息：
			<%
			if (channel != 0) {
		%>
			<a href="<%=request.getContextPath()%>/smsconfigmodel/setsmsview">配置默认短信渠道</a>
			<%
				} else if (channel != 1) {
			%>
			<a href="<%=request.getContextPath()%>/smsconfigmodel/setsmsview?channel=1">配置当当短信渠道</a>
			<%
				}
			%>
			<div class="jg_10"></div>
			<form name="form1" method="post"
				action="<%=request.getContextPath()%>/smsconfigmodel/createORsave">
				<input type="hidden" name="checksmsnull" value="${checksmsnull}" /> <input type="hidden"
					name="isCreate" value="1" /> <input type="hidden" value="<%=channel%>" name="channel" />
				<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_5">
					<%
						if (sms != null) {
					%>
					<tr>
						<td width="120" align="center" bgcolor="#f4f4f4">&nbsp;</td>
						<td align="left">
							<%
								if (sms.getIsOpen() == 1) {
							%> <strong>当前使用短信账户【<%=sms.getName()%>】为【<font id="stateId">开启</font>】状态
						</strong><input name="button" type="button" id="closeOrOpenId" value="关闭" /><input name="button"
							type="button" id="updateSms" value="修改账户" /> <%
 	if (channel == 1) { //当当短信渠道首次注册
 %> <br /> <input name="button" type="button" id="regist" value="首次激活" />当当短信渠道首次使用需要激活一次，且只能激活一次，激活多次，可能会被短信渠道提供商屏蔽ip
							<%
 	}
 %> <%
 	} else {
 %> <strong>当前使用短信账户【<%=sms.getName()%>】为【<font id="stateId">关闭</font>】状态
						</strong><input name="button" type="button" id="closeOrOpenId" value="开启" /><input
							style="display: none" name="button" type="button" id="updateSms" value="修改账户" /> <%
 	}
 %>
						</td>
					</tr>
					<tr id="zhanghu" style="display: none">
						<td align="center" bgcolor="#f4f4f4">帐户：</td>
						<td align="left"><input type="text" name="name" value="<%=sms.getName()%>" /></td>
					</tr>
					<tr id="mima" style="display: none">
						<td align="center" bgcolor="#f4f4f4">密码：</td>
						<td align="left"><input type="text" name="pass" value="<%=sms.getPassword()%>" /></td>
					</tr>
					<tr id="baocun" style="display: none">
						<td align="center" bgcolor="#f4f4f4">&nbsp;</td>
						<td align="left"><input name="button" type="submit" id="save" value="保存" /></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#f4f4f4">&nbsp;</td>
						<td align="left"><strong>短信剩余条数：${smsCount}条</strong></td>
					</tr>
					<%
						} else {
					%>
					<tr>
						<td width="120" align="center" bgcolor="#f4f4f4">&nbsp;</td>
						<td align="left"><strong>当前没有短信账户，需要从以下设置</strong></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#f4f4f4">帐户：</td>
						<td align="left"><input type="text" name="name" /></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#f4f4f4">密码：</td>
						<td align="left"><input type="text" name="pass" /></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#f4f4f4">&nbsp;</td>
						<td align="left"><input name="button" type="submit" id="save" value="保存" /></td>
					</tr>
					<%
						}
					%>
				</table>
			</form>
			<div class="jg_10"></div>
			规则设置：
			<div class="jg_10"></div>
			<form id="saveSMS" name="saveSMS" method="post" action="">
				<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_5">
					<tr>
						<td width="5%" align="center" bgcolor="#F4F4F4">&nbsp;</td>
						<td colspan="4" align="left"><input name="button2" type="button" id="button2"
							value="查看所有规则" onclick="showAll();" /></td>
					</tr>
					<tr>
						<td width="5%" align="center" bgcolor="#F4F4F4">创建：</td>
						<td width="10%"><label for="select"></label> <select name="flowordertype"
							id="flowordertype" onchange="updateCustomerids(); getSms();updateBranchids();disableUselessElement();"
							class="select1">
								<option value="-1">操作节点或反馈结果</option>
								<option value="<%=FlowOrderTypeEnum.RuKu.getValue()%> ">入库扫描</option>
								<option value="<%=FlowOrderTypeEnum.ChuKuSaoMiao.getValue()%>">出库扫描</option>
								<option value="<%=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()%>">到货扫描</option>
								<option value="<%=FlowOrderTypeEnum.FenZhanLingHuo.getValue()%>">领货扫描</option>
								<%
									for (DeliveryStateEnum c : DeliveryStateEnum.values()) {
								%>
								<%
									if (c.getValue() != 0) {
								%>
								<option value=<%=c.getValue() + 100%>><%=c.getText()%></option>
								<%
									}
								%>
								<%
									}
								%>
								<option value="100">站点返款结算（支付平台充值）</option>
						</select></td>
						<td width="20%" id="customerid"><select name="customerids" id="customerids"
							multiple="multiple" style="width: 180px;">
						</select> [<a href="javascript:multiSelectAll('customerids',1,'请选择');">全选</a>] [<a
							href="javascript:multiSelectAll('customerids',0,'请选择');">取消全选</a>]</td>
						<td width="10%" id="branchid"><select name="branchids" id="branchids" multiple="multiple"
							style="width: 180px;">
						</select></td>
						<td id="moneyTd" width="10%">高于： <select name="money" id="money" class="select1">
								<option value='-1'>金额</option>
								<option value='100'>100元</option>
								<option value='200'>200元</option>
								<option value='500'>500元</option>
								<option value='1000'>1000元</option>
								<option value='5000'>5000元</option>
						</select></td>
					</tr>
					<tr>
						<td align="center" bgcolor="#F4F4F4">发送内容：</td>
						<td colspan="4" align="left">
							<p>
								<strong>模板要求：</strong>
							</p>
							<div id="flowordertypeDiv">
								<p>
									<strong>1.替换字符：（供应商-customername,小件员-delivername,小件员电话-deliverphone,代收款金额-receivablefee）</strong>
								</p>
								<p>
									<strong>2.字符总长度不超过70个字符，超过70个字符，短信平台将分两条发送</strong>
								</p>
								<p>
									<strong>3.模板示例：您好，您在[customername]的订单已由[delivername][deliverphone]送出，代收款[receivablefee]，18点前查询及投诉请拨打010-85896659【易普联科】</strong>
								</p>
								<p>
									<strong>4.上门退模板示例(开启上门退模板设置)：您好，您的退件（尾号{tailnumber}）在24小时内由快递员{delivername}（{deliverphone}）上门收取。请提前备好包裹，留意致电</strong>
								</p>
							</div>
							
							<div id="payPlatformDiv" style="display: none;">
								<p>
									<strong>1.替换字符：（账单未收款金额-surplusAmount ; 实际扣款时间-chargeTime）</strong>
								</p>
								<p>
									<strong>2.字符总长度不超过70个字符，超过70个字符，短信平台将分两条发送</strong>
								</p>
								<p>
									<strong>3.模板示例：站长，您好！公司于「chargeTime」划扣您的账户，发现余额不足，导致财务人员无法进行收款，当前所需收款金额为「surplusAmount」元，请尽快进行充值。</strong>
								</p>
							</div>

							<textarea name=templatecontent cols="100" rows="5" id="templatecontent"></textarea>
						</td>
					</tr>
					<tr>
						<td align="center" bgcolor="#F4F4F4">&nbsp;</td>
						<td colspan="4" align="left"><input name="saveConfig" type="button" class="input_button1"
							id="saveConfig" value="保存" onclick="saveSms();"></td>
					</tr>
				</table>
			</form>

			<div class="jg_10"></div>
			预警设置：
			<div class="jg_10"></div>
			<form name="form1" method="post"
				action="<%=request.getContextPath()%>/smsconfigmodel/createORsave" method="post" id="searchForm">
				<input type="hidden" name="checksmsnull" value="${checksmsnull}" /> <input type="hidden"
					value="<%=channel%>" name="channel" />
				<table width="100%" border="0" cellspacing="1" cellpadding="5" class="table_5">
					<tr id="yujing" <%if (sms == null || (sms.getIsOpen() != 1)) {%> style="display: none" <%}%>>
						<td width="10%" align="center" bgcolor="#F4F4F4">打开预警：</td>
						<td colspan="4" align="left"><input type="checkbox" name="monitor" id="checkboxM"
							value="1" <%if (sms != null && sms.getMonitor() == 1) {%> checked="checked" <%}%> /></td>
					</tr>
					<tr id="shuliang" <%if (sms == null || (sms.getIsOpen() != 1 || sms.getMonitor() != 1)) {%>
						style="display: none" <%}%>>
						<td width="10%" align="center" bgcolor="#F4F4F4">剩余短信数量即预警：</td>
						<td colspan="4" align="left"><label for="select"> <input name="warningcount"
								type="text" id="textfield5" value="<%=sms == null ? 0 : sms.getWarningcount()%>" />
						</label></td>
					</tr>
					<tr id="shouji" <%if (sms == null || (sms.getIsOpen() != 1 || sms.getMonitor() != 1)) {%>
						style="display: none" <%}%>>
						<td width="10%" align="center" bgcolor="#F4F4F4">预警发送手机：</td>
						<td colspan="4" align="left"><textarea name="phone" cols="50" rows="5" id="textfield4"><%=sms == null ? "" : sms.getPhone()%></textarea>
							多个手机用英文逗号隔开</td>
					</tr>
					<tr id="yujingneirong"
						<%if (sms == null || (sms.getIsOpen() != 1 || sms.getMonitor() != 1)) {%>
						style="display: none" <%}%>>
						<td width="10%" align="center" bgcolor="#F4F4F4"><p>预警内容：</p>
							<p>最多70个字符</p></td>
						<td colspan="4" align="left"><textarea name="warningcontent" cols="50" rows="5"
								id="textfield3"><%=sms == null ? "" : sms.getWarningcontent()%></textarea></td>
					</tr>
					<tr id="baocunId" <%if (sms == null || (sms.getIsOpen() != 1 || sms.getMonitor() != 1)) {%>
						style="display: none" <%}%>>
						<td width="10%" align="center" bgcolor="#F4F4F4">&nbsp;</td>
						<td colspan="4" align="left"><input type="submit" name="button3" id="button3" value="保存"
							class="button" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<form action="<%=request.getContextPath()%>/smsconfigmodel/showAll" method="post" id="showAllForm">
	</form>
	<input type="hidden" id="alertsms" value="<%=request.getContextPath()%>/smsconfigmodel/showAll" />
</body>
</html>

