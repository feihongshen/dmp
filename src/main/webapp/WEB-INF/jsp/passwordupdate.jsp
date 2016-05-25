<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
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
function  trim(str)
{
    for(var  i  =  0  ;  i<str.length  &&  str.charAt(i)==" "  ;  i++  )  ;
    for(var  j  =str.length;  j>0  &&  str.charAt(j-1)==" "  ;  j--)  ;
    if(i>j)  return  "";
    return  str.substring(i,j);
}
function submitForm(form){
  if (trim(form.password.value)==""){
    alert("请输入密码");
    form.password.focus();
    return false;
  }else if (trim(form.confirmpassword.value)==""){
    alert("请确认密码");
    form.confirmpassword.focus();
    return false;
  }else if (trim(form.confirmpassword.value)!=trim(form.password.value)){
    alert("两次密码不一致");
    form.password.focus();
    return false;
  } else if(!isPasswordValid(form.password.value)){
	alert("密码至少要含小写字母、大写字母、数字、特殊字符中的任意三种且长度至少需要八位");
	return false;
  }
    form.submit();
    return true;
 }
  </script>
  </head>
 <% Map usermap=(Map)session.getAttribute("usermap");%>
 
 <body style="background:#f5f5f5">

<div class="right_box">
				<div class="right_title">
                <form action="<%=request.getContextPath() %>/user/updatepassword" method="post" name="form1" target="_self">
				<table width="100%" border="0" cellspacing="1" cellpadding="0" class="table_2" id="gd_table">
				  <tr class="font_1">
				        <td width="35%"></td>
						<td width="10%" align="center" valign="middle">用户名：</td>
                        <td width="20%" align="center" valign="middle">
                           <%=usermap.get("username").toString()%>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				      <td width="35%"></td>
                        <td width="10%" align="center" valign="middle">新密码：</td>
                        <td width="20%" align="center" valign="middle">
                          <input name="password" type="password" size="30"  maxlength="60" class="TextInput"/>
                        </td>
                        <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				        <td width="35%"></td>
                        <td width="10%" align="center" valign="middle">确认新密码：</td>
                        <td width="20%" align="center" valign="middle">
                        <input name="confirmpassword" type="password" size="30"  maxlength="60" class="TextInput" />
                        </td>
                        <td width="40%"></td>
				  </tr>
				  <tr class="font_1">
				  <td width="35%"></td>
				   <td width="30%" align="center" valign="middle" colspan ="2" >
				     <input type ="button" value ="保存" onClick="submitForm(form1);" class ="button" />
				     <input type ="reset" value ="重置"  class ="button"/>
				   </td>
				   <td width="35%"></td>
				  </tr>	
				  <tr class="font_1">
				     <td width="30%" align="center" valign="middle" colspan ="4">
				       <font id="errorState" color="red">${message}</font>
				     </td>
				  </tr>				 
				</table>
				</form>
				<div class="jg_10"></div><div class="jg_10"></div>
				</div>
			</div>
	<div class="jg_10"></div>
	<div class="clear"></div>
</body>
</html>
 
 