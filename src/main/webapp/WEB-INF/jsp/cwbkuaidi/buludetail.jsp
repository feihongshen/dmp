<%@page import="cn.explink.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="cn.explink.enumutil.PaytypeEnum"%>
<%@page import="cn.explink.enumutil.SignStateEnmu"%>
<%@page import="cn.explink.util.DateTimeUtil"%>
<%@page import="cn.explink.domain.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String newcwbs=request.getAttribute("newcwbs")==null?"":request.getAttribute("newcwbs").toString();
	CwbKuaiDiView ckv=(CwbKuaiDiView)request.getAttribute("ckv");
	String error=request.getAttribute("error")==null?"":request.getAttribute("error").toString();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>揽收订单补录信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css"/>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>

<script type="text/javascript">


function checkcheck(){
	var cwb=$("#cwbs").val();
	if(cwb.length>0){ 
		$("#searchForm").submit();
	 }else{ 
		alert("订单号不为空");
	} ;  
}

function buluOne(){
	if(bitian()&&checkFormat()){
		$.ajax({
			type:"post",
			url:$("#saveform").attr("action"),
			dataType:"json",
			data:$("#saveform").serialize(),
			success:function(){
				$("#searchForm").submit();
			}
		});
		
	}
	

}
function trim(str){
	return str.replace(/^s+|s+$/g, '');
}

function bitian(){
	if(trim($("#consigneename").val()).length==0){
		alert("请填写收件人姓名");
		return false;
	}
	if(trim($("#consigneeaddress").val()).length==0){
		alert("请填写收件人地址");
		return false;
	}
	if(trim($("#consigneemobile").val()).length==0){
		alert("请填写收件人手机");
		return  false;
	}
	if(!isPhoneNumber(trim($("#consigneemobile").val()))){
		alert("收件人手机号格式不正确");
		return false;
	}
	if($("#paytype").val()==0){
		alert("请选择付款方式");
		return false;
	}
	return true;
}

function checkfloatbyid(id){
	var element=trim($("#"+id).val());
	return isFloat(element); 
	
}

function checkNumberById(id){
	var element=trim($("#"+id).val());
	return isNumber(element);  
	
}

function checkFormat(){
	 if($("#signstate").val()==<%=SignStateEnmu.TaRenDaiQian.getKey()  %>&&$("#signman").val().length==0){
			alert("请填写签收人");
			return false;
		}
	 if($("#sendconsigneemobile").val()!=""&&$("#sendconsigneemobile").val().length>0&&!isPhoneNumber(trim($("#sendconsigneemobile").val()))){
		 alert("寄件人手机格式不正确");
		 return false;
	 }
	
	if(!checkNumberById("sendcarnum")){
		alert("件数必须为数字");
		return false;
	}
	if(!checkNumberById("chang")){
		alert("长度必须为数字");
		return false;
	}
	if(!checkNumberById("kuan")){
		alert("宽度必须为数字");
		return false;
	}
	if(!checkNumberById("gao")){
		alert("高度必须为数字");
		return false;
	}
	
	if(!checkfloatbyid("carrealweight")){
		alert("重量必须为浮点型（如0.00）");
		return  false;
	}
	if(!checkfloatbyid("packingfee")){
		alert("包装费必须为浮点型（如0.00）");
		return false;
	}
	if(!checkfloatbyid("transitfee")){
		alert("运费必须为浮点型（如0.00）");
		return false;
	}
	if(!checkfloatbyid("nowfee")){
		alert("现付款项必须为浮点型（如0.00）");
		return false ;
	}
	if(!checkfloatbyid("safefee")){
		alert("保险费必须为浮点型（如0.00）");
		return false;
	}
	if(!checkfloatbyid("weightfee")){
		alert("计费重量必须为浮点型（如0.00）");
		return false ;
	}
	if(!checkfloatbyid("insuredrate")){
		alert("保价率必须为浮点型（如0.00）");
		return false;
	}
	if(!checkfloatbyid("realweight")){
		alert("实际重量必须为浮点型（如0.00）");
		return false ;
	}
	if(!checkfloatbyid("allfee")){
		alert("费用总计必须为浮点型（如0.00）");
		return false ;
	}
	if(!checkfloatbyid("receivablefee")){
		alert("代收货款必须为浮点型（如0.00）");
		return false;
	}
	if(!checkfloatbyid("packagefee")){
		alert("封装费必须为浮点型（如0.00）");
		return false ;
	}
	if(!checkfloatbyid("otherfee")){
		alert("其他费用必须为浮点型（如0.00）");
		return false ;
	}
	return true;
	
}



