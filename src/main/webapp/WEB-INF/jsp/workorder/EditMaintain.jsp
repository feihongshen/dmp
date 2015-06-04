<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>创建供货商</h1>		
		<div id="box_form">
			<form action="<%=request.getContextPath()%>/workorder/addCallerArchival" id="addcallerForm">
				<table>
					<tr>
					<td><span>姓名*</span><input type="text" name="name"></td>
					<td><span>电话1</span><input type="text" name="phoneonOne" id='cp'></td>
					<td><span>电话2</span><input type="text" name="phoneonTwo"></td>
					</tr>
					<tr>
					<td><span>邮箱</span><input type="text" name="mailBox"></td>
					<td><span>城市</span><input type="text" name="city"></td>
					<td><span>公司</span>
						<select class="select1">
							<option></option>
						</select>
					</td>
					</tr>
					<tr>
						<td><span>客户分类*:</span>
						<select class="select1">
							<option></option>
						</select></td>
					</tr>				
				</table>
				<div>
				<label>备注:</label>
				<textarea style="width: 60%;height: 118px;margin-left: 60px"></textarea>																	
				</div>
				
			</form>	
			<input type="hidden" value="<%=request.getContextPath() %>/workorder/CallerArchivalRepository" id="returnCallerArchivalRepository">			
		</div>
		 <div align="center"><input type="submit" value="确认" class="button" align="center" onclick="addcaller()">
							 <input type="submit" value="取消" class="button" onclick="closeBox()"/>
		 	</div>
	</div>
</div>
<div id="box_yy"></div>