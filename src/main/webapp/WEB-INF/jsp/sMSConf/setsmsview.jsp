<%@page import="cn.explink.domain.SmsConfig"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
   SmsConfig s = (SmsConfig)request.getAttribute("smsconfig");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>短信设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
<script>
function saveSuccess(data){
	$("#searchForm").submit();
}
</script>
<script language="javascript" type="text/javascript">
    $(function () {
        $("#checkboxN").click(function () {
            if ($("#checkboxN").is(":checked")) {
                $("#zhuanghu").css("display", "");
                $("#mima").css("display", "");
                $("#moban").css("display", "");
                $("#yujing").css("display", "");
               if($("#checkboxM").is(":checked")){
	                $("#shuliang").css("display", "");
	                $("#shouji").css("display", "");
	                $("#yujingneirong").css("display", "");
               }
            }
            else {
                $("#zhuanghu").css("display", "none");
                $("#mima").css("display", "none");
                $("#moban").css("display", "none");
                $("#yujing").css("display", "none");
                $("#shuliang").css("display", "none");
                $("#shouji").css("display", "none");
                $("#yujingneirong").css("display", "none");
            }
            $("#msg").html("已做修改，未保存");
        });
        $("#checkboxM").click(function () {
            if ($("#checkboxM").is(":checked")) {
                $("#shuliang").css("display", "");
                $("#shouji").css("display", "");
                $("#yujingneirong").css("display", "");
            }
            else {
                $("#shuliang").css("display", "none");
                $("#shouji").css("display", "none");
                $("#yujingneirong").css("display", "none");
            }
            $("#msg").html("已做修改，未保存");
        });
    });
    function onblursms(){
    	if($("#smsRemackID").val().length>100){
    		alert("已超过100个字");
    		$("#smsRemackID").val($("#smsRemackID").val().substring(0,100));
    		$("#smscount").html(100);
    		return false;
    	}else{
    	$("#smscount").html($("#smsRemackID").val().length);
    	return true;
    	}
    }
    function onblurwang(){
    	if($("#smsWangID").val().length>70){
    		alert("已超过100个字");
    		$("#smsWangID").val($("#smsWangID").val().substring(0,70));
    		$("#wangcount").html(70);
    		return false;
    	}else{
    	$("#wangcount").html($("#smsWangID").val().length);
    	return true;
    	}
    }
</script>
</head>


<body style="background:#eef9ff">
<div class="right_box">
	<div class="right_title">
		<form action="<%=request.getContextPath()%>/smsconfig/createORsave" method="post" id="searchForm">
		<input type = "hidden" value="<%=s.getId() %>" name="id"/>
		<table width="100%" border="0" cellspacing="1" cellpadding="6" class="table_5" >
			<tr>
				<td width="15%" align="right" valign="middle">启用短信功能：</td>
				<td width="85%" align="left" valign="middle"><input type="checkbox" name ="isOpen" id ="checkboxN" value ="1" <%if(s.getIsOpen()==1){ %>checked="checked"<%} %>/>
					<label for="checkbox"></label></td>
				</tr>
			<tr id="zhuanghu"  <%if(s.getIsOpen()!=1){ %>style="display: none"<%} %> >
				<td width="15%" align="right" valign="middle">帐户：</td>
				<td width="85%" align="left" valign="middle"><label for="textfield"></label>
					<input type="text" name ="name" id ="" value ="<%=s.getName()==null?"":s.getName()%>"/></td>
			</tr>
			<tr id="mima" <%if(s.getIsOpen()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >密码：</td>
				<td align="left" valign="middle" ><input type="text" name ="pass" id ="" value ="<%=s.getPassword()==null?"":s.getPassword()%>"/></td>
				</tr>
			<tr id="moban" <%if(s.getIsOpen()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >信息模板内容：<br/>
				最多100字，已输入字符数（<font color ="red" id ="smscount"><%=s.getTemplatecontent()==null?"0":s.getTemplatecontent().length()%></font>）
				</td>
				<td align="left" valign="middle" ><textarea  name ="templatecontent" rows="3" cols="100" id="smsRemackID" onkeyup="onblursms();"><%=s.getTemplatecontent()==null?"":s.getTemplatecontent()%></textarea>
				</td>
			</tr>
			<tr id="yujing" <%if(s.getIsOpen()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >打开预警：</td>
				<td align="left" valign="middle" ><input type="checkbox" name ="monitor" id ="checkboxM" value ="1" <%if(s.getMonitor()==1){ %>checked="checked"<%} %>/>
					<label for="checkbox"></label></td>
			</tr>
			<tr id="shuliang" <%if(s.getIsOpen()!=1 || s.getMonitor()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >剩余短信数量即预警：</td>
				<td align="left" valign="middle" ><input type="text" name ="warningcount" id ="" value ="<%=s.getWarningcount()%>"/></td>
				</tr>
			<tr id="shouji" <%if(s.getIsOpen()!=1 ||  s.getMonitor()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >预警发送手机：</td>
				<td align="left" valign="middle" ><textarea  name ="phone" rows="3" cols="25"><%=s.getPhone()==null?"":s.getPhone()%></textarea>
					多个手机用英文逗号隔开</td>
			</tr>	
			<tr id="yujingneirong" <%if(s.getIsOpen()!=1 ||  s.getMonitor()!=1){ %>style="display: none"<%} %>>
				<td align="right" valign="top" >预警内容：<br/>
				最多70字，已输入字符数（<font color ="red" id ="wangcount"><%=s.getWarningcontent()==null?"0":s.getWarningcontent().length()%></font>）</td>
				<td align="left" valign="middle" ><textarea  name ="warningcontent" rows="3" cols="100"  id="smsWangID" onkeyup="onblurwang();" ><%=s.getWarningcontent()==null?"":s.getWarningcontent()%></textarea>
					</td>
			</tr>	
			<tr id="baocun">
				<td colspan="2" align="center" valign="middle" ><font color="red" id="msg"> </font>&nbsp;&nbsp;<input type="button" name="button3" id="button3" value="保存" class="button" onclick="javascript:getSysconfigBox()" /></td>
			</tr>
		</table>
		<input type="hidden" name ="checksmsnull" value="${checksmsnull}" />
		</form>
	</div>
	
</div>
<!-- 删除的ajax地址 -->
<input type="hidden" id="alertsms" value="<%=request.getContextPath()%>/smsconfig/alertsms/" />
</body>
</html>

