<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>推送MQ消息</title>
</head>
<body>
     <h1>推送MQ消息</h1>
	<form action="sendMQInfo" method="post">
		主题:<input size=50 name="topic" value="" />
		<br/>
		<br/>
		规格(RoutingKey):<input size=50 name="routingKey" value="" /><br/>
		内容:<br/>
		<textarea rows="30" cols="100" name="info"></textarea><br/>
		<br/>
		<input type="submit" style="margin-left: 350px;" value="发送" /><br/>
	</form>
</body>
</html>