<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.explink.domain.CsConsigneeInfo"%>
<% 
		CsConsigneeInfo ccf=(CsConsigneeInfo)request.getAttribute("ccf")==null?null:(CsConsigneeInfo)request.getAttribute("ccf");
%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/editCallerArchival" id="editcallerForm">
				<table>
					<tr>
					<td><input type="hidden" name="id" value="<%=ccf.getId()%>"></td>
					<td><span>姓名*</span><input type="text" name="name" value="<%=ccf.getName()%>"></td>
					<td><span>电话1</span><input type="text" name="phoneonOne" id='cp' value="<%=ccf.getPhoneonOne()%>"></td>
					<td><span>电话2</span><input type="text" name="phoneonTwo" value="<%=ccf.getPhoneonTwo()%>"></td>
					</tr>
					<tr>
					<td><span>邮箱</span><input type="text" name="mailBox" value="<%=ccf.getMailBox()%>"></td>
					<td><span>省份</span><input type="text" name="province" value="<%=ccf.getProvince()%>"></td>
					<td><span>城市</span><input type="text" name="city" value="<%=ccf.getCity()%>"></td>					
					</tr>
					<tr>
						<td><span>客户分类*:</span>
						<select class="select1" name="consigneeType" id="skhfl1">
							<option value="-1">请选择客户分类</option>
							<option value="1">VIP用户</option>
							<option value="0">普通客户</option>
						</select></td>
					</tr>				
				</table>
				<div>
				<label>备注:</label>
				<textarea style="width: 60%;height: 118px;margin-left: 60px" name="remark" ></textarea>																	
				</div>
				
			</form>	
					
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" align="center" onclick="editcaller()"/>
							 <input type="submit" value="取消" class="button" onclick="closeBox()"/>
		 	</div>
	</div>
</div>
<div id="box_yy"></div>