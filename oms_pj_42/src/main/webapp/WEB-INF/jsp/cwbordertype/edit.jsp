
<%@page import="cn.explink.domain.ImportCwbOrderType"%>
<%@page import="cn.explink.domain.CwbOrderTypeBean"%>
<%@page import="cn.explink.domain.CwbOrderType"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
ImportCwbOrderType importCwbOrderType = (ImportCwbOrderType)request.getAttribute("importCwbOrderType");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
div{ float:left; padding:0px 5px; list-style:none; width: 650px; }
li{ float:left; padding:0px 5px; list-style:none; width: 200px; border:1px;border-color:blue;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" type="text/javascript">
</script>
<script type="text/javascript">
$(function() {
	$("#save").click(function(event){
		if($("#importtype").val().length>50){
			alert("您输入的信息过长！请输入50个汉字以内的信息！");
			return;
		}else{
			$("#saveForm").submit();
		}
	});
	$("#importtype").blur(function(event){
		importtype1();
	});
});

function importtype1(){
	
	if($("#importtype").val().length>0){
		$.ajax({
			url:"<%=request.getContextPath()%>/cwbordertype/importtypecheck",
				data : {importtype : $("#importtype").val()},
					success : function(data) {
						if (data == false) {
							alert("备注已存在");
						} else {
							alert("备注可用");
						}
				}
			});
		} 
}

</script>
</HEAD>

<BODY>
<div>
<%

%>

<form id="saveForm" name="Form1" action="../save/${importCwbOrderType.importid }" method="post" enctype="multipart/form-data">
		${errorState }
		${editsave }<br>
		类型:<select id="importtypeid" name="importtypeid" >
		<%if(importCwbOrderType.getImporttypeid()==1){ %>
        		<option value="1">配送</option>
        		<option value="2">上门退货</option>
        		<option value="3">上门换货</option>
        <%}else if(importCwbOrderType.getImporttypeid()==2){ %>
        		<option value="2">上门退货</option>
        		<option value="1">配送</option>
        		<option value="3">上门换货</option>
          <%}else if(importCwbOrderType.getImporttypeid()==3){ %>
         	    <option value="3">上门换货</option>
         	    <option value="1">配送</option>
        		<option value="2">上门退货</option>
        <%} %>		
        </select><br>	
		 备注:<input type="text" name="importtype" id="importtype" value="${importCwbOrderType.importtype }"><br>
	    <input type="button" id="save" value="修改"/>
	    <a href="../list/1">返回</a>
	</form>
  

</div>	
</BODY>
</HTML>
