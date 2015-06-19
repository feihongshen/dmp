<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>新增</h1>		
				<div id="box_form">						
					 <div style="margin-top: 0.3cm;margin-left: 0.5cm">
					 	<input type="button" value="返回" class="input_button2"/>
						<input type="button" value="保存" class="input_button2" onclick="closeBox()"/>
				 	</div>
						 <div>
						 	<table>
						 		<tr>
						 				<td>规则类型</td>
						 				<td>
											<select class="select1">
												<option>落地配与配送员</option>
											</select>
										</td>
										<td>状态</td>
										<td>
											<select class="select1">
												<option>在用</option>
											</select>
										</td>
										<td>拒收派费(元)</td>
										<td>
											<input type="text" class="input_text1">
										</td>
						 		</tr>	
						 		<tr>
						 					<td>结算规则名称</td>
						 					<td>
						 						<input type="text">
						 					</td>
						 		</tr>	
						 		<tr>
						 					<td>备注</td>
						 					<td>
						 						<textarea></textarea>
						 					</td>
						 		</tr>			 		
						 	</table>			 	
		 	 		</div>
 	 	</div>	
	</div>
</div>
<div id="box_yy"></div>