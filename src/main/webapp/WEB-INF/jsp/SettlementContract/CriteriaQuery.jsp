<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="box_bg"></div>
<div id="box_contant">
	<div id="box_top_bg"></div>
	<div id="box_in_bg">
		<h1><div id="close_box" onclick="closeBox()"></div>结算条件查询</h1>		
				<div id="box_form">
						<table>
							<tr>
								<td>结算规则名称</td>
								<td><input type="text" class="input_text1"></td>
								<td>状态</td>
								<td>
									<select class="select1">
										<option>全部</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>规则类型</td>
								<td>
									<select class="select1">
										<option>全部</option>
									</select>
								</td>
								<td>备注</td>
								<td><input type="text" class="input_text1"></td>
							</tr>
							<tr>
								<td>排序</td>
								<td>
									<select class="select1">
										<option>结算规则名称</option>
									</select>
								</td>
								<td>
									<select class="select1">
										<option>升序</option>
									</select>
								</td>
							</tr>
						</table>
					</div>
				 <div align="center">
				 	<input type="button" value="查询" class="button"/>
					<input type="button" value="关闭" class="button" onclick="closeBox()"/>
			 	</div>
 	 </div>
	
</div>
<div id="box_yy"></div>