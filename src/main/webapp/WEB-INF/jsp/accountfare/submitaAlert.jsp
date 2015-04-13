<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>


<script type="text/javascript">



</script>
<!-- 弹出框开始 -->

	<div id="alert_box" style="display:none; " align="center">
	  <div id="box_bg" ></div>
	  <div id="box_contant" >
	    <div id="box_top_bg"></div>
	    <div id="box_in_bg">
	    <form id="createForm" action="<%=request.getContextPath()%>/accountcwbfare/payfare" method="post">
	      <h1><div id="close_box" onclick="closeBox()"></div>
	        	交款信息</h1>
	        <div class="right_title" style="padding:10px">
	        <input type="hidden" name="cwbs" id="cwbs" value="">
	         <h1>您需支付金额：<font id="hjOpen" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>
				元，&nbsp;&nbsp;还有<font id="sxhjShow" style="font-family:'微软雅黑', '黑体'; font-size:25px"></font>元未支付。</lable></h1>
	          <p>&nbsp;</p>
	          <table width="800" border="0" cellspacing="1" cellpadding="2" class="table_2" >
	            <tr>
					<td bgcolor="#F4F4F4">付款方式</td>
					<td bgcolor="#F4F4F4">金额</td>
					<td bgcolor="#F4F4F4">付款人</td>
					<td bgcolor="#F4F4F4">卡号</td>
				</tr>
				<tr>
					<td>转账</td>
					<td><input onblur="hjFeeCheck()" name="girofee" type="text" id="feetransfer" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
					<td><input name="girouser" type="text" id="usertransfer" size="10" maxlength="20" value=""/></td>
					<td><input name="girocardno" type="text" id="cardtransfer" size="25" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<td>现金</td>
					<td><input onblur="hjFeeCheck()" name="cashfee" type="text" id="feecash" size="10" maxlength="10" value="" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
					<td><input name="cashuser" type="text" id="usercash" size="10" maxlength="20" value=""/></td>
					<td></td>
				</tr>
	            <tr>
	              <td colspan="4" bgcolor="#F4F4F4">
	              	<div class="jg_10"></div>
	                  <input type="button" class="input_button1" id="checkF" onclick="checkFClick()" value="确 认" />
	                  <div class="jg_10"></div>
	                </td>
	            </tr>
	          </table>
	        </div>
	        </form>
	    </div>
	  </div>
	  <div id="box_yy"></div>
	</div>
	<!-- 弹出框结束 -->

