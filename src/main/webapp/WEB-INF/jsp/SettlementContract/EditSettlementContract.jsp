<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
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

	</body>
</html>