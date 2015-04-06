<%@page import="cn.explink.enumutil.CwbStateEnum"%>
<%@page import="cn.explink.domain.OperationRule,cn.explink.enumutil.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
OperationRule operationRule = (OperationRule)request.getAttribute("operationRule");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改操作检验设置</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>

function buttonSave(form){
	if($("#flowordertype").val()==-1){
		alert("环节不能为空");
		return false;
	}else if($("#expression").val()==0){
		alert("规则表达式不能为空");
		return false;
	}else{
		$.ajax({
			type: "POST",
			url:$(form).attr("action"),
			data:$(form).serialize(),
			dataType:"json",
			success : function(data) {
				alert(data.error);
			}
		});
	}
}
</script>
<div style="background:#f5f5f5">
	<div id="box_in_bg">
		<h1>修改操作检验设置</h1>
		<form id="parameterDetail_cre_Form" name="parameterDetail_cre_Form"
			 onSubmit="buttonSave(this);return false;" 
			 action="<%=request.getContextPath()%>/operationRule/save/<%=operationRule.getId()%>" method="post"  >
			<div id="box_form3">
				<ul>
					<li><span>规则名称：</span><input  id="name" name="name" value="<%=operationRule.getName()%>"/></li>
					<li><span>规则表达式：</span><textarea  id="expression" name="expression"><%=operationRule.getExpression()%></textarea>*</li>
					<li><span>环节：</span>
						<select id="flowordertype" name="flowordertype">
							<option value="-1" selected>----请选择----</option>
							<%for(FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()){ %>
								<option value="<%=ft.getValue() %>" <%if(operationRule.getFlowordertype()==ft.getValue()){ %>selected<%} %>><%=ft.getText() %></option>
							<%} %>
						</select>*
					</li>
					<li><span>校验失败返回信息：</span>
						<input id="errormessage" name="errormessage" value="<%=operationRule.getErrormessage()%>"/>
					</li>
					<li><span>校验结果：</span>
						<select id="result" name="result">
								<option value="<%=OpertaionResultEnum.Success.getValue() %>"<%if(operationRule.getResult()==OpertaionResultEnum.Success){ %>selected<%} %>>成功</option>
								<option value="<%=OpertaionResultEnum.Continue.getValue() %>"<%if(operationRule.getResult()==OpertaionResultEnum.Continue){ %>selected<%} %>>继续</option>
								<option value="<%=OpertaionResultEnum.Fail.getValue() %>"<%if(operationRule.getResult()==OpertaionResultEnum.Fail){ %>selected<%} %>>失败</option>
						</select>
					</li>
					
	         </ul>
		</div>
		<div align="center">
        <input type="submit" value="确认" class="button" id="sub" />　
        <input type="button" value="返回" class="button" id="cancel" onclick="location='<%=request.getContextPath()%>/operationRule/list/1'" /></div>
	</form>
	</div>
</div>


