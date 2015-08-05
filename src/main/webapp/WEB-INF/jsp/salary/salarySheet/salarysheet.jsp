<%@page import="cn.explink.enumutil.WhichValueEnum"%>
<%@page import="cn.explink.domain.SalaryImport"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资条设置</title>
<%
	List<SalaryImport> addList = (List<SalaryImport>)request.getAttribute("addList");
	List<SalaryImport> deductList = (List<SalaryImport>)request.getAttribute("deductList");
%>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness/jquery-ui-1.8.18.custom.css" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>

<script type="text/javascript">

function getall(){
	$("[name='ischeck']").attr("checked",true);	
}
function cancelall(){
	//$("[name='ischeck']").attr("checked",false);
	$("[name='ischeck']").each(function(index){
		if($(this).attr("checked")=='true'||$(this).attr("checked")=='checked'){
			$(this).attr("checked",false);
		}else{
			$(this).attr("checked","checked");
		}
		
	});
}

function saveForm(){
	var fileNameArr = [];
	var ischeckedArr = [];
	//获取checkbox内的值(放入ischeckedArr数组)
	var intvalue = $('input[name="ischeck"]:checked').size();
	if(intvalue==0){
		alert("请至少选择一项!");
	}else if(intvalue>0){
		$('input[name="ischeck"]').each(function(index){
			if($(this).attr("checked")=="true"||$(this).attr("checked")=="checked"){
				$(this).val("1");
			}else{
				$(this).val("0");
			}
			ischeckedArr.push($(this).val());
		});
	}
	
	//获取工资条信息名称(放入fileNameArr数组)
	<%for(int i=0;i<addList.size();i++){%>
		var filenametemp1 = "filenameadd"+<%=i%>;
		var filename1 = $("#"+filenametemp1).val();
		fileNameArr.push(filename1);
	<%}%>
	<%for(int i=0;i<deductList.size();i++){%>
		var filenametemp2 = "filenamededuct"+<%=i%>;
		var filename2 = $("#"+filenametemp2).val();
		fileNameArr.push(filename2);
	<%}%>
	
	var urldata = $("#saveform").attr("action");
	var dataJson = {};
	dataJson["fileNameArr"] = fileNameArr;
	dataJson["ischeckedArr"] = ischeckedArr;
	//通过ajax传入后台处理
	$.ajax({
		type:"POST",
		url:urldata,
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
<body >
<form action="<%=request.getContextPath() %>/salarySheet/updateischecked" method="post" id="saveform">
	<div style="width: 100%;" align="left">
		<input  type="button" id="save" name="save" value="保存"  onclick="saveForm();" align="left"/>
	</div>
	<div style="width: 100%;margin-right: 20%;" align="right" >
		<a href="#" onclick="getall();">全选</a>/<a href="#" onclick="cancelall();">反选</a>
	</div>	
	<%if(addList!=null&&deductList!=null){%>
	<div style="border: 1px;color: black;">
	<fieldset style="margin-left: 20%;margin-right:5%;margin-top: 0%;margin-bottom:2%;background-color: white;width: 60%;">
		<legend>增项</legend>
		<table style="width: 100%;margin-top: 0%" >
			<tr>
			<%for(int i=0;i<addList.size();i++){%>
				<td height="50px" align="right" style="width: 15%">
					<input id="ischeck<%=i %>" name="ischeck" type="checkbox" value="" <%if(addList.get(i).getIschecked()==1){ %> checked="checked" <%} %> />
				</td>
				<td height="50px" nowrap="nowrap" align="left" style="width: 15%"><%=addList.get(i).getFiletext()%>:
					<input id="filenameadd<%=i %>" name="filename" type="hidden" value="<%=addList.get(i).getFilename() %>" />
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
				<td height="50px" align="right" style="width: 15%">
					<input id="ischeck<%=i %>" name="ischeck" type="checkbox" value="" <%if(deductList.get(i).getIschecked()==1){ %> checked="checked" <%} %> />
				</td>
				<td height="50px" nowrap="nowrap" align="left" style="width: 15%"><%=deductList.get(i).getFiletext()%>:
					<input id="filenamededuct<%=i %>" name="filename" type="hidden" value="<%=deductList.get(i).getFilename() %>" />
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