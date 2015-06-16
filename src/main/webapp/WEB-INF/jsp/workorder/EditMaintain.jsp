<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<% 
		CsConsigneeInfo ccf=(CsConsigneeInfo)request.getAttribute("ccf")==null?null:(CsConsigneeInfo)request.getAttribute("ccf");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>修改来电人信息</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/editCallerArchival" id="editcallerForm">
				<table>
					<tr>
					<td><input type="hidden" name="id" value="<%=ccf.getId()%>"></td>
					<td><span><font color="red">*</font>姓名</span><input type="text" name="name" value="<%=ccf.getName()%>" id="caname"></td>
					<td><span><font color="red">*</font>电话1</span><input type="text" name="phoneonOne" id='cp' value="<%=ccf.getPhoneonOne()%>" id="cp" onblur=" ifphoneNum(this)"></td>
					<td><span>电话2</span><input type="text" name="phoneonTwo" value="<%=ccf.getPhoneonTwo()%>"></td>
					</tr>
					<tr>
					<td><span>邮箱</span><input type="text" name="mailBox" value="<%=ccf.getMailBox()%>" id="mailBoxid"></td>
					<td><span><font color="red">*</font>省份</span><input type="text" name="province" value="<%=ccf.getProvince()%>" id="cprovince" onblur="isChineseValue(this)"></td>
					<td><span><font color="red">*</font>城市</span><input type="text" name="city" value="<%=ccf.getCity()%>" id="ccity" onblur="isChineseValue1(this)"></td>					
					</tr>
					<tr>
						<td><span><font color="red">*</font>客户分类:</span>
						<select class="select1" name="consigneeType" id="skhfl">
							<option value="-1">请选择客户分类</option>     
							<option value="2">VIP用户</option>
							<option value="1">普通客户</option>
						</select></td>
					</tr>				
				</table>
				<div>
				<label><font color="red">*</font>备注:</label>
				<textarea onkeyup="checkLen(this)" style="width: 60%;height: 118px;margin-left: 60px" name=callerremark" id="callerremark"></textarea>																	
				<div>您还可以输入<font id="count" color="red">150</font>个文字</div>
				</div>
				
			
			</form>			
		</div>
		 <div align="center"><input type="button" value="确认修改" class="button" align="center" onclick="editcaller()"/>
							 <input type="button" value="取消" class="button" onclick="closeBox()"/>
		 	</div>
	</div>
	
</div>
<div id="box_yy"></div>