</script>
</head>
<body style="background:#fff" marginwidth="0" marginheight="0">
<form action="<%=request.getContextPath() %>/cwbkuaidi/buludetail" method="post"  id="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="10" class="table_5" >
			<tbody>
				<tr>
					<td align="left" valign="top" bgcolor="#EAF1B1">快递单号：
						<textarea name="cwbs" cols="30" rows="5" id="cwbs" style="vertical-align:middle" title="请输入运单号，多个运单号同时输入时，请用回车键隔开"><%=newcwbs%></textarea>
					<input type="button"  onclick="checkcheck();"  class="input_button2" value="查询" /></td>
				</tr>
			</tbody>
	</table>
</form> 
	<%if(ckv!=null){ %>
	 <form action="<%=request.getContextPath() %>/cwbkuaidi/savebulu" id="saveform" >  
		<table width="100%" border="0" cellspacing="0" cellpadding="10" class="table_5" >
			<tbody>
				<tr>
					<td colspan="2" align="left" valign="top" bgcolor="#F8F8F8">
					<table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td colspan="4" align="left" bgcolor="#F4F4F4"><h1><strong>收件人信息：</strong></h1></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF"><strong>*</strong>快递单号&nbsp;：
							<input type="text" name="cwb" id="cwb"  value="<%=ckv.getCwb() %>" readonly="readonly" /> </td>
							<td width="25%" align="left" bgcolor="#FFFFFF"><strong></strong>寄&nbsp;&nbsp;件&nbsp;&nbsp;人：
							<input type="text" name="sendconsigneename" value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneename())  %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>  id="sendconsigneename" /></td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;&nbsp;寄件公司：
							<input type="text" name="sendconsigneecompany" id="sendconsigneecompany"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneecompany())%>" <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF">寄件人区号：
							<input type="text" name="sendconsigneeareacode" id="sendconsigneeareacode" value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneeareacode())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>  /></td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">寄&nbsp;件&nbsp;城市：
							<input type="text" name="sendconsigneecity" id="sendconsigneecity"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneecity())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF"><strong></strong>寄&nbsp;件&nbsp;地址：
							<input type="text" size="50"  name="sendconsigneeaddress" id="sendconsigneeaddress"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneeaddress())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF"><strong></strong>寄件人邮编：
							<input type="text" name="sendconsigneepostcode" id="sendconsigneepostcode" value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneepostcode())%>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">寄件人座机：
							<input type="text" name="sendconsigneephone" id="sendconsigneephone"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneephone())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF"><strong></strong>寄件人手机：
							<input type="text" name="sendconsigneemobile" id="sendconsigneemobile"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendconsigneemobile())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td align="left" bgcolor="#FFFFFF"><strong>*</strong>付款&nbsp;方式：							
								<select name="paytype" id="paytype" <%if(ckv.getPaytype()>0){ %>readonly="readonly"<%} %>>
									<option value="0">请选择付款方式</option>
									<%for(PaytypeEnum pay:PaytypeEnum.values()){ %>
										<option value="<%=pay.getValue() %>" <%if(ckv.getPaytype()==pay.getValue()){ %>selected="selected"<%} %>  ><%=pay.getText() %></option>
									<%} %>
							</select></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF"><strong>*</strong>收&nbsp;件&nbsp;人&nbsp;：
								<input type="text" name="consigneename" id="consigneename" value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneename()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
								<td align="left" bgcolor="#FFFFFF">收&nbsp;件&nbsp;公司：
								<input type="text" name="shoujianrencompany" id="shoujianrencompany"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getShoujianrencompany()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
								<td align="left" bgcolor="#FFFFFF">收件人区号：
								<input type="text" name="consigneeareacode" id="consigneeareacode"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneeareacode())%>" <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">收&nbsp;件&nbsp;城市：
							<input type="text" name="cwbcity" id="cwbcity"   value="<%=StringUtil.nullConvertToEmptyString(ckv.getCwbcity())%>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF"><strong>*</strong>收&nbsp;件地址：
							<input type="text" size="50" name="consigneeaddress" id="consigneeaddress" value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneeaddress()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF"><strong></strong>收件人邮编：
							<input type="text" name="consigneepostcode" id="consigneepostcode" value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneepostcode()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">收件人座机：
							<input type="text" name="consigneephone" id="consigneephone"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneephone()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td align="left" bgcolor="#FFFFFF"><strong>*</strong>收件人手机：
							<input type="text" name="consigneemobile" id="consigneemobile"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getConsigneemobile()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
					</table></td>
				</tr>
				<tr>
					<td width="50%" align="left" valign="top" bgcolor="#F8F8F8"><table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td align="left" bgcolor="#F4F4F4"><h1><strong>货&nbsp;物&nbsp;信息：</strong></h1></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：
								<input type="text" name="sendcarname" id="sendcarname"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSendcarname()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：
								<input name="sendcarnum" type="text" id="sendcarnum"  value="<%=ckv.getSendcarnum()%>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/>
								重&nbsp;&nbsp;&nbsp;&nbsp;量：
								<input name="carrealweight" type="text" id="carrealweight" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getCarrealweight()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">体积：长
								<input name="chang" type="text" id="chang" value="<%=ckv.getChang() %>" <%if(error.length()==0){ %>readonly="readonly"<%} %> />cm&nbsp;X&nbsp;宽
								<input name="kuan" type="text" id="kuan" value="<%=ckv.getKuan() %>"   <%if(error.length()==0){ %>readonly="readonly"<%} %>  />cm&nbsp;X&nbsp;高
								<input name="gao" type="text" id="gao" value="<%=ckv.getGao() %>" <%if(error.length()==0){ %>readonly="readonly"<%} %>  />cm
							</td>
						</tr>
					</table></td>
					<td align="left" valign="top" bgcolor="#F8F8F8"><table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td colspan="3" align="left" bgcolor="#F4F4F4"><h1><strong>收派人信息：</strong></h1></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">揽&nbsp;&nbsp;收&nbsp;&nbsp;人：
								<input  name="lanshouuserid" id="lanshouuserid" value="<%=StringUtil.nullConvertToEmptyString(ckv.getLanshouusername())  %>" readonly="readonly"/>
								
							</td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;揽收站点&nbsp;：
							<input type="text" readonly="readonly"  name="lanshoubranchid" id="lanshoubranchid" value="<%=StringUtil.nullConvertToEmptyString(ckv.getLanshoubranchname()) %>" readonly="readonly"/>
							</td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;中转站点&nbsp;：
							<input type="text" readonly="readonly"  name="zhongzhuanbranchid" id="zhongzhuanbranchid" value="<%=StringUtil.nullConvertToEmptyString(ckv.getZhongzhuanzhanbranchname()) %>" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">派&nbsp;&nbsp;件&nbsp;&nbsp;人：
								<input type="text" readonly="readonly"  name="paisonguserid" id="paisonguserid" value="<%=StringUtil.nullConvertToEmptyString(ckv.getPaisongusername()) %>" readonly="readonly"/>
							</td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;配送站点&nbsp;：
								<input type="text"   readonly="readonly" name="paisongbranchid" id="paisongbranchid" value="<%=StringUtil.nullConvertToEmptyString(ckv.getPaisongzhandianname()) %>"  readonly="readonly"/>
							</td>
							<td  width="25%" align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
					</table></td>
				</tr>
				<tr>
					<td colspan="2" align="left" valign="top" bgcolor="#F8F8F8"><table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td colspan="4" align="left" bgcolor="#F4F4F4"><h1><strong>费用：</strong></h1></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">包&nbsp;&nbsp;装&nbsp;&nbsp;费：
								<input name="packingfee" type="text" id="packingfee" value="<%=ckv.getPackingfee()%>" <%if(error.length()==0){ %>readonly="readonly"<%} %> />
							元</td>
							<td width="25%" align="left" bgcolor="#FFFFFF">保&nbsp;&nbsp;险&nbsp;&nbsp;费：
								<input name="safefee" type="text" id="safefee"  value="<%=StringUtil.nullConvertToBigDecimal(ckv.getSafefee()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td width="25%" align="left" bgcolor="#FFFFFF">保&nbsp;&nbsp;价&nbsp;&nbsp;率：
								<input name="insuredrate" type="text" id="insuredrate"  value="<%=StringUtil.nullConvertToBigDecimal(ckv.getInsuredrate()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/>
								%</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;代收货款&nbsp;：
								<input name="receivablefee" type="text" id="receivablefee"  value="<%=StringUtil.nullConvertToBigDecimal(ckv.getReceivablefee()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %> />
								元</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：
								<input name="transitfee" type="text" id="transitfee"  value="<%=StringUtil.nullConvertToBigDecimal(ckv.getTransitfee()) %>"<%if(error.length()==0){ %>readonly="readonly"<%} %> />元</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;计费重量&nbsp;：
								<input name="weightfee" type="text" id="weightfee"   value="<%=StringUtil.nullConvertToBigDecimal(ckv.getWeightfee()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;实际重量&nbsp;：
								<input name="realweight" type="text" id="realweight" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getRealweight()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %> /></td>
							<td align="left" bgcolor="#FFFFFF">封&nbsp;&nbsp;装&nbsp;&nbsp;费：
								<input name="packagefee" type="text" id="packagefee" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getPackagefee()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> />元</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">现&nbsp;付款&nbsp;项：
								<input name="nowfee" type="text" id="nowfee" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getNowfee()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> />元</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;其他费用&nbsp;：
								<input name="otherfee" type="text" id="otherfee" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getOtherfee()) %>" <%if(error.length()==0){ %>readonly="readonly"<%} %> />元</td>
							<td align="left" bgcolor="#FFFFFF"><strong></strong>&nbsp;费用总计&nbsp;：
								<input name="allfee" type="text" id="allfee" value="<%=StringUtil.nullConvertToBigDecimal(ckv.getAllfee()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %> />元</td>
							<td align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
					</table></td>
				</tr>
				<tr>
					<td colspan="2" align="left" valign="top" bgcolor="#F8F8F8"><table width="100%" border="0" cellpadding="5" cellspacing="1" class="table_5">
						<tr>
							<td colspan="2" align="left" bgcolor="#F4F4F4"><h1><strong>签收信息：</strong></h1></td>
						</tr>
						<tr>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;签收状态&nbsp;：
								<label for="select2"></label>
								<select name="signstate" id="signstate"  <%if(ckv.getSignstate()>0){ %>readonly="readonly"<%} %>>
								<%for(SignStateEnmu se:SignStateEnmu.values()){ %>
									<option value="<%=se.getKey() %>"  <%if(se.getKey()==ckv.getSignstate()){ %>selected="selected"<%} %>  ><%=se.getValue() %></option>
								<%} %>
							</select></td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" bgcolor="#FFFFFF">签&nbsp;&nbsp;收&nbsp;&nbsp;人：
								<label for="textfield41"></label>
							<input type="text" name="signman" id="textfield41"  value="<%=StringUtil.nullConvertToEmptyString(ckv.getSignman()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td width="25%" align="left" bgcolor="#FFFFFF">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" valign="top" bgcolor="#FFFFFF">签&nbsp;收时间&nbsp;：
								<input type="text" name="signtime" id="textfield42" value="<%=StringUtil.nullConvertToEmptyString(ckv.getSigntime()) %>"  <%if(error.length()==0){ %>readonly="readonly"<%} %>/></td>
							<td width="25%" align="left" valign="top" bgcolor="#FFFFFF">备注：
							<textarea name="remark" cols="50" rows="5" id="remark"  <%if(error.length()==0){ %>readonly="readonly"<%} %>></textarea></td>
						</tr>
					</table></td>
				</tr>
				<tr>
					<td colspan="2" align="left" valign="top" bgcolor="#F8F8F8">
						<input name="button" type="button" class="input_button1" id="button<%=ckv.getCwb() %>"  onclick="buluOne();" value="确认并补录下一单" />
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	<%} %>
</body>

</html>

