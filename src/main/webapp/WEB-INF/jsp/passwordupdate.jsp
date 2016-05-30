<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<% 
Map usermap=(Map)session.getAttribute("usermap");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>修改密码</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
  <script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/js.js"></script>
  <link rel="stylesheet" href="css/styles.css" type="text/css">
  <SCRIPT LANGUAGE='JavaScript' src='js/havetype.js'></Script>
  <BASE TARGET="View">
  <script language="JavaScript" type="text/JavaScript">
  <% String msg="";  if (request.getAttribute("msg")!=null){
       msg=(String)request.getAttribute("msg");
  %>
  alert('<%=msg%>');
  <%}%>
$(function(){
	if (<%=usermap.get("roleid").toString()%> == 2 || <%=usermap.get("roleid").toString()%> == 4){
		$("#pdaPwdDiv").css('display',''); 
	}
});

function  trim(str)
{
    for(var  i  =  0  ;  i<str.length  &&  str.charAt(i)==" "  ;  i++  )  ;
    for(var  j  =str.length;  j>0  &&  str.charAt(j-1)==" "  ;  j--)  ;
    if(i>j)  return  "";
    return  str.substring(i,j);
}
 
function submitFormWeb(form){
	if (trim(form.webPassword.value)==""){
		alert("请输入网页登录密码");
		form.webPassword.focus();
		return false;
	} else if (trim(form.confirmWebPassword.value)==""){
		alert("请确认网页登录密码");
		form.confirmWebPassword.focus();
		return false;
	} else if (trim(form.confirmWebPassword.value)!=trim(form.webPassword.value)){
		alert("两次网页登录密码不一致");
		form.webPassword.focus();
		return false;
	} 
	if(!isPasswordValidForWeb(form.webPassword.value)){
		alert("网页登录密码至少要含小写字母、大写字母、数字、特殊字符中的任意三种，且长度至少需要八位");
		return false;
	}
	if(!isUsernamePasswordNotContainEachOther('<%=usermap.get("username").toString()%>', form.webPassword.value)){
		alert("用户名与网页登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
		return false;
	}
	form.submit();
    return true;
}

function submitForm(form){
	if (<%=usermap.get("roleid").toString()%> == 2 || <%=usermap.get("roleid").toString()%> == 4){
		if (trim(form.password.value)==""){
			alert("请输入PDA登录密码");
			form.password.focus();
			return false;
		} else if (trim(form.confirmpassword.value)==""){
			alert("请确认PDA登录密码");
			form.confirmpassword.focus();
			return false;
		} else if (trim(form.confirmpassword.value)!=trim(form.password.value)){
			alert("两次PDA登录密码不一致");
			form.password.focus();
			return false;
		}
		if(!isPasswordValidForPDA(form.password.value)){
			alert("PDA登录密码必须为数字，不能过于简单，至少要含三个不同数字，数字不能为日期，且长度至少需要六位");
			return false;
		}
		if(!isUsernamePasswordNotContainEachOther('<%=usermap.get("username").toString()%>', form.password.value)){
			alert("用户名与PDA登录密码的内容不能相互包含，包括倒序、忽略大小写等变化");
			return false;
		}
		form.submit();
	    return true;
	}
}

  </script>
  </head>
 
 <body style="background:#f5f5f5">

<div class="right_box">
				<div class="right_title">
                <form action="<%=request.getContextPath() %>/user/updatewebpassword" method="post" name="form2" target="_self">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table2">
				  <tr class="font_1">
				  		<td width="30%"></td>
				   		<td width="35%" align="center" valign="middle" colspan ="2" >
				     		<label>修改网页登录密码</label>
				   		</td>
				   		<td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				        <td width="30%"></td>
						<td width="15%" align="center" valign="middle">用户名：</td>
                        <td width="20%" align="center" valign="middle">
                           <%=usermap.get("username").toString()%>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				  	    <td width="30%"></td>
                        <td width="15%" align="center" valign="middle">网页登录新密码：</td>
                        <td width="20%" align="center" valign="middle">
                          <input name="webPassword" type="password" size="30"  maxlength="60" class="TextInput"/>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				        <td width="30%"></td>
                        <td width="15%" align="center" valign="middle">确认网页登录新密码：</td>
                        <td width="20%" align="center" valign="middle">
                        <input name="confirmWebPassword" type="password" size="30"  maxlength="60" class="TextInput" />
                        </td>
                        <td width="35%"></td>
				  </tr>
				  <tr class="font_1">
				  <td width="30%"></td>
				   <td width="35%" align="center" valign="middle" colspan ="2" >
				     <input type ="button" value ="保存" onClick="submitFormWeb(form2);" class ="button" />
				     <input type ="reset" value ="重置"  class ="button"/>
				   </td>
				   <td width="35%"></td>
				  </tr>		 
				</table>
				</form>				
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>
			
<div class="right_box" id="pdaPwdDiv" style="display:none">
				<div class="right_title">
                <form action="<%=request.getContextPath() %>/user/updatepassword" method="post" name="form1" target="_self">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				  <tr class="font_1">
				  		<td width="30%"></td>
				   		<td width="35%" align="center" valign="middle" colspan ="2" >
				     		<label>修改PDA登录密码</label>
				   		</td>
				   		<td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				        <td width="30%"></td>
						<td width="15%" align="center" valign="middle">用户名：</td>
                        <td width="20%" align="center" valign="middle">
                           <%=usermap.get("username").toString()%>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				      <td width="30%"></td>
                        <td width="15%" align="center" valign="middle">PDA登录新密码：</td>
                        <td width="20%" align="center" valign="middle">
                          <input name="password" type="password" size="30"  maxlength="60" class="TextInput"/>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				        <td width="30%"></td>
                        <td width="15%" align="center" valign="middle">确认PDA登录新密码：</td>
                        <td width="20%" align="center" valign="middle">
                        <input name="confirmpassword" type="password" size="30"  maxlength="60" class="TextInput" />
                        </td>
                        <td width="35%"></td>
				  </tr>
				  <tr class="font_1">
				  <td width="30%"></td>
				   <td width="35%" align="center" valign="middle" colspan ="2" >
				     <input type ="button" value ="保存" onClick="submitForm(form1);" class ="button" />
				     <input type ="reset" value ="重置"  class ="button"/>
				   </td>
				   <td width="35%"></td>
				  </tr>				 
				</table>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>
			<div>
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2">
				  <tr class="font_1">
				     <td width="30%" align="center" valign="middle" colspan ="4">
				       <font id="errorState2" color="red">${message}</font>
				     </td>
				  </tr>
				</table>
			</div>
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>
 
 