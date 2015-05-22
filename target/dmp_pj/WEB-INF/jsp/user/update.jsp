<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.swfupload.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/swfupload/swfupload.queue.js"></script>
<script type="text/javascript">
$(function(){uploadFormInit('<%=request.getParameter("fromAction") %>','<%=request.getContextPath()%>');});
</script>
</head>
<body  style="position:relative">
<label id="swfupload-control">
<div style="position:absolute; z-index:9; left:0; top:0px; bottom:0px ">
<input type="text" id="wavText" style="border: solid 1px; background-color: #FFFFFF;" disabled="disabled" />
</div>
<div style="position:absolute; z-index:9; left:154px; top:0px">
<input type="button" id="upbutton" />
</div></label>
</body>
</html>