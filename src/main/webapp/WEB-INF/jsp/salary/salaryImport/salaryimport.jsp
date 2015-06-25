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
	List<SalaryImport> simportList = (List<SalaryImport>)request.getAttribute("simportList");
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/2.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css" type="text/css"  />
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>

<script type="text/javascript">
function saveForm(){
	
	var fileNameArr = [];
	var whichValueArr = [];
	for(var ind = 0; ind < 37; ind ++){
		var fileNameTemp = "filename" + ind;
		fileNameArr.push($("#"+fileNameTemp).val());
		var whichValueTemp = "whichvalue" + ind;
		whichValueArr.push($("#"+whichValueTemp).val());
	}
	
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
	<%if(simportList!=null&&simportList.size()>0){%>
	<table style="width: 70%;margin-left: 10%;margin-top: 5%" >
		<tbody id="trlist">
			<tr>
			<%for(int i=0;i<simportList.size();i++){%>
				<td height="50px" nowrap="nowrap" align="right" style="width: 15%"><%=simportList.get(i).getFiletext()%>:
					<input id="filename<%=i %>" name="filename<%=i %>" type="hidden" value="<%=simportList.get(i).getFilename() %>" />
				</td>
				<td height="50px" align="left" style="width: 15%">
					<select id="whichvalue<%=i %>" name="whichvalue<%=i %>">
						<option value="<%=WhichValueEnum.Gudingxiang.getValue()%>" <%if(simportList.get(i).getWhichvalue()==WhichValueEnum.Gudingxiang.getValue()){ %> selected="selected" <%} %> ><%=WhichValueEnum.Gudingxiang.getText() %></option>
						<option value="<%=WhichValueEnum.Linshixiang.getValue()%>" <%if(simportList.get(i).getWhichvalue()==WhichValueEnum.Linshixiang.getValue()){ %> selected="selected" <%} %>><%=WhichValueEnum.Linshixiang.getText() %></option>
					</select>
				</td>
				<%if((i+1)%4==0){ %>
			</tr>
			<tr>
			<%} }%>
			</tr>
		</tbody>
	</table>
	<%} %>
</form>	
</body>
</html>