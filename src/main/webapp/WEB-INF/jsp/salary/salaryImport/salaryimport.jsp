<%@page import="cn.explink.enumutil.WhichValueEnum"%>
<%@page import="cn.explink.domain.SalaryImport"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资导入设置</title>
<%
	List<SalaryImport> addList = (List<SalaryImport>)request.getAttribute("addList");
	List<SalaryImport> deductList = (List<SalaryImport>)request.getAttribute("deductList");
%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>

<script type="text/javascript">
function saveForm(){
	
	var fileNameArr = [];
	var whichValueArr = [];
	<%for(int i=0;i<addList.size();i++){%> 
		var fileNameTemp = "filenameadd" + <%=i%>;
		fileNameArr.push($("#"+fileNameTemp).val());
		var whichValueTemp = "whichvalueadd" + <%=i%>;
		whichValueArr.push($("#"+whichValueTemp).val());
	<%}%>
	<%for(int i=0;i<deductList.size();i++){%> 
		var fileNameTemp = "filenamededuct" + <%=i%>;
		fileNameArr.push($("#"+fileNameTemp).val());
		var whichValueTemp = "whichvaluededuct" + <%=i%>;
		whichValueArr.push($("#"+whichValueTemp).val());
	<%}%>
	var urlStr = $("#saveform").attr("action");
	var dataJson = {};
	dataJson["fileNameArr"] = fileNameArr;
	dataJson["whichValueArr"] = whichValueArr;
	$.ajax({
		type: "POST",
		url:urlStr,
		data:dataJson,
		dataType:"json",
		success : function(data) {
			if(data.errorCode==0){
				alert(data.error);
			}else{
				alert(data.error);
			}
		}
	});
}
</script>
</head>
<body>
<form action="<%=request.getContextPath() %>/salaryImport/updateWhichvalue" method="post" id="saveform">
	<input  type="button" id="save" name="save" value="保存"  onclick="saveForm();"/>
	<%if(addList!=null&&deductList!=null){%>
	<div style="border: 1px;color: black;">
	<fieldset style="margin-left: 20%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;width: 60%;">
		<legend>增项</legend>
		<table style="width: 100%;margin-top: 0%" >
			<tr>
			<%for(int i=0;i<addList.size();i++){%>
				<td height="50px" nowrap="nowrap" align="right" style="width: 15%"><%=addList.get(i).getFiletext()%>:
					<input id="filenameadd<%=i %>" name="filename" type="hidden" value="<%=addList.get(i).getFilename() %>" />
				</td>
				<td height="50px" align="left" style="width: 15%">
					<select id="whichvalueadd<%=i %>" name="whichvalue">
						<option value="<%=WhichValueEnum.Gudingxiang.getValue()%>" <%if(addList.get(i).getWhichvalue()==WhichValueEnum.Gudingxiang.getValue()){ %> selected="selected" <%} %> ><%=WhichValueEnum.Gudingxiang.getText() %></option>
						<option value="<%=WhichValueEnum.Linshixiang.getValue()%>" <%if(addList.get(i).getWhichvalue()==WhichValueEnum.Linshixiang.getValue()){ %> selected="selected" <%} %>><%=WhichValueEnum.Linshixiang.getText() %></option>
					</select>				
				</td>
				<%if((i+1)%4==0){ %>
			</tr>
			<tr>
			<%} }%>
			</tr>
		</table>	
	</fieldset>	
	</div>
	<fieldset style="margin-left: 20%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;width: 60%;">
		<legend>减项</legend>
		<table style="width: 100%;margin-top: 0%" >	
			<tr>
			<%for(int i=0;i<deductList.size();i++){%>
				<td height="50px" nowrap="nowrap" align="right" style="width: 15%"><%=deductList.get(i).getFiletext()%>:
					<input id="filenamededuct<%=i %>" name="filename" type="hidden" value="<%=deductList.get(i).getFilename() %>" />
				</td>
				<td height="50px" align="left" style="width: 15%">
					<select id="whichvaluededuct<%=i %>" name="whichvalue<%=i %>">
						<option value="<%=WhichValueEnum.Gudingxiang.getValue()%>" <%if(deductList.get(i).getWhichvalue()==WhichValueEnum.Gudingxiang.getValue()){ %> selected="selected" <%} %> ><%=WhichValueEnum.Gudingxiang.getText() %></option>
						<option value="<%=WhichValueEnum.Linshixiang.getValue()%>" <%if(deductList.get(i).getWhichvalue()==WhichValueEnum.Linshixiang.getValue()){ %> selected="selected" <%} %>><%=WhichValueEnum.Linshixiang.getText() %></option>
					</select>				
				</td>
				<%if((i+1)%4==0){ %>
			</tr>
			<tr>
			<%} }%>
			</tr>
		</table>
	</fieldset>	
	<%} %>
</form>	
</body>
</html>