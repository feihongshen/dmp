<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
/* function validateXML() 
{ 
    var xmlContent=document.getElementById("xml").value;
    alert(xmlContent)
    //errorCode 0是xml正确，1是xml错误，2是无法验证 
    var xmlDoc,errorMessage,errorCode = 0; 
    // code for IE 
    if (window.ActiveXObject) 
    { 
        xmlDoc  = new ActiveXObject("Microsoft.XMLDOM"); 
        xmlDoc.async="false"; 
        xmlDoc.loadXML(xmlContent); 
 
        if(xmlDoc.parseError.errorCode!=0) 
        { 
            errorMessage="错误code: " + xmlDoc.parseError.errorCode + "\n"; 
            errorMessage=errorMessage+"错误原因: " + xmlDoc.parseError.reason; 
            errorMessage=errorMessage+"错误位置: " + xmlDoc.parseError.line; 
            errorCode = 1; 
            return ;
        } 
        else 
        { 
            errorMessage = "格式正确"; 
            return true;
        } 
    } 
    // code for Mozilla, Firefox, Opera, chrome, safari,etc. 
    else if (document.implementation.createDocument) 
    { 
        var parser=new DOMParser(); 
        xmlDoc = parser.parseFromString(xmlContent,"text/xml"); 
        var error = xmlDoc.getElementsByTagName("parsererror"); 
        if (error.length > 0) 
        { 
               if(xmlDoc.documentElement.nodeName=="parsererror"){ 
                errorCode = 1; 
                errorMessage = xmlDoc.documentElement.childNodes[0].nodeValue; 
            } else { 
                errorCode = 1; 
                errorMessage = xmlDoc.getElementsByTagName("parsererror")[0].innerHTML; 
            } 
               return ;
        } 
        else 
        { 
            errorMessage = "格式正确"; 
            return true;
        } 
    } 
    else 
    { 
        errorCode = 2; 
        errorMessage = "浏览器不支持验证，无法验证xml正确性"; 
        return fales;
    } 
    return { 
        "msg":errorMessage,  
        "error_code":errorCode 
    }; 
}  */
</script>
</head>
<body>
<form action="<%=request.getContextPath()%>/lechong/addCwb" method="post">
<textarea rows="15" cols="60" name="xml" id="xml">
</textarea><br>
<select name="flag">
<option value="">请选择</option>
<option value="yes">是</option>
<option value="no">否</option>
</select>
<input type="submit" value="提交">
<input type="reset">
</form>
</body>
</html>